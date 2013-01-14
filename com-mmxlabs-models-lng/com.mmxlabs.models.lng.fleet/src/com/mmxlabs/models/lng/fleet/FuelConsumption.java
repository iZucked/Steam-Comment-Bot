/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fuel Consumption</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FuelConsumption#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FuelConsumption#getConsumption <em>Consumption</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFuelConsumption()
 * @model
 * @generated
 */
public interface FuelConsumption extends EObject {
	/**
	 * Returns the value of the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Speed</em>' attribute.
	 * @see #setSpeed(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFuelConsumption_Speed()
	 * @model required="true"
	 * @generated
	 */
	double getSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.FuelConsumption#getSpeed <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Speed</em>' attribute.
	 * @see #getSpeed()
	 * @generated
	 */
	void setSpeed(double value);

	/**
	 * Returns the value of the '<em><b>Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Consumption</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Consumption</em>' attribute.
	 * @see #setConsumption(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFuelConsumption_Consumption()
	 * @model required="true"
	 * @generated
	 */
	int getConsumption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.FuelConsumption#getConsumption <em>Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Consumption</em>' attribute.
	 * @see #getConsumption()
	 * @generated
	 */
	void setConsumption(int value);

} // end of  FuelConsumption

// finish type fixing
