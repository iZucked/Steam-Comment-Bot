/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.cargo.CargoModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CargoModelItemProvider
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoModelItemProvider(AdapterFactory adapterFactory) {
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
			childrenFeatures.add(CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS);
			childrenFeatures.add(CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS);
			childrenFeatures.add(CargoPackage.Literals.CARGO_MODEL__CARGOES);
			childrenFeatures.add(CargoPackage.Literals.CARGO_MODEL__CARGO_GROUPS);
			childrenFeatures.add(CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES);
			childrenFeatures.add(CargoPackage.Literals.CARGO_MODEL__VESSEL_EVENTS);
			childrenFeatures.add(CargoPackage.Literals.CARGO_MODEL__VESSEL_TYPE_GROUPS);
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
	 * This returns CargoModel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CargoModel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((CargoModel)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_CargoModel_type") :
			getString("_UI_CargoModel_type") + " " + label;
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

		switch (notification.getFeatureID(CargoModel.class)) {
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
			case CargoPackage.CARGO_MODEL__CARGOES:
			case CargoPackage.CARGO_MODEL__CARGO_GROUPS:
			case CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES:
			case CargoPackage.CARGO_MODEL__VESSEL_EVENTS:
			case CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS:
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
				(CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS,
				 CargoFactory.eINSTANCE.createLoadSlot()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS,
				 CargoFactory.eINSTANCE.createSpotLoadSlot()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS,
				 CargoFactory.eINSTANCE.createDischargeSlot()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS,
				 CargoFactory.eINSTANCE.createSpotDischargeSlot()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.CARGO_MODEL__CARGOES,
				 CargoFactory.eINSTANCE.createCargo()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.CARGO_MODEL__CARGO_GROUPS,
				 CargoFactory.eINSTANCE.createCargoGroup()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES,
				 CargoFactory.eINSTANCE.createVesselAvailability()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.CARGO_MODEL__VESSEL_EVENTS,
				 CargoFactory.eINSTANCE.createMaintenanceEvent()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.CARGO_MODEL__VESSEL_EVENTS,
				 CargoFactory.eINSTANCE.createDryDockEvent()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.CARGO_MODEL__VESSEL_EVENTS,
				 CargoFactory.eINSTANCE.createCharterOutEvent()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.CARGO_MODEL__VESSEL_TYPE_GROUPS,
				 CargoFactory.eINSTANCE.createVesselTypeGroup()));
	}

}
