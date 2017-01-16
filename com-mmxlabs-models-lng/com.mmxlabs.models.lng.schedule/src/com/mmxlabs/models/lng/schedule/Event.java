/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import java.time.ZonedDateTime;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getStart <em>Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getEnd <em>End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getPreviousEvent <em>Previous Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getNextEvent <em>Next Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getSequence <em>Sequence</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getCharterCost <em>Charter Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getHeelAtStart <em>Heel At Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Event#getHeelAtEnd <em>Heel At End</em>}</li>
 * </ul>
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
	 * @see #setStart(ZonedDateTime)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent_Start()
	 * @model dataType="com.mmxlabs.models.datetime.DateTime" required="true"
	 * @generated
	 */
	ZonedDateTime getStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Event#getStart <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' attribute.
	 * @see #getStart()
	 * @generated
	 */
	void setStart(ZonedDateTime value);

	/**
	 * Returns the value of the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End</em>' attribute.
	 * @see #setEnd(ZonedDateTime)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent_End()
	 * @model dataType="com.mmxlabs.models.datetime.DateTime" required="true"
	 * @generated
	 */
	ZonedDateTime getEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Event#getEnd <em>End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End</em>' attribute.
	 * @see #getEnd()
	 * @generated
	 */
	void setEnd(ZonedDateTime value);

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
	 * Returns the value of the '<em><b>Charter Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Cost</em>' attribute.
	 * @see #setCharterCost(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent_CharterCost()
	 * @model
	 * @generated
	 */
	int getCharterCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Event#getCharterCost <em>Charter Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Cost</em>' attribute.
	 * @see #getCharterCost()
	 * @generated
	 */
	void setCharterCost(int value);

	/**
	 * Returns the value of the '<em><b>Heel At Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Heel At Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Heel At Start</em>' attribute.
	 * @see #setHeelAtStart(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent_HeelAtStart()
	 * @model
	 * @generated
	 */
	int getHeelAtStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Event#getHeelAtStart <em>Heel At Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heel At Start</em>' attribute.
	 * @see #getHeelAtStart()
	 * @generated
	 */
	void setHeelAtStart(int value);

	/**
	 * Returns the value of the '<em><b>Heel At End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Heel At End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Heel At End</em>' attribute.
	 * @see #setHeelAtEnd(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getEvent_HeelAtEnd()
	 * @model
	 * @generated
	 */
	int getHeelAtEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Event#getHeelAtEnd <em>Heel At End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heel At End</em>' attribute.
	 * @see #getHeelAtEnd()
	 * @generated
	 */
	void setHeelAtEnd(int value);

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

} // end of  Event

// finish type fixing
