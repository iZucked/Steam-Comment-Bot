/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.mmxcore;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Other Names Object</b></em>'.
 * @since 4.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.OtherNamesObject#getOtherNames <em>Other Names</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getOtherNamesObject()
 * @model
 * @generated
 */
public interface OtherNamesObject extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Other Names</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Other Names</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Other Names</em>' attribute list.
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getOtherNamesObject_OtherNames()
	 * @model
	 * @generated
	 */
	EList<String> getOtherNames();

} // OtherNamesObject
