/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for VesselStateAttributes instances
 *
 * @generated
 */
public class VesselStateAttributesComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public VesselStateAttributesComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public VesselStateAttributesComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using VesselStateAttributes as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_nboRateEditor(detailComposite, topClass);
		add_idleNBORateEditor(detailComposite, topClass);
		add_idleBaseRateEditor(detailComposite, topClass);
		add_inPortBaseRateEditor(detailComposite, topClass);
		add_fuelConsumptionEditor(detailComposite, topClass);
		add_serviceSpeedEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the nboRate feature on VesselStateAttributes
	 *
	 * @generated
	 */
	protected void add_nboRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE));
	}
	/**
	 * Create the editor for the idleNBORate feature on VesselStateAttributes
	 *
	 * @generated
	 */
	protected void add_idleNBORateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE));
	}
	/**
	 * Create the editor for the idleBaseRate feature on VesselStateAttributes
	 *
	 * @generated
	 */
	protected void add_idleBaseRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE));
	}
	/**
	 * Create the editor for the inPortBaseRate feature on VesselStateAttributes
	 *
	 * @generated
	 */
	protected void add_inPortBaseRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE));
	}

	/**
	 * Create the editor for the fuelConsumption feature on VesselStateAttributes
	 *
	 * @generated
	 */
	protected void add_fuelConsumptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION));
	}

	/**
	 * Create the editor for the serviceSpeed feature on VesselStateAttributes
	 *
	 * @generated
	 */
	protected void add_serviceSpeedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED));
	}
}