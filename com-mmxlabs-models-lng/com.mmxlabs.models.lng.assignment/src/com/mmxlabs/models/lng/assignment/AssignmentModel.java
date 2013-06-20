/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */


/**
 */
package com.mmxlabs.models.lng.assignment;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.assignment.AssignmentModel#getElementAssignments <em>Element Assignments</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.assignment.AssignmentPackage#getAssignmentModel()
 * @model
 * @generated
 */
public interface AssignmentModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Element Assignments</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.assignment.ElementAssignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element Assignments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element Assignments</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.assignment.AssignmentPackage#getAssignmentModel_ElementAssignments()
	 * @model containment="true"
	 * @generated
	 */
	EList<ElementAssignment> getElementAssignments();

} // AssignmentModel

// finish type fixing

