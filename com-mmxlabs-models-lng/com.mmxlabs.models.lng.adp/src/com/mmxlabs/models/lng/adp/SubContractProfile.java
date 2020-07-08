/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
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
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getContractType <em>Contract Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getDistributionModel <em>Distribution Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getSlotTemplateId <em>Slot Template Id</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getNominatedVessel <em>Nominated Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getShippingDays <em>Shipping Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getCustomAttribs <em>Custom Attribs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SubContractProfile#getWindowSizeUnits <em>Window Size Units</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile()
 * @model
 * @generated
 */
public interface SubContractProfile<T extends Slot<U>, U extends Contract> extends EObject {
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
	 * Returns the value of the '<em><b>Contract Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.commercial.ContractType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.commercial.ContractType
	 * @see #setContractType(ContractType)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile_ContractType()
	 * @model
	 * @generated
	 */
	ContractType getContractType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getContractType <em>Contract Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.commercial.ContractType
	 * @see #getContractType()
	 * @generated
	 */
	void setContractType(ContractType value);

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
	 * Returns the value of the '<em><b>Nominated Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nominated Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nominated Vessel</em>' reference.
	 * @see #setNominatedVessel(Vessel)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile_NominatedVessel()
	 * @model
	 * @generated
	 */
	Vessel getNominatedVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getNominatedVessel <em>Nominated Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nominated Vessel</em>' reference.
	 * @see #getNominatedVessel()
	 * @generated
	 */
	void setNominatedVessel(Vessel value);

	/**
	 * Returns the value of the '<em><b>Shipping Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Days</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Days</em>' attribute.
	 * @see #setShippingDays(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile_ShippingDays()
	 * @model
	 * @generated
	 */
	int getShippingDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getShippingDays <em>Shipping Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Days</em>' attribute.
	 * @see #getShippingDays()
	 * @generated
	 */
	void setShippingDays(int value);

	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.SubProfileConstraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile_Constraints()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<SubProfileConstraint> getConstraints();

	/**
	 * Returns the value of the '<em><b>Window Size</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Size</em>' attribute.
	 * @see #setWindowSize(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile_WindowSize()
	 * @model default="1" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='##,##0'"
	 * @generated
	 */
	int getWindowSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getWindowSize <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Size</em>' attribute.
	 * @see #getWindowSize()
	 * @generated
	 */
	void setWindowSize(int value);

	/**
	 * Returns the value of the '<em><b>Window Size Units</b></em>' attribute.
	 * The default value is <code>"MONTHS"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.TimePeriod}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #setWindowSizeUnits(TimePeriod)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSubContractProfile_WindowSizeUnits()
	 * @model default="MONTHS" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='##,##0'"
	 * @generated
	 */
	TimePeriod getWindowSizeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getWindowSizeUnits <em>Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #getWindowSizeUnits()
	 * @generated
	 */
	void setWindowSizeUnits(TimePeriod value);

} // SubContractProfile
