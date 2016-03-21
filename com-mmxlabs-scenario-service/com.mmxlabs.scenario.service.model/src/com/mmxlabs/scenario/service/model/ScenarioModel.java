/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scenario Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioModel#getScenarioServices <em>Scenario Services</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioModel()
 * @model
 * @generated
 */
public interface ScenarioModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Scenario Services</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.scenario.service.model.ScenarioService}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.scenario.service.model.ScenarioService#getScenarioModel <em>Scenario Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenario Services</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenario Services</em>' reference list.
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioModel_ScenarioServices()
	 * @see com.mmxlabs.scenario.service.model.ScenarioService#getScenarioModel
	 * @model opposite="scenarioModel" transient="true"
	 * @generated
	 */
	EList<ScenarioService> getScenarioServices();

} // ScenarioModel
