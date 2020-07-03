/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
 * A component helper for BreakEvenAnalysisRow instances
 *
 * @generated
 */
public class BreakEvenAnalysisRowComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public BreakEvenAnalysisRowComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public BreakEvenAnalysisRowComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using BreakEvenAnalysisRow as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW);	
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
		add_shippingEditor(detailComposite, topClass);
		add_lhsResultsEditor(detailComposite, topClass);
		add_rhsResultsEditor(detailComposite, topClass);
		add_lhsBasedOnEditor(detailComposite, topClass);
		add_rhsBasedOnEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the buyOption feature on BreakEvenAnalysisRow
	 *
	 * @generated
	 */
	protected void add_buyOptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION));
	}

	/**
	 * Create the editor for the sellOption feature on BreakEvenAnalysisRow
	 *
	 * @generated
	 */
	protected void add_sellOptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION));
	}

	/**
	 * Create the editor for the shipping feature on BreakEvenAnalysisRow
	 *
	 * @generated
	 */
	protected void add_shippingEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__SHIPPING));
	}
	/**
	 * Create the editor for the lhsResults feature on BreakEvenAnalysisRow
	 *
	 * @generated
	 */
	protected void add_lhsResultsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS));
	}
	/**
	 * Create the editor for the rhsResults feature on BreakEvenAnalysisRow
	 *
	 * @generated
	 */
	protected void add_rhsResultsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS));
	}

	/**
	 * Create the editor for the lhsBasedOn feature on BreakEvenAnalysisRow
	 *
	 * @generated
	 */
	protected void add_lhsBasedOnEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON));
	}

	/**
	 * Create the editor for the rhsBasedOn feature on BreakEvenAnalysisRow
	 *
	 * @generated
	 */
	protected void add_rhsBasedOnEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON));
	}
}