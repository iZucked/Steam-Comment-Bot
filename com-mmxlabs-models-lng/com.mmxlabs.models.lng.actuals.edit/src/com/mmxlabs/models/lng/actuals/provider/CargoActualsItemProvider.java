/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.actuals.ActualsFactory;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.actuals.CargoActuals} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CargoActualsItemProvider
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
	public CargoActualsItemProvider(AdapterFactory adapterFactory) {
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

			addContractYearPropertyDescriptor(object);
			addOperationNumberPropertyDescriptor(object);
			addSubOperationNumberPropertyDescriptor(object);
			addSellerIDPropertyDescriptor(object);
			addCargoReferencePropertyDescriptor(object);
			addCargoReferenceSellerPropertyDescriptor(object);
			addVesselPropertyDescriptor(object);
			addCharterRatePerDayPropertyDescriptor(object);
			addCargoPropertyDescriptor(object);
			addBaseFuelPricePropertyDescriptor(object);
			addInsurancePremiumPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Contract Year feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addContractYearPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoActuals_contractYear_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoActuals_contractYear_feature", "_UI_CargoActuals_type"),
				 ActualsPackage.Literals.CARGO_ACTUALS__CONTRACT_YEAR,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Operation Number feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOperationNumberPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoActuals_operationNumber_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoActuals_operationNumber_feature", "_UI_CargoActuals_type"),
				 ActualsPackage.Literals.CARGO_ACTUALS__OPERATION_NUMBER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Sub Operation Number feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSubOperationNumberPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoActuals_subOperationNumber_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoActuals_subOperationNumber_feature", "_UI_CargoActuals_type"),
				 ActualsPackage.Literals.CARGO_ACTUALS__SUB_OPERATION_NUMBER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Seller ID feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSellerIDPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoActuals_sellerID_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoActuals_sellerID_feature", "_UI_CargoActuals_type"),
				 ActualsPackage.Literals.CARGO_ACTUALS__SELLER_ID,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cargo Reference feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCargoReferencePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoActuals_cargoReference_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoActuals_cargoReference_feature", "_UI_CargoActuals_type"),
				 ActualsPackage.Literals.CARGO_ACTUALS__CARGO_REFERENCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cargo Reference Seller feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCargoReferenceSellerPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoActuals_cargoReferenceSeller_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoActuals_cargoReferenceSeller_feature", "_UI_CargoActuals_type"),
				 ActualsPackage.Literals.CARGO_ACTUALS__CARGO_REFERENCE_SELLER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
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
				 getString("_UI_CargoActuals_vessel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoActuals_vessel_feature", "_UI_CargoActuals_type"),
				 ActualsPackage.Literals.CARGO_ACTUALS__VESSEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Charter Rate Per Day feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCharterRatePerDayPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoActuals_charterRatePerDay_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoActuals_charterRatePerDay_feature", "_UI_CargoActuals_type"),
				 ActualsPackage.Literals.CARGO_ACTUALS__CHARTER_RATE_PER_DAY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cargo feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCargoPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoActuals_cargo_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoActuals_cargo_feature", "_UI_CargoActuals_type"),
				 ActualsPackage.Literals.CARGO_ACTUALS__CARGO,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Fuel Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseFuelPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoActuals_baseFuelPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoActuals_baseFuelPrice_feature", "_UI_CargoActuals_type"),
				 ActualsPackage.Literals.CARGO_ACTUALS__BASE_FUEL_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Insurance Premium feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInsurancePremiumPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoActuals_insurancePremium_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoActuals_insurancePremium_feature", "_UI_CargoActuals_type"),
				 ActualsPackage.Literals.CARGO_ACTUALS__INSURANCE_PREMIUM,
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
			childrenFeatures.add(ActualsPackage.Literals.CARGO_ACTUALS__ACTUALS);
			childrenFeatures.add(ActualsPackage.Literals.CARGO_ACTUALS__RETURN_ACTUALS);
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
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		CargoActuals cargoActuals = (CargoActuals)object;
		return getString("_UI_CargoActuals_type") + " " + cargoActuals.getContractYear();
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

		switch (notification.getFeatureID(CargoActuals.class)) {
			case ActualsPackage.CARGO_ACTUALS__CONTRACT_YEAR:
			case ActualsPackage.CARGO_ACTUALS__OPERATION_NUMBER:
			case ActualsPackage.CARGO_ACTUALS__SUB_OPERATION_NUMBER:
			case ActualsPackage.CARGO_ACTUALS__SELLER_ID:
			case ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE:
			case ActualsPackage.CARGO_ACTUALS__CARGO_REFERENCE_SELLER:
			case ActualsPackage.CARGO_ACTUALS__CHARTER_RATE_PER_DAY:
			case ActualsPackage.CARGO_ACTUALS__BASE_FUEL_PRICE:
			case ActualsPackage.CARGO_ACTUALS__INSURANCE_PREMIUM:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ActualsPackage.CARGO_ACTUALS__ACTUALS:
			case ActualsPackage.CARGO_ACTUALS__RETURN_ACTUALS:
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
				(ActualsPackage.Literals.CARGO_ACTUALS__ACTUALS,
				 ActualsFactory.eINSTANCE.createLoadActuals()));

		newChildDescriptors.add
			(createChildParameter
				(ActualsPackage.Literals.CARGO_ACTUALS__ACTUALS,
				 ActualsFactory.eINSTANCE.createDischargeActuals()));

		newChildDescriptors.add
			(createChildParameter
				(ActualsPackage.Literals.CARGO_ACTUALS__RETURN_ACTUALS,
				 ActualsFactory.eINSTANCE.createReturnActuals()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return ActualsEditPlugin.INSTANCE;
	}

}
