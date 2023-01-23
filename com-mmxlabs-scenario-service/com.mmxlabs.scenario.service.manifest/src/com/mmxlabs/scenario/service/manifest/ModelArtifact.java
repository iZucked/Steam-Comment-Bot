/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.scenario.service.manifest;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getKey <em>Key</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getStorageType <em>Storage Type</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getPath <em>Path</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getDataVersion <em>Data Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getDisplayName <em>Display Name</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getModelArtifact()
 * @model
 * @generated
 */
public interface ModelArtifact extends EObject {
	/**
	 * Returns the value of the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key</em>' attribute.
	 * @see #setKey(String)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getModelArtifact_Key()
	 * @model
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getKey <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

	/**
	 * Returns the value of the '<em><b>Storage Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.scenario.service.manifest.StorageType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Storage Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Storage Type</em>' attribute.
	 * @see com.mmxlabs.scenario.service.manifest.StorageType
	 * @see #setStorageType(StorageType)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getModelArtifact_StorageType()
	 * @model
	 * @generated
	 */
	StorageType getStorageType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getStorageType <em>Storage Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Storage Type</em>' attribute.
	 * @see com.mmxlabs.scenario.service.manifest.StorageType
	 * @see #getStorageType()
	 * @generated
	 */
	void setStorageType(StorageType value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getModelArtifact_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getModelArtifact_Path()
	 * @model
	 * @generated
	 */
	String getPath();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getPath <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
	void setPath(String value);

	/**
	 * Returns the value of the '<em><b>Data Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Version</em>' attribute.
	 * @see #setDataVersion(String)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getModelArtifact_DataVersion()
	 * @model
	 * @generated
	 */
	String getDataVersion();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getDataVersion <em>Data Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Version</em>' attribute.
	 * @see #getDataVersion()
	 * @generated
	 */
	void setDataVersion(String value);

	/**
	 * Returns the value of the '<em><b>Display Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Display Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Display Name</em>' attribute.
	 * @see #setDisplayName(String)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getModelArtifact_DisplayName()
	 * @model
	 * @generated
	 */
	String getDisplayName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getDisplayName <em>Display Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Display Name</em>' attribute.
	 * @see #getDisplayName()
	 * @generated
	 */
	void setDisplayName(String value);

} // ModelArtifact
