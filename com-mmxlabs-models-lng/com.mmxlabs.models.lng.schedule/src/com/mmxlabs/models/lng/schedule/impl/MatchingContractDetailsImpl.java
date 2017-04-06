/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.MatchingContractDetails;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Matching Contract Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.MatchingContractDetailsImpl#getMatchedPort <em>Matched Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class MatchingContractDetailsImpl extends UUIDObjectImpl implements MatchingContractDetails {
	/**
	 * The default value of the '{@link #getMatchedPort() <em>Matched Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMatchedPort()
	 * @generated
	 * @ordered
	 */
	protected static final String MATCHED_PORT_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getMatchedPort() <em>Matched Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMatchedPort()
	 * @generated
	 * @ordered
	 */
	protected String matchedPort = MATCHED_PORT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MatchingContractDetailsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.MATCHING_CONTRACT_DETAILS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMatchedPort() {
		return matchedPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMatchedPort(String newMatchedPort) {
		String oldMatchedPort = matchedPort;
		matchedPort = newMatchedPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.MATCHING_CONTRACT_DETAILS__MATCHED_PORT, oldMatchedPort, matchedPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.MATCHING_CONTRACT_DETAILS__MATCHED_PORT:
				return getMatchedPort();
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
			case SchedulePackage.MATCHING_CONTRACT_DETAILS__MATCHED_PORT:
				setMatchedPort((String)newValue);
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
			case SchedulePackage.MATCHING_CONTRACT_DETAILS__MATCHED_PORT:
				setMatchedPort(MATCHED_PORT_EDEFAULT);
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
			case SchedulePackage.MATCHING_CONTRACT_DETAILS__MATCHED_PORT:
				return MATCHED_PORT_EDEFAULT == null ? matchedPort != null : !MATCHED_PORT_EDEFAULT.equals(matchedPort);
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
		result.append(" (matchedPort: ");
		result.append(matchedPort);
		result.append(')');
		return result.toString();
	}

} //MatchingContractDetailsImpl
