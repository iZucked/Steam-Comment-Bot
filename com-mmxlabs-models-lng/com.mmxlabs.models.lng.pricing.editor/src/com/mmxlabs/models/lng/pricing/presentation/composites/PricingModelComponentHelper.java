/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
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
		add_currencyCurvesEditor(detailComposite, topClass);
		add_commodityCurvesEditor(detailComposite, topClass);
		add_charterCurvesEditor(detailComposite, topClass);
		add_bunkerFuelCurvesEditor(detailComposite, topClass);
		add_conversionFactorsEditor(detailComposite, topClass);
		add_settledPricesEditor(detailComposite, topClass);
		add_marketIndicesEditor(detailComposite, topClass);
		add_holidayCalendarsEditor(detailComposite, topClass);
		add_settleStrategiesEditor(detailComposite, topClass);
		add_marketCurvesVersionRecordEditor(detailComposite, topClass);
		add_settledPricesVersionRecordEditor(detailComposite, topClass);
		add_pricingCalendarsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the currencyCurves feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_currencyCurvesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__CURRENCY_CURVES));
	}

	/**
	 * Create the editor for the commodityCurves feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_commodityCurvesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__COMMODITY_CURVES));
	}

	/**
	 * Create the editor for the charterCurves feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_charterCurvesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__CHARTER_CURVES));
	}

	/**
	 * Create the editor for the bunkerFuelCurves feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_bunkerFuelCurvesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__BUNKER_FUEL_CURVES));
	}

	/**
	 * Create the editor for the conversionFactors feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_conversionFactorsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__CONVERSION_FACTORS));
	}

	/**
	 * Create the editor for the settledPrices feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_settledPricesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__SETTLED_PRICES));
	}

	/**
	 * Create the editor for the marketIndices feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_marketIndicesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__MARKET_INDICES));
	}

	/**
	 * Create the editor for the holidayCalendars feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_holidayCalendarsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__HOLIDAY_CALENDARS));
	}

	/**
	 * Create the editor for the settleStrategies feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_settleStrategiesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__SETTLE_STRATEGIES));
	}

	/**
	 * Create the editor for the marketCurvesVersionRecord feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_marketCurvesVersionRecordEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__MARKET_CURVES_VERSION_RECORD));
	}

	/**
	 * Create the editor for the settledPricesVersionRecord feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_settledPricesVersionRecordEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__SETTLED_PRICES_VERSION_RECORD));
	}

	/**
	 * Create the editor for the pricingCalendars feature on PricingModel
	 *
	 * @generated
	 */
	protected void add_pricingCalendarsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PRICING_MODEL__PRICING_CALENDARS));
	}
}