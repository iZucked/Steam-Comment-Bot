/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.services.events.IEventBroker;
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
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.impl.DisposeOnRemoveEclipseListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.ui.editorpart.events.IVesselEventsTableContextMenuExtension;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.models.util.StringEscaper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ScenarioServiceCommandUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.util.ModelReferenceThread;

public class InsertEventContextMenuExtension implements IVesselEventsTableContextMenuExtension {

	public static final String ChangeSetViewCreatorService_Topic = "create-change-set-view";

	private static final Logger log = LoggerFactory.getLogger(InsertEventContextMenuExtension.class);

	public InsertEventContextMenuExtension() {
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final VesselEvent vesselEvent, @NonNull final MenuManager menuManager) {

		if (!LicenseFeatures.isPermitted("features:options-suggester")) {
			return;
		}

		if (!LicenseFeatures.isPermitted("features:options-suggester-events")) {
			return;
		}
		if (vesselEvent instanceof CharterOutEvent) {

			final InsertEventAction action = new InsertEventAction(scenarioEditingLocation, Collections.singletonList(vesselEvent));

			if (vesselEvent.isLocked()) {
				action.setEnabled(false);
				action.setText(action.getText() + " (Locked)");
			}

			menuManager.add(action);
		}
		return;
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection selection, @NonNull final MenuManager menuManager) {

		if (!LicenseFeatures.isPermitted("features:options-suggester")) {
			return;
		}
		if (!LicenseFeatures.isPermitted("features:options-suggester-events")) {
			return;
		}
		{
			final List<VesselEvent> events = new LinkedList<>();
			final Iterator<?> itr = selection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof CharterOutEvent) {
					events.add((CharterOutEvent) obj);
					break;

				}
			}

			if (events.size() > 0) {
				final InsertEventAction action = new InsertEventAction(scenarioEditingLocation, events);

				for (final VesselEvent event : events) {
					if (event.isLocked()) {
						action.setEnabled(false);
						action.setText(action.getText() + " (Locked)");
						break;
					}
				}
				menuManager.add(action);

				return;
			}
		}
	}

	private static class InsertEventAction extends Action {

		private final List<VesselEvent> originalTargetEvents;
		private final IScenarioEditingLocation scenarioEditingLocation;

		public InsertEventAction(final IScenarioEditingLocation scenarioEditingLocation, final List<VesselEvent> targetVesselEvents) {
			super(generateActionName(targetVesselEvents) + " (Beta)");
			this.scenarioEditingLocation = scenarioEditingLocation;
			this.originalTargetEvents = targetVesselEvents;
		}

		@Override
		public void run() {
			final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

			final ScenarioInstance original = scenarioEditingLocation.getScenarioInstance();
			UserSettings userSettings = null;
			{

				final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(original);
				try (final ModelReference modelReference = modelRecord.aquireReference("InsertEventContextMenuExtension:1")) {

					final EObject object = modelReference.getInstance();

					if (object instanceof LNGScenarioModel) {
						final LNGScenarioModel root = (LNGScenarioModel) object;

						userSettings = OptimisationHelper.promptForInsertionUserSettings(root, false, true, false);
					}
				}
			}
			if (userSettings == null) {
				return;
			}
			// Reset settings not supplied to the user
			userSettings.setShippingOnly(false);
			userSettings.setBuildActionSets(false);
			userSettings.setCleanStateOptimisation(false);
			userSettings.setSimilarityMode(SimilarityMode.OFF);
			final UserSettings pUserSettings = userSettings;

			ScenarioInstance duplicate;
			try {
				final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(original);
				duplicate = ScenarioServiceCommandUtil.copyTo(original, original, generateActionName(originalTargetEvents));

				// While we only keep the reference for the duration of this method call, the two current concrete implementations of IJobControl will obtain a ModelReference
				final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(duplicate);
				try (final ModelReference modelReference = modelRecord.aquireReference("InsertEventContextMenuExtension:2")) {

					final EObject object = modelReference.getInstance();

					if (object instanceof LNGScenarioModel) {
						final LNGScenarioModel root = (LNGScenarioModel) object;

						final String uuid = duplicate.getUuid();
						// Check for existing job and return if there is one.
						{
							final IJobDescriptor job = jobManager.findJobForResource(uuid);
							if (job != null) {
								return;
							}
						}

						// Map between original and fork
						final List<VesselEvent> targetEvents = new LinkedList<>();
						final CargoModelFinder finder = new CargoModelFinder(root.getCargoModel());
						for (final VesselEvent originalEvent : originalTargetEvents) {
							targetEvents.add(finder.findVesselEvent(originalEvent.getName()));
						}

						new ModelReferenceThread("Insert " + generateActionName(originalTargetEvents), modelRecord, (ref) -> {
							final ScenarioLock scenarioLock = ref.getLock();
							scenarioLock.lock();
							// Use a latch to trigger unlock in this thread
							final CountDownLatch latch = new CountDownLatch(1);
							IJobControl control = null;
							IJobDescriptor job = null;
							try {

								// create a new job
								job = new LNGSlotInsertionJobDescriptor(generateName(targetEvents), duplicate, pUserSettings, Collections.emptyList(), targetEvents);

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

											if (newState == EJobState.COMPLETED) {
												try {
													ref.save();
												} catch (final IOException e) {
													e.printStackTrace();
												}

												final SlotInsertionOptions plan = (SlotInsertionOptions) jobControl.getJobOutput();
												if (plan != null) {

													duplicate.setReadonly(true);

													final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
													final AnalyticsSolution data = new AnalyticsSolution(duplicate, plan, generateName(plan));
													data.setCreateInsertionOptions(true);
													eventBroker.post(ChangeSetViewCreatorService_Topic, data);
												}
											}
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
										MessageDialog.openError(display.getActiveShell(), "Error inserting slot", "An error occured. See Error Log for more details.\n" + message);
									});
								}
							}
						}) //
								.start();
					}
				}
			} catch (final Exception e) {
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

	private static String generateName(final SlotInsertionOptions plan) {

		final List<String> names = new LinkedList<String>();
		for (final VesselEvent s : plan.getEventsInserted()) {
			names.add(s.getName());
		}

		return "Inserting: " + Joiner.on(", ").join(names);
	}

	private static String generateName(final Collection<VesselEvent> events) {

		final List<String> names = new LinkedList<String>();
		for (final VesselEvent s : events) {
			names.add(s.getName());
		}

		return "Inserting: " + Joiner.on(", ").join(names);
	}

	private static String generateActionName(final Collection<VesselEvent> events) {

		final List<String> names = new LinkedList<String>();
		for (final VesselEvent s : events) {
			names.add(s.getName());
		}

		return "Insert " + Joiner.on(", ").join(names);
	}
}
