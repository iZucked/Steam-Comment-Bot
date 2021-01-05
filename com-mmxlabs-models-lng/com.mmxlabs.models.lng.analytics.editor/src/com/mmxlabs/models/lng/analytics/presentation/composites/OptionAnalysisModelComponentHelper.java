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
 * A component helper for OptionAnalysisModel instances
 *
 * @generated
 */
public class OptionAnalysisModelComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public OptionAnalysisModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public OptionAnalysisModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL));
	}
	
	/**
	 * add editors to a composite, using OptionAnalysisModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_baseCaseEditor(detailComposite, topClass);
		add_partialCaseEditor(detailComposite, topClass);
		add_resultsEditor(detailComposite, topClass);
		add_useTargetPNLEditor(detailComposite, topClass);
		add_modeEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the baseCase feature on OptionAnalysisModel
	 *
	 * @generated
	 */
	protected void add_baseCaseEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BASE_CASE));
	}

	/**
	 * Create the editor for the partialCase feature on OptionAnalysisModel
	 *
	 * @generated
	 */
	protected void add_partialCaseEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE));
	}

	/**
	 * Create the editor for the results feature on OptionAnalysisModel
	 *
	 * @generated
	 */
	protected void add_resultsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULTS));
	}

	/**
	 * Create the editor for the useTargetPNL feature on OptionAnalysisModel
	 *
	 * @generated
	 */
	protected void add_useTargetPNLEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL));
	}

	/**
	 * Create the editor for the mode feature on OptionAnalysisModel
	 *
	 * @generated
	 */
	protected void add_modeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__MODE));
	}
}