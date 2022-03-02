/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.rateability.spacing.containers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.IntervalVar;

@NonNullByDefault
public class TravelContainer extends ShippedElementContainer {
	TravelContainer(final IntVar start, final IntVar end, final IntVar duration, final IntervalVar interval) {
		super(start, end, duration, interval);
	}
}
