/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.multiobjective.impl.NonDominatedSolution;

public class MultiObjectiveUtils {

	private MultiObjectiveUtils() {

	}

	public static boolean isDominated(List<NonDominatedSolution> archive, long[] thisFitness) {
		List<ISequences> dominated = new LinkedList<>();
		boolean add = true;
		for (NonDominatedSolution other : archive) {
			long[] otherFitness = other.getFitnesses();
			if (!atLeastOneObjectiveLower(thisFitness, otherFitness)) {
				// this solution is dominated
				add = false;
				break;
			}
			if (allObjectivesLower(thisFitness, otherFitness)) {
				// new solution dominates
				dominated.add(other.getSequences());
			} else if (allObjectivesLowerOrEqual(thisFitness, otherFitness)) {
				dominated.add(other.getSequences());
			}
		}
		for (ISequences a : dominated) {
			archive.removeIf(b -> b.getSequences().equals(a));
		}
		return add;
	}

	public static boolean isEpsilonDominated(List<NonDominatedSolution> archive, long[] thisFitness, long[] epsilon) {
		long[] epsilonUpdatedFitness = new long[thisFitness.length];
		for (int i = 0; i < thisFitness.length; i++) {
			epsilonUpdatedFitness[i] = thisFitness[i] + epsilon[i];
		}
		return isDominated(archive, epsilonUpdatedFitness);
	}

	private static boolean allObjectivesLower(long[] thisFitness, long[] otherFitness) {
		for (int i = 0; i < thisFitness.length; i++) {
			if (otherFitness[i] <= thisFitness[i]) {
				return false;
			}
		}
		return true;
	}

	private static boolean allObjectivesLowerOrEqual(long[] thisFitness, long[] otherFitness) {
		for (int i = 0; i < thisFitness.length; i++) {
			if (thisFitness[i] > otherFitness[i]) {
				return false;
			}
		}
		return true;
	}

	private static boolean atLeastOneObjectiveLower(long[] thisFitness, long[] otherFitness) {
		for (int i = 0; i < thisFitness.length; i++) {
			if (thisFitness[i] < otherFitness[i]) {
				return true;
			}
		}
		return false;
	}

	public static List<NonDominatedSolution> filterArchive(List<NonDominatedSolution> archive, int objectiveIndex, long[] epsilon) {
		if (archive.size() < 2) {
			return archive;
		}
		List<NonDominatedSolution> sortedOldArchive = getSortedArchive(archive, objectiveIndex);
		List<NonDominatedSolution> sortedNewArchive = new LinkedList<>();
		sortedNewArchive.add(sortedOldArchive.get(0));
		for (NonDominatedSolution nonDominatedSolution : sortedOldArchive.subList(1, sortedOldArchive.size())) {
			if (isEpsilonDominated(sortedNewArchive, nonDominatedSolution.getFitnesses(), epsilon)) {
				sortedNewArchive.add(nonDominatedSolution);
			}
		}
		Collections.reverse(sortedNewArchive);
		return sortedNewArchive;
	}

	private static List<NonDominatedSolution> getSortedArchive(List<NonDominatedSolution> archive, int objectiveIndex) {
		List<NonDominatedSolution> sortedValues = archive.stream().sorted((a, b) -> -1 * Long.compare(a.getFitnesses()[objectiveIndex], b.getFitnesses()[objectiveIndex])).collect(Collectors.toList());
		return sortedValues;
	}

}
