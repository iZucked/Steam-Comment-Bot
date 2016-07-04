/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for AnnealingSettings instances
 *
 * @generated
 */
public class AnnealingSettingsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public AnnealingSettingsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public AnnealingSettingsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using AnnealingSettings as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ParametersPackage.Literals.ANNEALING_SETTINGS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_iterationsEditor(detailComposite, topClass);
		add_epochLengthEditor(detailComposite, topClass);
		add_coolingEditor(detailComposite, topClass);
		add_initialTemperatureEditor(detailComposite, topClass);
		add_restartingEditor(detailComposite, topClass);
		add_restartIterationsThresholdEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the iterations feature on AnnealingSettings
	 *
	 * @generated
	 */
	protected void add_iterationsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.ANNEALING_SETTINGS__ITERATIONS));
	}
	/**
	 * Create the editor for the epochLength feature on AnnealingSettings
	 *
	 * @generated
	 */
	protected void add_epochLengthEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.ANNEALING_SETTINGS__EPOCH_LENGTH));
	}
	/**
	 * Create the editor for the cooling feature on AnnealingSettings
	 *
	 * @generated
	 */
	protected void add_coolingEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.ANNEALING_SETTINGS__COOLING));
	}
	/**
	 * Create the editor for the initialTemperature feature on AnnealingSettings
	 *
	 * @generated
	 */
	protected void add_initialTemperatureEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.ANNEALING_SETTINGS__INITIAL_TEMPERATURE));
	}

	/**
	 * Create the editor for the restarting feature on AnnealingSettings
	 *
	 * @generated
	 */
	protected void add_restartingEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.ANNEALING_SETTINGS__RESTARTING));
	}

	/**
	 * Create the editor for the restartIterationsThreshold feature on AnnealingSettings
	 *
	 * @generated
	 */
	protected void add_restartIterationsThresholdEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ParametersPackage.Literals.ANNEALING_SETTINGS__RESTART_ITERATIONS_THRESHOLD));
	}
}