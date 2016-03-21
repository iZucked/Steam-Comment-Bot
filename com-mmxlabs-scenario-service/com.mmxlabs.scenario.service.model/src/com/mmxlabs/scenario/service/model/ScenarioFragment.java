/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scenario Fragment</b></em>'.
 * A {@link ScenarioFragment} is a representation of part of the parent {@link ScenarioInstance} which is to be displayed in the scenario navigator.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getScenarioInstance <em>Scenario Instance</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getFragment <em>Fragment</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getContentType <em>Content Type</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioFragment()
 * @model
 * @generated
 */
public interface ScenarioFragment extends EObject {
	/**
	 * Returns the value of the '<em><b>Scenario Instance</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getFragments <em>Fragments</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenario Instance</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenario Instance</em>' container reference.
	 * @see #setScenarioInstance(ScenarioInstance)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioFragment_ScenarioInstance()
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getFragments
	 * @model opposite="fragments"
	 * @generated
	 */
	ScenarioInstance getScenarioInstance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getScenarioInstance <em>Scenario Instance</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scenario Instance</em>' container reference.
	 * @see #getScenarioInstance()
	 * @generated
	 */
	void setScenarioInstance(ScenarioInstance value);

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
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioFragment_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Fragment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fragment</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fragment</em>' reference.
	 * @see #setFragment(EObject)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioFragment_Fragment()
	 * @model
	 * @generated
	 */
	EObject getFragment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getFragment <em>Fragment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fragment</em>' reference.
	 * @see #getFragment()
	 * @generated
	 */
	void setFragment(EObject value);

	/**
	 * Returns the value of the '<em><b>Content Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content Type</em>' attribute.
	 * @see #setContentType(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioFragment_ContentType()
	 * @model
	 * @generated
	 */
	String getContentType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getContentType <em>Content Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content Type</em>' attribute.
	 * @see #getContentType()
	 * @generated
	 */
	void setContentType(String value);

} // ScenarioFragment
