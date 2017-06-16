package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public class ArrivalTimeScheduler {

	@Inject
	private TimeWindowScheduler timeWindowScheduler;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private PortTimesRecordMaker portTimesRecordMaker;

	@Inject
	private ISlotTimeScheduler slotTimeScheduler;

	public Map<IResource, List<@NonNull IPortTimesRecord>> schedule(final @NonNull ISequences fullSequences) {

		final ScheduledTimeWindows scheduledTimeWindows = timeWindowScheduler.schedule(fullSequences);

		final Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows = scheduledTimeWindows.getTrimmedTimeWindowsMap();

		final Map<IResource, List<IPortTimesRecord>> portTimeRecords = new HashMap<>();
		//
		for (int seqIndex = 0; seqIndex < fullSequences.getResources().size(); seqIndex++) {
			final IResource resource = fullSequences.getResources().get(seqIndex);
			final ISequence sequence = fullSequences.getSequence(resource);
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

			final MinTravelTimeData travelTimeData = scheduledTimeWindows.getTravelTimeData().get(resource);

			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

				final IPortTimesRecord record = portTimesRecordMaker.makeDESOrFOBPortTimesRecord(resource, sequence);
				if (record != null) {
					portTimeRecords.put(resource, Lists.newArrayList(record));
				}
			} else {
				portTimeRecords.put(resource, portTimesRecordMaker.makeShippedPortTimesRecords(seqIndex, resource, sequence, trimmedWindows.get(resource), travelTimeData, slotTimeScheduler));
			}
		}

		return portTimeRecords;
	}
}
