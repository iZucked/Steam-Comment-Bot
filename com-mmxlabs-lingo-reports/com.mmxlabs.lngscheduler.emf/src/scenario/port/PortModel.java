/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.PortModel#getPorts <em>Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getPortModel()
 * @model
 * @generated
 */
public interface PortModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Ports</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.port.Port}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' containment reference list.
	 * @see scenario.port.PortPackage#getPortModel_Ports()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Port> getPorts();

} // PortModel
