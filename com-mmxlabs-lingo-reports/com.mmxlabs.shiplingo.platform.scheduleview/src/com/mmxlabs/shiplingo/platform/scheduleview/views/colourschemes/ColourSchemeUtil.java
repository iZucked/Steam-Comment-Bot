package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;

public class ColourSchemeUtil {

	static final float threshold = 0.95f;
	static final float speed = 19.0f;

	
	
	static Idle findIdleForJourney(final Journey journey) {
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

	static Journey findJourneyForIdle(final Idle idle) {
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

	static boolean isRiskyVoyage(final Journey journey, final Idle idle, final float speed, final float threshold) {

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
