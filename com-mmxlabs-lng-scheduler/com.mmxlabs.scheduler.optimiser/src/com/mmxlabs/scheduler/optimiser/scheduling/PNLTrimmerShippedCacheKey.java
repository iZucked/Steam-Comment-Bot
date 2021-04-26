/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.cache.IWriteLockable;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.scheduling.PNLBasedWindowTrimmerUtils.TimeChoice;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

@NonNullByDefault
public final class PNLTrimmerShippedCacheKey {
	final IVesselAvailability vesselAvailability;
	final IResource resource;
	final @Nullable IPort firstLoadPort;

	final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap;
	final IPortTimeWindowsRecord ptwr;
	final MinTravelTimeData minTimeData;

	final ScheduledPlanInput spi;
	final boolean lastPlan;
	private final int hash;
	private final Object vesselKey;

	private PNLTrimmerShippedCacheKey(final IResource resource, final IVesselAvailability vesselAvailability, final @Nullable IPort firstLoadPort, final IPortTimeWindowsRecord portTimeWindowsRecord,
			final boolean lastPlan, final MinTravelTimeData minTimeData, final ScheduledPlanInput spi, final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap) {
		this.vesselAvailability = vesselAvailability;
		this.resource = resource;
		this.firstLoadPort = firstLoadPort;
		this.ptwr = portTimeWindowsRecord;
		this.lastPlan = lastPlan;
		this.minTimeData = minTimeData;
		this.spi = spi;
		this.intervalMap = intervalMap;

		// // Spot market vessels are equivalent
		final ISpotCharterInMarket spotCharterInMarket = vesselAvailability.getSpotCharterInMarket();
		if (spotCharterInMarket != null && vesselAvailability.getSpotIndex() >= 0) {
			this.vesselKey = spotCharterInMarket;
		} else {
			this.vesselKey = vesselAvailability;
		}

		final String firstLoadPortName = getPortName(firstLoadPort);
		this.hash = Objects.hash(vesselKey, firstLoadPortName, lastPlan, portTimeWindowsRecord.getSlots(), spi.getPlanStartTime());

		IWriteLockable.writeLock(ptwr);
		IWriteLockable.writeLock(minTimeData);
	}

	private String getPortName(final @Nullable IPort port) {
		final String portName = port == null ? "" : port.getName();
		return portName;
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
					&& Objects.equals(getPortName(this.firstLoadPort), getPortName(other.firstLoadPort)) //
					// && this.vesselAvailability == other.vesselAvailability //
					&& Objects.equals(this.spi, other.spi);

			if (valid) {
				// Same input record?
				if (!Objects.equals(ptwr, other.ptwr)) {
					return false;
				}

				// Check we have the same search range for the record.
				if (!Objects.equals(intervalMap, other.intervalMap)) {
					return false;
				}

				// Check the relevant sub-section of the travel time array
				for (final IPortSlot s : ptwr.getSlots()) {

					final int idx = ptwr.getIndex(s);
					if (minTimeData.getMinTravelTime(idx) != other.minTimeData.getMinTravelTime(idx)) {
						return false;
					}
					for (final ERouteOption ro : ERouteOption.values()) {
						if (minTimeData.getTravelTime(ro, idx) != other.minTimeData.getTravelTime(ro, idx)) {
							return false;
						}
					}
				}
				return true;
			}

		}
		return false;
	}

	public static PNLTrimmerShippedCacheKey from(final ScheduledRecord r, final @Nullable IPort firstLoad, final IResource resource, final IVesselAvailability vesselAvailability,
			final IPortTimeWindowsRecord portTimeWindowsRecord, final boolean lastPlan, final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap, final MinTravelTimeData minTimeData) {

		final ImmutableMap.Builder<IPortSlot, ImmutableList<TimeChoice>> subMap = new ImmutableMap.Builder<>();
		for (final IPortSlot slot : portTimeWindowsRecord.getSlots()) {
			assert slot != null;
			subMap.put(slot, ImmutableList.copyOf(intervalMap.get(slot)));
		}
		IPortSlot returnSlot = portTimeWindowsRecord.getReturnSlot();
		if (returnSlot != null) {
			subMap.put(returnSlot, ImmutableList.copyOf(intervalMap.get(returnSlot)));
		}

		final ScheduledPlanInput spi = new ScheduledPlanInput(r.sequenceStartTime, r.currentEndTime, r.previousHeelRecord);

		return new PNLTrimmerShippedCacheKey(resource, vesselAvailability, firstLoad, portTimeWindowsRecord, lastPlan, minTimeData, spi, subMap.build());
	}

	public static PNLTrimmerShippedCacheKey forFirstRecord(final int vesselStartTime, final @Nullable IPort firstLoad, final IResource resource, final IVesselAvailability vesselAvailability,
			final IPortTimeWindowsRecord portTimeWindowsRecord, final boolean lastPlan, final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap, final MinTravelTimeData minTimeData2) {

		final ImmutableMap.Builder<IPortSlot, ImmutableList<TimeChoice>> subMap = new ImmutableMap.Builder<>();
		for (final IPortSlot slot : portTimeWindowsRecord.getSlots()) {
			assert slot != null;
			subMap.put(slot, ImmutableList.copyOf(intervalMap.get(slot)));
		}
		IPortSlot returnSlot = portTimeWindowsRecord.getReturnSlot();
		if (returnSlot != null) {
			subMap.put(returnSlot, ImmutableList.copyOf(intervalMap.get(returnSlot)));
		}
		final ScheduledPlanInput spi = new ScheduledPlanInput(vesselStartTime, vesselStartTime, null);

		return new PNLTrimmerShippedCacheKey(resource, vesselAvailability, firstLoad, portTimeWindowsRecord, lastPlan, minTimeData2, spi, subMap.build());
	}
}