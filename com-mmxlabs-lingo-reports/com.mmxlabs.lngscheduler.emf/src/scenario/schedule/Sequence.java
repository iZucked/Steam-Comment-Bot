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
import scenario.schedule.fleet.AllocatedVessel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sequence</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.Sequence#getEvents <em>Events</em>}</li>
 *   <li>{@link scenario.schedule.Sequence#getVessel <em>Vessel</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.SchedulePackage#getSequence()
 * @model
 * @generated
 */
public interface Sequence extends EObject {
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
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel</em>' reference.
	 * @see #setVessel(AllocatedVessel)
	 * @see scenario.schedule.SchedulePackage#getSequence_Vessel()
	 * @model required="true"
	 * @generated
	 */
	AllocatedVessel getVessel();

	/**
	 * Sets the value of the '{@link scenario.schedule.Sequence#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(AllocatedVessel value);

} // Sequence
