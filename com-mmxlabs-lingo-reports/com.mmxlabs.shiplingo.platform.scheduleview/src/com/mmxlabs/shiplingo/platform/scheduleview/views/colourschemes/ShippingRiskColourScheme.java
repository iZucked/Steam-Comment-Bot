/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.shiplingo.platform.scheduleview.views.IScheduleViewColourScheme;

public class ShippingRiskColourScheme implements IScheduleViewColourScheme {

	private static final float threshold = 0.95f;
	private static final float speed = 19.0f;

	@Override
	public String getName() {
		return "Shipping Risk";
	}

	@Override
	public Color getForeground(final Object element) {
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;

			if (isRiskyVoyage(journey, findIdleForJourney(journey), speed, threshold)) {
				return ColorCache.getColor(200, 0, 0);
			}
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;

			if (isRiskyVoyage(findJourneyForIdle(idle), idle, speed, threshold)) {
				return ColorCache.getColor(200, 0, 0);
			}
		}
		return null;
	}

	private Idle findIdleForJourney(final Journey journey) {
		final Sequence sequence = (Sequence) journey.eContainer();
		final int index = sequence.getEvents().indexOf(journey);
		if (index != -1 && index + 1 < sequence.getEvents().size()) {
			final Event event = sequence.getEvents().get(index + 1);
			if (event instanceof Idle) {
				return (Idle) event;
			}
		}
		return null;
	}

	private Journey findJourneyForIdle(final Idle idle) {
		final Sequence sequence = (Sequence) idle.eContainer();
		final int index = sequence.getEvents().indexOf(idle);
		if (index != -1 && index - 1 >= 0) {
			final Event event = sequence.getEvents().get(index - 1);
			if (event instanceof Journey) {
				return (Journey) event;
			}
		}
		return null;
	}

	private boolean isRiskyVoyage(final Journey journey, final Idle idle, final float speed, final float threshold) {

		if (journey == null) {
			return false;
		}

		final int distance = journey.getDistance();
		int totalTime = journey.getDuration();
		if (idle != null) {
			totalTime += idle.getDuration();
		}

		final int travelTime = (int) Math.round((float) distance / speed);

		return (travelTime / totalTime > threshold);
	}
}
