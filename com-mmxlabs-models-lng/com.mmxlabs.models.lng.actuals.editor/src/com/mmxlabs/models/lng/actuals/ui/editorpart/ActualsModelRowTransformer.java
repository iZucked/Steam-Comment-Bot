/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.ui.editorpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jface.viewers.ViewerComparator;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class ActualsModelRowTransformer {

	public RootData transform(final ActualsModel actualsModel, final Map<Object, IStatus> validationInfo) {
		return transform(actualsModel.getCargoActuals(), validationInfo);

	}

	public RootData transform(final List<CargoActuals> cargoActuals, final Map<Object, IStatus> validationInfo) {

		final RootData root = new RootData();

		final Map<SlotActuals, RowData> rowDataMap = new HashMap<SlotActuals, ActualsModelRowTransformer.RowData>();

		// Loop through all cargoes first, generating full cargo row items
		for (final CargoActuals cargo : cargoActuals) {

			// Build up list of slots assigned to cargo, sorting into loads and discharges
			final List<LoadActuals> loadSlots = new ArrayList<>();
			final List<DischargeActuals> dischargeSlots = new ArrayList<>();

			for (final SlotActuals slot : cargo.getActuals()) {
				if (slot instanceof LoadActuals) {
					loadSlots.add((LoadActuals) slot);
				} else if (slot instanceof DischargeActuals) {
					dischargeSlots.add((DischargeActuals) slot);
				} else {
					// Assume some kind of discharge?
					// dischargeSlots.add((Slot) slot);
				}

			}

			// Create a row for each pair of load and discharge slots in the cargo. This may lead to a row with only one slot
			for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {

				LoadActuals loadSlot = null;
				if (i < loadSlots.size()) {
					loadSlot = loadSlots.get(i);
				}
				DischargeActuals dischargeSlot = null;
				if (i < dischargeSlots.size()) {
					dischargeSlot = dischargeSlots.get(i);
				}

				RowData rowData = new RowData();
				// Attempt to determine primary record. TODO: Confirm this is correct for LDD case
				rowData.primaryRecord = i == 0;

				rowData.loadSlot = loadSlot;
				rowData.dischargeSlot = dischargeSlot;

				rowDataMap.put(loadSlot, rowData);
				rowDataMap.put(dischargeSlot, rowData);

				if (!root.getRows().contains(rowData)) {
					root.getRows().add(rowData);
				}
				if (!root.getRows().contains(rowData)) {
					root.getRows().add(rowData);
				}
				rowData.cargo = cargo;
			}
		}

		// // Process all loads without a cargo
		// for (final LoadActuals slot : allLoadSlots) {
		// if (slot.getCargo() == null) {
		//
		// final RowData row = new RowData();
		//
		// if (!root.getRows().contains(row)) {
		// root.getRows().add(row);
		// }
		//
		// row.loadSlot = slot;
		//
		// rowDataMap.put(slot, row);
		// }
		// }

		// // Process all discharges without a cargo
		// for (final DischargeActuals slot : allDischargeSlots) {
		// if (slot.getCargo() == null) {
		//
		// final RowData row = new RowData();
		//
		// if (!root.getRows().contains(row)) {
		// root.getRows().add(row);
		// }
		// row.dischargeSlot = slot;
		// rowDataMap.put(slot, row);
		//
		// }
		// }

		// Construct arrays of data so that index X across all arrays points to the same row
		for (final RowData rd : root.getRows()) {
			root.getCargoActuals().add(rd.getCargo());
			root.getLoadActuals().add(rd.getLoadSlot());
			root.getDischargeActuals().add(rd.getDischargeSlot());

		}

		return root;
	}

	/**
	 * The {@link RowData} represents a single row in the trades viewer. It extends EObject for use with {@link EMFPath}, and specifically {@link RowDataEMFPath}
	 * 
	 */
	public static class RowData extends EObjectImpl {

		CargoActuals cargo;
		LoadActuals loadSlot;
		DischargeActuals dischargeSlot;

		// This is the RowData with the cargo defining load slot
		boolean primaryRecord;

		/**
		 * This is used in the {@link EObjectTableViewer} implementation of {@link ViewerComparator} for the fixed sort order.
		 */
		@Override
		public boolean equals(final Object obj) {

			if (obj instanceof RowData) {
				final RowData other = (RowData) obj;
				return Equality.isEqual(cargo, other.cargo) && Equality.isEqual(loadSlot, other.loadSlot) && Equality.isEqual(dischargeSlot, other.dischargeSlot);
			}

			return false;
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}
		
		public void setCargo(final CargoActuals cargo) {
			this.cargo = cargo;
		}

		public void setLoadSlot(final LoadActuals loadSlot) {
			this.loadSlot = loadSlot;
		}

		public void setDischargeSlot(final DischargeActuals dischargeSlot) {
			this.dischargeSlot = dischargeSlot;
		}

		public CargoActuals getCargo() {
			return cargo;
		}

		public LoadActuals getLoadSlot() {
			return loadSlot;
		}

		public DischargeActuals getDischargeSlot() {
			return dischargeSlot;
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
	 * The top node in the data structure.
	 * 
	 */
	public static class RootData extends EObjectImpl {

		private final List<RowData> rows = new ArrayList<RowData>();

		private final ArrayList<CargoActuals> cargoes = new ArrayList<>();
		private final ArrayList<LoadActuals> loadSlots = new ArrayList<>();
		private final ArrayList<DischargeActuals> dischargeSlots = new ArrayList<>();

		public List<RowData> getRows() {
			return rows;
		}

		public List<CargoActuals> getCargoActuals() {
			return cargoes;
		}

		public List<LoadActuals> getLoadActuals() {
			return loadSlots;
		}

		public List<DischargeActuals> getDischargeActuals() {
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

		public RowDataEMFPath(final boolean primaryRecordOnly, final Type type, final Iterable<ETypedElement> path) {
			super(true, path);
			this.type = type;
			this.primaryRecordOnly = primaryRecordOnly;
		}

		public RowDataEMFPath(final boolean primaryRecordOnly, final Type type, final ETypedElement... path) {
			super(true, path);
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
		CARGO, LOAD, DISCHARGE,
	}
}