/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SlotInsertionOption;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.stochasticactionsets.BreakEvenTransformerUnit;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGSchedulerInsertSlotJobControl extends AbstractEclipseJobControl {

	private static final Logger LOGGER = LoggerFactory.getLogger(LNGSchedulerInsertSlotJobControl.class);

	private final LNGSlotInsertionJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final ModelReference modelReference;

	private final LNGScenarioModel originalScenario;

	private final LNGScenarioRunner scenarioRunner;

	private final ExecutorService executorService;

	private final List<Slot> targetSlots;
	private final List<Slot> targetOptimiserSlots;
	private final List<VesselEvent> targetEvents;
	private final List<VesselEvent> targetOptimiserEvents;

	private final EditingDomain originalEditingDomain;

	private SlotInsertionOptions slotInsertionPlan;

	private static final String[] hint_with_breakeven = { LNGTransformerHelper.HINT_OPTIMISE_LSO, //
			LNGTransformerHelper.HINT_DISABLE_CACHES, //
			LNGTransformerHelper.HINT_KEEP_NOMINALS_IN_PROMPT, //
			LNGEvaluationModule.HINT_PORTFOLIO_BREAKEVEN };

	private static final String[] hint_without_breakeven = { LNGTransformerHelper.HINT_OPTIMISE_LSO, //
			LNGTransformerHelper.HINT_KEEP_NOMINALS_IN_PROMPT };

	public LNGSchedulerInsertSlotJobControl(final LNGSlotInsertionJobDescriptor jobDescriptor) {
		super(jobDescriptor.getJobName());

		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		this.modelReference = modelRecord.aquireReference("LNGSchedulerInsertSlotJobControl");
		this.originalScenario = (LNGScenarioModel) modelReference.getInstance();
		originalEditingDomain = modelReference.getEditingDomain();

		/*
		 * Error checks
		 */
		{
			// Disable optimisation in P&L testing phase
			if (LicenseFeatures.isPermitted("features:phase-pnl-testing")) {
				throw new RuntimeException("Optimisation is disabled during the P&L testing phase.");
			}
			// if (!OptimisationHelper.isAllowedGCO(this.originalScenario)) {
			// throw new RuntimeException("Optimisation is disabled when missing prices are used");
			// }
		}

		// TODO: This should be static / central service?
		executorService = LNGScenarioChainBuilder.createExecutorService();

		final UserSettings userSettings = jobDescriptor.getUserSettings();
		targetSlots = jobDescriptor.getTargetSlots();
		targetEvents = jobDescriptor.getTargetEvents();

		final OptimisationPlan plan = ParametersFactory.eINSTANCE.createOptimisationPlan();
		plan.setUserSettings(EcoreUtil.copy(userSettings));
		plan.setSolutionBuilderSettings(ScenarioUtils.createDefaultSolutionBuilderSettings());

		IOptimiserInjectorService extraService = new IOptimiserInjectorService() {

			@Override
			public @Nullable Module requestModule(@NonNull ModuleType moduleType, @NonNull Collection<@NonNull String> hints) {
				return null;
			}

			@Override
			public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull ModuleType moduleType, @NonNull Collection<@NonNull String> hints) {

				if (moduleType == ModuleType.Module_LNGTransformerModule) {
					return Collections.singletonList(new AbstractModule() {

						@Override
						protected void configure() {
							// Only one new option per month
							bind(int.class).annotatedWith(Names.named(LNGScenarioTransformer.LIMIT_SPOT_SLOT_CREATION)).toInstance(1);
						}
					});
				}
				return null;
			}

		};

		boolean isBreakEven = false;
		for (Slot slot : targetSlots) {
			if (slot.isSetPriceExpression()) {
				if (slot.getPriceExpression().contains("?")) {
					isBreakEven = true;
					break;
				}
			}
		}

		String[] hints = isBreakEven ? hint_with_breakeven : hint_without_breakeven;

		// TODO: Only disable caches if we do a break-even (caches *should* be ok otherwise?)
		scenarioRunner = new LNGScenarioRunner(executorService, originalScenario, scenarioInstance, plan, originalEditingDomain, null, extraService, null, false, hints);

		if (userSettings.isSetPeriodStart() || userSettings.isSetPeriodEnd()) {
			// Map between original and possible period scenario
			targetOptimiserSlots = new LinkedList<>();
			targetOptimiserEvents = new LinkedList<>();
			CargoModelFinder finder = new CargoModelFinder(scenarioRunner.getScenarioToOptimiserBridge().getOptimiserScenario().getCargoModel());
			for (Slot original : targetSlots) {
				try {
					if (original instanceof LoadSlot) {
						targetOptimiserSlots.add(finder.findLoadSlot(original.getName()));
					} else if (original instanceof DischargeSlot) {
						targetOptimiserSlots.add(finder.findDischargeSlot(original.getName()));
					}
				} catch (IllegalArgumentException e) {
					// Slot not found - probably outside of period.
					throw new RuntimeException(String.format("Slot %s not included within period", original.getName()));
				}
			}
			for (VesselEvent original : targetEvents) {
				try {
					targetOptimiserEvents.add(finder.findVesselEvent(original.getName()));
				} catch (IllegalArgumentException e) {
					// Slot not found - probably outside of period.
					throw new RuntimeException(String.format("Vessel event %s not included within period", original.getName()));
				}
			}
		} else {
			targetOptimiserSlots = targetSlots;
			targetOptimiserEvents = targetEvents;
		}

		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));
	}

	@Override
	protected void reallyPrepare() {
		scenarioRunner.evaluateInitialState();
	}

	@Override
	protected void doRunJob(final IProgressMonitor progressMonitor) {
		final long start = System.currentTimeMillis();
		progressMonitor.beginTask("Inserting option(s)", 100);
		try {

			@NonNull
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
			// TODO: Filter
			final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
			while (iterator.hasNext()) {
				final Constraint constraint = iterator.next();
				if (constraint.getName().equals(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
					iterator.remove();
				}
				if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
					iterator.remove();
				}

			}
			ScenarioUtils.createOrUpdateContraints(LadenLegLimitConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);

			boolean performBreakEven = false;
			for (final Slot slot : targetOptimiserSlots) {
				if (slot.getPriceExpression() != null) {
					if (slot.getPriceExpression().contains("?")) {
						performBreakEven = true;
					}
				}
			}

			final Schedule schedule = scenarioToOptimiserBridge.getScenario().getScheduleModel().getSchedule();
			final long targetPNL = performBreakEven ? ScheduleModelKPIUtils.getScheduleProfitAndLoss(schedule) : 0L;

			final SlotInsertionOptimiserUnit slotInserter = new SlotInsertionOptimiserUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(), constraintAndFitnessSettings,
					scenarioRunner.getExecutorService(), dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), dataTransformer.getHints());

			final Function<Integer, String> nameFactory = changeSetIdx -> {
				String newName;
				if (changeSetIdx == 0) {
					newName = "InsertionPlan-base";
					changeSetIdx++;
				} else {
					newName = String.format("InsertionPlan-%s", (changeSetIdx++));
				}
				return newName;
			};
			final IMultiStateResult results = slotInserter.run(targetOptimiserSlots, targetOptimiserEvents, 50_000, new SubProgressMonitor(progressMonitor, 90));
			if (progressMonitor.isCanceled()) {
				return;
			}
			{
				final IProgressMonitor monitor = new SubProgressMonitor(progressMonitor, 10);
				final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.getSolutions();
				monitor.beginTask("Export", solutions.size());
				System.out.printf("Found %d solutions\n", solutions.size() - 1);

				final CompoundCommand cmd = new CompoundCommand("Generate insertion options");
				AnalyticsModel analyticsModel = originalScenario.getAnalyticsModel();
				if (analyticsModel == null) {
					analyticsModel = AnalyticsFactory.eINSTANCE.createAnalyticsModel();
					cmd.append(SetCommand.create(originalEditingDomain, originalScenario, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AnalyticsModel(), analyticsModel));
				}

				final SlotInsertionOptions plan = AnalyticsFactory.eINSTANCE.createSlotInsertionOptions();
				// Make sure this is the original, not the optimiser
				plan.getSlotsInserted().addAll(targetSlots);
				plan.getEventsInserted().addAll(targetEvents);

				try {
					int changeSetIdx = 0;
					for (final NonNullPair<ISequences, Map<String, Object>> changeSet : solutions) {
						final String newName = nameFactory.apply(changeSetIdx++);

						@NonNull
						final Collection<@NonNull String> hints = new LinkedList<>(dataTransformer.getHints());
						// TODO: Only disable caches if we do a break-even (caches *should* be ok otherwise?)
						if (performBreakEven) {
							hints.add(LNGTransformerHelper.HINT_DISABLE_CACHES);
						}
						try {
							if (performBreakEven) {
								final BreakEvenOptimisationStage stageSettings = ParametersFactory.eINSTANCE.createBreakEvenOptimisationStage();
								stageSettings.setName("be-" + newName);
								stageSettings.setTargetProfitAndLoss(targetPNL);

								final BreakEvenTransformerUnit t = new BreakEvenTransformerUnit(dataTransformer, dataTransformer.getUserSettings(), stageSettings, changeSet.getFirst(),
										new MultiStateResult(changeSet.getFirst(), new HashMap<>()), hints);

								t.run(new NullProgressMonitor());
							}
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

							final SlotInsertionOption option = AnalyticsFactory.eINSTANCE.createSlotInsertionOption();
							option.setScheduleModel(scheduleModel);

							plan.getInsertionOptions().add(option);

							// scenarioToOptimiserBridge.storeAsCopy(changeSet.getFirst(), newName, scenarioInstance, null);
						} catch (final Exception e) {
							throw new RuntimeException("Unable to store scenario: " + e.getMessage(), e);
						}

						monitor.worked(1);
					}

					cmd.append(AddCommand.create(originalEditingDomain, analyticsModel, AnalyticsPackage.Literals.ANALYTICS_MODEL__INSERTION_OPTIONS, plan));
					// We are going to make scenario read-only, make sure it is not marked as dirty
					cmd.append(SetCommand.create(originalEditingDomain, originalScenario.getScheduleModel(), SchedulePackage.Literals.SCHEDULE_MODEL__DIRTY, Boolean.FALSE));

					RunnerHelper.syncExecDisplayOptional(() -> {
						try {
							if (originalEditingDomain instanceof CommandProviderAwareEditingDomain) {
								((CommandProviderAwareEditingDomain) originalEditingDomain).setCommandProvidersDisabled(true);
							}
							if (cmd.canExecute()) {
								originalEditingDomain.getCommandStack().execute(cmd);
							} else {
								throw new RuntimeException("Unable to save insertion result");
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage(), t);
						} finally {
							if (originalEditingDomain instanceof CommandProviderAwareEditingDomain) {
								((CommandProviderAwareEditingDomain) originalEditingDomain).setCommandProvidersDisabled(false);
							}
						}
					});
					slotInsertionPlan = plan;

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
	}

	@Override
	protected void kill() {
	}

	@Override
	public void dispose() {
		executorService.shutdownNow();

		if (modelReference != null) {
			modelReference.close();
		}

		super.dispose();
	}

	@Override
	public final SlotInsertionOptions getJobOutput() {
		return slotInsertionPlan;
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
