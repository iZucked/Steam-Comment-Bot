/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see scenario.ScenarioFactory
 * @model kind="package"
 * @generated
 */
public interface ScenarioPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "scenario";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ScenarioPackage eINSTANCE = scenario.impl.ScenarioPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.impl.ScenarioImpl <em>Scenario</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.impl.ScenarioImpl
	 * @see scenario.impl.ScenarioPackageImpl#getScenario()
	 * @generated
	 */
	int SCENARIO = 0;

	/**
	 * The feature id for the '<em><b>Fleet Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO__FLEET_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Port Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO__PORT_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Cargo Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO__CARGO_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Contract Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO__CONTRACT_MODEL = 3;

	/**
	 * The feature id for the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO__SCHEDULE_MODEL = 4;

	/**
	 * The feature id for the '<em><b>Market Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO__MARKET_MODEL = 5;

	/**
	 * The feature id for the '<em><b>Distance Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO__DISTANCE_MODEL = 6;

	/**
	 * The feature id for the '<em><b>Canal Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO__CANAL_MODEL = 7;

	/**
	 * The feature id for the '<em><b>Optimisation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO__OPTIMISATION = 8;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO__VERSION = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO__NAME = 10;

	/**
	 * The number of structural features of the '<em>Scenario</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FEATURE_COUNT = 11;

	/**
	 * The operation id for the '<em>Get Or Create Fleet Model</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO___GET_OR_CREATE_FLEET_MODEL = 0;

	/**
	 * The operation id for the '<em>Get Or Create Schedule Model</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO___GET_OR_CREATE_SCHEDULE_MODEL = 1;

	/**
	 * The operation id for the '<em>Create Missing Models</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO___CREATE_MISSING_MODELS = 2;

	/**
	 * The number of operations of the '<em>Scenario</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_OPERATION_COUNT = 3;

	/**
	 * The meta object id for the '{@link scenario.impl.ScenarioObjectImpl <em>Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.impl.ScenarioObjectImpl
	 * @see scenario.impl.ScenarioPackageImpl#getScenarioObject()
	 * @generated
	 */
	int SCENARIO_OBJECT = 1;

	/**
	 * The number of structural features of the '<em>Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_OBJECT_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_OBJECT___GET_CONTAINER = 0;

	/**
	 * The number of operations of the '<em>Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_OBJECT_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link scenario.impl.NamedObjectImpl <em>Named Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.impl.NamedObjectImpl
	 * @see scenario.impl.ScenarioPackageImpl#getNamedObject()
	 * @generated
	 */
	int NAMED_OBJECT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT__NAME = SCENARIO_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Named Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_FEATURE_COUNT = SCENARIO_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT___GET_CONTAINER = SCENARIO_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Named Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_OPERATION_COUNT = SCENARIO_OBJECT_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link scenario.Scenario <em>Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario</em>'.
	 * @see scenario.Scenario
	 * @generated
	 */
	EClass getScenario();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.Scenario#getFleetModel <em>Fleet Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fleet Model</em>'.
	 * @see scenario.Scenario#getFleetModel()
	 * @see #getScenario()
	 * @generated
	 */
	EReference getScenario_FleetModel();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.Scenario#getPortModel <em>Port Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Port Model</em>'.
	 * @see scenario.Scenario#getPortModel()
	 * @see #getScenario()
	 * @generated
	 */
	EReference getScenario_PortModel();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.Scenario#getCargoModel <em>Cargo Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Cargo Model</em>'.
	 * @see scenario.Scenario#getCargoModel()
	 * @see #getScenario()
	 * @generated
	 */
	EReference getScenario_CargoModel();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.Scenario#getContractModel <em>Contract Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Contract Model</em>'.
	 * @see scenario.Scenario#getContractModel()
	 * @see #getScenario()
	 * @generated
	 */
	EReference getScenario_ContractModel();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.Scenario#getScheduleModel <em>Schedule Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Schedule Model</em>'.
	 * @see scenario.Scenario#getScheduleModel()
	 * @see #getScenario()
	 * @generated
	 */
	EReference getScenario_ScheduleModel();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.Scenario#getMarketModel <em>Market Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Market Model</em>'.
	 * @see scenario.Scenario#getMarketModel()
	 * @see #getScenario()
	 * @generated
	 */
	EReference getScenario_MarketModel();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.Scenario#getDistanceModel <em>Distance Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Distance Model</em>'.
	 * @see scenario.Scenario#getDistanceModel()
	 * @see #getScenario()
	 * @generated
	 */
	EReference getScenario_DistanceModel();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.Scenario#getCanalModel <em>Canal Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Canal Model</em>'.
	 * @see scenario.Scenario#getCanalModel()
	 * @see #getScenario()
	 * @generated
	 */
	EReference getScenario_CanalModel();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.Scenario#getOptimisation <em>Optimisation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Optimisation</em>'.
	 * @see scenario.Scenario#getOptimisation()
	 * @see #getScenario()
	 * @generated
	 */
	EReference getScenario_Optimisation();

	/**
	 * Returns the meta object for the attribute '{@link scenario.Scenario#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see scenario.Scenario#getVersion()
	 * @see #getScenario()
	 * @generated
	 */
	EAttribute getScenario_Version();

	/**
	 * Returns the meta object for the attribute '{@link scenario.Scenario#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.Scenario#getName()
	 * @see #getScenario()
	 * @generated
	 */
	EAttribute getScenario_Name();

	/**
	 * Returns the meta object for the '{@link scenario.Scenario#getOrCreateFleetModel() <em>Get Or Create Fleet Model</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Or Create Fleet Model</em>' operation.
	 * @see scenario.Scenario#getOrCreateFleetModel()
	 * @generated
	 */
	EOperation getScenario__GetOrCreateFleetModel();

	/**
	 * Returns the meta object for the '{@link scenario.Scenario#getOrCreateScheduleModel() <em>Get Or Create Schedule Model</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Or Create Schedule Model</em>' operation.
	 * @see scenario.Scenario#getOrCreateScheduleModel()
	 * @generated
	 */
	EOperation getScenario__GetOrCreateScheduleModel();

	/**
	 * Returns the meta object for the '{@link scenario.Scenario#createMissingModels() <em>Create Missing Models</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Create Missing Models</em>' operation.
	 * @see scenario.Scenario#createMissingModels()
	 * @generated
	 */
	EOperation getScenario__CreateMissingModels();

	/**
	 * Returns the meta object for class '{@link scenario.ScenarioObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Object</em>'.
	 * @see scenario.ScenarioObject
	 * @generated
	 */
	EClass getScenarioObject();

	/**
	 * Returns the meta object for the '{@link scenario.ScenarioObject#getContainer() <em>Get Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Container</em>' operation.
	 * @see scenario.ScenarioObject#getContainer()
	 * @generated
	 */
	EOperation getScenarioObject__GetContainer();

	/**
	 * Returns the meta object for class '{@link scenario.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Object</em>'.
	 * @see scenario.NamedObject
	 * @generated
	 */
	EClass getNamedObject();

	/**
	 * Returns the meta object for the attribute '{@link scenario.NamedObject#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.NamedObject#getName()
	 * @see #getNamedObject()
	 * @generated
	 */
	EAttribute getNamedObject_Name();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ScenarioFactory getScenarioFactory();

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
		 * The meta object literal for the '{@link scenario.impl.ScenarioImpl <em>Scenario</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.impl.ScenarioImpl
		 * @see scenario.impl.ScenarioPackageImpl#getScenario()
		 * @generated
		 */
		EClass SCENARIO = eINSTANCE.getScenario();

		/**
		 * The meta object literal for the '<em><b>Fleet Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO__FLEET_MODEL = eINSTANCE.getScenario_FleetModel();

		/**
		 * The meta object literal for the '<em><b>Port Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO__PORT_MODEL = eINSTANCE.getScenario_PortModel();

		/**
		 * The meta object literal for the '<em><b>Cargo Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO__CARGO_MODEL = eINSTANCE.getScenario_CargoModel();

		/**
		 * The meta object literal for the '<em><b>Contract Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO__CONTRACT_MODEL = eINSTANCE.getScenario_ContractModel();

		/**
		 * The meta object literal for the '<em><b>Schedule Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO__SCHEDULE_MODEL = eINSTANCE.getScenario_ScheduleModel();

		/**
		 * The meta object literal for the '<em><b>Market Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO__MARKET_MODEL = eINSTANCE.getScenario_MarketModel();

		/**
		 * The meta object literal for the '<em><b>Distance Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO__DISTANCE_MODEL = eINSTANCE.getScenario_DistanceModel();

		/**
		 * The meta object literal for the '<em><b>Canal Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO__CANAL_MODEL = eINSTANCE.getScenario_CanalModel();

		/**
		 * The meta object literal for the '<em><b>Optimisation</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO__OPTIMISATION = eINSTANCE.getScenario_Optimisation();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO__VERSION = eINSTANCE.getScenario_Version();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO__NAME = eINSTANCE.getScenario_Name();

		/**
		 * The meta object literal for the '<em><b>Get Or Create Fleet Model</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SCENARIO___GET_OR_CREATE_FLEET_MODEL = eINSTANCE.getScenario__GetOrCreateFleetModel();

		/**
		 * The meta object literal for the '<em><b>Get Or Create Schedule Model</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SCENARIO___GET_OR_CREATE_SCHEDULE_MODEL = eINSTANCE.getScenario__GetOrCreateScheduleModel();

		/**
		 * The meta object literal for the '<em><b>Create Missing Models</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SCENARIO___CREATE_MISSING_MODELS = eINSTANCE.getScenario__CreateMissingModels();

		/**
		 * The meta object literal for the '{@link scenario.impl.ScenarioObjectImpl <em>Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.impl.ScenarioObjectImpl
		 * @see scenario.impl.ScenarioPackageImpl#getScenarioObject()
		 * @generated
		 */
		EClass SCENARIO_OBJECT = eINSTANCE.getScenarioObject();

		/**
		 * The meta object literal for the '<em><b>Get Container</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SCENARIO_OBJECT___GET_CONTAINER = eINSTANCE.getScenarioObject__GetContainer();

		/**
		 * The meta object literal for the '{@link scenario.impl.NamedObjectImpl <em>Named Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.impl.NamedObjectImpl
		 * @see scenario.impl.ScenarioPackageImpl#getNamedObject()
		 * @generated
		 */
		EClass NAMED_OBJECT = eINSTANCE.getNamedObject();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_OBJECT__NAME = eINSTANCE.getNamedObject_Name();

	}

} //ScenarioPackage
