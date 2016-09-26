/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.utils;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;

public class PNLDeltaUtils {

	public static int getPNLDelta(@NonNull final UserGroup userGroup) {
		int delta = 0;

		for (final CycleGroup cycleGroup : userGroup.getGroups()) {
			if (cycleGroup != null) {
				delta += getPNLDelta(cycleGroup);
			}
		}
		return delta;
	}

	public static int getPNLDelta(@NonNull final CycleGroup cycleGroup) {
		int delta = 0;

		for (final Row groupRow : cycleGroup.getRows()) {
			final Integer pnl = getElementProfitAndLoss(groupRow.getTarget());
			if (pnl == null) {
				continue;
			}
			if (groupRow.isReference()) {
				delta -= pnl.intValue();
			} else {
				delta += pnl.intValue();
			}
		}
		return delta;
	}

	public static long getLatenessDelta(@NonNull final CycleGroup cycleGroup) {
		long delta = 0;

		for (final Row groupRow : cycleGroup.getRows()) {
			final Long pnl = getElementLateness(groupRow.getTarget());
			if (pnl == null) {
				continue;
			}
			if (groupRow.isReference()) {
				delta -= pnl.intValue();
			} else {
				delta += pnl.intValue();
			}
		}
		return delta;
	}

	public static long getCapacityDelta(@NonNull final CycleGroup cycleGroup) {
		long delta = 0;

		for (final Row groupRow : cycleGroup.getRows()) {
			final Long pnl = getElementCapacity(groupRow.getTarget());
			if (pnl == null) {
				continue;
			}
			if (groupRow.isReference()) {
				delta -= pnl.intValue();
			} else {
				delta += pnl.intValue();
			}
		}
		return delta;
	}

	public static @Nullable Long getElementLateness(final @Nullable Object object) {
		if (object instanceof EventGrouping) {
			return LatenessUtils.getLatenessExcludingFlex((EventGrouping) object);
		}
		return null;
	}

	public static @Nullable Long getElementCapacity(final @Nullable Object object) {
		if (object instanceof EventGrouping) {
			return ScheduleModelKPIUtils.getCapacityViolationCount((EventGrouping) object);
		}
		return null;
	}

	public static @Nullable Integer getElementProfitAndLoss(final @Nullable Object object) {

		ProfitAndLossContainer container = null;

		if (object instanceof CargoAllocation || object instanceof VesselEventVisit || object instanceof StartEvent || object instanceof GeneratedCharterOut || object instanceof OpenSlotAllocation
				|| object instanceof EndEvent) {
			container = (ProfitAndLossContainer) object;
		}
		if (object instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) object;
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				container = slotVisit.getSlotAllocation().getCargoAllocation();
			}
		}

		if (container != null) {

			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				return (int) dataWithKey.getProfitAndLoss();
			}
		}
		return null;
	}
}
