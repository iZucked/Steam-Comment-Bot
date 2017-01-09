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
 * A representation of the model object '<em><b>Multiple Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.test.model.MultipleReference#getSingleReferences <em>Single References</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.MultipleReference#getModelRoot <em>Model Root</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getMultipleReference()
 * @model
 * @generated
 */
public interface MultipleReference extends EObject {
	/**
	 * Returns the value of the '<em><b>Single References</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.ui.test.model.SingleReference}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.ui.test.model.SingleReference#getMultipleReference <em>Multiple Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single References</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single References</em>' reference list.
	 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getMultipleReference_SingleReferences()
	 * @see com.mmxlabs.models.ui.test.model.SingleReference#getMultipleReference
	 * @model opposite="multipleReference"
	 * @generated
	 */
	EList<SingleReference> getSingleReferences();

	/**
	 * Returns the value of the '<em><b>Model Root</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.ui.test.model.ModelRoot#getMultipleReferences <em>Multiple References</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Root</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Root</em>' container reference.
	 * @see #setModelRoot(ModelRoot)
	 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getMultipleReference_ModelRoot()
	 * @see com.mmxlabs.models.ui.test.model.ModelRoot#getMultipleReferences
	 * @model opposite="multipleReferences" transient="false"
	 * @generated
	 */
	ModelRoot getModelRoot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.ui.test.model.MultipleReference#getModelRoot <em>Model Root</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Root</em>' container reference.
	 * @see #getModelRoot()
	 * @generated
	 */
	void setModelRoot(ModelRoot value);

} // MultipleReference
