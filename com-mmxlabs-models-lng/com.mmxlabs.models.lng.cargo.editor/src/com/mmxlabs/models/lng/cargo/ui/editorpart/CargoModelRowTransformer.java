/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelFactory;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.CargoEditorModelFactoryImpl;
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
 * A class to transform the {@link CargoModel} lists of {@link Cargo},
 * {@link LoadSlot} and {@link DischargeSlot}s into a single table - complete
 * with vessel {@link ElementAssignment}. The
 * {@link #transform(InputModel, List, List, List, Map)} method returns a
 * {@link RootData} object which encodes a {@link RowData} for each table row, a
 * {@link GroupData} defining related rows and the wiring, colour information
 * for terminal and wiring colouring.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoModelRowTransformer {
	static final TradesWiringColourScheme Colour_Scheme = new TradesWiringColourScheme();


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

		final Map<Slot<?>, TradesRow> tradesRowMap = new HashMap<>();

		final Map<Slot<?>, CargoAllocation> cargoAllocationMap = new HashMap<>();
		final Map<Slot<?>, SlotAllocation> slotAllocationMap = new HashMap<>();
		final Map<Slot<?>, MarketAllocation> marketAllocationMap = new HashMap<>();
		final Map<Slot<?>, OpenSlotAllocation> openAllocationMap = new HashMap<>();
		if (schedule != null) {
			for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
				// Map to first load slot
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot<?> slot = slotAllocation.getSlot();
					cargoAllocationMap.put(slot, cargoAllocation);
					break;
				}
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

		final Map<LoadSlot, TradesRow> existingLoadSlotToTradesRow = new HashMap<>();
		final Map<DischargeSlot, TradesRow> existingDischargeSlotToTradesRow = new HashMap<>();

		if (existingWiring != null) {
			// Take existing row data and clone
			for (final TradesRow tradesRow : existingWiring.rows) {
				if (tradesRow.getCargo() != null && (!cargoes.contains(tradesRow.getCargo()))) {
					// Cargo has been removed
					tradesRow.setCargo(null);
					tradesRow.setCargoAllocation(null);

				}

				if (tradesRow.getLoadSlot() != null) {
					if (allLoadSlots.contains(tradesRow.getLoadSlot())) {
						// Keep
						existingLoadSlotToTradesRow.put(tradesRow.getLoadSlot(), tradesRow);
					} else {
						// Load slot has been removed
						tradesRow.setLoadSlot(null);
						tradesRow.setLoadAllocation(null);
					}
				}

				if (tradesRow.getDischargeSlot() != null) {
					if (allDischargeSlots.contains(tradesRow.getDischargeSlot())) {
						// Keep
						existingDischargeSlotToTradesRow.put(tradesRow.getDischargeSlot(), tradesRow);
					} else {
						// Discharge slot has been removed
						tradesRow.setDischargeSlot(null);
						tradesRow.setDischargeAllocation(null);
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
			final List<LoadSlot> loadSlots = new ArrayList<>();
			final List<DischargeSlot> dischargeSlots = new ArrayList<>();
			for (final Slot<?> slot : cargo.getSortedSlots()) {
				if (slot instanceof final LoadSlot ls) {
					loadSlots.add(ls);
				} else if (slot instanceof final DischargeSlot ds) {
					dischargeSlots.add(ds);
				} else {
					// Assume some kind of discharge?
					// dischargeSlots.add((Slot) slot);
				}

			}

			// Generate the wiring - currently this is the full many-many mapping between
			// loads and discharges.
			// In future this should be better (in some way...)
			int numDischarges = 0;
			DischargeSlot d1 = null;
			for (final Slot<?> slot : cargo.getSlots()) {
				if (slot instanceof final LoadSlot loadSlot) {
					for (final Slot<?> slot2 : cargo.getSortedSlots()) {
						if (slot2 instanceof final DischargeSlot dischargeSlot) {
							if (d1 == null) {
								d1 = dischargeSlot;
							}
							final WireData wire = new WireData();
							wire.isBracket = ++numDischarges > 1;
							if (wire.isBracket) {
								wire.sourceDischargeSlot = d1;
							} else {
								wire.loadSlot = loadSlot;
							}
							wire.dischargeSlot = dischargeSlot;
							group.getWires().add(wire);
						}
					}
				}
			}
			// Set the colour for all cargo wires.
			setWiringColour(validationInfo, group);

			// Create a row for each pair of load and discharge slots in the cargo. This may
			// lead to a row with only one slot
			for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {

				LoadSlot loadSlot = null;
				if (i < loadSlots.size()) {
					loadSlot = loadSlots.get(i);
				}
				DischargeSlot dischargeSlot = null;
				if (i < dischargeSlots.size()) {
					dischargeSlot = dischargeSlots.get(i);
				}

				TradesRow loadTradesRow = null;
				if (existingLoadSlotToTradesRow.containsKey(loadSlot)) {
					loadTradesRow = existingLoadSlotToTradesRow.get(loadSlot);
				}
				TradesRow dischargeTradesRow = null;
				if (existingDischargeSlotToTradesRow.containsKey(dischargeSlot)) {
					dischargeTradesRow = existingDischargeSlotToTradesRow.get(dischargeSlot);
				}
				if (loadTradesRow == null && dischargeTradesRow == null) {
					loadTradesRow = CargoEditorModelFactoryImpl.init().createTradesRow();
					dischargeTradesRow = loadTradesRow;
				} else if (loadTradesRow == null && loadSlot != null) {
					loadTradesRow = CargoEditorModelFactoryImpl.init().createTradesRow();
				} else if (dischargeTradesRow == null && dischargeSlot != null) {
					loadTradesRow = CargoEditorModelFactoryImpl.init().createTradesRow();
				}

				if (loadTradesRow == null && dischargeTradesRow == null) {
					// We shouldn't really get here
					continue;
				}

				// In some cases where we only have one slot (e.g. as part of LDD) we can end up
				// with a null row data object, so use the other half as the reference for the
				// rest of the loop
				if (loadTradesRow == null) {
					loadTradesRow = dischargeTradesRow;
				} else if (dischargeTradesRow == null) {
					dischargeTradesRow = loadTradesRow;
				}
				// Attempt to determine primary record. TODO: Confirm this is correct for LDD
				// case
				loadTradesRow.setPrimaryRecord(i == 0);
				dischargeTradesRow.setPrimaryRecord(i == 0);

				loadTradesRow.setLoadSlot(loadSlot);
				dischargeTradesRow.setDischargeSlot(dischargeSlot);

				tradesRowMap.put(loadSlot, loadTradesRow);
				tradesRowMap.put(dischargeSlot, dischargeTradesRow);

				loadTradesRow.setGroup(group);
				group.getRows().add(loadTradesRow);

				if (!root.getRows().contains(loadTradesRow)) {
					root.getRows().add(loadTradesRow);
				}
				if (!root.getRows().contains(dischargeTradesRow)) {
					root.getRows().add(dischargeTradesRow);
				}
				loadTradesRow.setCargo(cargo);

				// Set terminal colours to valid - even if slot is missing, in such cases the
				// terminal will not be rendered
				loadTradesRow.setLoadTerminalValid(true);
				dischargeTradesRow.setDischargeTerminalValid(true);

				// patch up the WireData information with the new RowData objects
				for (final WireData wire : group.getWires()) {
					if (wire.loadSlot != null && wire.loadSlot == loadTradesRow.getLoadSlot()) {
						wire.loadTradesRow = loadTradesRow;
					}
					if (wire.dischargeSlot != null && wire.dischargeSlot == dischargeTradesRow.getDischargeSlot()) {
						wire.dischargeTradesRow = dischargeTradesRow;
					}
					if (wire.sourceDischargeSlot != null && wire.sourceDischargeSlot == dischargeTradesRow.getDischargeSlot()) {
						wire.sourceDischargeTradesRow = dischargeTradesRow;
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

				final TradesRow row;
				if (existingLoadSlotToTradesRow.containsKey(slot)) {
					row = existingLoadSlotToTradesRow.get(slot);
				} else {
					row = CargoEditorModelFactoryImpl.init().createTradesRow();
				}

				if (!root.getRows().contains(row)) {
					root.getRows().add(row);
				}

				row.setGroup(group);
				row.setLoadSlot(slot);

				tradesRowMap.put(slot, row);

				row.setLoadTerminalValid(slot.isOptional() || slot.isCancelled());
				if (row.getDischargeSlot() == null) {
					row.setDischargeTerminalValid(false);
				}
			}
		}

		// Process all discharges without a cargo
		for (final DischargeSlot slot : allDischargeSlots) {
			if (slot.getCargo() == null) {

				final GroupData group = new GroupData();
				root.getGroups().add(group);
				group.getObjects().add(slot);

				final TradesRow row;
				if (existingDischargeSlotToTradesRow.containsKey(slot)) {
					row = existingDischargeSlotToTradesRow.get(slot);
				} else {
					row = CargoEditorModelFactoryImpl.init().createTradesRow();
				}

				if (!root.getRows().contains(row)) {
					root.getRows().add(row);
				}
				row.setDischargeSlot(slot);
				row.setGroup(group);
				tradesRowMap.put(slot, row);

				row.setDischargeTerminalValid(slot.isOptional() || slot.isCancelled());

				if (row.getLoadSlot() == null) {
					row.setLoadTerminalValid(true);
				}
			}
		}

		// Construct arrays of data so that index X across all arrays points to the same
		// row
		for (final TradesRow tradesRow : root.getRows()) {
			root.getCargoes().add(tradesRow.getCargo());
			root.getLoadSlots().add(tradesRow.getLoadSlot());
			root.getDischargeSlots().add(tradesRow.getDischargeSlot());

			// Hook up allocation objects
			tradesRow.setCargoAllocation(cargoAllocationMap.get(tradesRow.getLoadSlot()));
			tradesRow.setMarketAllocation(marketAllocationMap.get((tradesRow.getLoadSlot() == null) ? tradesRow.getDischargeSlot() : tradesRow.getLoadSlot()));
			tradesRow.setOpenSlotAllocation(openAllocationMap.get((tradesRow.getLoadSlot() == null) ? tradesRow.getDischargeSlot() : tradesRow.getLoadSlot()));
			tradesRow.setLoadAllocation(slotAllocationMap.get(tradesRow.getLoadSlot()));
			tradesRow.setDischargeAllocation(slotAllocationMap.get(tradesRow.getDischargeSlot()));

		}

		for (final TradesRow tradesRow : root.getRows()) {
			final Slot<?> transferSlot = getLinkedSlot(tradesRow.getLoadSlot());
			if (transferSlot != null) {
				Object groupObject = tradesRow.getGroup();
				if (groupObject instanceof GroupData group) {
					final WireData wire = group.addShipToShipWire(tradesRow.getLoadSlot());
					wire.loadTradesRow = tradesRow;
					wire.dischargeTradesRow = tradesRowMap.get(transferSlot);
				} else {
					throw new IllegalStateException("Internal State should be group data object");
				}
			}
		}

		return root;
	}

	/**
	 * Returns any slot the specified slot is linked to in a ship-to-ship transfer
	 * 
	 * @param slot
	 */
	private static Slot<?> getLinkedSlot(final Slot<?> slot) {
		if (slot instanceof LoadSlot loadSlot) {
			return loadSlot.getTransferFrom();
		} else if (slot instanceof DischargeSlot dischargeSlot) {
			return dischargeSlot.getTransferTo();
		}

		return null;
	}

	/**
	 * Represents a "wire" in the wiring diagram
	 * 
	 */
	public static class WireData {

		Color colour;
		LoadSlot loadSlot;
		TradesRow loadTradesRow;
		DischargeSlot dischargeSlot;
		TradesRow dischargeTradesRow;
		boolean dashed;
		boolean isBracket; // For additional D rows in LDD case
		// Used instead of load for additional D rows.
		DischargeSlot sourceDischargeSlot;
		TradesRow sourceDischargeTradesRow;

	}

	/**
	 * Represents a single cargo (or a single slot). For simple L->D cargoes this
	 * will contain one slot. For complex cargoes, there may be multiple rows.
	 * 
	 * @author sg
	 * 
	 */
	public class GroupData {

		private final List<TradesRow> rows = new ArrayList<>();
		private final List<EObject> objects = new ArrayList<>();
		private final List<WireData> wires = new ArrayList<>();

		private List<EObject> getObjects() {
			return objects;
		}

		private WireData addShipToShipWire(final Slot slot) {
			// and add a wire to the group, showing the ship-to-ship transfer
			final WireData wire = new WireData();
			getWires().add(wire);
			wire.colour = Colour_Scheme.getFixedWireColour();
			wire.dashed = true;

			LoadSlot loadSlot = null;
			DischargeSlot dischargeSlot = null;

			if (slot instanceof final LoadSlot s) {
				loadSlot = s;
				dischargeSlot = s.getTransferFrom();
			} else if (slot instanceof final DischargeSlot s) {
				dischargeSlot = s;
				loadSlot = s.getTransferTo();
			}

			wire.loadSlot = loadSlot;
			wire.dischargeSlot = dischargeSlot;
			return wire;
		}

		public List<TradesRow> getRows() {
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
	public static class RootData {

		private final List<TradesRow> rows = new ArrayList<>();
		private final List<GroupData> groups = new ArrayList<>();

		private final ArrayList<Cargo> cargoes = new ArrayList<>();
		private final ArrayList<LoadSlot> loadSlots = new ArrayList<>();
		private final ArrayList<DischargeSlot> dischargeSlots = new ArrayList<>();

		public List<GroupData> getGroups() {
			return groups;
		}

		public List<TradesRow> getRows() {
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
	 * The {@link RowDataEMFPath} class is used to bridge our custom {@link RowData}
	 * objects with our reflective EMF based UI. This permits the step from
	 * {@link RowData} -> EObject , then on to the normal EMF Path navigation. This
	 * can be passed into the normal {@link EObjectTableViewer} and
	 * {@link BasicAttributeManipulator} based API's.
	 * 
	 */
	public static class TradesRowEMFPath extends EMFPath {

		private final Type type;

		/**
		 * Indicate that we want only the "primary record" - i.e. the row with the cargo
		 * defining load.
		 */
		private final boolean primaryRecordOnly;

		/**
		 */
		public TradesRowEMFPath(final boolean primaryRecordOnly, final Type type, final Iterable<ETypedElement> path) {
			super(true, path);
			this.type = type;
			this.primaryRecordOnly = primaryRecordOnly;
		}

		/**
		 */
		public TradesRowEMFPath(final boolean primaryRecordOnly, final Type type, final ETypedElement... path) {
			super(true, path);
			this.type = type;
			this.primaryRecordOnly = primaryRecordOnly;
		}

		public TradesRowEMFPath(final boolean failSilently, final boolean primaryRecordOnly, final Type type, final ETypedElement... path) {
			super(failSilently, path);
			this.type = type;
			this.primaryRecordOnly = primaryRecordOnly;
		}

		@Override
		public Object get(final EObject root, final int depth) {

			if (root instanceof TradesRow tradesRow) {
				final boolean showRecord = primaryRecordOnly ? tradesRow.isPrimaryRecord() : true;
				switch (type) {
				case CARGO:
					return showRecord ? super.get(tradesRow.getCargo(), depth) : null;
				case DISCHARGE:
					return super.get(tradesRow.getDischargeSlot(), depth);
				case LOAD:
					return super.get(tradesRow.getLoadSlot(), depth);
				case CARGO_ALLOCATION:
					return showRecord ? super.get(tradesRow.getCargoAllocation(), depth) : null;
				case MARKET_ALLOCATION:
					return super.get(tradesRow.getMarketAllocation(), depth);
				case CARGO_OR_MARKET_ALLOCATION:
					if (tradesRow.getCargoAllocation() != null) {
						return (showRecord ? super.get(tradesRow.getMarketAllocation(), depth) : null);
					} else {
						return super.get(tradesRow.getMarketAllocation(), depth);
					}
				case CARGO_OR_MARKET_OR_OPEN_ALLOCATION:
					if (tradesRow.getCargoAllocation() == null) {
						if (tradesRow.getMarketAllocation() != null) {
							return super.get(tradesRow.getMarketAllocation());
						} else {
							return super.get(tradesRow.getOpenSlotAllocation());
						}
					} else {
						return showRecord ? super.get(tradesRow.getCargoAllocation(), depth) : null;
					}
				case DISCHARGE_ALLOCATION:
					return super.get(tradesRow.getDischargeAllocation(), depth);
				case LOAD_ALLOCATION:
					return super.get(tradesRow.getLoadAllocation(), depth);
				case LOAD_OR_DISCHARGE: {
					final Object result = get(tradesRow.getLoadSlot(), depth);
					return result != null ? result : get(tradesRow.getDischargeSlot(), depth);

				}
				case DISCHARGE_OR_LOAD: {
					final Object result = get(tradesRow.getDischargeSlot(), depth);
					return result != null ? result : get(tradesRow.getLoadSlot(), depth);

				}
				case SLOT_OR_CARGO: {

					Object result = null;
					if (tradesRow.getLoadSlot() != null && tradesRow.getLoadSlot().isDESPurchase()) {
						result = get(tradesRow.getLoadSlot(), depth);
					}
					if (result == null && tradesRow.getDischargeSlot() != null && tradesRow.getDischargeSlot().isFOBSale()) {
						result = get(tradesRow.getDischargeSlot(), depth);
					}
					if (result == null) {
						result = get(tradesRow.getCargo(), depth);
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
	 * Method to update wiring colours without rebuilding the {@link RootData}
	 * object (e.g. after the validation status has changed).
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
	 * Method to query the validation information for issues with a cargo and update
	 * the wiring colour as appropriate
	 * 
	 * @param validationInformation
	 * @param g
	 */
	private void setWiringColour(final Map<Object, IStatus> validationInformation, final GroupData g) {
		boolean validWire = true;
		Cargo c = null;
		for (final EObject obj : g.getObjects()) {

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
				wire.colour = validWire ? (c.isAllowRewiring() ? Colour_Scheme.getRewirableColour() : Colour_Scheme.getFixedWireColour()) : Colour_Scheme.getInvalidWireColour();
			}
		}
	}

}