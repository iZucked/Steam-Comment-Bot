/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
 * A component helper for AbstractYearMonthCurve instances
 *
 * @generated
 */
public class AbstractYearMonthCurveComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public AbstractYearMonthCurveComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public AbstractYearMonthCurveComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(PricingPackage.Literals.YEAR_MONTH_POINT_CONTAINER));
	}
	
	/**
	 * add editors to a composite, using AbstractYearMonthCurve as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_currencyUnitEditor(detailComposite, topClass);
		add_volumeUnitEditor(detailComposite, topClass);
		add_expressionEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the currencyUnit feature on AbstractYearMonthCurve
	 *
	 * @generated
	 */
	protected void add_currencyUnitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT));
	}
	/**
	 * Create the editor for the volumeUnit feature on AbstractYearMonthCurve
	 *
	 * @generated
	 */
	protected void add_volumeUnitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT));
	}
	/**
	 * Create the editor for the expression feature on AbstractYearMonthCurve
	 *
	 * @generated
	 */
	protected void add_expressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION));
	}
}