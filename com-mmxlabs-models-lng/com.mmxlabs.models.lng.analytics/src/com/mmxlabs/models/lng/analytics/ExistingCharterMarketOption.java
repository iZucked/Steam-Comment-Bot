/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Existing Charter Market Option</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption#getCharterInMarket <em>Charter In Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption#getSpotIndex <em>Spot Index</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getExistingCharterMarketOption()
 * @model
 * @generated
 */
public interface ExistingCharterMarketOption extends UUIDObject, ShippingOption {
	/**
	 * Returns the value of the '<em><b>Charter In Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter In Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter In Market</em>' reference.
	 * @see #setCharterInMarket(CharterInMarket)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getExistingCharterMarketOption_CharterInMarket()
	 * @model
	 * @generated
	 */
	CharterInMarket getCharterInMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption#getCharterInMarket <em>Charter In Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter In Market</em>' reference.
	 * @see #getCharterInMarket()
	 * @generated
	 */
	void setCharterInMarket(CharterInMarket value);

	/**
	 * Returns the value of the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Index</em>' attribute.
	 * @see #setSpotIndex(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getExistingCharterMarketOption_SpotIndex()
	 * @model
	 * @generated
	 */
	int getSpotIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption#getSpotIndex <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Index</em>' attribute.
	 * @see #getSpotIndex()
	 * @generated
	 */
	void setSpotIndex(int value);

} // ExistingCharterMarketOption
