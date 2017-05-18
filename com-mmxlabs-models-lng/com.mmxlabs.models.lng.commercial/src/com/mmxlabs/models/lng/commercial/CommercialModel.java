/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getEntities <em>Entities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getSalesContracts <em>Sales Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getPurchaseContracts <em>Purchase Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getCharteringContracts <em>Chartering Contracts</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCommercialModel()
 * @model
 * @generated
 */
public interface CommercialModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Entities</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.BaseLegalEntity}.
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
	EList<BaseLegalEntity> getEntities();

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
	 * Returns the value of the '<em><b>Chartering Contracts</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.CharterContract}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Chartering Contracts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Chartering Contracts</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCommercialModel_CharteringContracts()
	 * @model containment="true"
	 * @generated
	 */
	EList<CharterContract> getCharteringContracts();

} // end of  CommercialModel

// finish type fixing
