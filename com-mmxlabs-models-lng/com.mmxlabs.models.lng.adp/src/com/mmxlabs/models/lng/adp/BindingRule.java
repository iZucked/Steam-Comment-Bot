/**
 */
package com.mmxlabs.models.lng.adp;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Binding Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.BindingRule#getProfile <em>Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.BindingRule#getSubProfile <em>Sub Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.BindingRule#getFlowType <em>Flow Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.BindingRule#getShippingOption <em>Shipping Option</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getBindingRule()
 * @model
 * @generated
 */
public interface BindingRule extends EObject {
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
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getBindingRule_Profile()
	 * @model
	 * @generated
	 */
	ContractProfile<?> getProfile();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.BindingRule#getProfile <em>Profile</em>}' reference.
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
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getBindingRule_SubProfile()
	 * @model
	 * @generated
	 */
	SubContractProfile<?> getSubProfile();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.BindingRule#getSubProfile <em>Sub Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sub Profile</em>' reference.
	 * @see #getSubProfile()
	 * @generated
	 */
	void setSubProfile(SubContractProfile<?> value);

	/**
	 * Returns the value of the '<em><b>Flow Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Flow Type</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Flow Type</em>' containment reference.
	 * @see #setFlowType(FlowType)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getBindingRule_FlowType()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	FlowType getFlowType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.BindingRule#getFlowType <em>Flow Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flow Type</em>' containment reference.
	 * @see #getFlowType()
	 * @generated
	 */
	void setFlowType(FlowType value);

	/**
	 * Returns the value of the '<em><b>Shipping Option</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Option</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Option</em>' containment reference.
	 * @see #setShippingOption(ShippingOption)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getBindingRule_ShippingOption()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	ShippingOption getShippingOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.BindingRule#getShippingOption <em>Shipping Option</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Option</em>' containment reference.
	 * @see #getShippingOption()
	 * @generated
	 */
	void setShippingOption(ShippingOption value);

} // BindingRule
