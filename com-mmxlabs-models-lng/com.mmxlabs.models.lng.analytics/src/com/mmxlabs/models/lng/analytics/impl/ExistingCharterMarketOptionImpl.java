/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Existing Charter Market Option</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ExistingCharterMarketOptionImpl#getCharterInMarket <em>Charter In Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ExistingCharterMarketOptionImpl#getSpotIndex <em>Spot Index</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExistingCharterMarketOptionImpl extends ShippingOptionImpl implements ExistingCharterMarketOption {
	/**
	 * The cached value of the '{@link #getCharterInMarket() <em>Charter In Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInMarket()
	 * @generated
	 * @ordered
	 */
	protected CharterInMarket charterInMarket;

	/**
	 * The default value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected int spotIndex = SPOT_INDEX_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExistingCharterMarketOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterInMarket getCharterInMarket() {
		if (charterInMarket != null && charterInMarket.eIsProxy()) {
			InternalEObject oldCharterInMarket = (InternalEObject)charterInMarket;
			charterInMarket = (CharterInMarket)eResolveProxy(oldCharterInMarket);
			if (charterInMarket != oldCharterInMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET, oldCharterInMarket, charterInMarket));
			}
		}
		return charterInMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterInMarket basicGetCharterInMarket() {
		return charterInMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterInMarket(CharterInMarket newCharterInMarket) {
		CharterInMarket oldCharterInMarket = charterInMarket;
		charterInMarket = newCharterInMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET, oldCharterInMarket, charterInMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSpotIndex() {
		return spotIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotIndex(int newSpotIndex) {
		int oldSpotIndex = spotIndex;
		spotIndex = newSpotIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX, oldSpotIndex, spotIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET:
				if (resolve) return getCharterInMarket();
				return basicGetCharterInMarket();
			case AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX:
				return getSpotIndex();
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
			case AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET:
				setCharterInMarket((CharterInMarket)newValue);
				return;
			case AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
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
			case AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET:
				setCharterInMarket((CharterInMarket)null);
				return;
			case AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX:
				setSpotIndex(SPOT_INDEX_EDEFAULT);
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
			case AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET:
				return charterInMarket != null;
			case AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX:
				return spotIndex != SPOT_INDEX_EDEFAULT;
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
		result.append(" (spotIndex: ");
		result.append(spotIndex);
		result.append(')');
		return result.toString();
	}

} //ExistingCharterMarketOptionImpl
