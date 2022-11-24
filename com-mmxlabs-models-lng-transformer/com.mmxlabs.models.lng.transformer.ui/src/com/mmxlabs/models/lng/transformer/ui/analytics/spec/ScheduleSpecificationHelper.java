/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.SlotType;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
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
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;
import com.mmxlabs.scheduler.optimiser.scheduling.ICustomTimeWindowTrimmer;

public class ScheduleSpecificationHelper {
	private final List<BiFunction<LNGScenarioToOptimiserBridge, Injector, Supplier<Command>>> jobs = new LinkedList<>();

	private ExtraDataProvider extraDataProvider = new ExtraDataProvider();

	private IScenarioDataProvider scenarioDataProvider;
	private @Nullable IOptimiserInjectorService extraInjectorService;

	public ScheduleSpecificationHelper(final IScenarioDataProvider scenarioDataProvider) {
		this.scenarioDataProvider = scenarioDataProvider;
	}

	public synchronized void processExtraDataProvider(ExtraDataProvider extraDataProvider) {

		this.extraDataProvider.merge(extraDataProvider);
	}

	public synchronized void addJobs(BiFunction<LNGScenarioToOptimiserBridge, Injector, Supplier<Command>> job) {
		jobs.add(job);
	}

	public synchronized void generateWith(final ScenarioInstance scenarioInstance, final UserSettings userSettings, final EditingDomain editingDomain, Collection<String> initialHints,
			Consumer<LNGScenarioToOptimiserBridge> action) {

		final UserSettings settings = EMFCopier.copy(userSettings);
		settings.unsetPeriodStartDate();
		settings.unsetPeriodEnd();

		@NonNull
		final Collection<@NonNull String> hints = new LinkedList<>(initialHints);
		hints.add(SchedulerConstants.HINT_DISABLE_CACHES);

		final SolutionBuilderSettings solutionBuilderSettings = ParametersFactory.eINSTANCE.createSolutionBuilderSettings();
		solutionBuilderSettings.setConstraintAndFitnessSettings(ParametersFactory.eINSTANCE.createConstraintAndFitnessSettings());

		final int cores = LNGScenarioChainBuilder.getNumberOfAvailableCores();

		final LNGScenarioToOptimiserBridge bridge = new LNGScenarioToOptimiserBridge(scenarioDataProvider, //
				scenarioInstance, //
				extraDataProvider, settings, //
				solutionBuilderSettings, //
				editingDomain, //
				cores, //
				null, //
				OptimiserInjectorServiceMaker.begin()//
						.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, new AbstractModule() {

							@Override
							protected void configure() {
								bind(ViabilityWindowTrimmer.class).in(Singleton.class);
								bind(ICustomTimeWindowTrimmer.class).to(ViabilityWindowTrimmer.class);
							}

						})//

						.make(), //
				true, // Evaluation only?
				hints.toArray(new String[hints.size()]) // Hints? No Caching?
		);

		action.accept(bridge);
	}

	public synchronized void generateResults(final ScenarioInstance scenarioInstance, final UserSettings userSettings, final EditingDomain editingDomain, Collection<String> initialHints,
			IProgressMonitor monitor) {

		monitor.beginTask("Evaluate", jobs.size());
		try {

			TriConsumer<LNGScenarioToOptimiserBridge, Injector, Integer> action = (bridge, injector, cores) -> {

				JobExecutorFactory jobExecutorFactory = LNGScenarioChainBuilder.createExecutorService(cores);
				try (JobExecutor jobExecutor = jobExecutorFactory.begin()) {
					List<Future<?>> futures = new ArrayList<>(jobs.size());
					List<Supplier<Command>> commandSuppliers = new LinkedList<>();
					jobs.forEach(c -> futures.add(jobExecutor.submit(() -> {
						Supplier<Command> cc = c.apply(bridge, injector);
						if (cc != null) {
							synchronized (commandSuppliers) {
								commandSuppliers.add(cc);
							}
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
							commandStack.execute(cmd);
						});
					}
				}
			};
			withRunner(scenarioInstance, userSettings, editingDomain, initialHints, action);
		} finally {
			monitor.done();
		}
	}

	public synchronized void withRunner(final ScenarioInstance scenarioInstance, final UserSettings userSettings, final EditingDomain editingDomain, Collection<String> initialHints,
			TriConsumer<LNGScenarioToOptimiserBridge, Injector, Integer> action) {

		final UserSettings settings = EMFCopier.copy(userSettings);
		settings.unsetPeriodStartDate();
		settings.unsetPeriodEnd();

		@NonNull
		final Collection<@NonNull String> hints = new LinkedList<>(initialHints);
		hints.add(SchedulerConstants.HINT_DISABLE_CACHES);
		hints.add(LNGTransformerHelper.HINT_EVALUATION_ONLY);

		final SolutionBuilderSettings solutionBuilderSettings = ParametersFactory.eINSTANCE.createSolutionBuilderSettings();
		solutionBuilderSettings.setConstraintAndFitnessSettings(ScenarioUtils.createDefaultConstraintAndFitnessSettings());
		// Ignore objectives
		solutionBuilderSettings.getConstraintAndFitnessSettings().getObjectives().clear();

		final int cores = LNGScenarioChainBuilder.getNumberOfAvailableCores();
		final LNGScenarioToOptimiserBridge bridge = new LNGScenarioToOptimiserBridge(scenarioDataProvider, //
				scenarioInstance, //
				extraDataProvider, //
				settings, //
				solutionBuilderSettings, //
				editingDomain, //
				cores, //
				null, //
				extraInjectorService, //
				true, // evaluation only?
				hints.toArray(new String[hints.size()]) // Hints? No Caching?
		);
		// Probably need to bring in the evaluation modules
		final Collection<IOptimiserInjectorService> services = bridge.getDataTransformer().getModuleServices();

		// FIXME: Disable main break even evaluator
		// FIXME: Disable caches!

		final List<Module> modules = new LinkedList<>();
		//
		ISequences emptySequences = new ModifiableSequences(new LinkedList<>());
		modules.add(new InitialSequencesModule(emptySequences));
		modules.add(new InputSequencesModule(emptySequences));
		modules.add(new InitialPhaseOptimisationDataModule());

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, ParametersFactory.eINSTANCE.createConstraintAndFitnessSettings()),
				services, IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

		final Injector injector = bridge.getInjector().createChildInjector(modules);

		action.accept(bridge, injector, cores);

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

	public synchronized void processExtraData_Loads(List<LoadSlot> extraLoads2) {
		this.extraDataProvider.extraLoads.addAll(extraLoads2);

	}

	public synchronized void processExtraData_Discharges(List<DischargeSlot> extraDischarges2) {
		this.extraDataProvider.extraDischarges.addAll(extraDischarges2);
	}

	public synchronized void processExtraData_VesselEvents(List<VesselEvent> extraEvents) {
		this.extraDataProvider.extraVesselEvents.addAll(extraEvents);
	}

	public synchronized void withModuleService(@NonNull IOptimiserInjectorService extraInjectorService) {
		this.extraInjectorService = extraInjectorService;
	}

	public synchronized void processExtraData_VesselCharters(List<VesselCharter> extraVesselAvailabilities) {
		if (extraVesselAvailabilities != null) {
			this.extraDataProvider.extraVesselCharters.addAll(extraVesselAvailabilities);
		}
	}

	public synchronized void processExtraData_CharterInMarkets(List<CharterInMarket> extraCharterInMarkets) {
		if (extraCharterInMarkets != null) {
			this.extraDataProvider.extraCharterInMarkets.addAll(extraCharterInMarkets);
		}
	}

	public synchronized void processExtraData_CharterInMarketOverrides(List<CharterInMarketOverride> extraCharterInMarketOverrides) {
		if (extraCharterInMarketOverrides != null) {
			this.extraDataProvider.extraCharterInMarketOverrides.addAll(extraCharterInMarketOverrides);
		}
	}

	public void processExtraData(@Nullable AbstractSolutionSet solutionSet) {
		if (solutionSet != null) {
			extraDataProvider.merge(solutionSet);
		}
	}

}
