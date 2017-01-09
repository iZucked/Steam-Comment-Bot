/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * @noimplement This interface is not intended to be implemented by clients.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage
 * @generated
 */
public interface ScenarioServiceFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ScenarioServiceFactory eINSTANCE = com.mmxlabs.scenario.service.model.impl.ScenarioServiceFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Scenario Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Scenario Model</em>'.
	 * @generated
	 */
	ScenarioModel createScenarioModel();

	/**
	 * Returns a new object of class '<em>Folder</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Folder</em>'.
	 * @generated
	 */
	Folder createFolder();

	/**
	 * Returns a new object of class '<em>Scenario Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Scenario Service</em>'.
	 * @generated
	 */
	ScenarioService createScenarioService();

	/**
	 * Returns a new object of class '<em>Scenario Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Scenario Instance</em>'.
	 * @generated
	 */
	ScenarioInstance createScenarioInstance();

	/**
	 * Returns a new object of class '<em>Metadata</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Metadata</em>'.
	 * @generated
	 */
	Metadata createMetadata();

	/**
	 * Returns a new object of class '<em>Scenario Lock</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Scenario Lock</em>'.
	 * @generated
	 */
	ScenarioLock createScenarioLock();

	/**
	 * Returns a new object of class '<em>Scenario Fragment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Scenario Fragment</em>'.
	 * @generated
	 */
	ScenarioFragment createScenarioFragment();

	/**
	 * Returns a new object of class '<em>Model Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model Reference</em>'.
	 * @generated
	 */
	ModelReference createModelReference();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ScenarioServicePackage getScenarioServicePackage();

} //ScenarioServiceFactory
