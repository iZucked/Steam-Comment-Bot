/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.test.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Single Containment Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.test.model.SingleContainmentReference#getSimpleObject <em>Simple Object</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getSingleContainmentReference()
 * @model
 * @generated
 */
public interface SingleContainmentReference extends EObject {

	/**
	 * Returns the value of the '<em><b>Simple Object</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Simple Object</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Simple Object</em>' containment reference.
	 * @see #setSimpleObject(SimpleObject)
	 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getSingleContainmentReference_SimpleObject()
	 * @model containment="true"
	 * @generated
	 */
	SimpleObject getSimpleObject();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.ui.test.model.SingleContainmentReference#getSimpleObject <em>Simple Object</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Simple Object</em>' containment reference.
	 * @see #getSimpleObject()
	 * @generated
	 */
	void setSimpleObject(SimpleObject value);
} // SingleContainmentReference
