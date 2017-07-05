package com.mmxlabs.models.lng.transformer.longterm;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.impl.DisposeOnRemoveEclipseListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.models.util.StringEscaper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.util.ModelReferenceThread;

public class LongTermOptimiserMenuExtension implements ITradesTableContextMenuExtension {

	private static final Logger log = LoggerFactory.getLogger(LongTermOptimiserMenuExtension.class);

	public LongTermOptimiserMenuExtension() {
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Slot slot, @NonNull final MenuManager menuManager) {
		if (!LicenseFeatures.isPermitted("features:long-term-optimiser")) {
			return;
		}

		if (slot.getCargo() == null) {
			final LongTermAction action = new LongTermAction(scenarioEditingLocation);
			menuManager.add(action);
			return;
		}
	}

	private static class LongTermAction extends Action {

		private final IScenarioEditingLocation scenarioEditingLocation;

		public LongTermAction(final IScenarioEditingLocation scenarioEditingLocation) {
			super("Long term optimisation");
			this.scenarioEditingLocation = scenarioEditingLocation;
		}

		@Override
		public void run() {
			final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

			final ScenarioInstance instance = scenarioEditingLocation.getScenarioInstance();
			final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);

			// While we only keep the reference for the duration of this method call, the two current concrete implementations of IJobControl will obtain a ModelReference
			try (final ModelReference modelReference = modelRecord.aquireReference("LongTermOptimiser")) {
				final EObject object = modelReference.getInstance();

				if (object instanceof LNGScenarioModel) {
					final LNGScenarioModel root = (LNGScenarioModel) object;

					final String uuid = instance.getUuid();
					// Check for existing job and return if there is one.
					{
						final IJobDescriptor job = jobManager.findJobForResource(uuid);
						if (job != null) {
							return;
						}
					}
					@NonNull
					final UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();

					new ModelReferenceThread("Long term optimiser", modelRecord, (ref) -> {
						final ScenarioLock scenarioLock = ref.getLock();
						scenarioLock.lock();
						// Use a latch to trigger unlock in this thread
						final CountDownLatch latch = new CountDownLatch(1);

						IJobControl control = null;
						IJobDescriptor job = null;
						try {
							// create a new job
							job = new LNGLongTermJobDescriptor(instance.getName(), instance, userSettings);

							// New optimisation, so check there are no validation errors.
							if (!validateScenario(root, false)) {
								scenarioLock.unlock();
								return;
							}

							// Automatically clean up job when removed from manager
							jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));
							control = jobManager.submitJob(job, uuid);
							// Add listener to clean up job when it finishes or has an exception.
							final IJobDescriptor finalJob = job;
							control.addListener(new IJobControlListener() {

								@Override
								public boolean jobStateChanged(final IJobControl jobControl, final EJobState oldState, final EJobState newState) {

									if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {
										latch.countDown();
										jobManager.removeJob(finalJob);
										return false;
									}
									return true;
								}

								@Override
								public boolean jobProgressUpdated(final IJobControl jobControl, final int progressDelta) {
									return true;
								}
							});
							
							// Start the job!
							control.prepare();
							control.start();
							
							latch.await();
							scenarioLock.unlock();

						} catch (final Throwable ex) {
							log.error(ex.getMessage(), ex);
							if (control != null) {
								control.cancel();
							}
							// Manual clean up incase the control listener doesn't fire
							if (job != null) {
								jobManager.removeJob(job);
							}
							// instance.setLocked(false);
							scenarioLock.unlock();

							final Display display = Display.getDefault();
							if (display != null) {
								display.asyncExec(() -> {
									final String message = StringEscaper.escapeUIString(ex.getMessage());
									MessageDialog.openError(display.getActiveShell(), "Error long term", "An error occured. See Error Log for more details.\n" + message);
								});
							}
						}
					}) // 
					.start();
				}
			}  catch (final Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public static boolean validateScenario(final MMXRootObject root, final boolean optimising) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter(new IConstraintFilter() {

			@Override
			public boolean accept(final IConstraintDescriptor constraint, final EObject target) {

				for (final Category cat : constraint.getCategories()) {
					if (cat.getId().endsWith(".base")) {
						return true;
					} else if (optimising && cat.getId().endsWith(".optimisation")) {
						return true;
					} else if (!optimising && cat.getId().endsWith(".evaluation")) {
						return true;
					}
				}

				return false;
			}
		});

		final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
			final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(root, false);
			return helper.runValidation(validator, extraContext, Collections.singleton(root));
		});

		if (status == null) {
			return false;
		}

		if (status.isOK() == false) {

			// See if this command was executed in the UI thread - if so fire up the dialog box.
			if (Display.getCurrent() != null) {

				final ValidationStatusDialog dialog = new ValidationStatusDialog(Display.getCurrent().getActiveShell(), status, status.getSeverity() != IStatus.ERROR);

				// Wait for use to press a button before continuing.
				dialog.setBlockOnOpen(true);

				if (dialog.open() == Window.CANCEL) {
					return false;
				}
			}
		}

		if (status.getSeverity() == IStatus.ERROR) {
			return false;
		}

		return true;
	}

	@Override
	public void contributeToMenu(@NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull IStructuredSelection selection, @NonNull MenuManager menuManager) {
		// TODO Auto-generated method stub
		
	}

}
