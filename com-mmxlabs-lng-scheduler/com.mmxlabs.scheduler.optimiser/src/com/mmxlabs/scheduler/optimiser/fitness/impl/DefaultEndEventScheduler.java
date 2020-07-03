/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;

public class DefaultEndEventScheduler implements IEndEventScheduler {

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	@Named(ENABLE_HIRE_COST_ONLY_END_RULE)
	private boolean enabelHireCostEndRule;

	@Override
	public List<IPortTimesRecord> scheduleEndEvent(final IResource resource, final IVesselAvailability vesselAvailability, final PortTimesRecord partialPortTimesRecord, final int scheduledTime,
			@NonNull final IPortSlot endEventSlot) {
		final List<IPortTimesRecord> additionalRecords = new LinkedList<>();
		@NonNull
		final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);

		if ((vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET || vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {

			if (endRequirement.isHireCostOnlyEndRule()) {
				if (enabelHireCostEndRule) {
					return scheduleOpenEndedVessel(vesselAvailability, partialPortTimesRecord, scheduledTime, endEventSlot);
				}
			}
		}

		partialPortTimesRecord.setReturnSlotTime(endEventSlot, scheduledTime);
		partialPortTimesRecord.setSlotDuration(endEventSlot, 0);

		// Create new PTR to for end event. This is to allow us to always attach P&L to
		// end events.
		// Note: This fouls up the GCO stuff for the end event.
		final PortTimesRecord endPortTimesRecord = new PortTimesRecord();
		endPortTimesRecord.setSlotTime(endEventSlot, scheduledTime);

		endPortTimesRecord.setSlotDuration(endEventSlot, 0);

		additionalRecords.add(endPortTimesRecord);

		return additionalRecords;
	}

	protected @NonNull List<@NonNull IPortTimesRecord> scheduleOpenEndedVessel(final @NonNull IVesselAvailability vesselAvailability, final @NonNull PortTimesRecord partialPortTimesRecord,
			final int scheduledTime, final @NonNull IPortSlot endEventSlot) {

		final IPortSlot prevPortSlot = partialPortTimesRecord.getSlots().get(partialPortTimesRecord.getSlots().size() - 1);

		assert prevPortSlot != null;
		final int prevArrivalTime = partialPortTimesRecord.getSlotTime(prevPortSlot);
		final int prevVisitDuration = partialPortTimesRecord.getSlotDuration(prevPortSlot);
		final int extraIdleTime = partialPortTimesRecord.getSlotExtraIdleTime(prevPortSlot);

		// TODO: Quickest != most economical routing
		final int availableTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), prevPortSlot.getPort(), endEventSlot.getPort(), vesselAvailability.getVessel().getMaxSpeed(),
				partialPortTimesRecord.getSlotNextVoyageOptions(prevPortSlot)).getSecond();
		final int shortCargoReturnArrivalTime = prevArrivalTime + prevVisitDuration + extraIdleTime + availableTime;

		partialPortTimesRecord.setReturnSlotTime(endEventSlot, shortCargoReturnArrivalTime);
		partialPortTimesRecord.setSlotDuration(endEventSlot, 0);
		partialPortTimesRecord.setSlotExtraIdleTime(endEventSlot, 0);

		// Create new PTR to record end event duration.
		// Note: This fouls up the GCO stuff for the end event.
		final PortTimesRecord endPortTimesRecord = new PortTimesRecord();
		endPortTimesRecord.setSlotTime(endEventSlot, shortCargoReturnArrivalTime);

		final int duration = scheduledTime - shortCargoReturnArrivalTime;
		endPortTimesRecord.setSlotDuration(endEventSlot, duration);

		return Collections.singletonList(endPortTimesRecord);
	}

}
