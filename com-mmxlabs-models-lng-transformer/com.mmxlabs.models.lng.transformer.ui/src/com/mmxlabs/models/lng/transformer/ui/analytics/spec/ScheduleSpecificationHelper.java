/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.viability.ViabilityWindowTrimmer;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;
import com.mmxlabs.scheduler.optimiser.scheduling.ICustomTimeWindowTrimmer;

public class ScheduleSpecificationHelper {
	private final List<BiFunction<LNGScenarioToOptimiserBridge, Injector, Supplier<Command>>> jobs = new LinkedList<>();
	private final List<VesselAvailability> extraAvailabilities = new LinkedList<>();
	private final List<CharterInMarketOverride> extraCharterInMarketOverrides = new LinkedList<>();
	private final List<CharterInMarket> extraCharterInMarkets = new LinkedList<>();

	private final List<LoadSlot> extraLoads = new LinkedList<>();
	private final List<DischargeSlot> extraDischarges = new LinkedList<>();

	private IScenarioDataProvider scenarioDataProvider;
	private @Nullable IOptimiserInjectorService extraInjectorService;

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

	public void addJobs(BiFunction<LNGScenarioToOptimiserBridge, Injector, Supplier<Command>> job) {
		jobs.add(job);
	}

	public void generateWith(final ScenarioInstance scenarioInstance, final UserSettings userSettings, final EditingDomain editingDomain, Collection<String> initialHints,
			Consumer<LNGScenarioToOptimiserBridge> action) {

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

								bind(ViabilityWindowTrimmer.class).in(Singleton.class);
								bind(ICustomTimeWindowTrimmer.class).to(ViabilityWindowTrimmer.class);
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

		action.accept(bridge);
	}

	public void generateResults(final ScenarioInstance scenarioInstance, final UserSettings userSettings, final EditingDomain editingDomain, Collection<String> initialHints,
			IProgressMonitor monitor) {

		monitor.beginTask("Evaluate", jobs.size());
		try {
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
									// Nothing to do here - all in providers
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
							.make(extraInjectorService), //

					true, // evaluation only?
					hints.toArray(new String[hints.size()]) // Hints? No Caching?
			);
			// Probably need to bring in the evaluation modules
			final Collection<IOptimiserInjectorService> services = bridge.getDataTransformer().getModuleServices();

			// FIXME: Disable main break even evaluator
			// FIXME: Disable caches!

			final List<Module> modules = new LinkedList<>();

			ISequences emptySequences = new ModifiableSequences(new LinkedList<>());
			modules.add(new InitialSequencesModule(emptySequences));
			modules.add(new InputSequencesModule(emptySequences));
			modules.add(new InitialPhaseOptimisationDataModule());

			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, ParametersFactory.eINSTANCE.createConstraintAndFitnessSettings()),
					services, IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

			final Injector injector = bridge.getInjector().createChildInjector(modules);
			ExecutorService executor = LNGScenarioChainBuilder.createExecutorService();
			try {
				List<Future<?>> futures = new ArrayList<>(jobs.size());
				List<Supplier<Command>> commandSuppliers = new LinkedList<>();
				jobs.forEach(c -> futures.add(executor.submit(() -> {
					Supplier<Command> cc = c.apply(bridge, injector);
					if (cc != null) {
						commandSuppliers.add(cc);
					}
					monitor.worked(1);
				})));
				futures.forEach(f -> {
					try {
						f.get();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				if (!commandSuppliers.isEmpty()) {
					CommandStack commandStack = editingDomain.getCommandStack();
					CompoundCommand cmd = new CompoundCommand();
					for (Supplier<Command> s : commandSuppliers) {
						Command c = s.get();
						if (c != null) {
							cmd.append(c);
						}
					}
					RunnerHelper.syncExecDisplayOptional(() -> {
						CommandStack commandStack1 = editingDomain.getCommandStack();
						commandStack.execute(cmd);
					});
				}
			} finally {
				executor.shutdownNow();
			}
		} finally {
			monitor.done();
		}
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

	public void withModuleService(@NonNull IOptimiserInjectorService extraInjectorService) {
		this.extraInjectorService = extraInjectorService;
	}
}
