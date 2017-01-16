/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import com.mmxlabs.scenario.service.IScenarioService;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scenario Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioService#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioService#getServiceRef <em>Service Ref</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioService#isSupportsForking <em>Supports Forking</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioService#isSupportsImport <em>Supports Import</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioService#getScenarioModel <em>Scenario Model</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioService#isLocal <em>Local</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioService()
 * @model
 * @generated
 */
public interface ScenarioService extends Container {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioService_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioService#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Service Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Ref</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Ref</em>' attribute.
	 * @see #setServiceRef(IScenarioService)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioService_ServiceRef()
	 * @model dataType="com.mmxlabs.scenario.service.model.IScenarioService" transient="true"
	 * @generated
	 */
	IScenarioService getServiceRef();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioService#getServiceRef <em>Service Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Ref</em>' attribute.
	 * @see #getServiceRef()
	 * @generated
	 */
	void setServiceRef(IScenarioService value);

	/**
	 * Returns the value of the '<em><b>Supports Forking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Supports Forking</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Supports Forking</em>' attribute.
	 * @see #setSupportsForking(boolean)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioService_SupportsForking()
	 * @model
	 * @generated
	 */
	boolean isSupportsForking();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioService#isSupportsForking <em>Supports Forking</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Supports Forking</em>' attribute.
	 * @see #isSupportsForking()
	 * @generated
	 */
	void setSupportsForking(boolean value);

	/**
	 * Returns the value of the '<em><b>Supports Import</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Supports Import</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Supports Import</em>' attribute.
	 * @see #setSupportsImport(boolean)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioService_SupportsImport()
	 * @model
	 * @generated
	 */
	boolean isSupportsImport();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioService#isSupportsImport <em>Supports Import</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Supports Import</em>' attribute.
	 * @see #isSupportsImport()
	 * @generated
	 */
	void setSupportsImport(boolean value);

	/**
	 * Returns the value of the '<em><b>Scenario Model</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.scenario.service.model.ScenarioModel#getScenarioServices <em>Scenario Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenario Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenario Model</em>' reference.
	 * @see #setScenarioModel(ScenarioModel)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioService_ScenarioModel()
	 * @see com.mmxlabs.scenario.service.model.ScenarioModel#getScenarioServices
	 * @model opposite="scenarioServices" transient="true"
	 * @generated
	 */
	ScenarioModel getScenarioModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioService#getScenarioModel <em>Scenario Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scenario Model</em>' reference.
	 * @see #getScenarioModel()
	 * @generated
	 */
	void setScenarioModel(ScenarioModel value);

	/**
	 * Returns the value of the '<em><b>Local</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Local</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Local</em>' attribute.
	 * @see #setLocal(boolean)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioService_Local()
	 * @model
	 * @generated
	 */
	boolean isLocal();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioService#isLocal <em>Local</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Local</em>' attribute.
	 * @see #isLocal()
	 * @generated
	 */
	void setLocal(boolean value);

} // ScenarioService
