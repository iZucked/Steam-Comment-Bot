/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import java.time.LocalDateTime;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.SlotSpecification#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.SlotSpecification#getArrivalDate <em>Arrival Date</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlotSpecification()
 * @model
 * @generated
 */
public interface SlotSpecification extends ScheduleSpecificationEvent {
	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #setSlot(Slot)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlotSpecification_Slot()
	 * @model
	 * @generated
	 */
	Slot getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.SlotSpecification#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(Slot value);

	/**
	 * Returns the value of the '<em><b>Arrival Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arrival Date</em>' attribute.
	 * @see #setArrivalDate(LocalDateTime)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSlotSpecification_ArrivalDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getArrivalDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.SlotSpecification#getArrivalDate <em>Arrival Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arrival Date</em>' attribute.
	 * @see #getArrivalDate()
	 * @generated
	 */
	void setArrivalDate(LocalDateTime value);

} // SlotSpecification
