/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.port;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Canal Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.CanalModel#getCanals <em>Canals</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getCanalModel()
 * @model
 * @generated
 */
public interface CanalModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Canals</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.port.Canal}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canals</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canals</em>' containment reference list.
	 * @see scenario.port.PortPackage#getCanalModel_Canals()
	 * @model containment="true"
	 * @generated
	 */
	EList<Canal> getCanals();

} // CanalModel
