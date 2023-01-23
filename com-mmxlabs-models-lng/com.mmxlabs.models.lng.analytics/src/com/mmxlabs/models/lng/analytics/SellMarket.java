/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.UUIDObject;
import java.time.YearMonth;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sell Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SellMarket#getMarket <em>Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SellMarket#getMonth <em>Month</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSellMarket()
 * @model
 * @generated
 */
public interface SellMarket extends UUIDObject, SellOption {
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSellMarket_Market()
	 * @model
	 * @generated
	 */
	SpotMarket getMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SellMarket#getMarket <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market</em>' reference.
	 * @see #getMarket()
	 * @generated
	 */
	void setMarket(SpotMarket value);

	/**
	 * Returns the value of the '<em><b>Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Month</em>' attribute.
	 * @see #isSetMonth()
	 * @see #unsetMonth()
	 * @see #setMonth(YearMonth)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSellMarket_Month()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getMonth();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SellMarket#getMonth <em>Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Month</em>' attribute.
	 * @see #isSetMonth()
	 * @see #unsetMonth()
	 * @see #getMonth()
	 * @generated
	 */
	void setMonth(YearMonth value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.analytics.SellMarket#getMonth <em>Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMonth()
	 * @see #getMonth()
	 * @see #setMonth(YearMonth)
	 * @generated
	 */
	void unsetMonth();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.analytics.SellMarket#getMonth <em>Month</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Month</em>' attribute is set.
	 * @see #unsetMonth()
	 * @see #getMonth()
	 * @see #setMonth(YearMonth)
	 * @generated
	 */
	boolean isSetMonth();

} // SellMarket
