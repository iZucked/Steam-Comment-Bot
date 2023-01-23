/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.marketability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.scheduling.ICustomTimeWindowTrimmer;
import com.mmxlabs.scheduler.optimiser.scheduling.MinTravelTimeData;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class MarketabilityWindowTrimmer implements ICustomTimeWindowTrimmer {
	public enum Mode {
		EARLIEST, LATEST, SHIFT, SET
	}

	private final ThreadLocal<Map<IPortSlot, Pair<Mode, Integer>>> locals = new ThreadLocal<>();

	public void setTrim(final IPortSlot target, final Mode mode, final int shift) {
		if(locals.get() == null) {
			final Map<IPortSlot, Pair<Mode, Integer>> initialMap = new HashMap<>();
			initialMap.put(target, new Pair<>(mode, shift));
			locals.set(initialMap);			
		} else {
			final var trimList = locals.get();
			trimList.put(target, new Pair<>(mode, shift));
		}
		
	}
	
	
	public void resetTrim(final IPortSlot target) {
		if(locals.get() == null) {
			return;
		}
		final var trimList = locals.get();
		if(trimList != null) {
			trimList.computeIfPresent(target, (x, y) -> new Pair<>());
		}
	}
	
	public void resetTrim() {
		locals.remove();
	}

	private void trimWindow(final IPortSlot target, final Mode mode, final int shift, final List<IPortTimeWindowsRecord> trimmedWindows ) {

		for (final IPortTimeWindowsRecord record : trimmedWindows) {
			for (final IPortSlot slot : record.getSlots()) {
				if (slot == target) {
					ITimeWindow tw = record.getSlotFeasibleTimeWindow(slot);
					if (mode == Mode.EARLIEST) {
						record.setSlotFeasibleTimeWindow(slot, new TimeWindow(tw.getInclusiveStart(), tw.getInclusiveStart() + 1));
					} else if (mode == Mode.LATEST) {
						record.setSlotFeasibleTimeWindow(slot, new TimeWindow(tw.getExclusiveEnd() - 1, tw.getExclusiveEnd()));
					} else if (mode == Mode.SHIFT) {
						int newStart = Math.max(tw.getInclusiveStart(), tw.getExclusiveEnd() - 1 - shift);
						record.setSlotFeasibleTimeWindow(slot, new TimeWindow(newStart, newStart + 1));
					} else if (mode == Mode.SET) {
						record.setSlotFeasibleTimeWindow(slot, new TimeWindow(shift, shift + 1));
					}

					return;
				}
			}
		}
	}
	
	@Override
	public void trimWindows(final IResource resource, final List<IPortTimeWindowsRecord> trimmedWindows, final MinTravelTimeData travelTimeData) {

		final var trimList = locals.get();
		if (trimList == null) {
			return;
		}
		for(Entry<IPortSlot, Pair<Mode, Integer>> entry : trimList.entrySet()) {
			trimWindow(entry.getKey(), entry.getValue().getFirst(), entry.getValue().getSecond(), trimmedWindows);
		}
		
	}
}
