/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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

} // SwapValueMatrixResult
