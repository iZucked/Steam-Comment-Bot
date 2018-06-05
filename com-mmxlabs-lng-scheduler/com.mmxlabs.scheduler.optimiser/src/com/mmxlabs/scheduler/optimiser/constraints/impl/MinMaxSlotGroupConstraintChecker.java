/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.providers.IMaxSlotCountConstraintProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * {@link IPairwiseConstraintChecker} to keep groups of slots in their required ranges.
 * 
 * @author achurchill
 */
public class MinMaxSlotGroupConstraintChecker implements IPairwiseConstraintChecker {
	@Inject
	private IMaxSlotCountConstraintProvider maxSlotCountConstraintProvider;
	
	@Inject
	private IPortSlotProvider portSlotProvider;
	
	private String name;

	public MinMaxSlotGroupConstraintChecker(String name) {
		this.name = name;
	}

	/**
	 * Note: this does not make sense for this constraint
	 */
	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return null;
	}

	@Override
	public @NonNull String getName() {
		return getName();
	}

	@Override
	public boolean checkConstraints(@NonNull ISequences sequences, @Nullable Collection<@NonNull IResource> changedResources) {
		Set<ISequenceElement> unusedSet = getUnusedSet(sequences);
		List<Pair<Set<IDischargeOption>,Integer>> allMinDischargeGroupCounts = maxSlotCountConstraintProvider.getAllMinDischargeGroupCounts();
		List<Pair<Set<IDischargeOption>,Integer>> allMaxDischargeGroupCounts = maxSlotCountConstraintProvider.getAllMaxDischargeGroupCounts();
		List<Pair<Set<ILoadOption>,Integer>> allMinLoadGroupCounts = maxSlotCountConstraintProvider.getAllMinLoadGroupCounts();
		List<Pair<Set<ILoadOption>,Integer>> allMaxLoadGroupCounts = maxSlotCountConstraintProvider.getAllMaxLoadGroupCounts();

		for (Pair<Set<IDischargeOption>, Integer> pair : allMinDischargeGroupCounts) {
			if (pair.getFirst().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count() < pair.getSecond()) {
				return false;
			}
		}
		for (Pair<Set<IDischargeOption>, Integer> pair : allMaxDischargeGroupCounts) {
			if (pair.getFirst().stream()
					.filter(s -> !unusedSet.contains(portSlotProvider.getElement(s)))
					.count() > pair.getSecond()) {
				return false;
			}
		}
		for (Pair<Set<ILoadOption>, Integer> pair : allMinLoadGroupCounts) {
			if (pair.getFirst().stream()
					.filter(s -> !unusedSet.contains(portSlotProvider.getElement(s)))
					.count() < pair.getSecond()) {
				return false;
			}
		}
		for (Pair<Set<ILoadOption>, Integer> pair : allMaxLoadGroupCounts) {
			if (pair.getFirst().stream()
					.filter(s -> !unusedSet.contains(portSlotProvider.getElement(s)))
					.count() > pair.getSecond()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean checkConstraints(@NonNull ISequences sequences, @Nullable Collection<@NonNull IResource> changedResources, @Nullable List<String> messages) {
		return checkConstraints(sequences, changedResources);
	}
	
	private Set<ISequenceElement> getUnusedSet(@NonNull ISequences sequences) {
		return new HashSet<>(sequences.getUnusedElements());
	}

}
