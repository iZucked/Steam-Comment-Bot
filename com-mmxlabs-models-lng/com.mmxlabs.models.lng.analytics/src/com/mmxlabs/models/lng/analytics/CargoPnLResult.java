/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.Cargo;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Pn LResult</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.CargoPnLResult#getCargo <em>Cargo</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCargoPnLResult()
 * @model
 * @generated
 */
public interface CargoPnLResult extends AbstractSensitivityResult {
	/**
	 * Returns the value of the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo</em>' reference.
	 * @see #setCargo(Cargo)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCargoPnLResult_Cargo()
	 * @model
	 * @generated
	 */
	Cargo getCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.CargoPnLResult#getCargo <em>Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo</em>' reference.
	 * @see #getCargo()
	 * @generated
	 */
	void setCargo(Cargo value);

} // CargoPnLResult
