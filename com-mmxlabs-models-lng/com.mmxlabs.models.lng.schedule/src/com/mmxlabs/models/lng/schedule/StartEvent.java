/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Start Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.StartEvent#getSlotAllocation <em>Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.StartEvent#getRepositioningFee <em>Repositioning Fee</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getStartEvent()
 * @model
 * @generated
 */
public interface StartEvent extends Event, FuelUsage, PortVisit, ProfitAndLossContainer, EventGrouping {
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
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getStartEvent_SlotAllocation()
	 * @model required="true"
	 * @generated
	 */
	SlotAllocation getSlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.StartEvent#getSlotAllocation <em>Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Allocation</em>' reference.
	 * @see #getSlotAllocation()
	 * @generated
	 */
	void setSlotAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repositioning Fee</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #setRepositioningFee(long)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getStartEvent_RepositioningFee()
	 * @model
	 * @generated
	 */
	long getRepositioningFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.StartEvent#getRepositioningFee <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #getRepositioningFee()
	 * @generated
	 */
	void setRepositioningFee(long value);

} // end of  StartEvent

// finish type fixing
