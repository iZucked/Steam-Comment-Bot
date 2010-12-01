/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;

import scenario.schedule.events.ScheduledEvent;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sequence</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.Sequence#getFleetVessel <em>Fleet Vessel</em>}</li>
 *   <li>{@link scenario.schedule.Sequence#getEvents <em>Events</em>}</li>
 *   <li>{@link scenario.schedule.Sequence#getCharterVesselClass <em>Charter Vessel Class</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.SchedulePackage#getSequence()
 * @model
 * @generated
 */
public interface Sequence extends EObject {
	/**
	 * Returns the value of the '<em><b>Fleet Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fleet Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fleet Vessel</em>' reference.
	 * @see #isSetFleetVessel()
	 * @see #unsetFleetVessel()
	 * @see #setFleetVessel(Vessel)
	 * @see scenario.schedule.SchedulePackage#getSequence_FleetVessel()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Vessel getFleetVessel();

	/**
	 * Sets the value of the '{@link scenario.schedule.Sequence#getFleetVessel <em>Fleet Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fleet Vessel</em>' reference.
	 * @see #isSetFleetVessel()
	 * @see #unsetFleetVessel()
	 * @see #getFleetVessel()
	 * @generated
	 */
	void setFleetVessel(Vessel value);

	/**
	 * Unsets the value of the '{@link scenario.schedule.Sequence#getFleetVessel <em>Fleet Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFleetVessel()
	 * @see #getFleetVessel()
	 * @see #setFleetVessel(Vessel)
	 * @generated
	 */
	void unsetFleetVessel();

	/**
	 * Returns whether the value of the '{@link scenario.schedule.Sequence#getFleetVessel <em>Fleet Vessel</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Fleet Vessel</em>' reference is set.
	 * @see #unsetFleetVessel()
	 * @see #getFleetVessel()
	 * @see #setFleetVessel(Vessel)
	 * @generated
	 */
	boolean isSetFleetVessel();

	/**
	 * Returns the value of the '<em><b>Events</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.schedule.events.ScheduledEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Events</em>' containment reference list.
	 * @see scenario.schedule.SchedulePackage#getSequence_Events()
	 * @model containment="true"
	 * @generated
	 */
	EList<ScheduledEvent> getEvents();

	/**
	 * Returns the value of the '<em><b>Charter Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Vessel Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Vessel Class</em>' reference.
	 * @see #isSetCharterVesselClass()
	 * @see #unsetCharterVesselClass()
	 * @see #setCharterVesselClass(VesselClass)
	 * @see scenario.schedule.SchedulePackage#getSequence_CharterVesselClass()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	VesselClass getCharterVesselClass();

	/**
	 * Sets the value of the '{@link scenario.schedule.Sequence#getCharterVesselClass <em>Charter Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Vessel Class</em>' reference.
	 * @see #isSetCharterVesselClass()
	 * @see #unsetCharterVesselClass()
	 * @see #getCharterVesselClass()
	 * @generated
	 */
	void setCharterVesselClass(VesselClass value);

	/**
	 * Unsets the value of the '{@link scenario.schedule.Sequence#getCharterVesselClass <em>Charter Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCharterVesselClass()
	 * @see #getCharterVesselClass()
	 * @see #setCharterVesselClass(VesselClass)
	 * @generated
	 */
	void unsetCharterVesselClass();

	/**
	 * Returns whether the value of the '{@link scenario.schedule.Sequence#getCharterVesselClass <em>Charter Vessel Class</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Charter Vessel Class</em>' reference is set.
	 * @see #unsetCharterVesselClass()
	 * @see #getCharterVesselClass()
	 * @see #setCharterVesselClass(VesselClass)
	 * @generated
	 */
	boolean isSetCharterVesselClass();

} // Sequence
