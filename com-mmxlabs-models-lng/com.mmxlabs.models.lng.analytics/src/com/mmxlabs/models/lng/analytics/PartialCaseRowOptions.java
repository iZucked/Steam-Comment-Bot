/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.lng.port.RouteOption;

import java.time.LocalDateTime;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Partial Case Row Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getLadenRoutes <em>Laden Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getBallastRoutes <em>Ballast Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getLadenFuelChoices <em>Laden Fuel Choices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getBallastFuelChoices <em>Ballast Fuel Choices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getLoadDates <em>Load Dates</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getDischargeDates <em>Discharge Dates</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRowOptions()
 * @model
 * @generated
 */
public interface PartialCaseRowOptions extends EObject {
	/**
	 * Returns the value of the '<em><b>Laden Routes</b></em>' attribute list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Routes</em>' attribute list.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRowOptions_LadenRoutes()
	 * @model
	 * @generated
	 */
	EList<RouteOption> getLadenRoutes();

	/**
	 * Returns the value of the '<em><b>Ballast Routes</b></em>' attribute list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Routes</em>' attribute list.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRowOptions_BallastRoutes()
	 * @model
	 * @generated
	 */
	EList<RouteOption> getBallastRoutes();

	/**
	 * Returns the value of the '<em><b>Laden Fuel Choices</b></em>' attribute list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.FuelChoice}.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.FuelChoice}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Fuel Choices</em>' attribute list.
	 * @see com.mmxlabs.models.lng.cargo.FuelChoice
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRowOptions_LadenFuelChoices()
	 * @model
	 * @generated
	 */
	EList<FuelChoice> getLadenFuelChoices();

	/**
	 * Returns the value of the '<em><b>Ballast Fuel Choices</b></em>' attribute list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.FuelChoice}.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.FuelChoice}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Fuel Choices</em>' attribute list.
	 * @see com.mmxlabs.models.lng.cargo.FuelChoice
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRowOptions_BallastFuelChoices()
	 * @model
	 * @generated
	 */
	EList<FuelChoice> getBallastFuelChoices();

	/**
	 * Returns the value of the '<em><b>Load Dates</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.LocalDateTimeHolder}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Dates</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRowOptions_LoadDates()
	 * @model containment="true"
	 * @generated
	 */
	EList<LocalDateTimeHolder> getLoadDates();

	/**
	 * Returns the value of the '<em><b>Discharge Dates</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.LocalDateTimeHolder}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Dates</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCaseRowOptions_DischargeDates()
	 * @model containment="true"
	 * @generated
	 */
	EList<LocalDateTimeHolder> getDischargeDates();

} // PartialCaseRowOptions
