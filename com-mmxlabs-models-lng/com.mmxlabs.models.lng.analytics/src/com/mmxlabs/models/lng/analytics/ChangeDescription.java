/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Description</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ChangeDescription#getChanges <em>Changes</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getChangeDescription()
 * @model
 * @generated
 */
public interface ChangeDescription extends EObject {
	/**
	 * Returns the value of the '<em><b>Changes</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.Change}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Changes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Changes</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getChangeDescription_Changes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Change> getChanges();

} // ChangeDescription
