/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.port;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Costs Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.PortCostsContainer#getPortCosts <em>Port Costs</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getPortCostsContainer()
 * @model
 * @generated
 */
public interface PortCostsContainer extends EObject {
	/**
	 * Returns the value of the '<em><b>Port Costs</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.port.PortVesselClassCost}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Costs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Costs</em>' containment reference list.
	 * @see scenario.port.PortPackage#getPortCostsContainer_PortCosts()
	 * @model containment="true"
	 * @generated
	 */
	EList<PortVesselClassCost> getPortCosts();

} // PortCostsContainer
