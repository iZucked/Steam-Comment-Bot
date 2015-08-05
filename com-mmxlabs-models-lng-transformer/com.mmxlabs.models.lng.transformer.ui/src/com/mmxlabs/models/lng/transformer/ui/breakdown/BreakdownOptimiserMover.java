package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class BreakdownOptimiserMover {
	public static final int DEPTH_START = -1;

	// Move types are for information only
	public static final int MOVE_TYPE_NONE = 0;
	private static final int MOVE_TYPE_VESSEL_SWAP = 1;
	private static final int MOVE_TYPE_LOAD_SWAP = 2;
	private static final int MOVE_TYPE_DISCHARGE_SWAP = 3;
	private static final int MOVE_TYPE_CARGO_REMOVE = 4;
	private static final int MOVE_TYPE_CARGO_INSERT = 5;
	private static final int MOVE_TYPE_UNUSED_DISCHARGE_SWAPPED = 6;
	private static final int MOVE_TYPE_UNUSED_LOAD_SWAPPED = 7;

	@Inject
	private ISequencesManipulator sequencesManipulator;

	@Inject
	private List<IConstraintChecker> constraintCheckers;

	@Inject
	private List<IEvaluationProcess> evaluationProcesses;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private IStartEndRequirementProvider startEndRequirementProvider;

	/**
	 * The size of the change sets. This is really n+1 changesets as 0 is also valid. Additionally note the first changeset can be +1 again due to the way we create the initial change set size.
	 * 
	 * TODO: Instead of try depth in the recursive method parameter, check the changes list size. (last attempt got stuck in a recursive loop for some reason, but may have been other bugs rather than
	 * directly from tryDepth == change.size().
	 */
	private final int TRY_DEPTH = 3;

	public Collection<JobState> search(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final int moveType, final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore) {
		@NonNull
		final List<JobState> newStates = new LinkedList<>();

		// Debugging quick exit, uncomment following line.
		if (true) {
			// return Collections.emptyList();
		}

		// Sanity check -- elements only used once.
		{
			final Set<ISequenceElement> unique = new HashSet<>();
			for (final IResource resource : currentSequences.getResources()) {
				final ISequence sequence = currentSequences.getSequence(resource.getIndex());
				for (final ISequenceElement current : sequence) {
					assert unique.add(current);
				}
			}
		}

		if (tryDepth >= 0) {
			final IModifiableSequences currentFullSequences = new ModifiableSequences(currentSequences);
			sequencesManipulator.manipulate(currentFullSequences);

			boolean failedEvaluation = false;

			// Apply hard constraint checkers
			for (final IConstraintChecker checker : constraintCheckers) {
				if (checker.checkConstraints(currentFullSequences) == false) {
					// Break out
					failedEvaluation = true;

					// String moveTypeStr = "";
					// switch (moveType) {
					// case 0:
					// moveTypeStr = "Initial";
					// break;
					// case 1:
					// moveTypeStr = "Vessel";
					// break;
					// case 2:
					// moveTypeStr = "Load";
					// break;
					// case 3:
					// moveTypeStr = "Discharge";
					// break;
					// }
					// System.out.printf("Failed eval after move type %s -- %s\n", moveTypeStr, checker.getName());

					break;
				}
			}

			if (!failedEvaluation) {
				long thisPNL = 0;
				long thisLateness = 0;
				if (true) {
					final IEvaluationState evaluationState = new EvaluationState();
					for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
						if (!evaluationProcess.evaluate(currentFullSequences, evaluationState)) {
							failedEvaluation = true;
							break;
						}
					}

					final ScheduledSequences ss = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
					assert ss != null;
					thisPNL = calculateSchedulePNL(currentFullSequences, ss);

					if (thisPNL <= currentPNL) {
						// failedEvaluation = true;
					} else {
						// currentPNL = thisPNL;
					}

					thisLateness = calculateScheduleLateness(currentFullSequences, ss);
					if (thisLateness > currentLateness) {
						failedEvaluation = true;
					} else {
						// currentLateness = thisLateness;
					}
				}
				if (!failedEvaluation) {
					// Convert change list set into a change set and record sate.
					// TOOD: Get fitness change and only accept improving solutions. (similarity, similarity plus others etc)
					final ChangeSet cs = new ChangeSet(changes);

					cs.pnlDelta = thisPNL - currentPNL;
					cs.latenessDelta = thisLateness - currentLateness;

					cs.pnlDeltaToBase = thisPNL - similarityState.basePNL;
					cs.latenessDeltaToBase = thisLateness - similarityState.baseLateness;

					changes.clear();
					changeSets.add(cs);

					JobState jobState = new JobState(new Sequences(currentSequences), changeSets, new LinkedList<Change>(), thisPNL, thisPNL - currentPNL, thisLateness, thisLateness - currentLateness,
							thisLateness - similarityState.basePNL, thisPNL - similarityState.baseLateness);

					final int changesCount = changedElements.size();
					if (changesCount == 0) {
						jobState.mode = JobStateMode.LEAF;
					}

					// Found a usable state, we no longer need to store limited states.
					jobStore.setFoundBranch();

					newStates.add(jobState);

					return newStates;

				}
			}
			if (failedEvaluation) {
				// Failed to to find valid state at the end of the search depth. Record a limited state and exit
				if (tryDepth == 0) {
					final JobState s = new JobState(new Sequences(currentSequences), changeSets, new LinkedList<Change>(changes), currentPNL, 0, currentLateness, 0, 0, 0);
					s.mode = JobStateMode.LIMITED;
					jobStore.store(s);
					return newStates;
				}
			}
		}

		int differenceCount = 0;
		boolean different = false;
		//
		for (final IResource resource : currentSequences.getResources()) {
			final ISequence sequence = currentSequences.getSequence(resource.getIndex());
			ISequenceElement prev = null;
			int prevIdx = -1;
			int currIdx = -1;
			for (final ISequenceElement current : sequence) {
				currIdx++;
				if (prev != null) {
					// Currently only looking at LD style cargoes
					if (portTypeProvider.getPortType(prev) == PortType.Load && portTypeProvider.getPortType(current) == PortType.Discharge) {
						// TODO Add to count changes
						// Wiring Change
						boolean wiringChange = false;
						final Integer matchedDischarge = similarityState.getDischargeForLoad(prev);
						final Integer matchedLoad = similarityState.getLoadForDischarge(current);
						if (matchedDischarge == null && matchedLoad == null) {
							// Neither slot appears in target solution.
							different = true;
							wiringChange = true;

							final IModifiableSequences copy = new ModifiableSequences(currentSequences);
							final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
							currentResource.remove(prev);
							currentResource.remove(current);
							copy.getUnusedElements().add(prev);
							copy.getUnusedElements().add(current);

							final int depth = getNextDepth(tryDepth);
							final List<Change> changes2 = new ArrayList<>(changes);
							changes2.add(new Change(String.format("Remove %s and %s\n", prev.getName(), current.getName())));
							newStates.addAll(search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentPNL, currentLateness, jobStore));

						} else if (matchedLoad == null && matchedDischarge != null) {
							// TODO Add to count changes
							// Discharge was previous unused, but the load is in correct solution
							different = true;
							wiringChange = true;

							// step (1) is the correct discharge in the unused? Swap it in
							if (currentSequences.getUnusedElements().contains(matchedDischarge)) {
								final ISequenceElement matchedDischargeElement = similarityState.getElementForIndex(matchedDischarge);
								final IModifiableSequences copy = new ModifiableSequences(currentSequences);
								final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
								copy.getUnusedElements().remove(matchedDischargeElement);
								currentResource.insert(currIdx, matchedDischargeElement);
								currentResource.remove(current);
								copy.getUnusedElements().add(current);
								final int depth = getNextDepth(tryDepth);
								final List<Change> changes2 = new ArrayList<>(changes);
								changes2.add(new Change(String.format("Remove discharge %s (unused in target solution) and insert discharge %s (unused in base solution)\n", current.getName(),
										matchedDischargeElement.getName())));
								newStates
										.addAll(search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_UNUSED_DISCHARGE_SWAPPED, currentPNL, currentLateness, jobStore));
							} else {
								// step (2) remove both slots
								// FIXME: Currently just unpair both slots and remove from solution
								final IModifiableSequences copy = new ModifiableSequences(currentSequences);
								final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
								currentResource.remove(prev);
								currentResource.remove(current);
								copy.getUnusedElements().add(prev);
								copy.getUnusedElements().add(current);

								final int depth = getNextDepth(tryDepth);
								final List<Change> changes2 = new ArrayList<>(changes);
								changes2.add(new Change(String.format("Remove %s and %s\n", prev.getName(), current.getName())));
								newStates.addAll(search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentPNL, currentLateness, jobStore));
							}
						} else if (matchedDischarge == null && matchedLoad != null) {

							// TODO Add to count changes
							// Load was previous unused, but the discharge was
							different = true;
							wiringChange = true;

							// step (1) is the correct load in the unused? Swap it in
							if (currentSequences.getUnusedElements().contains(matchedLoad)) {
								final ISequenceElement matchedLoadElement = similarityState.getElementForIndex(matchedLoad);
								final IModifiableSequences copy = new ModifiableSequences(currentSequences);
								final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
								copy.getUnusedElements().remove(matchedLoadElement);
								currentResource.insert(prevIdx, matchedLoadElement);
								currentResource.remove(prev);
								copy.getUnusedElements().add(prev);
								final int depth = getNextDepth(tryDepth);
								final List<Change> changes2 = new ArrayList<>(changes);
								changes2.add(new Change(
										String.format("Remove load %s (unused in target solution) and insert load %s (unused in base solution)\n", prev.getName(), matchedLoadElement.getName())));
								newStates.addAll(search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_UNUSED_LOAD_SWAPPED, currentPNL, currentLateness, jobStore));
							} else {
								// step (2)
								// FIXME: Currently just unpair both slots and remove from solution
								final IModifiableSequences copy = new ModifiableSequences(currentSequences);
								final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
								currentResource.remove(prev);
								currentResource.remove(current);
								copy.getUnusedElements().add(prev);
								copy.getUnusedElements().add(current);

								final int depth = getNextDepth(tryDepth);
								final List<Change> changes2 = new ArrayList<>(changes);
								changes2.add(new Change(String.format("Remove %s and %s\n", prev.getName(), current.getName())));
								newStates.addAll(search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentPNL, currentLateness, jobStore));
							}
						} else if (matchedDischarge != current.getIndex()) {
							different = true;
							wiringChange = true;
							differenceCount++;
							// Has the load moved vessel?
							if (similarityState.getResourceIdxForElement(prev).intValue() != resource.getIndex()) {
								// Hash the discharge moved vessel?
								if (similarityState.getResourceIdxForElement(current).intValue() != resource.getIndex()) {
									// Both load and discharge have moved

									// Search option 1, swap in original load for this discharge
									{
										// TODO: Remember DES purchases do not move
										newStates.addAll(swapLoad(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentPNL, currentLateness, jobStore));
									}
									// Case 2: Keep the load and swap in the original discharge
									{
										// TODO: Remember FOB Sales do not move
										newStates.addAll(
												swapDischarge(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentPNL, currentLateness, jobStore));
									}
								} else {
									// Just the load has moved.
									newStates.addAll(swapLoad(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentPNL, currentLateness, jobStore));
								}
							} else {
								// Load has stayed put, discharge must have moved.
								// Discharge stayed put
								newStates.addAll(swapDischarge(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentPNL, currentLateness, jobStore));
							}
						}

						// Vessel Change
						if (!wiringChange) {
							assert prev != null;
							if (similarityState.getResourceIdxForElement(prev) == null || similarityState.getResourceIdxForElement(prev).intValue() != resource.getIndex()) {
								different = true;
								differenceCount++;
								// Current Cargo load and discharge can move as a pair.
								if (similarityState.getResourceIdxForElement(prev).equals(similarityState.getResourceIdxForElement(current))) {
									// Find insertion point
									final ISequence originalResource = currentSequences.getSequence(similarityState.getResourceIdxForElement(prev));
									final ISequence currentResource = currentSequences.getSequence(resource.getIndex());

									// TODO: Create and apply change.
									for (int i = 0; i < currentResource.size(); ++i) {
										if (currentResource.get(i) == prev) {

											// Iterate over all possible positions and try it. Note we really could do with original index information to reduce the quantity of options generated. This
											// can get very long and quickly explodes the search space.
											for (int j : findInsertPoints(similarityState, originalResource, resource, prev, current)) {
												if (portTypeProvider.getPortType(originalResource.get(j)) != PortType.Discharge) {

													newStates.addAll(swapVessel(currentSequences, similarityState, changes, changeSets, tryDepth, currentPNL, currentLateness, resource, prev, current,
															j, jobStore));
												}
											}

											break;
										}
									}
								} else {
									// Part of a wiring change.
									// ^^^ above
								}
							}
						}

					}
				}
				prev = current;
				prevIdx = currIdx;
			}
		}

		// TODO Add to count changes

		// TODO Add to count changes
		Deque<ISequenceElement> unusedElements = new LinkedList<ISequenceElement>(currentFullSequences.getUnusedElements());
		while (unusedElements.size() > 0) {
			ISequenceElement element = unusedElements.pop();
			// Currently unused element needs to be placed onto a resource
			if (similarityState.getResourceIdxForElement(element) != null) {
				// This is an unused element which should be in the final solution.
				different = true;
				newStates.addAll(insertUnusedElementsIntoSequence(currentSequences, similarityState, changes, changeSets, tryDepth, element, currentPNL, currentLateness, unusedElements, jobStore));
			}
		}

		// FIXME: Also include alternative slots
		return newStates;
		//
		// if (different) {
		// // Still some (hopefully) correctable changes.
		// } else {
		//
		// // TODO: Combine this code with the section at the beginning?
		//
		// final int changesCount = changedElements.size();
		// if (changesCount > 0) {
		// // Can get here by short cutting the search above, avoiding setting different to true.
		// return newStates;
		// }
		//
		// // End of the line, nothing more we can do. Have we got to a valid state?
		// // We should have as we have no more changes. However the change count does not cover everything
		// // Exclusion include: Correct vessel, but incorrect position, any vessel event changes, open slot positions.
		// // Spurious changes: Same spot market & month, different instance.
		//
		// final IModifiableSequences currentFullSequences = new ModifiableSequences(currentSequences);
		// sequencesManipulator.manipulate(currentFullSequences);
		//
		// for (final IConstraintChecker checker : constraintCheckers) {
		// if (checker.checkConstraints(currentFullSequences) == false) {
		// // Break out -- could get here with bad vessel swap position.
		// return newStates;
		// }
		// }
		//
		// final IEvaluationState evaluationState = new EvaluationState();
		// for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
		// if (!evaluationProcess.evaluate(currentFullSequences, evaluationState)) {
		// // Ok, don't really expect to get here..
		// return newStates;
		// }
		// }
		//
		// // ... valid state, so we are in a leaf position.
		//
		// final ScheduledSequences ss = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
		// assert ss != null;
		// final long thisPNL = calculateSchedulePNL(currentFullSequences, ss);
		// final long thisLateness = calculateScheduleLateness(currentFullSequences, ss);
		//
		// // TODO: There should be no need really to create a new change set.
		// final ChangeSet cs = new ChangeSet(changes);
		//
		// cs.pnlDelta = thisPNL - currentPNL;
		// cs.latenessDelta = thisLateness - currentLateness;
		//
		// cs.pnlDeltaToBase = thisPNL - similarityState.basePNL;
		// cs.latenessDeltaToBase = thisLateness - similarityState.baseLateness;
		//
		// final ArrayList<ChangeSet> copiedChangeSets = new ArrayList<>(changeSets);
		// copiedChangeSets.add(cs);
		//
		// final JobState leafJobState = new JobState(new Sequences(currentSequences), copiedChangeSets, new LinkedList<Change>(), thisPNL, thisPNL - currentPNL, thisLateness,
		// thisLateness - currentLateness, thisLateness - similarityState.basePNL, thisPNL - similarityState.baseLateness);
		//
		// leafJobState.mode = JobStateMode.LEAF;
		// return Collections.singleton(leafJobState);
		// }
	}

	private Collection<Integer> findPerfectInsertPoint(SimilarityState similarityState, ISequence sequence, IResource resource, ISequenceElement insertingLoad, ISequenceElement insertingDischarge) {
		List<Integer> validPoints = new LinkedList<>();
		Pair<Integer, Integer> insertingCargo = new Pair<>(insertingLoad.getIndex(), insertingDischarge.getIndex());
		Pair<Integer, Integer> previousTargetCargo = similarityState.getPreviousCargo(insertingCargo);
		Pair<Integer, Integer> nextTargetCargo = similarityState.getNextCargo(insertingCargo);
		if (previousTargetCargo == null || nextTargetCargo == null) {
			// this cargo isn't in the target cargo
			return validPoints;
		}
		ISequenceElement prev = null;
		ISequenceElement current = null;
		Pair<Integer, Integer> prevCargo = new Pair<>(-2, -2); // start
		Pair<Integer, Integer> currCargo = new Pair<>(-1, -1); // end
		int prevSequenceIndex = -1;
		int currSequenceIndex = -1;
		for (int j = 1; j < sequence.size(); j++) {
			current = sequence.get(j);
			currSequenceIndex = j;
			if (prev != null) {
				if (portTypeProvider.getPortType(prev) == PortType.Load) {
					if (portTypeProvider.getPortType(current) == PortType.Discharge) {
						currCargo = new Pair<>(prev.getIndex(), current.getIndex());
						if (prevCargo.getSecond().equals(previousTargetCargo.getSecond()) && currCargo.getFirst().equals(nextTargetCargo.getFirst())) {
							validPoints.add(prevSequenceIndex);
							return validPoints;
						}
					}
				}
			}
			prev = current;
			prevCargo = currCargo;
			prevSequenceIndex = currSequenceIndex;
		}
		return validPoints;
	}

	private Collection<Integer> findInsertPoints(SimilarityState similarityState, ISequence sequence, IResource resource, ISequenceElement insertingLoad, ISequenceElement insertingDischarge) {
		// first see if this cargo will slot right in to the correct position
		Collection<Integer> perfectPoints = findPerfectInsertPoint(similarityState, sequence, resource, insertingLoad, insertingDischarge);
		if (!perfectPoints.isEmpty()) {
			// System.out.println("found perfect points");
			return perfectPoints;
		}
		int prevLoad = -1;
		ITimeWindow prevTimeWindow = null;
		int currLoad = -1;
		IPortSlot currPortSlot = null;
		ITimeWindow currTimeWindow = null;
		boolean consecutive = true;
		ITimeWindow insertingLoadTimeWindow = getTW(portSlotProvider.getPortSlot(insertingLoad), resource);
		LinkedHashSet<Integer> validPoints = new LinkedHashSet<Integer>();
		for (int j = 1; j < sequence.size(); ++j) {
			if (portTypeProvider.getPortType(sequence.get(j)) != PortType.Discharge) {
				validPoints.add(j);
			}
			if (portTypeProvider.getPortType(sequence.get(j)) == PortType.Load) {
				currLoad = j;
				currPortSlot = portSlotProvider.getPortSlot(sequence.get(j));
				currTimeWindow = getTW(currPortSlot, resource);
				if (prevLoad == -1) {
					continue;
				} else {
					prevLoad = currLoad;
					prevTimeWindow = currTimeWindow;
					assert prevTimeWindow != null;
					if (consecutive == true && prevTimeWindow.getStart() > currTimeWindow.getEnd()) {
						consecutive = false;
					}
					if (consecutive && prevTimeWindow.getEnd() < insertingLoadTimeWindow.getStart()) {
						// don't insert before this element
						validPoints.remove(prevLoad);
					}
				}
			}
		}
		return validPoints;
	}

	protected Collection<JobState> swapVessel(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final long currentPNL, final long currentLateness, @NonNull final IResource resource, @NonNull final ISequenceElement prev,
			@NonNull final ISequenceElement current, final int j, @NonNull final JobStore jobStore) {
		{

			if (similarityState.getResourceIdxForElement(prev).equals(resource.getIndex())) {
				assert false;
			}

			final IModifiableSequences copy = new ModifiableSequences(currentSequences);
			final IModifiableSequence modifiableSequence = copy.getModifiableSequence(similarityState.getResourceIdxForElement(prev));
			modifiableSequence.insert(j, current);
			modifiableSequence.insert(j, prev);
			copy.getModifiableSequence(resource.getIndex()).remove(prev);
			copy.getModifiableSequence(resource.getIndex()).remove(current);

			final int depth = getNextDepth(tryDepth);
			final List<Change> changes2 = new ArrayList<>(changes);
			changes2.add(new Change(String.format("Vessel %s from %s to %s\n", prev.getName(), resource.getName(), copy.getResources().get(similarityState.getResourceIdxForElement(prev)).getName())));

			if (copy.equals(currentSequences)) {
				// FIXME: Why do we get here?
				// return Collections.emptyList();
			}

			return search(copy, similarityState, changes2, new LinkedList<>(changeSets), depth, MOVE_TYPE_VESSEL_SWAP, currentPNL, currentLateness, jobStore);
		}
	}

	/**
	 * Given a load and discharge, assume the discharge is on the correct resource. Find the correct load for the discharge and swap it with the current load.
	 */
	protected Collection<JobState> swapLoad(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore) {

		if (!(portSlotProvider.getPortSlot(prev) instanceof ILoadSlot)) {
			return Collections.emptyList();
		}

		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());

		// Find the original load index
		final Integer originalLoadIdx = similarityState.getLoadForDischarge(current);
		assert originalLoadIdx != null;
		boolean swapped = false;

		// Find the original load element and swap with current load element
		ISequenceElement originalLoad = null;
		IResource otherResource = null;
		LOOP: for (final IResource r : copy.getResources()) {
			final IModifiableSequence s = copy.getModifiableSequence(r);

			for (int j = 0; j < s.size(); ++j) {
				if (s.get(j).getIndex() == originalLoadIdx.intValue()) {
					originalLoad = s.get(j);
					if (!(portSlotProvider.getPortSlot(originalLoad) instanceof ILoadSlot)) {
						return Collections.emptyList();
					}
					otherResource = r;
					s.set(j, prev);
					swapped = true;
					break LOOP;
				}
			}
		}
		assert otherResource != null;
		assert originalLoad != null;
		assert swapped;

		swapped = false;
		// Swap the current load element with the original one.
		for (int j = 0; j < currentResource.size(); ++j) {
			if (currentResource.get(j) == prev) {
				currentResource.set(j, originalLoad);
				swapped = true;
				break;
			}
		}

		// FIXME: GEtting a solution where we swap loads within a resource. Seems like discharge has not moved. However, my debuggin attempts do not show the load on this resource....
		if (copy.equals(currentSequences)) {
			// assert false;
		}
		if (resource.getIndex() == otherResource.getIndex()) {
			return Collections.emptyList();
		}

		assert swapped;
		assert prev != null;
		final int depth = getNextDepth(tryDepth);
		final List<Change> changes2 = new ArrayList<>(changes);
		changes2.add(new Change(String.format("Swap %s onto %s with %s onto %s\n", prev.getName(), otherResource.getName(), originalLoad.getName(), resource.getName())));
		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_LOAD_SWAP, currentPNL, currentLateness, jobStore);
	}

	/**
	 * Given a load and discharge, assume the load is on the correct resource. Find the correct discharge for the load and swap it with the current discharge.
	 */
	protected Collection<JobState> swapDischarge(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore) {

		if (!(portSlotProvider.getPortSlot(current) instanceof IDischargeSlot)) {
			return Collections.emptyList();
		}

		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());

		// Find the
		final int originalDischargeIdx = similarityState.getDischargeForLoad(prev);

		boolean swapped = false;
		ISequenceElement originalDischarge = null;
		ISequenceElement otherLoad = null;
		IResource otherResource = null;

		LOOP: for (final IResource r : copy.getResources()) {
			assert r != null;
			final IModifiableSequence s = copy.getModifiableSequence(r);

			for (int j = 0; j < s.size(); ++j) {
				if (s.get(j).getIndex() == originalDischargeIdx) {
					originalDischarge = s.get(j);
					if (!(portSlotProvider.getPortSlot(originalDischarge) instanceof IDischargeSlot)) {
						return Collections.emptyList();
					}
					otherLoad = s.get(j - 1);
					otherResource = r;
					s.set(j, current);
					swapped = true;
					break LOOP;
				}
			}
		}
		assert swapped;
		swapped = false;
		assert originalDischarge != null;
		assert otherLoad != null;
		assert otherResource != null;
		for (int j = 0; j < currentResource.size(); ++j) {
			if (currentResource.get(j) == current) {
				currentResource.set(j, originalDischarge);
				swapped = true;
				break;

			}
		}
		if (copy.equals(currentSequences)) {
			// assert false;
		}
		if (resource.getIndex() == otherResource.getIndex()) {
			return Collections.emptyList();
		}

		assert swapped;
		final int depth = getNextDepth(tryDepth);
		final List<Change> changes2 = new ArrayList<>(changes);
		changes2.add(new Change(String.format("Swap %s (to %s) with %s ( to %s)\n", current.getName(), otherLoad.getName(), originalDischarge.getName(), prev.getName())));
		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_DISCHARGE_SWAP, currentPNL, currentLateness, jobStore);
	}

	/**
	 * Completely remove a load and discharge from a Sequence
	 */
	protected Collection<JobState> removeElementsFromSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore) {

		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
		currentResource.remove(prev);
		currentResource.remove(current);
		copy.getUnusedElements().add(prev);
		copy.getUnusedElements().add(current);

		final int depth = getNextDepth(tryDepth);
		final List<Change> changes2 = new ArrayList<>(changes);
		changes2.add(new Change(String.format("Remove %s and %s\n", prev.getName(), current.getName())));
		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentPNL, currentLateness, jobStore);
	}

	/**
	 * Completely remove a load and discharge from a Sequence
	 */
	protected Collection<JobState> processOneHalfOfCargoUnused(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			@NonNull final ISequenceElement matchedElement, final int elementIdx, final long currentPNL, final long currentLateness, final boolean isLoadSwap, @NonNull final JobStore jobStore) {

		// (1) is the correct discharge in the unused? Swap it in
		if (currentSequences.getUnusedElements().contains(matchedElement)) {
			final IModifiableSequences copy = new ModifiableSequences(currentSequences);
			final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
			copy.getUnusedElements().remove(matchedElement);
			currentResource.insert(elementIdx, matchedElement);
			currentResource.remove(current);
			copy.getUnusedElements().add(current);
			final int depth = getNextDepth(tryDepth);
			final List<Change> changes2 = new ArrayList<>(changes);
			final int moveType;
			if (isLoadSwap) {
				changes2.add(new Change(String.format("Remove load %s (unused in target solution) and insert load %s (unused in base solution)\n", current.getName(), matchedElement.getName())));
				moveType = MOVE_TYPE_UNUSED_LOAD_SWAPPED;
			} else {
				changes2.add(
						new Change(String.format("Remove discharge %s (unused in target solution) and insert discharge %s (unused in base solution)\n", current.getName(), matchedElement.getName())));
				moveType = MOVE_TYPE_UNUSED_DISCHARGE_SWAPPED;
			}
			return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, moveType, currentPNL, currentLateness, jobStore);
		} else {
			// (2) remove both slots
			return removeElementsFromSequence(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentPNL, currentLateness, jobStore);
		}
	}

	protected Collection<JobState> insertUnusedElementsIntoSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final ISequenceElement element, final long currentPNL, final long currentLateness,
			Collection<ISequenceElement> unusedElements, @NonNull final JobStore jobStore) {
		if (portTypeProvider.getPortType(element) == PortType.Load) {
			Integer otherDischargeIdx = similarityState.getDischargeForLoad(element);
			ISequenceElement discharge = similarityState.getElementForIndex(otherDischargeIdx);
			// as moving as a pair, remove discharge from unusedElements queue that we're looping through
			unusedElements.remove(discharge);
			// move as pair
			if (currentSequences.getUnusedElements().contains(discharge)) {
				IResource resource = similarityState.getResourceForElement(element);
				return insertUnusedCargoIntoSequence(currentSequences, similarityState, changes, changeSets, tryDepth, resource, element, discharge, currentPNL, currentLateness, jobStore);
			}
		} else if (portTypeProvider.getPortType(element) == PortType.Discharge) {
			Integer otherLoadIdx = similarityState.getLoadForDischarge(element);
			// If we get here, the load is also unused
			ISequenceElement load = similarityState.getElementForIndex(otherLoadIdx);
			// as moving as a pair, remove discharge from unusedElements queue that we're looping through
			unusedElements.remove(load);
			// move as a pair
			if (currentSequences.getUnusedElements().contains(load)) {
				IResource resource = similarityState.getResourceForElement(element);
				return insertUnusedCargoIntoSequence(currentSequences, similarityState, changes, changeSets, tryDepth, resource, load, element, currentPNL, currentLateness, jobStore);
			} else {
				// Load already exists
			}
		} else {
			// assume vessel event?
		}
		return new LinkedList<JobState>();
	}

	// FIXME: Make sure different is counted in search()
	protected Collection<JobState> insertUnusedCargoIntoSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement load, @NonNull final ISequenceElement discharge,
			final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore) {
		ISequence originalResource = currentSequences.getSequence(similarityState.getResourceIdxForElement(load));
		for (int i : findInsertPoints(similarityState, originalResource, resource, load, discharge)) {
			if (portTypeProvider.getPortType(originalResource.get(i)) != PortType.Discharge) {
				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
				final IModifiableSequence modifiableSequence = copy.getModifiableSequence(similarityState.getResourceIdxForElement(load));
				modifiableSequence.insert(i, discharge);
				modifiableSequence.insert(i, load);

				final int depth = getNextDepth(tryDepth);
				final List<Change> changes2 = new ArrayList<>(changes);
				changes2.add(new Change(
						String.format("Insert cargo %s -> %s on %s\n", load.getName(), discharge.getName(), copy.getResources().get(similarityState.getResourceIdxForElement(load)).getName())));

				return search(copy, similarityState, changes2, new LinkedList<>(changeSets), depth, MOVE_TYPE_CARGO_INSERT, currentPNL, currentLateness, jobStore);
			}
		}
		return new LinkedList<JobState>();
	}

	public long calculateSchedulePNL(@NonNull final IModifiableSequences fullSequences, @NonNull final ScheduledSequences scheduledSequences) {
		long sumPNL = 0;

		for (final ScheduledSequence scheduledSequence : scheduledSequences) {
			for (final Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord> p : scheduledSequence.getVoyagePlans()) {
				sumPNL += scheduledSequences.getVoyagePlanGroupValue(p.getFirst());
			}
			for (final ISequenceElement element : fullSequences.getUnusedElements()) {
				final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
				assert portSlot != null;
				sumPNL += scheduledSequences.getUnusedSlotGroupValue(portSlot);
			}
		}
		return sumPNL;
	}

	public long calculateScheduleLateness(final IModifiableSequences fullSequences, final ScheduledSequences scheduledSequences) {
		long sumCost = 0;

		for (final IPortSlot lateSlot : scheduledSequences.getLateSlotsSet()) {
			sumCost += scheduledSequences.getLatenessCost(lateSlot).getSecond();
		}
		return sumCost;

	}

	private ITimeWindow getTW(IPortSlot portSlot, IResource resource) {
		ITimeWindow tw = null;

		if (portSlot instanceof StartPortSlot) {
			final IStartEndRequirement req = startEndRequirementProvider.getStartRequirement(resource);
			tw = req.getTimeWindow();
		} else if (portSlot instanceof EndPortSlot) {
			final IStartEndRequirement req = startEndRequirementProvider.getEndRequirement(resource);
			tw = req.getTimeWindow();
		} else {
			tw = portSlot.getTimeWindow();
		}
		return tw;
	}

	protected int getNextDepth(final int tryDepth) {
		assert tryDepth >= -1;
		return tryDepth == DEPTH_START ? TRY_DEPTH : tryDepth - 1;
	}

	public List<ISequenceElement> getChangedElements(final SimilarityState similarityState, final ISequences rawSequences) {

		final List<ISequenceElement> changedElements = new LinkedList<>();

		for (final IResource resource : rawSequences.getResources()) {
			final ISequence sequence = rawSequences.getSequence(resource.getIndex());
			ISequenceElement prev = null;
			for (final ISequenceElement current : sequence) {
				if (prev != null) {
					// Currently only looking at LD style cargoes
					if (portTypeProvider.getPortType(prev) == PortType.Load && portTypeProvider.getPortType(current) == PortType.Discharge) {
						// TODO Add to count changes
						// Wiring Change
						boolean wiringChange = false;
						final Integer matchedDischarge = similarityState.getDischargeForLoad(prev);
						final Integer matchedLoad = similarityState.getLoadForDischarge(current);
						if (matchedDischarge == null && matchedLoad == null) {
							changedElements.add(prev);
							changedElements.add(current);

						} else if (matchedLoad == null && matchedDischarge != null) {
							changedElements.add(current);
						} else if (matchedDischarge == null && matchedLoad != null) {
							wiringChange = true;
							changedElements.add(prev);
						} else if (matchedDischarge != current.getIndex()) {
							wiringChange = true;
							// Has the load moved vessel?
							if (similarityState.getResourceIdxForElement(prev).intValue() != resource.getIndex()) {
								// Hash the discharge moved vessel?
								if (similarityState.getResourceIdxForElement(current).intValue() != resource.getIndex()) {
									// Both load and discharge have moved
									changedElements.add(prev);
									changedElements.add(current);
								} else {
									changedElements.add(prev);
								}
							} else {
								changedElements.add(current);
							}
						}

						// Vessel Change
						if (!wiringChange) {
							assert prev != null;
							if (similarityState.getResourceIdxForElement(prev) == null || similarityState.getResourceIdxForElement(prev).intValue() != resource.getIndex()) {
								changedElements.add(prev);
								changedElements.add(current);
							}
						}

					} else {
						if (portTypeProvider.getPortType(current) == PortType.CharterOut || portTypeProvider.getPortType(current) == PortType.DryDock
								|| portTypeProvider.getPortType(current) == PortType.Maintenance) {

							if (similarityState.getResourceIdxForElement(current) == null || similarityState.getResourceIdxForElement(current).intValue() != resource.getIndex()) {
								changedElements.add(current);
							}
						}
					}
				}
				prev = current;
			}
		}

		final Deque<ISequenceElement> unusedElements = new LinkedList<ISequenceElement>(rawSequences.getUnusedElements());
		while (unusedElements.size() > 0) {
			final ISequenceElement element = unusedElements.pop();
			// Currently unused element needs to be placed onto a resource
			if (similarityState.getResourceIdxForElement(element) != null) {
				changedElements.add(element);
			}
		}
		return changedElements;
	}

}
