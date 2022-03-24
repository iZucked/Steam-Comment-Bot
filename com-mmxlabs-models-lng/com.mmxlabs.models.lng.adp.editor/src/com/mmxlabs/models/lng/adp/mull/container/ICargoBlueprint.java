package com.mmxlabs.models.lng.adp.mull.container;

import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public interface ICargoBlueprint {

	IMullContainer getMullContainer();

	IMudContainer getMudContainer();

	IAllocationTracker getAllocationTracker();

	LocalDateTime getWindowStart();

	int getAllocatedVolume();

	void setWindowStart(LocalDateTime newWindowStart);

	@Nullable
	Vessel getVessel();

	int getLoadCounter();

	void updateWindowSize(int newWindowSizeHours);

	int getWindowSizeHours();
}
