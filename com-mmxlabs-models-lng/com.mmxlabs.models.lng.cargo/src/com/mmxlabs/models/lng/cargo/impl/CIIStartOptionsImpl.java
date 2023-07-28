/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CIIStartOptions;
import com.mmxlabs.models.lng.cargo.CargoPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CII Start Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CIIStartOptionsImpl#getYearToDateEmissions <em>Year To Date Emissions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CIIStartOptionsImpl#getYearToDateDistance <em>Year To Date Distance</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CIIStartOptionsImpl extends EObjectImpl implements CIIStartOptions {
	/**
	 * The default value of the '{@link #getYearToDateEmissions() <em>Year To Date Emissions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearToDateEmissions()
	 * @generated
	 * @ordered
	 */
	protected static final int YEAR_TO_DATE_EMISSIONS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getYearToDateEmissions() <em>Year To Date Emissions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearToDateEmissions()
	 * @generated
	 * @ordered
	 */
	protected int yearToDateEmissions = YEAR_TO_DATE_EMISSIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getYearToDateDistance() <em>Year To Date Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearToDateDistance()
	 * @generated
	 * @ordered
	 */
	protected static final int YEAR_TO_DATE_DISTANCE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getYearToDateDistance() <em>Year To Date Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearToDateDistance()
	 * @generated
	 * @ordered
	 */
	protected int yearToDateDistance = YEAR_TO_DATE_DISTANCE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CIIStartOptionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CII_START_OPTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getYearToDateEmissions() {
		return yearToDateEmissions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setYearToDateEmissions(int newYearToDateEmissions) {
		int oldYearToDateEmissions = yearToDateEmissions;
		yearToDateEmissions = newYearToDateEmissions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CII_START_OPTIONS__YEAR_TO_DATE_EMISSIONS, oldYearToDateEmissions, yearToDateEmissions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getYearToDateDistance() {
		return yearToDateDistance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setYearToDateDistance(int newYearToDateDistance) {
		int oldYearToDateDistance = yearToDateDistance;
		yearToDateDistance = newYearToDateDistance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CII_START_OPTIONS__YEAR_TO_DATE_DISTANCE, oldYearToDateDistance, yearToDateDistance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.CII_START_OPTIONS__YEAR_TO_DATE_EMISSIONS:
				return getYearToDateEmissions();
			case CargoPackage.CII_START_OPTIONS__YEAR_TO_DATE_DISTANCE:
				return getYearToDateDistance();
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
			case CargoPackage.CII_START_OPTIONS__YEAR_TO_DATE_EMISSIONS:
				setYearToDateEmissions((Integer)newValue);
				return;
			case CargoPackage.CII_START_OPTIONS__YEAR_TO_DATE_DISTANCE:
				setYearToDateDistance((Integer)newValue);
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
			case CargoPackage.CII_START_OPTIONS__YEAR_TO_DATE_EMISSIONS:
				setYearToDateEmissions(YEAR_TO_DATE_EMISSIONS_EDEFAULT);
				return;
			case CargoPackage.CII_START_OPTIONS__YEAR_TO_DATE_DISTANCE:
				setYearToDateDistance(YEAR_TO_DATE_DISTANCE_EDEFAULT);
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
			case CargoPackage.CII_START_OPTIONS__YEAR_TO_DATE_EMISSIONS:
				return yearToDateEmissions != YEAR_TO_DATE_EMISSIONS_EDEFAULT;
			case CargoPackage.CII_START_OPTIONS__YEAR_TO_DATE_DISTANCE:
				return yearToDateDistance != YEAR_TO_DATE_DISTANCE_EDEFAULT;
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
		result.append(" (yearToDateEmissions: ");
		result.append(yearToDateEmissions);
		result.append(", yearToDateDistance: ");
		result.append(yearToDateDistance);
		result.append(')');
		return result.toString();
	}

} //CIIStartOptionsImpl
