/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.events;

import scenario.fleet.VesselEvent;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Event Visit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.events.VesselEventVisit#getVesselEvent <em>Vessel Event</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.events.EventsPackage#getVesselEventVisit()
 * @model
 * @generated
 */
public interface VesselEventVisit extends PortVisit {
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
	 * @see scenario.schedule.events.EventsPackage#getVesselEventVisit_VesselEvent()
	 * @model required="true"
	 * @generated
	 */
	VesselEvent getVesselEvent();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.VesselEventVisit#getVesselEvent <em>Vessel Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Event</em>' reference.
	 * @see #getVesselEvent()
	 * @generated
	 */
	void setVesselEvent(VesselEvent value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return getVesselEvent().getId();'"
	 * @generated
	 */
	String getId();

} // VesselEventVisit
