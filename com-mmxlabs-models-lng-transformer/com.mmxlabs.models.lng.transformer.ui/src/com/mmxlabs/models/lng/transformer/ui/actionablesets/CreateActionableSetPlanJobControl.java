/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.actionablesets;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.analytics.ActionableSet;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;

public class CreateActionableSetPlanJobControl extends AbstractEclipseJobControl {

	// private static final int REPORT_PERCENTAGE = 1;
	// private int currentProgress = 0;

	private final CreateActionableSetPlanJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final ModelReference modelReference;

	private final LNGScenarioModel originalScenario;

	private static final ImageDescriptor imgOpti = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	private final LNGScenarioRunner scenarioRunner;

	private final ExecutorService executorService;

	private final EditingDomain originalEditingDomain;

	private ActionableSetPlan actionableSetPlan = null;

	public CreateActionableSetPlanJobControl(final CreateActionableSetPlanJobDescriptor jobDescriptor) {
		super(jobDescriptor.getJobName());

		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		this.modelReference = scenarioInstance.getReference("LNGActionPlanJobControl");
		this.originalScenario = (LNGScenarioModel) modelReference.getInstance();
		originalEditingDomain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);

		// TODO: This should be static / central service?
		executorService = LNGScenarioChainBuilder.createExecutorService();// Executors.newSingleThreadExecutor();

		final UserSettings userSettings = jobDescriptor.getUserSettings();

		final OptimisationPlan plan = ParametersFactory.eINSTANCE.createOptimisationPlan();
		plan.setUserSettings(EcoreUtil.copy(userSettings));
		plan.setSolutionBuilderSettings(ScenarioUtils.createDefaultSolutionBuilderSettings());

		scenarioRunner = new LNGScenarioRunner(executorService, originalScenario, scenarioInstance, plan, originalEditingDomain, null, false, //
				LNGTransformerHelper.HINT_OPTIMISE_LSO);

		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));
	}

	@Override
	protected void reallyPrepare() {
		scenarioRunner.evaluateInitialState();
	}

	@Override
	protected void doRunJob(final IProgressMonitor progressMonitor) {
		final long start = System.currentTimeMillis();
		progressMonitor.beginTask("Optimise", 100);
		try {

			@NonNull
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
			// TODO: Filter
			// final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
			// while (iterator.hasNext()) {
			// final Constraint constraint = iterator.next();
			// if (constraint.getName().equals(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
			// iterator.remove();
			// }
			// if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
			// iterator.remove();
			// }
			//
			// }
			ScenarioUtils.createOrUpdateContraints(LadenLegLimitConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);

			final Schedule schedule = scenarioToOptimiserBridge.getScenario().getScheduleModel().getSchedule();
			ActionPlanOptimisationStage stageSettings = ParametersFactory.eINSTANCE.createActionPlanOptimisationStage();
			stageSettings.setName("actionplan");
			stageSettings.setConstraintAndFitnessSettings(constraintAndFitnessSettings);

			final ActionableSetsTransformerUnit actionPlanUnit = new ActionableSetsTransformerUnit(dataTransformer, "actionplan", dataTransformer.getUserSettings(), stageSettings,
					scenarioRunner.getExecutorService(), dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), dataTransformer.getHints());

			// final Function<Integer, String> nameFactory = changeSetIdx -> {
			// String newName;
			// if (changeSetIdx == 0) {
			// newName = "ActionPlan-base";
			// changeSetIdx++;
			// } else {
			// newName = String.format("ActionPlan-%s", (changeSetIdx++));
			// }
			// return newName;
			// };
			final IMultiStateResult results = actionPlanUnit.run(new SubProgressMonitor(progressMonitor, 90));
			{
				final IProgressMonitor monitor = new SubProgressMonitor(progressMonitor, 10);
				final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.getSolutions();
				monitor.beginTask("Export", solutions.size());
				System.out.printf("Found %d solutions\n", solutions.size() - 1);

				final CompoundCommand cmd = new CompoundCommand("Generate action plan");
				AnalyticsModel analyticsModel = originalScenario.getAnalyticsModel();
				if (analyticsModel == null) {
					analyticsModel = AnalyticsFactory.eINSTANCE.createAnalyticsModel();
					cmd.append(SetCommand.create(originalEditingDomain, originalScenario, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AnalyticsModel(), analyticsModel));
				}

				final ActionableSetPlan plan = AnalyticsFactory.eINSTANCE.createActionableSetPlan();

				if (solutions.size() > 1) {
					NonNullPair<ISequences, Map<String, Object>> last = solutions.get(solutions.size() - 1);
					// scenarioToOptimiserBridge.overwrite(100, last.getFirst(), last.getSecond());
				}
				try {
					for (final NonNullPair<ISequences, Map<String, Object>> changeSet : solutions) {
						// final String newName = nameFactory.apply(changeSetIdx++);

						@NonNull
						final Collection<@NonNull String> hints = new LinkedList<>(dataTransformer.getHints());
						try {

							final Schedule child_schedule = scenarioToOptimiserBridge.createSchedule(changeSet.getFirst(), changeSet.getSecond());

							final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
							scheduleModel.setSchedule(child_schedule);

							// New spot slots etc will need to be contained here.
							for (final SlotAllocation a : child_schedule.getSlotAllocations()) {
								final Slot slot = a.getSlot();
								if (slot != null && slot.eContainer() == null) {
									plan.getExtraSlots().add(slot);
								}
							}

							for (final CargoAllocation ca : child_schedule.getCargoAllocations()) {

								for (final SlotAllocation a : ca.getSlotAllocations()) {
									final Slot slot = a.getSlot();
									// if (slot instanceof SpotSlot) {
									// int ii = 0;
									// }
									//
									if (slot != null && slot.eContainer() == null) {
										plan.getExtraSlots().add(slot);
									}
								}
							}
							for (final OpenSlotAllocation ca : child_schedule.getOpenSlotAllocations()) {

								// for (final SlotAllocation a : ca.getSlotAllocations()) {
								final Slot slot = ca.getSlot();
								// if (slot instanceof SpotSlot) {
								// int ii = 0;
								// }
								//
								if (slot != null && slot.eContainer() == null) {
									// int ii = 0;
									assert false;// scheduleModel.getExtraSlots().contains(slot);
								}
								// }
							}

							final ActionableSet option = AnalyticsFactory.eINSTANCE.createActionableSet();
							option.setScheduleModel(scheduleModel);

							plan.getActionSets().add(option);

							// scenarioToOptimiserBridge.storeAsCopy(changeSet.getFirst(), newName, scenarioInstance, null);
						} catch (final Exception e) {
							throw new RuntimeException("Unable to store scenario: " + e.getMessage(), e);
						}

						monitor.worked(1);
					}

					cmd.append(AddCommand.create(originalEditingDomain, analyticsModel, AnalyticsPackage.Literals.ANALYTICS_MODEL__ACTIONABLE_SET_PLANS, plan));

					originalEditingDomain.getCommandStack().execute(cmd);

					actionableSetPlan = plan;

				} finally {
					monitor.done();
				}
			}
		} finally {
			progressMonitor.done();
			if (true) {
				System.out.println("done in:" + (System.currentTimeMillis() - start));
			}
		}
		// if (scenarioRunner.isFinished()) {
		// return false;
		// } else {
		// return true;
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.AbstractManagedJob#kill()
	 */
	@Override
	protected void kill() {
	}

	@Override
	public void dispose() {
		executorService.shutdownNow();

		if (modelReference != null) {
			modelReference.close();
		}

		// if (scenarioRunner != null) {
		// scenarioRunner.dispose();
		// }
		super.dispose();
	}

	@Override
	public final ActionableSetPlan getJobOutput() {
		return actionableSetPlan;
	}

	@Override
	public IJobDescriptor getJobDescriptor() {
		return jobDescriptor;
	}

	@Override
	public boolean isPauseable() {
		return true;
	}

}
