/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import com.mmxlabs.models.lng.adp.ADPPackage;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;

import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;

/**
 * A component helper for InventoryADPEntityRow instances
 *
 * @generated
 */
public class InventoryADPEntityRowComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public InventoryADPEntityRowComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public InventoryADPEntityRowComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using InventoryADPEntityRow as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_entityEditor(detailComposite, topClass);
		add_initialAllocationEditor(detailComposite, topClass);
		add_relativeEntitlementEditor(detailComposite, topClass);
		add_marketAllocationRowsEditor(detailComposite, topClass);
		add_portsEditor(detailComposite, topClass);
		add_contractAllocationRowsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the entity feature on InventoryADPEntityRow
	 *
	 * @generated
	 */
	protected void add_entityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__ENTITY));
	}
	/**
	 * Create the editor for the initialAllocation feature on InventoryADPEntityRow
	 *
	 * @generated
	 */
	protected void add_initialAllocationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__INITIAL_ALLOCATION));
	}
	/**
	 * Create the editor for the relativeEntitlement feature on InventoryADPEntityRow
	 *
	 * @generated
	 */
	protected void add_relativeEntitlementEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__RELATIVE_ENTITLEMENT));
	}

	/**
	 * Create the editor for the marketAllocationRows feature on InventoryADPEntityRow
	 *
	 * @generated
	 */
	protected void add_marketAllocationRowsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__MARKET_ALLOCATION_ROWS));
	}

	/**
	 * Create the editor for the ports feature on InventoryADPEntityRow
	 *
	 * @generated
	 */
	protected void add_portsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__PORTS));
	}

	/**
	 * Create the editor for the contractAllocationRows feature on InventoryADPEntityRow
	 *
	 * @generated
	 */
	protected void add_contractAllocationRowsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__CONTRACT_ALLOCATION_ROWS));
	}
}