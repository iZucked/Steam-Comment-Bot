/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.cache.IWriteLockable;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.scheduling.PNLBasedWindowTrimmerUtils.TimeChoice;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

@NonNullByDefault
public final class PNLTrimmerShippedCacheKey {
	final IVesselCharter vesselCharter;
	final IResource resource;
	final @Nullable IPort firstLoadPort;

	final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap;
	final IPortTimeWindowsRecord ptwr;
	final MinTravelTimeData minTimeData;
	final ISequencesAttributesProvider sequencesAttributesProvider;

	final ScheduledPlanInput spi;
	final boolean lastPlan;
	private final int hash;
	private final Object vesselKey;
	private @Nullable List<Integer> calculators;

	private PNLTrimmerShippedCacheKey(final IResource resource, final IVesselCharter vesselCharter, final @Nullable IPort firstLoadPort, final IPortTimeWindowsRecord portTimeWindowsRecord,
			final boolean lastPlan, final MinTravelTimeData minTimeData, final ScheduledPlanInput spi, final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap,
			ISequencesAttributesProvider sequencesAttributesProvider) {
		this.vesselCharter = vesselCharter;
		this.resource = resource;
		this.firstLoadPort = firstLoadPort;
		this.ptwr = portTimeWindowsRecord;
		this.lastPlan = lastPlan;
		this.minTimeData = minTimeData;
		this.spi = spi;
		this.intervalMap = intervalMap;
		this.sequencesAttributesProvider = sequencesAttributesProvider;

		// // Spot market vessels are equivalent
		final ISpotCharterInMarket spotCharterInMarket = vesselCharter.getSpotCharterInMarket();
		if (spotCharterInMarket != null && vesselCharter.getSpotIndex() >= 0) {
			this.vesselKey = spotCharterInMarket;
		} else {
			this.vesselKey = vesselCharter;
		}

		final String firstLoadPortName = getPortName(firstLoadPort);
		this.hash = Objects.hash(vesselKey, firstLoadPortName, lastPlan, portTimeWindowsRecord.getSlots(), spi.getPlanStartTime());

		IWriteLockable.writeLock(ptwr);
		IWriteLockable.writeLock(minTimeData);

		// Grab break-even prices
		for (var portSlot : portTimeWindowsRecord.getSlots()) {
			if (portSlot instanceof ILoadOption loadOption) {
				if (loadOption.getLoadPriceCalculator() instanceof IBreakEvenPriceCalculator c) {
					if (calculators == null) {
						calculators = new LinkedList<>();
					}
					calculators.add(c.getPrice());
				}
			} else if (portSlot instanceof IDischargeOption dischargeOption) {
				if (dischargeOption.getDischargePriceCalculator() instanceof IBreakEvenPriceCalculator c) {
					if (calculators == null) {
						calculators = new LinkedList<>();
					}
					calculators.add(c.getPrice());
				}
			}
		}
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

		if (obj instanceof PNLTrimmerShippedCacheKey other) {

			if (!Objects.equals(sequencesAttributesProvider, other.sequencesAttributesProvider)) {
				return false;
			}

			final boolean valid = this.lastPlan == other.lastPlan //
					&& this.vesselKey == other.vesselKey //
					&& Objects.equals(getPortName(this.firstLoadPort), getPortName(other.firstLoadPort)) //
					// && this.vesselCharter == other.vesselCharter //
					&& Objects.equals(this.spi, other.spi) //
					&& Objects.equals(this.calculators, other.calculators);

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

	public static PNLTrimmerShippedCacheKey from(final ScheduledRecord r, final @Nullable IPort firstLoad, final IResource resource, final IVesselCharter vesselCharter,
			final IPortTimeWindowsRecord portTimeWindowsRecord, final boolean lastPlan, final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap, final MinTravelTimeData minTimeData,
			ISequencesAttributesProvider sequencesAttributesProvider) {

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

		return new PNLTrimmerShippedCacheKey(resource, vesselCharter, firstLoad, portTimeWindowsRecord, lastPlan, minTimeData, spi, subMap.build(), sequencesAttributesProvider);
	}

	public static PNLTrimmerShippedCacheKey forFirstRecord(final int vesselStartTime, final @Nullable IPort firstLoad, final IResource resource, final IVesselCharter vesselCharter,
			final IPortTimeWindowsRecord portTimeWindowsRecord, final boolean lastPlan, final ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> intervalMap, final MinTravelTimeData minTimeData2,
			ISequencesAttributesProvider sequencesAttributesProvider) {

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

		return new PNLTrimmerShippedCacheKey(resource, vesselCharter, firstLoad, portTimeWindowsRecord, lastPlan, minTimeData2, spi, subMap.build(), sequencesAttributesProvider);
	}
}