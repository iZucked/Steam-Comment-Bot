/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.provider;


import com.mmxlabs.models.lng.schedule.NotionalJourneyDetails;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.schedule.NotionalJourneyDetails} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class NotionalJourneyDetailsItemProvider extends MatchingContractDetailsItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotionalJourneyDetailsItemProvider(AdapterFactory adapterFactory) {
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

			addDistancePropertyDescriptor(object);
			addTotalLNGUsedPropertyDescriptor(object);
			addLngPricePropertyDescriptor(object);
			addTotalLNGCostPropertyDescriptor(object);
			addTotalTimeInDaysPropertyDescriptor(object);
			addTotalFuelUsedPropertyDescriptor(object);
			addFuelPricePropertyDescriptor(object);
			addTotalFuelCostPropertyDescriptor(object);
			addHireRatePropertyDescriptor(object);
			addHireCostPropertyDescriptor(object);
			addRouteTakenPropertyDescriptor(object);
			addCanalCostPropertyDescriptor(object);
			addLumpSumPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Distance feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDistancePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_distance_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_distance_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__DISTANCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Total LNG Used feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTotalLNGUsedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_totalLNGUsed_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_totalLNGUsed_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__TOTAL_LNG_USED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Lng Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLngPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_lngPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_lngPrice_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__LNG_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Total LNG Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTotalLNGCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_totalLNGCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_totalLNGCost_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__TOTAL_LNG_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Total Time In Days feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTotalTimeInDaysPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_totalTimeInDays_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_totalTimeInDays_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__TOTAL_TIME_IN_DAYS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Total Fuel Used feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTotalFuelUsedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_totalFuelUsed_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_totalFuelUsed_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__TOTAL_FUEL_USED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Fuel Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFuelPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_fuelPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_fuelPrice_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__FUEL_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Total Fuel Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTotalFuelCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_totalFuelCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_totalFuelCost_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__TOTAL_FUEL_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Hire Rate feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHireRatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_hireRate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_hireRate_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__HIRE_RATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Hire Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHireCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_hireCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_hireCost_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__HIRE_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Route Taken feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRouteTakenPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_routeTaken_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_routeTaken_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__ROUTE_TAKEN,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Canal Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCanalCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_canalCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_canalCost_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__CANAL_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Lump Sum feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLumpSumPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyDetails_lumpSum_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyDetails_lumpSum_feature", "_UI_NotionalJourneyDetails_type"),
				 SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__LUMP_SUM,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns NotionalJourneyDetails.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/NotionalJourneyDetails"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((NotionalJourneyDetails)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_NotionalJourneyDetails_type") :
			getString("_UI_NotionalJourneyDetails_type") + " " + label;
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

		switch (notification.getFeatureID(NotionalJourneyDetails.class)) {
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__DISTANCE:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__TOTAL_LNG_USED:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__LNG_PRICE:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__TOTAL_LNG_COST:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__TOTAL_TIME_IN_DAYS:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__TOTAL_FUEL_USED:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__FUEL_PRICE:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__TOTAL_FUEL_COST:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__HIRE_RATE:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__HIRE_COST:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__ROUTE_TAKEN:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__CANAL_COST:
			case SchedulePackage.NOTIONAL_JOURNEY_DETAILS__LUMP_SUM:
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
