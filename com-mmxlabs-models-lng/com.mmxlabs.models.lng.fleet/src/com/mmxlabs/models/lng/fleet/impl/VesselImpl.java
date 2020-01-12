/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.DelegatingEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.impl.AVesselSetImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Vessel</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getShortName <em>Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getIMO <em>IMO</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getReference <em>Reference</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getFillCapacity <em>Fill Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getScnt <em>Scnt</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getBaseFuel <em>Base Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getInPortBaseFuel <em>In Port Base Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getPilotLightBaseFuel <em>Pilot Light Base Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getIdleBaseFuel <em>Idle Base Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getPilotLightRate <em>Pilot Light Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getSafetyHeel <em>Safety Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getWarmingTime <em>Warming Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getCoolingVolume <em>Cooling Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getCoolingTime <em>Cooling Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getPurgeVolume <em>Purge Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getPurgeTime <em>Purge Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getLadenAttributes <em>Laden Attributes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getBallastAttributes <em>Ballast Attributes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getMinSpeed <em>Min Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getMaxSpeed <em>Max Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#isInaccessiblePortsOverride <em>Inaccessible Ports Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#isInaccessibleRoutesOverride <em>Inaccessible Routes Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getInaccessibleRoutes <em>Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#isRouteParametersOverride <em>Route Parameters Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getRouteParameters <em>Route Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getMinBaseFuelConsumption <em>Min Base Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#isHasReliqCapabilityOverride <em>Has Reliq Capability Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#isHasReliqCapability <em>Has Reliq Capability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getNotes <em>Notes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getMmxId <em>Mmx Id</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselImpl extends AVesselSetImpl<Vessel> implements Vessel {
	/**
	 * The default value of the '{@link #getShortName() <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShortName()
	 * @generated
	 * @ordered
	 */
	protected static final String SHORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getShortName() <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShortName()
	 * @generated
	 * @ordered
	 */
	protected String shortName = SHORT_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getIMO() <em>IMO</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIMO()
	 * @generated
	 * @ordered
	 */
	protected static final String IMO_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIMO() <em>IMO</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIMO()
	 * @generated
	 * @ordered
	 */
	protected String imo = IMO_EDEFAULT;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReference() <em>Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReference()
	 * @generated
	 * @ordered
	 */
	protected Vessel reference;

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
	 * This is true if the Capacity attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean capacityESet;

	/**
	 * The default value of the '{@link #getFillCapacity() <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFillCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final double FILL_CAPACITY_EDEFAULT = 0.0;

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
	 * This is true if the Fill Capacity attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean fillCapacityESet;

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
	 * This is true if the Scnt attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean scntESet;

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
	 * This is true if the Base Fuel reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean baseFuelESet;

	/**
	 * The cached value of the '{@link #getInPortBaseFuel() <em>In Port Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInPortBaseFuel()
	 * @generated
	 * @ordered
	 */
	protected BaseFuel inPortBaseFuel;

	/**
	 * This is true if the In Port Base Fuel reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean inPortBaseFuelESet;

	/**
	 * The cached value of the '{@link #getPilotLightBaseFuel() <em>Pilot Light Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPilotLightBaseFuel()
	 * @generated
	 * @ordered
	 */
	protected BaseFuel pilotLightBaseFuel;

	/**
	 * This is true if the Pilot Light Base Fuel reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean pilotLightBaseFuelESet;

	/**
	 * The cached value of the '{@link #getIdleBaseFuel() <em>Idle Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdleBaseFuel()
	 * @generated
	 * @ordered
	 */
	protected BaseFuel idleBaseFuel;

	/**
	 * This is true if the Idle Base Fuel reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean idleBaseFuelESet;

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
	 * This is true if the Pilot Light Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean pilotLightRateESet;

	/**
	 * The default value of the '{@link #getSafetyHeel() <em>Safety Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSafetyHeel()
	 * @generated
	 * @ordered
	 */
	protected static final int SAFETY_HEEL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSafetyHeel() <em>Safety Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSafetyHeel()
	 * @generated
	 * @ordered
	 */
	protected int safetyHeel = SAFETY_HEEL_EDEFAULT;

	/**
	 * This is true if the Safety Heel attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean safetyHeelESet;

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
	 * This is true if the Warming Time attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean warmingTimeESet;

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
	 * This is true if the Cooling Volume attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean coolingVolumeESet;

	/**
	 * The default value of the '{@link #getCoolingTime() <em>Cooling Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCoolingTime()
	 * @generated
	 * @ordered
	 */
	protected static final int COOLING_TIME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCoolingTime() <em>Cooling Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCoolingTime()
	 * @generated
	 * @ordered
	 */
	protected int coolingTime = COOLING_TIME_EDEFAULT;

	/**
	 * This is true if the Cooling Time attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean coolingTimeESet;

	/**
	 * The default value of the '{@link #getPurgeVolume() <em>Purge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPurgeVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int PURGE_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPurgeVolume() <em>Purge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPurgeVolume()
	 * @generated
	 * @ordered
	 */
	protected int purgeVolume = PURGE_VOLUME_EDEFAULT;

	/**
	 * This is true if the Purge Volume attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean purgeVolumeESet;

	/**
	 * The default value of the '{@link #getPurgeTime() <em>Purge Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPurgeTime()
	 * @generated
	 * @ordered
	 */
	protected static final int PURGE_TIME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPurgeTime() <em>Purge Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPurgeTime()
	 * @generated
	 * @ordered
	 */
	protected int purgeTime = PURGE_TIME_EDEFAULT;

	/**
	 * This is true if the Purge Time attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean purgeTimeESet;

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
	 * This is true if the Min Speed attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minSpeedESet;

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
	 * This is true if the Max Speed attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxSpeedESet;

	/**
	 * The default value of the '{@link #isInaccessiblePortsOverride() <em>Inaccessible Ports Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInaccessiblePortsOverride()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INACCESSIBLE_PORTS_OVERRIDE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isInaccessiblePortsOverride() <em>Inaccessible Ports Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInaccessiblePortsOverride()
	 * @generated
	 * @ordered
	 */
	protected boolean inaccessiblePortsOverride = INACCESSIBLE_PORTS_OVERRIDE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInaccessiblePorts() <em>Inaccessible Ports</em>}' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getInaccessiblePorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> inaccessiblePorts;

	/**
	 * The default value of the '{@link #isInaccessibleRoutesOverride() <em>Inaccessible Routes Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInaccessibleRoutesOverride()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INACCESSIBLE_ROUTES_OVERRIDE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isInaccessibleRoutesOverride() <em>Inaccessible Routes Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInaccessibleRoutesOverride()
	 * @generated
	 * @ordered
	 */
	protected boolean inaccessibleRoutesOverride = INACCESSIBLE_ROUTES_OVERRIDE_EDEFAULT;

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
	 * The default value of the '{@link #isRouteParametersOverride() <em>Route Parameters Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRouteParametersOverride()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ROUTE_PARAMETERS_OVERRIDE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRouteParametersOverride() <em>Route Parameters Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRouteParametersOverride()
	 * @generated
	 * @ordered
	 */
	protected boolean routeParametersOverride = ROUTE_PARAMETERS_OVERRIDE_EDEFAULT;

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
	 * This is true if the Min Base Fuel Consumption attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minBaseFuelConsumptionESet;

	/**
	 * The default value of the '{@link #isHasReliqCapabilityOverride() <em>Has Reliq Capability Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHasReliqCapabilityOverride()
	 * @generated
	 * @ordered
	 */
	protected static final boolean HAS_RELIQ_CAPABILITY_OVERRIDE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isHasReliqCapabilityOverride() <em>Has Reliq Capability Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHasReliqCapabilityOverride()
	 * @generated
	 * @ordered
	 */
	protected boolean hasReliqCapabilityOverride = HAS_RELIQ_CAPABILITY_OVERRIDE_EDEFAULT;

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
	 * The default value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected static final String NOTES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected String notes = NOTES_EDEFAULT;

	/**
	 * The default value of the '{@link #getMmxId() <em>Mmx Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmxId()
	 * @generated
	 * @ordered
	 */
	protected static final String MMX_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMmxId() <em>Mmx Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmxId()
	 * @generated
	 * @ordered
	 */
	protected String mmxId = MMX_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getShortName() {
		return shortName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShortName(String newShortName) {
		String oldShortName = shortName;
		shortName = newShortName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__SHORT_NAME, oldShortName, shortName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getIMO() {
		return imo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIMO(String newIMO) {
		String oldIMO = imo;
		imo = newIMO;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__IMO, oldIMO, imo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Vessel getReference() {
		if (reference != null && reference.eIsProxy()) {
			InternalEObject oldReference = (InternalEObject)reference;
			reference = (Vessel)eResolveProxy(oldReference);
			if (reference != oldReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__REFERENCE, oldReference, reference));
			}
		}
		return reference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetReference() {
		return reference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReference(Vessel newReference) {
		Vessel oldReference = reference;
		reference = newReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__REFERENCE, oldReference, reference));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<APortSet<Port>> getVesselOrDelegateInaccessiblePorts() {
		if (getReference() != null && !isInaccessiblePortsOverride()) {
			return new DelegatingEList.UnmodifiableEList<>(getReference().getInaccessiblePorts());
		}
		return getInaccessiblePorts();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isInaccessibleRoutesOverride() {
		return inaccessibleRoutesOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInaccessibleRoutesOverride(boolean newInaccessibleRoutesOverride) {
		boolean oldInaccessibleRoutesOverride = inaccessibleRoutesOverride;
		inaccessibleRoutesOverride = newInaccessibleRoutesOverride;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE, oldInaccessibleRoutesOverride, inaccessibleRoutesOverride));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<RouteOption> getInaccessibleRoutes() {
		if (inaccessibleRoutes == null) {
			inaccessibleRoutes = new EDataTypeUniqueEList<RouteOption>(RouteOption.class, this, FleetPackage.VESSEL__INACCESSIBLE_ROUTES);
		}
		return inaccessibleRoutes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseFuel getBaseFuel() {
		if (baseFuel != null && baseFuel.eIsProxy()) {
			InternalEObject oldBaseFuel = (InternalEObject)baseFuel;
			baseFuel = (BaseFuel)eResolveProxy(oldBaseFuel);
			if (baseFuel != oldBaseFuel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__BASE_FUEL, oldBaseFuel, baseFuel));
			}
		}
		return baseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getVesselOrDelegateCapacity() {
		
		if (getReference() != null && !isSetCapacity()) {
			return getReference().getCapacity();
		}
		return getCapacity();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCapacity(int newCapacity) {
		int oldCapacity = capacity;
		capacity = newCapacity;
		boolean oldCapacityESet = capacityESet;
		capacityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__CAPACITY, oldCapacity, capacity, !oldCapacityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCapacity() {
		int oldCapacity = capacity;
		boolean oldCapacityESet = capacityESet;
		capacity = CAPACITY_EDEFAULT;
		capacityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__CAPACITY, oldCapacity, CAPACITY_EDEFAULT, oldCapacityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCapacity() {
		return capacityESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getFillCapacity() {
		return fillCapacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrDelegateFillCapacity() {
		if (getReference() != null && !isSetFillCapacity()) {
			return getReference().getFillCapacity();
		}
		return getFillCapacity();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFillCapacity(double newFillCapacity) {
		double oldFillCapacity = fillCapacity;
		fillCapacity = newFillCapacity;
		boolean oldFillCapacityESet = fillCapacityESet;
		fillCapacityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__FILL_CAPACITY, oldFillCapacity, fillCapacity, !oldFillCapacityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetFillCapacity() {
		double oldFillCapacity = fillCapacity;
		boolean oldFillCapacityESet = fillCapacityESet;
		fillCapacity = FILL_CAPACITY_EDEFAULT;
		fillCapacityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__FILL_CAPACITY, oldFillCapacity, FILL_CAPACITY_EDEFAULT, oldFillCapacityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetFillCapacity() {
		return fillCapacityESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__LADEN_ATTRIBUTES, oldLadenAttributes, newLadenAttributes);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenAttributes(VesselStateAttributes newLadenAttributes) {
		if (newLadenAttributes != ladenAttributes) {
			NotificationChain msgs = null;
			if (ladenAttributes != null)
				msgs = ((InternalEObject)ladenAttributes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__LADEN_ATTRIBUTES, null, msgs);
			if (newLadenAttributes != null)
				msgs = ((InternalEObject)newLadenAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__LADEN_ATTRIBUTES, null, msgs);
			msgs = basicSetLadenAttributes(newLadenAttributes, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__LADEN_ATTRIBUTES, newLadenAttributes, newLadenAttributes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__BALLAST_ATTRIBUTES, oldBallastAttributes, newBallastAttributes);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastAttributes(VesselStateAttributes newBallastAttributes) {
		if (newBallastAttributes != ballastAttributes) {
			NotificationChain msgs = null;
			if (ballastAttributes != null)
				msgs = ((InternalEObject)ballastAttributes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__BALLAST_ATTRIBUTES, null, msgs);
			if (newBallastAttributes != null)
				msgs = ((InternalEObject)newBallastAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__BALLAST_ATTRIBUTES, null, msgs);
			msgs = basicSetBallastAttributes(newBallastAttributes, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__BALLAST_ATTRIBUTES, newBallastAttributes, newBallastAttributes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getMinSpeed() {
		return minSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrDelegateMinSpeed() {
		if (getReference() != null && !isSetMinSpeed()) {
			return getReference().getMinSpeed();
		}
		return getMinSpeed();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinSpeed(double newMinSpeed) {
		double oldMinSpeed = minSpeed;
		minSpeed = newMinSpeed;
		boolean oldMinSpeedESet = minSpeedESet;
		minSpeedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__MIN_SPEED, oldMinSpeed, minSpeed, !oldMinSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinSpeed() {
		double oldMinSpeed = minSpeed;
		boolean oldMinSpeedESet = minSpeedESet;
		minSpeed = MIN_SPEED_EDEFAULT;
		minSpeedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__MIN_SPEED, oldMinSpeed, MIN_SPEED_EDEFAULT, oldMinSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinSpeed() {
		return minSpeedESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrDelegateMaxSpeed() {
		if (getReference() != null && !isSetMaxSpeed()) {
			return getReference().getMaxSpeed();
		}
		return getMaxSpeed();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxSpeed(double newMaxSpeed) {
		double oldMaxSpeed = maxSpeed;
		maxSpeed = newMaxSpeed;
		boolean oldMaxSpeedESet = maxSpeedESet;
		maxSpeedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__MAX_SPEED, oldMaxSpeed, maxSpeed, !oldMaxSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMaxSpeed() {
		double oldMaxSpeed = maxSpeed;
		boolean oldMaxSpeedESet = maxSpeedESet;
		maxSpeed = MAX_SPEED_EDEFAULT;
		maxSpeedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__MAX_SPEED, oldMaxSpeed, MAX_SPEED_EDEFAULT, oldMaxSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMaxSpeed() {
		return maxSpeedESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSafetyHeel() {
		return safetyHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getVesselOrDelegateSafetyHeel() {
		if (getReference() != null && !isSetSafetyHeel()) {
			return getReference().getSafetyHeel();
		}
		return getSafetyHeel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public int getVesselOrDelegatePurgeTime() {
		if (getReference() != null && !isSetPurgeTime()) {
			return getReference().getPurgeTime();
		}
		return getPurgeTime();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public int getVesselOrDelegatePurgeVolume() {
		if (getReference() != null && !isSetPurgeVolume()) {
			return getReference().getPurgeVolume();
		}
		return getPurgeVolume();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSafetyHeel(int newSafetyHeel) {
		int oldSafetyHeel = safetyHeel;
		safetyHeel = newSafetyHeel;
		boolean oldSafetyHeelESet = safetyHeelESet;
		safetyHeelESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__SAFETY_HEEL, oldSafetyHeel, safetyHeel, !oldSafetyHeelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetSafetyHeel() {
		int oldSafetyHeel = safetyHeel;
		boolean oldSafetyHeelESet = safetyHeelESet;
		safetyHeel = SAFETY_HEEL_EDEFAULT;
		safetyHeelESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__SAFETY_HEEL, oldSafetyHeel, SAFETY_HEEL_EDEFAULT, oldSafetyHeelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetSafetyHeel() {
		return safetyHeelESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getWarmingTime() {
		return warmingTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getVesselOrDelegateWarmingTime() {
		if (getReference() != null && !isSetWarmingTime()) {
			return getReference().getWarmingTime();
		}
		return getWarmingTime();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public int getVesselOrDelegateCoolingTime() {
		if (getReference() != null && !isSetCoolingTime()) {
			return getReference().getCoolingTime();
		}
		return getCoolingTime();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWarmingTime(int newWarmingTime) {
		int oldWarmingTime = warmingTime;
		warmingTime = newWarmingTime;
		boolean oldWarmingTimeESet = warmingTimeESet;
		warmingTimeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__WARMING_TIME, oldWarmingTime, warmingTime, !oldWarmingTimeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetWarmingTime() {
		int oldWarmingTime = warmingTime;
		boolean oldWarmingTimeESet = warmingTimeESet;
		warmingTime = WARMING_TIME_EDEFAULT;
		warmingTimeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__WARMING_TIME, oldWarmingTime, WARMING_TIME_EDEFAULT, oldWarmingTimeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetWarmingTime() {
		return warmingTimeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPurgeTime() {
		return purgeTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPurgeTime(int newPurgeTime) {
		int oldPurgeTime = purgeTime;
		purgeTime = newPurgeTime;
		boolean oldPurgeTimeESet = purgeTimeESet;
		purgeTimeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__PURGE_TIME, oldPurgeTime, purgeTime, !oldPurgeTimeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPurgeTime() {
		int oldPurgeTime = purgeTime;
		boolean oldPurgeTimeESet = purgeTimeESet;
		purgeTime = PURGE_TIME_EDEFAULT;
		purgeTimeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__PURGE_TIME, oldPurgeTime, PURGE_TIME_EDEFAULT, oldPurgeTimeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPurgeTime() {
		return purgeTimeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCoolingVolume() {
		return coolingVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getVesselOrDelegateCoolingVolume() {
		if (getReference() != null && !isSetCoolingVolume()) {
			return getReference().getCoolingVolume();
		}
		return getCoolingVolume();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCoolingVolume(int newCoolingVolume) {
		int oldCoolingVolume = coolingVolume;
		coolingVolume = newCoolingVolume;
		boolean oldCoolingVolumeESet = coolingVolumeESet;
		coolingVolumeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__COOLING_VOLUME, oldCoolingVolume, coolingVolume, !oldCoolingVolumeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCoolingVolume() {
		int oldCoolingVolume = coolingVolume;
		boolean oldCoolingVolumeESet = coolingVolumeESet;
		coolingVolume = COOLING_VOLUME_EDEFAULT;
		coolingVolumeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__COOLING_VOLUME, oldCoolingVolume, COOLING_VOLUME_EDEFAULT, oldCoolingVolumeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCoolingVolume() {
		return coolingVolumeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCoolingTime() {
		return coolingTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCoolingTime(int newCoolingTime) {
		int oldCoolingTime = coolingTime;
		coolingTime = newCoolingTime;
		boolean oldCoolingTimeESet = coolingTimeESet;
		coolingTimeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__COOLING_TIME, oldCoolingTime, coolingTime, !oldCoolingTimeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCoolingTime() {
		int oldCoolingTime = coolingTime;
		boolean oldCoolingTimeESet = coolingTimeESet;
		coolingTime = COOLING_TIME_EDEFAULT;
		coolingTimeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__COOLING_TIME, oldCoolingTime, COOLING_TIME_EDEFAULT, oldCoolingTimeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCoolingTime() {
		return coolingTimeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPurgeVolume() {
		return purgeVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPurgeVolume(int newPurgeVolume) {
		int oldPurgeVolume = purgeVolume;
		purgeVolume = newPurgeVolume;
		boolean oldPurgeVolumeESet = purgeVolumeESet;
		purgeVolumeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__PURGE_VOLUME, oldPurgeVolume, purgeVolume, !oldPurgeVolumeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPurgeVolume() {
		int oldPurgeVolume = purgeVolume;
		boolean oldPurgeVolumeESet = purgeVolumeESet;
		purgeVolume = PURGE_VOLUME_EDEFAULT;
		purgeVolumeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__PURGE_VOLUME, oldPurgeVolume, PURGE_VOLUME_EDEFAULT, oldPurgeVolumeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPurgeVolume() {
		return purgeVolumeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isRouteParametersOverride() {
		return routeParametersOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRouteParametersOverride(boolean newRouteParametersOverride) {
		boolean oldRouteParametersOverride = routeParametersOverride;
		routeParametersOverride = newRouteParametersOverride;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__ROUTE_PARAMETERS_OVERRIDE, oldRouteParametersOverride, routeParametersOverride));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselClassRouteParameters> getRouteParameters() {
		if (routeParameters == null) {
			routeParameters = new EObjectContainmentEList<VesselClassRouteParameters>(VesselClassRouteParameters.class, this, FleetPackage.VESSEL__ROUTE_PARAMETERS);
		}
		return routeParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getPilotLightRate() {
		return pilotLightRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<VesselClassRouteParameters> getVesselOrDelegateRouteParameters() {
		if (getReference() != null && !isRouteParametersOverride()) {
			return new DelegatingEList.UnmodifiableEList<>(getReference().getRouteParameters());
		}
		return getRouteParameters();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrDelegatePilotLightRate() {
		if (getReference() != null && !isSetPilotLightRate()) {
			return getReference().getPilotLightRate();
		}
		return getPilotLightRate();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPilotLightRate(double newPilotLightRate) {
		double oldPilotLightRate = pilotLightRate;
		pilotLightRate = newPilotLightRate;
		boolean oldPilotLightRateESet = pilotLightRateESet;
		pilotLightRateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__PILOT_LIGHT_RATE, oldPilotLightRate, pilotLightRate, !oldPilotLightRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPilotLightRate() {
		double oldPilotLightRate = pilotLightRate;
		boolean oldPilotLightRateESet = pilotLightRateESet;
		pilotLightRate = PILOT_LIGHT_RATE_EDEFAULT;
		pilotLightRateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__PILOT_LIGHT_RATE, oldPilotLightRate, PILOT_LIGHT_RATE_EDEFAULT, oldPilotLightRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPilotLightRate() {
		return pilotLightRateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getMinBaseFuelConsumption() {
		return minBaseFuelConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrDelegateMinBaseFuelConsumption() {
		if (getReference() != null && !isSetMinBaseFuelConsumption()) {
			return getReference().getMinBaseFuelConsumption();
		}
		return getMinBaseFuelConsumption();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean getVesselOrDelegateHasReliqCapability() {
		if (getReference() != null && !isHasReliqCapabilityOverride()) {
			return getReference().isHasReliqCapability();
		}
		return isHasReliqCapability();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getVesselOrDelegateSCNT() {
		if (getReference() != null && !isSetScnt()) {
			return getReference().getScnt();
		}
		return getScnt();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinBaseFuelConsumption(double newMinBaseFuelConsumption) {
		double oldMinBaseFuelConsumption = minBaseFuelConsumption;
		minBaseFuelConsumption = newMinBaseFuelConsumption;
		boolean oldMinBaseFuelConsumptionESet = minBaseFuelConsumptionESet;
		minBaseFuelConsumptionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__MIN_BASE_FUEL_CONSUMPTION, oldMinBaseFuelConsumption, minBaseFuelConsumption, !oldMinBaseFuelConsumptionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinBaseFuelConsumption() {
		double oldMinBaseFuelConsumption = minBaseFuelConsumption;
		boolean oldMinBaseFuelConsumptionESet = minBaseFuelConsumptionESet;
		minBaseFuelConsumption = MIN_BASE_FUEL_CONSUMPTION_EDEFAULT;
		minBaseFuelConsumptionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__MIN_BASE_FUEL_CONSUMPTION, oldMinBaseFuelConsumption, MIN_BASE_FUEL_CONSUMPTION_EDEFAULT, oldMinBaseFuelConsumptionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinBaseFuelConsumption() {
		return minBaseFuelConsumptionESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isHasReliqCapabilityOverride() {
		return hasReliqCapabilityOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHasReliqCapabilityOverride(boolean newHasReliqCapabilityOverride) {
		boolean oldHasReliqCapabilityOverride = hasReliqCapabilityOverride;
		hasReliqCapabilityOverride = newHasReliqCapabilityOverride;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE, oldHasReliqCapabilityOverride, hasReliqCapabilityOverride));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isHasReliqCapability() {
		return hasReliqCapability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHasReliqCapability(boolean newHasReliqCapability) {
		boolean oldHasReliqCapability = hasReliqCapability;
		hasReliqCapability = newHasReliqCapability;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY, oldHasReliqCapability, hasReliqCapability));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getNotes() {
		return notes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNotes(String newNotes) {
		String oldNotes = notes;
		notes = newNotes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__NOTES, oldNotes, notes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMmxId() {
		return mmxId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMmxId(String newMmxId) {
		String oldMmxId = mmxId;
		mmxId = newMmxId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__MMX_ID, oldMmxId, mmxId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getScnt() {
		return scnt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setScnt(int newScnt) {
		int oldScnt = scnt;
		scnt = newScnt;
		boolean oldScntESet = scntESet;
		scntESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__SCNT, oldScnt, scnt, !oldScntESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetScnt() {
		int oldScnt = scnt;
		boolean oldScntESet = scntESet;
		scnt = SCNT_EDEFAULT;
		scntESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__SCNT, oldScnt, SCNT_EDEFAULT, oldScntESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetScnt() {
		return scntESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isInaccessiblePortsOverride() {
		return inaccessiblePortsOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInaccessiblePortsOverride(boolean newInaccessiblePortsOverride) {
		boolean oldInaccessiblePortsOverride = inaccessiblePortsOverride;
		inaccessiblePortsOverride = newInaccessiblePortsOverride;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__INACCESSIBLE_PORTS_OVERRIDE, oldInaccessiblePortsOverride, inaccessiblePortsOverride));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<APortSet<Port>> getInaccessiblePorts() {
		if (inaccessiblePorts == null) {
			inaccessiblePorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, FleetPackage.VESSEL__INACCESSIBLE_PORTS);
		}
		return inaccessiblePorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<RouteOption> getVesselOrDelegateInaccessibleRoutes() {
		if (getReference() != null && !isInaccessibleRoutesOverride()) {
			return new DelegatingEList.UnmodifiableEList<>(getReference().getInaccessibleRoutes());
		}
		return getInaccessibleRoutes();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public BaseFuel getVesselOrDelegateBaseFuel() {
		if (getReference() != null && !isSetBaseFuel()) {
			return getReference().getBaseFuel();
		}
		return getBaseFuel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public BaseFuel getVesselOrDelegateIdleBaseFuel() {
		if (getReference() != null && !isSetIdleBaseFuel()) {
			return getReference().getIdleBaseFuel();
		}
		return getIdleBaseFuel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public BaseFuel getVesselOrDelegatePilotLightBaseFuel() {
		if (getReference() != null && !isSetPilotLightBaseFuel()) {
			return getReference().getPilotLightBaseFuel();
		}
		return getPilotLightBaseFuel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public BaseFuel getVesselOrDelegateInPortBaseFuel() {
		if (getReference() != null && !isSetInPortBaseFuel()) {
			return getReference().getInPortBaseFuel();
		}
		return getInPortBaseFuel();
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
	@Override
	public void setBaseFuel(BaseFuel newBaseFuel) {
		BaseFuel oldBaseFuel = baseFuel;
		baseFuel = newBaseFuel;
		boolean oldBaseFuelESet = baseFuelESet;
		baseFuelESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__BASE_FUEL, oldBaseFuel, baseFuel, !oldBaseFuelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetBaseFuel() {
		BaseFuel oldBaseFuel = baseFuel;
		boolean oldBaseFuelESet = baseFuelESet;
		baseFuel = null;
		baseFuelESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__BASE_FUEL, oldBaseFuel, null, oldBaseFuelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetBaseFuel() {
		return baseFuelESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseFuel getInPortBaseFuel() {
		if (inPortBaseFuel != null && inPortBaseFuel.eIsProxy()) {
			InternalEObject oldInPortBaseFuel = (InternalEObject)inPortBaseFuel;
			inPortBaseFuel = (BaseFuel)eResolveProxy(oldInPortBaseFuel);
			if (inPortBaseFuel != oldInPortBaseFuel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__IN_PORT_BASE_FUEL, oldInPortBaseFuel, inPortBaseFuel));
			}
		}
		return inPortBaseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseFuel basicGetInPortBaseFuel() {
		return inPortBaseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInPortBaseFuel(BaseFuel newInPortBaseFuel) {
		BaseFuel oldInPortBaseFuel = inPortBaseFuel;
		inPortBaseFuel = newInPortBaseFuel;
		boolean oldInPortBaseFuelESet = inPortBaseFuelESet;
		inPortBaseFuelESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__IN_PORT_BASE_FUEL, oldInPortBaseFuel, inPortBaseFuel, !oldInPortBaseFuelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetInPortBaseFuel() {
		BaseFuel oldInPortBaseFuel = inPortBaseFuel;
		boolean oldInPortBaseFuelESet = inPortBaseFuelESet;
		inPortBaseFuel = null;
		inPortBaseFuelESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__IN_PORT_BASE_FUEL, oldInPortBaseFuel, null, oldInPortBaseFuelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetInPortBaseFuel() {
		return inPortBaseFuelESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseFuel getPilotLightBaseFuel() {
		if (pilotLightBaseFuel != null && pilotLightBaseFuel.eIsProxy()) {
			InternalEObject oldPilotLightBaseFuel = (InternalEObject)pilotLightBaseFuel;
			pilotLightBaseFuel = (BaseFuel)eResolveProxy(oldPilotLightBaseFuel);
			if (pilotLightBaseFuel != oldPilotLightBaseFuel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__PILOT_LIGHT_BASE_FUEL, oldPilotLightBaseFuel, pilotLightBaseFuel));
			}
		}
		return pilotLightBaseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseFuel basicGetPilotLightBaseFuel() {
		return pilotLightBaseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPilotLightBaseFuel(BaseFuel newPilotLightBaseFuel) {
		BaseFuel oldPilotLightBaseFuel = pilotLightBaseFuel;
		pilotLightBaseFuel = newPilotLightBaseFuel;
		boolean oldPilotLightBaseFuelESet = pilotLightBaseFuelESet;
		pilotLightBaseFuelESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__PILOT_LIGHT_BASE_FUEL, oldPilotLightBaseFuel, pilotLightBaseFuel, !oldPilotLightBaseFuelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPilotLightBaseFuel() {
		BaseFuel oldPilotLightBaseFuel = pilotLightBaseFuel;
		boolean oldPilotLightBaseFuelESet = pilotLightBaseFuelESet;
		pilotLightBaseFuel = null;
		pilotLightBaseFuelESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__PILOT_LIGHT_BASE_FUEL, oldPilotLightBaseFuel, null, oldPilotLightBaseFuelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPilotLightBaseFuel() {
		return pilotLightBaseFuelESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseFuel getIdleBaseFuel() {
		if (idleBaseFuel != null && idleBaseFuel.eIsProxy()) {
			InternalEObject oldIdleBaseFuel = (InternalEObject)idleBaseFuel;
			idleBaseFuel = (BaseFuel)eResolveProxy(oldIdleBaseFuel);
			if (idleBaseFuel != oldIdleBaseFuel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__IDLE_BASE_FUEL, oldIdleBaseFuel, idleBaseFuel));
			}
		}
		return idleBaseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseFuel basicGetIdleBaseFuel() {
		return idleBaseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIdleBaseFuel(BaseFuel newIdleBaseFuel) {
		BaseFuel oldIdleBaseFuel = idleBaseFuel;
		idleBaseFuel = newIdleBaseFuel;
		boolean oldIdleBaseFuelESet = idleBaseFuelESet;
		idleBaseFuelESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__IDLE_BASE_FUEL, oldIdleBaseFuel, idleBaseFuel, !oldIdleBaseFuelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetIdleBaseFuel() {
		BaseFuel oldIdleBaseFuel = idleBaseFuel;
		boolean oldIdleBaseFuelESet = idleBaseFuelESet;
		idleBaseFuel = null;
		idleBaseFuelESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__IDLE_BASE_FUEL, oldIdleBaseFuel, null, oldIdleBaseFuelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetIdleBaseFuel() {
		return idleBaseFuelESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCapacity() {
		return capacity;
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
		result.add(this);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getVesselOrVesselClassCapacity() {
		
		return (Integer) eGetWithDefault(FleetPackage.eINSTANCE.getVessel_Capacity());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getVesselOrVesselClassFillCapacity() {
		return (Double) eGetWithDefault(FleetPackage.eINSTANCE.getVessel_FillCapacity());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getShortenedName() {
		
		final String shortName = getShortName();
		if (shortName == null || shortName.isEmpty()) {
			return getName();
		}
		return shortName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.VESSEL__LADEN_ATTRIBUTES:
				return basicSetLadenAttributes(null, msgs);
			case FleetPackage.VESSEL__BALLAST_ATTRIBUTES:
				return basicSetBallastAttributes(null, msgs);
			case FleetPackage.VESSEL__ROUTE_PARAMETERS:
				return ((InternalEList<?>)getRouteParameters()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL__SHORT_NAME:
				return getShortName();
			case FleetPackage.VESSEL__IMO:
				return getIMO();
			case FleetPackage.VESSEL__TYPE:
				return getType();
			case FleetPackage.VESSEL__REFERENCE:
				if (resolve) return getReference();
				return basicGetReference();
			case FleetPackage.VESSEL__CAPACITY:
				return getCapacity();
			case FleetPackage.VESSEL__FILL_CAPACITY:
				return getFillCapacity();
			case FleetPackage.VESSEL__SCNT:
				return getScnt();
			case FleetPackage.VESSEL__BASE_FUEL:
				if (resolve) return getBaseFuel();
				return basicGetBaseFuel();
			case FleetPackage.VESSEL__IN_PORT_BASE_FUEL:
				if (resolve) return getInPortBaseFuel();
				return basicGetInPortBaseFuel();
			case FleetPackage.VESSEL__PILOT_LIGHT_BASE_FUEL:
				if (resolve) return getPilotLightBaseFuel();
				return basicGetPilotLightBaseFuel();
			case FleetPackage.VESSEL__IDLE_BASE_FUEL:
				if (resolve) return getIdleBaseFuel();
				return basicGetIdleBaseFuel();
			case FleetPackage.VESSEL__PILOT_LIGHT_RATE:
				return getPilotLightRate();
			case FleetPackage.VESSEL__SAFETY_HEEL:
				return getSafetyHeel();
			case FleetPackage.VESSEL__WARMING_TIME:
				return getWarmingTime();
			case FleetPackage.VESSEL__COOLING_VOLUME:
				return getCoolingVolume();
			case FleetPackage.VESSEL__COOLING_TIME:
				return getCoolingTime();
			case FleetPackage.VESSEL__PURGE_VOLUME:
				return getPurgeVolume();
			case FleetPackage.VESSEL__PURGE_TIME:
				return getPurgeTime();
			case FleetPackage.VESSEL__LADEN_ATTRIBUTES:
				return getLadenAttributes();
			case FleetPackage.VESSEL__BALLAST_ATTRIBUTES:
				return getBallastAttributes();
			case FleetPackage.VESSEL__MIN_SPEED:
				return getMinSpeed();
			case FleetPackage.VESSEL__MAX_SPEED:
				return getMaxSpeed();
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS_OVERRIDE:
				return isInaccessiblePortsOverride();
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				return getInaccessiblePorts();
			case FleetPackage.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE:
				return isInaccessibleRoutesOverride();
			case FleetPackage.VESSEL__INACCESSIBLE_ROUTES:
				return getInaccessibleRoutes();
			case FleetPackage.VESSEL__ROUTE_PARAMETERS_OVERRIDE:
				return isRouteParametersOverride();
			case FleetPackage.VESSEL__ROUTE_PARAMETERS:
				return getRouteParameters();
			case FleetPackage.VESSEL__MIN_BASE_FUEL_CONSUMPTION:
				return getMinBaseFuelConsumption();
			case FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE:
				return isHasReliqCapabilityOverride();
			case FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY:
				return isHasReliqCapability();
			case FleetPackage.VESSEL__NOTES:
				return getNotes();
			case FleetPackage.VESSEL__MMX_ID:
				return getMmxId();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FleetPackage.VESSEL__SHORT_NAME:
				setShortName((String)newValue);
				return;
			case FleetPackage.VESSEL__IMO:
				setIMO((String)newValue);
				return;
			case FleetPackage.VESSEL__TYPE:
				setType((String)newValue);
				return;
			case FleetPackage.VESSEL__REFERENCE:
				setReference((Vessel)newValue);
				return;
			case FleetPackage.VESSEL__CAPACITY:
				setCapacity((Integer)newValue);
				return;
			case FleetPackage.VESSEL__FILL_CAPACITY:
				setFillCapacity((Double)newValue);
				return;
			case FleetPackage.VESSEL__SCNT:
				setScnt((Integer)newValue);
				return;
			case FleetPackage.VESSEL__BASE_FUEL:
				setBaseFuel((BaseFuel)newValue);
				return;
			case FleetPackage.VESSEL__IN_PORT_BASE_FUEL:
				setInPortBaseFuel((BaseFuel)newValue);
				return;
			case FleetPackage.VESSEL__PILOT_LIGHT_BASE_FUEL:
				setPilotLightBaseFuel((BaseFuel)newValue);
				return;
			case FleetPackage.VESSEL__IDLE_BASE_FUEL:
				setIdleBaseFuel((BaseFuel)newValue);
				return;
			case FleetPackage.VESSEL__PILOT_LIGHT_RATE:
				setPilotLightRate((Double)newValue);
				return;
			case FleetPackage.VESSEL__SAFETY_HEEL:
				setSafetyHeel((Integer)newValue);
				return;
			case FleetPackage.VESSEL__WARMING_TIME:
				setWarmingTime((Integer)newValue);
				return;
			case FleetPackage.VESSEL__COOLING_VOLUME:
				setCoolingVolume((Integer)newValue);
				return;
			case FleetPackage.VESSEL__COOLING_TIME:
				setCoolingTime((Integer)newValue);
				return;
			case FleetPackage.VESSEL__PURGE_VOLUME:
				setPurgeVolume((Integer)newValue);
				return;
			case FleetPackage.VESSEL__PURGE_TIME:
				setPurgeTime((Integer)newValue);
				return;
			case FleetPackage.VESSEL__LADEN_ATTRIBUTES:
				setLadenAttributes((VesselStateAttributes)newValue);
				return;
			case FleetPackage.VESSEL__BALLAST_ATTRIBUTES:
				setBallastAttributes((VesselStateAttributes)newValue);
				return;
			case FleetPackage.VESSEL__MIN_SPEED:
				setMinSpeed((Double)newValue);
				return;
			case FleetPackage.VESSEL__MAX_SPEED:
				setMaxSpeed((Double)newValue);
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS_OVERRIDE:
				setInaccessiblePortsOverride((Boolean)newValue);
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
				getInaccessiblePorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE:
				setInaccessibleRoutesOverride((Boolean)newValue);
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_ROUTES:
				getInaccessibleRoutes().clear();
				getInaccessibleRoutes().addAll((Collection<? extends RouteOption>)newValue);
				return;
			case FleetPackage.VESSEL__ROUTE_PARAMETERS_OVERRIDE:
				setRouteParametersOverride((Boolean)newValue);
				return;
			case FleetPackage.VESSEL__ROUTE_PARAMETERS:
				getRouteParameters().clear();
				getRouteParameters().addAll((Collection<? extends VesselClassRouteParameters>)newValue);
				return;
			case FleetPackage.VESSEL__MIN_BASE_FUEL_CONSUMPTION:
				setMinBaseFuelConsumption((Double)newValue);
				return;
			case FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE:
				setHasReliqCapabilityOverride((Boolean)newValue);
				return;
			case FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY:
				setHasReliqCapability((Boolean)newValue);
				return;
			case FleetPackage.VESSEL__NOTES:
				setNotes((String)newValue);
				return;
			case FleetPackage.VESSEL__MMX_ID:
				setMmxId((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case FleetPackage.VESSEL__SHORT_NAME:
				setShortName(SHORT_NAME_EDEFAULT);
				return;
			case FleetPackage.VESSEL__IMO:
				setIMO(IMO_EDEFAULT);
				return;
			case FleetPackage.VESSEL__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case FleetPackage.VESSEL__REFERENCE:
				setReference((Vessel)null);
				return;
			case FleetPackage.VESSEL__CAPACITY:
				unsetCapacity();
				return;
			case FleetPackage.VESSEL__FILL_CAPACITY:
				unsetFillCapacity();
				return;
			case FleetPackage.VESSEL__SCNT:
				unsetScnt();
				return;
			case FleetPackage.VESSEL__BASE_FUEL:
				unsetBaseFuel();
				return;
			case FleetPackage.VESSEL__IN_PORT_BASE_FUEL:
				unsetInPortBaseFuel();
				return;
			case FleetPackage.VESSEL__PILOT_LIGHT_BASE_FUEL:
				unsetPilotLightBaseFuel();
				return;
			case FleetPackage.VESSEL__IDLE_BASE_FUEL:
				unsetIdleBaseFuel();
				return;
			case FleetPackage.VESSEL__PILOT_LIGHT_RATE:
				unsetPilotLightRate();
				return;
			case FleetPackage.VESSEL__SAFETY_HEEL:
				unsetSafetyHeel();
				return;
			case FleetPackage.VESSEL__WARMING_TIME:
				unsetWarmingTime();
				return;
			case FleetPackage.VESSEL__COOLING_VOLUME:
				unsetCoolingVolume();
				return;
			case FleetPackage.VESSEL__COOLING_TIME:
				unsetCoolingTime();
				return;
			case FleetPackage.VESSEL__PURGE_VOLUME:
				unsetPurgeVolume();
				return;
			case FleetPackage.VESSEL__PURGE_TIME:
				unsetPurgeTime();
				return;
			case FleetPackage.VESSEL__LADEN_ATTRIBUTES:
				setLadenAttributes((VesselStateAttributes)null);
				return;
			case FleetPackage.VESSEL__BALLAST_ATTRIBUTES:
				setBallastAttributes((VesselStateAttributes)null);
				return;
			case FleetPackage.VESSEL__MIN_SPEED:
				unsetMinSpeed();
				return;
			case FleetPackage.VESSEL__MAX_SPEED:
				unsetMaxSpeed();
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS_OVERRIDE:
				setInaccessiblePortsOverride(INACCESSIBLE_PORTS_OVERRIDE_EDEFAULT);
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE:
				setInaccessibleRoutesOverride(INACCESSIBLE_ROUTES_OVERRIDE_EDEFAULT);
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_ROUTES:
				getInaccessibleRoutes().clear();
				return;
			case FleetPackage.VESSEL__ROUTE_PARAMETERS_OVERRIDE:
				setRouteParametersOverride(ROUTE_PARAMETERS_OVERRIDE_EDEFAULT);
				return;
			case FleetPackage.VESSEL__ROUTE_PARAMETERS:
				getRouteParameters().clear();
				return;
			case FleetPackage.VESSEL__MIN_BASE_FUEL_CONSUMPTION:
				unsetMinBaseFuelConsumption();
				return;
			case FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE:
				setHasReliqCapabilityOverride(HAS_RELIQ_CAPABILITY_OVERRIDE_EDEFAULT);
				return;
			case FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY:
				setHasReliqCapability(HAS_RELIQ_CAPABILITY_EDEFAULT);
				return;
			case FleetPackage.VESSEL__NOTES:
				setNotes(NOTES_EDEFAULT);
				return;
			case FleetPackage.VESSEL__MMX_ID:
				setMmxId(MMX_ID_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case FleetPackage.VESSEL__SHORT_NAME:
				return SHORT_NAME_EDEFAULT == null ? shortName != null : !SHORT_NAME_EDEFAULT.equals(shortName);
			case FleetPackage.VESSEL__IMO:
				return IMO_EDEFAULT == null ? imo != null : !IMO_EDEFAULT.equals(imo);
			case FleetPackage.VESSEL__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case FleetPackage.VESSEL__REFERENCE:
				return reference != null;
			case FleetPackage.VESSEL__CAPACITY:
				return isSetCapacity();
			case FleetPackage.VESSEL__FILL_CAPACITY:
				return isSetFillCapacity();
			case FleetPackage.VESSEL__SCNT:
				return isSetScnt();
			case FleetPackage.VESSEL__BASE_FUEL:
				return isSetBaseFuel();
			case FleetPackage.VESSEL__IN_PORT_BASE_FUEL:
				return isSetInPortBaseFuel();
			case FleetPackage.VESSEL__PILOT_LIGHT_BASE_FUEL:
				return isSetPilotLightBaseFuel();
			case FleetPackage.VESSEL__IDLE_BASE_FUEL:
				return isSetIdleBaseFuel();
			case FleetPackage.VESSEL__PILOT_LIGHT_RATE:
				return isSetPilotLightRate();
			case FleetPackage.VESSEL__SAFETY_HEEL:
				return isSetSafetyHeel();
			case FleetPackage.VESSEL__WARMING_TIME:
				return isSetWarmingTime();
			case FleetPackage.VESSEL__COOLING_VOLUME:
				return isSetCoolingVolume();
			case FleetPackage.VESSEL__COOLING_TIME:
				return isSetCoolingTime();
			case FleetPackage.VESSEL__PURGE_VOLUME:
				return isSetPurgeVolume();
			case FleetPackage.VESSEL__PURGE_TIME:
				return isSetPurgeTime();
			case FleetPackage.VESSEL__LADEN_ATTRIBUTES:
				return ladenAttributes != null;
			case FleetPackage.VESSEL__BALLAST_ATTRIBUTES:
				return ballastAttributes != null;
			case FleetPackage.VESSEL__MIN_SPEED:
				return isSetMinSpeed();
			case FleetPackage.VESSEL__MAX_SPEED:
				return isSetMaxSpeed();
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS_OVERRIDE:
				return inaccessiblePortsOverride != INACCESSIBLE_PORTS_OVERRIDE_EDEFAULT;
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				return inaccessiblePorts != null && !inaccessiblePorts.isEmpty();
			case FleetPackage.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE:
				return inaccessibleRoutesOverride != INACCESSIBLE_ROUTES_OVERRIDE_EDEFAULT;
			case FleetPackage.VESSEL__INACCESSIBLE_ROUTES:
				return inaccessibleRoutes != null && !inaccessibleRoutes.isEmpty();
			case FleetPackage.VESSEL__ROUTE_PARAMETERS_OVERRIDE:
				return routeParametersOverride != ROUTE_PARAMETERS_OVERRIDE_EDEFAULT;
			case FleetPackage.VESSEL__ROUTE_PARAMETERS:
				return routeParameters != null && !routeParameters.isEmpty();
			case FleetPackage.VESSEL__MIN_BASE_FUEL_CONSUMPTION:
				return isSetMinBaseFuelConsumption();
			case FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE:
				return hasReliqCapabilityOverride != HAS_RELIQ_CAPABILITY_OVERRIDE_EDEFAULT;
			case FleetPackage.VESSEL__HAS_RELIQ_CAPABILITY:
				return hasReliqCapability != HAS_RELIQ_CAPABILITY_EDEFAULT;
			case FleetPackage.VESSEL__NOTES:
				return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
			case FleetPackage.VESSEL__MMX_ID:
				return MMX_ID_EDEFAULT == null ? mmxId != null : !MMX_ID_EDEFAULT.equals(mmxId);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (shortName: ");
		result.append(shortName);
		result.append(", IMO: ");
		result.append(imo);
		result.append(", type: ");
		result.append(type);
		result.append(", capacity: ");
		if (capacityESet) result.append(capacity); else result.append("<unset>");
		result.append(", fillCapacity: ");
		if (fillCapacityESet) result.append(fillCapacity); else result.append("<unset>");
		result.append(", scnt: ");
		if (scntESet) result.append(scnt); else result.append("<unset>");
		result.append(", pilotLightRate: ");
		if (pilotLightRateESet) result.append(pilotLightRate); else result.append("<unset>");
		result.append(", safetyHeel: ");
		if (safetyHeelESet) result.append(safetyHeel); else result.append("<unset>");
		result.append(", warmingTime: ");
		if (warmingTimeESet) result.append(warmingTime); else result.append("<unset>");
		result.append(", coolingVolume: ");
		if (coolingVolumeESet) result.append(coolingVolume); else result.append("<unset>");
		result.append(", coolingTime: ");
		if (coolingTimeESet) result.append(coolingTime); else result.append("<unset>");
		result.append(", purgeVolume: ");
		if (purgeVolumeESet) result.append(purgeVolume); else result.append("<unset>");
		result.append(", purgeTime: ");
		if (purgeTimeESet) result.append(purgeTime); else result.append("<unset>");
		result.append(", minSpeed: ");
		if (minSpeedESet) result.append(minSpeed); else result.append("<unset>");
		result.append(", maxSpeed: ");
		if (maxSpeedESet) result.append(maxSpeed); else result.append("<unset>");
		result.append(", inaccessiblePortsOverride: ");
		result.append(inaccessiblePortsOverride);
		result.append(", inaccessibleRoutesOverride: ");
		result.append(inaccessibleRoutesOverride);
		result.append(", inaccessibleRoutes: ");
		result.append(inaccessibleRoutes);
		result.append(", routeParametersOverride: ");
		result.append(routeParametersOverride);
		result.append(", minBaseFuelConsumption: ");
		if (minBaseFuelConsumptionESet) result.append(minBaseFuelConsumption); else result.append("<unset>");
		result.append(", hasReliqCapabilityOverride: ");
		result.append(hasReliqCapabilityOverride);
		result.append(", hasReliqCapability: ");
		result.append(hasReliqCapability);
		result.append(", notes: ");
		result.append(notes);
		result.append(", mmxId: ");
		result.append(mmxId);
		result.append(')');
		return result.toString();
	}
	
	/**
	 * @generated NOT
	 */
	@Override
	public boolean eIsSet(EStructuralFeature eFeature) {
		EStructuralFeature eStructuralFeature = eClass().getEStructuralFeature(eFeature.getName() + "Override");
		if (eStructuralFeature != null) {
			if (getReference() != null) {
				return  (Boolean)eGet(eStructuralFeature);
			}
		}
		return super.eIsSet(eFeature);
	}
//	/**
//	 * @generated NOT
//	 */
//	public Object eGetWithDefault(EStructuralFeature feature) {
//		EStructuralFeature eStructuralFeature = eClass().getEStructuralFeature(feature.getName() + "Override");
//		if (eStructuralFeature != null) {
//			return  
//		}
//		return super. eGetWithDefault(EStructuralFeature feature);
//	}

	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		FleetPackage fleetPackage = FleetPackage.eINSTANCE;
		if (getReference() != null) {
			EStructuralFeature eStructuralFeature = eClass().getEStructuralFeature(feature.getName() + "Override");
			if (eStructuralFeature != null) {
				return new DelegateInformation(fleetPackage.getVessel_Reference(), feature, null);
			}
			if (feature.isUnsettable()) {
				return new DelegateInformation(fleetPackage.getVessel_Reference(), feature, null);
			}
		} else {
			EStructuralFeature eStructuralFeature = eClass().getEStructuralFeature(feature.getName() + "Override");
			if (eStructuralFeature != null) {
				return new DelegateInformation(fleetPackage.getVessel_Reference(), null, null);
			}
			if (feature.isUnsettable()) {
				return new DelegateInformation(fleetPackage.getVessel_Reference(), null, null);
			}
		}
		
		return super.getUnsetValueOrDelegate(feature);
	}	

} // end of VesselImpl

// finish type fixing
