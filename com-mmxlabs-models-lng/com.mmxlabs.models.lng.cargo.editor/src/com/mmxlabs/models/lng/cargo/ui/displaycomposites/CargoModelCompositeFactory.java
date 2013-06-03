/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

/**
 * Factory for {@link CargoTopLevelComposite} application.
 * 
 * @author hinton
 * 
 */
public class CargoModelCompositeFactory extends DefaultDisplayCompositeFactory {
	public CargoModelCompositeFactory() {

	}

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IScenarioEditingLocation location) {
		return new CargoTopLevelComposite(composite, SWT.NONE, location);
	}

	@Override
	public IDisplayComposite createSublevelComposite(final Composite composite, final EClass eClass, final IScenarioEditingLocation location) {
		// This is not expected to be called. The CargoTopLevelComposite will create it's own instances directly.
		throw new UnsupportedOperationException("Unexpected method invocations");
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {
		final List<EObject> external = super.getExternalEditingRange(root, value);

		if (value instanceof Cargo) {
			final Cargo cargo = (Cargo) value;

			Set<Slot> slots = new HashSet<Slot>();
			for (Slot slot : cargo.getSlots()) {
				slots.add(slot);
				if (slot instanceof LoadSlot) {
					LoadSlot loadSlot = (LoadSlot) slot;
					if (loadSlot.getTransferFrom() != null) {
						slots.add(loadSlot.getTransferFrom());
					}
				} else if (slot instanceof DischargeSlot) {
					DischargeSlot dischargeSlot = (DischargeSlot) slot;
					if (dischargeSlot.getTransferTo() != null) {
						slots.add(dischargeSlot.getTransferTo());
					}
				}
			}
			external.addAll(slots);
		}

		return external;
	}
}
