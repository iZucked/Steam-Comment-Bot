/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
 * A component helper for BreakEvenAnalysisResult instances
 *
 * @generated
 */
public class BreakEvenAnalysisResultComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public BreakEvenAnalysisResultComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public BreakEvenAnalysisResultComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using BreakEvenAnalysisResult as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_RESULT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_targetEditor(detailComposite, topClass);
		add_priceEditor(detailComposite, topClass);
		add_etaEditor(detailComposite, topClass);
		add_referencePriceEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the target feature on BreakEvenAnalysisResult
	 *
	 * @generated
	 */
	protected void add_targetEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_RESULT__TARGET));
	}
	/**
	 * Create the editor for the price feature on BreakEvenAnalysisResult
	 *
	 * @generated
	 */
	protected void add_priceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_RESULT__PRICE));
	}
	/**
	 * Create the editor for the eta feature on BreakEvenAnalysisResult
	 *
	 * @generated
	 */
	protected void add_etaEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_RESULT__ETA));
	}
	/**
	 * Create the editor for the referencePrice feature on BreakEvenAnalysisResult
	 *
	 * @generated
	 */
	protected void add_referencePriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_RESULT__REFERENCE_PRICE));
	}
}