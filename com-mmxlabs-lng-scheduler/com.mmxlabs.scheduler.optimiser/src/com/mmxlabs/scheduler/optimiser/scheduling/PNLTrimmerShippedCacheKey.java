package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
import com.mmxlabs.scheduler.optimiser.scheduling.PNLBasedWindowTrimmerUtils.TimeChoice;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class PNLTrimmerShippedCacheKey {
	IVesselAvailability vesselAvailability;
	IResource resource;

	Map<IPortSlot, Collection<TimeChoice>> intervalMap;
	IPortTimeWindowsRecord ptwr;
	MinTravelTimeData minTimeData;

	ScheduledPlanInput spi;
	boolean lastPlan;
	private final int hash;
	private final Object vesselKey;

	public PNLTrimmerShippedCacheKey(final IResource resource, final IVesselAvailability vesselAvailability, final IPortTimeWindowsRecord portTimeWindowsRecord, final boolean lastPlan,
			final MinTravelTimeData minTimeData, final ScheduledPlanInput spi, final Map<IPortSlot, Collection<TimeChoice>> intervalMap) {
		this.vesselAvailability = vesselAvailability;
		this.resource = resource;
		this.ptwr = portTimeWindowsRecord;
		this.lastPlan = lastPlan;
		this.minTimeData = minTimeData;
		this.spi = spi;
		this.intervalMap = intervalMap;

		// Spot market vessels are equivalent
		final ISpotCharterInMarket spotCharterInMarket = vesselAvailability.getSpotCharterInMarket();
		if (spotCharterInMarket != null && vesselAvailability.getSpotIndex() >= 0) {
			this.vesselKey = spotCharterInMarket;
		} else {
			this.vesselKey = vesselAvailability;
		}
		// Add return interval size (if present) to hash
		final Collection<TimeChoice> collection = intervalMap.get(ptwr.getReturnSlot());
		final int sz = 0;// collection == null ? 0 : collection.size();
		this.hash = Objects.hash(vesselKey, lastPlan, portTimeWindowsRecord.getSlots(), spi.getPlanStartTime(), sz);
		// this.hash = Objects.hash(vesselKey, portTimeWindowsRecord, lastPlan, spi.getPlanStartTime());
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof PNLTrimmerShippedCacheKey) {
			final PNLTrimmerShippedCacheKey other = (PNLTrimmerShippedCacheKey) obj;

			final boolean valid = this.lastPlan == other.lastPlan //
					&& this.vesselKey == other.vesselKey //
					// && this.vesselAvailability == other.vesselAvailability //
					&& this.spi.getVesselStartTime() == other.spi.getVesselStartTime() //
					&& this.spi.getPlanStartTime() == other.spi.getPlanStartTime() //
					&& Objects.equals(this.spi.getPreviousHeelRecord(), other.spi.getPreviousHeelRecord());

			if (valid) {
				// Same return?
				if (!Objects.equals(ptwr.getReturnSlot(), other.ptwr.getReturnSlot())) {
					return false;
				}
				// Same intermediate?
				if (!Objects.equals(ptwr.getSlots(), other.ptwr.getSlots())) {
					return false;
				}

				final IPortSlot returnSlot = this.ptwr.getReturnSlot();
				if (!(returnSlot instanceof IEndPortSlot)) {
					final Collection<TimeChoice> aa = intervalMap.get(returnSlot);
					final Collection<TimeChoice> bb = other.intervalMap.get(returnSlot);
					if (!Objects.equals(aa, bb)) {
						return false;
					}
				}
				// TODO: MinTimeData and portTimeWindowRecords may contain required info - esp! panama bookings

				return true;
			}

		}
		return false;
	}

	public static PNLTrimmerShippedCacheKey from(final ScheduledRecord r, final IResource resource, final IVesselAvailability vesselAvailability, final IPortTimeWindowsRecord portTimeWindowsRecord,
			final boolean lastPlan, final Map<IPortSlot, Collection<TimeChoice>> intervalMap, final MinTravelTimeData minTimeData) {

		final Map<IPortSlot, Collection<TimeChoice>> subMap = new HashMap<>();
		for (final IPortSlot slot : portTimeWindowsRecord.getSlots()) {
			subMap.put(slot, intervalMap.get(slot));
		}
		if (portTimeWindowsRecord.getReturnSlot() != null) {
			subMap.put(portTimeWindowsRecord.getReturnSlot(), intervalMap.get(portTimeWindowsRecord.getReturnSlot()));
		}

		final ScheduledPlanInput spi = new ScheduledPlanInput(r.sequenceStartTime, r.currentEndTime, r.previousHeelRecord);

		return new PNLTrimmerShippedCacheKey(resource, vesselAvailability, portTimeWindowsRecord, lastPlan, minTimeData, spi, subMap);
	}

	public static PNLTrimmerShippedCacheKey forFirstRecord(final int vesselStartTime, final IResource resource, final IVesselAvailability vesselAvailability,
			final IPortTimeWindowsRecord portTimeWindowsRecord, final boolean lastPlan, final Map<IPortSlot, Collection<TimeChoice>> intervalMap, final MinTravelTimeData minTimeData2) {

		final Map<IPortSlot, Collection<TimeChoice>> subMap = new HashMap<>();
		for (final IPortSlot slot : portTimeWindowsRecord.getSlots()) {
			subMap.put(slot, intervalMap.get(slot));
		}
		if (portTimeWindowsRecord.getReturnSlot() != null) {
			subMap.put(portTimeWindowsRecord.getReturnSlot(), intervalMap.get(portTimeWindowsRecord.getReturnSlot()));
		}
		final ScheduledPlanInput spi = new ScheduledPlanInput(vesselStartTime, vesselStartTime, null);

		return new PNLTrimmerShippedCacheKey(resource, vesselAvailability, portTimeWindowsRecord, lastPlan, minTimeData2, spi, subMap);
	}
}