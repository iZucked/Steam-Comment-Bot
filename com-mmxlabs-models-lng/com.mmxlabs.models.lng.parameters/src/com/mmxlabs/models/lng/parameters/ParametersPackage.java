/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.parameters.ParametersFactory
 * @model kind="package"
 * @generated
 */
public interface ParametersPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "parameters";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/parameters/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.parameters";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ParametersPackage eINSTANCE = com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl <em>User Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getUserSettings()
	 * @generated
	 */
	int USER_SETTINGS = 0;

	/**
	 * The feature id for the '<em><b>Period Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__PERIOD_START = 0;

	/**
	 * The feature id for the '<em><b>Period End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__PERIOD_END = 1;

	/**
	 * The feature id for the '<em><b>Shipping Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__SHIPPING_ONLY = 2;

	/**
	 * The feature id for the '<em><b>Generate Charter Outs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__GENERATE_CHARTER_OUTS = 3;

	/**
	 * The feature id for the '<em><b>With Spot Cargo Markets</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__WITH_SPOT_CARGO_MARKETS = 4;

	/**
	 * The feature id for the '<em><b>Build Action Sets</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__BUILD_ACTION_SETS = 5;

	/**
	 * The feature id for the '<em><b>Similarity Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__SIMILARITY_MODE = 6;

	/**
	 * The feature id for the '<em><b>Clean State Optimisation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__CLEAN_STATE_OPTIMISATION = 7;

	/**
	 * The feature id for the '<em><b>Floating Days Limit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__FLOATING_DAYS_LIMIT = 8;

	/**
	 * The number of structural features of the '<em>User Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS_FEATURE_COUNT = 9;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ObjectiveImpl <em>Objective</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ObjectiveImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getObjective()
	 * @generated
	 */
	int OBJECTIVE = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__WEIGHT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__ENABLED = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Objective</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ConstraintImpl <em>Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ConstraintImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getConstraint()
	 * @generated
	 */
	int CONSTRAINT = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__ENABLED = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl <em>Annealing Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getAnnealingSettings()
	 * @generated
	 */
	int ANNEALING_SETTINGS = 3;

	/**
	 * The feature id for the '<em><b>Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNEALING_SETTINGS__ITERATIONS = 0;

	/**
	 * The feature id for the '<em><b>Epoch Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNEALING_SETTINGS__EPOCH_LENGTH = 1;

	/**
	 * The feature id for the '<em><b>Cooling</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNEALING_SETTINGS__COOLING = 2;

	/**
	 * The feature id for the '<em><b>Initial Temperature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNEALING_SETTINGS__INITIAL_TEMPERATURE = 3;

	/**
	 * The feature id for the '<em><b>Restarting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNEALING_SETTINGS__RESTARTING = 4;

	/**
	 * The feature id for the '<em><b>Restart Iterations Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNEALING_SETTINGS__RESTART_ITERATIONS_THRESHOLD = 5;

	/**
	 * The number of structural features of the '<em>Annealing Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNEALING_SETTINGS_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl <em>Similarity Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getSimilaritySettings()
	 * @generated
	 */
	int SIMILARITY_SETTINGS = 4;

	/**
	 * The feature id for the '<em><b>Low Interval</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMILARITY_SETTINGS__LOW_INTERVAL = 0;

	/**
	 * The feature id for the '<em><b>Med Interval</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMILARITY_SETTINGS__MED_INTERVAL = 1;

	/**
	 * The feature id for the '<em><b>High Interval</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMILARITY_SETTINGS__HIGH_INTERVAL = 2;

	/**
	 * The feature id for the '<em><b>Out Of Bounds Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMILARITY_SETTINGS__OUT_OF_BOUNDS_WEIGHT = 3;

	/**
	 * The number of structural features of the '<em>Similarity Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMILARITY_SETTINGS_FEATURE_COUNT = 4;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.SimilarityIntervalImpl <em>Similarity Interval</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.SimilarityIntervalImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getSimilarityInterval()
	 * @generated
	 */
	int SIMILARITY_INTERVAL = 5;

	/**
	 * The feature id for the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMILARITY_INTERVAL__WEIGHT = 0;

	/**
	 * The feature id for the '<em><b>Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMILARITY_INTERVAL__THRESHOLD = 1;

	/**
	 * The number of structural features of the '<em>Similarity Interval</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMILARITY_INTERVAL_FEATURE_COUNT = 2;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.OptimisationStage <em>Optimisation Stage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.OptimisationStage
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getOptimisationStage()
	 * @generated
	 */
	int OPTIMISATION_STAGE = 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ParallisableOptimisationStageImpl <em>Parallisable Optimisation Stage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ParallisableOptimisationStageImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getParallisableOptimisationStage()
	 * @generated
	 */
	int PARALLISABLE_OPTIMISATION_STAGE = 10;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ParallelOptimisationStageImpl <em>Parallel Optimisation Stage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ParallelOptimisationStageImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getParallelOptimisationStage()
	 * @generated
	 */
	int PARALLEL_OPTIMISATION_STAGE = 11;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.CleanStateOptimisationStageImpl <em>Clean State Optimisation Stage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.CleanStateOptimisationStageImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getCleanStateOptimisationStage()
	 * @generated
	 */
	int CLEAN_STATE_OPTIMISATION_STAGE = 12;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.LocalSearchOptimisationStageImpl <em>Local Search Optimisation Stage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.LocalSearchOptimisationStageImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getLocalSearchOptimisationStage()
	 * @generated
	 */
	int LOCAL_SEARCH_OPTIMISATION_STAGE = 13;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.HillClimbOptimisationStageImpl <em>Hill Climb Optimisation Stage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.HillClimbOptimisationStageImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getHillClimbOptimisationStage()
	 * @generated
	 */
	int HILL_CLIMB_OPTIMISATION_STAGE = 14;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ActionPlanOptimisationStageImpl <em>Action Plan Optimisation Stage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ActionPlanOptimisationStageImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getActionPlanOptimisationStage()
	 * @generated
	 */
	int ACTION_PLAN_OPTIMISATION_STAGE = 15;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ResetInitialSequencesStageImpl <em>Reset Initial Sequences Stage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ResetInitialSequencesStageImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getResetInitialSequencesStage()
	 * @generated
	 */
	int RESET_INITIAL_SEQUENCES_STAGE = 16;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ConstraintAndFitnessSettingsImpl <em>Constraint And Fitness Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ConstraintAndFitnessSettingsImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getConstraintAndFitnessSettings()
	 * @generated
	 */
	int CONSTRAINT_AND_FITNESS_SETTINGS = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.OptimisationPlanImpl <em>Optimisation Plan</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.OptimisationPlanImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getOptimisationPlan()
	 * @generated
	 */
	int OPTIMISATION_PLAN = 6;

	/**
	 * The feature id for the '<em><b>User Settings</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_PLAN__USER_SETTINGS = 0;

	/**
	 * The feature id for the '<em><b>Stages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_PLAN__STAGES = 1;

	/**
	 * The feature id for the '<em><b>Solution Builder Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS = 2;

	/**
	 * The number of structural features of the '<em>Optimisation Plan</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_PLAN_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Objectives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_AND_FITNESS_SETTINGS__OBJECTIVES = 0;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_AND_FITNESS_SETTINGS__CONSTRAINTS = 1;

	/**
	 * The feature id for the '<em><b>Floating Days Limit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_AND_FITNESS_SETTINGS__FLOATING_DAYS_LIMIT = 2;

	/**
	 * The feature id for the '<em><b>Similarity Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS = 3;

	/**
	 * The number of structural features of the '<em>Constraint And Fitness Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_AND_FITNESS_SETTINGS_FEATURE_COUNT = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_STAGE__NAME = 0;

	/**
	 * The number of structural features of the '<em>Optimisation Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_STAGE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ConstraintsAndFitnessSettingsStageImpl <em>Constraints And Fitness Settings Stage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ConstraintsAndFitnessSettingsStageImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getConstraintsAndFitnessSettingsStage()
	 * @generated
	 */
	int CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE__NAME = OPTIMISATION_STAGE__NAME;

	/**
	 * The feature id for the '<em><b>Constraint And Fitness Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS = OPTIMISATION_STAGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Constraints And Fitness Settings Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE_FEATURE_COUNT = OPTIMISATION_STAGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARALLISABLE_OPTIMISATION_STAGE__NAME = OPTIMISATION_STAGE__NAME;

	/**
	 * The number of structural features of the '<em>Parallisable Optimisation Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT = OPTIMISATION_STAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARALLEL_OPTIMISATION_STAGE__NAME = OPTIMISATION_STAGE__NAME;

	/**
	 * The feature id for the '<em><b>Job Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARALLEL_OPTIMISATION_STAGE__JOB_COUNT = OPTIMISATION_STAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Template</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARALLEL_OPTIMISATION_STAGE__TEMPLATE = OPTIMISATION_STAGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Parallel Optimisation Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARALLEL_OPTIMISATION_STAGE_FEATURE_COUNT = OPTIMISATION_STAGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLEAN_STATE_OPTIMISATION_STAGE__NAME = PARALLISABLE_OPTIMISATION_STAGE__NAME;

	/**
	 * The feature id for the '<em><b>Constraint And Fitness Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLEAN_STATE_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLEAN_STATE_OPTIMISATION_STAGE__SEED = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Annealing Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Clean State Optimisation Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLEAN_STATE_OPTIMISATION_STAGE_FEATURE_COUNT = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_SEARCH_OPTIMISATION_STAGE__NAME = PARALLISABLE_OPTIMISATION_STAGE__NAME;

	/**
	 * The feature id for the '<em><b>Constraint And Fitness Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_SEARCH_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_SEARCH_OPTIMISATION_STAGE__SEED = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Annealing Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_SEARCH_OPTIMISATION_STAGE__ANNEALING_SETTINGS = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Local Search Optimisation Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_SEARCH_OPTIMISATION_STAGE_FEATURE_COUNT = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HILL_CLIMB_OPTIMISATION_STAGE__NAME = PARALLISABLE_OPTIMISATION_STAGE__NAME;

	/**
	 * The feature id for the '<em><b>Constraint And Fitness Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HILL_CLIMB_OPTIMISATION_STAGE__SEED = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Annealing Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Hill Climb Optimisation Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HILL_CLIMB_OPTIMISATION_STAGE_FEATURE_COUNT = PARALLISABLE_OPTIMISATION_STAGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_PLAN_OPTIMISATION_STAGE__NAME = CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE__NAME;

	/**
	 * The feature id for the '<em><b>Constraint And Fitness Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_PLAN_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS = CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS;

	/**
	 * The feature id for the '<em><b>Total Evaluations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_PLAN_OPTIMISATION_STAGE__TOTAL_EVALUATIONS = CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>In Run Evaluations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_PLAN_OPTIMISATION_STAGE__IN_RUN_EVALUATIONS = CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Search Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_PLAN_OPTIMISATION_STAGE__SEARCH_DEPTH = CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Action Plan Optimisation Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_PLAN_OPTIMISATION_STAGE_FEATURE_COUNT = CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESET_INITIAL_SEQUENCES_STAGE__NAME = OPTIMISATION_STAGE__NAME;

	/**
	 * The number of structural features of the '<em>Reset Initial Sequences Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESET_INITIAL_SEQUENCES_STAGE_FEATURE_COUNT = OPTIMISATION_STAGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.BreakEvenOptimisationStageImpl <em>Break Even Optimisation Stage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.BreakEvenOptimisationStageImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getBreakEvenOptimisationStage()
	 * @generated
	 */
	int BREAK_EVEN_OPTIMISATION_STAGE = 17;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_OPTIMISATION_STAGE__NAME = OPTIMISATION_STAGE__NAME;

	/**
	 * The feature id for the '<em><b>Target Profit And Loss</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_OPTIMISATION_STAGE__TARGET_PROFIT_AND_LOSS = OPTIMISATION_STAGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Break Even Optimisation Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_OPTIMISATION_STAGE_FEATURE_COUNT = OPTIMISATION_STAGE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.SolutionBuilderSettingsImpl <em>Solution Builder Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.SolutionBuilderSettingsImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getSolutionBuilderSettings()
	 * @generated
	 */
	int SOLUTION_BUILDER_SETTINGS = 18;

	/**
	 * The feature id for the '<em><b>Constraint And Fitness Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS = 0;

	/**
	 * The number of structural features of the '<em>Solution Builder Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_BUILDER_SETTINGS_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.SimilarityMode <em>Similarity Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.SimilarityMode
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getSimilarityMode()
	 * @generated
	 */
	int SIMILARITY_MODE = 19;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.UserSettings <em>User Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>User Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.UserSettings
	 * @generated
	 */
	EClass getUserSettings();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodStart <em>Period Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Period Start</em>'.
	 * @see com.mmxlabs.models.lng.parameters.UserSettings#getPeriodStart()
	 * @see #getUserSettings()
	 * @generated
	 */
	EAttribute getUserSettings_PeriodStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.UserSettings#getPeriodEnd <em>Period End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Period End</em>'.
	 * @see com.mmxlabs.models.lng.parameters.UserSettings#getPeriodEnd()
	 * @see #getUserSettings()
	 * @generated
	 */
	EAttribute getUserSettings_PeriodEnd();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.UserSettings#isShippingOnly <em>Shipping Only</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shipping Only</em>'.
	 * @see com.mmxlabs.models.lng.parameters.UserSettings#isShippingOnly()
	 * @see #getUserSettings()
	 * @generated
	 */
	EAttribute getUserSettings_ShippingOnly();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.UserSettings#isGenerateCharterOuts <em>Generate Charter Outs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Charter Outs</em>'.
	 * @see com.mmxlabs.models.lng.parameters.UserSettings#isGenerateCharterOuts()
	 * @see #getUserSettings()
	 * @generated
	 */
	EAttribute getUserSettings_GenerateCharterOuts();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.UserSettings#isWithSpotCargoMarkets <em>With Spot Cargo Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>With Spot Cargo Markets</em>'.
	 * @see com.mmxlabs.models.lng.parameters.UserSettings#isWithSpotCargoMarkets()
	 * @see #getUserSettings()
	 * @generated
	 */
	EAttribute getUserSettings_WithSpotCargoMarkets();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.UserSettings#isBuildActionSets <em>Build Action Sets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Build Action Sets</em>'.
	 * @see com.mmxlabs.models.lng.parameters.UserSettings#isBuildActionSets()
	 * @see #getUserSettings()
	 * @generated
	 */
	EAttribute getUserSettings_BuildActionSets();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.UserSettings#getSimilarityMode <em>Similarity Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Similarity Mode</em>'.
	 * @see com.mmxlabs.models.lng.parameters.UserSettings#getSimilarityMode()
	 * @see #getUserSettings()
	 * @generated
	 */
	EAttribute getUserSettings_SimilarityMode();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.UserSettings#isCleanStateOptimisation <em>Clean State Optimisation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Clean State Optimisation</em>'.
	 * @see com.mmxlabs.models.lng.parameters.UserSettings#isCleanStateOptimisation()
	 * @see #getUserSettings()
	 * @generated
	 */
	EAttribute getUserSettings_CleanStateOptimisation();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.UserSettings#getFloatingDaysLimit <em>Floating Days Limit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Floating Days Limit</em>'.
	 * @see com.mmxlabs.models.lng.parameters.UserSettings#getFloatingDaysLimit()
	 * @see #getUserSettings()
	 * @generated
	 */
	EAttribute getUserSettings_FloatingDaysLimit();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.Objective <em>Objective</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Objective</em>'.
	 * @see com.mmxlabs.models.lng.parameters.Objective
	 * @generated
	 */
	EClass getObjective();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.Objective#getWeight <em>Weight</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Weight</em>'.
	 * @see com.mmxlabs.models.lng.parameters.Objective#getWeight()
	 * @see #getObjective()
	 * @generated
	 */
	EAttribute getObjective_Weight();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.Objective#isEnabled <em>Enabled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Enabled</em>'.
	 * @see com.mmxlabs.models.lng.parameters.Objective#isEnabled()
	 * @see #getObjective()
	 * @generated
	 */
	EAttribute getObjective_Enabled();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.Constraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraint</em>'.
	 * @see com.mmxlabs.models.lng.parameters.Constraint
	 * @generated
	 */
	EClass getConstraint();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.Constraint#isEnabled <em>Enabled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Enabled</em>'.
	 * @see com.mmxlabs.models.lng.parameters.Constraint#isEnabled()
	 * @see #getConstraint()
	 * @generated
	 */
	EAttribute getConstraint_Enabled();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.AnnealingSettings <em>Annealing Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annealing Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.AnnealingSettings
	 * @generated
	 */
	EClass getAnnealingSettings();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getIterations <em>Iterations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Iterations</em>'.
	 * @see com.mmxlabs.models.lng.parameters.AnnealingSettings#getIterations()
	 * @see #getAnnealingSettings()
	 * @generated
	 */
	EAttribute getAnnealingSettings_Iterations();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getEpochLength <em>Epoch Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Epoch Length</em>'.
	 * @see com.mmxlabs.models.lng.parameters.AnnealingSettings#getEpochLength()
	 * @see #getAnnealingSettings()
	 * @generated
	 */
	EAttribute getAnnealingSettings_EpochLength();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getCooling <em>Cooling</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cooling</em>'.
	 * @see com.mmxlabs.models.lng.parameters.AnnealingSettings#getCooling()
	 * @see #getAnnealingSettings()
	 * @generated
	 */
	EAttribute getAnnealingSettings_Cooling();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getInitialTemperature <em>Initial Temperature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Initial Temperature</em>'.
	 * @see com.mmxlabs.models.lng.parameters.AnnealingSettings#getInitialTemperature()
	 * @see #getAnnealingSettings()
	 * @generated
	 */
	EAttribute getAnnealingSettings_InitialTemperature();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#isRestarting <em>Restarting</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restarting</em>'.
	 * @see com.mmxlabs.models.lng.parameters.AnnealingSettings#isRestarting()
	 * @see #getAnnealingSettings()
	 * @generated
	 */
	EAttribute getAnnealingSettings_Restarting();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getRestartIterationsThreshold <em>Restart Iterations Threshold</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restart Iterations Threshold</em>'.
	 * @see com.mmxlabs.models.lng.parameters.AnnealingSettings#getRestartIterationsThreshold()
	 * @see #getAnnealingSettings()
	 * @generated
	 */
	EAttribute getAnnealingSettings_RestartIterationsThreshold();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings <em>Similarity Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Similarity Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.SimilaritySettings
	 * @generated
	 */
	EClass getSimilaritySettings();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getLowInterval <em>Low Interval</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Low Interval</em>'.
	 * @see com.mmxlabs.models.lng.parameters.SimilaritySettings#getLowInterval()
	 * @see #getSimilaritySettings()
	 * @generated
	 */
	EReference getSimilaritySettings_LowInterval();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getMedInterval <em>Med Interval</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Med Interval</em>'.
	 * @see com.mmxlabs.models.lng.parameters.SimilaritySettings#getMedInterval()
	 * @see #getSimilaritySettings()
	 * @generated
	 */
	EReference getSimilaritySettings_MedInterval();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getHighInterval <em>High Interval</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>High Interval</em>'.
	 * @see com.mmxlabs.models.lng.parameters.SimilaritySettings#getHighInterval()
	 * @see #getSimilaritySettings()
	 * @generated
	 */
	EReference getSimilaritySettings_HighInterval();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getOutOfBoundsWeight <em>Out Of Bounds Weight</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Out Of Bounds Weight</em>'.
	 * @see com.mmxlabs.models.lng.parameters.SimilaritySettings#getOutOfBoundsWeight()
	 * @see #getSimilaritySettings()
	 * @generated
	 */
	EAttribute getSimilaritySettings_OutOfBoundsWeight();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.SimilarityInterval <em>Similarity Interval</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Similarity Interval</em>'.
	 * @see com.mmxlabs.models.lng.parameters.SimilarityInterval
	 * @generated
	 */
	EClass getSimilarityInterval();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.SimilarityInterval#getWeight <em>Weight</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Weight</em>'.
	 * @see com.mmxlabs.models.lng.parameters.SimilarityInterval#getWeight()
	 * @see #getSimilarityInterval()
	 * @generated
	 */
	EAttribute getSimilarityInterval_Weight();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.SimilarityInterval#getThreshold <em>Threshold</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Threshold</em>'.
	 * @see com.mmxlabs.models.lng.parameters.SimilarityInterval#getThreshold()
	 * @see #getSimilarityInterval()
	 * @generated
	 */
	EAttribute getSimilarityInterval_Threshold();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.OptimisationStage <em>Optimisation Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Optimisation Stage</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimisationStage
	 * @generated
	 */
	EClass getOptimisationStage();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.OptimisationStage#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimisationStage#getName()
	 * @see #getOptimisationStage()
	 * @generated
	 */
	EAttribute getOptimisationStage_Name();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage <em>Constraints And Fitness Settings Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraints And Fitness Settings Stage</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage
	 * @generated
	 */
	EClass getConstraintsAndFitnessSettingsStage();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage#getConstraintAndFitnessSettings <em>Constraint And Fitness Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Constraint And Fitness Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage#getConstraintAndFitnessSettings()
	 * @see #getConstraintsAndFitnessSettingsStage()
	 * @generated
	 */
	EReference getConstraintsAndFitnessSettingsStage_ConstraintAndFitnessSettings();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.ParallisableOptimisationStage <em>Parallisable Optimisation Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parallisable Optimisation Stage</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ParallisableOptimisationStage
	 * @generated
	 */
	EClass getParallisableOptimisationStage();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.ParallelOptimisationStage <em>Parallel Optimisation Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parallel Optimisation Stage</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ParallelOptimisationStage
	 * @generated
	 */
	EClass getParallelOptimisationStage();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.ParallelOptimisationStage#getJobCount <em>Job Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Job Count</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ParallelOptimisationStage#getJobCount()
	 * @see #getParallelOptimisationStage()
	 * @generated
	 */
	EAttribute getParallelOptimisationStage_JobCount();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.ParallelOptimisationStage#getTemplate <em>Template</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Template</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ParallelOptimisationStage#getTemplate()
	 * @see #getParallelOptimisationStage()
	 * @generated
	 */
	EReference getParallelOptimisationStage_Template();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage <em>Clean State Optimisation Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Clean State Optimisation Stage</em>'.
	 * @see com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage
	 * @generated
	 */
	EClass getCleanStateOptimisationStage();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage#getSeed <em>Seed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Seed</em>'.
	 * @see com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage#getSeed()
	 * @see #getCleanStateOptimisationStage()
	 * @generated
	 */
	EAttribute getCleanStateOptimisationStage_Seed();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage#getAnnealingSettings <em>Annealing Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Annealing Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage#getAnnealingSettings()
	 * @see #getCleanStateOptimisationStage()
	 * @generated
	 */
	EReference getCleanStateOptimisationStage_AnnealingSettings();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage <em>Local Search Optimisation Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Local Search Optimisation Stage</em>'.
	 * @see com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage
	 * @generated
	 */
	EClass getLocalSearchOptimisationStage();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage#getSeed <em>Seed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Seed</em>'.
	 * @see com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage#getSeed()
	 * @see #getLocalSearchOptimisationStage()
	 * @generated
	 */
	EAttribute getLocalSearchOptimisationStage_Seed();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage#getAnnealingSettings <em>Annealing Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Annealing Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage#getAnnealingSettings()
	 * @see #getLocalSearchOptimisationStage()
	 * @generated
	 */
	EReference getLocalSearchOptimisationStage_AnnealingSettings();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage <em>Hill Climb Optimisation Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Hill Climb Optimisation Stage</em>'.
	 * @see com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage
	 * @generated
	 */
	EClass getHillClimbOptimisationStage();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage#getSeed <em>Seed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Seed</em>'.
	 * @see com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage#getSeed()
	 * @see #getHillClimbOptimisationStage()
	 * @generated
	 */
	EAttribute getHillClimbOptimisationStage_Seed();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage#getAnnealingSettings <em>Annealing Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Annealing Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage#getAnnealingSettings()
	 * @see #getHillClimbOptimisationStage()
	 * @generated
	 */
	EReference getHillClimbOptimisationStage_AnnealingSettings();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage <em>Action Plan Optimisation Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Action Plan Optimisation Stage</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage
	 * @generated
	 */
	EClass getActionPlanOptimisationStage();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage#getTotalEvaluations <em>Total Evaluations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Total Evaluations</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage#getTotalEvaluations()
	 * @see #getActionPlanOptimisationStage()
	 * @generated
	 */
	EAttribute getActionPlanOptimisationStage_TotalEvaluations();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage#getInRunEvaluations <em>In Run Evaluations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>In Run Evaluations</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage#getInRunEvaluations()
	 * @see #getActionPlanOptimisationStage()
	 * @generated
	 */
	EAttribute getActionPlanOptimisationStage_InRunEvaluations();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage#getSearchDepth <em>Search Depth</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Search Depth</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage#getSearchDepth()
	 * @see #getActionPlanOptimisationStage()
	 * @generated
	 */
	EAttribute getActionPlanOptimisationStage_SearchDepth();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.ResetInitialSequencesStage <em>Reset Initial Sequences Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reset Initial Sequences Stage</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ResetInitialSequencesStage
	 * @generated
	 */
	EClass getResetInitialSequencesStage();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage <em>Break Even Optimisation Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Break Even Optimisation Stage</em>'.
	 * @see com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage
	 * @generated
	 */
	EClass getBreakEvenOptimisationStage();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage#getTargetProfitAndLoss <em>Target Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target Profit And Loss</em>'.
	 * @see com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage#getTargetProfitAndLoss()
	 * @see #getBreakEvenOptimisationStage()
	 * @generated
	 */
	EAttribute getBreakEvenOptimisationStage_TargetProfitAndLoss();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings <em>Constraint And Fitness Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraint And Fitness Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings
	 * @generated
	 */
	EClass getConstraintAndFitnessSettings();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getObjectives <em>Objectives</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Objectives</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getObjectives()
	 * @see #getConstraintAndFitnessSettings()
	 * @generated
	 */
	EReference getConstraintAndFitnessSettings_Objectives();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getConstraints <em>Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Constraints</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getConstraints()
	 * @see #getConstraintAndFitnessSettings()
	 * @generated
	 */
	EReference getConstraintAndFitnessSettings_Constraints();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getFloatingDaysLimit <em>Floating Days Limit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Floating Days Limit</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getFloatingDaysLimit()
	 * @see #getConstraintAndFitnessSettings()
	 * @generated
	 */
	EAttribute getConstraintAndFitnessSettings_FloatingDaysLimit();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getSimilaritySettings <em>Similarity Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Similarity Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getSimilaritySettings()
	 * @see #getConstraintAndFitnessSettings()
	 * @generated
	 */
	EReference getConstraintAndFitnessSettings_SimilaritySettings();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.OptimisationPlan <em>Optimisation Plan</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Optimisation Plan</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimisationPlan
	 * @generated
	 */
	EClass getOptimisationPlan();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.parameters.OptimisationPlan#getUserSettings <em>User Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>User Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimisationPlan#getUserSettings()
	 * @see #getOptimisationPlan()
	 * @generated
	 */
	EReference getOptimisationPlan_UserSettings();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.parameters.OptimisationPlan#getStages <em>Stages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Stages</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimisationPlan#getStages()
	 * @see #getOptimisationPlan()
	 * @generated
	 */
	EReference getOptimisationPlan_Stages();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.OptimisationPlan#getSolutionBuilderSettings <em>Solution Builder Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Solution Builder Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimisationPlan#getSolutionBuilderSettings()
	 * @see #getOptimisationPlan()
	 * @generated
	 */
	EReference getOptimisationPlan_SolutionBuilderSettings();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.SolutionBuilderSettings <em>Solution Builder Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Solution Builder Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.SolutionBuilderSettings
	 * @generated
	 */
	EClass getSolutionBuilderSettings();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.SolutionBuilderSettings#getConstraintAndFitnessSettings <em>Constraint And Fitness Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Constraint And Fitness Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.SolutionBuilderSettings#getConstraintAndFitnessSettings()
	 * @see #getSolutionBuilderSettings()
	 * @generated
	 */
	EReference getSolutionBuilderSettings_ConstraintAndFitnessSettings();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.parameters.SimilarityMode <em>Similarity Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Similarity Mode</em>'.
	 * @see com.mmxlabs.models.lng.parameters.SimilarityMode
	 * @generated
	 */
	EEnum getSimilarityMode();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ParametersFactory getParametersFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl <em>User Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getUserSettings()
		 * @generated
		 */
		EClass USER_SETTINGS = eINSTANCE.getUserSettings();

		/**
		 * The meta object literal for the '<em><b>Period Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_SETTINGS__PERIOD_START = eINSTANCE.getUserSettings_PeriodStart();

		/**
		 * The meta object literal for the '<em><b>Period End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_SETTINGS__PERIOD_END = eINSTANCE.getUserSettings_PeriodEnd();

		/**
		 * The meta object literal for the '<em><b>Shipping Only</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_SETTINGS__SHIPPING_ONLY = eINSTANCE.getUserSettings_ShippingOnly();

		/**
		 * The meta object literal for the '<em><b>Generate Charter Outs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_SETTINGS__GENERATE_CHARTER_OUTS = eINSTANCE.getUserSettings_GenerateCharterOuts();

		/**
		 * The meta object literal for the '<em><b>With Spot Cargo Markets</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_SETTINGS__WITH_SPOT_CARGO_MARKETS = eINSTANCE.getUserSettings_WithSpotCargoMarkets();

		/**
		 * The meta object literal for the '<em><b>Build Action Sets</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_SETTINGS__BUILD_ACTION_SETS = eINSTANCE.getUserSettings_BuildActionSets();

		/**
		 * The meta object literal for the '<em><b>Similarity Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_SETTINGS__SIMILARITY_MODE = eINSTANCE.getUserSettings_SimilarityMode();

		/**
		 * The meta object literal for the '<em><b>Clean State Optimisation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_SETTINGS__CLEAN_STATE_OPTIMISATION = eINSTANCE.getUserSettings_CleanStateOptimisation();

		/**
		 * The meta object literal for the '<em><b>Floating Days Limit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_SETTINGS__FLOATING_DAYS_LIMIT = eINSTANCE.getUserSettings_FloatingDaysLimit();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.ObjectiveImpl <em>Objective</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.ObjectiveImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getObjective()
		 * @generated
		 */
		EClass OBJECTIVE = eINSTANCE.getObjective();

		/**
		 * The meta object literal for the '<em><b>Weight</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OBJECTIVE__WEIGHT = eINSTANCE.getObjective_Weight();

		/**
		 * The meta object literal for the '<em><b>Enabled</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OBJECTIVE__ENABLED = eINSTANCE.getObjective_Enabled();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.ConstraintImpl <em>Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.ConstraintImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getConstraint()
		 * @generated
		 */
		EClass CONSTRAINT = eINSTANCE.getConstraint();

		/**
		 * The meta object literal for the '<em><b>Enabled</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT__ENABLED = eINSTANCE.getConstraint_Enabled();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl <em>Annealing Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getAnnealingSettings()
		 * @generated
		 */
		EClass ANNEALING_SETTINGS = eINSTANCE.getAnnealingSettings();

		/**
		 * The meta object literal for the '<em><b>Iterations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNEALING_SETTINGS__ITERATIONS = eINSTANCE.getAnnealingSettings_Iterations();

		/**
		 * The meta object literal for the '<em><b>Epoch Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNEALING_SETTINGS__EPOCH_LENGTH = eINSTANCE.getAnnealingSettings_EpochLength();

		/**
		 * The meta object literal for the '<em><b>Cooling</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNEALING_SETTINGS__COOLING = eINSTANCE.getAnnealingSettings_Cooling();

		/**
		 * The meta object literal for the '<em><b>Initial Temperature</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNEALING_SETTINGS__INITIAL_TEMPERATURE = eINSTANCE.getAnnealingSettings_InitialTemperature();

		/**
		 * The meta object literal for the '<em><b>Restarting</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNEALING_SETTINGS__RESTARTING = eINSTANCE.getAnnealingSettings_Restarting();

		/**
		 * The meta object literal for the '<em><b>Restart Iterations Threshold</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNEALING_SETTINGS__RESTART_ITERATIONS_THRESHOLD = eINSTANCE.getAnnealingSettings_RestartIterationsThreshold();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl <em>Similarity Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getSimilaritySettings()
		 * @generated
		 */
		EClass SIMILARITY_SETTINGS = eINSTANCE.getSimilaritySettings();

		/**
		 * The meta object literal for the '<em><b>Low Interval</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SIMILARITY_SETTINGS__LOW_INTERVAL = eINSTANCE.getSimilaritySettings_LowInterval();

		/**
		 * The meta object literal for the '<em><b>Med Interval</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SIMILARITY_SETTINGS__MED_INTERVAL = eINSTANCE.getSimilaritySettings_MedInterval();

		/**
		 * The meta object literal for the '<em><b>High Interval</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SIMILARITY_SETTINGS__HIGH_INTERVAL = eINSTANCE.getSimilaritySettings_HighInterval();

		/**
		 * The meta object literal for the '<em><b>Out Of Bounds Weight</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SIMILARITY_SETTINGS__OUT_OF_BOUNDS_WEIGHT = eINSTANCE.getSimilaritySettings_OutOfBoundsWeight();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.SimilarityIntervalImpl <em>Similarity Interval</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.SimilarityIntervalImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getSimilarityInterval()
		 * @generated
		 */
		EClass SIMILARITY_INTERVAL = eINSTANCE.getSimilarityInterval();

		/**
		 * The meta object literal for the '<em><b>Weight</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SIMILARITY_INTERVAL__WEIGHT = eINSTANCE.getSimilarityInterval_Weight();

		/**
		 * The meta object literal for the '<em><b>Threshold</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SIMILARITY_INTERVAL__THRESHOLD = eINSTANCE.getSimilarityInterval_Threshold();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.OptimisationStage <em>Optimisation Stage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.OptimisationStage
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getOptimisationStage()
		 * @generated
		 */
		EClass OPTIMISATION_STAGE = eINSTANCE.getOptimisationStage();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISATION_STAGE__NAME = eINSTANCE.getOptimisationStage_Name();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.ConstraintsAndFitnessSettingsStageImpl <em>Constraints And Fitness Settings Stage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.ConstraintsAndFitnessSettingsStageImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getConstraintsAndFitnessSettingsStage()
		 * @generated
		 */
		EClass CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE = eINSTANCE.getConstraintsAndFitnessSettingsStage();

		/**
		 * The meta object literal for the '<em><b>Constraint And Fitness Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS = eINSTANCE.getConstraintsAndFitnessSettingsStage_ConstraintAndFitnessSettings();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.ParallisableOptimisationStageImpl <em>Parallisable Optimisation Stage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.ParallisableOptimisationStageImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getParallisableOptimisationStage()
		 * @generated
		 */
		EClass PARALLISABLE_OPTIMISATION_STAGE = eINSTANCE.getParallisableOptimisationStage();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.ParallelOptimisationStageImpl <em>Parallel Optimisation Stage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.ParallelOptimisationStageImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getParallelOptimisationStage()
		 * @generated
		 */
		EClass PARALLEL_OPTIMISATION_STAGE = eINSTANCE.getParallelOptimisationStage();

		/**
		 * The meta object literal for the '<em><b>Job Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARALLEL_OPTIMISATION_STAGE__JOB_COUNT = eINSTANCE.getParallelOptimisationStage_JobCount();

		/**
		 * The meta object literal for the '<em><b>Template</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARALLEL_OPTIMISATION_STAGE__TEMPLATE = eINSTANCE.getParallelOptimisationStage_Template();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.CleanStateOptimisationStageImpl <em>Clean State Optimisation Stage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.CleanStateOptimisationStageImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getCleanStateOptimisationStage()
		 * @generated
		 */
		EClass CLEAN_STATE_OPTIMISATION_STAGE = eINSTANCE.getCleanStateOptimisationStage();

		/**
		 * The meta object literal for the '<em><b>Seed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLEAN_STATE_OPTIMISATION_STAGE__SEED = eINSTANCE.getCleanStateOptimisationStage_Seed();

		/**
		 * The meta object literal for the '<em><b>Annealing Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS = eINSTANCE.getCleanStateOptimisationStage_AnnealingSettings();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.LocalSearchOptimisationStageImpl <em>Local Search Optimisation Stage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.LocalSearchOptimisationStageImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getLocalSearchOptimisationStage()
		 * @generated
		 */
		EClass LOCAL_SEARCH_OPTIMISATION_STAGE = eINSTANCE.getLocalSearchOptimisationStage();

		/**
		 * The meta object literal for the '<em><b>Seed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCAL_SEARCH_OPTIMISATION_STAGE__SEED = eINSTANCE.getLocalSearchOptimisationStage_Seed();

		/**
		 * The meta object literal for the '<em><b>Annealing Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LOCAL_SEARCH_OPTIMISATION_STAGE__ANNEALING_SETTINGS = eINSTANCE.getLocalSearchOptimisationStage_AnnealingSettings();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.HillClimbOptimisationStageImpl <em>Hill Climb Optimisation Stage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.HillClimbOptimisationStageImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getHillClimbOptimisationStage()
		 * @generated
		 */
		EClass HILL_CLIMB_OPTIMISATION_STAGE = eINSTANCE.getHillClimbOptimisationStage();

		/**
		 * The meta object literal for the '<em><b>Seed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HILL_CLIMB_OPTIMISATION_STAGE__SEED = eINSTANCE.getHillClimbOptimisationStage_Seed();

		/**
		 * The meta object literal for the '<em><b>Annealing Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS = eINSTANCE.getHillClimbOptimisationStage_AnnealingSettings();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.ActionPlanOptimisationStageImpl <em>Action Plan Optimisation Stage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.ActionPlanOptimisationStageImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getActionPlanOptimisationStage()
		 * @generated
		 */
		EClass ACTION_PLAN_OPTIMISATION_STAGE = eINSTANCE.getActionPlanOptimisationStage();

		/**
		 * The meta object literal for the '<em><b>Total Evaluations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACTION_PLAN_OPTIMISATION_STAGE__TOTAL_EVALUATIONS = eINSTANCE.getActionPlanOptimisationStage_TotalEvaluations();

		/**
		 * The meta object literal for the '<em><b>In Run Evaluations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACTION_PLAN_OPTIMISATION_STAGE__IN_RUN_EVALUATIONS = eINSTANCE.getActionPlanOptimisationStage_InRunEvaluations();

		/**
		 * The meta object literal for the '<em><b>Search Depth</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACTION_PLAN_OPTIMISATION_STAGE__SEARCH_DEPTH = eINSTANCE.getActionPlanOptimisationStage_SearchDepth();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.ResetInitialSequencesStageImpl <em>Reset Initial Sequences Stage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.ResetInitialSequencesStageImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getResetInitialSequencesStage()
		 * @generated
		 */
		EClass RESET_INITIAL_SEQUENCES_STAGE = eINSTANCE.getResetInitialSequencesStage();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.BreakEvenOptimisationStageImpl <em>Break Even Optimisation Stage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.BreakEvenOptimisationStageImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getBreakEvenOptimisationStage()
		 * @generated
		 */
		EClass BREAK_EVEN_OPTIMISATION_STAGE = eINSTANCE.getBreakEvenOptimisationStage();

		/**
		 * The meta object literal for the '<em><b>Target Profit And Loss</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BREAK_EVEN_OPTIMISATION_STAGE__TARGET_PROFIT_AND_LOSS = eINSTANCE.getBreakEvenOptimisationStage_TargetProfitAndLoss();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.ConstraintAndFitnessSettingsImpl <em>Constraint And Fitness Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.ConstraintAndFitnessSettingsImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getConstraintAndFitnessSettings()
		 * @generated
		 */
		EClass CONSTRAINT_AND_FITNESS_SETTINGS = eINSTANCE.getConstraintAndFitnessSettings();

		/**
		 * The meta object literal for the '<em><b>Objectives</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINT_AND_FITNESS_SETTINGS__OBJECTIVES = eINSTANCE.getConstraintAndFitnessSettings_Objectives();

		/**
		 * The meta object literal for the '<em><b>Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINT_AND_FITNESS_SETTINGS__CONSTRAINTS = eINSTANCE.getConstraintAndFitnessSettings_Constraints();

		/**
		 * The meta object literal for the '<em><b>Floating Days Limit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT_AND_FITNESS_SETTINGS__FLOATING_DAYS_LIMIT = eINSTANCE.getConstraintAndFitnessSettings_FloatingDaysLimit();

		/**
		 * The meta object literal for the '<em><b>Similarity Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS = eINSTANCE.getConstraintAndFitnessSettings_SimilaritySettings();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.OptimisationPlanImpl <em>Optimisation Plan</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.OptimisationPlanImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getOptimisationPlan()
		 * @generated
		 */
		EClass OPTIMISATION_PLAN = eINSTANCE.getOptimisationPlan();

		/**
		 * The meta object literal for the '<em><b>User Settings</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISATION_PLAN__USER_SETTINGS = eINSTANCE.getOptimisationPlan_UserSettings();

		/**
		 * The meta object literal for the '<em><b>Stages</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISATION_PLAN__STAGES = eINSTANCE.getOptimisationPlan_Stages();

		/**
		 * The meta object literal for the '<em><b>Solution Builder Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS = eINSTANCE.getOptimisationPlan_SolutionBuilderSettings();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.SolutionBuilderSettingsImpl <em>Solution Builder Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.SolutionBuilderSettingsImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getSolutionBuilderSettings()
		 * @generated
		 */
		EClass SOLUTION_BUILDER_SETTINGS = eINSTANCE.getSolutionBuilderSettings();

		/**
		 * The meta object literal for the '<em><b>Constraint And Fitness Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS = eINSTANCE.getSolutionBuilderSettings_ConstraintAndFitnessSettings();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.SimilarityMode <em>Similarity Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.SimilarityMode
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getSimilarityMode()
		 * @generated
		 */
		EEnum SIMILARITY_MODE = eINSTANCE.getSimilarityMode();

	}

} //OptimiserPackage
