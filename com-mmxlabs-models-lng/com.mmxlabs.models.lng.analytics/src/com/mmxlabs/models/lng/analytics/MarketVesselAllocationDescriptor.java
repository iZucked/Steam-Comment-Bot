/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Market Vessel Allocation Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor#getMarketName <em>Market Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor#getSpotIndex <em>Spot Index</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketVesselAllocationDescriptor()
 * @model
 * @generated
 */
public interface MarketVesselAllocationDescriptor extends VesselAllocationDescriptor {
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketVesselAllocationDescriptor_MarketName()
	 * @model
	 * @generated
	 */
	String getMarketName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor#getMarketName <em>Market Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Name</em>' attribute.
	 * @see #getMarketName()
	 * @generated
	 */
	void setMarketName(String value);

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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketVesselAllocationDescriptor_SpotIndex()
	 * @model
	 * @generated
	 */
	int getSpotIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor#getSpotIndex <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Index</em>' attribute.
	 * @see #getSpotIndex()
	 * @generated
	 */
	void setSpotIndex(int value);

} // MarketVesselAllocationDescriptor
