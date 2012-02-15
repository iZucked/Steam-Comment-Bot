

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.input;
import com.mmxlabs.models.lng.types.AVesselSet;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assignment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.input.Assignment#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.Assignment#isAssignToSpot <em>Assign To Spot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.Assignment#getNextAssignment <em>Next Assignment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.Assignment#getAssignedObject <em>Assigned Object</em>}</li>
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
	 * Returns the value of the '<em><b>Next Assignment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next Assignment</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next Assignment</em>' reference.
	 * @see #setNextAssignment(Assignment)
	 * @see com.mmxlabs.models.lng.input.InputPackage#getAssignment_NextAssignment()
	 * @model required="true"
	 * @generated
	 */
	Assignment getNextAssignment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.input.Assignment#getNextAssignment <em>Next Assignment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Assignment</em>' reference.
	 * @see #getNextAssignment()
	 * @generated
	 */
	void setNextAssignment(Assignment value);

	/**
	 * Returns the value of the '<em><b>Assigned Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assigned Object</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assigned Object</em>' reference.
	 * @see #setAssignedObject(UUIDObject)
	 * @see com.mmxlabs.models.lng.input.InputPackage#getAssignment_AssignedObject()
	 * @model required="true"
	 * @generated
	 */
	UUIDObject getAssignedObject();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.input.Assignment#getAssignedObject <em>Assigned Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assigned Object</em>' reference.
	 * @see #getAssignedObject()
	 * @generated
	 */
	void setAssignedObject(UUIDObject value);

} // end of  Assignment

// finish type fixing
