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
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
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

} // TransferAgreement
