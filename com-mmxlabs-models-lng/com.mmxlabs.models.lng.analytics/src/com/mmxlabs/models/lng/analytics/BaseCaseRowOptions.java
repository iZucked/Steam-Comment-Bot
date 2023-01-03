/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.lng.port.RouteOption;

import java.time.LocalDateTime;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Case Row Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenRoute <em>Laden Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastRoute <em>Ballast Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenFuelChoice <em>Laden Fuel Choice</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastFuelChoice <em>Ballast Fuel Choice</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLoadDate <em>Load Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getDischargeDate <em>Discharge Date</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRowOptions()
 * @model
 * @generated
 */
public interface BaseCaseRowOptions extends EObject {
	/**
	 * Returns the value of the '<em><b>Laden Route</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Route</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #isSetLadenRoute()
	 * @see #unsetLadenRoute()
	 * @see #setLadenRoute(RouteOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRowOptions_LadenRoute()
	 * @model unsettable="true"
	 * @generated
	 */
	RouteOption getLadenRoute();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenRoute <em>Laden Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Route</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #isSetLadenRoute()
	 * @see #unsetLadenRoute()
	 * @see #getLadenRoute()
	 * @generated
	 */
	void setLadenRoute(RouteOption value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenRoute <em>Laden Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetLadenRoute()
	 * @see #getLadenRoute()
	 * @see #setLadenRoute(RouteOption)
	 * @generated
	 */
	void unsetLadenRoute();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenRoute <em>Laden Route</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Laden Route</em>' attribute is set.
	 * @see #unsetLadenRoute()
	 * @see #getLadenRoute()
	 * @see #setLadenRoute(RouteOption)
	 * @generated
	 */
	boolean isSetLadenRoute();

	/**
	 * Returns the value of the '<em><b>Ballast Route</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Route</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #isSetBallastRoute()
	 * @see #unsetBallastRoute()
	 * @see #setBallastRoute(RouteOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRowOptions_BallastRoute()
	 * @model unsettable="true"
	 * @generated
	 */
	RouteOption getBallastRoute();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastRoute <em>Ballast Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Route</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #isSetBallastRoute()
	 * @see #unsetBallastRoute()
	 * @see #getBallastRoute()
	 * @generated
	 */
	void setBallastRoute(RouteOption value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastRoute <em>Ballast Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetBallastRoute()
	 * @see #getBallastRoute()
	 * @see #setBallastRoute(RouteOption)
	 * @generated
	 */
	void unsetBallastRoute();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastRoute <em>Ballast Route</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Ballast Route</em>' attribute is set.
	 * @see #unsetBallastRoute()
	 * @see #getBallastRoute()
	 * @see #setBallastRoute(RouteOption)
	 * @generated
	 */
	boolean isSetBallastRoute();

	/**
	 * Returns the value of the '<em><b>Laden Fuel Choice</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.FuelChoice}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Fuel Choice</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.FuelChoice
	 * @see #isSetLadenFuelChoice()
	 * @see #unsetLadenFuelChoice()
	 * @see #setLadenFuelChoice(FuelChoice)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRowOptions_LadenFuelChoice()
	 * @model unsettable="true"
	 * @generated
	 */
	FuelChoice getLadenFuelChoice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenFuelChoice <em>Laden Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Fuel Choice</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.FuelChoice
	 * @see #isSetLadenFuelChoice()
	 * @see #unsetLadenFuelChoice()
	 * @see #getLadenFuelChoice()
	 * @generated
	 */
	void setLadenFuelChoice(FuelChoice value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenFuelChoice <em>Laden Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetLadenFuelChoice()
	 * @see #getLadenFuelChoice()
	 * @see #setLadenFuelChoice(FuelChoice)
	 * @generated
	 */
	void unsetLadenFuelChoice();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenFuelChoice <em>Laden Fuel Choice</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Laden Fuel Choice</em>' attribute is set.
	 * @see #unsetLadenFuelChoice()
	 * @see #getLadenFuelChoice()
	 * @see #setLadenFuelChoice(FuelChoice)
	 * @generated
	 */
	boolean isSetLadenFuelChoice();

	/**
	 * Returns the value of the '<em><b>Ballast Fuel Choice</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.FuelChoice}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Fuel Choice</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.FuelChoice
	 * @see #isSetBallastFuelChoice()
	 * @see #unsetBallastFuelChoice()
	 * @see #setBallastFuelChoice(FuelChoice)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRowOptions_BallastFuelChoice()
	 * @model unsettable="true"
	 * @generated
	 */
	FuelChoice getBallastFuelChoice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastFuelChoice <em>Ballast Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Fuel Choice</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.FuelChoice
	 * @see #isSetBallastFuelChoice()
	 * @see #unsetBallastFuelChoice()
	 * @see #getBallastFuelChoice()
	 * @generated
	 */
	void setBallastFuelChoice(FuelChoice value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastFuelChoice <em>Ballast Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetBallastFuelChoice()
	 * @see #getBallastFuelChoice()
	 * @see #setBallastFuelChoice(FuelChoice)
	 * @generated
	 */
	void unsetBallastFuelChoice();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastFuelChoice <em>Ballast Fuel Choice</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Ballast Fuel Choice</em>' attribute is set.
	 * @see #unsetBallastFuelChoice()
	 * @see #getBallastFuelChoice()
	 * @see #setBallastFuelChoice(FuelChoice)
	 * @generated
	 */
	boolean isSetBallastFuelChoice();

	/**
	 * Returns the value of the '<em><b>Load Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Date</em>' attribute.
	 * @see #isSetLoadDate()
	 * @see #unsetLoadDate()
	 * @see #setLoadDate(LocalDateTime)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRowOptions_LoadDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getLoadDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLoadDate <em>Load Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Date</em>' attribute.
	 * @see #isSetLoadDate()
	 * @see #unsetLoadDate()
	 * @see #getLoadDate()
	 * @generated
	 */
	void setLoadDate(LocalDateTime value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLoadDate <em>Load Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetLoadDate()
	 * @see #getLoadDate()
	 * @see #setLoadDate(LocalDateTime)
	 * @generated
	 */
	void unsetLoadDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLoadDate <em>Load Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Load Date</em>' attribute is set.
	 * @see #unsetLoadDate()
	 * @see #getLoadDate()
	 * @see #setLoadDate(LocalDateTime)
	 * @generated
	 */
	boolean isSetLoadDate();

	/**
	 * Returns the value of the '<em><b>Discharge Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Date</em>' attribute.
	 * @see #isSetDischargeDate()
	 * @see #unsetDischargeDate()
	 * @see #setDischargeDate(LocalDateTime)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRowOptions_DischargeDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getDischargeDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getDischargeDate <em>Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Date</em>' attribute.
	 * @see #isSetDischargeDate()
	 * @see #unsetDischargeDate()
	 * @see #getDischargeDate()
	 * @generated
	 */
	void setDischargeDate(LocalDateTime value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getDischargeDate <em>Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDischargeDate()
	 * @see #getDischargeDate()
	 * @see #setDischargeDate(LocalDateTime)
	 * @generated
	 */
	void unsetDischargeDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getDischargeDate <em>Discharge Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Discharge Date</em>' attribute is set.
	 * @see #unsetDischargeDate()
	 * @see #getDischargeDate()
	 * @see #setDischargeDate(LocalDateTime)
	 * @generated
	 */
	boolean isSetDischargeDate();

} // BaseCaseRowOptions
