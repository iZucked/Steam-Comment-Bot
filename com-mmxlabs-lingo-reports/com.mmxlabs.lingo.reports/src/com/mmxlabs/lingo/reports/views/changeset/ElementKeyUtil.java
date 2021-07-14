/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Joiner;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GroupedCharterOutEvent;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.NamedObject;

public class ElementKeyUtil {

	/**
	 * Returns a reproducable key of some kind for the element
	 * 
	 * @param element
	 * @return
	 */
	public static String getElementKey(EObject element) {
		if (element instanceof Row) {
			if (element.eIsSet(ScheduleReportPackage.Literals.ROW__CARGO_ALLOCATION)) {
				element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__CARGO_ALLOCATION);
			} else if (element.eIsSet(ScheduleReportPackage.Literals.ROW__OPEN_LOAD_SLOT_ALLOCATION)) {
				element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__OPEN_LOAD_SLOT_ALLOCATION);
			} else if (element.eIsSet(ScheduleReportPackage.Literals.ROW__OPEN_DISCHARGE_SLOT_ALLOCATION)) {
				element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__OPEN_DISCHARGE_SLOT_ALLOCATION);
			} else if (element.eIsSet(ScheduleReportPackage.Literals.ROW__TARGET)) {
				element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__TARGET);
			}
		}
		if (element instanceof SlotVisit) {
			return "visit-" + getElementKey(((SlotVisit) element).getSlotAllocation());
		}
		if (element instanceof SlotAllocation) {
			final SlotAllocation slotAllocation = (SlotAllocation) element;
			String prefix = "";
			final Slot<?> slot = slotAllocation.getSlot();
			prefix = "allocation-" + getSlotTypePrefix(slot);
			if (slot instanceof SpotSlot) {
				return prefix + "-" + getSpotSlotSuffix(slot);

			} else {
				final String baseName = slotAllocation.getName();
				return prefix + "-" + baseName;
			}
		} else if (element instanceof CargoAllocation) {
			return "cargo-" + ((CargoAllocation) element).getName();
		} else if (element instanceof OpenSlotAllocation) {
			final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
			String prefix = "";
			final Slot<?> slot = openSlotAllocation.getSlot();
			prefix = getSlotTypePrefix(slot);
			if (slot instanceof SpotSlot) {
				return prefix + "-" + getSpotSlotSuffix(slot);
			} else if (openSlotAllocation.getSlot() != null) {
				final String baseName = openSlotAllocation.getSlot().getName();
				return prefix + "-" + baseName;
			}
		} else if (element instanceof StartEvent) {
			final StartEvent startEvent = (StartEvent) element;
			Sequence sequence = startEvent.getSequence();
			VesselAvailability vesselAvailability = sequence.getVesselAvailability();
			final String base = "start-" + sequence.getName() + "-" + (vesselAvailability == null ? "" : Integer.toString(vesselAvailability.getCharterNumber()));
			if (sequence.isSetSpotIndex()) {
				return base + "-" + sequence.getSpotIndex();
			}
			return base;
		} else if (element instanceof EndEvent) {
			final EndEvent endEvent = (EndEvent) element;
			Sequence sequence = endEvent.getSequence();
			VesselAvailability vesselAvailability = sequence.getVesselAvailability();
			final String base = "end-" + sequence.getName() + "-" + (vesselAvailability == null ? "" : Integer.toString(vesselAvailability.getCharterNumber()));
			if (sequence.isSetSpotIndex()) {
				return base + "-" + sequence.getSpotIndex();
			}
			return base;
		} else if (element instanceof GeneratedCharterOut) {
			// Add in hash code to keep elements unique.
			// See start of #checkElementEquivalence
			// Equivalence is really overlapping event time on a resource, element name (currently) encode the previous cargo/event ID which may change.
			return element.eClass().getName() + "-" + ((Event) element).name() + "-" + element.hashCode();
		} else if (element instanceof CharterLengthEvent) {
			// Add in hash code to keep elements unique.
			// See start of #checkElementEquivalence
			// Equivalence is really overlapping event time on a resource, element name (currently) encode the previous cargo/event ID which may change.
			return element.eClass().getName() + "-" + ((Event) element).name() + "-" + element.hashCode();
		} else if (element instanceof GroupedCharterLengthEvent) {
			// Add in hash code to keep elements unique.
			// See start of #checkElementEquivalence
			// Equivalence is really overlapping event time on a resource, element name (currently) encode the previous cargo/event ID which may change.
			return element.eClass().getName() + "-" + ((Event) element).name();
		} else if (element instanceof GroupedCharterOutEvent) {
			// Add in hash code to keep elements unique.
			// See start of #checkElementEquivalence
			// Equivalence is really overlapping event time on a resource, element name (currently) encode the previous cargo/event ID which may change.
			return element.eClass().getName() + "-" + ((Event) element).name();
		} else if (element instanceof Event) {
			return element.eClass().getName() + "-" + ((Event) element).name();
		}
		if (element instanceof Slot) {
			String prefix = "";
			final Slot<?> slot = (Slot<?>) element;
			prefix = getSlotTypePrefix(slot);
			if (slot instanceof SpotSlot) {
				return prefix + "-" + getSpotSlotSuffix(slot);
			} else {
				final String baseName = slot.getName();
				return prefix + "-" + baseName;
			}

		}
		if (element instanceof NamedObject) {
			return element.getClass().getName() + "-" + ((NamedObject) element).getName();
		}
		return element.toString();
	}

	private static @NonNull String getSpotSlotSuffix(final Slot<?> slot) {
		final SpotMarket market = ((SpotSlot) slot).getMarket();
		final @NonNull String id = String.format("%s-%s-%s", market.eClass().getName(), market.getName(), format(slot.getWindowStart()));
		final Cargo c = slot.getCargo();

		if (c != null) {
			final List<String> elements = new LinkedList<>();
			for (final Slot<?> s : c.getSortedSlots()) {
				if (s == slot) {
					elements.add(id);
				} else if (s instanceof SpotSlot) {
					// Avoid recursion
					elements.add("spot");
				} else {
					elements.add(getElementKey(s));
				}
			}
			return Joiner.on("--").join(elements);
		} else {
			return id;
		}
	}

	private static @NonNull String getSlotTypePrefix(final Slot<?> slot) {
		String prefix;
		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (loadSlot.isDESPurchase()) {
				prefix = "des-purchase";
			} else {
				prefix = "fob-purchase";
			}
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			if (dischargeSlot.isFOBSale()) {
				prefix = "fob-sale";
			} else {
				prefix = "des-sale";
			}
		} else {
			prefix = "unknownslottype";
		}
		return prefix;
	}

	private static @NonNull String format(final @Nullable LocalDate date) {
		if (date == null) {
			return "<no date>";
		}
		return String.format("%04d-%02d", date.getYear(), date.getMonthValue());

	}
}
