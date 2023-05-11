/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Swap Value Matrix Shipped Cargo Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getShippingCost <em>Shipping Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getLoadVolume <em>Load Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getDischargeVolume <em>Discharge Volume</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixShippedCargoResult()
 * @model
 * @generated
 */
public interface SwapValueMatrixShippedCargoResult extends SwapValueMatrixCargoResult {
	/**
	 * Returns the value of the '<em><b>Shipping Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Cost</em>' attribute.
	 * @see #setShippingCost(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixShippedCargoResult_ShippingCost()
	 * @model
	 * @generated
	 */
	long getShippingCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getShippingCost <em>Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Cost</em>' attribute.
	 * @see #getShippingCost()
	 * @generated
	 */
	void setShippingCost(long value);

	/**
	 * Returns the value of the '<em><b>Load Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Volume</em>' attribute.
	 * @see #setLoadVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixShippedCargoResult_LoadVolume()
	 * @model
	 * @generated
	 */
	int getLoadVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getLoadVolume <em>Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Volume</em>' attribute.
	 * @see #getLoadVolume()
	 * @generated
	 */
	void setLoadVolume(int value);

	/**
	 * Returns the value of the '<em><b>Discharge Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Volume</em>' attribute.
	 * @see #setDischargeVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixShippedCargoResult_DischargeVolume()
	 * @model
	 * @generated
	 */
	int getDischargeVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getDischargeVolume <em>Discharge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Volume</em>' attribute.
	 * @see #getDischargeVolume()
	 * @generated
	 */
	void setDischargeVolume(int value);

} // SwapValueMatrixShippedCargoResult
