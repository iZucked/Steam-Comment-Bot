/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.positionssequences;

import java.util.Optional;

import com.mmxlabs.models.lng.schedule.util.PositionsSequence;

public class EnabledPositionsSequenceProviderTracker extends EnabledProviderTracker<PositionsSequenceProviderException> {
	
	public boolean isVisible(PositionsSequence ps) {
		return isEnabledWithNoError(ps.getProviderId()) || hasNoCorrectlyEnabledExtensions() && !ps.isPartition();
	}
	
	public Optional<PositionsSequenceProviderException> toggleVisibilityOrGetError(String providerId) {
		if (isEnabled(providerId)) {
			return disableOrGetError(providerId);
		} else {
			enable(providerId);
			return Optional.empty();
		}
	}
	
}
