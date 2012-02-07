/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Cargo Revenue</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link scenario.schedule.CargoRevenue#getCargo <em>Cargo</em>}</li>
 * </ul>
 * </p>
 * 
 * @see scenario.schedule.SchedulePackage#getCargoRevenue()
 * @model
 * @generated
 */
public interface CargoRevenue extends BookedRevenue {
	/**
	 * Returns the value of the '<em><b>Cargo</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Cargo</em>' reference.
	 * @see #setCargo(CargoAllocation)
	 * @see scenario.schedule.SchedulePackage#getCargoRevenue_Cargo()
	 * @model required="true"
	 * @generated
	 */
	CargoAllocation getCargo();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoRevenue#getCargo <em>Cargo</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Cargo</em>' reference.
	 * @see #getCargo()
	 * @generated
	 */
	void setCargo(CargoAllocation value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true" annotation="http://www.eclipse.org/emf/2002/GenModel body='return getCargo().getName();'"
	 * @generated
	 */
	@Override
	String getName();

} // CargoRevenue
