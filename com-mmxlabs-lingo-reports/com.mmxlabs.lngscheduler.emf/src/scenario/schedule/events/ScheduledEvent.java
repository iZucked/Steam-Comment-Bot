/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.events;

import java.util.Date;

import scenario.ScenarioObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scheduled Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.events.ScheduledEvent#getStartTime <em>Start Time</em>}</li>
 *   <li>{@link scenario.schedule.events.ScheduledEvent#getEndTime <em>End Time</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.events.EventsPackage#getScheduledEvent()
 * @model
 * @generated
 */
public interface ScheduledEvent extends ScenarioObject {
	/**
	 * Returns the value of the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Time</em>' attribute.
	 * @see #setStartTime(Date)
	 * @see scenario.schedule.events.EventsPackage#getScheduledEvent_StartTime()
	 * @model required="true"
	 * @generated
	 */
	Date getStartTime();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.ScheduledEvent#getStartTime <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Time</em>' attribute.
	 * @see #getStartTime()
	 * @generated
	 */
	void setStartTime(Date value);

	/**
	 * Returns the value of the '<em><b>End Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Time</em>' attribute.
	 * @see #setEndTime(Date)
	 * @see scenario.schedule.events.EventsPackage#getScheduledEvent_EndTime()
	 * @model required="true"
	 * @generated
	 */
	Date getEndTime();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.ScheduledEvent#getEndTime <em>End Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Time</em>' attribute.
	 * @see #getEndTime()
	 * @generated
	 */
	void setEndTime(Date value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return (int) ((getEndTime().getTime() - getStartTime().getTime()) / javax.management.timer.Timer.ONE_HOUR); '"
	 * @generated
	 */
	int getEventDuration();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return (long) (((scenario.schedule.Sequence) eContainer()).getVessel().getHourlyCharterPrice() * getEventDuration());'"
	 * @generated
	 */
	long getHireCost();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	Object getLocalStartTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Object getLocalEndTime();

} // ScheduledEvent
