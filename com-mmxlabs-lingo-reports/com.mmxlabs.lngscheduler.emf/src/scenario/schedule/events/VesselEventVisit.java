/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.events;

import scenario.fleet.VesselEvent;
import scenario.schedule.VesselEventRevenue;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Vessel Event Visit</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link scenario.schedule.events.VesselEventVisit#getVesselEvent <em>Vessel Event</em>}</li>
 * <li>{@link scenario.schedule.events.VesselEventVisit#getRevenue <em>Revenue</em>}</li>
 * </ul>
 * </p>
 * 
 * @see scenario.schedule.events.EventsPackage#getVesselEventVisit()
 * @model
 * @generated
 */
public interface VesselEventVisit extends PortVisit {
	/**
	 * Returns the value of the '<em><b>Vessel Event</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Event</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Vessel Event</em>' reference.
	 * @see #setVesselEvent(VesselEvent)
	 * @see scenario.schedule.events.EventsPackage#getVesselEventVisit_VesselEvent()
	 * @model required="true"
	 * @generated
	 */
	VesselEvent getVesselEvent();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.VesselEventVisit#getVesselEvent <em>Vessel Event</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Vessel Event</em>' reference.
	 * @see #getVesselEvent()
	 * @generated
	 */
	void setVesselEvent(VesselEvent value);

	/**
	 * Returns the value of the '<em><b>Revenue</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Revenue</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Revenue</em>' reference.
	 * @see #setRevenue(VesselEventRevenue)
	 * @see scenario.schedule.events.EventsPackage#getVesselEventVisit_Revenue()
	 * @model required="true"
	 * @generated
	 */
	VesselEventRevenue getRevenue();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.VesselEventVisit#getRevenue <em>Revenue</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Revenue</em>' reference.
	 * @see #getRevenue()
	 * @generated
	 */
	void setRevenue(VesselEventRevenue value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true" annotation="http://www.eclipse.org/emf/2002/GenModel body='return getVesselEvent().getId();'"
	 * @generated
	 */
	@Override
	String getId();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true" annotation="http://www.eclipse.org/emf/2002/GenModel body='return getId();'"
	 * @generated
	 */
	@Override
	String getName();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true" annotation="http://www.eclipse.org/emf/2002/GenModel body='return getVesselEvent().eClass().getName();'"
	 * @generated
	 */
	@Override
	String getDisplayTypeName();

} // VesselEventVisit
