/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.presentation.composites;

import com.mmxlabs.models.lng.parameters.ParametersPackage;

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
 * A component helper for UserSettings instances
 *
 * @generated
 */
public class UserSettingsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public UserSettingsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public UserSettingsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using UserSettings as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ParametersPackage.Literals.USER_SETTINGS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_periodStartEditor(detailComposite, topClass);
		add_periodEndEditor(detailComposite, topClass);
		add_shippingOnlyEditor(detailComposite, topClass);
		add_generateCharterOutsEditor(detailComposite, topClass);
		add_buildActionSetsEditor(detailComposite, topClass);
		add_similarityModeEditor(detailComposite, topClass);
		add_floatingDaysLimitEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the periodStart feature on UserSettings
	 *
	 * @generated
	 */
	protected void add_periodStartEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.USER_SETTINGS__PERIOD_START));
	}
	/**
	 * Create the editor for the periodEnd feature on UserSettings
	 *
	 * @generated
	 */
	protected void add_periodEndEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.USER_SETTINGS__PERIOD_END));
	}
	/**
	 * Create the editor for the shippingOnly feature on UserSettings
	 *
	 * @generated
	 */
	protected void add_shippingOnlyEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.USER_SETTINGS__SHIPPING_ONLY));
	}
	/**
	 * Create the editor for the generateCharterOuts feature on UserSettings
	 *
	 * @generated
	 */
	protected void add_generateCharterOutsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.USER_SETTINGS__GENERATE_CHARTER_OUTS));
	}
	/**
	 * Create the editor for the buildActionSets feature on UserSettings
	 *
	 * @generated
	 */
	protected void add_buildActionSetsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.USER_SETTINGS__BUILD_ACTION_SETS));
	}
	/**
	 * Create the editor for the similarityMode feature on UserSettings
	 *
	 * @generated
	 */
	protected void add_similarityModeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.USER_SETTINGS__SIMILARITY_MODE));
	}

	/**
	 * Create the editor for the floatingDaysLimit feature on UserSettings
	 *
	 * @generated
	 */
	protected void add_floatingDaysLimitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.USER_SETTINGS__FLOATING_DAYS_LIMIT));
	}
}