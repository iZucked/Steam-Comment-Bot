/**
 */
package com.mmxlabs.models.lng.analytics;

import java.time.LocalDate;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Marketability Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getBuyOption <em>Buy Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getSellOption <em>Sell Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getLhsResults <em>Lhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getRhsResults <em>Rhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getEta <em>Eta</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getReferencePrice <em>Reference Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getStartVolume <em>Start Volume</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow()
 * @model
 * @generated
 */
public interface MarketabilityRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Buy Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buy Option</em>' reference.
	 * @see #setBuyOption(BuyOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_BuyOption()
	 * @model
	 * @generated
	 */
	BuyOption getBuyOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getBuyOption <em>Buy Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Buy Option</em>' reference.
	 * @see #getBuyOption()
	 * @generated
	 */
	void setBuyOption(BuyOption value);

	/**
	 * Returns the value of the '<em><b>Sell Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sell Option</em>' reference.
	 * @see #setSellOption(SellOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_SellOption()
	 * @model
	 * @generated
	 */
	SellOption getSellOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getSellOption <em>Sell Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sell Option</em>' reference.
	 * @see #getSellOption()
	 * @generated
	 */
	void setSellOption(SellOption value);

	/**
	 * Returns the value of the '<em><b>Shipping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping</em>' reference.
	 * @see #setShipping(ShippingOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_Shipping()
	 * @model
	 * @generated
	 */
	ShippingOption getShipping();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getShipping <em>Shipping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping</em>' reference.
	 * @see #getShipping()
	 * @generated
	 */
	void setShipping(ShippingOption value);

	/**
	 * Returns the value of the '<em><b>Lhs Results</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.MarketabilityResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Results</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_LhsResults()
	 * @model containment="true"
	 * @generated
	 */
	EList<MarketabilityResult> getLhsResults();

	/**
	 * Returns the value of the '<em><b>Rhs Results</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.MarketabilityResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Results</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_RhsResults()
	 * @model containment="true"
	 * @generated
	 */
	EList<MarketabilityResult> getRhsResults();

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(EObject)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_Target()
	 * @model
	 * @generated
	 */
	EObject getTarget();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(EObject value);

	/**
	 * Returns the value of the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price</em>' attribute.
	 * @see #setPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_Price()
	 * @model
	 * @generated
	 */
	double getPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getPrice <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price</em>' attribute.
	 * @see #getPrice()
	 * @generated
	 */
	void setPrice(double value);

	/**
	 * Returns the value of the '<em><b>Eta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Eta</em>' attribute.
	 * @see #setEta(LocalDate)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_Eta()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getEta();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getEta <em>Eta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Eta</em>' attribute.
	 * @see #getEta()
	 * @generated
	 */
	void setEta(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Reference Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference Price</em>' attribute.
	 * @see #setReferencePrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_ReferencePrice()
	 * @model
	 * @generated
	 */
	double getReferencePrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getReferencePrice <em>Reference Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference Price</em>' attribute.
	 * @see #getReferencePrice()
	 * @generated
	 */
	void setReferencePrice(double value);

	/**
	 * Returns the value of the '<em><b>Start Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Volume</em>' attribute.
	 * @see #setStartVolume(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_StartVolume()
	 * @model unique="false"
	 * @generated
	 */
	long getStartVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getStartVolume <em>Start Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Volume</em>' attribute.
	 * @see #getStartVolume()
	 * @generated
	 */
	void setStartVolume(long value);

} // MarketabilityRow
