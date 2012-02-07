/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see scenario.ScenarioPackage
 * @generated
 */
public interface ScenarioFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	ScenarioFactory eINSTANCE = scenario.impl.ScenarioFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Scenario</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Scenario</em>'.
	 * @generated
	 */
	Scenario createScenario();

	/**
	 * Returns a new object of class '<em>Annotated Object</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Annotated Object</em>'.
	 * @generated
	 */
	AnnotatedObject createAnnotatedObject();

	/**
	 * Returns a new object of class '<em>Detail</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Detail</em>'.
	 * @generated
	 */
	Detail createDetail();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	ScenarioPackage getScenarioPackage();

} // ScenarioFactory
