/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port And Time</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.PortAndTime#getPort <em>Port</em>}</li>
 *   <li>{@link scenario.fleet.PortAndTime#getTime <em>Time</em>}</li>
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
	 * If the meaning of the '<em>Port</em>' reference isn't clear,
	 * there really should be more of a description here...
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #isSetPort()
	 * @see #unsetPort()
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.PortAndTime#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPort()
	 * @see #getPort()
	 * @see #setPort(Port)
	 * @generated
	 */
	void unsetPort();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.PortAndTime#getPort <em>Port</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Port</em>' reference is set.
	 * @see #unsetPort()
	 * @see #getPort()
	 * @see #setPort(Port)
	 * @generated
	 */
	boolean isSetPort();

	/**
	 * Returns the value of the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time</em>' attribute.
	 * @see #isSetTime()
	 * @see #unsetTime()
	 * @see #setTime(Date)
	 * @see scenario.fleet.FleetPackage#getPortAndTime_Time()
	 * @model unsettable="true"
	 * @generated
	 */
	Date getTime();

	/**
	 * Sets the value of the '{@link scenario.fleet.PortAndTime#getTime <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time</em>' attribute.
	 * @see #isSetTime()
	 * @see #unsetTime()
	 * @see #getTime()
	 * @generated
	 */
	void setTime(Date value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.PortAndTime#getTime <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTime()
	 * @see #getTime()
	 * @see #setTime(Date)
	 * @generated
	 */
	void unsetTime();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.PortAndTime#getTime <em>Time</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Time</em>' attribute is set.
	 * @see #unsetTime()
	 * @see #getTime()
	 * @see #setTime(Date)
	 * @generated
	 */
	boolean isSetTime();

} // PortAndTime
