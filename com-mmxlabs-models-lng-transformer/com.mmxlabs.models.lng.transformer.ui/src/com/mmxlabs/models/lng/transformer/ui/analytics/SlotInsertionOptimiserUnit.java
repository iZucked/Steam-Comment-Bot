/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.InsertionOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
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
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.actionplan.ChangeChecker;
import com.mmxlabs.scheduler.optimiser.actionplan.SimilarityState;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiser;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserInitialState;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveGeneratorModule;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.Followers;
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

	private final Map<Thread, SlotInsertionOptimiser> threadCache_SlotInsertionOptimiser = new ConcurrentHashMap<>(100);
	private final Map<Thread, EvaluationHelper> threadCache_EvaluationHelper = new ConcurrentHashMap<>(100);

	private @NonNull ExecutorService executorService;

	private @NonNull InsertionOptimisationStage stage;

	@SuppressWarnings("null")
	public SlotInsertionOptimiserUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final InsertionOptimisationStage stage, @NonNull final ExecutorService executorService, @NonNull final ISequences initialSequences, @NonNull final IMultiStateResult inputState,
			@NonNull final Collection<String> initialHints) {

		this.stage = stage;
		final Set<String> hints = new HashSet<>(initialHints);
		hints.add(LNGTransformerHelper.HINT_OPTIMISE_INSERTION);

		this.dataTransformer = dataTransformer;
		this.phase = phase;
		this.executorService = executorService;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
		modules.add(new InitialPhaseOptimisationDataModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, stage.getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

		modules.add(new AbstractModule() {
			@Override
			protected void configure() {
				install(new MoveGeneratorModule(true));
			}

			@Provides
			private EvaluationHelper provideEvaluationHelper(final Injector injector, @Named(LNGParameters_EvaluationSettingsModule.OPTIMISER_REEVALUATE) final boolean isReevaluating,
					@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) final ISequences initialRawSequences) {

				EvaluationHelper helper = threadCache_EvaluationHelper.get(Thread.currentThread());
				if (helper == null) {
					helper = new EvaluationHelper(isReevaluating);
					injector.injectMembers(helper);

					final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
					helper.acceptSequences(initialRawSequences, manipulator.createManipulatedSequences(initialRawSequences));

					helper.setFlexibleCapacityViolationCount(Integer.MAX_VALUE);
					threadCache_EvaluationHelper.put(Thread.currentThread(), helper);
				}
				return helper;
			}

			@Provides
			private SlotInsertionOptimiser providePerThreadOptimiser(@NonNull final Injector injector) {

				SlotInsertionOptimiser optimiser = threadCache_SlotInsertionOptimiser.get(Thread.currentThread());
				if (optimiser == null) {
					final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);
					scope.enter();
					optimiser = new SlotInsertionOptimiser();
					injector.injectMembers(optimiser);
					threadCache_SlotInsertionOptimiser.put(Thread.currentThread(), optimiser);
				}
				return optimiser;
			}
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.inputState = inputState;
	}

	public IMultiStateResult run(final @NonNull List<Slot> slotsToInsert, final List<VesselEvent> eventsToInsert, @NonNull final IProgressMonitor monitor) {
		return run(slotsToInsert, eventsToInsert, stage.getIterations(), monitor);
	}

	public IMultiStateResult run(final @NonNull List<Slot> slotsToInsert, final List<VesselEvent> eventsToInsert, final int tries, @NonNull final IProgressMonitor monitor) {
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

			final List<IPortSlot> optionElements = new ArrayList<IPortSlot>(slotElements.size() + eventElements.size());
			optionElements.addAll(slotElements);
			optionElements.addAll(eventElements);

			monitor.beginTask("Generate solutions", tries);

			final SlotInsertionOptimiserInitialState state = new SlotInsertionOptimiserInitialState();
			{
				final IPhaseOptimisationData phaseOptimisationData = injector.getInstance(IPhaseOptimisationData.class);
				final IMoveHandlerHelper moveHandlerHelper = injector.getInstance(IMoveHandlerHelper.class);
				final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);

				// Calculate the initial metrics
				try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
					scope.enter();
					final IFollowersAndPreceders followersAndPreceders = injector.getInstance(IFollowersAndPreceders.class);

					{
						final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
						final EvaluationHelper evaluationHelper = injector.getInstance(EvaluationHelper.class);
						final ISequences initialRawSequences = injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)));
						state.initialMetrics = evaluationHelper.evaluateState(initialRawSequences, manipulator.createManipulatedSequences(initialRawSequences), null, true, null, null);
						if (state.initialMetrics == null) {
							throw new UserFeedbackException(
									"Unable to perform insertion on this scenario. This is most likely caused by late and overlapping cargoes. Please check validation messages.");
						}
						state.originalRawSequences = initialRawSequences;
						state.lookupManager = new LookupManager();// .getInstance(ILookupManager.class);
						state.lookupManager.createLookup(initialRawSequences);
					}

					{
						final IModifiableSequences tmpRawSequences = new ModifiableSequences(state.originalRawSequences);

						for (final ISequenceElement e : tmpRawSequences.getUnusedElements()) {
							if (phaseOptimisationData.isElementRequired(e) || phaseOptimisationData.getSoftRequiredElements().contains(e)) {
								state.initiallyUnused.add(e);
							}
						}

						// Makes sure target slots are not contained in the solution.
						final LookupManager lookupManager = new LookupManager(tmpRawSequences);
						for (final IPortSlot portSlot : optionElements) {
							final ISequenceElement element = portSlotProvider.getElement(portSlot);

							final @Nullable Pair<IResource, Integer> lookup = lookupManager.lookup(element);
							if (lookup != null && lookup.getFirst() != null) {
								@NonNull
								final IModifiableSequence modifiableSequence = tmpRawSequences.getModifiableSequence(lookup.getFirst());
								@NonNull
								final List<ISequenceElement> segment = moveHandlerHelper.extractSegment(modifiableSequence, element);
								for (final ISequenceElement e : segment) {
									modifiableSequence.remove(e);
									tmpRawSequences.getModifiableUnusedElements().add(e);

									// Increment the compulsory slot count to take into account solution change. Otherwise when inserting multiple slots, the first move has to insert all the slots
									// at
									// once.
									if (phaseOptimisationData.isElementRequired(e)) {
										++state.initialMetrics[MetricType.COMPULSARY_SLOT.ordinal()];
									}
								}
								lookupManager.updateLookup(tmpRawSequences, lookup.getFirst(), null);

							}
						}
						state.startingPointRawSequences = tmpRawSequences;
					}

					// check for insertion feasibility
					{
						boolean isFeasible = true;
						for (final IPortSlot portSlot : optionElements) {
							final ISequenceElement element = portSlotProvider.getElement(portSlot);
							String slotName = "";
							if (portSlot instanceof ILoadOption) {
								final Followers<ISequenceElement> validFollowers = followersAndPreceders.getValidFollowers(element);
								isFeasible = validFollowers.size() >= 1;
								final LoadSlot load = modelEntityMap.getModelObject(portSlot, LoadSlot.class);
								slotName = load.getName();
							} else if (portSlot instanceof IDischargeOption) {
								final Followers<ISequenceElement> validPreceders = followersAndPreceders.getValidPreceders(element);
								final DischargeSlot discharge = modelEntityMap.getModelObject(portSlot, DischargeSlot.class);
								slotName = discharge.getName();
								isFeasible = validPreceders.size() >= 1;
							}
							if (!isFeasible) {
								throw new UserFeedbackException(String.format("Unable to perform insertion on this scenario. This is caused by the slot %s having no possible pairings.", slotName));
							}
						}
					}
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
						} catch (final Exception e) {
							e.printStackTrace();
						} finally {
							monitor.worked(1);
						}
						return null;
					}));
				}
				List<Pair<ISequences, Long>> results = new LinkedList<>();

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
					} catch (final InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
				if (monitor.isCanceled()) {
					return null;
				}

				// Reduce result to unique solutions
				results = results.parallelStream().distinct().collect(Collectors.toList());

				final int maxSize = 300;
				final List<NonNullPair<ISequences, Map<String, Object>>> solutions;
				if (results.size() > maxSize) {
					final SimilarityState initialSimilarityState = injector.getInstance(SimilarityState.class);
					initialSimilarityState.init(state.originalRawSequences);

					final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
					// Categories solutions
					final Map<Record, List<Pair<ISequences, Long>>> m = results.parallelStream().distinct() //
							.collect(Collectors.groupingBy(p -> {
								final Record record = new Record();

								final SimilarityState thisSimilarityState = injector.getInstance(SimilarityState.class);
								thisSimilarityState.init(p.getFirst());

								for (final IPortSlot s : slotElements) {
									final ISequenceElement e = portSlotProvider.getElement(s);
									if (e instanceof ILoadOption) {
										final ISequenceElement other = thisSimilarityState.getDischargeElementForLoad(e);
										record.linkedTo.add(other);
									} else {
										final ISequenceElement other = thisSimilarityState.getLoadElementForDischarge(e);
										record.linkedTo.add(other);
									}
								}
								// TODO: Vessel events
								final ChangeChecker checker = injector.getInstance(ChangeChecker.class);
								checker.init(initialSimilarityState, thisSimilarityState, state.originalRawSequences);
								record.complexity = checker.getFullDifferences().size();
								return record;
							}));
					// ,

					// Sort
					m.values().forEach(l -> {
						Collections.sort(l, (a, b) -> {
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
					});

					solutions = new ArrayList<>(Math.min(maxSize, results.size()));
					while (solutions.size() < maxSize && !m.isEmpty()) {
						final Iterator<Map.Entry<Record, List<Pair<ISequences, Long>>>> itr = m.entrySet().iterator();
						while (itr.hasNext()) {
							final List<Pair<ISequences, Long>> l = itr.next().getValue();
							if (!l.isEmpty()) {
								solutions.add(new NonNullPair<ISequences, Map<String, Object>>(l.remove(0).getFirst(), new HashMap<>()));
								if (solutions.size() == maxSize) {
									break;
								}
							}
							if (l.isEmpty()) {
								itr.remove();
							}
						}
					}
				} else {
					solutions = results.stream() //
							.sorted((a, b) -> {
								final long al = a.getSecond();
								final long bl = b.getSecond();
								if (al > bl) {
									return -1;
								} else if (al < bl) {
									return 1;
								} else {
									return 0;
								}
							}).map(r -> new NonNullPair<ISequences, Map<String, Object>>(r.getFirst(), new HashMap<>())) //
							.collect(Collectors.toList());
				}

				if (solutions.size() < results.size()) {
					System.out.printf("Found %d insertion options - clamped to %d.\n", results.size(), solutions.size());
				} else {
					System.out.printf("Found %d insertion options.\n", solutions.size());
				}

				solutions.add(0, new NonNullPair<ISequences, Map<String, Object>>(inputState.getBestSolution().getFirst(), new HashMap<>()));

				return new MultiStateResult(inputState.getBestSolution(), solutions);
			} finally {
				monitor.done();
			}

		} finally {
			final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);

			// Clean up thread-locals created in the scope object
			for (final Thread thread : threadCache_SlotInsertionOptimiser.keySet()) {
				scope.exit(thread);
			}
			threadCache_SlotInsertionOptimiser.clear();

			threadCache_EvaluationHelper.clear();
		}
	}

	private static class Record {
		int complexity;
		List<ISequenceElement> linkedTo = new LinkedList<>();

		@Override
		public int hashCode() {
			return Objects.hash(complexity, linkedTo);
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof Record) {
				final Record other = (Record) obj;
				return complexity == other.complexity //
						&& linkedTo.equals(other.linkedTo) //
				;
			}
			return false;
		}
	}
}
