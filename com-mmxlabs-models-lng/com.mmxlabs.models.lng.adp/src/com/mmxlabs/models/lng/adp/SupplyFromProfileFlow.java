/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.LoadSlot;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Supply From Profile Flow</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.SupplyFromProfileFlow#getProfile <em>Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SupplyFromProfileFlow#getSubProfile <em>Sub Profile</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSupplyFromProfileFlow()
 * @model
 * @generated
 */
public interface SupplyFromProfileFlow extends SupplyFromFlow {
	/**
	 * Returns the value of the '<em><b>Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Profile</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Profile</em>' reference.
	 * @see #setProfile(ContractProfile)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSupplyFromProfileFlow_Profile()
	 * @model
	 * @generated
	 */
	ContractProfile<?> getProfile();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SupplyFromProfileFlow#getProfile <em>Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Profile</em>' reference.
	 * @see #getProfile()
	 * @generated
	 */
	void setProfile(ContractProfile<?> value);

	/**
	 * Returns the value of the '<em><b>Sub Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Profile</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Profile</em>' reference.
	 * @see #setSubProfile(SubContractProfile)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSupplyFromProfileFlow_SubProfile()
	 * @model
	 * @generated
	 */
	SubContractProfile<LoadSlot> getSubProfile();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SupplyFromProfileFlow#getSubProfile <em>Sub Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sub Profile</em>' reference.
	 * @see #getSubProfile()
	 * @generated
	 */
	void setSubProfile(SubContractProfile<LoadSlot> value);

} // SupplyFromProfileFlow
