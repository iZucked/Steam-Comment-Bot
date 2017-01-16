/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.APortSet;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DES Purchase Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.DESPurchaseMarketImpl#getCv <em>Cv</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.DESPurchaseMarketImpl#getDestinationPorts <em>Destination Ports</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DESPurchaseMarketImpl extends SpotMarketImpl implements DESPurchaseMarket {
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
	 * The cached value of the '{@link #getDestinationPorts() <em>Destination Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> destinationPorts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DESPurchaseMarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.DES_PURCHASE_MARKET;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.DES_PURCHASE_MARKET__CV, oldCv, cv));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getDestinationPorts() {
		if (destinationPorts == null) {
			destinationPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, SpotMarketsPackage.DES_PURCHASE_MARKET__DESTINATION_PORTS);
		}
		return destinationPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotMarketsPackage.DES_PURCHASE_MARKET__CV:
				return getCv();
			case SpotMarketsPackage.DES_PURCHASE_MARKET__DESTINATION_PORTS:
				return getDestinationPorts();
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
			case SpotMarketsPackage.DES_PURCHASE_MARKET__CV:
				setCv((Double)newValue);
				return;
			case SpotMarketsPackage.DES_PURCHASE_MARKET__DESTINATION_PORTS:
				getDestinationPorts().clear();
				getDestinationPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
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
			case SpotMarketsPackage.DES_PURCHASE_MARKET__CV:
				setCv(CV_EDEFAULT);
				return;
			case SpotMarketsPackage.DES_PURCHASE_MARKET__DESTINATION_PORTS:
				getDestinationPorts().clear();
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
			case SpotMarketsPackage.DES_PURCHASE_MARKET__CV:
				return cv != CV_EDEFAULT;
			case SpotMarketsPackage.DES_PURCHASE_MARKET__DESTINATION_PORTS:
				return destinationPorts != null && !destinationPorts.isEmpty();
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

} // end of DESPurchaseMarketImpl

// finish type fixing
