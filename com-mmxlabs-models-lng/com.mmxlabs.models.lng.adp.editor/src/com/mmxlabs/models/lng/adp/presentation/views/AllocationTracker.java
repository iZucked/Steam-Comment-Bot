package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditorMenuHelper;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public abstract class AllocationTracker {
	private final double relativeEntitlement;
	private long runningAllocation = 0L;
	protected final List<Vessel> vesselList;
	protected boolean sharesVessels;
	
	public AllocationTracker(final double relativeEntitlement, final List<Vessel> vesselList) {
		this.relativeEntitlement = relativeEntitlement;
		this.vesselList = vesselList;
	}
	
	public void updateRunningAllocation(final long allocationToShare) {
		this.runningAllocation += ((Double) (allocationToShare*this.relativeEntitlement)).longValue();
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
		return expectedBoiloff + (int) (vessel.getVesselOrDelegateCapacity()*vessel.getVesselOrDelegateFillCapacity() -  vessel.getVesselOrDelegateSafetyHeel());
	}
	
	public int calculateExpectedBoiloff(final Vessel vessel, final int loadDuration) {
		return (int) (loadDuration*(vessel.getLadenAttributes().getVesselOrDelegateInPortNBORate()/24.0));
	}
	
	public List<Vessel> getVessels() {
		return this.vesselList;
	}
	
	public void setVesselSharing(final Set<Vessel> vesselsToIntersect) {
		this.sharesVessels = this.vesselList.stream().anyMatch(vesselsToIntersect::contains);
	}
	
	public static LocalDate calculateDischargeDate(@NonNull final LoadSlot loadSlot, @NonNull DischargeSlot dischargeSlot, final Vessel vessel, @NonNull final IScenarioDataProvider sdp) {
		final int travelTime = CargoEditorMenuHelper.getTravelTime(loadSlot, dischargeSlot, vessel, sdp);
		if (travelTime == Integer.MAX_VALUE) {
			final String message = String.format("Cannot determine travel time between %s and %s.%n Travel time cannot be %d hours.", loadSlot.getPort().getName(), dischargeSlot.getPort().getName(), travelTime);
			throw new RuntimeException(message);
		}
		final SchedulingTimeWindow loadSTW = loadSlot.getSchedulingTimeWindow();
		return loadSTW.getStart().plusHours(travelTime + (long) loadSTW.getDuration()).withDayOfMonth(1).withHour(0).toLocalDate();
	}
	
	public abstract DischargeSlot createDischargeSlot(final CargoEditingCommands cec, List<Command> setCommands, final CargoModel cargoModel, @NonNull final IScenarioDataProvider sdp, final LoadSlot loadSlot, final Vessel vessel);
}