/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule;

import scenario.schedule.events.VesselEventVisit;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Event Revenue</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.VesselEventRevenue#getVesselEventVisit <em>Vessel Event Visit</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.SchedulePackage#getVesselEventRevenue()
 * @model
 * @generated
 */
public interface VesselEventRevenue extends BookedRevenue {
	/**
	 * Returns the value of the '<em><b>Vessel Event Visit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Event Visit</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Event Visit</em>' reference.
	 * @see #setVesselEventVisit(VesselEventVisit)
	 * @see scenario.schedule.SchedulePackage#getVesselEventRevenue_VesselEventVisit()
	 * @model required="true"
	 * @generated
	 */
	VesselEventVisit getVesselEventVisit();

	/**
	 * Sets the value of the '{@link scenario.schedule.VesselEventRevenue#getVesselEventVisit <em>Vessel Event Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Event Visit</em>' reference.
	 * @see #getVesselEventVisit()
	 * @generated
	 */
	void setVesselEventVisit(VesselEventVisit value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return getVesselEventVisit().getVesselEvent().getId();'"
	 * @generated
	 */
	String getName();

} // VesselEventRevenue
