/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sensitivity Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SensitivityModel#getSensitivityModel <em>Sensitivity Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SensitivityModel#getSensitivitySolution <em>Sensitivity Solution</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSensitivityModel()
 * @model
 * @generated
 */
public interface SensitivityModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Sensitivity Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sensitivity Model</em>' containment reference.
	 * @see #setSensitivityModel(OptionAnalysisModel)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSensitivityModel_SensitivityModel()
	 * @model containment="true"
	 * @generated
	 */
	OptionAnalysisModel getSensitivityModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SensitivityModel#getSensitivityModel <em>Sensitivity Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sensitivity Model</em>' containment reference.
	 * @see #getSensitivityModel()
	 * @generated
	 */
	void setSensitivityModel(OptionAnalysisModel value);

	/**
	 * Returns the value of the '<em><b>Sensitivity Solution</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sensitivity Solution</em>' containment reference.
	 * @see #setSensitivitySolution(SensitivitySolutionSet)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSensitivityModel_SensitivitySolution()
	 * @model containment="true"
	 * @generated
	 */
	SensitivitySolutionSet getSensitivitySolution();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SensitivityModel#getSensitivitySolution <em>Sensitivity Solution</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sensitivity Solution</em>' containment reference.
	 * @see #getSensitivitySolution()
	 * @generated
	 */
	void setSensitivitySolution(SensitivitySolutionSet value);

} // SensitivityModel
