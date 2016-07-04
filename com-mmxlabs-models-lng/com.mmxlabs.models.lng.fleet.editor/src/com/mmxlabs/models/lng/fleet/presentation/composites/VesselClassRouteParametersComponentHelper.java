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
 * A component helper for VesselClassRouteParameters instances
 *
 * @generated
 */
public class VesselClassRouteParametersComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public VesselClassRouteParametersComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public VesselClassRouteParametersComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using VesselClassRouteParameters as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_routeEditor(detailComposite, topClass);
		add_extraTransitTimeEditor(detailComposite, topClass);
		add_ladenConsumptionRateEditor(detailComposite, topClass);
		add_ladenNBORateEditor(detailComposite, topClass);
		add_ballastConsumptionRateEditor(detailComposite, topClass);
		add_ballastNBORateEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the route feature on VesselClassRouteParameters
	 *
	 * @generated
	 */
	protected void add_routeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__ROUTE));
	}
	/**
	 * Create the editor for the extraTransitTime feature on VesselClassRouteParameters
	 *
	 * @generated
	 */
	protected void add_extraTransitTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME));
	}
	/**
	 * Create the editor for the ladenConsumptionRate feature on VesselClassRouteParameters
	 *
	 * @generated
	 */
	protected void add_ladenConsumptionRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE));
	}
	/**
	 * Create the editor for the ladenNBORate feature on VesselClassRouteParameters
	 *
	 * @generated
	 */
	protected void add_ladenNBORateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_NBO_RATE));
	}
	/**
	 * Create the editor for the ballastConsumptionRate feature on VesselClassRouteParameters
	 *
	 * @generated
	 */
	protected void add_ballastConsumptionRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE));
	}
	/**
	 * Create the editor for the ballastNBORate feature on VesselClassRouteParameters
	 *
	 * @generated
	 */
	protected void add_ballastNBORateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_NBO_RATE));
	}
}