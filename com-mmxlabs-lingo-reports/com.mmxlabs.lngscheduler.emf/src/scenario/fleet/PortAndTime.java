/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

import scenario.port.Port;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Port And Time</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.PortAndTime#getStartTime <em>Start Time</em>}</li>
 *   <li>{@link scenario.fleet.PortAndTime#getEndTime <em>End Time</em>}</li>
 *   <li>{@link scenario.fleet.PortAndTime#getPort <em>Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getPortAndTime()
 * @model
 * @generated
 */
public interface PortAndTime extends EObject {
	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #isSetPort()
	 * @see #unsetPort()
	 * @see #setPort(Port)
	 * @see scenario.fleet.FleetPackage#getPortAndTime_Port()
	 * @model unsettable="true"
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link scenario.fleet.PortAndTime#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #isSetPort()
	 * @see #unsetPort()
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.PortAndTime#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetPort()
	 * @see #getPort()
	 * @see #setPort(Port)
	 * @generated
	 */
	void unsetPort();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.PortAndTime#getPort <em>Port</em>}' reference is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Port</em>' reference is set.
	 * @see #unsetPort()
	 * @see #getPort()
	 * @see #setPort(Port)
	 * @generated
	 */
	boolean isSetPort();

	/**
	 * Returns the value of the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Time</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Time</em>' attribute.
	 * @see #isSetStartTime()
	 * @see #unsetStartTime()
	 * @see #setStartTime(Date)
	 * @see scenario.fleet.FleetPackage#getPortAndTime_StartTime()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getStartTime();

	/**
	 * Sets the value of the '{@link scenario.fleet.PortAndTime#getStartTime <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Time</em>' attribute.
	 * @see #isSetStartTime()
	 * @see #unsetStartTime()
	 * @see #getStartTime()
	 * @generated
	 */
	void setStartTime(Date value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.PortAndTime#getStartTime <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetStartTime()
	 * @see #getStartTime()
	 * @see #setStartTime(Date)
	 * @generated
	 */
	void unsetStartTime();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.PortAndTime#getStartTime <em>Start Time</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start Time</em>' attribute is set.
	 * @see #unsetStartTime()
	 * @see #getStartTime()
	 * @see #setStartTime(Date)
	 * @generated
	 */
	boolean isSetStartTime();

	/**
	 * Returns the value of the '<em><b>End Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Time</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Time</em>' attribute.
	 * @see #isSetEndTime()
	 * @see #unsetEndTime()
	 * @see #setEndTime(Date)
	 * @see scenario.fleet.FleetPackage#getPortAndTime_EndTime()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getEndTime();

	/**
	 * Sets the value of the '{@link scenario.fleet.PortAndTime#getEndTime <em>End Time</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Time</em>' attribute.
	 * @see #isSetEndTime()
	 * @see #unsetEndTime()
	 * @see #getEndTime()
	 * @generated
	 */
	void setEndTime(Date value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.PortAndTime#getEndTime <em>End Time</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetEndTime()
	 * @see #getEndTime()
	 * @see #setEndTime(Date)
	 * @generated
	 */
	void unsetEndTime();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.PortAndTime#getEndTime <em>End Time</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>End Time</em>' attribute is set.
	 * @see #unsetEndTime()
	 * @see #getEndTime()
	 * @see #setEndTime(Date)
	 * @generated
	 */
	boolean isSetEndTime();

} // PortAndTime
