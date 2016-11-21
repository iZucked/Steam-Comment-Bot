/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.types.APortSet;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.PortGroup#getContents <em>Contents</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getPortGroup()
 * @model annotation="http://www.mmxlabs.com/models/csv namePrefix='Group'"
 * @generated
 */
public interface PortGroup extends APortSet<Port> {

	/**
	 * Returns the value of the '<em><b>Contents</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contents</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contents</em>' reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortGroup_Contents()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getContents();
} // PortGroup
