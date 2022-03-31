package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.cargo.Cargo;

@NonNullByDefault
public interface IRollingWindow {

	public InventoryDateTimeEvent getLastEvent();
	
	public void stepForward();

	public boolean isLoading();

	public LocalDateTime getStartDateTime();
	
	public boolean canLift(final int allocationDrop);

	public void startLoad(final int allocationDrop);
	
	public List<ICargoBlueprint> startFixedLoad(final Cargo cargo, final LinkedList<ICargoBlueprint> generatedCargoBlueprints);
	
	public InventoryDateTimeEvent getCurrentEvent();
	
	public int getEndWindowTankMin();
	
	public int getEndWindowVolume();
}
