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
			addBasePrecedingPnlPropertyDescriptor(object);
			addBaseCargoPnlPropertyDescriptor(object);
			addBaseSucceedingPnlPropertyDescriptor(object);
			addSwapPrecedingPnlPropertyDescriptor(object);
			addSwapShippedCargoPnlPropertyDescriptor(object);
			addSwapBackfillCargoPnlPropertyDescriptor(object);
			addSwapSucceedingPnlPropertyDescriptor(object);
			addBasePurchaseCostPropertyDescriptor(object);
			addBaseSaleRevenuePropertyDescriptor(object);
			addBaseShippingCostPropertyDescriptor(object);
			addBaseAdditionalPnlPropertyDescriptor(object);
			addSwapCargoPurchaseCostPropertyDescriptor(object);
			addSwapCargoSaleRevenuePropertyDescriptor(object);
			addSwapCargoShippingCostPropertyDescriptor(object);
			addSwapCargoAdditionalPnlPropertyDescriptor(object);
			addSwapBackfillPurchaseCostPropertyDescriptor(object);
			addSwapBackfillSaleRevenuePropertyDescriptor(object);
			addSwapBackfillAdditionalPnlPropertyDescriptor(object);
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
	 * This adds a property descriptor for the Base Preceding Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBasePrecedingPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_basePrecedingPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_basePrecedingPnl_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Cargo Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseCargoPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_baseCargoPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_baseCargoPnl_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Succeeding Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseSucceedingPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_baseSucceedingPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_baseSucceedingPnl_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Preceding Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapPrecedingPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapPrecedingPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapPrecedingPnl_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Shipped Cargo Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapShippedCargoPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapShippedCargoPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapShippedCargoPnl_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_SHIPPED_CARGO_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Backfill Cargo Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapBackfillCargoPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapBackfillCargoPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapBackfillCargoPnl_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Succeeding Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapSucceedingPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapSucceedingPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapSucceedingPnl_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Purchase Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBasePurchaseCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_basePurchaseCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_basePurchaseCost_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__BASE_PURCHASE_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Sale Revenue feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseSaleRevenuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_baseSaleRevenue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_baseSaleRevenue_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__BASE_SALE_REVENUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Shipping Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseShippingCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_baseShippingCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_baseShippingCost_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__BASE_SHIPPING_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Additional Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseAdditionalPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_baseAdditionalPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_baseAdditionalPnl_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__BASE_ADDITIONAL_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Cargo Purchase Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapCargoPurchaseCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapCargoPurchaseCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapCargoPurchaseCost_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_PURCHASE_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Cargo Sale Revenue feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapCargoSaleRevenuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapCargoSaleRevenue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapCargoSaleRevenue_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SALE_REVENUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Cargo Shipping Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapCargoShippingCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapCargoShippingCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapCargoShippingCost_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SHIPPING_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Cargo Additional Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapCargoAdditionalPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapCargoAdditionalPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapCargoAdditionalPnl_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_ADDITIONAL_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Backfill Purchase Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapBackfillPurchaseCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapBackfillPurchaseCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapBackfillPurchaseCost_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_PURCHASE_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Backfill Sale Revenue feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapBackfillSaleRevenuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapBackfillSaleRevenue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapBackfillSaleRevenue_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_SALE_REVENUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Backfill Additional Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapBackfillAdditionalPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixResult_swapBackfillAdditionalPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixResult_swapBackfillAdditionalPnl_feature", "_UI_SwapValueMatrixResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_ADDITIONAL_PNL,
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO_PNL:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SHIPPED_CARGO_PNL:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO_PNL:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PURCHASE_COST:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SALE_REVENUE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SHIPPING_COST:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_ADDITIONAL_PNL:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_PURCHASE_COST:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SALE_REVENUE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SHIPPING_COST:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_ADDITIONAL_PNL:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_PURCHASE_COST:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_SALE_REVENUE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_ADDITIONAL_PNL:
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
