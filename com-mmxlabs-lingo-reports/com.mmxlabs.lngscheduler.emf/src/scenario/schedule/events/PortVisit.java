/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.schedule.events;

import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port Visit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.events.PortVisit#getPort <em>Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.events.EventsPackage#getPortVisit()
 * @model
 * @generated
 */
public interface PortVisit extends ScheduledEvent {
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
	 * @see scenario.schedule.events.EventsPackage#getPortVisit_Port()
	 * @model required="true"
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.PortVisit#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final java.util.Calendar calendar = java.util.Calendar.getInstance(\njava.util.TimeZone.getTimeZone(getPort().getTimeZone())\n);\ncalendar.setTime(getStartTime());\nreturn calendar;'"
	 * @generated
	 */
	@Override
	Object getLocalStartTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final java.util.Calendar calendar = java.util.Calendar.getInstance(\njava.util.TimeZone.getTimeZone(getPort().getTimeZone())\n);\ncalendar.setTime(getEndTime());\nreturn calendar;'"
	 * @generated
	 */
	@Override
	Object getLocalEndTime();

	String getId();
	String getDisplayTypeName();
} // PortVisit
