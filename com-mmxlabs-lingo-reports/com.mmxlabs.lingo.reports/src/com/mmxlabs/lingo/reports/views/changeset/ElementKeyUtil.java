/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.cargo.VesselCharter;
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
	@NonNull
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
		if (element instanceof SlotVisit slotVisit) {
			return "visit-" + getElementKey(slotVisit.getSlotAllocation());
		}
		if (element instanceof SlotAllocation slotAllocation) {
			String prefix = "";
			final Slot<?> slot = slotAllocation.getSlot();
			prefix = "allocation-" + getSlotTypePrefix(slot);
			if (slot instanceof SpotSlot) {
				return prefix + "-" + getSpotSlotSuffix(slot);

			} else {
				final String baseName = slotAllocation.getName();
				return prefix + "-" + baseName;
			}
		} else if (element instanceof CargoAllocation cargoAllocation) {
			return "cargo-" + cargoAllocation.getName();
		} else if (element instanceof OpenSlotAllocation openSlotAllocation) {
			String prefix = "";
			final Slot<?> slot = openSlotAllocation.getSlot();
			prefix = getSlotTypePrefix(slot);
			if (slot instanceof SpotSlot) {
				return prefix + "-" + getSpotSlotSuffix(slot);
			} else if (openSlotAllocation.getSlot() != null) {
				final String baseName = openSlotAllocation.getSlot().getName();
				return prefix + "-" + baseName;
			}
		} else if (element instanceof StartEvent startEvent) {
			Sequence sequence = startEvent.getSequence();
			VesselCharter vesselCharter = sequence.getVesselCharter();
			final String base = "start-" + sequence.getName() + "-" + (vesselCharter == null ? "" : Integer.toString(vesselCharter.getCharterNumber()));
			if (sequence.isSetSpotIndex()) {
				return base + "-" + sequence.getSpotIndex();
			}
			return base;
		} else if (element instanceof EndEvent endEvent) {
			Sequence sequence = endEvent.getSequence();
			VesselCharter vesselCharter = sequence.getVesselCharter();
			final String base = "end-" + sequence.getName() + "-" + (vesselCharter == null ? "" : Integer.toString(vesselCharter.getCharterNumber()));
			if (sequence.isSetSpotIndex()) {
				return base + "-" + sequence.getSpotIndex();
			}
			return base;
		} else if (element instanceof GeneratedCharterOut generatedCharterOut) {
			// Add in hash code to keep elements unique.
			// See start of #checkElementEquivalence
			// Equivalence is really overlapping event time on a resource, element name (currently) encode the previous cargo/event ID which may change.
			return element.eClass().getName() + "-" + generatedCharterOut.name() + "-" + element.hashCode();
		} else if (element instanceof CharterLengthEvent charterLengthEvent) {
			// Add in hash code to keep elements unique.
			// See start of #checkElementEquivalence
			// Equivalence is really overlapping event time on a resource, element name (currently) encode the previous cargo/event ID which may change.
			return element.eClass().getName() + "-" + charterLengthEvent.name() + "-" + element.hashCode();
		} else if (element instanceof GroupedCharterLengthEvent groupedCharterLengthEvent) {
			// Add in hash code to keep elements unique.
			// See start of #checkElementEquivalence
			// Equivalence is really overlapping event time on a resource, element name (currently) encode the previous cargo/event ID which may change.
			return element.eClass().getName() + "-" + groupedCharterLengthEvent.name();
		} else if (element instanceof GroupedCharterOutEvent groupedCharterOutEvent) {
			// Add in hash code to keep elements unique.
			// See start of #checkElementEquivalence
			// Equivalence is really overlapping event time on a resource, element name (currently) encode the previous cargo/event ID which may change.
			return element.eClass().getName() + "-" + groupedCharterOutEvent.name();
		} else if (element instanceof Event event) {
			return element.eClass().getName() + "-" + event.name();
		}
		if (element instanceof Slot<?> slot) {
			String prefix = "";
			prefix = getSlotTypePrefix(slot);
			if (slot instanceof SpotSlot) {
				return prefix + "-" + getSpotSlotSuffix(slot);
			} else {
				final String baseName = slot.getName();
				return prefix + "-" + baseName;
			}

		}
		if (element instanceof NamedObject namedObject) {
			return element.getClass().getName() + "-" + namedObject.getName();
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
		if (slot instanceof LoadSlot loadSlot) {
			if (loadSlot.isDESPurchase()) {
				prefix = "des-purchase";
			} else {
				prefix = "fob-purchase";
			}
		} else if (slot instanceof DischargeSlot dischargeSlot) {
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
