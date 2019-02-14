/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for AnalyticsModel instances
 *
 * @generated
 */
public class AnalyticsModelComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public AnalyticsModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public AnalyticsModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
	}
	
	/**
	 * add editors to a composite, using AnalyticsModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.ANALYTICS_MODEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_optionModelsEditor(detailComposite, topClass);
		add_optimisationsEditor(detailComposite, topClass);
		add_viabilityModelEditor(detailComposite, topClass);
		add_mtmModelEditor(detailComposite, topClass);
		add_breakevenModelsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the optionModels feature on AnalyticsModel
	 *
	 * @generated
	 */
	protected void add_optionModelsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTION_MODELS));
	}

	/**
	 * Create the editor for the optimisations feature on AnalyticsModel
	 *
	 * @generated
	 */
	protected void add_optimisationsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTIMISATIONS));
	}

	/**
	 * Create the editor for the viabilityModel feature on AnalyticsModel
	 *
	 * @generated
	 */
	protected void add_viabilityModelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYTICS_MODEL__VIABILITY_MODEL));
	}

	/**
	 * Create the editor for the mtmModel feature on AnalyticsModel
	 *
	 * @generated
	 */
	protected void add_mtmModelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYTICS_MODEL__MTM_MODEL));
	}

	/**
	 * Create the editor for the breakevenModels feature on AnalyticsModel
	 *
	 * @generated
	 */
	protected void add_breakevenModelsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYTICS_MODEL__BREAKEVEN_MODELS));
	}
}