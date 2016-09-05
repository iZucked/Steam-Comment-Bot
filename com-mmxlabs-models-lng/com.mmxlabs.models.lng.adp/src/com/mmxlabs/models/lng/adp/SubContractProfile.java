/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.Slot;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sub Contract Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getDistributionModel <em>Distribution Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getSlotTemplateId <em>Slot Template Id</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getCustomAttribs <em>Custom Attribs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getSlots <em>Slots</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile()
 * @model
 * @generated
 */
public interface SubContractProfile<T extends Slot> extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Distribution Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distribution Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distribution Model</em>' containment reference.
	 * @see #setDistributionModel(DistributionModel)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile_DistributionModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	DistributionModel getDistributionModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getDistributionModel <em>Distribution Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distribution Model</em>' containment reference.
	 * @see #getDistributionModel()
	 * @generated
	 */
	void setDistributionModel(DistributionModel value);

	/**
	 * Returns the value of the '<em><b>Slot Template Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Template Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Template Id</em>' attribute.
	 * @see #setSlotTemplateId(String)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile_SlotTemplateId()
	 * @model
	 * @generated
	 */
	String getSlotTemplateId();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getSlotTemplateId <em>Slot Template Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Template Id</em>' attribute.
	 * @see #getSlotTemplateId()
	 * @generated
	 */
	void setSlotTemplateId(String value);

	/**
	 * Returns the value of the '<em><b>Custom Attribs</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Custom Attribs</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Custom Attribs</em>' containment reference.
	 * @see #setCustomAttribs(CustomSubProfileAttributes)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile_CustomAttribs()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	CustomSubProfileAttributes getCustomAttribs();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getCustomAttribs <em>Custom Attribs</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Custom Attribs</em>' containment reference.
	 * @see #getCustomAttribs()
	 * @generated
	 */
	void setCustomAttribs(CustomSubProfileAttributes value);

	/**
	 * Returns the value of the '<em><b>Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slots</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slots</em>' reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile_Slots()
	 * @model
	 * @generated
	 */
	EList<T> getSlots();

} // SubContractProfile
