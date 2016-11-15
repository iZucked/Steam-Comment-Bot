/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
 * A component helper for AnalysisResultRow instances
 *
 * @generated
 */
public class AnalysisResultRowComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public AnalysisResultRowComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public AnalysisResultRowComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using AnalysisResultRow as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_buyOptionEditor(detailComposite, topClass);
		add_sellOptionEditor(detailComposite, topClass);
		add_resultDetailEditor(detailComposite, topClass);
		add_shippingEditor(detailComposite, topClass);
		add_resultDetailsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the buyOption feature on AnalysisResultRow
	 *
	 * @generated
	 */
	protected void add_buyOptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__BUY_OPTION));
	}
	/**
	 * Create the editor for the sellOption feature on AnalysisResultRow
	 *
	 * @generated
	 */
	protected void add_sellOptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SELL_OPTION));
	}
	/**
	 * Create the editor for the resultDetail feature on AnalysisResultRow
	 *
	 * @generated
	 */
	protected void add_resultDetailEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__RESULT_DETAIL));
	}

	/**
	 * Create the editor for the shipping feature on AnalysisResultRow
	 *
	 * @generated
	 */
	protected void add_shippingEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SHIPPING));
	}

	/**
	 * Create the editor for the resultDetails feature on AnalysisResultRow
	 *
	 * @generated
	 */
	protected void add_resultDetailsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__RESULT_DETAILS));
	}
}