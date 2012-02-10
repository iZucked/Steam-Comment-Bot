/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.fleetallocation.AllocatedVessel;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Sequence</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.Sequence#getFitness <em>Fitness</em>}</li>
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
	 * If the meaning of the '<em>Events</em>' containment reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Events</em>' containment reference list.
	 * @see scenario.schedule.SchedulePackage#getSequence_Events()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<ScheduledEvent> getEvents();

	/**
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel</em>' reference isn't clear, there really should be more of a description here...
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(AllocatedVessel value);

	/**
	 * Returns the value of the '<em><b>Fitness</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.schedule.ScheduleFitness}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fitness</em>' containment reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fitness</em>' containment reference list.
	 * @see scenario.schedule.SchedulePackage#getSequence_Fitness()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<ScheduleFitness> getFitness();

} // Sequence
