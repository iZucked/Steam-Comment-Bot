/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResult;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;

import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;
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
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SwapValueMatrixResultItemProvider 
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SwapValueMatrixResultItemProvider(AdapterFactory adapterFactory) {
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

			addBaseDischargePricePropertyDescriptor(object);
			addSwapMarketPricePropertyDescriptor(object);
			addSwapPnlMinusBasePnlPropertyDescriptor(object);
			addBaseLoadPricePropertyDescriptor(object);
			addSwapFobLoadPricePropertyDescriptor(object);
			addBaseFobLoadVolumePropertyDescriptor(object);
			addSwapFobLoadVolumePropertyDescriptor(object);
			addMarketBuyVolumePropertyDescriptor(object);
			addMarketSellVolumePropertyDescriptor(object);
			addBaseDesSellVolumePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Base Discharge Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseDischargePricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_baseDischargePrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_baseDischargePrice_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__BASE_DISCHARGE_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Market Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapMarketPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapMarketPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapMarketPrice_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_MARKET_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Pnl Minus Base Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapPnlMinusBasePnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapPnlMinusBasePnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapPnlMinusBasePnl_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Load Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseLoadPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_baseLoadPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_baseLoadPrice_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__BASE_LOAD_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Fob Load Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapFobLoadPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapFobLoadPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapFobLoadPrice_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Fob Load Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseFobLoadVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_baseFobLoadVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_baseFobLoadVolume_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__BASE_FOB_LOAD_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Fob Load Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapFobLoadVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapFobLoadVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapFobLoadVolume_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Market Buy Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMarketBuyVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_marketBuyVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_marketBuyVolume_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__MARKET_BUY_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Market Sell Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMarketSellVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_marketSellVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_marketSellVolume_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__MARKET_SELL_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Des Sell Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseDesSellVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_baseDesSellVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_baseDesSellVolume_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__BASE_DES_SELL_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns SwapValueMatrixResult.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SwapValueMatrixResult"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((SwapValueMatrixResult)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_SwapValueMatrixResult_type") :
			getString("_UI_SwapValueMatrixResult_type") + " " + label;
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

		switch (notification.getFeatureID(SwapValueMatrixResult.class)) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DISCHARGE_PRICE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_MARKET_PRICE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_LOAD_PRICE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_PRICE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_FOB_LOAD_VOLUME:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_VOLUME:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_BUY_VOLUME:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_SELL_VOLUME:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DES_SELL_VOLUME:
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
