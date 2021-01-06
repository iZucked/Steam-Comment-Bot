/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.provider;


import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class InventoryChangeEventItemProvider 
	extends ItemProviderAdapter
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InventoryChangeEventItemProvider(AdapterFactory adapterFactory) {
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

			addDatePropertyDescriptor(object);
			addChangeQuantityPropertyDescriptor(object);
			addCurrentLevelPropertyDescriptor(object);
			addCurrentMinPropertyDescriptor(object);
			addCurrentMaxPropertyDescriptor(object);
			addEventPropertyDescriptor(object);
			addSlotAllocationPropertyDescriptor(object);
			addOpenSlotAllocationPropertyDescriptor(object);
			addBreachedMinPropertyDescriptor(object);
			addBreachedMaxPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_InventoryChangeEvent_date_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_InventoryChangeEvent_date_feature", "_UI_InventoryChangeEvent_type"),
				 SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__DATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Change Quantity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addChangeQuantityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_InventoryChangeEvent_changeQuantity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_InventoryChangeEvent_changeQuantity_feature", "_UI_InventoryChangeEvent_type"),
				 SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__CHANGE_QUANTITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Current Level feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCurrentLevelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_InventoryChangeEvent_currentLevel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_InventoryChangeEvent_currentLevel_feature", "_UI_InventoryChangeEvent_type"),
				 SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__CURRENT_LEVEL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Current Min feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCurrentMinPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_InventoryChangeEvent_currentMin_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_InventoryChangeEvent_currentMin_feature", "_UI_InventoryChangeEvent_type"),
				 SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__CURRENT_MIN,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Current Max feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCurrentMaxPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_InventoryChangeEvent_currentMax_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_InventoryChangeEvent_currentMax_feature", "_UI_InventoryChangeEvent_type"),
				 SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__CURRENT_MAX,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Event feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEventPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_InventoryChangeEvent_event_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_InventoryChangeEvent_event_feature", "_UI_InventoryChangeEvent_type"),
				 SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__EVENT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Slot Allocation feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSlotAllocationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_InventoryChangeEvent_slotAllocation_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_InventoryChangeEvent_slotAllocation_feature", "_UI_InventoryChangeEvent_type"),
				 SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__SLOT_ALLOCATION,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Open Slot Allocation feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOpenSlotAllocationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_InventoryChangeEvent_openSlotAllocation_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_InventoryChangeEvent_openSlotAllocation_feature", "_UI_InventoryChangeEvent_type"),
				 SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__OPEN_SLOT_ALLOCATION,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Breached Min feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBreachedMinPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_InventoryChangeEvent_breachedMin_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_InventoryChangeEvent_breachedMin_feature", "_UI_InventoryChangeEvent_type"),
				 SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__BREACHED_MIN,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Breached Max feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBreachedMaxPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_InventoryChangeEvent_breachedMax_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_InventoryChangeEvent_breachedMax_feature", "_UI_InventoryChangeEvent_type"),
				 SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__BREACHED_MAX,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns InventoryChangeEvent.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/InventoryChangeEvent"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		LocalDateTime labelValue = ((InventoryChangeEvent)object).getDate();
		String label = labelValue == null ? null : labelValue.toString();
		return label == null || label.length() == 0 ?
			getString("_UI_InventoryChangeEvent_type") :
			getString("_UI_InventoryChangeEvent_type") + " " + label;
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

		switch (notification.getFeatureID(InventoryChangeEvent.class)) {
			case SchedulePackage.INVENTORY_CHANGE_EVENT__DATE:
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CHANGE_QUANTITY:
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_LEVEL:
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MIN:
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MAX:
			case SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MIN:
			case SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MAX:
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

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return ((IChildCreationExtender)adapterFactory).getResourceLocator();
	}

}
