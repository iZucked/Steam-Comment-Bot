/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSlotAllocations <em>Slot Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getInputCargo <em>Input Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSequence <em>Sequence</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation()
 * @model
 * @generated
 */
public interface CargoAllocation extends MMXObject, ProfitAndLossContainer, EventGrouping {
	/**
	 * Returns the value of the '<em><b>Slot Allocations</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.SlotAllocation}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getCargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Allocations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Allocations</em>' reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_SlotAllocations()
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getCargoAllocation
	 * @model opposite="cargoAllocation" required="true"
	 * @generated
	 */
	EList<SlotAllocation> getSlotAllocations();

	/**
	 * Returns the value of the '<em><b>Input Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Cargo</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Cargo</em>' reference.
	 * @see #isSetInputCargo()
	 * @see #unsetInputCargo()
	 * @see #setInputCargo(Cargo)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_InputCargo()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Cargo getInputCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getInputCargo <em>Input Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input Cargo</em>' reference.
	 * @see #isSetInputCargo()
	 * @see #unsetInputCargo()
	 * @see #getInputCargo()
	 * @generated
	 */
	void setInputCargo(Cargo value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getInputCargo <em>Input Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInputCargo()
	 * @see #getInputCargo()
	 * @see #setInputCargo(Cargo)
	 * @generated
	 */
	void unsetInputCargo();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getInputCargo <em>Input Cargo</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Input Cargo</em>' reference is set.
	 * @see #unsetInputCargo()
	 * @see #getInputCargo()
	 * @see #setInputCargo(Cargo)
	 * @generated
	 */
	boolean isSetInputCargo();

	/**
	 * Returns the value of the '<em><b>Sequence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sequence</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sequence</em>' reference.
	 * @see #isSetSequence()
	 * @see #unsetSequence()
	 * @see #setSequence(Sequence)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCargoAllocation_Sequence()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Sequence getSequence();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSequence <em>Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence</em>' reference.
	 * @see #isSetSequence()
	 * @see #unsetSequence()
	 * @see #getSequence()
	 * @generated
	 */
	void setSequence(Sequence value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSequence <em>Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSequence()
	 * @see #getSequence()
	 * @see #setSequence(Sequence)
	 * @generated
	 */
	void unsetSequence();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSequence <em>Sequence</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Sequence</em>' reference is set.
	 * @see #unsetSequence()
	 * @see #getSequence()
	 * @see #setSequence(Sequence)
	 * @generated
	 */
	boolean isSetSequence();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getName();

} // end of  CargoAllocation

// finish type fixing
