/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
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
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
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
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.actionplan.ChangeChecker;
import com.mmxlabs.scheduler.optimiser.actionplan.SimilarityState;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.insertion.SequencesHitchHikerHelper;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionEvaluator;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiser;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserInitialState;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;
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

	private @NonNull JobExecutorFactory jobExecutorFactory;

	private @NonNull InsertionOptimisationStage stage;

	@SuppressWarnings("null")
	public SlotInsertionOptimiserUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final InsertionOptimisationStage stage, @NonNull final JobExecutorFactory jobExecutorFactory, @NonNull final ISequences initialSequences,
			@NonNull final IMultiStateResult inputState, @NonNull final Collection<String> initialHints) {

		this.stage = stage;
		final Set<String> hints = new HashSet<>(initialHints);
		hints.add(LNGTransformerHelper.HINT_OPTIMISE_INSERTION);

		this.dataTransformer = dataTransformer;
		this.phase = phase;
		this.jobExecutorFactory = jobExecutorFactory;

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
			@ThreadLocalScope
			private EvaluationHelper provideEvaluationHelper(final Injector injector, @Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) final ISequences initialRawSequences) {

				EvaluationHelper helper = new EvaluationHelper();
				injector.injectMembers(helper);

				final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
				helper.acceptSequences(initialRawSequences, manipulator.createManipulatedSequences(initialRawSequences));

				helper.setFlexibleCapacityViolationCount(Integer.MAX_VALUE);
				return helper;
			}

			@Provides
			@ThreadLocalScope
			private SlotInsertionOptimiser providePerThreadOptimiser(@NonNull final Injector injector) {

				SlotInsertionOptimiser optimiser = new SlotInsertionOptimiser();
				injector.injectMembers(optimiser);
				return optimiser;
			}

			@Provides
			@ThreadLocalScope
			private SlotInsertionEvaluator providePerThreadEvaluator(@NonNull final Injector injector) {

				SlotInsertionEvaluator optimiser = new SlotInsertionEvaluator();
				injector.injectMembers(optimiser);
				return optimiser;
			}
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.inputState = inputState;
	}

	public IMultiStateResult run(final @NonNull List<Slot<?>> slotsToInsert, final List<VesselEvent> eventsToInsert, SlotInsertionOptimiserLogger logger, @NonNull final IProgressMonitor monitor) {
		return run(slotsToInsert, eventsToInsert, stage.getIterations(), logger, monitor);
	}

	public IMultiStateResult run(final @NonNull List<Slot<?>> slotsToInsert, final List<VesselEvent> eventsToInsert, final int tries, @Nullable SlotInsertionOptimiserLogger logger,
			@NonNull final IProgressMonitor monitor) {

		final JobExecutorFactory subExecutorFactory = jobExecutorFactory.withDefaultBegin(() -> {
			final ThreadLocalScopeImpl s = injector.getInstance(ThreadLocalScopeImpl.class);
			s.enter();
			return s;
		});

		try (JobExecutor jobExecutor = subExecutorFactory.begin()) {
			if (logger != null) {
				logger.begin();
			}

			@NonNull
			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final List<IPortSlot> slotElements = slotsToInsert.stream() //
					.map(s -> modelEntityMap.getOptimiserObjectNullChecked(s, IPortSlot.class)) //
					.collect(Collectors.toList());
			final List<IPortSlot> eventElements = eventsToInsert.stream() //
					.map(s -> modelEntityMap.getOptimiserObjectNullChecked(s, IPortSlot.class)) //
					.collect(Collectors.toList());

			final List<IPortSlot> optionElements = new ArrayList<>(slotElements.size() + eventElements.size());
			optionElements.addAll(slotElements);
			optionElements.addAll(eventElements);

			monitor.beginTask("Generate solutions", tries);

			final SlotInsertionOptimiserInitialState state = new SlotInsertionOptimiserInitialState();
			final List<Pair<ISequenceElement, ISequenceElement>> nonShippedPairs = new LinkedList<>();
			final Map<ISequenceElement, Pair<IResource, List<ISequenceElement>>> initialAllocation = new HashMap<>();
			{
				final IPhaseOptimisationData phaseOptimisationData = injector.getInstance(IPhaseOptimisationData.class);
				final IMoveHandlerHelper moveHandlerHelper = injector.getInstance(IMoveHandlerHelper.class);
				final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);

				// Calculate the initial metrics and slot pairings.
				try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
					scope.enter();
					final IFollowersAndPreceders followersAndPreceders = injector.getInstance(IFollowersAndPreceders.class);

					{
						final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
						final EvaluationHelper evaluationHelper = injector.getInstance(EvaluationHelper.class);
						final ISequences initialRawSequences = injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)));

						if (!SequencesHitchHikerHelper.checkValidSequences(initialRawSequences)) {
							throw new UserFeedbackException("Unable to optionise this scenario. Initial sequences are invalid. Please report issue to Minimax Labs.");
						}

						state.initialMetrics = evaluationHelper.evaluateState(initialRawSequences, manipulator.createManipulatedSequences(initialRawSequences), null, true, true, null, null);
						if (state.initialMetrics == null) {
							throw new UserFeedbackException("Unable to optionise this scenario. This is most likely caused by late and overlapping cargoes. Please check validation messages.");
						}
						state.originalRawSequences = initialRawSequences;
						state.lookupManager = new LookupManager();
						state.lookupManager.createLookup(initialRawSequences);

						// Record the initial vessel & cargo pairing
						for (final IPortSlot portSlot : optionElements) {
							assert portSlot != null;

							final ISequenceElement e = portSlotProvider.getElement(portSlot);
							final Pair<@Nullable IResource, @NonNull Integer> lookup = state.lookupManager.lookup(e);
							final IResource resource = lookup.getFirst();
							if (lookup == null || resource == null) {
								initialAllocation.put(e, null);
							} else {
								initialAllocation.put(e, Pair.of(resource, moveHandlerHelper.extractSegment(initialRawSequences.getSequence(resource), e)));
							}
						}
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
							assert portSlot != null;
							final ISequenceElement element = portSlotProvider.getElement(portSlot);

							final @Nullable Pair<IResource, Integer> lookup = lookupManager.lookup(element);
							if (lookup != null && lookup.getFirst() != null) {

								final @NonNull IModifiableSequence modifiableSequence = tmpRawSequences.getModifiableSequence(lookup.getFirst());
								final @NonNull List<ISequenceElement> segment = moveHandlerHelper.extractSegment(modifiableSequence, element);
								for (final ISequenceElement e : segment) {
									assert e != null;
									modifiableSequence.remove(e);
									tmpRawSequences.getModifiableUnusedElements().add(e);

									// Increment the compulsory slot count to take into account solution change.
									// Otherwise when inserting multiple slots, the first move has to insert all the
									// slots at once.
									if (phaseOptimisationData.isElementRequired(e)) {
										++state.initialMetrics[MetricType.COMPULSARY_SLOT.ordinal()];
									}
								}
								lookupManager.updateLookup(tmpRawSequences, lookup.getFirst(), null);

							}
						}

						// Make sure option elements are properly hooked into the sequences
						for (final IPortSlot portSlot : optionElements) {
							final ISequenceElement e = portSlotProvider.getElement(portSlot);

							final var lookup = lookupManager.lookup(e);
							if (lookup == null) {
								tmpRawSequences.getModifiableUnusedElements().add(e);
								lookupManager.updateLookup(tmpRawSequences, null, null);
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
								throw new UserFeedbackException(String.format("Unable to optionise this scenario. This is caused by the slot %s having no possible pairings.", slotName));
							}
						}
					}

					// Find all possible valid non-shipped pairs
					for (final IPortSlot portSlot : slotElements) {
						final ISequenceElement element = portSlotProvider.getElement(portSlot);
						if (portSlot instanceof ILoadOption) {
							final boolean isFOBPurchase = portSlot instanceof ILoadSlot;
							final Followers<ISequenceElement> validFollowers = followersAndPreceders.getValidFollowers(element);
							for (final ISequenceElement follower : validFollowers) {
								{
									// Make sure this is an unused option
									var l = state.lookupManager.lookup(follower);
									if (l != null && l.getFirst() != null) {
										continue;
									}
								}

								final IPortSlot followerSlot = portSlotProvider.getPortSlot(follower);
								if (followerSlot instanceof IDischargeOption dischargeOption) {
									final boolean isDESSale = dischargeOption instanceof IDischargeSlot;
									if (isFOBPurchase && !isDESSale) {
										nonShippedPairs.add(new Pair<>(element, follower));
									} else if (!isFOBPurchase && isDESSale) {
										nonShippedPairs.add(new Pair<>(element, follower));
									}
								}
							}
						} else if (portSlot instanceof IDischargeOption) {
							final boolean isDESSale = portSlot instanceof IDischargeSlot;
							final Followers<ISequenceElement> validPreceders = followersAndPreceders.getValidPreceders(element);
							for (final ISequenceElement preceder : validPreceders) {
								{
									// Make sure this is an unused option
									var l = state.lookupManager.lookup(preceder);
									if (l != null && l.getFirst() != null) {
										continue;
									}
								}
								final IPortSlot precederSlot = portSlotProvider.getPortSlot(preceder);
								if (precederSlot instanceof ILoadOption loadOption) {
									final boolean isFOBPurchase = loadOption instanceof ILoadSlot;
									if (isFOBPurchase && !isDESSale) {
										nonShippedPairs.add(new Pair<>(preceder, element));
									} else if (!isFOBPurchase && isDESSale) {
										nonShippedPairs.add(new Pair<>(preceder, element));
									}
								}
							}
						}
					}
				}
			}

			final List<Future<Pair<ISequences, Long>>> futures = new LinkedList<>();
			List<Pair<ISequences, Long>> results = new LinkedList<>();

			// Step 1: Exhaustive search of non-shipped pairs
			try {
				{
					if (logger != null) {
						logger.beginStage(SlotInsertionOptimiserLogger.STAGE_NON_SHIPPED_PAIRS);
					}
					for (final Pair<ISequenceElement, ISequenceElement> p : nonShippedPairs) {
						final ISequenceElement buy = p.getFirst();
						final ISequenceElement sell = p.getSecond();

						futures.add(jobExecutor.submit(() -> {
							if (monitor.isCanceled()) {
								return null;
							}
							try {
								final SlotInsertionEvaluator calculator = injector.getInstance(SlotInsertionEvaluator.class);
								return calculator.insert(state, buy, sell);
							} catch (final Exception e) {
								e.printStackTrace();
							} finally {
								monitor.worked(1);
							}
							return null;
						}));
					}
					if (logger != null) {
						logger.doneStage(SlotInsertionOptimiserLogger.STAGE_NON_SHIPPED_PAIRS);
					}
				}
				{
					// Ensure the non-shipped part has completed before we go onto the next stage.
					// It is *possible* (but not certain) that there is some unexpected interaction
					// between the solutions in the stages. I've (SG) added this when trying to work
					// out why an element can be inserted twice. (gitlab #216)

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
					futures.clear();
				}
				// Step 2: full search
				{
					if (logger != null) {
						logger.beginStage(SlotInsertionOptimiserLogger.STAGE_FULL_SEARCH);
					}
					for (int tryNo = 0; tryNo < tries; ++tryNo) {
						final int pTryNo = tryNo;

						futures.add(jobExecutor.submit(() -> {
							if (monitor.isCanceled()) {
								return null;
							}
							try {
								final SlotInsertionOptimiser calculator = injector.getInstance(SlotInsertionOptimiser.class);
								final @Nullable Pair<ISequences, Long> result = calculator.generate(optionElements, state, pTryNo);

								// Sometimes due to spot slot equivalence we can obtain solutions which do not
								// change the target elements.
								if (result != null) {
									final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
									final IMoveHandlerHelper moveHandlerHelper = injector.getInstance(IMoveHandlerHelper.class);

									final LookupManager lookupManager = injector.getInstance(LookupManager.class);
									lookupManager.createLookup(result.getFirst());

									boolean isOneTargetDifferent = false;

									// Compare current paring and vessel to initial state.
									for (final IPortSlot portSlot : optionElements) {
										final ISequenceElement e = portSlotProvider.getElement(portSlot);
										final Pair<@Nullable IResource, @NonNull Integer> lookup = lookupManager.lookup(e);
										final IResource resource = lookup.getFirst();
										if (lookup == null || resource == null) {
											if (initialAllocation.get(e) != null) {
												// Slot is unused, when it was originally used
												isOneTargetDifferent = true;
												break;
											}
											initialAllocation.put(e, null);
										} else {
											if (initialAllocation.get(e) == null) {
												// Slot was unused, and now it is used
												isOneTargetDifferent = true;
												break;
											}
											if (resource != initialAllocation.get(e).getFirst()) {
												// Slot is on a different vessel
												isOneTargetDifferent = true;
												break;
											}
											if (!Objects.equals( //
													moveHandlerHelper.extractSegment(result.getFirst().getSequence(resource), e), //
													initialAllocation.get(e).getSecond() //
											)) {
												// Slot is paired differently
												isOneTargetDifferent = true;
												break;
											}
										}
									}
									if (!isOneTargetDifferent) {
										return null;
									}
								}
								return result;
							} catch (final Exception e) {
								e.printStackTrace();
							} finally {
								monitor.worked(1);
							}
							return null;
						}));
					}
				}

				// Block until all futures completed
				int i = 0;

				for (final Future<Pair<ISequences, Long>> f : futures) {
					if (monitor.isCanceled()) {
						return null;
					}
					try {
						final Pair<ISequences, Long> s = f.get();

						if (logger != null) {

							if (++i % logger.getLoggingInterval() == 0) {
								logger.logCurrentMemoryUsage(String.format("Full search iteration %d", i));
							}
						}
						if (s != null) {
							results.add(s);
						}
					} catch (final InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
				if (logger != null) {
					logger.doneStage(SlotInsertionOptimiserLogger.STAGE_FULL_SEARCH);
				}

				if (monitor.isCanceled()) {
					return null;
				}
				if (results.isEmpty()) {
					throw new UserFeedbackException("No feasible solutions found.");
				}
				if (logger != null) {

					logger.beginStage(SlotInsertionOptimiserLogger.STAGE_PROCESS_SOLUTIONS);
				}
				// Reduce result to unique solutions
				results = results.stream().distinct().toList();

				final int maxSize = 300;
				final List<NonNullPair<ISequences, Map<String, Object>>> solutions;
				if (results.size() > maxSize) {
					final SimilarityState initialSimilarityState = injector.getInstance(SimilarityState.class);
					initialSimilarityState.init(state.originalRawSequences);

					final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
					// Categories solutions
					final Map<Record, List<Pair<ISequences, Long>>> m = results.stream() //
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
							}, LinkedHashMap::new, Collectors.toList()));
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
								solutions.add(new NonNullPair<>(l.remove(0).getFirst(), new HashMap<>()));
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

				if (logger != null) {
					logger.setSolutionsFound(solutions.size());
				}

				solutions.add(0, new NonNullPair<>(inputState.getBestSolution().getFirst(), new HashMap<>()));

				if (logger != null) {
					logger.doneStage(SlotInsertionOptimiserLogger.STAGE_PROCESS_SOLUTIONS);
				}
				return new MultiStateResult(inputState.getBestSolution(), solutions);
			} finally {
				monitor.done();
				if (logger != null) {
					logger.done();
				}
			}
		}
	}

	private static class Record {
		private int complexity;
		private List<ISequenceElement> linkedTo = new LinkedList<>();

		@Override
		public int hashCode() {
			return Objects.hash(complexity, linkedTo);
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof Record other) {
				return complexity == other.complexity //
						&& linkedTo.equals(other.linkedTo) //
				;
			}
			return false;
		}
	}
}
