/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.manifest;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Manifest</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.Manifest#getModelURIs <em>Model UR Is</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.Manifest#getDependencyUUIDs <em>Dependency UUI Ds</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.Manifest#getUUID <em>UUID</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.Manifest#getScenarioType <em>Scenario Type</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.Manifest#getScenarioVersion <em>Scenario Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.Manifest#getVersionContext <em>Version Context</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.Manifest#getClientScenarioVersion <em>Client Scenario Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.Manifest#getClientVersionContext <em>Client Version Context</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getManifest()
 * @model
 * @generated
 */
public interface Manifest extends EObject {
	/**
	 * Returns the value of the '<em><b>Model UR Is</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model UR Is</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model UR Is</em>' attribute list.
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getManifest_ModelURIs()
	 * @model
	 * @generated
	 */
	EList<String> getModelURIs();

	/**
	 * Returns the value of the '<em><b>Dependency UUI Ds</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependency UUI Ds</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependency UUI Ds</em>' attribute list.
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getManifest_DependencyUUIDs()
	 * @model
	 * @generated
	 */
	EList<String> getDependencyUUIDs();

	/**
	 * Returns the value of the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>UUID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>UUID</em>' attribute.
	 * @see #setUUID(String)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getManifest_UUID()
	 * @model required="true"
	 * @generated
	 */
	String getUUID();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.Manifest#getUUID <em>UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>UUID</em>' attribute.
	 * @see #getUUID()
	 * @generated
	 */
	void setUUID(String value);

	/**
	 * Returns the value of the '<em><b>Scenario Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenario Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenario Type</em>' attribute.
	 * @see #setScenarioType(String)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getManifest_ScenarioType()
	 * @model required="true"
	 * @generated
	 */
	String getScenarioType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.Manifest#getScenarioType <em>Scenario Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scenario Type</em>' attribute.
	 * @see #getScenarioType()
	 * @generated
	 */
	void setScenarioType(String value);

	/**
	 * Returns the value of the '<em><b>Scenario Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenario Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenario Version</em>' attribute.
	 * @see #setScenarioVersion(int)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getManifest_ScenarioVersion()
	 * @model
	 * @generated
	 */
	int getScenarioVersion();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.Manifest#getScenarioVersion <em>Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scenario Version</em>' attribute.
	 * @see #getScenarioVersion()
	 * @generated
	 */
	void setScenarioVersion(int value);

	/**
	 * Returns the value of the '<em><b>Version Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version Context</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version Context</em>' attribute.
	 * @see #setVersionContext(String)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getManifest_VersionContext()
	 * @model
	 * @generated
	 */
	String getVersionContext();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.Manifest#getVersionContext <em>Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version Context</em>' attribute.
	 * @see #getVersionContext()
	 * @generated
	 */
	void setVersionContext(String value);

	/**
	 * Returns the value of the '<em><b>Client Scenario Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Client Scenario Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Client Scenario Version</em>' attribute.
	 * @see #setClientScenarioVersion(int)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getManifest_ClientScenarioVersion()
	 * @model
	 * @generated
	 */
	int getClientScenarioVersion();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.Manifest#getClientScenarioVersion <em>Client Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Client Scenario Version</em>' attribute.
	 * @see #getClientScenarioVersion()
	 * @generated
	 */
	void setClientScenarioVersion(int value);

	/**
	 * Returns the value of the '<em><b>Client Version Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Client Version Context</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Client Version Context</em>' attribute.
	 * @see #setClientVersionContext(String)
	 * @see com.mmxlabs.scenario.service.manifest.ManifestPackage#getManifest_ClientVersionContext()
	 * @model
	 * @generated
	 */
	String getClientVersionContext();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.manifest.Manifest#getClientVersionContext <em>Client Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Client Version Context</em>' attribute.
	 * @see #getClientVersionContext()
	 * @generated
	 */
	void setClientVersionContext(String value);

} // Manifest
