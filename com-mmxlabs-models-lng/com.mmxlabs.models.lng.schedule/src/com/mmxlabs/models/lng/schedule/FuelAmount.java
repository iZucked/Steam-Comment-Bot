/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fuel Amount</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.FuelAmount#getUnit <em>Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.FuelAmount#getQuantity <em>Quantity</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFuelAmount()
 * @model
 * @generated
 */
public interface FuelAmount extends EObject {
	/**
	 * Returns the value of the '<em><b>Unit</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.schedule.FuelUnit}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.schedule.FuelUnit
	 * @see #setUnit(FuelUnit)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFuelAmount_Unit()
	 * @model required="true"
	 * @generated
	 */
	FuelUnit getUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.FuelAmount#getUnit <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.schedule.FuelUnit
	 * @see #getUnit()
	 * @generated
	 */
	void setUnit(FuelUnit value);

	/**
	 * Returns the value of the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quantity</em>' attribute.
	 * @see #setQuantity(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFuelAmount_Quantity()
	 * @model required="true"
	 * @generated
	 */
	int getQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.FuelAmount#getQuantity <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quantity</em>' attribute.
	 * @see #getQuantity()
	 * @generated
	 */
	void setQuantity(int value);

} // end of  FuelAmount

// finish type fixing
