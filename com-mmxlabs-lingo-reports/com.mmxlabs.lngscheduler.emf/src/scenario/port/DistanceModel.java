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
 * A representation of the model object '<em><b>Distance Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.DistanceModel#getDistances <em>Distances</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getDistanceModel()
 * @model
 * @generated
 */
public interface DistanceModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Distances</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.port.DistanceLine}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distances</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distances</em>' containment reference list.
	 * @see scenario.port.PortPackage#getDistanceModel_Distances()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<DistanceLine> getDistances();

} // DistanceModel
