/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import java.time.LocalDateTime;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Local Date Time Holder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.LocalDateTimeHolder#getDateTime <em>Date Time</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getLocalDateTimeHolder()
 * @model
 * @generated
 */
public interface LocalDateTimeHolder extends EObject {
	/**
	 * Returns the value of the '<em><b>Date Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date Time</em>' attribute.
	 * @see #setDateTime(LocalDateTime)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getLocalDateTimeHolder_DateTime()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getDateTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.LocalDateTimeHolder#getDateTime <em>Date Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date Time</em>' attribute.
	 * @see #getDateTime()
	 * @generated
	 */
	void setDateTime(LocalDateTime value);

} // LocalDateTimeHolder
