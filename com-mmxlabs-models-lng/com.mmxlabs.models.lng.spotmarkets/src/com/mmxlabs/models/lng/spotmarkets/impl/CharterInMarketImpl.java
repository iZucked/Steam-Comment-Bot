/**
 */
package com.mmxlabs.models.lng.spotmarkets.impl;

import com.mmxlabs.models.lng.pricing.CharterIndex;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter In Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getCharterInPrice <em>Charter In Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CharterInMarketImpl extends SpotCharterMarketImpl implements CharterInMarket {
	/**
	 * The cached value of the '{@link #getCharterInPrice() <em>Charter In Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInPrice()
	 * @generated
	 * @ordered
	 */
	protected CharterIndex charterInPrice;

	/**
	 * The default value of the '{@link #getSpotCharterCount() <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotCharterCount()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_CHARTER_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotCharterCount() <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotCharterCount()
	 * @generated
	 * @ordered
	 */
	protected int spotCharterCount = SPOT_CHARTER_COUNT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterInMarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.CHARTER_IN_MARKET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterIndex getCharterInPrice() {
		if (charterInPrice != null && charterInPrice.eIsProxy()) {
			InternalEObject oldCharterInPrice = (InternalEObject)charterInPrice;
			charterInPrice = (CharterIndex)eResolveProxy(oldCharterInPrice);
			if (charterInPrice != oldCharterInPrice) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_PRICE, oldCharterInPrice, charterInPrice));
			}
		}
		return charterInPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterIndex basicGetCharterInPrice() {
		return charterInPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterInPrice(CharterIndex newCharterInPrice) {
		CharterIndex oldCharterInPrice = charterInPrice;
		charterInPrice = newCharterInPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_PRICE, oldCharterInPrice, charterInPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSpotCharterCount() {
		return spotCharterCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotCharterCount(int newSpotCharterCount) {
		int oldSpotCharterCount = spotCharterCount;
		spotCharterCount = newSpotCharterCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT, oldSpotCharterCount, spotCharterCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_PRICE:
				if (resolve) return getCharterInPrice();
				return basicGetCharterInPrice();
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				return getSpotCharterCount();
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
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_PRICE:
				setCharterInPrice((CharterIndex)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				setSpotCharterCount((Integer)newValue);
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
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_PRICE:
				setCharterInPrice((CharterIndex)null);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				setSpotCharterCount(SPOT_CHARTER_COUNT_EDEFAULT);
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
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_PRICE:
				return charterInPrice != null;
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				return spotCharterCount != SPOT_CHARTER_COUNT_EDEFAULT;
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (spotCharterCount: ");
		result.append(spotCharterCount);
		result.append(')');
		return result.toString();
	}

} //CharterInMarketImpl
