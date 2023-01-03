/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.LocalDateTimeHolder;
import com.mmxlabs.models.lng.analytics.PartialCaseRowOptions;

import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.lng.port.RouteOption;

import java.time.LocalDateTime;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Partial Case Row Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowOptionsImpl#getLadenRoutes <em>Laden Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowOptionsImpl#getBallastRoutes <em>Ballast Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowOptionsImpl#getLadenFuelChoices <em>Laden Fuel Choices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowOptionsImpl#getBallastFuelChoices <em>Ballast Fuel Choices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowOptionsImpl#getLoadDates <em>Load Dates</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowOptionsImpl#getDischargeDates <em>Discharge Dates</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PartialCaseRowOptionsImpl extends EObjectImpl implements PartialCaseRowOptions {
	/**
	 * The cached value of the '{@link #getLadenRoutes() <em>Laden Routes</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenRoutes()
	 * @generated
	 * @ordered
	 */
	protected EList<RouteOption> ladenRoutes;

	/**
	 * The cached value of the '{@link #getBallastRoutes() <em>Ballast Routes</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastRoutes()
	 * @generated
	 * @ordered
	 */
	protected EList<RouteOption> ballastRoutes;

	/**
	 * The cached value of the '{@link #getLadenFuelChoices() <em>Laden Fuel Choices</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenFuelChoices()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelChoice> ladenFuelChoices;

	/**
	 * The cached value of the '{@link #getBallastFuelChoices() <em>Ballast Fuel Choices</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastFuelChoices()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelChoice> ballastFuelChoices;

	/**
	 * The cached value of the '{@link #getLoadDates() <em>Load Dates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadDates()
	 * @generated
	 * @ordered
	 */
	protected EList<LocalDateTimeHolder> loadDates;

	/**
	 * The cached value of the '{@link #getDischargeDates() <em>Discharge Dates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeDates()
	 * @generated
	 * @ordered
	 */
	protected EList<LocalDateTimeHolder> dischargeDates;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PartialCaseRowOptionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<RouteOption> getLadenRoutes() {
		if (ladenRoutes == null) {
			ladenRoutes = new EDataTypeUniqueEList<RouteOption>(RouteOption.class, this, AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES);
		}
		return ladenRoutes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<RouteOption> getBallastRoutes() {
		if (ballastRoutes == null) {
			ballastRoutes = new EDataTypeUniqueEList<RouteOption>(RouteOption.class, this, AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__BALLAST_ROUTES);
		}
		return ballastRoutes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<FuelChoice> getLadenFuelChoices() {
		if (ladenFuelChoices == null) {
			ladenFuelChoices = new EDataTypeUniqueEList<FuelChoice>(FuelChoice.class, this, AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES);
		}
		return ladenFuelChoices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<FuelChoice> getBallastFuelChoices() {
		if (ballastFuelChoices == null) {
			ballastFuelChoices = new EDataTypeUniqueEList<FuelChoice>(FuelChoice.class, this, AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICES);
		}
		return ballastFuelChoices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<LocalDateTimeHolder> getLoadDates() {
		if (loadDates == null) {
			loadDates = new EObjectContainmentEList<LocalDateTimeHolder>(LocalDateTimeHolder.class, this, AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES);
		}
		return loadDates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<LocalDateTimeHolder> getDischargeDates() {
		if (dischargeDates == null) {
			dischargeDates = new EObjectContainmentEList<LocalDateTimeHolder>(LocalDateTimeHolder.class, this, AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES);
		}
		return dischargeDates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES:
				return ((InternalEList<?>)getLoadDates()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES:
				return ((InternalEList<?>)getDischargeDates()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES:
				return getLadenRoutes();
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__BALLAST_ROUTES:
				return getBallastRoutes();
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES:
				return getLadenFuelChoices();
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICES:
				return getBallastFuelChoices();
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES:
				return getLoadDates();
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES:
				return getDischargeDates();
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
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES:
				getLadenRoutes().clear();
				getLadenRoutes().addAll((Collection<? extends RouteOption>)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__BALLAST_ROUTES:
				getBallastRoutes().clear();
				getBallastRoutes().addAll((Collection<? extends RouteOption>)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES:
				getLadenFuelChoices().clear();
				getLadenFuelChoices().addAll((Collection<? extends FuelChoice>)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICES:
				getBallastFuelChoices().clear();
				getBallastFuelChoices().addAll((Collection<? extends FuelChoice>)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES:
				getLoadDates().clear();
				getLoadDates().addAll((Collection<? extends LocalDateTimeHolder>)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES:
				getDischargeDates().clear();
				getDischargeDates().addAll((Collection<? extends LocalDateTimeHolder>)newValue);
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
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES:
				getLadenRoutes().clear();
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__BALLAST_ROUTES:
				getBallastRoutes().clear();
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES:
				getLadenFuelChoices().clear();
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICES:
				getBallastFuelChoices().clear();
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES:
				getLoadDates().clear();
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES:
				getDischargeDates().clear();
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
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES:
				return ladenRoutes != null && !ladenRoutes.isEmpty();
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__BALLAST_ROUTES:
				return ballastRoutes != null && !ballastRoutes.isEmpty();
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES:
				return ladenFuelChoices != null && !ladenFuelChoices.isEmpty();
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICES:
				return ballastFuelChoices != null && !ballastFuelChoices.isEmpty();
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES:
				return loadDates != null && !loadDates.isEmpty();
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES:
				return dischargeDates != null && !dischargeDates.isEmpty();
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
		result.append(" (ladenRoutes: ");
		result.append(ladenRoutes);
		result.append(", ballastRoutes: ");
		result.append(ballastRoutes);
		result.append(", ladenFuelChoices: ");
		result.append(ladenFuelChoices);
		result.append(", ballastFuelChoices: ");
		result.append(ballastFuelChoices);
		result.append(')');
		return result.toString();
	}

} //PartialCaseRowOptionsImpl
