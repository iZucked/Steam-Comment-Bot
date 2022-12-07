/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.marketability;

import java.util.List;

import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.scheduling.ICustomTimeWindowTrimmer;
import com.mmxlabs.scheduler.optimiser.scheduling.MinTravelTimeData;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class MarketabilityWindowTrimmer implements ICustomTimeWindowTrimmer {
	public enum Mode {
		EARLIEST, LATEST, SHIFT
	}

	ThreadLocal<Triple<Mode, IPortSlot, Integer>> locals = new ThreadLocal<>();

	public void setTrim(final IPortSlot target, final Mode mode, final int shift) {

		locals.set(new Triple<>(mode, target, shift));
	}

	@Override
	public void trimWindows(final IResource resource, final List<IPortTimeWindowsRecord> trimmedWindows, final MinTravelTimeData travelTimeData) {

		final Triple<Mode, IPortSlot, Integer> pair = locals.get();
		if (pair == null) {
			return;
		}

		final IPortSlot target = pair.getSecond();
		final Mode mode = pair.getFirst();
		final int shift = pair.getThird();

		for (final IPortTimeWindowsRecord record : trimmedWindows) {
			for (final IPortSlot slot : record.getSlots()) {
				if (slot == target) {
					final ITimeWindow tw = record.getSlotFeasibleTimeWindow(slot);
					if (mode == Mode.EARLIEST) {
						record.setSlotFeasibleTimeWindow(slot, new TimeWindow(tw.getInclusiveStart(), tw.getInclusiveStart() + 1));
					} else if (mode == Mode.LATEST) {
						record.setSlotFeasibleTimeWindow(slot, new TimeWindow(tw.getExclusiveEnd() - 1, tw.getExclusiveEnd()));
					} else if (mode == Mode.SHIFT) {
						int newStart = Math.max(tw.getInclusiveStart(), tw.getExclusiveEnd() - 1 - shift);
						
						record.setSlotFeasibleTimeWindow(slot, new TimeWindow(newStart, newStart + 1));
					}

					return;
				}
			}
		}
	}
}
