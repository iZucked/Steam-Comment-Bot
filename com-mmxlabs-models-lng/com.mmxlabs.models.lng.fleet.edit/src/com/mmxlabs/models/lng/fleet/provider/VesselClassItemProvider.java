/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.types.provider.AVesselSetItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.fleet.VesselClass} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class VesselClassItemProvider
	extends AVesselSetItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClassItemProvider(AdapterFactory adapterFactory) {
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

			addInaccessiblePortsPropertyDescriptor(object);
			addInaccessibleRoutesPropertyDescriptor(object);
			addBaseFuelPropertyDescriptor(object);
			addCapacityPropertyDescriptor(object);
			addFillCapacityPropertyDescriptor(object);
			addMinSpeedPropertyDescriptor(object);
			addMaxSpeedPropertyDescriptor(object);
			addMinHeelPropertyDescriptor(object);
			addWarmingTimePropertyDescriptor(object);
			addCoolingVolumePropertyDescriptor(object);
			addRouteParametersPropertyDescriptor(object);
			addPilotLightRatePropertyDescriptor(object);
			addMinBaseFuelConsumptionPropertyDescriptor(object);
			addHasReliqCapabilityPropertyDescriptor(object);
			addScntPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
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
				 getString("_UI_VesselClass_inaccessiblePorts_feature"),
				 getString("_UI_VesselClass_inaccessiblePorts_description"),
				 FleetPackage.Literals.VESSEL_CLASS__INACCESSIBLE_PORTS,
				 true,
				 false,
				 true,
				 null,
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
				 getString("_UI_VesselClass_baseFuel_feature"),
				 getString("_UI_VesselClass_baseFuel_description"),
				 FleetPackage.Literals.VESSEL_CLASS__BASE_FUEL,
				 true,
				 false,
				 true,
				 null,
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
				 getString("_UI_VesselClass_capacity_feature"),
				 getString("_UI_VesselClass_capacity_description"),
				 FleetPackage.Literals.VESSEL_CLASS__CAPACITY,
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
				 getString("_UI_VesselClass_fillCapacity_feature"),
				 getString("_UI_VesselClass_fillCapacity_description"),
				 FleetPackage.Literals.VESSEL_CLASS__FILL_CAPACITY,
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
				 getString("_UI_VesselClass_minSpeed_feature"),
				 getString("_UI_VesselClass_minSpeed_description"),
				 FleetPackage.Literals.VESSEL_CLASS__MIN_SPEED,
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
				 getString("_UI_VesselClass_maxSpeed_feature"),
				 getString("_UI_VesselClass_maxSpeed_description"),
				 FleetPackage.Literals.VESSEL_CLASS__MAX_SPEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min Heel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinHeelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClass_minHeel_feature"),
				 getString("_UI_VesselClass_minHeel_description"),
				 FleetPackage.Literals.VESSEL_CLASS__MIN_HEEL,
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
				 getString("_UI_VesselClass_warmingTime_feature"),
				 getString("_UI_VesselClass_warmingTime_description"),
				 FleetPackage.Literals.VESSEL_CLASS__WARMING_TIME,
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
				 getString("_UI_VesselClass_coolingVolume_feature"),
				 getString("_UI_VesselClass_coolingVolume_description"),
				 FleetPackage.Literals.VESSEL_CLASS__COOLING_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
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
				 getString("_UI_VesselClass_routeParameters_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_routeParameters_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__ROUTE_PARAMETERS,
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
				 getString("_UI_VesselClass_pilotLightRate_feature"),
				 getString("_UI_VesselClass_pilotLightRate_description"),
				 FleetPackage.Literals.VESSEL_CLASS__PILOT_LIGHT_RATE,
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
				 getString("_UI_VesselClass_minBaseFuelConsumption_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_minBaseFuelConsumption_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__MIN_BASE_FUEL_CONSUMPTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
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
				 getString("_UI_VesselClass_hasReliqCapability_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_hasReliqCapability_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__HAS_RELIQ_CAPABILITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
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
				 getString("_UI_VesselClass_inaccessibleRoutes_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_inaccessibleRoutes_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__INACCESSIBLE_ROUTES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
				 getString("_UI_VesselClass_scnt_feature"),
				 getString("_UI_VesselClass_scnt_description"),
				 FleetPackage.Literals.VESSEL_CLASS__SCNT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
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
			childrenFeatures.add(FleetPackage.Literals.VESSEL_CLASS__LADEN_ATTRIBUTES);
			childrenFeatures.add(FleetPackage.Literals.VESSEL_CLASS__BALLAST_ATTRIBUTES);
			childrenFeatures.add(FleetPackage.Literals.VESSEL_CLASS__ROUTE_PARAMETERS);
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
	 * This returns VesselClass.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/VesselClass"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String label = ((VesselClass)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_VesselClass_type") :
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

		switch (notification.getFeatureID(VesselClass.class)) {
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_ROUTES:
			case FleetPackage.VESSEL_CLASS__CAPACITY:
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
			case FleetPackage.VESSEL_CLASS__MIN_HEEL:
			case FleetPackage.VESSEL_CLASS__WARMING_TIME:
			case FleetPackage.VESSEL_CLASS__COOLING_VOLUME:
			case FleetPackage.VESSEL_CLASS__PILOT_LIGHT_RATE:
			case FleetPackage.VESSEL_CLASS__MIN_BASE_FUEL_CONSUMPTION:
			case FleetPackage.VESSEL_CLASS__HAS_RELIQ_CAPABILITY:
			case FleetPackage.VESSEL_CLASS__SCNT:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
			case FleetPackage.VESSEL_CLASS__ROUTE_PARAMETERS:
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
				(FleetPackage.Literals.VESSEL_CLASS__LADEN_ATTRIBUTES,
				 FleetFactory.eINSTANCE.createVesselStateAttributes()));

		newChildDescriptors.add
			(createChildParameter
				(FleetPackage.Literals.VESSEL_CLASS__BALLAST_ATTRIBUTES,
				 FleetFactory.eINSTANCE.createVesselStateAttributes()));

		newChildDescriptors.add
			(createChildParameter
				(FleetPackage.Literals.VESSEL_CLASS__ROUTE_PARAMETERS,
				 FleetFactory.eINSTANCE.createVesselClassRouteParameters()));
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
			childFeature == FleetPackage.Literals.VESSEL_CLASS__LADEN_ATTRIBUTES ||
			childFeature == FleetPackage.Literals.VESSEL_CLASS__BALLAST_ATTRIBUTES;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
