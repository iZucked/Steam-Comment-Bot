/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioService#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioService#getServiceRef <em>Service Ref</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioService#isSupportsForking <em>Supports Forking</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioService#isSupportsImport <em>Supports Import</em>}</li>
 * </ul>
 * </p>
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

} // ScenarioService
