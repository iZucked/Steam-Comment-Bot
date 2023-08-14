/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>CII End Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CIIEndOptions#getDesiredCIIGrade <em>Desired CII Grade</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCIIEndOptions()
 * @model
 * @generated
 */
public interface CIIEndOptions extends EObject {
	/**
	 * Returns the value of the '<em><b>Desired CII Grade</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Desired CII Grade</em>' attribute.
	 * @see #setDesiredCIIGrade(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCIIEndOptions_DesiredCIIGrade()
	 * @model
	 * @generated
	 */
	String getDesiredCIIGrade();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CIIEndOptions#getDesiredCIIGrade <em>Desired CII Grade</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Desired CII Grade</em>' attribute.
	 * @see #getDesiredCIIGrade()
	 * @generated
	 */
	void setDesiredCIIGrade(String value);

} // CIIEndOptions
