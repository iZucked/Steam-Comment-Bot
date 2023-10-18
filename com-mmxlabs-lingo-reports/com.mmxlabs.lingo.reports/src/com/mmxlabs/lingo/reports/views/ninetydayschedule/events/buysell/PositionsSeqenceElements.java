/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.MultiEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

/**
 * Helper methods related to PositionsSequecneElement class
 * 
 * @author AP
 *
 */
public class PositionsSeqenceElements {

	private PositionsSeqenceElements() {
		// no instances created
	}

	private static LocalDateTime getMultiEventTime(final MultiEvent multiEvent) {
		if (multiEvent.getElements().isEmpty()) {
			throw new IllegalArgumentException("Empty Multi Event");
		}
		final Object firstElement = multiEvent.getElements().get(0);
		return getEventTime(firstElement);
	}

	/**
	 * Get the time of a buy sell position.
	 * 
	 * @param element
	 *            - OpenSlotAllocation, SlotVisit or a MultiEvent
	 * @return the local date time depending on the event internal element.
	 */
	public static LocalDateTime getEventTime(Object element) {
		if (element instanceof final OpenSlotAllocation openSlotAllocation) {
			return LocalDateTime.of(openSlotAllocation.getSlot().getWindowStart(), LocalTime.now());
		}
		if (element instanceof final SlotVisit slotVisit) {
			return slotVisit.getStart().toLocalDateTime();
		}
		if (element instanceof final MultiEvent multiEvent) {
			return PositionsSeqenceElements.getMultiEventTime(multiEvent);
		}
		throw new IllegalArgumentException("Unmatched Position Sequence Element Match");
	}

	private static PositionType getPositionTypeOfElement(final Object element) {
		if (element instanceof final SlotVisit slotVisit) {
			return getPositionTypeFromSlot(slotVisit.getSlotAllocation().getSlot());
		}
		if (element instanceof final OpenSlotAllocation openSlotAllocation) {
			return getPositionTypeFromSlot(openSlotAllocation.getSlot());
		}
		if (element instanceof final MultiEvent multiEvent) {
			final boolean allDES = multiEvent.getElements().stream().allMatch(elem -> getPositionTypeOfElement(elem) == PositionType.DES);
			if (allDES) {
				return PositionType.DES;
			}
			final boolean allFOB = multiEvent.getElements().stream().allMatch(elem -> getPositionTypeOfElement(elem) == PositionType.FOB);
			if (allFOB) {
				return PositionType.FOB;
			}
			return PositionType.MIXED;
		}
		throw new IllegalStateException("Incorrect Position Element");
	}

	private static PositionType getPositionTypeFromSlot(final Slot<?> slot) {
		if (slot instanceof final LoadSlot loadSlot) {
			return loadSlot.isDESPurchase() ? PositionType.DES : PositionType.FOB;
		}
		if (slot instanceof final DischargeSlot dischargeSlot) {
			return dischargeSlot.isFOBSale() ? PositionType.FOB : PositionType.DES;
		}
		throw new IllegalStateException();
	}

	/**
	 * Nicer way to distinguish type of buysell event than instanceof patternmatch.
	 * It does that for you.
	 */
	public static PositionsSequenceClassification getClassification(final PositionsSequenceElement positionsElement) {
		final Object element = positionsElement.getElement();
		final PositionType positionType = getPositionTypeOfElement(element);
		final PositionStateType positionStateType;

		if (element instanceof final SlotVisit slotVisit) {
			final boolean pairedToSpot = slotVisit.getSlotAllocation().getCargoAllocation().getSlotAllocations().stream().filter(sa -> sa != slotVisit.getSlotAllocation()).map(sa -> sa.getSlot())
					.anyMatch(SpotSlot.class::isInstance);
			if (pairedToSpot) {
				positionStateType = PositionStateType.PAIRED_TO_SPOT;
			} else {
				positionStateType = PositionStateType.PAIRED;
			}
		} else if (element instanceof final OpenSlotAllocation openSlotAllocation) {
			if (openSlotAllocation.getSlot().isOptional()) {
				positionStateType = PositionStateType.OPTIONAL;
			} else {
				positionStateType = PositionStateType.OPEN;
			}
		} else if (element instanceof final MultiEvent multiEvent) {
			final boolean anyOpen = multiEvent.getElements().stream().anyMatch(elem -> elem instanceof OpenSlotAllocation osa && !osa.getSlot().isOptional());
			positionStateType = anyOpen ? PositionStateType.OPEN : PositionStateType.PAIRED;
		} else {
			throw new IllegalStateException("Incorrect Position Element");
		}
		return new PositionsSequenceClassification(positionStateType, positionType, positionsElement.isBuyRow(), positionsElement.isMulti());
	}

	/**
	 * Just a factory method for {@link DrawableScheduleEvent}
	 */
	public static DrawableScheduleEvent createDrawableScheduleEventFromPositionSequence( //
			final ScheduleEvent scheduleEvent, //
			final Rectangle bounds, //
			final boolean noneSelected, //
			final PositionsSequenceElement positionsElement //
	) {
		final boolean isBuy = positionsElement.isBuyRow();
		final boolean isMulti = positionsElement.isMulti();

		final PositionsSequenceClassification classification = getClassification(positionsElement);
		return new BuySellDrawableScheduleEvent(scheduleEvent, bounds, noneSelected, isBuy, classification.positionStateType(), classification.positionType(), isMulti);
	}
}
