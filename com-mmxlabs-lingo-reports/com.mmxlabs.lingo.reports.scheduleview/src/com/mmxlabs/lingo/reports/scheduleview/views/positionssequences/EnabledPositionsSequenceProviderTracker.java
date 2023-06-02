/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.positionssequences;

import java.util.Collection;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class EnabledPositionsSequenceProviderTracker extends EnabledProviderTracker<PositionsSequenceProviderException> {
	
	public Optional<PositionsSequenceProviderException> toggleVisibilityOrGetError(String providerId) {
		if (isEnabled(providerId)) {
			return disableOrGetError(providerId);
		} else {
			enable(providerId);
			return Optional.empty();
		}
	}
	
	public void collectErrors(Iterable<ISchedulePositionsSequenceProviderExtension> exts, Schedule schedule, @NonNull Collection<@NonNull SlotVisit> slotVisitsToIgnore) {
		for (var ext: exts) {
			ISchedulePositionsSequenceProvider provider = ext.createInstance();
			try {
				provider.provide(schedule, slotVisitsToIgnore);
			} catch (PositionsSequenceProviderException e) {
				addError(provider.getId(), e);
			}
		}
		
	}
	
}
