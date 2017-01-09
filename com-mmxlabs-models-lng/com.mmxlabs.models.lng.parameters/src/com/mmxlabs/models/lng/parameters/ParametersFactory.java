/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
	 * Returns a new object of class '<em>Annealing Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annealing Settings</em>'.
	 * @generated
	 */
	AnnealingSettings createAnnealingSettings();

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
	 * Returns a new object of class '<em>Parallisable Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parallisable Optimisation Stage</em>'.
	 * @generated
	 */
	ParallisableOptimisationStage createParallisableOptimisationStage();

	/**
	 * Returns a new object of class '<em>Parallel Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parallel Optimisation Stage</em>'.
	 * @generated
	 */
	<T extends ParallisableOptimisationStage> ParallelOptimisationStage<T> createParallelOptimisationStage();

	/**
	 * Returns a new object of class '<em>Clean State Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Clean State Optimisation Stage</em>'.
	 * @generated
	 */
	CleanStateOptimisationStage createCleanStateOptimisationStage();

	/**
	 * Returns a new object of class '<em>Local Search Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Local Search Optimisation Stage</em>'.
	 * @generated
	 */
	LocalSearchOptimisationStage createLocalSearchOptimisationStage();

	/**
	 * Returns a new object of class '<em>Hill Climb Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Hill Climb Optimisation Stage</em>'.
	 * @generated
	 */
	HillClimbOptimisationStage createHillClimbOptimisationStage();

	/**
	 * Returns a new object of class '<em>Action Plan Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Action Plan Optimisation Stage</em>'.
	 * @generated
	 */
	ActionPlanOptimisationStage createActionPlanOptimisationStage();

	/**
	 * Returns a new object of class '<em>Reset Initial Sequences Stage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Reset Initial Sequences Stage</em>'.
	 * @generated
	 */
	ResetInitialSequencesStage createResetInitialSequencesStage();

	/**
	 * Returns a new object of class '<em>Break Even Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Break Even Optimisation Stage</em>'.
	 * @generated
	 */
	BreakEvenOptimisationStage createBreakEvenOptimisationStage();

	/**
	 * Returns a new object of class '<em>Constraint And Fitness Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Constraint And Fitness Settings</em>'.
	 * @generated
	 */
	ConstraintAndFitnessSettings createConstraintAndFitnessSettings();

	/**
	 * Returns a new object of class '<em>Optimisation Plan</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Optimisation Plan</em>'.
	 * @generated
	 */
	OptimisationPlan createOptimisationPlan();

	/**
	 * Returns a new object of class '<em>Solution Builder Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Solution Builder Settings</em>'.
	 * @generated
	 */
	SolutionBuilderSettings createSolutionBuilderSettings();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ParametersPackage getParametersPackage();

} //OptimiserFactory
