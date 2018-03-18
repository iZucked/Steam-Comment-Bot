/**
 */
package com.mmxlabs.models.lng.spotmarkets.impl;

import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Out Market Parameters</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterOutMarketParametersImpl#getCharterOutStartDate <em>Charter Out Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterOutMarketParametersImpl#getCharterOutEndDate <em>Charter Out End Date</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CharterOutMarketParametersImpl extends EObjectImpl implements CharterOutMarketParameters {
	/**
	 * The default value of the '{@link #getCharterOutStartDate() <em>Charter Out Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOutStartDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate CHARTER_OUT_START_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCharterOutStartDate() <em>Charter Out Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOutStartDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate charterOutStartDate = CHARTER_OUT_START_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCharterOutEndDate() <em>Charter Out End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOutEndDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate CHARTER_OUT_END_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCharterOutEndDate() <em>Charter Out End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOutEndDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate charterOutEndDate = CHARTER_OUT_END_DATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutMarketParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getCharterOutStartDate() {
		return charterOutStartDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterOutStartDate(LocalDate newCharterOutStartDate) {
		LocalDate oldCharterOutStartDate = charterOutStartDate;
		charterOutStartDate = newCharterOutStartDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE, oldCharterOutStartDate, charterOutStartDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getCharterOutEndDate() {
		return charterOutEndDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterOutEndDate(LocalDate newCharterOutEndDate) {
		LocalDate oldCharterOutEndDate = charterOutEndDate;
		charterOutEndDate = newCharterOutEndDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE, oldCharterOutEndDate, charterOutEndDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE:
				return getCharterOutStartDate();
			case SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE:
				return getCharterOutEndDate();
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
			case SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE:
				setCharterOutStartDate((LocalDate)newValue);
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE:
				setCharterOutEndDate((LocalDate)newValue);
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
			case SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE:
				setCharterOutStartDate(CHARTER_OUT_START_DATE_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE:
				setCharterOutEndDate(CHARTER_OUT_END_DATE_EDEFAULT);
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
			case SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE:
				return CHARTER_OUT_START_DATE_EDEFAULT == null ? charterOutStartDate != null : !CHARTER_OUT_START_DATE_EDEFAULT.equals(charterOutStartDate);
			case SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE:
				return CHARTER_OUT_END_DATE_EDEFAULT == null ? charterOutEndDate != null : !CHARTER_OUT_END_DATE_EDEFAULT.equals(charterOutEndDate);
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
		result.append(" (charterOutStartDate: ");
		result.append(charterOutStartDate);
		result.append(", charterOutEndDate: ");
		result.append(charterOutEndDate);
		result.append(')');
		return result.toString();
	}

} //CharterOutMarketParametersImpl
