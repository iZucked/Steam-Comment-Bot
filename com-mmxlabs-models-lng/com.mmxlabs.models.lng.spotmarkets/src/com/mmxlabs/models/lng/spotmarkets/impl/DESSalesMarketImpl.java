/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DES Sales Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.DESSalesMarketImpl#getNotionalPort <em>Notional Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.DESSalesMarketImpl#getDaysBuffer <em>Days Buffer</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DESSalesMarketImpl extends SpotMarketImpl implements DESSalesMarket {
	/**
	 * The cached value of the '{@link #getNotionalPort() <em>Notional Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotionalPort()
	 * @generated
	 * @ordered
	 */
	protected Port notionalPort;

	/**
	 * The default value of the '{@link #getDaysBuffer() <em>Days Buffer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDaysBuffer()
	 * @generated
	 * @ordered
	 */
	protected static final int DAYS_BUFFER_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDaysBuffer() <em>Days Buffer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDaysBuffer()
	 * @generated
	 * @ordered
	 */
	protected int daysBuffer = DAYS_BUFFER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DESSalesMarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.DES_SALES_MARKET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getNotionalPort() {
		if (notionalPort != null && notionalPort.eIsProxy()) {
			InternalEObject oldNotionalPort = (InternalEObject)notionalPort;
			notionalPort = (Port)eResolveProxy(oldNotionalPort);
			if (notionalPort != oldNotionalPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.DES_SALES_MARKET__NOTIONAL_PORT, oldNotionalPort, notionalPort));
			}
		}
		return notionalPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetNotionalPort() {
		return notionalPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNotionalPort(Port newNotionalPort) {
		Port oldNotionalPort = notionalPort;
		notionalPort = newNotionalPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.DES_SALES_MARKET__NOTIONAL_PORT, oldNotionalPort, notionalPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDaysBuffer() {
		return daysBuffer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDaysBuffer(int newDaysBuffer) {
		int oldDaysBuffer = daysBuffer;
		daysBuffer = newDaysBuffer;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.DES_SALES_MARKET__DAYS_BUFFER, oldDaysBuffer, daysBuffer));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotMarketsPackage.DES_SALES_MARKET__NOTIONAL_PORT:
				if (resolve) return getNotionalPort();
				return basicGetNotionalPort();
			case SpotMarketsPackage.DES_SALES_MARKET__DAYS_BUFFER:
				return getDaysBuffer();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SpotMarketsPackage.DES_SALES_MARKET__NOTIONAL_PORT:
				setNotionalPort((Port)newValue);
				return;
			case SpotMarketsPackage.DES_SALES_MARKET__DAYS_BUFFER:
				setDaysBuffer((Integer)newValue);
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
			case SpotMarketsPackage.DES_SALES_MARKET__NOTIONAL_PORT:
				setNotionalPort((Port)null);
				return;
			case SpotMarketsPackage.DES_SALES_MARKET__DAYS_BUFFER:
				setDaysBuffer(DAYS_BUFFER_EDEFAULT);
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
			case SpotMarketsPackage.DES_SALES_MARKET__NOTIONAL_PORT:
				return notionalPort != null;
			case SpotMarketsPackage.DES_SALES_MARKET__DAYS_BUFFER:
				return daysBuffer != DAYS_BUFFER_EDEFAULT;
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
		result.append(" (daysBuffer: ");
		result.append(daysBuffer);
		result.append(')');
		return result.toString();
	}

} // end of DESSalesMarketImpl

// finish type fixing
