/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.pairing;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.FollowersAndPrecedersProviderImpl;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashSetLongTermSlotsEditor;

public class PairingOptimiserUnit {

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final IMultiStateResult inputState;

	@NonNull
	private final String phase;

	private @NonNull JobExecutorFactory jobExecutorFactory;

	private LNGScenarioModel initialScenario;

	@SuppressWarnings("null")
	public PairingOptimiserUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constainAndFitnessSettings, @NonNull final JobExecutorFactory jobExecutorFactory, @NonNull final ISequences initialSequences,
			final LNGScenarioModel initialScenario, @NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;
		this.phase = phase;
		this.jobExecutorFactory = jobExecutorFactory;
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
				final HashSetLongTermSlotsEditor longTermSlotEditor = new HashSetLongTermSlotsEditor();
				bind(ILongTermSlotsProvider.class).toInstance(longTermSlotEditor);
				bind(ILongTermSlotsProviderEditor.class).toInstance(longTermSlotEditor);
				final GoogleORToolsPairingMatrixOptimiser matrixOptimiser = new GoogleORToolsPairingMatrixOptimiser();
				bind(IPairingMatrixOptimiser.class).toInstance(matrixOptimiser);
			}

			@Provides
			@ThreadLocalScope
			private PairingOptimiser providePerThreadBagMover(@NonNull final Injector injector) {
				PairingOptimiser longTermOptimiser = new PairingOptimiser();
				injector.injectMembers(longTermOptimiser);
				return longTermOptimiser;
			}

		});

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.inputState = inputState;
	}

	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {
		try {

			final ILongTermSlotsProviderEditor longTermSlotsProviderEditor = injector.getInstance(ILongTermSlotsProviderEditor.class);
			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);

			final Collection<IPortSlot> allPortSlots = SequencesToPortSlotsUtils.getAllPortSlots(dataTransformer.getOptimisationData().getSequenceElements(), portSlotProvider);
			allPortSlots.forEach(longTermSlotsProviderEditor::addLongTermSlot);
			addLongTermOptimiserEvents(longTermSlotsProviderEditor, allPortSlots);

			monitor.beginTask("Generate solutions", 100);
			final CharterInMarket charterInMarket = initialScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(0);
			final List<Future<Pair<ISequences, Long>>> futures = new LinkedList<>();
			try (JobExecutor jobExecutor = jobExecutorFactory.begin()) {

				futures.add(jobExecutor.submit(() -> {
					try {
						// Bit nasty, but we are still in PoC stages

						final PairingOptimiser calculator = injector.getInstance(PairingOptimiser.class);
						return calculator.optimise(dataTransformer, charterInMarket);
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
					} catch (final InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}

				Collections.sort(results, (a, b) -> {
					final long al = a.getSecond();
					final long bl = b.getSecond();
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

	private void addLongTermOptimiserEvents(final ILongTermSlotsProviderEditor longTermSlotsProviderEditor, final Collection<IPortSlot> allPortSlots) {
		final Set<PortType> eventsPortType = Sets.newHashSet(PortType.DryDock, PortType.Maintenance, PortType.CharterOut, PortType.CharterLength);
		allPortSlots.forEach(e -> {
			if (eventsPortType.contains(e.getPortType())) {
				if (e instanceof IVesselEventPortSlot) {
					longTermSlotsProviderEditor.addEvent(((IVesselEventPortSlot) e).getEventPortSlots());
				} else {
					longTermSlotsProviderEditor.addEvent(Collections.singletonList(e));
				}
			}
		});
	}
}
