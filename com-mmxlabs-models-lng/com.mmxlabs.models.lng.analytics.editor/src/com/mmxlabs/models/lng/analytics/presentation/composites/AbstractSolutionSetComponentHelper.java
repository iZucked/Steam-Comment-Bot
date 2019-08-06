/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;

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
 * A component helper for AbstractSolutionSet instances
 *
 * @generated
 */
public class AbstractSolutionSetComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public AbstractSolutionSetComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public AbstractSolutionSetComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
	}
	
	/**
	 * add editors to a composite, using AbstractSolutionSet as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_hasDualModeSolutionsEditor(detailComposite, topClass);
		add_portfolioBreakEvenModeEditor(detailComposite, topClass);
		add_userSettingsEditor(detailComposite, topClass);
		add_extraSlotsEditor(detailComposite, topClass);
		add_baseOptionEditor(detailComposite, topClass);
		add_optionsEditor(detailComposite, topClass);
		add_extraVesselEventsEditor(detailComposite, topClass);
		add_extraVesselAvailabilitiesEditor(detailComposite, topClass);
		add_charterInMarketOverridesEditor(detailComposite, topClass);
		add_extraCharterInMarketsEditor(detailComposite, topClass);
		add_useScenarioBaseEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the hasDualModeSolutions feature on AbstractSolutionSet
	 *
	 * @generated
	 */
	protected void add_hasDualModeSolutionsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS));
	}

	/**
	 * Create the editor for the portfolioBreakEvenMode feature on AbstractSolutionSet
	 *
	 * @generated
	 */
	protected void add_portfolioBreakEvenModeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE));
	}

	/**
	 * Create the editor for the userSettings feature on AbstractSolutionSet
	 *
	 * @generated
	 */
	protected void add_userSettingsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__USER_SETTINGS));
	}

	/**
	 * Create the editor for the options feature on AbstractSolutionSet
	 *
	 * @generated
	 */
	protected void add_optionsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__OPTIONS));
	}
	/**
	 * Create the editor for the extraVesselEvents feature on AbstractSolutionSet
	 *
	 * @generated
	 */
	protected void add_extraVesselEventsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS));
	}

	/**
	 * Create the editor for the extraVesselAvailabilities feature on AbstractSolutionSet
	 *
	 * @generated
	 */
	protected void add_extraVesselAvailabilitiesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_AVAILABILITIES));
	}

	/**
	 * Create the editor for the charterInMarketOverrides feature on AbstractSolutionSet
	 *
	 * @generated
	 */
	protected void add_charterInMarketOverridesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES));
	}

	/**
	 * Create the editor for the extraCharterInMarkets feature on AbstractSolutionSet
	 *
	 * @generated
	 */
	protected void add_extraCharterInMarketsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS));
	}

	/**
	 * Create the editor for the useScenarioBase feature on AbstractSolutionSet
	 *
	 * @generated
	 */
	protected void add_useScenarioBaseEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE));
	}

	/**
	 * Create the editor for the extraSlots feature on AbstractSolutionSet
	 *
	 * @generated
	 */
	protected void add_extraSlotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS));
	}

	/**
	 * Create the editor for the baseOption feature on AbstractSolutionSet
	 *
	 * @generated
	 */
	protected void add_baseOptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__BASE_OPTION));
	}
}