/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Case Row Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRowGroup#getRows <em>Rows</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRowGroup()
 * @model
 * @generated
 */
public interface BaseCaseRowGroup extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Rows</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.BaseCaseRow}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rows</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRowGroup_Rows()
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRow#getGroup
	 * @model opposite="group"
	 * @generated
	 */
	EList<BaseCaseRow> getRows();

} // BaseCaseRowGroup
