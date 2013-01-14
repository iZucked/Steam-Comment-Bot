/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;
import com.mmxlabs.models.lng.types.ALegalEntity;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Legal Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.LegalEntity#getTaxRates <em>Tax Rates</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.LegalEntity#getTransferPrice <em>Transfer Price</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getLegalEntity()
 * @model
 * @generated
 */
public interface LegalEntity extends ALegalEntity {

	/**
	 * Returns the value of the '<em><b>Tax Rates</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.TaxRate}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tax Rates</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tax Rates</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getLegalEntity_TaxRates()
	 * @model containment="true"
	 * @generated
	 */
	EList<TaxRate> getTaxRates();

	/**
	 * Returns the value of the '<em><b>Transfer Price</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transfer Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transfer Price</em>' attribute.
	 * @see #setTransferPrice(float)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getLegalEntity_TransferPrice()
	 * @model default="0"
	 * @generated
	 */
	float getTransferPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.LegalEntity#getTransferPrice <em>Transfer Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transfer Price</em>' attribute.
	 * @see #getTransferPrice()
	 * @generated
	 */
	void setTransferPrice(float value);
} // end of  LegalEntity

// finish type fixing
