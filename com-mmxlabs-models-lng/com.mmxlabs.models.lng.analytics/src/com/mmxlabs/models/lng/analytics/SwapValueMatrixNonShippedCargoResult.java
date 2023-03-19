/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Swap Value Matrix Non Shipped Cargo Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixNonShippedCargoResult#getVolume <em>Volume</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixNonShippedCargoResult()
 * @model
 * @generated
 */
public interface SwapValueMatrixNonShippedCargoResult extends SwapValueMatrixCargoResult {
	/**
	 * Returns the value of the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume</em>' attribute.
	 * @see #setVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixNonShippedCargoResult_Volume()
	 * @model
	 * @generated
	 */
	int getVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixNonShippedCargoResult#getVolume <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume</em>' attribute.
	 * @see #getVolume()
	 * @generated
	 */
	void setVolume(int value);

} // SwapValueMatrixNonShippedCargoResult
