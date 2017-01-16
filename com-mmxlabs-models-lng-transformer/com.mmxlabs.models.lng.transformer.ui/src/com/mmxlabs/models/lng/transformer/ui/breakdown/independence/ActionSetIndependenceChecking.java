/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown.independence;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ActionSetEvaluationHelper;
import com.mmxlabs.models.lng.transformer.ui.breakdown.Change;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeSet;
import com.mmxlabs.models.lng.transformer.ui.breakdown.MetricType;
import com.mmxlabs.models.lng.transformer.ui.breakdown.SimilarityState;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;

/**
 * Methods to find dependency relationships in a sequenced list of action sets.
 * 
 * @author achurchill
 * 
 */
public class ActionSetIndependenceChecking {

	@Inject
	@NonNull
	protected IPortTypeProvider portTypeProvider;

	@Inject
	@NonNull
	protected IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	protected ISequencesManipulator sequencesManipulator;

	@Inject
	@NonNull
	protected List<IConstraintChecker> constraintCheckers;

	@Inject
	@NonNull
	protected List<IEvaluationProcess> evaluationProcesses;

	@Inject
	@NonNull
	private IOptionalElementsProvider optionalElementsProvider;

	@Inject
	protected ActionSetEvaluationHelper evaluationHelper;

	private final boolean DEBUG = true;

	/**
	 * Get dependancy information for each individual change set. This can be used to create a dependency graph
	 * 
	 * @param changeSets
	 * @param baseSequences
	 * @param similarityState
	 * @param currentMetrics
	 * @return
	 */
	public Map<ChangeSet, Set<List<ChangeSet>>> getChangeSetIndependence(@NonNull final List<ChangeSet> changeSets, @NonNull final ISequences baseSequences,
			@NonNull final SimilarityState similarityState, final long @NonNull [] currentMetrics) {
		final Map<ChangeSet, Set<List<ChangeSet>>> relationships = new HashMap<>();
		final Deque<List<ChangeSet>> perms = createPermutations(changeSets);
		while (perms.size() > 0) {
			final List<ChangeSet> dependency = perms.pop();
			assert dependency != null;
			final ISequences applied = applyChanges(baseSequences, dependency, similarityState, currentMetrics);
			if (applied != null) {
				// We care about the final change set
				final ChangeSet interestingChangeSet = dependency.get(dependency.size() - 1);
				Set<List<ChangeSet>> set = relationships.get(interestingChangeSet);
				if (set == null) {
					set = new HashSet<>();
					relationships.put(interestingChangeSet, set);
				}
				set.add(dependency);
				pruneSuccessfulPermutations(dependency, (List) perms);
			} else {
				pruneUnsuccessfulPermutations(dependency, (List) perms);
			}
		}
		if (DEBUG) {
			for (final ChangeSet cs : relationships.keySet()) {
				System.out.println("---Map---");
				for (final Change c : cs.changesList) {
					System.out.println(c.description);
				}
				System.out.println("---------");
				final Set<List<ChangeSet>> rels = relationships.get(cs);
				for (final List<ChangeSet> lcs : rels) {
					String output = "";
					for (final ChangeSet _cs : lcs) {
						for (int i = 0; i < changeSets.size(); i++) {
							if (changeSets.get(i) == _cs) {
								output += " --> " + i;
							}
						}
					}
					System.out.println(output);
				}
			}
		}
		return relationships;
	}

	private void pruneSuccessfulPermutations(final List<ChangeSet> dependency, final List<List<ChangeSet>> perms) {
		for (int idx = perms.size() - 1; idx > -1; idx--) {
			final List<ChangeSet> lcs = perms.get(idx);
			if (dependency.get(dependency.size() - 1).equals(lcs.get(lcs.size() - 1))) {
				perms.remove(idx);
			}
		}
	}

	private void pruneUnsuccessfulPermutations(final List<ChangeSet> dependency, final List<List<ChangeSet>> perms) {
		for (int idx = perms.size() - 1; idx > -1; idx--) {
			final List<ChangeSet> lcs = perms.get(idx);
			boolean beginningIdentical = true;
			for (int i = 0; i < dependency.size(); i++) {
				if (!dependency.get(i).equals(lcs.get(i))) {
					beginningIdentical = false;
					break;
				}
			}
			if (beginningIdentical) {
				perms.remove(idx);
			}
		}
	}

	private ISequences applyChanges(@NonNull final ISequences baseSequences, @NonNull final List<ChangeSet> changeSets, @NonNull final SimilarityState similarityState,
			final long @NonNull [] currentMetrics) {
		long[] thisCurrentMetrics = currentMetrics;
		ISequences currentSequences = baseSequences;
		for (final ChangeSet cs : changeSets) {
			for (final Change c : cs.changesList) {
				Pair<Boolean, ISequences> result = null;
				if (c instanceof LoadRewireChange) {
					result = applyLoadRewireChange(currentSequences, (LoadRewireChange) c);
				} else if (c instanceof DischargeRewireChange) {
					result = applyDischargeRewireChange(currentSequences, (DischargeRewireChange) c);
				} else if (c instanceof VesselChange) {
					result = applyVesselChange(currentSequences, (VesselChange) c);
				} else if (c instanceof RemoveCargoChange) {
					result = applyRemoveCargoChange(currentSequences, (RemoveCargoChange) c);
				} else if (c instanceof InsertUnusedCargoChange) {
					result = applyInsertUnusedCargoChange(currentSequences, (InsertUnusedCargoChange) c);
				} else if (c instanceof UnusedToUsedLoadChange) {
					result = applyUnusedLoadRewireChange(currentSequences, (UnusedToUsedLoadChange) c);
				} else if (c instanceof UnusedToUsedDischargeChange) {
					result = applyUnusedDischargeRewireChange(currentSequences, (UnusedToUsedDischargeChange) c);
				} else {
					assert false;
				}
				if (result == null || result.getFirst() == false) {
					return null;
				} else {
					currentSequences = result.getSecond();
					assert currentSequences != null;
				}
			}
			if (currentSequences != null) {
				thisCurrentMetrics = evaluateSolution(currentSequences, similarityState, thisCurrentMetrics);
				if (thisCurrentMetrics == null) {
					return null;
				}
			}
		}
		return currentSequences;
	}

	private Deque<List<ChangeSet>> createPermutations(final List<ChangeSet> finalBreakdown) {
		final Deque<List<ChangeSet>> perms = new LinkedList<>();
		final int current = finalBreakdown.size() - 1;
		for (int length = 1; length < finalBreakdown.size() + 1; length++) {
			final int[] initial = new int[length];
			for (int index = 0; index < length; index++) {
				initial[index] = index;
			}
			getPermutation(initial, finalBreakdown, current, perms);
		}
		return perms;
	}

	private Deque<List<ChangeSet>> getPermutation(int[] solution, final List<ChangeSet> original, final int current, final Deque<List<ChangeSet>> permutations) {

		while (solution != null) {
			permutations.add(generatePermutations(solution, original));
			solution = resetPermutation(solution, current);
		}

		return permutations;
	}

	private List<ChangeSet> generatePermutations(final int[] solution, final List<ChangeSet> original) {
		final List<ChangeSet> processed = new LinkedList<>();
		for (int i = 0; i < solution.length; i++) {
			processed.add(original.get(solution[i]));
		}
		return processed;
	}

	private int[] resetPermutation(final int[] solution, final int current) {
		final int length = solution.length;
		int pivot = -1;
		for (int i = length - 1; i > -1; i--) {
			if (i == length - 1) {
				if (solution[i] < current) {
					solution[i] += 1;
					return solution;
				}
			} else {
				if (solution[i] < solution[i + 1] - 1) {
					solution[i] += 1;
					pivot = i + 1;
					break;
				}
			}
		}
		if (pivot != -1) {
			for (int i = pivot; i < solution.length; i++) {
				solution[i] = solution[i - 1] + 1;
			}
			return solution;
		} else {
			return null;
		}
	}

	private Pair<Boolean, ISequences> applyDischargeRewireChange(@NonNull final ISequences currentSequences, @NonNull final DischargeRewireChange change) {
		final int[] indexes = getDualCargoIndexes(currentSequences, change);
		final int loadAIdx = indexes[0];
		final int dischargeAIdx = indexes[1];
		final int loadBIdx = indexes[2];
		final int dischargeBIdx = indexes[3];
		for (final int index : indexes) {
			if (index == -1) {
				return new Pair<>(false, null);
			}
		}
		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence resourceA = copy.getModifiableSequence(change.getResourceA());
		final IModifiableSequence resourceB = copy.getModifiableSequence(change.getResourceB());
		resourceA.remove(change.getElement2A());
		resourceB.remove(change.getElement2B());
		resourceA.insert(dischargeAIdx, change.getElement2B());
		resourceB.insert(dischargeBIdx, change.getElement2A());
		return new Pair<Boolean, ISequences>(true, copy);
	}

	private Pair<Boolean, ISequences> applyLoadRewireChange(@NonNull final ISequences currentSequences, @NonNull final LoadRewireChange change) {
		final int[] indexes = getDualCargoIndexes(currentSequences, change);
		final int loadAIdx = indexes[0];
		final int dischargeAIdx = indexes[1];
		final int loadBIdx = indexes[2];
		final int dischargeBIdx = indexes[3];
		for (final int index : indexes) {
			if (index == -1) {
				return new Pair<>(false, null);
			}
		}
		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence resourceA = copy.getModifiableSequence(change.getResourceA());
		final IModifiableSequence resourceB = copy.getModifiableSequence(change.getResourceB());

		// remove loads
		resourceA.remove(change.getElement1A());
		resourceB.remove(change.getElement1B());

		// insert loads
		resourceA.insert(loadAIdx, change.getElement1B());
		resourceB.insert(loadBIdx, change.getElement1A());
		return new Pair<Boolean, ISequences>(true, copy);
	}

	private Pair<Boolean, ISequences> applyUnusedDischargeRewireChange(final ISequences currentSequences, final UnusedToUsedDischargeChange change) {
		// check that the unused element is currently unused
		if (!currentSequences.getUnusedElements().contains(change.getUnusedDischarge())) {
			return new Pair<>(false, null);
		}
		// check other elements are in order
		final int[] indexes = getSingleCargoIndexes(currentSequences, change.getLoadA(), change.getDischargeA(), change.getResourceA());
		final int loadAIdx = indexes[0];
		final int dischargeAIdx = indexes[1];
		for (final int index : indexes) {
			if (index == -1) {
				return new Pair<>(false, null);
			}
		}
		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence resource = copy.getModifiableSequence(change.getResourceA());
		// swap used for unused discharge
		resource.remove(change.getDischargeA());
		copy.getModifiableUnusedElements().add(change.getDischargeA());
		copy.getModifiableUnusedElements().remove(change.getUnusedDischarge());
		resource.insert(dischargeAIdx, change.getUnusedDischarge());
		return new Pair<Boolean, ISequences>(true, copy);
	}

	private Pair<Boolean, ISequences> applyRemoveCargoChange(@NonNull final ISequences currentSequences, @NonNull final RemoveCargoChange change) {
		// check elements are in order
		final int[] indexes = getSingleCargoIndexes(currentSequences, change.getLoadA(), change.getDischargeA(), change.getResourceA());
		for (final int index : indexes) {
			if (index == -1) {
				return new Pair<>(false, null);
			}
		}
		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence resource = copy.getModifiableSequence(change.getResourceA());
		// remove cargo
		resource.remove(change.getLoadA());
		resource.remove(change.getDischargeA());
		copy.getModifiableUnusedElements().add(change.getLoadA());
		copy.getModifiableUnusedElements().add(change.getDischargeA());
		return new Pair<Boolean, ISequences>(true, copy);
	}

	private Pair<Boolean, ISequences> applyUnusedLoadRewireChange(@NonNull final ISequences currentSequences, @NonNull final UnusedToUsedLoadChange change) {
		// check that the unused element is currently unused
		if (!currentSequences.getUnusedElements().contains(change.getUnusedLoad())) {
			return new Pair<>(false, null);
		}
		// check other elements are in order
		final int[] indexes = getSingleCargoIndexes(currentSequences, change.getUsedLoad(), change.getDischarge(), change.getResource());
		final int loadAIdx = indexes[0];
		final int dischargeAIdx = indexes[1];
		for (final int index : indexes) {
			if (index == -1) {
				return new Pair<>(false, null);
			}
		}
		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence resource = copy.getModifiableSequence(change.getResource());
		// swap used for unused load
		resource.remove(change.getUsedLoad());
		copy.getModifiableUnusedElements().add(change.getUsedLoad());
		copy.getModifiableUnusedElements().remove(change.getUnusedLoad());
		resource.insert(loadAIdx, change.getUnusedLoad());
		return new Pair<Boolean, ISequences>(true, copy);
	}

	private Pair<Boolean, ISequences> applyInsertUnusedCargoChange(@NonNull final ISequences currentSequences, @NonNull final InsertUnusedCargoChange change) {
		// check that the unused element is currently unused
		if (!(currentSequences.getUnusedElements().contains(change.getUnusedLoad()) && currentSequences.getUnusedElements().contains(change.getUnusedDischarge()))) {
			return new Pair<>(false, null);
		}
		// check other elements are in order
		final int[] indexes = getSingleCargoIndexes(currentSequences, change.getLoadA(), change.getDischargeA(), change.getResourceA());
		final int loadAIdx = indexes[0];
		final int dischargeAIdx = indexes[1];
		for (final int index : indexes) {
			if (index == -1) {
				return new Pair<>(false, null);
			}
		}
		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence resource = copy.getModifiableSequence(change.getResourceA());
		// insert unused cargo
		copy.getModifiableUnusedElements().remove(change.getUnusedLoad());
		copy.getModifiableUnusedElements().remove(change.getUnusedDischarge());
		resource.insert(loadAIdx + 1, change.getUnusedDischarge()); // insert after previous load: loadA ---> new load ---> new discharge ---> dischargeA
		resource.insert(loadAIdx + 1, change.getUnusedLoad());
		return new Pair<Boolean, ISequences>(true, copy);
	}

	private Pair<Boolean, ISequences> applyVesselChange(@NonNull final ISequences currentSequences, @NonNull final VesselChange change) {
		final int[] indexes = getDualCargoIndexes(currentSequences, change);
		final int loadAIdx = indexes[0];
		final int dischargeAIdx = indexes[1];
		final int element1BIdx = indexes[2];
		final int element2BIdx = indexes[3];
		for (final int index : indexes) {
			if (index == -1) {
				return new Pair<>(false, null);
			}
		}
		// check elements either side are consecutive on new vessel
		if (element2BIdx != element1BIdx + 1) {
			return new Pair<>(false, null);
		}

		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence resourceA = copy.getModifiableSequence(change.getResourceA());
		final IModifiableSequence resourceB = copy.getModifiableSequence(change.getResourceB());

		// remove cargo
		resourceA.remove(change.getElement1A());
		resourceA.remove(change.getElement2A());

		// insert cargo
		resourceB.insert(element1BIdx + 1, change.getElement2A()); // discharge
		resourceB.insert(element1BIdx + 1, change.getElement1A()); // load
		return new Pair<Boolean, ISequences>(true, copy);
	}

	private int[] getSingleCargoIndexes(@NonNull final ISequences currentSequences, @NonNull final ISequenceElement load, @NonNull final ISequenceElement discharge,
			@NonNull final IResource resourceA) {
		final int[] indexes = new int[2];
		indexes[0] = indexes[1] = -1;
		for (final IResource resource : currentSequences.getResources()) {
			assert resource != null;
			final ISequence sequence = currentSequences.getSequence(resource);
			int idx = 0;
			for (final ISequenceElement element : sequence) {
				if (element == load && resource == resourceA) {
					indexes[0] = idx;
				} else if (element == discharge && resource == resourceA) {
					indexes[1] = idx;
				}
				idx++;
			}
		}
		return indexes;
	}

	private int[] getDualCargoIndexes(final ISequences currentSequences, final RewireChange change) {
		final int[] indexes = new int[4];
		indexes[0] = indexes[1] = indexes[2] = indexes[3] = -1;
		for (final IResource resource : currentSequences.getResources()) {
			assert resource != null;
			final ISequence sequence = currentSequences.getSequence(resource);
			int idx = 0;
			for (final ISequenceElement element : sequence) {
				if (element == change.getElement1A() && resource == change.getResourceA()) {
					indexes[0] = idx;
				} else if (element == change.getElement2A() && resource == change.getResourceA()) {
					indexes[1] = idx;
				} else if (element == change.getElement1B() && resource == change.getResourceB()) {
					indexes[2] = idx;
				} else if (element == change.getElement2B() && resource == change.getResourceB()) {
					indexes[3] = idx;
				}
				idx++;
			}
		}
		return indexes;
	}

	private long @Nullable [] evaluateSolution(@NonNull final ISequences currentSequences, final SimilarityState similarityState, final long[] currentMetrics) {

		final IModifiableSequences currentFullSequences = new ModifiableSequences(currentSequences);
		sequencesManipulator.manipulate(currentFullSequences);

		long[] metrics = evaluationHelper.evaluateState(currentSequences, currentFullSequences, null, similarityState, null);
		if (metrics == null) {
			return null;
		}
		long thisPNL = metrics[MetricType.PNL.ordinal()];
		long thisLateness = metrics[MetricType.LATENESS.ordinal()];
		if (thisPNL - currentMetrics[MetricType.PNL.ordinal()] < 0 && thisLateness >= similarityState.getBaseMetrics()[MetricType.LATENESS.ordinal()]) {
			return null;
		}
		return metrics;
	}
}
