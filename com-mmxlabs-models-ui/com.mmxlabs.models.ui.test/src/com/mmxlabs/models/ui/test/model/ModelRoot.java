/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.test.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.test.model.ModelRoot#getSimpleObjects <em>Simple Objects</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.ModelRoot#getSingleReferences <em>Single References</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.ModelRoot#getMultipleReferences <em>Multiple References</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.ModelRoot#getSingleContainmentReferences <em>Single Containment References</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.ModelRoot#getMultipleContainmentReferences <em>Multiple Containment References</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getModelRoot()
 * @model
 * @generated
 */
public interface ModelRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Simple Objects</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.ui.test.model.SimpleObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Simple Objects</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Simple Objects</em>' containment reference list.
	 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getModelRoot_SimpleObjects()
	 * @model containment="true"
	 * @generated
	 */
	EList<SimpleObject> getSimpleObjects();

	/**
	 * Returns the value of the '<em><b>Single References</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.ui.test.model.SingleReference}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.ui.test.model.SingleReference#getModelRoot <em>Model Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single References</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single References</em>' containment reference list.
	 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getModelRoot_SingleReferences()
	 * @see com.mmxlabs.models.ui.test.model.SingleReference#getModelRoot
	 * @model opposite="modelRoot" containment="true"
	 * @generated
	 */
	EList<SingleReference> getSingleReferences();

	/**
	 * Returns the value of the '<em><b>Multiple References</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.ui.test.model.MultipleReference}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.ui.test.model.MultipleReference#getModelRoot <em>Model Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiple References</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiple References</em>' containment reference list.
	 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getModelRoot_MultipleReferences()
	 * @see com.mmxlabs.models.ui.test.model.MultipleReference#getModelRoot
	 * @model opposite="modelRoot" containment="true"
	 * @generated
	 */
	EList<MultipleReference> getMultipleReferences();

	/**
	 * Returns the value of the '<em><b>Single Containment References</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.ui.test.model.SingleContainmentReference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single Containment References</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single Containment References</em>' containment reference list.
	 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getModelRoot_SingleContainmentReferences()
	 * @model containment="true"
	 * @generated
	 */
	EList<SingleContainmentReference> getSingleContainmentReferences();

	/**
	 * Returns the value of the '<em><b>Multiple Containment References</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.ui.test.model.MultipleContainmentReference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiple Containment References</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiple Containment References</em>' containment reference list.
	 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getModelRoot_MultipleContainmentReferences()
	 * @model containment="true"
	 * @generated
	 */
	EList<MultipleContainmentReference> getMultipleContainmentReferences();

} // ModelRoot
