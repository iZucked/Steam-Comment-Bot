/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.test.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multiple Containment Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.test.model.MultipleContainmentReference#getSimpleObjects <em>Simple Objects</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getMultipleContainmentReference()
 * @model
 * @generated
 */
public interface MultipleContainmentReference extends EObject {

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
	 * @see com.mmxlabs.models.ui.test.model.ModelPackage#getMultipleContainmentReference_SimpleObjects()
	 * @model containment="true"
	 * @generated
	 */
	EList<SimpleObject> getSimpleObjects();
} // MultipleContainmentReference
