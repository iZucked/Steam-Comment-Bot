/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage
 * @generated
 */
public interface ParametersFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ParametersFactory eINSTANCE = com.mmxlabs.models.lng.parameters.impl.ParametersFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>User Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>User Settings</em>'.
	 * @generated
	 */
	UserSettings createUserSettings();

	/**
	 * Returns a new object of class '<em>Individual Solution Improvement Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Individual Solution Improvement Settings</em>'.
	 * @generated
	 */
	IndividualSolutionImprovementSettings createIndividualSolutionImprovementSettings();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	ParametersModel createParametersModel();

	/**
	 * Returns a new object of class '<em>Optimiser Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Optimiser Settings</em>'.
	 * @generated
	 */
	OptimiserSettings createOptimiserSettings();

	/**
	 * Returns a new object of class '<em>Objective</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Objective</em>'.
	 * @generated
	 */
	Objective createObjective();

	/**
	 * Returns a new object of class '<em>Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Constraint</em>'.
	 * @generated
	 */
	Constraint createConstraint();

	/**
	 * Returns a new object of class '<em>Optimisation Range</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Optimisation Range</em>'.
	 * @generated
	 */
	OptimisationRange createOptimisationRange();

	/**
	 * Returns a new object of class '<em>Annealing Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annealing Settings</em>'.
	 * @generated
	 */
	AnnealingSettings createAnnealingSettings();

	/**
	 * Returns a new object of class '<em>Argument</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Argument</em>'.
	 * @generated
	 */
	Argument createArgument();

	/**
	 * Returns a new object of class '<em>Similarity Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Similarity Settings</em>'.
	 * @generated
	 */
	SimilaritySettings createSimilaritySettings();

	/**
	 * Returns a new object of class '<em>Similarity Interval</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Similarity Interval</em>'.
	 * @generated
	 */
	SimilarityInterval createSimilarityInterval();

	/**
	 * Returns a new object of class '<em>Action Plan Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Action Plan Settings</em>'.
	 * @generated
	 */
	ActionPlanSettings createActionPlanSettings();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ParametersPackage getParametersPackage();

} //OptimiserFactory
