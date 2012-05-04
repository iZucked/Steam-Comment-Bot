/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.IComponentHelper;
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
	public IDisplayComposite createToplevelComposite(Composite composite, EClass eClass) {
		return new CargoTopLevelComposite(composite, SWT.NONE);
	}

	@Override
	public IDisplayComposite createSublevelComposite(Composite composite, EClass eClass) {
		// This is not expected to be called. The CargoTopLevelComposite will create it's own instances directly.
		throw new UnsupportedOperationException("Unexpected method invocations");
	}

	@Override
	public List<EObject> getExternalEditingRange(MMXRootObject root, EObject value) {
		final List<EObject> external = super.getExternalEditingRange(root, value);

		if (value instanceof Cargo) {
			final Cargo cargo = (Cargo) value;

			for (final IComponentHelper helper : Activator.getDefault().getComponentHelperRegistry().getComponentHelpers(cargo.getLoadSlot().eClass())) {
				external.addAll(helper.getExternalEditingRange(root, cargo.getLoadSlot()));
			}
			for (final IComponentHelper helper : Activator.getDefault().getComponentHelperRegistry().getComponentHelpers(cargo.getDischargeSlot().eClass())) {
				external.addAll(helper.getExternalEditingRange(root, cargo.getDischargeSlot()));
			}
			external.add(cargo.getLoadSlot());
			external.add(cargo.getDischargeSlot());
		}

		return external;
	}
}
