/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Swap Value Matrix Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getParameters <em>Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapValueMatrixResult <em>Swap Value Matrix Result</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel()
 * @model
 * @generated
 */
public interface SwapValueMatrixModel extends AbstractAnalysisModel {
	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference.
	 * @see #setParameters(SwapValueMatrixParameters)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	SwapValueMatrixParameters getParameters();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getParameters <em>Parameters</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameters</em>' containment reference.
	 * @see #getParameters()
	 * @generated
	 */
	void setParameters(SwapValueMatrixParameters value);

	/**
	 * Returns the value of the '<em><b>Swap Value Matrix Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Value Matrix Result</em>' containment reference.
	 * @see #setSwapValueMatrixResult(SwapValueMatrixResultSet)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_SwapValueMatrixResult()
	 * @model containment="true"
	 * @generated
	 */
	SwapValueMatrixResultSet getSwapValueMatrixResult();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapValueMatrixResult <em>Swap Value Matrix Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Value Matrix Result</em>' containment reference.
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 */
	void setSwapValueMatrixResult(SwapValueMatrixResultSet value);

} // SwapValueMatrixModel
