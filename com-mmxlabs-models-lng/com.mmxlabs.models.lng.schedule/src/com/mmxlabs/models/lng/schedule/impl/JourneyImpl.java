/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.port.EntryPoint;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
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

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
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
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getRoute <em>Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getToll <em>Toll</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getDistance <em>Distance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getCanalEntry <em>Canal Entry</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getCanalDate <em>Canal Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getCanalBooking <em>Canal Booking</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getLatestPossibleCanalDate <em>Latest Possible Canal Date</em>}</li>
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
	 * The cached value of the '{@link #getRoute() <em>Route</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected Route route;

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
	 * The cached value of the '{@link #getCanalEntry() <em>Canal Entry</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalEntry()
	 * @generated
	 * @ordered
	 */
	protected EntryPoint canalEntry;

	/**
	 * The default value of the '{@link #getCanalDate() <em>Canal Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate CANAL_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCanalDate() <em>Canal Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate canalDate = CANAL_DATE_EDEFAULT;

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
	 * The default value of the '{@link #getLatestPossibleCanalDate() <em>Latest Possible Canal Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestPossibleCanalDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate LATEST_POSSIBLE_CANAL_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLatestPossibleCanalDate() <em>Latest Possible Canal Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestPossibleCanalDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate latestPossibleCanalDate = LATEST_POSSIBLE_CANAL_DATE_EDEFAULT;

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
	public boolean isLaden() {
		return laden;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public Route getRoute() {
		if (route != null && route.eIsProxy()) {
			InternalEObject oldRoute = (InternalEObject)route;
			route = (Route)eResolveProxy(oldRoute);
			if (route != oldRoute) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.JOURNEY__ROUTE, oldRoute, route));
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
	public void setRoute(Route newRoute) {
		Route oldRoute = route;
		route = newRoute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__ROUTE, oldRoute, route));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getToll() {
		return toll;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public int getDistance() {
		return distance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public double getSpeed() {
		return speed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public EntryPoint getCanalEntry() {
		if (canalEntry != null && canalEntry.eIsProxy()) {
			InternalEObject oldCanalEntry = (InternalEObject)canalEntry;
			canalEntry = (EntryPoint)eResolveProxy(oldCanalEntry);
			if (canalEntry != oldCanalEntry) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.JOURNEY__CANAL_ENTRY, oldCanalEntry, canalEntry));
			}
		}
		return canalEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntryPoint basicGetCanalEntry() {
		return canalEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCanalEntry(EntryPoint newCanalEntry) {
		EntryPoint oldCanalEntry = canalEntry;
		canalEntry = newCanalEntry;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__CANAL_ENTRY, oldCanalEntry, canalEntry));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getCanalDate() {
		return canalDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCanalDate(LocalDate newCanalDate) {
		LocalDate oldCanalDate = canalDate;
		canalDate = newCanalDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__CANAL_DATE, oldCanalDate, canalDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public LocalDate getLatestPossibleCanalDate() {
		return latestPossibleCanalDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLatestPossibleCanalDate(LocalDate newLatestPossibleCanalDate) {
		LocalDate oldLatestPossibleCanalDate = latestPossibleCanalDate;
		latestPossibleCanalDate = newLatestPossibleCanalDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__LATEST_POSSIBLE_CANAL_DATE, oldLatestPossibleCanalDate, latestPossibleCanalDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getFuelCost() {
		int sum = 0;
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
			case SchedulePackage.JOURNEY__ROUTE:
				if (resolve) return getRoute();
				return basicGetRoute();
			case SchedulePackage.JOURNEY__TOLL:
				return getToll();
			case SchedulePackage.JOURNEY__DISTANCE:
				return getDistance();
			case SchedulePackage.JOURNEY__SPEED:
				return getSpeed();
			case SchedulePackage.JOURNEY__CANAL_ENTRY:
				if (resolve) return getCanalEntry();
				return basicGetCanalEntry();
			case SchedulePackage.JOURNEY__CANAL_DATE:
				return getCanalDate();
			case SchedulePackage.JOURNEY__CANAL_BOOKING:
				if (resolve) return getCanalBooking();
				return basicGetCanalBooking();
			case SchedulePackage.JOURNEY__LATEST_POSSIBLE_CANAL_DATE:
				return getLatestPossibleCanalDate();
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
			case SchedulePackage.JOURNEY__ROUTE:
				setRoute((Route)newValue);
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
			case SchedulePackage.JOURNEY__CANAL_ENTRY:
				setCanalEntry((EntryPoint)newValue);
				return;
			case SchedulePackage.JOURNEY__CANAL_DATE:
				setCanalDate((LocalDate)newValue);
				return;
			case SchedulePackage.JOURNEY__CANAL_BOOKING:
				setCanalBooking((CanalBookingSlot)newValue);
				return;
			case SchedulePackage.JOURNEY__LATEST_POSSIBLE_CANAL_DATE:
				setLatestPossibleCanalDate((LocalDate)newValue);
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
			case SchedulePackage.JOURNEY__ROUTE:
				setRoute((Route)null);
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
			case SchedulePackage.JOURNEY__CANAL_ENTRY:
				setCanalEntry((EntryPoint)null);
				return;
			case SchedulePackage.JOURNEY__CANAL_DATE:
				setCanalDate(CANAL_DATE_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__CANAL_BOOKING:
				setCanalBooking((CanalBookingSlot)null);
				return;
			case SchedulePackage.JOURNEY__LATEST_POSSIBLE_CANAL_DATE:
				setLatestPossibleCanalDate(LATEST_POSSIBLE_CANAL_DATE_EDEFAULT);
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
			case SchedulePackage.JOURNEY__ROUTE:
				return route != null;
			case SchedulePackage.JOURNEY__TOLL:
				return toll != TOLL_EDEFAULT;
			case SchedulePackage.JOURNEY__DISTANCE:
				return distance != DISTANCE_EDEFAULT;
			case SchedulePackage.JOURNEY__SPEED:
				return speed != SPEED_EDEFAULT;
			case SchedulePackage.JOURNEY__CANAL_ENTRY:
				return canalEntry != null;
			case SchedulePackage.JOURNEY__CANAL_DATE:
				return CANAL_DATE_EDEFAULT == null ? canalDate != null : !CANAL_DATE_EDEFAULT.equals(canalDate);
			case SchedulePackage.JOURNEY__CANAL_BOOKING:
				return canalBooking != null;
			case SchedulePackage.JOURNEY__LATEST_POSSIBLE_CANAL_DATE:
				return LATEST_POSSIBLE_CANAL_DATE_EDEFAULT == null ? latestPossibleCanalDate != null : !LATEST_POSSIBLE_CANAL_DATE_EDEFAULT.equals(latestPossibleCanalDate);
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (laden: ");
		result.append(laden);
		result.append(", toll: ");
		result.append(toll);
		result.append(", distance: ");
		result.append(distance);
		result.append(", speed: ");
		result.append(speed);
		result.append(", canalDate: ");
		result.append(canalDate);
		result.append(", latestPossibleCanalDate: ");
		result.append(latestPossibleCanalDate);
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
			if (p == null || p.getTimeZone() == null || p.getTimeZone().isEmpty()) {
				return "UTC";
			}
			return p.getTimeZone();
		} else {
			return super.getTimeZone(attribute);
		}
	}
	
} // end of JourneyImpl

// finish type fixing
