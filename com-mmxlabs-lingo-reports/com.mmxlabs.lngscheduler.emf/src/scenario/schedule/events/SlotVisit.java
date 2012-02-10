/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.events;

import scenario.cargo.Slot;
import scenario.schedule.CargoAllocation;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Slot Visit</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.events.SlotVisit#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link scenario.schedule.events.SlotVisit#getSlot <em>Slot</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.events.EventsPackage#getSlotVisit()
 * @model
 * @generated
 */
public interface SlotVisit extends PortVisit {
	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #setSlot(Slot)
	 * @see scenario.schedule.events.EventsPackage#getSlotVisit_Slot()
	 * @model required="true"
	 * @generated
	 */
	Slot getSlot();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.SlotVisit#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(Slot value);

	/**
	 * Returns the value of the '<em><b>Cargo Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Allocation</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Allocation</em>' reference.
	 * @see #setCargoAllocation(CargoAllocation)
	 * @see scenario.schedule.events.EventsPackage#getSlotVisit_CargoAllocation()
	 * @model required="true"
	 * @generated
	 */
	CargoAllocation getCargoAllocation();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.SlotVisit#getCargoAllocation <em>Cargo Allocation</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Allocation</em>' reference.
	 * @see #getCargoAllocation()
	 * @generated
	 */
	void setCargoAllocation(CargoAllocation value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return getSlot().getId();'"
	 * @generated
	 */
	@Override
	String getId();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (getSlot() instanceof scenario.cargo.LoadSlot) return \"Load\";\r\nelse return \"Discharge\";'"
	 * @generated
	 */
	@Override
	String getDisplayTypeName();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return getCargoAllocation().getName();'"
	 * @generated
	 */
	@Override
	String getName();

} // SlotVisit
