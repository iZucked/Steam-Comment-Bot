/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract;

import scenario.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.contract.Entity#getTaxRate <em>Tax Rate</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.contract.ContractPackage#getEntity()
 * @model
 * @generated
 */
public interface Entity extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Tax Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tax Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tax Rate</em>' attribute.
	 * @see #setTaxRate(float)
	 * @see scenario.contract.ContractPackage#getEntity_TaxRate()
	 * @model required="true"
	 * @generated
	 */
	float getTaxRate();

	/**
	 * Sets the value of the '{@link scenario.contract.Entity#getTaxRate <em>Tax Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tax Rate</em>' attribute.
	 * @see #getTaxRate()
	 * @generated
	 */
	void setTaxRate(float value);

} // Entity
