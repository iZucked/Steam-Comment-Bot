/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.provider.MMXObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.schedule.Schedule} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ScheduleItemProvider
	extends MMXObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScheduleItemProvider(AdapterFactory adapterFactory) {
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

			addCargoAllocationsPropertyDescriptor(object);
			addUnusedElementsPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Cargo Allocations feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCargoAllocationsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Schedule_cargoAllocations_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Schedule_cargoAllocations_feature", "_UI_Schedule_type"),
				 SchedulePackage.Literals.SCHEDULE__CARGO_ALLOCATIONS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Unused Elements feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUnusedElementsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Schedule_unusedElements_feature"),
				 getString("_UI_Schedule_unusedElements_description"),
				 SchedulePackage.Literals.SCHEDULE__UNUSED_ELEMENTS,
				 true,
				 false,
				 true,
				 null,
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
			childrenFeatures.add(SchedulePackage.Literals.SCHEDULE__SEQUENCES);
			childrenFeatures.add(SchedulePackage.Literals.SCHEDULE__CARGO_ALLOCATIONS);
			childrenFeatures.add(SchedulePackage.Literals.SCHEDULE__OPEN_SLOT_ALLOCATIONS);
			childrenFeatures.add(SchedulePackage.Literals.SCHEDULE__MARKET_ALLOCATIONS);
			childrenFeatures.add(SchedulePackage.Literals.SCHEDULE__SLOT_ALLOCATIONS);
			childrenFeatures.add(SchedulePackage.Literals.SCHEDULE__FITNESSES);
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
	 * This returns Schedule.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Schedule"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		return getString("_UI_Schedule_type");
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

		switch (notification.getFeatureID(Schedule.class)) {
			case SchedulePackage.SCHEDULE__SEQUENCES:
			case SchedulePackage.SCHEDULE__OPEN_SLOT_ALLOCATIONS:
			case SchedulePackage.SCHEDULE__MARKET_ALLOCATIONS:
			case SchedulePackage.SCHEDULE__SLOT_ALLOCATIONS:
			case SchedulePackage.SCHEDULE__FITNESSES:
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
				(SchedulePackage.Literals.SCHEDULE__SEQUENCES,
				 ScheduleFactory.eINSTANCE.createSequence()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__OPEN_SLOT_ALLOCATIONS,
				 ScheduleFactory.eINSTANCE.createOpenSlotAllocation()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__MARKET_ALLOCATIONS,
				 ScheduleFactory.eINSTANCE.createMarketAllocation()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__SLOT_ALLOCATIONS,
				 ScheduleFactory.eINSTANCE.createSlotAllocation()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__FITNESSES,
				 ScheduleFactory.eINSTANCE.createFitness()));
	}

}
