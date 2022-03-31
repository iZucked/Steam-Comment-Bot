package com.mmxlabs.models.lng.adp.mull.container;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class CargoBlueprint implements ICargoBlueprint {

	private final int loadCounter;
	private final IMullContainer mullContainer;
	private final IMudContainer mudContainer;
	private final IAllocationTracker allocationTracker;
	@Nullable
	private final Vessel vessel;
	private final int allocatedVolume;
	private LocalDateTime windowStart;
	private int windowSizeHours;

	public CargoBlueprint(final int loadCounter, final IMullContainer mullContainer, final IMudContainer mudContainer, final IAllocationTracker allocationTracker, @Nullable final Vessel vessel,
			final int allocatedVolume, final LocalDateTime windowStart, final int windowSizeHours) {
		this.loadCounter = loadCounter;
		this.mullContainer = mullContainer;
		this.mudContainer = mudContainer;
		this.allocationTracker = allocationTracker;
		this.vessel = vessel;
		this.allocatedVolume = allocatedVolume;
		this.windowStart = windowStart;
		final LocalDateTime endWindowDatetime = windowStart.plusHours(windowSizeHours);
		final LocalDateTime lastDateTimeOfMonth = LocalDateTime.of(YearMonth.from(windowStart).atEndOfMonth(), LocalTime.of(23, 0));
		this.windowSizeHours = lastDateTimeOfMonth.isBefore(endWindowDatetime) ? Hours.between(windowStart, lastDateTimeOfMonth) : windowSizeHours;
	}

	@Override
	public int getLoadCounter() {
		return loadCounter;
	}

	@Override
	public IMullContainer getMullContainer() {
		return mullContainer;
	}

	@Override
	public IMudContainer getMudContainer() {
		return mudContainer;
	}

	@Override
	public IAllocationTracker getAllocationTracker() {
		return allocationTracker;
	}

	@Override
	@Nullable
	public Vessel getVessel() {
		return vessel;
	}

	@Override
	public int getAllocatedVolume() {
		return allocatedVolume;
	}

	@Override
	public int getWindowSizeHours() {
		return windowSizeHours;
	}

	@Override
	public LocalDateTime getWindowStart() {
		return windowStart;
	}

	public void setWindowStart(final LocalDateTime newWindowStart) {
		this.windowStart = newWindowStart;
	}

	public void updateWindowSize(final int newWindowSizeHours) {
		if (this.windowSizeHours > newWindowSizeHours) {
			this.windowSizeHours = newWindowSizeHours;
		}
	}
}
