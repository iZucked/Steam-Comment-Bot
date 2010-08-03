/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario;

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
	 * The number of structural features of the '<em>Scenario</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FEATURE_COUNT = 7;


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

	}

} //ScenarioPackage
