/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.mmxcore.MMXObject;
import org.eclipse.emf.common.util.EList;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.mmxcore.MMXObject;
import org.eclipse.emf.common.util.EList;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.mmxcore.MMXObject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.mmxcore.MMXObject;

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
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getFitnesses <em>Fitnesses</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getDailyHireRate <em>Daily Hire Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getSequenceType <em>Sequence Type</em>}</li>
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
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.schedule.Event#getSequence <em>Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Events</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Events</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequence_Events()
	 * @see com.mmxlabs.models.lng.schedule.Event#getSequence
	 * @model opposite="sequence" containment="true"
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

	/**
	 * Returns the value of the '<em><b>Fitnesses</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.Fitness}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fitnesses</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fitnesses</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequence_Fitnesses()
	 * @model containment="true"
	 * @generated
	 */
	EList<Fitness> getFitnesses();

	/**
	 * Returns the value of the '<em><b>Daily Hire Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Daily Hire Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Daily Hire Rate</em>' attribute.
	 * @see #setDailyHireRate(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequence_DailyHireRate()
	 * @model required="true"
	 * @generated
	 */
	int getDailyHireRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getDailyHireRate <em>Daily Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Daily Hire Rate</em>' attribute.
	 * @see #getDailyHireRate()
	 * @generated
	 */
	void setDailyHireRate(int value);

	/**
	 * Returns the value of the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Index</em>' attribute.
	 * @see #isSetSpotIndex()
	 * @see #unsetSpotIndex()
	 * @see #setSpotIndex(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequence_SpotIndex()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getSpotIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getSpotIndex <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Index</em>' attribute.
	 * @see #isSetSpotIndex()
	 * @see #unsetSpotIndex()
	 * @see #getSpotIndex()
	 * @generated
	 */
	void setSpotIndex(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getSpotIndex <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSpotIndex()
	 * @see #getSpotIndex()
	 * @see #setSpotIndex(int)
	 * @generated
	 */
	void unsetSpotIndex();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getSpotIndex <em>Spot Index</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Spot Index</em>' attribute is set.
	 * @see #unsetSpotIndex()
	 * @see #getSpotIndex()
	 * @see #setSpotIndex(int)
	 * @generated
	 */
	boolean isSetSpotIndex();

	/**
	 * Returns the value of the '<em><b>Sequence Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.schedule.SequenceType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sequence Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sequence Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.schedule.SequenceType
	 * @see #setSequenceType(SequenceType)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequence_SequenceType()
	 * @model
	 * @generated
	 */
	SequenceType getSequenceType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getSequenceType <em>Sequence Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.schedule.SequenceType
	 * @see #getSequenceType()
	 * @generated
	 */
	void setSequenceType(SequenceType value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getName();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	boolean isSpotVessel();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	boolean isFleetVessel();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	boolean isTimeCharterVessel();

} // end of  Sequence

// finish type fixing
