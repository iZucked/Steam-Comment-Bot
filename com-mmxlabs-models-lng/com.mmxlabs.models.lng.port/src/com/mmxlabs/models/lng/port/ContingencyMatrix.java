/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.port;

import com.mmxlabs.models.lng.types.TimePeriod;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contingency Matrix</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.ContingencyMatrix#getEntries <em>Entries</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.ContingencyMatrix#getDefaultDuration <em>Default Duration</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getContingencyMatrix()
 * @model
 * @generated
 */
public interface ContingencyMatrix extends EObject {
	/**
	 * Returns the value of the '<em><b>Entries</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.ContingencyMatrixEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entries</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getContingencyMatrix_Entries()
	 * @model containment="true"
	 * @generated
	 */
	EList<ContingencyMatrixEntry> getEntries();

	/**
	 * Returns the value of the '<em><b>Default Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Duration</em>' attribute.
	 * @see #setDefaultDuration(int)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getContingencyMatrix_DefaultDuration()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='#0'"
	 * @generated
	 */
	int getDefaultDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.ContingencyMatrix#getDefaultDuration <em>Default Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Duration</em>' attribute.
	 * @see #getDefaultDuration()
	 * @generated
	 */
	void setDefaultDuration(int value);

} // ContingencyMatrix
