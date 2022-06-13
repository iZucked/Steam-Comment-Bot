/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;
import com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence.ILoadSequence;
import com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence.SequentialLoadSequence;
import com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence.SimpleLoadSequence;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;

@NonNullByDefault
public class RollingLoadWindow implements IRollingWindow {

	protected int endWindowVolume = 0;
	protected int endWindowTankMin = 0;
	protected LocalDateTime startDateTime;
	protected int beforeWindowTankVolume;
	protected final InventoryGlobalState inventoryGlobalState;

	protected final LinkedList<InventoryDateTimeEvent> frontWindow = new LinkedList<>();
	protected final LinkedList<InventoryDateTimeEvent> backWindow = new LinkedList<>();
	protected final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> inventoryEventIter;
	protected int currentLoadTime;

	protected final Deque<ILoadSequence> loadSequences = new LinkedList<>();
	protected final BiMap<ICargoBlueprint, ILoadSequence> activeLoadAssociation = HashBiMap.create();

	public RollingLoadWindow(final InventoryGlobalState inventoryGlobalState, final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> entries, final int maxExistingLoadDuration,
			final int initialTankVolume) {
		
		this.beforeWindowTankVolume = initialTankVolume;
		this.endWindowVolume = initialTankVolume;
		this.inventoryGlobalState = inventoryGlobalState;

		for (int i = 0; i < inventoryGlobalState.getLoadDuration(); ++i) {
			if (entries.hasNext()) {
				final InventoryDateTimeEvent event = entries.next().getValue();
				frontWindow.add(event);
				endWindowVolume += event.getNetVolumeIn();
				endWindowTankMin = event.minVolume;
			}
		}
		final int extraTrackedWindowSize = maxExistingLoadDuration - inventoryGlobalState.getLoadDuration();
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

		if (inventoryGlobalState.getPort().getBerths() <= 0) {
			throw new IllegalStateException("Inventory load port must have at least one berth");
		}
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

		final Iterator<ILoadSequence> loadSequenceIter = loadSequences.iterator();
		while (loadSequenceIter.hasNext()) {
			final ILoadSequence loadSequence = loadSequenceIter.next();
			final int volumeDrop = loadSequence.stepForward();
			beforeWindowTankVolume -= volumeDrop;
			endWindowVolume -= volumeDrop;
			if (loadSequence.isComplete()) {
				loadSequenceIter.remove();
			}
		}
	}

	@Override
	public boolean isLoading() {
		return this.loadSequences.size() >= this.inventoryGlobalState.getNumberOfBerths();
	}

	@Override
	public LocalDateTime getStartDateTime() {
		return this.startDateTime;
	}

	protected ILoadSequence createStandardLoadSequence(final int allocationDrop, final int loadDuration) {
		final int tailLoad = allocationDrop / loadDuration;
		final int headDelta = allocationDrop % loadDuration;
		if (headDelta == 0) {
			return new SimpleLoadSequence(tailLoad, loadDuration);
		}
		final int headLoad = tailLoad + headDelta;
		final ILoadSequence headLoadSequence = new SimpleLoadSequence(headLoad, 1);
		final ILoadSequence tailLoadSequence = new SimpleLoadSequence(tailLoad, loadDuration - 1);
		final List<ILoadSequence> sequentialSequence = new ArrayList<>(2);
		sequentialSequence.add(headLoadSequence);
		sequentialSequence.add(tailLoadSequence);
		return new SequentialLoadSequence(sequentialSequence);
	}

	@Override
	public boolean canLift(int allocationDrop) {
		if (inventoryGlobalState.getLiftTimeSpecifier().isValidLiftTime(startDateTime) //
				&& this.endWindowVolume >= allocationDrop + this.endWindowTankMin) {
			int localTankVolume = this.beforeWindowTankVolume;

			final ILoadSequence newLoadSequence = createStandardLoadSequence(allocationDrop, inventoryGlobalState.getLoadDuration());
			final List<Iterator<Integer>> loadSequenceIters = new LinkedList<>();
			for (final ILoadSequence loadSequence : loadSequences) {
				loadSequenceIters.add(loadSequence.createRemainingLoadIterator());
			}
			loadSequenceIters.add(newLoadSequence.createRemainingLoadIterator());

			for (final InventoryDateTimeEvent currentEvent : this.frontWindow) {
				final Iterator<Iterator<Integer>> loadSequenceIterIter = loadSequenceIters.iterator();
				int currentVolumeChange = currentEvent.getNetVolumeIn();
				while (loadSequenceIterIter.hasNext()) {
					final Iterator<Integer> nextIter = loadSequenceIterIter.next();
					if (nextIter.hasNext()) {
						currentVolumeChange -= nextIter.next();
					} else {
						loadSequenceIterIter.remove();
					}
				}

				localTankVolume += currentVolumeChange;
				if (localTankVolume < currentEvent.minVolume) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	protected ILoadSequence startLoad(final int allocationDrop, final int duration) {
		final ILoadSequence newLoadSequence = createStandardLoadSequence(allocationDrop, duration);
		this.loadSequences.add(newLoadSequence);
		return newLoadSequence;
	}

	@Override
	public void startLoad(final int allocationDrop) {
		startLoad(allocationDrop, inventoryGlobalState.getLoadDuration());
	}

	@Override
	public void startLoad(final ICargoBlueprint cargoBlueprint) {
		final ILoadSequence loadSequence = startLoad(cargoBlueprint.getAllocatedVolume(), inventoryGlobalState.getLoadDuration());
		activeLoadAssociation.put(cargoBlueprint, loadSequence);
	}

	protected void restoreVolume(final int volumeToRestore) {
		this.beforeWindowTankVolume += volumeToRestore;
		this.endWindowVolume += volumeToRestore;
	}

	@Override
	public List<ICargoBlueprint> startFixedLoad(final Cargo cargo, final LinkedList<ICargoBlueprint> generatedCargoBlueprints) {
		// Undo any partially active load unless it is a fixed load
		final List<ICargoBlueprint> cargoBluePrintsToUndo = new LinkedList<>();

		// Remove load if maximising use of berths
		while (loadSequences.size() >= inventoryGlobalState.getNumberOfBerths()) {
			final ICargoBlueprint cargoBlueprintToUndo = generatedCargoBlueprints.getLast();
			final ILoadSequence loadSequence = activeLoadAssociation.remove(cargoBlueprintToUndo);
			if (loadSequence == null) {
				// Only fixed loads are in the load sequences, break the loop
				break;
			} else {
				generatedCargoBlueprints.removeLast();
				cargoBluePrintsToUndo.add(cargoBlueprintToUndo);
				restoreVolume(loadSequence.getUndoVolumeToRestore());
				final Iterator<ILoadSequence> loadSequenceIter = loadSequences.iterator();
				while (loadSequenceIter.hasNext()) {
					if (loadSequenceIter.next() == loadSequence) {
						loadSequenceIter.remove();
						break;
					}
				}
			}
		}

		// Start fixed load
		final LoadSlot loadSlot = (LoadSlot) cargo.getSlots().get(0);
		final int expectedLoadAmount = loadSlot.getMaxQuantity();
		final int duration = loadSlot.isSetDuration() ? loadSlot.getDuration() : inventoryGlobalState.getLoadDuration();
		startLoad(expectedLoadAmount, duration);

		// Undo any generated cargoes that conflict with current fixed load
		while (!generatedCargoBlueprints.isEmpty() && !canHourlyLift()) {
			final ICargoBlueprint nextUndo = generatedCargoBlueprints.removeLast();
			cargoBluePrintsToUndo.add(nextUndo);
			final ILoadSequence loadSequence = activeLoadAssociation.remove(nextUndo);
			final int volumeToRestore;
			if (loadSequence == null) {
				volumeToRestore = nextUndo.getAllocatedVolume();
			} else {
				volumeToRestore = loadSequence.getUndoVolumeToRestore();
				final Iterator<ILoadSequence> loadSequenceIter = loadSequences.iterator();
				while (loadSequenceIter.hasNext()) {
					if (loadSequenceIter.next() == loadSequence) {
						loadSequenceIter.remove();
						break;
					}
				}
			}
			restoreVolume(volumeToRestore);
		}
		return cargoBluePrintsToUndo;
	}

	private boolean canHourlyLift() {
		int localTankVolume = this.beforeWindowTankVolume;
		final List<Iterator<Integer>> loadSequenceIters = new LinkedList<>();
		for (final ILoadSequence loadSequence : loadSequences) {
			loadSequenceIters.add(loadSequence.createRemainingLoadIterator());
		}

		for (final InventoryDateTimeEvent currentEvent : this.frontWindow) {
			final Iterator<Iterator<Integer>> loadSequenceIterIter = loadSequenceIters.iterator();
			int currentVolumeChange = currentEvent.getNetVolumeIn();
			while (loadSequenceIterIter.hasNext()) {
				final Iterator<Integer> nextIter = loadSequenceIterIter.next();
				if (nextIter.hasNext()) {
					currentVolumeChange -= nextIter.next();
				} else {
					loadSequenceIterIter.remove();
				}
			}

			localTankVolume += currentVolumeChange;
			if (localTankVolume < currentEvent.minVolume) {
				return false;
			}
		}

		for (final InventoryDateTimeEvent currentEvent : this.backWindow) {
			final Iterator<Iterator<Integer>> loadSequenceIterIter = loadSequenceIters.iterator();
			int currentVolumeChange = currentEvent.getNetVolumeIn();
			while (loadSequenceIterIter.hasNext()) {
				final Iterator<Integer> nextIter = loadSequenceIterIter.next();
				if (nextIter.hasNext()) {
					currentVolumeChange -= nextIter.next();
				} else {
					loadSequenceIterIter.remove();
				}
			}

			localTankVolume += currentVolumeChange;
			if (localTankVolume < currentEvent.minVolume) {
				return false;
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
