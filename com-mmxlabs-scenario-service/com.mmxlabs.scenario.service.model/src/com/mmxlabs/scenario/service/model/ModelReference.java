/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import java.io.Closeable;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.ModelReference#getScenarioInstance <em>Scenario Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getModelReference()
 * @model superTypes="com.mmxlabs.scenario.service.model.Closeable"
 * @generated
 */
public interface ModelReference extends EObject, Closeable {
	/**
	 * Returns the value of the '<em><b>Scenario Instance</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getModelReferences <em>Model References</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenario Instance</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenario Instance</em>' container reference.
	 * @see #setScenarioInstance(ScenarioInstance)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getModelReference_ScenarioInstance()
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getModelReferences
	 * @model opposite="modelReferences"
	 * @generated
	 */
	ScenarioInstance getScenarioInstance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ModelReference#getScenarioInstance <em>Scenario Instance</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scenario Instance</em>' container reference.
	 * @see #getScenarioInstance()
	 * @generated
	 */
	void setScenarioInstance(ScenarioInstance value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EObject getInstance();

	/**
	 * <!-- begin-user-doc -->
	 * Override and suppress default {@link Closeable#close} swallowing (or wrapping) any exceptions
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated NOT
	 */
	void close();

} // ModelReference
