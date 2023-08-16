package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.WireData;

/**
 * Represents a single cargo (or a single slot). For simple L->D cargoes this
 * will contain one slot. For complex cargoes, there may be multiple rows.
 * 
 * @author sg, ew
 * 
 */
public class TradesRowGroup {

	/**
	 * 
	 */
	private final TradesWiringColourScheme colourScheme;

	/**
	 * @param cargoModelRowTransformer
	 */
	TradesRowGroup(TradesWiringColourScheme colourScheme) {
		this.colourScheme = colourScheme;
	}

	private final List<TradesRow> rows = new ArrayList<>();
	private final List<EObject> objects = new ArrayList<>();
	private final List<WireData> wires = new ArrayList<>();

	List<EObject> getObjects() {
		return objects;
	}

	WireData addShipToShipWire(final Slot slot) {
		// and add a wire to the group, showing the ship-to-ship transfer
		final WireData wire = new CargoModelRowTransformer.WireData();
		getWires().add(wire);
		wire.colour = colourScheme.getFixedWireColour();
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