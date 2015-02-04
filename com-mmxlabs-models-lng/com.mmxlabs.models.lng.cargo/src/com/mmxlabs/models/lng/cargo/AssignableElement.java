/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.lng.types.AVesselSet;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assignable Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.AssignableElement#getAssignment <em>Assignment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.AssignableElement#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.AssignableElement#getSequenceHint <em>Sequence Hint</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.AssignableElement#isLocked <em>Locked</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getAssignableElement()
 * @model abstract="true"
 * @generated
 */
public interface AssignableElement extends EObject {
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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getAssignableElement_Assignment()
	 * @model
	 * @generated
	 */
	AVesselSet<? extends Vessel> getAssignment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.AssignableElement#getAssignment <em>Assignment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assignment</em>' reference.
	 * @see #getAssignment()
	 * @generated
	 */
	void setAssignment(AVesselSet<? extends Vessel> value);

	/**
	 * Returns the value of the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Index</em>' attribute.
	 * @see #isSetSpotIndex()
	 * @see #unsetSpotIndex()
	 * @see #setSpotIndex(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getAssignableElement_SpotIndex()
	 * @model unsettable="true"
	 * @generated
	 */
	int getSpotIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.AssignableElement#getSpotIndex <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Index</em>' attribute.
	 * @see #isSetSpotIndex()
	 * @see #unsetSpotIndex()
	 * @see #getSpotIndex()
	 * @generated
	 */
	void setSpotIndex(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.AssignableElement#getSpotIndex <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSpotIndex()
	 * @see #getSpotIndex()
	 * @see #setSpotIndex(int)
	 * @generated
	 */
	void unsetSpotIndex();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.AssignableElement#getSpotIndex <em>Spot Index</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Spot Index</em>' attribute is set.
	 * @see #unsetSpotIndex()
	 * @see #getSpotIndex()
	 * @see #setSpotIndex(int)
	 * @generated
	 */
	boolean isSetSpotIndex();

	/**
	 * Returns the value of the '<em><b>Sequence Hint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sequence Hint</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sequence Hint</em>' attribute.
	 * @see #setSequenceHint(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getAssignableElement_SequenceHint()
	 * @model
	 * @generated
	 */
	int getSequenceHint();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.AssignableElement#getSequenceHint <em>Sequence Hint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence Hint</em>' attribute.
	 * @see #getSequenceHint()
	 * @generated
	 */
	void setSequenceHint(int value);

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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getAssignableElement_Locked()
	 * @model
	 * @generated
	 */
	boolean isLocked();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.AssignableElement#isLocked <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Locked</em>' attribute.
	 * @see #isLocked()
	 * @generated
	 */
	void setLocked(boolean value);

} // AssignableElement
