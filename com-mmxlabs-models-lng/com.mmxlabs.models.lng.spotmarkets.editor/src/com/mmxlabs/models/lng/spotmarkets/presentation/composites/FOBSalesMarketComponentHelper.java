/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for FOBSalesMarket instances
 *
 * @generated
 */
public class FOBSalesMarketComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public FOBSalesMarketComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public FOBSalesMarketComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(SpotMarketsPackage.Literals.SPOT_MARKET));
	}
	
	/**
	 * add editors to a composite, using FOBSalesMarket as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SpotMarketsPackage.Literals.FOB_SALES_MARKET);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_originPortsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the loadPort feature on FOBSalesMarket
	 *
	 * @generated NOT
	 */
	protected void add_loadPortEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// Deprecated for origin ports
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.FOB_SALES_MARKET__LOAD_PORT));
	}

	/**
	 * Create the editor for the originPorts feature on FOBSalesMarket
	 *
	 * @generated
	 */
	protected void add_originPortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.FOB_SALES_MARKET__ORIGIN_PORTS));
	}
}