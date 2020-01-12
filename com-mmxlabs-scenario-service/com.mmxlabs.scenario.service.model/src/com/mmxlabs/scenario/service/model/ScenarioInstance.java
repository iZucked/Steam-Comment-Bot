/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import com.mmxlabs.scenario.service.manifest.Manifest;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scenario Instance</b></em>'.
 * @noimplement This interface is not intended to be implemented by clients.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getUuid <em>Uuid</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getRootObjectURI <em>Root Object URI</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getScenarioVersion <em>Scenario Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getVersionContext <em>Version Context</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getClientScenarioVersion <em>Client Scenario Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getClientVersionContext <em>Client Version Context</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getFragments <em>Fragments</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isReadonly <em>Readonly</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getValidationStatusCode <em>Validation Status Code</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getManifest <em>Manifest</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getExternalID <em>External ID</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance()
 * @model
 * @generated
 */
public interface ScenarioInstance extends Container {
	/**
	 * Returns the value of the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Returns a unique identifier for the scenario. This is used to reference the scenario externally.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uuid</em>' attribute.
	 * @see #setUuid(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Uuid()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getUuid();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getUuid <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uuid</em>' attribute.
	 * @see #getUuid()
	 * @generated
	 */
	void setUuid(String value);

	/**
	 * Returns the value of the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metadata</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metadata</em>' containment reference.
	 * @see #setMetadata(Metadata)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Metadata()
	 * @model containment="true"
	 * @generated
	 */
	Metadata getMetadata();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getMetadata <em>Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metadata</em>' containment reference.
	 * @see #getMetadata()
	 * @generated
	 */
	void setMetadata(Metadata value);

	/**
	 * Returns the value of the '<em><b>Root Object URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Returns the {@link ScenarioService} relative URI to the underlying data.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Root Object URI</em>' attribute.
	 * @see #setRootObjectURI(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_RootObjectURI()
	 * @model
	 * @generated
	 */
	String getRootObjectURI();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getRootObjectURI <em>Root Object URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Root Object URI</em>' attribute.
	 * @see #getRootObjectURI()
	 * @generated
	 */
	void setRootObjectURI(String value);

	/**
	 * Returns the value of the '<em><b>Scenario Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Returns the core scenario data-model version. Used for scenario migration.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenario Version</em>' attribute.
	 * @see #setScenarioVersion(int)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_ScenarioVersion()
	 * @model
	 * @generated
	 */
	int getScenarioVersion();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getScenarioVersion <em>Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * Set the core scenario data-model version. Used for scenario migration.
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
	 * Returns the core scenario data-model version context string. Used for scenario migration.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version Context</em>' attribute.
	 * @see #setVersionContext(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_VersionContext()
	 * @model
	 * @generated
	 */
	String getVersionContext();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getVersionContext <em>Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * Sets the core scenario data-model version context string. Used for scenario migration.
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version Context</em>' attribute.
	 * @see #getVersionContext()
	 * @generated
	 */
	void setVersionContext(String value);

	/**
	 * Returns the value of the '<em><b>Fragments</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.scenario.service.model.ScenarioFragment}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getScenarioInstance <em>Scenario Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fragments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fragments</em>' containment reference list.
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Fragments()
	 * @see com.mmxlabs.scenario.service.model.ScenarioFragment#getScenarioInstance
	 * @model opposite="scenarioInstance" containment="true" transient="true"
	 * @generated
	 */
	EList<ScenarioFragment> getFragments();

	/**
	 * Returns the value of the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Readonly</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Readonly</em>' attribute.
	 * @see #setReadonly(boolean)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Readonly()
	 * @model
	 * @generated
	 */
	boolean isReadonly();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isReadonly <em>Readonly</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Readonly</em>' attribute.
	 * @see #isReadonly()
	 * @generated
	 */
	void setReadonly(boolean value);

	/**
	 * Returns the value of the '<em><b>Validation Status Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Validation Status Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Validation Status Code</em>' attribute.
	 * @see #setValidationStatusCode(int)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_ValidationStatusCode()
	 * @model
	 * @generated
	 */
	int getValidationStatusCode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getValidationStatusCode <em>Validation Status Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Validation Status Code</em>' attribute.
	 * @see #getValidationStatusCode()
	 * @generated
	 */
	void setValidationStatusCode(int value);

	/**
	 * Returns the value of the '<em><b>Manifest</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Manifest</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Manifest</em>' reference.
	 * @see #setManifest(Manifest)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Manifest()
	 * @model transient="true"
	 * @generated
	 */
	Manifest getManifest();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getManifest <em>Manifest</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Manifest</em>' reference.
	 * @see #getManifest()
	 * @generated
	 */
	void setManifest(Manifest value);

	/**
	 * Returns the value of the '<em><b>External ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>External ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>External ID</em>' attribute.
	 * @see #setExternalID(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_ExternalID()
	 * @model
	 * @generated
	 */
	String getExternalID();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getExternalID <em>External ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>External ID</em>' attribute.
	 * @see #getExternalID()
	 * @generated
	 */
	void setExternalID(String value);

	/**
	 * Returns the value of the '<em><b>Client Scenario Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Returns the client specific add-on data-model version. Used for scenario migration.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Client Scenario Version</em>' attribute.
	 * @see #setClientScenarioVersion(int)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_ClientScenarioVersion()
	 * @model
	 * @generated
	 */
	int getClientScenarioVersion();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getClientScenarioVersion <em>Client Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * Sets the client specific add-on data-model version. Used for scenario migration.
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
	 * Returns the client specific add-on version context string. Used for scenario migration.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Client Version Context</em>' attribute.
	 * @see #setClientVersionContext(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_ClientVersionContext()
	 * @model
	 * @generated
	 */
	String getClientVersionContext();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getClientVersionContext <em>Client Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * Sets the client specific add-on version context string. Used for scenario migration.
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Client Version Context</em>' attribute.
	 * @see #getClientVersionContext()
	 * @generated
	 */
	void setClientVersionContext(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return super.getContainedInstanceCount()+1;'"
	 * @generated
	 */
	int getContainedInstanceCount();

} // ScenarioInstance
