/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Fuel Consumption Line</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Represents a point on the fuel consumption curve, so a containing VesselStateAttributes will consume the associated amount of fuel at the given speed.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.FuelConsumptionLine#getSpeed <em>Speed</em>}</li>
 *   <li>{@link scenario.fleet.FuelConsumptionLine#getConsumption <em>Consumption</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getFuelConsumptionLine()
 * @model
 * @generated
 */
public interface FuelConsumptionLine extends EObject {
	/**
	 * Returns the value of the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Speed</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Speed</em>' attribute.
	 * @see #setSpeed(float)
	 * @see scenario.fleet.FleetPackage#getFuelConsumptionLine_Speed()
	 * @model
	 * @generated
	 */
	float getSpeed();

	/**
	 * Sets the value of the '{@link scenario.fleet.FuelConsumptionLine#getSpeed <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Speed</em>' attribute.
	 * @see #getSpeed()
	 * @generated
	 */
	void setSpeed(float value);

	/**
	 * Returns the value of the '<em><b>Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Consumption</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Consumption</em>' attribute.
	 * @see #setConsumption(float)
	 * @see scenario.fleet.FleetPackage#getFuelConsumptionLine_Consumption()
	 * @model
	 * @generated
	 */
	float getConsumption();

	/**
	 * Sets the value of the '{@link scenario.fleet.FuelConsumptionLine#getConsumption <em>Consumption</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Consumption</em>' attribute.
	 * @see #getConsumption()
	 * @generated
	 */
	void setConsumption(float value);

} // FuelConsumptionLine
