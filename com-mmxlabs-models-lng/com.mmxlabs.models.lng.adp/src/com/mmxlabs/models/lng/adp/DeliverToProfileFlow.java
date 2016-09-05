/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.DischargeSlot;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deliver To Profile Flow</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.DeliverToProfileFlow#getProfile <em>Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.DeliverToProfileFlow#getSubProfile <em>Sub Profile</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDeliverToProfileFlow()
 * @model
 * @generated
 */
public interface DeliverToProfileFlow extends DeliverToFlow {
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
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDeliverToProfileFlow_Profile()
	 * @model
	 * @generated
	 */
	ContractProfile<?> getProfile();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DeliverToProfileFlow#getProfile <em>Profile</em>}' reference.
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
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDeliverToProfileFlow_SubProfile()
	 * @model
	 * @generated
	 */
	SubContractProfile<DischargeSlot> getSubProfile();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DeliverToProfileFlow#getSubProfile <em>Sub Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sub Profile</em>' reference.
	 * @see #getSubProfile()
	 * @generated
	 */
	void setSubProfile(SubContractProfile<DischargeSlot> value);

} // DeliverToProfileFlow
