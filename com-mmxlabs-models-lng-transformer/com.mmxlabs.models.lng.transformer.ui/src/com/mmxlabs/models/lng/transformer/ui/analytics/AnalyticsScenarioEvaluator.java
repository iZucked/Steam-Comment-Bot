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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import javax.inject.Singleton;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
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
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.ui.ExportScheduleHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.mtm.MTMSanboxUnit;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ScheduleSpecificationHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.viability.ViabilitySanboxUnit;
import com.mmxlabs.models.lng.transformer.ui.analytics.viability.ViabilityWindowTrimmer;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.common.constraints.LockedUnusedElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.ui.ScenarioResult;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
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
	public void evaluateBaseCase(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final UserSettings userSettings, @Nullable final ScenarioInstance parentForFork,
			final boolean fork, final String forkName) {

		@NonNull
		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, scenarioDataProvider.getTypedScenario(LNGScenarioModel.class));
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		// No optimisation going on, clear stages. Need better OptimisationHelper API?
		optimisationPlan.getStages().clear();

		try {

			LNGOptimisationBuilder.quickEvaluation(scenarioDataProvider, optimisationPlan, null);

			if (parentForFork != null && fork) {
				final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(parentForFork);
				scenarioService.copyInto(parentForFork, scenarioDataProvider, forkName, new NullProgressMonitor());
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void multiEvaluate(ScenarioInstance scenarioInstance, EditingDomain editingDomain, @NonNull IScenarioDataProvider scenarioDataProvider, @NonNull UserSettings userSettings,
			long targetProfitAndLoss, BreakEvenMode breakEvenMode, List<Pair<BaseCase, ScheduleSpecification>> baseCases, IMapperClass mapper, BiConsumer<BaseCase, Schedule> resultHandler) {

		final ScheduleSpecificationHelper helper = new ScheduleSpecificationHelper(scenarioDataProvider);
		helper.processExtraDataProvider(mapper.getExtraDataProvider());

		for (final Pair<BaseCase, ScheduleSpecification> p : baseCases) {

			final BiFunction<LNGScenarioToOptimiserBridge, Injector, Supplier<Command>> job = (bridge, injector) -> {
				final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
				final ISequences base = transformer.createSequences(p.getSecond(), bridge.getDataTransformer());
				Long be_pnl = null;
				if (breakEvenMode == BreakEvenMode.PORTFOLIO) {
					be_pnl = OptimiserUnitConvertor.convertToInternalFixedCost(targetProfitAndLoss);
				}
				try {
					final Schedule schedule = bridge.createSchedule(base, Collections.emptyMap(), be_pnl);
					resultHandler.accept(p.getFirst(), schedule);
				} catch (final Throwable e) {
				}
				return null;
			};
			helper.addJobs(job);
		}
		final List<String> hints = new LinkedList<>();
		if (breakEvenMode == BreakEvenMode.PORTFOLIO) {
			hints.add(LNGEvaluationModule.HINT_PORTFOLIO_BREAKEVEN);
		}

		helper.generateResults(scenarioInstance, userSettings, editingDomain, hints, new NullProgressMonitor());
	}

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
				for (LoadSlot slot : usedLoadSlots) {
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
				for (DischargeSlot slot : usedDischargeSlots) {
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
				for (Cargo cargo : usedCargoes) {
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
				for (VesselEvent vesselEvent : usedVesselEvents) {
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
				for (VesselAvailability vesselAvailability : usedVesselAvailabilites) {
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
			analyticsModel.getOptimisations().clear();
			analyticsModel.getOptionModels().clear();

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
				LNGDataTransformer dataTransformer = bridge.getDataTransformer();
				ViabilitySanboxUnit unit = new ViabilitySanboxUnit(lngScenarioModel, dataTransformer, "viability-sandbox", userSettings, constraints, executorService,
						dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), dataTransformer.getHints());
				/* Command cmd = */
				unit.run(model, mapper, shippingMap, new NullProgressMonitor());
			});
		} finally {
			executorService.shutdownNow();
		}
	}

	@Override
	public void evaluateMTMSandbox(@NonNull IScenarioDataProvider scenarioDataProvider, @Nullable ScenarioInstance scenarioInstance, @NonNull UserSettings userSettings, MTMModel model,
			IMapperClass mapper) {
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
				LNGDataTransformer dataTransformer = bridge.getDataTransformer();
				MTMSanboxUnit unit = new MTMSanboxUnit(lngScenarioModel, dataTransformer, "mtm-sandbox", userSettings, constraints, executorService,
						dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), dataTransformer.getHints());
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
		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		final ScheduleSpecificationHelper helper = new ScheduleSpecificationHelper(scenarioDataProvider);
		helper.processExtraDataProvider(mapper.getExtraDataProvider());

		final List<String> hints = new LinkedList<>();
		hints.add(LNGTransformerHelper.HINT_DISABLE_CACHES);
		final ConstraintAndFitnessSettings constraints = ScenarioUtils.createDefaultConstraintAndFitnessSettings(false);

		final ExecutorService executorService = Executors.newFixedThreadPool(6);
		helper.generateWith(scenarioInstance, userSettings, scenarioDataProvider.getEditingDomain(), hints, (bridge) -> {
			LNGDataTransformer dataTransformer = bridge.getDataTransformer();
			BreakEvenSanboxUnit unit = new BreakEvenSanboxUnit(lngScenarioModel, dataTransformer, "break-even-sandbox", userSettings, constraints, executorService,
					dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), dataTransformer.getHints());
			/* Command cmd = */
			unit.run(model, mapper, shippingMap, new NullProgressMonitor());
		});
	}
}
