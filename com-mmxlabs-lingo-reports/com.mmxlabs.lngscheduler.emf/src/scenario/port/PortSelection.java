/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port;

import org.eclipse.emf.common.util.EList;

import scenario.NamedObject;
import scenario.UUIDObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Selection</b></em>'. <!-- end-user-doc -->
 *
 *
 * @see scenario.port.PortPackage#getPortSelection()
 * @model abstract="true"
 * @generated
 */
public interface PortSelection extends UUIDObject, NamedObject {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model required="true" ignoreSelectionsRequired="true" ignoreSelectionsMany="true"
	 * @generated
	 */
	EList<Port> getClosure(EList<PortSelection> ignoreSelections);

} // PortSelection
