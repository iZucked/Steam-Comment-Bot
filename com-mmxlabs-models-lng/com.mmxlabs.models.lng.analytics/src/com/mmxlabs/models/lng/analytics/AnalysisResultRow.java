/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Analysis Result Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getBuyOption <em>Buy Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getSellOption <em>Sell Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getResultDetail <em>Result Detail</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getResultDetails <em>Result Details</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalysisResultRow()
 * @model
 * @generated
 */
public interface AnalysisResultRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Buy Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Buy Option</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buy Option</em>' reference.
	 * @see #setBuyOption(BuyOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalysisResultRow_BuyOption()
	 * @model
	 * @generated
	 */
	BuyOption getBuyOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getBuyOption <em>Buy Option</em>}' reference.
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
	 * If the meaning of the '<em>Sell Option</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sell Option</em>' reference.
	 * @see #setSellOption(SellOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalysisResultRow_SellOption()
	 * @model
	 * @generated
	 */
	SellOption getSellOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getSellOption <em>Sell Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sell Option</em>' reference.
	 * @see #getSellOption()
	 * @generated
	 */
	void setSellOption(SellOption value);

	/**
	 * Returns the value of the '<em><b>Result Detail</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Result Detail</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result Detail</em>' containment reference.
	 * @see #setResultDetail(AnalysisResultDetail)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalysisResultRow_ResultDetail()
	 * @model containment="true"
	 * @generated
	 */
	AnalysisResultDetail getResultDetail();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getResultDetail <em>Result Detail</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result Detail</em>' containment reference.
	 * @see #getResultDetail()
	 * @generated
	 */
	void setResultDetail(AnalysisResultDetail value);

	/**
	 * Returns the value of the '<em><b>Shipping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping</em>' reference.
	 * @see #setShipping(ShippingOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalysisResultRow_Shipping()
	 * @model
	 * @generated
	 */
	ShippingOption getShipping();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getShipping <em>Shipping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping</em>' reference.
	 * @see #getShipping()
	 * @generated
	 */
	void setShipping(ShippingOption value);

	/**
	 * Returns the value of the '<em><b>Result Details</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Result Details</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result Details</em>' containment reference.
	 * @see #setResultDetails(ResultContainer)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalysisResultRow_ResultDetails()
	 * @model containment="true"
	 * @generated
	 */
	ResultContainer getResultDetails();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getResultDetails <em>Result Details</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result Details</em>' containment reference.
	 * @see #getResultDetails()
	 * @generated
	 */
	void setResultDetails(ResultContainer value);

} // AnalysisResultRow
