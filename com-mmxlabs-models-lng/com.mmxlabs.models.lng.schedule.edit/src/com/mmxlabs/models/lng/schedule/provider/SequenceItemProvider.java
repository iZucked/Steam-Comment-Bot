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

import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.mmxcore.provider.MMXObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.schedule.Sequence} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SequenceItemProvider
	extends MMXObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SequenceItemProvider(AdapterFactory adapterFactory) {
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

			addVesselAvailabilityPropertyDescriptor(object);
			addCharterInMarketPropertyDescriptor(object);
			addSpotIndexPropertyDescriptor(object);
			addSequenceTypePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Vessel Availability feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVesselAvailabilityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Sequence_vesselAvailability_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Sequence_vesselAvailability_feature", "_UI_Sequence_type"),
				 SchedulePackage.Literals.SEQUENCE__VESSEL_AVAILABILITY,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Charter In Market feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCharterInMarketPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Sequence_charterInMarket_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Sequence_charterInMarket_feature", "_UI_Sequence_type"),
				 SchedulePackage.Literals.SEQUENCE__CHARTER_IN_MARKET,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Spot Index feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSpotIndexPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Sequence_spotIndex_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Sequence_spotIndex_feature", "_UI_Sequence_type"),
				 SchedulePackage.Literals.SEQUENCE__SPOT_INDEX,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Sequence Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSequenceTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Sequence_sequenceType_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Sequence_sequenceType_feature", "_UI_Sequence_type"),
				 SchedulePackage.Literals.SEQUENCE__SEQUENCE_TYPE,
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
			childrenFeatures.add(SchedulePackage.Literals.SEQUENCE__EVENTS);
			childrenFeatures.add(SchedulePackage.Literals.SEQUENCE__FITNESSES);
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
	 * This returns Sequence.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Sequence"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		Sequence sequence = (Sequence)object;
		String text = getString("_UI_Sequence_type") + " " + sequence.getName();
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

		switch (notification.getFeatureID(Sequence.class)) {
			case SchedulePackage.SEQUENCE__SPOT_INDEX:
			case SchedulePackage.SEQUENCE__SEQUENCE_TYPE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case SchedulePackage.SEQUENCE__EVENTS:
			case SchedulePackage.SEQUENCE__FITNESSES:
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
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createStartEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createEndEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createJourney()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createIdle()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createPortVisit()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createSlotVisit()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createVesselEventVisit()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createGeneratedCharterOut()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 ScheduleFactory.eINSTANCE.createCooldown()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__FITNESSES,
				 ScheduleFactory.eINSTANCE.createFitness()));
	}

}
