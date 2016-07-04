/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.test.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Single Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.test.model.SingleReference#getMultipleReference <em>Multiple Reference</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.SingleReference#getModelRoot <em>Model Root</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getSingleReference()
 * @model
 * @generated
 */
public interface SingleReference extends EObject {
	/**
	 * Returns the value of the '<em><b>Multiple Reference</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.ui.test.model.MultipleReference#getSingleReferences <em>Single References</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiple Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiple Reference</em>' reference.
	 * @see #setMultipleReference(MultipleReference)
	 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getSingleReference_MultipleReference()
	 * @see com.mmxlabs.models.ui.test.model.MultipleReference#getSingleReferences
	 * @model opposite="singleReferences"
	 * @generated
	 */
	MultipleReference getMultipleReference();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.ui.test.model.SingleReference#getMultipleReference <em>Multiple Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiple Reference</em>' reference.
	 * @see #getMultipleReference()
	 * @generated
	 */
	void setMultipleReference(MultipleReference value);

	/**
	 * Returns the value of the '<em><b>Model Root</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.ui.test.model.ModelRoot#getSingleReferences <em>Single References</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Root</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Root</em>' container reference.
	 * @see #setModelRoot(ModelRoot)
	 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getSingleReference_ModelRoot()
	 * @see com.mmxlabs.models.ui.test.model.ModelRoot#getSingleReferences
	 * @model opposite="singleReferences" transient="false"
	 * @generated
	 */
	ModelRoot getModelRoot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.ui.test.model.SingleReference#getModelRoot <em>Model Root</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Root</em>' container reference.
	 * @see #getModelRoot()
	 * @generated
	 */
	void setModelRoot(ModelRoot value);

} // SingleReference
