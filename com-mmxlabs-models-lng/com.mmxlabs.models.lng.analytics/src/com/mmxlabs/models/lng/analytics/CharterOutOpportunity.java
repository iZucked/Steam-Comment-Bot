/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.mmxcore.UUIDObject;

import java.time.LocalDate;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Out Opportunity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getDuration <em>Duration</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCharterOutOpportunity()
 * @model
 * @generated
 */
public interface CharterOutOpportunity extends UUIDObject, VesselEventOption {
	/**
	 * Returns the value of the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hire Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hire Cost</em>' attribute.
	 * @see #setHireCost(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCharterOutOpportunity_HireCost()
	 * @model
	 * @generated
	 */
	int getHireCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getHireCost <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hire Cost</em>' attribute.
	 * @see #getHireCost()
	 * @generated
	 */
	void setHireCost(int value);

	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #setPort(Port)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCharterOutOpportunity_Port()
	 * @model
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCharterOutOpportunity_Date()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCharterOutOpportunity_Duration()
	 * @model
	 * @generated
	 */
	int getDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(int value);

} // CharterOutOpportunity
