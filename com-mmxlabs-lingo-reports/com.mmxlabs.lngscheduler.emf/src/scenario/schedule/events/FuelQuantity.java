/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.events;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Fuel Quantity</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.events.FuelQuantity#getFuelType <em>Fuel Type</em>}</li>
 *   <li>{@link scenario.schedule.events.FuelQuantity#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link scenario.schedule.events.FuelQuantity#getUnitPrice <em>Unit Price</em>}</li>
 *   <li>{@link scenario.schedule.events.FuelQuantity#getTotalPrice <em>Total Price</em>}</li>
 *   <li>{@link scenario.schedule.events.FuelQuantity#getFuelUnit <em>Fuel Unit</em>}</li>
 *   <li>{@link scenario.schedule.events.FuelQuantity#getPurpose <em>Purpose</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.events.EventsPackage#getFuelQuantity()
 * @model
 * @generated
 */
public interface FuelQuantity extends EObject {
	/**
	 * Returns the value of the '<em><b>Fuel Type</b></em>' attribute.
	 * The literals are from the enumeration {@link scenario.schedule.events.FuelType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel Type</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Type</em>' attribute.
	 * @see scenario.schedule.events.FuelType
	 * @see #setFuelType(FuelType)
	 * @see scenario.schedule.events.EventsPackage#getFuelQuantity_FuelType()
	 * @model required="true"
	 * @generated
	 */
	FuelType getFuelType();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.FuelQuantity#getFuelType <em>Fuel Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Type</em>' attribute.
	 * @see scenario.schedule.events.FuelType
	 * @see #getFuelType()
	 * @generated
	 */
	void setFuelType(FuelType value);

	/**
	 * Returns the value of the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quantity</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quantity</em>' attribute.
	 * @see #setQuantity(long)
	 * @see scenario.schedule.events.EventsPackage#getFuelQuantity_Quantity()
	 * @model required="true"
	 * @generated
	 */
	long getQuantity();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.FuelQuantity#getQuantity <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quantity</em>' attribute.
	 * @see #getQuantity()
	 * @generated
	 */
	void setQuantity(long value);

	/**
	 * Returns the value of the '<em><b>Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit Price</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit Price</em>' attribute.
	 * @see #setUnitPrice(long)
	 * @see scenario.schedule.events.EventsPackage#getFuelQuantity_UnitPrice()
	 * @model required="true"
	 * @generated
	 */
	long getUnitPrice();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.FuelQuantity#getUnitPrice <em>Unit Price</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit Price</em>' attribute.
	 * @see #getUnitPrice()
	 * @generated
	 */
	void setUnitPrice(long value);

	/**
	 * Returns the value of the '<em><b>Total Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Total Price</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Total Price</em>' attribute.
	 * @see #setTotalPrice(long)
	 * @see scenario.schedule.events.EventsPackage#getFuelQuantity_TotalPrice()
	 * @model required="true"
	 * @generated
	 */
	long getTotalPrice();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.FuelQuantity#getTotalPrice <em>Total Price</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Total Price</em>' attribute.
	 * @see #getTotalPrice()
	 * @generated
	 */
	void setTotalPrice(long value);

	/**
	 * Returns the value of the '<em><b>Fuel Unit</b></em>' attribute.
	 * The literals are from the enumeration {@link scenario.schedule.events.FuelUnit}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel Unit</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Unit</em>' attribute.
	 * @see scenario.schedule.events.FuelUnit
	 * @see #setFuelUnit(FuelUnit)
	 * @see scenario.schedule.events.EventsPackage#getFuelQuantity_FuelUnit()
	 * @model required="true"
	 * @generated
	 */
	FuelUnit getFuelUnit();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.FuelQuantity#getFuelUnit <em>Fuel Unit</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Unit</em>' attribute.
	 * @see scenario.schedule.events.FuelUnit
	 * @see #getFuelUnit()
	 * @generated
	 */
	void setFuelUnit(FuelUnit value);

	/**
	 * Returns the value of the '<em><b>Purpose</b></em>' attribute.
	 * The literals are from the enumeration {@link scenario.schedule.events.FuelPurpose}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Purpose</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Purpose</em>' attribute.
	 * @see scenario.schedule.events.FuelPurpose
	 * @see #setPurpose(FuelPurpose)
	 * @see scenario.schedule.events.EventsPackage#getFuelQuantity_Purpose()
	 * @model required="true"
	 * @generated
	 */
	FuelPurpose getPurpose();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.FuelQuantity#getPurpose <em>Purpose</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Purpose</em>' attribute.
	 * @see scenario.schedule.events.FuelPurpose
	 * @see #getPurpose()
	 * @generated
	 */
	void setPurpose(FuelPurpose value);

} // FuelQuantity
