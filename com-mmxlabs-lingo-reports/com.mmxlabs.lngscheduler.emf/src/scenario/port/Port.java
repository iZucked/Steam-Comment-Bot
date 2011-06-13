/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port;

import scenario.NamedObject;

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
 *   <li>{@link scenario.port.Port#getDefaultIndex <em>Default Index</em>}</li>
 *   <li>{@link scenario.port.Port#getTimeZone <em>Time Zone</em>}</li>
 *   <li>{@link scenario.port.Port#getDefaultContract <em>Default Contract</em>}</li>
 *   <li>{@link scenario.port.Port#getRegasEfficiency <em>Regas Efficiency</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getPort()
 * @model
 * @generated
 */
public interface Port extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Default Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Index</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Index</em>' reference.
	 * @see #setDefaultIndex(Index)
	 * @see scenario.port.PortPackage#getPort_DefaultIndex()
	 * @model
	 * @generated
	 */
	Index getDefaultIndex();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getDefaultIndex <em>Default Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Index</em>' reference.
	 * @see #getDefaultIndex()
	 * @generated
	 */
	void setDefaultIndex(Index value);

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
	 * Returns the value of the '<em><b>Default Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Contract</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Contract</em>' reference.
	 * @see #setDefaultContract(Contract)
	 * @see scenario.port.PortPackage#getPort_DefaultContract()
	 * @model required="true"
	 * @generated
	 */
	Contract getDefaultContract();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getDefaultContract <em>Default Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Contract</em>' reference.
	 * @see #getDefaultContract()
	 * @generated
	 */
	void setDefaultContract(Contract value);

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

} // Port
