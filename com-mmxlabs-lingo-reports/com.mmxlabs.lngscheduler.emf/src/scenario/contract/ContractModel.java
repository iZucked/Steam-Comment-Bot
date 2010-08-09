/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract;

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
 *   <li>{@link scenario.contract.ContractModel#getPurchaseContracts <em>Purchase Contracts</em>}</li>
 *   <li>{@link scenario.contract.ContractModel#getSalesContracts <em>Sales Contracts</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.contract.ContractPackage#getContractModel()
 * @model
 * @generated
 */
public interface ContractModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Purchase Contracts</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.contract.PurchaseContract}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Purchase Contracts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Purchase Contracts</em>' containment reference list.
	 * @see scenario.contract.ContractPackage#getContractModel_PurchaseContracts()
	 * @model containment="true"
	 * @generated
	 */
	EList<PurchaseContract> getPurchaseContracts();

	/**
	 * Returns the value of the '<em><b>Sales Contracts</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.contract.SalesContract}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sales Contracts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sales Contracts</em>' containment reference list.
	 * @see scenario.contract.ContractPackage#getContractModel_SalesContracts()
	 * @model containment="true"
	 * @generated
	 */
	EList<SalesContract> getSalesContracts();

} // ContractModel
