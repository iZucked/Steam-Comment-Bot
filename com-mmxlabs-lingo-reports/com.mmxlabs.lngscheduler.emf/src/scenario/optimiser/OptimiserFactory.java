/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package scenario.optimiser;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see scenario.optimiser.OptimiserPackage
 * @generated
 */
public interface OptimiserFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	OptimiserFactory eINSTANCE = scenario.optimiser.impl.OptimiserFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Optimisation Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Optimisation Settings</em>'.
	 * @generated
	 */
	OptimisationSettings createOptimisationSettings();

	/**
	 * Returns a new object of class '<em>Optimisation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Optimisation</em>'.
	 * @generated
	 */
	Optimisation createOptimisation();

	/**
	 * Returns a new object of class '<em>Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Constraint</em>'.
	 * @generated
	 */
	Constraint createConstraint();

	/**
	 * Returns a new object of class '<em>Objective</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Objective</em>'.
	 * @generated
	 */
	Objective createObjective();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	OptimiserPackage getOptimiserPackage();

} //OptimiserFactory
