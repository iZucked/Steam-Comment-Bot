/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for SpotDischargeSlot instances
 *
 * @generated
 */
public class SpotDischargeSlotComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SpotDischargeSlotComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SpotDischargeSlotComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(CargoPackage.Literals.DISCHARGE_SLOT));
		superClassesHelpers.addAll(registry.getComponentHelpers(CargoPackage.Literals.SPOT_SLOT));
	}
	
	/**
	 * add editors to a composite, using SpotDischargeSlot as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.SPOT_DISCHARGE_SLOT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
	}
	
	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {

		// This is required for the getCargo() feature on the slot
		final List<EObject> external = new LinkedList<EObject>();
		if (value instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) value;
			final Cargo cargo = dischargeSlot.getCargo();
			if (cargo != null) {
				external.add(cargo);
				external.addAll(cargo.getSlots());
				external.remove(value);
			}
		}
		external.addAll(super.getExternalEditingRange(root, value));

		return external;
	}
}