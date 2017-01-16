/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.utils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;

/**
 */
public class CargoAllocationUtils {

	public static final String NODE_FEATURE_CARGO = "cargo";
	public static final String NODE_FEATURE_LOAD = "load";
	public static final String NODE_FEATURE_DISCHARGE = "discharge";

	public static List<EObject> generateNodes(final EPackage dataModel, final EObject dataModelInstance, final List<CargoAllocation> cargoAllocations) {
		final List<EObject> nodes = new ArrayList<EObject>(cargoAllocations.size());
		for (final CargoAllocation cargoAllocation : cargoAllocations) {

			// Build up list of slots assigned to cargo, sorting into loads and discharges
			final List<SlotAllocation> loadSlots = new ArrayList<SlotAllocation>();
			final List<SlotAllocation> dischargeSlots = new ArrayList<SlotAllocation>();
			for (final SlotAllocation slot : cargoAllocation.getSlotAllocations()) {
				if (slot.getSlot() instanceof LoadSlot) {
					loadSlots.add(slot);
				} else if (slot.getSlot() instanceof DischargeSlot) {
					dischargeSlots.add(slot);
				} else {
					// Assume some kind of discharge?
					// dischargeSlots.add((Slot) slot);
				}

			}

			final EObject group = GenericEMFTableDataModel.createGroup(dataModel, dataModelInstance);
			// Create a row for each pair of load and discharge slots in the cargo. This may lead to a row with only one slot
			for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {

				final EObject node = GenericEMFTableDataModel.createRow(dataModel, dataModelInstance, group);
				GenericEMFTableDataModel.setRowValue(dataModel, node, NODE_FEATURE_CARGO, cargoAllocation);
				if (i < loadSlots.size()) {
					GenericEMFTableDataModel.setRowValue(dataModel, node, NODE_FEATURE_LOAD, loadSlots.get(i));
				}
				if (i < dischargeSlots.size()) {
					GenericEMFTableDataModel.setRowValue(dataModel, node, NODE_FEATURE_DISCHARGE, dischargeSlots.get(i));
				}
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * Format this cargo allocation's wiring of slot allocations to a string
	 * 
	 * @param CargoAllocation
	 * @return String
	 */
	public static String getWiringAsString(final CargoAllocation ca) {
		final List<SlotAllocation> slotAllocations = ca.getSlotAllocations();
		final StringBuffer sb = new StringBuffer();

		final SlotAllocation sa0 = slotAllocations.get(0);
		sb.append(sa0 != null ? "'" + sa0.getName() + "'" : "()");

		for (int i = 1; i < slotAllocations.size(); ++i) {
			final SlotAllocation sa = slotAllocations.get(i);
			sb.append(" -- ").append(sa != null ? sa.getName() : "()");
		}

		return sb.toString();
	}

	public static String getSalesWiringAsString(final CargoAllocation ca) {
		if (ca == null) {
			return "";
		}
		final List<SlotAllocation> slotAllocations = ca.getSlotAllocations();
		final StringBuffer sb = new StringBuffer();

		// SlotAllocation sa0 = slotAllocations.get(0);
		// sb.append(sa0 != null ? sa0.getName() : "()");

		boolean first = true;
		for (int i = 0; i < slotAllocations.size(); ++i) {
			final SlotAllocation sa = slotAllocations.get(i);
			if (sa.getSlot() instanceof SpotDischargeSlot) {
				final SpotDischargeSlot slot = (SpotDischargeSlot) sa.getSlot();
				if (!first) {
					sb.append(" -- ");
				} else {
					first = false;
				}
				final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
				sb.append(String.format("'%s-%s'", slot.getMarket().getName(), slot.getWindowStart().format(df)));
			} else if (sa.getSlot() instanceof DischargeSlot) {
				if (!first) {
					sb.append(" -- ");
				} else {
					first = false;
				}
				sb.append("'" + sa.getName() + "'");
			}
		}

		return sb.toString();
	}

	public static String getPurchaseWiringAsString(final CargoAllocation ca) {
		if (ca == null) {
			return "";
		}
		final List<SlotAllocation> slotAllocations = ca.getSlotAllocations();
		final StringBuffer sb = new StringBuffer();

		// SlotAllocation sa0 = slotAllocations.get(0);
		// sb.append(sa0 != null ? sa0.getName() : "()");

		boolean first = true;
		for (int i = 0; i < slotAllocations.size(); ++i) {
			final SlotAllocation sa = slotAllocations.get(i);
			if (sa.getSlot() instanceof SpotLoadSlot) {
				final SpotLoadSlot slot = (SpotLoadSlot) sa.getSlot();
				if (!first) {
					sb.append(" -- ");
				} else {
					first = false;
				}
				final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
				sb.append(String.format("'%s-%s'", slot.getMarket().getName(), slot.getWindowStart().format(df)));
			} else if (sa.getSlot() instanceof LoadSlot) {
				if (!first) {
					sb.append(" -- ");
				} else {
					first = false;
				}
				sb.append("'" + sa.getName() + "'");
			}
		}

		return sb.toString();
	}

	public static String getVesselAssignmentName(final CargoAllocation ca) {
		if (ca == null) {
			return "";
		}
		final Sequence sequence = ca.getSequence();
		if (sequence == null) {
			return "";
		}

		if (sequence.isSetVesselAvailability()) {
			final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
			if (vesselAvailability != null) {
				final Vessel vessel = vesselAvailability.getVessel();
				if (vessel != null) {
					return vessel.getName();
				}
			}
		} else if (sequence.isSetCharterInMarket()) {
			final CharterInMarket charterInMarket = sequence.getCharterInMarket();
			if (charterInMarket != null) {
				final VesselClass vesselClass = charterInMarket.getVesselClass();
				if (vesselClass != null) {
					return vesselClass.getName();
				}
			}
		}
		return "";
	}
}
