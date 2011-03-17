/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
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
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__CONSTRAINTS = 2;

	/**
	 * The feature id for the '<em><b>Objectives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__OBJECTIVES = 3;

	/**
	 * The feature id for the '<em><b>Initial Schedule</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS__INITIAL_SCHEDULE = 4;

	/**
	 * The number of structural features of the '<em>Optimisation Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Optimisation Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_SETTINGS_OPERATION_COUNT = 0;

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
	int CONSTRAINT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__ENABLED = 1;

	/**
	 * The number of structural features of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_OPERATION_COUNT = 0;

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
	int OBJECTIVE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE__WEIGHT = 1;

	/**
	 * The number of structural features of the '<em>Objective</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE_FEATURE_COUNT = 2;


	/**
	 * The number of operations of the '<em>Objective</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECTIVE_OPERATION_COUNT = 0;


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
	 * Returns the meta object for the attribute '{@link scenario.optimiser.Constraint#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.optimiser.Constraint#getName()
	 * @see #getConstraint()
	 * @generated
	 */
	EAttribute getConstraint_Name();

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
	 * Returns the meta object for the attribute '{@link scenario.optimiser.Objective#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.optimiser.Objective#getName()
	 * @see #getObjective()
	 * @generated
	 */
	EAttribute getObjective_Name();

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
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT__NAME = eINSTANCE.getConstraint_Name();

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
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OBJECTIVE__NAME = eINSTANCE.getObjective_Name();

		/**
		 * The meta object literal for the '<em><b>Weight</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OBJECTIVE__WEIGHT = eINSTANCE.getObjective_Weight();

	}

} //OptimiserPackage
