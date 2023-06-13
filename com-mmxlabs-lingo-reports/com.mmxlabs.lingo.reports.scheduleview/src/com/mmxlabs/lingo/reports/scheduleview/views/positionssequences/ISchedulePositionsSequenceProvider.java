/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.positionssequences;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.PositionsSequence;

public interface ISchedulePositionsSequenceProvider {
	
	default List<@NonNull PositionsSequence> provide(Schedule schedule, @NonNull Collection<@NonNull SlotVisit> slotVisitsToIgnore) throws PositionsSequenceProviderException {
		return PositionsSequence.makeBuySellSequences(schedule, slotVisitsToIgnore, getId()).stream().filter(Predicate.not(p -> p.getDescription().equals("") && p.getElements().isEmpty())).toList();
	}
	
	default String getId() {
		return this.getClass().getName();
	}
}
