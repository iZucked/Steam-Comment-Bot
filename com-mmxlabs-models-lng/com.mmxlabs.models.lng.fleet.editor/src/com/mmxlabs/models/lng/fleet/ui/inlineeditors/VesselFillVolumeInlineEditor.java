/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.editors.impl.NumberInlineEditor;

public class VesselFillVolumeInlineEditor extends NumberInlineEditor {

	public VesselFillVolumeInlineEditor(final EStructuralFeature feature) {
		super(feature);
		
		//Make read only.
		this.setEditorEnabled(false);
		
		//NB: we remove label and make it fit on the same line as Fill Capacity in the FillCapacityDetailComposite. 
	}
	
	@Override
	public void setEditorEnabled(final boolean enabled) {
		super.setEditorEnabled(false);
	}
	
	@Override
	protected Object getValue() {
		if (this.input instanceof Vessel v) {
			return v.getVesselOrDelegateFillCapacity() * v.getVesselOrDelegateCapacity();
		}
		else {
			return super.getValue();
		}
	}
	
	@Override
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		if (changedFeature == FleetPackage.eINSTANCE.getVessel_FillCapacity() || changedFeature == FleetPackage.eINSTANCE.getVessel_Capacity()) {
			return true;
		}
		return super.updateOnChangeToFeature(changedFeature);
	}
}
