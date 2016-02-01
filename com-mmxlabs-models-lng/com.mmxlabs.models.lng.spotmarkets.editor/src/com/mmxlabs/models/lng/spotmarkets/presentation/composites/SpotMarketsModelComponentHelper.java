/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for SpotMarketsModel instances
 *
 * @generated
 */
public class SpotMarketsModelComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SpotMarketsModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SpotMarketsModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
	}
	
	/**
	 * add editors to a composite, using SpotMarketsModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_desPurchaseSpotMarketEditor(detailComposite, topClass);
		add_desSalesSpotMarketEditor(detailComposite, topClass);
		add_fobPurchasesSpotMarketEditor(detailComposite, topClass);
		add_fobSalesSpotMarketEditor(detailComposite, topClass);
		add_charterOutStartDateEditor(detailComposite, topClass);
		add_charterInMarketsEditor(detailComposite, topClass);
		add_charterOutMarketsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the desPurchaseSpotMarket feature on SpotMarketsModel
	 *
	 * @generated
	 */
	protected void add_desPurchaseSpotMarketEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET));
	}
	/**
	 * Create the editor for the desSalesSpotMarket feature on SpotMarketsModel
	 *
	 * @generated
	 */
	protected void add_desSalesSpotMarketEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET));
	}
	/**
	 * Create the editor for the fobPurchasesSpotMarket feature on SpotMarketsModel
	 *
	 * @generated
	 */
	protected void add_fobPurchasesSpotMarketEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET));
	}
	/**
	 * Create the editor for the fobSalesSpotMarket feature on SpotMarketsModel
	 *
	 * @generated
	 */
	protected void add_fobSalesSpotMarketEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET));
	}
	/**
	 * Create the editor for the charterOutStartDate feature on SpotMarketsModel
	 *
	 * @generated
	 */
	protected void add_charterOutStartDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_START_DATE));
	}

	/**
	 * Create the editor for the charterInMarkets feature on SpotMarketsModel
	 *
	 * @generated
	 */
	protected void add_charterInMarketsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS));
	}

	/**
	 * Create the editor for the charterOutMarkets feature on SpotMarketsModel
	 *
	 * @generated
	 */
	protected void add_charterOutMarketsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS));
	}
}