/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataContainer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>End Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.EndEvent#getSlotAllocation <em>Slot Allocation</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEndEvent()
 * @model
 * @generated
 */
public interface EndEvent extends Event, FuelUsage, PortVisit, ExtraDataContainer {
	/**
	 * Returns the value of the '<em><b>Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Allocation</em>' reference.
	 * @see #setSlotAllocation(SlotAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEndEvent_SlotAllocation()
	 * @model required="true"
	 * @generated
	 */
	SlotAllocation getSlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.EndEvent#getSlotAllocation <em>Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Allocation</em>' reference.
	 * @see #getSlotAllocation()
	 * @generated
	 */
	void setSlotAllocation(SlotAllocation value);

} // end of  EndEvent

// finish type fixing
