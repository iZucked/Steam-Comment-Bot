/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Set Table Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot#getGroups <em>Groups</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRoot()
 * @model
 * @generated
 */
public interface ChangeSetTableRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Groups</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRoot_Groups()
	 * @model containment="true"
	 * @generated
	 */
	EList<ChangeSetTableGroup> getGroups();

} // ChangeSetTableRoot
