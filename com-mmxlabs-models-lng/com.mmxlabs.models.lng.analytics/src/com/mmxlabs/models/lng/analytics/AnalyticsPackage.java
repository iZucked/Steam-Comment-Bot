/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
 * @see com.mmxlabs.models.lng.analytics.AnalyticsFactory
 * @model kind="package"
 * @generated
 */
public interface AnalyticsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "analytics";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/analytics/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.analytics";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AnalyticsPackage eINSTANCE = com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAnalyticsModel()
	 * @generated
	 */
	int ANALYTICS_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Round Trip Matrices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__ROUND_TRIP_MATRICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl <em>Unit Cost Matrix</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getUnitCostMatrix()
	 * @generated
	 */
	int UNIT_COST_MATRIX = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__PORTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__VESSEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Notional Day Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__NOTIONAL_DAY_RATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__SPEED = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Round Trip</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__ROUND_TRIP = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Minimum Load</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__MINIMUM_LOAD = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Maximum Load</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__MAXIMUM_LOAD = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Minimum Discharge</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__MINIMUM_DISCHARGE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Maximum Discharge</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__MAXIMUM_DISCHARGE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Cargo Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__CARGO_PRICE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Base Fuel Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__BASE_FUEL_PRICE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__CV_VALUE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Discharge Idle Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__DISCHARGE_IDLE_TIME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Return Idle Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__RETURN_IDLE_TIME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Cost Lines</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__COST_LINES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 15;

	/**
	 * The number of structural features of the '<em>Unit Cost Matrix</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 16;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl <em>Unit Cost Line</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getUnitCostLine()
	 * @generated
	 */
	int UNIT_COST_LINE = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Unit Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__UNIT_COST = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Mmbtu Delivered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__MMBTU_DELIVERED = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>From</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__FROM = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__TO = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__DURATION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Volume Loaded</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__VOLUME_LOADED = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Volume Discharged</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__VOLUME_DISCHARGED = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__HIRE_COST = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Fuel Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__FUEL_COST = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Canal Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__CANAL_COST = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Cost Components</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__COST_COMPONENTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__PORT_COST = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The number of structural features of the '<em>Unit Cost Line</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 12;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.CostComponentImpl <em>Cost Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.CostComponentImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCostComponent()
	 * @generated
	 */
	int COST_COMPONENT = 5;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_COMPONENT__DURATION = 0;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_COMPONENT__HIRE_COST = 1;

	/**
	 * The feature id for the '<em><b>Fuel Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_COMPONENT__FUEL_COSTS = 2;

	/**
	 * The number of structural features of the '<em>Cost Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_COMPONENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.VoyageImpl <em>Voyage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.VoyageImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVoyage()
	 * @generated
	 */
	int VOYAGE = 3;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE__DURATION = COST_COMPONENT__DURATION;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE__HIRE_COST = COST_COMPONENT__HIRE_COST;

	/**
	 * The feature id for the '<em><b>Fuel Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE__FUEL_COSTS = COST_COMPONENT__FUEL_COSTS;

	/**
	 * The feature id for the '<em><b>Route</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE__ROUTE = COST_COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Route Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE__ROUTE_COST = COST_COMPONENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE__SPEED = COST_COMPONENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE__DISTANCE = COST_COMPONENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Idle Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE__IDLE_TIME = COST_COMPONENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Travel Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE__TRAVEL_TIME = COST_COMPONENT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Voyage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE_FEATURE_COUNT = COST_COMPONENT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.VisitImpl <em>Visit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.VisitImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVisit()
	 * @generated
	 */
	int VISIT = 4;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VISIT__DURATION = COST_COMPONENT__DURATION;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VISIT__HIRE_COST = COST_COMPONENT__HIRE_COST;

	/**
	 * The feature id for the '<em><b>Fuel Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VISIT__FUEL_COSTS = COST_COMPONENT__FUEL_COSTS;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VISIT__PORT_COST = COST_COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VISIT_FEATURE_COUNT = COST_COMPONENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.FuelCostImpl <em>Fuel Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.FuelCostImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getFuelCost()
	 * @generated
	 */
	int FUEL_COST = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_COST__NAME = 0;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_COST__UNIT = 1;

	/**
	 * The feature id for the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_COST__QUANTITY = 2;

	/**
	 * The feature id for the '<em><b>Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_COST__COST = 3;

	/**
	 * The number of structural features of the '<em>Fuel Cost</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_COST_FEATURE_COUNT = 4;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel
	 * @generated
	 */
	EClass getAnalyticsModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getRoundTripMatrices <em>Round Trip Matrices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Round Trip Matrices</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel#getRoundTripMatrices()
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	EReference getAnalyticsModel_RoundTripMatrices();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix <em>Unit Cost Matrix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unit Cost Matrix</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix
	 * @generated
	 */
	EClass getUnitCostMatrix();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ports</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getPorts()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EReference getUnitCostMatrix_Ports();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getVessel()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EReference getUnitCostMatrix_Vessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getNotionalDayRate <em>Notional Day Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notional Day Rate</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getNotionalDayRate()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_NotionalDayRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getSpeed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getSpeed()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_Speed();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#isRoundTrip <em>Round Trip</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Round Trip</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#isRoundTrip()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_RoundTrip();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMinimumLoad <em>Minimum Load</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minimum Load</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMinimumLoad()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_MinimumLoad();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMaximumLoad <em>Maximum Load</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum Load</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMaximumLoad()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_MaximumLoad();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMinimumDischarge <em>Minimum Discharge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minimum Discharge</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMinimumDischarge()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_MinimumDischarge();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMaximumDischarge <em>Maximum Discharge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum Discharge</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMaximumDischarge()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_MaximumDischarge();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCargoPrice <em>Cargo Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCargoPrice()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_CargoPrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getBaseFuelPrice <em>Base Fuel Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Fuel Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getBaseFuelPrice()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_BaseFuelPrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCvValue <em>Cv Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cv Value</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCvValue()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_CvValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getDischargeIdleTime <em>Discharge Idle Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discharge Idle Time</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getDischargeIdleTime()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_DischargeIdleTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getReturnIdleTime <em>Return Idle Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Return Idle Time</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getReturnIdleTime()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_ReturnIdleTime();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCostLines <em>Cost Lines</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cost Lines</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCostLines()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EReference getUnitCostMatrix_CostLines();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.UnitCostLine <em>Unit Cost Line</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unit Cost Line</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine
	 * @generated
	 */
	EClass getUnitCostLine();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getUnitCost <em>Unit Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getUnitCost()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EAttribute getUnitCostLine_UnitCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getMmbtuDelivered <em>Mmbtu Delivered</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mmbtu Delivered</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getMmbtuDelivered()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EAttribute getUnitCostLine_MmbtuDelivered();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getFrom()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EReference getUnitCostLine_From();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getTo()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EReference getUnitCostLine_To();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getDuration()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EAttribute getUnitCostLine_Duration();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getVolumeLoaded <em>Volume Loaded</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Loaded</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getVolumeLoaded()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EAttribute getUnitCostLine_VolumeLoaded();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getVolumeDischarged <em>Volume Discharged</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Discharged</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getVolumeDischarged()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EAttribute getUnitCostLine_VolumeDischarged();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getHireCost <em>Hire Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hire Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getHireCost()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EAttribute getUnitCostLine_HireCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getFuelCost <em>Fuel Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fuel Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getFuelCost()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EAttribute getUnitCostLine_FuelCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getCanalCost <em>Canal Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Canal Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getCanalCost()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EAttribute getUnitCostLine_CanalCost();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getCostComponents <em>Cost Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cost Components</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getCostComponents()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EReference getUnitCostLine_CostComponents();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getPortCost <em>Port Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Port Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getPortCost()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EAttribute getUnitCostLine_PortCost();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.Voyage <em>Voyage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Voyage</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Voyage
	 * @generated
	 */
	EClass getVoyage();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.Voyage#getRoute <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Voyage#getRoute()
	 * @see #getVoyage()
	 * @generated
	 */
	EAttribute getVoyage_Route();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.Voyage#getRouteCost <em>Route Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Voyage#getRouteCost()
	 * @see #getVoyage()
	 * @generated
	 */
	EAttribute getVoyage_RouteCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.Voyage#getSpeed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Voyage#getSpeed()
	 * @see #getVoyage()
	 * @generated
	 */
	EAttribute getVoyage_Speed();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.Voyage#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Voyage#getDistance()
	 * @see #getVoyage()
	 * @generated
	 */
	EAttribute getVoyage_Distance();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.Voyage#getIdleTime <em>Idle Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Idle Time</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Voyage#getIdleTime()
	 * @see #getVoyage()
	 * @generated
	 */
	EAttribute getVoyage_IdleTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.Voyage#getTravelTime <em>Travel Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Travel Time</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Voyage#getTravelTime()
	 * @see #getVoyage()
	 * @generated
	 */
	EAttribute getVoyage_TravelTime();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.Visit <em>Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Visit</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Visit
	 * @generated
	 */
	EClass getVisit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.Visit#getPortCost <em>Port Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Port Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Visit#getPortCost()
	 * @see #getVisit()
	 * @generated
	 */
	EAttribute getVisit_PortCost();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.CostComponent <em>Cost Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cost Component</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CostComponent
	 * @generated
	 */
	EClass getCostComponent();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.CostComponent#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CostComponent#getDuration()
	 * @see #getCostComponent()
	 * @generated
	 */
	EAttribute getCostComponent_Duration();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.CostComponent#getHireCost <em>Hire Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hire Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CostComponent#getHireCost()
	 * @see #getCostComponent()
	 * @generated
	 */
	EAttribute getCostComponent_HireCost();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.CostComponent#getFuelCosts <em>Fuel Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fuel Costs</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CostComponent#getFuelCosts()
	 * @see #getCostComponent()
	 * @generated
	 */
	EReference getCostComponent_FuelCosts();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.FuelCost <em>Fuel Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fuel Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FuelCost
	 * @generated
	 */
	EClass getFuelCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.FuelCost#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FuelCost#getName()
	 * @see #getFuelCost()
	 * @generated
	 */
	EAttribute getFuelCost_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.FuelCost#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FuelCost#getUnit()
	 * @see #getFuelCost()
	 * @generated
	 */
	EAttribute getFuelCost_Unit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.FuelCost#getQuantity <em>Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Quantity</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FuelCost#getQuantity()
	 * @see #getFuelCost()
	 * @generated
	 */
	EAttribute getFuelCost_Quantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.FuelCost#getCost <em>Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FuelCost#getCost()
	 * @see #getFuelCost()
	 * @generated
	 */
	EAttribute getFuelCost_Cost();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AnalyticsFactory getAnalyticsFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAnalyticsModel()
		 * @generated
		 */
		EClass ANALYTICS_MODEL = eINSTANCE.getAnalyticsModel();

		/**
		 * The meta object literal for the '<em><b>Round Trip Matrices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYTICS_MODEL__ROUND_TRIP_MATRICES = eINSTANCE.getAnalyticsModel_RoundTripMatrices();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl <em>Unit Cost Matrix</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getUnitCostMatrix()
		 * @generated
		 */
		EClass UNIT_COST_MATRIX = eINSTANCE.getUnitCostMatrix();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNIT_COST_MATRIX__PORTS = eINSTANCE.getUnitCostMatrix_Ports();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNIT_COST_MATRIX__VESSEL = eINSTANCE.getUnitCostMatrix_Vessel();

		/**
		 * The meta object literal for the '<em><b>Notional Day Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__NOTIONAL_DAY_RATE = eINSTANCE.getUnitCostMatrix_NotionalDayRate();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__SPEED = eINSTANCE.getUnitCostMatrix_Speed();

		/**
		 * The meta object literal for the '<em><b>Round Trip</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__ROUND_TRIP = eINSTANCE.getUnitCostMatrix_RoundTrip();

		/**
		 * The meta object literal for the '<em><b>Minimum Load</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__MINIMUM_LOAD = eINSTANCE.getUnitCostMatrix_MinimumLoad();

		/**
		 * The meta object literal for the '<em><b>Maximum Load</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__MAXIMUM_LOAD = eINSTANCE.getUnitCostMatrix_MaximumLoad();

		/**
		 * The meta object literal for the '<em><b>Minimum Discharge</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__MINIMUM_DISCHARGE = eINSTANCE.getUnitCostMatrix_MinimumDischarge();

		/**
		 * The meta object literal for the '<em><b>Maximum Discharge</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__MAXIMUM_DISCHARGE = eINSTANCE.getUnitCostMatrix_MaximumDischarge();

		/**
		 * The meta object literal for the '<em><b>Cargo Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__CARGO_PRICE = eINSTANCE.getUnitCostMatrix_CargoPrice();

		/**
		 * The meta object literal for the '<em><b>Base Fuel Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__BASE_FUEL_PRICE = eINSTANCE.getUnitCostMatrix_BaseFuelPrice();

		/**
		 * The meta object literal for the '<em><b>Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__CV_VALUE = eINSTANCE.getUnitCostMatrix_CvValue();

		/**
		 * The meta object literal for the '<em><b>Discharge Idle Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__DISCHARGE_IDLE_TIME = eINSTANCE.getUnitCostMatrix_DischargeIdleTime();

		/**
		 * The meta object literal for the '<em><b>Return Idle Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__RETURN_IDLE_TIME = eINSTANCE.getUnitCostMatrix_ReturnIdleTime();

		/**
		 * The meta object literal for the '<em><b>Cost Lines</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNIT_COST_MATRIX__COST_LINES = eINSTANCE.getUnitCostMatrix_CostLines();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl <em>Unit Cost Line</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getUnitCostLine()
		 * @generated
		 */
		EClass UNIT_COST_LINE = eINSTANCE.getUnitCostLine();

		/**
		 * The meta object literal for the '<em><b>Unit Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_LINE__UNIT_COST = eINSTANCE.getUnitCostLine_UnitCost();

		/**
		 * The meta object literal for the '<em><b>Mmbtu Delivered</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_LINE__MMBTU_DELIVERED = eINSTANCE.getUnitCostLine_MmbtuDelivered();

		/**
		 * The meta object literal for the '<em><b>From</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNIT_COST_LINE__FROM = eINSTANCE.getUnitCostLine_From();

		/**
		 * The meta object literal for the '<em><b>To</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNIT_COST_LINE__TO = eINSTANCE.getUnitCostLine_To();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_LINE__DURATION = eINSTANCE.getUnitCostLine_Duration();

		/**
		 * The meta object literal for the '<em><b>Volume Loaded</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_LINE__VOLUME_LOADED = eINSTANCE.getUnitCostLine_VolumeLoaded();

		/**
		 * The meta object literal for the '<em><b>Volume Discharged</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_LINE__VOLUME_DISCHARGED = eINSTANCE.getUnitCostLine_VolumeDischarged();

		/**
		 * The meta object literal for the '<em><b>Hire Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_LINE__HIRE_COST = eINSTANCE.getUnitCostLine_HireCost();

		/**
		 * The meta object literal for the '<em><b>Fuel Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_LINE__FUEL_COST = eINSTANCE.getUnitCostLine_FuelCost();

		/**
		 * The meta object literal for the '<em><b>Canal Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_LINE__CANAL_COST = eINSTANCE.getUnitCostLine_CanalCost();

		/**
		 * The meta object literal for the '<em><b>Cost Components</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNIT_COST_LINE__COST_COMPONENTS = eINSTANCE.getUnitCostLine_CostComponents();

		/**
		 * The meta object literal for the '<em><b>Port Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_LINE__PORT_COST = eINSTANCE.getUnitCostLine_PortCost();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.VoyageImpl <em>Voyage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.VoyageImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVoyage()
		 * @generated
		 */
		EClass VOYAGE = eINSTANCE.getVoyage();

		/**
		 * The meta object literal for the '<em><b>Route</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VOYAGE__ROUTE = eINSTANCE.getVoyage_Route();

		/**
		 * The meta object literal for the '<em><b>Route Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VOYAGE__ROUTE_COST = eINSTANCE.getVoyage_RouteCost();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VOYAGE__SPEED = eINSTANCE.getVoyage_Speed();

		/**
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VOYAGE__DISTANCE = eINSTANCE.getVoyage_Distance();

		/**
		 * The meta object literal for the '<em><b>Idle Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VOYAGE__IDLE_TIME = eINSTANCE.getVoyage_IdleTime();

		/**
		 * The meta object literal for the '<em><b>Travel Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VOYAGE__TRAVEL_TIME = eINSTANCE.getVoyage_TravelTime();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.VisitImpl <em>Visit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.VisitImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVisit()
		 * @generated
		 */
		EClass VISIT = eINSTANCE.getVisit();

		/**
		 * The meta object literal for the '<em><b>Port Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VISIT__PORT_COST = eINSTANCE.getVisit_PortCost();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.CostComponentImpl <em>Cost Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.CostComponentImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCostComponent()
		 * @generated
		 */
		EClass COST_COMPONENT = eINSTANCE.getCostComponent();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COST_COMPONENT__DURATION = eINSTANCE.getCostComponent_Duration();

		/**
		 * The meta object literal for the '<em><b>Hire Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COST_COMPONENT__HIRE_COST = eINSTANCE.getCostComponent_HireCost();

		/**
		 * The meta object literal for the '<em><b>Fuel Costs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COST_COMPONENT__FUEL_COSTS = eINSTANCE.getCostComponent_FuelCosts();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.FuelCostImpl <em>Fuel Cost</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.FuelCostImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getFuelCost()
		 * @generated
		 */
		EClass FUEL_COST = eINSTANCE.getFuelCost();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_COST__NAME = eINSTANCE.getFuelCost_Name();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_COST__UNIT = eINSTANCE.getFuelCost_Unit();

		/**
		 * The meta object literal for the '<em><b>Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_COST__QUANTITY = eINSTANCE.getFuelCost_Quantity();

		/**
		 * The meta object literal for the '<em><b>Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_COST__COST = eINSTANCE.getFuelCost_Cost();

	}

} //AnalyticsPackage
