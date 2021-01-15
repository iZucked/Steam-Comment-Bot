/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.port.impl;

import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contingency Matrix Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.ContingencyMatrixEntryImpl#getFromPort <em>From Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.ContingencyMatrixEntryImpl#getToPort <em>To Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.ContingencyMatrixEntryImpl#getDuration <em>Duration</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContingencyMatrixEntryImpl extends EObjectImpl implements ContingencyMatrixEntry {
	/**
	 * The cached value of the '{@link #getFromPort() <em>From Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromPort()
	 * @generated
	 * @ordered
	 */
	protected Port fromPort;

	/**
	 * The cached value of the '{@link #getToPort() <em>To Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToPort()
	 * @generated
	 * @ordered
	 */
	protected Port toPort;

	/**
	 * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected int duration = DURATION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContingencyMatrixEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.CONTINGENCY_MATRIX_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getFromPort() {
		if (fromPort != null && fromPort.eIsProxy()) {
			InternalEObject oldFromPort = (InternalEObject)fromPort;
			fromPort = (Port)eResolveProxy(oldFromPort);
			if (fromPort != oldFromPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PortPackage.CONTINGENCY_MATRIX_ENTRY__FROM_PORT, oldFromPort, fromPort));
			}
		}
		return fromPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetFromPort() {
		return fromPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFromPort(Port newFromPort) {
		Port oldFromPort = fromPort;
		fromPort = newFromPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.CONTINGENCY_MATRIX_ENTRY__FROM_PORT, oldFromPort, fromPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getToPort() {
		if (toPort != null && toPort.eIsProxy()) {
			InternalEObject oldToPort = (InternalEObject)toPort;
			toPort = (Port)eResolveProxy(oldToPort);
			if (toPort != oldToPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PortPackage.CONTINGENCY_MATRIX_ENTRY__TO_PORT, oldToPort, toPort));
			}
		}
		return toPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetToPort() {
		return toPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setToPort(Port newToPort) {
		Port oldToPort = toPort;
		toPort = newToPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.CONTINGENCY_MATRIX_ENTRY__TO_PORT, oldToPort, toPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDuration() {
		return duration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDuration(int newDuration) {
		int oldDuration = duration;
		duration = newDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.CONTINGENCY_MATRIX_ENTRY__DURATION, oldDuration, duration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__FROM_PORT:
				if (resolve) return getFromPort();
				return basicGetFromPort();
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__TO_PORT:
				if (resolve) return getToPort();
				return basicGetToPort();
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__DURATION:
				return getDuration();
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
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__FROM_PORT:
				setFromPort((Port)newValue);
				return;
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__TO_PORT:
				setToPort((Port)newValue);
				return;
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__DURATION:
				setDuration((Integer)newValue);
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
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__FROM_PORT:
				setFromPort((Port)null);
				return;
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__TO_PORT:
				setToPort((Port)null);
				return;
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__DURATION:
				setDuration(DURATION_EDEFAULT);
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
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__FROM_PORT:
				return fromPort != null;
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__TO_PORT:
				return toPort != null;
			case PortPackage.CONTINGENCY_MATRIX_ENTRY__DURATION:
				return duration != DURATION_EDEFAULT;
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
		result.append(" (duration: ");
		result.append(duration);
		result.append(')');
		return result.toString();
	}

} //ContingencyMatrixEntryImpl
