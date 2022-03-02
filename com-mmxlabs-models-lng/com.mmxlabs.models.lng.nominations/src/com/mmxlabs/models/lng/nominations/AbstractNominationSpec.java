/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Nomination Spec</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#isCounterparty <em>Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getRemark <em>Remark</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSize <em>Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSizeUnits <em>Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getDayOfMonth <em>Day Of Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getAlertSize <em>Alert Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getAlertSizeUnits <em>Alert Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSide <em>Side</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getRefererId <em>Referer Id</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNominationSpec()
 * @model abstract="true"
 * @generated
 */
public interface AbstractNominationSpec extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNominationSpec_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Counterparty</em>' attribute.
	 * @see #setCounterparty(boolean)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNominationSpec_Counterparty()
	 * @model
	 * @generated
	 */
	boolean isCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#isCounterparty <em>Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Counterparty</em>' attribute.
	 * @see #isCounterparty()
	 * @generated
	 */
	void setCounterparty(boolean value);

	/**
	 * Returns the value of the '<em><b>Remark</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Remark</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Remark</em>' attribute.
	 * @see #setRemark(String)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNominationSpec_Remark()
	 * @model
	 * @generated
	 */
	String getRemark();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getRemark <em>Remark</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Remark</em>' attribute.
	 * @see #getRemark()
	 * @generated
	 */
	void setRemark(String value);

	/**
	 * Returns the value of the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Size</em>' attribute.
	 * @see #isSetSize()
	 * @see #unsetSize()
	 * @see #setSize(int)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNominationSpec_Size()
	 * @model unsettable="true"
	 * @generated
	 */
	int getSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSize <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Size</em>' attribute.
	 * @see #isSetSize()
	 * @see #unsetSize()
	 * @see #getSize()
	 * @generated
	 */
	void setSize(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSize <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSize()
	 * @see #getSize()
	 * @see #setSize(int)
	 * @generated
	 */
	void unsetSize();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSize <em>Size</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Size</em>' attribute is set.
	 * @see #unsetSize()
	 * @see #getSize()
	 * @see #setSize(int)
	 * @generated
	 */
	boolean isSetSize();

	/**
	 * Returns the value of the '<em><b>Size Units</b></em>' attribute.
	 * The default value is <code>"DaysPrior"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.nominations.DatePeriodPrior}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Size Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.nominations.DatePeriodPrior
	 * @see #setSizeUnits(DatePeriodPrior)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNominationSpec_SizeUnits()
	 * @model default="DaysPrior"
	 * @generated
	 */
	DatePeriodPrior getSizeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSizeUnits <em>Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.nominations.DatePeriodPrior
	 * @see #getSizeUnits()
	 * @generated
	 */
	void setSizeUnits(DatePeriodPrior value);

	/**
	 * Returns the value of the '<em><b>Day Of Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Day Of Month</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Day Of Month</em>' attribute.
	 * @see #isSetDayOfMonth()
	 * @see #unsetDayOfMonth()
	 * @see #setDayOfMonth(int)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNominationSpec_DayOfMonth()
	 * @model unsettable="true"
	 * @generated
	 */
	int getDayOfMonth();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getDayOfMonth <em>Day Of Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Day Of Month</em>' attribute.
	 * @see #isSetDayOfMonth()
	 * @see #unsetDayOfMonth()
	 * @see #getDayOfMonth()
	 * @generated
	 */
	void setDayOfMonth(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getDayOfMonth <em>Day Of Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDayOfMonth()
	 * @see #getDayOfMonth()
	 * @see #setDayOfMonth(int)
	 * @generated
	 */
	void unsetDayOfMonth();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getDayOfMonth <em>Day Of Month</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Day Of Month</em>' attribute is set.
	 * @see #unsetDayOfMonth()
	 * @see #getDayOfMonth()
	 * @see #setDayOfMonth(int)
	 * @generated
	 */
	boolean isSetDayOfMonth();

	/**
	 * Returns the value of the '<em><b>Alert Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Alert Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alert Size</em>' attribute.
	 * @see #isSetAlertSize()
	 * @see #unsetAlertSize()
	 * @see #setAlertSize(int)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNominationSpec_AlertSize()
	 * @model unsettable="true"
	 * @generated
	 */
	int getAlertSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getAlertSize <em>Alert Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Alert Size</em>' attribute.
	 * @see #isSetAlertSize()
	 * @see #unsetAlertSize()
	 * @see #getAlertSize()
	 * @generated
	 */
	void setAlertSize(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getAlertSize <em>Alert Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAlertSize()
	 * @see #getAlertSize()
	 * @see #setAlertSize(int)
	 * @generated
	 */
	void unsetAlertSize();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getAlertSize <em>Alert Size</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Alert Size</em>' attribute is set.
	 * @see #unsetAlertSize()
	 * @see #getAlertSize()
	 * @see #setAlertSize(int)
	 * @generated
	 */
	boolean isSetAlertSize();

	/**
	 * Returns the value of the '<em><b>Alert Size Units</b></em>' attribute.
	 * The default value is <code>"DaysPrior"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.nominations.DatePeriodPrior}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Alert Size Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alert Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.nominations.DatePeriodPrior
	 * @see #setAlertSizeUnits(DatePeriodPrior)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNominationSpec_AlertSizeUnits()
	 * @model default="DaysPrior"
	 * @generated
	 */
	DatePeriodPrior getAlertSizeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getAlertSizeUnits <em>Alert Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Alert Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.nominations.DatePeriodPrior
	 * @see #getAlertSizeUnits()
	 * @generated
	 */
	void setAlertSizeUnits(DatePeriodPrior value);

	/**
	 * Returns the value of the '<em><b>Side</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.nominations.Side}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Side</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Side</em>' attribute.
	 * @see com.mmxlabs.models.lng.nominations.Side
	 * @see #setSide(Side)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNominationSpec_Side()
	 * @model
	 * @generated
	 */
	Side getSide();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getSide <em>Side</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Side</em>' attribute.
	 * @see com.mmxlabs.models.lng.nominations.Side
	 * @see #getSide()
	 * @generated
	 */
	void setSide(Side value);

	/**
	 * Returns the value of the '<em><b>Referer Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referer Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referer Id</em>' attribute.
	 * @see #setRefererId(String)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractNominationSpec_RefererId()
	 * @model
	 * @generated
	 */
	String getRefererId();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec#getRefererId <em>Referer Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referer Id</em>' attribute.
	 * @see #getRefererId()
	 * @generated
	 */
	void setRefererId(String value);

} // AbstractNominationSpec
