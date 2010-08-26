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
	String eNS_URI = "http://com.mmxlabs.lng.emf/optimiser";

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
	int OPTIMISATION_SETTINGS__NAME = 0;

	/**
	 * The feature id for the '<em><b>Random Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__RANDOM_SEED = 1;

	/**
	 * The number of structural features of the '<em>Optimisation Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS_FEATURE_COUNT = 2;

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
	 * The feature id for the '<em><b>All Settings</b></em>' containment reference.
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
	 * The meta object id for the '{@link scenario.optimiser.impl.LSOSettingsImpl <em>LSO Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.optimiser.impl.LSOSettingsImpl
	 * @see scenario.optimiser.impl.OptimiserPackageImpl#getLSOSettings()
	 * @generated
	 */
	int LSO_SETTINGS = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__NAME = OPTIMISATION_SETTINGS__NAME;

	/**
	 * The feature id for the '<em><b>Random Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__RANDOM_SEED = OPTIMISATION_SETTINGS__RANDOM_SEED;

	/**
	 * The feature id for the '<em><b>Number Of Steps</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__NUMBER_OF_STEPS = OPTIMISATION_SETTINGS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Step Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__STEP_SIZE = OPTIMISATION_SETTINGS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Initial Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS__INITIAL_THRESHOLD = OPTIMISATION_SETTINGS_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>LSO Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LSO_SETTINGS_FEATURE_COUNT = OPTIMISATION_SETTINGS_FEATURE_COUNT + 3;


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
	 * Returns the meta object for the attribute '{@link scenario.optimiser.OptimisationSettings#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.optimiser.OptimisationSettings#getName()
	 * @see #getOptimisationSettings()
	 * @generated
	 */
	EAttribute getOptimisationSettings_Name();

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
	 * Returns the meta object for class '{@link scenario.optimiser.Optimisation <em>Optimisation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Optimisation</em>'.
	 * @see scenario.optimiser.Optimisation
	 * @generated
	 */
	EClass getOptimisation();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.optimiser.Optimisation#getAllSettings <em>All Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>All Settings</em>'.
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
	 * Returns the meta object for class '{@link scenario.optimiser.LSOSettings <em>LSO Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>LSO Settings</em>'.
	 * @see scenario.optimiser.LSOSettings
	 * @generated
	 */
	EClass getLSOSettings();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.LSOSettings#getNumberOfSteps <em>Number Of Steps</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Steps</em>'.
	 * @see scenario.optimiser.LSOSettings#getNumberOfSteps()
	 * @see #getLSOSettings()
	 * @generated
	 */
	EAttribute getLSOSettings_NumberOfSteps();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.LSOSettings#getStepSize <em>Step Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Step Size</em>'.
	 * @see scenario.optimiser.LSOSettings#getStepSize()
	 * @see #getLSOSettings()
	 * @generated
	 */
	EAttribute getLSOSettings_StepSize();

	/**
	 * Returns the meta object for the attribute '{@link scenario.optimiser.LSOSettings#getInitialThreshold <em>Initial Threshold</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Initial Threshold</em>'.
	 * @see scenario.optimiser.LSOSettings#getInitialThreshold()
	 * @see #getLSOSettings()
	 * @generated
	 */
	EAttribute getLSOSettings_InitialThreshold();

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
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISATION_SETTINGS__NAME = eINSTANCE.getOptimisationSettings_Name();

		/**
		 * The meta object literal for the '<em><b>Random Seed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIMISATION_SETTINGS__RANDOM_SEED = eINSTANCE.getOptimisationSettings_RandomSeed();

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
		 * The meta object literal for the '<em><b>All Settings</b></em>' containment reference feature.
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
		 * The meta object literal for the '{@link scenario.optimiser.impl.LSOSettingsImpl <em>LSO Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.optimiser.impl.LSOSettingsImpl
		 * @see scenario.optimiser.impl.OptimiserPackageImpl#getLSOSettings()
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
		 * The meta object literal for the '<em><b>Step Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LSO_SETTINGS__STEP_SIZE = eINSTANCE.getLSOSettings_StepSize();

		/**
		 * The meta object literal for the '<em><b>Initial Threshold</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LSO_SETTINGS__INITIAL_THRESHOLD = eINSTANCE.getLSOSettings_InitialThreshold();

	}

} //OptimiserPackage
