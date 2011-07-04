/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import scenario.ScenarioPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see scenario.optimiser.OptimiserFactory
 * @model kind="package"
 * @generated
 */
public interface OptimiserPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "optimiser";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf1/optimiser";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.optimiser";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	OptimiserPackage eINSTANCE = scenario.optimiser.impl.OptimiserPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.optimiser.impl.OptimisationSettingsImpl <em>Optimisation Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.impl.OptimisationSettingsImpl
	 * @see scenario.optimiser.impl.OptimiserPackageImpl#getOptimisationSettings()
	 * @generated
	 */
	int OPTIMISATION_SETTINGS = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__NAME = ScenarioPackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Random Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__RANDOM_SEED = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__CONSTRAINTS = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Objectives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__OBJECTIVES = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Initial Schedule</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__INITIAL_SCHEDULE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Default Discount Curve</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Freeze Days From Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__FREEZE_DAYS_FROM_START = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Ignore Elements After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__IGNORE_ELEMENTS_AFTER = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Optimisation Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS_FEATURE_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS___GET_CONTAINER = ScenarioPackage.NAMED_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Optimisation Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS_OPERATION_COUNT = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.optimiser.impl.OptimisationImpl <em>Optimisation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.impl.OptimisationImpl
	 * @see scenario.optimiser.impl.OptimiserPackageImpl#getOptimisation()
	 * @generated
	 */
	int OPTIMISATION = 1;

	/**
	 * The feature id for the '<em><b>All Settings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION__ALL_SETTINGS = 0;

	/**
	 * The feature id for the '<em><b>Current Settings</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION__CURRENT_SETTINGS = 1;

	/**
	 * The number of structural features of the '<em>Optimisation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Optimisation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.optimiser.impl.ConstraintImpl <em>Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.impl.ConstraintImpl
	 * @see scenario.optimiser.impl.OptimiserPackageImpl#getConstraint()
	 * @generated
	 */
	int CONSTRAINT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__NAME = ScenarioPackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__ENABLED = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_FEATURE_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT___GET_CONTAINER = ScenarioPackage.NAMED_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_OPERATION_COUNT = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.optimiser.impl.ObjectiveImpl <em>Objective</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.impl.ObjectiveImpl
	 * @see scenario.optimiser.impl.OptimiserPackageImpl#getObjective()
	 * @generated
	 */
	int OBJECTIVE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__NAME = ScenarioPackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__WEIGHT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Discount Curve</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__DISCOUNT_CURVE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Objective</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE_FEATURE_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE___GET_CONTAINER = ScenarioPackage.NAMED_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Objective</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE_OPERATION_COUNT = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.optimiser.impl.DiscountCurveImpl <em>Discount Curve</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.impl.DiscountCurveImpl
	 * @see scenario.optimiser.impl.OptimiserPackageImpl#getDiscountCurve()
	 * @generated
	 */
	int DISCOUNT_CURVE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCOUNT_CURVE__NAME = ScenarioPackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Discounts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCOUNT_CURVE__DISCOUNTS = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCOUNT_CURVE__START_DATE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Discount Curve</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCOUNT_CURVE_FEATURE_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCOUNT_CURVE___GET_CONTAINER = ScenarioPackage.NAMED_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Discount Curve</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCOUNT_CURVE_OPERATION_COUNT = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.optimiser.impl.DiscountImpl <em>Discount</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.impl.DiscountImpl
	 * @see scenario.optimiser.impl.OptimiserPackageImpl#getDiscount()
	 * @generated
	 */
	int DISCOUNT = 5;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCOUNT__TIME = 0;

	/**
	 * The feature id for the '<em><b>Discount Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCOUNT__DISCOUNT_FACTOR = 1;

	/**
	 * The number of structural features of the '<em>Discount</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCOUNT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Discount</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCOUNT_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link scenario.optimiser.OptimisationSettings <em>Optimisation Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Optimisation Settings</em>'.
	 * @see scenario.optimiser.OptimisationSettings
	 * @generated
	 */
	EClass getOptimisationSettings();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.OptimisationSettings#getRandomSeed <em>Random Seed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Random Seed</em>'.
	 * @see scenario.optimiser.OptimisationSettings#getRandomSeed()
	 * @see #getOptimisationSettings()
	 * @generated
	 */
	EAttribute getOptimisationSettings_RandomSeed();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.optimiser.OptimisationSettings#getConstraints <em>Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Constraints</em>'.
	 * @see scenario.optimiser.OptimisationSettings#getConstraints()
	 * @see #getOptimisationSettings()
	 * @generated
	 */
	EReference getOptimisationSettings_Constraints();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.optimiser.OptimisationSettings#getObjectives <em>Objectives</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Objectives</em>'.
	 * @see scenario.optimiser.OptimisationSettings#getObjectives()
	 * @see #getOptimisationSettings()
	 * @generated
	 */
	EReference getOptimisationSettings_Objectives();

	/**
	 * Returns the meta object for the reference '{@link scenario.optimiser.OptimisationSettings#getInitialSchedule <em>Initial Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Initial Schedule</em>'.
	 * @see scenario.optimiser.OptimisationSettings#getInitialSchedule()
	 * @see #getOptimisationSettings()
	 * @generated
	 */
	EReference getOptimisationSettings_InitialSchedule();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.optimiser.OptimisationSettings#getDefaultDiscountCurve <em>Default Discount Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Default Discount Curve</em>'.
	 * @see scenario.optimiser.OptimisationSettings#getDefaultDiscountCurve()
	 * @see #getOptimisationSettings()
	 * @generated
	 */
	EReference getOptimisationSettings_DefaultDiscountCurve();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.OptimisationSettings#getFreezeDaysFromStart <em>Freeze Days From Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Freeze Days From Start</em>'.
	 * @see scenario.optimiser.OptimisationSettings#getFreezeDaysFromStart()
	 * @see #getOptimisationSettings()
	 * @generated
	 */
	EAttribute getOptimisationSettings_FreezeDaysFromStart();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.OptimisationSettings#getIgnoreElementsAfter <em>Ignore Elements After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ignore Elements After</em>'.
	 * @see scenario.optimiser.OptimisationSettings#getIgnoreElementsAfter()
	 * @see #getOptimisationSettings()
	 * @generated
	 */
	EAttribute getOptimisationSettings_IgnoreElementsAfter();

	/**
	 * Returns the meta object for class '{@link scenario.optimiser.Optimisation <em>Optimisation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Optimisation</em>'.
	 * @see scenario.optimiser.Optimisation
	 * @generated
	 */
	EClass getOptimisation();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.optimiser.Optimisation#getAllSettings <em>All Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>All Settings</em>'.
	 * @see scenario.optimiser.Optimisation#getAllSettings()
	 * @see #getOptimisation()
	 * @generated
	 */
	EReference getOptimisation_AllSettings();

	/**
	 * Returns the meta object for the reference '{@link scenario.optimiser.Optimisation#getCurrentSettings <em>Current Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Current Settings</em>'.
	 * @see scenario.optimiser.Optimisation#getCurrentSettings()
	 * @see #getOptimisation()
	 * @generated
	 */
	EReference getOptimisation_CurrentSettings();

	/**
	 * Returns the meta object for class '{@link scenario.optimiser.Constraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraint</em>'.
	 * @see scenario.optimiser.Constraint
	 * @generated
	 */
	EClass getConstraint();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.Constraint#isEnabled <em>Enabled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Enabled</em>'.
	 * @see scenario.optimiser.Constraint#isEnabled()
	 * @see #getConstraint()
	 * @generated
	 */
	EAttribute getConstraint_Enabled();

	/**
	 * Returns the meta object for class '{@link scenario.optimiser.Objective <em>Objective</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Objective</em>'.
	 * @see scenario.optimiser.Objective
	 * @generated
	 */
	EClass getObjective();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.Objective#getWeight <em>Weight</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Weight</em>'.
	 * @see scenario.optimiser.Objective#getWeight()
	 * @see #getObjective()
	 * @generated
	 */
	EAttribute getObjective_Weight();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.optimiser.Objective#getDiscountCurve <em>Discount Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Discount Curve</em>'.
	 * @see scenario.optimiser.Objective#getDiscountCurve()
	 * @see #getObjective()
	 * @generated
	 */
	EReference getObjective_DiscountCurve();

	/**
	 * Returns the meta object for class '{@link scenario.optimiser.DiscountCurve <em>Discount Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Discount Curve</em>'.
	 * @see scenario.optimiser.DiscountCurve
	 * @generated
	 */
	EClass getDiscountCurve();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.optimiser.DiscountCurve#getDiscounts <em>Discounts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Discounts</em>'.
	 * @see scenario.optimiser.DiscountCurve#getDiscounts()
	 * @see #getDiscountCurve()
	 * @generated
	 */
	EReference getDiscountCurve_Discounts();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.DiscountCurve#getStartDate <em>Start Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Date</em>'.
	 * @see scenario.optimiser.DiscountCurve#getStartDate()
	 * @see #getDiscountCurve()
	 * @generated
	 */
	EAttribute getDiscountCurve_StartDate();

	/**
	 * Returns the meta object for class '{@link scenario.optimiser.Discount <em>Discount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Discount</em>'.
	 * @see scenario.optimiser.Discount
	 * @generated
	 */
	EClass getDiscount();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.Discount#getTime <em>Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time</em>'.
	 * @see scenario.optimiser.Discount#getTime()
	 * @see #getDiscount()
	 * @generated
	 */
	EAttribute getDiscount_Time();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.Discount#getDiscountFactor <em>Discount Factor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discount Factor</em>'.
	 * @see scenario.optimiser.Discount#getDiscountFactor()
	 * @see #getDiscount()
	 * @generated
	 */
	EAttribute getDiscount_DiscountFactor();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	OptimiserFactory getOptimiserFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link scenario.optimiser.impl.OptimisationSettingsImpl <em>Optimisation Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.impl.OptimisationSettingsImpl
		 * @see scenario.optimiser.impl.OptimiserPackageImpl#getOptimisationSettings()
		 * @generated
		 */
		EClass OPTIMISATION_SETTINGS = eINSTANCE.getOptimisationSettings();

		/**
		 * The meta object literal for the '<em><b>Random Seed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISATION_SETTINGS__RANDOM_SEED = eINSTANCE.getOptimisationSettings_RandomSeed();

		/**
		 * The meta object literal for the '<em><b>Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISATION_SETTINGS__CONSTRAINTS = eINSTANCE.getOptimisationSettings_Constraints();

		/**
		 * The meta object literal for the '<em><b>Objectives</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISATION_SETTINGS__OBJECTIVES = eINSTANCE.getOptimisationSettings_Objectives();

		/**
		 * The meta object literal for the '<em><b>Initial Schedule</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISATION_SETTINGS__INITIAL_SCHEDULE = eINSTANCE.getOptimisationSettings_InitialSchedule();

		/**
		 * The meta object literal for the '<em><b>Default Discount Curve</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE = eINSTANCE.getOptimisationSettings_DefaultDiscountCurve();

		/**
		 * The meta object literal for the '<em><b>Freeze Days From Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISATION_SETTINGS__FREEZE_DAYS_FROM_START = eINSTANCE.getOptimisationSettings_FreezeDaysFromStart();

		/**
		 * The meta object literal for the '<em><b>Ignore Elements After</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISATION_SETTINGS__IGNORE_ELEMENTS_AFTER = eINSTANCE.getOptimisationSettings_IgnoreElementsAfter();

		/**
		 * The meta object literal for the '{@link scenario.optimiser.impl.OptimisationImpl <em>Optimisation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.impl.OptimisationImpl
		 * @see scenario.optimiser.impl.OptimiserPackageImpl#getOptimisation()
		 * @generated
		 */
		EClass OPTIMISATION = eINSTANCE.getOptimisation();

		/**
		 * The meta object literal for the '<em><b>All Settings</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISATION__ALL_SETTINGS = eINSTANCE.getOptimisation_AllSettings();

		/**
		 * The meta object literal for the '<em><b>Current Settings</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIMISATION__CURRENT_SETTINGS = eINSTANCE.getOptimisation_CurrentSettings();

		/**
		 * The meta object literal for the '{@link scenario.optimiser.impl.ConstraintImpl <em>Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.impl.ConstraintImpl
		 * @see scenario.optimiser.impl.OptimiserPackageImpl#getConstraint()
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
		 * The meta object literal for the '{@link scenario.optimiser.impl.ObjectiveImpl <em>Objective</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.impl.ObjectiveImpl
		 * @see scenario.optimiser.impl.OptimiserPackageImpl#getObjective()
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
		 * The meta object literal for the '<em><b>Discount Curve</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OBJECTIVE__DISCOUNT_CURVE = eINSTANCE.getObjective_DiscountCurve();

		/**
		 * The meta object literal for the '{@link scenario.optimiser.impl.DiscountCurveImpl <em>Discount Curve</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.impl.DiscountCurveImpl
		 * @see scenario.optimiser.impl.OptimiserPackageImpl#getDiscountCurve()
		 * @generated
		 */
		EClass DISCOUNT_CURVE = eINSTANCE.getDiscountCurve();

		/**
		 * The meta object literal for the '<em><b>Discounts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DISCOUNT_CURVE__DISCOUNTS = eINSTANCE.getDiscountCurve_Discounts();

		/**
		 * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCOUNT_CURVE__START_DATE = eINSTANCE.getDiscountCurve_StartDate();

		/**
		 * The meta object literal for the '{@link scenario.optimiser.impl.DiscountImpl <em>Discount</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.impl.DiscountImpl
		 * @see scenario.optimiser.impl.OptimiserPackageImpl#getDiscount()
		 * @generated
		 */
		EClass DISCOUNT = eINSTANCE.getDiscount();

		/**
		 * The meta object literal for the '<em><b>Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCOUNT__TIME = eINSTANCE.getDiscount_Time();

		/**
		 * The meta object literal for the '<em><b>Discount Factor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCOUNT__DISCOUNT_FACTOR = eINSTANCE.getDiscount_DiscountFactor();

	}

} //OptimiserPackage
