/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
 * A component helper for DischargeSlot instances
 * 
 * @generated
 */
public class DischargeSlotComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 * 
	 * @generated
	 */
	public DischargeSlotComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 * 
	 * @generated
	 */
	public DischargeSlotComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(CargoPackage.Literals.SLOT));
	}

	/**
	 * add editors to a composite, using DischargeSlot as the supertype
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.DISCHARGE_SLOT);
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers)
			helper.addEditorsToComposite(detailComposite, topClass);
		add_cargoEditor(detailComposite, topClass);
		add_FOBSaleEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the cargo feature on DischargeSlot
	 * 
	 * @generated NOT
	 */
	protected void add_cargoEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// This is an opposite reference - not for direct UI consumption

		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.DISCHARGE_SLOT__CARGO));
	}

	/**
	 * Create the editor for the FOBSale feature on DischargeSlot
	 * 
	 * @generated NOT
	 */
	protected void add_FOBSaleEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE));
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
				if (cargo.getLoadSlot() != null) {
					external.add(cargo.getLoadSlot());
				}
			}
		}
		external.addAll(super.getExternalEditingRange(root, value));

		return external;
	}
}