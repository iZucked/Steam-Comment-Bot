package com.mmxlabs.models.lng.schedule.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.types.util.SetUtils;

public class PositionsSequencePredicateUtils {
	
	public static Predicate<SlotAllocation> buy() {
		return sa -> sa.getSlotAllocationType() == SlotAllocationType.PURCHASE;
	}
	public static final Predicate<SlotAllocation> sell() {
		return sa -> sa.getSlotAllocationType() == SlotAllocationType.SALE;
	}

	public static boolean sell(SlotAllocation sa) {
		return sa.getSlotAllocationType() == SlotAllocationType.SALE;
	}

	public static @NonNull Predicate<SlotAllocation> isInPortGroup(PortGroup portGroup) {
		Set<?> ports = SetUtils.getObjects(portGroup);
		return sa -> ports.contains(sa.getPort());
	}
	
	public static @NonNull Predicate<SlotAllocation> isNotInPortGroups(List<PortGroup> portGroup) {
		Set<Port> ports = new HashSet<>();
		for (final PortGroup pg : portGroup) {
			SetUtils.addObjects(pg, ports);
		}
		return sa -> !ports.contains(sa.getPort());
	}
}
