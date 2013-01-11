/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.Voyage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Voyage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.VoyageImpl#getRoute <em>Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.VoyageImpl#getRouteCost <em>Route Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.VoyageImpl#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.VoyageImpl#getDistance <em>Distance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.VoyageImpl#getIdleTime <em>Idle Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.VoyageImpl#getTravelTime <em>Travel Time</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VoyageImpl extends CostComponentImpl implements Voyage {
	/**
	 * The default value of the '{@link #getRoute() <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected static final String ROUTE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRoute() <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected String route = ROUTE_EDEFAULT;

	/**
	 * The default value of the '{@link #getRouteCost() <em>Route Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteCost()
	 * @generated
	 * @ordered
	 */
	protected static final int ROUTE_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRouteCost() <em>Route Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteCost()
	 * @generated
	 * @ordered
	 */
	protected int routeCost = ROUTE_COST_EDEFAULT;

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
	 * The default value of the '{@link #getIdleTime() <em>Idle Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdleTime()
	 * @generated
	 * @ordered
	 */
	protected static final int IDLE_TIME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getIdleTime() <em>Idle Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdleTime()
	 * @generated
	 * @ordered
	 */
	protected int idleTime = IDLE_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getTravelTime() <em>Travel Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTravelTime()
	 * @generated
	 * @ordered
	 */
	protected static final int TRAVEL_TIME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTravelTime() <em>Travel Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTravelTime()
	 * @generated
	 * @ordered
	 */
	protected int travelTime = TRAVEL_TIME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VoyageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.VOYAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRoute() {
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRoute(String newRoute) {
		String oldRoute = route;
		route = newRoute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VOYAGE__ROUTE, oldRoute, route));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getRouteCost() {
		return routeCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRouteCost(int newRouteCost) {
		int oldRouteCost = routeCost;
		routeCost = newRouteCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VOYAGE__ROUTE_COST, oldRouteCost, routeCost));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VOYAGE__SPEED, oldSpeed, speed));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VOYAGE__DISTANCE, oldDistance, distance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getIdleTime() {
		return idleTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdleTime(int newIdleTime) {
		int oldIdleTime = idleTime;
		idleTime = newIdleTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VOYAGE__IDLE_TIME, oldIdleTime, idleTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTravelTime() {
		return travelTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTravelTime(int newTravelTime) {
		int oldTravelTime = travelTime;
		travelTime = newTravelTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VOYAGE__TRAVEL_TIME, oldTravelTime, travelTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.VOYAGE__ROUTE:
				return getRoute();
			case AnalyticsPackage.VOYAGE__ROUTE_COST:
				return getRouteCost();
			case AnalyticsPackage.VOYAGE__SPEED:
				return getSpeed();
			case AnalyticsPackage.VOYAGE__DISTANCE:
				return getDistance();
			case AnalyticsPackage.VOYAGE__IDLE_TIME:
				return getIdleTime();
			case AnalyticsPackage.VOYAGE__TRAVEL_TIME:
				return getTravelTime();
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
			case AnalyticsPackage.VOYAGE__ROUTE:
				setRoute((String)newValue);
				return;
			case AnalyticsPackage.VOYAGE__ROUTE_COST:
				setRouteCost((Integer)newValue);
				return;
			case AnalyticsPackage.VOYAGE__SPEED:
				setSpeed((Double)newValue);
				return;
			case AnalyticsPackage.VOYAGE__DISTANCE:
				setDistance((Integer)newValue);
				return;
			case AnalyticsPackage.VOYAGE__IDLE_TIME:
				setIdleTime((Integer)newValue);
				return;
			case AnalyticsPackage.VOYAGE__TRAVEL_TIME:
				setTravelTime((Integer)newValue);
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
			case AnalyticsPackage.VOYAGE__ROUTE:
				setRoute(ROUTE_EDEFAULT);
				return;
			case AnalyticsPackage.VOYAGE__ROUTE_COST:
				setRouteCost(ROUTE_COST_EDEFAULT);
				return;
			case AnalyticsPackage.VOYAGE__SPEED:
				setSpeed(SPEED_EDEFAULT);
				return;
			case AnalyticsPackage.VOYAGE__DISTANCE:
				setDistance(DISTANCE_EDEFAULT);
				return;
			case AnalyticsPackage.VOYAGE__IDLE_TIME:
				setIdleTime(IDLE_TIME_EDEFAULT);
				return;
			case AnalyticsPackage.VOYAGE__TRAVEL_TIME:
				setTravelTime(TRAVEL_TIME_EDEFAULT);
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
			case AnalyticsPackage.VOYAGE__ROUTE:
				return ROUTE_EDEFAULT == null ? route != null : !ROUTE_EDEFAULT.equals(route);
			case AnalyticsPackage.VOYAGE__ROUTE_COST:
				return routeCost != ROUTE_COST_EDEFAULT;
			case AnalyticsPackage.VOYAGE__SPEED:
				return speed != SPEED_EDEFAULT;
			case AnalyticsPackage.VOYAGE__DISTANCE:
				return distance != DISTANCE_EDEFAULT;
			case AnalyticsPackage.VOYAGE__IDLE_TIME:
				return idleTime != IDLE_TIME_EDEFAULT;
			case AnalyticsPackage.VOYAGE__TRAVEL_TIME:
				return travelTime != TRAVEL_TIME_EDEFAULT;
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
		result.append(" (route: ");
		result.append(route);
		result.append(", routeCost: ");
		result.append(routeCost);
		result.append(", speed: ");
		result.append(speed);
		result.append(", distance: ");
		result.append(distance);
		result.append(", idleTime: ");
		result.append(idleTime);
		result.append(", travelTime: ");
		result.append(travelTime);
		result.append(')');
		return result.toString();
	}

} // end of VoyageImpl

// finish type fixing
