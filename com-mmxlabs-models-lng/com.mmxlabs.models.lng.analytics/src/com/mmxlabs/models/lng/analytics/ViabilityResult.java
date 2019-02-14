/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Viability Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestETA <em>Earliest ETA</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestETA <em>Latest ETA</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestVolume <em>Earliest Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestVolume <em>Latest Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestPrice <em>Earliest Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestPrice <em>Latest Price</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityResult()
 * @model
 * @generated
 */
public interface ViabilityResult extends EObject {
	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(SpotMarket)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityResult_Target()
	 * @model
	 * @generated
	 */
	SpotMarket getTarget();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(SpotMarket value);

	/**
	 * Returns the value of the '<em><b>Earliest ETA</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Earliest ETA</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Earliest ETA</em>' attribute.
	 * @see #setEarliestETA(LocalDate)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityResult_EarliestETA()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getEarliestETA();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestETA <em>Earliest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Earliest ETA</em>' attribute.
	 * @see #getEarliestETA()
	 * @generated
	 */
	void setEarliestETA(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Latest ETA</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Latest ETA</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Latest ETA</em>' attribute.
	 * @see #setLatestETA(LocalDate)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityResult_LatestETA()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getLatestETA();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestETA <em>Latest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Latest ETA</em>' attribute.
	 * @see #getLatestETA()
	 * @generated
	 */
	void setLatestETA(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Earliest Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Earliest Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Earliest Volume</em>' attribute.
	 * @see #setEarliestVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityResult_EarliestVolume()
	 * @model unique="false"
	 * @generated
	 */
	int getEarliestVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestVolume <em>Earliest Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Earliest Volume</em>' attribute.
	 * @see #getEarliestVolume()
	 * @generated
	 */
	void setEarliestVolume(int value);

	/**
	 * Returns the value of the '<em><b>Latest Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Latest Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Latest Volume</em>' attribute.
	 * @see #setLatestVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityResult_LatestVolume()
	 * @model unique="false"
	 * @generated
	 */
	int getLatestVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestVolume <em>Latest Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Latest Volume</em>' attribute.
	 * @see #getLatestVolume()
	 * @generated
	 */
	void setLatestVolume(int value);

	/**
	 * Returns the value of the '<em><b>Earliest Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Earliest Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Earliest Price</em>' attribute.
	 * @see #setEarliestPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityResult_EarliestPrice()
	 * @model unique="false"
	 * @generated
	 */
	double getEarliestPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestPrice <em>Earliest Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Earliest Price</em>' attribute.
	 * @see #getEarliestPrice()
	 * @generated
	 */
	void setEarliestPrice(double value);

	/**
	 * Returns the value of the '<em><b>Latest Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Latest Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Latest Price</em>' attribute.
	 * @see #setLatestPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityResult_LatestPrice()
	 * @model unique="false"
	 * @generated
	 */
	double getLatestPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestPrice <em>Latest Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Latest Price</em>' attribute.
	 * @see #getLatestPrice()
	 * @generated
	 */
	void setLatestPrice(double value);

} // ViabilityResult
