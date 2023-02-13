/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.spotmarkets.SpotMarket} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SpotMarketItemProvider
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketItemProvider(AdapterFactory adapterFactory) {
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
			addEnabledPropertyDescriptor(object);
			addMinQuantityPropertyDescriptor(object);
			addMaxQuantityPropertyDescriptor(object);
			addVolumeLimitsUnitPropertyDescriptor(object);
			addEntityPropertyDescriptor(object);
			addPricingEventPropertyDescriptor(object);
			addRestrictedContractsPropertyDescriptor(object);
			addRestrictedContractsArePermissivePropertyDescriptor(object);
			addRestrictedPortsPropertyDescriptor(object);
			addRestrictedPortsArePermissivePropertyDescriptor(object);
			addRestrictedVesselsPropertyDescriptor(object);
			addRestrictedVesselsArePermissivePropertyDescriptor(object);
			addMtmPropertyDescriptor(object);
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
				 getString("_UI_NamedObject_name_feature"),
				 getString("_UI_NamedObject_name_description"),
				 MMXCorePackage.Literals.NAMED_OBJECT__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Enabled feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEnabledPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_enabled_feature"),
				 getString("_UI_SpotMarket_enabled_description"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__ENABLED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min Quantity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinQuantityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_minQuantity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SpotMarket_minQuantity_feature", "_UI_SpotMarket_type"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__MIN_QUANTITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Max Quantity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaxQuantityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_maxQuantity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SpotMarket_maxQuantity_feature", "_UI_SpotMarket_type"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__MAX_QUANTITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume Limits Unit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeLimitsUnitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_volumeLimitsUnit_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SpotMarket_volumeLimitsUnit_feature", "_UI_SpotMarket_type"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__VOLUME_LIMITS_UNIT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Entity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEntityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_entity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SpotMarket_entity_feature", "_UI_SpotMarket_type"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__ENTITY,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Pricing Event feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPricingEventPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_pricingEvent_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SpotMarket_pricingEvent_feature", "_UI_SpotMarket_type"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__PRICING_EVENT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Ports feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedPortsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_restrictedPorts_feature"),
				 getString("_UI_SpotMarket_restrictedPorts_description"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_PORTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Ports Are Permissive feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedPortsArePermissivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_restrictedPortsArePermissive_feature"),
				 getString("_UI_SpotMarket_restrictedPortsArePermissive_description"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_PORTS_ARE_PERMISSIVE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Vessels feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedVesselsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_restrictedVessels_feature"),
				 getString("_UI_SpotMarket_restrictedVessels_description"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Vessels Are Permissive feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedVesselsArePermissivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_restrictedVesselsArePermissive_feature"),
				 getString("_UI_SpotMarket_restrictedVesselsArePermissive_description"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS_ARE_PERMISSIVE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Contracts feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedContractsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_restrictedContracts_feature"),
				 getString("_UI_SpotMarket_restrictedContracts_description"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_CONTRACTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Contracts Are Permissive feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedContractsArePermissivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_restrictedContractsArePermissive_feature"),
				 getString("_UI_SpotMarket_restrictedContractsArePermissive_description"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_CONTRACTS_ARE_PERMISSIVE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Mtm feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMtmPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SpotMarket_mtm_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SpotMarket_mtm_feature", "_UI_SpotMarket_type"),
				 SpotMarketsPackage.Literals.SPOT_MARKET__MTM,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
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
			childrenFeatures.add(SpotMarketsPackage.Literals.SPOT_MARKET__AVAILABILITY);
			childrenFeatures.add(SpotMarketsPackage.Literals.SPOT_MARKET__PRICE_INFO);
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
		String label = ((SpotMarket)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_SpotMarket_type") :
			getString("_UI_SpotMarket_type") + " " + label;
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

		switch (notification.getFeatureID(SpotMarket.class)) {
			case SpotMarketsPackage.SPOT_MARKET__NAME:
			case SpotMarketsPackage.SPOT_MARKET__ENABLED:
			case SpotMarketsPackage.SPOT_MARKET__MIN_QUANTITY:
			case SpotMarketsPackage.SPOT_MARKET__MAX_QUANTITY:
			case SpotMarketsPackage.SPOT_MARKET__VOLUME_LIMITS_UNIT:
			case SpotMarketsPackage.SPOT_MARKET__PRICING_EVENT:
			case SpotMarketsPackage.SPOT_MARKET__RESTRICTED_CONTRACTS_ARE_PERMISSIVE:
			case SpotMarketsPackage.SPOT_MARKET__RESTRICTED_PORTS_ARE_PERMISSIVE:
			case SpotMarketsPackage.SPOT_MARKET__RESTRICTED_VESSELS_ARE_PERMISSIVE:
			case SpotMarketsPackage.SPOT_MARKET__MTM:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case SpotMarketsPackage.SPOT_MARKET__AVAILABILITY:
			case SpotMarketsPackage.SPOT_MARKET__PRICE_INFO:
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
				(SpotMarketsPackage.Literals.SPOT_MARKET__AVAILABILITY,
				 SpotMarketsFactory.eINSTANCE.createSpotAvailability()));

		newChildDescriptors.add
			(createChildParameter
				(SpotMarketsPackage.Literals.SPOT_MARKET__PRICE_INFO,
				 CommercialFactory.eINSTANCE.createExpressionPriceParameters()));

		newChildDescriptors.add
			(createChildParameter
				(SpotMarketsPackage.Literals.SPOT_MARKET__PRICE_INFO,
				 CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters()));
	}

}
