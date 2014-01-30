/**
 */
package com.mmxlabs.models.lng.actuals;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Load Actuals</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.LoadActuals#getContractType <em>Contract Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getLoadActuals()
 * @model
 * @generated
 */
public interface LoadActuals extends SlotActuals {

	/**
	 * Returns the value of the '<em><b>Contract Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract Type</em>' attribute.
	 * @see #setContractType(String)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getLoadActuals_ContractType()
	 * @model
	 * @generated
	 */
	String getContractType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.LoadActuals#getContractType <em>Contract Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract Type</em>' attribute.
	 * @see #getContractType()
	 * @generated
	 */
	void setContractType(String value);
} // LoadActuals
