/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for PricingModel instances
 *
 * @generated
 */
public class PricingModelComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public PricingModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PricingModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
	}
	
	/**
	 * add editors to a composite, using PricingModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.PRICING_MODEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_commodityIndicesEditor(detailComposite, topClass);
		add_charterIndicesEditor(detailComposite, topClass);
		add_fleetCostEditor(detailComposite, topClass);
		add_routeCostsEditor(detailComposite, topClass);
		add_portCostsEditor(detailComposite, topClass);
		add_cooldownPricesEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the commodityIndices feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_commodityIndicesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES));
	}
	/**
	 * Create the editor for the charterIndices feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_charterIndicesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES));
	}
	/**
	 * Create the editor for the fleetCost feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_fleetCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__FLEET_COST));
	}
	/**
	 * Create the editor for the routeCosts feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_routeCostsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__ROUTE_COSTS));
	}
	/**
	 * Create the editor for the portCosts feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_portCostsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__PORT_COSTS));
	}

	/**
	 * Create the editor for the cooldownPrices feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_cooldownPricesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__COOLDOWN_PRICES));
	}
}