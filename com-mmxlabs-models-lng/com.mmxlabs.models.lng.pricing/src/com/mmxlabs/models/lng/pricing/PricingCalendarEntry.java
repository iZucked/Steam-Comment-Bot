/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import java.time.LocalDate;
import java.time.YearMonth;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Calendar Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingCalendarEntry#getComment <em>Comment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingCalendarEntry#getStart <em>Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingCalendarEntry#getEnd <em>End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingCalendarEntry#getMonth <em>Month</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingCalendarEntry()
 * @model
 * @generated
 */
public interface PricingCalendarEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Comment</em>' attribute.
	 * @see #setComment(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingCalendarEntry_Comment()
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PricingCalendarEntry#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

	/**
	 * Returns the value of the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start</em>' attribute.
	 * @see #setStart(LocalDate)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingCalendarEntry_Start()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PricingCalendarEntry#getStart <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' attribute.
	 * @see #getStart()
	 * @generated
	 */
	void setStart(LocalDate value);

	/**
	 * Returns the value of the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End</em>' attribute.
	 * @see #setEnd(LocalDate)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingCalendarEntry_End()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PricingCalendarEntry#getEnd <em>End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End</em>' attribute.
	 * @see #getEnd()
	 * @generated
	 */
	void setEnd(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Month</em>' attribute.
	 * @see #setMonth(YearMonth)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingCalendarEntry_Month()
	 * @model dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getMonth();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PricingCalendarEntry#getMonth <em>Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Month</em>' attribute.
	 * @see #getMonth()
	 * @generated
	 */
	void setMonth(YearMonth value);

} // PricingCalendarEntry
