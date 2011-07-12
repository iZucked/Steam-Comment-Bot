/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port;

import scenario.NamedObject;

import scenario.UUIDObject;
import scenario.contract.Contract;

import scenario.market.Index;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.Port#getTimeZone <em>Time Zone</em>}</li>
 *   <li>{@link scenario.port.Port#getRegasEfficiency <em>Regas Efficiency</em>}</li>
 *   <li>{@link scenario.port.Port#getDefaultCVValue <em>Default CV Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getPort()
 * @model
 * @generated
 */
public interface Port extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Time Zone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Zone</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Zone</em>' attribute.
	 * @see #setTimeZone(String)
	 * @see scenario.port.PortPackage#getPort_TimeZone()
	 * @model
	 * @generated
	 */
	String getTimeZone();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getTimeZone <em>Time Zone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Zone</em>' attribute.
	 * @see #getTimeZone()
	 * @generated
	 */
	void setTimeZone(String value);

	/**
	 * Returns the value of the '<em><b>Regas Efficiency</b></em>' attribute.
	 * The default value is <code>"1.0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Regas Efficiency</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Regas Efficiency</em>' attribute.
	 * @see #setRegasEfficiency(float)
	 * @see scenario.port.PortPackage#getPort_RegasEfficiency()
	 * @model default="1.0" required="true"
	 * @generated
	 */
	float getRegasEfficiency();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getRegasEfficiency <em>Regas Efficiency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Regas Efficiency</em>' attribute.
	 * @see #getRegasEfficiency()
	 * @generated
	 */
	void setRegasEfficiency(float value);

	/**
	 * Returns the value of the '<em><b>Default CV Value</b></em>' attribute.
	 * The default value is <code>"22.8"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default CV Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default CV Value</em>' attribute.
	 * @see #setDefaultCVValue(float)
	 * @see scenario.port.PortPackage#getPort_DefaultCVValue()
	 * @model default="22.8" required="true"
	 * @generated
	 */
	float getDefaultCVValue();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getDefaultCVValue <em>Default CV Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default CV Value</em>' attribute.
	 * @see #getDefaultCVValue()
	 * @generated
	 */
	void setDefaultCVValue(float value);

} // Port
