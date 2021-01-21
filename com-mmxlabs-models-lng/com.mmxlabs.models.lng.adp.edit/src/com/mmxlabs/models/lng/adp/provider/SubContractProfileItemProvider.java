/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.provider;


import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.SubContractProfile;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

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
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.adp.SubContractProfile} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SubContractProfileItemProvider 
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
	public SubContractProfileItemProvider(AdapterFactory adapterFactory) {
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

			addNamePropertyDescriptor(object);
			addContractTypePropertyDescriptor(object);
			addSlotTemplateIdPropertyDescriptor(object);
			addNominatedVesselPropertyDescriptor(object);
			addShippingDaysPropertyDescriptor(object);
			addWindowSizePropertyDescriptor(object);
			addWindowSizeUnitsPropertyDescriptor(object);
			addPortPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SubContractProfile_name_feature"),
				 getString("_UI_SubContractProfile_name_description"),
				 ADPPackage.Literals.SUB_CONTRACT_PROFILE__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Contract Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addContractTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SubContractProfile_contractType_feature"),
				 getString("_UI_SubContractProfile_contractType_description"),
				 ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONTRACT_TYPE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Slot Template Id feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSlotTemplateIdPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SubContractProfile_slotTemplateId_feature"),
				 getString("_UI_SubContractProfile_slotTemplateId_description"),
				 ADPPackage.Literals.SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Nominated Vessel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNominatedVesselPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SubContractProfile_nominatedVessel_feature"),
				 getString("_UI_SubContractProfile_nominatedVessel_description"),
				 ADPPackage.Literals.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Shipping Days feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addShippingDaysPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SubContractProfile_shippingDays_feature"),
				 getString("_UI_SubContractProfile_shippingDays_description"),
				 ADPPackage.Literals.SUB_CONTRACT_PROFILE__SHIPPING_DAYS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Window Size feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowSizePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SubContractProfile_windowSize_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SubContractProfile_windowSize_feature", "_UI_SubContractProfile_type"),
				 ADPPackage.Literals.SUB_CONTRACT_PROFILE__WINDOW_SIZE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Window Size Units feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowSizeUnitsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SubContractProfile_windowSizeUnits_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SubContractProfile_windowSizeUnits_feature", "_UI_SubContractProfile_type"),
				 ADPPackage.Literals.SUB_CONTRACT_PROFILE__WINDOW_SIZE_UNITS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SubContractProfile_port_feature"),
				 getString("_UI_SubContractProfile_port_description"),
				 ADPPackage.Literals.SUB_CONTRACT_PROFILE__PORT,
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
			childrenFeatures.add(ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL);
			childrenFeatures.add(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS);
			childrenFeatures.add(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS);
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
	 * This returns SubContractProfile.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SubContractProfile"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((SubContractProfile<?, ?>)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_SubContractProfile_type") :
			getString("_UI_SubContractProfile_type") + " " + label;
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

		switch (notification.getFeatureID(SubContractProfile.class)) {
			case ADPPackage.SUB_CONTRACT_PROFILE__NAME:
			case ADPPackage.SUB_CONTRACT_PROFILE__CONTRACT_TYPE:
			case ADPPackage.SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID:
			case ADPPackage.SUB_CONTRACT_PROFILE__SHIPPING_DAYS:
			case ADPPackage.SUB_CONTRACT_PROFILE__WINDOW_SIZE:
			case ADPPackage.SUB_CONTRACT_PROFILE__WINDOW_SIZE_UNITS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL:
			case ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS:
			case ADPPackage.SUB_CONTRACT_PROFILE__CONSTRAINTS:
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
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL,
				 ADPFactory.eINSTANCE.createCargoSizeDistributionModel()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL,
				 ADPFactory.eINSTANCE.createCargoNumberDistributionModel()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL,
				 ADPFactory.eINSTANCE.createCargoByQuarterDistributionModel()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL,
				 ADPFactory.eINSTANCE.createCargoIntervalDistributionModel()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL,
				 ADPFactory.eINSTANCE.createPreDefinedDistributionModel()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS,
				 ADPFactory.eINSTANCE.createFlowType()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS,
				 ADPFactory.eINSTANCE.createSupplyFromFlow()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS,
				 ADPFactory.eINSTANCE.createDeliverToFlow()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS,
				 ADPFactory.eINSTANCE.createSupplyFromProfileFlow()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS,
				 ADPFactory.eINSTANCE.createDeliverToProfileFlow()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS,
				 ADPFactory.eINSTANCE.createSupplyFromSpotFlow()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS,
				 ADPFactory.eINSTANCE.createDeliverToSpotFlow()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS,
				 ADPFactory.eINSTANCE.createProfileVesselRestriction()));

		newChildDescriptors.add
			(createChildParameter
				(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS,
				 ADPFactory.eINSTANCE.createShippingOption()));
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
