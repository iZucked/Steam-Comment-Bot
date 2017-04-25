/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.impl.AVesselSetImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Class</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getInaccessibleRoutes <em>Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getBaseFuel <em>Base Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getFillCapacity <em>Fill Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getLadenAttributes <em>Laden Attributes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getBallastAttributes <em>Ballast Attributes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getMinSpeed <em>Min Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getMaxSpeed <em>Max Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getMinHeel <em>Min Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getWarmingTime <em>Warming Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getCoolingVolume <em>Cooling Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getRouteParameters <em>Route Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getPilotLightRate <em>Pilot Light Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getMinBaseFuelConsumption <em>Min Base Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#isHasReliqCapability <em>Has Reliq Capability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl#getScnt <em>Scnt</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselClassImpl extends AVesselSetImpl<Vessel> implements VesselClass {
	/**
	 * The cached value of the '{@link #getInaccessiblePorts() <em>Inaccessible Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInaccessiblePorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> inaccessiblePorts;

	/**
	 * The cached value of the '{@link #getInaccessibleRoutes() <em>Inaccessible Routes</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInaccessibleRoutes()
	 * @generated
	 * @ordered
	 */
	protected EList<RouteOption> inaccessibleRoutes;

	/**
	 * The cached value of the '{@link #getBaseFuel() <em>Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuel()
	 * @generated
	 * @ordered
	 */
	protected BaseFuel baseFuel;

	/**
	 * The default value of the '{@link #getCapacity() <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final int CAPACITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCapacity() <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacity()
	 * @generated
	 * @ordered
	 */
	protected int capacity = CAPACITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getFillCapacity() <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFillCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final double FILL_CAPACITY_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getFillCapacity() <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFillCapacity()
	 * @generated
	 * @ordered
	 */
	protected double fillCapacity = FILL_CAPACITY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLadenAttributes() <em>Laden Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenAttributes()
	 * @generated
	 * @ordered
	 */
	protected VesselStateAttributes ladenAttributes;

	/**
	 * The cached value of the '{@link #getBallastAttributes() <em>Ballast Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastAttributes()
	 * @generated
	 * @ordered
	 */
	protected VesselStateAttributes ballastAttributes;

	/**
	 * The default value of the '{@link #getMinSpeed() <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final double MIN_SPEED_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMinSpeed() <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinSpeed()
	 * @generated
	 * @ordered
	 */
	protected double minSpeed = MIN_SPEED_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxSpeed() <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final double MAX_SPEED_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMaxSpeed() <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSpeed()
	 * @generated
	 * @ordered
	 */
	protected double maxSpeed = MAX_SPEED_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinHeel() <em>Min Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinHeel()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_HEEL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinHeel() <em>Min Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinHeel()
	 * @generated
	 * @ordered
	 */
	protected int minHeel = MIN_HEEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getWarmingTime() <em>Warming Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWarmingTime()
	 * @generated
	 * @ordered
	 */
	protected static final int WARMING_TIME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWarmingTime() <em>Warming Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWarmingTime()
	 * @generated
	 * @ordered
	 */
	protected int warmingTime = WARMING_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getCoolingVolume() <em>Cooling Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCoolingVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int COOLING_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCoolingVolume() <em>Cooling Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCoolingVolume()
	 * @generated
	 * @ordered
	 */
	protected int coolingVolume = COOLING_VOLUME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRouteParameters() <em>Route Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselClassRouteParameters> routeParameters;

	/**
	 * The default value of the '{@link #getPilotLightRate() <em>Pilot Light Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPilotLightRate()
	 * @generated
	 * @ordered
	 */
	protected static final double PILOT_LIGHT_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPilotLightRate() <em>Pilot Light Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPilotLightRate()
	 * @generated
	 * @ordered
	 */
	protected double pilotLightRate = PILOT_LIGHT_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinBaseFuelConsumption() <em>Min Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinBaseFuelConsumption()
	 * @generated
	 * @ordered
	 */
	protected static final double MIN_BASE_FUEL_CONSUMPTION_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMinBaseFuelConsumption() <em>Min Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinBaseFuelConsumption()
	 * @generated
	 * @ordered
	 */
	protected double minBaseFuelConsumption = MIN_BASE_FUEL_CONSUMPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #isHasReliqCapability() <em>Has Reliq Capability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHasReliqCapability()
	 * @generated
	 * @ordered
	 */
	protected static final boolean HAS_RELIQ_CAPABILITY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isHasReliqCapability() <em>Has Reliq Capability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHasReliqCapability()
	 * @generated
	 * @ordered
	 */
	protected boolean hasReliqCapability = HAS_RELIQ_CAPABILITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getScnt() <em>Scnt</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScnt()
	 * @generated
	 * @ordered
	 */
	protected static final int SCNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getScnt() <em>Scnt</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScnt()
	 * @generated
	 * @ordered
	 */
	protected int scnt = SCNT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselClassImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL_CLASS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getInaccessiblePorts() {
		if (inaccessiblePorts == null) {
			inaccessiblePorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS);
		}
		return inaccessiblePorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseFuel getBaseFuel() {
		if (baseFuel != null && baseFuel.eIsProxy()) {
			InternalEObject oldBaseFuel = (InternalEObject)baseFuel;
			baseFuel = (BaseFuel)eResolveProxy(oldBaseFuel);
			if (baseFuel != oldBaseFuel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL_CLASS__BASE_FUEL, oldBaseFuel, baseFuel));
			}
		}
		return baseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseFuel basicGetBaseFuel() {
		return baseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseFuel(BaseFuel newBaseFuel) {
		BaseFuel oldBaseFuel = baseFuel;
		baseFuel = newBaseFuel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__BASE_FUEL, oldBaseFuel, baseFuel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCapacity(int newCapacity) {
		int oldCapacity = capacity;
		capacity = newCapacity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__CAPACITY, oldCapacity, capacity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getFillCapacity() {
		return fillCapacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFillCapacity(double newFillCapacity) {
		double oldFillCapacity = fillCapacity;
		fillCapacity = newFillCapacity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__FILL_CAPACITY, oldFillCapacity, fillCapacity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselStateAttributes getLadenAttributes() {
		return ladenAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLadenAttributes(VesselStateAttributes newLadenAttributes, NotificationChain msgs) {
		VesselStateAttributes oldLadenAttributes = ladenAttributes;
		ladenAttributes = newLadenAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, oldLadenAttributes, newLadenAttributes);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLadenAttributes(VesselStateAttributes newLadenAttributes) {
		if (newLadenAttributes != ladenAttributes) {
			NotificationChain msgs = null;
			if (ladenAttributes != null)
				msgs = ((InternalEObject)ladenAttributes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, null, msgs);
			if (newLadenAttributes != null)
				msgs = ((InternalEObject)newLadenAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, null, msgs);
			msgs = basicSetLadenAttributes(newLadenAttributes, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, newLadenAttributes, newLadenAttributes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselStateAttributes getBallastAttributes() {
		return ballastAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBallastAttributes(VesselStateAttributes newBallastAttributes, NotificationChain msgs) {
		VesselStateAttributes oldBallastAttributes = ballastAttributes;
		ballastAttributes = newBallastAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, oldBallastAttributes, newBallastAttributes);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastAttributes(VesselStateAttributes newBallastAttributes) {
		if (newBallastAttributes != ballastAttributes) {
			NotificationChain msgs = null;
			if (ballastAttributes != null)
				msgs = ((InternalEObject)ballastAttributes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, null, msgs);
			if (newBallastAttributes != null)
				msgs = ((InternalEObject)newBallastAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, null, msgs);
			msgs = basicSetBallastAttributes(newBallastAttributes, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, newBallastAttributes, newBallastAttributes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMinSpeed() {
		return minSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinSpeed(double newMinSpeed) {
		double oldMinSpeed = minSpeed;
		minSpeed = newMinSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__MIN_SPEED, oldMinSpeed, minSpeed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxSpeed(double newMaxSpeed) {
		double oldMaxSpeed = maxSpeed;
		maxSpeed = newMaxSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__MAX_SPEED, oldMaxSpeed, maxSpeed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinHeel() {
		return minHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinHeel(int newMinHeel) {
		int oldMinHeel = minHeel;
		minHeel = newMinHeel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__MIN_HEEL, oldMinHeel, minHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getWarmingTime() {
		return warmingTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWarmingTime(int newWarmingTime) {
		int oldWarmingTime = warmingTime;
		warmingTime = newWarmingTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__WARMING_TIME, oldWarmingTime, warmingTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCoolingVolume() {
		return coolingVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCoolingVolume(int newCoolingVolume) {
		int oldCoolingVolume = coolingVolume;
		coolingVolume = newCoolingVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__COOLING_VOLUME, oldCoolingVolume, coolingVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselClassRouteParameters> getRouteParameters() {
		if (routeParameters == null) {
			routeParameters = new EObjectContainmentEList<VesselClassRouteParameters>(VesselClassRouteParameters.class, this, FleetPackage.VESSEL_CLASS__ROUTE_PARAMETERS);
		}
		return routeParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getPilotLightRate() {
		return pilotLightRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPilotLightRate(double newPilotLightRate) {
		double oldPilotLightRate = pilotLightRate;
		pilotLightRate = newPilotLightRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__PILOT_LIGHT_RATE, oldPilotLightRate, pilotLightRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMinBaseFuelConsumption() {
		return minBaseFuelConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinBaseFuelConsumption(double newMinBaseFuelConsumption) {
		double oldMinBaseFuelConsumption = minBaseFuelConsumption;
		minBaseFuelConsumption = newMinBaseFuelConsumption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__MIN_BASE_FUEL_CONSUMPTION, oldMinBaseFuelConsumption, minBaseFuelConsumption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isHasReliqCapability() {
		return hasReliqCapability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHasReliqCapability(boolean newHasReliqCapability) {
		boolean oldHasReliqCapability = hasReliqCapability;
		hasReliqCapability = newHasReliqCapability;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__HAS_RELIQ_CAPABILITY, oldHasReliqCapability, hasReliqCapability));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<RouteOption> getInaccessibleRoutes() {
		if (inaccessibleRoutes == null) {
			inaccessibleRoutes = new EDataTypeUniqueEList<RouteOption>(RouteOption.class, this, FleetPackage.VESSEL_CLASS__INACCESSIBLE_ROUTES);
		}
		return inaccessibleRoutes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getScnt() {
		return scnt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScnt(int newScnt) {
		int oldScnt = scnt;
		scnt = newScnt;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__SCNT, oldScnt, scnt));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Vessel> collect(EList<AVesselSet<Vessel>> marked) {
		if (marked.contains(this)) {
			return ECollections.emptyEList();
		}
		final UniqueEList<Vessel> result = new UniqueEList<Vessel>();
		marked.add(this);
		
		final FleetModel myModel = (FleetModel) eContainer();
		for (final Vessel v : myModel.getVessels()) {
			if (v.getVesselClass() == this) {
				result.add(v);
			}
		}
		
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				return basicSetLadenAttributes(null, msgs);
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				return basicSetBallastAttributes(null, msgs);
			case FleetPackage.VESSEL_CLASS__ROUTE_PARAMETERS:
				return ((InternalEList<?>)getRouteParameters()).basicRemove(otherEnd, msgs);
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
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				return getInaccessiblePorts();
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_ROUTES:
				return getInaccessibleRoutes();
			case FleetPackage.VESSEL_CLASS__BASE_FUEL:
				if (resolve) return getBaseFuel();
				return basicGetBaseFuel();
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				return getCapacity();
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				return getFillCapacity();
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				return getLadenAttributes();
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				return getBallastAttributes();
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				return getMinSpeed();
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				return getMaxSpeed();
			case FleetPackage.VESSEL_CLASS__MIN_HEEL:
				return getMinHeel();
			case FleetPackage.VESSEL_CLASS__WARMING_TIME:
				return getWarmingTime();
			case FleetPackage.VESSEL_CLASS__COOLING_VOLUME:
				return getCoolingVolume();
			case FleetPackage.VESSEL_CLASS__ROUTE_PARAMETERS:
				return getRouteParameters();
			case FleetPackage.VESSEL_CLASS__PILOT_LIGHT_RATE:
				return getPilotLightRate();
			case FleetPackage.VESSEL_CLASS__MIN_BASE_FUEL_CONSUMPTION:
				return getMinBaseFuelConsumption();
			case FleetPackage.VESSEL_CLASS__HAS_RELIQ_CAPABILITY:
				return isHasReliqCapability();
			case FleetPackage.VESSEL_CLASS__SCNT:
				return getScnt();
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
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
				getInaccessiblePorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_ROUTES:
				getInaccessibleRoutes().clear();
				getInaccessibleRoutes().addAll((Collection<? extends RouteOption>)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__BASE_FUEL:
				setBaseFuel((BaseFuel)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				setCapacity((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				setFillCapacity((Double)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				setLadenAttributes((VesselStateAttributes)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				setBallastAttributes((VesselStateAttributes)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				setMinSpeed((Double)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				setMaxSpeed((Double)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_HEEL:
				setMinHeel((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__WARMING_TIME:
				setWarmingTime((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__COOLING_VOLUME:
				setCoolingVolume((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__ROUTE_PARAMETERS:
				getRouteParameters().clear();
				getRouteParameters().addAll((Collection<? extends VesselClassRouteParameters>)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__PILOT_LIGHT_RATE:
				setPilotLightRate((Double)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_BASE_FUEL_CONSUMPTION:
				setMinBaseFuelConsumption((Double)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__HAS_RELIQ_CAPABILITY:
				setHasReliqCapability((Boolean)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__SCNT:
				setScnt((Integer)newValue);
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
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
				return;
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_ROUTES:
				getInaccessibleRoutes().clear();
				return;
			case FleetPackage.VESSEL_CLASS__BASE_FUEL:
				setBaseFuel((BaseFuel)null);
				return;
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				setCapacity(CAPACITY_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				setFillCapacity(FILL_CAPACITY_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				setLadenAttributes((VesselStateAttributes)null);
				return;
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				setBallastAttributes((VesselStateAttributes)null);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				setMinSpeed(MIN_SPEED_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				setMaxSpeed(MAX_SPEED_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_HEEL:
				setMinHeel(MIN_HEEL_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__WARMING_TIME:
				setWarmingTime(WARMING_TIME_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__COOLING_VOLUME:
				setCoolingVolume(COOLING_VOLUME_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__ROUTE_PARAMETERS:
				getRouteParameters().clear();
				return;
			case FleetPackage.VESSEL_CLASS__PILOT_LIGHT_RATE:
				setPilotLightRate(PILOT_LIGHT_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_BASE_FUEL_CONSUMPTION:
				setMinBaseFuelConsumption(MIN_BASE_FUEL_CONSUMPTION_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__HAS_RELIQ_CAPABILITY:
				setHasReliqCapability(HAS_RELIQ_CAPABILITY_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__SCNT:
				setScnt(SCNT_EDEFAULT);
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
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				return inaccessiblePorts != null && !inaccessiblePorts.isEmpty();
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_ROUTES:
				return inaccessibleRoutes != null && !inaccessibleRoutes.isEmpty();
			case FleetPackage.VESSEL_CLASS__BASE_FUEL:
				return baseFuel != null;
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				return capacity != CAPACITY_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				return fillCapacity != FILL_CAPACITY_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				return ladenAttributes != null;
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				return ballastAttributes != null;
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				return minSpeed != MIN_SPEED_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				return maxSpeed != MAX_SPEED_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__MIN_HEEL:
				return minHeel != MIN_HEEL_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__WARMING_TIME:
				return warmingTime != WARMING_TIME_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__COOLING_VOLUME:
				return coolingVolume != COOLING_VOLUME_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__ROUTE_PARAMETERS:
				return routeParameters != null && !routeParameters.isEmpty();
			case FleetPackage.VESSEL_CLASS__PILOT_LIGHT_RATE:
				return pilotLightRate != PILOT_LIGHT_RATE_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__MIN_BASE_FUEL_CONSUMPTION:
				return minBaseFuelConsumption != MIN_BASE_FUEL_CONSUMPTION_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__HAS_RELIQ_CAPABILITY:
				return hasReliqCapability != HAS_RELIQ_CAPABILITY_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__SCNT:
				return scnt != SCNT_EDEFAULT;
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
		result.append(" (inaccessibleRoutes: ");
		result.append(inaccessibleRoutes);
		result.append(", capacity: ");
		result.append(capacity);
		result.append(", fillCapacity: ");
		result.append(fillCapacity);
		result.append(", minSpeed: ");
		result.append(minSpeed);
		result.append(", maxSpeed: ");
		result.append(maxSpeed);
		result.append(", minHeel: ");
		result.append(minHeel);
		result.append(", warmingTime: ");
		result.append(warmingTime);
		result.append(", coolingVolume: ");
		result.append(coolingVolume);
		result.append(", pilotLightRate: ");
		result.append(pilotLightRate);
		result.append(", minBaseFuelConsumption: ");
		result.append(minBaseFuelConsumption);
		result.append(", hasReliqCapability: ");
		result.append(hasReliqCapability);
		result.append(", scnt: ");
		result.append(scnt);
		result.append(')');
		return result.toString();
	}

} // end of VesselClassImpl

// finish type fixing
