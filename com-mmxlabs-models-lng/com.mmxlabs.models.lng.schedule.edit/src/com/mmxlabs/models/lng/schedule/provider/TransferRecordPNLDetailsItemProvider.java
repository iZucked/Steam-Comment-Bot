/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.provider;


import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TransferRecordPNLDetailsItemProvider extends GeneralPNLDetailsItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransferRecordPNLDetailsItemProvider(AdapterFactory adapterFactory) {
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

			addTransferRecordPropertyDescriptor(object);
			addTransferPricePropertyDescriptor(object);
			addFromEntityPropertyDescriptor(object);
			addFromEntityRevenuePropertyDescriptor(object);
			addFromEntityCostPropertyDescriptor(object);
			addToEntityPropertyDescriptor(object);
			addToEntityRevenuePropertyDescriptor(object);
			addToEntityCostPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Transfer Record feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTransferRecordPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecordPNLDetails_transferRecord_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TransferRecordPNLDetails_transferRecord_feature", "_UI_TransferRecordPNLDetails_type"),
				 SchedulePackage.Literals.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_RECORD,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Transfer Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTransferPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecordPNLDetails_transferPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TransferRecordPNLDetails_transferPrice_feature", "_UI_TransferRecordPNLDetails_type"),
				 SchedulePackage.Literals.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the From Entity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFromEntityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecordPNLDetails_fromEntity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TransferRecordPNLDetails_fromEntity_feature", "_UI_TransferRecordPNLDetails_type"),
				 SchedulePackage.Literals.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the From Entity Revenue feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFromEntityRevenuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecordPNLDetails_fromEntityRevenue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TransferRecordPNLDetails_fromEntityRevenue_feature", "_UI_TransferRecordPNLDetails_type"),
				 SchedulePackage.Literals.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_REVENUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the From Entity Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFromEntityCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecordPNLDetails_fromEntityCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TransferRecordPNLDetails_fromEntityCost_feature", "_UI_TransferRecordPNLDetails_type"),
				 SchedulePackage.Literals.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the To Entity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addToEntityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecordPNLDetails_toEntity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TransferRecordPNLDetails_toEntity_feature", "_UI_TransferRecordPNLDetails_type"),
				 SchedulePackage.Literals.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the To Entity Revenue feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addToEntityRevenuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecordPNLDetails_toEntityRevenue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TransferRecordPNLDetails_toEntityRevenue_feature", "_UI_TransferRecordPNLDetails_type"),
				 SchedulePackage.Literals.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_REVENUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the To Entity Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addToEntityCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecordPNLDetails_toEntityCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TransferRecordPNLDetails_toEntityCost_feature", "_UI_TransferRecordPNLDetails_type"),
				 SchedulePackage.Literals.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns TransferRecordPNLDetails.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/TransferRecordPNLDetails"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		TransferRecordPNLDetails transferRecordPNLDetails = (TransferRecordPNLDetails)object;
		return getString("_UI_TransferRecordPNLDetails_type") + " " + transferRecordPNLDetails.getTransferPrice();
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

		switch (notification.getFeatureID(TransferRecordPNLDetails.class)) {
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_PRICE:
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_REVENUE:
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_COST:
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_REVENUE:
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_COST:
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
