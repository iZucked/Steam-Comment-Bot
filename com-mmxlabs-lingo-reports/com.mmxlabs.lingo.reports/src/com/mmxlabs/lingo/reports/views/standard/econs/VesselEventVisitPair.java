/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class VesselEventVisitPair extends DeltaPair {
	private final VesselEventVisit first;
	private final VesselEventVisit second;

	VesselEventVisitPair(final VesselEventVisit first, final VesselEventVisit second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public VesselEventVisit first() {
		return first;
	}

	@Override
	public VesselEventVisit second() {
		return second;
	}

	public static List<VesselEventVisitPair> generateVesselEventPair(final List<VesselEventVisit> vesselEventVisits) {
		Collections.sort(vesselEventVisits, (a, b) -> a.name().compareTo(b.name()));
		final List<VesselEventVisitPair> pairs = new ArrayList<>();
		// Edge case, only one lonely element in the list

		if (vesselEventVisits.size() == 1) {
			final VesselEventVisit a = vesselEventVisits.get(0);
			// pairs.add(new VesselEventVisitPair(a, null));
		}

		for (int i = 0; i < vesselEventVisits.size() - 1; i++) {
			final VesselEventVisit a = vesselEventVisits.get(i);
			final VesselEventVisit b = vesselEventVisits.get(i + 1);

			if (a.name().equals(b.name())) {
				pairs.add(new VesselEventVisitPair(a, b));
				i++;
			} else {
				// pairs.add(new VesselEventVisitPair(a, null));
			}
		}

		if (vesselEventVisits.size() > 1) {
			final VesselEventVisit a = vesselEventVisits.get(vesselEventVisits.size() - 2);
			final VesselEventVisit b = vesselEventVisits.get(vesselEventVisits.size() - 1);
			if (!a.name().equals(b.name())) {
				// pairs.add(new VesselEventVisitPair(b, null));
			}
		}
		return pairs;
	}

	@Override
	public String getName() {
		return first.name();
	}
}