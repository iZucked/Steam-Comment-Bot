/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;

@NonNullByDefault
public class RollingLoadWindow implements IRollingWindow {

	protected final int loadDuration;

	protected int endWindowVolume = 0;
	protected int endWindowTankMin = 0;
	protected int remainingLoadHours = 0;
	protected LocalDateTime startDateTime;
	protected int headLoadVolume;
	protected int tailLoadVolume;
	protected int beforeWindowTankVolume;
	protected boolean isLoadingExisting = false;

	protected final LinkedList<InventoryDateTimeEvent> frontWindow = new LinkedList<>();
	protected final LinkedList<InventoryDateTimeEvent> backWindow = new LinkedList<>();
	protected final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> inventoryEventIter;
	protected int currentLoadTime;

	public RollingLoadWindow(final InventoryGlobalState inventoryGlobalState, final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> entries, final int maxExistingLoadDuration,
			final int initialTankVolume) {
		this.beforeWindowTankVolume = initialTankVolume;
		this.endWindowVolume = initialTankVolume;
		this.loadDuration = inventoryGlobalState.getLoadDuration();

		this.remainingLoadHours = 0;

		for (int i = 0; i < this.loadDuration; ++i) {
			if (entries.hasNext()) {
				final InventoryDateTimeEvent event = entries.next().getValue();
				frontWindow.add(event);
				endWindowVolume += event.getNetVolumeIn();
				endWindowTankMin = event.minVolume;
			}
		}
		final int extraTrackedWindowSize = maxExistingLoadDuration - loadDuration;
		for (int i = 0; i < extraTrackedWindowSize; ++i) {
			if (entries.hasNext()) {
				final InventoryDateTimeEvent event = entries.next().getValue();
				backWindow.add(event);
				endWindowVolume += event.getNetVolumeIn();
				endWindowTankMin = event.minVolume;
			}
		}
		this.inventoryEventIter = entries;
		this.startDateTime = frontWindow.getFirst().getDateTime();
	}

	@Override
	public InventoryDateTimeEvent getLastEvent() {
		return backWindow.isEmpty() ? frontWindow.getLast() : backWindow.getLast();
	}

	@Override
	public void stepForward() {
		final InventoryDateTimeEvent newEvent;
		if (this.inventoryEventIter.hasNext()) {
			newEvent = this.inventoryEventIter.next().getValue();
		} else {
			final InventoryDateTimeEvent previousDateTimeEvent = this.getLastEvent();
			newEvent = new InventoryDateTimeEvent(previousDateTimeEvent.getDateTime().plusHours(1L), 0, previousDateTimeEvent.minVolume, previousDateTimeEvent.maxVolume);
		}

		final InventoryDateTimeEvent droppedEvent = frontWindow.removeFirst();
		backWindow.add(newEvent);
		final InventoryDateTimeEvent transferredEvent = backWindow.removeFirst();
		frontWindow.add(transferredEvent);

		beforeWindowTankVolume += droppedEvent.getNetVolumeIn();
		endWindowVolume += transferredEvent.getNetVolumeIn();
		endWindowTankMin = transferredEvent.minVolume;
		this.startDateTime = frontWindow.getFirst().getDateTime();

		if (remainingLoadHours > 0) {
			final int volumeDrop = remainingLoadHours == this.currentLoadTime ? this.headLoadVolume : this.tailLoadVolume;
			beforeWindowTankVolume -= volumeDrop;
			endWindowVolume -= volumeDrop;
			remainingLoadHours--;
			if (remainingLoadHours == 0) {
				this.isLoadingExisting = false;
			}
		}
	}

	@Override
	public boolean isLoading() {
		return this.remainingLoadHours > 0;
	}

	@Override
	public LocalDateTime getStartDateTime() {
		return this.startDateTime;
	}

	@Override
	public boolean canLift(int allocationDrop) {
		if (this.endWindowVolume >= allocationDrop + this.endWindowTankMin) {
			int localTankVolume = this.beforeWindowTankVolume;
			final int localTailLoad = allocationDrop / this.loadDuration;
			final int localHeadLoad = localTailLoad + (allocationDrop % this.loadDuration);
			final Iterator<InventoryDateTimeEvent> iter = this.frontWindow.iterator();
			InventoryDateTimeEvent currentEvent = iter.next();
			int currentVolumeChange = currentEvent.getNetVolumeIn() - localHeadLoad;
			localTankVolume += currentVolumeChange;
			if (localTankVolume < currentEvent.minVolume) {
				return false;
			}
			for (int i = 1; i < this.loadDuration; ++i) {
				currentEvent = iter.next();
				currentVolumeChange = currentEvent.getNetVolumeIn() - localTailLoad;
				localTankVolume += currentVolumeChange;
				if (localTankVolume < currentEvent.minVolume) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	protected void startLoad(final int allocationDrop, final int duration) {
		this.tailLoadVolume = allocationDrop / duration;
		this.headLoadVolume = this.tailLoadVolume + (allocationDrop % duration);
		this.remainingLoadHours = duration;
		this.currentLoadTime = duration;
	}

	@Override
	public void startLoad(final int allocationDrop) {
		startLoad(allocationDrop, this.loadDuration);
	}

	@Override
	public List<ICargoBlueprint> startFixedLoad(final Cargo cargo, final LinkedList<ICargoBlueprint> generatedCargoBlueprints) {
		// Undo any partially active load unless it is a fixed load
		final List<ICargoBlueprint> cargoBluePrintsToUndo = new LinkedList<>();
		if (!this.isLoadingExisting && this.remainingLoadHours > 0) {
			final int volumeToRestore = this.headLoadVolume + (this.loadDuration - this.remainingLoadHours - 1) * this.tailLoadVolume;
			cargoBluePrintsToUndo.add(generatedCargoBlueprints.removeLast());
			this.beforeWindowTankVolume += volumeToRestore;
			this.endWindowVolume += volumeToRestore;
		}

		// Start fixed load
		final LoadSlot loadSlot = (LoadSlot) cargo.getSlots().get(0);
		final int expectedLoadAmount = loadSlot.getMaxQuantity();
		final int duration = loadSlot.isSetDuration() ? loadSlot.getDuration() : this.loadDuration;
		startLoad(expectedLoadAmount, duration);
		isLoadingExisting = true;

		// Undo any generated cargoes that conflict with current fixed load
		final int localTailLoad = expectedLoadAmount / duration;
		final int localHeadLoad = localTailLoad + (expectedLoadAmount % duration);
		final int frontHourCount = Math.min(duration, this.loadDuration);
		final int backHourCount = Math.max(0, duration - this.loadDuration);
		while (!generatedCargoBlueprints.isEmpty() && !canHourlyLift(frontHourCount, backHourCount, localHeadLoad, localTailLoad)) {
			final ICargoBlueprint nextUndo = generatedCargoBlueprints.removeLast();
			cargoBluePrintsToUndo.add(nextUndo);
			final int volumeToRestore = nextUndo.getAllocatedVolume();
			this.beforeWindowTankVolume += volumeToRestore;
			this.endWindowVolume += volumeToRestore;
		}
		return cargoBluePrintsToUndo;
	}

	private boolean canHourlyLift(final int frontHourCount, final int backHourCount, final int headLoad, final int tailLoad) {
		int localTankVolume = this.beforeWindowTankVolume;
		final Iterator<InventoryDateTimeEvent> iter = this.frontWindow.iterator();

		// Check head
		InventoryDateTimeEvent currentEvent = iter.next();
		int currentVolumeChange = currentEvent.getNetVolumeIn() - headLoad;
		localTankVolume += currentVolumeChange;
		if (localTankVolume < currentEvent.minVolume) {
			return false;
		}

		// Check tail that coincides with front window
		for (int i = 1; i < frontHourCount; ++i) {
			currentEvent = iter.next();
			currentVolumeChange = currentEvent.getNetVolumeIn() - tailLoad;
			localTankVolume += currentVolumeChange;
			if (localTankVolume < currentEvent.minVolume) {
				return false;
			}
		}

		// Check tail that coincides with back window
		if (backHourCount > 0) {
			final Iterator<InventoryDateTimeEvent> backWindowIter = this.backWindow.iterator();
			for (int i = 0; i < backHourCount; ++i) {
				currentEvent = backWindowIter.next();
				currentVolumeChange = currentEvent.getNetVolumeIn() - tailLoad;
				localTankVolume += currentVolumeChange;
				if (localTankVolume < currentEvent.minVolume) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public InventoryDateTimeEvent getCurrentEvent() {
		return this.frontWindow.getFirst();
	}

	@Override
	public int getEndWindowTankMin() {
		return this.endWindowTankMin;
	}

	@Override
	public int getEndWindowVolume() {
		return this.endWindowVolume;
	}

	@Override
	public int getProductionToAllocate() {
		return this.getCurrentEvent().getNetVolumeIn();
	}

}
