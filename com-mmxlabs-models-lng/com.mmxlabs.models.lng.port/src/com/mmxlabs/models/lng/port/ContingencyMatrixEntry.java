/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.port;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contingency Matrix Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getFromPort <em>From Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getToPort <em>To Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getDuration <em>Duration</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getContingencyMatrixEntry()
 * @model
 * @generated
 */
public interface ContingencyMatrixEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>From Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>From Port</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From Port</em>' reference.
	 * @see #setFromPort(Port)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getContingencyMatrixEntry_FromPort()
	 * @model
	 * @generated
	 */
	Port getFromPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getFromPort <em>From Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From Port</em>' reference.
	 * @see #getFromPort()
	 * @generated
	 */
	void setFromPort(Port value);

	/**
	 * Returns the value of the '<em><b>To Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>To Port</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To Port</em>' reference.
	 * @see #setToPort(Port)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getContingencyMatrixEntry_ToPort()
	 * @model
	 * @generated
	 */
	Port getToPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getToPort <em>To Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Port</em>' reference.
	 * @see #getToPort()
	 * @generated
	 */
	void setToPort(Port value);

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(int)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getContingencyMatrixEntry_Duration()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='#0'"
	 * @generated
	 */
	int getDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(int value);

} // ContingencyMatrixEntry
