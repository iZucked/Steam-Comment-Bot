/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;
import com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence.ILoadSequence;
import com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence.SequentialLoadSequence;
import com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence.SimpleLoadSequence;

@NonNullByDefault
public class VanillaRollingLoadWindow extends RollingLoadWindow {

	protected final LocalTime liftTriggerTime;
//	protected final int preLoadSetupTime;
	// protected final LocalTime loadTime;
	protected final List<ILoadSequence> loadSequences = new LinkedList<>();
	protected final HourlyToDailyEventIterator lookaheadIterator;
	
	

	public VanillaRollingLoadWindow(InventoryGlobalState inventoryGlobalState, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> entries, int maxExistingLoadDuration, int initialTankVolume,
			final LocalTime liftTriggerTime) {
		super(inventoryGlobalState, entries, maxExistingLoadDuration, initialTankVolume);
		// this.loadTime = loadTime;
		this.liftTriggerTime = liftTriggerTime;
//		this.preLoadSetupTime = preLoadSetupTime;
		lookaheadIterator = new HourlyToDailyEventIterator(inventoryGlobalState.getInsAndOuts().entrySet().iterator());
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
		if (!localLoadTime.equals(this.liftTriggerTime)) {
			return false;
		}

		final int productionAmount;
		final LocalDate currentDate = this.startDateTime.toLocalDate();
		if (currentDate.equals(lookaheadIterator.getCachedLastNextDate())) {
			productionAmount = lookaheadIterator.getCachedLastNextVolume();
		} else {
			if (lookaheadIterator.hasNext()) {
				final Pair<LocalDate, Integer> nextPair = lookaheadIterator.next();
				assert currentDate.equals(nextPair.getFirst());
				productionAmount = nextPair.getSecond();
			} else {
				productionAmount = 0;
			}
		}
		return this.beforeWindowTankVolume + productionAmount >= allocationDrop + this.endWindowTankMin;
	}

	@Override
	protected void startLoad(final int allocationDrop, final int duration) {
		final ILoadSequence newLoadSequence;
		{
			final int localTailLoad = allocationDrop / this.loadDuration;
			final int localHeadLoad = localTailLoad + (allocationDrop % this.loadDuration);
			final List<ILoadSequence> localSequences = new ArrayList<>();
			localSequences.add(new SimpleLoadSequence(localHeadLoad, 1));
			localSequences.add(new SimpleLoadSequence(localTailLoad, this.loadDuration - 1));
			
			newLoadSequence = new SequentialLoadSequence(localSequences);
		}
		this.loadSequences.add(newLoadSequence);
	}

	@Override
	public int getProductionToAllocate() {
		final LocalTime currentTime = this.startDateTime.toLocalTime();
		if (currentTime != this.liftTriggerTime) {
			return 0;
		}
		final LocalDate currentDate = this.startDateTime.toLocalDate();
		if (currentDate.equals(lookaheadIterator.getCachedLastNextDate())) {
			return lookaheadIterator.getCachedLastNextVolume();
		} else {
			if (lookaheadIterator.hasNext()) {
				final Pair<LocalDate, Integer> nextPair = lookaheadIterator.next();
				assert currentDate.equals(nextPair.getFirst());
				return nextPair.getSecond();
			} else {
				return 0;
			}
		}
	}
}
