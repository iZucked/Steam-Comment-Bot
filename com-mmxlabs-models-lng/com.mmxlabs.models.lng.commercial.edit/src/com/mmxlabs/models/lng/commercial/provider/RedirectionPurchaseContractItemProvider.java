/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.provider;


import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract} object.
 * <!-- begin-user-doc -->
 * @since 2.0
 * <!-- end-user-doc -->
 * @generated
 */
public class RedirectionPurchaseContractItemProvider
	extends PurchaseContractItemProvider
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
	public RedirectionPurchaseContractItemProvider(AdapterFactory adapterFactory) {
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

			addBaseSalesMarketPortPropertyDescriptor(object);
			addBaseSalesPriceExpressionPropertyDescriptor(object);
			addBasePurchasePriceExpressionPropertyDescriptor(object);
			addNotionalSpeedPropertyDescriptor(object);
			addDesPurchasePortPropertyDescriptor(object);
			addSourcePurchasePortPropertyDescriptor(object);
			addProfitSharePropertyDescriptor(object);
			addVesselClassPropertyDescriptor(object);
			addHireCostPropertyDescriptor(object);
			addDaysFromSourcePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Base Sales Market Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseSalesMarketPortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPurchaseContract_baseSalesMarketPort_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPurchaseContract_baseSalesMarketPort_feature", "_UI_RedirectionPurchaseContract_type"),
				 CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_MARKET_PORT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Sales Price Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseSalesPriceExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPurchaseContract_baseSalesPriceExpression_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPurchaseContract_baseSalesPriceExpression_feature", "_UI_RedirectionPurchaseContract_type"),
				 CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_PRICE_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Purchase Price Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBasePurchasePriceExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPurchaseContract_basePurchasePriceExpression_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPurchaseContract_basePurchasePriceExpression_feature", "_UI_RedirectionPurchaseContract_type"),
				 CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__BASE_PURCHASE_PRICE_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Notional Speed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNotionalSpeedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPurchaseContract_notionalSpeed_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPurchaseContract_notionalSpeed_feature", "_UI_RedirectionPurchaseContract_type"),
				 CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__NOTIONAL_SPEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Des Purchase Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDesPurchasePortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPurchaseContract_desPurchasePort_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPurchaseContract_desPurchasePort_feature", "_UI_RedirectionPurchaseContract_type"),
				 CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__DES_PURCHASE_PORT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Source Purchase Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSourcePurchasePortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPurchaseContract_sourcePurchasePort_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPurchaseContract_sourcePurchasePort_feature", "_UI_RedirectionPurchaseContract_type"),
				 CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__SOURCE_PURCHASE_PORT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Profit Share feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addProfitSharePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPurchaseContract_profitShare_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPurchaseContract_profitShare_feature", "_UI_RedirectionPurchaseContract_type"),
				 CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__PROFIT_SHARE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Vessel Class feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVesselClassPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPurchaseContract_vesselClass_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPurchaseContract_vesselClass_feature", "_UI_RedirectionPurchaseContract_type"),
				 CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__VESSEL_CLASS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Hire Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHireCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPurchaseContract_hireCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPurchaseContract_hireCost_feature", "_UI_RedirectionPurchaseContract_type"),
				 CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__HIRE_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Days From Source feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDaysFromSourcePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPurchaseContract_daysFromSource_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPurchaseContract_daysFromSource_feature", "_UI_RedirectionPurchaseContract_type"),
				 CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT__DAYS_FROM_SOURCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns RedirectionPurchaseContract.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/RedirectionPurchaseContract"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((RedirectionPurchaseContract)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_RedirectionPurchaseContract_type") :
			getString("_UI_RedirectionPurchaseContract_type") + " " + label;
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

		switch (notification.getFeatureID(RedirectionPurchaseContract.class)) {
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_PRICE_EXPRESSION:
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_PURCHASE_PRICE_EXPRESSION:
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__NOTIONAL_SPEED:
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__PROFIT_SHARE:
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__HIRE_COST:
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__DAYS_FROM_SOURCE:
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
