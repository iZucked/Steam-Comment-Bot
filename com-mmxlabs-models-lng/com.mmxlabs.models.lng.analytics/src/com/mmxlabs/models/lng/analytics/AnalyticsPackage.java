/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @noimplement This interface is not intended to be implemented by clients.
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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Round Trip Matrices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__ROUND_TRIP_MATRICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Selected Matrix</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__SELECTED_MATRIX = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Shipping Cost Plans</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__SHIPPING_COST_PLANS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Cargo Sandboxes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__CARGO_SANDBOXES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

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
	 * The feature id for the '<em><b>From Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__FROM_PORTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>To Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__TO_PORTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__VESSEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Notional Day Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__NOTIONAL_DAY_RATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__SPEED = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Round Trip</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__ROUND_TRIP = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Minimum Load</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__MINIMUM_LOAD = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Maximum Load</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__MAXIMUM_LOAD = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Minimum Discharge</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__MINIMUM_DISCHARGE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Maximum Discharge</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__MAXIMUM_DISCHARGE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Retain Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__RETAIN_HEEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Cargo Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__CARGO_PRICE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Base Fuel Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__BASE_FUEL_PRICE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__CV_VALUE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Cost Lines</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__COST_LINES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Allowed Routes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__ALLOWED_ROUTES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Revenue Share</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__REVENUE_SHARE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Laden Time Allowance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__LADEN_TIME_ALLOWANCE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 18;

	/**
	 * The feature id for the '<em><b>Ballast Time Allowance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX__BALLAST_TIME_ALLOWANCE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 19;

	/**
	 * The number of structural features of the '<em>Unit Cost Matrix</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_MATRIX_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 20;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Profit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE__PROFIT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The number of structural features of the '<em>Unit Cost Line</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_COST_LINE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 13;


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
	 * The feature id for the '<em><b>Route</b></em>' reference.
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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.JourneyImpl <em>Journey</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.JourneyImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getJourney()
	 * @generated
	 */
	int JOURNEY = 7;

	/**
	 * The feature id for the '<em><b>From</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__FROM = 0;

	/**
	 * The feature id for the '<em><b>To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__TO = 1;

	/**
	 * The number of structural features of the '<em>Journey</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY_FEATURE_COUNT = 2;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostPlanImpl <em>Shipping Cost Plan</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ShippingCostPlanImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getShippingCostPlan()
	 * @generated
	 */
	int SHIPPING_COST_PLAN = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_PLAN__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_PLAN__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_PLAN__VESSEL = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Notional Day Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_PLAN__NOTIONAL_DAY_RATE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Base Fuel Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_PLAN__BASE_FUEL_PRICE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_PLAN__ROWS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Shipping Cost Plan</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_PLAN_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostRowImpl <em>Shipping Cost Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ShippingCostRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getShippingCostRow()
	 * @generated
	 */
	int SHIPPING_COST_ROW = 9;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_ROW__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_ROW__PORT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_ROW__DATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Cargo Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_ROW__CARGO_PRICE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_ROW__CV_VALUE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Destination Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_ROW__DESTINATION_TYPE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Heel Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_ROW__HEEL_VOLUME = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Include Port Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_ROW__INCLUDE_PORT_COSTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Shipping Cost Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_COST_ROW_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.CargoSandboxImpl <em>Cargo Sandbox</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.CargoSandboxImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCargoSandbox()
	 * @generated
	 */
	int CARGO_SANDBOX = 10;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SANDBOX__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SANDBOX__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Cargoes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SANDBOX__CARGOES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Cargo Sandbox</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SANDBOX_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ProvisionalCargoImpl <em>Provisional Cargo</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ProvisionalCargoImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getProvisionalCargo()
	 * @generated
	 */
	int PROVISIONAL_CARGO = 11;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVISIONAL_CARGO__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Buy</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVISIONAL_CARGO__BUY = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sell</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVISIONAL_CARGO__SELL = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVISIONAL_CARGO__VESSEL = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Portfolio Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVISIONAL_CARGO__PORTFOLIO_MODEL = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Provisional Cargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVISIONAL_CARGO_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl <em>Buy Opportunity</em>}' class.
	 * <!-- begin-user-doc -->
s	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyOpportunity()
	 * @generated
	 */
	int BUY_OPPORTUNITY = 12;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__PORT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__CONTRACT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__DATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__PRICE_EXPRESSION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Buy Opportunity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SellOpportunityImpl <em>Sell Opportunity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SellOpportunityImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellOpportunity()
	 * @generated
	 */
	int SELL_OPPORTUNITY = 13;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__PORT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__CONTRACT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__DATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__PRICE_EXPRESSION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Sell Opportunity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.DestinationType <em>Destination Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.DestinationType
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getDestinationType()
	 * @generated
	 */
	int DESTINATION_TYPE = 14;


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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getSelectedMatrix <em>Selected Matrix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Selected Matrix</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel#getSelectedMatrix()
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	EReference getAnalyticsModel_SelectedMatrix();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getShippingCostPlans <em>Shipping Cost Plans</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Shipping Cost Plans</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel#getShippingCostPlans()
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	EReference getAnalyticsModel_ShippingCostPlans();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getCargoSandboxes <em>Cargo Sandboxes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargo Sandboxes</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel#getCargoSandboxes()
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	EReference getAnalyticsModel_CargoSandboxes();

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
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getFromPorts <em>From Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>From Ports</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getFromPorts()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EReference getUnitCostMatrix_FromPorts();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getToPorts <em>To Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>To Ports</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getToPorts()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EReference getUnitCostMatrix_ToPorts();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getRetainHeel <em>Retain Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Retain Heel</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getRetainHeel()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_RetainHeel();

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
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getAllowedRoutes <em>Allowed Routes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Allowed Routes</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getAllowedRoutes()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EReference getUnitCostMatrix_AllowedRoutes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getRevenueShare <em>Revenue Share</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Revenue Share</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getRevenueShare()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_RevenueShare();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getLadenTimeAllowance <em>Laden Time Allowance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden Time Allowance</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getLadenTimeAllowance()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_LadenTimeAllowance();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getBallastTimeAllowance <em>Ballast Time Allowance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Time Allowance</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix#getBallastTimeAllowance()
	 * @see #getUnitCostMatrix()
	 * @generated
	 */
	EAttribute getUnitCostMatrix_BallastTimeAllowance();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.UnitCostLine#getProfit <em>Profit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Profit</em>'.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine#getProfit()
	 * @see #getUnitCostLine()
	 * @generated
	 */
	EAttribute getUnitCostLine_Profit();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.Voyage#getRoute <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Route</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Voyage#getRoute()
	 * @see #getVoyage()
	 * @generated
	 */
	EReference getVoyage_Route();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.Journey <em>Journey</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Journey</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Journey
	 * @generated
	 */
	EClass getJourney();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.Journey#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Journey#getFrom()
	 * @see #getJourney()
	 * @generated
	 */
	EReference getJourney_From();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.Journey#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Journey#getTo()
	 * @see #getJourney()
	 * @generated
	 */
	EReference getJourney_To();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan <em>Shipping Cost Plan</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Shipping Cost Plan</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostPlan
	 * @generated
	 */
	EClass getShippingCostPlan();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostPlan#getVessel()
	 * @see #getShippingCostPlan()
	 * @generated
	 */
	EReference getShippingCostPlan_Vessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getNotionalDayRate <em>Notional Day Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notional Day Rate</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostPlan#getNotionalDayRate()
	 * @see #getShippingCostPlan()
	 * @generated
	 */
	EAttribute getShippingCostPlan_NotionalDayRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getBaseFuelPrice <em>Base Fuel Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Fuel Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostPlan#getBaseFuelPrice()
	 * @see #getShippingCostPlan()
	 * @generated
	 */
	EAttribute getShippingCostPlan_BaseFuelPrice();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostPlan#getRows()
	 * @see #getShippingCostPlan()
	 * @generated
	 */
	EReference getShippingCostPlan_Rows();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow <em>Shipping Cost Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Shipping Cost Row</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostRow
	 * @generated
	 */
	EClass getShippingCostRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostRow#getPort()
	 * @see #getShippingCostRow()
	 * @generated
	 */
	EReference getShippingCostRow_Port();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostRow#getDate()
	 * @see #getShippingCostRow()
	 * @generated
	 */
	EAttribute getShippingCostRow_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getCargoPrice <em>Cargo Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostRow#getCargoPrice()
	 * @see #getShippingCostRow()
	 * @generated
	 */
	EAttribute getShippingCostRow_CargoPrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getCvValue <em>Cv Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cv Value</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostRow#getCvValue()
	 * @see #getShippingCostRow()
	 * @generated
	 */
	EAttribute getShippingCostRow_CvValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getDestinationType <em>Destination Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Destination Type</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostRow#getDestinationType()
	 * @see #getShippingCostRow()
	 * @generated
	 */
	EAttribute getShippingCostRow_DestinationType();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getHeelVolume <em>Heel Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Heel Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostRow#getHeelVolume()
	 * @see #getShippingCostRow()
	 * @generated
	 */
	EAttribute getShippingCostRow_HeelVolume();
	
	
	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#isIncludePortCosts <em>Include Port Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Include Port Costs</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostRow#isIncludePortCosts()
	 * @see #getShippingCostRow()
	 * @generated
	 */
	EAttribute getShippingCostRow_IncludePortCosts();


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.CargoSandbox <em>Cargo Sandbox</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo Sandbox</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CargoSandbox
	 * @generated
	 */
	EClass getCargoSandbox();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.CargoSandbox#getCargoes <em>Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargoes</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CargoSandbox#getCargoes()
	 * @see #getCargoSandbox()
	 * @generated
	 */
	EReference getCargoSandbox_Cargoes();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo <em>Provisional Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Provisional Cargo</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ProvisionalCargo
	 * @generated
	 */
	EClass getProvisionalCargo();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getBuy <em>Buy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Buy</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ProvisionalCargo#getBuy()
	 * @see #getProvisionalCargo()
	 * @generated
	 */
	EReference getProvisionalCargo_Buy();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getSell <em>Sell</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Sell</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ProvisionalCargo#getSell()
	 * @see #getProvisionalCargo()
	 * @generated
	 */
	EReference getProvisionalCargo_Sell();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ProvisionalCargo#getVessel()
	 * @see #getProvisionalCargo()
	 * @generated
	 */
	EReference getProvisionalCargo_Vessel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getPortfolioModel <em>Portfolio Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Portfolio Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ProvisionalCargo#getPortfolioModel()
	 * @see #getProvisionalCargo()
	 * @generated
	 */
	EReference getProvisionalCargo_PortfolioModel();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity <em>Buy Opportunity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Buy Opportunity</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity
	 * @generated
	 */
	EClass getBuyOpportunity();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getPort()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EReference getBuyOpportunity_Port();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getContract()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EReference getBuyOpportunity_Contract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getDate()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getPriceExpression()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_PriceExpression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SellOpportunity <em>Sell Opportunity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sell Opportunity</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity
	 * @generated
	 */
	EClass getSellOpportunity();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getPort()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EReference getSellOpportunity_Port();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getContract()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EReference getSellOpportunity_Contract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getDate()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getPriceExpression()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_PriceExpression();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.analytics.DestinationType <em>Destination Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Destination Type</em>'.
	 * @see com.mmxlabs.models.lng.analytics.DestinationType
	 * @generated
	 */
	EEnum getDestinationType();

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
	 * @noimplement This interface is not intended to be implemented by clients.
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
		 * The meta object literal for the '<em><b>Selected Matrix</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYTICS_MODEL__SELECTED_MATRIX = eINSTANCE.getAnalyticsModel_SelectedMatrix();

		/**
		 * The meta object literal for the '<em><b>Shipping Cost Plans</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYTICS_MODEL__SHIPPING_COST_PLANS = eINSTANCE.getAnalyticsModel_ShippingCostPlans();

		/**
		 * The meta object literal for the '<em><b>Cargo Sandboxes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYTICS_MODEL__CARGO_SANDBOXES = eINSTANCE.getAnalyticsModel_CargoSandboxes();

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
		 * The meta object literal for the '<em><b>From Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNIT_COST_MATRIX__FROM_PORTS = eINSTANCE.getUnitCostMatrix_FromPorts();

		/**
		 * The meta object literal for the '<em><b>To Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNIT_COST_MATRIX__TO_PORTS = eINSTANCE.getUnitCostMatrix_ToPorts();

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
		 * The meta object literal for the '<em><b>Retain Heel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__RETAIN_HEEL = eINSTANCE.getUnitCostMatrix_RetainHeel();

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
		 * The meta object literal for the '<em><b>Cost Lines</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNIT_COST_MATRIX__COST_LINES = eINSTANCE.getUnitCostMatrix_CostLines();

		/**
		 * The meta object literal for the '<em><b>Allowed Routes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNIT_COST_MATRIX__ALLOWED_ROUTES = eINSTANCE.getUnitCostMatrix_AllowedRoutes();

		/**
		 * The meta object literal for the '<em><b>Revenue Share</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__REVENUE_SHARE = eINSTANCE.getUnitCostMatrix_RevenueShare();

		/**
		 * The meta object literal for the '<em><b>Laden Time Allowance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__LADEN_TIME_ALLOWANCE = eINSTANCE.getUnitCostMatrix_LadenTimeAllowance();

		/**
		 * The meta object literal for the '<em><b>Ballast Time Allowance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_MATRIX__BALLAST_TIME_ALLOWANCE = eINSTANCE.getUnitCostMatrix_BallastTimeAllowance();

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
		 * The meta object literal for the '<em><b>Profit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_COST_LINE__PROFIT = eINSTANCE.getUnitCostLine_Profit();

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
		 * The meta object literal for the '<em><b>Route</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VOYAGE__ROUTE = eINSTANCE.getVoyage_Route();

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

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.JourneyImpl <em>Journey</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.JourneyImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getJourney()
		 * @generated
		 */
		EClass JOURNEY = eINSTANCE.getJourney();

		/**
		 * The meta object literal for the '<em><b>From</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JOURNEY__FROM = eINSTANCE.getJourney_From();

		/**
		 * The meta object literal for the '<em><b>To</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JOURNEY__TO = eINSTANCE.getJourney_To();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostPlanImpl <em>Shipping Cost Plan</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ShippingCostPlanImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getShippingCostPlan()
		 * @generated
		 */
		EClass SHIPPING_COST_PLAN = eINSTANCE.getShippingCostPlan();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHIPPING_COST_PLAN__VESSEL = eINSTANCE.getShippingCostPlan_Vessel();

		/**
		 * The meta object literal for the '<em><b>Notional Day Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHIPPING_COST_PLAN__NOTIONAL_DAY_RATE = eINSTANCE.getShippingCostPlan_NotionalDayRate();

		/**
		 * The meta object literal for the '<em><b>Base Fuel Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHIPPING_COST_PLAN__BASE_FUEL_PRICE = eINSTANCE.getShippingCostPlan_BaseFuelPrice();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHIPPING_COST_PLAN__ROWS = eINSTANCE.getShippingCostPlan_Rows();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ShippingCostRowImpl <em>Shipping Cost Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ShippingCostRowImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getShippingCostRow()
		 * @generated
		 */
		EClass SHIPPING_COST_ROW = eINSTANCE.getShippingCostRow();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHIPPING_COST_ROW__PORT = eINSTANCE.getShippingCostRow_Port();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHIPPING_COST_ROW__DATE = eINSTANCE.getShippingCostRow_Date();

		/**
		 * The meta object literal for the '<em><b>Cargo Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHIPPING_COST_ROW__CARGO_PRICE = eINSTANCE.getShippingCostRow_CargoPrice();

		/**
		 * The meta object literal for the '<em><b>Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHIPPING_COST_ROW__CV_VALUE = eINSTANCE.getShippingCostRow_CvValue();

		/**
		 * The meta object literal for the '<em><b>Destination Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHIPPING_COST_ROW__DESTINATION_TYPE = eINSTANCE.getShippingCostRow_DestinationType();

		/**
		 * The meta object literal for the '<em><b>Heel Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHIPPING_COST_ROW__HEEL_VOLUME = eINSTANCE.getShippingCostRow_HeelVolume();

		/**
		 * The meta object literal for the '<em><b>Include Port Costs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHIPPING_COST_ROW__INCLUDE_PORT_COSTS = eINSTANCE.getShippingCostRow_IncludePortCosts();

		
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.CargoSandboxImpl <em>Cargo Sandbox</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.CargoSandboxImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCargoSandbox()
		 * @generated
		 */
		EClass CARGO_SANDBOX = eINSTANCE.getCargoSandbox();

		/**
		 * The meta object literal for the '<em><b>Cargoes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_SANDBOX__CARGOES = eINSTANCE.getCargoSandbox_Cargoes();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ProvisionalCargoImpl <em>Provisional Cargo</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ProvisionalCargoImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getProvisionalCargo()
		 * @generated
		 */
		EClass PROVISIONAL_CARGO = eINSTANCE.getProvisionalCargo();

		/**
		 * The meta object literal for the '<em><b>Buy</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROVISIONAL_CARGO__BUY = eINSTANCE.getProvisionalCargo_Buy();

		/**
		 * The meta object literal for the '<em><b>Sell</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROVISIONAL_CARGO__SELL = eINSTANCE.getProvisionalCargo_Sell();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROVISIONAL_CARGO__VESSEL = eINSTANCE.getProvisionalCargo_Vessel();

		/**
		 * The meta object literal for the '<em><b>Portfolio Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROVISIONAL_CARGO__PORTFOLIO_MODEL = eINSTANCE.getProvisionalCargo_PortfolioModel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl <em>Buy Opportunity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyOpportunity()
		 * @generated
		 */
		EClass BUY_OPPORTUNITY = eINSTANCE.getBuyOpportunity();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUY_OPPORTUNITY__PORT = eINSTANCE.getBuyOpportunity_Port();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUY_OPPORTUNITY__CONTRACT = eINSTANCE.getBuyOpportunity_Contract();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__DATE = eINSTANCE.getBuyOpportunity_Date();

		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__PRICE_EXPRESSION = eINSTANCE.getBuyOpportunity_PriceExpression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SellOpportunityImpl <em>Sell Opportunity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SellOpportunityImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellOpportunity()
		 * @generated
		 */
		EClass SELL_OPPORTUNITY = eINSTANCE.getSellOpportunity();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SELL_OPPORTUNITY__PORT = eINSTANCE.getSellOpportunity_Port();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SELL_OPPORTUNITY__CONTRACT = eINSTANCE.getSellOpportunity_Contract();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__DATE = eINSTANCE.getSellOpportunity_Date();

		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__PRICE_EXPRESSION = eINSTANCE.getSellOpportunity_PriceExpression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.DestinationType <em>Destination Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.DestinationType
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getDestinationType()
		 * @generated
		 */
		EEnum DESTINATION_TYPE = eINSTANCE.getDestinationType();

	}

} //AnalyticsPackage
