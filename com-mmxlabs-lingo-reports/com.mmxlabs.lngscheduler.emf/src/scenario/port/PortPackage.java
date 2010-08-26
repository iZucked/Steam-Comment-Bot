/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__MARKET = 1;

	/**
	 * The number of structural features of the '<em>Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_FEATURE_COUNT = 2;


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
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL__DISTANCE = 1;

	/**
	 * The feature id for the '<em><b>Entry Distances</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL__ENTRY_DISTANCES = 2;

	/**
	 * The feature id for the '<em><b>Exit Distances</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL__EXIT_DISTANCES = 3;

	/**
	 * The number of structural features of the '<em>Canal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_FEATURE_COUNT = 4;

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
	 * The meta object id for the '{@link scenario.port.impl.PartialDistanceImpl <em>Partial Distance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.port.impl.PartialDistanceImpl
	 * @see scenario.port.impl.PortPackageImpl#getPartialDistance()
	 * @generated
	 */
	int PARTIAL_DISTANCE = 6;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_DISTANCE__DISTANCE = 0;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_DISTANCE__PORT = 1;

	/**
	 * The number of structural features of the '<em>Partial Distance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_DISTANCE_FEATURE_COUNT = 2;


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
	 * Returns the meta object for the reference '{@link scenario.port.Port#getMarket <em>Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market</em>'.
	 * @see scenario.port.Port#getMarket()
	 * @see #getPort()
	 * @generated
	 */
	EReference getPort_Market();

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
	 * Returns the meta object for the attribute '{@link scenario.port.Canal#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see scenario.port.Canal#getDistance()
	 * @see #getCanal()
	 * @generated
	 */
	EAttribute getCanal_Distance();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.port.Canal#getEntryDistances <em>Entry Distances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entry Distances</em>'.
	 * @see scenario.port.Canal#getEntryDistances()
	 * @see #getCanal()
	 * @generated
	 */
	EReference getCanal_EntryDistances();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.port.Canal#getExitDistances <em>Exit Distances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Exit Distances</em>'.
	 * @see scenario.port.Canal#getExitDistances()
	 * @see #getCanal()
	 * @generated
	 */
	EReference getCanal_ExitDistances();

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
	 * Returns the meta object for class '{@link scenario.port.PartialDistance <em>Partial Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Partial Distance</em>'.
	 * @see scenario.port.PartialDistance
	 * @generated
	 */
	EClass getPartialDistance();

	/**
	 * Returns the meta object for the attribute '{@link scenario.port.PartialDistance#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see scenario.port.PartialDistance#getDistance()
	 * @see #getPartialDistance()
	 * @generated
	 */
	EAttribute getPartialDistance_Distance();

	/**
	 * Returns the meta object for the reference '{@link scenario.port.PartialDistance#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see scenario.port.PartialDistance#getPort()
	 * @see #getPartialDistance()
	 * @generated
	 */
	EReference getPartialDistance_Port();

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
		 * The meta object literal for the '<em><b>Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT__MARKET = eINSTANCE.getPort_Market();

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
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL__DISTANCE = eINSTANCE.getCanal_Distance();

		/**
		 * The meta object literal for the '<em><b>Entry Distances</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CANAL__ENTRY_DISTANCES = eINSTANCE.getCanal_EntryDistances();

		/**
		 * The meta object literal for the '<em><b>Exit Distances</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CANAL__EXIT_DISTANCES = eINSTANCE.getCanal_ExitDistances();

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
		 * The meta object literal for the '{@link scenario.port.impl.PartialDistanceImpl <em>Partial Distance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.port.impl.PartialDistanceImpl
		 * @see scenario.port.impl.PortPackageImpl#getPartialDistance()
		 * @generated
		 */
		EClass PARTIAL_DISTANCE = eINSTANCE.getPartialDistance();

		/**
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARTIAL_DISTANCE__DISTANCE = eINSTANCE.getPartialDistance_Distance();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARTIAL_DISTANCE__PORT = eINSTANCE.getPartialDistance_Port();

	}

} //PortPackage
