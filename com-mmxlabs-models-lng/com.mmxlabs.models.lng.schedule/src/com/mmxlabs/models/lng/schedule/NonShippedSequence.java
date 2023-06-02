/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.mmxcore.MMXObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Non Shipped Sequence</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NonShippedSequence#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NonShippedSequence#getEvents <em>Events</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNonShippedSequence()
 * @model
 * @generated
 */
public interface NonShippedSequence extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel</em>' reference.
	 * @see #setVessel(Vessel)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNonShippedSequence_Vessel()
	 * @model
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NonShippedSequence#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

	/**
	 * Returns the value of the '<em><b>Events</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.Event}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Events</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNonShippedSequence_Events()
	 * @model containment="true"
	 * @generated
	 */
	EList<Event> getEvents();

} // NonShippedSequence
