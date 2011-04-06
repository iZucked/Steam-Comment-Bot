/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.port;

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
 * @see scenario.port.PortFactory
 * @model kind="package"
 * @generated
 */
public interface PortPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "port";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf/port";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.port";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PortPackage eINSTANCE = scenario.port.impl.PortPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.port.impl.PortModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.port.impl.PortModelImpl
	 * @see scenario.port.impl.PortPackageImpl#getPortModel()
	 * @generated
	 */
	int PORT_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__PORTS = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.port.impl.PortImpl <em>Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.port.impl.PortImpl
	 * @see scenario.port.impl.PortPackageImpl#getPort()
	 * @generated
	 */
	int PORT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Default Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DEFAULT_MARKET = 1;

	/**
	 * The feature id for the '<em><b>Time Zone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__TIME_ZONE = 2;

	/**
	 * The feature id for the '<em><b>Default Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DEFAULT_CONTRACT = 3;

	/**
	 * The number of structural features of the '<em>Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_FEATURE_COUNT = 4;


	/**
	 * The number of operations of the '<em>Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.port.impl.DistanceModelImpl <em>Distance Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.port.impl.DistanceModelImpl
	 * @see scenario.port.impl.PortPackageImpl#getDistanceModel()
	 * @generated
	 */
	int DISTANCE_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Distances</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTANCE_MODEL__DISTANCES = 0;

	/**
	 * The number of structural features of the '<em>Distance Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTANCE_MODEL_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Distance Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTANCE_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.port.impl.DistanceLineImpl <em>Distance Line</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.port.impl.DistanceLineImpl
	 * @see scenario.port.impl.PortPackageImpl#getDistanceLine()
	 * @generated
	 */
	int DISTANCE_LINE = 3;

	/**
	 * The feature id for the '<em><b>From Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTANCE_LINE__FROM_PORT = 0;

	/**
	 * The feature id for the '<em><b>To Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTANCE_LINE__TO_PORT = 1;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTANCE_LINE__DISTANCE = 2;

	/**
	 * The number of structural features of the '<em>Distance Line</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTANCE_LINE_FEATURE_COUNT = 3;


	/**
	 * The number of operations of the '<em>Distance Line</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTANCE_LINE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.port.impl.CanalImpl <em>Canal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.port.impl.CanalImpl
	 * @see scenario.port.impl.PortPackageImpl#getCanal()
	 * @generated
	 */
	int CANAL = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL__NAME = 0;

	/**
	 * The feature id for the '<em><b>Class Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL__CLASS_COSTS = 1;

	/**
	 * The feature id for the '<em><b>Default Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL__DEFAULT_COST = 2;

	/**
	 * The feature id for the '<em><b>Distance Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL__DISTANCE_MODEL = 3;

	/**
	 * The number of structural features of the '<em>Canal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Canal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.port.impl.CanalModelImpl <em>Canal Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.port.impl.CanalModelImpl
	 * @see scenario.port.impl.PortPackageImpl#getCanalModel()
	 * @generated
	 */
	int CANAL_MODEL = 5;

	/**
	 * The feature id for the '<em><b>Canals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_MODEL__CANALS = 0;

	/**
	 * The number of structural features of the '<em>Canal Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_MODEL_FEATURE_COUNT = 1;


	/**
	 * The number of operations of the '<em>Canal Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.port.impl.VesselClassCostImpl <em>Vessel Class Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.port.impl.VesselClassCostImpl
	 * @see scenario.port.impl.PortPackageImpl#getVesselClassCost()
	 * @generated
	 */
	int VESSEL_CLASS_COST = 6;

	/**
	 * The feature id for the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST__VESSEL_CLASS = 0;

	/**
	 * The feature id for the '<em><b>Laden Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST__LADEN_COST = 1;

	/**
	 * The feature id for the '<em><b>Unladen Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST__UNLADEN_COST = 2;

	/**
	 * The feature id for the '<em><b>Transit Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST__TRANSIT_TIME = 3;

	/**
	 * The feature id for the '<em><b>Transit Fuel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST__TRANSIT_FUEL = 4;

	/**
	 * The number of structural features of the '<em>Vessel Class Cost</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Vessel Class Cost</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST_OPERATION_COUNT = 0;

	/**
	 * Returns the meta object for class '{@link scenario.port.PortModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see scenario.port.PortModel
	 * @generated
	 */
	EClass getPortModel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.port.PortModel#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Ports</em>'.
	 * @see scenario.port.PortModel#getPorts()
	 * @see #getPortModel()
	 * @generated
	 */
	EReference getPortModel_Ports();

	/**
	 * Returns the meta object for class '{@link scenario.port.Port <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port</em>'.
	 * @see scenario.port.Port
	 * @generated
	 */
	EClass getPort();

	/**
	 * Returns the meta object for the attribute '{@link scenario.port.Port#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.port.Port#getName()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_Name();

	/**
	 * Returns the meta object for the reference '{@link scenario.port.Port#getDefaultMarket <em>Default Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Default Market</em>'.
	 * @see scenario.port.Port#getDefaultMarket()
	 * @see #getPort()
	 * @generated
	 */
	EReference getPort_DefaultMarket();

	/**
	 * Returns the meta object for the attribute '{@link scenario.port.Port#getTimeZone <em>Time Zone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time Zone</em>'.
	 * @see scenario.port.Port#getTimeZone()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_TimeZone();

	/**
	 * Returns the meta object for the reference '{@link scenario.port.Port#getDefaultContract <em>Default Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Default Contract</em>'.
	 * @see scenario.port.Port#getDefaultContract()
	 * @see #getPort()
	 * @generated
	 */
	EReference getPort_DefaultContract();

	/**
	 * Returns the meta object for class '{@link scenario.port.DistanceModel <em>Distance Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Distance Model</em>'.
	 * @see scenario.port.DistanceModel
	 * @generated
	 */
	EClass getDistanceModel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.port.DistanceModel#getDistances <em>Distances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Distances</em>'.
	 * @see scenario.port.DistanceModel#getDistances()
	 * @see #getDistanceModel()
	 * @generated
	 */
	EReference getDistanceModel_Distances();

	/**
	 * Returns the meta object for class '{@link scenario.port.DistanceLine <em>Distance Line</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Distance Line</em>'.
	 * @see scenario.port.DistanceLine
	 * @generated
	 */
	EClass getDistanceLine();

	/**
	 * Returns the meta object for the reference '{@link scenario.port.DistanceLine#getFromPort <em>From Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From Port</em>'.
	 * @see scenario.port.DistanceLine#getFromPort()
	 * @see #getDistanceLine()
	 * @generated
	 */
	EReference getDistanceLine_FromPort();

	/**
	 * Returns the meta object for the reference '{@link scenario.port.DistanceLine#getToPort <em>To Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To Port</em>'.
	 * @see scenario.port.DistanceLine#getToPort()
	 * @see #getDistanceLine()
	 * @generated
	 */
	EReference getDistanceLine_ToPort();

	/**
	 * Returns the meta object for the attribute '{@link scenario.port.DistanceLine#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see scenario.port.DistanceLine#getDistance()
	 * @see #getDistanceLine()
	 * @generated
	 */
	EAttribute getDistanceLine_Distance();

	/**
	 * Returns the meta object for class '{@link scenario.port.Canal <em>Canal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Canal</em>'.
	 * @see scenario.port.Canal
	 * @generated
	 */
	EClass getCanal();

	/**
	 * Returns the meta object for the attribute '{@link scenario.port.Canal#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.port.Canal#getName()
	 * @see #getCanal()
	 * @generated
	 */
	EAttribute getCanal_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.port.Canal#getClassCosts <em>Class Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Class Costs</em>'.
	 * @see scenario.port.Canal#getClassCosts()
	 * @see #getCanal()
	 * @generated
	 */
	EReference getCanal_ClassCosts();

	/**
	 * Returns the meta object for the attribute '{@link scenario.port.Canal#getDefaultCost <em>Default Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Cost</em>'.
	 * @see scenario.port.Canal#getDefaultCost()
	 * @see #getCanal()
	 * @generated
	 */
	EAttribute getCanal_DefaultCost();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.port.Canal#getDistanceModel <em>Distance Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Distance Model</em>'.
	 * @see scenario.port.Canal#getDistanceModel()
	 * @see #getCanal()
	 * @generated
	 */
	EReference getCanal_DistanceModel();

	/**
	 * Returns the meta object for class '{@link scenario.port.CanalModel <em>Canal Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Canal Model</em>'.
	 * @see scenario.port.CanalModel
	 * @generated
	 */
	EClass getCanalModel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.port.CanalModel#getCanals <em>Canals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Canals</em>'.
	 * @see scenario.port.CanalModel#getCanals()
	 * @see #getCanalModel()
	 * @generated
	 */
	EReference getCanalModel_Canals();

	/**
	 * Returns the meta object for class '{@link scenario.port.VesselClassCost <em>Vessel Class Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Class Cost</em>'.
	 * @see scenario.port.VesselClassCost
	 * @generated
	 */
	EClass getVesselClassCost();

	/**
	 * Returns the meta object for the reference '{@link scenario.port.VesselClassCost#getVesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Class</em>'.
	 * @see scenario.port.VesselClassCost#getVesselClass()
	 * @see #getVesselClassCost()
	 * @generated
	 */
	EReference getVesselClassCost_VesselClass();

	/**
	 * Returns the meta object for the attribute '{@link scenario.port.VesselClassCost#getLadenCost <em>Laden Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden Cost</em>'.
	 * @see scenario.port.VesselClassCost#getLadenCost()
	 * @see #getVesselClassCost()
	 * @generated
	 */
	EAttribute getVesselClassCost_LadenCost();

	/**
	 * Returns the meta object for the attribute '{@link scenario.port.VesselClassCost#getUnladenCost <em>Unladen Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unladen Cost</em>'.
	 * @see scenario.port.VesselClassCost#getUnladenCost()
	 * @see #getVesselClassCost()
	 * @generated
	 */
	EAttribute getVesselClassCost_UnladenCost();

	/**
	 * Returns the meta object for the attribute '{@link scenario.port.VesselClassCost#getTransitTime <em>Transit Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transit Time</em>'.
	 * @see scenario.port.VesselClassCost#getTransitTime()
	 * @see #getVesselClassCost()
	 * @generated
	 */
	EAttribute getVesselClassCost_TransitTime();

	/**
	 * Returns the meta object for the attribute '{@link scenario.port.VesselClassCost#getTransitFuel <em>Transit Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transit Fuel</em>'.
	 * @see scenario.port.VesselClassCost#getTransitFuel()
	 * @see #getVesselClassCost()
	 * @generated
	 */
	EAttribute getVesselClassCost_TransitFuel();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PortFactory getPortFactory();

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
		 * The meta object literal for the '{@link scenario.port.impl.PortModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.port.impl.PortModelImpl
		 * @see scenario.port.impl.PortPackageImpl#getPortModel()
		 * @generated
		 */
		EClass PORT_MODEL = eINSTANCE.getPortModel();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_MODEL__PORTS = eINSTANCE.getPortModel_Ports();

		/**
		 * The meta object literal for the '{@link scenario.port.impl.PortImpl <em>Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.port.impl.PortImpl
		 * @see scenario.port.impl.PortPackageImpl#getPort()
		 * @generated
		 */
		EClass PORT = eINSTANCE.getPort();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__NAME = eINSTANCE.getPort_Name();

		/**
		 * The meta object literal for the '<em><b>Default Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT__DEFAULT_MARKET = eINSTANCE.getPort_DefaultMarket();

		/**
		 * The meta object literal for the '<em><b>Time Zone</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__TIME_ZONE = eINSTANCE.getPort_TimeZone();

		/**
		 * The meta object literal for the '<em><b>Default Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT__DEFAULT_CONTRACT = eINSTANCE.getPort_DefaultContract();

		/**
		 * The meta object literal for the '{@link scenario.port.impl.DistanceModelImpl <em>Distance Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.port.impl.DistanceModelImpl
		 * @see scenario.port.impl.PortPackageImpl#getDistanceModel()
		 * @generated
		 */
		EClass DISTANCE_MODEL = eINSTANCE.getDistanceModel();

		/**
		 * The meta object literal for the '<em><b>Distances</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DISTANCE_MODEL__DISTANCES = eINSTANCE.getDistanceModel_Distances();

		/**
		 * The meta object literal for the '{@link scenario.port.impl.DistanceLineImpl <em>Distance Line</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.port.impl.DistanceLineImpl
		 * @see scenario.port.impl.PortPackageImpl#getDistanceLine()
		 * @generated
		 */
		EClass DISTANCE_LINE = eINSTANCE.getDistanceLine();

		/**
		 * The meta object literal for the '<em><b>From Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DISTANCE_LINE__FROM_PORT = eINSTANCE.getDistanceLine_FromPort();

		/**
		 * The meta object literal for the '<em><b>To Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DISTANCE_LINE__TO_PORT = eINSTANCE.getDistanceLine_ToPort();

		/**
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISTANCE_LINE__DISTANCE = eINSTANCE.getDistanceLine_Distance();

		/**
		 * The meta object literal for the '{@link scenario.port.impl.CanalImpl <em>Canal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.port.impl.CanalImpl
		 * @see scenario.port.impl.PortPackageImpl#getCanal()
		 * @generated
		 */
		EClass CANAL = eINSTANCE.getCanal();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL__NAME = eINSTANCE.getCanal_Name();

		/**
		 * The meta object literal for the '<em><b>Class Costs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CANAL__CLASS_COSTS = eINSTANCE.getCanal_ClassCosts();

		/**
		 * The meta object literal for the '<em><b>Default Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL__DEFAULT_COST = eINSTANCE.getCanal_DefaultCost();

		/**
		 * The meta object literal for the '<em><b>Distance Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CANAL__DISTANCE_MODEL = eINSTANCE.getCanal_DistanceModel();

		/**
		 * The meta object literal for the '{@link scenario.port.impl.CanalModelImpl <em>Canal Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.port.impl.CanalModelImpl
		 * @see scenario.port.impl.PortPackageImpl#getCanalModel()
		 * @generated
		 */
		EClass CANAL_MODEL = eINSTANCE.getCanalModel();

		/**
		 * The meta object literal for the '<em><b>Canals</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CANAL_MODEL__CANALS = eINSTANCE.getCanalModel_Canals();

		/**
		 * The meta object literal for the '{@link scenario.port.impl.VesselClassCostImpl <em>Vessel Class Cost</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.port.impl.VesselClassCostImpl
		 * @see scenario.port.impl.PortPackageImpl#getVesselClassCost()
		 * @generated
		 */
		EClass VESSEL_CLASS_COST = eINSTANCE.getVesselClassCost();

		/**
		 * The meta object literal for the '<em><b>Vessel Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CLASS_COST__VESSEL_CLASS = eINSTANCE.getVesselClassCost_VesselClass();

		/**
		 * The meta object literal for the '<em><b>Laden Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS_COST__LADEN_COST = eINSTANCE.getVesselClassCost_LadenCost();

		/**
		 * The meta object literal for the '<em><b>Unladen Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS_COST__UNLADEN_COST = eINSTANCE.getVesselClassCost_UnladenCost();

		/**
		 * The meta object literal for the '<em><b>Transit Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS_COST__TRANSIT_TIME = eINSTANCE.getVesselClassCost_TransitTime();

		/**
		 * The meta object literal for the '<em><b>Transit Fuel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS_COST__TRANSIT_FUEL = eINSTANCE.getVesselClassCost_TransitFuel();

	}

} //PortPackage
