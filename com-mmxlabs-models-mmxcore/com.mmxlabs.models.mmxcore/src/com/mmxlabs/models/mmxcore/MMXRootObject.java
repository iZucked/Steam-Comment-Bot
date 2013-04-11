/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MMX Root Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXRootObject#getSubModels <em>Sub Models</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXRootObject()
 * @model
 * @generated
 */
public interface MMXRootObject extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Sub Models</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.mmxcore.MMXSubModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Models</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Models</em>' containment reference list.
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXRootObject_SubModels()
	 * @model containment="true" transient="true"
	 * @generated
	 */
	EList<MMXSubModel> getSubModels();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model subModelRequired="true"
	 * @generated
	 */
	void addSubModel(UUIDObject subModel);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	<T> T getSubModel(Class<T> subModelClass);

} // MMXRootObject
