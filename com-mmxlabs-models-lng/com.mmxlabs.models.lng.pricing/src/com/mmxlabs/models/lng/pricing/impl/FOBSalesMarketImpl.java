

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.FOBSalesMarket;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.lng.port.Port;

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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.FOB_SALES_MARKET__LOAD_PORT:
				if (resolve) return getLoadPort();
				return basicGetLoadPort();
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
		}
		return super.eIsSet(featureID);
	}

} // end of FOBSalesMarketImpl

// finish type fixing
