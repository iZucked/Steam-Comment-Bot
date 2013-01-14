/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.mmxcore.MMXObject;

import java.util.Calendar;
import java.util.Date;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getStart <em>Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getEnd <em>End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getPreviousEvent <em>Previous Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getNextEvent <em>Next Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getSequence <em>Sequence</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent()
 * @model
 * @generated
 */
public interface Event extends MMXObject, ITimezoneProvider {
	/**
	 * Returns the value of the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start</em>' attribute.
	 * @see #setStart(Date)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent_Start()
	 * @model required="true"
	 * @generated
	 */
	Date getStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Event#getStart <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' attribute.
	 * @see #getStart()
	 * @generated
	 */
	void setStart(Date value);

	/**
	 * Returns the value of the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End</em>' attribute.
	 * @see #setEnd(Date)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent_End()
	 * @model required="true"
	 * @generated
	 */
	Date getEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Event#getEnd <em>End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End</em>' attribute.
	 * @see #getEnd()
	 * @generated
	 */
	void setEnd(Date value);

	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #setPort(Port)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent_Port()
	 * @model required="true"
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Event#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * Returns the value of the '<em><b>Previous Event</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.schedule.Event#getNextEvent <em>Next Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Previous Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Previous Event</em>' reference.
	 * @see #setPreviousEvent(Event)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent_PreviousEvent()
	 * @see com.mmxlabs.models.lng.schedule.Event#getNextEvent
	 * @model opposite="nextEvent"
	 * @generated
	 */
	Event getPreviousEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Event#getPreviousEvent <em>Previous Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Previous Event</em>' reference.
	 * @see #getPreviousEvent()
	 * @generated
	 */
	void setPreviousEvent(Event value);

	/**
	 * Returns the value of the '<em><b>Next Event</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.schedule.Event#getPreviousEvent <em>Previous Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next Event</em>' reference.
	 * @see #setNextEvent(Event)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent_NextEvent()
	 * @see com.mmxlabs.models.lng.schedule.Event#getPreviousEvent
	 * @model opposite="previousEvent"
	 * @generated
	 */
	Event getNextEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Event#getNextEvent <em>Next Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Event</em>' reference.
	 * @see #getNextEvent()
	 * @generated
	 */
	void setNextEvent(Event value);

	/**
	 * Returns the value of the '<em><b>Sequence</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.schedule.Sequence#getEvents <em>Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sequence</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sequence</em>' container reference.
	 * @see #setSequence(Sequence)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent_Sequence()
	 * @see com.mmxlabs.models.lng.schedule.Sequence#getEvents
	 * @model opposite="events" transient="false"
	 * @generated
	 */
	Sequence getSequence();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Event#getSequence <em>Sequence</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence</em>' container reference.
	 * @see #getSequence()
	 * @generated
	 */
	void setSequence(Sequence value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getDuration();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.lng.schedule.Calendar" required="true"
	 * @generated
	 */
	Calendar getLocalStart();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.lng.schedule.Calendar" required="true"
	 * @generated
	 */
	Calendar getLocalEnd();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	String type();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	String name();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getHireCost();

} // end of  Event

// finish type fixing
