/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract;

import org.eclipse.emf.common.util.EList;
import scenario.NamedObject;
import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.contract.Contract#getEntity <em>Entity</em>}</li>
 *   <li>{@link scenario.contract.Contract#getDefaultPorts <em>Default Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.contract.ContractPackage#getContract()
 * @model abstract="true"
 * @generated
 */
public interface Contract extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(Entity)
	 * @see scenario.contract.ContractPackage#getContract_Entity()
	 * @model required="true"
	 * @generated
	 */
	Entity getEntity();

	/**
	 * Sets the value of the '{@link scenario.contract.Contract#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(Entity value);

	/**
	 * Returns the value of the '<em><b>Default Ports</b></em>' reference list.
	 * The list contents are of type {@link scenario.port.Port}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Ports</em>' reference list.
	 * @see scenario.contract.ContractPackage#getContract_DefaultPorts()
	 * @model ordered="false"
	 * @generated
	 */
	EList<Port> getDefaultPorts();

} // Contract
