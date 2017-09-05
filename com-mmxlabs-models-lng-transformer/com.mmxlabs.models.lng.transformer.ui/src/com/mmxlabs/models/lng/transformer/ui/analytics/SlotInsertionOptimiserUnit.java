/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.ArrayList;
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiser;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserInitialState;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveGeneratorModule;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class SlotInsertionOptimiserUnit {

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final IMultiStateResult inputState;

	@NonNull
	private final String phase;

	private final Map<Thread, SlotInsertionOptimiser> threadCache = new ConcurrentHashMap<>(100);

	private @NonNull ExecutorService executorService;

	private IVesselAvailability nominalMarketAvailability;

	@SuppressWarnings("null")
	public SlotInsertionOptimiserUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constainAndFitnessSettings, @NonNull final ExecutorService executorService, @NonNull final ISequences initialSequences,
			@NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;
		this.phase = phase;
		this.executorService = executorService;

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
				install(new MoveGeneratorModule(true));
			}

			@Provides
			private EvaluationHelper provideEvaluationHelper(final Injector injector, @Named(LNGParameters_EvaluationSettingsModule.OPTIMISER_REEVALUATE) final boolean isReevaluating) {
				final EvaluationHelper helper = new EvaluationHelper(isReevaluating);
				injector.injectMembers(helper);
				
				helper.setFlexibleViolationCount(Integer.MAX_VALUE);
				
				return helper;
			}

			@Provides
			private SlotInsertionOptimiser providePerThreadOptimiser(@NonNull final Injector injector) {

				SlotInsertionOptimiser optimiser = threadCache.get(Thread.currentThread());
				if (optimiser == null) {
					final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);
					scope.enter();
					optimiser = new SlotInsertionOptimiser();
					injector.injectMembers(optimiser);
					threadCache.put(Thread.currentThread(), optimiser);
				}
				return optimiser;
			}
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.inputState = inputState;
	}

	public IMultiStateResult run(final @NonNull List<Slot> slotsToInsert, List<VesselEvent> eventsToInsert, final int tries, @NonNull final IProgressMonitor monitor) {
		// try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
		// scope.enter();
		try {

			@NonNull
			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final List<IPortSlot> slotElements = slotsToInsert.stream() //
					.map(s -> modelEntityMap.getOptimiserObjectNullChecked(s, IPortSlot.class)) //
					.collect(Collectors.toList());
			final List<IPortSlot> eventElements = eventsToInsert.stream() //
					.map(s -> modelEntityMap.getOptimiserObjectNullChecked(s, IPortSlot.class)) //
					.collect(Collectors.toList());

			List<IPortSlot> optionElements = new ArrayList<IPortSlot>(slotElements.size() + eventElements.size());
			optionElements.addAll(slotElements);
			optionElements.addAll(eventElements);

			monitor.beginTask("Generate solutions", tries);

			final SlotInsertionOptimiserInitialState state = new SlotInsertionOptimiserInitialState();
			{
				final IOptionalElementsProvider optionalElementsProvider = injector.getInstance(IOptionalElementsProvider.class);
				final IMoveHandlerHelper moveHandlerHelper = injector.getInstance(IMoveHandlerHelper.class);
				final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);

				{
					// Calculate the initial metrics
					try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
						scope.enter();
						ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
						final EvaluationHelper evaluationHelper = injector.getInstance(EvaluationHelper.class);
						final ISequences initialRawSequences = injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)));
						state.initialMetrics = evaluationHelper.evaluateState(initialRawSequences, manipulator.createManipulatedSequences(initialRawSequences), null, true, null, null);
						if (state.initialMetrics == null) {
							throw new UserFeedbackException(
									"Unable to perform insertion on this scenario. This is most likely caused by late and overlapping cargoes. Please check validation messages.");
						}
						state.originalRawSequences = initialRawSequences;
					}
				}
				{
					final IModifiableSequences tmpRawSequences = new ModifiableSequences(state.originalRawSequences);

					for (ISequenceElement e : tmpRawSequences.getUnusedElements()) {
						if (optionalElementsProvider.isElementRequired(e) || optionalElementsProvider.getSoftRequiredElements().contains(e)) {
							state.initiallyUnused.add(e);
						}
					}

					// Makes sure target slots are not contained in the solution.
					for (final IPortSlot portSlot : optionElements) {
						final ISequenceElement element = portSlotProvider.getElement(portSlot);

						final LookupManager lookupManager = new LookupManager(tmpRawSequences);
						final @Nullable Pair<IResource, Integer> lookup = lookupManager.lookup(element);
						if (lookup != null && lookup.getFirst() != null) {
							@NonNull
							final IModifiableSequence modifiableSequence = tmpRawSequences.getModifiableSequence(lookup.getFirst());
							@NonNull
							final List<ISequenceElement> segment = moveHandlerHelper.extractSegment(modifiableSequence, element);
							for (final ISequenceElement e : segment) {
								modifiableSequence.remove(e);
								tmpRawSequences.getModifiableUnusedElements().add(e);
							}

							// Increment the compulsory slot count to take into account solution change. Otherwise when inserting multiple slots, the first move has to insert all the slots at once.
							if (optionalElementsProvider.isElementRequired(element)) {
								++state.initialMetrics[MetricType.COMPULSARY_SLOT.ordinal()];
							}
						}
					}
					state.startingPointRawSequences = tmpRawSequences;
				}
			}

			final List<Future<Pair<ISequences, Long>>> futures = new LinkedList<>();
			try {
				for (int tryNo = 0; tryNo < tries; ++tryNo) {
					final int pTryNo = tryNo;

					futures.add(executorService.submit(() -> {
						if (monitor.isCanceled()) {
							return null;
						}
						try {
							// Bit nasty, but we are still in PoC stages

							final SlotInsertionOptimiser calculator = injector.getInstance(SlotInsertionOptimiser.class);
							return calculator.generate(optionElements, state, pTryNo);
						} finally {
							monitor.worked(1);
						}
					}));
				}
				final List<Pair<ISequences, Long>> results = new LinkedList<>();

				// Block until all futures completed
				for (final Future<Pair<ISequences, Long>> f : futures) {
					if (monitor.isCanceled()) {
						return null;
					}
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
				if (monitor.isCanceled()) {
					return null;
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
						.limit(500) //
						.map(r -> new NonNullPair<ISequences, Map<String, Object>>(r.getFirst(), new HashMap<>())) //
						.collect(Collectors.toList());

				solutions.add(0, new NonNullPair<ISequences, Map<String, Object>>(inputState.getBestSolution().getFirst(), new HashMap<>()));

				return new MultiStateResult(inputState.getBestSolution(), solutions);
			} finally {
				monitor.done();
			}

		} finally {
			final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);

			// Clean up thread-locals created in the scope object
			for (final Thread thread : threadCache.keySet()) {
				scope.exit(thread);
			}
			threadCache.clear();
		}
	}
}
