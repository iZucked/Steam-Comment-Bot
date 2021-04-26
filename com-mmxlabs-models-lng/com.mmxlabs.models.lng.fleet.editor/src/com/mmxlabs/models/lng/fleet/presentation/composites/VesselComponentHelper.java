/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.RouteExclusionMultiInlineEditor;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.editors.impl.YesNoInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Vessel instances
 *
 * @generated
 */
public class VesselComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public VesselComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public VesselComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.AVESSEL_SET));
	}
	
	/**
	 * add editors to a composite, using Vessel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, FleetPackage.Literals.VESSEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated NOT
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_shortNameEditor(detailComposite, topClass);
		add_IMOEditor(detailComposite, topClass);
		add_typeEditor(detailComposite, topClass);
		add_referenceEditor(detailComposite, topClass);
		add_referenceVesselEditor(detailComposite, topClass);
		add_capacityEditor(detailComposite, topClass);
		add_fillCapacityEditor(detailComposite, topClass);
		add_fillVolumeReadOnlyEditor(detailComposite, topClass);
		add_scntEditor(detailComposite, topClass);
		add_baseFuelEditor(detailComposite, topClass);
		add_inPortBaseFuelEditor(detailComposite, topClass);
		add_pilotLightBaseFuelEditor(detailComposite, topClass);
		add_idleBaseFuelEditor(detailComposite, topClass);
		add_pilotLightRateEditor(detailComposite, topClass);
		add_safetyHeelEditor(detailComposite, topClass);
		add_warmingTimeEditor(detailComposite, topClass);
		add_coolingVolumeEditor(detailComposite, topClass);
		add_coolingTimeEditor(detailComposite, topClass);
		add_purgeVolumeEditor(detailComposite, topClass);
		add_purgeTimeEditor(detailComposite, topClass);
		add_ladenAttributesEditor(detailComposite, topClass);
		add_ballastAttributesEditor(detailComposite, topClass);
		add_minSpeedEditor(detailComposite, topClass);
		add_maxSpeedEditor(detailComposite, topClass);
		add_inaccessiblePortsOverrideEditor(detailComposite, topClass);
		add_inaccessiblePortsEditor(detailComposite, topClass);
		add_inaccessibleRoutesOverrideEditor(detailComposite, topClass);
		add_inaccessibleRoutesEditor(detailComposite, topClass);
		add_routeParametersOverrideEditor(detailComposite, topClass);
		add_routeParametersEditor(detailComposite, topClass);
		add_minBaseFuelConsumptionEditor(detailComposite, topClass);
		add_hasReliqCapabilityOverrideEditor(detailComposite, topClass);
		add_hasReliqCapabilityEditor(detailComposite, topClass);
		add_notesEditor(detailComposite, topClass);
		add_mmxIdEditor(detailComposite, topClass);
	}
	
	/**
	 * Create the editor for the shortName feature on Vessel
	 *
	 * @generated
	 */
	protected void add_shortNameEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__SHORT_NAME));
	}

	/**
	 * Create the editor for the IMO feature on Vessel
	 *
	 * @generated
	 */
	protected void add_IMOEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__IMO));
	}

	/**
	 * Create the editor for the type feature on Vessel
	 *
	 * @generated
	 */
	protected void add_typeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__TYPE));
	}

	/**
	 * Create the editor for the reference feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_referenceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor wrapped = new ReferenceVesselInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__REFERENCE));
		detailComposite.addInlineEditor(wrapped);
	}

	/**
	 * Create the editor for the inaccessiblePorts feature on Vessel
	 *
	 * @generated
	 */
	protected void add_inaccessiblePortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS));
	}
	/**
	 * Create the editor for the inaccessibleRoutesOverride feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_inaccessibleRoutesOverrideEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE));
	}

	/**
	 * Create the editor for the capacity feature on Vessel
	 *
	 * @generated
	 */
	protected void add_capacityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__CAPACITY));
	}

	/**
	 * Create the editor for the fillCapacity feature on Vessel
	 *
	 * @generated
	 */
	protected void add_fillCapacityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__FILL_CAPACITY));
	}

	/**
	 * Create the editor for the fillCapacity feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_fillVolumeReadOnlyEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new VesselFillVolumeInlineEditor(FleetPackage.Literals.VESSEL__CAPACITY));
	}
	
	/**
	 * Create the editor for the ladenAttributes feature on Vessel
	 *
	 * @generated
	 */
	protected void add_ladenAttributesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__LADEN_ATTRIBUTES));
	}

	/**
	 * Create the editor for the ballastAttributes feature on Vessel
	 *
	 * @generated
	 */
	protected void add_ballastAttributesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__BALLAST_ATTRIBUTES));
	}

	/**
	 * Create the editor for the minSpeed feature on Vessel
	 *
	 * @generated
	 */
	protected void add_minSpeedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__MIN_SPEED));
	}

	/**
	 * Create the editor for the maxSpeed feature on Vessel
	 *
	 * @generated
	 */
	protected void add_maxSpeedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__MAX_SPEED));
	}

	/**
	 * Create the editor for the safetyHeel feature on Vessel
	 *
	 * @generated
	 */
	protected void add_safetyHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__SAFETY_HEEL));
	}

	/**
	 * Create the editor for the warmingTime feature on Vessel
	 *
	 * @generated
	 */
	protected void add_warmingTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__WARMING_TIME));
	}

	/**
	 * Create the editor for the purgeTime feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_purgeTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PURGE)) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__PURGE_TIME));
		}
	}

	/**
	 * Create the editor for the coolingVolume feature on Vessel
	 *
	 * @generated
	 */
	protected void add_coolingVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__COOLING_VOLUME));
	}

	/**
	 * Create the editor for the coolingTime feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_coolingTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PURGE)) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__COOLING_TIME));
		}
	}

	/**
	 * Create the editor for the purgeVolume feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_purgeVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PURGE)) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__PURGE_VOLUME));
		}
	}

	/**
	 * Create the editor for the routeParametersOverride feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_routeParametersOverrideEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS_OVERRIDE));
	}

	/**
	 * Create the editor for the routeParameters feature on Vessel
	 *
	 * @generated
	 */
	protected void add_routeParametersEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS));
	}

	/**
	 * Create the editor for the pilotLightRate feature on Vessel
	 *
	 * @generated
	 */
	protected void add_pilotLightRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__PILOT_LIGHT_RATE));
	}

	/**
	 * Create the editor for the minBaseFuelConsumption feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_minBaseFuelConsumptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted("features:min-base-fuel-support")) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__MIN_BASE_FUEL_CONSUMPTION));
		}

	}

	/**
	 * Create the editor for the hasReliqCapabilityOverride feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_hasReliqCapabilityOverrideEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE));
	}

	/**
	 * Create the editor for the hasReliqCapability feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_hasReliqCapabilityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted("features:reliq-support")) {
			detailComposite.addInlineEditor(new YesNoInlineEditor(FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY));
		}
	}

	/**
	 * Create the editor for the notes feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_notesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new MultiTextInlineEditor(FleetPackage.Literals.VESSEL__NOTES));
	}

	/**
	 * Create the editor for the mmxId feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_mmxIdEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__MMX_ID));
	}

	/**
	 * Create the editor for the referenceVessel feature on Vessel
	 *
	 * @generated
	 */
	protected void add_referenceVesselEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__REFERENCE_VESSEL));
	}

	/**
	 * Create the editor for the mmxReference feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_mmxReferenceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__MMX_REFERENCE));
	}

	/**
	 * Create the editor for the scnt feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_scntEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__SCNT));
	}

	/**
	 * Create the editor for the inaccessiblePortsOverride feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_inaccessiblePortsOverrideEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS_OVERRIDE));
	}

	/**
	 * Create the editor for the inaccessibleRoutes feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_inaccessibleRoutesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new RouteExclusionMultiInlineEditor(FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES));
	}

	/**
	 * Create the editor for the baseFuel feature on Vessel
	 *
	 * @generated
	 */
	protected void add_baseFuelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__BASE_FUEL));
	}

	/**
	 * Create the editor for the inPortBaseFuel feature on Vessel
	 *
	 * @generated
	 */
	protected void add_inPortBaseFuelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__IN_PORT_BASE_FUEL));
	}

	/**
	 * Create the editor for the pilotLightBaseFuel feature on Vessel
	 *
	 * @generated
	 */
	protected void add_pilotLightBaseFuelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__PILOT_LIGHT_BASE_FUEL));
	}

	/**
	 * Create the editor for the idleBaseFuel feature on Vessel
	 *
	 * @generated
	 */
	protected void add_idleBaseFuelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__IDLE_BASE_FUEL));
	}
}