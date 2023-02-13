/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.schedule.Schedule;

import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Swap Value Matrix Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseDischargePrice <em>Base Discharge Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapMarketPrice <em>Swap Market Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPnlMinusBasePnl <em>Swap Pnl Minus Base Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseLoadPrice <em>Base Load Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapFobLoadPrice <em>Swap Fob Load Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseFobLoadVolume <em>Base Fob Load Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapFobLoadVolume <em>Swap Fob Load Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getMarketBuyVolume <em>Market Buy Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getMarketSellVolume <em>Market Sell Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseDesSellVolume <em>Base Des Sell Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBasePrecedingPnl <em>Base Preceding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseCargoPnl <em>Base Cargo Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseSucceedingPnl <em>Base Succeeding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPrecedingPnl <em>Swap Preceding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapShippedCargoPnl <em>Swap Shipped Cargo Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillCargoPnl <em>Swap Backfill Cargo Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapSucceedingPnl <em>Swap Succeeding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBasePurchaseCost <em>Base Purchase Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseSaleRevenue <em>Base Sale Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseShippingCost <em>Base Shipping Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseAdditionalPnl <em>Base Additional Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapCargoPurchaseCost <em>Swap Cargo Purchase Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapCargoSaleRevenue <em>Swap Cargo Sale Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapCargoShippingCost <em>Swap Cargo Shipping Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapCargoAdditionalPnl <em>Swap Cargo Additional Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillPurchaseCost <em>Swap Backfill Purchase Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillSaleRevenue <em>Swap Backfill Sale Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillAdditionalPnl <em>Swap Backfill Additional Pnl</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult()
 * @model
 * @generated
 */
public interface SwapValueMatrixResult extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Base Discharge Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Discharge Price</em>' attribute.
	 * @see #setBaseDischargePrice(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BaseDischargePrice()
	 * @model
	 * @generated
	 */
	int getBaseDischargePrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseDischargePrice <em>Base Discharge Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Discharge Price</em>' attribute.
	 * @see #getBaseDischargePrice()
	 * @generated
	 */
	void setBaseDischargePrice(int value);

	/**
	 * Returns the value of the '<em><b>Swap Market Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Market Price</em>' attribute.
	 * @see #setSwapMarketPrice(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapMarketPrice()
	 * @model
	 * @generated
	 */
	int getSwapMarketPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapMarketPrice <em>Swap Market Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Market Price</em>' attribute.
	 * @see #getSwapMarketPrice()
	 * @generated
	 */
	void setSwapMarketPrice(int value);

	/**
	 * Returns the value of the '<em><b>Swap Pnl Minus Base Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Pnl Minus Base Pnl</em>' attribute.
	 * @see #setSwapPnlMinusBasePnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapPnlMinusBasePnl()
	 * @model
	 * @generated
	 */
	long getSwapPnlMinusBasePnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPnlMinusBasePnl <em>Swap Pnl Minus Base Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Pnl Minus Base Pnl</em>' attribute.
	 * @see #getSwapPnlMinusBasePnl()
	 * @generated
	 */
	void setSwapPnlMinusBasePnl(long value);

	/**
	 * Returns the value of the '<em><b>Base Load Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Load Price</em>' attribute.
	 * @see #setBaseLoadPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BaseLoadPrice()
	 * @model
	 * @generated
	 */
	double getBaseLoadPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseLoadPrice <em>Base Load Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Load Price</em>' attribute.
	 * @see #getBaseLoadPrice()
	 * @generated
	 */
	void setBaseLoadPrice(double value);

	/**
	 * Returns the value of the '<em><b>Swap Fob Load Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Fob Load Price</em>' attribute.
	 * @see #setSwapFobLoadPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapFobLoadPrice()
	 * @model
	 * @generated
	 */
	double getSwapFobLoadPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapFobLoadPrice <em>Swap Fob Load Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Fob Load Price</em>' attribute.
	 * @see #getSwapFobLoadPrice()
	 * @generated
	 */
	void setSwapFobLoadPrice(double value);

	/**
	 * Returns the value of the '<em><b>Base Fob Load Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fob Load Volume</em>' attribute.
	 * @see #setBaseFobLoadVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BaseFobLoadVolume()
	 * @model
	 * @generated
	 */
	int getBaseFobLoadVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseFobLoadVolume <em>Base Fob Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fob Load Volume</em>' attribute.
	 * @see #getBaseFobLoadVolume()
	 * @generated
	 */
	void setBaseFobLoadVolume(int value);

	/**
	 * Returns the value of the '<em><b>Swap Fob Load Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Fob Load Volume</em>' attribute.
	 * @see #setSwapFobLoadVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapFobLoadVolume()
	 * @model
	 * @generated
	 */
	int getSwapFobLoadVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapFobLoadVolume <em>Swap Fob Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Fob Load Volume</em>' attribute.
	 * @see #getSwapFobLoadVolume()
	 * @generated
	 */
	void setSwapFobLoadVolume(int value);

	/**
	 * Returns the value of the '<em><b>Market Buy Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Buy Volume</em>' attribute.
	 * @see #setMarketBuyVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_MarketBuyVolume()
	 * @model
	 * @generated
	 */
	int getMarketBuyVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getMarketBuyVolume <em>Market Buy Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Buy Volume</em>' attribute.
	 * @see #getMarketBuyVolume()
	 * @generated
	 */
	void setMarketBuyVolume(int value);

	/**
	 * Returns the value of the '<em><b>Market Sell Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Sell Volume</em>' attribute.
	 * @see #setMarketSellVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_MarketSellVolume()
	 * @model
	 * @generated
	 */
	int getMarketSellVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getMarketSellVolume <em>Market Sell Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Sell Volume</em>' attribute.
	 * @see #getMarketSellVolume()
	 * @generated
	 */
	void setMarketSellVolume(int value);

	/**
	 * Returns the value of the '<em><b>Base Des Sell Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Des Sell Volume</em>' attribute.
	 * @see #setBaseDesSellVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BaseDesSellVolume()
	 * @model
	 * @generated
	 */
	int getBaseDesSellVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseDesSellVolume <em>Base Des Sell Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Des Sell Volume</em>' attribute.
	 * @see #getBaseDesSellVolume()
	 * @generated
	 */
	void setBaseDesSellVolume(int value);

	/**
	 * Returns the value of the '<em><b>Base Preceding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Preceding Pnl</em>' attribute.
	 * @see #setBasePrecedingPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BasePrecedingPnl()
	 * @model
	 * @generated
	 */
	long getBasePrecedingPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBasePrecedingPnl <em>Base Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Preceding Pnl</em>' attribute.
	 * @see #getBasePrecedingPnl()
	 * @generated
	 */
	void setBasePrecedingPnl(long value);

	/**
	 * Returns the value of the '<em><b>Base Cargo Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Cargo Pnl</em>' attribute.
	 * @see #setBaseCargoPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BaseCargoPnl()
	 * @model
	 * @generated
	 */
	long getBaseCargoPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseCargoPnl <em>Base Cargo Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Cargo Pnl</em>' attribute.
	 * @see #getBaseCargoPnl()
	 * @generated
	 */
	void setBaseCargoPnl(long value);

	/**
	 * Returns the value of the '<em><b>Base Succeeding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Succeeding Pnl</em>' attribute.
	 * @see #setBaseSucceedingPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BaseSucceedingPnl()
	 * @model
	 * @generated
	 */
	long getBaseSucceedingPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseSucceedingPnl <em>Base Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Succeeding Pnl</em>' attribute.
	 * @see #getBaseSucceedingPnl()
	 * @generated
	 */
	void setBaseSucceedingPnl(long value);

	/**
	 * Returns the value of the '<em><b>Swap Preceding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Preceding Pnl</em>' attribute.
	 * @see #setSwapPrecedingPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapPrecedingPnl()
	 * @model
	 * @generated
	 */
	long getSwapPrecedingPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPrecedingPnl <em>Swap Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Preceding Pnl</em>' attribute.
	 * @see #getSwapPrecedingPnl()
	 * @generated
	 */
	void setSwapPrecedingPnl(long value);

	/**
	 * Returns the value of the '<em><b>Swap Shipped Cargo Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Shipped Cargo Pnl</em>' attribute.
	 * @see #setSwapShippedCargoPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapShippedCargoPnl()
	 * @model
	 * @generated
	 */
	long getSwapShippedCargoPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapShippedCargoPnl <em>Swap Shipped Cargo Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Shipped Cargo Pnl</em>' attribute.
	 * @see #getSwapShippedCargoPnl()
	 * @generated
	 */
	void setSwapShippedCargoPnl(long value);

	/**
	 * Returns the value of the '<em><b>Swap Backfill Cargo Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Backfill Cargo Pnl</em>' attribute.
	 * @see #setSwapBackfillCargoPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapBackfillCargoPnl()
	 * @model
	 * @generated
	 */
	long getSwapBackfillCargoPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillCargoPnl <em>Swap Backfill Cargo Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Backfill Cargo Pnl</em>' attribute.
	 * @see #getSwapBackfillCargoPnl()
	 * @generated
	 */
	void setSwapBackfillCargoPnl(long value);

	/**
	 * Returns the value of the '<em><b>Swap Succeeding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Succeeding Pnl</em>' attribute.
	 * @see #setSwapSucceedingPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapSucceedingPnl()
	 * @model
	 * @generated
	 */
	long getSwapSucceedingPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapSucceedingPnl <em>Swap Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Succeeding Pnl</em>' attribute.
	 * @see #getSwapSucceedingPnl()
	 * @generated
	 */
	void setSwapSucceedingPnl(long value);

	/**
	 * Returns the value of the '<em><b>Base Purchase Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Purchase Cost</em>' attribute.
	 * @see #setBasePurchaseCost(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BasePurchaseCost()
	 * @model
	 * @generated
	 */
	long getBasePurchaseCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBasePurchaseCost <em>Base Purchase Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Purchase Cost</em>' attribute.
	 * @see #getBasePurchaseCost()
	 * @generated
	 */
	void setBasePurchaseCost(long value);

	/**
	 * Returns the value of the '<em><b>Base Sale Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Sale Revenue</em>' attribute.
	 * @see #setBaseSaleRevenue(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BaseSaleRevenue()
	 * @model
	 * @generated
	 */
	long getBaseSaleRevenue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseSaleRevenue <em>Base Sale Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Sale Revenue</em>' attribute.
	 * @see #getBaseSaleRevenue()
	 * @generated
	 */
	void setBaseSaleRevenue(long value);

	/**
	 * Returns the value of the '<em><b>Base Shipping Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Shipping Cost</em>' attribute.
	 * @see #setBaseShippingCost(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BaseShippingCost()
	 * @model
	 * @generated
	 */
	long getBaseShippingCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseShippingCost <em>Base Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Shipping Cost</em>' attribute.
	 * @see #getBaseShippingCost()
	 * @generated
	 */
	void setBaseShippingCost(long value);

	/**
	 * Returns the value of the '<em><b>Base Additional Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Additional Pnl</em>' attribute.
	 * @see #setBaseAdditionalPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_BaseAdditionalPnl()
	 * @model
	 * @generated
	 */
	long getBaseAdditionalPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseAdditionalPnl <em>Base Additional Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Additional Pnl</em>' attribute.
	 * @see #getBaseAdditionalPnl()
	 * @generated
	 */
	void setBaseAdditionalPnl(long value);

	/**
	 * Returns the value of the '<em><b>Swap Cargo Purchase Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Cargo Purchase Cost</em>' attribute.
	 * @see #setSwapCargoPurchaseCost(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapCargoPurchaseCost()
	 * @model
	 * @generated
	 */
	long getSwapCargoPurchaseCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapCargoPurchaseCost <em>Swap Cargo Purchase Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Cargo Purchase Cost</em>' attribute.
	 * @see #getSwapCargoPurchaseCost()
	 * @generated
	 */
	void setSwapCargoPurchaseCost(long value);

	/**
	 * Returns the value of the '<em><b>Swap Cargo Sale Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Cargo Sale Revenue</em>' attribute.
	 * @see #setSwapCargoSaleRevenue(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapCargoSaleRevenue()
	 * @model
	 * @generated
	 */
	long getSwapCargoSaleRevenue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapCargoSaleRevenue <em>Swap Cargo Sale Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Cargo Sale Revenue</em>' attribute.
	 * @see #getSwapCargoSaleRevenue()
	 * @generated
	 */
	void setSwapCargoSaleRevenue(long value);

	/**
	 * Returns the value of the '<em><b>Swap Cargo Shipping Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Cargo Shipping Cost</em>' attribute.
	 * @see #setSwapCargoShippingCost(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapCargoShippingCost()
	 * @model
	 * @generated
	 */
	long getSwapCargoShippingCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapCargoShippingCost <em>Swap Cargo Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Cargo Shipping Cost</em>' attribute.
	 * @see #getSwapCargoShippingCost()
	 * @generated
	 */
	void setSwapCargoShippingCost(long value);

	/**
	 * Returns the value of the '<em><b>Swap Cargo Additional Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Cargo Additional Pnl</em>' attribute.
	 * @see #setSwapCargoAdditionalPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapCargoAdditionalPnl()
	 * @model
	 * @generated
	 */
	long getSwapCargoAdditionalPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapCargoAdditionalPnl <em>Swap Cargo Additional Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Cargo Additional Pnl</em>' attribute.
	 * @see #getSwapCargoAdditionalPnl()
	 * @generated
	 */
	void setSwapCargoAdditionalPnl(long value);

	/**
	 * Returns the value of the '<em><b>Swap Backfill Purchase Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Backfill Purchase Cost</em>' attribute.
	 * @see #setSwapBackfillPurchaseCost(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapBackfillPurchaseCost()
	 * @model
	 * @generated
	 */
	long getSwapBackfillPurchaseCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillPurchaseCost <em>Swap Backfill Purchase Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Backfill Purchase Cost</em>' attribute.
	 * @see #getSwapBackfillPurchaseCost()
	 * @generated
	 */
	void setSwapBackfillPurchaseCost(long value);

	/**
	 * Returns the value of the '<em><b>Swap Backfill Sale Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Backfill Sale Revenue</em>' attribute.
	 * @see #setSwapBackfillSaleRevenue(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapBackfillSaleRevenue()
	 * @model
	 * @generated
	 */
	long getSwapBackfillSaleRevenue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillSaleRevenue <em>Swap Backfill Sale Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Backfill Sale Revenue</em>' attribute.
	 * @see #getSwapBackfillSaleRevenue()
	 * @generated
	 */
	void setSwapBackfillSaleRevenue(long value);

	/**
	 * Returns the value of the '<em><b>Swap Backfill Additional Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Backfill Additional Pnl</em>' attribute.
	 * @see #setSwapBackfillAdditionalPnl(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixResult_SwapBackfillAdditionalPnl()
	 * @model
	 * @generated
	 */
	long getSwapBackfillAdditionalPnl();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillAdditionalPnl <em>Swap Backfill Additional Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Backfill Additional Pnl</em>' attribute.
	 * @see #getSwapBackfillAdditionalPnl()
	 * @generated
	 */
	void setSwapBackfillAdditionalPnl(long value);

} // SwapValueMatrixResult
