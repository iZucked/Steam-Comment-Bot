

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getEntities <em>Entities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getContracts <em>Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CommercialModel#getShippingEntity <em>Shipping Entity</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCommercialModel()
 * @model
 * @generated
 */
public interface CommercialModel extends UUIDObject, NamedObject {
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
	 * Returns the value of the '<em><b>Contracts</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.Contract}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contracts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contracts</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCommercialModel_Contracts()
	 * @model containment="true"
	 * @generated
	 */
	EList<Contract> getContracts();

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

} // end of  CommercialModel

// finish type fixing
