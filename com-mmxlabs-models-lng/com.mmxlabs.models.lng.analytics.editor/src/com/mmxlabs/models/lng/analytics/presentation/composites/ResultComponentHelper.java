/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;

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
 * A component helper for Result instances
 *
 * @generated
 */
public class ResultComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ResultComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ResultComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using Result as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.RESULT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_extraSlotsEditor(detailComposite, topClass);
		add_resultSetsEditor(detailComposite, topClass);
		add_extraVesselAvailabilitiesEditor(detailComposite, topClass);
		add_charterInMarketOverridesEditor(detailComposite, topClass);
		add_extraCharterInMarketsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the extraSlots feature on Result
	 *
	 * @generated
	 */
	protected void add_extraSlotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.RESULT__EXTRA_SLOTS));
	}
	/**
	 * Create the editor for the resultSets feature on Result
	 *
	 * @generated
	 */
	protected void add_resultSetsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.RESULT__RESULT_SETS));
	}

	/**
	 * Create the editor for the extraVesselAvailabilities feature on Result
	 *
	 * @generated
	 */
	protected void add_extraVesselAvailabilitiesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.RESULT__EXTRA_VESSEL_AVAILABILITIES));
	}

	/**
	 * Create the editor for the charterInMarketOverrides feature on Result
	 *
	 * @generated
	 */
	protected void add_charterInMarketOverridesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.RESULT__CHARTER_IN_MARKET_OVERRIDES));
	}

	/**
	 * Create the editor for the extraCharterInMarkets feature on Result
	 *
	 * @generated
	 */
	protected void add_extraCharterInMarketsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.RESULT__EXTRA_CHARTER_IN_MARKETS));
	}
}