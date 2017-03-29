/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;

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
 *   <li>{@link com.mmxlabs.models.lng.schedule.VesselEventVisit#getRedeliveryPort <em>Redelivery Port</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Redelivery Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Redelivery Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Redelivery Port</em>' reference.
	 * @see #isSetRedeliveryPort()
	 * @see #unsetRedeliveryPort()
	 * @see #setRedeliveryPort(Port)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getVesselEventVisit_RedeliveryPort()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Port getRedeliveryPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.VesselEventVisit#getRedeliveryPort <em>Redelivery Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Redelivery Port</em>' reference.
	 * @see #isSetRedeliveryPort()
	 * @see #unsetRedeliveryPort()
	 * @see #getRedeliveryPort()
	 * @generated
	 */
	void setRedeliveryPort(Port value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.VesselEventVisit#getRedeliveryPort <em>Redelivery Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetRedeliveryPort()
	 * @see #getRedeliveryPort()
	 * @see #setRedeliveryPort(Port)
	 * @generated
	 */
	void unsetRedeliveryPort();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.VesselEventVisit#getRedeliveryPort <em>Redelivery Port</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Redelivery Port</em>' reference is set.
	 * @see #unsetRedeliveryPort()
	 * @see #getRedeliveryPort()
	 * @see #setRedeliveryPort(Port)
	 * @generated
	 */
	boolean isSetRedeliveryPort();

} // end of  VesselEventVisit

// finish type fixing
