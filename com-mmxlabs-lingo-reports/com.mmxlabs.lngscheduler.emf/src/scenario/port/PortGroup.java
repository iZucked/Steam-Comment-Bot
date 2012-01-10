/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.PortGroup#getContents <em>Contents</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getPortGroup()
 * @model
 * @generated
 */
public interface PortGroup extends PortSelection {
	/**
	 * Returns the value of the '<em><b>Contents</b></em>' reference list.
	 * The list contents are of type {@link scenario.port.PortSelection}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contents</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contents</em>' reference list.
	 * @see scenario.port.PortPackage#getPortGroup_Contents()
	 * @model
	 * @generated
	 */
	EList<PortSelection> getContents();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Recursively collect up all the ports contained in this selection
	 * <!-- end-model-doc -->
	 * @model required="true" ignoreSelectionsRequired="true" ignoreSelectionsMany="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (ignoreSelections.contains(this)) return org.eclipse.emf.common.util.ECollections.emptyEList();\n\nfinal org.eclipse.emf.common.util.UniqueEList<Port> result = new org.eclipse.emf.common.util.UniqueEList<Port>();\nignoreSelections.add(this);\n\nfor (final PortSelection selection : getContents()) {\n\tresult.addAll(selection.getClosure(ignoreSelections));\n}\n\nreturn result;'"
	 * @generated
	 */
	EList<Port> getClosure(EList<PortSelection> ignoreSelections);

} // PortGroup
