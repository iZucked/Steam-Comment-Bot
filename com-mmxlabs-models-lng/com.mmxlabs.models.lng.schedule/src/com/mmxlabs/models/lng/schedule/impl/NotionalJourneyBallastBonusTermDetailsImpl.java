/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.NotionalJourneyBallastBonusTermDetails;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Notional Journey Ballast Bonus Term Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NotionalJourneyBallastBonusTermDetailsImpl#getReturnPort <em>Return Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NotionalJourneyBallastBonusTermDetailsImpl extends NotionalJourneyDetailsImpl implements NotionalJourneyBallastBonusTermDetails {
	/**
	 * The default value of the '{@link #getReturnPort() <em>Return Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnPort()
	 * @generated
	 * @ordered
	 */
	protected static final String RETURN_PORT_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getReturnPort() <em>Return Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnPort()
	 * @generated
	 * @ordered
	 */
	protected String returnPort = RETURN_PORT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NotionalJourneyBallastBonusTermDetailsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_DETAILS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getReturnPort() {
		return returnPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReturnPort(String newReturnPort) {
		String oldReturnPort = returnPort;
		returnPort = newReturnPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_DETAILS__RETURN_PORT, oldReturnPort, returnPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_DETAILS__RETURN_PORT:
				return getReturnPort();
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
			case SchedulePackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_DETAILS__RETURN_PORT:
				setReturnPort((String)newValue);
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
			case SchedulePackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_DETAILS__RETURN_PORT:
				setReturnPort(RETURN_PORT_EDEFAULT);
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
			case SchedulePackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_DETAILS__RETURN_PORT:
				return RETURN_PORT_EDEFAULT == null ? returnPort != null : !RETURN_PORT_EDEFAULT.equals(returnPort);
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
		result.append(" (returnPort: ");
		result.append(returnPort);
		result.append(')');
		return result.toString();
	}

} //NotionalJourneyBallastBonusTermDetailsImpl
