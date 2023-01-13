/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generated Charter Out</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GeneratedCharterOut#getRevenue <em>Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GeneratedCharterOut#getCharterOutMarket <em>Charter Out Market</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGeneratedCharterOut()
 * @model
 * @generated
 */
public interface GeneratedCharterOut extends PortVisit, ProfitAndLossContainer, EventGrouping {
	/**
	 * Returns the value of the '<em><b>Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Revenue</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Revenue</em>' attribute.
	 * @see #setRevenue(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGeneratedCharterOut_Revenue()
	 * @model required="true"
	 * @generated
	 */
	int getRevenue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.GeneratedCharterOut#getRevenue <em>Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Revenue</em>' attribute.
	 * @see #getRevenue()
	 * @generated
	 */
	void setRevenue(int value);

	/**
	 * Returns the value of the '<em><b>Charter Out Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Out Market</em>' reference.
	 * @see #setCharterOutMarket(CharterOutMarket)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGeneratedCharterOut_CharterOutMarket()
	 * @model required="true"
	 * @generated
	 */
	CharterOutMarket getCharterOutMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.GeneratedCharterOut#getCharterOutMarket <em>Charter Out Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Out Market</em>' reference.
	 * @see #getCharterOutMarket()
	 * @generated
	 */
	void setCharterOutMarket(CharterOutMarket value);

} // end of  GeneratedCharterOut

// finish type fixing
