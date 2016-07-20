/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.mmxcore.UUIDObject;

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
public class ParametersSwitch<@Nullable T1> extends Switch<T1> {
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
	protected T1 doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case ParametersPackage.USER_SETTINGS: {
				UserSettings userSettings = (UserSettings)theEObject;
				T1 result = caseUserSettings(userSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.OBJECTIVE: {
				Objective objective = (Objective)theEObject;
				T1 result = caseObjective(objective);
				if (result == null) result = caseNamedObject(objective);
				if (result == null) result = caseMMXObject(objective);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.CONSTRAINT: {
				Constraint constraint = (Constraint)theEObject;
				T1 result = caseConstraint(constraint);
				if (result == null) result = caseNamedObject(constraint);
				if (result == null) result = caseMMXObject(constraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.ANNEALING_SETTINGS: {
				AnnealingSettings annealingSettings = (AnnealingSettings)theEObject;
				T1 result = caseAnnealingSettings(annealingSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.SIMILARITY_SETTINGS: {
				SimilaritySettings similaritySettings = (SimilaritySettings)theEObject;
				T1 result = caseSimilaritySettings(similaritySettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.SIMILARITY_INTERVAL: {
				SimilarityInterval similarityInterval = (SimilarityInterval)theEObject;
				T1 result = caseSimilarityInterval(similarityInterval);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.OPTIMISATION_PLAN: {
				OptimisationPlan optimisationPlan = (OptimisationPlan)theEObject;
				T1 result = caseOptimisationPlan(optimisationPlan);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS: {
				ConstraintAndFitnessSettings constraintAndFitnessSettings = (ConstraintAndFitnessSettings)theEObject;
				T1 result = caseConstraintAndFitnessSettings(constraintAndFitnessSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.OPTIMISATION_STAGE: {
				OptimisationStage optimisationStage = (OptimisationStage)theEObject;
				T1 result = caseOptimisationStage(optimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE: {
				ConstraintsAndFitnessSettingsStage constraintsAndFitnessSettingsStage = (ConstraintsAndFitnessSettingsStage)theEObject;
				T1 result = caseConstraintsAndFitnessSettingsStage(constraintsAndFitnessSettingsStage);
				if (result == null) result = caseOptimisationStage(constraintsAndFitnessSettingsStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.PARALLISABLE_OPTIMISATION_STAGE: {
				ParallisableOptimisationStage parallisableOptimisationStage = (ParallisableOptimisationStage)theEObject;
				T1 result = caseParallisableOptimisationStage(parallisableOptimisationStage);
				if (result == null) result = caseOptimisationStage(parallisableOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE: {
				ParallelOptimisationStage<?> parallelOptimisationStage = (ParallelOptimisationStage<?>)theEObject;
				T1 result = caseParallelOptimisationStage(parallelOptimisationStage);
				if (result == null) result = caseOptimisationStage(parallelOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE: {
				CleanStateOptimisationStage cleanStateOptimisationStage = (CleanStateOptimisationStage)theEObject;
				T1 result = caseCleanStateOptimisationStage(cleanStateOptimisationStage);
				if (result == null) result = caseParallisableOptimisationStage(cleanStateOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(cleanStateOptimisationStage);
				if (result == null) result = caseOptimisationStage(cleanStateOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.LOCAL_SEARCH_OPTIMISATION_STAGE: {
				LocalSearchOptimisationStage localSearchOptimisationStage = (LocalSearchOptimisationStage)theEObject;
				T1 result = caseLocalSearchOptimisationStage(localSearchOptimisationStage);
				if (result == null) result = caseParallisableOptimisationStage(localSearchOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(localSearchOptimisationStage);
				if (result == null) result = caseOptimisationStage(localSearchOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE: {
				HillClimbOptimisationStage hillClimbOptimisationStage = (HillClimbOptimisationStage)theEObject;
				T1 result = caseHillClimbOptimisationStage(hillClimbOptimisationStage);
				if (result == null) result = caseParallisableOptimisationStage(hillClimbOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(hillClimbOptimisationStage);
				if (result == null) result = caseOptimisationStage(hillClimbOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.ACTION_PLAN_OPTIMISATION_STAGE: {
				ActionPlanOptimisationStage actionPlanOptimisationStage = (ActionPlanOptimisationStage)theEObject;
				T1 result = caseActionPlanOptimisationStage(actionPlanOptimisationStage);
				if (result == null) result = caseConstraintsAndFitnessSettingsStage(actionPlanOptimisationStage);
				if (result == null) result = caseOptimisationStage(actionPlanOptimisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.RESET_INITIAL_SEQUENCES_STAGE: {
				ResetInitialSequencesStage resetInitialSequencesStage = (ResetInitialSequencesStage)theEObject;
				T1 result = caseResetInitialSequencesStage(resetInitialSequencesStage);
				if (result == null) result = caseOptimisationStage(resetInitialSequencesStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.BREAK_EVEN_OPTMISATION_STAGE: {
				BreakEvenOptmisationStage breakEvenOptmisationStage = (BreakEvenOptmisationStage)theEObject;
				T1 result = caseBreakEvenOptmisationStage(breakEvenOptmisationStage);
				if (result == null) result = caseOptimisationStage(breakEvenOptmisationStage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ParametersPackage.SOLUTION_BUILDER_SETTINGS: {
				SolutionBuilderSettings solutionBuilderSettings = (SolutionBuilderSettings)theEObject;
				T1 result = caseSolutionBuilderSettings(solutionBuilderSettings);
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
	public T1 caseUserSettings(UserSettings object) {
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
	public T1 caseObjective(Objective object) {
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
	public T1 caseConstraint(Constraint object) {
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
	public T1 caseAnnealingSettings(AnnealingSettings object) {
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
	public T1 caseSimilaritySettings(SimilaritySettings object) {
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
	public T1 caseSimilarityInterval(SimilarityInterval object) {
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
	public T1 caseOptimisationStage(OptimisationStage object) {
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
	public T1 caseConstraintsAndFitnessSettingsStage(ConstraintsAndFitnessSettingsStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Parallisable Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Parallisable Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseParallisableOptimisationStage(ParallisableOptimisationStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Parallel Optimisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Parallel Optimisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends ParallisableOptimisationStage> T1 caseParallelOptimisationStage(ParallelOptimisationStage<T> object) {
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
	public T1 caseCleanStateOptimisationStage(CleanStateOptimisationStage object) {
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
	public T1 caseLocalSearchOptimisationStage(LocalSearchOptimisationStage object) {
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
	public T1 caseHillClimbOptimisationStage(HillClimbOptimisationStage object) {
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
	public T1 caseActionPlanOptimisationStage(ActionPlanOptimisationStage object) {
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
	public T1 caseResetInitialSequencesStage(ResetInitialSequencesStage object) {
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
	public T1 caseConstraintAndFitnessSettings(ConstraintAndFitnessSettings object) {
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
	public T1 caseOptimisationPlan(OptimisationPlan object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Break Even Optmisation Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Break Even Optmisation Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseBreakEvenOptmisationStage(BreakEvenOptmisationStage object) {
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
	public T1 caseSolutionBuilderSettings(SolutionBuilderSettings object) {
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
	public T1 caseMMXObject(MMXObject object) {
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
	public T1 caseNamedObject(NamedObject object) {
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
	public T1 defaultCase(EObject object) {
		return null;
	}

} //ParametersSwitch
