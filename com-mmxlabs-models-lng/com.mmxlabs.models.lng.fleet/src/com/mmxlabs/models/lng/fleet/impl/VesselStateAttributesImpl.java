/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.DelegatingEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.mmxcore.MMXObject;
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
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl#isFuelConsumptionOverride <em>Fuel Consumption Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl#getFuelConsumption <em>Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl#getServiceSpeed <em>Service Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl#getInPortNBORate <em>In Port NBO Rate</em>}</li>
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
	 * This is true if the Nbo Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean nboRateESet;

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
	 * This is true if the Idle NBO Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean idleNBORateESet;

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
	 * This is true if the Idle Base Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean idleBaseRateESet;

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
	 * This is true if the In Port Base Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean inPortBaseRateESet;

	/**
	 * The default value of the '{@link #isFuelConsumptionOverride() <em>Fuel Consumption Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFuelConsumptionOverride()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FUEL_CONSUMPTION_OVERRIDE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFuelConsumptionOverride() <em>Fuel Consumption Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFuelConsumptionOverride()
	 * @generated
	 * @ordered
	 */
	protected boolean fuelConsumptionOverride = FUEL_CONSUMPTION_OVERRIDE_EDEFAULT;

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
	 * This is true if the Service Speed attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean serviceSpeedESet;

	/**
	 * The default value of the '{@link #getInPortNBORate() <em>In Port NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInPortNBORate()
	 * @generated
	 * @ordered
	 */
	protected static final double IN_PORT_NBO_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getInPortNBORate() <em>In Port NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInPortNBORate()
	 * @generated
	 * @ordered
	 */
	protected double inPortNBORate = IN_PORT_NBO_RATE_EDEFAULT;

	/**
	 * This is true if the In Port NBO Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean inPortNBORateESet;

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
	@Override
	public double getNboRate() {
		return nboRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNboRate(double newNboRate) {
		double oldNboRate = nboRate;
		nboRate = newNboRate;
		boolean oldNboRateESet = nboRateESet;
		nboRateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE, oldNboRate, nboRate, !oldNboRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetNboRate() {
		double oldNboRate = nboRate;
		boolean oldNboRateESet = nboRateESet;
		nboRate = NBO_RATE_EDEFAULT;
		nboRateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE, oldNboRate, NBO_RATE_EDEFAULT, oldNboRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetNboRate() {
		return nboRateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getIdleNBORate() {
		return idleNBORate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrDelegateIdleNBORate() {
		if (eContainer() instanceof Vessel) {
			Vessel vessel = (Vessel) eContainer();
			if (vessel.getReference() != null&& !isSetIdleNBORate()) {
				VesselStateAttributes attribs = (VesselStateAttributes) vessel.getReference().eGet(eContainingFeature());
				if (attribs != null) {
					return attribs.getIdleNBORate();
				}
			}
		}
	 
		return getIdleNBORate();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIdleNBORate(double newIdleNBORate) {
		double oldIdleNBORate = idleNBORate;
		idleNBORate = newIdleNBORate;
		boolean oldIdleNBORateESet = idleNBORateESet;
		idleNBORateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, oldIdleNBORate, idleNBORate, !oldIdleNBORateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetIdleNBORate() {
		double oldIdleNBORate = idleNBORate;
		boolean oldIdleNBORateESet = idleNBORateESet;
		idleNBORate = IDLE_NBO_RATE_EDEFAULT;
		idleNBORateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, oldIdleNBORate, IDLE_NBO_RATE_EDEFAULT, oldIdleNBORateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetIdleNBORate() {
		return idleNBORateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getIdleBaseRate() {
		return idleBaseRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrDelegateIdleBaseRate() {
		if (eContainer() instanceof Vessel) {
			Vessel vessel = (Vessel) eContainer();
			if (vessel.getReference() != null && !isSetIdleBaseRate()) {
				VesselStateAttributes attribs = (VesselStateAttributes) vessel.getReference().eGet(eContainingFeature());
				if (attribs != null) {
					return attribs.getIdleBaseRate();
				}
			}
		}
	 
		return getIdleBaseRate();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIdleBaseRate(double newIdleBaseRate) {
		double oldIdleBaseRate = idleBaseRate;
		idleBaseRate = newIdleBaseRate;
		boolean oldIdleBaseRateESet = idleBaseRateESet;
		idleBaseRateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, oldIdleBaseRate, idleBaseRate, !oldIdleBaseRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetIdleBaseRate() {
		double oldIdleBaseRate = idleBaseRate;
		boolean oldIdleBaseRateESet = idleBaseRateESet;
		idleBaseRate = IDLE_BASE_RATE_EDEFAULT;
		idleBaseRateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, oldIdleBaseRate, IDLE_BASE_RATE_EDEFAULT, oldIdleBaseRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetIdleBaseRate() {
		return idleBaseRateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getInPortBaseRate() {
		return inPortBaseRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrDelegateInPortBaseRate() {
		if (eContainer() instanceof Vessel) {
			Vessel vessel = (Vessel) eContainer();
			if (vessel.getReference() != null && !isSetInPortBaseRate()) {
				VesselStateAttributes attribs = (VesselStateAttributes) vessel.getReference().eGet(eContainingFeature());
				if (attribs != null) {
					return attribs.getInPortBaseRate();
				}
			}
		}
	 
		return getInPortBaseRate();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInPortBaseRate(double newInPortBaseRate) {
		double oldInPortBaseRate = inPortBaseRate;
		inPortBaseRate = newInPortBaseRate;
		boolean oldInPortBaseRateESet = inPortBaseRateESet;
		inPortBaseRateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE, oldInPortBaseRate, inPortBaseRate, !oldInPortBaseRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetInPortBaseRate() {
		double oldInPortBaseRate = inPortBaseRate;
		boolean oldInPortBaseRateESet = inPortBaseRateESet;
		inPortBaseRate = IN_PORT_BASE_RATE_EDEFAULT;
		inPortBaseRateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE, oldInPortBaseRate, IN_PORT_BASE_RATE_EDEFAULT, oldInPortBaseRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetInPortBaseRate() {
		return inPortBaseRateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFuelConsumptionOverride() {
		return fuelConsumptionOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFuelConsumptionOverride(boolean newFuelConsumptionOverride) {
		boolean oldFuelConsumptionOverride = fuelConsumptionOverride;
		fuelConsumptionOverride = newFuelConsumptionOverride;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE, oldFuelConsumptionOverride, fuelConsumptionOverride));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public double getServiceSpeed() {
		return serviceSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<FuelConsumption> getVesselOrDelegateFuelConsumption() {
		if (eContainer() instanceof Vessel) {
			Vessel vessel = (Vessel) eContainer();
			if (vessel.getReference() != null && !isFuelConsumptionOverride()) {
				VesselStateAttributes attribs = (VesselStateAttributes) vessel.getReference().eGet(eContainingFeature());
				if (attribs != null) {
					return  new DelegatingEList.UnmodifiableEList<>(attribs.getFuelConsumption());
				}
			}
		}
	 
		return getFuelConsumption();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrDelegateServiceSpeed() {
		if (eContainer() instanceof Vessel) {
			Vessel vessel = (Vessel) eContainer();
			if (vessel.getReference() != null && !isSetServiceSpeed()) {
				VesselStateAttributes attribs = (VesselStateAttributes) vessel.getReference().eGet(eContainingFeature());
				if (attribs != null) {
					return attribs.getServiceSpeed();
				}
			}
		}
	 
		return getServiceSpeed();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setServiceSpeed(double newServiceSpeed) {
		double oldServiceSpeed = serviceSpeed;
		serviceSpeed = newServiceSpeed;
		boolean oldServiceSpeedESet = serviceSpeedESet;
		serviceSpeedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED, oldServiceSpeed, serviceSpeed, !oldServiceSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetServiceSpeed() {
		double oldServiceSpeed = serviceSpeed;
		boolean oldServiceSpeedESet = serviceSpeedESet;
		serviceSpeed = SERVICE_SPEED_EDEFAULT;
		serviceSpeedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED, oldServiceSpeed, SERVICE_SPEED_EDEFAULT, oldServiceSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetServiceSpeed() {
		return serviceSpeedESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getInPortNBORate() {
		return inPortNBORate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrDelegateInPortNBORate() {
		if (eContainer() instanceof Vessel) {
			Vessel vessel = (Vessel) eContainer();
			if (vessel.getReference() != null && !isSetInPortNBORate()) {
				VesselStateAttributes attribs = (VesselStateAttributes) vessel.getReference().eGet(eContainingFeature());
				if (attribs != null) {
					return attribs.getInPortNBORate();
				}
			}
		}
	 
		return getInPortNBORate();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInPortNBORate(double newInPortNBORate) {
		double oldInPortNBORate = inPortNBORate;
		inPortNBORate = newInPortNBORate;
		boolean oldInPortNBORateESet = inPortNBORateESet;
		inPortNBORateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE, oldInPortNBORate, inPortNBORate, !oldInPortNBORateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetInPortNBORate() {
		double oldInPortNBORate = inPortNBORate;
		boolean oldInPortNBORateESet = inPortNBORateESet;
		inPortNBORate = IN_PORT_NBO_RATE_EDEFAULT;
		inPortNBORateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE, oldInPortNBORate, IN_PORT_NBO_RATE_EDEFAULT, oldInPortNBORateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetInPortNBORate() {
		return inPortNBORateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrDelegateNBORate() {
		if (eContainer() instanceof Vessel) {
			Vessel vessel = (Vessel) eContainer();
			if (vessel.getReference() != null && !isSetNboRate()) {
				VesselStateAttributes attribs = (VesselStateAttributes) vessel.getReference().eGet(eContainingFeature());
				if (attribs != null) {
					return attribs.getNboRate();
				}
			}
		}
	 
		return getNboRate();
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
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE:
				return isFuelConsumptionOverride();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION:
				return getFuelConsumption();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED:
				return getServiceSpeed();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE:
				return getInPortNBORate();
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
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE:
				setFuelConsumptionOverride((Boolean)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION:
				getFuelConsumption().clear();
				getFuelConsumption().addAll((Collection<? extends FuelConsumption>)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED:
				setServiceSpeed((Double)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE:
				setInPortNBORate((Double)newValue);
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
				unsetNboRate();
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE:
				unsetIdleNBORate();
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE:
				unsetIdleBaseRate();
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE:
				unsetInPortBaseRate();
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE:
				setFuelConsumptionOverride(FUEL_CONSUMPTION_OVERRIDE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION:
				getFuelConsumption().clear();
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED:
				unsetServiceSpeed();
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE:
				unsetInPortNBORate();
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
				return isSetNboRate();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE:
				return isSetIdleNBORate();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE:
				return isSetIdleBaseRate();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE:
				return isSetInPortBaseRate();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE:
				return fuelConsumptionOverride != FUEL_CONSUMPTION_OVERRIDE_EDEFAULT;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION:
				return fuelConsumption != null && !fuelConsumption.isEmpty();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED:
				return isSetServiceSpeed();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE:
				return isSetInPortNBORate();
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (nboRate: ");
		if (nboRateESet) result.append(nboRate); else result.append("<unset>");
		result.append(", idleNBORate: ");
		if (idleNBORateESet) result.append(idleNBORate); else result.append("<unset>");
		result.append(", idleBaseRate: ");
		if (idleBaseRateESet) result.append(idleBaseRate); else result.append("<unset>");
		result.append(", inPortBaseRate: ");
		if (inPortBaseRateESet) result.append(inPortBaseRate); else result.append("<unset>");
		result.append(", fuelConsumptionOverride: ");
		result.append(fuelConsumptionOverride);
		result.append(", serviceSpeed: ");
		if (serviceSpeedESet) result.append(serviceSpeed); else result.append("<unset>");
		result.append(", inPortNBORate: ");
		if (inPortNBORateESet) result.append(inPortNBORate); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}
	
	@Override
	public boolean eIsSet(EStructuralFeature eFeature) {
		EStructuralFeature eStructuralFeature = eClass().getEStructuralFeature(eFeature.getName() + "Override");
		if (eStructuralFeature != null) {
			if (eContainer() instanceof Vessel) {
				Vessel vessel = (Vessel) eContainer();
				if (vessel.getReference() != null) {
					return  (Boolean)eGet(eStructuralFeature);
				}
			}
		}
		return super.eIsSet(eFeature);
	}
	
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		FleetPackage fleetPackage = FleetPackage.eINSTANCE;
		if (eContainer() instanceof Vessel) {
			Vessel vessel = (Vessel) eContainer();
			if (vessel.getReference() != null) {
				Vessel other = vessel.getReference();
				EStructuralFeature eStructuralFeature = eClass().getEStructuralFeature(feature.getName() + "Override");
				if (eStructuralFeature != null) {
					return new DelegateInformation((a) -> (MMXObject)other.eGet(eContainingFeature()),fleetPackage.getVessel_Reference(), feature, null);
				} else {
					if (feature.isUnsettable()) {
						return new DelegateInformation((a) -> (MMXObject)other.eGet(eContainingFeature()), fleetPackage.getVessel_Reference(), feature, null);
					}
				}
			} else {
				EStructuralFeature eStructuralFeature = eClass().getEStructuralFeature(feature.getName() + "Override");
				if (eStructuralFeature != null) {
					return new DelegateInformation( (a) -> null , fleetPackage.getVessel_Reference(), null, null);
				} else {
					if (feature.isUnsettable()) {
						return new DelegateInformation( (a) -> null, fleetPackage.getVessel_Reference(), null, null);
					}
				}
			}
		}	
		return super.getUnsetValueOrDelegate(feature);
	}	
	public Object eGetWithDefault(EStructuralFeature feature) {
		
		if ((feature == FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION ||feature.isUnsettable()) && !eIsSet(feature)) {
			return getUnsetValue(feature);
		} else {
			return eGet(feature);
		}
	}

} // end of VesselStateAttributesImpl

// finish type fixing
