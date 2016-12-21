/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.util.emfpath.EMFPath;

/**
 * A class to transform the {@link CargoModel} lists of {@link Cargo}, {@link LoadSlot} and {@link DischargeSlot}s into a single table - complete with vessel {@link ElementAssignment}. The
 * {@link #transform(InputModel, List, List, List, Map)} method returns a {@link RootData} object which encodes a {@link RowData} for each table row, a {@link GroupData} defining related rows and the
 * wiring, colour information for terminal and wiring colouring.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoModelRowTransformer {
	static final Color InvalidTerminalColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private final Color InvalidWireColour = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);

	private static final Color Grey = new Color(Display.getDefault(), new RGB(64, 64, 64));
	static final Color ValidTerminalColour = TradesWiringDiagram.Light_Green;
	private final Color RewirableColour = Grey;// Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private final Color FixedWireColour = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

	/**
	 */
	public RootData transform(final CargoModel cargoModel, final ScheduleModel scheduleModel, final Map<Object, IStatus> validationInfo, final RootData existingData) {
		return transform(cargoModel.getCargoes(), cargoModel.getLoadSlots(), cargoModel.getDischargeSlots(), scheduleModel.getSchedule(), validationInfo, existingData);

	}

	/**
	 * Returns any slot the specified slot is linked to in a ship-to-ship transfer
	 * 
	 * @param cargoes
	 * @param allLoadSlots
	 * @param allDischargeSlots
	 * @param validationInfo
	 * @return
	 */
	public RootData transform(final List<Cargo> cargoes, final List<LoadSlot> allLoadSlots, final List<DischargeSlot> allDischargeSlots, final Schedule schedule,
			final Map<Object, IStatus> validationInfo, final RootData existingWiring) {

		final RootData root = new RootData();

		final Map<Slot, RowData> rowDataMap = new HashMap<Slot, CargoModelRowTransformer.RowData>();

		final Map<Cargo, CargoAllocation> cargoAllocationMap = new HashMap<Cargo, CargoAllocation>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<Slot, SlotAllocation>();
		final Map<Slot, MarketAllocation> marketAllocationMap = new HashMap<Slot, MarketAllocation>();
		final Map<Slot, OpenSlotAllocation> openAllocationMap = new HashMap<>();
		if (schedule != null) {
			for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
//				cargoAllocationMap.put(cargoAllocation.getInputCargo(), cargoAllocation);
			}
			for (final OpenSlotAllocation openAllocation : schedule.getOpenSlotAllocations()) {
				openAllocationMap.put(openAllocation.getSlot(), openAllocation);
			}
			for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
				marketAllocationMap.put(marketAllocation.getSlot(), marketAllocation);
			}
			for (final SlotAllocation slotAllocation : schedule.getSlotAllocations()) {
				slotAllocationMap.put(slotAllocation.getSlot(), slotAllocation);
			}
		}

		final Map<LoadSlot, RowData> existingLoadSlotToRowData = new HashMap<LoadSlot, CargoModelRowTransformer.RowData>();
		final Map<DischargeSlot, RowData> existingDischargeSlotToRowData = new HashMap<DischargeSlot, CargoModelRowTransformer.RowData>();

		if (existingWiring != null) {
			// Take existing row data and clone
			for (final RowData rowData : existingWiring.rows) {
				if (rowData.cargo != null) {
					if (!cargoes.contains(rowData.cargo)) {
						// Cargo has been removed
						rowData.cargo = null;
						rowData.cargoAllocation = null;
					}
				}

				if (rowData.loadSlot != null) {
					if (allLoadSlots.contains(rowData.loadSlot)) {
						// Keep
						existingLoadSlotToRowData.put(rowData.loadSlot, rowData);
					} else {
						// Load slot has been removed
						rowData.loadSlot = null;
						rowData.loadAllocation = null;
					}
				}

				if (rowData.dischargeSlot != null) {
					if (allDischargeSlots.contains(rowData.dischargeSlot)) {
						// Keep
						existingDischargeSlotToRowData.put(rowData.dischargeSlot, rowData);
					} else {
						// Discharge slot has been removed
						rowData.dischargeSlot = null;
						rowData.dischargeAllocation = null;
					}
				}
			}
		}

		// Loop through all cargoes first, generating full cargo row items
		for (final Cargo cargo : cargoes) {
			final GroupData group = new GroupData();
			root.getGroups().add(group);
			group.getObjects().add(cargo);

			// Build up list of slots assigned to cargo, sorting into loads and discharges
			final List<LoadSlot> loadSlots = new ArrayList<LoadSlot>();
			final List<DischargeSlot> dischargeSlots = new ArrayList<DischargeSlot>();
			for (final Object slot : cargo.getSortedSlots()) {
				if (slot instanceof LoadSlot) {
					loadSlots.add((LoadSlot) slot);
				} else if (slot instanceof DischargeSlot) {
					dischargeSlots.add((DischargeSlot) slot);
				} else {
					// Assume some kind of discharge?
					// dischargeSlots.add((Slot) slot);
				}

			}

			// Generate the wiring - currently this is the full many-many mapping between loads and discharges.
			// In future this should be better (in some way...)
			for (final Slot slot : cargo.getSlots()) {
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					for (final Object slot2 : cargo.getSlots()) {
						if (slot2 instanceof DischargeSlot) {
							final DischargeSlot dischargeSlot = (DischargeSlot) slot2;
							final WireData wire = new WireData();
							wire.loadSlot = loadSlot;
							wire.dischargeSlot = dischargeSlot;
							group.getWires().add(wire);
						}
					}
				}
			}
			// Set the colour for all cargo wires.
			setWiringColour(validationInfo, group);

			// Create a row for each pair of load and discharge slots in the cargo. This may lead to a row with only one slot
			for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {

				LoadSlot loadSlot = null;
				if (i < loadSlots.size()) {
					loadSlot = loadSlots.get(i);
				}
				DischargeSlot dischargeSlot = null;
				if (i < dischargeSlots.size()) {
					dischargeSlot = dischargeSlots.get(i);
				}

				RowData loadRowData = null;
				if (existingLoadSlotToRowData.containsKey(loadSlot)) {
					loadRowData = existingLoadSlotToRowData.get(loadSlot);
				}
				RowData dischargeRowData = null;
				if (existingDischargeSlotToRowData.containsKey(dischargeSlot)) {
					dischargeRowData = existingDischargeSlotToRowData.get(dischargeSlot);
				}
				if (loadRowData == null && dischargeRowData == null) {
					loadRowData = new RowData();
					dischargeRowData = loadRowData;
				} else if (loadRowData == null && loadSlot != null) {
					loadRowData = new RowData();
				} else if (dischargeRowData == null && dischargeSlot != null) {
					dischargeRowData = new RowData();
				}

				if (loadRowData == null && dischargeRowData == null) {
					// We shouldn't really get here
					continue;
				}

				// In some cases where we only have one slot (e.g. as part of LDD) we can end up with a null row data object, so use the other half as the reference for the rest of the loop
				if (loadRowData == null) {
					loadRowData = dischargeRowData;
				} else if (dischargeRowData == null) {
					dischargeRowData = loadRowData;
				}
				// Attempt to determine primary record. TODO: Confirm this is correct for LDD case
				loadRowData.primaryRecord = i == 0;
				dischargeRowData.primaryRecord = i == 0;

				loadRowData.loadSlot = loadSlot;
				dischargeRowData.dischargeSlot = dischargeSlot;

				rowDataMap.put(loadSlot, loadRowData);
				rowDataMap.put(dischargeSlot, dischargeRowData);

				// final RowData row = new RowData();
				loadRowData.setGroup(group);
				// dischargeRowData.setGroup(group);
				group.getRows().add(loadRowData);
				// group.getRows().add(loadRowData);

				if (!root.getRows().contains(loadRowData)) {
					root.getRows().add(loadRowData);
				}
				if (!root.getRows().contains(dischargeRowData)) {
					root.getRows().add(dischargeRowData);
				}
				loadRowData.cargo = cargo;

				// Set terminal colours to valid - even if slot is missing, in such cases the terminal will not be rendered
				loadRowData.loadTerminalColour = ValidTerminalColour;
				dischargeRowData.dischargeTerminalColour = ValidTerminalColour;

				// patch up the WireData information with the new RowData objects
				for (final WireData wire : group.getWires()) {
					if (wire.loadSlot != null && wire.loadSlot == loadRowData.loadSlot) {
						wire.loadRowData = loadRowData;
					}
					if (wire.dischargeSlot != null && wire.dischargeSlot == dischargeRowData.dischargeSlot) {
						wire.dischargeRowData = dischargeRowData;
					}
				}
			}

		}

		// Process all loads without a cargo
		for (final LoadSlot slot : allLoadSlots) {
			if (slot.getCargo() == null) {

				final GroupData group = new GroupData();
				root.getGroups().add(group);
				group.getObjects().add(slot);

				final RowData row;
				if (existingLoadSlotToRowData.containsKey(slot)) {
					row = existingLoadSlotToRowData.get(slot);
				} else {
					row = new RowData();
				}

				if (!root.getRows().contains(row)) {
					root.getRows().add(row);
				}

				row.setGroup(group);
				row.loadSlot = slot;

				rowDataMap.put(slot, row);

				if (slot.isOptional()) {
					row.loadTerminalColour = ValidTerminalColour;
				} else {
					row.loadTerminalColour = InvalidTerminalColour;
				}
				if (row.dischargeSlot == null) {
					row.dischargeTerminalColour = InvalidTerminalColour;
				}
			}
		}

		// Process all discharges without a cargo
		for (final DischargeSlot slot : allDischargeSlots) {
			if (slot.getCargo() == null) {

				final GroupData group = new GroupData();
				root.getGroups().add(group);
				group.getObjects().add(slot);

				final RowData row;
				if (existingDischargeSlotToRowData.containsKey(slot)) {
					row = existingDischargeSlotToRowData.get(slot);
				} else {
					row = new RowData();
				}

				if (!root.getRows().contains(row)) {
					root.getRows().add(row);
				}
				row.setGroup(group);
				row.dischargeSlot = slot;
				rowDataMap.put(slot, row);

				if (slot.isOptional()) {
					row.dischargeTerminalColour = ValidTerminalColour;
				} else {
					row.dischargeTerminalColour = InvalidTerminalColour;
				}
				if (row.loadSlot == null) {
					row.loadTerminalColour = InvalidTerminalColour;
				}
			}
		}

		// Construct arrays of data so that index X across all arrays points to the same row
		for (final RowData rd : root.getRows()) {
			root.getCargoes().add(rd.getCargo());
			root.getLoadSlots().add(rd.getLoadSlot());
			root.getDischargeSlots().add(rd.getDischargeSlot());

			// Hook up allocation objects
			rd.cargoAllocation = cargoAllocationMap.get(rd.cargo);
			rd.marketAllocation = marketAllocationMap.get((rd.loadSlot == null) ? rd.dischargeSlot : rd.loadSlot);
			rd.openSlotAllocation = openAllocationMap.get((rd.loadSlot == null) ? rd.dischargeSlot : rd.loadSlot);
			rd.loadAllocation = slotAllocationMap.get(rd.loadSlot);
			rd.dischargeAllocation = slotAllocationMap.get(rd.dischargeSlot);

		}

		for (final RowData rd : root.getRows()) {
			final Slot transferSlot = getLinkedSlot(rd.loadSlot);
			if (transferSlot != null) {
				final WireData wire = rd.getGroup().addShipToShipWire(rd.loadSlot);
				wire.loadRowData = rd;
				wire.dischargeRowData = rowDataMap.get(transferSlot);
			}
		}

		return root;
	}

	/**
	 * Returns any slot the specified slot is linked to in a ship-to-ship transfer
	 * 
	 * @param slot
	 */
	private static Slot getLinkedSlot(final Slot slot) {
		if (slot instanceof LoadSlot) {
			return ((LoadSlot) slot).getTransferFrom();
		} else if (slot instanceof DischargeSlot) {
			return ((DischargeSlot) slot).getTransferTo();
		}

		return null;
	}

	//
	// private List<GroupData> makeGroups(final AssignmentModel assignmentModel, final List<Cargo> cargoes, final List<LoadSlot> allLoadSlots, final List<DischargeSlot> allDischargeSlots,
	// Schedule schedule, final Map<Object, IStatus> validationInfo) {
	// final List<GroupData> result = new LinkedList<GroupData>();
	//
	// // make a modifiable copy of the cargoes list
	// final List<Cargo> cargoesCopy = new LinkedList<Cargo>(cargoes);
	// final List<Slot> unpairedSlots = new LinkedList<Slot>();
	//
	// for (Slot slot : allLoadSlots) {
	// if (slot.getCargo() == null) {
	// unpairedSlots.add(slot);
	// }
	// }
	// for (Slot slot : allDischargeSlots) {
	// if (slot.getCargo() == null) {
	// unpairedSlots.add(slot);
	// }
	// }
	//
	// // build a list of GroupData objects from the cargoes, merging them into groups if appropriate
	// while (!cargoesCopy.isEmpty()) {
	// Cargo cargo = cargoesCopy.remove(0);
	// result.add(mergeCargoes(cargo, cargoesCopy, unpairedSlots, assignmentModel));
	// }
	//
	// // and add any necessary groups of unpaired cargoes
	// while (!unpairedSlots.isEmpty()) {
	// GroupData group = new GroupData();
	//
	// Slot slot = unpairedSlots.remove(0);
	//
	// group.addLoneSlot(slot);
	//
	// Slot linkedSlot = getLinkedSlot(slot);
	// if (unpairedSlots.contains(linkedSlot)) {
	// unpairedSlots.remove(linkedSlot);
	// group.addLoneSlot(linkedSlot);
	// group.addShipToShipWire(slot);
	// }
	//
	// group.patchWires();
	//
	// result.add(group);
	// }
	//
	// return result;
	// }
	//
	// /**
	// * Perform the List to {@link RootData} transformation.
	// *
	// * @param assignmentModel
	// * @param cargoes
	// * @param allLoadSlots
	// * @param allDischargeSlots
	// * @param validationInfo
	// * @return
	// */
	// public RootData transform(final AssignmentModel assignmentModel, final List<Cargo> cargoes, final List<LoadSlot> allLoadSlots, final List<DischargeSlot> allDischargeSlots, Schedule schedule,
	// final Map<Object, IStatus> validationInfo) {
	//
	// final RootData root = new RootData();
	//
	// final Map<Cargo, CargoAllocation> cargoAllocationMap = new HashMap<Cargo, CargoAllocation>();
	// final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<Slot, SlotAllocation>();
	// final Map<Slot, MarketAllocation> marketAllocationMap = new HashMap<Slot, MarketAllocation>();
	// if (schedule != null) {
	// for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
	// cargoAllocationMap.put(cargoAllocation.getInputCargo(), cargoAllocation);
	// }
	// for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
	// marketAllocationMap.put(marketAllocation.getSlot(), marketAllocation);
	// }
	// for (final SlotAllocation slotAllocation : schedule.getSlotAllocations()) {
	// slotAllocationMap.put(slotAllocation.getSlot(), slotAllocation);
	// }
	// }
	//
	// List<GroupData> groups = makeGroups(assignmentModel, cargoes, allLoadSlots, allDischargeSlots, schedule, validationInfo);
	//
	// for (GroupData group : groups) {
	// root.getGroups().add(group);
	// setWiringColour(validationInfo, group);
	// root.getRows().addAll(group.getRows());
	// }
	//
	// /*
	// * // Process all loads without a cargo for (final LoadSlot slot : allLoadSlots) { if (slot.getCargo() == null) {
	// *
	// * final GroupData group = new GroupData(); root.getGroups().add(group); group.getObjects().add(slot);
	// *
	// * final RowData row = new RowData(); row.setGroup(group); root.getRows().add(row); row.loadSlot = slot;
	// *
	// * if (slot.isOptional()) { row.loadTerminalColour = green; } else { row.loadTerminalColour = red; } row.dischargeTerminalColour = red; } }
	// *
	// * // Process all discharges without a cargo for (final DischargeSlot slot : allDischargeSlots) { if (slot.getCargo() == null) {
	// *
	// * final GroupData group = new GroupData(); root.getGroups().add(group); group.getObjects().add(slot);
	// *
	// * final RowData row = new RowData(); row.setGroup(group); root.getRows().add(row); row.dischargeSlot = slot;
	// *
	// * if (slot.isOptional()) { row.dischargeTerminalColour = green; } else { row.dischargeTerminalColour = red; } row.loadTerminalColour = red; } }
	// */
	//
	// // Construct arrays of data so that index X across all arrays points to the same row
	// for (final RowData rd : root.getRows()) {
	// root.getCargoes().add(rd.getCargo());
	// root.getLoadSlots().add(rd.getLoadSlot());
	// root.getDischargeSlots().add(rd.getDischargeSlot());
	//
	// // Hook up allocation objects
	// rd.cargoAllocation = cargoAllocationMap.get(rd.cargo);
	// rd.marketAllocation = marketAllocationMap.get((rd.loadSlot == null) ? rd.dischargeSlot : rd.loadSlot);
	// rd.loadAllocation = slotAllocationMap.get(rd.loadSlot);
	// rd.dischargeAllocation = slotAllocationMap.get(rd.dischargeSlot);
	//
	// }
	//
	// return root;
	// }

	/**
	 * The {@link RowData} represents a single row in the trades viewer. It extends EObject for use with {@link EMFPath}, and specifically {@link RowDataEMFPath}
	 * 
	 */
	public static class RowData extends EObjectImpl {

		GroupData group;
		Cargo cargo;
		CargoAllocation cargoAllocation;
		OpenSlotAllocation openSlotAllocation;
		MarketAllocation marketAllocation;
		LoadSlot loadSlot;
		SlotAllocation loadAllocation;
		DischargeSlot dischargeSlot;
		SlotAllocation dischargeAllocation;

		// This is the RowData with the cargo defining load slot
		boolean primaryRecord;

		// GUI STATE
		Color loadTerminalColour;
		Color dischargeTerminalColour;

		/**
		 * This is used in the {@link EObjectTableViewer} implementation of {@link ViewerComparator} for the fixed sort order.
		 */
		@Override
		public boolean equals(final Object obj) {

			if (obj instanceof RowData) {
				final RowData other = (RowData) obj;
				return Equality.isEqual(cargo, other.cargo) && Equality.isEqual(loadSlot, other.loadSlot) && Equality.isEqual(dischargeSlot, other.dischargeSlot)
						&& Equality.isEqual(marketAllocation, other.marketAllocation) && Equality.isEqual(openSlotAllocation, other.openSlotAllocation);
			}

			return false;
		}

		@Override
		public int hashCode() {
			// Implemented to "fix" findbugs warning.
			return super.hashCode();
		}

		public void setCargo(final Cargo cargo) {
			this.cargo = cargo;
		}

		public void setLoadSlot(final LoadSlot loadSlot) {
			this.loadSlot = loadSlot;
		}

		public void setDischargeSlot(final DischargeSlot dischargeSlot) {
			this.dischargeSlot = dischargeSlot;
		}

		public GroupData getGroup() {
			return group;
		}

		public Cargo getCargo() {
			return cargo;
		}

		public LoadSlot getLoadSlot() {
			return loadSlot;
		}

		public DischargeSlot getDischargeSlot() {
			return dischargeSlot;
		}

		public void setGroup(final GroupData group) {
			this.group = group;
		}

		/**
		 */
		public boolean isPrimaryRecord() {
			return primaryRecord;
		}

		/**
		 */
		public void setPrimaryRecord(final boolean primaryRecord) {
			this.primaryRecord = primaryRecord;
		}

	}

	/**
	 * Represents a "wire" in the wiring diagram
	 * 
	 */
	public static class WireData {

		Color colour;
		LoadSlot loadSlot;
		RowData loadRowData;
		DischargeSlot dischargeSlot;
		RowData dischargeRowData;
		boolean dashed;

	}

	/**
	 * Represents a single cargo (or a single slot). For simple L->D cargoes this will contain one slot. For complex cargoes, there may be multiple rows.
	 * 
	 * @author sg
	 * 
	 */
	public class GroupData extends EObjectImpl {

		private final List<RowData> rows = new ArrayList<RowData>();
		private final List<EObject> objects = new ArrayList<EObject>();
		private final List<WireData> wires = new ArrayList<WireData>();

		public List<EObject> getObjects() {
			return objects;
		}

		/**
		 */
		public void addLoneSlot(final Slot slot) {
			if (slot.getCargo() == null) {

				getObjects().add(slot);

				final RowData row = new RowData();
				row.setGroup(this);
				getRows().add(row);

				if (slot instanceof LoadSlot) {
					// root.getRows().add(row);
					row.loadSlot = (LoadSlot) slot;

					if (slot.isOptional()) {
						row.loadTerminalColour = ValidTerminalColour;
					} else {
						row.loadTerminalColour = InvalidTerminalColour;
					}
					row.dischargeTerminalColour = InvalidTerminalColour;
				} else if (slot instanceof DischargeSlot) {
					row.dischargeSlot = (DischargeSlot) slot;

					if (slot.isOptional()) {
						row.dischargeTerminalColour = ValidTerminalColour;
					} else {
						row.dischargeTerminalColour = InvalidTerminalColour;
					}
					row.loadTerminalColour = InvalidTerminalColour;
				}

			}

		}

		/**
		 */
		public WireData addShipToShipWire(final Slot slot) {
			// and add a wire to the group, showing the ship-to-ship transfer
			final WireData wire = new WireData();
			getWires().add(wire);
			wire.colour = FixedWireColour;
			wire.dashed = true;

			LoadSlot loadSlot = null;
			DischargeSlot dischargeSlot = null;

			if (slot instanceof LoadSlot) {
				loadSlot = (LoadSlot) slot;
				dischargeSlot = ((LoadSlot) slot).getTransferFrom();
			} else if (slot instanceof DischargeSlot) {
				dischargeSlot = (DischargeSlot) slot;
				loadSlot = ((DischargeSlot) slot).getTransferTo();
			}

			wire.loadSlot = loadSlot;
			wire.dischargeSlot = dischargeSlot;
			return wire;
		}

		/**
		 */
		public void addCargo(final Cargo cargo) {
			getObjects().add(cargo);

			// Build up list of slots assigned to cargo, sorting into loads and discharges
			final List<LoadSlot> loadSlots = new ArrayList<LoadSlot>();
			final List<DischargeSlot> dischargeSlots = new ArrayList<DischargeSlot>();
			for (final Object slot : cargo.getSortedSlots()) {
				if (slot instanceof LoadSlot) {
					loadSlots.add((LoadSlot) slot);
				} else if (slot instanceof DischargeSlot) {
					dischargeSlots.add((DischargeSlot) slot);
				} else {
					// Assume some kind of discharge?
					// dischargeSlots.add((Slot) slot);
				}

			}

			// Generate the wiring - currently this is the full many-many mapping between loads and discharges.
			// In future this should be better (in some way...)
			for (final Object slot : cargo.getSlots()) {

				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					for (final Object slot2 : cargo.getSlots()) {
						if (slot2 instanceof DischargeSlot) {
							final DischargeSlot dischargeSlot = (DischargeSlot) slot2;
							final WireData wire = new WireData();
							wire.loadSlot = loadSlot;
							wire.dischargeSlot = dischargeSlot;
							getWires().add(wire);
						}
					}
				}
			}
			// Set the colour for all cargo wires.
			// setWiringColour(validationInfo, group);

			// Create a row for each pair of load and discharge slots in the cargo. This may lead to a row with only one slot
			for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {
				final RowData row = new RowData();
				row.setGroup(this);
				getRows().add(row);
				// root.getRows().add(row);

				row.primaryRecord = i == 0;

				row.cargo = cargo;
				// Add slot if possible
				if (i < loadSlots.size()) {
					row.loadSlot = loadSlots.get(i);
				}
				if (i < dischargeSlots.size()) {
					row.dischargeSlot = dischargeSlots.get(i);
				}

				// Set terminal colours to valid - even if slot is missing, in such cases the terminal will not be rendered
				row.loadTerminalColour = ValidTerminalColour;
				row.dischargeTerminalColour = ValidTerminalColour;
			}
		}

		/**
		 */
		public void patchWires() {
			for (final RowData row : getRows()) {
				// patch up the WireData information with the new RowData objects
				for (final WireData wire : getWires()) {
					if (wire.loadSlot != null && wire.loadSlot == row.loadSlot) {
						wire.loadRowData = row;
					}
					if (wire.dischargeSlot != null && wire.dischargeSlot == row.dischargeSlot) {
						wire.dischargeRowData = row;
					}
				}
			}
		}

		public List<RowData> getRows() {
			return rows;
		}

		public List<WireData> getWires() {
			return wires;
		}

	}

	/**
	 * The top node in the data structure.
	 * 
	 */
	public static class RootData extends EObjectImpl {

		private final List<RowData> rows = new ArrayList<RowData>();
		private final List<GroupData> groups = new ArrayList<GroupData>();

		private final ArrayList<Cargo> cargoes = new ArrayList<Cargo>();
		private final ArrayList<LoadSlot> loadSlots = new ArrayList<LoadSlot>();
		private final ArrayList<DischargeSlot> dischargeSlots = new ArrayList<DischargeSlot>();

		public List<GroupData> getGroups() {
			return groups;
		}

		public List<RowData> getRows() {
			return rows;
		}

		public List<Cargo> getCargoes() {
			return cargoes;
		}

		public List<LoadSlot> getLoadSlots() {
			return loadSlots;
		}

		public List<DischargeSlot> getDischargeSlots() {
			return dischargeSlots;
		}

	}

	/**
	 * The {@link RowDataEMFPath} class is used to bridge our custom {@link RowData} objects with our reflective EMF based UI. This permits the step from {@link RowData} -> EObject , then on to the
	 * normal EMF Path navigation. This can be passed into the normal {@link EObjectTableViewer} and {@link BasicAttributeManipulator} based API's.
	 * 
	 */
	public static class RowDataEMFPath extends EMFPath {

		private final Type type;

		/**
		 * Indicate that we want only the "primary record" - i.e. the row with the cargo defining load.
		 */
		private final boolean primaryRecordOnly;

		/**
		 */
		public RowDataEMFPath(final boolean primaryRecordOnly, final Type type, final Iterable<ETypedElement> path) {
			super(true, path);
			this.type = type;
			this.primaryRecordOnly = primaryRecordOnly;
		}

		/**
		 */
		public RowDataEMFPath(final boolean primaryRecordOnly, final Type type, final ETypedElement... path) {
			super(true, path);
			this.type = type;
			this.primaryRecordOnly = primaryRecordOnly;
		}
		public RowDataEMFPath(final boolean failSilently, final boolean primaryRecordOnly, final Type type, final ETypedElement... path) {
			super(failSilently, path);
			this.type = type;
			this.primaryRecordOnly = primaryRecordOnly;
		}

		@Override
		public Object get(final EObject root, final int depth) {

			if (root instanceof RowData) {
				final RowData rowData = (RowData) root;
				final boolean showRecord = primaryRecordOnly ? rowData.primaryRecord : true;
				switch (type) {
				case CARGO:
					return showRecord ? super.get(rowData.cargo, depth) : null;
				case DISCHARGE:
					return super.get(rowData.dischargeSlot, depth);
				case LOAD:
					return super.get(rowData.loadSlot, depth);
				case CARGO_ALLOCATION:
					return showRecord ? super.get(rowData.cargoAllocation, depth) : null;
				case MARKET_ALLOCATION:
					return super.get(rowData.marketAllocation, depth);
				case CARGO_OR_MARKET_ALLOCATION:
					return (rowData.cargoAllocation != null) ? (showRecord ? super.get(rowData.cargoAllocation, depth) : null) : super.get(rowData.marketAllocation, depth);
				case CARGO_OR_MARKET_OR_OPEN_ALLOCATION:
					if (rowData.cargoAllocation == null) {
						if (rowData.marketAllocation != null) {
							return super.get(rowData.marketAllocation);
						} else {
							return super.get(rowData.openSlotAllocation);
						}
					} else {
						return showRecord ? super.get(rowData.cargoAllocation, depth) : null;
					}
				case DISCHARGE_ALLOCATION:
					return super.get(rowData.dischargeAllocation, depth);
				case LOAD_ALLOCATION:
					return super.get(rowData.loadAllocation, depth);
				case LOAD_OR_DISCHARGE: {
					final Object result = get(rowData.loadSlot, depth);
					return result != null ? result : get(rowData.dischargeSlot, depth);

				}
				case DISCHARGE_OR_LOAD: {
					final Object result = get(rowData.dischargeSlot, depth);
					return result != null ? result : get(rowData.loadSlot, depth);

				}
				case SLOT_OR_CARGO: {

					Object result = null;
					if (rowData.loadSlot != null && rowData.loadSlot.isDESPurchase()) {
						result = get(rowData.loadSlot, depth);
					}
					if (result == null && rowData.dischargeSlot != null && rowData.dischargeSlot.isFOBSale()) {
						result = get(rowData.dischargeSlot, depth);
					}
					if (result == null) {
						result = get(rowData.cargo, depth);
					}
					return result;
				}

				}
			}
			return super.get(root, depth);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + (type.hashCode());
			return result;
		}
	}

	public enum Type {
		CARGO, LOAD, DISCHARGE, CARGO_ALLOCATION, LOAD_ALLOCATION, DISCHARGE_ALLOCATION,
		/**
		 */
		DISCHARGE_OR_LOAD,
		/**
		 */
		LOAD_OR_DISCHARGE,

		/**
		 */
		MARKET_ALLOCATION,
		/**
		 */
		CARGO_OR_MARKET_ALLOCATION,

		/**
		 */
		SLOT_OR_CARGO,
		/**
		 */
		CARGO_OR_MARKET_OR_OPEN_ALLOCATION
	}

	/**
	 * Method to update wiring colours without rebuilding the {@link RootData} object (e.g. after the validation status has changed).
	 * 
	 * @param rootData
	 * @param validationInformation
	 */
	public void updateWiringValidity(final RootData rootData, final Map<Object, IStatus> validationInformation) {

		for (final GroupData g : rootData.getGroups()) {
			setWiringColour(validationInformation, g);
		}
	}

	/**
	 * Method to query the validation information for issues with a cargo and update the wiring colour as appropriate
	 * 
	 * @param validationInformation
	 * @param g
	 */
	private void setWiringColour(final Map<Object, IStatus> validationInformation, final GroupData g) {
		boolean validWire = true;
		Cargo c = null;
		for (final Object obj : g.getObjects()) {

			if (obj instanceof Cargo) {
				c = (Cargo) obj;
			}

			if (validationInformation.containsKey(obj)) {
				final IStatus status = validationInformation.get(obj);
				if (status.matches(IStatus.ERROR)) {
					validWire = false;
				}
			}
		}
		if (c != null) {
			for (final WireData wire : g.getWires()) {
				wire.colour = validWire ? (c.isAllowRewiring() ? RewirableColour : FixedWireColour) : InvalidWireColour;
			}
		}
	}

}