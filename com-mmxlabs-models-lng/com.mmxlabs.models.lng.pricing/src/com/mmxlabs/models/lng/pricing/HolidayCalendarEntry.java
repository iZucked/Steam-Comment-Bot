/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Holiday Calendar Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.HolidayCalendarEntry#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.HolidayCalendarEntry#getComment <em>Comment</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getHolidayCalendarEntry()
 * @model
 * @generated
 */
public interface HolidayCalendarEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(LocalDate)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getHolidayCalendarEntry_Date()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.HolidayCalendarEntry#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(LocalDate value);

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
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getHolidayCalendarEntry_Comment()
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.HolidayCalendarEntry#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

} // HolidayCalendarEntry
