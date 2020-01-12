/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CargoChange;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.CargoChange} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CargoChangeItemProvider extends ChangeItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoChangeItemProvider(AdapterFactory adapterFactory) {
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

		}
		return itemPropertyDescriptors;
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
			childrenFeatures.add(AnalyticsPackage.Literals.CARGO_CHANGE__SLOT_DESCRIPTORS);
			childrenFeatures.add(AnalyticsPackage.Literals.CARGO_CHANGE__VESSEL_ALLOCATION);
			childrenFeatures.add(AnalyticsPackage.Literals.CARGO_CHANGE__POSITION);
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
	 * This returns CargoChange.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CargoChange"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		return getString("_UI_CargoChange_type");
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

		switch (notification.getFeatureID(CargoChange.class)) {
			case AnalyticsPackage.CARGO_CHANGE__SLOT_DESCRIPTORS:
			case AnalyticsPackage.CARGO_CHANGE__VESSEL_ALLOCATION:
			case AnalyticsPackage.CARGO_CHANGE__POSITION:
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
				(AnalyticsPackage.Literals.CARGO_CHANGE__SLOT_DESCRIPTORS,
				 AnalyticsFactory.eINSTANCE.createRealSlotDescriptor()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.CARGO_CHANGE__SLOT_DESCRIPTORS,
				 AnalyticsFactory.eINSTANCE.createSpotMarketSlotDescriptor()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.CARGO_CHANGE__VESSEL_ALLOCATION,
				 AnalyticsFactory.eINSTANCE.createVesselAllocationDescriptor()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.CARGO_CHANGE__VESSEL_ALLOCATION,
				 AnalyticsFactory.eINSTANCE.createMarketVesselAllocationDescriptor()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.CARGO_CHANGE__VESSEL_ALLOCATION,
				 AnalyticsFactory.eINSTANCE.createFleetVesselAllocationDescriptor()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.CARGO_CHANGE__POSITION,
				 AnalyticsFactory.eINSTANCE.createPositionDescriptor()));
	}

}
