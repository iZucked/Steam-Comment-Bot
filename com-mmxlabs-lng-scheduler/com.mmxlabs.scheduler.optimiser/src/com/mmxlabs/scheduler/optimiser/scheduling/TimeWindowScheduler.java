package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class TimeWindowScheduler implements ITimeWindowScheduler {

	@Inject
	private FeasibleTimeWindowTrimmer timeWindowTrimmer;

	@Inject
	private PriceBasedWindowTrimmer priceBasedWindowTrimmer;

	@Inject
	@Named(SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming)
	private boolean useCanalSlotBasedWindowTrimming = false;

	@Inject
	@Named(SchedulerConstants.Key_UsePriceBasedWindowTrimming)
	private boolean usePriceBasedWindowTrimming = false;

	@Override
	public ScheduledTimeWindows schedule(final @NonNull ISequences fullSequences) {

		final MinTravelTimeData travelTimeData = new MinTravelTimeData(fullSequences);

		final Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows = timeWindowTrimmer.generateTrimmedWindows(fullSequences, travelTimeData);

		if (useCanalSlotBasedWindowTrimming) {
			// panamaTrimmer.update_trimmedWindows, travelTimeData);
		}

		if (usePriceBasedWindowTrimming) {
			priceBasedWindowTrimmer.updateWindows(trimmedWindows, fullSequences, travelTimeData);
		}

		return new ScheduledTimeWindows(travelTimeData, trimmedWindows);
	}

	public boolean isUsePriceBasedWindowTrimming() {
		return usePriceBasedWindowTrimming;
	}

	public void setUsePriceBasedWindowTrimming(boolean usePriceBasedWindowTrimming) {
		this.usePriceBasedWindowTrimming = usePriceBasedWindowTrimming;
	}

}
