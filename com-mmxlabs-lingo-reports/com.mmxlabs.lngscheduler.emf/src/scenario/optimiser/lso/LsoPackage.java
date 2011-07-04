/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser.lso;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import scenario.optimiser.OptimiserPackage;

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
 * @see scenario.optimiser.lso.LsoFactory
 * @model kind="package"
 * @generated
 */
public interface LsoPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "lso";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf1/optimiser/lso";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.optimiser.lso";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	LsoPackage eINSTANCE = scenario.optimiser.lso.impl.LsoPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.optimiser.lso.impl.LSOSettingsImpl <em>LSO Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.lso.impl.LSOSettingsImpl
	 * @see scenario.optimiser.lso.impl.LsoPackageImpl#getLSOSettings()
	 * @generated
	 */
	int LSO_SETTINGS = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__NAME = OptimiserPackage.OPTIMISATION_SETTINGS__NAME;

	/**
	 * The feature id for the '<em><b>Random Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__RANDOM_SEED = OptimiserPackage.OPTIMISATION_SETTINGS__RANDOM_SEED;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__CONSTRAINTS = OptimiserPackage.OPTIMISATION_SETTINGS__CONSTRAINTS;

	/**
	 * The feature id for the '<em><b>Objectives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__OBJECTIVES = OptimiserPackage.OPTIMISATION_SETTINGS__OBJECTIVES;

	/**
	 * The feature id for the '<em><b>Initial Schedule</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__INITIAL_SCHEDULE = OptimiserPackage.OPTIMISATION_SETTINGS__INITIAL_SCHEDULE;

	/**
	 * The feature id for the '<em><b>Default Discount Curve</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__DEFAULT_DISCOUNT_CURVE = OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE;

	/**
	 * The feature id for the '<em><b>Freeze Days From Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__FREEZE_DAYS_FROM_START = OptimiserPackage.OPTIMISATION_SETTINGS__FREEZE_DAYS_FROM_START;

	/**
	 * The feature id for the '<em><b>Ignore Elements After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__IGNORE_ELEMENTS_AFTER = OptimiserPackage.OPTIMISATION_SETTINGS__IGNORE_ELEMENTS_AFTER;

	/**
	 * The feature id for the '<em><b>Number Of Steps</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__NUMBER_OF_STEPS = OptimiserPackage.OPTIMISATION_SETTINGS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Thresholder Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__THRESHOLDER_SETTINGS = OptimiserPackage.OPTIMISATION_SETTINGS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Move Generator Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__MOVE_GENERATOR_SETTINGS = OptimiserPackage.OPTIMISATION_SETTINGS_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>LSO Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS_FEATURE_COUNT = OptimiserPackage.OPTIMISATION_SETTINGS_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS___GET_CONTAINER = OptimiserPackage.OPTIMISATION_SETTINGS___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>LSO Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS_OPERATION_COUNT = OptimiserPackage.OPTIMISATION_SETTINGS_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.optimiser.lso.impl.ThresholderSettingsImpl <em>Thresholder Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.lso.impl.ThresholderSettingsImpl
	 * @see scenario.optimiser.lso.impl.LsoPackageImpl#getThresholderSettings()
	 * @generated
	 */
	int THRESHOLDER_SETTINGS = 1;

	/**
	 * The feature id for the '<em><b>Alpha</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int THRESHOLDER_SETTINGS__ALPHA = 0;

	/**
	 * The feature id for the '<em><b>Initial Acceptance Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int THRESHOLDER_SETTINGS__INITIAL_ACCEPTANCE_RATE = 1;

	/**
	 * The feature id for the '<em><b>Epoch Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int THRESHOLDER_SETTINGS__EPOCH_LENGTH = 2;

	/**
	 * The number of structural features of the '<em>Thresholder Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int THRESHOLDER_SETTINGS_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Thresholder Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int THRESHOLDER_SETTINGS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.optimiser.lso.impl.MoveGeneratorSettingsImpl <em>Move Generator Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.lso.impl.MoveGeneratorSettingsImpl
	 * @see scenario.optimiser.lso.impl.LsoPackageImpl#getMoveGeneratorSettings()
	 * @generated
	 */
	int MOVE_GENERATOR_SETTINGS = 2;

	/**
	 * The number of structural features of the '<em>Move Generator Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_GENERATOR_SETTINGS_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Move Generator Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_GENERATOR_SETTINGS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl <em>Random Move Generator Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl
	 * @see scenario.optimiser.lso.impl.LsoPackageImpl#getRandomMoveGeneratorSettings()
	 * @generated
	 */
	int RANDOM_MOVE_GENERATOR_SETTINGS = 3;

	/**
	 * The feature id for the '<em><b>Using2over2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANDOM_MOVE_GENERATOR_SETTINGS__USING2OVER2 = MOVE_GENERATOR_SETTINGS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Using3over2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANDOM_MOVE_GENERATOR_SETTINGS__USING3OVER2 = MOVE_GENERATOR_SETTINGS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Using4over1</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER1 = MOVE_GENERATOR_SETTINGS_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Using4over2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER2 = MOVE_GENERATOR_SETTINGS_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Random Move Generator Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANDOM_MOVE_GENERATOR_SETTINGS_FEATURE_COUNT = MOVE_GENERATOR_SETTINGS_FEATURE_COUNT + 4;

	/**
	 * The number of operations of the '<em>Random Move Generator Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANDOM_MOVE_GENERATOR_SETTINGS_OPERATION_COUNT = MOVE_GENERATOR_SETTINGS_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.optimiser.lso.impl.ConstrainedMoveGeneratorSettingsImpl <em>Constrained Move Generator Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.lso.impl.ConstrainedMoveGeneratorSettingsImpl
	 * @see scenario.optimiser.lso.impl.LsoPackageImpl#getConstrainedMoveGeneratorSettings()
	 * @generated
	 */
	int CONSTRAINED_MOVE_GENERATOR_SETTINGS = 4;

	/**
	 * The number of structural features of the '<em>Constrained Move Generator Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINED_MOVE_GENERATOR_SETTINGS_FEATURE_COUNT = MOVE_GENERATOR_SETTINGS_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Constrained Move Generator Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINED_MOVE_GENERATOR_SETTINGS_OPERATION_COUNT = MOVE_GENERATOR_SETTINGS_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link scenario.optimiser.lso.LSOSettings <em>LSO Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>LSO Settings</em>'.
	 * @see scenario.optimiser.lso.LSOSettings
	 * @generated
	 */
	EClass getLSOSettings();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.lso.LSOSettings#getNumberOfSteps <em>Number Of Steps</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Steps</em>'.
	 * @see scenario.optimiser.lso.LSOSettings#getNumberOfSteps()
	 * @see #getLSOSettings()
	 * @generated
	 */
	EAttribute getLSOSettings_NumberOfSteps();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.optimiser.lso.LSOSettings#getThresholderSettings <em>Thresholder Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Thresholder Settings</em>'.
	 * @see scenario.optimiser.lso.LSOSettings#getThresholderSettings()
	 * @see #getLSOSettings()
	 * @generated
	 */
	EReference getLSOSettings_ThresholderSettings();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.optimiser.lso.LSOSettings#getMoveGeneratorSettings <em>Move Generator Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Move Generator Settings</em>'.
	 * @see scenario.optimiser.lso.LSOSettings#getMoveGeneratorSettings()
	 * @see #getLSOSettings()
	 * @generated
	 */
	EReference getLSOSettings_MoveGeneratorSettings();

	/**
	 * Returns the meta object for class '{@link scenario.optimiser.lso.ThresholderSettings <em>Thresholder Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Thresholder Settings</em>'.
	 * @see scenario.optimiser.lso.ThresholderSettings
	 * @generated
	 */
	EClass getThresholderSettings();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.lso.ThresholderSettings#getAlpha <em>Alpha</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alpha</em>'.
	 * @see scenario.optimiser.lso.ThresholderSettings#getAlpha()
	 * @see #getThresholderSettings()
	 * @generated
	 */
	EAttribute getThresholderSettings_Alpha();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.lso.ThresholderSettings#getInitialAcceptanceRate <em>Initial Acceptance Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Initial Acceptance Rate</em>'.
	 * @see scenario.optimiser.lso.ThresholderSettings#getInitialAcceptanceRate()
	 * @see #getThresholderSettings()
	 * @generated
	 */
	EAttribute getThresholderSettings_InitialAcceptanceRate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.lso.ThresholderSettings#getEpochLength <em>Epoch Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Epoch Length</em>'.
	 * @see scenario.optimiser.lso.ThresholderSettings#getEpochLength()
	 * @see #getThresholderSettings()
	 * @generated
	 */
	EAttribute getThresholderSettings_EpochLength();

	/**
	 * Returns the meta object for class '{@link scenario.optimiser.lso.MoveGeneratorSettings <em>Move Generator Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Move Generator Settings</em>'.
	 * @see scenario.optimiser.lso.MoveGeneratorSettings
	 * @generated
	 */
	EClass getMoveGeneratorSettings();

	/**
	 * Returns the meta object for class '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings <em>Random Move Generator Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Random Move Generator Settings</em>'.
	 * @see scenario.optimiser.lso.RandomMoveGeneratorSettings
	 * @generated
	 */
	EClass getRandomMoveGeneratorSettings();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing2over2 <em>Using2over2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Using2over2</em>'.
	 * @see scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing2over2()
	 * @see #getRandomMoveGeneratorSettings()
	 * @generated
	 */
	EAttribute getRandomMoveGeneratorSettings_Using2over2();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing3over2 <em>Using3over2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Using3over2</em>'.
	 * @see scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing3over2()
	 * @see #getRandomMoveGeneratorSettings()
	 * @generated
	 */
	EAttribute getRandomMoveGeneratorSettings_Using3over2();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing4over1 <em>Using4over1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Using4over1</em>'.
	 * @see scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing4over1()
	 * @see #getRandomMoveGeneratorSettings()
	 * @generated
	 */
	EAttribute getRandomMoveGeneratorSettings_Using4over1();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing4over2 <em>Using4over2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Using4over2</em>'.
	 * @see scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing4over2()
	 * @see #getRandomMoveGeneratorSettings()
	 * @generated
	 */
	EAttribute getRandomMoveGeneratorSettings_Using4over2();

	/**
	 * Returns the meta object for class '{@link scenario.optimiser.lso.ConstrainedMoveGeneratorSettings <em>Constrained Move Generator Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constrained Move Generator Settings</em>'.
	 * @see scenario.optimiser.lso.ConstrainedMoveGeneratorSettings
	 * @generated
	 */
	EClass getConstrainedMoveGeneratorSettings();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	LsoFactory getLsoFactory();

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
		 * The meta object literal for the '{@link scenario.optimiser.lso.impl.LSOSettingsImpl <em>LSO Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.lso.impl.LSOSettingsImpl
		 * @see scenario.optimiser.lso.impl.LsoPackageImpl#getLSOSettings()
		 * @generated
		 */
		EClass LSO_SETTINGS = eINSTANCE.getLSOSettings();

		/**
		 * The meta object literal for the '<em><b>Number Of Steps</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LSO_SETTINGS__NUMBER_OF_STEPS = eINSTANCE.getLSOSettings_NumberOfSteps();

		/**
		 * The meta object literal for the '<em><b>Thresholder Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LSO_SETTINGS__THRESHOLDER_SETTINGS = eINSTANCE.getLSOSettings_ThresholderSettings();

		/**
		 * The meta object literal for the '<em><b>Move Generator Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LSO_SETTINGS__MOVE_GENERATOR_SETTINGS = eINSTANCE.getLSOSettings_MoveGeneratorSettings();

		/**
		 * The meta object literal for the '{@link scenario.optimiser.lso.impl.ThresholderSettingsImpl <em>Thresholder Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.lso.impl.ThresholderSettingsImpl
		 * @see scenario.optimiser.lso.impl.LsoPackageImpl#getThresholderSettings()
		 * @generated
		 */
		EClass THRESHOLDER_SETTINGS = eINSTANCE.getThresholderSettings();

		/**
		 * The meta object literal for the '<em><b>Alpha</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute THRESHOLDER_SETTINGS__ALPHA = eINSTANCE.getThresholderSettings_Alpha();

		/**
		 * The meta object literal for the '<em><b>Initial Acceptance Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute THRESHOLDER_SETTINGS__INITIAL_ACCEPTANCE_RATE = eINSTANCE.getThresholderSettings_InitialAcceptanceRate();

		/**
		 * The meta object literal for the '<em><b>Epoch Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute THRESHOLDER_SETTINGS__EPOCH_LENGTH = eINSTANCE.getThresholderSettings_EpochLength();

		/**
		 * The meta object literal for the '{@link scenario.optimiser.lso.impl.MoveGeneratorSettingsImpl <em>Move Generator Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.lso.impl.MoveGeneratorSettingsImpl
		 * @see scenario.optimiser.lso.impl.LsoPackageImpl#getMoveGeneratorSettings()
		 * @generated
		 */
		EClass MOVE_GENERATOR_SETTINGS = eINSTANCE.getMoveGeneratorSettings();

		/**
		 * The meta object literal for the '{@link scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl <em>Random Move Generator Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl
		 * @see scenario.optimiser.lso.impl.LsoPackageImpl#getRandomMoveGeneratorSettings()
		 * @generated
		 */
		EClass RANDOM_MOVE_GENERATOR_SETTINGS = eINSTANCE.getRandomMoveGeneratorSettings();

		/**
		 * The meta object literal for the '<em><b>Using2over2</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RANDOM_MOVE_GENERATOR_SETTINGS__USING2OVER2 = eINSTANCE.getRandomMoveGeneratorSettings_Using2over2();

		/**
		 * The meta object literal for the '<em><b>Using3over2</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RANDOM_MOVE_GENERATOR_SETTINGS__USING3OVER2 = eINSTANCE.getRandomMoveGeneratorSettings_Using3over2();

		/**
		 * The meta object literal for the '<em><b>Using4over1</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER1 = eINSTANCE.getRandomMoveGeneratorSettings_Using4over1();

		/**
		 * The meta object literal for the '<em><b>Using4over2</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER2 = eINSTANCE.getRandomMoveGeneratorSettings_Using4over2();

		/**
		 * The meta object literal for the '{@link scenario.optimiser.lso.impl.ConstrainedMoveGeneratorSettingsImpl <em>Constrained Move Generator Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.lso.impl.ConstrainedMoveGeneratorSettingsImpl
		 * @see scenario.optimiser.lso.impl.LsoPackageImpl#getConstrainedMoveGeneratorSettings()
		 * @generated
		 */
		EClass CONSTRAINED_MOVE_GENERATOR_SETTINGS = eINSTANCE.getConstrainedMoveGeneratorSettings();

	}

} //LsoPackage
