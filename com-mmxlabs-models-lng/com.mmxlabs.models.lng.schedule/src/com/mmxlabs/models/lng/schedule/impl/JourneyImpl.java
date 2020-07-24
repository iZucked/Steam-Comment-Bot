/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PanamaBookingPeriod;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Journey</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getFuels <em>Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getDestination <em>Destination</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#isLaden <em>Laden</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getRouteOption <em>Route Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getToll <em>Toll</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getDistance <em>Distance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getCanalEntrance <em>Canal Entrance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getCanalDateTime <em>Canal Date Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getCanalBooking <em>Canal Booking</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getLatestPossibleCanalDateTime <em>Latest Possible Canal Date Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getCanalArrivalTime <em>Canal Arrival Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getCanalBookingPeriod <em>Canal Booking Period</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getCanalEntrancePort <em>Canal Entrance Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public class JourneyImpl extends EventImpl implements Journey {
	/**
	 * The cached value of the '{@link #getFuels() <em>Fuels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuels()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelQuantity> fuels;

	/**
	 * The cached value of the '{@link #getDestination() <em>Destination</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestination()
	 * @generated
	 * @ordered
	 */
	protected Port destination;

	/**
	 * The default value of the '{@link #isLaden() <em>Laden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLaden()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LADEN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLaden() <em>Laden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLaden()
	 * @generated
	 * @ordered
	 */
	protected boolean laden = LADEN_EDEFAULT;

	/**
	 * The default value of the '{@link #getRouteOption() <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOption()
	 * @generated
	 * @ordered
	 */
	protected static final RouteOption ROUTE_OPTION_EDEFAULT = RouteOption.DIRECT;

	/**
	 * The cached value of the '{@link #getRouteOption() <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOption()
	 * @generated
	 * @ordered
	 */
	protected RouteOption routeOption = ROUTE_OPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getToll() <em>Toll</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToll()
	 * @generated
	 * @ordered
	 */
	protected static final int TOLL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getToll() <em>Toll</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToll()
	 * @generated
	 * @ordered
	 */
	protected int toll = TOLL_EDEFAULT;

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
	 * The default value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final double SPEED_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected double speed = SPEED_EDEFAULT;

	/**
	 * The default value of the '{@link #getCanalEntrance() <em>Canal Entrance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalEntrance()
	 * @generated
	 * @ordered
	 */
	protected static final CanalEntry CANAL_ENTRANCE_EDEFAULT = CanalEntry.NORTHSIDE;

	/**
	 * The cached value of the '{@link #getCanalEntrance() <em>Canal Entrance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalEntrance()
	 * @generated
	 * @ordered
	 */
	protected CanalEntry canalEntrance = CANAL_ENTRANCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCanalDateTime() <em>Canal Date Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalDateTime()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime CANAL_DATE_TIME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCanalDateTime() <em>Canal Date Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalDateTime()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime canalDateTime = CANAL_DATE_TIME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCanalBooking() <em>Canal Booking</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalBooking()
	 * @generated
	 * @ordered
	 */
	protected CanalBookingSlot canalBooking;

	/**
	 * The default value of the '{@link #getLatestPossibleCanalDateTime() <em>Latest Possible Canal Date Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestPossibleCanalDateTime()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime LATEST_POSSIBLE_CANAL_DATE_TIME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLatestPossibleCanalDateTime() <em>Latest Possible Canal Date Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestPossibleCanalDateTime()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime latestPossibleCanalDateTime = LATEST_POSSIBLE_CANAL_DATE_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getCanalArrivalTime() <em>Canal Arrival Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalArrivalTime()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime CANAL_ARRIVAL_TIME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCanalArrivalTime() <em>Canal Arrival Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalArrivalTime()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime canalArrivalTime = CANAL_ARRIVAL_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getCanalBookingPeriod() <em>Canal Booking Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalBookingPeriod()
	 * @generated
	 * @ordered
	 */
	protected static final PanamaBookingPeriod CANAL_BOOKING_PERIOD_EDEFAULT = PanamaBookingPeriod.STRICT;

	/**
	 * The cached value of the '{@link #getCanalBookingPeriod() <em>Canal Booking Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalBookingPeriod()
	 * @generated
	 * @ordered
	 */
	protected PanamaBookingPeriod canalBookingPeriod = CANAL_BOOKING_PERIOD_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCanalEntrancePort() <em>Canal Entrance Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalEntrancePort()
	 * @generated
	 * @ordered
	 */
	protected Port canalEntrancePort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JourneyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.JOURNEY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<FuelQuantity> getFuels() {
		if (fuels == null) {
			fuels = new EObjectContainmentEList<FuelQuantity>(FuelQuantity.class, this, SchedulePackage.JOURNEY__FUELS);
		}
		return fuels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getDestination() {
		if (destination != null && destination.eIsProxy()) {
			InternalEObject oldDestination = (InternalEObject)destination;
			destination = (Port)eResolveProxy(oldDestination);
			if (destination != oldDestination) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.JOURNEY__DESTINATION, oldDestination, destination));
			}
		}
		return destination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetDestination() {
		return destination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDestination(Port newDestination) {
		Port oldDestination = destination;
		destination = newDestination;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__DESTINATION, oldDestination, destination));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isLaden() {
		return laden;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLaden(boolean newLaden) {
		boolean oldLaden = laden;
		laden = newLaden;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__LADEN, oldLaden, laden));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RouteOption getRouteOption() {
		return routeOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRouteOption(RouteOption newRouteOption) {
		RouteOption oldRouteOption = routeOption;
		routeOption = newRouteOption == null ? ROUTE_OPTION_EDEFAULT : newRouteOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__ROUTE_OPTION, oldRouteOption, routeOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getToll() {
		return toll;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setToll(int newToll) {
		int oldToll = toll;
		toll = newToll;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__TOLL, oldToll, toll));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__DISTANCE, oldDistance, distance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getSpeed() {
		return speed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSpeed(double newSpeed) {
		double oldSpeed = speed;
		speed = newSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__SPEED, oldSpeed, speed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CanalEntry getCanalEntrance() {
		return canalEntrance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCanalEntrance(CanalEntry newCanalEntrance) {
		CanalEntry oldCanalEntrance = canalEntrance;
		canalEntrance = newCanalEntrance == null ? CANAL_ENTRANCE_EDEFAULT : newCanalEntrance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__CANAL_ENTRANCE, oldCanalEntrance, canalEntrance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getCanalDateTime() {
		return canalDateTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCanalDateTime(LocalDateTime newCanalDateTime) {
		LocalDateTime oldCanalDateTime = canalDateTime;
		canalDateTime = newCanalDateTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__CANAL_DATE_TIME, oldCanalDateTime, canalDateTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CanalBookingSlot getCanalBooking() {
		if (canalBooking != null && canalBooking.eIsProxy()) {
			InternalEObject oldCanalBooking = (InternalEObject)canalBooking;
			canalBooking = (CanalBookingSlot)eResolveProxy(oldCanalBooking);
			if (canalBooking != oldCanalBooking) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.JOURNEY__CANAL_BOOKING, oldCanalBooking, canalBooking));
			}
		}
		return canalBooking;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CanalBookingSlot basicGetCanalBooking() {
		return canalBooking;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCanalBooking(CanalBookingSlot newCanalBooking) {
		CanalBookingSlot oldCanalBooking = canalBooking;
		canalBooking = newCanalBooking;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__CANAL_BOOKING, oldCanalBooking, canalBooking));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getLatestPossibleCanalDateTime() {
		return latestPossibleCanalDateTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLatestPossibleCanalDateTime(LocalDateTime newLatestPossibleCanalDateTime) {
		LocalDateTime oldLatestPossibleCanalDateTime = latestPossibleCanalDateTime;
		latestPossibleCanalDateTime = newLatestPossibleCanalDateTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__LATEST_POSSIBLE_CANAL_DATE_TIME, oldLatestPossibleCanalDateTime, latestPossibleCanalDateTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getCanalArrivalTime() {
		return canalArrivalTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCanalArrivalTime(LocalDateTime newCanalArrivalTime) {
		LocalDateTime oldCanalArrivalTime = canalArrivalTime;
		canalArrivalTime = newCanalArrivalTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__CANAL_ARRIVAL_TIME, oldCanalArrivalTime, canalArrivalTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PanamaBookingPeriod getCanalBookingPeriod() {
		return canalBookingPeriod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCanalBookingPeriod(PanamaBookingPeriod newCanalBookingPeriod) {
		PanamaBookingPeriod oldCanalBookingPeriod = canalBookingPeriod;
		canalBookingPeriod = newCanalBookingPeriod == null ? CANAL_BOOKING_PERIOD_EDEFAULT : newCanalBookingPeriod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__CANAL_BOOKING_PERIOD, oldCanalBookingPeriod, canalBookingPeriod));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getCanalEntrancePort() {
		if (canalEntrancePort != null && canalEntrancePort.eIsProxy()) {
			InternalEObject oldCanalEntrancePort = (InternalEObject)canalEntrancePort;
			canalEntrancePort = (Port)eResolveProxy(oldCanalEntrancePort);
			if (canalEntrancePort != oldCanalEntrancePort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.JOURNEY__CANAL_ENTRANCE_PORT, oldCanalEntrancePort, canalEntrancePort));
			}
		}
		return canalEntrancePort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetCanalEntrancePort() {
		return canalEntrancePort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCanalEntrancePort(Port newCanalEntrancePort) {
		Port oldCanalEntrancePort = canalEntrancePort;
		canalEntrancePort = newCanalEntrancePort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__CANAL_ENTRANCE_PORT, oldCanalEntrancePort, canalEntrancePort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getFuelCost() {
		double sum = 0.0;
		for (final FuelQuantity u : getFuels()) {
			sum += u.getCost();
		}
		return sum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.JOURNEY__FUELS:
				return ((InternalEList<?>)getFuels()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.JOURNEY__FUELS:
				return getFuels();
			case SchedulePackage.JOURNEY__DESTINATION:
				if (resolve) return getDestination();
				return basicGetDestination();
			case SchedulePackage.JOURNEY__LADEN:
				return isLaden();
			case SchedulePackage.JOURNEY__ROUTE_OPTION:
				return getRouteOption();
			case SchedulePackage.JOURNEY__TOLL:
				return getToll();
			case SchedulePackage.JOURNEY__DISTANCE:
				return getDistance();
			case SchedulePackage.JOURNEY__SPEED:
				return getSpeed();
			case SchedulePackage.JOURNEY__CANAL_ENTRANCE:
				return getCanalEntrance();
			case SchedulePackage.JOURNEY__CANAL_DATE_TIME:
				return getCanalDateTime();
			case SchedulePackage.JOURNEY__CANAL_BOOKING:
				if (resolve) return getCanalBooking();
				return basicGetCanalBooking();
			case SchedulePackage.JOURNEY__LATEST_POSSIBLE_CANAL_DATE_TIME:
				return getLatestPossibleCanalDateTime();
			case SchedulePackage.JOURNEY__CANAL_ARRIVAL_TIME:
				return getCanalArrivalTime();
			case SchedulePackage.JOURNEY__CANAL_BOOKING_PERIOD:
				return getCanalBookingPeriod();
			case SchedulePackage.JOURNEY__CANAL_ENTRANCE_PORT:
				if (resolve) return getCanalEntrancePort();
				return basicGetCanalEntrancePort();
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
			case SchedulePackage.JOURNEY__FUELS:
				getFuels().clear();
				getFuels().addAll((Collection<? extends FuelQuantity>)newValue);
				return;
			case SchedulePackage.JOURNEY__DESTINATION:
				setDestination((Port)newValue);
				return;
			case SchedulePackage.JOURNEY__LADEN:
				setLaden((Boolean)newValue);
				return;
			case SchedulePackage.JOURNEY__ROUTE_OPTION:
				setRouteOption((RouteOption)newValue);
				return;
			case SchedulePackage.JOURNEY__TOLL:
				setToll((Integer)newValue);
				return;
			case SchedulePackage.JOURNEY__DISTANCE:
				setDistance((Integer)newValue);
				return;
			case SchedulePackage.JOURNEY__SPEED:
				setSpeed((Double)newValue);
				return;
			case SchedulePackage.JOURNEY__CANAL_ENTRANCE:
				setCanalEntrance((CanalEntry)newValue);
				return;
			case SchedulePackage.JOURNEY__CANAL_DATE_TIME:
				setCanalDateTime((LocalDateTime)newValue);
				return;
			case SchedulePackage.JOURNEY__CANAL_BOOKING:
				setCanalBooking((CanalBookingSlot)newValue);
				return;
			case SchedulePackage.JOURNEY__LATEST_POSSIBLE_CANAL_DATE_TIME:
				setLatestPossibleCanalDateTime((LocalDateTime)newValue);
				return;
			case SchedulePackage.JOURNEY__CANAL_ARRIVAL_TIME:
				setCanalArrivalTime((LocalDateTime)newValue);
				return;
			case SchedulePackage.JOURNEY__CANAL_BOOKING_PERIOD:
				setCanalBookingPeriod((PanamaBookingPeriod)newValue);
				return;
			case SchedulePackage.JOURNEY__CANAL_ENTRANCE_PORT:
				setCanalEntrancePort((Port)newValue);
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
			case SchedulePackage.JOURNEY__FUELS:
				getFuels().clear();
				return;
			case SchedulePackage.JOURNEY__DESTINATION:
				setDestination((Port)null);
				return;
			case SchedulePackage.JOURNEY__LADEN:
				setLaden(LADEN_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__ROUTE_OPTION:
				setRouteOption(ROUTE_OPTION_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__TOLL:
				setToll(TOLL_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__DISTANCE:
				setDistance(DISTANCE_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__SPEED:
				setSpeed(SPEED_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__CANAL_ENTRANCE:
				setCanalEntrance(CANAL_ENTRANCE_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__CANAL_DATE_TIME:
				setCanalDateTime(CANAL_DATE_TIME_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__CANAL_BOOKING:
				setCanalBooking((CanalBookingSlot)null);
				return;
			case SchedulePackage.JOURNEY__LATEST_POSSIBLE_CANAL_DATE_TIME:
				setLatestPossibleCanalDateTime(LATEST_POSSIBLE_CANAL_DATE_TIME_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__CANAL_ARRIVAL_TIME:
				setCanalArrivalTime(CANAL_ARRIVAL_TIME_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__CANAL_BOOKING_PERIOD:
				setCanalBookingPeriod(CANAL_BOOKING_PERIOD_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__CANAL_ENTRANCE_PORT:
				setCanalEntrancePort((Port)null);
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
			case SchedulePackage.JOURNEY__FUELS:
				return fuels != null && !fuels.isEmpty();
			case SchedulePackage.JOURNEY__DESTINATION:
				return destination != null;
			case SchedulePackage.JOURNEY__LADEN:
				return laden != LADEN_EDEFAULT;
			case SchedulePackage.JOURNEY__ROUTE_OPTION:
				return routeOption != ROUTE_OPTION_EDEFAULT;
			case SchedulePackage.JOURNEY__TOLL:
				return toll != TOLL_EDEFAULT;
			case SchedulePackage.JOURNEY__DISTANCE:
				return distance != DISTANCE_EDEFAULT;
			case SchedulePackage.JOURNEY__SPEED:
				return speed != SPEED_EDEFAULT;
			case SchedulePackage.JOURNEY__CANAL_ENTRANCE:
				return canalEntrance != CANAL_ENTRANCE_EDEFAULT;
			case SchedulePackage.JOURNEY__CANAL_DATE_TIME:
				return CANAL_DATE_TIME_EDEFAULT == null ? canalDateTime != null : !CANAL_DATE_TIME_EDEFAULT.equals(canalDateTime);
			case SchedulePackage.JOURNEY__CANAL_BOOKING:
				return canalBooking != null;
			case SchedulePackage.JOURNEY__LATEST_POSSIBLE_CANAL_DATE_TIME:
				return LATEST_POSSIBLE_CANAL_DATE_TIME_EDEFAULT == null ? latestPossibleCanalDateTime != null : !LATEST_POSSIBLE_CANAL_DATE_TIME_EDEFAULT.equals(latestPossibleCanalDateTime);
			case SchedulePackage.JOURNEY__CANAL_ARRIVAL_TIME:
				return CANAL_ARRIVAL_TIME_EDEFAULT == null ? canalArrivalTime != null : !CANAL_ARRIVAL_TIME_EDEFAULT.equals(canalArrivalTime);
			case SchedulePackage.JOURNEY__CANAL_BOOKING_PERIOD:
				return canalBookingPeriod != CANAL_BOOKING_PERIOD_EDEFAULT;
			case SchedulePackage.JOURNEY__CANAL_ENTRANCE_PORT:
				return canalEntrancePort != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == FuelUsage.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.JOURNEY__FUELS: return SchedulePackage.FUEL_USAGE__FUELS;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == FuelUsage.class) {
			switch (baseFeatureID) {
				case SchedulePackage.FUEL_USAGE__FUELS: return SchedulePackage.JOURNEY__FUELS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == FuelUsage.class) {
			switch (baseOperationID) {
				case SchedulePackage.FUEL_USAGE___GET_FUEL_COST: return SchedulePackage.JOURNEY___GET_FUEL_COST;
				default: return -1;
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.JOURNEY___GET_FUEL_COST:
				return getFuelCost();
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (laden: ");
		result.append(laden);
		result.append(", routeOption: ");
		result.append(routeOption);
		result.append(", toll: ");
		result.append(toll);
		result.append(", distance: ");
		result.append(distance);
		result.append(", speed: ");
		result.append(speed);
		result.append(", canalEntrance: ");
		result.append(canalEntrance);
		result.append(", canalDateTime: ");
		result.append(canalDateTime);
		result.append(", latestPossibleCanalDateTime: ");
		result.append(latestPossibleCanalDateTime);
		result.append(", canalArrivalTime: ");
		result.append(canalArrivalTime);
		result.append(", canalBookingPeriod: ");
		result.append(canalBookingPeriod);
		result.append(')');
		return result.toString();
	}
	
	/**
	 * @generated NOT
	 */
	@Override
	public String type() {
		return "Journey";
	}
	
	/**
	 * @generated NOT
	 */
	@Override
	public String name() {
		
		// Work backwards through the events list to find a slot or cargo to get the name
		final EObject container = this.eContainer();
		if (container instanceof Sequence) {
			final Sequence sequence = (Sequence) container;
			final EList<Event> events = sequence.getEvents();
			int idx = events.indexOf(this);
			if (idx >= 0) {
				while (--idx >= 0) {
					final EObject namedObject = events.get(idx);
					if (namedObject instanceof SlotVisit) {
						return ((SlotVisit) namedObject).name();
					} else if (namedObject instanceof VesselEventVisit) {
						return ((VesselEventVisit) namedObject).name();
					}
				}
			}
		}

		return super.name();
	}

	/**
	 * @generated NOT
	 */
	public String getTimeZone(EAttribute attribute) {
		if (attribute == SchedulePackage.Literals.EVENT__END) {
			final Port p = getDestination();
			if (p == null) {
				return "UTC";
			}
			Location l = p.getLocation();
			if (l == null || l.getTimeZone() == null || l.getTimeZone().isEmpty()) {
				return "UTC";
			}
			return l.getTimeZone();
		} else	if (attribute == SchedulePackage.Literals.JOURNEY__CANAL_DATE_TIME //
				|| attribute == SchedulePackage.Literals.JOURNEY__CANAL_ARRIVAL_TIME //
				|| attribute == SchedulePackage.Literals.JOURNEY__LATEST_POSSIBLE_CANAL_DATE_TIME) {
			final Port p = getCanalEntrancePort();
			if (p == null) {
				return "UTC";
			}
			Location l = p.getLocation();
			if (l == null || l.getTimeZone() == null || l.getTimeZone().isEmpty()) {
				return "UTC";
			}
			return l.getTimeZone();
		} else {
			return super.getTimeZone(attribute);
		}
	}
	
} // end of JourneyImpl

// finish type fixing
