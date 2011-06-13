/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import java.util.Date;

import scenario.ScenarioObject;

import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port Exclusion</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.PortExclusion#getPort <em>Port</em>}</li>
 *   <li>{@link scenario.fleet.PortExclusion#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link scenario.fleet.PortExclusion#getEndDate <em>End Date</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getPortExclusion()
 * @model
 * @generated
 */
public interface PortExclusion extends ScenarioObject {
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
	 * @see scenario.fleet.FleetPackage#getPortExclusion_Port()
	 * @model required="true"
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link scenario.fleet.PortExclusion#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * Returns the value of the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Date</em>' attribute.
	 * @see #isSetStartDate()
	 * @see #unsetStartDate()
	 * @see #setStartDate(Date)
	 * @see scenario.fleet.FleetPackage#getPortExclusion_StartDate()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getStartDate();

	/**
	 * Sets the value of the '{@link scenario.fleet.PortExclusion#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Date</em>' attribute.
	 * @see #isSetStartDate()
	 * @see #unsetStartDate()
	 * @see #getStartDate()
	 * @generated
	 */
	void setStartDate(Date value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.PortExclusion#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartDate()
	 * @see #getStartDate()
	 * @see #setStartDate(Date)
	 * @generated
	 */
	void unsetStartDate();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.PortExclusion#getStartDate <em>Start Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start Date</em>' attribute is set.
	 * @see #unsetStartDate()
	 * @see #getStartDate()
	 * @see #setStartDate(Date)
	 * @generated
	 */
	boolean isSetStartDate();

	/**
	 * Returns the value of the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Date</em>' attribute.
	 * @see #isSetEndDate()
	 * @see #unsetEndDate()
	 * @see #setEndDate(Date)
	 * @see scenario.fleet.FleetPackage#getPortExclusion_EndDate()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getEndDate();

	/**
	 * Sets the value of the '{@link scenario.fleet.PortExclusion#getEndDate <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Date</em>' attribute.
	 * @see #isSetEndDate()
	 * @see #unsetEndDate()
	 * @see #getEndDate()
	 * @generated
	 */
	void setEndDate(Date value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.PortExclusion#getEndDate <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndDate()
	 * @see #getEndDate()
	 * @see #setEndDate(Date)
	 * @generated
	 */
	void unsetEndDate();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.PortExclusion#getEndDate <em>End Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End Date</em>' attribute is set.
	 * @see #unsetEndDate()
	 * @see #getEndDate()
	 * @see #setEndDate(Date)
	 * @generated
	 */
	boolean isSetEndDate();

} // PortExclusion
