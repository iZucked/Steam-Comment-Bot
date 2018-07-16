/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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
 * A component helper for FleetProfile instances
 *
 * @generated
 */
public class FleetProfileComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public FleetProfileComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public FleetProfileComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using FleetProfile as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ADPPackage.Literals.FLEET_PROFILE);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_vesselAvailabilitiesEditor(detailComposite, topClass);
		add_includeEnabledCharterMarketsEditor(detailComposite, topClass);
		add_constraintsEditor(detailComposite, topClass);
		add_vesselEventsEditor(detailComposite, topClass);
		add_defaultVesselEditor(detailComposite, topClass);
		add_defaultVesselCharterInRateEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the vesselAvailabilities feature on FleetProfile
	 *
	 * @generated
	 */
	protected void add_vesselAvailabilitiesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.FLEET_PROFILE__VESSEL_AVAILABILITIES));
	}

	/**
	 * Create the editor for the includeEnabledCharterMarkets feature on FleetProfile
	 *
	 * @generated
	 */
	protected void add_includeEnabledCharterMarketsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.FLEET_PROFILE__INCLUDE_ENABLED_CHARTER_MARKETS));
	}

	/**
	 * Create the editor for the constraints feature on FleetProfile
	 *
	 * @generated
	 */
	protected void add_constraintsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.FLEET_PROFILE__CONSTRAINTS));
	}

	/**
	 * Create the editor for the vesselEvents feature on FleetProfile
	 *
	 * @generated
	 */
	protected void add_vesselEventsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.FLEET_PROFILE__VESSEL_EVENTS));
	}

	/**
	 * Create the editor for the defaultVessel feature on FleetProfile
	 *
	 * @generated
	 */
	protected void add_defaultVesselEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.FLEET_PROFILE__DEFAULT_VESSEL));
	}

	/**
	 * Create the editor for the defaultVesselCharterInRate feature on FleetProfile
	 *
	 * @generated
	 */
	protected void add_defaultVesselCharterInRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.FLEET_PROFILE__DEFAULT_VESSEL_CHARTER_IN_RATE));
	}
}