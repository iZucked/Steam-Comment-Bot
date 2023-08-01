/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.provider;


import com.mmxlabs.models.lng.schedule.NonShippedSequence;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import com.mmxlabs.models.mmxcore.provider.MMXObjectItemProvider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.schedule.NonShippedSequence} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class NonShippedSequenceItemProvider extends MMXObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NonShippedSequenceItemProvider(AdapterFactory adapterFactory) {
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

			addVesselPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
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
				 getString("_UI_NonShippedSequence_vessel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NonShippedSequence_vessel_feature", "_UI_NonShippedSequence_type"),
				 SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__VESSEL,
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
			childrenFeatures.add(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS);
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
	 * This returns NonShippedSequence.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/NonShippedSequence"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		return getString("_UI_NonShippedSequence_type");
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

		switch (notification.getFeatureID(NonShippedSequence.class)) {
			case SchedulePackage.NON_SHIPPED_SEQUENCE__EVENTS:
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
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createStartEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createEndEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createJourney()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createIdle()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createPortVisit()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createSlotVisit()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createVesselEventVisit()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createGeneratedCharterOut()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createGeneratedCharterLengthEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createCooldown()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createPurge()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createCharterAvailableToEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createCanalJourneyEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createCharterAvailableFromEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createGroupedCharterOutEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createGroupedCharterLengthEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createNonShippedSlotVisit()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createNonShippedJourney()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.NON_SHIPPED_SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createNonShippedIdle()));
	}

}