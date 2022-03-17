/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Viability Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityModel#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ViabilityModel#getMarkets <em>Markets</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityModel()
 * @model
 * @generated
 */
public interface ViabilityModel extends AbstractAnalysisModel {
	/**
	 * Returns the value of the '<em><b>Rows</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.ViabilityRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rows</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rows</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityModel_Rows()
	 * @model containment="true"
	 * @generated
	 */
	EList<ViabilityRow> getRows();

	/**
	 * Returns the value of the '<em><b>Markets</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.spotmarkets.SpotMarket}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Markets</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Markets</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getViabilityModel_Markets()
	 * @model
	 * @generated
	 */
	EList<SpotMarket> getMarkets();

} // ViabilityModel
