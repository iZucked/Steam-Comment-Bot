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

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.RouteCostInlineEditor;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.RouteExclusionMultiInlineEditor;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.MultiEnumInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for VesselClass instances
 *
 * @generated
 */
public class VesselClassComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public VesselClassComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public VesselClassComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.AVESSEL_SET));
	}
	
	/**
	 * add editors to a composite, using VesselClass as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, FleetPackage.Literals.VESSEL_CLASS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_inaccessiblePortsEditor(detailComposite, topClass);
		add_baseFuelEditor(detailComposite, topClass);
		add_capacityEditor(detailComposite, topClass);
		add_fillCapacityEditor(detailComposite, topClass);
		add_ladenAttributesEditor(detailComposite, topClass);
		add_ballastAttributesEditor(detailComposite, topClass);
		add_minSpeedEditor(detailComposite, topClass);
		add_maxSpeedEditor(detailComposite, topClass);
		add_minHeelEditor(detailComposite, topClass);
		add_warmingTimeEditor(detailComposite, topClass);
		add_coolingVolumeEditor(detailComposite, topClass);
		add_routeParametersEditor(detailComposite, topClass);
		add_pilotLightRateEditor(detailComposite, topClass);
		add_minBaseFuelConsumptionEditor(detailComposite, topClass);
		add_hasReliqCapabilityEditor(detailComposite, topClass);
		add_inaccessibleRoutesEditor(detailComposite, topClass);
		add_scntEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the inaccessiblePorts feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_inaccessiblePortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__INACCESSIBLE_PORTS));
	}
	/**
	 * Create the editor for the baseFuel feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_baseFuelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__BASE_FUEL));
	}
	/**
	 * Create the editor for the capacity feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_capacityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__CAPACITY));
	}
	/**
	 * Create the editor for the fillCapacity feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_fillCapacityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__FILL_CAPACITY));
	}
	/**
	 * Create the editor for the ladenAttributes feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_ladenAttributesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__LADEN_ATTRIBUTES));
	}
	/**
	 * Create the editor for the ballastAttributes feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_ballastAttributesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__BALLAST_ATTRIBUTES));
	}
	/**
	 * Create the editor for the minSpeed feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_minSpeedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__MIN_SPEED));
	}
	/**
	 * Create the editor for the maxSpeed feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_maxSpeedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__MAX_SPEED));
	}
	/**
	 * Create the editor for the minHeel feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_minHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__MIN_HEEL));
	}
	/**
	 * Create the editor for the warmingTime feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_warmingTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__WARMING_TIME));
	}
	/**
	 * Create the editor for the coolingVolume feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_coolingVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__COOLING_VOLUME));
	}

	/**
	 * Create the editor for the routeParameters feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_routeParametersEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__ROUTE_PARAMETERS));
	}

	/**
	 * Create the editor for the pilotLightRate feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_pilotLightRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__PILOT_LIGHT_RATE));
	}

	/**
	 * Create the editor for the minBaseFuelConsumption feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_minBaseFuelConsumptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__MIN_BASE_FUEL_CONSUMPTION));
	}

	/**
	 * Create the editor for the hasReliqCapability feature on VesselClass
	 *
	 * @generated NOT
	 */
	protected void add_hasReliqCapabilityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted("features:reliq-support")) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__HAS_RELIQ_CAPABILITY));
		}
	}

	/**
	 * Create the editor for the inaccessibleRoutes feature on VesselClass
	 *
	 * @generated NOT
	 */
	protected void add_inaccessibleRoutesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new RouteExclusionMultiInlineEditor(FleetPackage.Literals.VESSEL_CLASS__INACCESSIBLE_ROUTES));
	}

	/**
	 * Create the editor for the scnt feature on VesselClass
	 *
	 * @generated NOT
	 */
	protected void add_scntEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// BugzId: 1039 - Disabled until ready to release
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__SCNT));
	}
}