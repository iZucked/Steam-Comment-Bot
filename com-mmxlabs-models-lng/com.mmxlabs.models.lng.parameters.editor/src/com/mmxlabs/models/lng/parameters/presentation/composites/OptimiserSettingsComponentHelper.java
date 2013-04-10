/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for OptimiserSettings instances
 *
 * @generated
 */
public class OptimiserSettingsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public OptimiserSettingsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public OptimiserSettingsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.AOPTIMISATION_SETTINGS));
	}
	
	/**
	 * add editors to a composite, using OptimiserSettings as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ParametersPackage.Literals.OPTIMISER_SETTINGS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_objectivesEditor(detailComposite, topClass);
		add_constraintsEditor(detailComposite, topClass);
		add_rangeEditor(detailComposite, topClass);
		add_annealingSettingsEditor(detailComposite, topClass);
		add_seedEditor(detailComposite, topClass);
		add_argumentsEditor(detailComposite, topClass);
		add_rewireEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the objectives feature on OptimiserSettings
	 *
	 * @generated
	 */
	protected void add_objectivesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.OPTIMISER_SETTINGS__OBJECTIVES));
	}
	/**
	 * Create the editor for the constraints feature on OptimiserSettings
	 *
	 * @generated
	 */
	protected void add_constraintsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.OPTIMISER_SETTINGS__CONSTRAINTS));
	}
	/**
	 * Create the editor for the range feature on OptimiserSettings
	 *
	 * @generated
	 */
	protected void add_rangeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.OPTIMISER_SETTINGS__RANGE));
	}
	/**
	 * Create the editor for the annealingSettings feature on OptimiserSettings
	 *
	 * @generated
	 */
	protected void add_annealingSettingsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.OPTIMISER_SETTINGS__ANNEALING_SETTINGS));
	}
	/**
	 * Create the editor for the seed feature on OptimiserSettings
	 *
	 * @generated
	 */
	protected void add_seedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.OPTIMISER_SETTINGS__SEED));
	}
	/**
	 * Create the editor for the arguments feature on OptimiserSettings
	 *
	 * @generated
	 */
	protected void add_argumentsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.OPTIMISER_SETTINGS__ARGUMENTS));
	}

	/**
	 * Create the editor for the rewire feature on OptimiserSettings
	 *
	 * @generated
	 */
	protected void add_rewireEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.OPTIMISER_SETTINGS__REWIRE));
	}
}