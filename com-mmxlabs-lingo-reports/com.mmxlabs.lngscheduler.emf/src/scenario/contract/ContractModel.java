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
 *   <li>{@link scenario.contract.ContractModel#getVolumeConstraints <em>Volume Constraints</em>}</li>
 *   <li>{@link scenario.contract.ContractModel#getEntities <em>Entities</em>}</li>
 *   <li>{@link scenario.contract.ContractModel#getShippingEntity <em>Shipping Entity</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Volume Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.contract.TotalVolumeLimit}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Constraints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Constraints</em>' containment reference list.
	 * @see scenario.contract.ContractPackage#getContractModel_VolumeConstraints()
	 * @model containment="true"
	 * @generated
	 */
	EList<TotalVolumeLimit> getVolumeConstraints();

	/**
	 * Returns the value of the '<em><b>Entities</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.contract.Entity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entities</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entities</em>' containment reference list.
	 * @see scenario.contract.ContractPackage#getContractModel_Entities()
	 * @model containment="true"
	 * @generated
	 */
	EList<Entity> getEntities();

	/**
	 * Returns the value of the '<em><b>Shipping Entity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Entity</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Entity</em>' containment reference.
	 * @see #setShippingEntity(Entity)
	 * @see scenario.contract.ContractPackage#getContractModel_ShippingEntity()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Entity getShippingEntity();

	/**
	 * Sets the value of the '{@link scenario.contract.ContractModel#getShippingEntity <em>Shipping Entity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Entity</em>' containment reference.
	 * @see #getShippingEntity()
	 * @generated
	 */
	void setShippingEntity(Entity value);

} // ContractModel
