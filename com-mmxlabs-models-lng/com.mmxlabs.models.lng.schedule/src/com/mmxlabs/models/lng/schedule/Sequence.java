/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sequence</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getEvents <em>Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getVesselAvailability <em>Vessel Availability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getCharterInMarket <em>Charter In Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getFitnesses <em>Fitnesses</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Sequence#getSequenceType <em>Sequence Type</em>}</li>
 * </ul>
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
	 * Returns the value of the '<em><b>Vessel Availability</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Availability</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Availability</em>' reference.
	 * @see #isSetVesselAvailability()
	 * @see #unsetVesselAvailability()
	 * @see #setVesselAvailability(VesselAvailability)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequence_VesselAvailability()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	VesselAvailability getVesselAvailability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getVesselAvailability <em>Vessel Availability</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Availability</em>' reference.
	 * @see #isSetVesselAvailability()
	 * @see #unsetVesselAvailability()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	void setVesselAvailability(VesselAvailability value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getVesselAvailability <em>Vessel Availability</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVesselAvailability()
	 * @see #getVesselAvailability()
	 * @see #setVesselAvailability(VesselAvailability)
	 * @generated
	 */
	void unsetVesselAvailability();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getVesselAvailability <em>Vessel Availability</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Vessel Availability</em>' reference is set.
	 * @see #unsetVesselAvailability()
	 * @see #getVesselAvailability()
	 * @see #setVesselAvailability(VesselAvailability)
	 * @generated
	 */
	boolean isSetVesselAvailability();

	/**
	 * Returns the value of the '<em><b>Charter In Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter In Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter In Market</em>' reference.
	 * @see #isSetCharterInMarket()
	 * @see #unsetCharterInMarket()
	 * @see #setCharterInMarket(CharterInMarket)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequence_CharterInMarket()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	CharterInMarket getCharterInMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getCharterInMarket <em>Charter In Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter In Market</em>' reference.
	 * @see #isSetCharterInMarket()
	 * @see #unsetCharterInMarket()
	 * @see #getCharterInMarket()
	 * @generated
	 */
	void setCharterInMarket(CharterInMarket value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getCharterInMarket <em>Charter In Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCharterInMarket()
	 * @see #getCharterInMarket()
	 * @see #setCharterInMarket(CharterInMarket)
	 * @generated
	 */
	void unsetCharterInMarket();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.Sequence#getCharterInMarket <em>Charter In Market</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Charter In Market</em>' reference is set.
	 * @see #unsetCharterInMarket()
	 * @see #getCharterInMarket()
	 * @see #setCharterInMarket(CharterInMarket)
	 * @generated
	 */
	boolean isSetCharterInMarket();

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
