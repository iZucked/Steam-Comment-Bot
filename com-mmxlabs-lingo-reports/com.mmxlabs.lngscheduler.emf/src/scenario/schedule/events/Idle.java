/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.events;

import scenario.fleet.VesselState;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Idle</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link scenario.schedule.events.Idle#getVesselState <em>Vessel State</em>}</li>
 * </ul>
 * </p>
 * 
 * @see scenario.schedule.events.EventsPackage#getIdle()
 * @model
 * @generated
 */
public interface Idle extends PortVisit, FuelMixture {
	/**
	 * Returns the value of the '<em><b>Vessel State</b></em>' attribute. The literals are from the enumeration {@link scenario.fleet.VesselState}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel State</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Vessel State</em>' attribute.
	 * @see scenario.fleet.VesselState
	 * @see #setVesselState(VesselState)
	 * @see scenario.schedule.events.EventsPackage#getIdle_VesselState()
	 * @model required="true"
	 * @generated
	 */
	VesselState getVesselState();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.Idle#getVesselState <em>Vessel State</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Vessel State</em>' attribute.
	 * @see scenario.fleet.VesselState
	 * @see #getVesselState()
	 * @generated
	 */
	void setVesselState(VesselState value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true" annotation="http://www.eclipse.org/emf/2002/GenModel body='return getTotalFuelCost() + getHireCost();'"
	 * @generated
	 */
	long getTotalCost();

} // Idle
