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
		final Set<ISequenceElement> unusedSet = getUnusedSet(fullSequences);
		final List<GroupedSlotsConstraintInfo<IDischargeOption>> allDischargeCounts = groupedSlotsDataProvider.getAllMinDischargeGroupCounts();
		for (final GroupedSlotsConstraintInfo<IDischargeOption> constraint : allDischargeCounts) {
			int unusedMembershipCount = 0;
			final int maxUnusedSlots = constraint.getSlots().size() - constraint.getBound();
			final long shortCircuitedActualUnusedCount = constraint.getSlots().stream().unordered().filter(dischargeOption -> unusedSet.contains(portSlotProvider.getElement(dischargeOption)))
					.limit(maxUnusedSlots).count();
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
