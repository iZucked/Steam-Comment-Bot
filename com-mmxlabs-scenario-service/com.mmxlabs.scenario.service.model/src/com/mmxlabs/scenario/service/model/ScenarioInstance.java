/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getUuid <em>Uuid</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getUri <em>Uri</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isArchived <em>Archived</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getInitialSolution <em>Initial Solution</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getVariations <em>Variations</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getParameterSets <em>Parameter Sets</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getInstance <em>Instance</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getController <em>Controller</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getAdapters <em>Adapters</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance()
 * @model
 * @generated
 */
public interface ScenarioInstance extends EObject {
	/**
	 * Returns the value of the '<em><b>Variations</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.scenario.service.model.ScenarioInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Variations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Variations</em>' containment reference list.
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Variations()
	 * @model containment="true"
	 * @generated
	 */
	EList<ScenarioInstance> getVariations();

	/**
	 * Returns the value of the '<em><b>Parameter Sets</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.scenario.service.model.ParamSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Sets</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Sets</em>' containment reference list.
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_ParameterSets()
	 * @model containment="true"
	 * @generated
	 */
	EList<ParamSet> getParameterSets();

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
	 * Returns the value of the '<em><b>Controller</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Controller</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Controller</em>' reference.
	 * @see #setController(EObject)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Controller()
	 * @model transient="true"
	 * @generated
	 */
	EObject getController();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getController <em>Controller</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Controller</em>' reference.
	 * @see #getController()
	 * @generated
	 */
	void setController(EObject value);

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
	 * Returns the value of the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uri</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uri</em>' attribute.
	 * @see #setUri(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Uri()
	 * @model required="true"
	 * @generated
	 */
	String getUri();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getUri <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uri</em>' attribute.
	 * @see #getUri()
	 * @generated
	 */
	void setUri(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Initial Solution</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Solution</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Solution</em>' containment reference.
	 * @see #setInitialSolution(Solution)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_InitialSolution()
	 * @model containment="true"
	 * @generated
	 */
	Solution getInitialSolution();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getInitialSolution <em>Initial Solution</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Solution</em>' containment reference.
	 * @see #getInitialSolution()
	 * @generated
	 */
	void setInitialSolution(Solution value);

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
	 * Returns the value of the '<em><b>Archived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Archived</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Archived</em>' attribute.
	 * @see #setArchived(boolean)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioInstance_Archived()
	 * @model
	 * @generated
	 */
	boolean isArchived();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isArchived <em>Archived</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Archived</em>' attribute.
	 * @see #isArchived()
	 * @generated
	 */
	void setArchived(boolean value);

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

} // ScenarioInstance
