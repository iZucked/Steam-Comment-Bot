/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.manifest;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Manifest</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest#getEntries <em>Entries</em>}</li>
 *   <li>{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest#getCurrentVersion <em>Current Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.ManifestPackage#getManifest()
 * @model
 * @generated
 */
public interface Manifest extends EObject {
	/**
	 * Returns the value of the '<em><b>Entries</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entries</em>' containment reference list.
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.ManifestPackage#getManifest_Entries()
	 * @model containment="true"
	 * @generated
	 */
	EList<Entry> getEntries();

	/**
	 * Returns the value of the '<em><b>Current Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Version</em>' attribute.
	 * @see #setCurrentVersion(int)
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.ManifestPackage#getManifest_CurrentVersion()
	 * @model required="true"
	 * @generated
	 */
	int getCurrentVersion();

	/**
	 * Sets the value of the '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest#getCurrentVersion <em>Current Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Version</em>' attribute.
	 * @see #getCurrentVersion()
	 * @generated
	 */
	void setCurrentVersion(int value);

} // Manifest
