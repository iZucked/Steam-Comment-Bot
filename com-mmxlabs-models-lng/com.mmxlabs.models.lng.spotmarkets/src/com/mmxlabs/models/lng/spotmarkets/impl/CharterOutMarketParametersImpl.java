/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
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
	 * This is true if the Charter Out Start Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean charterOutStartDateESet;

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
	 * This is true if the Charter Out End Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean charterOutEndDateESet;

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
	@Override
	public LocalDate getCharterOutStartDate() {
		return charterOutStartDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCharterOutStartDate(LocalDate newCharterOutStartDate) {
		LocalDate oldCharterOutStartDate = charterOutStartDate;
		charterOutStartDate = newCharterOutStartDate;
		boolean oldCharterOutStartDateESet = charterOutStartDateESet;
		charterOutStartDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE, oldCharterOutStartDate, charterOutStartDate, !oldCharterOutStartDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCharterOutStartDate() {
		LocalDate oldCharterOutStartDate = charterOutStartDate;
		boolean oldCharterOutStartDateESet = charterOutStartDateESet;
		charterOutStartDate = CHARTER_OUT_START_DATE_EDEFAULT;
		charterOutStartDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE, oldCharterOutStartDate, CHARTER_OUT_START_DATE_EDEFAULT, oldCharterOutStartDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCharterOutStartDate() {
		return charterOutStartDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getCharterOutEndDate() {
		return charterOutEndDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCharterOutEndDate(LocalDate newCharterOutEndDate) {
		LocalDate oldCharterOutEndDate = charterOutEndDate;
		charterOutEndDate = newCharterOutEndDate;
		boolean oldCharterOutEndDateESet = charterOutEndDateESet;
		charterOutEndDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE, oldCharterOutEndDate, charterOutEndDate, !oldCharterOutEndDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCharterOutEndDate() {
		LocalDate oldCharterOutEndDate = charterOutEndDate;
		boolean oldCharterOutEndDateESet = charterOutEndDateESet;
		charterOutEndDate = CHARTER_OUT_END_DATE_EDEFAULT;
		charterOutEndDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE, oldCharterOutEndDate, CHARTER_OUT_END_DATE_EDEFAULT, oldCharterOutEndDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCharterOutEndDate() {
		return charterOutEndDateESet;
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
				unsetCharterOutStartDate();
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE:
				unsetCharterOutEndDate();
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
				return isSetCharterOutStartDate();
			case SpotMarketsPackage.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE:
				return isSetCharterOutEndDate();
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
		result.append(" (charterOutStartDate: ");
		if (charterOutStartDateESet) result.append(charterOutStartDate); else result.append("<unset>");
		result.append(", charterOutEndDate: ");
		if (charterOutEndDateESet) result.append(charterOutEndDate); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //CharterOutMarketParametersImpl
