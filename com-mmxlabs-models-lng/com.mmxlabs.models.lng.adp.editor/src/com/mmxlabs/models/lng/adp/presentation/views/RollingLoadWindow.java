/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

public class RollingLoadWindow {
	private final int loadDuration;

	private final LinkedList<InventoryDateTimeEvent> currentWindow = new LinkedList<>();
	private int endWindowVolume = 0;
	private int endWindowTankMin = 0;
	private int remainingLoadHours = 0;
	private LocalDateTime startDateTime = null;
	private int headLoadVolume;
	private int tailLoadVolume;
	private int beforeWindowTankVolume;

	public RollingLoadWindow(final int loadDuration, final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> entries) {
		this.loadDuration = loadDuration;
		if (loadDuration <= 0) {
			throw new IllegalStateException("Load duration cannot be zero");
		}
		this.remainingLoadHours = 0;
		int i;
		for (i = 0; i < this.loadDuration; i++) {
			if (entries.hasNext()) {
				InventoryDateTimeEvent event = entries.next().getValue();
				currentWindow.add(event);
				endWindowVolume += event.getNetVolumeIn();
				endWindowTankMin = event.minVolume;
			} else {
				throw new IllegalStateException("Not enough load data");
			}
		}
		this.startDateTime = currentWindow.getFirst().getDateTime();
	}

	public void stepForward(final InventoryDateTimeEvent newEvent) {
		InventoryDateTimeEvent droppedEvent = currentWindow.removeFirst();
		currentWindow.add(newEvent);
		beforeWindowTankVolume += droppedEvent.getNetVolumeIn();
		endWindowVolume += newEvent.getNetVolumeIn();
		endWindowTankMin = newEvent.minVolume;
		this.startDateTime = currentWindow.getFirst().getDateTime();
		if (remainingLoadHours > 0) {
			final int volumeDrop = remainingLoadHours == this.loadDuration ? this.headLoadVolume : this.tailLoadVolume;
			beforeWindowTankVolume -= volumeDrop;
			endWindowVolume -= volumeDrop;
			remainingLoadHours--;
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
			final Iterator<InventoryDateTimeEvent> iter = this.currentWindow.iterator();
			InventoryDateTimeEvent currentEvent = iter.next();
			int currentVolumeChange = currentEvent.getNetVolumeIn() - localHeadLoad;
			localTankVolume += currentVolumeChange;
			if (localTankVolume < currentEvent.minVolume) {
				return false;
			}
			while (iter.hasNext()) {
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

	public void startLoad(final int allocationDrop) {
		this.tailLoadVolume = allocationDrop/this.loadDuration;
		this.headLoadVolume = this.tailLoadVolume + (allocationDrop%this.loadDuration);
		this.remainingLoadHours = this.loadDuration;
	}

	public InventoryDateTimeEvent getCurrentEvent() {
		return this.currentWindow.getFirst();
	}

	public int getEndWindowTankMin() {
		return this.endWindowTankMin;
	}

	public int getEndWindowVolume() {
		return this.endWindowVolume;
	}
}
