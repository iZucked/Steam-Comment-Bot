/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.provider;


import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
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
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.commercial.PurchaseContract} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ProfitSharePurchaseContractItemProvider
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
	public ProfitSharePurchaseContractItemProvider(AdapterFactory adapterFactory) {
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

			addBaseMarketPortsPropertyDescriptor(object);
			addBaseMarketIndexPropertyDescriptor(object);
			addBaseMarketMultiplierPropertyDescriptor(object);
			addBaseMarketConstantPropertyDescriptor(object);
			addRefMarketIndexPropertyDescriptor(object);
			addRefMarketMultiplierPropertyDescriptor(object);
			addRefMarketConstantPropertyDescriptor(object);
			addSharePropertyDescriptor(object);
			addMarginPropertyDescriptor(object);
			addSalesMultiplierPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Base Market Ports feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseMarketPortsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfitSharePurchaseContract_baseMarketPorts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfitSharePurchaseContract_baseMarketPorts_feature", "_UI_ProfitSharePurchaseContract_type"),
				 CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_PORTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Market Index feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseMarketIndexPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfitSharePurchaseContract_baseMarketIndex_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfitSharePurchaseContract_baseMarketIndex_feature", "_UI_ProfitSharePurchaseContract_type"),
				 CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_INDEX,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Market Constant feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseMarketConstantPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfitSharePurchaseContract_baseMarketConstant_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfitSharePurchaseContract_baseMarketConstant_feature", "_UI_ProfitSharePurchaseContract_type"),
				 CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_CONSTANT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Market Multiplier feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseMarketMultiplierPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfitSharePurchaseContract_baseMarketMultiplier_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfitSharePurchaseContract_baseMarketMultiplier_feature", "_UI_ProfitSharePurchaseContract_type"),
				 CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_MULTIPLIER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ref Market Index feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRefMarketIndexPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfitSharePurchaseContract_refMarketIndex_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfitSharePurchaseContract_refMarketIndex_feature", "_UI_ProfitSharePurchaseContract_type"),
				 CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_INDEX,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ref Market Constant feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRefMarketConstantPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfitSharePurchaseContract_refMarketConstant_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfitSharePurchaseContract_refMarketConstant_feature", "_UI_ProfitSharePurchaseContract_type"),
				 CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_CONSTANT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ref Market Multiplier feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRefMarketMultiplierPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfitSharePurchaseContract_refMarketMultiplier_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfitSharePurchaseContract_refMarketMultiplier_feature", "_UI_ProfitSharePurchaseContract_type"),
				 CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_MULTIPLIER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Share feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSharePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfitSharePurchaseContract_share_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfitSharePurchaseContract_share_feature", "_UI_ProfitSharePurchaseContract_type"),
				 CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Margin feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMarginPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfitSharePurchaseContract_margin_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfitSharePurchaseContract_margin_feature", "_UI_ProfitSharePurchaseContract_type"),
				 CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__MARGIN,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Sales Multiplier feature.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSalesMultiplierPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfitSharePurchaseContract_salesMultiplier_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfitSharePurchaseContract_salesMultiplier_feature", "_UI_ProfitSharePurchaseContract_type"),
				 CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__SALES_MULTIPLIER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns ProfitSharePurchaseContract.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ProfitSharePurchaseContract"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((PurchaseContract)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_ProfitSharePurchaseContract_type") :
			getString("_UI_ProfitSharePurchaseContract_type") + " " + label;
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

		switch (notification.getFeatureID(PurchaseContract.class)) {
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_MULTIPLIER:
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_CONSTANT:
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_MULTIPLIER:
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_CONSTANT:
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE:
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__MARGIN:
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SALES_MULTIPLIER:
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
