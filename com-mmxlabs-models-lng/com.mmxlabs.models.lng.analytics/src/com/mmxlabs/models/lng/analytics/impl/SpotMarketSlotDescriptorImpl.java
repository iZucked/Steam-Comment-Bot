/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor;

import java.time.YearMonth;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spot Market Slot Descriptor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SpotMarketSlotDescriptorImpl#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SpotMarketSlotDescriptorImpl#getMarketName <em>Market Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SpotMarketSlotDescriptorImpl#getPortName <em>Port Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpotMarketSlotDescriptorImpl extends SlotDescriptorImpl implements SpotMarketSlotDescriptor {
	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final YearMonth DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected YearMonth date = DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMarketName() <em>Market Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketName()
	 * @generated
	 * @ordered
	 */
	protected static final String MARKET_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMarketName() <em>Market Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketName()
	 * @generated
	 * @ordered
	 */
	protected String marketName = MARKET_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortName() <em>Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortName()
	 * @generated
	 * @ordered
	 */
	protected static final String PORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPortName() <em>Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortName()
	 * @generated
	 * @ordered
	 */
	protected String portName = PORT_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotMarketSlotDescriptorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SPOT_MARKET_SLOT_DESCRIPTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public YearMonth getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDate(YearMonth newDate) {
		YearMonth oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMarketName() {
		return marketName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarketName(String newMarketName) {
		String oldMarketName = marketName;
		marketName = newMarketName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__MARKET_NAME, oldMarketName, marketName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPortName() {
		return portName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortName(String newPortName) {
		String oldPortName = portName;
		portName = newPortName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__PORT_NAME, oldPortName, portName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__DATE:
				return getDate();
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__MARKET_NAME:
				return getMarketName();
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__PORT_NAME:
				return getPortName();
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
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__DATE:
				setDate((YearMonth)newValue);
				return;
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__MARKET_NAME:
				setMarketName((String)newValue);
				return;
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__PORT_NAME:
				setPortName((String)newValue);
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
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__MARKET_NAME:
				setMarketName(MARKET_NAME_EDEFAULT);
				return;
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__PORT_NAME:
				setPortName(PORT_NAME_EDEFAULT);
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
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__MARKET_NAME:
				return MARKET_NAME_EDEFAULT == null ? marketName != null : !MARKET_NAME_EDEFAULT.equals(marketName);
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR__PORT_NAME:
				return PORT_NAME_EDEFAULT == null ? portName != null : !PORT_NAME_EDEFAULT.equals(portName);
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
		result.append(" (date: ");
		result.append(date);
		result.append(", marketName: ");
		result.append(marketName);
		result.append(", portName: ");
		result.append(portName);
		result.append(')');
		return result.toString();
	}

} //SpotMarketSlotDescriptorImpl
