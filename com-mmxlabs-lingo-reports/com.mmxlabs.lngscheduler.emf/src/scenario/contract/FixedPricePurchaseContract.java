/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fixed Price Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.contract.FixedPricePurchaseContract#getUnitPrice <em>Unit Price</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.contract.ContractPackage#getFixedPricePurchaseContract()
 * @model
 * @generated
 */
public interface FixedPricePurchaseContract extends SimplePurchaseContract {
	/**
	 * Returns the value of the '<em><b>Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit Price</em>' attribute.
	 * @see #setUnitPrice(float)
	 * @see scenario.contract.ContractPackage#getFixedPricePurchaseContract_UnitPrice()
	 * @model required="true"
	 * @generated
	 */
	float getUnitPrice();

	/**
	 * Sets the value of the '{@link scenario.contract.FixedPricePurchaseContract#getUnitPrice <em>Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit Price</em>' attribute.
	 * @see #getUnitPrice()
	 * @generated
	 */
	void setUnitPrice(float value);

} // FixedPricePurchaseContract
