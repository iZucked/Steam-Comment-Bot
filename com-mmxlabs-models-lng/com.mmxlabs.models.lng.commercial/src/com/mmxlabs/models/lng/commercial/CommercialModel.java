/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;
import org.eclipse.emf.common.util.EList;
import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getEntities <em>Entities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getSalesContracts <em>Sales Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getShippingEntity <em>Shipping Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getPurchaseContracts <em>Purchase Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getContractSlotExtensions <em>Contract Slot Extensions</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCommercialModel()
 * @model
 * @generated
 */
public interface CommercialModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Entities</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.LegalEntity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entities</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entities</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCommercialModel_Entities()
	 * @model containment="true"
	 * @generated
	 */
	EList<LegalEntity> getEntities();

	/**
	 * Returns the value of the '<em><b>Sales Contracts</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.SalesContract}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sales Contracts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sales Contracts</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCommercialModel_SalesContracts()
	 * @model containment="true"
	 * @generated
	 */
	EList<SalesContract> getSalesContracts();

	/**
	 * Returns the value of the '<em><b>Shipping Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Entity</em>' reference.
	 * @see #setShippingEntity(LegalEntity)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCommercialModel_ShippingEntity()
	 * @model required="true"
	 * @generated
	 */
	LegalEntity getShippingEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.CommercialModel#getShippingEntity <em>Shipping Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Entity</em>' reference.
	 * @see #getShippingEntity()
	 * @generated
	 */
	void setShippingEntity(LegalEntity value);

	/**
	 * Returns the value of the '<em><b>Purchase Contracts</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.PurchaseContract}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Purchase Contracts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Purchase Contracts</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCommercialModel_PurchaseContracts()
	 * @model containment="true"
	 * @generated
	 */
	EList<PurchaseContract> getPurchaseContracts();

	/**
	 * Returns the value of the '<em><b>Contract Slot Extensions</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.mmxcore.UUIDObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract Slot Extensions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract Slot Extensions</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCommercialModel_ContractSlotExtensions()
	 * @model containment="true"
	 * @generated
	 */
	EList<UUIDObject> getContractSlotExtensions();

} // end of  CommercialModel

// finish type fixing
