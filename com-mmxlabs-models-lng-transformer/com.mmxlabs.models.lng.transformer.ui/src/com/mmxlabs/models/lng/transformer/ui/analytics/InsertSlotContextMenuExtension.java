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
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.models.util.StringEscaper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class InsertSlotContextMenuExtension implements ITradesTableContextMenuExtension {

	public static final String ChangeSetViewCreatorService_Topic = "create-change-set-view";

	private static final Logger log = LoggerFactory.getLogger(InsertSlotContextMenuExtension.class);

	public InsertSlotContextMenuExtension() {
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Slot slot, @NonNull final MenuManager menuManager) {

		if (!LicenseFeatures.isPermitted("features:options-suggester")) {
			return;
		}

		if (slot.getCargo() == null) {
			final InsertSlotAction action = new InsertSlotAction(scenarioEditingLocation.getScenarioInstance(), Collections.singletonList(slot));

			if (slot.isLocked()) {
				action.setEnabled(false);
				action.setText(action.getText() + " (Kept open)");
			}

			menuManager.add(action);
			return;
		}
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection selection, @NonNull final MenuManager menuManager) {

		if (!LicenseFeatures.isPermitted("features:options-suggester")) {
			return;
		}
		final List<Slot> slots = new LinkedList<Slot>();
		final Iterator<?> itr = selection.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			if (obj instanceof RowData) {
				final RowData rowData = (RowData) obj;
				final LoadSlot load = rowData.getLoadSlot();
				if (load != null && load.getCargo() == null) {
					slots.add(load);
					break;
				}
				final DischargeSlot discharge = rowData.getDischargeSlot();
				if (discharge != null && discharge.getCargo() == null) {
					slots.add(discharge);
					break;
				}
			}
		}

		if (slots.size() > 0) {
			final InsertSlotAction action = new InsertSlotAction(scenarioEditingLocation.getScenarioInstance(), slots);

			for (Slot slot : slots) {
				if (slot.isLocked()) {
					action.setEnabled(false);
					action.setText(action.getText() + " (Kept open)");
					break;
				}
			}
			menuManager.add(action);

			return;
		}
	}

	private static class InsertSlotAction extends Action {

		private final List<Slot> originalTargetSlots;
		private final ScenarioInstance original;

		public InsertSlotAction(final ScenarioInstance scenarioInstance, final List<Slot> targetSlots) {
			super(generateActionName(targetSlots) + " (Beta)");
			this.original = scenarioInstance;
			this.originalTargetSlots = targetSlots;
		}

		@Override
		public void run() {
			final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

			ScenarioInstance duplicate;
			try {
				duplicate = original.getScenarioService().duplicate(original, original);

				duplicate.setName(generateActionName(originalTargetSlots));

				// While we only keep the reference for the duration of this method call, the two current concrete implementations of IJobControl will obtain a ModelReference
				try (final ModelReference modelRefence = duplicate.getReference("InsertSlotContextMenuExtension")) {
					final EObject object = modelRefence.getInstance();

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

						UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();
						userSettings = OptimisationHelper.promptForInsertionUserSettings(root, false, true, false);

						// Reset settings not supplied to the user
						userSettings.setShippingOnly(false);
						userSettings.setBuildActionSets(false);
						userSettings.setCleanStateOptimisation(false);
						userSettings.setSimilarityMode(SimilarityMode.OFF);

						// Period is not valid yet
						// userSettings.unsetPeriodStart();
						// userSettings.unsetPeriodEnd();

						final ScenarioLock scenarioLock = duplicate.getLock(ScenarioLock.OPTIMISER);
						if (scenarioLock.awaitClaim()) {
							IJobControl control = null;
							IJobDescriptor job = null;
							try {

								// Map between original and fork
								List<Slot> targetSlots = new LinkedList<Slot>();
								CargoModelFinder finder = new CargoModelFinder(root.getCargoModel());
								for (Slot original : originalTargetSlots) {
									if (original instanceof LoadSlot) {
										targetSlots.add(finder.findLoadSlot(original.getName()));
									} else if (original instanceof DischargeSlot) {
										targetSlots.add(finder.findDischargeSlot(original.getName()));
									}
								}

								// create a new job
								job = new LNGSlotInsertionJobDescriptor(generateName(targetSlots), duplicate, userSettings, targetSlots);

								// New optimisation, so check there are no validation errors.
								if (!validateScenario(root, false)) {
									scenarioLock.release();
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

											scenarioLock.release();
											try {
												duplicate.save();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}

											jobManager.removeJob(finalJob);

											if (newState == EJobState.COMPLETED) {
												SlotInsertionOptions plan = (SlotInsertionOptions) jobControl.getJobOutput();
												if (plan != null) {
													// Forces editor lock to disallow users from editing the scenario.
													// TODO: This is not a very clean way to do it!
//													final ScenarioLock lock = duplicate.getLock(ScenarioLock.EDITORS);
//													lock.claim();
													duplicate.setReadonly(true);

													final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
													AnalyticsSolution data = new AnalyticsSolution(duplicate, plan, generateName(plan));
													data.setCreateInsertionOptions(true);
													eventBroker.post(ChangeSetViewCreatorService_Topic, data);
												}
											}

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
								scenarioLock.release();

								final Display display = Display.getDefault();
								if (display != null) {
									display.asyncExec(() -> {
										final String message = StringEscaper.escapeUIString(ex.getMessage());
										MessageDialog.openError(display.getActiveShell(), "Error inserting slot", "An error occured. See Error Log for more details.\n" + message);
									});
								}
							}
						}
					}
				}

				// final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
				// final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
				// try {
				// editorLock.claim();
				// scenarioEditingLocation.setDisableUpdates(true);
				// dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), Collections.<EObject> singletonList(redirectionSlotHistory), scenarioEditingLocation.isLocked());
				// } finally {
				// scenarioEditingLocation.setDisableUpdates(false);
				// editorLock.release();
				// }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	private static String generateName(SlotInsertionOptions plan) {

		List<String> names = new LinkedList<String>();
		for (Slot s : plan.getSlotsInserted()) {
			names.add(s.getName());
		}

		return "Inserting: " + Joiner.on(", ").join(names);
	}

	private static String generateName(Collection<Slot> slots) {

		List<String> names = new LinkedList<String>();
		for (Slot s : slots) {
			names.add(s.getName());
		}

		return "Inserting: " + Joiner.on(", ").join(names);
	}

	private static String generateActionName(Collection<Slot> slots) {

		List<String> names = new LinkedList<String>();
		for (Slot s : slots) {
			names.add(s.getName());
		}

		return "Insert " + Joiner.on(", ").join(names);
	}
}
