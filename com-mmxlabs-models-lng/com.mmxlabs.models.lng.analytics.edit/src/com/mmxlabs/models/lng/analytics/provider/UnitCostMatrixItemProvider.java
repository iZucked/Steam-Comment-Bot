/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.UnitCostMatrix} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class UnitCostMatrixItemProvider
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnitCostMatrixItemProvider(AdapterFactory adapterFactory) {
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

			addNamePropertyDescriptor(object);
			addFromPortsPropertyDescriptor(object);
			addToPortsPropertyDescriptor(object);
			addVesselPropertyDescriptor(object);
			addNotionalDayRatePropertyDescriptor(object);
			addSpeedPropertyDescriptor(object);
			addRoundTripPropertyDescriptor(object);
			addMinimumLoadPropertyDescriptor(object);
			addMaximumLoadPropertyDescriptor(object);
			addMinimumDischargePropertyDescriptor(object);
			addMaximumDischargePropertyDescriptor(object);
			addRetainHeelPropertyDescriptor(object);
			addCargoPricePropertyDescriptor(object);
			addBaseFuelPricePropertyDescriptor(object);
			addCvValuePropertyDescriptor(object);
			addAllowedRoutesPropertyDescriptor(object);
			addRevenueSharePropertyDescriptor(object);
			addLadenTimeAllowancePropertyDescriptor(object);
			addBallastTimeAllowancePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NamedObject_name_feature"),
				 getString("_UI_NamedObject_name_description"),
				 MMXCorePackage.Literals.NAMED_OBJECT__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the From Ports feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFromPortsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_fromPorts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_fromPorts_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__FROM_PORTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the To Ports feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addToPortsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_toPorts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_toPorts_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__TO_PORTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Vessel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVesselPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_vessel_feature"),
				 getString("_UI_UnitCostMatrix_vessel_description"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__VESSEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Notional Day Rate feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNotionalDayRatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_notionalDayRate_feature"),
				 getString("_UI_UnitCostMatrix_notionalDayRate_description"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__NOTIONAL_DAY_RATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Speed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSpeedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_speed_feature"),
				 getString("_UI_UnitCostMatrix_speed_description"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__SPEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Round Trip feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRoundTripPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_roundTrip_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_roundTrip_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__ROUND_TRIP,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Minimum Load feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinimumLoadPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_minimumLoad_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_minimumLoad_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__MINIMUM_LOAD,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Maximum Load feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaximumLoadPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_maximumLoad_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_maximumLoad_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__MAXIMUM_LOAD,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Minimum Discharge feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinimumDischargePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_minimumDischarge_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_minimumDischarge_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__MINIMUM_DISCHARGE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Maximum Discharge feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaximumDischargePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_maximumDischarge_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_maximumDischarge_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__MAXIMUM_DISCHARGE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Retain Heel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRetainHeelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_retainHeel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_retainHeel_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__RETAIN_HEEL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cargo Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCargoPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_cargoPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_cargoPrice_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__CARGO_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Fuel Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseFuelPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_baseFuelPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_baseFuelPrice_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__BASE_FUEL_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cv Value feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCvValuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_cvValue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_cvValue_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__CV_VALUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Allowed Routes feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAllowedRoutesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_allowedRoutes_feature"),
				 getString("_UI_UnitCostMatrix_allowedRoutes_description"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__ALLOWED_ROUTES,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Revenue Share feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRevenueSharePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_revenueShare_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_revenueShare_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__REVENUE_SHARE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Laden Time Allowance feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLadenTimeAllowancePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_ladenTimeAllowance_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_ladenTimeAllowance_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__LADEN_TIME_ALLOWANCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ballast Time Allowance feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBallastTimeAllowancePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostMatrix_ballastTimeAllowance_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostMatrix_ballastTimeAllowance_feature", "_UI_UnitCostMatrix_type"),
				 AnalyticsPackage.Literals.UNIT_COST_MATRIX__BALLAST_TIME_ALLOWANCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
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
			childrenFeatures.add(AnalyticsPackage.Literals.UNIT_COST_MATRIX__COST_LINES);
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
	 * This returns UnitCostMatrix.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/UnitCostMatrix"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((UnitCostMatrix)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_UnitCostMatrix_type") :
			getString("_UI_UnitCostMatrix_type") + " " + label;
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

		switch (notification.getFeatureID(UnitCostMatrix.class)) {
			case AnalyticsPackage.UNIT_COST_MATRIX__NAME:
			case AnalyticsPackage.UNIT_COST_MATRIX__NOTIONAL_DAY_RATE:
			case AnalyticsPackage.UNIT_COST_MATRIX__SPEED:
			case AnalyticsPackage.UNIT_COST_MATRIX__ROUND_TRIP:
			case AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_LOAD:
			case AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_LOAD:
			case AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_DISCHARGE:
			case AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_DISCHARGE:
			case AnalyticsPackage.UNIT_COST_MATRIX__RETAIN_HEEL:
			case AnalyticsPackage.UNIT_COST_MATRIX__CARGO_PRICE:
			case AnalyticsPackage.UNIT_COST_MATRIX__BASE_FUEL_PRICE:
			case AnalyticsPackage.UNIT_COST_MATRIX__CV_VALUE:
			case AnalyticsPackage.UNIT_COST_MATRIX__REVENUE_SHARE:
			case AnalyticsPackage.UNIT_COST_MATRIX__LADEN_TIME_ALLOWANCE:
			case AnalyticsPackage.UNIT_COST_MATRIX__BALLAST_TIME_ALLOWANCE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES:
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
				(AnalyticsPackage.Literals.UNIT_COST_MATRIX__COST_LINES,
				 AnalyticsFactory.eINSTANCE.createUnitCostLine()));
	}

}
