/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.port;

import org.eclipse.emf.ecore.EObject;

import scenario.contract.Contract;
import scenario.market.Market;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.Port#getName <em>Name</em>}</li>
 *   <li>{@link scenario.port.Port#getDefaultMarket <em>Default Market</em>}</li>
 *   <li>{@link scenario.port.Port#getTimeZone <em>Time Zone</em>}</li>
 *   <li>{@link scenario.port.Port#getDefaultContract <em>Default Contract</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getPort()
 * @model
 * @generated
 */
public interface Port extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see scenario.port.PortPackage#getPort_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Default Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Market</em>' reference.
	 * @see #setDefaultMarket(Market)
	 * @see scenario.port.PortPackage#getPort_DefaultMarket()
	 * @model
	 * @generated
	 */
	Market getDefaultMarket();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getDefaultMarket <em>Default Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Market</em>' reference.
	 * @see #getDefaultMarket()
	 * @generated
	 */
	void setDefaultMarket(Market value);

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

} // Port
