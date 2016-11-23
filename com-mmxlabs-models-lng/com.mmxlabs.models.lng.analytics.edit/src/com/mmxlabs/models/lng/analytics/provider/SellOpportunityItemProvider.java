/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.mmxcore.provider.MMXObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.SellOpportunity} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SellOpportunityItemProvider
	extends MMXObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SellOpportunityItemProvider(AdapterFactory adapterFactory) {
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

			addFobSalePropertyDescriptor(object);
			addPortPropertyDescriptor(object);
			addContractPropertyDescriptor(object);
			addDatePropertyDescriptor(object);
			addPriceExpressionPropertyDescriptor(object);
			addEntityPropertyDescriptor(object);
			addCancellationExpressionPropertyDescriptor(object);
			addMiscCostsPropertyDescriptor(object);
			addVolumeModePropertyDescriptor(object);
			addVolumeUnitsPropertyDescriptor(object);
			addMinVolumePropertyDescriptor(object);
			addMaxVolumePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Fob Sale feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFobSalePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SellOpportunity_fobSale_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_fobSale_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__FOB_SALE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
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
				 getString("_UI_SellOpportunity_port_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_port_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__PORT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Contract feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addContractPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SellOpportunity_contract_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_contract_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__CONTRACT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
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
				 getString("_UI_SellOpportunity_date_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_date_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Price Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPriceExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SellOpportunity_priceExpression_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_priceExpression_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__PRICE_EXPRESSION,
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
				 getString("_UI_SellOpportunity_entity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_entity_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__ENTITY,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cancellation Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCancellationExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SellOpportunity_cancellationExpression_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_cancellationExpression_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__CANCELLATION_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Misc Costs feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMiscCostsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SellOpportunity_miscCosts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_miscCosts_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__MISC_COSTS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume Mode feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeModePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SellOpportunity_volumeMode_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_volumeMode_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__VOLUME_MODE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume Units feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeUnitsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SellOpportunity_volumeUnits_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_volumeUnits_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__VOLUME_UNITS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SellOpportunity_minVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_minVolume_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__MIN_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Max Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaxVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SellOpportunity_maxVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SellOpportunity_maxVolume_feature", "_UI_SellOpportunity_type"),
				 AnalyticsPackage.Literals.SELL_OPPORTUNITY__MAX_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns SellOpportunity.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SellOpportunity"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		SellOpportunity sellOpportunity = (SellOpportunity)object;
		return getString("_UI_SellOpportunity_type") + " " + sellOpportunity.isFobSale();
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

		switch (notification.getFeatureID(SellOpportunity.class)) {
			case AnalyticsPackage.SELL_OPPORTUNITY__FOB_SALE:
			case AnalyticsPackage.SELL_OPPORTUNITY__DATE:
			case AnalyticsPackage.SELL_OPPORTUNITY__PRICE_EXPRESSION:
			case AnalyticsPackage.SELL_OPPORTUNITY__CANCELLATION_EXPRESSION:
			case AnalyticsPackage.SELL_OPPORTUNITY__MISC_COSTS:
			case AnalyticsPackage.SELL_OPPORTUNITY__VOLUME_MODE:
			case AnalyticsPackage.SELL_OPPORTUNITY__VOLUME_UNITS:
			case AnalyticsPackage.SELL_OPPORTUNITY__MIN_VOLUME:
			case AnalyticsPackage.SELL_OPPORTUNITY__MAX_VOLUME:
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

}
