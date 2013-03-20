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
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXRootObject#getVersion <em>Version</em>}</li>
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
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(int)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXRootObject_Version()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getVersion();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.MMXRootObject#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(int value);

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
	 * @model
	 * @generated
	 */
	void restoreSubModels();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	<T> T getSubModel(Class<T> subModelClass);

} // MMXRootObject
