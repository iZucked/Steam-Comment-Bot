/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.provider;


import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;

import com.mmxlabs.models.mmxcore.provider.MMXObjectItemProvider;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.cargo.CanalBookings} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CanalBookingsItemProvider 
	extends MMXObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CanalBookingsItemProvider(AdapterFactory adapterFactory) {
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

			addStrictBoundaryOffsetDaysPropertyDescriptor(object);
			addRelaxedBoundaryOffsetDaysPropertyDescriptor(object);
			addArrivalMarginHoursPropertyDescriptor(object);
			addFlexibleBookingAmountNorthboundPropertyDescriptor(object);
			addFlexibleBookingAmountSouthboundPropertyDescriptor(object);
			addNorthboundMaxIdleDaysPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Strict Boundary Offset Days feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStrictBoundaryOffsetDaysPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CanalBookings_strictBoundaryOffsetDays_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CanalBookings_strictBoundaryOffsetDays_feature", "_UI_CanalBookings_type"),
				 CargoPackage.Literals.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Relaxed Boundary Offset Days feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRelaxedBoundaryOffsetDaysPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CanalBookings_relaxedBoundaryOffsetDays_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CanalBookings_relaxedBoundaryOffsetDays_feature", "_UI_CanalBookings_type"),
				 CargoPackage.Literals.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Arrival Margin Hours feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addArrivalMarginHoursPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CanalBookings_arrivalMarginHours_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CanalBookings_arrivalMarginHours_feature", "_UI_CanalBookings_type"),
				 CargoPackage.Literals.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Flexible Booking Amount Northbound feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFlexibleBookingAmountNorthboundPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CanalBookings_flexibleBookingAmountNorthbound_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CanalBookings_flexibleBookingAmountNorthbound_feature", "_UI_CanalBookings_type"),
				 CargoPackage.Literals.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Flexible Booking Amount Southbound feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFlexibleBookingAmountSouthboundPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CanalBookings_flexibleBookingAmountSouthbound_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CanalBookings_flexibleBookingAmountSouthbound_feature", "_UI_CanalBookings_type"),
				 CargoPackage.Literals.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Northbound Max Idle Days feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNorthboundMaxIdleDaysPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CanalBookings_northboundMaxIdleDays_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CanalBookings_northboundMaxIdleDays_feature", "_UI_CanalBookings_type"),
				 CargoPackage.Literals.CANAL_BOOKINGS__NORTHBOUND_MAX_IDLE_DAYS,
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
			childrenFeatures.add(CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS);
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
	 * This returns CanalBookings.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CanalBookings"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		CanalBookings canalBookings = (CanalBookings)object;
		return getString("_UI_CanalBookings_type") + " " + canalBookings.getStrictBoundaryOffsetDays();
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

		switch (notification.getFeatureID(CanalBookings.class)) {
			case CargoPackage.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS:
			case CargoPackage.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS:
			case CargoPackage.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS:
			case CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND:
			case CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND:
			case CargoPackage.CANAL_BOOKINGS__NORTHBOUND_MAX_IDLE_DAYS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case CargoPackage.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS:
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
				(CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS,
				 CargoFactory.eINSTANCE.createCanalBookingSlot()));
	}

}
