/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scenario Lock</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioLock#isAvailable <em>Available</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioLock#isClaimed <em>Claimed</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioLock#getKey <em>Key</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.ScenarioLock#getInstance <em>Instance</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioLock()
 * @model
 * @generated
 */
public interface ScenarioLock extends EObject {
	public static final String EDITORS = "editors";
	public static final String EVALUATOR = "evaluator";
	public static final String OPTIMISER = "optimiser";
	public static final String NAVIGATOR = "navigator";
	public static final String SAVING = "saving";
	public static final String VALIDATION = "validation";

	/**
	 * Returns the value of the '<em><b>Available</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Available</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Available</em>' attribute.
	 * @see #setAvailable(boolean)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioLock_Available()
	 * @model required="true"
	 * @generated
	 */
	boolean isAvailable();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioLock#isAvailable <em>Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Available</em>' attribute.
	 * @see #isAvailable()
	 * @generated
	 */
	void setAvailable(boolean value);

	/**
	 * Returns the value of the '<em><b>Claimed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Claimed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Claimed</em>' attribute.
	 * @see #setClaimed(boolean)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioLock_Claimed()
	 * @model required="true"
	 * @generated
	 */
	boolean isClaimed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioLock#isClaimed <em>Claimed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Claimed</em>' attribute.
	 * @see #isClaimed()
	 * @generated
	 */
	void setClaimed(boolean value);

	/**
	 * Returns the value of the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key</em>' attribute.
	 * @see #setKey(String)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioLock_Key()
	 * @model required="true"
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioLock#getKey <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

	/**
	 * Returns the value of the '<em><b>Instance</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getLocks <em>Locks</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Instance</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Instance</em>' container reference.
	 * @see #setInstance(ScenarioInstance)
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#getScenarioLock_Instance()
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getLocks
	 * @model opposite="locks" required="true"
	 * @generated
	 */
	ScenarioInstance getInstance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.scenario.service.model.ScenarioLock#getInstance <em>Instance</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Instance</em>' container reference.
	 * @see #getInstance()
	 * @generated
	 */
	void setInstance(ScenarioInstance value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	boolean claim();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	boolean awaitClaim();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void release();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void init();

} // ScenarioLock
