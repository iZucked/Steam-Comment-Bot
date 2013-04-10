/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.types.TypesPackage;
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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ParametersModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersModelImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getParametersModel()
	 * @generated
	 */
	int PARAMETERS_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS_MODEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

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
	int OPTIMISER_SETTINGS = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__EXTENSIONS = TypesPackage.AOPTIMISATION_SETTINGS__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__PROXIES = TypesPackage.AOPTIMISATION_SETTINGS__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__UUID = TypesPackage.AOPTIMISATION_SETTINGS__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__NAME = TypesPackage.AOPTIMISATION_SETTINGS__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__OTHER_NAMES = TypesPackage.AOPTIMISATION_SETTINGS__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Objectives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__OBJECTIVES = TypesPackage.AOPTIMISATION_SETTINGS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__CONSTRAINTS = TypesPackage.AOPTIMISATION_SETTINGS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Range</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__RANGE = TypesPackage.AOPTIMISATION_SETTINGS_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Annealing Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__ANNEALING_SETTINGS = TypesPackage.AOPTIMISATION_SETTINGS_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__SEED = TypesPackage.AOPTIMISATION_SETTINGS_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__ARGUMENTS = TypesPackage.AOPTIMISATION_SETTINGS_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Rewire</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS__REWIRE = TypesPackage.AOPTIMISATION_SETTINGS_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Optimiser Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISER_SETTINGS_FEATURE_COUNT = TypesPackage.AOPTIMISATION_SETTINGS_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ObjectiveImpl <em>Objective</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ObjectiveImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getObjective()
	 * @generated
	 */
	int OBJECTIVE = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__PROXIES = MMXCorePackage.NAMED_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__OTHER_NAMES = MMXCorePackage.NAMED_OBJECT__OTHER_NAMES;

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
	int CONSTRAINT = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__PROXIES = MMXCorePackage.NAMED_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__OTHER_NAMES = MMXCorePackage.NAMED_OBJECT__OTHER_NAMES;

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
	int OPTIMISATION_RANGE = 4;

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
	int ANNEALING_SETTINGS = 5;

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
	 * The number of structural features of the '<em>Annealing Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNEALING_SETTINGS_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.parameters.impl.ArgumentImpl <em>Argument</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.impl.ArgumentImpl
	 * @see com.mmxlabs.models.lng.parameters.impl.ParametersPackageImpl#getArgument()
	 * @generated
	 */
	int ARGUMENT = 6;

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isRewire <em>Rewire</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rewire</em>'.
	 * @see com.mmxlabs.models.lng.parameters.OptimiserSettings#isRewire()
	 * @see #getOptimiserSettings()
	 * @generated
	 */
	EAttribute getOptimiserSettings_Rewire();

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
		 * The meta object literal for the '<em><b>Rewire</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISER_SETTINGS__REWIRE = eINSTANCE.getOptimiserSettings_Rewire();

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

	}

} //OptimiserPackage
