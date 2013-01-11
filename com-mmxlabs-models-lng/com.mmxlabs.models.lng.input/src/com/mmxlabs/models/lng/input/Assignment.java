/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input;
import com.mmxlabs.models.lng.types.AVesselSet;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assignment</b></em>'.
 * @deprecated Use {@link ElementAssignment}
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.input.Assignment#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.Assignment#isAssignToSpot <em>Assign To Spot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.Assignment#getAssignedObjects <em>Assigned Objects</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.input.InputPackage#getAssignment()
 * @model
 * @generated
 */
public interface Assignment extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.input.InputPackage#getAssignment_Vessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet> getVessels();

	/**
	 * Returns the value of the '<em><b>Assign To Spot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assign To Spot</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assign To Spot</em>' attribute.
	 * @see #setAssignToSpot(boolean)
	 * @see com.mmxlabs.models.lng.input.InputPackage#getAssignment_AssignToSpot()
	 * @model required="true"
	 * @generated
	 */
	boolean isAssignToSpot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.input.Assignment#isAssignToSpot <em>Assign To Spot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assign To Spot</em>' attribute.
	 * @see #isAssignToSpot()
	 * @generated
	 */
	void setAssignToSpot(boolean value);

	/**
	 * Returns the value of the '<em><b>Assigned Objects</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.mmxcore.UUIDObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assigned Objects</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assigned Objects</em>' reference list.
	 * @see com.mmxlabs.models.lng.input.InputPackage#getAssignment_AssignedObjects()
	 * @model
	 * @generated
	 */
	EList<UUIDObject> getAssignedObjects();

} // end of  Assignment

// finish type fixing
