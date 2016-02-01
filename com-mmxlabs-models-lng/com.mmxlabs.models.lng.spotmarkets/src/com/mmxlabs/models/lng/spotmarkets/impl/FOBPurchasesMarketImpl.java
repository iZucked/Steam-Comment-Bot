/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.APortSet;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>FOB Purchases Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.FOBPurchasesMarketImpl#getNotionalPort <em>Notional Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.FOBPurchasesMarketImpl#getCv <em>Cv</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.FOBPurchasesMarketImpl#getMarketPorts <em>Market Ports</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FOBPurchasesMarketImpl extends SpotMarketImpl implements FOBPurchasesMarket {
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
	 * The default value of the '{@link #getCv() <em>Cv</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCv()
	 * @generated
	 * @ordered
	 */
	protected static final double CV_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCv() <em>Cv</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCv()
	 * @generated
	 * @ordered
	 */
	protected double cv = CV_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMarketPorts() <em>Market Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> marketPorts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FOBPurchasesMarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.FOB_PURCHASES_MARKET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getNotionalPort() {
		if (notionalPort != null && notionalPort.eIsProxy()) {
			InternalEObject oldNotionalPort = (InternalEObject)notionalPort;
			notionalPort = (Port)eResolveProxy(oldNotionalPort);
			if (notionalPort != oldNotionalPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT, oldNotionalPort, notionalPort));
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
	public void setNotionalPort(Port newNotionalPort) {
		Port oldNotionalPort = notionalPort;
		notionalPort = newNotionalPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT, oldNotionalPort, notionalPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getCv() {
		return cv;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCv(double newCv) {
		double oldCv = cv;
		cv = newCv;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.FOB_PURCHASES_MARKET__CV, oldCv, cv));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getMarketPorts() {
		if (marketPorts == null) {
			marketPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, SpotMarketsPackage.FOB_PURCHASES_MARKET__MARKET_PORTS);
		}
		return marketPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT:
				if (resolve) return getNotionalPort();
				return basicGetNotionalPort();
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__CV:
				return getCv();
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__MARKET_PORTS:
				return getMarketPorts();
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
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT:
				setNotionalPort((Port)newValue);
				return;
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__CV:
				setCv((Double)newValue);
				return;
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__MARKET_PORTS:
				getMarketPorts().clear();
				getMarketPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
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
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT:
				setNotionalPort((Port)null);
				return;
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__CV:
				setCv(CV_EDEFAULT);
				return;
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__MARKET_PORTS:
				getMarketPorts().clear();
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
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT:
				return notionalPort != null;
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__CV:
				return cv != CV_EDEFAULT;
			case SpotMarketsPackage.FOB_PURCHASES_MARKET__MARKET_PORTS:
				return marketPorts != null && !marketPorts.isEmpty();
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
		result.append(" (cv: ");
		result.append(cv);
		result.append(')');
		return result.toString();
	}

} // end of FOBPurchasesMarketImpl

// finish type fixing
