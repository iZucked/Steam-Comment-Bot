/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Netback Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.contract.NetbackPurchaseContract#getLowerBound <em>Lower Bound</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.contract.ContractPackage#getNetbackPurchaseContract()
 * @model
 * @generated
 */
public interface NetbackPurchaseContract extends PurchaseContract {
	/**
	 * Returns the value of the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lower Bound</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lower Bound</em>' attribute.
	 * @see #setLowerBound(int)
	 * @see scenario.contract.ContractPackage#getNetbackPurchaseContract_LowerBound()
	 * @model required="true"
	 * @generated
	 */
	int getLowerBound();

	/**
	 * Sets the value of the '{@link scenario.contract.NetbackPurchaseContract#getLowerBound <em>Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lower Bound</em>' attribute.
	 * @see #getLowerBound()
	 * @generated
	 */
	void setLowerBound(int value);

} // NetbackPurchaseContract
