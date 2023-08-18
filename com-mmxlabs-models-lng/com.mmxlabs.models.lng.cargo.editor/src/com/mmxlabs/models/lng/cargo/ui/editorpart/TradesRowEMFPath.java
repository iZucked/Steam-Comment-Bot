/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.layout.RowData;

import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.util.emfpath.EMFPath;

/**
 * The {@link RowDataEMFPath} class is used to bridge our custom {@link RowData} objects with our reflective EMF based UI. This permits the step from {@link RowData} -> EObject , then on to the normal
 * EMF Path navigation. This can be passed into the normal {@link EObjectTableViewer} and {@link BasicAttributeManipulator} based API's.
 * 
 */
public class TradesRowEMFPath extends EMFPath {

	private final Type type;

	/**
	 * Indicate that we want only the "primary record" - i.e. the row with the cargo defining load.
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
		result = prime * result + Objects.hash(primaryRecordOnly, type);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (obj instanceof TradesRowEMFPath other) {
			return primaryRecordOnly == other.primaryRecordOnly && type == other.type;
		}
		return false;
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
}