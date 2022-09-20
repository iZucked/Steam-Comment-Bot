/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.transfers;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transfer Agreement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getFromEntity <em>From Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getToEntity <em>To Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getIncoterm <em>Incoterm</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getCompanyStatus <em>Company Status</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getPricingBasis <em>Pricing Basis</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getBufferDays <em>Buffer Days</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferAgreement()
 * @model
 * @generated
 */
public interface TransferAgreement extends NamedObject {
	/**
	 * Returns the value of the '<em><b>From Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From Entity</em>' reference.
	 * @see #setFromEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferAgreement_FromEntity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getFromEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getFromEntity <em>From Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From Entity</em>' reference.
	 * @see #getFromEntity()
	 * @generated
	 */
	void setFromEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>To Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To Entity</em>' reference.
	 * @see #setToEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferAgreement_ToEntity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getToEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getToEntity <em>To Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Entity</em>' reference.
	 * @see #getToEntity()
	 * @generated
	 */
	void setToEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Expression</em>' attribute.
	 * @see #setPriceExpression(String)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferAgreement_PriceExpression()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity' parameters='salesprice'"
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

	/**
	 * Returns the value of the '<em><b>Incoterm</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.transfers.TransferIncoterm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Incoterm</em>' attribute.
	 * @see com.mmxlabs.models.lng.transfers.TransferIncoterm
	 * @see #setIncoterm(TransferIncoterm)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferAgreement_Incoterm()
	 * @model
	 * @generated
	 */
	TransferIncoterm getIncoterm();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getIncoterm <em>Incoterm</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Incoterm</em>' attribute.
	 * @see com.mmxlabs.models.lng.transfers.TransferIncoterm
	 * @see #getIncoterm()
	 * @generated
	 */
	void setIncoterm(TransferIncoterm value);

	/**
	 * Returns the value of the '<em><b>Company Status</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.transfers.CompanyStatus}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Company Status</em>' attribute.
	 * @see com.mmxlabs.models.lng.transfers.CompanyStatus
	 * @see #setCompanyStatus(CompanyStatus)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferAgreement_CompanyStatus()
	 * @model
	 * @generated
	 */
	CompanyStatus getCompanyStatus();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getCompanyStatus <em>Company Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Company Status</em>' attribute.
	 * @see com.mmxlabs.models.lng.transfers.CompanyStatus
	 * @see #getCompanyStatus()
	 * @generated
	 */
	void setCompanyStatus(CompanyStatus value);

	/**
	 * Returns the value of the '<em><b>Pricing Basis</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pricing Basis</em>' attribute.
	 * @see #setPricingBasis(String)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferAgreement_PricingBasis()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='pricingBasis'"
	 * @generated
	 */
	String getPricingBasis();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getPricingBasis <em>Pricing Basis</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Basis</em>' attribute.
	 * @see #getPricingBasis()
	 * @generated
	 */
	void setPricingBasis(String value);

	/**
	 * Returns the value of the '<em><b>Buffer Days</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buffer Days</em>' attribute.
	 * @see #isSetBufferDays()
	 * @see #unsetBufferDays()
	 * @see #setBufferDays(int)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferAgreement_BufferDays()
	 * @model default="0" unsettable="true"
	 * @generated
	 */
	int getBufferDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getBufferDays <em>Buffer Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Buffer Days</em>' attribute.
	 * @see #isSetBufferDays()
	 * @see #unsetBufferDays()
	 * @see #getBufferDays()
	 * @generated
	 */
	void setBufferDays(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getBufferDays <em>Buffer Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetBufferDays()
	 * @see #getBufferDays()
	 * @see #setBufferDays(int)
	 * @generated
	 */
	void unsetBufferDays();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getBufferDays <em>Buffer Days</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Buffer Days</em>' attribute is set.
	 * @see #unsetBufferDays()
	 * @see #getBufferDays()
	 * @see #setBufferDays(int)
	 * @generated
	 */
	boolean isSetBufferDays();

} // TransferAgreement
