/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeChecker.DifferenceType;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.DischargeRewireChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.InsertUnusedCargoChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.LoadRewireChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.RemoveCargoChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.UnusedToUsedDischargeChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.UnusedToUsedLoadChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.VesselChange;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
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
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class BagMover extends BreakdownOptimiserMover {
	Random rdm = new Random(0);
	private int depthStart = 1;
	private int depthEnd = 8;

	@Inject
	@NonNull
	protected Injector injector;

	@Inject
	@NonNull
	IResourceAllocationConstraintDataComponentProvider resourceAllocationProvider;

	@Override
	public Collection<JobState> search(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final int moveType, final long[] currentMetrics, @NonNull final JobStore jobStore,
			@Nullable List<ISequenceElement> targetElements, List<Difference> differencesList) {
		final List<JobState> newStates = new LinkedList<>();
		if (DEBUG_VALIDATION) {
			// check no spurious differences
			{
				ChangeChecker cc = injector.getInstance(ChangeChecker.class);
				cc.init(similarityState, similarityState, currentSequences);
				List<Difference> otherDL = cc.getFullDifferences();
				for (Difference d : differencesList) {
					if (!otherDL.contains(d)) {
						assert false;
					}
				}
				for (Difference d : otherDL) {
					if (!differencesList.contains(d)) {
						System.out.println(d);
						assert false;
					}
				}
			}
			// Sanity check -- elements only used once.
			{
				final Set<ISequenceElement> unique = new HashSet<>();
				for (final IResource resource : currentSequences.getResources()) {
					final ISequence sequence = currentSequences.getSequence(resource);
					for (final ISequenceElement current : sequence) {
						if (unique.contains(current))
							System.out.println(String.format("%s|%s", resource.getName(), current.getName()));
						assert unique.add(current);
					}
				}
			}
		}

		final IModifiableSequences currentFullSequences = new ModifiableSequences(currentSequences);
		sequencesManipulator.manipulate(currentFullSequences);

		if (tryDepth == 0 || differencesList.size() == 0) {
			boolean failedEvaluation = false;

			// Apply hard constraint checkers
			for (final IConstraintChecker checker : constraintCheckers) {
				if (checker.checkConstraints(currentFullSequences) == false) {
					// Break out
					failedEvaluation = true;
					break;
				}
			}
			if (!failedEvaluation) {

				final long thisUnusedCompulsarySlotCount = calculateUnusedCompulsarySlot(currentSequences);
				if (thisUnusedCompulsarySlotCount > similarityState.getBaseMetrics()[MetricType.COMPULSARY_SLOT.ordinal()]) {
					failedEvaluation = true;
				}

				if (!failedEvaluation) {

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

						final long thisLateness = calculateScheduleLateness(currentFullSequences, ss);
						if (thisLateness > similarityState.getBaseMetrics()[MetricType.LATENESS.ordinal()]) {
							failedEvaluation = true;
						} else {
							// currentLateness = thisLateness;
						}

						final long thisCapacity = calculateScheduleCapacity(currentFullSequences, ss);
						if (thisCapacity > similarityState.getBaseMetrics()[MetricType.CAPACITY.ordinal()]) {
							failedEvaluation = true;
						} else {
							// currentLateness = thisLateness;
						}
						long thisPNL = Long.MAX_VALUE;
						if (!failedEvaluation) {

							for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
								// Do PNL bit
								if (evaluationProcess instanceof SchedulerEvaluationProcess) {
									final SchedulerEvaluationProcess schedulerEvaluationProcess = (SchedulerEvaluationProcess) evaluationProcess;
									// schedulerEvaluationProcess.doPNL(currentFullSequences, evaluationState);
								}
							}

							thisPNL = calculateSchedulePNL(currentFullSequences, ss);

							if (thisPNL - currentMetrics[MetricType.PNL.ordinal()] < 0 && thisLateness >= similarityState.getBaseMetrics()[MetricType.LATENESS.ordinal()]) {
								failedEvaluation = true;
							} else {
								// currentPNL = thisPNL;
							}
						}
						if (!failedEvaluation) {

							// Convert change list set into a change set and record sate.
							// TOOD: Get fitness change and only accept improving solutions. (similarity, similarity plus others etc)
							final ChangeSet cs = new ChangeSet(changes);

							cs.setMetric(MetricType.PNL, thisPNL, thisPNL - currentMetrics[MetricType.PNL.ordinal()], thisPNL - similarityState.getBaseMetrics()[MetricType.PNL.ordinal()]);
							cs.setMetric(MetricType.LATENESS, thisLateness, thisLateness - currentMetrics[MetricType.LATENESS.ordinal()], thisLateness
									- similarityState.getBaseMetrics()[MetricType.LATENESS.ordinal()]);
							cs.setMetric(MetricType.CAPACITY, thisCapacity, thisCapacity - currentMetrics[MetricType.CAPACITY.ordinal()], thisCapacity
									- similarityState.getBaseMetrics()[MetricType.CAPACITY.ordinal()]);
							cs.setMetric(MetricType.COMPULSARY_SLOT, thisUnusedCompulsarySlotCount, thisUnusedCompulsarySlotCount - currentMetrics[MetricType.COMPULSARY_SLOT.ordinal()],
									thisUnusedCompulsarySlotCount - similarityState.getBaseMetrics()[MetricType.COMPULSARY_SLOT.ordinal()]);
							cs.setRawSequences(currentSequences);
							changes.clear();
							changeSets.add(cs);

							final JobState jobState = new JobState(new Sequences(currentSequences), changeSets, new LinkedList<Change>(), differencesList);

							jobState.setMetric(MetricType.PNL, thisPNL, thisPNL - currentMetrics[MetricType.PNL.ordinal()], thisPNL - similarityState.getBaseMetrics()[MetricType.PNL.ordinal()]);
							jobState.setMetric(MetricType.LATENESS, thisLateness, thisLateness - currentMetrics[MetricType.LATENESS.ordinal()], thisLateness
									- similarityState.getBaseMetrics()[MetricType.LATENESS.ordinal()]);
							jobState.setMetric(MetricType.CAPACITY, thisCapacity, thisCapacity - currentMetrics[MetricType.CAPACITY.ordinal()], thisCapacity
									- similarityState.getBaseMetrics()[MetricType.CAPACITY.ordinal()]);
							jobState.setMetric(MetricType.COMPULSARY_SLOT, thisUnusedCompulsarySlotCount, thisUnusedCompulsarySlotCount - currentMetrics[MetricType.COMPULSARY_SLOT.ordinal()],
									thisUnusedCompulsarySlotCount - similarityState.getBaseMetrics()[MetricType.COMPULSARY_SLOT.ordinal()]);

							final int changesCount = differencesList.size();
							if (changesCount == 0) {
								jobState.mode = JobStateMode.LEAF;
							}

							// Found a usable state, we no longer need to store limited states.
							// jobStore.setFoundBranch();

							newStates.add(jobState);

							return newStates;

						}
					}
				}
			}
			if (failedEvaluation) {
				// Failed to to find valid state at the end of the search depth. Record a limited state and exit
				if (tryDepth == 0) {
					final JobState jobState = new JobState(new Sequences(currentSequences), changeSets, new LinkedList<Change>(changes));

					jobState.setMetric(MetricType.PNL, currentMetrics[MetricType.PNL.ordinal()], 0, 0);
					jobState.setMetric(MetricType.LATENESS, currentMetrics[MetricType.LATENESS.ordinal()], 0, 0);
					jobState.setMetric(MetricType.CAPACITY, currentMetrics[MetricType.CAPACITY.ordinal()], 0, 0);
					jobState.setMetric(MetricType.COMPULSARY_SLOT, currentMetrics[MetricType.COMPULSARY_SLOT.ordinal()], 0, 0);

					jobState.mode = JobStateMode.LIMITED;
					// jobStore.store(jobState);
					return newStates;
				}
			}
		}
		if (differencesList.size() > 0) {
			StateManager stateManager = new StateManager(currentSequences, currentFullSequences);
			// (1) Choose a difference
			Difference difference = pickRandomElementFromList(differencesList, rdm);
			while (difference.move != DifferenceType.CARGO_WRONG_WIRING && difference.move != DifferenceType.CARGO_WRONG_VESSEL && difference.move != DifferenceType.CARGO_NOT_IN_TARGET
					&& difference.move != DifferenceType.DISCHARGE_UNUSED_IN_BASE && difference.move != DifferenceType.LOAD_UNUSED_IN_BASE
					&& difference.move != DifferenceType.UNUSED_DISCHARGE_IN_TARGET && difference.move != DifferenceType.UNUSED_LOAD_IN_TARGET) {
				difference = pickRandomElementFromList(differencesList, rdm);
			}
			// (2) Fix a difference
			if (difference.move == DifferenceType.CARGO_WRONG_WIRING) {
				if (differencesList.contains(new Difference(DifferenceType.LOAD_WRONG_VESSEL, difference.load, null, difference.resource))
						&& differencesList.contains(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, difference.discharge, difference.resource))) {
					// either do a load swap or a discharge swap
					if (rdm.nextBoolean()) {
						if (!currentSequences.getUnusedElements().contains(similarityState.getElementForIndex(similarityState.getLoadForDischarge(difference.discharge)))) {
							// load
							newStates.addAll(swapLoad(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge, currentMetrics,
									jobStore, null, ChangeChecker.copyDifferenceList(differencesList)));
						}
					} else {
						if (!currentSequences.getUnusedElements().contains(similarityState.getElementForIndex(similarityState.getDischargeForLoad(difference.load)))) {
							// discharge
							newStates.addAll(swapDischarge(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge,
									currentMetrics, jobStore, null, ChangeChecker.copyDifferenceList(differencesList)));
						}
					}

				} else if (differencesList.contains(new Difference(DifferenceType.LOAD_WRONG_VESSEL, difference.load, null, difference.resource))) {
					if (!currentSequences.getUnusedElements().contains(similarityState.getElementForIndex(similarityState.getLoadForDischarge(difference.discharge)))) {
						// load swap
						newStates.addAll(swapLoad(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge, currentMetrics,
								jobStore, null, ChangeChecker.copyDifferenceList(differencesList)));
					}
				} else if (differencesList.contains(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, difference.discharge, difference.resource))) {
					if (!currentSequences.getUnusedElements().contains(similarityState.getElementForIndex(similarityState.getDischargeForLoad(difference.load)))) {
						// discharge swap
						newStates.addAll(swapDischarge(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge, currentMetrics,
								jobStore, null, ChangeChecker.copyDifferenceList(differencesList)));
					}
				} else {
					if (!currentSequences.getUnusedElements().contains(similarityState.getElementForIndex(similarityState.getDischargeForLoad(difference.load)))) {
						// discharge swap
						newStates.addAll(swapDischarge(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge, currentMetrics,
								jobStore, null, ChangeChecker.copyDifferenceList(differencesList)));
					}
				}
			} else if (difference.move == DifferenceType.CARGO_WRONG_VESSEL) {
				// vessel swap
				final ISequence originalResource = currentSequences.getSequence(similarityState.getResourceForElement(difference.load));
				final ISequence currentResource = currentSequences.getSequence(difference.resource);

				// TODO: Create and apply change.
				for (int i = 0; i < currentResource.size(); ++i) {
					if (currentResource.get(i) == difference.load) {

						// Iterate over all possible positions and try it. Note we really could do with original index information to reduce the quantity of options generated. This
						// can get very long and quickly explodes the search space.
						for (int j : findInsertPoints(similarityState, originalResource, difference.resource, difference.load, difference.discharge)) {
							if (portTypeProvider.getPortType(originalResource.get(j)) != PortType.Discharge) {

								newStates.addAll(swapVessel(currentSequences, similarityState, changes, changeSets, tryDepth, currentMetrics, difference.resource, difference.load,
										difference.discharge, j, jobStore, null, ChangeChecker.copyDifferenceList(differencesList)));
							}
						}
						break;
					}
				}
			} else if (difference.move == DifferenceType.UNUSED_DISCHARGE_IN_TARGET) {
				newStates.addAll(processOneHalfOfCargoUnused(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge,
						currentMetrics, false, jobStore, targetElements, ChangeChecker.copyDifferenceList(differencesList)));
			} else if (difference.move == DifferenceType.UNUSED_LOAD_IN_TARGET) {
				newStates.addAll(processOneHalfOfCargoUnused(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.discharge, difference.load,
						currentMetrics, true, jobStore, targetElements, ChangeChecker.copyDifferenceList(differencesList)));

			} else if (difference.move == DifferenceType.CARGO_NOT_IN_TARGET) {
				newStates.addAll(removeElementsFromSequence(currentSequences, similarityState, changes, changeSets, tryDepth, difference.resource, difference.load, difference.discharge,
						currentMetrics, jobStore, targetElements, ChangeChecker.copyDifferenceList(differencesList)));
			} else if (difference.move == DifferenceType.LOAD_UNUSED_IN_BASE) {
				newStates.addAll(insertUnusedElementsIntoSequence(currentSequences, similarityState, stateManager, changes, changeSets, tryDepth, difference.load, currentMetrics, jobStore,
						targetElements, ChangeChecker.copyDifferenceList(differencesList)));
			} else if (difference.move == DifferenceType.DISCHARGE_UNUSED_IN_BASE) {
				newStates.addAll(insertUnusedElementsIntoSequence(currentSequences, similarityState, stateManager, changes, changeSets, tryDepth, difference.discharge, currentMetrics, jobStore,
						targetElements, ChangeChecker.copyDifferenceList(differencesList)));
			}
		}
		// (3) finish
		return newStates;
	}

	private <T> T pickRandomElementFromList(List<T> list, Random randomState) {
		return list.get(randomState.nextInt(list.size()));
	}

	protected Collection<JobState> swapVessel(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final long[] currentMetrics, @NonNull final IResource resource, @NonNull final ISequenceElement prev,
			@NonNull final ISequenceElement current, final int j, @NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull final List<Difference> differences) {
		{

			if (similarityState.getResourceForElement(prev).equals(resource)) {
				assert false;
			}

			final IModifiableSequences copy = new ModifiableSequences(currentSequences);
			final IModifiableSequence modifiableSequence = copy.getModifiableSequence(similarityState.getResourceForElement(prev));
			modifiableSequence.insert(j, current);
			modifiableSequence.insert(j, prev);
			copy.getModifiableSequence(resource).remove(prev);
			copy.getModifiableSequence(resource).remove(current);

			final int depth = getNextDepth(tryDepth);
			final List<Change> changes2 = new ArrayList<>(changes);
			changes2.add(new VesselChange(String.format("Vessel %s from %s to %s\n", prev.getName(), resource.getName(), similarityState.getResourceForElement(prev).getName()), prev, current, modifiableSequence.get(j-1), modifiableSequence.get(j + 2), resource, similarityState.getResourceForElement(prev)));
			if (copy.equals(currentSequences)) {
				// FIXME: Why do we get here?
				// return Collections.emptyList();
			}

			differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
			differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
			differences.remove(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, current, resource));
			for (Difference d : new LinkedList<>(differences)) {
				if (d.move == DifferenceType.CARGO_WRONG_VESSEL && ((prev == d.load && current == d.discharge) || (current == d.load && prev == d.discharge))) {
					differences.remove(d);
				}
			}

			return search(copy, similarityState, changes2, new LinkedList<>(changeSets), depth, MOVE_TYPE_VESSEL_SWAP, currentMetrics, jobStore, null, differences);
		}
	}

	/**
	 * Given a load and discharge, assume the discharge is on the correct resource. Find the correct load for the discharge and swap it with the current load.
	 */
	protected Collection<JobState> swapLoad(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			final long[] currentMetrics, @NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull final List<Difference> differences) {

		if (!(portSlotProvider.getPortSlot(prev) instanceof ILoadSlot)) {
			return Collections.emptyList();
		}

		// Find the original load index
		final Integer originalLoadIdx = similarityState.getLoadForDischarge(current);
		assert originalLoadIdx != null;
		if (isElementUnused(currentSequences, similarityState.getElementForIndex(originalLoadIdx))) {
			// matching load is in unused list, handle elsewhere
			return processOneHalfOfCargoUnused(currentSequences, similarityState, changes, changeSets, tryDepth, resource, current, prev, currentMetrics, true, jobStore, targetElements, differences);
		}

		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence currentResource = copy.getModifiableSequence(resource);
		boolean swapped = false;

		// Find the original load element and swap with current load element
		ISequenceElement otherLoad = null;
		ISequenceElement originalDischarge = null;
		IResource otherResource = null;
		LOOP: for (final IResource r : copy.getResources()) {
			final IModifiableSequence s = copy.getModifiableSequence(r);

			for (int j = 0; j < s.size(); ++j) {
				if (s.get(j).getIndex() == originalLoadIdx.intValue()) {
					otherLoad = s.get(j);
					if (!(portSlotProvider.getPortSlot(otherLoad) instanceof ILoadSlot)) {
						return Collections.emptyList();
					}
					if ((portSlotProvider.getPortSlot(s.get(j + 1)) instanceof IDischargeSlot)) {
						originalDischarge = s.get(j + 1);
					}
					otherResource = r;
					s.set(j, prev);
					swapped = true;
					break LOOP;
				}
			}
		}
		assert otherResource != null;
		assert otherLoad != null;
		assert swapped;
		assert originalDischarge != null;
		swapped = false;
		// Swap the current load element with the original one.
		for (int j = 0; j < currentResource.size(); ++j) {
			if (currentResource.get(j) == prev) {
				currentResource.set(j, otherLoad);
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
		String description = String.format("Swap %s onto %s with %s onto %s\n", prev.getName(), otherResource.getName(), otherLoad.getName(), resource.getName());
		changes2.add(new LoadRewireChange(description, prev, current, otherLoad, originalDischarge, resource, otherResource));
		// now modify differences
		// (1) remove initial wrong vessel
		differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
		differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, otherLoad, null, otherResource));
		// and any existing unused
		differences.remove(new Difference(DifferenceType.UNUSED_LOAD_IN_TARGET, prev, current, resource));
		differences.remove(new Difference(DifferenceType.UNUSED_LOAD_IN_TARGET, otherLoad, originalDischarge, otherResource));
		// (2) fixed wiring change
		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, prev, current, resource));
		// (3) new load wrong vessel?
		if (!similarityState.getResourceForElement(otherLoad).equals(resource)) {
			differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, otherLoad, current, resource));
			differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
		} else {
			differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
			differences.remove(new Difference(DifferenceType.CARGO_WRONG_VESSEL, otherLoad, current, resource));
		}
		// (4) old load on wrong vessel? or correct discharge?
		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, otherLoad, originalDischarge, otherResource));
		ISequenceElement matchedDischargeForOldLoad = similarityState.getElementForIndex(similarityState.getDischargeForLoad(prev));
		boolean prevOnCorrectVessel = similarityState.getResourceForElement(prev).equals(otherResource);
		if (similarityState.getLoadForDischarge(originalDischarge) == null) {
			// current discharge is not in target
			Difference unusedDischarge = new Difference(DifferenceType.UNUSED_DISCHARGE_IN_TARGET, prev, originalDischarge, otherResource);
			if (!differences.contains(unusedDischarge)) {
				differences.add(unusedDischarge);
				if (!prevOnCorrectVessel) {
					differences.add(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, otherResource));
				}
			}
			return processOneHalfOfCargoUnused(copy, similarityState, changes2, new ArrayList<>(changeSets), tryDepth, otherResource, prev, originalDischarge, currentMetrics, false, jobStore,
					targetElements, differences);
		} else {
			if (originalDischarge != null && originalDischarge.equals(matchedDischargeForOldLoad)) {
				if (!prevOnCorrectVessel) {
					differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, originalDischarge, otherResource));
					differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, originalDischarge, otherResource));
				}
			} else {
				differences.add(new Difference(DifferenceType.CARGO_WRONG_WIRING, prev, originalDischarge, otherResource));
				if (!prevOnCorrectVessel) {
					differences.add(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, otherResource));
				}
			}
		}

		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_LOAD_SWAP, currentMetrics, jobStore, null, differences);
	}

	/**
	 * Given a load and discharge, assume the load is on the correct resource. Find the correct discharge for the load and swap it with the current discharge.
	 */
	protected Collection<JobState> swapDischarge(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			final long[] currentMetrics, @NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull final List<Difference> differences) {

		if (!(portSlotProvider.getPortSlot(current) instanceof IDischargeSlot)) {
			return Collections.emptyList();
		}

		// Find the matching discharge for load
		final int originalDischargeIdx = similarityState.getDischargeForLoad(prev);
		if (isElementUnused(currentSequences, similarityState.getElementForIndex(originalDischargeIdx))) {
			// matching discharge is in unused list, handle elsewhere
			return processOneHalfOfCargoUnused(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentMetrics, false, jobStore, targetElements, differences);
		}
		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence currentResource = copy.getModifiableSequence(resource);

		boolean swapped = false;
		ISequenceElement originalDischarge = null;
		ISequenceElement otherLoad = null;
		IResource otherResource = null;
		ISequence otherResourceSequence = null;
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
					otherResourceSequence = s;
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
		String description = String.format("Swap %s (to %s) with %s ( to %s)\n", current.getName(), otherLoad.getName(), originalDischarge.getName(), prev.getName());
		changes2.add(new DischargeRewireChange(description, prev, current, otherLoad, originalDischarge, resource, otherResource));

		// now modify differences
		// (1) remove initial wrong vessel
		differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
		differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, originalDischarge, otherResource));
		// and any unused issue
		differences.remove(new Difference(DifferenceType.UNUSED_DISCHARGE_IN_TARGET, prev, current, resource));
		differences.remove(new Difference(DifferenceType.UNUSED_DISCHARGE_IN_TARGET, otherLoad, originalDischarge, otherResource));
		// (2) fixed wiring change
		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, prev, current, resource));
		// (3) new cargo wrong vessel?
		if (!similarityState.getResourceForElement(originalDischarge).equals(resource)) {
			differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, originalDischarge, resource));
			differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
		} else {
			differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
			differences.remove(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, originalDischarge, resource));
		}
		// (4) old discharge on wrong vessel?
		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, otherLoad, originalDischarge, otherResource));
		ISequenceElement matchedLoadForOldDischarge = similarityState.getElementForIndex(similarityState.getLoadForDischarge(current));
		boolean currOnCorrectResource = similarityState.getResourceForElement(current).equals(otherResource);
		if (similarityState.getDischargeForLoad(otherLoad) == null) {
			// current load is not in target
			Difference unusedLoad = new Difference(DifferenceType.UNUSED_LOAD_IN_TARGET, otherLoad, current, otherResource);
			if (!differences.contains(unusedLoad)) {
				differences.add(unusedLoad);
				if (!currOnCorrectResource) {
					differences.add(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, otherResource));
				}
			}
			return processOneHalfOfCargoUnused(copy, similarityState, changes2, new ArrayList<>(changeSets), tryDepth, otherResource, current, otherLoad, currentMetrics, true, jobStore,
					targetElements, differences);
		} else {
			if (otherLoad != null && otherLoad.equals(matchedLoadForOldDischarge)) {
				if (!currOnCorrectResource) {
					differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, otherLoad, current, otherResource));
					differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, otherLoad, null, otherResource));
				}
			} else {
				differences.add(new Difference(DifferenceType.CARGO_WRONG_WIRING, otherLoad, current, otherResource));
				if (!currOnCorrectResource) {
					differences.add(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, otherResource));
				}
			}
		}

		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_DISCHARGE_SWAP, currentMetrics, jobStore, null, differences);
	}

	/**
	 * Completely remove a load and discharge from a Sequence
	 */
	protected Collection<JobState> removeElementsFromSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			final long[] currentMetrics, @NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull List<Difference> differences) {
		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence copyOfTargetSequence = copy.getModifiableSequence(resource);
		copyOfTargetSequence.remove(prev);
		copyOfTargetSequence.remove(current);
		copy.getModifiableUnusedElements().add(prev);
		copy.getModifiableUnusedElements().add(current);

		final int depth = getNextDepth(tryDepth);
		final List<Change> changes2 = new ArrayList<>(changes);
		String description = String.format("Remove %s and %s\n", prev.getName(), current.getName());
		changes2.add(new RemoveCargoChange(description, prev, current, resource));
		differences.remove(new Difference(DifferenceType.CARGO_NOT_IN_TARGET, prev, current, null));
		differences.remove(new Difference(DifferenceType.CARGO_NOT_IN_TARGET, current, prev, null));
		if (resource != null) {
			ISequenceElement load;
			ISequenceElement discharge;
			if (portTypeProvider.getPortType(prev) == PortType.Load) {
				load = prev;
				discharge = current;
			} else {
				load = current;
				discharge = prev;
			}
			differences.remove(new Difference(DifferenceType.UNUSED_LOAD_IN_TARGET, load, discharge, resource));
			updateWrongVesselDifferenceLoad(differences, load);
			differences.remove(new Difference(DifferenceType.UNUSED_DISCHARGE_IN_TARGET, load, discharge, resource));
			updateWrongVesselDifferenceDischarge(differences, discharge);
			differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, load, discharge, resource));
			Difference loadNeedsInserting = new Difference(DifferenceType.LOAD_UNUSED_IN_BASE, load, null, null);
			Difference dischargeNeedsInserting = new Difference(DifferenceType.DISCHARGE_UNUSED_IN_BASE, null, discharge, null);
			updateDifferencesListAfterElementsRemoval(similarityState, differences, load, discharge);
			if (similarityState.getLoadForDischarge(discharge) != null && !differences.contains(dischargeNeedsInserting)) {
				differences.add(dischargeNeedsInserting);
			}
			if (similarityState.getDischargeForLoad(load) != null & !differences.contains(loadNeedsInserting)) {
				differences.add(loadNeedsInserting);
			}
			for (Difference d : new LinkedList<>(differences)) {
				if (d.move == DifferenceType.CARGO_WRONG_VESSEL && ((prev == d.load && current == d.discharge) || (current == d.load && prev == d.discharge))) {
					differences.remove(d);
				}
			}
		}
		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentMetrics, jobStore, targetElements, differences);
	}

	/**
	 * Completely remove a load and discharge from a Sequence
	 */
	private Collection<JobState> processOneHalfOfCargoUnused(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement elementToKeep,
			@NonNull final ISequenceElement elementToRemove, final long[] currentMetrics, final boolean isLoadSwap, @NonNull final JobStore jobStore,
			@Nullable final List<ISequenceElement> targetElements, @NonNull List<Difference> differencesList) {
		ISequenceElement matchedElement;
		if (isLoadSwap) {
			// find correct load
			matchedElement = similarityState.getElementForIndex(similarityState.getLoadForDischarge(elementToKeep));
		} else {
			// find correct discharge
			matchedElement = similarityState.getElementForIndex(similarityState.getDischargeForLoad(elementToKeep));
		}
		// (1) is the correct element in the unused? Swap it in
		if ((portSlotProvider.getPortSlot(elementToRemove) instanceof ILoadSlot) || (portSlotProvider.getPortSlot(elementToRemove) instanceof IDischargeSlot)
				&& currentSequences.getUnusedElements().contains(matchedElement)) {

			if ((portSlotProvider.getPortSlot(matchedElement) instanceof ILoadSlot) || (portSlotProvider.getPortSlot(matchedElement) instanceof IDischargeSlot)) {
				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
				final IModifiableSequence currentResource = copy.getModifiableSequence(resource);

				copy.getModifiableUnusedElements().remove(matchedElement);
				Integer elementIdx = -11;
				for (int i = 0; i < currentResource.size(); i++) {
					if (currentResource.get(i).getIndex() == elementToRemove.getIndex()) {
						elementIdx = i;
					}
				}
				assert elementIdx != null;
				currentResource.insert(elementIdx, matchedElement);
				currentResource.remove(elementToRemove);

				copy.getModifiableUnusedElements().add(elementToRemove);
				final int depth = getNextDepth(tryDepth);
				final List<Change> changes2 = new ArrayList<>(changes);
				final int moveType;
				if (isLoadSwap) {
					String description = String
							.format("Remove load %s (unused in target solution) and insert load %s (unused in base solution)\n", elementToRemove.getName(), matchedElement.getName());
					changes2.add(new UnusedToUsedLoadChange(description, elementToKeep, elementToRemove, matchedElement, resource));
					moveType = MOVE_TYPE_UNUSED_LOAD_SWAPPED;
					updateWrongCargoWiringDifference(differencesList, elementToRemove, elementToKeep);
					updateDifferencesRemoveUnusedLoadInTarget(differencesList, elementToRemove);
					updateWrongVesselDifferenceLoad(differencesList, elementToRemove);
					checkAndAddDifferenceForUnusedLoadInBase(similarityState, differencesList, elementToRemove);
					updateDifferencesRemoveUnusedLoadInBase(differencesList, matchedElement);
					checkAndAddDifferenceForWrongVesselCargo(similarityState, differencesList, matchedElement, elementToKeep, resource);
				} else {
					String description = String.format("Remove discharge %s (unused in target solution) and insert discharge %s (unused in base solution)\n", elementToRemove.getName(),
							matchedElement.getName());
					changes2.add(new UnusedToUsedDischargeChange(description, elementToKeep, elementToRemove, matchedElement, resource));
					moveType = MOVE_TYPE_UNUSED_DISCHARGE_SWAPPED;
					updateWrongCargoWiringDifference(differencesList, elementToKeep, elementToRemove);
					updateDifferencesRemoveUnusedDischargeInTarget(differencesList, elementToRemove);
					updateWrongVesselDifferenceDischarge(differencesList, elementToRemove);
					checkAndAddDifferenceForUnusedDischargeInBase(similarityState, differencesList, elementToRemove);
					updateDifferencesRemoveUnusedDischargeInBase(differencesList, matchedElement);
					checkAndAddDifferenceForWrongVesselCargo(similarityState, differencesList, elementToKeep, matchedElement, resource);
				}
				return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, moveType, currentMetrics, jobStore, targetElements, differencesList);
			} else {
				// FOB SALE OR DES PURCHASE
				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
				final IModifiableSequence currentResource = copy.getModifiableSequence(resource);
				Collection<IResource> allowedResources = resourceAllocationProvider.getAllowedResources(matchedElement);
				assert allowedResources.size() == 1;

				final IModifiableSequence fsSequence = copy.getModifiableSequence(allowedResources.iterator().next());
				final IModifiableSequence currentSequence = copy.getModifiableSequence(resource);
				copy.getModifiableUnusedElements().remove(matchedElement);
				if (!isLoadSwap) {
					fsSequence.insert(1, matchedElement);
					fsSequence.insert(1, elementToKeep);
					currentSequence.remove(elementToKeep);
					currentSequence.remove(elementToRemove);
					copy.getModifiableUnusedElements().add(elementToRemove);
					final int depth = getNextDepth(tryDepth);
					final List<Change> changes2 = new ArrayList<>(changes);
					String description = String
							.format("Insert FS  %s (unused in target solution) and remove  load %s (unused in base solution)\n", matchedElement.getName(), elementToRemove.getName());
					changes2.add(new UnusedToUsedLoadChange(description, elementToKeep, elementToRemove, matchedElement, resource));
					updateWrongCargoWiringDifference(differencesList, elementToKeep, elementToRemove);
					updateDifferencesRemoveUnusedDischargeInTarget(differencesList, elementToRemove);
					updateDifferencesRemoveUnusedDischargeInBase(differencesList, matchedElement);
					updateWrongVesselDifferenceLoad(differencesList, elementToKeep);
					updateWrongVesselDifferenceDischarge(differencesList, elementToRemove);
					checkAndAddDifferenceForUnusedDischargeInBase(similarityState, differencesList, elementToRemove);
					return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_UNUSED_LOAD_SWAPPED, currentMetrics, jobStore, targetElements, differencesList);
				} else {
					fsSequence.insert(1, elementToKeep);
					fsSequence.insert(1, matchedElement);
					currentSequence.remove(elementToKeep);
					currentSequence.remove(elementToRemove);
					copy.getModifiableUnusedElements().add(elementToRemove);
					final int depth = getNextDepth(tryDepth);
					final List<Change> changes2 = new ArrayList<>(changes);
					String description = String.format("Insert DP  %s (unused in target solution) and remove Discharge %s (unused in base solution)\n", matchedElement.getName(),
							elementToRemove.getName());
					changes2.add(new UnusedToUsedDischargeChange(description, elementToKeep, elementToRemove, matchedElement, resource));
					updateWrongCargoWiringDifference(differencesList, elementToRemove, elementToKeep);
					updateDifferencesRemoveUnusedLoadInBase(differencesList, matchedElement);
					updateDifferencesRemoveUnusedLoadInTarget(differencesList, elementToRemove);
					updateWrongVesselDifferenceLoad(differencesList, elementToRemove);
					checkAndAddDifferenceForUnusedLoadInBase(similarityState, differencesList, elementToRemove);
					updateWrongVesselDifferenceDischarge(differencesList, elementToKeep);

					return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_UNUSED_DISCHARGE_SWAPPED, currentMetrics, jobStore, targetElements, differencesList);
				}
			}
		} else {
			// (2) remove both slots
			return removeElementsFromSequence(currentSequences, similarityState, changes, new ArrayList<>(changeSets), tryDepth, resource, elementToKeep, elementToRemove, currentMetrics, jobStore,
					targetElements, differencesList);
		}
	}

	private Collection<JobState> insertUnusedElementsIntoSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final StateManager stateManager,
			@NonNull final List<Change> changes, @NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final ISequenceElement element, final long[] currentMetrics,
			@NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull List<Difference> differencesList) {
		if (portTypeProvider.getPortType(element) == PortType.Load) {
			final Integer otherDischargeIdx = similarityState.getDischargeForLoad(element);
			final ISequenceElement discharge = similarityState.getElementForIndex(otherDischargeIdx);
			assert isElementUnused(currentSequences, element);
			if (currentSequences.getUnusedElements().contains(discharge)) {
				final IResource resource = similarityState.getResourceForElement(element);
				// move as pair
				return insertUnusedCargoIntoSequence(currentSequences, similarityState, changes, new ArrayList<>(changeSets), tryDepth, resource, element, discharge, currentMetrics, jobStore,
						targetElements, differencesList);
			} else {

				final Pair<IResource, Integer> p = stateManager.getPositionForElement(discharge);

				// step (2) remove both slots
				// FIXME: Currently just unpair both slots and remove from solution
				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
				final IModifiableSequence currentSequence = copy.getModifiableSequence(p.getFirst());

				for (int i = 0; i < currentSequence.size(); ++i) {
					final ISequenceElement e = currentSequence.get(i);
					if (e == discharge) {
						final ISequenceElement load = currentSequence.get(i - 1);
						copy.getModifiableUnusedElements().add(load);
						copy.getModifiableUnusedElements().add(discharge);

						currentSequence.remove(load);
						currentSequence.remove(discharge);

						final List<ISequenceElement> searchElements = targetElements == null ? new LinkedList<ISequenceElement>() : new LinkedList<>(targetElements);
						// Tell next level to focus on the load
						if (!searchElements.contains(load)) {
							searchElements.add(load);
						}
						if (!searchElements.contains(discharge)) {
							searchElements.add(discharge);
						}

						final int depth = getNextDepth(tryDepth);
						final List<Change> changes2 = new ArrayList<>(changes);
						String description = String.format("Remove %s and %s\n", load.getName(), discharge.getName());
						changes2.add(new RemoveCargoChange(description, load, discharge, p.getFirst()));
						updateDifferencesListAfterElementsRemoval(similarityState, differencesList, load, discharge);

						return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentMetrics, jobStore, searchElements, differencesList);
					}
				}

				// Discharge already used, give up on current search and clear hints
				return search(currentSequences, similarityState, new LinkedList<>(changes), new LinkedList<>(changeSets), getNextDepth(tryDepth), 0, currentMetrics, jobStore, null, differencesList);
			}
		} else if (portTypeProvider.getPortType(element) == PortType.Discharge) {
			final Integer otherLoadIdx = similarityState.getLoadForDischarge(element);
			// If we get here, the load is also unused
			final ISequenceElement load = similarityState.getElementForIndex(otherLoadIdx);
			if (currentSequences.getUnusedElements().contains(load)) {
				// as moving as a pair, remove load from unusedElements queue that we're looping through
				final IResource resource = similarityState.getResourceForElement(element);
				// move as a pair
				return insertUnusedCargoIntoSequence(currentSequences, similarityState, changes, new ArrayList<>(changeSets), tryDepth, resource, load, element, currentMetrics, jobStore,
						targetElements, differencesList);
			} else {
				final Pair<IResource, Integer> p = stateManager.getPositionForElement(load);

				// step (2) remove both slots
				// FIXME: Currently just unpair both slots and remove from solution
				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
				final IModifiableSequence currentSequence = copy.getModifiableSequence(p.getFirst());

				for (int i = 0; i < currentSequence.size(); ++i) {
					final ISequenceElement e = currentSequence.get(i);
					if (e == load) {
						final ISequenceElement discharge = currentSequence.get(i + 1);
						assert discharge != null;
						copy.getModifiableUnusedElements().add(load);
						copy.getModifiableUnusedElements().add(discharge);
						currentSequence.remove(load);
						currentSequence.remove(discharge);

						final List<ISequenceElement> searchElements = targetElements == null ? new LinkedList<ISequenceElement>() : new LinkedList<>(targetElements);
						// Tell next level to focus on the load
						if (!searchElements.contains(load)) {
							searchElements.add(load);
						}
						if (!searchElements.contains(discharge)) {
							searchElements.add(discharge);
						}

						final int depth = getNextDepth(tryDepth);
						final List<Change> changes2 = new ArrayList<>(changes);
//						changes2.add(new Change(String.format("Remove %s and %s\n", load.getName(), discharge.getName())));
						String description = String.format("Remove %s and %s\n", load.getName(), discharge.getName());
						changes2.add(new RemoveCargoChange(description, load, discharge, p.getFirst()));
						updateDifferencesListAfterElementsRemoval(similarityState, differencesList, load, discharge);

						return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentMetrics, jobStore, searchElements, differencesList);
					}
				}

				// // Load already used, give up on current search and clear hints
				// return search(currentSequences, similarityState, new LinkedList<>(changes), new LinkedList<>(changeSets), getNextDepth(tryDepth), 0, currentMetrics, jobStore, null);
			}
		} else {
			// assume vessel event?
		}
		return new LinkedList<JobState>();
	}

	private void updateDifferencesListAfterElementsRemoval(SimilarityState similarityState, List<Difference> differencesList, ISequenceElement load, ISequenceElement discharge) {
		checkAndAddDifferenceForUnusedLoadInBase(similarityState, differencesList, load);
		updateDifferencesRemoveUnusedLoadInTarget(differencesList, load);

		checkAndAddDifferenceForUnusedDischargeInBase(similarityState, differencesList, discharge);
		updateDifferencesRemoveUnusedDischargeInTarget(differencesList, discharge);

		updateWrongCargoWiringDifference(differencesList, load, discharge);
		updateWrongVesselDifferenceDischarge(differencesList, discharge);
		updateWrongVesselDifferenceLoad(differencesList, load);
	}

	private void checkAndAddDifferenceForUnusedLoadInBase(SimilarityState similarityState, List<Difference> differencesList, ISequenceElement load) {
		if (similarityState.getDischargeForLoad(load) != null) {
			if (!differencesList.contains(new Difference(DifferenceType.LOAD_UNUSED_IN_BASE, load, null, null))) {
				differencesList.add(new Difference(DifferenceType.LOAD_UNUSED_IN_BASE, load, null, null));
			}
		}
	}

	private void checkAndAddDifferenceForWrongVesselCargo(SimilarityState similarityState, List<Difference> differencesList, ISequenceElement load, ISequenceElement discharge, IResource resource) {
		if (similarityState.getResourceForElement(load) != resource) {
			updateWrongVesselDifferenceLoad(differencesList, load);
			updateWrongVesselDifferenceDischarge(differencesList, discharge);
			if (!differencesList.contains(new Difference(DifferenceType.CARGO_WRONG_VESSEL, load, discharge, resource))) {
				differencesList.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, load, discharge, resource));
			}
		} else {
			updateWrongVesselDifferenceLoad(differencesList, load);
			if (similarityState.getResourceForElement(discharge) != resource) {
				if (!differencesList.contains(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, discharge, resource))) {
					differencesList.add(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, discharge, resource));
				}
			}
		}
	}

	private void checkAndAddDifferenceForUnusedDischargeInBase(SimilarityState similarityState, List<Difference> differencesList, ISequenceElement discharge) {
		if (similarityState.getLoadForDischarge(discharge) != null) {
			if (!differencesList.contains(new Difference(DifferenceType.DISCHARGE_UNUSED_IN_BASE, null, discharge, null))) {
				differencesList.add(new Difference(DifferenceType.DISCHARGE_UNUSED_IN_BASE, null, discharge, null));
			}
		}
	}

	private void updateDifferencesRemoveUnusedLoadInBase(List<Difference> differencesList, ISequenceElement load) {
		for (Difference d : differencesList) {
			if (d.move == DifferenceType.LOAD_UNUSED_IN_BASE && d.load == load) {
				differencesList.remove(d);
				break;
			}
		}
	}

	private void updateDifferencesRemoveUnusedDischargeInBase(List<Difference> differencesList, ISequenceElement discharge) {
		for (Difference d : differencesList) {
			if (d.move == DifferenceType.DISCHARGE_UNUSED_IN_BASE && d.discharge == discharge) {
				differencesList.remove(d);
				break;
			}
		}
	}

	private void updateDifferencesRemoveUnusedLoadInTarget(List<Difference> differencesList, ISequenceElement load) {
		for (int i = differencesList.size() - 1; i >= 0; i--) {
			Difference d = differencesList.get(i);
			if (d.move == DifferenceType.UNUSED_LOAD_IN_TARGET && d.load == load) {
				differencesList.remove(i);
				break;
			}
		}
	}

	private void updateDifferencesRemoveUnusedDischargeInTarget(List<Difference> differencesList, ISequenceElement discharge) {
		// for (Difference d : differencesList) {
		for (int i = differencesList.size() - 1; i >= 0; i--) {
			Difference d = differencesList.get(i);
			if (d.move == DifferenceType.UNUSED_DISCHARGE_IN_TARGET && d.discharge == discharge) {
				differencesList.remove(i);
			}
		}
	}

	private void updateWrongCargoWiringDifference(List<Difference> differencesList, ISequenceElement load, ISequenceElement discharge) {
		for (Difference d : differencesList) {
			if (d.move == DifferenceType.CARGO_WRONG_WIRING && d.load == load && d.discharge == discharge) {
				differencesList.remove(d);
				break;
			}
		}
	}

	private void updateWrongVesselDifferenceDischarge(List<Difference> differencesList, ISequenceElement discharge) {
		for (Difference d : differencesList) {
			if (d.move == DifferenceType.DISCHARGE_WRONG_VESSEL && d.discharge == discharge) {
				differencesList.remove(d);
				break;
			}
		}
	}

	private void updateWrongVesselDifferenceLoad(List<Difference> differencesList, ISequenceElement load) {
		for (Difference d : differencesList) {
			if (d.move == DifferenceType.LOAD_WRONG_VESSEL && d.load == load) {
				differencesList.remove(d);
				break;
			}
		}
	}

	// FIXME: Make sure different is counted in search()
	protected Collection<JobState> insertUnusedCargoIntoSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement load, @NonNull final ISequenceElement discharge,
			final long[] currentMetrics, @NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull List<Difference> differencesList) {
		final ISequence sequenceOfOriginalResource = currentSequences.getSequence(similarityState.getResourceForElement(load));
		for (IResource r : currentSequences.getResources()) {
			for (ISequenceElement element : currentSequences.getSequence(r)) {
				assert element != load;
				assert element != discharge;
			}
		}
		List<JobState> newStates = new LinkedList<>();
		for (final int i : findInsertPoints(similarityState, sequenceOfOriginalResource, similarityState.getResourceForElement(load), load, discharge)) {
			if (portTypeProvider.getPortType(sequenceOfOriginalResource.get(i)) != PortType.Discharge) {
				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
				final IModifiableSequence modifiableSequence = copy.getModifiableSequence(similarityState.getResourceForElement(load));
				modifiableSequence.insert(i, discharge);
				modifiableSequence.insert(i, load);

				copy.getModifiableUnusedElements().remove(load);
				copy.getModifiableUnusedElements().remove(discharge);

				final int depth = getNextDepth(tryDepth);
				final List<Change> changes2 = new ArrayList<>(changes);
//				changes2.add(new Change(String.format("Insert cargo %s -> %s on %s\n", load.getName(), discharge.getName(), similarityState.getResourceForElement(load).getName())));
				String description = String.format("Insert cargo %s -> %s on %s\n", load.getName(), discharge.getName(), similarityState.getResourceForElement(load).getName());
				changes2.add(new InsertUnusedCargoChange(description, modifiableSequence.get(i-1), modifiableSequence.get(i+2), load, discharge, similarityState.getResourceForElement(load)));
				differencesList.remove(new Difference(DifferenceType.CARGO_NOT_IN_TARGET, load, discharge, null));
				differencesList.remove(new Difference(DifferenceType.LOAD_UNUSED_IN_BASE, load, null, null));
				differencesList.remove(new Difference(DifferenceType.DISCHARGE_UNUSED_IN_BASE, null, discharge, null));
				newStates.addAll(search(copy, similarityState, changes2, new LinkedList<>(changeSets), depth, MOVE_TYPE_CARGO_INSERT, currentMetrics, jobStore, targetElements, differencesList));
			}
		}
		return newStates;
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

	@Override
	protected int getNextDepth(final int tryDepth) {
		assert tryDepth >= -1;
		return tryDepth == DEPTH_START ? getRandomDepth() : tryDepth - 1;
	}

	private int getRandomDepth() {
		int diff = depthEnd - depthStart;
		int offset = rdm.nextInt(diff);
		return depthStart + offset;
	}

	public void setDepthStart(int depthStart) {
		this.depthStart = depthStart;
	}

	public void setDepthEnd(int depthEnd) {
		this.depthEnd = depthEnd;
	}

	public void setDepthRange(int start, int end) {
		this.depthStart = start;
		this.depthEnd = end;
	}

	public Pair<Integer, Integer> getDepthRange() {
		return new Pair<Integer, Integer>(depthStart, depthEnd);
	}

	private boolean isElementUnused(@NonNull final ISequences currentSequences, @NonNull ISequenceElement element) {
		return currentSequences.getUnusedElements().contains(element);
	}
}
