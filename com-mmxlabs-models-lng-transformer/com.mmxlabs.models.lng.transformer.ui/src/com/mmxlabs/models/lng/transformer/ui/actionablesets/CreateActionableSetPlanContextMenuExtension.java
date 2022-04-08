/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.actionablesets;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.LocalJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.actionableplan.ActionablePlanTask;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class CreateActionableSetPlanContextMenuExtension implements ITradesTableContextMenuExtension {

	private static final Logger log = LoggerFactory.getLogger(CreateActionableSetPlanContextMenuExtension.class);

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Slot slot, @NonNull final MenuManager menuManager) {

		final MyAction action = new MyAction(scenarioEditingLocation);
		menuManager.add(action);
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection selection, @NonNull final MenuManager menuManager) {

		final MyAction action = new MyAction(scenarioEditingLocation);
		menuManager.add(action);
	}

	private static class MyAction extends Action {
		//
		private final IScenarioEditingLocation scenarioEditingLocation;

		public MyAction(final IScenarioEditingLocation scenarioEditingLocation) {
			super("Generate actionable plan");
			this.scenarioEditingLocation = scenarioEditingLocation;
		}

		@Override
		public void run() {
			ActionablePlanTask.submit(scenarioEditingLocation.getScenarioInstance(), LocalJobManager.INSTANCE);

		}

	}
	// final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
	//
	// final ScenarioInstance instance = scenarioEditingLocation.getScenarioInstance();
	// // While we only keep the reference for the duration of this method call, the two current concrete implementations of IJobControl will obtain a ModelReference
	// @NonNull
	// final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecordChecked(instance);
	// try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ActionPlanContextMenuExtension")) {
	// final EObject object = scenarioDataProvider.getScenario();
	//
	// if (object instanceof LNGScenarioModel) {
	//
	// final LNGScenarioModel root = (LNGScenarioModel) object;
	//
	// final String uuid = instance.getUuid();
	// // Check for existing job and return if there is one.
	// {
	// final IJobDescriptor job = jobManager.findJobForResource(uuid);
	// if (job != null) {
	// return;
	// }
	// }
	// final Set<String> existingNames = new HashSet<>();
	// instance.getFragments().forEach(f -> existingNames.add(f.getName()));
	// instance.getElements().forEach(f -> existingNames.add(f.getName()));
	// final UserSettings userSettings = UserSettingsHelper.promptForUserSettings(root, false, true, false, new NameProvider("Action set", existingNames), null);
	// new Thread("CreateActionableSetThread") {
	//
	// @Override
	// public void run() {
	// final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("ActionPlanContextMenuExtension:2");
	// final ScenarioLock scenarioLock = sdp.getModelReference().getLock();
	//
	// scenarioLock.lock();
	// // Use a latch to trigger unlock in this thread
	// final CountDownLatch latch = new CountDownLatch(1);
	//
	// IJobControl control = null;
	// IJobDescriptor job = null;
	// try {
	// // create a new job
	// job = new CreateActionableSetPlanJobDescriptor(instance.getName(), instance, userSettings);
	//
	// // New optimisation, so check there are no validation errors.
	// if (!validateScenario(sdp, false)) {
	// scenarioLock.unlock();
	// return;
	// }
	//
	// // Automatically clean up job when removed from manager
	// jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));
	// control = jobManager.submitJob(job, uuid);
	// // Add listener to clean up job when it finishes or has an exception.
	// final IJobDescriptor finalJob = job;
	// control.addListener(new IJobControlListener() {
	//
	// @Override
	// public boolean jobStateChanged(final IJobControl jobControl, final EJobState oldState, final EJobState newState) {
	//
	// if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {
	// latch.countDown();
	// jobManager.removeJob(finalJob);
	//
	// if (newState == EJobState.COMPLETED) {
	// final ActionableSetPlan plan = (ActionableSetPlan) jobControl.getJobOutput();
	// if (plan != null) {
	// final AnalyticsSolution data = new AnalyticsSolution(instance, plan, "My Action Set");
	// data.openAndSwitchScreen();
	// }
	// }
	//
	// return false;
	// }
	// return true;
	// }
	//
	// @Override
	// public boolean jobProgressUpdated(final IJobControl jobControl, final int progressDelta) {
	// return true;
	// }
	// });
	// // Start the job!
	// control.prepare();
	// control.start();
	//
	// latch.await();
	// scenarioLock.unlock();
	//
	// } catch (final Throwable ex) {
	// log.error(ex.getMessage(), ex);
	// if (control != null) {
	// control.cancel();
	// }
	// // Manual clean up incase the control listener doesn't fire
	// if (job != null) {
	// jobManager.removeJob(job);
	// }
	// // instance.setLocked(false);
	// scenarioLock.unlock();
	//
	// final Display display = Display.getDefault();
	// if (display != null) {
	// display.asyncExec(() -> {
	// final String message = StringEscaper.escapeUIString(ex.getMessage());
	// MessageDialog.openError(display.getActiveShell(), "Error generating action plan", "An error occured. See Error Log for more details.\n" + message);
	// });
	// }
	// }
	// }
	// }.start();
	// }
	// }
	// }
	// }
	//
	// public static boolean validateScenario(final IScenarioDataProvider scenarioDataProvider, final boolean optimising) {
	// final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
	// validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);
	//
	// validator.addConstraintFilter(new IConstraintFilter() {
	//
	// @Override
	// public boolean accept(final IConstraintDescriptor constraint, final EObject target) {
	//
	// for (final Category cat : constraint.getCategories()) {
	// if (cat.getId().endsWith(".base")) {
	// return true;
	// } else if (optimising && cat.getId().endsWith(".optimisation")) {
	// return true;
	// } else if (!optimising && cat.getId().endsWith(".evaluation")) {
	// return true;
	// }
	// }
	//
	// return false;
	// }
	// });
	// final MMXRootObject rootObject = scenarioDataProvider.getTypedScenario(MMXRootObject.class);
	// final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
	// final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(scenarioDataProvider, false, false);
	// return helper.runValidation(validator, extraContext, rootObject, null);
	// });
	//
	// if (status == null) {
	// return false;
	// }
	//
	// if (status.isOK() == false) {
	//
	// // See if this command was executed in the UI thread - if so fire up the dialog box.
	// if (Display.getCurrent() != null) {
	//
	// final ValidationStatusDialog dialog = new ValidationStatusDialog(Display.getCurrent().getActiveShell(), status, status.getSeverity() != IStatus.ERROR);
	//
	// // Wait for use to press a button before continuing.
	// dialog.setBlockOnOpen(true);
	//
	// if (dialog.open() == Window.CANCEL) {
	// return false;
	// }
	// }
	// }
	//
	// if (status.getSeverity() == IStatus.ERROR) {
	// return false;
	// }
	//
	// return true;
	// }
	//
}
