/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.contract.GroupEntity#getTaxRate <em>Tax Rate</em>}</li>
 *   <li>{@link scenario.contract.GroupEntity#getOwnership <em>Ownership</em>}</li>
 *   <li>{@link scenario.contract.GroupEntity#getTransferOffset <em>Transfer Offset</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.contract.ContractPackage#getGroupEntity()
 * @model
 * @generated
 */
public interface GroupEntity extends Entity {
	/**
	 * Returns the value of the '<em><b>Tax Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tax Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tax Rate</em>' attribute.
	 * @see #setTaxRate(Double)
	 * @see scenario.contract.ContractPackage#getGroupEntity_TaxRate()
	 * @model dataType="scenario.Percentage" required="true"
	 * @generated
	 */
	Double getTaxRate();

	/**
	 * Sets the value of the '{@link scenario.contract.GroupEntity#getTaxRate <em>Tax Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tax Rate</em>' attribute.
	 * @see #getTaxRate()
	 * @generated
	 */
	void setTaxRate(Double value);

	/**
	 * Returns the value of the '<em><b>Ownership</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ownership</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ownership</em>' attribute.
	 * @see #setOwnership(Double)
	 * @see scenario.contract.ContractPackage#getGroupEntity_Ownership()
	 * @model dataType="scenario.Percentage" required="true"
	 * @generated
	 */
	Double getOwnership();

	/**
	 * Sets the value of the '{@link scenario.contract.GroupEntity#getOwnership <em>Ownership</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ownership</em>' attribute.
	 * @see #getOwnership()
	 * @generated
	 */
	void setOwnership(Double value);

	/**
	 * Returns the value of the '<em><b>Transfer Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transfer Offset</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transfer Offset</em>' attribute.
	 * @see #setTransferOffset(float)
	 * @see scenario.contract.ContractPackage#getGroupEntity_TransferOffset()
	 * @model required="true"
	 * @generated
	 */
	float getTransferOffset();

	/**
	 * Sets the value of the '{@link scenario.contract.GroupEntity#getTransferOffset <em>Transfer Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transfer Offset</em>' attribute.
	 * @see #getTransferOffset()
	 * @generated
	 */
	void setTransferOffset(float value);

} // GroupEntity
