/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Case</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCase#getBaseCase <em>Base Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCase#getProfitAndLoss <em>Profit And Loss</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCase()
 * @model
 * @generated
 */
public interface BaseCase extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Case</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.BaseCaseRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Case</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Case</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCase_BaseCase()
	 * @model containment="true"
	 * @generated
	 */
	EList<BaseCaseRow> getBaseCase();

	/**
	 * Returns the value of the '<em><b>Profit And Loss</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Profit And Loss</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Profit And Loss</em>' attribute.
	 * @see #setProfitAndLoss(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCase_ProfitAndLoss()
	 * @model
	 * @generated
	 */
	long getProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCase#getProfitAndLoss <em>Profit And Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Profit And Loss</em>' attribute.
	 * @see #getProfitAndLoss()
	 * @generated
	 */
	void setProfitAndLoss(long value);

} // BaseCase
