package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;


@NonNullByDefault
public class VanillaRollingLoadWindow extends RollingLoadWindow {

	protected final LocalTime loadTime;
	protected final List<LoadSequence> loadSequences = new LinkedList<>();

	public VanillaRollingLoadWindow(InventoryGlobalState inventoryGlobalState, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> entries, int maxExistingLoadDuration, int initialTankVolume, final LocalTime loadTime) {
		super(inventoryGlobalState, entries, maxExistingLoadDuration, initialTankVolume);
		this.loadTime = loadTime;
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

		final Iterator<LoadSequence> loadSequenceIter = loadSequences.iterator();
		while (loadSequenceIter.hasNext()) {
			final LoadSequence loadSequence = loadSequenceIter.next();
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
//		return !loadSequences.stream().allMatch(LoadSequence::isComplete);
	}
	@Override
	public boolean canLift(int allocationDrop) {
		final LocalTime localLoadTime = LocalTime.of(this.startDateTime.getHour(), 0);
		if (!localLoadTime.equals(this.loadTime)) {
			return false;
		}
		if (this.endWindowVolume >= allocationDrop + this.endWindowTankMin) {
			int localTankVolume = this.beforeWindowTankVolume;
			final int localTailLoad = allocationDrop / this.loadDuration;
			final int localHeadLoad = localTailLoad + (allocationDrop % this.loadDuration);
			final Iterator<InventoryDateTimeEvent> iter = this.frontWindow.iterator();
			final List<Iterator<Integer>> remainingLoadSequenceIters = new LinkedList<>();
			loadSequences.stream().map(LoadSequence::createRemainingLoadIterator).forEach(remainingLoadSequenceIters::add);

			InventoryDateTimeEvent currentEvent = iter.next();
			int currentVolumeChange = currentEvent.getNetVolumeIn() - localHeadLoad;
			{
				final Iterator<Iterator<Integer>> iterIter = remainingLoadSequenceIters.iterator();
				while (iterIter.hasNext()) {
					final Iterator<Integer> nextIter = iterIter.next();
					if (nextIter.hasNext()) {
						final int nextVol = nextIter.next();
						currentVolumeChange -= nextVol;
						if (!nextIter.hasNext()) {
							iterIter.remove();
						}
					} else {
						iterIter.remove();
					}
				}
			}

			localTankVolume += currentVolumeChange;
			if (localTankVolume < currentEvent.minVolume) {
				return false;
			}
			for (int i = 1; i < this.loadDuration; ++i) {
				currentEvent = iter.next();
				currentVolumeChange = currentEvent.getNetVolumeIn() - localTailLoad;

				{
					final Iterator<Iterator<Integer>> iterIter = remainingLoadSequenceIters.iterator();
					while (iterIter.hasNext()) {
						final Iterator<Integer> nextIter = iterIter.next();
						final int nextVol = nextIter.next();
						currentVolumeChange -= nextVol;
						if (!nextIter.hasNext()) {
							iterIter.remove();
						}

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
	
	@Override
	protected void startLoad(final int allocationDrop, final int duration) {
		this.loadSequences.add(new LoadSequence(allocationDrop, duration));
	}
}
