

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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element Assignment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.input.ElementAssignment#getAssignedObject <em>Assigned Object</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.ElementAssignment#getAssignment <em>Assignment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.ElementAssignment#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.ElementAssignment#getNextAssignment <em>Next Assignment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.ElementAssignment#getSequence <em>Sequence</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.ElementAssignment#getSpotIndex <em>Spot Index</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.input.InputPackage#getElementAssignment()
 * @model
 * @generated
 */
public interface ElementAssignment extends MMXObject {
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
	 * @see com.mmxlabs.models.lng.input.InputPackage#getElementAssignment_AssignedObject()
	 * @model required="true"
	 * @generated
	 */
	UUIDObject getAssignedObject();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.input.ElementAssignment#getAssignedObject <em>Assigned Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assigned Object</em>' reference.
	 * @see #getAssignedObject()
	 * @generated
	 */
	void setAssignedObject(UUIDObject value);

	/**
	 * Returns the value of the '<em><b>Assignment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assignment</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assignment</em>' reference.
	 * @see #setAssignment(AVesselSet)
	 * @see com.mmxlabs.models.lng.input.InputPackage#getElementAssignment_Assignment()
	 * @model
	 * @generated
	 */
	AVesselSet getAssignment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.input.ElementAssignment#getAssignment <em>Assignment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assignment</em>' reference.
	 * @see #getAssignment()
	 * @generated
	 */
	void setAssignment(AVesselSet value);

	/**
	 * Returns the value of the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Locked</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Locked</em>' attribute.
	 * @see #setLocked(boolean)
	 * @see com.mmxlabs.models.lng.input.InputPackage#getElementAssignment_Locked()
	 * @model required="true"
	 * @generated
	 */
	boolean isLocked();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.input.ElementAssignment#isLocked <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Locked</em>' attribute.
	 * @see #isLocked()
	 * @generated
	 */
	void setLocked(boolean value);

	/**
	 * Returns the value of the '<em><b>Next Assignment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next Assignment</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next Assignment</em>' reference.
	 * @see #setNextAssignment(ElementAssignment)
	 * @see com.mmxlabs.models.lng.input.InputPackage#getElementAssignment_NextAssignment()
	 * @model
	 * @generated
	 */
	ElementAssignment getNextAssignment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.input.ElementAssignment#getNextAssignment <em>Next Assignment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Assignment</em>' reference.
	 * @see #getNextAssignment()
	 * @generated
	 */
	void setNextAssignment(ElementAssignment value);

	/**
	 * Returns the value of the '<em><b>Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sequence</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sequence</em>' attribute.
	 * @see #setSequence(int)
	 * @see com.mmxlabs.models.lng.input.InputPackage#getElementAssignment_Sequence()
	 * @model required="true"
	 * @generated
	 */
	int getSequence();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.input.ElementAssignment#getSequence <em>Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence</em>' attribute.
	 * @see #getSequence()
	 * @generated
	 */
	void setSequence(int value);

	/**
	 * Returns the value of the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Index</em>' attribute.
	 * @see #setSpotIndex(int)
	 * @see com.mmxlabs.models.lng.input.InputPackage#getElementAssignment_SpotIndex()
	 * @model required="true"
	 * @generated
	 */
	int getSpotIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.input.ElementAssignment#getSpotIndex <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Index</em>' attribute.
	 * @see #getSpotIndex()
	 * @generated
	 */
	void setSpotIndex(int value);

} // end of  ElementAssignment

// finish type fixing
