package com.mmxlabs.shiplingo.platform.reports.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;

/**
 * @since 4.0
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

}
