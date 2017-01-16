/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.mmxcore.provider.MMXObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.schedule.SlotAllocation} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SlotAllocationItemProvider
	extends MMXObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocationItemProvider(AdapterFactory adapterFactory) {
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

			addSlotPropertyDescriptor(object);
			addSpotMarketPropertyDescriptor(object);
			addCargoAllocationPropertyDescriptor(object);
			addMarketAllocationPropertyDescriptor(object);
			addSlotVisitPropertyDescriptor(object);
			addPricePropertyDescriptor(object);
			addVolumeTransferredPropertyDescriptor(object);
			addEnergyTransferredPropertyDescriptor(object);
			addCvPropertyDescriptor(object);
			addVolumeValuePropertyDescriptor(object);
			addPhysicalVolumeTransferredPropertyDescriptor(object);
			addPhysicalEnergyTransferredPropertyDescriptor(object);
			addSlotAllocationTypePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Slot feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSlotPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_slot_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_slot_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__SLOT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Spot Market feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSpotMarketPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_spotMarket_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_spotMarket_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__SPOT_MARKET,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cargo Allocation feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCargoAllocationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_cargoAllocation_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_cargoAllocation_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__CARGO_ALLOCATION,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Market Allocation feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMarketAllocationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_marketAllocation_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_marketAllocation_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__MARKET_ALLOCATION,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Slot Visit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSlotVisitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_slotVisit_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_slotVisit_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__SLOT_VISIT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_price_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_price_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume Transferred feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeTransferredPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_volumeTransferred_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_volumeTransferred_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_TRANSFERRED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Energy Transferred feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEnergyTransferredPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_energyTransferred_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_energyTransferred_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cv feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCvPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_cv_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_cv_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__CV,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume Value feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeValuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_volumeValue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_volumeValue_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Physical Volume Transferred feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPhysicalVolumeTransferredPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_physicalVolumeTransferred_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_physicalVolumeTransferred_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__PHYSICAL_VOLUME_TRANSFERRED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Physical Energy Transferred feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPhysicalEnergyTransferredPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_physicalEnergyTransferred_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_physicalEnergyTransferred_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__PHYSICAL_ENERGY_TRANSFERRED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Slot Allocation Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSlotAllocationTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotAllocation_slotAllocationType_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotAllocation_slotAllocationType_feature", "_UI_SlotAllocation_type"),
				 SchedulePackage.Literals.SLOT_ALLOCATION__SLOT_ALLOCATION_TYPE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
			childrenFeatures.add(SchedulePackage.Literals.SLOT_ALLOCATION__SLOT);
			childrenFeatures.add(SchedulePackage.Literals.SLOT_ALLOCATION__EXPOSURES);
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
	 * This returns SlotAllocation.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SlotAllocation"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		SlotAllocation slotAllocation = (SlotAllocation)object;
		String text =  getString("_UI_SlotAllocation_type");
		Slot slot = slotAllocation.getSlot();
		if (slot != null) {
			text += " " + slot.getName();
		}
		return text;
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

		switch (notification.getFeatureID(SlotAllocation.class)) {
			case SchedulePackage.SLOT_ALLOCATION__PRICE:
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_TRANSFERRED:
			case SchedulePackage.SLOT_ALLOCATION__ENERGY_TRANSFERRED:
			case SchedulePackage.SLOT_ALLOCATION__CV:
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_VALUE:
			case SchedulePackage.SLOT_ALLOCATION__PHYSICAL_VOLUME_TRANSFERRED:
			case SchedulePackage.SLOT_ALLOCATION__PHYSICAL_ENERGY_TRANSFERRED:
			case SchedulePackage.SLOT_ALLOCATION__SLOT_ALLOCATION_TYPE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case SchedulePackage.SLOT_ALLOCATION__EXPOSURES:
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
				(SchedulePackage.Literals.SLOT_ALLOCATION__EXPOSURES,
				 ScheduleFactory.eINSTANCE.createExposureDetail()));
	}

}
