/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;
import com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence.ILoadSequence;
import com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence.SequentialLoadSequence;
import com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence.SimpleLoadSequence;

@NonNullByDefault
public class VanillaRollingLoadWindow extends RollingLoadWindow {

	protected final LocalTime ratTime;
	protected final int preLoadSetupTime;
	// protected final LocalTime loadTime;
	protected final List<ILoadSequence> loadSequences = new LinkedList<>();

	public VanillaRollingLoadWindow(InventoryGlobalState inventoryGlobalState, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> entries, int maxExistingLoadDuration, int initialTankVolume,
			final LocalTime ratTime, final int preLoadSetupTime) {
		super(inventoryGlobalState, entries, maxExistingLoadDuration + preLoadSetupTime, initialTankVolume);
		// this.loadTime = loadTime;
		this.ratTime = ratTime;
		this.preLoadSetupTime = preLoadSetupTime;
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
		// vanilla can consider simultaneous loads
		return false;
		// return !loadSequences.stream().allMatch(LoadSequence::isComplete);
	}

	@Override
	public boolean canLift(int allocationDrop) {
		final LocalTime localLoadTime = LocalTime.of(this.startDateTime.getHour(), 0);
		if (!localLoadTime.equals(this.ratTime)) {
			return false;
		}
		if (this.endWindowVolume >= allocationDrop + this.endWindowTankMin) {
			int localTankVolume = this.beforeWindowTankVolume;
			final int localTailLoad = allocationDrop / this.loadDuration;
			final int localHeadLoad = localTailLoad + (allocationDrop % this.loadDuration);

			// Create grouped sequence since it's convenient
			final ILoadSequence localLoadSequence;
			{
				final List<ILoadSequence> localSequences = new ArrayList<>();
				localSequences.add(new SimpleLoadSequence(0, preLoadSetupTime));
				localSequences.add(new SimpleLoadSequence(localHeadLoad, 1));
				localSequences.add(new SimpleLoadSequence(localTailLoad, this.loadDuration - 1));

				localLoadSequence = new SequentialLoadSequence(localSequences);
			}
			boolean usingBackIter = false;
			Iterator<InventoryDateTimeEvent> iter = this.frontWindow.iterator();
			final List<Iterator<Integer>> remainingLoadSequenceIters = new LinkedList<>();
			loadSequences.stream().map(ILoadSequence::createRemainingLoadIterator).forEach(remainingLoadSequenceIters::add);
			remainingLoadSequenceIters.add(localLoadSequence.createRemainingLoadIterator());

			InventoryDateTimeEvent currentEvent;
			for (int i = 0; i < preLoadSetupTime + loadDuration; ++i) {
				if (!iter.hasNext()) {
					if (!usingBackIter) {
						iter = this.backWindow.iterator();
						usingBackIter = true;
					}
					if (!iter.hasNext()) {
						throw new IllegalStateException("Ran out of events");
					}
				}
				currentEvent = iter.next();

				int volumeLifted = remainingLoadSequenceIters.stream() //
						.mapToInt(Iterator::next) //
						.sum();
				int currentVolumeChange = currentEvent.getNetVolumeIn() - volumeLifted;

				localTankVolume += currentVolumeChange;
				if (localTankVolume < currentEvent.minVolume) {
					return false;
				}
				// Clear any empty iterators
				final Iterator<Iterator<Integer>> iterRemainingLoadSequenceIters = remainingLoadSequenceIters.iterator();
				while (iterRemainingLoadSequenceIters.hasNext()) {
					if (!iterRemainingLoadSequenceIters.next().hasNext()) {
						iterRemainingLoadSequenceIters.remove();
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected void startLoad(final int allocationDrop, final int duration) {
		final ILoadSequence newLoadSequence;
		{
			final int localTailLoad = allocationDrop / this.loadDuration;
			final int localHeadLoad = localTailLoad + (allocationDrop % this.loadDuration);
			final List<ILoadSequence> localSequences = new ArrayList<>();
			localSequences.add(new SimpleLoadSequence(0, preLoadSetupTime));
			localSequences.add(new SimpleLoadSequence(localHeadLoad, 1));
			localSequences.add(new SimpleLoadSequence(localTailLoad, this.loadDuration - 1));
			
			newLoadSequence = new SequentialLoadSequence(localSequences);
		}
		this.loadSequences.add(newLoadSequence);
	}
}
