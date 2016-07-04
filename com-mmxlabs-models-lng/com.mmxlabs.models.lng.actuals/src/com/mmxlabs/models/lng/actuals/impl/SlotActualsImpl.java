/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.PenaltyType;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Slot Actuals</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getCounterparty <em>Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getOperationsStart <em>Operations Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getOperationsEnd <em>Operations End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getTitleTransferPoint <em>Title Transfer Point</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getVolumeInM3 <em>Volume In M3</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getVolumeInMMBtu <em>Volume In MM Btu</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getPriceDOL <em>Price DOL</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getPenalty <em>Penalty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getNotes <em>Notes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getCV <em>CV</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getBaseFuelConsumption <em>Base Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getPortBaseFuelConsumption <em>Port Base Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getRoute <em>Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getDistance <em>Distance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getRouteCosts <em>Route Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getCrewBonus <em>Crew Bonus</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getPortCharges <em>Port Charges</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl#getCapacityCharges <em>Capacity Charges</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class SlotActualsImpl extends EObjectImpl implements SlotActuals {
	/**
	 * The cached value of the '{@link #getSlot() <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlot()
	 * @generated
	 * @ordered
	 */
	protected Slot slot;

	/**
	 * The default value of the '{@link #getCounterparty() <em>Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCounterparty()
	 * @generated
	 * @ordered
	 */
	protected static final String COUNTERPARTY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCounterparty() <em>Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCounterparty()
	 * @generated
	 * @ordered
	 */
	protected String counterparty = COUNTERPARTY_EDEFAULT;

	/**
	 * The default value of the '{@link #getOperationsStart() <em>Operations Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationsStart()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime OPERATIONS_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOperationsStart() <em>Operations Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationsStart()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime operationsStart = OPERATIONS_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getOperationsEnd() <em>Operations End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationsEnd()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime OPERATIONS_END_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOperationsEnd() <em>Operations End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationsEnd()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime operationsEnd = OPERATIONS_END_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTitleTransferPoint() <em>Title Transfer Point</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitleTransferPoint()
	 * @generated
	 * @ordered
	 */
	protected Port titleTransferPoint;

	/**
	 * The default value of the '{@link #getVolumeInM3() <em>Volume In M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeInM3()
	 * @generated
	 * @ordered
	 */
	protected static final double VOLUME_IN_M3_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getVolumeInM3() <em>Volume In M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeInM3()
	 * @generated
	 * @ordered
	 */
	protected double volumeInM3 = VOLUME_IN_M3_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeInMMBtu() <em>Volume In MM Btu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeInMMBtu()
	 * @generated
	 * @ordered
	 */
	protected static final double VOLUME_IN_MM_BTU_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getVolumeInMMBtu() <em>Volume In MM Btu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeInMMBtu()
	 * @generated
	 * @ordered
	 */
	protected double volumeInMMBtu = VOLUME_IN_MM_BTU_EDEFAULT;

	/**
	 * The default value of the '{@link #getPriceDOL() <em>Price DOL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceDOL()
	 * @generated
	 * @ordered
	 */
	protected static final double PRICE_DOL_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPriceDOL() <em>Price DOL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceDOL()
	 * @generated
	 * @ordered
	 */
	protected double priceDOL = PRICE_DOL_EDEFAULT;

	/**
	 * The default value of the '{@link #getPenalty() <em>Penalty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPenalty()
	 * @generated
	 * @ordered
	 */
	protected static final PenaltyType PENALTY_EDEFAULT = PenaltyType.TOP;

	/**
	 * The cached value of the '{@link #getPenalty() <em>Penalty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPenalty()
	 * @generated
	 * @ordered
	 */
	protected PenaltyType penalty = PENALTY_EDEFAULT;

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
	 * The default value of the '{@link #getCV() <em>CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCV()
	 * @generated
	 * @ordered
	 */
	protected static final double CV_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCV() <em>CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCV()
	 * @generated
	 * @ordered
	 */
	protected double cv = CV_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseFuelConsumption() <em>Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelConsumption()
	 * @generated
	 * @ordered
	 */
	protected static final int BASE_FUEL_CONSUMPTION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBaseFuelConsumption() <em>Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelConsumption()
	 * @generated
	 * @ordered
	 */
	protected int baseFuelConsumption = BASE_FUEL_CONSUMPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortBaseFuelConsumption() <em>Port Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortBaseFuelConsumption()
	 * @generated
	 * @ordered
	 */
	protected static final int PORT_BASE_FUEL_CONSUMPTION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPortBaseFuelConsumption() <em>Port Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortBaseFuelConsumption()
	 * @generated
	 * @ordered
	 */
	protected int portBaseFuelConsumption = PORT_BASE_FUEL_CONSUMPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRoute() <em>Route</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected Route route;

	/**
	 * The default value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected static final int DISTANCE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected int distance = DISTANCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getRouteCosts() <em>Route Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteCosts()
	 * @generated
	 * @ordered
	 */
	protected static final int ROUTE_COSTS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRouteCosts() <em>Route Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteCosts()
	 * @generated
	 * @ordered
	 */
	protected int routeCosts = ROUTE_COSTS_EDEFAULT;

	/**
	 * The default value of the '{@link #getCrewBonus() <em>Crew Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCrewBonus()
	 * @generated
	 * @ordered
	 */
	protected static final int CREW_BONUS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCrewBonus() <em>Crew Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCrewBonus()
	 * @generated
	 * @ordered
	 */
	protected int crewBonus = CREW_BONUS_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortCharges() <em>Port Charges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCharges()
	 * @generated
	 * @ordered
	 */
	protected static final int PORT_CHARGES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPortCharges() <em>Port Charges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCharges()
	 * @generated
	 * @ordered
	 */
	protected int portCharges = PORT_CHARGES_EDEFAULT;

	/**
	 * The default value of the '{@link #getCapacityCharges() <em>Capacity Charges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacityCharges()
	 * @generated
	 * @ordered
	 */
	protected static final int CAPACITY_CHARGES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCapacityCharges() <em>Capacity Charges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacityCharges()
	 * @generated
	 * @ordered
	 */
	protected int capacityCharges = CAPACITY_CHARGES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotActualsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ActualsPackage.Literals.SLOT_ACTUALS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getCV() {
		return cv;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCV(double newCV) {
		double oldCV = cv;
		cv = newCV;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__CV, oldCV, cv));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPortCharges() {
		return portCharges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortCharges(int newPortCharges) {
		int oldPortCharges = portCharges;
		portCharges = newPortCharges;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__PORT_CHARGES, oldPortCharges, portCharges));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCapacityCharges() {
		return capacityCharges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCapacityCharges(int newCapacityCharges) {
		int oldCapacityCharges = capacityCharges;
		capacityCharges = newCapacityCharges;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__CAPACITY_CHARGES, oldCapacityCharges, capacityCharges));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public ZonedDateTime getOperationsStartAsDateTime() {
		final LocalDateTime os = getOperationsStart();
		if (os != null) {
			return os.atZone(ZoneId.of(getTimeZone(ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START)));
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public ZonedDateTime getOperationsEndAsDateTime() {
		final LocalDateTime os = getOperationsEnd();
		if (os != null) {
			return os.atZone(ZoneId.of(getTimeZone(ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_END)));
		}
		return null;
	}

	 
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getBaseFuelConsumption() {
		return baseFuelConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseFuelConsumption(int newBaseFuelConsumption) {
		int oldBaseFuelConsumption = baseFuelConsumption;
		baseFuelConsumption = newBaseFuelConsumption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION, oldBaseFuelConsumption, baseFuelConsumption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPortBaseFuelConsumption() {
		return portBaseFuelConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortBaseFuelConsumption(int newPortBaseFuelConsumption) {
		int oldPortBaseFuelConsumption = portBaseFuelConsumption;
		portBaseFuelConsumption = newPortBaseFuelConsumption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION, oldPortBaseFuelConsumption, portBaseFuelConsumption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Route getRoute() {
		if (route != null && route.eIsProxy()) {
			InternalEObject oldRoute = (InternalEObject)route;
			route = (Route)eResolveProxy(oldRoute);
			if (route != oldRoute) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActualsPackage.SLOT_ACTUALS__ROUTE, oldRoute, route));
			}
		}
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Route basicGetRoute() {
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRoute(Route newRoute) {
		Route oldRoute = route;
		route = newRoute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__ROUTE, oldRoute, route));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDistance() {
		return distance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDistance(int newDistance) {
		int oldDistance = distance;
		distance = newDistance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__DISTANCE, oldDistance, distance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getRouteCosts() {
		return routeCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRouteCosts(int newRouteCosts) {
		int oldRouteCosts = routeCosts;
		routeCosts = newRouteCosts;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__ROUTE_COSTS, oldRouteCosts, routeCosts));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCrewBonus() {
		return crewBonus;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCrewBonus(int newCrewBonus) {
		int oldCrewBonus = crewBonus;
		crewBonus = newCrewBonus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__CREW_BONUS, oldCrewBonus, crewBonus));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getTimeZone(EAttribute attribute) {
		if (getTitleTransferPoint() == null) return "UTC";
		if (getTitleTransferPoint().getTimeZone() == null) return "UTC";
		if (getTitleTransferPoint().getTimeZone().isEmpty()) return "UTC";
		return getTitleTransferPoint().getTimeZone();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Slot getSlot() {
		if (slot != null && slot.eIsProxy()) {
			InternalEObject oldSlot = (InternalEObject)slot;
			slot = (Slot)eResolveProxy(oldSlot);
			if (slot != oldSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActualsPackage.SLOT_ACTUALS__SLOT, oldSlot, slot));
			}
		}
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot basicGetSlot() {
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSlot(Slot newSlot) {
		Slot oldSlot = slot;
		slot = newSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__SLOT, oldSlot, slot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCounterparty() {
		return counterparty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCounterparty(String newCounterparty) {
		String oldCounterparty = counterparty;
		counterparty = newCounterparty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__COUNTERPARTY, oldCounterparty, counterparty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getOperationsStart() {
		return operationsStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOperationsStart(LocalDateTime newOperationsStart) {
		LocalDateTime oldOperationsStart = operationsStart;
		operationsStart = newOperationsStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__OPERATIONS_START, oldOperationsStart, operationsStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getOperationsEnd() {
		return operationsEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOperationsEnd(LocalDateTime newOperationsEnd) {
		LocalDateTime oldOperationsEnd = operationsEnd;
		operationsEnd = newOperationsEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__OPERATIONS_END, oldOperationsEnd, operationsEnd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getTitleTransferPoint() {
		if (titleTransferPoint != null && titleTransferPoint.eIsProxy()) {
			InternalEObject oldTitleTransferPoint = (InternalEObject)titleTransferPoint;
			titleTransferPoint = (Port)eResolveProxy(oldTitleTransferPoint);
			if (titleTransferPoint != oldTitleTransferPoint) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActualsPackage.SLOT_ACTUALS__TITLE_TRANSFER_POINT, oldTitleTransferPoint, titleTransferPoint));
			}
		}
		return titleTransferPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetTitleTransferPoint() {
		return titleTransferPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTitleTransferPoint(Port newTitleTransferPoint) {
		Port oldTitleTransferPoint = titleTransferPoint;
		titleTransferPoint = newTitleTransferPoint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__TITLE_TRANSFER_POINT, oldTitleTransferPoint, titleTransferPoint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getVolumeInM3() {
		return volumeInM3;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeInM3(double newVolumeInM3) {
		double oldVolumeInM3 = volumeInM3;
		volumeInM3 = newVolumeInM3;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__VOLUME_IN_M3, oldVolumeInM3, volumeInM3));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getVolumeInMMBtu() {
		return volumeInMMBtu;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeInMMBtu(double newVolumeInMMBtu) {
		double oldVolumeInMMBtu = volumeInMMBtu;
		volumeInMMBtu = newVolumeInMMBtu;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__VOLUME_IN_MM_BTU, oldVolumeInMMBtu, volumeInMMBtu));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getPriceDOL() {
		return priceDOL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPriceDOL(double newPriceDOL) {
		double oldPriceDOL = priceDOL;
		priceDOL = newPriceDOL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__PRICE_DOL, oldPriceDOL, priceDOL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PenaltyType getPenalty() {
		return penalty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPenalty(PenaltyType newPenalty) {
		PenaltyType oldPenalty = penalty;
		penalty = newPenalty == null ? PENALTY_EDEFAULT : newPenalty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__PENALTY, oldPenalty, penalty));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.SLOT_ACTUALS__NOTES, oldNotes, notes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ActualsPackage.SLOT_ACTUALS__SLOT:
				if (resolve) return getSlot();
				return basicGetSlot();
			case ActualsPackage.SLOT_ACTUALS__COUNTERPARTY:
				return getCounterparty();
			case ActualsPackage.SLOT_ACTUALS__OPERATIONS_START:
				return getOperationsStart();
			case ActualsPackage.SLOT_ACTUALS__OPERATIONS_END:
				return getOperationsEnd();
			case ActualsPackage.SLOT_ACTUALS__TITLE_TRANSFER_POINT:
				if (resolve) return getTitleTransferPoint();
				return basicGetTitleTransferPoint();
			case ActualsPackage.SLOT_ACTUALS__VOLUME_IN_M3:
				return getVolumeInM3();
			case ActualsPackage.SLOT_ACTUALS__VOLUME_IN_MM_BTU:
				return getVolumeInMMBtu();
			case ActualsPackage.SLOT_ACTUALS__PRICE_DOL:
				return getPriceDOL();
			case ActualsPackage.SLOT_ACTUALS__PENALTY:
				return getPenalty();
			case ActualsPackage.SLOT_ACTUALS__NOTES:
				return getNotes();
			case ActualsPackage.SLOT_ACTUALS__CV:
				return getCV();
			case ActualsPackage.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION:
				return getBaseFuelConsumption();
			case ActualsPackage.SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION:
				return getPortBaseFuelConsumption();
			case ActualsPackage.SLOT_ACTUALS__ROUTE:
				if (resolve) return getRoute();
				return basicGetRoute();
			case ActualsPackage.SLOT_ACTUALS__DISTANCE:
				return getDistance();
			case ActualsPackage.SLOT_ACTUALS__ROUTE_COSTS:
				return getRouteCosts();
			case ActualsPackage.SLOT_ACTUALS__CREW_BONUS:
				return getCrewBonus();
			case ActualsPackage.SLOT_ACTUALS__PORT_CHARGES:
				return getPortCharges();
			case ActualsPackage.SLOT_ACTUALS__CAPACITY_CHARGES:
				return getCapacityCharges();
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
			case ActualsPackage.SLOT_ACTUALS__SLOT:
				setSlot((Slot)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__COUNTERPARTY:
				setCounterparty((String)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__OPERATIONS_START:
				setOperationsStart((LocalDateTime)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__OPERATIONS_END:
				setOperationsEnd((LocalDateTime)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__TITLE_TRANSFER_POINT:
				setTitleTransferPoint((Port)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__VOLUME_IN_M3:
				setVolumeInM3((Double)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__VOLUME_IN_MM_BTU:
				setVolumeInMMBtu((Double)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__PRICE_DOL:
				setPriceDOL((Double)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__PENALTY:
				setPenalty((PenaltyType)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__NOTES:
				setNotes((String)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__CV:
				setCV((Double)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION:
				setBaseFuelConsumption((Integer)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION:
				setPortBaseFuelConsumption((Integer)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__ROUTE:
				setRoute((Route)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__DISTANCE:
				setDistance((Integer)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__ROUTE_COSTS:
				setRouteCosts((Integer)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__CREW_BONUS:
				setCrewBonus((Integer)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__PORT_CHARGES:
				setPortCharges((Integer)newValue);
				return;
			case ActualsPackage.SLOT_ACTUALS__CAPACITY_CHARGES:
				setCapacityCharges((Integer)newValue);
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
			case ActualsPackage.SLOT_ACTUALS__SLOT:
				setSlot((Slot)null);
				return;
			case ActualsPackage.SLOT_ACTUALS__COUNTERPARTY:
				setCounterparty(COUNTERPARTY_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__OPERATIONS_START:
				setOperationsStart(OPERATIONS_START_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__OPERATIONS_END:
				setOperationsEnd(OPERATIONS_END_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__TITLE_TRANSFER_POINT:
				setTitleTransferPoint((Port)null);
				return;
			case ActualsPackage.SLOT_ACTUALS__VOLUME_IN_M3:
				setVolumeInM3(VOLUME_IN_M3_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__VOLUME_IN_MM_BTU:
				setVolumeInMMBtu(VOLUME_IN_MM_BTU_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__PRICE_DOL:
				setPriceDOL(PRICE_DOL_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__PENALTY:
				setPenalty(PENALTY_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__NOTES:
				setNotes(NOTES_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__CV:
				setCV(CV_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION:
				setBaseFuelConsumption(BASE_FUEL_CONSUMPTION_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION:
				setPortBaseFuelConsumption(PORT_BASE_FUEL_CONSUMPTION_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__ROUTE:
				setRoute((Route)null);
				return;
			case ActualsPackage.SLOT_ACTUALS__DISTANCE:
				setDistance(DISTANCE_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__ROUTE_COSTS:
				setRouteCosts(ROUTE_COSTS_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__CREW_BONUS:
				setCrewBonus(CREW_BONUS_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__PORT_CHARGES:
				setPortCharges(PORT_CHARGES_EDEFAULT);
				return;
			case ActualsPackage.SLOT_ACTUALS__CAPACITY_CHARGES:
				setCapacityCharges(CAPACITY_CHARGES_EDEFAULT);
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
			case ActualsPackage.SLOT_ACTUALS__SLOT:
				return slot != null;
			case ActualsPackage.SLOT_ACTUALS__COUNTERPARTY:
				return COUNTERPARTY_EDEFAULT == null ? counterparty != null : !COUNTERPARTY_EDEFAULT.equals(counterparty);
			case ActualsPackage.SLOT_ACTUALS__OPERATIONS_START:
				return OPERATIONS_START_EDEFAULT == null ? operationsStart != null : !OPERATIONS_START_EDEFAULT.equals(operationsStart);
			case ActualsPackage.SLOT_ACTUALS__OPERATIONS_END:
				return OPERATIONS_END_EDEFAULT == null ? operationsEnd != null : !OPERATIONS_END_EDEFAULT.equals(operationsEnd);
			case ActualsPackage.SLOT_ACTUALS__TITLE_TRANSFER_POINT:
				return titleTransferPoint != null;
			case ActualsPackage.SLOT_ACTUALS__VOLUME_IN_M3:
				return volumeInM3 != VOLUME_IN_M3_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__VOLUME_IN_MM_BTU:
				return volumeInMMBtu != VOLUME_IN_MM_BTU_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__PRICE_DOL:
				return priceDOL != PRICE_DOL_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__PENALTY:
				return penalty != PENALTY_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__NOTES:
				return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
			case ActualsPackage.SLOT_ACTUALS__CV:
				return cv != CV_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION:
				return baseFuelConsumption != BASE_FUEL_CONSUMPTION_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION:
				return portBaseFuelConsumption != PORT_BASE_FUEL_CONSUMPTION_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__ROUTE:
				return route != null;
			case ActualsPackage.SLOT_ACTUALS__DISTANCE:
				return distance != DISTANCE_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__ROUTE_COSTS:
				return routeCosts != ROUTE_COSTS_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__CREW_BONUS:
				return crewBonus != CREW_BONUS_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__PORT_CHARGES:
				return portCharges != PORT_CHARGES_EDEFAULT;
			case ActualsPackage.SLOT_ACTUALS__CAPACITY_CHARGES:
				return capacityCharges != CAPACITY_CHARGES_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case ActualsPackage.SLOT_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME:
				return getOperationsStartAsDateTime();
			case ActualsPackage.SLOT_ACTUALS___GET_OPERATIONS_END_AS_DATE_TIME:
				return getOperationsEndAsDateTime();
			case ActualsPackage.SLOT_ACTUALS___GET_TIME_ZONE__EATTRIBUTE:
				return getTimeZone((EAttribute)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (counterparty: ");
		result.append(counterparty);
		result.append(", operationsStart: ");
		result.append(operationsStart);
		result.append(", operationsEnd: ");
		result.append(operationsEnd);
		result.append(", volumeInM3: ");
		result.append(volumeInM3);
		result.append(", volumeInMMBtu: ");
		result.append(volumeInMMBtu);
		result.append(", priceDOL: ");
		result.append(priceDOL);
		result.append(", penalty: ");
		result.append(penalty);
		result.append(", notes: ");
		result.append(notes);
		result.append(", CV: ");
		result.append(cv);
		result.append(", baseFuelConsumption: ");
		result.append(baseFuelConsumption);
		result.append(", portBaseFuelConsumption: ");
		result.append(portBaseFuelConsumption);
		result.append(", distance: ");
		result.append(distance);
		result.append(", routeCosts: ");
		result.append(routeCosts);
		result.append(", crewBonus: ");
		result.append(crewBonus);
		result.append(", portCharges: ");
		result.append(portCharges);
		result.append(", capacityCharges: ");
		result.append(capacityCharges);
		result.append(')');
		return result.toString();
	}

} //SlotActualsImpl
