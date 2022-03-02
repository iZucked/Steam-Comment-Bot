/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.OriginPortRepositioningFeeTermDetails;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Origin Port Repositioning Fee Term Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.OriginPortRepositioningFeeTermDetailsImpl#getOriginPort <em>Origin Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OriginPortRepositioningFeeTermDetailsImpl extends NotionalJourneyDetailsImpl implements OriginPortRepositioningFeeTermDetails {
	/**
	 * The default value of the '{@link #getOriginPort() <em>Origin Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginPort()
	 * @generated
	 * @ordered
	 */
	protected static final String ORIGIN_PORT_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getOriginPort() <em>Origin Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginPort()
	 * @generated
	 * @ordered
	 */
	protected String originPort = ORIGIN_PORT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OriginPortRepositioningFeeTermDetailsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.ORIGIN_PORT_REPOSITIONING_FEE_TERM_DETAILS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getOriginPort() {
		return originPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOriginPort(String newOriginPort) {
		String oldOriginPort = originPort;
		originPort = newOriginPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM_DETAILS__ORIGIN_PORT, oldOriginPort, originPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM_DETAILS__ORIGIN_PORT:
				return getOriginPort();
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
			case SchedulePackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM_DETAILS__ORIGIN_PORT:
				setOriginPort((String)newValue);
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
			case SchedulePackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM_DETAILS__ORIGIN_PORT:
				setOriginPort(ORIGIN_PORT_EDEFAULT);
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
			case SchedulePackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM_DETAILS__ORIGIN_PORT:
				return ORIGIN_PORT_EDEFAULT == null ? originPort != null : !ORIGIN_PORT_EDEFAULT.equals(originPort);
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
		result.append(" (originPort: ");
		result.append(originPort);
		result.append(')');
		return result.toString();
	}

} //OriginPortRepositioningFeeTermDetailsImpl
