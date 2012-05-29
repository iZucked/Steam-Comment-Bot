/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.input.InputModel#getAssignments <em>Assignments</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.InputModel#getLockedAssignedObjects <em>Locked Assigned Objects</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.input.InputPackage#getInputModel()
 * @model
 * @generated
 */
public interface InputModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Assignments</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.input.Assignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assignments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assignments</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.input.InputPackage#getInputModel_Assignments()
	 * @model containment="true"
	 * @generated
	 */
	EList<Assignment> getAssignments();

	/**
	 * Returns the value of the '<em><b>Locked Assigned Objects</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.mmxcore.UUIDObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Locked Assigned Objects</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Locked Assigned Objects</em>' reference list.
	 * @see com.mmxlabs.models.lng.input.InputPackage#getInputModel_LockedAssignedObjects()
	 * @model
	 * @generated
	 */
	EList<UUIDObject> getLockedAssignedObjects();

} // end of  InputModel

// finish type fixing
