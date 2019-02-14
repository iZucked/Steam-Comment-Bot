/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Commodity Curve</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CommodityCurveImpl#getMarketIndex <em>Market Index</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CommodityCurveImpl extends AbstractYearMonthCurveImpl implements CommodityCurve {
	/**
	 * The cached value of the '{@link #getMarketIndex() <em>Market Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketIndex()
	 * @generated
	 * @ordered
	 */
	protected MarketIndex marketIndex;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CommodityCurveImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.COMMODITY_CURVE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MarketIndex getMarketIndex() {
		if (marketIndex != null && marketIndex.eIsProxy()) {
			InternalEObject oldMarketIndex = (InternalEObject)marketIndex;
			marketIndex = (MarketIndex)eResolveProxy(oldMarketIndex);
			if (marketIndex != oldMarketIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.COMMODITY_CURVE__MARKET_INDEX, oldMarketIndex, marketIndex));
			}
		}
		return marketIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MarketIndex basicGetMarketIndex() {
		return marketIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarketIndex(MarketIndex newMarketIndex) {
		MarketIndex oldMarketIndex = marketIndex;
		marketIndex = newMarketIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.COMMODITY_CURVE__MARKET_INDEX, oldMarketIndex, marketIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.COMMODITY_CURVE__MARKET_INDEX:
				if (resolve) return getMarketIndex();
				return basicGetMarketIndex();
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
			case PricingPackage.COMMODITY_CURVE__MARKET_INDEX:
				setMarketIndex((MarketIndex)newValue);
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
			case PricingPackage.COMMODITY_CURVE__MARKET_INDEX:
				setMarketIndex((MarketIndex)null);
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
			case PricingPackage.COMMODITY_CURVE__MARKET_INDEX:
				return marketIndex != null;
		}
		return super.eIsSet(featureID);
	}

} //CommodityCurveImpl
