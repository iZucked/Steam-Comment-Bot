

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;

import com.mmxlabs.models.mmxcore.MMXObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sequence</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getEvents <em>Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getVesselClass <em>Vessel Class</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequence()
 * @model
 * @generated
 */
public interface Sequence extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Events</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.Event}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Events</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Events</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequence_Events()
	 * @model containment="true"
	 * @generated
	 */
	EList<Event> getEvents();

	/**
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel</em>' reference.
	 * @see #isSetVessel()
	 * @see #unsetVessel()
	 * @see #setVessel(Vessel)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequence_Vessel()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #isSetVessel()
	 * @see #unsetVessel()
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVessel()
	 * @see #getVessel()
	 * @see #setVessel(Vessel)
	 * @generated
	 */
	void unsetVessel();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getVessel <em>Vessel</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Vessel</em>' reference is set.
	 * @see #unsetVessel()
	 * @see #getVessel()
	 * @see #setVessel(Vessel)
	 * @generated
	 */
	boolean isSetVessel();

	/**
	 * Returns the value of the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Class</em>' reference.
	 * @see #isSetVesselClass()
	 * @see #unsetVesselClass()
	 * @see #setVesselClass(VesselClass)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequence_VesselClass()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	VesselClass getVesselClass();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getVesselClass <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Class</em>' reference.
	 * @see #isSetVesselClass()
	 * @see #unsetVesselClass()
	 * @see #getVesselClass()
	 * @generated
	 */
	void setVesselClass(VesselClass value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getVesselClass <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVesselClass()
	 * @see #getVesselClass()
	 * @see #setVesselClass(VesselClass)
	 * @generated
	 */
	void unsetVesselClass();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getVesselClass <em>Vessel Class</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Vessel Class</em>' reference is set.
	 * @see #unsetVesselClass()
	 * @see #getVesselClass()
	 * @see #setVesselClass(VesselClass)
	 * @generated
	 */
	boolean isSetVesselClass();

} // end of  Sequence

// finish type fixing
