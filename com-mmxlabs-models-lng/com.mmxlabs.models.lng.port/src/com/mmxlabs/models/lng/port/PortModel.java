/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getPortGroups <em>Port Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getRoutes <em>Routes</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel()
 * @model
 * @generated
 */
public interface PortModel extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Ports</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.Port}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_Ports()
	 * @model containment="true"
	 * @generated
	 */
	EList<Port> getPorts();

	/**
	 * Returns the value of the '<em><b>Port Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.PortGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Groups</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_PortGroups()
	 * @model containment="true"
	 * @generated
	 */
	EList<PortGroup> getPortGroups();

	/**
	 * Returns the value of the '<em><b>Routes</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.Route}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Routes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Routes</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_Routes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Route> getRoutes();

} // PortModel
