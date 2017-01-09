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
 * A representation of the model object '<em><b>Partial Case</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.PartialCase#getPartialCase <em>Partial Case</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCase()
 * @model
 * @generated
 */
public interface PartialCase extends EObject {
	/**
	 * Returns the value of the '<em><b>Partial Case</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.PartialCaseRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Partial Case</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Partial Case</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getPartialCase_PartialCase()
	 * @model containment="true"
	 * @generated
	 */
	EList<PartialCaseRow> getPartialCase();

} // PartialCase
