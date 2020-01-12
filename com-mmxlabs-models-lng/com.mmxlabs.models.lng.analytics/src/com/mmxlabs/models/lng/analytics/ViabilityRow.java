/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import java.time.LocalDate;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Viability Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getBuyOption <em>Buy Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getSellOption <em>Sell Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getLhsResults <em>Lhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getRhsResults <em>Rhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getEta <em>Eta</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getReferencePrice <em>Reference Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getStartVolume <em>Start Volume</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityRow()
 * @model
 * @generated
 */
public interface ViabilityRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Buy Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Buy Option</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buy Option</em>' reference.
	 * @see #setBuyOption(BuyOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityRow_BuyOption()
	 * @model
	 * @generated
	 */
	BuyOption getBuyOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getBuyOption <em>Buy Option</em>}' reference.
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
	 * <p>
	 * If the meaning of the '<em>Sell Option</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sell Option</em>' reference.
	 * @see #setSellOption(SellOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityRow_SellOption()
	 * @model
	 * @generated
	 */
	SellOption getSellOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getSellOption <em>Sell Option</em>}' reference.
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
	 * <p>
	 * If the meaning of the '<em>Shipping</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping</em>' reference.
	 * @see #setShipping(ShippingOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityRow_Shipping()
	 * @model
	 * @generated
	 */
	ShippingOption getShipping();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getShipping <em>Shipping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping</em>' reference.
	 * @see #getShipping()
	 * @generated
	 */
	void setShipping(ShippingOption value);

	/**
	 * Returns the value of the '<em><b>Lhs Results</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.ViabilityResult}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Results</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Results</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityRow_LhsResults()
	 * @model containment="true"
	 * @generated
	 */
	EList<ViabilityResult> getLhsResults();

	/**
	 * Returns the value of the '<em><b>Rhs Results</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.ViabilityResult}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Results</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Results</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityRow_RhsResults()
	 * @model containment="true"
	 * @generated
	 */
	EList<ViabilityResult> getRhsResults();

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(EObject)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityRow_Target()
	 * @model
	 * @generated
	 */
	EObject getTarget();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getTarget <em>Target</em>}' reference.
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
	 * <p>
	 * If the meaning of the '<em>Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price</em>' attribute.
	 * @see #setPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityRow_Price()
	 * @model
	 * @generated
	 */
	double getPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getPrice <em>Price</em>}' attribute.
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
	 * <p>
	 * If the meaning of the '<em>Eta</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Eta</em>' attribute.
	 * @see #setEta(LocalDate)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityRow_Eta()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getEta();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getEta <em>Eta</em>}' attribute.
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
	 * <p>
	 * If the meaning of the '<em>Reference Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference Price</em>' attribute.
	 * @see #setReferencePrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityRow_ReferencePrice()
	 * @model
	 * @generated
	 */
	double getReferencePrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getReferencePrice <em>Reference Price</em>}' attribute.
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
	 * <p>
	 * If the meaning of the '<em>Start Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Volume</em>' attribute.
	 * @see #setStartVolume(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityRow_StartVolume()
	 * @model unique="false"
	 * @generated
	 */
	long getStartVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getStartVolume <em>Start Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Volume</em>' attribute.
	 * @see #getStartVolume()
	 * @generated
	 */
	void setStartVolume(long value);

} // ViabilityRow
