/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRowOptions;

import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.lng.port.RouteOption;

import java.time.LocalDateTime;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Base Case Row Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowOptionsImpl#getLadenRoute <em>Laden Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowOptionsImpl#getBallastRoute <em>Ballast Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowOptionsImpl#getLadenFuelChoice <em>Laden Fuel Choice</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowOptionsImpl#getBallastFuelChoice <em>Ballast Fuel Choice</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowOptionsImpl#getLoadDate <em>Load Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowOptionsImpl#getDischargeDate <em>Discharge Date</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BaseCaseRowOptionsImpl extends EObjectImpl implements BaseCaseRowOptions {
	/**
	 * The default value of the '{@link #getLadenRoute() <em>Laden Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenRoute()
	 * @generated
	 * @ordered
	 */
	protected static final RouteOption LADEN_ROUTE_EDEFAULT = RouteOption.DIRECT;

	/**
	 * The cached value of the '{@link #getLadenRoute() <em>Laden Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenRoute()
	 * @generated
	 * @ordered
	 */
	protected RouteOption ladenRoute = LADEN_ROUTE_EDEFAULT;

	/**
	 * This is true if the Laden Route attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean ladenRouteESet;

	/**
	 * The default value of the '{@link #getBallastRoute() <em>Ballast Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastRoute()
	 * @generated
	 * @ordered
	 */
	protected static final RouteOption BALLAST_ROUTE_EDEFAULT = RouteOption.DIRECT;

	/**
	 * The cached value of the '{@link #getBallastRoute() <em>Ballast Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastRoute()
	 * @generated
	 * @ordered
	 */
	protected RouteOption ballastRoute = BALLAST_ROUTE_EDEFAULT;

	/**
	 * This is true if the Ballast Route attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean ballastRouteESet;

	/**
	 * The default value of the '{@link #getLadenFuelChoice() <em>Laden Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenFuelChoice()
	 * @generated
	 * @ordered
	 */
	protected static final FuelChoice LADEN_FUEL_CHOICE_EDEFAULT = FuelChoice.NBO_FBO;

	/**
	 * The cached value of the '{@link #getLadenFuelChoice() <em>Laden Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenFuelChoice()
	 * @generated
	 * @ordered
	 */
	protected FuelChoice ladenFuelChoice = LADEN_FUEL_CHOICE_EDEFAULT;

	/**
	 * This is true if the Laden Fuel Choice attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean ladenFuelChoiceESet;

	/**
	 * The default value of the '{@link #getBallastFuelChoice() <em>Ballast Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastFuelChoice()
	 * @generated
	 * @ordered
	 */
	protected static final FuelChoice BALLAST_FUEL_CHOICE_EDEFAULT = FuelChoice.NBO_FBO;

	/**
	 * The cached value of the '{@link #getBallastFuelChoice() <em>Ballast Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastFuelChoice()
	 * @generated
	 * @ordered
	 */
	protected FuelChoice ballastFuelChoice = BALLAST_FUEL_CHOICE_EDEFAULT;

	/**
	 * This is true if the Ballast Fuel Choice attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean ballastFuelChoiceESet;

	/**
	 * The default value of the '{@link #getLoadDate() <em>Load Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime LOAD_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLoadDate() <em>Load Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime loadDate = LOAD_DATE_EDEFAULT;

	/**
	 * This is true if the Load Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean loadDateESet;

	/**
	 * The default value of the '{@link #getDischargeDate() <em>Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime DISCHARGE_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDischargeDate() <em>Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime dischargeDate = DISCHARGE_DATE_EDEFAULT;

	/**
	 * This is true if the Discharge Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean dischargeDateESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseCaseRowOptionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RouteOption getLadenRoute() {
		return ladenRoute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenRoute(RouteOption newLadenRoute) {
		RouteOption oldLadenRoute = ladenRoute;
		ladenRoute = newLadenRoute == null ? LADEN_ROUTE_EDEFAULT : newLadenRoute;
		boolean oldLadenRouteESet = ladenRouteESet;
		ladenRouteESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_ROUTE, oldLadenRoute, ladenRoute, !oldLadenRouteESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetLadenRoute() {
		RouteOption oldLadenRoute = ladenRoute;
		boolean oldLadenRouteESet = ladenRouteESet;
		ladenRoute = LADEN_ROUTE_EDEFAULT;
		ladenRouteESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_ROUTE, oldLadenRoute, LADEN_ROUTE_EDEFAULT, oldLadenRouteESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetLadenRoute() {
		return ladenRouteESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RouteOption getBallastRoute() {
		return ballastRoute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastRoute(RouteOption newBallastRoute) {
		RouteOption oldBallastRoute = ballastRoute;
		ballastRoute = newBallastRoute == null ? BALLAST_ROUTE_EDEFAULT : newBallastRoute;
		boolean oldBallastRouteESet = ballastRouteESet;
		ballastRouteESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_ROUTE, oldBallastRoute, ballastRoute, !oldBallastRouteESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetBallastRoute() {
		RouteOption oldBallastRoute = ballastRoute;
		boolean oldBallastRouteESet = ballastRouteESet;
		ballastRoute = BALLAST_ROUTE_EDEFAULT;
		ballastRouteESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_ROUTE, oldBallastRoute, BALLAST_ROUTE_EDEFAULT, oldBallastRouteESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetBallastRoute() {
		return ballastRouteESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FuelChoice getLadenFuelChoice() {
		return ladenFuelChoice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenFuelChoice(FuelChoice newLadenFuelChoice) {
		FuelChoice oldLadenFuelChoice = ladenFuelChoice;
		ladenFuelChoice = newLadenFuelChoice == null ? LADEN_FUEL_CHOICE_EDEFAULT : newLadenFuelChoice;
		boolean oldLadenFuelChoiceESet = ladenFuelChoiceESet;
		ladenFuelChoiceESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICE, oldLadenFuelChoice, ladenFuelChoice, !oldLadenFuelChoiceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetLadenFuelChoice() {
		FuelChoice oldLadenFuelChoice = ladenFuelChoice;
		boolean oldLadenFuelChoiceESet = ladenFuelChoiceESet;
		ladenFuelChoice = LADEN_FUEL_CHOICE_EDEFAULT;
		ladenFuelChoiceESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICE, oldLadenFuelChoice, LADEN_FUEL_CHOICE_EDEFAULT, oldLadenFuelChoiceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetLadenFuelChoice() {
		return ladenFuelChoiceESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FuelChoice getBallastFuelChoice() {
		return ballastFuelChoice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastFuelChoice(FuelChoice newBallastFuelChoice) {
		FuelChoice oldBallastFuelChoice = ballastFuelChoice;
		ballastFuelChoice = newBallastFuelChoice == null ? BALLAST_FUEL_CHOICE_EDEFAULT : newBallastFuelChoice;
		boolean oldBallastFuelChoiceESet = ballastFuelChoiceESet;
		ballastFuelChoiceESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICE, oldBallastFuelChoice, ballastFuelChoice, !oldBallastFuelChoiceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetBallastFuelChoice() {
		FuelChoice oldBallastFuelChoice = ballastFuelChoice;
		boolean oldBallastFuelChoiceESet = ballastFuelChoiceESet;
		ballastFuelChoice = BALLAST_FUEL_CHOICE_EDEFAULT;
		ballastFuelChoiceESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICE, oldBallastFuelChoice, BALLAST_FUEL_CHOICE_EDEFAULT, oldBallastFuelChoiceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetBallastFuelChoice() {
		return ballastFuelChoiceESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getLoadDate() {
		return loadDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLoadDate(LocalDateTime newLoadDate) {
		LocalDateTime oldLoadDate = loadDate;
		loadDate = newLoadDate;
		boolean oldLoadDateESet = loadDateESet;
		loadDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LOAD_DATE, oldLoadDate, loadDate, !oldLoadDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetLoadDate() {
		LocalDateTime oldLoadDate = loadDate;
		boolean oldLoadDateESet = loadDateESet;
		loadDate = LOAD_DATE_EDEFAULT;
		loadDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LOAD_DATE, oldLoadDate, LOAD_DATE_EDEFAULT, oldLoadDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetLoadDate() {
		return loadDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getDischargeDate() {
		return dischargeDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDischargeDate(LocalDateTime newDischargeDate) {
		LocalDateTime oldDischargeDate = dischargeDate;
		dischargeDate = newDischargeDate;
		boolean oldDischargeDateESet = dischargeDateESet;
		dischargeDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__DISCHARGE_DATE, oldDischargeDate, dischargeDate, !oldDischargeDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetDischargeDate() {
		LocalDateTime oldDischargeDate = dischargeDate;
		boolean oldDischargeDateESet = dischargeDateESet;
		dischargeDate = DISCHARGE_DATE_EDEFAULT;
		dischargeDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AnalyticsPackage.BASE_CASE_ROW_OPTIONS__DISCHARGE_DATE, oldDischargeDate, DISCHARGE_DATE_EDEFAULT, oldDischargeDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetDischargeDate() {
		return dischargeDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_ROUTE:
				return getLadenRoute();
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_ROUTE:
				return getBallastRoute();
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICE:
				return getLadenFuelChoice();
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICE:
				return getBallastFuelChoice();
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LOAD_DATE:
				return getLoadDate();
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__DISCHARGE_DATE:
				return getDischargeDate();
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
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_ROUTE:
				setLadenRoute((RouteOption)newValue);
				return;
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_ROUTE:
				setBallastRoute((RouteOption)newValue);
				return;
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICE:
				setLadenFuelChoice((FuelChoice)newValue);
				return;
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICE:
				setBallastFuelChoice((FuelChoice)newValue);
				return;
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LOAD_DATE:
				setLoadDate((LocalDateTime)newValue);
				return;
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__DISCHARGE_DATE:
				setDischargeDate((LocalDateTime)newValue);
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
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_ROUTE:
				unsetLadenRoute();
				return;
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_ROUTE:
				unsetBallastRoute();
				return;
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICE:
				unsetLadenFuelChoice();
				return;
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICE:
				unsetBallastFuelChoice();
				return;
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LOAD_DATE:
				unsetLoadDate();
				return;
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__DISCHARGE_DATE:
				unsetDischargeDate();
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
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_ROUTE:
				return isSetLadenRoute();
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_ROUTE:
				return isSetBallastRoute();
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICE:
				return isSetLadenFuelChoice();
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICE:
				return isSetBallastFuelChoice();
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__LOAD_DATE:
				return isSetLoadDate();
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS__DISCHARGE_DATE:
				return isSetDischargeDate();
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
		result.append(" (ladenRoute: ");
		if (ladenRouteESet) result.append(ladenRoute); else result.append("<unset>");
		result.append(", ballastRoute: ");
		if (ballastRouteESet) result.append(ballastRoute); else result.append("<unset>");
		result.append(", ladenFuelChoice: ");
		if (ladenFuelChoiceESet) result.append(ladenFuelChoice); else result.append("<unset>");
		result.append(", ballastFuelChoice: ");
		if (ballastFuelChoiceESet) result.append(ballastFuelChoice); else result.append("<unset>");
		result.append(", loadDate: ");
		if (loadDateESet) result.append(loadDate); else result.append("<unset>");
		result.append(", dischargeDate: ");
		if (dischargeDateESet) result.append(dischargeDate); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //BaseCaseRowOptionsImpl
