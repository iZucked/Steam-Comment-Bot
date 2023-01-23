/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
		result.append(')');
		return result.toString();
	}

} //SwapValueMatrixResultImpl
