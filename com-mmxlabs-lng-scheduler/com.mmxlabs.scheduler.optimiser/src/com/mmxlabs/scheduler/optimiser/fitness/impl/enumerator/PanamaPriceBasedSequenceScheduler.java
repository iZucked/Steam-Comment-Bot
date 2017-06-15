package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

/**
 * <b>Not thread-safe</b>
 * 
 * @author robert
 *
 */
public class PanamaPriceBasedSequenceScheduler extends PriceBasedSequenceScheduler {

	private boolean trimmed = false;

	@Override
	public int @Nullable [][] schedule(@NonNull final ISequences sequences) {
		setSequences(sequences);

		prepare();
		trimPanama();
		endsSet = new boolean[arrivalTimes.length];

		sequentialEarliestTimePriceBasedWindowTrimming(sequences, portTimeWindowsRecords);
		for (int index = 0; index < sequences.size(); ++index) {
			setTimeWindowsToEarliest(index);
		}
		synchroniseShipToShipBindings();

		trimmed = true;

		return arrivalTimes;
	}

	public AvailableRouteChoices[][] canalDecision() {
		if (!trimmed) {
			throw new IllegalStateException("Canal decisions only exist after trimming");
		}
		return throughPanama;
	}

	public IRouteOptionBooking[][] slotsAssigned() {
		if (!trimmed) {
			throw new IllegalStateException("Canal decisions only exist after trimming");
		}
		return routeOptionBookings;
	}
}
