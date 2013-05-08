/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MMX Sub Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXSubModel#getSubModelInstance <em>Sub Model Instance</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXSubModel#getOriginalResource <em>Original Resource</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXSubModel()
 * @model
 * @generated
 */
public interface MMXSubModel extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Sub Model Instance</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Model Instance</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Model Instance</em>' reference.
	 * @see #setSubModelInstance(UUIDObject)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXSubModel_SubModelInstance()
	 * @model required="true"
	 * @generated
	 */
	UUIDObject getSubModelInstance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.MMXSubModel#getSubModelInstance <em>Sub Model Instance</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sub Model Instance</em>' reference.
	 * @see #getSubModelInstance()
	 * @generated
	 */
	void setSubModelInstance(UUIDObject value);

	/**
	 * Returns the value of the '<em><b>Original Resource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Resource</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Resource</em>' attribute.
	 * @see #setOriginalResource(Resource)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXSubModel_OriginalResource()
	 * @model dataType="com.mmxlabs.models.mmxcore.MMXResource" required="true"
	 * @generated
	 */
	Resource getOriginalResource();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.MMXSubModel#getOriginalResource <em>Original Resource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Resource</em>' attribute.
	 * @see #getOriginalResource()
	 * @generated
	 */
	void setOriginalResource(Resource value);

} // MMXSubModel
