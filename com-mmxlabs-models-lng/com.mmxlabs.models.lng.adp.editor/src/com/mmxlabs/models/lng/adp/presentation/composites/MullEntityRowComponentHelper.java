/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
 * A component helper for MullEntityRow instances
 *
 * @generated
 */
public class MullEntityRowComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public MullEntityRowComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public MullEntityRowComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using MullEntityRow as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ADPPackage.Literals.MULL_ENTITY_ROW);	
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
		add_desSalesMarketAllocationRowsEditor(detailComposite, topClass);
		add_salesContractAllocationRowsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the entity feature on MullEntityRow
	 *
	 * @generated
	 */
	protected void add_entityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.MULL_ENTITY_ROW__ENTITY));
	}
	/**
	 * Create the editor for the initialAllocation feature on MullEntityRow
	 *
	 * @generated
	 */
	protected void add_initialAllocationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.MULL_ENTITY_ROW__INITIAL_ALLOCATION));
	}
	/**
	 * Create the editor for the relativeEntitlement feature on MullEntityRow
	 *
	 * @generated
	 */
	protected void add_relativeEntitlementEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT));
	}
	/**
	 * Create the editor for the desSalesMarketAllocationRows feature on MullEntityRow
	 *
	 * @generated
	 */
	protected void add_desSalesMarketAllocationRowsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS));
	}
	/**
	 * Create the editor for the salesContractAllocationRows feature on MullEntityRow
	 *
	 * @generated
	 */
	protected void add_salesContractAllocationRowsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS));
	}
}