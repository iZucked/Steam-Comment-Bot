/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.spec;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.analytics.SlotType;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;

public class ScheduleSpecificationHelper {
	private final List<BiConsumer<LNGScenarioToOptimiserBridge, Injector>> jobs = new LinkedList<>();
	private final List<VesselAvailability> extraAvailabilities = new LinkedList<>();
	private final List<CharterInMarketOverride> extraCharterInMarketOverrides = new LinkedList<>();
	private final List<CharterInMarket> extraCharterInMarkets = new LinkedList<>();

	private final List<LoadSlot> extraLoads = new LinkedList<>();
	private final List<DischargeSlot> extraDischarges = new LinkedList<>();

	private IScenarioDataProvider scenarioDataProvider;

	public ScheduleSpecificationHelper(final IScenarioDataProvider scenarioDataProvider) {
		this.scenarioDataProvider = scenarioDataProvider;
	}

	public void processExtraDataProvider(ExtraDataProvider extraDataProvider) {

		// Null Check
		if (extraDataProvider.extraVesselAvailabilities != null) {
			extraAvailabilities.addAll(extraDataProvider.extraVesselAvailabilities);
		}
		if (extraDataProvider.extraCharterInMarkets != null) {
			extraCharterInMarkets.addAll(extraDataProvider.extraCharterInMarkets);
		}
		if (extraDataProvider.extraCharterInMarketOverrides != null) {
			extraCharterInMarketOverrides.addAll(extraDataProvider.extraCharterInMarketOverrides);
		}
		if (extraDataProvider.extraLoads != null) {
			extraLoads.addAll(extraDataProvider.extraLoads);
		}
		if (extraDataProvider.extraDischarges != null) {
			extraDischarges.addAll(extraDataProvider.extraDischarges);
		}
	}

	public void addJobs(BiConsumer<LNGScenarioToOptimiserBridge, Injector> job) {
		jobs.add(job);
	}

	public void generateResults(final ScenarioInstance scenarioInstance, final UserSettings userSettings, final EditingDomain editingDomain, Collection<String> initialHints) {
		final UserSettings settings = EcoreUtil.copy(userSettings);
		settings.unsetPeriodStartDate();
		settings.unsetPeriodEnd();

		@NonNull
		final Collection<@NonNull String> hints = new LinkedList<>(initialHints);
		hints.add(LNGTransformerHelper.HINT_DISABLE_CACHES);

		final SolutionBuilderSettings solutionBuilderSettings = ParametersFactory.eINSTANCE.createSolutionBuilderSettings();
		solutionBuilderSettings.setConstraintAndFitnessSettings(ParametersFactory.eINSTANCE.createConstraintAndFitnessSettings());

		final LNGScenarioToOptimiserBridge bridge = new LNGScenarioToOptimiserBridge(scenarioDataProvider, //
				scenarioInstance, //
				settings, //
				solutionBuilderSettings, //
				editingDomain, //
				null, //
				OptimiserInjectorServiceMaker.begin()//
						.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, new AbstractModule() {

							@Override
							protected void configure() {

							}

							@Provides
							@Named(LNGScenarioTransformer.EXTRA_VESSEL_AVAILABILITIES)
							private List<VesselAvailability> provideExtraAvailabilities() {
								return extraAvailabilities;
							}

							@Provides
							@Named(LNGScenarioTransformer.EXTRA_CHARTER_IN_MARKET_OVERRIDES)
							private List<CharterInMarketOverride> provideCharterInMarketOverrides() {
								return extraCharterInMarketOverrides;
							}

							@Provides
							@Named(LNGScenarioTransformer.EXTRA_CHARTER_IN_MARKETS)
							private List<CharterInMarket> provideCharterInMarkets() {
								return extraCharterInMarkets;
							}

							@Provides
							@Named(LNGScenarioTransformer.EXTRA_LOAD_SLOTS)
							private List<LoadSlot> provideLoadSlots() {
								return extraLoads;
							}

							@Provides
							@Named(LNGScenarioTransformer.EXTRA_DISCHARGE_SLOTS)
							private List<DischargeSlot> provideDischargeSlots() {
								return extraDischarges;
							}
						})//
						.make(), //
				true, // Evaluation only?
				hints.toArray(new String[hints.size()]) // Hints? No Caching?
		);
		// Probably need to bring in the evaluation modules
		final Collection<IOptimiserInjectorService> services = bridge.getDataTransformer().getModuleServices();

		// FIXME: Disable main break even evalRuator
		// FIXME: Disable caches!

		final List<Module> modules = new LinkedList<>();

		ISequences emptySequences = new ModifiableSequences(new LinkedList<>());
		modules.add(new InitialSequencesModule(emptySequences));
		modules.add(new InputSequencesModule(emptySequences));

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, ParametersFactory.eINSTANCE.createConstraintAndFitnessSettings()),
				services, IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

		final Injector injector = bridge.getInjector().createChildInjector(modules);

		jobs.forEach(c -> c.accept(bridge, injector));
		// Clean up inputs (rather than explicit #dispose())
		jobs.clear();
	}

	public static SlotType getSlotType(final Slot slot) {

		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			return loadSlot.isDESPurchase() ? SlotType.DES_PURCHASE : SlotType.FOB_PURCHASE;
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			return dischargeSlot.isFOBSale() ? SlotType.FOB_SALE : SlotType.DES_SALE;

		}
		throw new IllegalArgumentException();
	}

	public void processExtraData_Loads(List<LoadSlot> extraLoads2) {
		this.extraLoads.addAll(extraLoads2);

	}

	public void processExtraData_Discharges(List<DischargeSlot> extraDischarges2) {
		this.extraDischarges.addAll(extraDischarges2);
	}
}
