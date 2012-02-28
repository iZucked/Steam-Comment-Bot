/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import java.math.BigDecimal;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fuel Quantity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.FuelQuantity#getFuelName <em>Fuel Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.FuelQuantity#getCost <em>Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.FuelQuantity#getAmounts <em>Amounts</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFuelQuantity()
 * @model
 * @generated
 */
public interface FuelQuantity extends EObject {
	/**
	 * Returns the value of the '<em><b>Fuel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Name</em>' attribute.
	 * @see #setFuelName(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFuelQuantity_FuelName()
	 * @model required="true"
	 * @generated
	 */
	String getFuelName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.FuelQuantity#getFuelName <em>Fuel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Name</em>' attribute.
	 * @see #getFuelName()
	 * @generated
	 */
	void setFuelName(String value);

	/**
	 * Returns the value of the '<em><b>Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cost</em>' attribute.
	 * @see #setCost(BigDecimal)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFuelQuantity_Cost()
	 * @model required="true"
	 * @generated
	 */
	BigDecimal getCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.FuelQuantity#getCost <em>Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cost</em>' attribute.
	 * @see #getCost()
	 * @generated
	 */
	void setCost(BigDecimal value);

	/**
	 * Returns the value of the '<em><b>Amounts</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.FuelAmount}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Amounts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Amounts</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFuelQuantity_Amounts()
	 * @model containment="true"
	 * @generated
	 */
	EList<FuelAmount> getAmounts();

} // end of  FuelQuantity

// finish type fixing
