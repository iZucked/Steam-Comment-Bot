/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.provider;


import com.mmxlabs.models.lng.fleet.FleetFactory;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.provider.AVesselSetItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.fleet.Vessel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class VesselItemProvider
	extends AVesselSetItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addShortNamePropertyDescriptor(object);
			addIMOPropertyDescriptor(object);
			addTypePropertyDescriptor(object);
			addReferencePropertyDescriptor(object);
			addCapacityPropertyDescriptor(object);
			addFillCapacityPropertyDescriptor(object);
			addScntPropertyDescriptor(object);
			addBaseFuelPropertyDescriptor(object);
			addInPortBaseFuelPropertyDescriptor(object);
			addPilotLightBaseFuelPropertyDescriptor(object);
			addIdleBaseFuelPropertyDescriptor(object);
			addPilotLightRatePropertyDescriptor(object);
			addSafetyHeelPropertyDescriptor(object);
			addWarmingTimePropertyDescriptor(object);
			addCoolingVolumePropertyDescriptor(object);
			addCoolingTimePropertyDescriptor(object);
			addPurgeVolumePropertyDescriptor(object);
			addPurgeTimePropertyDescriptor(object);
			addMinSpeedPropertyDescriptor(object);
			addMaxSpeedPropertyDescriptor(object);
			addInaccessiblePortsOverridePropertyDescriptor(object);
			addInaccessiblePortsPropertyDescriptor(object);
			addInaccessibleRoutesOverridePropertyDescriptor(object);
			addInaccessibleRoutesPropertyDescriptor(object);
			addRouteParametersOverridePropertyDescriptor(object);
			addRouteParametersPropertyDescriptor(object);
			addMinBaseFuelConsumptionPropertyDescriptor(object);
			addHasReliqCapabilityOverridePropertyDescriptor(object);
			addHasReliqCapabilityPropertyDescriptor(object);
			addNotesPropertyDescriptor(object);
			addMmxIdPropertyDescriptor(object);
			addReferenceVesselPropertyDescriptor(object);
			addMmxReferencePropertyDescriptor(object);
			addMarkerPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Short Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addShortNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_shortName_feature"),
				 getString("_UI_Vessel_shortName_description"),
				 FleetPackage.Literals.VESSEL__SHORT_NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the IMO feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIMOPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_IMO_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_IMO_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__IMO,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_type_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_type_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__TYPE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Reference feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReferencePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_reference_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_reference_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__REFERENCE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Inaccessible Ports feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInaccessiblePortsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_inaccessiblePorts_feature"),
				 getString("_UI_Vessel_inaccessiblePorts_description"),
				 FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Inaccessible Routes Override feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInaccessibleRoutesOverridePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_inaccessibleRoutesOverride_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_inaccessibleRoutesOverride_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Capacity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCapacityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_capacity_feature"),
				 getString("_UI_Vessel_capacity_description"),
				 FleetPackage.Literals.VESSEL__CAPACITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Fill Capacity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFillCapacityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_fillCapacity_feature"),
				 getString("_UI_Vessel_fillCapacity_description"),
				 FleetPackage.Literals.VESSEL__FILL_CAPACITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min Speed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinSpeedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_minSpeed_feature"),
				 getString("_UI_Vessel_minSpeed_description"),
				 FleetPackage.Literals.VESSEL__MIN_SPEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Max Speed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaxSpeedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_maxSpeed_feature"),
				 getString("_UI_Vessel_maxSpeed_description"),
				 FleetPackage.Literals.VESSEL__MAX_SPEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Safety Heel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSafetyHeelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_safetyHeel_feature"),
				 getString("_UI_Vessel_safetyHeel_description"),
				 FleetPackage.Literals.VESSEL__SAFETY_HEEL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Warming Time feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWarmingTimePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_warmingTime_feature"),
				 getString("_UI_Vessel_warmingTime_description"),
				 FleetPackage.Literals.VESSEL__WARMING_TIME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Purge Time feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPurgeTimePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_purgeTime_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_purgeTime_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__PURGE_TIME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cooling Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCoolingVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_coolingVolume_feature"),
				 getString("_UI_Vessel_coolingVolume_description"),
				 FleetPackage.Literals.VESSEL__COOLING_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cooling Time feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCoolingTimePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_coolingTime_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_coolingTime_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__COOLING_TIME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Purge Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPurgeVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_purgeVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_purgeVolume_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__PURGE_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Route Parameters Override feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRouteParametersOverridePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_routeParametersOverride_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_routeParametersOverride_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS_OVERRIDE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Route Parameters feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRouteParametersPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_routeParameters_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_routeParameters_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS,
				 true,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Pilot Light Rate feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPilotLightRatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_pilotLightRate_feature"),
				 getString("_UI_Vessel_pilotLightRate_description"),
				 FleetPackage.Literals.VESSEL__PILOT_LIGHT_RATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min Base Fuel Consumption feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinBaseFuelConsumptionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_minBaseFuelConsumption_feature"),
				 getString("_UI_Vessel_minBaseFuelConsumption_description"),
				 FleetPackage.Literals.VESSEL__MIN_BASE_FUEL_CONSUMPTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Has Reliq Capability Override feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHasReliqCapabilityOverridePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_hasReliqCapabilityOverride_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_hasReliqCapabilityOverride_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Has Reliq Capability feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHasReliqCapabilityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_hasReliqCapability_feature"),
				 getString("_UI_Vessel_hasReliqCapability_description"),
				 FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Notes feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNotesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_notes_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_notes_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__NOTES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Mmx Id feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMmxIdPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_mmxId_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_mmxId_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__MMX_ID,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Reference Vessel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReferenceVesselPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_referenceVessel_feature"),
				 getString("_UI_Vessel_referenceVessel_description"),
				 FleetPackage.Literals.VESSEL__REFERENCE_VESSEL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Mmx Reference feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMmxReferencePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_mmxReference_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_mmxReference_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__MMX_REFERENCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Marker feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMarkerPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_marker_feature"),
				 getString("_UI_Vessel_marker_description"),
				 FleetPackage.Literals.VESSEL__MARKER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Scnt feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addScntPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_scnt_feature"),
				 getString("_UI_Vessel_scnt_description"),
				 FleetPackage.Literals.VESSEL__SCNT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Inaccessible Ports Override feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInaccessiblePortsOverridePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_inaccessiblePortsOverride_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_inaccessiblePortsOverride_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS_OVERRIDE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(FleetPackage.Literals.VESSEL__LADEN_ATTRIBUTES);
			childrenFeatures.add(FleetPackage.Literals.VESSEL__BALLAST_ATTRIBUTES);
			childrenFeatures.add(FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This adds a property descriptor for the Inaccessible Routes feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInaccessibleRoutesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_inaccessibleRoutes_feature"),
				 getString("_UI_Vessel_inaccessibleRoutes_description"),
				 FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Fuel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseFuelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_baseFuel_feature"),
				 getString("_UI_Vessel_baseFuel_description"),
				 FleetPackage.Literals.VESSEL__BASE_FUEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the In Port Base Fuel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInPortBaseFuelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_inPortBaseFuel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_inPortBaseFuel_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__IN_PORT_BASE_FUEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Pilot Light Base Fuel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPilotLightBaseFuelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_pilotLightBaseFuel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_pilotLightBaseFuel_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__PILOT_LIGHT_BASE_FUEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Idle Base Fuel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIdleBaseFuelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Vessel_idleBaseFuel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Vessel_idleBaseFuel_feature", "_UI_Vessel_type"),
				 FleetPackage.Literals.VESSEL__IDLE_BASE_FUEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This returns Vessel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Vessel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String label = ((Vessel)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_Vessel_type") :
			label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(Vessel.class)) {
			case FleetPackage.VESSEL__SHORT_NAME:
			case FleetPackage.VESSEL__IMO:
			case FleetPackage.VESSEL__TYPE:
			case FleetPackage.VESSEL__CAPACITY:
			case FleetPackage.VESSEL__FILL_CAPACITY:
			case FleetPackage.VESSEL__SCNT:
			case FleetPackage.VESSEL__PILOT_LIGHT_RATE:
			case FleetPackage.VESSEL__SAFETY_HEEL:
			case FleetPackage.VESSEL__WARMING_TIME:
			case FleetPackage.VESSEL__COOLING_VOLUME:
			case FleetPackage.VESSEL__COOLING_TIME:
			case FleetPackage.VESSEL__PURGE_VOLUME:
			case FleetPackage.VESSEL__PURGE_TIME:
			case FleetPackage.VESSEL__MIN_SPEED:
			case FleetPackage.VESSEL__MAX_SPEED:
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS_OVERRIDE:
			case FleetPackage.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE:
			case FleetPackage.VESSEL__INACCESSIBLE_ROUTES:
			case FleetPackage.VESSEL__ROUTE_PARAMETERS_OVERRIDE:
			case FleetPackage.VESSEL__MIN_BASE_FUEL_CONSUMPTION:
			case FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE:
			case FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY:
			case FleetPackage.VESSEL__NOTES:
			case FleetPackage.VESSEL__MMX_ID:
			case FleetPackage.VESSEL__REFERENCE_VESSEL:
			case FleetPackage.VESSEL__MMX_REFERENCE:
			case FleetPackage.VESSEL__MARKER:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case FleetPackage.VESSEL__LADEN_ATTRIBUTES:
			case FleetPackage.VESSEL__BALLAST_ATTRIBUTES:
			case FleetPackage.VESSEL__ROUTE_PARAMETERS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(FleetPackage.Literals.VESSEL__LADEN_ATTRIBUTES,
				 FleetFactory.eINSTANCE.createVesselStateAttributes()));

		newChildDescriptors.add
			(createChildParameter
				(FleetPackage.Literals.VESSEL__BALLAST_ATTRIBUTES,
				 FleetFactory.eINSTANCE.createVesselStateAttributes()));

		newChildDescriptors.add
			(createChildParameter
				(FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS,
				 FleetFactory.eINSTANCE.createVesselRouteParameters()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == FleetPackage.Literals.VESSEL__LADEN_ATTRIBUTES ||
			childFeature == FleetPackage.Literals.VESSEL__BALLAST_ATTRIBUTES;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
