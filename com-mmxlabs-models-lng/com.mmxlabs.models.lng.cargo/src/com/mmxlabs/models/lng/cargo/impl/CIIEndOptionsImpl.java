/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CIIEndOptions;
import com.mmxlabs.models.lng.cargo.CargoPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CII End Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CIIEndOptionsImpl#getDesiredCIIGrade <em>Desired CII Grade</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CIIEndOptionsImpl extends EObjectImpl implements CIIEndOptions {
	/**
	 * The default value of the '{@link #getDesiredCIIGrade() <em>Desired CII Grade</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesiredCIIGrade()
	 * @generated
	 * @ordered
	 */
	protected static final String DESIRED_CII_GRADE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDesiredCIIGrade() <em>Desired CII Grade</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesiredCIIGrade()
	 * @generated
	 * @ordered
	 */
	protected String desiredCIIGrade = DESIRED_CII_GRADE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CIIEndOptionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CII_END_OPTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDesiredCIIGrade() {
		return desiredCIIGrade;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDesiredCIIGrade(String newDesiredCIIGrade) {
		String oldDesiredCIIGrade = desiredCIIGrade;
		desiredCIIGrade = newDesiredCIIGrade;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CII_END_OPTIONS__DESIRED_CII_GRADE, oldDesiredCIIGrade, desiredCIIGrade));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.CII_END_OPTIONS__DESIRED_CII_GRADE:
				return getDesiredCIIGrade();
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
			case CargoPackage.CII_END_OPTIONS__DESIRED_CII_GRADE:
				setDesiredCIIGrade((String)newValue);
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
			case CargoPackage.CII_END_OPTIONS__DESIRED_CII_GRADE:
				setDesiredCIIGrade(DESIRED_CII_GRADE_EDEFAULT);
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
			case CargoPackage.CII_END_OPTIONS__DESIRED_CII_GRADE:
				return DESIRED_CII_GRADE_EDEFAULT == null ? desiredCIIGrade != null : !DESIRED_CII_GRADE_EDEFAULT.equals(desiredCIIGrade);
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
		result.append(" (desiredCIIGrade: ");
		result.append(desiredCIIGrade);
		result.append(')');
		return result.toString();
	}

} //CIIEndOptionsImpl
