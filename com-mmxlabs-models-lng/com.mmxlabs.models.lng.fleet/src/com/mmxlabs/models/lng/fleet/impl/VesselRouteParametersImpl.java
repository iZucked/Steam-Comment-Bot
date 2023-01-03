/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselRouteParameters;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Route Parameters</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselRouteParametersImpl#getRouteOption <em>Route Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselRouteParametersImpl#getExtraTransitTime <em>Extra Transit Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselRouteParametersImpl#getLadenConsumptionRate <em>Laden Consumption Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselRouteParametersImpl#getLadenNBORate <em>Laden NBO Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselRouteParametersImpl#getBallastConsumptionRate <em>Ballast Consumption Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselRouteParametersImpl#getBallastNBORate <em>Ballast NBO Rate</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselRouteParametersImpl extends MMXObjectImpl implements VesselRouteParameters {
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
	 * The default value of the '{@link #getExtraTransitTime() <em>Extra Transit Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraTransitTime()
	 * @generated
	 * @ordered
	 */
	protected static final int EXTRA_TRANSIT_TIME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getExtraTransitTime() <em>Extra Transit Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraTransitTime()
	 * @generated
	 * @ordered
	 */
	protected int extraTransitTime = EXTRA_TRANSIT_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getLadenConsumptionRate() <em>Laden Consumption Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenConsumptionRate()
	 * @generated
	 * @ordered
	 */
	protected static final double LADEN_CONSUMPTION_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLadenConsumptionRate() <em>Laden Consumption Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenConsumptionRate()
	 * @generated
	 * @ordered
	 */
	protected double ladenConsumptionRate = LADEN_CONSUMPTION_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLadenNBORate() <em>Laden NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenNBORate()
	 * @generated
	 * @ordered
	 */
	protected static final double LADEN_NBO_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLadenNBORate() <em>Laden NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenNBORate()
	 * @generated
	 * @ordered
	 */
	protected double ladenNBORate = LADEN_NBO_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBallastConsumptionRate() <em>Ballast Consumption Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastConsumptionRate()
	 * @generated
	 * @ordered
	 */
	protected static final double BALLAST_CONSUMPTION_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getBallastConsumptionRate() <em>Ballast Consumption Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastConsumptionRate()
	 * @generated
	 * @ordered
	 */
	protected double ballastConsumptionRate = BALLAST_CONSUMPTION_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBallastNBORate() <em>Ballast NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastNBORate()
	 * @generated
	 * @ordered
	 */
	protected static final double BALLAST_NBO_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getBallastNBORate() <em>Ballast NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastNBORate()
	 * @generated
	 * @ordered
	 */
	protected double ballastNBORate = BALLAST_NBO_RATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselRouteParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL_ROUTE_PARAMETERS;
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
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_ROUTE_PARAMETERS__ROUTE_OPTION, oldRouteOption, routeOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getExtraTransitTime() {
		return extraTransitTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExtraTransitTime(int newExtraTransitTime) {
		int oldExtraTransitTime = extraTransitTime;
		extraTransitTime = newExtraTransitTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME, oldExtraTransitTime, extraTransitTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getLadenConsumptionRate() {
		return ladenConsumptionRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenConsumptionRate(double newLadenConsumptionRate) {
		double oldLadenConsumptionRate = ladenConsumptionRate;
		ladenConsumptionRate = newLadenConsumptionRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE, oldLadenConsumptionRate, ladenConsumptionRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getLadenNBORate() {
		return ladenNBORate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenNBORate(double newLadenNBORate) {
		double oldLadenNBORate = ladenNBORate;
		ladenNBORate = newLadenNBORate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_ROUTE_PARAMETERS__LADEN_NBO_RATE, oldLadenNBORate, ladenNBORate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @generated
	 */
	@Override
	public double getBallastConsumptionRate() {
		return ballastConsumptionRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastConsumptionRate(double newBallastConsumptionRate) {
		double oldBallastConsumptionRate = ballastConsumptionRate;
		ballastConsumptionRate = newBallastConsumptionRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE, oldBallastConsumptionRate, ballastConsumptionRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getBallastNBORate() {
		return ballastNBORate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastNBORate(double newBallastNBORate) {
		double oldBallastNBORate = ballastNBORate;
		ballastNBORate = newBallastNBORate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_ROUTE_PARAMETERS__BALLAST_NBO_RATE, oldBallastNBORate, ballastNBORate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__ROUTE_OPTION:
				return getRouteOption();
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME:
				return getExtraTransitTime();
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE:
				return getLadenConsumptionRate();
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__LADEN_NBO_RATE:
				return getLadenNBORate();
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE:
				return getBallastConsumptionRate();
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__BALLAST_NBO_RATE:
				return getBallastNBORate();
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
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__ROUTE_OPTION:
				setRouteOption((RouteOption)newValue);
				return;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME:
				setExtraTransitTime((Integer)newValue);
				return;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE:
				setLadenConsumptionRate((Double)newValue);
				return;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__LADEN_NBO_RATE:
				setLadenNBORate((Double)newValue);
				return;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE:
				setBallastConsumptionRate((Double)newValue);
				return;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__BALLAST_NBO_RATE:
				setBallastNBORate((Double)newValue);
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
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__ROUTE_OPTION:
				setRouteOption(ROUTE_OPTION_EDEFAULT);
				return;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME:
				setExtraTransitTime(EXTRA_TRANSIT_TIME_EDEFAULT);
				return;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE:
				setLadenConsumptionRate(LADEN_CONSUMPTION_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__LADEN_NBO_RATE:
				setLadenNBORate(LADEN_NBO_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE:
				setBallastConsumptionRate(BALLAST_CONSUMPTION_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__BALLAST_NBO_RATE:
				setBallastNBORate(BALLAST_NBO_RATE_EDEFAULT);
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
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__ROUTE_OPTION:
				return routeOption != ROUTE_OPTION_EDEFAULT;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME:
				return extraTransitTime != EXTRA_TRANSIT_TIME_EDEFAULT;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE:
				return ladenConsumptionRate != LADEN_CONSUMPTION_RATE_EDEFAULT;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__LADEN_NBO_RATE:
				return ladenNBORate != LADEN_NBO_RATE_EDEFAULT;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE:
				return ballastConsumptionRate != BALLAST_CONSUMPTION_RATE_EDEFAULT;
			case FleetPackage.VESSEL_ROUTE_PARAMETERS__BALLAST_NBO_RATE:
				return ballastNBORate != BALLAST_NBO_RATE_EDEFAULT;
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
		result.append(" (routeOption: ");
		result.append(routeOption);
		result.append(", extraTransitTime: ");
		result.append(extraTransitTime);
		result.append(", ladenConsumptionRate: ");
		result.append(ladenConsumptionRate);
		result.append(", ladenNBORate: ");
		result.append(ladenNBORate);
		result.append(", ballastConsumptionRate: ");
		result.append(ballastConsumptionRate);
		result.append(", ballastNBORate: ");
		result.append(ballastNBORate);
		result.append(')');
		return result.toString();
	}

} // end of VesselRouteParametersImpl

// finish type fixing
