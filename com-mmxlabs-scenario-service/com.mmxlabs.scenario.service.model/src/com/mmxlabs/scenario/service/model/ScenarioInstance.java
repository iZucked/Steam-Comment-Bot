/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import java.util.Map;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scenario Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getUuid <em>Uuid</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getInstance <em>Instance</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getAdapters <em>Adapters</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getSubModelURIs <em>Sub Model UR Is</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getDependencyUUIDs <em>Dependency UUI Ds</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isDirty <em>Dirty</em>}</li>
 * </ul>
 * </p>
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
	 * If the meaning of the '<em>Uuid</em>' attribute isn't clear,
	 * there really should be more of a description here...
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
	 * If the meaning of the '<em>Locked</em>' attribute isn't clear,
	 * there really should be more of a description here...
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
	 * If the meaning of the '<em>Instance</em>' reference isn't clear,
	 * there really should be more of a description here...
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
	 * If the meaning of the '<em>Adapters</em>' attribute isn't clear,
	 * there really should be more of a description here...
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
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Adapters</em>' attribute.
	 * @see #getAdapters()
	 * @generated
	 */
	void setAdapters(Map<Class<?>, Object> value);

	/**
	 * Returns the value of the '<em><b>Sub Model UR Is</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Model UR Is</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Model UR Is</em>' attribute list.
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_SubModelURIs()
	 * @model
	 * @generated
	 */
	EList<String> getSubModelURIs();

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
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_DependencyUUIDs()
	 * @model
	 * @generated
	 */
	EList<String> getDependencyUUIDs();

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return super.getContainedInstanceCount()+1;'"
	 * @generated
	 */
	int getContainedInstanceCount();

} // ScenarioInstance
