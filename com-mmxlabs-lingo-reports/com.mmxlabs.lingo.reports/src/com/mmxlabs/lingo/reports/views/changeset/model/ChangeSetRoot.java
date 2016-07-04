/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Set Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot#getChangeSets <em>Change Sets</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRoot()
 * @model
 * @generated
 */
public interface ChangeSetRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Change Sets</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Sets</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Sets</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRoot_ChangeSets()
	 * @model containment="true"
	 * @generated
	 */
	EList<ChangeSet> getChangeSets();

} // ChangeSetRoot
