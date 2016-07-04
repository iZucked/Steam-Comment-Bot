/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets.impl;

import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Out Start Date</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterOutStartDateImpl#getCharterOutStartDate <em>Charter Out Start Date</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CharterOutStartDateImpl extends EObjectImpl implements CharterOutStartDate {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutStartDateImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.CHARTER_OUT_START_DATE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_OUT_START_DATE__CHARTER_OUT_START_DATE, oldCharterOutStartDate, charterOutStartDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotMarketsPackage.CHARTER_OUT_START_DATE__CHARTER_OUT_START_DATE:
				return getCharterOutStartDate();
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
			case SpotMarketsPackage.CHARTER_OUT_START_DATE__CHARTER_OUT_START_DATE:
				setCharterOutStartDate((LocalDate)newValue);
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
			case SpotMarketsPackage.CHARTER_OUT_START_DATE__CHARTER_OUT_START_DATE:
				setCharterOutStartDate(CHARTER_OUT_START_DATE_EDEFAULT);
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
			case SpotMarketsPackage.CHARTER_OUT_START_DATE__CHARTER_OUT_START_DATE:
				return CHARTER_OUT_START_DATE_EDEFAULT == null ? charterOutStartDate != null : !CHARTER_OUT_START_DATE_EDEFAULT.equals(charterOutStartDate);
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
		result.append(')');
		return result.toString();
	}

} //CharterOutStartDateImpl
