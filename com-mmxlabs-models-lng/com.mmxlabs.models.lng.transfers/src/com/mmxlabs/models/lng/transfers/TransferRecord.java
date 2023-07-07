/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.transfers;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

import com.mmxlabs.models.lng.commercial.BusinessUnit;
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
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getFromBU <em>From BU</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getToBU <em>To BU</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferRecord#getCode <em>Code</em>}</li>
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
	 * @see #isSetLhs()
	 * @see #unsetLhs()
	 * @see #setLhs(Slot)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_Lhs()
	 * @model unsettable="true"
	 * @generated
	 */
	Slot<?> getLhs();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getLhs <em>Lhs</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs</em>' reference.
	 * @see #isSetLhs()
	 * @see #unsetLhs()
	 * @see #getLhs()
	 * @generated
	 */
	void setLhs(Slot<?> value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getLhs <em>Lhs</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetLhs()
	 * @see #getLhs()
	 * @see #setLhs(Slot)
	 * @generated
	 */
	void unsetLhs();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getLhs <em>Lhs</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Lhs</em>' reference is set.
	 * @see #unsetLhs()
	 * @see #getLhs()
	 * @see #setLhs(Slot)
	 * @generated
	 */
	boolean isSetLhs();

	/**
	 * Returns the value of the '<em><b>Rhs</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs</em>' reference.
	 * @see #isSetRhs()
	 * @see #unsetRhs()
	 * @see #setRhs(TransferRecord)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_Rhs()
	 * @model unsettable="true"
	 * @generated
	 */
	TransferRecord getRhs();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getRhs <em>Rhs</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs</em>' reference.
	 * @see #isSetRhs()
	 * @see #unsetRhs()
	 * @see #getRhs()
	 * @generated
	 */
	void setRhs(TransferRecord value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getRhs <em>Rhs</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetRhs()
	 * @see #getRhs()
	 * @see #setRhs(TransferRecord)
	 * @generated
	 */
	void unsetRhs();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getRhs <em>Rhs</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Rhs</em>' reference is set.
	 * @see #unsetRhs()
	 * @see #getRhs()
	 * @see #setRhs(TransferRecord)
	 * @generated
	 */
	boolean isSetRhs();

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
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity' parameters='salesprice'"
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
	 * Returns the value of the '<em><b>From BU</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From BU</em>' reference.
	 * @see #isSetFromBU()
	 * @see #unsetFromBU()
	 * @see #setFromBU(BusinessUnit)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_FromBU()
	 * @model unsettable="true"
	 * @generated
	 */
	BusinessUnit getFromBU();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getFromBU <em>From BU</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From BU</em>' reference.
	 * @see #isSetFromBU()
	 * @see #unsetFromBU()
	 * @see #getFromBU()
	 * @generated
	 */
	void setFromBU(BusinessUnit value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getFromBU <em>From BU</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFromBU()
	 * @see #getFromBU()
	 * @see #setFromBU(BusinessUnit)
	 * @generated
	 */
	void unsetFromBU();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getFromBU <em>From BU</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>From BU</em>' reference is set.
	 * @see #unsetFromBU()
	 * @see #getFromBU()
	 * @see #setFromBU(BusinessUnit)
	 * @generated
	 */
	boolean isSetFromBU();

	/**
	 * Returns the value of the '<em><b>To BU</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To BU</em>' reference.
	 * @see #isSetToBU()
	 * @see #unsetToBU()
	 * @see #setToBU(BusinessUnit)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_ToBU()
	 * @model unsettable="true"
	 * @generated
	 */
	BusinessUnit getToBU();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getToBU <em>To BU</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To BU</em>' reference.
	 * @see #isSetToBU()
	 * @see #unsetToBU()
	 * @see #getToBU()
	 * @generated
	 */
	void setToBU(BusinessUnit value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getToBU <em>To BU</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetToBU()
	 * @see #getToBU()
	 * @see #setToBU(BusinessUnit)
	 * @generated
	 */
	void unsetToBU();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getToBU <em>To BU</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>To BU</em>' reference is set.
	 * @see #unsetToBU()
	 * @see #getToBU()
	 * @see #setToBU(BusinessUnit)
	 * @generated
	 */
	boolean isSetToBU();

	/**
	 * Returns the value of the '<em><b>Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Code</em>' attribute.
	 * @see #setCode(String)
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferRecord_Code()
	 * @model
	 * @generated
	 */
	String getCode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getCode <em>Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code</em>' attribute.
	 * @see #getCode()
	 * @generated
	 */
	void setCode(String value);

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

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	BusinessUnit getRecordOrDelegateFromBU();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	BusinessUnit getRecordOrDelegateToBU();

} // TransferRecord
