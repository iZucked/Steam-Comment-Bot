/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Canal Booking Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CanalJourneyEvent#getLinkedSequence <em>Linked Sequence</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CanalJourneyEvent#getLinkedJourney <em>Linked Journey</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CanalJourneyEvent#getPanamaWaitingTimeHours <em>Panama Waiting Time Hours</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CanalJourneyEvent#getMaxAvailablePanamaWaitingTimeHours <em>Max Available Panama Waiting Time Hours</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCanalJourneyEvent()
 * @model
 * @generated
 */
public interface CanalJourneyEvent extends Event {
	/**
	 * Returns the value of the '<em><b>Linked Sequence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Linked Sequence</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Linked Sequence</em>' reference.
	 * @see #setLinkedSequence(Sequence)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCanalJourneyEvent_LinkedSequence()
	 * @model
	 * @generated
	 */
	Sequence getLinkedSequence();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CanalJourneyEvent#getLinkedSequence <em>Linked Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Linked Sequence</em>' reference.
	 * @see #getLinkedSequence()
	 * @generated
	 */
	void setLinkedSequence(Sequence value);

	/**
	 * Returns the value of the '<em><b>Linked Journey</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Linked Journey</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Linked Journey</em>' reference.
	 * @see #setLinkedJourney(Journey)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCanalJourneyEvent_LinkedJourney()
	 * @model
	 * @generated
	 */
	Journey getLinkedJourney();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CanalJourneyEvent#getLinkedJourney <em>Linked Journey</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Linked Journey</em>' reference.
	 * @see #getLinkedJourney()
	 * @generated
	 */
	void setLinkedJourney(Journey value);

	/**
	 * Returns the value of the '<em><b>Panama Waiting Time Hours</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Panama Waiting Time Hours</em>' attribute.
	 * @see #setPanamaWaitingTimeHours(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCanalJourneyEvent_PanamaWaitingTimeHours()
	 * @model
	 * @generated
	 */
	int getPanamaWaitingTimeHours();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CanalJourneyEvent#getPanamaWaitingTimeHours <em>Panama Waiting Time Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Panama Waiting Time Hours</em>' attribute.
	 * @see #getPanamaWaitingTimeHours()
	 * @generated
	 */
	void setPanamaWaitingTimeHours(int value);

	/**
	 * Returns the value of the '<em><b>Max Available Panama Waiting Time Hours</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Available Panama Waiting Time Hours</em>' attribute.
	 * @see #setMaxAvailablePanamaWaitingTimeHours(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCanalJourneyEvent_MaxAvailablePanamaWaitingTimeHours()
	 * @model
	 * @generated
	 */
	int getMaxAvailablePanamaWaitingTimeHours();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CanalJourneyEvent#getMaxAvailablePanamaWaitingTimeHours <em>Max Available Panama Waiting Time Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Available Panama Waiting Time Hours</em>' attribute.
	 * @see #getMaxAvailablePanamaWaitingTimeHours()
	 * @generated
	 */
	void setMaxAvailablePanamaWaitingTimeHours(int value);

} // CanalBookingEvent
