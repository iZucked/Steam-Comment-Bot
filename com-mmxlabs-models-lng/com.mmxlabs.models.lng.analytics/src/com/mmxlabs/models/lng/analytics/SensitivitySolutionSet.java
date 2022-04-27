/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sensitivity Solution Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SensitivitySolutionSet#getPorfolioPnLResult <em>Porfolio Pn LResult</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SensitivitySolutionSet#getCargoPnLResults <em>Cargo Pn LResults</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSensitivitySolutionSet()
 * @model
 * @generated
 */
public interface SensitivitySolutionSet extends AbstractSolutionSet {
	/**
	 * Returns the value of the '<em><b>Porfolio Pn LResult</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Porfolio Pn LResult</em>' containment reference.
	 * @see #setPorfolioPnLResult(PortfolioSensitivityResult)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSensitivitySolutionSet_PorfolioPnLResult()
	 * @model containment="true"
	 * @generated
	 */
	PortfolioSensitivityResult getPorfolioPnLResult();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SensitivitySolutionSet#getPorfolioPnLResult <em>Porfolio Pn LResult</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Porfolio Pn LResult</em>' containment reference.
	 * @see #getPorfolioPnLResult()
	 * @generated
	 */
	void setPorfolioPnLResult(PortfolioSensitivityResult value);

	/**
	 * Returns the value of the '<em><b>Cargo Pn LResults</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.CargoPnLResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Pn LResults</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSensitivitySolutionSet_CargoPnLResults()
	 * @model containment="true"
	 * @generated
	 */
	EList<CargoPnLResult> getCargoPnLResults();

} // SensitivitySolutionSet
