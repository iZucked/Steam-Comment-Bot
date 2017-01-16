/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

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
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getInstance <em>Instance</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getFragments <em>Fragments</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getModelReferences <em>Model References</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getAdapters <em>Adapters</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getLocks <em>Locks</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isReadonly <em>Readonly</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isDirty <em>Dirty</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getValidationStatusCode <em>Validation Status Code</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isLoadFailure <em>Load Failure</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getLoadException <em>Load Exception</em>}</li>
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
	 * Returns the value of the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Returns true if the scenario is locked for changes. See {@link #getLock(String)} and {@link #getLocks()} determine how the scenario is locked.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Locked</em>' attribute.
	 * @see #setLocked(boolean)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Locked()
	 * @model transient="true"
	 * @generated
	 */
	boolean isLocked();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isLocked <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * Set the locked status of the Scenario. This should not be called directly, rather it should be set as a result of obtaining a {@link ScenarioLock}.
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Locked</em>' attribute.
	 * @see #isLocked()
	 * @generated
	 */
	void setLocked(boolean value);

	/**
	 * Returns the value of the '<em><b>Instance</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Returns the scenario model instance if loaded, otherwise null.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Instance</em>' reference.
	 * @see #setInstance(EObject)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Instance()
	 * @model transient="true"
	 * @generated
	 */
	EObject getInstance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getInstance <em>Instance</em>}' reference.
	 * <!-- begin-user-doc -->
	 * Set the model instance. This should not be called directly, but rather as a result of {@link #unload()}.
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Instance</em>' reference.
	 * @see #getInstance()
	 * @generated
	 */
	void setInstance(EObject value);

	/**
	 * Returns the value of the '<em><b>Adapters</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Returns a {@link Map} of runtime related objects for the loaded scenario. For example this will store {@link EditingDomain} and {@link CommandStack} keyed by {@link Class}. 
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Adapters</em>' attribute.
	 * @see #setAdapters(Map)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Adapters()
	 * @model transient="true"
	 * @generated
	 */
	Map<Class<?>, Object> getAdapters();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getAdapters <em>Adapters</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * Sets the adapter map. Do not call directl, this will be set as a result of a {@link #load()}.
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Adapters</em>' attribute.
	 * @see #getAdapters()
	 * @generated
	 */
	void setAdapters(Map<Class<?>, Object> value);

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
	 * Returns the value of the '<em><b>Model References</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.scenario.service.model.ModelReference}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.scenario.service.model.ModelReference#getScenarioInstance <em>Scenario Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Returns the list of active {@link ModelReference}s. A {@link ModelReference} should be obtained from a call to {@link #getReference()} and then released called {@link ModelReference#close()}
	 * rather than manipulating this list directly. When this list is empty, scenarios may be unloaded.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model References</em>' containment reference list.
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_ModelReferences()
	 * @see com.mmxlabs.scenario.service.model.ModelReference#getScenarioInstance
	 * @model opposite="scenarioInstance" containment="true" transient="true"
	 * @generated
	 */
	EList<ModelReference> getModelReferences();

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
	 * Returns the value of the '<em><b>Dirty</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dirty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dirty</em>' attribute.
	 * @see #setDirty(boolean)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Dirty()
	 * @model default="false" transient="true"
	 * @generated
	 */
	boolean isDirty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isDirty <em>Dirty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dirty</em>' attribute.
	 * @see #isDirty()
	 * @generated
	 */
	void setDirty(boolean value);

	/**
	 * Returns the value of the '<em><b>Locks</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.scenario.service.model.ScenarioLock}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.scenario.service.model.ScenarioLock#getInstance <em>Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Locks</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Locks</em>' containment reference list.
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Locks()
	 * @see com.mmxlabs.scenario.service.model.ScenarioLock#getInstance
	 * @model opposite="instance" containment="true" transient="true"
	 * @generated
	 */
	EList<ScenarioLock> getLocks();

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
	 * Returns the value of the '<em><b>Load Failure</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Failure</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Failure</em>' attribute.
	 * @see #setLoadFailure(boolean)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_LoadFailure()
	 * @model transient="true"
	 * @generated
	 */
	boolean isLoadFailure();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isLoadFailure <em>Load Failure</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Failure</em>' attribute.
	 * @see #isLoadFailure()
	 * @generated
	 */
	void setLoadFailure(boolean value);

	/**
	 * Returns the value of the '<em><b>Load Exception</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Exception</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Exception</em>' attribute.
	 * @see #setLoadException(Exception)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_LoadException()
	 * @model dataType="com.mmxlabs.scenario.service.model.Exception" transient="true"
	 * @generated
	 */
	Exception getLoadException();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getLoadException <em>Load Exception</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Exception</em>' attribute.
	 * @see #getLoadException()
	 * @generated
	 */
	void setLoadException(Exception value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return super.getContainedInstanceCount()+1;'"
	 * @generated
	 */
	int getContainedInstanceCount();

	/**
	 * <!-- begin-user-doc -->
	 * Request a {@link ScenarioLock} object of the given type. Typically before claiming the lock a {@link ModelReference} should also be obtained.
	 * <!-- end-user-doc -->
	 * @model required="true" keyRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final List<ScenarioLock> locks = getLocks();\n\t\tsynchronized (locks) {\n\t\t\tfor (final ScenarioLock lock : locks) {\n\t\t\t\tif (lock.getKey().equals(key)) {\n\t\t\t\t\treturn lock;\n\t\t\t\t}\n\t\t\t}\n\n\t\t\tfinal ScenarioLock newLock = ScenarioServiceFactory.eINSTANCE.createScenarioLock();\n\t\t\tnewLock.setKey(key);\n\t\t\tlocks.add(newLock);\n\t\t\tnewLock.init();\n\t\t\treturn newLock;\n\t\t}'"
	 * @generated
	 */
	ScenarioLock getLock(String key);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	ModelReference getReference(String referenceID);

	/**
	 * <!-- begin-user-doc -->
	 * Load a scenario instance. Do not call directly. This should be invoked when requesting {@link ModelReference#getInstance()}.
	 * <!-- end-user-doc -->
	 * @model exceptions="com.mmxlabs.scenario.service.model.IOException"
	 * @generated
	 */
	EObject load() throws IOException;

	/**
	 * <!-- begin-user-doc -->
	 * Unload a loaded scenario instance. Do not call directly. This should be invoked when all {@link ModelReference}s have been {@link ModelReference#close()}d.
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void unload();

	/**
	 * <!-- begin-user-doc -->
	 * Save the scenario. This requires a {@link #getScenarioService()} to be set.
	 * <!-- end-user-doc -->
	 * @model exceptions="com.mmxlabs.scenario.service.model.IOException"
	 * @generated
	 */
	void save() throws IOException;

} // ScenarioInstance
