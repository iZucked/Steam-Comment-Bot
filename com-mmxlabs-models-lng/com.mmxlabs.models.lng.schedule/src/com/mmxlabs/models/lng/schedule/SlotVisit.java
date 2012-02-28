/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot Visit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotVisit#getSlot <em>Slot</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotVisit()
 * @model abstract="true"
 * @generated
 */
public interface SlotVisit extends Event {
	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #setSlot(SlotAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotVisit_Slot()
	 * @model required="true"
	 * @generated
	 */
	SlotAllocation getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotVisit#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(SlotAllocation value);

} // end of  SlotVisit

// finish type fixing
