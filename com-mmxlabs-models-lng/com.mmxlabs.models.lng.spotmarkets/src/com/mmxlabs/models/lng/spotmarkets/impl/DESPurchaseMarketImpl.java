

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.spotmarkets.impl;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.commercial.PurchaseContract;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DES Purchase Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.DESPurchaseMarketImpl#getCv <em>Cv</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.DESPurchaseMarketImpl#getDestinationPorts <em>Destination Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.DESPurchaseMarketImpl#getContract <em>Contract</em>}</li>
 * </ul>
 * </p>
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
	protected EList<APortSet> destinationPorts;

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
	public EList<APortSet> getDestinationPorts() {
		if (destinationPorts == null) {
			destinationPorts = new EObjectResolvingEList<APortSet>(APortSet.class, this, SpotMarketsPackage.DES_PURCHASE_MARKET__DESTINATION_PORTS);
		}
		return destinationPorts;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.DES_PURCHASE_MARKET__CONTRACT, oldContract, contract));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.DES_PURCHASE_MARKET__CONTRACT, oldContract, contract));
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
			case SpotMarketsPackage.DES_PURCHASE_MARKET__CONTRACT:
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
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SpotMarketsPackage.DES_PURCHASE_MARKET__CV:
				setCv((Double)newValue);
				return;
			case SpotMarketsPackage.DES_PURCHASE_MARKET__DESTINATION_PORTS:
				getDestinationPorts().clear();
				getDestinationPorts().addAll((Collection<? extends APortSet>)newValue);
				return;
			case SpotMarketsPackage.DES_PURCHASE_MARKET__CONTRACT:
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
			case SpotMarketsPackage.DES_PURCHASE_MARKET__CV:
				setCv(CV_EDEFAULT);
				return;
			case SpotMarketsPackage.DES_PURCHASE_MARKET__DESTINATION_PORTS:
				getDestinationPorts().clear();
				return;
			case SpotMarketsPackage.DES_PURCHASE_MARKET__CONTRACT:
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
			case SpotMarketsPackage.DES_PURCHASE_MARKET__CV:
				return cv != CV_EDEFAULT;
			case SpotMarketsPackage.DES_PURCHASE_MARKET__DESTINATION_PORTS:
				return destinationPorts != null && !destinationPorts.isEmpty();
			case SpotMarketsPackage.DES_PURCHASE_MARKET__CONTRACT:
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

} // end of DESPurchaseMarketImpl

// finish type fixing
