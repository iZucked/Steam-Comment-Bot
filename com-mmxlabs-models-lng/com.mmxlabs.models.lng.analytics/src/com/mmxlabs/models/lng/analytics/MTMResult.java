/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
 * A representation of the model object '<em><b>MTM Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MTMResult#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MTMResult#getEarliestETA <em>Earliest ETA</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MTMResult#getEarliestVolume <em>Earliest Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MTMResult#getEarliestPrice <em>Earliest Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MTMResult#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MTMResult#getShippingCost <em>Shipping Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MTMResult#getOriginalVolume <em>Original Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MTMResult#getOriginalPrice <em>Original Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MTMResult#getTotalShippingCost <em>Total Shipping Cost</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMTMResult()
 * @model
 * @generated
 */
public interface MTMResult extends EObject {
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMTMResult_Target()
	 * @model
	 * @generated
	 */
	SpotMarket getTarget();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MTMResult#getTarget <em>Target</em>}' reference.
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMTMResult_EarliestETA()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getEarliestETA();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MTMResult#getEarliestETA <em>Earliest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Earliest ETA</em>' attribute.
	 * @see #getEarliestETA()
	 * @generated
	 */
	void setEarliestETA(LocalDate value);

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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMTMResult_EarliestVolume()
	 * @model unique="false"
	 * @generated
	 */
	int getEarliestVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MTMResult#getEarliestVolume <em>Earliest Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Earliest Volume</em>' attribute.
	 * @see #getEarliestVolume()
	 * @generated
	 */
	void setEarliestVolume(int value);

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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMTMResult_EarliestPrice()
	 * @model unique="false"
	 * @generated
	 */
	double getEarliestPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MTMResult#getEarliestPrice <em>Earliest Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Earliest Price</em>' attribute.
	 * @see #getEarliestPrice()
	 * @generated
	 */
	void setEarliestPrice(double value);

	/**
	 * Returns the value of the '<em><b>Shipping</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping</em>' containment reference.
	 * @see #setShipping(ShippingOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMTMResult_Shipping()
	 * @model containment="true"
	 * @generated
	 */
	ShippingOption getShipping();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MTMResult#getShipping <em>Shipping</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping</em>' containment reference.
	 * @see #getShipping()
	 * @generated
	 */
	void setShipping(ShippingOption value);

	/**
	 * Returns the value of the '<em><b>Shipping Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Cost</em>' attribute.
	 * @see #setShippingCost(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMTMResult_ShippingCost()
	 * @model
	 * @generated
	 */
	double getShippingCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MTMResult#getShippingCost <em>Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Cost</em>' attribute.
	 * @see #getShippingCost()
	 * @generated
	 */
	void setShippingCost(double value);

	/**
	 * Returns the value of the '<em><b>Original Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Volume</em>' attribute.
	 * @see #setOriginalVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMTMResult_OriginalVolume()
	 * @model unique="false"
	 * @generated
	 */
	int getOriginalVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MTMResult#getOriginalVolume <em>Original Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Volume</em>' attribute.
	 * @see #getOriginalVolume()
	 * @generated
	 */
	void setOriginalVolume(int value);

	/**
	 * Returns the value of the '<em><b>Original Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Price</em>' attribute.
	 * @see #setOriginalPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMTMResult_OriginalPrice()
	 * @model unique="false"
	 * @generated
	 */
	double getOriginalPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MTMResult#getOriginalPrice <em>Original Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Price</em>' attribute.
	 * @see #getOriginalPrice()
	 * @generated
	 */
	void setOriginalPrice(double value);

	/**
	 * Returns the value of the '<em><b>Total Shipping Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Total Shipping Cost</em>' attribute.
	 * @see #setTotalShippingCost(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMTMResult_TotalShippingCost()
	 * @model unique="false"
	 * @generated
	 */
	int getTotalShippingCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MTMResult#getTotalShippingCost <em>Total Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Total Shipping Cost</em>' attribute.
	 * @see #getTotalShippingCost()
	 * @generated
	 */
	void setTotalShippingCost(int value);

} // MTMResult
