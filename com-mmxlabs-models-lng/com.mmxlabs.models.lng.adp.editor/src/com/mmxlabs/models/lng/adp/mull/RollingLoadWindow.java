/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;

public class RollingLoadWindow {
	private final int loadDuration;

	private int endWindowVolume = 0;
	private int endWindowTankMin = 0;
	private int remainingLoadHours = 0;
	private LocalDateTime startDateTime = null;
	private int headLoadVolume;
	private int tailLoadVolume;
	private int beforeWindowTankVolume;
	private boolean isLoadingExisting = false;

	private final LinkedList<InventoryDateTimeEvent> frontWindow = new LinkedList<>();
	private final LinkedList<InventoryDateTimeEvent> backWindow = new LinkedList<>();
	private int currentLoadTime;

	public RollingLoadWindow(final int loadDuration, final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> entries, final int maxExistingLoadDuration) {
		this.loadDuration = loadDuration;
		final int extraTrackedWindowSize = maxExistingLoadDuration - loadDuration;
		if (loadDuration <= 0) {
			throw new IllegalStateException("Load duration cannot be zero");
		}
		this.remainingLoadHours = 0;

		for (int i = 0; i < this.loadDuration; ++i) {
			if (entries.hasNext()) {
				final InventoryDateTimeEvent event = entries.next().getValue();
				frontWindow.add(event);
				endWindowVolume += event.getNetVolumeIn();
				endWindowTankMin = event.minVolume;
			}
		}
		for (int i = 0; i < extraTrackedWindowSize; ++i) {
			if (entries.hasNext()) {
				final InventoryDateTimeEvent event = entries.next().getValue();
				backWindow.add(event);
				endWindowVolume += event.getNetVolumeIn();
				endWindowTankMin = event.minVolume;
			}
		}
		this.startDateTime = frontWindow.getFirst().getDateTime();
	}

	public InventoryDateTimeEvent getLastEvent() {
		return backWindow.isEmpty() ? frontWindow.getLast() : backWindow.getLast();
	}

	public void stepForward(final InventoryDateTimeEvent newEvent) {
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

	public boolean isLoading() {
		return this.remainingLoadHours > 0;
	}

	public LocalDateTime getStartDateTime() {
		return this.startDateTime;
	}

	public boolean canLift(final int allocationDrop) {
		if (this.endWindowVolume >= allocationDrop + this.endWindowTankMin) {
			int localTankVolume = this.beforeWindowTankVolume;
			final int localTailLoad = allocationDrop/this.loadDuration;
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

	private void startLoad(final int allocationDrop, final int duration) {
		this.tailLoadVolume = allocationDrop/duration;
		this.headLoadVolume = this.tailLoadVolume + (allocationDrop%duration);
		this.remainingLoadHours = duration;
		this.currentLoadTime = duration;
	}

	public void startLoad(final int allocationDrop) {
		startLoad(allocationDrop, this.loadDuration);
	}

	public List<CargoBlueprint> startFixedLoad(final Cargo cargo, final LinkedList<CargoBlueprint> generatedCargoBlueprints) {
		// Undo any partially active load unless it is a fixed load
		final List<CargoBlueprint> cargoBluePrintsToUndo = new LinkedList<>();
		if (!this.isLoadingExisting && this.remainingLoadHours > 0) {
			final int volumeToRestore = this.headLoadVolume + (this.loadDuration - this.remainingLoadHours - 1)*this.tailLoadVolume;
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
		final int localTailLoad = expectedLoadAmount/duration;
		final int localHeadLoad = localTailLoad + (expectedLoadAmount % duration);
		final int frontHourCount = Math.min(duration, this.loadDuration);
		final int backHourCount = Math.max(0, duration - this.loadDuration);
		while(!generatedCargoBlueprints.isEmpty() && !canHourlyLift(expectedLoadAmount, frontHourCount, backHourCount, localHeadLoad, localTailLoad)) {
			final CargoBlueprint nextUndo = generatedCargoBlueprints.removeLast();
			cargoBluePrintsToUndo.add(nextUndo);
			final int volumeToRestore = nextUndo.getAllocatedVolume();
			this.beforeWindowTankVolume += volumeToRestore;
			this.endWindowVolume += volumeToRestore;
		}
		return cargoBluePrintsToUndo;
	}

	private boolean canHourlyLift(final int expectedLoadAmount, final int frontHourCount, final int backHourCount, final int headLoad, final int tailLoad) {
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

	public InventoryDateTimeEvent getCurrentEvent() {
		return this.frontWindow.getFirst();
	}

	public int getEndWindowTankMin() {
		return this.endWindowTankMin;
	}

	public int getEndWindowVolume() {
		return this.endWindowVolume;
	}
}
