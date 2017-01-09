/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
		add_roundTripMatricesEditor(detailComposite, topClass);
		add_selectedMatrixEditor(detailComposite, topClass);
		add_shippingCostPlansEditor(detailComposite, topClass);
		add_cargoSandboxesEditor(detailComposite, topClass);
		add_optionModelsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the roundTripMatrices feature on AnalyticsModel
	 *
	 * @generated
	 */
	protected void add_roundTripMatricesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYTICS_MODEL__ROUND_TRIP_MATRICES));
	}

	/**
	 * Create the editor for the selectedMatrix feature on AnalyticsModel
	 *
	 * @generated
	 */
	protected void add_selectedMatrixEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYTICS_MODEL__SELECTED_MATRIX));
	}

	/**
	 * Create the editor for the shippingCostPlans feature on AnalyticsModel
	 *
	 * @generated
	 */
	protected void add_shippingCostPlansEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYTICS_MODEL__SHIPPING_COST_PLANS));
	}

	/**
	 * Create the editor for the cargoSandboxes feature on AnalyticsModel
	 *
	 * @generated
	 */
	protected void add_cargoSandboxesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYTICS_MODEL__CARGO_SANDBOXES));
	}

	/**
	 * Create the editor for the optionModels feature on AnalyticsModel
	 *
	 * @generated
	 */
	protected void add_optionModelsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTION_MODELS));
	}
}