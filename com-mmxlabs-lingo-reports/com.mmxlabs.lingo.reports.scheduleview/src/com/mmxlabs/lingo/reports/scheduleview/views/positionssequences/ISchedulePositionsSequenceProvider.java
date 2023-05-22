package com.mmxlabs.lingo.reports.scheduleview.views.positionssequences;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.PositionsSequence;

public interface ISchedulePositionsSequenceProvider {
	
	default List<@NonNull PositionsSequence> provide(Schedule schedule) throws PositionsSequenceProviderException {
		return PositionsSequence.makeBuySellSequences(schedule, getId()).stream().filter(Predicate.not(p -> p.getDescription().equals("") && p.getElements().isEmpty())).toList();
	}
	
	default String getId() {
		return this.getClass().getName();
	}
}
