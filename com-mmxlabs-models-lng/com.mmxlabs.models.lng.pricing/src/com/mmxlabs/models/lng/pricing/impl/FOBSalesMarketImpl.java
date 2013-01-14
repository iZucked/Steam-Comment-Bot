/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.FOBSalesMarket;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.commercial.SalesContract;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>FOB Sales Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.FOBSalesMarketImpl#getLoadPort <em>Load Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.FOBSalesMarketImpl#getContract <em>Contract</em>}</li>
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
	 * The cached value of the '{@link #getContract() <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContract()
	 * @generated
	 * @ordered
	 */
	protected SalesContract contract;

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
		return PricingPackage.Literals.FOB_SALES_MARKET;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.FOB_SALES_MARKET__LOAD_PORT, oldLoadPort, loadPort));
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
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.FOB_SALES_MARKET__LOAD_PORT, oldLoadPort, loadPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SalesContract getContract() {
		if (contract != null && contract.eIsProxy()) {
			InternalEObject oldContract = (InternalEObject)contract;
			contract = (SalesContract)eResolveProxy(oldContract);
			if (contract != oldContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.FOB_SALES_MARKET__CONTRACT, oldContract, contract));
			}
		}
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SalesContract basicGetContract() {
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContract(SalesContract newContract) {
		SalesContract oldContract = contract;
		contract = newContract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.FOB_SALES_MARKET__CONTRACT, oldContract, contract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.FOB_SALES_MARKET__LOAD_PORT:
				if (resolve) return getLoadPort();
				return basicGetLoadPort();
			case PricingPackage.FOB_SALES_MARKET__CONTRACT:
				if (resolve) return getContract();
				return basicGetContract();
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
			case PricingPackage.FOB_SALES_MARKET__LOAD_PORT:
				setLoadPort((Port)newValue);
				return;
			case PricingPackage.FOB_SALES_MARKET__CONTRACT:
				setContract((SalesContract)newValue);
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
			case PricingPackage.FOB_SALES_MARKET__LOAD_PORT:
				setLoadPort((Port)null);
				return;
			case PricingPackage.FOB_SALES_MARKET__CONTRACT:
				setContract((SalesContract)null);
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
			case PricingPackage.FOB_SALES_MARKET__LOAD_PORT:
				return loadPort != null;
			case PricingPackage.FOB_SALES_MARKET__CONTRACT:
				return contract != null;
		}
		return super.eIsSet(featureID);
	}

} // end of FOBSalesMarketImpl

// finish type fixing
