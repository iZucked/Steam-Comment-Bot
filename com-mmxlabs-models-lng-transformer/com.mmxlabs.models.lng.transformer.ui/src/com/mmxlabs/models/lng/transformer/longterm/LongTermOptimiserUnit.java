/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.longterm.webservice.WebserviceLongTermMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.ui.LNGExporterUnit;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.FollowersAndPrecedersProviderImpl;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashSetLongTermSlotsEditor;

public class LongTermOptimiserUnit {

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final IMultiStateResult inputState;

	@NonNull
	private final String phase;

	private final Map<Thread, LongTermOptimiser> threadCache = new ConcurrentHashMap<>(100);

	private @NonNull ExecutorService executorService;

	private IVesselAvailability nominalMarketAvailability;
	
	private LNGScenarioModel initialScenario;

	@SuppressWarnings("null")
	public LongTermOptimiserUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constainAndFitnessSettings, @NonNull final ExecutorService executorService, @NonNull final ISequences initialSequences,
			LNGScenarioModel initialScenario, @NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;
		this.phase = phase;
		this.executorService = executorService;
		this.initialScenario = initialScenario;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, constainAndFitnessSettings), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

		modules.add(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IFollowersAndPreceders.class).to(FollowersAndPrecedersProviderImpl.class).in(Singleton.class);
				HashSetLongTermSlotsEditor longTermSlotEditor = new HashSetLongTermSlotsEditor();
				bind(ILongTermSlotsProvider.class).toInstance(longTermSlotEditor);
				bind(ILongTermSlotsProviderEditor.class).toInstance(longTermSlotEditor);
				WebserviceLongTermMatrixOptimiser matrixOptimiser = new WebserviceLongTermMatrixOptimiser();
				bind(ILongTermMatrixOptimiser.class).toInstance(matrixOptimiser);
			}

			@Provides
			private LongTermOptimiser providePerThreadBagMover(@NonNull final Injector injector) {

				LongTermOptimiser longTermOptimiser = threadCache.get(Thread.currentThread());
				if (longTermOptimiser == null) {
					longTermOptimiser = new LongTermOptimiser();
					injector.injectMembers(longTermOptimiser);
					threadCache.put(Thread.currentThread(), longTermOptimiser);
				}
				return longTermOptimiser;
			}
			
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.inputState = inputState;
	}

	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {
			try {

				@NonNull
				ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
				ILongTermSlotsProviderEditor longTermSlotsProviderEditor = injector.getInstance(ILongTermSlotsProviderEditor.class);
				IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
				Collection<IPortSlot> allPortSlots = SequencesToPortSlotsUtils.getAllPortSlots(dataTransformer.getOptimisationData().getSequenceElements(), portSlotProvider);
				allPortSlots.forEach(e -> longTermSlotsProviderEditor.addLongTermSlot(e));
				monitor.beginTask("Generate solutions", 100);
				CharterInMarket charterInMarket = initialScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(0);
				final List<Future<Pair<ISequences, Long>>> futures = new LinkedList<>();
				try {

					futures.add(executorService.submit(() -> {
						try {
							// Bit nasty, but we are still in PoC stages

							final LongTermOptimiser calculator = injector.getInstance(LongTermOptimiser.class);
							return calculator.optimise(executorService, dataTransformer, charterInMarket);
						} finally {
							monitor.worked(1);
						}
					}));
					final List<Pair<ISequences, Long>> results = new LinkedList<>();

					// Block until all futures completed
					for (final Future<Pair<ISequences, Long>> f : futures) {
						try {
							final Pair<ISequences, Long> s = f.get();
							if (s != null) {
								results.add(s);
							}
						} catch (final InterruptedException e) {
							e.printStackTrace();
						} catch (final ExecutionException e) {
							e.printStackTrace();
						}
					}

					Collections.sort(results, (a, b) -> {
						long al = a.getSecond();
						long bl = b.getSecond();
						if (al > bl) {
							return -1;
						} else if (al < bl) {
							return 1;
						} else {
							return 0;
						}
					});

					final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.stream() //
							.distinct() //
							.map(r -> new NonNullPair<ISequences, Map<String, Object>>(r.getFirst(), new HashMap<>())) //
							.collect(Collectors.toList());

					return new MultiStateResult(solutions.get(0), solutions);
				} finally {
					monitor.done();
				}

			} finally {
			}
	}

//	public static IChainLink export(final ChainBuilder chainBuilder, final int progressTicks, @NonNull final LNGScenarioToOptimiserBridge runner, @NonNull final ContainerProvider containerProvider) {
//		return LNGExporterUnit.exportMultiple(chainBuilder, progressTicks, runner, containerProvider, "Saving insertion plans", parent -> {
//
//			final List<Container> elementsToRemove = new LinkedList<>();
//			for (final Container c : parent.getElements()) {
//				if (c.getName().startsWith("InsertionPlan-")) {
//					elementsToRemove.add(c);
//				}
//			}
//			for (final Container c : elementsToRemove) {
//				SSDataManager.Instance.findScenarioService(parent).delete(c);
//			}
//		}, changeSetIdx -> {
//			String newName;
//			if (changeSetIdx == 0) {
//				newName = "InsertionPlan-base";
//				changeSetIdx++;
//			} else {
//				newName = String.format("InsertionPlan-%s", (changeSetIdx++));
//			}
//			return newName;
//		});
//	}
}
