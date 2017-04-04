/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.LumpSumContractDetails;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Lump Sum Contract Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.LumpSumContractDetailsImpl#getLumpSum <em>Lump Sum</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LumpSumContractDetailsImpl extends MatchingContractDetailsImpl implements LumpSumContractDetails {
	/**
	 * The default value of the '{@link #getLumpSum() <em>Lump Sum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLumpSum()
	 * @generated
	 * @ordered
	 */
	protected static final int LUMP_SUM_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLumpSum() <em>Lump Sum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLumpSum()
	 * @generated
	 * @ordered
	 */
	protected int lumpSum = LUMP_SUM_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LumpSumContractDetailsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.LUMP_SUM_CONTRACT_DETAILS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLumpSum() {
		return lumpSum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLumpSum(int newLumpSum) {
		int oldLumpSum = lumpSum;
		lumpSum = newLumpSum;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.LUMP_SUM_CONTRACT_DETAILS__LUMP_SUM, oldLumpSum, lumpSum));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.LUMP_SUM_CONTRACT_DETAILS__LUMP_SUM:
				return getLumpSum();
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
			case SchedulePackage.LUMP_SUM_CONTRACT_DETAILS__LUMP_SUM:
				setLumpSum((Integer)newValue);
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
			case SchedulePackage.LUMP_SUM_CONTRACT_DETAILS__LUMP_SUM:
				setLumpSum(LUMP_SUM_EDEFAULT);
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
			case SchedulePackage.LUMP_SUM_CONTRACT_DETAILS__LUMP_SUM:
				return lumpSum != LUMP_SUM_EDEFAULT;
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
		result.append(" (lumpSum: ");
		result.append(lumpSum);
		result.append(')');
		return result.toString();
	}

} //LumpSumContractDetailsImpl
