/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule;

import java.util.Date;

import scenario.Detail;
import scenario.ScenarioObject;
import scenario.contract.Entity;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Booked Revenue</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.BookedRevenue#getDate <em>Date</em>}</li>
 *   <li>{@link scenario.schedule.BookedRevenue#getEntity <em>Entity</em>}</li>
 *   <li>{@link scenario.schedule.BookedRevenue#getValue <em>Value</em>}</li>
 *   <li>{@link scenario.schedule.BookedRevenue#getDetails <em>Details</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.SchedulePackage#getBookedRevenue()
 * @model
 * @generated
 */
public interface BookedRevenue extends ScenarioObject {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(Entity)
	 * @see scenario.schedule.SchedulePackage#getBookedRevenue_Entity()
	 * @model required="true"
	 * @generated
	 */
	Entity getEntity();

	/**
	 * Sets the value of the '{@link scenario.schedule.BookedRevenue#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(Entity value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(int)
	 * @see scenario.schedule.SchedulePackage#getBookedRevenue_Value()
	 * @model required="true"
	 * @generated
	 */
	int getValue();

	/**
	 * Sets the value of the '{@link scenario.schedule.BookedRevenue#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(int value);

	/**
	 * Returns the value of the '<em><b>Details</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Details</em>' containment reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Details</em>' containment reference.
	 * @see #setDetails(Detail)
	 * @see scenario.schedule.SchedulePackage#getBookedRevenue_Details()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	Detail getDetails();

	/**
	 * Sets the value of the '{@link scenario.schedule.BookedRevenue#getDetails <em>Details</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Details</em>' containment reference.
	 * @see #getDetails()
	 * @generated
	 */
	void setDetails(Detail value);

	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(Date)
	 * @see scenario.schedule.SchedulePackage#getBookedRevenue_Date()
	 * @model required="true"
	 * @generated
	 */
	Date getDate();

	/**
	 * Sets the value of the '{@link scenario.schedule.BookedRevenue#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(Date value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getName();

} // BookedRevenue
