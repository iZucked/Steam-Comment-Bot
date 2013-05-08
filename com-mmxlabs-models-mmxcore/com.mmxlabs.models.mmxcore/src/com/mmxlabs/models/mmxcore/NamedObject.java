/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Named Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.NamedObject#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.NamedObject#getOtherNames <em>Other Names</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getNamedObject()
 * @model
 * @generated
 */
public interface NamedObject extends MMXObject {
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
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getNamedObject_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.NamedObject#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Other Names</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Other Names</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.2
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Other Names</em>' attribute list.
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getNamedObject_OtherNames()
	 * @model
	 * @generated
	 */
	EList<String> getOtherNames();

} // NamedObject
