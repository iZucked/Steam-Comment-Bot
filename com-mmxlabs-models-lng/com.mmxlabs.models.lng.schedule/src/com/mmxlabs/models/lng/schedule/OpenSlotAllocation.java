/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.cargo.Slot;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Open Slot Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.OpenSlotAllocation#getSlot <em>Slot</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getOpenSlotAllocation()
 * @model
 * @generated
 */
public interface OpenSlotAllocation extends ProfitAndLossContainer {
	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #isSetSlot()
	 * @see #unsetSlot()
	 * @see #setSlot(Slot)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getOpenSlotAllocation_Slot()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Slot getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.OpenSlotAllocation#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #isSetSlot()
	 * @see #unsetSlot()
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(Slot value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.OpenSlotAllocation#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSlot()
	 * @see #getSlot()
	 * @see #setSlot(Slot)
	 * @generated
	 */
	void unsetSlot();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.OpenSlotAllocation#getSlot <em>Slot</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Slot</em>' reference is set.
	 * @see #unsetSlot()
	 * @see #getSlot()
	 * @see #setSlot(Slot)
	 * @generated
	 */
	boolean isSetSlot();

} // OpenSlotAllocation
