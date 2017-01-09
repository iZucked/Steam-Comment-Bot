/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.cargo.VesselEvent;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Event Visit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.VesselEventVisit#getVesselEvent <em>Vessel Event</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getVesselEventVisit()
 * @model
 * @generated
 */
public interface VesselEventVisit extends Event, PortVisit, ProfitAndLossContainer, EventGrouping {
	/**
	 * Returns the value of the '<em><b>Vessel Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Event</em>' reference.
	 * @see #setVesselEvent(VesselEvent)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getVesselEventVisit_VesselEvent()
	 * @model required="true"
	 * @generated
	 */
	VesselEvent getVesselEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.VesselEventVisit#getVesselEvent <em>Vessel Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Event</em>' reference.
	 * @see #getVesselEvent()
	 * @generated
	 */
	void setVesselEvent(VesselEvent value);

} // end of  VesselEventVisit

// finish type fixing
