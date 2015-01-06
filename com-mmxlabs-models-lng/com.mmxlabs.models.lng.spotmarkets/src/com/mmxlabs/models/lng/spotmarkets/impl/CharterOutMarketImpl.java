/**
 */
package com.mmxlabs.models.lng.spotmarkets.impl;

import com.mmxlabs.models.lng.pricing.CharterIndex;

import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Out Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterOutMarketImpl#getCharterOutPrice <em>Charter Out Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterOutMarketImpl#getMinCharterOutDuration <em>Min Charter Out Duration</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CharterOutMarketImpl extends SpotCharterMarketImpl implements CharterOutMarket {
	/**
	 * The cached value of the '{@link #getCharterOutPrice() <em>Charter Out Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOutPrice()
	 * @generated
	 * @ordered
	 */
	protected CharterIndex charterOutPrice;

	/**
	 * The default value of the '{@link #getMinCharterOutDuration() <em>Min Charter Out Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCharterOutDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_CHARTER_OUT_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinCharterOutDuration() <em>Min Charter Out Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCharterOutDuration()
	 * @generated
	 * @ordered
	 */
	protected int minCharterOutDuration = MIN_CHARTER_OUT_DURATION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutMarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.CHARTER_OUT_MARKET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterIndex getCharterOutPrice() {
		if (charterOutPrice != null && charterOutPrice.eIsProxy()) {
			InternalEObject oldCharterOutPrice = (InternalEObject)charterOutPrice;
			charterOutPrice = (CharterIndex)eResolveProxy(oldCharterOutPrice);
			if (charterOutPrice != oldCharterOutPrice) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.CHARTER_OUT_MARKET__CHARTER_OUT_PRICE, oldCharterOutPrice, charterOutPrice));
			}
		}
		return charterOutPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterIndex basicGetCharterOutPrice() {
		return charterOutPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterOutPrice(CharterIndex newCharterOutPrice) {
		CharterIndex oldCharterOutPrice = charterOutPrice;
		charterOutPrice = newCharterOutPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_OUT_MARKET__CHARTER_OUT_PRICE, oldCharterOutPrice, charterOutPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinCharterOutDuration() {
		return minCharterOutDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinCharterOutDuration(int newMinCharterOutDuration) {
		int oldMinCharterOutDuration = minCharterOutDuration;
		minCharterOutDuration = newMinCharterOutDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION, oldMinCharterOutDuration, minCharterOutDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotMarketsPackage.CHARTER_OUT_MARKET__CHARTER_OUT_PRICE:
				if (resolve) return getCharterOutPrice();
				return basicGetCharterOutPrice();
			case SpotMarketsPackage.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION:
				return getMinCharterOutDuration();
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
			case SpotMarketsPackage.CHARTER_OUT_MARKET__CHARTER_OUT_PRICE:
				setCharterOutPrice((CharterIndex)newValue);
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION:
				setMinCharterOutDuration((Integer)newValue);
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
			case SpotMarketsPackage.CHARTER_OUT_MARKET__CHARTER_OUT_PRICE:
				setCharterOutPrice((CharterIndex)null);
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION:
				setMinCharterOutDuration(MIN_CHARTER_OUT_DURATION_EDEFAULT);
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
			case SpotMarketsPackage.CHARTER_OUT_MARKET__CHARTER_OUT_PRICE:
				return charterOutPrice != null;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION:
				return minCharterOutDuration != MIN_CHARTER_OUT_DURATION_EDEFAULT;
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
		result.append(" (minCharterOutDuration: ");
		result.append(minCharterOutDuration);
		result.append(')');
		return result.toString();
	}

} //CharterOutMarketImpl
