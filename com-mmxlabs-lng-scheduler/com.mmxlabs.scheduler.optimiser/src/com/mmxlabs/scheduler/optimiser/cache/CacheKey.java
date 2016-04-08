/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.cache;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public final class CacheKey<T> {

	private final @NonNull IVesselAvailability vesselAvailability;
	private final @NonNull IPortTimesRecord portTimesRecord;
	private final long startHeelInM3;

	private final @NonNull T record;

	private final @NonNull List<@NonNull DepCacheKey> dependencyKeys;

	private final int hash;

	public CacheKey(final @NonNull IVesselAvailability vesselAvailability, final long startHeelInM3, @NonNull final IPortTimesRecord portTimesRecord, @NonNull final T record) {
		this(vesselAvailability, startHeelInM3, portTimesRecord, record, Collections.emptyList());
	}

	public CacheKey(final @NonNull IVesselAvailability vesselAvailability, final long startHeelInM3, @NonNull final IPortTimesRecord portTimesRecord, @NonNull final T record,
			@NonNull List<@NonNull DepCacheKey> dependencyKeys) {
		this.vesselAvailability = vesselAvailability;
		this.startHeelInM3 = startHeelInM3;
		this.portTimesRecord = portTimesRecord;
		this.record = record;
		this.dependencyKeys = dependencyKeys;

		this.hash = Objects.hash(startHeelInM3, vesselAvailability, portTimesRecord.getSlots().stream().map(IPortSlot::getId).collect(Collectors.toList()), portTimesRecord.getFirstSlotTime(),
				dependencyKeys);
	}

	@Override
	public final int hashCode() {

		return hash;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj instanceof CacheKey) {
			final CacheKey<?> other = (CacheKey<?>) obj;
			final IPortSlot returnSlot = portTimesRecord.getReturnSlot();
			final IPortSlot otherReturnSlot = other.portTimesRecord.getReturnSlot();
			final boolean partA = startHeelInM3 == other.startHeelInM3 //
					&& Objects.equals(vesselAvailability, other.vesselAvailability) //
					&& Objects.equals(returnSlot, otherReturnSlot) //
					&& Objects.equals(portTimesRecord.getSlots(), other.portTimesRecord.getSlots());

			if (partA) {
				for (final IPortSlot slot : portTimesRecord.getSlots()) {
					if (portTimesRecord.getSlotDuration(slot) != other.portTimesRecord.getSlotDuration(slot)) {
						return false;
					}
					if (portTimesRecord.getSlotTime(slot) != other.portTimesRecord.getSlotTime(slot)) {
						return false;
					}
				}
				if (returnSlot != null && otherReturnSlot != null) {
					if (portTimesRecord.getSlotTime(returnSlot) != other.portTimesRecord.getSlotTime(otherReturnSlot)) {
						return false;
					}
				}

				return Objects.equals(this.dependencyKeys, other.dependencyKeys);
			}
		}
		return false;
	}

	public final @NonNull T getRecord() {
		return record;
	}

	public IVesselAvailability getVesselAvailability() {
		return vesselAvailability;
	}

	public IPortTimesRecord getPortTimesRecord() {
		return portTimesRecord;
	}

	public long getStartHeelInM3() {
		return startHeelInM3;
	}
}
