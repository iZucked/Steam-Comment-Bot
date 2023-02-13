/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResult;

import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.schedule.Schedule;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Swap Value Matrix Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBaseDischargePrice <em>Base Discharge Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapMarketPrice <em>Swap Market Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapPnlMinusBasePnl <em>Swap Pnl Minus Base Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBaseLoadPrice <em>Base Load Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapFobLoadPrice <em>Swap Fob Load Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBaseFobLoadVolume <em>Base Fob Load Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapFobLoadVolume <em>Swap Fob Load Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getMarketBuyVolume <em>Market Buy Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getMarketSellVolume <em>Market Sell Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBaseDesSellVolume <em>Base Des Sell Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBasePrecedingPnl <em>Base Preceding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBaseCargoPnl <em>Base Cargo Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBaseSucceedingPnl <em>Base Succeeding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapPrecedingPnl <em>Swap Preceding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapShippedCargoPnl <em>Swap Shipped Cargo Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapBackfillCargoPnl <em>Swap Backfill Cargo Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapSucceedingPnl <em>Swap Succeeding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBasePurchaseCost <em>Base Purchase Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBaseSaleRevenue <em>Base Sale Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBaseShippingCost <em>Base Shipping Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBaseAdditionalPnl <em>Base Additional Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapCargoPurchaseCost <em>Swap Cargo Purchase Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapCargoSaleRevenue <em>Swap Cargo Sale Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapCargoShippingCost <em>Swap Cargo Shipping Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapCargoAdditionalPnl <em>Swap Cargo Additional Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapBackfillPurchaseCost <em>Swap Backfill Purchase Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapBackfillSaleRevenue <em>Swap Backfill Sale Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapBackfillAdditionalPnl <em>Swap Backfill Additional Pnl</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SwapValueMatrixResultImpl extends UUIDObjectImpl implements SwapValueMatrixResult {
	/**
	 * The default value of the '{@link #getBaseDischargePrice() <em>Base Discharge Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseDischargePrice()
	 * @generated
	 * @ordered
	 */
	protected static final int BASE_DISCHARGE_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBaseDischargePrice() <em>Base Discharge Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseDischargePrice()
	 * @generated
	 * @ordered
	 */
	protected int baseDischargePrice = BASE_DISCHARGE_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapMarketPrice() <em>Swap Market Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapMarketPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int SWAP_MARKET_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSwapMarketPrice() <em>Swap Market Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapMarketPrice()
	 * @generated
	 * @ordered
	 */
	protected int swapMarketPrice = SWAP_MARKET_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapPnlMinusBasePnl() <em>Swap Pnl Minus Base Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapPnlMinusBasePnl()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_PNL_MINUS_BASE_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapPnlMinusBasePnl() <em>Swap Pnl Minus Base Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapPnlMinusBasePnl()
	 * @generated
	 * @ordered
	 */
	protected long swapPnlMinusBasePnl = SWAP_PNL_MINUS_BASE_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseLoadPrice() <em>Base Load Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseLoadPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double BASE_LOAD_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getBaseLoadPrice() <em>Base Load Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseLoadPrice()
	 * @generated
	 * @ordered
	 */
	protected double baseLoadPrice = BASE_LOAD_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapFobLoadPrice() <em>Swap Fob Load Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapFobLoadPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double SWAP_FOB_LOAD_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSwapFobLoadPrice() <em>Swap Fob Load Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapFobLoadPrice()
	 * @generated
	 * @ordered
	 */
	protected double swapFobLoadPrice = SWAP_FOB_LOAD_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseFobLoadVolume() <em>Base Fob Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFobLoadVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int BASE_FOB_LOAD_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBaseFobLoadVolume() <em>Base Fob Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFobLoadVolume()
	 * @generated
	 * @ordered
	 */
	protected int baseFobLoadVolume = BASE_FOB_LOAD_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapFobLoadVolume() <em>Swap Fob Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapFobLoadVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int SWAP_FOB_LOAD_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSwapFobLoadVolume() <em>Swap Fob Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapFobLoadVolume()
	 * @generated
	 * @ordered
	 */
	protected int swapFobLoadVolume = SWAP_FOB_LOAD_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getMarketBuyVolume() <em>Market Buy Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketBuyVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int MARKET_BUY_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMarketBuyVolume() <em>Market Buy Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketBuyVolume()
	 * @generated
	 * @ordered
	 */
	protected int marketBuyVolume = MARKET_BUY_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getMarketSellVolume() <em>Market Sell Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketSellVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int MARKET_SELL_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMarketSellVolume() <em>Market Sell Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketSellVolume()
	 * @generated
	 * @ordered
	 */
	protected int marketSellVolume = MARKET_SELL_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseDesSellVolume() <em>Base Des Sell Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseDesSellVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int BASE_DES_SELL_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBaseDesSellVolume() <em>Base Des Sell Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseDesSellVolume()
	 * @generated
	 * @ordered
	 */
	protected int baseDesSellVolume = BASE_DES_SELL_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getBasePrecedingPnl() <em>Base Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasePrecedingPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long BASE_PRECEDING_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getBasePrecedingPnl() <em>Base Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasePrecedingPnl()
	 * @generated
	 * @ordered
	 */
	protected long basePrecedingPnl = BASE_PRECEDING_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseCargoPnl() <em>Base Cargo Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseCargoPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long BASE_CARGO_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getBaseCargoPnl() <em>Base Cargo Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseCargoPnl()
	 * @generated
	 * @ordered
	 */
	protected long baseCargoPnl = BASE_CARGO_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseSucceedingPnl() <em>Base Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseSucceedingPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long BASE_SUCCEEDING_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getBaseSucceedingPnl() <em>Base Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseSucceedingPnl()
	 * @generated
	 * @ordered
	 */
	protected long baseSucceedingPnl = BASE_SUCCEEDING_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapPrecedingPnl() <em>Swap Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapPrecedingPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_PRECEDING_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapPrecedingPnl() <em>Swap Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapPrecedingPnl()
	 * @generated
	 * @ordered
	 */
	protected long swapPrecedingPnl = SWAP_PRECEDING_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapShippedCargoPnl() <em>Swap Shipped Cargo Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapShippedCargoPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_SHIPPED_CARGO_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapShippedCargoPnl() <em>Swap Shipped Cargo Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapShippedCargoPnl()
	 * @generated
	 * @ordered
	 */
	protected long swapShippedCargoPnl = SWAP_SHIPPED_CARGO_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapBackfillCargoPnl() <em>Swap Backfill Cargo Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapBackfillCargoPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_BACKFILL_CARGO_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapBackfillCargoPnl() <em>Swap Backfill Cargo Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapBackfillCargoPnl()
	 * @generated
	 * @ordered
	 */
	protected long swapBackfillCargoPnl = SWAP_BACKFILL_CARGO_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapSucceedingPnl() <em>Swap Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapSucceedingPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_SUCCEEDING_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapSucceedingPnl() <em>Swap Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapSucceedingPnl()
	 * @generated
	 * @ordered
	 */
	protected long swapSucceedingPnl = SWAP_SUCCEEDING_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getBasePurchaseCost() <em>Base Purchase Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasePurchaseCost()
	 * @generated
	 * @ordered
	 */
	protected static final long BASE_PURCHASE_COST_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getBasePurchaseCost() <em>Base Purchase Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasePurchaseCost()
	 * @generated
	 * @ordered
	 */
	protected long basePurchaseCost = BASE_PURCHASE_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseSaleRevenue() <em>Base Sale Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseSaleRevenue()
	 * @generated
	 * @ordered
	 */
	protected static final long BASE_SALE_REVENUE_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getBaseSaleRevenue() <em>Base Sale Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseSaleRevenue()
	 * @generated
	 * @ordered
	 */
	protected long baseSaleRevenue = BASE_SALE_REVENUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseShippingCost() <em>Base Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseShippingCost()
	 * @generated
	 * @ordered
	 */
	protected static final long BASE_SHIPPING_COST_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getBaseShippingCost() <em>Base Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseShippingCost()
	 * @generated
	 * @ordered
	 */
	protected long baseShippingCost = BASE_SHIPPING_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseAdditionalPnl() <em>Base Additional Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseAdditionalPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long BASE_ADDITIONAL_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getBaseAdditionalPnl() <em>Base Additional Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseAdditionalPnl()
	 * @generated
	 * @ordered
	 */
	protected long baseAdditionalPnl = BASE_ADDITIONAL_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapCargoPurchaseCost() <em>Swap Cargo Purchase Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapCargoPurchaseCost()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_CARGO_PURCHASE_COST_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapCargoPurchaseCost() <em>Swap Cargo Purchase Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapCargoPurchaseCost()
	 * @generated
	 * @ordered
	 */
	protected long swapCargoPurchaseCost = SWAP_CARGO_PURCHASE_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapCargoSaleRevenue() <em>Swap Cargo Sale Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapCargoSaleRevenue()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_CARGO_SALE_REVENUE_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapCargoSaleRevenue() <em>Swap Cargo Sale Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapCargoSaleRevenue()
	 * @generated
	 * @ordered
	 */
	protected long swapCargoSaleRevenue = SWAP_CARGO_SALE_REVENUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapCargoShippingCost() <em>Swap Cargo Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapCargoShippingCost()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_CARGO_SHIPPING_COST_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapCargoShippingCost() <em>Swap Cargo Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapCargoShippingCost()
	 * @generated
	 * @ordered
	 */
	protected long swapCargoShippingCost = SWAP_CARGO_SHIPPING_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapCargoAdditionalPnl() <em>Swap Cargo Additional Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapCargoAdditionalPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_CARGO_ADDITIONAL_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapCargoAdditionalPnl() <em>Swap Cargo Additional Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapCargoAdditionalPnl()
	 * @generated
	 * @ordered
	 */
	protected long swapCargoAdditionalPnl = SWAP_CARGO_ADDITIONAL_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapBackfillPurchaseCost() <em>Swap Backfill Purchase Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapBackfillPurchaseCost()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_BACKFILL_PURCHASE_COST_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapBackfillPurchaseCost() <em>Swap Backfill Purchase Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapBackfillPurchaseCost()
	 * @generated
	 * @ordered
	 */
	protected long swapBackfillPurchaseCost = SWAP_BACKFILL_PURCHASE_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapBackfillSaleRevenue() <em>Swap Backfill Sale Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapBackfillSaleRevenue()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_BACKFILL_SALE_REVENUE_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapBackfillSaleRevenue() <em>Swap Backfill Sale Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapBackfillSaleRevenue()
	 * @generated
	 * @ordered
	 */
	protected long swapBackfillSaleRevenue = SWAP_BACKFILL_SALE_REVENUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapBackfillAdditionalPnl() <em>Swap Backfill Additional Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapBackfillAdditionalPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_BACKFILL_ADDITIONAL_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapBackfillAdditionalPnl() <em>Swap Backfill Additional Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapBackfillAdditionalPnl()
	 * @generated
	 * @ordered
	 */
	protected long swapBackfillAdditionalPnl = SWAP_BACKFILL_ADDITIONAL_PNL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SwapValueMatrixResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getBaseDischargePrice() {
		return baseDischargePrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseDischargePrice(int newBaseDischargePrice) {
		int oldBaseDischargePrice = baseDischargePrice;
		baseDischargePrice = newBaseDischargePrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DISCHARGE_PRICE, oldBaseDischargePrice, baseDischargePrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSwapMarketPrice() {
		return swapMarketPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapMarketPrice(int newSwapMarketPrice) {
		int oldSwapMarketPrice = swapMarketPrice;
		swapMarketPrice = newSwapMarketPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_MARKET_PRICE, oldSwapMarketPrice, swapMarketPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapPnlMinusBasePnl() {
		return swapPnlMinusBasePnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapPnlMinusBasePnl(long newSwapPnlMinusBasePnl) {
		long oldSwapPnlMinusBasePnl = swapPnlMinusBasePnl;
		swapPnlMinusBasePnl = newSwapPnlMinusBasePnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL, oldSwapPnlMinusBasePnl, swapPnlMinusBasePnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getBaseLoadPrice() {
		return baseLoadPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseLoadPrice(double newBaseLoadPrice) {
		double oldBaseLoadPrice = baseLoadPrice;
		baseLoadPrice = newBaseLoadPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_LOAD_PRICE, oldBaseLoadPrice, baseLoadPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getSwapFobLoadPrice() {
		return swapFobLoadPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapFobLoadPrice(double newSwapFobLoadPrice) {
		double oldSwapFobLoadPrice = swapFobLoadPrice;
		swapFobLoadPrice = newSwapFobLoadPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_PRICE, oldSwapFobLoadPrice, swapFobLoadPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getBaseFobLoadVolume() {
		return baseFobLoadVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseFobLoadVolume(int newBaseFobLoadVolume) {
		int oldBaseFobLoadVolume = baseFobLoadVolume;
		baseFobLoadVolume = newBaseFobLoadVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_FOB_LOAD_VOLUME, oldBaseFobLoadVolume, baseFobLoadVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSwapFobLoadVolume() {
		return swapFobLoadVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapFobLoadVolume(int newSwapFobLoadVolume) {
		int oldSwapFobLoadVolume = swapFobLoadVolume;
		swapFobLoadVolume = newSwapFobLoadVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_VOLUME, oldSwapFobLoadVolume, swapFobLoadVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMarketBuyVolume() {
		return marketBuyVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarketBuyVolume(int newMarketBuyVolume) {
		int oldMarketBuyVolume = marketBuyVolume;
		marketBuyVolume = newMarketBuyVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_BUY_VOLUME, oldMarketBuyVolume, marketBuyVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMarketSellVolume() {
		return marketSellVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarketSellVolume(int newMarketSellVolume) {
		int oldMarketSellVolume = marketSellVolume;
		marketSellVolume = newMarketSellVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_SELL_VOLUME, oldMarketSellVolume, marketSellVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getBaseDesSellVolume() {
		return baseDesSellVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseDesSellVolume(int newBaseDesSellVolume) {
		int oldBaseDesSellVolume = baseDesSellVolume;
		baseDesSellVolume = newBaseDesSellVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DES_SELL_VOLUME, oldBaseDesSellVolume, baseDesSellVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getBasePrecedingPnl() {
		return basePrecedingPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBasePrecedingPnl(long newBasePrecedingPnl) {
		long oldBasePrecedingPnl = basePrecedingPnl;
		basePrecedingPnl = newBasePrecedingPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL, oldBasePrecedingPnl, basePrecedingPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getBaseCargoPnl() {
		return baseCargoPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseCargoPnl(long newBaseCargoPnl) {
		long oldBaseCargoPnl = baseCargoPnl;
		baseCargoPnl = newBaseCargoPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO_PNL, oldBaseCargoPnl, baseCargoPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getBaseSucceedingPnl() {
		return baseSucceedingPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseSucceedingPnl(long newBaseSucceedingPnl) {
		long oldBaseSucceedingPnl = baseSucceedingPnl;
		baseSucceedingPnl = newBaseSucceedingPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL, oldBaseSucceedingPnl, baseSucceedingPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapPrecedingPnl() {
		return swapPrecedingPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapPrecedingPnl(long newSwapPrecedingPnl) {
		long oldSwapPrecedingPnl = swapPrecedingPnl;
		swapPrecedingPnl = newSwapPrecedingPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL, oldSwapPrecedingPnl, swapPrecedingPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapShippedCargoPnl() {
		return swapShippedCargoPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapShippedCargoPnl(long newSwapShippedCargoPnl) {
		long oldSwapShippedCargoPnl = swapShippedCargoPnl;
		swapShippedCargoPnl = newSwapShippedCargoPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SHIPPED_CARGO_PNL, oldSwapShippedCargoPnl, swapShippedCargoPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapBackfillCargoPnl() {
		return swapBackfillCargoPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapBackfillCargoPnl(long newSwapBackfillCargoPnl) {
		long oldSwapBackfillCargoPnl = swapBackfillCargoPnl;
		swapBackfillCargoPnl = newSwapBackfillCargoPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO_PNL, oldSwapBackfillCargoPnl, swapBackfillCargoPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapSucceedingPnl() {
		return swapSucceedingPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapSucceedingPnl(long newSwapSucceedingPnl) {
		long oldSwapSucceedingPnl = swapSucceedingPnl;
		swapSucceedingPnl = newSwapSucceedingPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL, oldSwapSucceedingPnl, swapSucceedingPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getBasePurchaseCost() {
		return basePurchaseCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBasePurchaseCost(long newBasePurchaseCost) {
		long oldBasePurchaseCost = basePurchaseCost;
		basePurchaseCost = newBasePurchaseCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PURCHASE_COST, oldBasePurchaseCost, basePurchaseCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getBaseSaleRevenue() {
		return baseSaleRevenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseSaleRevenue(long newBaseSaleRevenue) {
		long oldBaseSaleRevenue = baseSaleRevenue;
		baseSaleRevenue = newBaseSaleRevenue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SALE_REVENUE, oldBaseSaleRevenue, baseSaleRevenue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getBaseShippingCost() {
		return baseShippingCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseShippingCost(long newBaseShippingCost) {
		long oldBaseShippingCost = baseShippingCost;
		baseShippingCost = newBaseShippingCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SHIPPING_COST, oldBaseShippingCost, baseShippingCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getBaseAdditionalPnl() {
		return baseAdditionalPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseAdditionalPnl(long newBaseAdditionalPnl) {
		long oldBaseAdditionalPnl = baseAdditionalPnl;
		baseAdditionalPnl = newBaseAdditionalPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_ADDITIONAL_PNL, oldBaseAdditionalPnl, baseAdditionalPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapCargoPurchaseCost() {
		return swapCargoPurchaseCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapCargoPurchaseCost(long newSwapCargoPurchaseCost) {
		long oldSwapCargoPurchaseCost = swapCargoPurchaseCost;
		swapCargoPurchaseCost = newSwapCargoPurchaseCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_PURCHASE_COST, oldSwapCargoPurchaseCost, swapCargoPurchaseCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapCargoSaleRevenue() {
		return swapCargoSaleRevenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapCargoSaleRevenue(long newSwapCargoSaleRevenue) {
		long oldSwapCargoSaleRevenue = swapCargoSaleRevenue;
		swapCargoSaleRevenue = newSwapCargoSaleRevenue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SALE_REVENUE, oldSwapCargoSaleRevenue, swapCargoSaleRevenue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapCargoShippingCost() {
		return swapCargoShippingCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapCargoShippingCost(long newSwapCargoShippingCost) {
		long oldSwapCargoShippingCost = swapCargoShippingCost;
		swapCargoShippingCost = newSwapCargoShippingCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SHIPPING_COST, oldSwapCargoShippingCost, swapCargoShippingCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapCargoAdditionalPnl() {
		return swapCargoAdditionalPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapCargoAdditionalPnl(long newSwapCargoAdditionalPnl) {
		long oldSwapCargoAdditionalPnl = swapCargoAdditionalPnl;
		swapCargoAdditionalPnl = newSwapCargoAdditionalPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_ADDITIONAL_PNL, oldSwapCargoAdditionalPnl, swapCargoAdditionalPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapBackfillPurchaseCost() {
		return swapBackfillPurchaseCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapBackfillPurchaseCost(long newSwapBackfillPurchaseCost) {
		long oldSwapBackfillPurchaseCost = swapBackfillPurchaseCost;
		swapBackfillPurchaseCost = newSwapBackfillPurchaseCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_PURCHASE_COST, oldSwapBackfillPurchaseCost, swapBackfillPurchaseCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapBackfillSaleRevenue() {
		return swapBackfillSaleRevenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapBackfillSaleRevenue(long newSwapBackfillSaleRevenue) {
		long oldSwapBackfillSaleRevenue = swapBackfillSaleRevenue;
		swapBackfillSaleRevenue = newSwapBackfillSaleRevenue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_SALE_REVENUE, oldSwapBackfillSaleRevenue, swapBackfillSaleRevenue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapBackfillAdditionalPnl() {
		return swapBackfillAdditionalPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapBackfillAdditionalPnl(long newSwapBackfillAdditionalPnl) {
		long oldSwapBackfillAdditionalPnl = swapBackfillAdditionalPnl;
		swapBackfillAdditionalPnl = newSwapBackfillAdditionalPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_ADDITIONAL_PNL, oldSwapBackfillAdditionalPnl, swapBackfillAdditionalPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DISCHARGE_PRICE:
				return getBaseDischargePrice();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_MARKET_PRICE:
				return getSwapMarketPrice();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL:
				return getSwapPnlMinusBasePnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_LOAD_PRICE:
				return getBaseLoadPrice();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_PRICE:
				return getSwapFobLoadPrice();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_FOB_LOAD_VOLUME:
				return getBaseFobLoadVolume();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_VOLUME:
				return getSwapFobLoadVolume();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_BUY_VOLUME:
				return getMarketBuyVolume();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_SELL_VOLUME:
				return getMarketSellVolume();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DES_SELL_VOLUME:
				return getBaseDesSellVolume();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL:
				return getBasePrecedingPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO_PNL:
				return getBaseCargoPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL:
				return getBaseSucceedingPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL:
				return getSwapPrecedingPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SHIPPED_CARGO_PNL:
				return getSwapShippedCargoPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO_PNL:
				return getSwapBackfillCargoPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL:
				return getSwapSucceedingPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PURCHASE_COST:
				return getBasePurchaseCost();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SALE_REVENUE:
				return getBaseSaleRevenue();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SHIPPING_COST:
				return getBaseShippingCost();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_ADDITIONAL_PNL:
				return getBaseAdditionalPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_PURCHASE_COST:
				return getSwapCargoPurchaseCost();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SALE_REVENUE:
				return getSwapCargoSaleRevenue();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SHIPPING_COST:
				return getSwapCargoShippingCost();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_ADDITIONAL_PNL:
				return getSwapCargoAdditionalPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_PURCHASE_COST:
				return getSwapBackfillPurchaseCost();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_SALE_REVENUE:
				return getSwapBackfillSaleRevenue();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_ADDITIONAL_PNL:
				return getSwapBackfillAdditionalPnl();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DISCHARGE_PRICE:
				setBaseDischargePrice((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_MARKET_PRICE:
				setSwapMarketPrice((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL:
				setSwapPnlMinusBasePnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_LOAD_PRICE:
				setBaseLoadPrice((Double)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_PRICE:
				setSwapFobLoadPrice((Double)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_FOB_LOAD_VOLUME:
				setBaseFobLoadVolume((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_VOLUME:
				setSwapFobLoadVolume((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_BUY_VOLUME:
				setMarketBuyVolume((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_SELL_VOLUME:
				setMarketSellVolume((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DES_SELL_VOLUME:
				setBaseDesSellVolume((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL:
				setBasePrecedingPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO_PNL:
				setBaseCargoPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL:
				setBaseSucceedingPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL:
				setSwapPrecedingPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SHIPPED_CARGO_PNL:
				setSwapShippedCargoPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO_PNL:
				setSwapBackfillCargoPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL:
				setSwapSucceedingPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PURCHASE_COST:
				setBasePurchaseCost((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SALE_REVENUE:
				setBaseSaleRevenue((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SHIPPING_COST:
				setBaseShippingCost((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_ADDITIONAL_PNL:
				setBaseAdditionalPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_PURCHASE_COST:
				setSwapCargoPurchaseCost((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SALE_REVENUE:
				setSwapCargoSaleRevenue((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SHIPPING_COST:
				setSwapCargoShippingCost((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_ADDITIONAL_PNL:
				setSwapCargoAdditionalPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_PURCHASE_COST:
				setSwapBackfillPurchaseCost((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_SALE_REVENUE:
				setSwapBackfillSaleRevenue((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_ADDITIONAL_PNL:
				setSwapBackfillAdditionalPnl((Long)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DISCHARGE_PRICE:
				setBaseDischargePrice(BASE_DISCHARGE_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_MARKET_PRICE:
				setSwapMarketPrice(SWAP_MARKET_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL:
				setSwapPnlMinusBasePnl(SWAP_PNL_MINUS_BASE_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_LOAD_PRICE:
				setBaseLoadPrice(BASE_LOAD_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_PRICE:
				setSwapFobLoadPrice(SWAP_FOB_LOAD_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_FOB_LOAD_VOLUME:
				setBaseFobLoadVolume(BASE_FOB_LOAD_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_VOLUME:
				setSwapFobLoadVolume(SWAP_FOB_LOAD_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_BUY_VOLUME:
				setMarketBuyVolume(MARKET_BUY_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_SELL_VOLUME:
				setMarketSellVolume(MARKET_SELL_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DES_SELL_VOLUME:
				setBaseDesSellVolume(BASE_DES_SELL_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL:
				setBasePrecedingPnl(BASE_PRECEDING_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO_PNL:
				setBaseCargoPnl(BASE_CARGO_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL:
				setBaseSucceedingPnl(BASE_SUCCEEDING_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL:
				setSwapPrecedingPnl(SWAP_PRECEDING_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SHIPPED_CARGO_PNL:
				setSwapShippedCargoPnl(SWAP_SHIPPED_CARGO_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO_PNL:
				setSwapBackfillCargoPnl(SWAP_BACKFILL_CARGO_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL:
				setSwapSucceedingPnl(SWAP_SUCCEEDING_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PURCHASE_COST:
				setBasePurchaseCost(BASE_PURCHASE_COST_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SALE_REVENUE:
				setBaseSaleRevenue(BASE_SALE_REVENUE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SHIPPING_COST:
				setBaseShippingCost(BASE_SHIPPING_COST_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_ADDITIONAL_PNL:
				setBaseAdditionalPnl(BASE_ADDITIONAL_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_PURCHASE_COST:
				setSwapCargoPurchaseCost(SWAP_CARGO_PURCHASE_COST_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SALE_REVENUE:
				setSwapCargoSaleRevenue(SWAP_CARGO_SALE_REVENUE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SHIPPING_COST:
				setSwapCargoShippingCost(SWAP_CARGO_SHIPPING_COST_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_ADDITIONAL_PNL:
				setSwapCargoAdditionalPnl(SWAP_CARGO_ADDITIONAL_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_PURCHASE_COST:
				setSwapBackfillPurchaseCost(SWAP_BACKFILL_PURCHASE_COST_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_SALE_REVENUE:
				setSwapBackfillSaleRevenue(SWAP_BACKFILL_SALE_REVENUE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_ADDITIONAL_PNL:
				setSwapBackfillAdditionalPnl(SWAP_BACKFILL_ADDITIONAL_PNL_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DISCHARGE_PRICE:
				return baseDischargePrice != BASE_DISCHARGE_PRICE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_MARKET_PRICE:
				return swapMarketPrice != SWAP_MARKET_PRICE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL:
				return swapPnlMinusBasePnl != SWAP_PNL_MINUS_BASE_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_LOAD_PRICE:
				return baseLoadPrice != BASE_LOAD_PRICE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_PRICE:
				return swapFobLoadPrice != SWAP_FOB_LOAD_PRICE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_FOB_LOAD_VOLUME:
				return baseFobLoadVolume != BASE_FOB_LOAD_VOLUME_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_VOLUME:
				return swapFobLoadVolume != SWAP_FOB_LOAD_VOLUME_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_BUY_VOLUME:
				return marketBuyVolume != MARKET_BUY_VOLUME_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__MARKET_SELL_VOLUME:
				return marketSellVolume != MARKET_SELL_VOLUME_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_DES_SELL_VOLUME:
				return baseDesSellVolume != BASE_DES_SELL_VOLUME_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL:
				return basePrecedingPnl != BASE_PRECEDING_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO_PNL:
				return baseCargoPnl != BASE_CARGO_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL:
				return baseSucceedingPnl != BASE_SUCCEEDING_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL:
				return swapPrecedingPnl != SWAP_PRECEDING_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SHIPPED_CARGO_PNL:
				return swapShippedCargoPnl != SWAP_SHIPPED_CARGO_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO_PNL:
				return swapBackfillCargoPnl != SWAP_BACKFILL_CARGO_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL:
				return swapSucceedingPnl != SWAP_SUCCEEDING_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PURCHASE_COST:
				return basePurchaseCost != BASE_PURCHASE_COST_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SALE_REVENUE:
				return baseSaleRevenue != BASE_SALE_REVENUE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SHIPPING_COST:
				return baseShippingCost != BASE_SHIPPING_COST_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_ADDITIONAL_PNL:
				return baseAdditionalPnl != BASE_ADDITIONAL_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_PURCHASE_COST:
				return swapCargoPurchaseCost != SWAP_CARGO_PURCHASE_COST_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SALE_REVENUE:
				return swapCargoSaleRevenue != SWAP_CARGO_SALE_REVENUE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_SHIPPING_COST:
				return swapCargoShippingCost != SWAP_CARGO_SHIPPING_COST_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_CARGO_ADDITIONAL_PNL:
				return swapCargoAdditionalPnl != SWAP_CARGO_ADDITIONAL_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_PURCHASE_COST:
				return swapBackfillPurchaseCost != SWAP_BACKFILL_PURCHASE_COST_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_SALE_REVENUE:
				return swapBackfillSaleRevenue != SWAP_BACKFILL_SALE_REVENUE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_ADDITIONAL_PNL:
				return swapBackfillAdditionalPnl != SWAP_BACKFILL_ADDITIONAL_PNL_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (baseDischargePrice: ");
		result.append(baseDischargePrice);
		result.append(", swapMarketPrice: ");
		result.append(swapMarketPrice);
		result.append(", swapPnlMinusBasePnl: ");
		result.append(swapPnlMinusBasePnl);
		result.append(", baseLoadPrice: ");
		result.append(baseLoadPrice);
		result.append(", swapFobLoadPrice: ");
		result.append(swapFobLoadPrice);
		result.append(", baseFobLoadVolume: ");
		result.append(baseFobLoadVolume);
		result.append(", swapFobLoadVolume: ");
		result.append(swapFobLoadVolume);
		result.append(", marketBuyVolume: ");
		result.append(marketBuyVolume);
		result.append(", marketSellVolume: ");
		result.append(marketSellVolume);
		result.append(", baseDesSellVolume: ");
		result.append(baseDesSellVolume);
		result.append(", basePrecedingPnl: ");
		result.append(basePrecedingPnl);
		result.append(", baseCargoPnl: ");
		result.append(baseCargoPnl);
		result.append(", baseSucceedingPnl: ");
		result.append(baseSucceedingPnl);
		result.append(", swapPrecedingPnl: ");
		result.append(swapPrecedingPnl);
		result.append(", swapShippedCargoPnl: ");
		result.append(swapShippedCargoPnl);
		result.append(", swapBackfillCargoPnl: ");
		result.append(swapBackfillCargoPnl);
		result.append(", swapSucceedingPnl: ");
		result.append(swapSucceedingPnl);
		result.append(", basePurchaseCost: ");
		result.append(basePurchaseCost);
		result.append(", baseSaleRevenue: ");
		result.append(baseSaleRevenue);
		result.append(", baseShippingCost: ");
		result.append(baseShippingCost);
		result.append(", baseAdditionalPnl: ");
		result.append(baseAdditionalPnl);
		result.append(", swapCargoPurchaseCost: ");
		result.append(swapCargoPurchaseCost);
		result.append(", swapCargoSaleRevenue: ");
		result.append(swapCargoSaleRevenue);
		result.append(", swapCargoShippingCost: ");
		result.append(swapCargoShippingCost);
		result.append(", swapCargoAdditionalPnl: ");
		result.append(swapCargoAdditionalPnl);
		result.append(", swapBackfillPurchaseCost: ");
		result.append(swapBackfillPurchaseCost);
		result.append(", swapBackfillSaleRevenue: ");
		result.append(swapBackfillSaleRevenue);
		result.append(", swapBackfillAdditionalPnl: ");
		result.append(swapBackfillAdditionalPnl);
		result.append(')');
		return result.toString();
	}

} //SwapValueMatrixResultImpl
