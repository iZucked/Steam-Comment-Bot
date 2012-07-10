/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.pricing.SpotType;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.impl.ASpotMarketImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spot Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl#getAvailability <em>Availability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl#getNotionalPort <em>Notional Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl#getContract <em>Contract</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SpotMarketImpl extends ASpotMarketImpl implements SpotMarket {
	/**
	 * The cached value of the '{@link #getAvailability() <em>Availability</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAvailability()
	 * @generated
	 * @ordered
	 */
	protected Index<Integer> availability;

	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet> ports;

	/**
	 * The default value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_QUANTITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected int minQuantity = MIN_QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_QUANTITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected int maxQuantity = MAX_QUANTITY_EDEFAULT;

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
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final SpotType TYPE_EDEFAULT = SpotType.FOB_SALE;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected SpotType type = TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getContract() <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContract()
	 * @generated
	 * @ordered
	 */
	protected Contract contract;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotMarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.SPOT_MARKET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public Index<Integer> getAvailability() {
		if (availability != null && availability.eIsProxy()) {
			InternalEObject oldAvailability = (InternalEObject)availability;
			availability = (Index<Integer>)eResolveProxy(oldAvailability);
			if (availability != oldAvailability) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.SPOT_MARKET__AVAILABILITY, oldAvailability, availability));
			}
		}
		return availability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index<Integer> basicGetAvailability() {
		return availability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAvailability(Index<Integer> newAvailability) {
		Index<Integer> oldAvailability = availability;
		availability = newAvailability;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SPOT_MARKET__AVAILABILITY, oldAvailability, availability));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet> getPorts() {
		if (ports == null) {
			ports = new EObjectResolvingEList<APortSet>(APortSet.class, this, PricingPackage.SPOT_MARKET__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinQuantity() {
		return minQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinQuantity(int newMinQuantity) {
		int oldMinQuantity = minQuantity;
		minQuantity = newMinQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SPOT_MARKET__MIN_QUANTITY, oldMinQuantity, minQuantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxQuantity() {
		return maxQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxQuantity(int newMaxQuantity) {
		int oldMaxQuantity = maxQuantity;
		maxQuantity = newMaxQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SPOT_MARKET__MAX_QUANTITY, oldMaxQuantity, maxQuantity));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.SPOT_MARKET__NOTIONAL_PORT, oldNotionalPort, notionalPort));
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
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SPOT_MARKET__NOTIONAL_PORT, oldNotionalPort, notionalPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(SpotType newType) {
		SpotType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SPOT_MARKET__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Contract getContract() {
		if (contract != null && contract.eIsProxy()) {
			InternalEObject oldContract = (InternalEObject)contract;
			contract = (Contract)eResolveProxy(oldContract);
			if (contract != oldContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.SPOT_MARKET__CONTRACT, oldContract, contract));
			}
		}
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Contract basicGetContract() {
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContract(Contract newContract) {
		Contract oldContract = contract;
		contract = newContract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SPOT_MARKET__CONTRACT, oldContract, contract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.SPOT_MARKET__AVAILABILITY:
				if (resolve) return getAvailability();
				return basicGetAvailability();
			case PricingPackage.SPOT_MARKET__PORTS:
				return getPorts();
			case PricingPackage.SPOT_MARKET__MIN_QUANTITY:
				return getMinQuantity();
			case PricingPackage.SPOT_MARKET__MAX_QUANTITY:
				return getMaxQuantity();
			case PricingPackage.SPOT_MARKET__NOTIONAL_PORT:
				if (resolve) return getNotionalPort();
				return basicGetNotionalPort();
			case PricingPackage.SPOT_MARKET__TYPE:
				return getType();
			case PricingPackage.SPOT_MARKET__CONTRACT:
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
			case PricingPackage.SPOT_MARKET__AVAILABILITY:
				setAvailability((Index<Integer>)newValue);
				return;
			case PricingPackage.SPOT_MARKET__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends APortSet>)newValue);
				return;
			case PricingPackage.SPOT_MARKET__MIN_QUANTITY:
				setMinQuantity((Integer)newValue);
				return;
			case PricingPackage.SPOT_MARKET__MAX_QUANTITY:
				setMaxQuantity((Integer)newValue);
				return;
			case PricingPackage.SPOT_MARKET__NOTIONAL_PORT:
				setNotionalPort((Port)newValue);
				return;
			case PricingPackage.SPOT_MARKET__TYPE:
				setType((SpotType)newValue);
				return;
			case PricingPackage.SPOT_MARKET__CONTRACT:
				setContract((Contract)newValue);
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
			case PricingPackage.SPOT_MARKET__AVAILABILITY:
				setAvailability((Index<Integer>)null);
				return;
			case PricingPackage.SPOT_MARKET__PORTS:
				getPorts().clear();
				return;
			case PricingPackage.SPOT_MARKET__MIN_QUANTITY:
				setMinQuantity(MIN_QUANTITY_EDEFAULT);
				return;
			case PricingPackage.SPOT_MARKET__MAX_QUANTITY:
				setMaxQuantity(MAX_QUANTITY_EDEFAULT);
				return;
			case PricingPackage.SPOT_MARKET__NOTIONAL_PORT:
				setNotionalPort((Port)null);
				return;
			case PricingPackage.SPOT_MARKET__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case PricingPackage.SPOT_MARKET__CONTRACT:
				setContract((Contract)null);
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
			case PricingPackage.SPOT_MARKET__AVAILABILITY:
				return availability != null;
			case PricingPackage.SPOT_MARKET__PORTS:
				return ports != null && !ports.isEmpty();
			case PricingPackage.SPOT_MARKET__MIN_QUANTITY:
				return minQuantity != MIN_QUANTITY_EDEFAULT;
			case PricingPackage.SPOT_MARKET__MAX_QUANTITY:
				return maxQuantity != MAX_QUANTITY_EDEFAULT;
			case PricingPackage.SPOT_MARKET__NOTIONAL_PORT:
				return notionalPort != null;
			case PricingPackage.SPOT_MARKET__TYPE:
				return type != TYPE_EDEFAULT;
			case PricingPackage.SPOT_MARKET__CONTRACT:
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
		result.append(" (minQuantity: ");
		result.append(minQuantity);
		result.append(", maxQuantity: ");
		result.append(maxQuantity);
		result.append(", type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} // end of SpotMarketImpl

// finish type fixing
