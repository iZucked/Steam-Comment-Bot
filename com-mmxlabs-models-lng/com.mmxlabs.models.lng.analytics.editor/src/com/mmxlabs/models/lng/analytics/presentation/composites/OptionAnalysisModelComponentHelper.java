/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
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
		add_buysEditor(detailComposite, topClass);
		add_sellsEditor(detailComposite, topClass);
		add_baseCaseEditor(detailComposite, topClass);
		add_shippingTemplatesEditor(detailComposite, topClass);
		add_rulesEditor(detailComposite, topClass);
		add_partialCaseEditor(detailComposite, topClass);
		add_resultSetsEditor(detailComposite, topClass);
		add_useTargetPNLEditor(detailComposite, topClass);
		add_childrenEditor(detailComposite, topClass);
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
	 * Create the editor for the shippingTemplates feature on OptionAnalysisModel
	 *
	 * @generated
	 */
	protected void add_shippingTemplatesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES));
	}

	/**
	 * Create the editor for the buys feature on OptionAnalysisModel
	 *
	 * @generated
	 */
	protected void add_buysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS));
	}
	/**
	 * Create the editor for the sells feature on OptionAnalysisModel
	 *
	 * @generated
	 */
	protected void add_sellsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS));
	}
	/**
	 * Create the editor for the rules feature on OptionAnalysisModel
	 *
	 * @generated
	 */
	protected void add_rulesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RULES));
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
	 * Create the editor for the resultSets feature on OptionAnalysisModel
	 *
	 * @generated
	 */
	protected void add_resultSetsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_SETS));
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
	 * Create the editor for the children feature on OptionAnalysisModel
	 *
	 * @generated
	 */
	protected void add_childrenEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__CHILDREN));
	}
}