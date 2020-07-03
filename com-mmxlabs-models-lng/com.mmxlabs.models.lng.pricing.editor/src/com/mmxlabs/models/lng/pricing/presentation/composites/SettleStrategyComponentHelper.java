/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.editor.SettleStrategyStartDayWrapper;
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
 * A component helper for SettleStrategy instances
 *
 * @generated
 */
public class SettleStrategyComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SettleStrategyComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SettleStrategyComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
	}
	
	/**
	 * add editors to a composite, using SettleStrategy as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.SETTLE_STRATEGY);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_dayOfTheMonthEditor(detailComposite, topClass);
		add_lastDayOfTheMonthEditor(detailComposite, topClass);
		add_settlePeriodEditor(detailComposite, topClass);
		add_settlePeriodUnitEditor(detailComposite, topClass);
		add_settleStartMonthsPriorEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the dayOfTheMonth feature on SettleStrategy
	 *
	 * @generated NOT
	 */
	protected void add_dayOfTheMonthEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new SettleStrategyStartDayWrapper(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SETTLE_STRATEGY__DAY_OF_THE_MONTH)));
	}
	/**
	 * Create the editor for the settlePeriod feature on SettleStrategy
	 *
	 * @generated
	 */
	protected void add_settlePeriodEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SETTLE_STRATEGY__SETTLE_PERIOD));
	}
	/**
	 * Create the editor for the settlePeriodUnit feature on SettleStrategy
	 *
	 * @generated
	 */
	protected void add_settlePeriodUnitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SETTLE_STRATEGY__SETTLE_PERIOD_UNIT));
	}

	/**
	 * Create the editor for the settleStartMonthsPrior feature on SettleStrategy
	 *
	 * @generated
	 */
	protected void add_settleStartMonthsPriorEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SETTLE_STRATEGY__SETTLE_START_MONTHS_PRIOR));
	}

	/**
	 * Create the editor for the lastDayOfTheMonth feature on SettleStrategy
	 *
	 * @generated
	 */
	protected void add_lastDayOfTheMonthEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH));
	}
}