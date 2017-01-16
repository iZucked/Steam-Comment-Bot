/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.mmxcore.provider.MMXObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class VesselClassRouteParametersItemProvider
	extends MMXObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClassRouteParametersItemProvider(AdapterFactory adapterFactory) {
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

			addRoutePropertyDescriptor(object);
			addExtraTransitTimePropertyDescriptor(object);
			addLadenConsumptionRatePropertyDescriptor(object);
			addLadenNBORatePropertyDescriptor(object);
			addBallastConsumptionRatePropertyDescriptor(object);
			addBallastNBORatePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Route feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRoutePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClassRouteParameters_route_feature"),
				 getString("_UI_VesselClassRouteParameters_route_description"),
				 FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__ROUTE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Extra Transit Time feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addExtraTransitTimePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClassRouteParameters_extraTransitTime_feature"),
				 getString("_UI_VesselClassRouteParameters_extraTransitTime_description"),
				 FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Laden Consumption Rate feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLadenConsumptionRatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClassRouteParameters_ladenConsumptionRate_feature"),
				 getString("_UI_VesselClassRouteParameters_ladenConsumptionRate_description"),
				 FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Laden NBO Rate feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLadenNBORatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClassRouteParameters_ladenNBORate_feature"),
				 getString("_UI_VesselClassRouteParameters_ladenNBORate_description"),
				 FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_NBO_RATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ballast Consumption Rate feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBallastConsumptionRatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClassRouteParameters_ballastConsumptionRate_feature"),
				 getString("_UI_VesselClassRouteParameters_ballastConsumptionRate_description"),
				 FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ballast NBO Rate feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBallastNBORatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClassRouteParameters_ballastNBORate_feature"),
				 getString("_UI_VesselClassRouteParameters_ballastNBORate_description"),
				 FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_NBO_RATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns VesselClassRouteParameters.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/VesselClassRouteParameters"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		VesselClassRouteParameters vesselClassRouteParameters = (VesselClassRouteParameters)object;
		return getString("_UI_VesselClassRouteParameters_type") + " " + vesselClassRouteParameters.getExtraTransitTime();
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

		switch (notification.getFeatureID(VesselClassRouteParameters.class)) {
			case FleetPackage.VESSEL_CLASS_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME:
			case FleetPackage.VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE:
			case FleetPackage.VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_NBO_RATE:
			case FleetPackage.VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE:
			case FleetPackage.VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_NBO_RATE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
	}

}
