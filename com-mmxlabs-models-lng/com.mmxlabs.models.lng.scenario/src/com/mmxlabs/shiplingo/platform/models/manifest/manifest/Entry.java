/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.manifest;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry#getSubModelKey <em>Sub Model Key</em>}</li>
 *   <li>{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry#getRelativePath <em>Relative Path</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.ManifestPackage#getEntry()
 * @model
 * @generated
 */
public interface Entry extends EObject {
	/**
	 * Returns the value of the '<em><b>Sub Model Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Model Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Model Key</em>' attribute.
	 * @see #setSubModelKey(String)
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.ManifestPackage#getEntry_SubModelKey()
	 * @model required="true"
	 * @generated
	 */
	String getSubModelKey();

	/**
	 * Sets the value of the '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry#getSubModelKey <em>Sub Model Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sub Model Key</em>' attribute.
	 * @see #getSubModelKey()
	 * @generated
	 */
	void setSubModelKey(String value);

	/**
	 * Returns the value of the '<em><b>Relative Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Relative Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Relative Path</em>' attribute.
	 * @see #setRelativePath(String)
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.ManifestPackage#getEntry_RelativePath()
	 * @model required="true"
	 * @generated
	 */
	String getRelativePath();

	/**
	 * Sets the value of the '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry#getRelativePath <em>Relative Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Relative Path</em>' attribute.
	 * @see #getRelativePath()
	 * @generated
	 */
	void setRelativePath(String value);

} // Entry
