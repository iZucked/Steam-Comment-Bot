/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import com.mmxlabs.models.lng.pricing.PricingPackage;

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
 * A component helper for SuezCanalTariff instances
 *
 * @generated
 */
public class SuezCanalTariffComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SuezCanalTariffComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SuezCanalTariffComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using SuezCanalTariff as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.SUEZ_CANAL_TARIFF);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_bandsEditor(detailComposite, topClass);
		add_tugBandsEditor(detailComposite, topClass);
		add_tugCostEditor(detailComposite, topClass);
		add_mooringCostEditor(detailComposite, topClass);
		add_pilotageCostEditor(detailComposite, topClass);
		add_disbursementsEditor(detailComposite, topClass);
		add_discountFactorEditor(detailComposite, topClass);
		add_sdrToUSDEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the bands feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_bandsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__BANDS));
	}
	/**
	 * Create the editor for the tugBands feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_tugBandsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__TUG_BANDS));
	}
	/**
	 * Create the editor for the tugCost feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_tugCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__TUG_COST));
	}
	/**
	 * Create the editor for the mooringCost feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_mooringCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__MOORING_COST));
	}
	/**
	 * Create the editor for the pilotageCost feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_pilotageCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__PILOTAGE_COST));
	}
	/**
	 * Create the editor for the disbursements feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_disbursementsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__DISBURSEMENTS));
	}

	/**
	 * Create the editor for the discountFactor feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_discountFactorEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR));
	}

	/**
	 * Create the editor for the sdrToUSD feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_sdrToUSDEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__SDR_TO_USD));
	}
}