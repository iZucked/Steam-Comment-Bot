/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for SellOpportunity instances
 *
 * @generated
 */
public class SellOpportunityComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SellOpportunityComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SellOpportunityComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using SellOpportunity as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.SELL_OPPORTUNITY);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_portEditor(detailComposite, topClass);
		add_contractEditor(detailComposite, topClass);
		add_dateEditor(detailComposite, topClass);
		add_priceExpressionEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the port feature on SellOpportunity
	 *
	 * @generated
	 */
	protected void add_portEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PORT));
	}
	/**
	 * Create the editor for the contract feature on SellOpportunity
	 *
	 * @generated
	 */
	protected void add_contractEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SELL_OPPORTUNITY__CONTRACT));
	}
	/**
	 * Create the editor for the date feature on SellOpportunity
	 *
	 * @generated
	 */
	protected void add_dateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE));
	}
	/**
	 * Create the editor for the priceExpression feature on SellOpportunity
	 *
	 * @generated
	 */
	protected void add_priceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PRICE_EXPRESSION));
	}
}