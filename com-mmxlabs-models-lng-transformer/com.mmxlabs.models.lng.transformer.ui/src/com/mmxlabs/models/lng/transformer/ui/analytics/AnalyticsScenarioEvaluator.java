/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
	 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.DualModeSolutionOption;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SandboxResult;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseToScheduleSpecification;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.ExistingBaseCaseToScheduleSpecification;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.Mapper;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.ExportScheduleHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.OptimisationJobRunner;
import com.mmxlabs.models.lng.transformer.ui.analytics.mtm.MTMSanboxUnit;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ScheduleSpecificationHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.viability.ViabilitySanboxUnit;
import com.mmxlabs.models.lng.transformer.ui.analytics.viability.ViabilityWindowTrimmer;
import com.mmxlabs.models.lng.transformer.ui.common.SolutionSetExporterUnit;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.common.constraints.LockedUnusedElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResult;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl.DefaultBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduling.ICustomTimeWindowTrimmer;

public class AnalyticsScenarioEvaluator implements IAnalyticsScenarioEvaluator {

	@Override
	public ScenarioInstance exportResult(final ScenarioResult result, final String name) {

		try {
			return ExportScheduleHelper.export(result, name, true, createModelCustomiser());
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BiConsumer<LNGScenarioModel, Schedule> createModelCustomiser() {
		return (input_scenario, input_schedule) -> {

			final Set<VesselAvailability> usedVesselAvailabilites = new LinkedHashSet<>();
			final Set<CharterInMarketOverride> usedCharterInMarketOverrides = new LinkedHashSet<>();
			final Set<CharterInMarket> usedCharterInMarkets = new LinkedHashSet<>();
			final Set<LoadSlot> usedLoadSlots = new LinkedHashSet<>();
			final Set<DischargeSlot> usedDischargeSlots = new LinkedHashSet<>();
			final Set<VesselEvent> usedVesselEvents = new LinkedHashSet<>();
			final Set<Cargo> usedCargoes = new LinkedHashSet<>();

			for (final SlotAllocation slotAllocation : input_schedule.getSlotAllocations()) {
				final Slot<?> slot = slotAllocation.getSlot();
				if (slot instanceof LoadSlot) {
					usedLoadSlots.add((LoadSlot) slot);

					if (slot.getCargo() != null && slotAllocation.getCargoAllocation() != null) {
						if (slotAllocation.getCargoAllocation().getSlotAllocations().get(0) == slotAllocation) {
							usedCargoes.add(slot.getCargo());
						}
					}
				} else if (slot instanceof DischargeSlot) {
					usedDischargeSlots.add((DischargeSlot) slot);
				} else {
					assert false;
				}
			}
			for (final OpenSlotAllocation openSlotAllocation : input_schedule.getOpenSlotAllocations()) {
				final Slot<?> slot = openSlotAllocation.getSlot();
				if (slot instanceof LoadSlot) {
					usedLoadSlots.add((LoadSlot) slot);
				} else if (slot instanceof DischargeSlot) {
					usedDischargeSlots.add((DischargeSlot) slot);
				} else {
					assert false;
				}
			}
			for (final Sequence sequence : input_schedule.getSequences()) {
				if (sequence.getVesselAvailability() != null) {
					usedVesselAvailabilites.add(sequence.getVesselAvailability());
				}
				if (sequence.getCharterInMarketOverride() != null) {
					usedCharterInMarketOverrides.add(sequence.getCharterInMarketOverride());
				} else if (sequence.getCharterInMarket() != null) {
					usedCharterInMarkets.add(sequence.getCharterInMarket());
				}
				for (final Event event : sequence.getEvents()) {
					if (event instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
						usedVesselEvents.add(vesselEventVisit.getVesselEvent());
					}
				}
			}

			final Set<EObject> objectsToDelete = new LinkedHashSet<>();
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(input_scenario);

			{
				final Iterator<LoadSlot> l_itr = cargoModel.getLoadSlots().iterator();
				while (l_itr.hasNext()) {
					final LoadSlot loadSlot = l_itr.next();
					if (!usedLoadSlots.contains(loadSlot)) {
						loadSlot.setCargo(null);
						objectsToDelete.add(loadSlot);
						l_itr.remove();
					}
				}
				for (final LoadSlot slot : usedLoadSlots) {
					if (!cargoModel.getLoadSlots().contains(slot)) {
						cargoModel.getLoadSlots().add(slot);
					}
				}
			}
			{
				final Iterator<DischargeSlot> l_itr = cargoModel.getDischargeSlots().iterator();
				while (l_itr.hasNext()) {
					final DischargeSlot dischargeSlot = l_itr.next();
					if (!usedDischargeSlots.contains(dischargeSlot)) {
						dischargeSlot.setCargo(null);
						objectsToDelete.add(dischargeSlot);
						l_itr.remove();
					}
				}
				for (final DischargeSlot slot : usedDischargeSlots) {
					if (!cargoModel.getDischargeSlots().contains(slot)) {
						cargoModel.getDischargeSlots().add(slot);
					}
				}
			}
			{
				final Iterator<Cargo> l_itr = cargoModel.getCargoes().iterator();
				while (l_itr.hasNext()) {
					final Cargo cargo = l_itr.next();
					if (cargo.getSlots().size() < 2 || !usedCargoes.contains(cargo)) {
						cargo.getSlots().clear();
						objectsToDelete.add(cargo);

						l_itr.remove();
					}
				}
				for (final Cargo cargo : usedCargoes) {
					if (!cargoModel.getCargoes().contains(cargo)) {
						cargoModel.getCargoes().add(cargo);
					}
				}
			}
			{
				final Iterator<VesselEvent> l_itr = cargoModel.getVesselEvents().iterator();
				while (l_itr.hasNext()) {
					final VesselEvent vesselEvent = l_itr.next();
					if (!usedVesselEvents.contains(vesselEvent)) {
						objectsToDelete.add(vesselEvent);

						l_itr.remove();
					}
				}
				for (final VesselEvent vesselEvent : usedVesselEvents) {
					if (!cargoModel.getVesselEvents().contains(vesselEvent)) {
						cargoModel.getVesselEvents().add(vesselEvent);
					}
				}
			}
			{
				final Iterator<VesselAvailability> l_itr = cargoModel.getVesselAvailabilities().iterator();
				while (l_itr.hasNext()) {
					final VesselAvailability vesselAvailability = l_itr.next();
					if (!usedVesselAvailabilites.contains(vesselAvailability)) {
						objectsToDelete.add(vesselAvailability);
						l_itr.remove();
					}
				}
				for (final VesselAvailability vesselAvailability : usedVesselAvailabilites) {
					if (!cargoModel.getVesselAvailabilities().contains(vesselAvailability)) {
						cargoModel.getVesselAvailabilities().add(vesselAvailability);
					}
				}
			}

			cargoModel.getCharterInMarketOverrides().clear();
			cargoModel.getCharterInMarketOverrides().addAll(usedCharterInMarketOverrides);

			final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(input_scenario);
			usedCharterInMarkets.removeAll(spotMarketsModel.getCharterInMarkets());
			spotMarketsModel.getCharterInMarkets().addAll(usedCharterInMarkets);

			input_scenario.getScheduleModel().setSchedule(null);

			final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(input_scenario);
			LNGSchedulerJobUtils.clearAnalyticsResults(analyticsModel);

			final Map<EObject, Collection<Setting>> crossReferences = EcoreUtil.UsageCrossReferencer.findAll(objectsToDelete, input_scenario);
			for (final Map.Entry<EObject, Collection<Setting>> ee : crossReferences.entrySet()) {
				final EObject object = ee.getKey();
				for (final Setting setting : ee.getValue()) {
					final EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
					final EObject container = setting.getEObject();
					if (eStructuralFeature.isMany()) {
						((Collection<?>) container.eGet(eStructuralFeature)).remove(object);
					} else {
						container.eUnset(eStructuralFeature);
					}
				}
			}
		};
	}

	@Override
	public void evaluateViabilitySandbox(@NonNull final IScenarioDataProvider scenarioDataProvider, @Nullable final ScenarioInstance scenarioInstance, @NonNull final UserSettings userSettings,
			final ViabilityModel model, final IMapperClass mapper, final Map<ShippingOption, VesselAssignmentType> shippingMap) {

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		final ScheduleSpecificationHelper helper = new ScheduleSpecificationHelper(scenarioDataProvider);
		helper.processExtraDataProvider(mapper.getExtraDataProvider());

		helper.withModuleService(OptimiserInjectorServiceMaker.begin()//
				.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, new AbstractModule() {

					@Override
					protected void configure() {

						bind(ViabilityWindowTrimmer.class).in(Singleton.class);
						bind(ICustomTimeWindowTrimmer.class).to(ViabilityWindowTrimmer.class);
					}

				})//
				.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_Evaluation, new AbstractModule() {

					@Override
					protected void configure() {

						bind(IBreakEvenEvaluator.class).to(DefaultBreakEvenEvaluator.class);
					}

				})//

				.make());

		final List<String> hints = new LinkedList<>();
		hints.add(LNGTransformerHelper.HINT_DISABLE_CACHES);
		final ConstraintAndFitnessSettings constraints = ScenarioUtils.createDefaultConstraintAndFitnessSettings(false);
		customiseConstraints(constraints);

		final ExecutorService executorService = LNGScenarioChainBuilder.createExecutorService();
		try {
			helper.generateWith(scenarioInstance, userSettings, scenarioDataProvider.getEditingDomain(), hints, (bridge) -> {
				final LNGDataTransformer dataTransformer = bridge.getDataTransformer();
				final ViabilitySanboxUnit unit = new ViabilitySanboxUnit(lngScenarioModel, dataTransformer, "viability-sandbox", userSettings, constraints, executorService,
						dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), dataTransformer.getHints());
				/* Command cmd = */
				unit.run(model, mapper, shippingMap, new NullProgressMonitor());
			});
		} finally {
			executorService.shutdownNow();
		}
	}

	@Override
	public void evaluateMTMSandbox(@NonNull final IScenarioDataProvider scenarioDataProvider, @Nullable final ScenarioInstance scenarioInstance, @NonNull final UserSettings userSettings,
			final MTMModel model, final IMapperClass mapper) {
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		final ScheduleSpecificationHelper helper = new ScheduleSpecificationHelper(scenarioDataProvider);
		helper.processExtraDataProvider(mapper.getExtraDataProvider());

		helper.withModuleService(OptimiserInjectorServiceMaker.begin()//
				.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_Evaluation, new AbstractModule() {

					@Override
					protected void configure() {

						bind(IBreakEvenEvaluator.class).to(DefaultBreakEvenEvaluator.class);
					}

				})//

				.make());

		final List<String> hints = new LinkedList<>();
		hints.add(LNGTransformerHelper.HINT_DISABLE_CACHES);
		final ConstraintAndFitnessSettings constraints = ScenarioUtils.createDefaultConstraintAndFitnessSettings(false);
		customiseConstraints(constraints);

		final ExecutorService executorService = LNGScenarioChainBuilder.createExecutorService();
		try {
			helper.generateWith(scenarioInstance, userSettings, scenarioDataProvider.getEditingDomain(), hints, (bridge) -> {
				final LNGDataTransformer dataTransformer = bridge.getDataTransformer();
				final MTMSanboxUnit unit = new MTMSanboxUnit(lngScenarioModel, dataTransformer, "mtm-sandbox", userSettings, constraints, executorService, dataTransformer.getInitialSequences(),
						dataTransformer.getInitialResult(), dataTransformer.getHints());
				/* Command cmd = */
				unit.run(model, mapper, new NullProgressMonitor());
			});
		} finally {
			executorService.shutdownNow();
		}

	}

	public void customiseConstraints(final ConstraintAndFitnessSettings constraintAndFitnessSettings) {
		final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
		while (iterator.hasNext()) {
			final Constraint constraint = iterator.next();
			if (constraint.getName().equals(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
			if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
			if (constraint.getName().equals(LockedUnusedElementsConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
			if (constraint.getName().equals(AllowedVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
		}
	}

	@Override
	public void evaluateBreakEvenSandbox(@NonNull final IScenarioDataProvider scenarioDataProvider, @Nullable final ScenarioInstance scenarioInstance, @NonNull final UserSettings userSettings,
			final BreakEvenAnalysisModel model, final IMapperClass mapper, final Map<ShippingOption, VesselAssignmentType> shippingMap, final CompoundCommand cmd) {

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		final ScheduleSpecificationHelper helper = new ScheduleSpecificationHelper(scenarioDataProvider);
		helper.processExtraDataProvider(mapper.getExtraDataProvider());

		final List<String> hints = new LinkedList<>();
		hints.add(LNGTransformerHelper.HINT_DISABLE_CACHES);
		final ConstraintAndFitnessSettings constraints = ScenarioUtils.createDefaultConstraintAndFitnessSettings(false);

		final ExecutorService executorService = Executors.newFixedThreadPool(LNGScenarioChainBuilder.getNumberOfAvailableCores());
		helper.generateWith(scenarioInstance, userSettings, scenarioDataProvider.getEditingDomain(), hints, (bridge) -> {
			final LNGDataTransformer dataTransformer = bridge.getDataTransformer();
			final BreakEvenSanboxUnit unit = new BreakEvenSanboxUnit(lngScenarioModel, dataTransformer, "break-even-sandbox", userSettings, constraints, executorService,
					dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), dataTransformer.getHints());
			/* Command cmd = */
			unit.run(model, mapper, shippingMap, new NullProgressMonitor());
		});
	}

	@Override
	public void runSandboxOptions(@NonNull final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance, final OptionAnalysisModel model,
			final Consumer<AbstractSolutionSet> action, boolean runAsync) {

		String taskName = "Sandbox result";

		final EObject object = scenarioDataProvider.getScenario();
		UserSettings userSettings = null;
		if (object instanceof LNGScenarioModel) {
			final LNGScenarioModel root = (LNGScenarioModel) object;

			UserSettings previousSettings = null;
			if (model.getResults() != null) {
				previousSettings = model.getResults().getUserSettings();
			}
			if (previousSettings == null) {
				previousSettings = root.getUserSettings();
			}
			boolean promptUser = System.getProperty("lingo.suppress.dialogs") == null;

			userSettings = OptimisationHelper.promptForSandboxUserSettings(root, false, promptUser, false, null, previousSettings);
		}
		if (userSettings == null) {
			return;
		}
		// Reset settings not supplied to the user
		userSettings.setShippingOnly(false);
		userSettings.setBuildActionSets(false);
		userSettings.setCleanStateOptimisation(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final String pTaskName = taskName;
		final UserSettings pUserSettings = userSettings;

		if (runAsync) {
			final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> {
				return new LNGSandboxJobDescriptor(pTaskName, scenarioInstance, createSandboxOptionsFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model));
			};

			final TriConsumer<IJobControl, EJobState, IScenarioDataProvider> jobCompletedCallback = (jobControl, newState, sdp) -> {
				if (newState == EJobState.COMPLETED) {
					action.accept((AbstractSolutionSet) jobControl.getJobOutput());
				}
			};
			assert scenarioInstance != null;

			final OptimisationJobRunner jobRunner = new OptimisationJobRunner();
			final ScenarioModelRecord originalModelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
			jobRunner.run(taskName, scenarioInstance, originalModelRecord, null, createJobDescriptorCallback, jobCompletedCallback);
		} else {
			AbstractSolutionSet result = createSandboxOptionsFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model).apply(new NullProgressMonitor());
			action.accept(result);
		}

	}

	@Override
	public void runSandboxInsertion(@NonNull final IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, final OptionAnalysisModel model,
			final Consumer<AbstractSolutionSet> action, boolean runAsync) {

		String taskName = "Sandbox result";

		final EObject object = scenarioDataProvider.getScenario();
		UserSettings userSettings = null;
		if (object instanceof LNGScenarioModel) {
			final LNGScenarioModel root = (LNGScenarioModel) object;

			UserSettings previousSettings = null;
			if (model.getResults() != null) {
				previousSettings = model.getResults().getUserSettings();
			}
			if (previousSettings == null) {
				previousSettings = root.getUserSettings();
			}
			boolean promptUser = System.getProperty("lingo.suppress.dialogs") == null;

			userSettings = OptimisationHelper.promptForInsertionUserSettings(root, false, promptUser, false, null, previousSettings);
		}
		if (userSettings == null) {
			return;
		}
		// Reset settings not supplied to the user
		userSettings.setShippingOnly(false);
		userSettings.setBuildActionSets(false);
		userSettings.setCleanStateOptimisation(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final String pTaskName = taskName;
		final UserSettings pUserSettings = userSettings;

		if (runAsync) {
			final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> {
				return new LNGSandboxJobDescriptor(pTaskName, scenarioInstance, createSandboxInsertionFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model));
			};

			final TriConsumer<IJobControl, EJobState, IScenarioDataProvider> jobCompletedCallback = (jobControl, newState, sdp) -> {
				if (newState == EJobState.COMPLETED) {
					action.accept((AbstractSolutionSet) jobControl.getJobOutput());
				}
			};
			assert scenarioInstance != null;

			final OptimisationJobRunner jobRunner = new OptimisationJobRunner();
			final ScenarioModelRecord originalModelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
			jobRunner.run(taskName, scenarioInstance, originalModelRecord, null, createJobDescriptorCallback, jobCompletedCallback);
		} else {
			AbstractSolutionSet result = createSandboxInsertionFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model).apply(new NullProgressMonitor());
			action.accept(result);
		}

	}

	public interface SandboxJob {
		LNGScenarioToOptimiserBridge getScenarioRunner();

		IMultiStateResult run(IProgressMonitor monitor);

		void dispose();
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxOptionsFunction(final IScenarioDataProvider sdp, final ScenarioInstance scenarioInstance, final UserSettings userSettings,
			final OptionAnalysisModel model) {

		final SandboxResult sandboxResult = AnalyticsFactory.eINSTANCE.createSandboxResult();
		sandboxResult.setUseScenarioBase(false);

		return createSandboxFunction(sdp, scenarioInstance, userSettings, model, sandboxResult, (mapper, baseScheduleSpecification) -> {

			final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();

			final SandboxManualRunner insertionRunner = new SandboxManualRunner(scenarioInstance, sdp, userSettings, mapper, model);

			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return insertionRunner.getBridge();
				}

				@Override
				public IMultiStateResult run(IProgressMonitor monitor) {
					return insertionRunner.runSandbox(monitor);
				}

				@Override
				public void dispose() {
					insertionRunner.dispose();
				}
			};
		});
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxInsertionFunction(final IScenarioDataProvider sdp, final ScenarioInstance scenarioInstance, final UserSettings userSettings,
			final OptionAnalysisModel model) {

		final SlotInsertionOptions sandboxResult = AnalyticsFactory.eINSTANCE.createSlotInsertionOptions();
		sandboxResult.setUseScenarioBase(false);

		return createSandboxFunction(sdp, scenarioInstance, userSettings, model, sandboxResult, (mapper, baseScheduleSpecification) -> {

			final List<Slot<?>> targetSlots = new LinkedList<>();
			final List<VesselEvent> targetEvents = new LinkedList<>();

			final List<EObject> objectsToInsert = new LinkedList<>();
			for (final BaseCaseRow row : model.getBaseCase().getBaseCase()) {
				if (row.getVesselEventOption() != null) {
					objectsToInsert.add(mapper.getOriginal(row.getVesselEventOption()));
				}
				if (row.getBuyOption() != null) {
					objectsToInsert.add(mapper.getOriginal(row.getBuyOption()));
				}
				if (row.getSellOption() != null) {
					objectsToInsert.add(mapper.getOriginal(row.getSellOption()));
				}
			}

			for (final EObject obj : objectsToInsert) {
				if (obj instanceof Slot) {
					final Slot<?> slot = (Slot<?>) obj;
					if (slot instanceof SpotSlot) {
						// Ignore spot market slots.
						continue;
					}
					targetSlots.add(slot);
					sandboxResult.getSlotsInserted().add(slot);
				} else if (obj instanceof VesselEvent) {
					final VesselEvent vesselEvent = (VesselEvent) obj;
					targetEvents.add(vesselEvent);
					sandboxResult.getEventsInserted().add(vesselEvent);
				}
			}
			final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();

			final LNGSchedulerInsertSlotJobRunner insertionRunner = new LNGSchedulerInsertSlotJobRunner(scenarioInstance, sdp, sdp.getEditingDomain(), userSettings, targetSlots, targetEvents,
					extraDataProvider, (mem, data, injector) -> {
						final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
						return transformer.createSequences(baseScheduleSpecification, mem, data, injector, true);
					});

			insertionRunner.setIteration(10_000);

			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return insertionRunner.getLNGScenarioRunner().getScenarioToOptimiserBridge();
				}

				@Override
				public IMultiStateResult run(IProgressMonitor monitor) {
					return insertionRunner.runInsertion(monitor);
				}

				@Override
				public void dispose() {
					insertionRunner.dispose();
				}
			};
		});
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxOptimiserFunction(final IScenarioDataProvider sdp, final ScenarioInstance scenarioInstance, final UserSettings userSettings,
			final OptionAnalysisModel model) {

		final OptimisationResult sandboxResult = AnalyticsFactory.eINSTANCE.createOptimisationResult();
		sandboxResult.setUseScenarioBase(false);

		return createSandboxFunction(sdp, scenarioInstance, userSettings, model, sandboxResult, (mapper, baseScheduleSpecification) -> {

			final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();

			final SandboxOptimiserRunner insertionRunner = new SandboxOptimiserRunner(scenarioInstance, sdp, sdp.getEditingDomain(), userSettings, extraDataProvider, (mem, data, injector) -> {
				final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
				return transformer.createSequences(baseScheduleSpecification, mem, data, injector, true);
			});

			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return insertionRunner.getLNGScenarioRunner().getScenarioToOptimiserBridge();
				}

				@Override
				public IMultiStateResult run(IProgressMonitor monitor) {
					return insertionRunner.runOptimiser(monitor);
				}

				@Override
				public void dispose() {
					insertionRunner.dispose();
				}
			};
		});
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxFunction(final IScenarioDataProvider sdp, final ScenarioInstance scenarioInstance, final UserSettings userSettings,
			final OptionAnalysisModel model, AbstractSolutionSet sandboxResult, BiFunction<IMapperClass, ScheduleSpecification, SandboxJob> jobAction) {

		return monitor -> {

			final boolean dualPNLMode = false;

			final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);
			final IMapperClass mapper = new Mapper(lngScenarioModel, false);
			ScheduleSpecification baseScheduleSpecification = createBaseScheduleSpecification(sdp, model, mapper);
			final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();

			SandboxJob sandboxJob = jobAction.apply(mapper, baseScheduleSpecification);

			try {

				final IMultiStateResult results = sandboxJob.run(monitor);
				final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = sandboxJob.getScenarioRunner();
				final ExecutorService executor = LNGScenarioChainBuilder.createExecutorService();

				final SolutionSetExporterUnit.Util<SolutionOption> exporter = new SolutionSetExporterUnit.Util<>(scenarioToOptimiserBridge, userSettings,
						AnalyticsFactory.eINSTANCE::createSolutionOption, dualPNLMode, true /* enableChangeDescription */);

				exporter.setBreakEvenMode(model.isUseTargetPNL() ? BreakEvenMode.PORTFOLIO : BreakEvenMode.POINT_TO_POINT);
				sandboxResult.setBaseOption(exporter.useAsBaseSolution(baseScheduleSpecification));

				try {
					final List<Future<?>> jobs = new LinkedList<>();

					for (final NonNullPair<ISequences, Map<String, Object>> p : results.getSolutions()) {

						jobs.add(executor.submit(() -> {

							final ISequences seq = p.getFirst();
							final SolutionOption resultSet = exporter.computeOption(seq);
							synchronized (sandboxResult) {
								sandboxResult.getOptions().add(resultSet);
							}

							return null;
						}));
					}

					jobs.forEach(f -> {
						try {
							f.get();
						} catch (final Exception e) {

						}
					});

					exporter.applyPostTasks(sandboxResult);
				} finally {
					executor.shutdown();
				}

			} finally {
				sandboxJob.dispose();
			}

			sandboxResult.setName("SandboxResult");
			sandboxResult.setHasDualModeSolutions(dualPNLMode);
			sandboxResult.setUserSettings(EcoreUtil.copy(userSettings));

			sandboxResult.getCharterInMarketOverrides().addAll(extraDataProvider.extraCharterInMarketOverrides);
			sandboxResult.getExtraCharterInMarkets().addAll(extraDataProvider.extraCharterInMarkets);
			sandboxResult.getExtraVesselAvailabilities().addAll(extraDataProvider.extraVesselAvailabilities);
			sandboxResult.getExtraSlots().addAll(extraDataProvider.extraLoads);
			sandboxResult.getExtraSlots().addAll(extraDataProvider.extraDischarges);
			sandboxResult.getExtraVesselEvents().addAll(extraDataProvider.extraVesselEvents);

			// Check that all the spot slot references are properly contained. E.g. Schedule specification may have extra spot slot instances not used in the schedule models.
			addExtraData(sandboxResult.getBaseOption(), sandboxResult);
			for (final SolutionOption opt : sandboxResult.getOptions()) {
				addExtraData(opt, sandboxResult);
			}

			if (dualPNLMode) {
				SolutionSetExporterUnit.convertToSimpleResult(sandboxResult, dualPNLMode);
			}

			return sandboxResult;
		};
	}

	/**
	 * Check solution option for any extra un-contained objects. E.g. market slots.
	 * 
	 * @param opt
	 * @param sandboxResult
	 */
	private void addExtraData(final SolutionOption opt, final AbstractSolutionSet solutionSet) {
		if (opt != null) {
			final ScheduleSpecification scheduleSpecification = opt.getScheduleSpecification();
			addExtraData(solutionSet, scheduleSpecification);
			if (opt instanceof DualModeSolutionOption) {
				final DualModeSolutionOption dualModeSolutionOption = (DualModeSolutionOption) opt;
				if (dualModeSolutionOption.getMicroBaseCase() != null) {
					addExtraData(solutionSet, dualModeSolutionOption.getMicroBaseCase().getScheduleSpecification());
				}
				if (dualModeSolutionOption.getMicroTargetCase() != null) {
					addExtraData(solutionSet, dualModeSolutionOption.getMicroTargetCase().getScheduleSpecification());
				}
			}
		}
	}

	private void addExtraData(final AbstractSolutionSet solutionSet, final ScheduleSpecification scheduleSpecification) {
		if (scheduleSpecification != null) {
			final Consumer<SlotSpecification> action = e -> {
				final Slot<?> s = e.getSlot();
				if (s.eContainer() == null) {
					solutionSet.getExtraSlots().add(s);
				}
			};
			scheduleSpecification.getVesselScheduleSpecifications().stream() //
					.flatMap(s -> s.getEvents().stream()) //
					.filter(SlotSpecification.class::isInstance) //
					.map(SlotSpecification.class::cast) //
					.forEach(action);
			scheduleSpecification.getNonShippedCargoSpecifications().stream() //
					.flatMap(s -> s.getSlotSpecifications().stream()) //
					.forEach(action);
			scheduleSpecification.getOpenEvents().stream() //
					.filter(SlotSpecification.class::isInstance) //
					.map(SlotSpecification.class::cast) //
					.forEach(action);

			scheduleSpecification.getVesselScheduleSpecifications().stream() //
					.flatMap(s -> s.getEvents().stream()) //
					.filter(VesselEventSpecification.class::isInstance) //
					.map(VesselEventSpecification.class::cast) //
					.forEach(e -> {
						final VesselEvent s = e.getVesselEvent();
						if (s.eContainer() == null) {
							solutionSet.getExtraVesselEvents().add(s);
						}
					});
			scheduleSpecification.getVesselScheduleSpecifications().stream() //
					.map(s -> s.getVesselAllocation()) //
					.filter(VesselAvailability.class::isInstance) //
					.map(VesselAvailability.class::cast) //
					.forEach(s -> {
						if (s.eContainer() == null) {
							solutionSet.getExtraVesselAvailabilities().add(s);
						}
					});
		}
	}

	@Override
	public void runSandboxOptimisation(@NonNull final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance, final OptionAnalysisModel model,
			final Consumer<AbstractSolutionSet> action, boolean runAsync) {

		String taskName = "Sandbox result";

		final EObject object = scenarioDataProvider.getScenario();
		UserSettings userSettings = null;
		if (object instanceof LNGScenarioModel) {
			final LNGScenarioModel root = (LNGScenarioModel) object;

			UserSettings previousSettings = null;
			if (model.getResults() != null) {
				previousSettings = model.getResults().getUserSettings();
			}
			if (previousSettings == null) {
				previousSettings = root.getUserSettings();
			}
			boolean promptUser = System.getProperty("lingo.suppress.dialogs") == null;

			userSettings = OptimisationHelper.promptForUserSettings(root, false, promptUser, false, null, previousSettings);
		}
		if (userSettings == null) {
			return;
		}
		// Reset settings not supplied to the user

		final String pTaskName = taskName;
		final UserSettings pUserSettings = userSettings;

		if (runAsync) {
			final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> {
				return new LNGSandboxJobDescriptor(pTaskName, scenarioInstance, createSandboxOptimiserFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model));
			};

			final TriConsumer<IJobControl, EJobState, IScenarioDataProvider> jobCompletedCallback = (jobControl, newState, sdp) -> {
				if (newState == EJobState.COMPLETED) {
					action.accept((AbstractSolutionSet) jobControl.getJobOutput());
				}
			};
			assert scenarioInstance != null;
			final ScenarioModelRecord originalModelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

			final OptimisationJobRunner jobRunner = new OptimisationJobRunner();
			jobRunner.run(taskName, scenarioInstance, originalModelRecord, null, createJobDescriptorCallback, jobCompletedCallback);
		} else {
			AbstractSolutionSet result = createSandboxOptimiserFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model).apply(new NullProgressMonitor());
			action.accept(result);
		}
	}

	private ScheduleSpecification createBaseScheduleSpecification(final IScenarioDataProvider scenarioDataProvider, final OptionAnalysisModel model, final IMapperClass mapper) {
		ScheduleSpecification baseScheduleSpecification;

		if (model.getBaseCase().isKeepExistingScenario()) {
			final ExistingBaseCaseToScheduleSpecification builder = new ExistingBaseCaseToScheduleSpecification(scenarioDataProvider, mapper);
			baseScheduleSpecification = builder.generate(model.getBaseCase());
		} else {

			final BaseCaseToScheduleSpecification builder = new BaseCaseToScheduleSpecification(scenarioDataProvider.getTypedScenario(LNGScenarioModel.class), mapper);
			baseScheduleSpecification = builder.generate(model.getBaseCase());
		}
		return baseScheduleSpecification;
	}
}
