/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Buy Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyMarket#getMarket <em>Market</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyMarket()
 * @model
 * @generated
 */
public interface BuyMarket extends BuyOption {
	/**
	 * Returns the value of the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market</em>' reference.
	 * @see #setMarket(SpotMarket)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyMarket_Market()
	 * @model
	 * @generated
	 */
	SpotMarket getMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyMarket#getMarket <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market</em>' reference.
	 * @see #getMarket()
	 * @generated
	 */
	void setMarket(SpotMarket value);

} // BuyMarket
