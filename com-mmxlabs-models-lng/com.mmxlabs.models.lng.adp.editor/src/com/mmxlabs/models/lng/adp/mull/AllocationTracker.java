/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.MullAllocationRow;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditorMenuHelper;
import com.mmxlabs.models.lng.cargo.util.CargoTravelTimeUtils;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public abstract class AllocationTracker {
	private final double relativeEntitlement;
	protected long runningAllocation = 0L;
	protected final List<Vessel> vesselList;
	protected boolean sharesVessels;
	protected final int aacq;

	protected AllocationTracker(final MullAllocationRow row, final double totalWeight) {
		this.aacq = row.getWeight();
		this.relativeEntitlement = this.aacq / totalWeight;
		this.vesselList = row.getVessels();
	}

	public int getAACQ() {
		return this.aacq;
	}

	public void updateRunningAllocation(final long allocationToShare) {
		this.runningAllocation += ((Double) (allocationToShare * this.relativeEntitlement)).longValue();
	}

	public long getRunningAllocation() {
		return this.runningAllocation;
	}

	public void dropAllocation(final long allocationDrop) {
		this.runningAllocation -= allocationDrop;
	}

	public int calculateExpectedAllocationDrop(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final int defaultAllocationDrop, final int loadDuration) {
		if (vesselList.isEmpty()) {
			return defaultAllocationDrop;
		}
		final Vessel expectedVessel = this.vesselList.stream() //
				.min((v1, v2) -> vesselToMostRecentUseDateTime.get(v1).compareTo(vesselToMostRecentUseDateTime.get(v2))) //
				.get();
		return calculateExpectedAllocationDrop(expectedVessel, loadDuration);
	}

	public int calculateExpectedAllocationDrop(final Vessel vessel, final int loadDuration) {
		final int expectedBoiloff = calculateExpectedBoiloff(vessel, loadDuration);
		return expectedBoiloff + (int) (vessel.getVesselOrDelegateCapacity() * vessel.getVesselOrDelegateFillCapacity() - vessel.getVesselOrDelegateSafetyHeel());
	}

	public int calculateExpectedBoiloff(final Vessel vessel, final int loadDuration) {
		return (int) (loadDuration * (vessel.getLadenAttributes().getVesselOrDelegateInPortNBORate() / 24.0));
	}

	public List<Vessel> getVessels() {
		return this.vesselList;
	}

	public boolean isSharingVessels() {
		return this.sharesVessels;
	}

	public void setVesselSharing(final Set<Vessel> vesselsToIntersect) {
		this.sharesVessels = this.vesselList.stream().anyMatch(vesselsToIntersect::contains);
	}

	public void setVesselSharing(final boolean sharesVessels) {
		this.sharesVessels = sharesVessels;
	}

	public void undo(final CargoBlueprint cargoBlueprint) {
		if (this.equals(cargoBlueprint.getAllocationTracker())) {
			this.runningAllocation += cargoBlueprint.getAllocatedVolume();
		}
	}

	public static LocalDate calculateDischargeDate(@NonNull final LoadSlot loadSlot, @NonNull DischargeSlot dischargeSlot, final Vessel vessel, @NonNull final IScenarioDataProvider sdp, final Map<Vessel, VesselAvailability> vesselToVA, final LNGScenarioModel sm) {
		final VesselAvailability vesselAvailability = vesselToVA.get(vessel);
		if (vesselAvailability == null) {
			final int travelTime = CargoEditorMenuHelper.getTravelTime(loadSlot, dischargeSlot, vessel, sdp);
			if (travelTime == Integer.MAX_VALUE) {
				final String message = String.format("Cannot determine travel time between %s and %s.%n Travel time cannot be %d hours.", loadSlot.getPort().getName(), dischargeSlot.getPort().getName(),
						travelTime);
				throw new RuntimeException(message);
			}
			final SchedulingTimeWindow loadSTW = loadSlot.getSchedulingTimeWindow();
			return loadSTW.getStart().plusHours(travelTime + loadSTW.getDuration()).withDayOfMonth(1).withHour(0).toLocalDate();
		} else {
			final double vesselMaxSpeedKnots = Math.max(vessel.getVesselOrDelegateMaxSpeed(), 0.0);
			@NonNull
			final ModelDistanceProvider modelDistanceProvider = sdp.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
			final Integer minTime = CargoTravelTimeUtils.getFobMinTimeInHours(loadSlot, dischargeSlot, loadSlot.getWindowStart(), vesselAvailability, ScenarioModelUtil.getPortModel(sm), vesselMaxSpeedKnots, modelDistanceProvider);
			final SchedulingTimeWindow loadSTW = loadSlot.getSchedulingTimeWindow();
			return loadSTW.getStart().plusHours(minTime + (long) loadSTW.getDuration()).withDayOfMonth(1).withHour(0).toLocalDate();
		}
	}

	public abstract DischargeSlot createDischargeSlot(final CargoEditingCommands cec, List<Command> setCommands, final CargoModel cargoModel, @NonNull final IScenarioDataProvider sdp,
			final LoadSlot loadSlot, final Vessel vessel, final Map<Vessel, VesselAvailability> vesselToVA, final LNGScenarioModel sm);

	public abstract void dropFixedLoad(final Cargo cargo);
}