/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
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
 * @since 3.0
 * 
 */
public class CargoModelRowTransformer {
	private final Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private final Color darkRed = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
	private final Color green = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
	private final Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private final Color gray = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

	/**
	 * @since 4.0
	 */
	public RootData transform(final AssignmentModel assignmentModel, final CargoModel cargoModel, ScheduleModel scheduleModel, final Map<Object, IStatus> validationInfo) {
		return transform(assignmentModel, cargoModel.getCargoes(), cargoModel.getLoadSlots(), cargoModel.getDischargeSlots(), scheduleModel.getSchedule(), validationInfo);

	}

	/**
	 * Perform the List to {@link RootData} transformation.
	 * 
	 * @param assignmentModel
	 * @param cargoes
	 * @param allLoadSlots
	 * @param allDischargeSlots
	 * @param validationInfo
	 * @return
	 * @since 4.0
	 */
	public RootData transform(final AssignmentModel assignmentModel, final List<Cargo> cargoes, final List<LoadSlot> allLoadSlots, final List<DischargeSlot> allDischargeSlots, Schedule schedule,
			final Map<Object, IStatus> validationInfo) {

		final RootData root = new RootData();

		final Map<Cargo, CargoAllocation> cargoAllocationMap = new HashMap<Cargo, CargoAllocation>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<Slot, SlotAllocation>();
		if (schedule != null) {
			for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
				cargoAllocationMap.put(cargoAllocation.getInputCargo(), cargoAllocation);
			}
			for (final SlotAllocation slotAllocation : schedule.getSlotAllocations()) {
				slotAllocationMap.put(slotAllocation.getSlot(), slotAllocation);
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
			for (final Object slot : cargo.getSlots()) {

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
				final RowData row = new RowData();
				row.setGroup(group);
				group.getRows().add(row);
				root.getRows().add(row);

				row.primaryRecord = i == 0;

				row.cargo = cargo;
				// Add slot if possible
				if (i < loadSlots.size()) {
					row.loadSlot = loadSlots.get(i);
				}
				if (i < dischargeSlots.size()) {
					row.dischargeSlot = dischargeSlots.get(i);
				}
				// Add element assignment to all rows (TODO: just add it once?)
				final ElementAssignment elementAssignment = AssignmentEditorHelper.getElementAssignment(assignmentModel, cargo);
				if (elementAssignment != null) {
					row.elementAssignment = elementAssignment;
				}

				// Set terminal colours to valid - even if slot is missing, in such cases the terminal will not be rendered
				row.loadTerminalColour = green;
				row.dischargeTerminalColour = green;

				// patch up the WireData information with the new RowData objects
				for (final WireData wire : group.getWires()) {
					if (wire.loadSlot != null && wire.loadSlot == row.loadSlot) {
						wire.loadRowData = row;
					}
					if (wire.dischargeSlot != null && wire.dischargeSlot == row.dischargeSlot) {
						wire.dischargeRowData = row;
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

				final RowData row = new RowData();
				row.setGroup(group);
				root.getRows().add(row);
				row.loadSlot = slot;

				if (slot.isOptional()) {
					row.loadTerminalColour = green;
				} else {
					row.loadTerminalColour = red;
				}
				row.dischargeTerminalColour = red;
			}
		}

		// Process all discharges without a cargo
		for (final DischargeSlot slot : allDischargeSlots) {
			if (slot.getCargo() == null) {

				final GroupData group = new GroupData();
				root.getGroups().add(group);
				group.getObjects().add(slot);

				final RowData row = new RowData();
				row.setGroup(group);
				root.getRows().add(row);
				row.dischargeSlot = slot;

				if (slot.isOptional()) {
					row.dischargeTerminalColour = green;
				} else {
					row.dischargeTerminalColour = red;
				}
				row.loadTerminalColour = red;
			}
		}

		// Construct arrays of data so that index X across all arrays points to the same row
		for (final RowData rd : root.getRows()) {
			root.getCargoes().add(rd.getCargo());
			root.getLoadSlots().add(rd.getLoadSlot());
			root.getDischargeSlots().add(rd.getDischargeSlot());

			// Hook up allocation objects
			rd.cargoAllocation = cargoAllocationMap.get(rd.cargo);
			rd.loadAllocation = slotAllocationMap.get(rd.loadSlot);
			rd.dischargeAllocation = slotAllocationMap.get(rd.dischargeSlot);

		}

		return root;
	}

	/**
	 * The {@link RowData} represents a single row in the trades viewer. It extends EObject for use with {@link EMFPath}, and specifically {@link RowDataEMFPath}
	 * 
	 */
	public static class RowData extends EObjectImpl {

		GroupData group;
		Cargo cargo;
		CargoAllocation cargoAllocation;
		LoadSlot loadSlot;
		SlotAllocation loadAllocation;
		DischargeSlot dischargeSlot;
		SlotAllocation dischargeAllocation;

		ElementAssignment elementAssignment;

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
						&& Equality.isEqual(elementAssignment, other.elementAssignment);
			}

			return false;
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

		/**
		 * @since 4.0
		 */
		public void setElementAssignment(final ElementAssignment elementAssignment) {
			this.elementAssignment = elementAssignment;
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

		/**
		 * @since 4.0
		 */
		public ElementAssignment getElementAssignment() {
			return elementAssignment;
		}

		public void setGroup(final GroupData group) {
			this.group = group;
		}

		/**
		 * @since 4.0
		 */
		public boolean isPrimaryRecord() {
			return primaryRecord;
		}

		/**
		 * @since 4.0
		 */
		public void setPrimaryRecord(boolean primaryRecord) {
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

	}

	/**
	 * Represents a single cargo (or a single slot). For simple L->D cargoes this will contain one slot. For complex cargoes, there may be multiple rows.
	 * 
	 * @author sg
	 * 
	 */
	public static class GroupData extends EObjectImpl {

		private final List<RowData> rows = new ArrayList<RowData>();
		private final List<EObject> objects = new ArrayList<EObject>();
		private final List<WireData> wires = new ArrayList<WireData>();

		public List<EObject> getObjects() {
			return objects;
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
		private boolean primaryRecordOnly;

		/**
		 * @since 4.0
		 */
		public RowDataEMFPath(boolean primaryRecordOnly, final Type type, final Iterable<?> path) {
			super(true, path);
			this.type = type;
			this.primaryRecordOnly = primaryRecordOnly;
		}

		/**
		 * @since 4.0
		 */
		public RowDataEMFPath(boolean primaryRecordOnly, final Type type, final Object... path) {
			super(true, path);
			this.type = type;
			this.primaryRecordOnly = primaryRecordOnly;
		}

		@Override
		public Object get(final EObject root, final int depth) {

			if (root instanceof RowData) {
				final RowData rowData = (RowData) root;
				boolean showRecord = primaryRecordOnly ? rowData.primaryRecord : true;
				switch (type) {
				case ASSIGNMENT:
					return showRecord ? super.get(rowData.elementAssignment, depth) : null;
				case CARGO:
					return showRecord ? super.get(rowData.cargo, depth) : null;
				case DISCHARGE:
					return super.get(rowData.dischargeSlot, depth);
				case LOAD:
					return super.get(rowData.loadSlot, depth);
				case CARGO_ALLOCATION:
					return showRecord ? super.get(rowData.cargoAllocation, depth) : null;
				case DISCHARGE_ALLOCATION:
					return super.get(rowData.dischargeAllocation, depth);
				case LOAD_ALLOCATION:
					return super.get(rowData.loadAllocation, depth);
				}
			}
			return super.get(root, depth);
		}
	}

	public enum Type {
		CARGO, LOAD, DISCHARGE, ASSIGNMENT, CARGO_ALLOCATION, LOAD_ALLOCATION, DISCHARGE_ALLOCATION
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
				wire.colour = validWire ? (c.isAllowRewiring() ? gray : black) : darkRed;
			}
		}
	}
}