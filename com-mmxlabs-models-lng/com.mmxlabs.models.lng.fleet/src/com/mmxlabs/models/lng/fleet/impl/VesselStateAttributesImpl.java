/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel State Attributes</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl#getNboRate <em>Nbo Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl#getIdleNBORate <em>Idle NBO Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl#getIdleBaseRate <em>Idle Base Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl#getInPortBaseRate <em>In Port Base Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl#getFuelConsumption <em>Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl#getServiceSpeed <em>Service Speed</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselStateAttributesImpl extends MMXObjectImpl implements VesselStateAttributes {
	/**
	 * The default value of the '{@link #getNboRate() <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNboRate()
	 * @generated
	 * @ordered
	 */
	protected static final double NBO_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getNboRate() <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNboRate()
	 * @generated
	 * @ordered
	 */
	protected double nboRate = NBO_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getIdleNBORate() <em>Idle NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdleNBORate()
	 * @generated
	 * @ordered
	 */
	protected static final double IDLE_NBO_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getIdleNBORate() <em>Idle NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdleNBORate()
	 * @generated
	 * @ordered
	 */
	protected double idleNBORate = IDLE_NBO_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getIdleBaseRate() <em>Idle Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdleBaseRate()
	 * @generated
	 * @ordered
	 */
	protected static final double IDLE_BASE_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getIdleBaseRate() <em>Idle Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdleBaseRate()
	 * @generated
	 * @ordered
	 */
	protected double idleBaseRate = IDLE_BASE_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getInPortBaseRate() <em>In Port Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInPortBaseRate()
	 * @generated
	 * @ordered
	 */
	protected static final double IN_PORT_BASE_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getInPortBaseRate() <em>In Port Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInPortBaseRate()
	 * @generated
	 * @ordered
	 */
	protected double inPortBaseRate = IN_PORT_BASE_RATE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFuelConsumption() <em>Fuel Consumption</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelConsumption()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelConsumption> fuelConsumption;

	/**
	 * The default value of the '{@link #getServiceSpeed() <em>Service Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final double SERVICE_SPEED_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getServiceSpeed() <em>Service Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceSpeed()
	 * @generated
	 * @ordered
	 */
	protected double serviceSpeed = SERVICE_SPEED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselStateAttributesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getNboRate() {
		return nboRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNboRate(double newNboRate) {
		double oldNboRate = nboRate;
		nboRate = newNboRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE, oldNboRate, nboRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getIdleNBORate() {
		return idleNBORate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdleNBORate(double newIdleNBORate) {
		double oldIdleNBORate = idleNBORate;
		idleNBORate = newIdleNBORate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, oldIdleNBORate, idleNBORate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getIdleBaseRate() {
		return idleBaseRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdleBaseRate(double newIdleBaseRate) {
		double oldIdleBaseRate = idleBaseRate;
		idleBaseRate = newIdleBaseRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, oldIdleBaseRate, idleBaseRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getInPortBaseRate() {
		return inPortBaseRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInPortBaseRate(double newInPortBaseRate) {
		double oldInPortBaseRate = inPortBaseRate;
		inPortBaseRate = newInPortBaseRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE, oldInPortBaseRate, inPortBaseRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FuelConsumption> getFuelConsumption() {
		if (fuelConsumption == null) {
			fuelConsumption = new EObjectContainmentEList<FuelConsumption>(FuelConsumption.class, this, FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION);
		}
		return fuelConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getServiceSpeed() {
		return serviceSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceSpeed(double newServiceSpeed) {
		double oldServiceSpeed = serviceSpeed;
		serviceSpeed = newServiceSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED, oldServiceSpeed, serviceSpeed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION:
				return ((InternalEList<?>)getFuelConsumption()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE:
				return getNboRate();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE:
				return getIdleNBORate();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE:
				return getIdleBaseRate();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE:
				return getInPortBaseRate();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION:
				return getFuelConsumption();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED:
				return getServiceSpeed();
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
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE:
				setNboRate((Double)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE:
				setIdleNBORate((Double)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE:
				setIdleBaseRate((Double)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE:
				setInPortBaseRate((Double)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION:
				getFuelConsumption().clear();
				getFuelConsumption().addAll((Collection<? extends FuelConsumption>)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED:
				setServiceSpeed((Double)newValue);
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
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE:
				setNboRate(NBO_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE:
				setIdleNBORate(IDLE_NBO_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE:
				setIdleBaseRate(IDLE_BASE_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE:
				setInPortBaseRate(IN_PORT_BASE_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION:
				getFuelConsumption().clear();
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED:
				setServiceSpeed(SERVICE_SPEED_EDEFAULT);
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
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE:
				return nboRate != NBO_RATE_EDEFAULT;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE:
				return idleNBORate != IDLE_NBO_RATE_EDEFAULT;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE:
				return idleBaseRate != IDLE_BASE_RATE_EDEFAULT;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE:
				return inPortBaseRate != IN_PORT_BASE_RATE_EDEFAULT;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION:
				return fuelConsumption != null && !fuelConsumption.isEmpty();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED:
				return serviceSpeed != SERVICE_SPEED_EDEFAULT;
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
		result.append(" (nboRate: ");
		result.append(nboRate);
		result.append(", idleNBORate: ");
		result.append(idleNBORate);
		result.append(", idleBaseRate: ");
		result.append(idleBaseRate);
		result.append(", inPortBaseRate: ");
		result.append(inPortBaseRate);
		result.append(", serviceSpeed: ");
		result.append(serviceSpeed);
		result.append(')');
		return result.toString();
	}

} // end of VesselStateAttributesImpl

// finish type fixing
