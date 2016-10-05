/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallisableOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.ResetInitialSequencesStage;
import com.mmxlabs.models.lng.parameters.SimilarityInterval;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ParametersFactoryImpl extends EFactoryImpl implements ParametersFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ParametersFactory init() {
		try {
			ParametersFactory theParametersFactory = (ParametersFactory)EPackage.Registry.INSTANCE.getEFactory(ParametersPackage.eNS_URI);
			if (theParametersFactory != null) {
				return theParametersFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ParametersFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParametersFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ParametersPackage.USER_SETTINGS: return createUserSettings();
			case ParametersPackage.OBJECTIVE: return createObjective();
			case ParametersPackage.CONSTRAINT: return createConstraint();
			case ParametersPackage.ANNEALING_SETTINGS: return createAnnealingSettings();
			case ParametersPackage.SIMILARITY_SETTINGS: return createSimilaritySettings();
			case ParametersPackage.SIMILARITY_INTERVAL: return createSimilarityInterval();
			case ParametersPackage.OPTIMISATION_PLAN: return createOptimisationPlan();
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS: return createConstraintAndFitnessSettings();
			case ParametersPackage.PARALLISABLE_OPTIMISATION_STAGE: return createParallisableOptimisationStage();
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE: return createParallelOptimisationStage();
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE: return createCleanStateOptimisationStage();
			case ParametersPackage.LOCAL_SEARCH_OPTIMISATION_STAGE: return createLocalSearchOptimisationStage();
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE: return createHillClimbOptimisationStage();
			case ParametersPackage.ACTION_PLAN_OPTIMISATION_STAGE: return createActionPlanOptimisationStage();
			case ParametersPackage.RESET_INITIAL_SEQUENCES_STAGE: return createResetInitialSequencesStage();
			case ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE: return createBreakEvenOptimisationStage();
			case ParametersPackage.SOLUTION_BUILDER_SETTINGS: return createSolutionBuilderSettings();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ParametersPackage.SIMILARITY_MODE:
				return createSimilarityModeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ParametersPackage.SIMILARITY_MODE:
				return convertSimilarityModeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public UserSettings createUserSettings() {
		UserSettingsImpl userSettings = new UserSettingsImpl();
		return userSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Objective createObjective() {
		ObjectiveImpl objective = new ObjectiveImpl();
		return objective;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Constraint createConstraint() {
		ConstraintImpl constraint = new ConstraintImpl();
		return constraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AnnealingSettings createAnnealingSettings() {
		AnnealingSettingsImpl annealingSettings = new AnnealingSettingsImpl();
		return annealingSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimilaritySettings createSimilaritySettings() {
		SimilaritySettingsImpl similaritySettings = new SimilaritySettingsImpl();
		return similaritySettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimilarityInterval createSimilarityInterval() {
		SimilarityIntervalImpl similarityInterval = new SimilarityIntervalImpl();
		return similarityInterval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ParallisableOptimisationStage createParallisableOptimisationStage() {
		ParallisableOptimisationStageImpl parallisableOptimisationStage = new ParallisableOptimisationStageImpl();
		return parallisableOptimisationStage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <T extends ParallisableOptimisationStage> ParallelOptimisationStage<T> createParallelOptimisationStage() {
		ParallelOptimisationStageImpl<T> parallelOptimisationStage = new ParallelOptimisationStageImpl<T>();
		return parallelOptimisationStage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CleanStateOptimisationStage createCleanStateOptimisationStage() {
		CleanStateOptimisationStageImpl cleanStateOptimisationStage = new CleanStateOptimisationStageImpl();
		return cleanStateOptimisationStage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalSearchOptimisationStage createLocalSearchOptimisationStage() {
		LocalSearchOptimisationStageImpl localSearchOptimisationStage = new LocalSearchOptimisationStageImpl();
		return localSearchOptimisationStage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public HillClimbOptimisationStage createHillClimbOptimisationStage() {
		HillClimbOptimisationStageImpl hillClimbOptimisationStage = new HillClimbOptimisationStageImpl();
		return hillClimbOptimisationStage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ActionPlanOptimisationStage createActionPlanOptimisationStage() {
		ActionPlanOptimisationStageImpl actionPlanOptimisationStage = new ActionPlanOptimisationStageImpl();
		return actionPlanOptimisationStage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResetInitialSequencesStage createResetInitialSequencesStage() {
		ResetInitialSequencesStageImpl resetInitialSequencesStage = new ResetInitialSequencesStageImpl();
		return resetInitialSequencesStage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BreakEvenOptimisationStage createBreakEvenOptimisationStage() {
		BreakEvenOptimisationStageImpl breakEvenOptimisationStage = new BreakEvenOptimisationStageImpl();
		return breakEvenOptimisationStage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ConstraintAndFitnessSettings createConstraintAndFitnessSettings() {
		ConstraintAndFitnessSettingsImpl constraintAndFitnessSettings = new ConstraintAndFitnessSettingsImpl();
		return constraintAndFitnessSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OptimisationPlan createOptimisationPlan() {
		OptimisationPlanImpl optimisationPlan = new OptimisationPlanImpl();
		return optimisationPlan;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SolutionBuilderSettings createSolutionBuilderSettings() {
		SolutionBuilderSettingsImpl solutionBuilderSettings = new SolutionBuilderSettingsImpl();
		return solutionBuilderSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimilarityMode createSimilarityModeFromString(EDataType eDataType, String initialValue) {
		SimilarityMode result = SimilarityMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSimilarityModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ParametersPackage getParametersPackage() {
		return (ParametersPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ParametersPackage getPackage() {
		return ParametersPackage.eINSTANCE;
	}

} //ParametersFactoryImpl
