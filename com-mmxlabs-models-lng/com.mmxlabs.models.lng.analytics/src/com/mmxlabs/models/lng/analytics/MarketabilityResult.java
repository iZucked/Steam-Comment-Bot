/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Marketability Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getEarliestETA <em>Earliest ETA</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getLatestETA <em>Latest ETA</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getEarliestVolume <em>Earliest Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getLatestVolume <em>Latest Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getEarliestPrice <em>Earliest Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getLatestPrice <em>Latest Price</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResult()
 * @model
 * @generated
 */
public interface MarketabilityResult extends EObject {
	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(SpotMarket)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResult_Target()
	 * @model
	 * @generated
	 */
	SpotMarket getTarget();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getTarget <em>Target</em>}' reference.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Earliest ETA</em>' attribute.
	 * @see #setEarliestETA(ZonedDateTime)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResult_EarliestETA()
	 * @model dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getEarliestETA();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getEarliestETA <em>Earliest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Earliest ETA</em>' attribute.
	 * @see #getEarliestETA()
	 * @generated
	 */
	void setEarliestETA(ZonedDateTime value);

	/**
	 * Returns the value of the '<em><b>Latest ETA</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Latest ETA</em>' attribute.
	 * @see #setLatestETA(ZonedDateTime)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResult_LatestETA()
	 * @model dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getLatestETA();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getLatestETA <em>Latest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Latest ETA</em>' attribute.
	 * @see #getLatestETA()
	 * @generated
	 */
	void setLatestETA(ZonedDateTime value);

	/**
	 * Returns the value of the '<em><b>Earliest Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Earliest Volume</em>' attribute.
	 * @see #setEarliestVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResult_EarliestVolume()
	 * @model unique="false"
	 * @generated
	 */
	int getEarliestVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getEarliestVolume <em>Earliest Volume</em>}' attribute.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Latest Volume</em>' attribute.
	 * @see #setLatestVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResult_LatestVolume()
	 * @model unique="false"
	 * @generated
	 */
	int getLatestVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getLatestVolume <em>Latest Volume</em>}' attribute.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Earliest Price</em>' attribute.
	 * @see #setEarliestPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResult_EarliestPrice()
	 * @model unique="false"
	 * @generated
	 */
	double getEarliestPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getEarliestPrice <em>Earliest Price</em>}' attribute.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Latest Price</em>' attribute.
	 * @see #setLatestPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResult_LatestPrice()
	 * @model unique="false"
	 * @generated
	 */
	double getLatestPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getLatestPrice <em>Latest Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Latest Price</em>' attribute.
	 * @see #getLatestPrice()
	 * @generated
	 */
	void setLatestPrice(double value);

} // MarketabilityResult
