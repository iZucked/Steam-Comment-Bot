/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditorMenuHelper;
import com.mmxlabs.models.lng.cargo.util.CargoTravelTimeUtils;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public interface IAllocationTracker {

	public void setMonthlyAllocations(final Map<YearMonth, Integer> monthlyAllocations);

	public void setAllocatedAacq(final int currentAllocatedAacq);

	public int getAacq();

	public int getCurrentAllocatedAacq();

	public void updateRunningAllocation(final long allocationToShare);

	public long getRunningAllocation();

	public void dropAllocation(final long allocationDrop);

	public int calculateExpectedAllocationDrop(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final Map<Vessel, LocalDateTime> vesselToNextForwardUseTime,
			final int defaultAllocationDrop, final int loadDuration, final Set<Vessel> firstPartyVessels, final LocalDateTime currentDateTime);

	public int calculateExpectedAllocationDrop(final Vessel vessel, final int loadDuration, final boolean isSharedVessel);

	public int calculateExpectedBoiloff(final Vessel vessel, final int loadDuration, final boolean isSharedVessel);

	public List<Vessel> getVessels();

	public boolean isSharingVessels();

	public void setVesselSharing(final Set<Vessel> vesselsToIntersect);

	public void setVesselSharing(final boolean sharesVessels);

	public void undo(final ICargoBlueprint cargoBlueprint);

	public static LocalDate calculateDischargeDate(final LoadSlot loadSlot, DischargeSlot dischargeSlot, final Vessel vessel, final IScenarioDataProvider sdp,
			final Map<Vessel, @Nullable VesselCharter> vesselToVA) {
		final VesselCharter vesselCharter = vesselToVA.get(vessel);
		if (vesselCharter == null) {
			final int travelTime = CargoEditorMenuHelper.getTravelTime(loadSlot, dischargeSlot, vessel, sdp);
			if (travelTime == Integer.MAX_VALUE) {
				final String message = String.format("Cannot determine travel time between %s and %s.%n Travel time cannot be %d hours.", loadSlot.getPort().getName(),
						dischargeSlot.getPort().getName(), travelTime);
				throw new RuntimeException(message);
			}
			final SchedulingTimeWindow loadSTW = loadSlot.getSchedulingTimeWindow();
			return loadSTW.getStart().plusHours(travelTime + loadSTW.getDuration()).withDayOfMonth(1).withHour(0).toLocalDate();
		} else {
			final double vesselMaxSpeedKnots = Math.max(vessel.getVesselOrDelegateMaxSpeed(), 0.0);
			@NonNull
			final ModelDistanceProvider modelDistanceProvider = sdp.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
			final Integer minTime = CargoTravelTimeUtils.getFobMinTimeInHours(loadSlot, dischargeSlot, loadSlot.getWindowStart(), vesselCharter, ScenarioModelUtil.getPortModel(sdp),
					vesselMaxSpeedKnots, modelDistanceProvider);
			final SchedulingTimeWindow loadSTW = loadSlot.getSchedulingTimeWindow();
			final ZonedDateTime loadTimeZoneDischargeArrival = loadSTW.getStart().plusHours(minTime + (long) loadSTW.getDuration());
			final ZonedDateTime dischargeTimeZoneDischargeArrival = loadTimeZoneDischargeArrival
					.withZoneSameInstant(ZoneId.of(dischargeSlot.getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart())));
			final LocalDateTime localDischargeArrival = dischargeTimeZoneDischargeArrival.toLocalDateTime();

			return localDischargeArrival.withDayOfMonth(1).withHour(0).toLocalDate();
		}
	}

	public void dropFixedLoad(final Cargo cargo);

	public void dropFixedLoad(final int volumeLoaded);

	public boolean satisfiesAacq();

	public boolean satisfiedMonthlyAllocation();

	public void updateCurrentMonthAllocations(final YearMonth nextMonth);

	public boolean matches(final Cargo cargo);

	public void incrementCurrentAllocatedAacq();

	public void decrementCurrentAllocatedAacq();

	public void increaseRunningAllocation(final int amountToIncreaseBy);

	public void decreaseRunningAllocation(final int amountToDecreaseBy);

	public void incrementCurrentMonthLifted();

	public void decrementCurrentMonthLifted();
}
