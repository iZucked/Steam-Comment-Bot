/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 s * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for LoadSlot instances
 * 
 * @generated
 */
public class LoadSlotComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 * 
	 * @generated
	 */
	public LoadSlotComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 * 
	 * @generated
	 */
	public LoadSlotComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(CargoPackage.Literals.SLOT));
	}

	/**
	 * add editors to a composite, using LoadSlot as the supertype
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.LOAD_SLOT);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_cargoCVEditor(detailComposite, topClass);
		add_arriveColdEditor(detailComposite, topClass);
		add_DESPurchaseEditor(detailComposite, topClass);
		add_transferFromEditor(detailComposite, topClass);
		add_salesDeliveryTypeEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the cargoCV feature on LoadSlot
	 * 
	 * @generated
	 */
	protected void add_cargoCVEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.LOAD_SLOT__CARGO_CV));
	}

	/**
	 * Create the editor for the arriveCold feature on LoadSlot
	 * 
	 * @generated
	 */
	protected void add_arriveColdEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.LOAD_SLOT__ARRIVE_COLD));
	}

	/**
	 * Create the editor for the cargo feature on LoadSlot
	 * 
	 * @generated NOT
	 */
	protected void add_cargoEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// This is an opposite reference - not for direct UI consumption

		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.LOAD_SLOT__CARGO));
	}

	/**
	 * Create the editor for the DESPurchase feature on LoadSlot
	 * 
	 * @generated NOT
	 */
	protected void add_DESPurchaseEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE));
	}

	/**
	 * Create the editor for the transferFrom feature on LoadSlot
	 *
	 * @generated NOT
	 */
	protected void add_transferFromEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.LOAD_SLOT__TRANSFER_FROM));
	}

	/**
	 * Create the editor for the salesDeliveryType feature on LoadSlot
	 *
	 * @generated
	 */
	protected void add_salesDeliveryTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.LOAD_SLOT__SALES_DELIVERY_TYPE));
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {

		// This is required for the getCargo() feature on the slot
		final List<EObject> external = new LinkedList<EObject>();
		if (value instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) value;
			final Cargo cargo = loadSlot.getCargo();
			if (cargo != null) {
				external.add(cargo);
				external.addAll(cargo.getSlots());
				external.remove(value);
			}
			if (loadSlot.getTransferFrom() != null) {
				external.add(loadSlot.getTransferFrom());
			}
			external.addAll(loadSlot.getExtensions());
		}
		external.addAll(super.getExternalEditingRange(root, value));

		return external;
	}
}