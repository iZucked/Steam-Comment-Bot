/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DesSpacingRow;

import java.time.LocalDateTime;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Des Spacing Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.DesSpacingRowImpl#getMinDischargeDate <em>Min Discharge Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.DesSpacingRowImpl#getMaxDischargeDate <em>Max Discharge Date</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DesSpacingRowImpl extends EObjectImpl implements DesSpacingRow {
	/**
	 * The default value of the '{@link #getMinDischargeDate() <em>Min Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinDischargeDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime MIN_DISCHARGE_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMinDischargeDate() <em>Min Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinDischargeDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime minDischargeDate = MIN_DISCHARGE_DATE_EDEFAULT;

	/**
	 * This is true if the Min Discharge Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minDischargeDateESet;

	/**
	 * The default value of the '{@link #getMaxDischargeDate() <em>Max Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDischargeDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime MAX_DISCHARGE_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMaxDischargeDate() <em>Max Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDischargeDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime maxDischargeDate = MAX_DISCHARGE_DATE_EDEFAULT;

	/**
	 * This is true if the Max Discharge Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxDischargeDateESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DesSpacingRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.DES_SPACING_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getMinDischargeDate() {
		return minDischargeDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinDischargeDate(LocalDateTime newMinDischargeDate) {
		LocalDateTime oldMinDischargeDate = minDischargeDate;
		minDischargeDate = newMinDischargeDate;
		boolean oldMinDischargeDateESet = minDischargeDateESet;
		minDischargeDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.DES_SPACING_ROW__MIN_DISCHARGE_DATE, oldMinDischargeDate, minDischargeDate, !oldMinDischargeDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinDischargeDate() {
		LocalDateTime oldMinDischargeDate = minDischargeDate;
		boolean oldMinDischargeDateESet = minDischargeDateESet;
		minDischargeDate = MIN_DISCHARGE_DATE_EDEFAULT;
		minDischargeDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ADPPackage.DES_SPACING_ROW__MIN_DISCHARGE_DATE, oldMinDischargeDate, MIN_DISCHARGE_DATE_EDEFAULT, oldMinDischargeDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinDischargeDate() {
		return minDischargeDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getMaxDischargeDate() {
		return maxDischargeDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxDischargeDate(LocalDateTime newMaxDischargeDate) {
		LocalDateTime oldMaxDischargeDate = maxDischargeDate;
		maxDischargeDate = newMaxDischargeDate;
		boolean oldMaxDischargeDateESet = maxDischargeDateESet;
		maxDischargeDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.DES_SPACING_ROW__MAX_DISCHARGE_DATE, oldMaxDischargeDate, maxDischargeDate, !oldMaxDischargeDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMaxDischargeDate() {
		LocalDateTime oldMaxDischargeDate = maxDischargeDate;
		boolean oldMaxDischargeDateESet = maxDischargeDateESet;
		maxDischargeDate = MAX_DISCHARGE_DATE_EDEFAULT;
		maxDischargeDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ADPPackage.DES_SPACING_ROW__MAX_DISCHARGE_DATE, oldMaxDischargeDate, MAX_DISCHARGE_DATE_EDEFAULT, oldMaxDischargeDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMaxDischargeDate() {
		return maxDischargeDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.DES_SPACING_ROW__MIN_DISCHARGE_DATE:
				return getMinDischargeDate();
			case ADPPackage.DES_SPACING_ROW__MAX_DISCHARGE_DATE:
				return getMaxDischargeDate();
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
			case ADPPackage.DES_SPACING_ROW__MIN_DISCHARGE_DATE:
				setMinDischargeDate((LocalDateTime)newValue);
				return;
			case ADPPackage.DES_SPACING_ROW__MAX_DISCHARGE_DATE:
				setMaxDischargeDate((LocalDateTime)newValue);
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
			case ADPPackage.DES_SPACING_ROW__MIN_DISCHARGE_DATE:
				unsetMinDischargeDate();
				return;
			case ADPPackage.DES_SPACING_ROW__MAX_DISCHARGE_DATE:
				unsetMaxDischargeDate();
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
			case ADPPackage.DES_SPACING_ROW__MIN_DISCHARGE_DATE:
				return isSetMinDischargeDate();
			case ADPPackage.DES_SPACING_ROW__MAX_DISCHARGE_DATE:
				return isSetMaxDischargeDate();
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
		result.append(" (minDischargeDate: ");
		if (minDischargeDateESet) result.append(minDischargeDate); else result.append("<unset>");
		result.append(", maxDischargeDate: ");
		if (maxDischargeDateESet) result.append(maxDischargeDate); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //DesSpacingRowImpl
