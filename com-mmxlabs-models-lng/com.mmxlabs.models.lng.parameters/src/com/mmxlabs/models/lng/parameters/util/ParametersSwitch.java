/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.util;

import com.mmxlabs.models.lng.parameters.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.jdt.annotation.Nullable;
import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.SimilarityInterval;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage
 * @generated
 */
public class ParametersSwitch<@Nullable T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ParametersPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParametersSwitch() {
		if (modelPackage == null) {
			modelPackage = ParametersPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case ParametersPackage.USER_SETTINGS: {
				UserSettings userSettings = (UserSettings)theEObject;
				T result = caseUserSettings(userSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.OBJECTIVE: {
				Objective objective = (Objective)theEObject;
				T result = caseObjective(objective);
				if (result == null) result = caseNamedObject(objective);
				if (result == null) result = caseMMXObject(objective);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.CONSTRAINT: {
				Constraint constraint = (Constraint)theEObject;
				T result = caseConstraint(constraint);
				if (result == null) result = caseNamedObject(constraint);
				if (result == null) result = caseMMXObject(constraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.ANNEALING_SETTINGS: {
				AnnealingSettings annealingSettings = (AnnealingSettings)theEObject;
				T result = caseAnnealingSettings(annealingSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.SIMILARITY_SETTINGS: {
				SimilaritySettings similaritySettings = (SimilaritySettings)theEObject;
				T result = caseSimilaritySettings(similaritySettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.SIMILARITY_INTERVAL: {
				SimilarityInterval similarityInterval = (SimilarityInterval)theEObject;
				T result = caseSimilarityInterval(similarityInterval);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.OPTIMISATION_PLAN: {
				OptimisationPlan optimisationPlan = (OptimisationPlan)theEObject;
				T result = caseOptimisationPlan(optimisationPlan);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS: {
				ConstraintAndFitnessSettings constraintAndFitnessSettings = (ConstraintAndFitnessSettings)theEObject;
				T result = caseConstraintAndFitnessSettings(constraintAndFitnessSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.OPTIMISATION_STAGE: {
				OptimisationStage optimisationStage = (OptimisationStage)theEObject;
				T result = caseOptimisationStage(optimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE: {
				ConstraintsAndFitnessSettingsStage constraintsAndFitnessSettingsStage = (ConstraintsAndFitnessSettingsStage)theEObject;
				T result = caseConstraintsAndFitnessSettingsStage(constraintsAndFitnessSettingsStage);
				if (result == null) result = caseOptimisationStage(constraintsAndFitnessSettingsStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE: {
				CleanStateOptimisationStage cleanStateOptimisationStage = (CleanStateOptimisationStage)theEObject;
				T result = caseCleanStateOptimisationStage(cleanStateOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(cleanStateOptimisationStage);
				if (result == null) result = caseOptimisationStage(cleanStateOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.STRATEGIC_LOCAL_SEARCH_OPTIMISATION_STAGE: {
				StrategicLocalSearchOptimisationStage strategicLocalSearchOptimisationStage = (StrategicLocalSearchOptimisationStage)theEObject;
				T result = caseStrategicLocalSearchOptimisationStage(strategicLocalSearchOptimisationStage);
				if (result == null) result = caseLocalSearchOptimisationStage(strategicLocalSearchOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(strategicLocalSearchOptimisationStage);
				if (result == null) result = caseOptimisationStage(strategicLocalSearchOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.LOCAL_SEARCH_OPTIMISATION_STAGE: {
				LocalSearchOptimisationStage localSearchOptimisationStage = (LocalSearchOptimisationStage)theEObject;
				T result = caseLocalSearchOptimisationStage(localSearchOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(localSearchOptimisationStage);
				if (result == null) result = caseOptimisationStage(localSearchOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE: {
				HillClimbOptimisationStage hillClimbOptimisationStage = (HillClimbOptimisationStage)theEObject;
				T result = caseHillClimbOptimisationStage(hillClimbOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(hillClimbOptimisationStage);
				if (result == null) result = caseOptimisationStage(hillClimbOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.ACTION_PLAN_OPTIMISATION_STAGE: {
				ActionPlanOptimisationStage actionPlanOptimisationStage = (ActionPlanOptimisationStage)theEObject;
				T result = caseActionPlanOptimisationStage(actionPlanOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(actionPlanOptimisationStage);
				if (result == null) result = caseOptimisationStage(actionPlanOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.RESET_INITIAL_SEQUENCES_STAGE: {
				ResetInitialSequencesStage resetInitialSequencesStage = (ResetInitialSequencesStage)theEObject;
				T result = caseResetInitialSequencesStage(resetInitialSequencesStage);
				if (result == null) result = caseOptimisationStage(resetInitialSequencesStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.REDUCE_SEQUENCES_STAGE: {
				ReduceSequencesStage reduceSequencesStage = (ReduceSequencesStage)theEObject;
				T result = caseReduceSequencesStage(reduceSequencesStage);
				if (result == null) result = caseOptimisationStage(reduceSequencesStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.INSERTION_OPTIMISATION_STAGE: {
				InsertionOptimisationStage insertionOptimisationStage = (InsertionOptimisationStage)theEObject;
				T result = caseInsertionOptimisationStage(insertionOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(insertionOptimisationStage);
				if (result == null) result = caseOptimisationStage(insertionOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE: {
				BreakEvenOptimisationStage breakEvenOptimisationStage = (BreakEvenOptimisationStage)theEObject;
				T result = caseBreakEvenOptimisationStage(breakEvenOptimisationStage);
				if (result == null) result = caseOptimisationStage(breakEvenOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.SOLUTION_BUILDER_SETTINGS: {
				SolutionBuilderSettings solutionBuilderSettings = (SolutionBuilderSettings)theEObject;
				T result = caseSolutionBuilderSettings(solutionBuilderSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.MULTIPLE_SOLUTION_SIMILARITY_OPTIMISATION_STAGE: {
				MultipleSolutionSimilarityOptimisationStage multipleSolutionSimilarityOptimisationStage = (MultipleSolutionSimilarityOptimisationStage)theEObject;
				T result = caseMultipleSolutionSimilarityOptimisationStage(multipleSolutionSimilarityOptimisationStage);
				if (result == null) result = caseLocalSearchOptimisationStage(multipleSolutionSimilarityOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(multipleSolutionSimilarityOptimisationStage);
				if (result == null) result = caseOptimisationStage(multipleSolutionSimilarityOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.MULTIOBJECTIVE_SIMILARITY_OPTIMISATION_STAGE: {
				MultiobjectiveSimilarityOptimisationStage multiobjectiveSimilarityOptimisationStage = (MultiobjectiveSimilarityOptimisationStage)theEObject;
				T result = caseMultiobjectiveSimilarityOptimisationStage(multiobjectiveSimilarityOptimisationStage);
				if (result == null) result = caseMultipleSolutionSimilarityOptimisationStage(multiobjectiveSimilarityOptimisationStage);
				if (result == null) result = caseLocalSearchOptimisationStage(multiobjectiveSimilarityOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(multiobjectiveSimilarityOptimisationStage);
				if (result == null) result = caseOptimisationStage(multiobjectiveSimilarityOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS: {
				CleanStateOptimisationSettings cleanStateOptimisationSettings = (CleanStateOptimisationSettings)theEObject;
				T result = caseCleanStateOptimisationSettings(cleanStateOptimisationSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>User Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>User Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUserSettings(UserSettings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Objective</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Objective</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseObjective(Objective object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstraint(Constraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Annealing Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Annealing Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnnealingSettings(AnnealingSettings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Similarity Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Similarity Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimilaritySettings(SimilaritySettings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Similarity Interval</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Similarity Interval</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimilarityInterval(SimilarityInterval object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOptimisationStage(OptimisationStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Constraints And Fitness Settings Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Constraints And Fitness Settings Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstraintsAndFitnessSettingsStage(ConstraintsAndFitnessSettingsStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Clean State Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Clean State Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCleanStateOptimisationStage(CleanStateOptimisationStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Strategic Local Search Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Strategic Local Search Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStrategicLocalSearchOptimisationStage(StrategicLocalSearchOptimisationStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Local Search Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Local Search Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLocalSearchOptimisationStage(LocalSearchOptimisationStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Hill Climb Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Hill Climb Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseHillClimbOptimisationStage(HillClimbOptimisationStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Action Plan Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Action Plan Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseActionPlanOptimisationStage(ActionPlanOptimisationStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reset Initial Sequences Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reset Initial Sequences Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResetInitialSequencesStage(ResetInitialSequencesStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reduce Sequences Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reduce Sequences Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReduceSequencesStage(ReduceSequencesStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Insertion Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Insertion Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInsertionOptimisationStage(InsertionOptimisationStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Break Even Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Break Even Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBreakEvenOptimisationStage(BreakEvenOptimisationStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Constraint And Fitness Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Constraint And Fitness Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstraintAndFitnessSettings(ConstraintAndFitnessSettings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Optimisation Plan</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Optimisation Plan</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOptimisationPlan(OptimisationPlan object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Solution Builder Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Solution Builder Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSolutionBuilderSettings(SolutionBuilderSettings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Multiple Solution Similarity Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Multiple Solution Similarity Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMultipleSolutionSimilarityOptimisationStage(MultipleSolutionSimilarityOptimisationStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Multiobjective Similarity Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Multiobjective Similarity Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMultiobjectiveSimilarityOptimisationStage(MultiobjectiveSimilarityOptimisationStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Clean State Optimisation Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Clean State Optimisation Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCleanStateOptimisationSettings(CleanStateOptimisationSettings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>MMX Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MMX Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMMXObject(MMXObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedObject(NamedObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //ParametersSwitch
