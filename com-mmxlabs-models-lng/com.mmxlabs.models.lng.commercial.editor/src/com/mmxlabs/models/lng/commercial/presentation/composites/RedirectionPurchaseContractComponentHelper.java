/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import com.mmxlabs.models.lng.commercial.CommercialPackage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * A component helper for RedirectionPurchaseContract instances
 *
 * @generated
 */
public class RedirectionPurchaseContractComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public RedirectionPurchaseContractComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public RedirectionPurchaseContractComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(CommercialPackage.Literals.PURCHASE_CONTRACT));
	}
	
	/**
	 * add editors to a composite, using RedirectionPurchaseContract as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_baseSalesMarketPortEditor(detailComposite, topClass);
		add_baseSalesPriceExpressionEditor(detailComposite, topClass);
		add_basePurchasePriceExpressionEditor(detailComposite, topClass);
		add_notionalSpeedEditor(detailComposite, topClass);
		add_desPurchasePortEditor(detailComposite, topClass);
		add_sourcePurchasePortEditor(detailComposite, topClass);
		add_profitShareEditor(detailComposite, topClass);
		add_vesselClassEditor(detailComposite, topClass);
		add_hireCostEditor(detailComposite, topClass);
		add_daysFromSourceEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the baseSalesMarketPort feature on RedirectionPurchaseContract
	 *
	 * @generated
	 */
	protected void add_baseSalesMarketPortEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_MARKET_PORT));
	}
	/**
	 * Create the editor for the baseSalesPriceExpression feature on RedirectionPurchaseContract
	 *
	 * @generated
	 */
	protected void add_baseSalesPriceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_PRICE_EXPRESSION));
	}
	/**
	 * Create the editor for the basePurchasePriceExpression feature on RedirectionPurchaseContract
	 *
	 * @generated
	 */
	protected void add_basePurchasePriceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__BASE_PURCHASE_PRICE_EXPRESSION));
	}
	/**
	 * Create the editor for the notionalSpeed feature on RedirectionPurchaseContract
	 *
	 * @generated
	 */
	protected void add_notionalSpeedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__NOTIONAL_SPEED));
	}

	/**
	 * Create the editor for the desPurchasePort feature on RedirectionPurchaseContract
	 *
	 * @generated NOT
	 */
	protected void add_desPurchasePortEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__DES_PURCHASE_PORT));
	}

	/**
	 * Create the editor for the sourcePurchasePort feature on RedirectionPurchaseContract
	 *
	 * @generated
	 */
	protected void add_sourcePurchasePortEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__SOURCE_PURCHASE_PORT));
	}

	/**
	 * Create the editor for the profitShare feature on RedirectionPurchaseContract
	 *
	 * @generated
	 */
	protected void add_profitShareEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__PROFIT_SHARE));
	}

	/**
	 * Create the editor for the vesselClass feature on RedirectionPurchaseContract
	 *
	 * @generated
	 */
	protected void add_vesselClassEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__VESSEL_CLASS));
	}

	/**
	 * Create the editor for the hireCost feature on RedirectionPurchaseContract
	 *
	 * @generated
	 */
	protected void add_hireCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__HIRE_COST));
	}

	/**
	 * Create the editor for the daysFromSource feature on RedirectionPurchaseContract
	 *
	 * @generated
	 */
	protected void add_daysFromSourceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__DAYS_FROM_SOURCE));
	}
}