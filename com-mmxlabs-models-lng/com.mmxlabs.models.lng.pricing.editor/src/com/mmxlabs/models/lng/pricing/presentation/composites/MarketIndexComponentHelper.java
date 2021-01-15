/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
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
 * A component helper for MarketIndex instances
 *
 * @generated
 */
public class MarketIndexComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public MarketIndexComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public MarketIndexComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
	}
	
	/**
	 * add editors to a composite, using MarketIndex as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.MARKET_INDEX);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_settleCalendarEditor(detailComposite, topClass);
		add_pricingCalendarEditor(detailComposite, topClass);
		add_flatCurveEditor(detailComposite, topClass);
		add_bidCurveEditor(detailComposite, topClass);
		add_offerCurveEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the settleCalendar feature on MarketIndex
	 *
	 * @generated
	 */
	protected void add_settleCalendarEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.MARKET_INDEX__SETTLE_CALENDAR));
	}

	/**
	 * Create the editor for the pricingCalendar feature on MarketIndex
	 *
	 * @generated
	 */
	protected void add_pricingCalendarEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.MARKET_INDEX__PRICING_CALENDAR));
	}

	/**
	 * Create the editor for the flatCurve feature on MarketIndex
	 *
	 * @generated NOT
	 */
	protected void add_flatCurveEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS)) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.MARKET_INDEX__FLAT_CURVE));
		}
	}

	/**
	 * Create the editor for the bidCurve feature on MarketIndex
	 *
	 * @generated NOT
	 */
	protected void add_bidCurveEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS)) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.MARKET_INDEX__BID_CURVE));
		}
	}

	/**
	 * Create the editor for the offerCurve feature on MarketIndex
	 *
	 * @generated NOT
	 */
	protected void add_offerCurveEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS)) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.MARKET_INDEX__OFFER_CURVE));
		}
	}
}