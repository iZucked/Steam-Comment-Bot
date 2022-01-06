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
 * A component helper for BaseCaseRowOptions instances
 *
 * @generated
 */
public class BaseCaseRowOptionsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public BaseCaseRowOptionsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public BaseCaseRowOptionsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using BaseCaseRowOptions as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_ladenRouteEditor(detailComposite, topClass);
		add_ballastRouteEditor(detailComposite, topClass);
		add_ladenFuelChoiceEditor(detailComposite, topClass);
		add_ballastFuelChoiceEditor(detailComposite, topClass);
		add_loadDateEditor(detailComposite, topClass);
		add_dischargeDateEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the ladenRoute feature on BaseCaseRowOptions
	 *
	 * @generated
	 */
	protected void add_ladenRouteEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS__LADEN_ROUTE));
	}
	/**
	 * Create the editor for the ballastRoute feature on BaseCaseRowOptions
	 *
	 * @generated
	 */
	protected void add_ballastRouteEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS__BALLAST_ROUTE));
	}
	/**
	 * Create the editor for the ladenFuelChoice feature on BaseCaseRowOptions
	 *
	 * @generated
	 */
	protected void add_ladenFuelChoiceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICE));
	}
	/**
	 * Create the editor for the ballastFuelChoice feature on BaseCaseRowOptions
	 *
	 * @generated
	 */
	protected void add_ballastFuelChoiceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICE));
	}

	/**
	 * Create the editor for the loadDate feature on BaseCaseRowOptions
	 *
	 * @generated
	 */
	protected void add_loadDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS__LOAD_DATE));
	}

	/**
	 * Create the editor for the dischargeDate feature on BaseCaseRowOptions
	 *
	 * @generated
	 */
	protected void add_dischargeDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS__DISCHARGE_DATE));
	}
}