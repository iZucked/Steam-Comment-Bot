/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Out Market Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters#getCharterOutStartDate <em>Charter Out Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters#getCharterOutEndDate <em>Charter Out End Date</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterOutMarketParameters()
 * @model
 * @generated
 */
public interface CharterOutMarketParameters extends EObject {
	/**
	 * Returns the value of the '<em><b>Charter Out Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Out Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Out Start Date</em>' attribute.
	 * @see #isSetCharterOutStartDate()
	 * @see #unsetCharterOutStartDate()
	 * @see #setCharterOutStartDate(LocalDate)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterOutMarketParameters_CharterOutStartDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getCharterOutStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters#getCharterOutStartDate <em>Charter Out Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Out Start Date</em>' attribute.
	 * @see #isSetCharterOutStartDate()
	 * @see #unsetCharterOutStartDate()
	 * @see #getCharterOutStartDate()
	 * @generated
	 */
	void setCharterOutStartDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters#getCharterOutStartDate <em>Charter Out Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCharterOutStartDate()
	 * @see #getCharterOutStartDate()
	 * @see #setCharterOutStartDate(LocalDate)
	 * @generated
	 */
	void unsetCharterOutStartDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters#getCharterOutStartDate <em>Charter Out Start Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Charter Out Start Date</em>' attribute is set.
	 * @see #unsetCharterOutStartDate()
	 * @see #getCharterOutStartDate()
	 * @see #setCharterOutStartDate(LocalDate)
	 * @generated
	 */
	boolean isSetCharterOutStartDate();

	/**
	 * Returns the value of the '<em><b>Charter Out End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Out End Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Out End Date</em>' attribute.
	 * @see #isSetCharterOutEndDate()
	 * @see #unsetCharterOutEndDate()
	 * @see #setCharterOutEndDate(LocalDate)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterOutMarketParameters_CharterOutEndDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getCharterOutEndDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters#getCharterOutEndDate <em>Charter Out End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Out End Date</em>' attribute.
	 * @see #isSetCharterOutEndDate()
	 * @see #unsetCharterOutEndDate()
	 * @see #getCharterOutEndDate()
	 * @generated
	 */
	void setCharterOutEndDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters#getCharterOutEndDate <em>Charter Out End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCharterOutEndDate()
	 * @see #getCharterOutEndDate()
	 * @see #setCharterOutEndDate(LocalDate)
	 * @generated
	 */
	void unsetCharterOutEndDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters#getCharterOutEndDate <em>Charter Out End Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Charter Out End Date</em>' attribute is set.
	 * @see #unsetCharterOutEndDate()
	 * @see #getCharterOutEndDate()
	 * @see #setCharterOutEndDate(LocalDate)
	 * @generated
	 */
	boolean isSetCharterOutEndDate();

} // CharterOutMarketParameters
