/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class BasicSlotPNLDetailsItemProvider
	extends GeneralPNLDetailsItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BasicSlotPNLDetailsItemProvider(AdapterFactory adapterFactory) {
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

			addExtraShippingPNLPropertyDescriptor(object);
			addAdditionalPNLPropertyDescriptor(object);
			addCancellationFeesPropertyDescriptor(object);
			addHedgingValuePropertyDescriptor(object);
			addExtraUpsidePNLPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Extra Shipping PNL feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addExtraShippingPNLPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BasicSlotPNLDetails_extraShippingPNL_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BasicSlotPNLDetails_extraShippingPNL_feature", "_UI_BasicSlotPNLDetails_type"),
				 SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS__EXTRA_SHIPPING_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Additional PNL feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAdditionalPNLPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BasicSlotPNLDetails_additionalPNL_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BasicSlotPNLDetails_additionalPNL_feature", "_UI_BasicSlotPNLDetails_type"),
				 SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS__ADDITIONAL_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cancellation Fees feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCancellationFeesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BasicSlotPNLDetails_cancellationFees_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BasicSlotPNLDetails_cancellationFees_feature", "_UI_BasicSlotPNLDetails_type"),
				 SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Hedging Value feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHedgingValuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BasicSlotPNLDetails_hedgingValue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BasicSlotPNLDetails_hedgingValue_feature", "_UI_BasicSlotPNLDetails_type"),
				 SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS__HEDGING_VALUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Extra Upside PNL feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addExtraUpsidePNLPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BasicSlotPNLDetails_extraUpsidePNL_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BasicSlotPNLDetails_extraUpsidePNL_feature", "_UI_BasicSlotPNLDetails_type"),
				 SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS__EXTRA_UPSIDE_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns BasicSlotPNLDetails.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/BasicSlotPNLDetails"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		BasicSlotPNLDetails basicSlotPNLDetails = (BasicSlotPNLDetails)object;
		return getString("_UI_BasicSlotPNLDetails_type") + " " + basicSlotPNLDetails.getExtraShippingPNL();
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

		switch (notification.getFeatureID(BasicSlotPNLDetails.class)) {
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_SHIPPING_PNL:
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__ADDITIONAL_PNL:
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES:
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__HEDGING_VALUE:
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_UPSIDE_PNL:
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
