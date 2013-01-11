/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.FOBPurchasesMarket;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.commercial.PurchaseContract;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>FOB Purchases Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.FOBPurchasesMarketImpl#getNotionalPort <em>Notional Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.FOBPurchasesMarketImpl#getCv <em>Cv</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.FOBPurchasesMarketImpl#getContract <em>Contract</em>}</li>
 * </ul>
 * </p>
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
	 * The cached value of the '{@link #getContract() <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContract()
	 * @generated
	 * @ordered
	 */
	protected PurchaseContract contract;

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
		return PricingPackage.Literals.FOB_PURCHASES_MARKET;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT, oldNotionalPort, notionalPort));
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
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT, oldNotionalPort, notionalPort));
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
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.FOB_PURCHASES_MARKET__CV, oldCv, cv));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PurchaseContract getContract() {
		if (contract != null && contract.eIsProxy()) {
			InternalEObject oldContract = (InternalEObject)contract;
			contract = (PurchaseContract)eResolveProxy(oldContract);
			if (contract != oldContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.FOB_PURCHASES_MARKET__CONTRACT, oldContract, contract));
			}
		}
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PurchaseContract basicGetContract() {
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContract(PurchaseContract newContract) {
		PurchaseContract oldContract = contract;
		contract = newContract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.FOB_PURCHASES_MARKET__CONTRACT, oldContract, contract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT:
				if (resolve) return getNotionalPort();
				return basicGetNotionalPort();
			case PricingPackage.FOB_PURCHASES_MARKET__CV:
				return getCv();
			case PricingPackage.FOB_PURCHASES_MARKET__CONTRACT:
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
			case PricingPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT:
				setNotionalPort((Port)newValue);
				return;
			case PricingPackage.FOB_PURCHASES_MARKET__CV:
				setCv((Double)newValue);
				return;
			case PricingPackage.FOB_PURCHASES_MARKET__CONTRACT:
				setContract((PurchaseContract)newValue);
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
			case PricingPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT:
				setNotionalPort((Port)null);
				return;
			case PricingPackage.FOB_PURCHASES_MARKET__CV:
				setCv(CV_EDEFAULT);
				return;
			case PricingPackage.FOB_PURCHASES_MARKET__CONTRACT:
				setContract((PurchaseContract)null);
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
			case PricingPackage.FOB_PURCHASES_MARKET__NOTIONAL_PORT:
				return notionalPort != null;
			case PricingPackage.FOB_PURCHASES_MARKET__CV:
				return cv != CV_EDEFAULT;
			case PricingPackage.FOB_PURCHASES_MARKET__CONTRACT:
				return contract != null;
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
