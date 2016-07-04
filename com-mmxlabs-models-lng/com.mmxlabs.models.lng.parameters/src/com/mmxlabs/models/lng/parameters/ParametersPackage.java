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
	 * The feature id for the '<em><b>Build Action Sets</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__BUILD_ACTION_SETS = 4;

	/**
	 * The feature id for the '<em><b>Similarity Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__SIMILARITY_MODE = 5;

	/**
	 * The feature id for the '<em><b>Floating Days Limit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS__FLOATING_DAYS_LIMIT = 6;

	/**
	 * The number of structural features of the '<em>User Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_SETTINGS_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.IndividualSolutionImprovementSettingsImpl <em>Individual Solution Improvement Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.IndividualSolutionImprovementSettingsImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getIndividualSolutionImprovementSettings()
	 * @generated
	 */
	int INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS = 1;

	/**
	 * The feature id for the '<em><b>Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__ITERATIONS = 0;

	/**
	 * The feature id for the '<em><b>Improving Solutions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__IMPROVING_SOLUTIONS = 1;

	/**
	 * The number of structural features of the '<em>Individual Solution Improvement Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ParametersModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersModelImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getParametersModel()
	 * @generated
	 */
	int PARAMETERS_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Settings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS_MODEL__SETTINGS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Active Setting</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS_MODEL__ACTIVE_SETTING = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl <em>Optimiser Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getOptimiserSettings()
	 * @generated
	 */
	int OPTIMISER_SETTINGS = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Objectives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__OBJECTIVES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__CONSTRAINTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Range</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__RANGE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Annealing Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__ANNEALING_SETTINGS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__SEED = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__ARGUMENTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Generate Charter Outs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__GENERATE_CHARTER_OUTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Shipping Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__SHIPPING_ONLY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Similarity Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__SIMILARITY_SETTINGS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Solution Improvement Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Build Action Sets</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__BUILD_ACTION_SETS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Action Plan Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Floating Days Limit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__FLOATING_DAYS_LIMIT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The number of structural features of the '<em>Optimiser Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ObjectiveImpl <em>Objective</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ObjectiveImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getObjective()
	 * @generated
	 */
	int OBJECTIVE = 4;

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
	int CONSTRAINT = 5;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.OptimisationRangeImpl <em>Optimisation Range</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.OptimisationRangeImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getOptimisationRange()
	 * @generated
	 */
	int OPTIMISATION_RANGE = 6;

	/**
	 * The feature id for the '<em><b>Optimise After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RANGE__OPTIMISE_AFTER = 0;

	/**
	 * The feature id for the '<em><b>Optimise Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RANGE__OPTIMISE_BEFORE = 1;

	/**
	 * The number of structural features of the '<em>Optimisation Range</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RANGE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl <em>Annealing Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getAnnealingSettings()
	 * @generated
	 */
	int ANNEALING_SETTINGS = 7;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ArgumentImpl <em>Argument</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ArgumentImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getArgument()
	 * @generated
	 */
	int ARGUMENT = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARGUMENT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARGUMENT__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Argument</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARGUMENT_FEATURE_COUNT = 2;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl <em>Similarity Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getSimilaritySettings()
	 * @generated
	 */
	int SIMILARITY_SETTINGS = 9;

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
	int SIMILARITY_INTERVAL = 10;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ActionPlanSettingsImpl <em>Action Plan Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ActionPlanSettingsImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getActionPlanSettings()
	 * @generated
	 */
	int ACTION_PLAN_SETTINGS = 11;

	/**
	 * The feature id for the '<em><b>Total Evaluations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_PLAN_SETTINGS__TOTAL_EVALUATIONS = 0;

	/**
	 * The feature id for the '<em><b>In Run Evaluations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_PLAN_SETTINGS__IN_RUN_EVALUATIONS = 1;

	/**
	 * The feature id for the '<em><b>Search Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_PLAN_SETTINGS__SEARCH_DEPTH = 2;

	/**
	 * The number of structural features of the '<em>Action Plan Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_PLAN_SETTINGS_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.SimilarityMode <em>Similarity Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.SimilarityMode
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getSimilarityMode()
	 * @generated
	 */
	int SIMILARITY_MODE = 12;


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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings <em>Individual Solution Improvement Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Individual Solution Improvement Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings
	 * @generated
	 */
	EClass getIndividualSolutionImprovementSettings();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings#getIterations <em>Iterations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Iterations</em>'.
	 * @see com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings#getIterations()
	 * @see #getIndividualSolutionImprovementSettings()
	 * @generated
	 */
	EAttribute getIndividualSolutionImprovementSettings_Iterations();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings#isImprovingSolutions <em>Improving Solutions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Improving Solutions</em>'.
	 * @see com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings#isImprovingSolutions()
	 * @see #getIndividualSolutionImprovementSettings()
	 * @generated
	 */
	EAttribute getIndividualSolutionImprovementSettings_ImprovingSolutions();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.ParametersModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ParametersModel
	 * @generated
	 */
	EClass getParametersModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.parameters.ParametersModel#getSettings <em>Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ParametersModel#getSettings()
	 * @see #getParametersModel()
	 * @generated
	 */
	EReference getParametersModel_Settings();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.parameters.ParametersModel#getActiveSetting <em>Active Setting</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Active Setting</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ParametersModel#getActiveSetting()
	 * @see #getParametersModel()
	 * @generated
	 */
	EReference getParametersModel_ActiveSetting();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings <em>Optimiser Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Optimiser Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings
	 * @generated
	 */
	EClass getOptimiserSettings();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getObjectives <em>Objectives</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Objectives</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#getObjectives()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EReference getOptimiserSettings_Objectives();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getConstraints <em>Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Constraints</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#getConstraints()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EReference getOptimiserSettings_Constraints();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getRange <em>Range</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Range</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#getRange()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EReference getOptimiserSettings_Range();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getAnnealingSettings <em>Annealing Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Annealing Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#getAnnealingSettings()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EReference getOptimiserSettings_AnnealingSettings();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getSeed <em>Seed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Seed</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#getSeed()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EAttribute getOptimiserSettings_Seed();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getArguments <em>Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Arguments</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#getArguments()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EReference getOptimiserSettings_Arguments();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isGenerateCharterOuts <em>Generate Charter Outs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Charter Outs</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#isGenerateCharterOuts()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EAttribute getOptimiserSettings_GenerateCharterOuts();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isShippingOnly <em>Shipping Only</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shipping Only</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#isShippingOnly()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EAttribute getOptimiserSettings_ShippingOnly();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getSimilaritySettings <em>Similarity Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Similarity Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#getSimilaritySettings()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EReference getOptimiserSettings_SimilaritySettings();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getSolutionImprovementSettings <em>Solution Improvement Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Solution Improvement Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#getSolutionImprovementSettings()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EReference getOptimiserSettings_SolutionImprovementSettings();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isBuildActionSets <em>Build Action Sets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Build Action Sets</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#isBuildActionSets()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EAttribute getOptimiserSettings_BuildActionSets();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getActionPlanSettings <em>Action Plan Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Action Plan Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#getActionPlanSettings()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EReference getOptimiserSettings_ActionPlanSettings();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getFloatingDaysLimit <em>Floating Days Limit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Floating Days Limit</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#getFloatingDaysLimit()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EAttribute getOptimiserSettings_FloatingDaysLimit();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.OptimisationRange <em>Optimisation Range</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Optimisation Range</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimisationRange
	 * @generated
	 */
	EClass getOptimisationRange();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseAfter <em>Optimise After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Optimise After</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseAfter()
	 * @see #getOptimisationRange()
	 * @generated
	 */
	EAttribute getOptimisationRange_OptimiseAfter();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseBefore <em>Optimise Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Optimise Before</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseBefore()
	 * @see #getOptimisationRange()
	 * @generated
	 */
	EAttribute getOptimisationRange_OptimiseBefore();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.Argument <em>Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Argument</em>'.
	 * @see com.mmxlabs.models.lng.parameters.Argument
	 * @generated
	 */
	EClass getArgument();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.Argument#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.models.lng.parameters.Argument#getName()
	 * @see #getArgument()
	 * @generated
	 */
	EAttribute getArgument_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.Argument#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see com.mmxlabs.models.lng.parameters.Argument#getValue()
	 * @see #getArgument()
	 * @generated
	 */
	EAttribute getArgument_Value();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.parameters.ActionPlanSettings <em>Action Plan Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Action Plan Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ActionPlanSettings
	 * @generated
	 */
	EClass getActionPlanSettings();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.ActionPlanSettings#getTotalEvaluations <em>Total Evaluations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Total Evaluations</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ActionPlanSettings#getTotalEvaluations()
	 * @see #getActionPlanSettings()
	 * @generated
	 */
	EAttribute getActionPlanSettings_TotalEvaluations();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.ActionPlanSettings#getInRunEvaluations <em>In Run Evaluations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>In Run Evaluations</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ActionPlanSettings#getInRunEvaluations()
	 * @see #getActionPlanSettings()
	 * @generated
	 */
	EAttribute getActionPlanSettings_InRunEvaluations();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.ActionPlanSettings#getSearchDepth <em>Search Depth</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Search Depth</em>'.
	 * @see com.mmxlabs.models.lng.parameters.ActionPlanSettings#getSearchDepth()
	 * @see #getActionPlanSettings()
	 * @generated
	 */
	EAttribute getActionPlanSettings_SearchDepth();

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
		 * The meta object literal for the '<em><b>Floating Days Limit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_SETTINGS__FLOATING_DAYS_LIMIT = eINSTANCE.getUserSettings_FloatingDaysLimit();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.IndividualSolutionImprovementSettingsImpl <em>Individual Solution Improvement Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.IndividualSolutionImprovementSettingsImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getIndividualSolutionImprovementSettings()
		 * @generated
		 */
		EClass INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS = eINSTANCE.getIndividualSolutionImprovementSettings();

		/**
		 * The meta object literal for the '<em><b>Iterations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__ITERATIONS = eINSTANCE.getIndividualSolutionImprovementSettings_Iterations();

		/**
		 * The meta object literal for the '<em><b>Improving Solutions</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__IMPROVING_SOLUTIONS = eINSTANCE.getIndividualSolutionImprovementSettings_ImprovingSolutions();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.ParametersModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersModelImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getParametersModel()
		 * @generated
		 */
		EClass PARAMETERS_MODEL = eINSTANCE.getParametersModel();

		/**
		 * The meta object literal for the '<em><b>Settings</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETERS_MODEL__SETTINGS = eINSTANCE.getParametersModel_Settings();

		/**
		 * The meta object literal for the '<em><b>Active Setting</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETERS_MODEL__ACTIVE_SETTING = eINSTANCE.getParametersModel_ActiveSetting();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl <em>Optimiser Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getOptimiserSettings()
		 * @generated
		 */
		EClass OPTIMISER_SETTINGS = eINSTANCE.getOptimiserSettings();

		/**
		 * The meta object literal for the '<em><b>Objectives</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISER_SETTINGS__OBJECTIVES = eINSTANCE.getOptimiserSettings_Objectives();

		/**
		 * The meta object literal for the '<em><b>Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISER_SETTINGS__CONSTRAINTS = eINSTANCE.getOptimiserSettings_Constraints();

		/**
		 * The meta object literal for the '<em><b>Range</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISER_SETTINGS__RANGE = eINSTANCE.getOptimiserSettings_Range();

		/**
		 * The meta object literal for the '<em><b>Annealing Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISER_SETTINGS__ANNEALING_SETTINGS = eINSTANCE.getOptimiserSettings_AnnealingSettings();

		/**
		 * The meta object literal for the '<em><b>Seed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISER_SETTINGS__SEED = eINSTANCE.getOptimiserSettings_Seed();

		/**
		 * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISER_SETTINGS__ARGUMENTS = eINSTANCE.getOptimiserSettings_Arguments();

		/**
		 * The meta object literal for the '<em><b>Generate Charter Outs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISER_SETTINGS__GENERATE_CHARTER_OUTS = eINSTANCE.getOptimiserSettings_GenerateCharterOuts();

		/**
		 * The meta object literal for the '<em><b>Shipping Only</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISER_SETTINGS__SHIPPING_ONLY = eINSTANCE.getOptimiserSettings_ShippingOnly();

		/**
		 * The meta object literal for the '<em><b>Similarity Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISER_SETTINGS__SIMILARITY_SETTINGS = eINSTANCE.getOptimiserSettings_SimilaritySettings();

		/**
		 * The meta object literal for the '<em><b>Solution Improvement Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS = eINSTANCE.getOptimiserSettings_SolutionImprovementSettings();

		/**
		 * The meta object literal for the '<em><b>Build Action Sets</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISER_SETTINGS__BUILD_ACTION_SETS = eINSTANCE.getOptimiserSettings_BuildActionSets();

		/**
		 * The meta object literal for the '<em><b>Action Plan Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS = eINSTANCE.getOptimiserSettings_ActionPlanSettings();

		/**
		 * The meta object literal for the '<em><b>Floating Days Limit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISER_SETTINGS__FLOATING_DAYS_LIMIT = eINSTANCE.getOptimiserSettings_FloatingDaysLimit();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.OptimisationRangeImpl <em>Optimisation Range</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.OptimisationRangeImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getOptimisationRange()
		 * @generated
		 */
		EClass OPTIMISATION_RANGE = eINSTANCE.getOptimisationRange();

		/**
		 * The meta object literal for the '<em><b>Optimise After</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISATION_RANGE__OPTIMISE_AFTER = eINSTANCE.getOptimisationRange_OptimiseAfter();

		/**
		 * The meta object literal for the '<em><b>Optimise Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISATION_RANGE__OPTIMISE_BEFORE = eINSTANCE.getOptimisationRange_OptimiseBefore();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.ArgumentImpl <em>Argument</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.ArgumentImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getArgument()
		 * @generated
		 */
		EClass ARGUMENT = eINSTANCE.getArgument();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ARGUMENT__NAME = eINSTANCE.getArgument_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ARGUMENT__VALUE = eINSTANCE.getArgument_Value();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.parameters.impl.ActionPlanSettingsImpl <em>Action Plan Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.impl.ActionPlanSettingsImpl
		 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getActionPlanSettings()
		 * @generated
		 */
		EClass ACTION_PLAN_SETTINGS = eINSTANCE.getActionPlanSettings();

		/**
		 * The meta object literal for the '<em><b>Total Evaluations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACTION_PLAN_SETTINGS__TOTAL_EVALUATIONS = eINSTANCE.getActionPlanSettings_TotalEvaluations();

		/**
		 * The meta object literal for the '<em><b>In Run Evaluations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACTION_PLAN_SETTINGS__IN_RUN_EVALUATIONS = eINSTANCE.getActionPlanSettings_InRunEvaluations();

		/**
		 * The meta object literal for the '<em><b>Search Depth</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACTION_PLAN_SETTINGS__SEARCH_DEPTH = eINSTANCE.getActionPlanSettings_SearchDepth();

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
