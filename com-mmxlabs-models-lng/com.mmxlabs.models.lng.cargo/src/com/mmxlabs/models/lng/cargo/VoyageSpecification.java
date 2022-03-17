/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.port.RouteOption;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Voyage Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VoyageSpecification#getRouteOption <em>Route Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VoyageSpecification#getFuelChoice <em>Fuel Choice</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVoyageSpecification()
 * @model
 * @generated
 */
public interface VoyageSpecification extends ScheduleSpecificationEvent {

	/**
	 * Returns the value of the '<em><b>Route Option</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Option</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #isSetRouteOption()
	 * @see #unsetRouteOption()
	 * @see #setRouteOption(RouteOption)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVoyageSpecification_RouteOption()
	 * @model unsettable="true"
	 * @generated
	 */
	RouteOption getRouteOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VoyageSpecification#getRouteOption <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route Option</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #isSetRouteOption()
	 * @see #unsetRouteOption()
	 * @see #getRouteOption()
	 * @generated
	 */
	void setRouteOption(RouteOption value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VoyageSpecification#getRouteOption <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetRouteOption()
	 * @see #getRouteOption()
	 * @see #setRouteOption(RouteOption)
	 * @generated
	 */
	void unsetRouteOption();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VoyageSpecification#getRouteOption <em>Route Option</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Route Option</em>' attribute is set.
	 * @see #unsetRouteOption()
	 * @see #getRouteOption()
	 * @see #setRouteOption(RouteOption)
	 * @generated
	 */
	boolean isSetRouteOption();

	/**
	 * Returns the value of the '<em><b>Fuel Choice</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.FuelChoice}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Choice</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.FuelChoice
	 * @see #isSetFuelChoice()
	 * @see #unsetFuelChoice()
	 * @see #setFuelChoice(FuelChoice)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVoyageSpecification_FuelChoice()
	 * @model unsettable="true"
	 * @generated
	 */
	FuelChoice getFuelChoice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VoyageSpecification#getFuelChoice <em>Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Choice</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.FuelChoice
	 * @see #isSetFuelChoice()
	 * @see #unsetFuelChoice()
	 * @see #getFuelChoice()
	 * @generated
	 */
	void setFuelChoice(FuelChoice value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VoyageSpecification#getFuelChoice <em>Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFuelChoice()
	 * @see #getFuelChoice()
	 * @see #setFuelChoice(FuelChoice)
	 * @generated
	 */
	void unsetFuelChoice();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VoyageSpecification#getFuelChoice <em>Fuel Choice</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Fuel Choice</em>' attribute is set.
	 * @see #unsetFuelChoice()
	 * @see #getFuelChoice()
	 * @see #setFuelChoice(FuelChoice)
	 * @generated
	 */
	boolean isSetFuelChoice();
} // VoyageSpecification
