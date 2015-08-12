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
import java.util.Random;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeChecker.DifferenceType;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
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
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class BagMover extends BreakdownOptimiserMover {
//	Random rdm = new Random(0);
//	private int depthStart = 1;
//	private int depthEnd = 8;
//	@Override
//	public Collection<JobState> search(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
//			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final int moveType, final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore, List<Difference> differencesList) {
//		final List<JobState> newStates = new LinkedList<>();
//
//		final IModifiableSequences currentFullSequences = new ModifiableSequences(currentSequences);
//		sequencesManipulator.manipulate(currentFullSequences);
//
//		// Sanity check -- elements only used once.
//		{
//			final Set<ISequenceElement> unique = new HashSet<>();
//			for (final IResource resource : currentSequences.getResources()) {
//				final ISequence sequence = currentSequences.getSequence(resource.getIndex());
//				for (final ISequenceElement current : sequence) {
////					System.out.println(String.format("%s|%s",resource.getName(),current.getName()));
//					assert unique.add(current);
//				}
//			}
//		}
//
//		if (tryDepth == 0) {
//
//			boolean failedEvaluation = false;
//
//			// Apply hard constraint checkers
//			for (final IConstraintChecker checker : constraintCheckers) {
//				if (checker.checkConstraints(currentFullSequences) == false) {
//					// Break out
//					failedEvaluation = true;
//
//					break;
//				}
//			}
//
//			IEvaluationState evaluationState = null;
//			if (!failedEvaluation) {
//				long thisPNL = 0;
//				long thisLateness = 0;
//				if (true) {
//					evaluationState = new EvaluationState();
//					for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
//						if (!evaluationProcess.evaluate(currentFullSequences, evaluationState)) {
//							failedEvaluation = true;
//							break;
//						}
//					}
//
//					final ScheduledSequences ss = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
//					assert ss != null;
//					thisPNL = calculateSchedulePNL(currentFullSequences, ss);
//
//					if (thisPNL <= currentPNL) {
//						// failedEvaluation = true;
//					} else {
//						// currentPNL = thisPNL;
//					}
//
//					thisLateness = calculateScheduleLateness(currentFullSequences, ss);
//					if (thisLateness > currentLateness) {
//						failedEvaluation = true;
//					} else {
//						// currentLateness = thisLateness;
//					}
//				}
//				if (!failedEvaluation) {
//					// Convert change list set into a change set and record sate.
//					// TOOD: Get fitness change and only accept improving solutions. (similarity, similarity plus others etc)
//					final ChangeSet cs = new ChangeSet(changes);
//
//					cs.pnlDelta = thisPNL - currentPNL;
//					cs.latenessDelta = thisLateness - currentLateness;
//					
//					// Store Changeset info for creating scenarios later
//					cs.setRawSequences(currentSequences);
//					cs.setFullSequences(currentFullSequences);
//					
//					changes.clear();
//					changeSets.add(cs);
//					JobState js = new JobState(new Sequences(currentSequences), changeSets, new LinkedList<Change>(), thisPNL, thisPNL - currentPNL, thisLateness, thisLateness - currentLateness, differencesList);
//					newStates.add(js);
//
//					return newStates;
//
//				}
//			}
//			if (failedEvaluation) {
//				// if (changes.size() == TRY_DEPTH) {
//				// return newStates;
//				// }
//				if (tryDepth == 0) { // changes.size() == TRY_DEPTH) {
//					final JobState s = new JobState(new Sequences(currentSequences), changeSets, new LinkedList<Change>(changes), currentPNL, 0, currentLateness, 0);
//					s.mode = JobStateMode.LIMITED;
//
////					jobStore.store(s);
//
//					return newStates;
//				}
//			}
//		}
//		if (differencesList.size() > 0) {
//		// (1) Choose a difference
//		Difference difference = pickRandomElementFromList(differencesList, rdm);
//		while (difference.move != DifferenceType.CARGO_WRONG_WIRING && difference.move != DifferenceType.CARGO_WRONG_VESSEL) {
//			difference = pickRandomElementFromList(differencesList, rdm);
//		}
//		// (2) Fix a difference
//		if (difference.move == DifferenceType.CARGO_WRONG_WIRING) {
//			if (differencesList.contains(new Difference(DifferenceType.LOAD_WRONG_VESSEL, difference.load, null, difference.resource))
//					&& differencesList.contains(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, difference.discharge, difference.resource))) {
//				// either do a load swap or a discharge swap
//				if (rdm.nextBoolean()) {
//					// load
//					newStates.addAll(swapLoad(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge, currentPNL,
//							currentLateness, jobStore, ChangeChecker.copyDifferenceList(differencesList)));
//				} else {
//					// discharge
//					newStates.addAll(swapDischarge(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge, currentPNL,
//							currentLateness, jobStore, ChangeChecker.copyDifferenceList(differencesList)));
//				}
//
//			} else if (differencesList.contains(new Difference(DifferenceType.LOAD_WRONG_VESSEL, difference.load, null, difference.resource))) {
//				// load swap
//				newStates.addAll(swapLoad(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge, currentPNL, currentLateness,
//						jobStore, ChangeChecker.copyDifferenceList(differencesList)));
//			} else if (differencesList.contains(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, difference.discharge, difference.resource))) {
//				// discharge swap
//				newStates.addAll(swapDischarge(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge, currentPNL,
//						currentLateness, jobStore, ChangeChecker.copyDifferenceList(differencesList)));
//			} else {
//				// discharge swap
//				newStates.addAll(swapDischarge(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge, currentPNL,
//						currentLateness, jobStore, ChangeChecker.copyDifferenceList(differencesList)));
//			}
//		} else if (difference.move == DifferenceType.CARGO_WRONG_VESSEL) {
//			// vessel swap
//			final ISequence originalResource = currentSequences.getSequence(similarityState.getResourceIdxForElement(difference.load));
//			final ISequence currentResource = currentSequences.getSequence(difference.resource.getIndex());
//
//			// TODO: Create and apply change.
//			for (int i = 0; i < currentResource.size(); ++i) {
//				if (currentResource.get(i) == difference.load) {
//
//					// Iterate over all possible positions and try it. Note we really could do with original index information to reduce the quantity of options generated. This
//					// can get very long and quickly explodes the search space.
//					for (int j : findInsertPoints(similarityState, originalResource, difference.resource, difference.load, difference.discharge)) {
//						if (portTypeProvider.getPortType(originalResource.get(j)) != PortType.Discharge) {
//
//							newStates.addAll(swapVessel(currentSequences, similarityState, changes, changeSets, tryDepth, currentPNL, currentLateness, difference.resource, difference.load, difference.discharge,
//									j, jobStore, ChangeChecker.copyDifferenceList(differencesList)));
//						}
//					}
//					break;
//				}
//			}
//		}
//		}
//		// (3) finish
//		if (differencesList.size() > 0) {
//			// Still some (hopefully) correctable changes.
//			return newStates;
//		} else {
//
//			// End of the line, nothing more we can do. Have we got to a valid state?
//			// We should have as we have no more changes. However the change count does not cover everything
//			// Exclusion include: Correct vessel, but incorrect position, any vessel event changes, open slot positions.
//			// Spurious changes: Same spot market & month, different instance.
//
//			for (final IConstraintChecker checker : constraintCheckers) {
//				if (checker.checkConstraints(currentFullSequences) == false) {
//					// Break out -- could get here with bad vessel swap position.
//					return newStates;
//				}
//			}
//
//			final IEvaluationState evaluationState = new EvaluationState();
//			for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
//				if (!evaluationProcess.evaluate(currentFullSequences, evaluationState)) {
//					// Ok, don't really expect to get here..
//					return newStates;
//				}
//			}
//
//			// ... valid state, so we are in a leaf position.
//
//			final ScheduledSequences ss = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
//			assert ss != null;
//			final long thisPNL = calculateSchedulePNL(currentFullSequences, ss);
//			final long thisLateness = calculateScheduleLateness(currentFullSequences, ss);
//
//			final ChangeSet cs = new ChangeSet(changes);
//
//			cs.pnlDelta = thisPNL - currentPNL;
//			cs.latenessDelta = thisLateness - currentLateness;
//
//			final ArrayList<ChangeSet> copiedChangeSets = new ArrayList<>(changeSets);
//			copiedChangeSets.add(cs);
//
//			// Store Changeset info for creating scenarios later
//			cs.setRawSequences(currentSequences);
//			cs.setFullSequences(currentFullSequences);
//
//			final JobState leafJobState = new JobState(new Sequences(currentSequences), copiedChangeSets, new LinkedList<Change>(), thisPNL, thisPNL - currentPNL, thisLateness,
//					thisLateness - currentLateness, Collections.<Difference> emptyList());
//
//			// evaluateLeaf(loadDischargeMap, elementResourceMap, leafJobState.changesAsList, leafJobState.changeSetsAsList, leafJobState.currentPNL, new
//			// ModifiableSequences(leafJobState.rawSequences));
//
//			leafJobState.mode = JobStateMode.LEAF;
//			return Collections.singleton(leafJobState);
//		}
//
//	}
//
//	private <T> T pickRandomElementFromList(List<T> list, Random randomState) {
//		return list.get(randomState.nextInt(list.size()));
//	}
//
//	protected Collection<JobState> swapVessel(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
//			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final long currentPNL, final long currentLateness, @NonNull final IResource resource, @NonNull final ISequenceElement prev,
//			@NonNull final ISequenceElement current, final int j, @NonNull final JobStore jobStore, final List<Difference> differences) {
//		{
//
//			if (similarityState.getResourceIdxForElement(prev).equals(resource.getIndex())) {
//				assert false;
//			}
//
//			final IModifiableSequences copy = new ModifiableSequences(currentSequences);
//			final IModifiableSequence modifiableSequence = copy.getModifiableSequence(similarityState.getResourceIdxForElement(prev));
//			modifiableSequence.insert(j, current);
//			modifiableSequence.insert(j, prev);
//			copy.getModifiableSequence(resource.getIndex()).remove(prev);
//			copy.getModifiableSequence(resource.getIndex()).remove(current);
//
//			final int depth = getNextDepth(tryDepth);
//			final List<Change> changes2 = new ArrayList<>(changes);
//			changes2.add(new Change(String.format("Vessel %s from %s to %s\n", prev.getName(), resource.getName(), copy.getResources().get(similarityState.getResourceIdxForElement(prev)).getName())));
//
//			if (copy.equals(currentSequences)) {
//				// FIXME: Why do we get here?
//				// return Collections.emptyList();
//			}
//
//			differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
//			differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
//			differences.remove(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, current, resource));
//
//			return search(copy, similarityState, changes2, new LinkedList<>(changeSets), depth, MOVE_TYPE_VESSEL_SWAP, currentPNL, currentLateness, jobStore, differences);
//		}
//	}
//
//	/**
//	 * Given a load and discharge, assume the discharge is on the correct resource. Find the correct load for the discharge and swap it with the current load.
//	 */
//	@Override
//	protected Collection<JobState> swapLoad(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
//			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
//			final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore, final List<Difference> differences) {
//
//		if (!(portSlotProvider.getPortSlot(prev) instanceof ILoadSlot)) {
//			return Collections.emptyList();
//		}
//
//		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
//		final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
//
//		// Find the original load index
//		final Integer originalLoadIdx = similarityState.getLoadForDischarge(current);
//		assert originalLoadIdx != null;
//		boolean swapped = false;
//
//		// Find the original load element and swap with current load element
//		ISequenceElement otherLoad = null;
//		ISequenceElement originalDischarge = null;
//		IResource otherResource = null;
//		LOOP: for (final IResource r : copy.getResources()) {
//			final IModifiableSequence s = copy.getModifiableSequence(r);
//
//			for (int j = 0; j < s.size(); ++j) {
//				if (s.get(j).getIndex() == originalLoadIdx.intValue()) {
//					otherLoad = s.get(j);
//					if (!(portSlotProvider.getPortSlot(otherLoad) instanceof ILoadSlot)) {
//						return Collections.emptyList();
//					}
//					if ((portSlotProvider.getPortSlot(s.get(j+1)) instanceof IDischargeSlot)) {
//						originalDischarge = s.get(j+1);
//					}
//					otherResource = r;
//					s.set(j, prev);
//					swapped = true;
//					break LOOP;
//				}
//			}
//		}
//		assert otherResource != null;
//		assert otherLoad != null;
//		assert swapped;
//
//		swapped = false;
//		// Swap the current load element with the original one.
//		for (int j = 0; j < currentResource.size(); ++j) {
//			if (currentResource.get(j) == prev) {
//				currentResource.set(j, otherLoad);
//				swapped = true;
//				break;
//			}
//		}
//
//		// FIXME: GEtting a solution where we swap loads within a resource. Seems like discharge has not moved. However, my debuggin attempts do not show the load on this resource....
//		if (copy.equals(currentSequences)) {
//			// assert false;
//		}
//		if (resource.getIndex() == otherResource.getIndex()) {
//			return Collections.emptyList();
//		}
//
//		assert swapped;
//		assert prev != null;
//		final int depth = getNextDepth(tryDepth);
//		final List<Change> changes2 = new ArrayList<>(changes);
//		changes2.add(new Change(String.format("Swap %s onto %s with %s onto %s\n", prev.getName(), otherResource.getName(), otherLoad.getName(), resource.getName())));
//		// now modify differences
//		// (1) remove initial wrong vessel
//		differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
//		differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, otherLoad, null, otherResource));
//		// (2) fixed wiring change
//		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, prev, current, resource));
//		// (3) new load wrong vessel?
//		if (!similarityState.getResourceForElement(otherLoad).equals(resource)) {
//			differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, otherLoad, current, resource));
//			differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
//		} else {
//			differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
//			differences.remove(new Difference(DifferenceType.CARGO_WRONG_VESSEL, otherLoad, current, resource));
//		}
//		// (4) old load on wrong vessel? or correct discharge?
//		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, otherLoad, originalDischarge, otherResource));
//		ISequenceElement matchedDischargeForOldLoad = similarityState.getElementForIndex(similarityState.getDischargeForLoad(prev));
//		boolean prevOnCorrectLoad = similarityState.getResourceForElement(prev).equals(otherResource);
//		if (originalDischarge != null && originalDischarge.equals(matchedDischargeForOldLoad)) {
//			if (!prevOnCorrectLoad) {
//				differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, originalDischarge, otherResource));
//				differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, originalDischarge, otherResource));
//			}
//		} else {
//			differences.add(new Difference(DifferenceType.CARGO_WRONG_WIRING, prev, originalDischarge, otherResource));
//			if (!prevOnCorrectLoad) {
//				differences.add(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, otherResource));
//			}
//		}
//		
//		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_LOAD_SWAP, currentPNL, currentLateness, jobStore, differences);
//	}
//
//	/**
//	 * Given a load and discharge, assume the load is on the correct resource. Find the correct discharge for the load and swap it with the current discharge.
//	 */
//	protected Collection<JobState> swapDischarge(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
//			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
//			final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore, final List<Difference> differences) {
//
//		if (!(portSlotProvider.getPortSlot(current) instanceof IDischargeSlot)) {
//			return Collections.emptyList();
//		}
//
//		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
//		final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
//
//		// Find the
//		final int originalDischargeIdx = similarityState.getDischargeForLoad(prev);
//
//		boolean swapped = false;
//		ISequenceElement originalDischarge = null;
//		ISequenceElement otherLoad = null;
//		IResource otherResource = null;
//
//		LOOP: for (final IResource r : copy.getResources()) {
//			assert r != null;
//			final IModifiableSequence s = copy.getModifiableSequence(r);
//
//			for (int j = 0; j < s.size(); ++j) {
//				if (s.get(j).getIndex() == originalDischargeIdx) {
//					originalDischarge = s.get(j);
//					if (!(portSlotProvider.getPortSlot(originalDischarge) instanceof IDischargeSlot)) {
//						return Collections.emptyList();
//					}
//					otherLoad = s.get(j - 1);
//					otherResource = r;
//					s.set(j, current);
//					swapped = true;
//					break LOOP;
//				}
//			}
//		}
//		assert swapped;
//		swapped = false;
//		assert originalDischarge != null;
//		assert otherLoad != null;
//		assert otherResource != null;
//		for (int j = 0; j < currentResource.size(); ++j) {
//			if (currentResource.get(j) == current) {
//				currentResource.set(j, originalDischarge);
//				swapped = true;
//				break;
//
//			}
//		}
//		if (copy.equals(currentSequences)) {
//			// assert false;
//		}
//		if (resource.getIndex() == otherResource.getIndex()) {
//			return Collections.emptyList();
//		}
//
//		assert swapped;
//		final int depth = getNextDepth(tryDepth);
//		final List<Change> changes2 = new ArrayList<>(changes);
//		changes2.add(new Change(String.format("Swap %s (to %s) with %s ( to %s)\n", current.getName(), otherLoad.getName(), originalDischarge.getName(), prev.getName())));
//		
//		// now modify differences
//		// (1) remove initial wrong vessel
//		differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
//		differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, originalDischarge, otherResource));
//		// (2) fixed wiring change
//		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, prev, current, resource));
//		// (3) new cargo wrong vessel?
//		if (!similarityState.getResourceForElement(originalDischarge).equals(resource)) {
//			differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, originalDischarge, resource));
//			differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
//		} else {
//			differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
//			differences.remove(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, originalDischarge, resource));
//		}
//		// (4) old discharge on wrong vessel?
//		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, otherLoad, originalDischarge, otherResource));
//		ISequenceElement matchedLoadForOldDischarge = similarityState.getElementForIndex(similarityState.getLoadForDischarge(current));
//		boolean currOnCorrectResource = similarityState.getResourceForElement(current).equals(otherResource);
//		if (otherLoad != null && otherLoad.equals(matchedLoadForOldDischarge)) {
//			if (!currOnCorrectResource) {
//				differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, otherLoad, current, otherResource));
//				differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, otherLoad, null, otherResource));
//			}
//		} else {
//			differences.add(new Difference(DifferenceType.CARGO_WRONG_WIRING, otherLoad, current, otherResource));
//			if (!currOnCorrectResource) {
//				differences.add(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, otherResource));
//			}
//		}
//
//		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_DISCHARGE_SWAP, currentPNL, currentLateness, jobStore, differences);
//	}
//
//	/**
//	 * Completely remove a load and discharge from a Sequence
//	 */
//	protected Collection<JobState> removeElementsFromSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
//			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
//			final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore) {
//
//		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
//		final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
//		currentResource.remove(prev);
//		currentResource.remove(current);
//		copy.getUnusedElements().add(prev);
//		copy.getUnusedElements().add(current);
//
//		final int depth = getNextDepth(tryDepth);
//		final List<Change> changes2 = new ArrayList<>(changes);
//		changes2.add(new Change(String.format("Remove %s and %s\n", prev.getName(), current.getName())));
//		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentPNL, currentLateness, jobStore, null);
//	}
//
//	/**
//	 * Completely remove a load and discharge from a Sequence
//	 */
//	protected Collection<JobState> processOneHalfOfCargoUnused(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
//			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
//			@NonNull final ISequenceElement matchedElement, final int elementIdx, final long currentPNL, final long currentLateness, final boolean isLoadSwap, @NonNull final JobStore jobStore) {
//
//		// (1) is the correct discharge in the unused? Swap it in
//		if (currentSequences.getUnusedElements().contains(matchedElement)) {
//			final IModifiableSequences copy = new ModifiableSequences(currentSequences);
//			final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
//			copy.getUnusedElements().remove(matchedElement);
//			currentResource.insert(elementIdx, matchedElement);
//			currentResource.remove(current);
//			copy.getUnusedElements().add(current);
//			final int depth = getNextDepth(tryDepth);
//			final List<Change> changes2 = new ArrayList<>(changes);
//			final int moveType;
//			if (isLoadSwap) {
//				changes2.add(new Change(String.format("Remove load %s (unused in target solution) and insert load %s (unused in base solution)\n", current.getName(), matchedElement.getName())));
//				moveType = MOVE_TYPE_UNUSED_LOAD_SWAPPED;
//			} else {
//				changes2.add(new Change(String.format("Remove discharge %s (unused in target solution) and insert discharge %s (unused in base solution)\n", current.getName(),
//						matchedElement.getName())));
//				moveType = MOVE_TYPE_UNUSED_DISCHARGE_SWAPPED;
//			}
//			return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, moveType, currentPNL, currentLateness, jobStore, null);
//		} else {
//			// (2) remove both slots
//			return removeElementsFromSequence(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentPNL, currentLateness, jobStore);
//		}
//	}
//
//	protected Collection<JobState> insertUnusedElementsIntoSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
//			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final ISequenceElement element, final long currentPNL, final long currentLateness,
//			Collection<ISequenceElement> unusedElements, @NonNull final JobStore jobStore) {
//		if (portTypeProvider.getPortType(element) == PortType.Load) {
//			Integer otherDischargeIdx = similarityState.getDischargeForLoad(element);
//			ISequenceElement discharge = similarityState.getElementForIndex(otherDischargeIdx);
//			// as moving as a pair, remove discharge from unusedElements queue that we're looping through
//			unusedElements.remove(discharge);
//			// move as pair
//			if (currentSequences.getUnusedElements().contains(discharge)) {
//				IResource resource = similarityState.getResourceForElement(element);
//				return insertUnusedCargoIntoSequence(currentSequences, similarityState, changes, changeSets, tryDepth, resource, element, discharge, currentPNL, currentLateness, jobStore);
//			}
//		} else if (portTypeProvider.getPortType(element) == PortType.Discharge) {
//			Integer otherLoadIdx = similarityState.getLoadForDischarge(element);
//			// If we get here, the load is also unused
//			ISequenceElement load = similarityState.getElementForIndex(otherLoadIdx);
//			// as moving as a pair, remove discharge from unusedElements queue that we're looping through
//			unusedElements.remove(load);
//			// move as a pair
//			if (currentSequences.getUnusedElements().contains(load)) {
//				IResource resource = similarityState.getResourceForElement(element);
//				return insertUnusedCargoIntoSequence(currentSequences, similarityState, changes, changeSets, tryDepth, resource, load, element, currentPNL, currentLateness, jobStore);
//			} else {
//				// Load already exists
//			}
//		} else {
//			// assume vessel event?
//		}
//		return new LinkedList<JobState>();
//	}
//
//	// FIXME: Make sure different is counted in search()
//	protected Collection<JobState> insertUnusedCargoIntoSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
//			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement load, @NonNull final ISequenceElement discharge,
//			final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore) {
//		ISequence originalResource = currentSequences.getSequence(similarityState.getResourceIdxForElement(load));
//		for (int i : findInsertPoints(similarityState, originalResource, resource, load, discharge)) {
//			if (portTypeProvider.getPortType(originalResource.get(i)) != PortType.Discharge) {
//				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
//				final IModifiableSequence modifiableSequence = copy.getModifiableSequence(similarityState.getResourceIdxForElement(load));
//				modifiableSequence.insert(i, discharge);
//				modifiableSequence.insert(i, load);
//
//				final int depth = getNextDepth(tryDepth);
//				final List<Change> changes2 = new ArrayList<>(changes);
//				changes2.add(new Change(String.format("Insert cargo %s -> %s on %s\n", load.getName(), discharge.getName(), copy.getResources().get(similarityState.getResourceIdxForElement(load))
//						.getName())));
//
//				return search(copy, similarityState, changes2, new LinkedList<>(changeSets), depth, MOVE_TYPE_CARGO_INSERT, currentPNL, currentLateness, jobStore, null);
//			}
//		}
//		return new LinkedList<JobState>();
//	}
//
//	public long calculateSchedulePNL(@NonNull final IModifiableSequences fullSequences, @NonNull final ScheduledSequences scheduledSequences) {
//		long sumPNL = 0;
//
//		for (final ScheduledSequence scheduledSequence : scheduledSequences) {
//			for (final Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord> p : scheduledSequence.getVoyagePlans()) {
//				sumPNL += scheduledSequences.getVoyagePlanGroupValue(p.getFirst());
//			}
//			for (final ISequenceElement element : fullSequences.getUnusedElements()) {
//				final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
//				assert portSlot != null;
//				sumPNL += scheduledSequences.getUnusedSlotGroupValue(portSlot);
//			}
//		}
//		return sumPNL;
//	}
//
//	@Override
//	protected int getNextDepth(final int tryDepth) {
//		assert tryDepth >= -1;
//		return tryDepth == DEPTH_START ? getRandomDepth() : tryDepth - 1;
//	}
//	
//	private int getRandomDepth() {
//		int diff = depthEnd - depthStart;
//		int offset = rdm.nextInt(diff);
//		return depthStart + offset;
//	}
//	
//	public void setDepthStart(int depthStart) {
//		this.depthStart = depthStart;
//	}
//
//	public void setDepthEnd(int depthEnd) {
//		this.depthEnd = depthEnd;
//	}
//
//	public void setDepthRange(int start, int end) {
//		this.depthStart = start;
//		this.depthEnd = end;
//	}
//	
//	public Pair<Integer, Integer> getDepthRange() {
//		return new Pair<Integer, Integer>(depthStart, depthEnd);
//	}
//	
//	public IEvaluationState evaluateSequence(IModifiableSequences currentFullSequences) {
//		IEvaluationState evaluationState = new EvaluationState();
//		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
//			if (!evaluationProcess.evaluate(currentFullSequences, evaluationState)) {
//				break;
//			}
//		}
//
//		final ScheduledSequences ss = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
//		assert ss != null;
//		calculateSchedulePNL(currentFullSequences, ss);
//		return evaluationState;
//	}
}
