/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.APortSet;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DES Sales Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.DESSalesMarketImpl#getNotionalPort <em>Notional Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.DESSalesMarketImpl#getMarketPorts <em>Market Ports</em>}</li>
 * </ul>
 * </p>
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
	 * The cached value of the '{@link #getMarketPorts() <em>Market Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * @since 5.0
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
	public void setNotionalPort(Port newNotionalPort) {
		Port oldNotionalPort = notionalPort;
		notionalPort = newNotionalPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.DES_SALES_MARKET__NOTIONAL_PORT, oldNotionalPort, notionalPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @deprecated
	 */
	@Deprecated
	public EList<APortSet<Port>> getMarketPorts() {
		if (marketPorts == null) {
			marketPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, SpotMarketsPackage.DES_SALES_MARKET__MARKET_PORTS);
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
			case SpotMarketsPackage.DES_SALES_MARKET__NOTIONAL_PORT:
				if (resolve) return getNotionalPort();
				return basicGetNotionalPort();
			case SpotMarketsPackage.DES_SALES_MARKET__MARKET_PORTS:
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
			case SpotMarketsPackage.DES_SALES_MARKET__NOTIONAL_PORT:
				setNotionalPort((Port)newValue);
				return;
			case SpotMarketsPackage.DES_SALES_MARKET__MARKET_PORTS:
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
			case SpotMarketsPackage.DES_SALES_MARKET__NOTIONAL_PORT:
				setNotionalPort((Port)null);
				return;
			case SpotMarketsPackage.DES_SALES_MARKET__MARKET_PORTS:
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
			case SpotMarketsPackage.DES_SALES_MARKET__NOTIONAL_PORT:
				return notionalPort != null;
			case SpotMarketsPackage.DES_SALES_MARKET__MARKET_PORTS:
				return marketPorts != null && !marketPorts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of DESSalesMarketImpl

// finish type fixing
