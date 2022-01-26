package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.providers.GroupedSlotsConstraintInfo;
import com.mmxlabs.scheduler.optimiser.providers.IGroupedSlotsConstraintDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * Constraint checker that ensures grouped discharge slot constraints are satisfied by sequences. Each constraint is a discharge slot collection and a lower amount that must be associated with
 * cargoes.
 * 
 * @author miten
 *
 */
public class GroupedSlotsConstraintChecker implements IConstraintChecker {

	private final @NonNull String name;

	public GroupedSlotsConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IGroupedSlotsConstraintDataProvider groupedSlotsDataProvider;

	@Override
	public @NonNull String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull ISequences fullSequences, @Nullable Collection<@NonNull IResource> changedResources, @Nullable List<@Nullable String> messages) {
		// Implementation below finds the minimum number of slots that can be unused and does a short circuiting check against this minimum.
		final Set<ISequenceElement> unusedSet = getUnusedSet(fullSequences);
		final List<GroupedSlotsConstraintInfo<IDischargeOption>> allDischargeCounts = groupedSlotsDataProvider.getAllMinDischargeGroupCounts();
		for (final GroupedSlotsConstraintInfo<IDischargeOption> constraint : allDischargeCounts) {
			final int maxUnusedSlots = constraint.getSlots().size() - constraint.getBound();
			final long minimumUnusedSlotsNeededToViolate = maxUnusedSlots + 1L;
			final long shortCircuitedActualUnusedCount = constraint.getSlots().stream().unordered().map(portSlotProvider::getElement).filter(unusedSet::contains)
					.limit(minimumUnusedSlotsNeededToViolate).count();
			if (maxUnusedSlots < shortCircuitedActualUnusedCount) {
				if (messages != null) {
					messages.add(String.format("%s: Some grouped slot bounds are violated", this.name));
				}
				return false;
			}
		}
		return true;
	}

	private Set<ISequenceElement> getUnusedSet(@NonNull final ISequences sequences) {
		return new HashSet<>(sequences.getUnusedElements());
	}

}
