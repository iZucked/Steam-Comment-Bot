/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import com.mmxlabs.models.lng.adp.ADPPackage;

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
 * A component helper for TargetCargoesOnVesselConstraint instances
 *
 * @generated
 */
public class TargetCargoesOnVesselConstraintComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public TargetCargoesOnVesselConstraintComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public TargetCargoesOnVesselConstraintComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(ADPPackage.Literals.FLEET_CONSTRAINT));
	}
	
	/**
	 * add editors to a composite, using TargetCargoesOnVesselConstraint as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ADPPackage.Literals.TARGET_CARGOES_ON_VESSEL_CONSTRAINT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_vesselEditor(detailComposite, topClass);
		add_targetNumberOfCargoesEditor(detailComposite, topClass);
		add_intervalTypeEditor(detailComposite, topClass);
		add_weightEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the vessel feature on TargetCargoesOnVesselConstraint
	 *
	 * @generated
	 */
	protected void add_vesselEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__VESSEL));
	}
	/**
	 * Create the editor for the targetNumberOfCargoes feature on TargetCargoesOnVesselConstraint
	 *
	 * @generated
	 */
	protected void add_targetNumberOfCargoesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__TARGET_NUMBER_OF_CARGOES));
	}
	/**
	 * Create the editor for the intervalType feature on TargetCargoesOnVesselConstraint
	 *
	 * @generated
	 */
	protected void add_intervalTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__INTERVAL_TYPE));
	}

	/**
	 * Create the editor for the weight feature on TargetCargoesOnVesselConstraint
	 *
	 * @generated
	 */
	protected void add_weightEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.TARGET_CARGOES_ON_VESSEL_CONSTRAINT__WEIGHT));
	}
}