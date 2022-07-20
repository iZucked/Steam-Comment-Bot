/**
 */
package com.mmxlabs.models.lng.transfers;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transfer Record</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getTransferAgreement <em>Transfer Agreement</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getLhs <em>Lhs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getRhs <em>Rhs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getCargoReleaseDate <em>Cargo Release Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getPricingDate <em>Pricing Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getIncoterm <em>Incoterm</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getStatus <em>Status</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getNotes <em>Notes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#isStale <em>Stale</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord()
 * @model
 * @generated
 */
public interface TransferRecord extends NamedObject, UUIDObject {
	/**
	 * Returns the value of the '<em><b>Transfer Agreement</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transfer Agreement</em>' reference.
	 * @see #setTransferAgreement(TransferAgreement)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_TransferAgreement()
	 * @model
	 * @generated
	 */
	TransferAgreement getTransferAgreement();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getTransferAgreement <em>Transfer Agreement</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transfer Agreement</em>' reference.
	 * @see #getTransferAgreement()
	 * @generated
	 */
	void setTransferAgreement(TransferAgreement value);

	/**
	 * Returns the value of the '<em><b>Lhs</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs</em>' reference.
	 * @see #setLhs(LoadSlot)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_Lhs()
	 * @model
	 * @generated
	 */
	LoadSlot getLhs();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getLhs <em>Lhs</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs</em>' reference.
	 * @see #getLhs()
	 * @generated
	 */
	void setLhs(LoadSlot value);

	/**
	 * Returns the value of the '<em><b>Rhs</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs</em>' reference.
	 * @see #setRhs(DischargeSlot)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_Rhs()
	 * @model
	 * @generated
	 */
	DischargeSlot getRhs();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getRhs <em>Rhs</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs</em>' reference.
	 * @see #getRhs()
	 * @generated
	 */
	void setRhs(DischargeSlot value);

	/**
	 * Returns the value of the '<em><b>Cargo Release Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Release Date</em>' attribute.
	 * @see #setCargoReleaseDate(LocalDate)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_CargoReleaseDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getCargoReleaseDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getCargoReleaseDate <em>Cargo Release Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Release Date</em>' attribute.
	 * @see #getCargoReleaseDate()
	 * @generated
	 */
	void setCargoReleaseDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Expression</em>' attribute.
	 * @see #isSetPriceExpression()
	 * @see #unsetPriceExpression()
	 * @see #setPriceExpression(String)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_PriceExpression()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #isSetPriceExpression()
	 * @see #unsetPriceExpression()
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPriceExpression()
	 * @see #getPriceExpression()
	 * @see #setPriceExpression(String)
	 * @generated
	 */
	void unsetPriceExpression();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getPriceExpression <em>Price Expression</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Price Expression</em>' attribute is set.
	 * @see #unsetPriceExpression()
	 * @see #getPriceExpression()
	 * @see #setPriceExpression(String)
	 * @generated
	 */
	boolean isSetPriceExpression();

	/**
	 * Returns the value of the '<em><b>Pricing Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pricing Date</em>' attribute.
	 * @see #isSetPricingDate()
	 * @see #unsetPricingDate()
	 * @see #setPricingDate(LocalDate)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_PricingDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getPricingDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getPricingDate <em>Pricing Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Date</em>' attribute.
	 * @see #isSetPricingDate()
	 * @see #unsetPricingDate()
	 * @see #getPricingDate()
	 * @generated
	 */
	void setPricingDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getPricingDate <em>Pricing Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPricingDate()
	 * @see #getPricingDate()
	 * @see #setPricingDate(LocalDate)
	 * @generated
	 */
	void unsetPricingDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getPricingDate <em>Pricing Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Pricing Date</em>' attribute is set.
	 * @see #unsetPricingDate()
	 * @see #getPricingDate()
	 * @see #setPricingDate(LocalDate)
	 * @generated
	 */
	boolean isSetPricingDate();

	/**
	 * Returns the value of the '<em><b>Incoterm</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.transfers.TransferIncoterm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Incoterm</em>' attribute.
	 * @see com.mmxlabs.models.lng.transfers.TransferIncoterm
	 * @see #isSetIncoterm()
	 * @see #unsetIncoterm()
	 * @see #setIncoterm(TransferIncoterm)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_Incoterm()
	 * @model unsettable="true"
	 * @generated
	 */
	TransferIncoterm getIncoterm();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getIncoterm <em>Incoterm</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Incoterm</em>' attribute.
	 * @see com.mmxlabs.models.lng.transfers.TransferIncoterm
	 * @see #isSetIncoterm()
	 * @see #unsetIncoterm()
	 * @see #getIncoterm()
	 * @generated
	 */
	void setIncoterm(TransferIncoterm value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getIncoterm <em>Incoterm</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIncoterm()
	 * @see #getIncoterm()
	 * @see #setIncoterm(TransferIncoterm)
	 * @generated
	 */
	void unsetIncoterm();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getIncoterm <em>Incoterm</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Incoterm</em>' attribute is set.
	 * @see #unsetIncoterm()
	 * @see #getIncoterm()
	 * @see #setIncoterm(TransferIncoterm)
	 * @generated
	 */
	boolean isSetIncoterm();

	/**
	 * Returns the value of the '<em><b>Status</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.transfers.TransferStatus}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Status</em>' attribute.
	 * @see com.mmxlabs.models.lng.transfers.TransferStatus
	 * @see #setStatus(TransferStatus)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_Status()
	 * @model
	 * @generated
	 */
	TransferStatus getStatus();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getStatus <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Status</em>' attribute.
	 * @see com.mmxlabs.models.lng.transfers.TransferStatus
	 * @see #getStatus()
	 * @generated
	 */
	void setStatus(TransferStatus value);

	/**
	 * Returns the value of the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notes</em>' attribute.
	 * @see #setNotes(String)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_Notes()
	 * @model
	 * @generated
	 */
	String getNotes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getNotes <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notes</em>' attribute.
	 * @see #getNotes()
	 * @generated
	 */
	void setNotes(String value);

	/**
	 * Returns the value of the '<em><b>Stale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Stale</em>' attribute.
	 * @see #setStale(boolean)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_Stale()
	 * @model
	 * @generated
	 */
	boolean isStale();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#isStale <em>Stale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Stale</em>' attribute.
	 * @see #isStale()
	 * @generated
	 */
	void setStale(boolean value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	BaseLegalEntity getFromEntity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	BaseLegalEntity getToEntity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	CompanyStatus getCompanyStatus();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getRecordOrDelegatePriceExpression();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	TransferIncoterm getRecordOrDelegateIncoterm();

} // TransferRecord
