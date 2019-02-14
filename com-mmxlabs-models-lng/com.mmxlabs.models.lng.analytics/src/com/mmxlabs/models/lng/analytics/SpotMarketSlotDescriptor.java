/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import java.time.YearMonth;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spot Market Slot Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getMarketName <em>Market Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getPortName <em>Port Name</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSpotMarketSlotDescriptor()
 * @model
 * @generated
 */
public interface SpotMarketSlotDescriptor extends SlotDescriptor {
	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(YearMonth)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSpotMarketSlotDescriptor_Date()
	 * @model dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(YearMonth value);

	/**
	 * Returns the value of the '<em><b>Market Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Name</em>' attribute.
	 * @see #setMarketName(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSpotMarketSlotDescriptor_MarketName()
	 * @model
	 * @generated
	 */
	String getMarketName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getMarketName <em>Market Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Name</em>' attribute.
	 * @see #getMarketName()
	 * @generated
	 */
	void setMarketName(String value);

	/**
	 * Returns the value of the '<em><b>Port Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Name</em>' attribute.
	 * @see #setPortName(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSpotMarketSlotDescriptor_PortName()
	 * @model
	 * @generated
	 */
	String getPortName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getPortName <em>Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Name</em>' attribute.
	 * @see #getPortName()
	 * @generated
	 */
	void setPortName(String value);

} // SpotMarketSlotDescriptor
