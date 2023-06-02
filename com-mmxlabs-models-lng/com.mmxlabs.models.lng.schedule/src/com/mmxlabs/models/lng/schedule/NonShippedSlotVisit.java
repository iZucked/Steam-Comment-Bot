/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.cargo.Slot;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Non Shipped Slot Visit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NonShippedSlotVisit#getSlot <em>Slot</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNonShippedSlotVisit()
 * @model
 * @generated
 */
public interface NonShippedSlotVisit extends Event {
	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #setSlot(Slot)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNonShippedSlotVisit_Slot()
	 * @model
	 * @generated
	 */
	Slot getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NonShippedSlotVisit#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(Slot value);

} // NonShippedSlotVisit
