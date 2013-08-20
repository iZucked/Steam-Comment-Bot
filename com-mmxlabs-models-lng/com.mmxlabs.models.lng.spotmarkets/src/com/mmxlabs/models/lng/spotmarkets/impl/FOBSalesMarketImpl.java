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
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.APortSet;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>FOB Sales Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.FOBSalesMarketImpl#getLoadPort <em>Load Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.FOBSalesMarketImpl#getOriginPorts <em>Origin Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FOBSalesMarketImpl extends SpotMarketImpl implements FOBSalesMarket {
	/**
	 * The cached value of the '{@link #getLoadPort() <em>Load Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadPort()
	 * @generated
	 * @ordered
	 */
	protected Port loadPort;

	/**
	 * The cached value of the '{@link #getOriginPorts() <em>Origin Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @see #getOriginPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> originPorts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FOBSalesMarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.FOB_SALES_MARKET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getLoadPort() {
		if (loadPort != null && loadPort.eIsProxy()) {
			InternalEObject oldLoadPort = (InternalEObject)loadPort;
			loadPort = (Port)eResolveProxy(oldLoadPort);
			if (loadPort != oldLoadPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.FOB_SALES_MARKET__LOAD_PORT, oldLoadPort, loadPort));
			}
		}
		return loadPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetLoadPort() {
		return loadPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadPort(Port newLoadPort) {
		Port oldLoadPort = loadPort;
		loadPort = newLoadPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.FOB_SALES_MARKET__LOAD_PORT, oldLoadPort, loadPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getOriginPorts() {
		if (originPorts == null) {
			originPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, SpotMarketsPackage.FOB_SALES_MARKET__ORIGIN_PORTS);
		}
		return originPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotMarketsPackage.FOB_SALES_MARKET__LOAD_PORT:
				if (resolve) return getLoadPort();
				return basicGetLoadPort();
			case SpotMarketsPackage.FOB_SALES_MARKET__ORIGIN_PORTS:
				return getOriginPorts();
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
			case SpotMarketsPackage.FOB_SALES_MARKET__LOAD_PORT:
				setLoadPort((Port)newValue);
				return;
			case SpotMarketsPackage.FOB_SALES_MARKET__ORIGIN_PORTS:
				getOriginPorts().clear();
				getOriginPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
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
			case SpotMarketsPackage.FOB_SALES_MARKET__LOAD_PORT:
				setLoadPort((Port)null);
				return;
			case SpotMarketsPackage.FOB_SALES_MARKET__ORIGIN_PORTS:
				getOriginPorts().clear();
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
			case SpotMarketsPackage.FOB_SALES_MARKET__LOAD_PORT:
				return loadPort != null;
			case SpotMarketsPackage.FOB_SALES_MARKET__ORIGIN_PORTS:
				return originPorts != null && !originPorts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of FOBSalesMarketImpl

// finish type fixing
