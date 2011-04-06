/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
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
 *   <li>{@link scenario.contract.Entity#getOwnership <em>Ownership</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Ownership</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ownership</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ownership</em>' attribute.
	 * @see #setOwnership(float)
	 * @see scenario.contract.ContractPackage#getEntity_Ownership()
	 * @model default="1" required="true"
	 * @generated
	 */
	float getOwnership();

	/**
	 * Sets the value of the '{@link scenario.contract.Entity#getOwnership <em>Ownership</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ownership</em>' attribute.
	 * @see #getOwnership()
	 * @generated
	 */
	void setOwnership(float value);

} // Entity
