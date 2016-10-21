/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
 * A component helper for CostModel instances
 *
 * @generated
 */
public class CostModelComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CostModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CostModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
	}
	
	/**
	 * add editors to a composite, using CostModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.COST_MODEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_routeCostsEditor(detailComposite, topClass);
		add_portCostsEditor(detailComposite, topClass);
		add_cooldownCostsEditor(detailComposite, topClass);
		add_baseFuelCostsEditor(detailComposite, topClass);
		add_panamaCanalTariffEditor(detailComposite, topClass);
		add_suezCanalTariffEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the routeCosts feature on CostModel
	 *
	 * @generated
	 */
	protected void add_routeCostsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.COST_MODEL__ROUTE_COSTS));
	}
	/**
	 * Create the editor for the portCosts feature on CostModel
	 *
	 * @generated
	 */
	protected void add_portCostsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.COST_MODEL__PORT_COSTS));
	}
	/**
	 * Create the editor for the cooldownCosts feature on CostModel
	 *
	 * @generated
	 */
	protected void add_cooldownCostsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.COST_MODEL__COOLDOWN_COSTS));
	}
	/**
	 * Create the editor for the baseFuelCosts feature on CostModel
	 *
	 * @generated
	 */
	protected void add_baseFuelCostsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.COST_MODEL__BASE_FUEL_COSTS));
	}

	/**
	 * Create the editor for the panamaCanalTariff feature on CostModel
	 *
	 * @generated
	 */
	protected void add_panamaCanalTariffEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.COST_MODEL__PANAMA_CANAL_TARIFF));
	}

	/**
	 * Create the editor for the suezCanalTariff feature on CostModel
	 *
	 * @generated
	 */
	protected void add_suezCanalTariffEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.COST_MODEL__SUEZ_CANAL_TARIFF));
	}
}