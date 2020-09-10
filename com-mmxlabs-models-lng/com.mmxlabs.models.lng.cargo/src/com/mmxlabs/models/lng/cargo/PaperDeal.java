/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.mmxcore.NamedObject;
import java.time.LocalDate;

import java.time.YearMonth;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Paper Deal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PaperDeal#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PaperDeal#getPricingType <em>Pricing Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PaperDeal#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PaperDeal#getInstrument <em>Instrument</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PaperDeal#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PaperDeal#getPricingMonth <em>Pricing Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PaperDeal#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PaperDeal#getEndDate <em>End Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PaperDeal#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PaperDeal#getYear <em>Year</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PaperDeal#getComment <em>Comment</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal()
 * @model abstract="true"
 * @generated
 */
public interface PaperDeal extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quantity</em>' attribute.
	 * @see #setQuantity(double)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal_Quantity()
	 * @model
	 * @generated
	 */
	double getQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getQuantity <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quantity</em>' attribute.
	 * @see #getQuantity()
	 * @generated
	 */
	void setQuantity(double value);

	/**
	 * Returns the value of the '<em><b>Pricing Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pricing Month</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pricing Month</em>' attribute.
	 * @see #setPricingMonth(YearMonth)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal_PricingMonth()
	 * @model dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getPricingMonth();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getPricingMonth <em>Pricing Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Month</em>' attribute.
	 * @see #getPricingMonth()
	 * @generated
	 */
	void setPricingMonth(YearMonth value);

	/**
	 * Returns the value of the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Date</em>' attribute.
	 * @see #setStartDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal_StartDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Date</em>' attribute.
	 * @see #getStartDate()
	 * @generated
	 */
	void setStartDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Date</em>' attribute.
	 * @see #setEndDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal_EndDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getEndDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getEndDate <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Date</em>' attribute.
	 * @see #getEndDate()
	 * @generated
	 */
	void setEndDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>Year</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Year</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Year</em>' attribute.
	 * @see #setYear(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal_Year()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='####'"
	 * @generated
	 */
	int getYear();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getYear <em>Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Year</em>' attribute.
	 * @see #getYear()
	 * @generated
	 */
	void setYear(int value);

	/**
	 * Returns the value of the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Comment</em>' attribute.
	 * @see #setComment(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal_Comment()
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

	/**
	 * Returns the value of the '<em><b>Instrument</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Instrument</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Instrument</em>' reference.
	 * @see #isSetInstrument()
	 * @see #unsetInstrument()
	 * @see #setInstrument(SettleStrategy)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal_Instrument()
	 * @model unsettable="true"
	 * @generated
	 */
	SettleStrategy getInstrument();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getInstrument <em>Instrument</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Instrument</em>' reference.
	 * @see #isSetInstrument()
	 * @see #unsetInstrument()
	 * @see #getInstrument()
	 * @generated
	 */
	void setInstrument(SettleStrategy value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getInstrument <em>Instrument</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInstrument()
	 * @see #getInstrument()
	 * @see #setInstrument(SettleStrategy)
	 * @generated
	 */
	void unsetInstrument();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getInstrument <em>Instrument</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Instrument</em>' reference is set.
	 * @see #unsetInstrument()
	 * @see #getInstrument()
	 * @see #setInstrument(SettleStrategy)
	 * @generated
	 */
	boolean isSetInstrument();

	/**
	 * Returns the value of the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price</em>' attribute.
	 * @see #setPrice(double)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal_Price()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='-##0.###' exportFormatString='##0.###'"
	 * @generated
	 */
	double getPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getPrice <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price</em>' attribute.
	 * @see #getPrice()
	 * @generated
	 */
	void setPrice(double value);

	/**
	 * Returns the value of the '<em><b>Pricing Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.PaperPricingType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pricing Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pricing Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.PaperPricingType
	 * @see #setPricingType(PaperPricingType)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal_PricingType()
	 * @model
	 * @generated
	 */
	PaperPricingType getPricingType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getPricingType <em>Pricing Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.PaperPricingType
	 * @see #getPricingType()
	 * @generated
	 */
	void setPricingType(PaperPricingType value);

	/**
	 * Returns the value of the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' attribute.
	 * @see #setIndex(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPaperDeal_Index()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getIndex <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' attribute.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(String value);

} // PaperDeal
