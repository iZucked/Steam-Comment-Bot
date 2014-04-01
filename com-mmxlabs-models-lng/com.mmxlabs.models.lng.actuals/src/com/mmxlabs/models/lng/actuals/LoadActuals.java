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
 *   <li>{@link com.mmxlabs.models.lng.actuals.LoadActuals#getStartingHeelM3 <em>Starting Heel M3</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.LoadActuals#getStartingHeelMMBTu <em>Starting Heel MMB Tu</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Starting Heel M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Starting Heel M3</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Starting Heel M3</em>' attribute.
	 * @see #setStartingHeelM3(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getLoadActuals_StartingHeelM3()
	 * @model
	 * @generated
	 */
	int getStartingHeelM3();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.LoadActuals#getStartingHeelM3 <em>Starting Heel M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Starting Heel M3</em>' attribute.
	 * @see #getStartingHeelM3()
	 * @generated
	 */
	void setStartingHeelM3(int value);

	/**
	 * Returns the value of the '<em><b>Starting Heel MMB Tu</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Starting Heel MMB Tu</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Starting Heel MMB Tu</em>' attribute.
	 * @see #setStartingHeelMMBTu(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getLoadActuals_StartingHeelMMBTu()
	 * @model
	 * @generated
	 */
	int getStartingHeelMMBTu();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.LoadActuals#getStartingHeelMMBTu <em>Starting Heel MMB Tu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Starting Heel MMB Tu</em>' attribute.
	 * @see #getStartingHeelMMBTu()
	 * @generated
	 */
	void setStartingHeelMMBTu(int value);
} // LoadActuals
