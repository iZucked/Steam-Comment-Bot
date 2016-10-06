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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.BuyOption <em>Buy Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.BuyOption
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyOption()
	 * @generated
	 */
	int BUY_OPTION = 12;

	/**
	 * The number of structural features of the '<em>Buy Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPTION_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.SellOption <em>Sell Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.SellOption
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellOption()
	 * @generated
	 */
	int SELL_OPTION = 13;

	/**
	 * The number of structural features of the '<em>Sell Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPTION_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl <em>Buy Opportunity</em>}' class.
	 * <!-- begin-user-doc -->
s	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyOpportunity()
	 * @generated
	 */
	int BUY_OPPORTUNITY = 14;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Des Purchase</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__DES_PURCHASE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__PORT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__CONTRACT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__DATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__PRICE_EXPRESSION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__ENTITY = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Buy Opportunity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SellOpportunityImpl <em>Sell Opportunity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SellOpportunityImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellOpportunity()
	 * @generated
	 */
	int SELL_OPPORTUNITY = 15;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Fob Sale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__FOB_SALE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__PORT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__CONTRACT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__DATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__PRICE_EXPRESSION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__ENTITY = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Sell Opportunity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BuyMarketImpl <em>Buy Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BuyMarketImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyMarket()
	 * @generated
	 */
	int BUY_MARKET = 16;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_MARKET__MARKET = BUY_OPTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Buy Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_MARKET_FEATURE_COUNT = BUY_OPTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SellMarketImpl <em>Sell Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SellMarketImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellMarket()
	 * @generated
	 */
	int SELL_MARKET = 17;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_MARKET__MARKET = SELL_OPTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Sell Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_MARKET_FEATURE_COUNT = SELL_OPTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BuyReferenceImpl <em>Buy Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BuyReferenceImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyReference()
	 * @generated
	 */
	int BUY_REFERENCE = 18;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_REFERENCE__SLOT = BUY_OPTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Buy Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_REFERENCE_FEATURE_COUNT = BUY_OPTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SellReferenceImpl <em>Sell Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SellReferenceImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellReference()
	 * @generated
	 */
	int SELL_REFERENCE = 19;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_REFERENCE__SLOT = SELL_OPTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Sell Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_REFERENCE_FEATURE_COUNT = SELL_OPTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowImpl <em>Base Case Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BaseCaseRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBaseCaseRow()
	 * @generated
	 */
	int BASE_CASE_ROW = 20;

	/**
	 * The feature id for the '<em><b>Buy Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW__BUY_OPTION = 0;

	/**
	 * The feature id for the '<em><b>Sell Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW__SELL_OPTION = 1;

	/**
	 * The feature id for the '<em><b>Shipping</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW__SHIPPING = 2;

	/**
	 * The number of structural features of the '<em>Base Case Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl <em>Partial Case Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPartialCaseRow()
	 * @generated
	 */
	int PARTIAL_CASE_ROW = 21;

	/**
	 * The feature id for the '<em><b>Buy Options</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW__BUY_OPTIONS = 0;

	/**
	 * The feature id for the '<em><b>Sell Options</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW__SELL_OPTIONS = 1;

	/**
	 * The feature id for the '<em><b>Shipping</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW__SHIPPING = 2;

	/**
	 * The number of structural features of the '<em>Partial Case Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ShippingOptionImpl <em>Shipping Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ShippingOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getShippingOption()
	 * @generated
	 */
	int SHIPPING_OPTION = 22;

	/**
	 * The number of structural features of the '<em>Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.FleetShippingOptionImpl <em>Fleet Shipping Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.FleetShippingOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getFleetShippingOption()
	 * @generated
	 */
	int FLEET_SHIPPING_OPTION = 23;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_SHIPPING_OPTION__VESSEL = SHIPPING_OPTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_SHIPPING_OPTION__HIRE_COST = SHIPPING_OPTION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_SHIPPING_OPTION__ENTITY = SHIPPING_OPTION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Fleet Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_SHIPPING_OPTION_FEATURE_COUNT = SHIPPING_OPTION_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.RoundTripShippingOptionImpl <em>Round Trip Shipping Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.RoundTripShippingOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getRoundTripShippingOption()
	 * @generated
	 */
	int ROUND_TRIP_SHIPPING_OPTION = 24;

	/**
	 * The feature id for the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUND_TRIP_SHIPPING_OPTION__VESSEL_CLASS = SHIPPING_OPTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUND_TRIP_SHIPPING_OPTION__HIRE_COST = SHIPPING_OPTION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Round Trip Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUND_TRIP_SHIPPING_OPTION_FEATURE_COUNT = SHIPPING_OPTION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.NominatedShippingOptionImpl <em>Nominated Shipping Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.NominatedShippingOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getNominatedShippingOption()
	 * @generated
	 */
	int NOMINATED_SHIPPING_OPTION = 25;

	/**
	 * The feature id for the '<em><b>Nominated Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATED_SHIPPING_OPTION__NOMINATED_VESSEL = SHIPPING_OPTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Nominated Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATED_SHIPPING_OPTION_FEATURE_COUNT = SHIPPING_OPTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.AnalysisResultRowImpl <em>Analysis Result Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalysisResultRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAnalysisResultRow()
	 * @generated
	 */
	int ANALYSIS_RESULT_ROW = 26;

	/**
	 * The feature id for the '<em><b>Buy Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_RESULT_ROW__BUY_OPTION = 0;

	/**
	 * The feature id for the '<em><b>Sell Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_RESULT_ROW__SELL_OPTION = 1;

	/**
	 * The feature id for the '<em><b>Result Detail</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_RESULT_ROW__RESULT_DETAIL = 2;

	/**
	 * The number of structural features of the '<em>Analysis Result Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_RESULT_ROW_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.OptionRule <em>Option Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.OptionRule
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptionRule()
	 * @generated
	 */
	int OPTION_RULE = 27;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_RULE__NAME = 0;

	/**
	 * The number of structural features of the '<em>Option Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_RULE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.AnalysisResultDetailImpl <em>Analysis Result Detail</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalysisResultDetailImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAnalysisResultDetail()
	 * @generated
	 */
	int ANALYSIS_RESULT_DETAIL = 28;

	/**
	 * The number of structural features of the '<em>Analysis Result Detail</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_RESULT_DETAIL_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ProfitAndLossResultImpl <em>Profit And Loss Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ProfitAndLossResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getProfitAndLossResult()
	 * @generated
	 */
	int PROFIT_AND_LOSS_RESULT = 29;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_AND_LOSS_RESULT__VALUE = ANALYSIS_RESULT_DETAIL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Profit And Loss Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_AND_LOSS_RESULT_FEATURE_COUNT = ANALYSIS_RESULT_DETAIL_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenResultImpl <em>Break Even Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BreakEvenResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBreakEvenResult()
	 * @generated
	 */
	int BREAK_EVEN_RESULT = 30;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_RESULT__PRICE = ANALYSIS_RESULT_DETAIL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Break Even Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_RESULT_FEATURE_COUNT = ANALYSIS_RESULT_DETAIL_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ModeOptionRuleImpl <em>Mode Option Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ModeOptionRuleImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getModeOptionRule()
	 * @generated
	 */
	int MODE_OPTION_RULE = 31;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODE_OPTION_RULE__NAME = OPTION_RULE__NAME;

	/**
	 * The number of structural features of the '<em>Mode Option Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODE_OPTION_RULE_FEATURE_COUNT = OPTION_RULE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl <em>Option Analysis Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptionAnalysisModel()
	 * @generated
	 */
	int OPTION_ANALYSIS_MODEL = 32;

	/**
	 * The feature id for the '<em><b>Buys</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__BUYS = 0;

	/**
	 * The feature id for the '<em><b>Sells</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__SELLS = 1;

	/**
	 * The feature id for the '<em><b>Base Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__BASE_CASE = 2;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__RULES = 3;

	/**
	 * The feature id for the '<em><b>Partial Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__PARTIAL_CASE = 4;

	/**
	 * The feature id for the '<em><b>Result Sets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__RESULT_SETS = 5;

	/**
	 * The feature id for the '<em><b>Use Target PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__USE_TARGET_PNL = 6;

	/**
	 * The number of structural features of the '<em>Option Analysis Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ResultSetImpl <em>Result Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ResultSetImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getResultSet()
	 * @generated
	 */
	int RESULT_SET = 33;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET__ROWS = 0;

	/**
	 * The feature id for the '<em><b>Profit And Loss</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET__PROFIT_AND_LOSS = 1;

	/**
	 * The number of structural features of the '<em>Result Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl <em>Base Case</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBaseCase()
	 * @generated
	 */
	int BASE_CASE = 34;

	/**
	 * The feature id for the '<em><b>Base Case</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE__BASE_CASE = 0;

	/**
	 * The feature id for the '<em><b>Profit And Loss</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE__PROFIT_AND_LOSS = 1;

	/**
	 * The number of structural features of the '<em>Base Case</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseImpl <em>Partial Case</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.PartialCaseImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPartialCase()
	 * @generated
	 */
	int PARTIAL_CASE = 35;

	/**
	 * The feature id for the '<em><b>Partial Case</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE__PARTIAL_CASE = 0;

	/**
	 * The number of structural features of the '<em>Partial Case</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.DestinationType <em>Destination Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.DestinationType
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getDestinationType()
	 * @generated
	 */
	int DESTINATION_TYPE = 36;


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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BuyOption <em>Buy Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Buy Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOption
	 * @generated
	 */
	EClass getBuyOption();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SellOption <em>Sell Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sell Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOption
	 * @generated
	 */
	EClass getSellOption();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#isDesPurchase <em>Des Purchase</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Des Purchase</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#isDesPurchase()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_DesPurchase();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getEntity()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EReference getBuyOpportunity_Entity();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#isFobSale <em>Fob Sale</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fob Sale</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#isFobSale()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_FobSale();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getEntity()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EReference getSellOpportunity_Entity();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BuyMarket <em>Buy Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Buy Market</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyMarket
	 * @generated
	 */
	EClass getBuyMarket();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BuyMarket#getMarket <em>Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyMarket#getMarket()
	 * @see #getBuyMarket()
	 * @generated
	 */
	EReference getBuyMarket_Market();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SellMarket <em>Sell Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sell Market</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellMarket
	 * @generated
	 */
	EClass getSellMarket();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.SellMarket#getMarket <em>Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellMarket#getMarket()
	 * @see #getSellMarket()
	 * @generated
	 */
	EReference getSellMarket_Market();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BuyReference <em>Buy Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Buy Reference</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyReference
	 * @generated
	 */
	EClass getBuyReference();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BuyReference#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyReference#getSlot()
	 * @see #getBuyReference()
	 * @generated
	 */
	EReference getBuyReference_Slot();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SellReference <em>Sell Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sell Reference</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellReference
	 * @generated
	 */
	EClass getSellReference();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.SellReference#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellReference#getSlot()
	 * @see #getSellReference()
	 * @generated
	 */
	EReference getSellReference_Slot();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow <em>Base Case Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Case Row</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRow
	 * @generated
	 */
	EClass getBaseCaseRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getBuyOption <em>Buy Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Buy Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRow#getBuyOption()
	 * @see #getBaseCaseRow()
	 * @generated
	 */
	EReference getBaseCaseRow_BuyOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getSellOption <em>Sell Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sell Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRow#getSellOption()
	 * @see #getBaseCaseRow()
	 * @generated
	 */
	EReference getBaseCaseRow_SellOption();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getShipping <em>Shipping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Shipping</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRow#getShipping()
	 * @see #getBaseCaseRow()
	 * @generated
	 */
	EReference getBaseCaseRow_Shipping();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.PartialCaseRow <em>Partial Case Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Partial Case Row</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRow
	 * @generated
	 */
	EClass getPartialCaseRow();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getBuyOptions <em>Buy Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Buy Options</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRow#getBuyOptions()
	 * @see #getPartialCaseRow()
	 * @generated
	 */
	EReference getPartialCaseRow_BuyOptions();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getSellOptions <em>Sell Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Sell Options</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRow#getSellOptions()
	 * @see #getPartialCaseRow()
	 * @generated
	 */
	EReference getPartialCaseRow_SellOptions();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getShipping <em>Shipping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Shipping</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRow#getShipping()
	 * @see #getPartialCaseRow()
	 * @generated
	 */
	EReference getPartialCaseRow_Shipping();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ShippingOption <em>Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Shipping Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ShippingOption
	 * @generated
	 */
	EClass getShippingOption();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.FleetShippingOption <em>Fleet Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fleet Shipping Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FleetShippingOption
	 * @generated
	 */
	EClass getFleetShippingOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.FleetShippingOption#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FleetShippingOption#getVessel()
	 * @see #getFleetShippingOption()
	 * @generated
	 */
	EReference getFleetShippingOption_Vessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.FleetShippingOption#getHireCost <em>Hire Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hire Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FleetShippingOption#getHireCost()
	 * @see #getFleetShippingOption()
	 * @generated
	 */
	EAttribute getFleetShippingOption_HireCost();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.FleetShippingOption#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FleetShippingOption#getEntity()
	 * @see #getFleetShippingOption()
	 * @generated
	 */
	EReference getFleetShippingOption_Entity();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption <em>Round Trip Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Round Trip Shipping Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.RoundTripShippingOption
	 * @generated
	 */
	EClass getRoundTripShippingOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getVesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Class</em>'.
	 * @see com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getVesselClass()
	 * @see #getRoundTripShippingOption()
	 * @generated
	 */
	EReference getRoundTripShippingOption_VesselClass();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getHireCost <em>Hire Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hire Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getHireCost()
	 * @see #getRoundTripShippingOption()
	 * @generated
	 */
	EAttribute getRoundTripShippingOption_HireCost();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.NominatedShippingOption <em>Nominated Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Nominated Shipping Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.NominatedShippingOption
	 * @generated
	 */
	EClass getNominatedShippingOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.NominatedShippingOption#getNominatedVessel <em>Nominated Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Nominated Vessel</em>'.
	 * @see com.mmxlabs.models.lng.analytics.NominatedShippingOption#getNominatedVessel()
	 * @see #getNominatedShippingOption()
	 * @generated
	 */
	EReference getNominatedShippingOption_NominatedVessel();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow <em>Analysis Result Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Analysis Result Row</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalysisResultRow
	 * @generated
	 */
	EClass getAnalysisResultRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getBuyOption <em>Buy Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Buy Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalysisResultRow#getBuyOption()
	 * @see #getAnalysisResultRow()
	 * @generated
	 */
	EReference getAnalysisResultRow_BuyOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getSellOption <em>Sell Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sell Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalysisResultRow#getSellOption()
	 * @see #getAnalysisResultRow()
	 * @generated
	 */
	EReference getAnalysisResultRow_SellOption();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getResultDetail <em>Result Detail</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Result Detail</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalysisResultRow#getResultDetail()
	 * @see #getAnalysisResultRow()
	 * @generated
	 */
	EReference getAnalysisResultRow_ResultDetail();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.OptionRule <em>Option Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Option Rule</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionRule
	 * @generated
	 */
	EClass getOptionRule();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.OptionRule#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionRule#getName()
	 * @see #getOptionRule()
	 * @generated
	 */
	EAttribute getOptionRule_Name();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.AnalysisResultDetail <em>Analysis Result Detail</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Analysis Result Detail</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalysisResultDetail
	 * @generated
	 */
	EClass getAnalysisResultDetail();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ProfitAndLossResult <em>Profit And Loss Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Profit And Loss Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ProfitAndLossResult
	 * @generated
	 */
	EClass getProfitAndLossResult();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ProfitAndLossResult#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ProfitAndLossResult#getValue()
	 * @see #getProfitAndLossResult()
	 * @generated
	 */
	EAttribute getProfitAndLossResult_Value();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BreakEvenResult <em>Break Even Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Break Even Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenResult
	 * @generated
	 */
	EClass getBreakEvenResult();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BreakEvenResult#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenResult#getPrice()
	 * @see #getBreakEvenResult()
	 * @generated
	 */
	EAttribute getBreakEvenResult_Price();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ModeOptionRule <em>Mode Option Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Mode Option Rule</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ModeOptionRule
	 * @generated
	 */
	EClass getModeOptionRule();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel <em>Option Analysis Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Option Analysis Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel
	 * @generated
	 */
	EClass getOptionAnalysisModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBaseCase <em>Base Case</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Base Case</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBaseCase()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EReference getOptionAnalysisModel_BaseCase();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBuys <em>Buys</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Buys</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBuys()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EReference getOptionAnalysisModel_Buys();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getSells <em>Sells</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sells</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getSells()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EReference getOptionAnalysisModel_Sells();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BaseCase <em>Base Case</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Case</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCase
	 * @generated
	 */
	EClass getBaseCase();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.BaseCase#getBaseCase <em>Base Case</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Base Case</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCase#getBaseCase()
	 * @see #getBaseCase()
	 * @generated
	 */
	EReference getBaseCase_BaseCase();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BaseCase#getProfitAndLoss <em>Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Profit And Loss</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCase#getProfitAndLoss()
	 * @see #getBaseCase()
	 * @generated
	 */
	EAttribute getBaseCase_ProfitAndLoss();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.PartialCase <em>Partial Case</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Partial Case</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCase
	 * @generated
	 */
	EClass getPartialCase();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.PartialCase#getPartialCase <em>Partial Case</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Partial Case</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCase#getPartialCase()
	 * @see #getPartialCase()
	 * @generated
	 */
	EReference getPartialCase_PartialCase();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getRules <em>Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rules</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getRules()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EReference getOptionAnalysisModel_Rules();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getPartialCase <em>Partial Case</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Partial Case</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getPartialCase()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EReference getOptionAnalysisModel_PartialCase();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getResultSets <em>Result Sets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Result Sets</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getResultSets()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EReference getOptionAnalysisModel_ResultSets();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#isUseTargetPNL <em>Use Target PNL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Target PNL</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#isUseTargetPNL()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EAttribute getOptionAnalysisModel_UseTargetPNL();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ResultSet <em>Result Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Result Set</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultSet
	 * @generated
	 */
	EClass getResultSet();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.ResultSet#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultSet#getRows()
	 * @see #getResultSet()
	 * @generated
	 */
	EReference getResultSet_Rows();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ResultSet#getProfitAndLoss <em>Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Profit And Loss</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultSet#getProfitAndLoss()
	 * @see #getResultSet()
	 * @generated
	 */
	EAttribute getResultSet_ProfitAndLoss();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.BuyOption <em>Buy Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.BuyOption
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyOption()
		 * @generated
		 */
		EClass BUY_OPTION = eINSTANCE.getBuyOption();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.SellOption <em>Sell Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.SellOption
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellOption()
		 * @generated
		 */
		EClass SELL_OPTION = eINSTANCE.getSellOption();

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
		 * The meta object literal for the '<em><b>Des Purchase</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__DES_PURCHASE = eINSTANCE.getBuyOpportunity_DesPurchase();

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
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUY_OPPORTUNITY__ENTITY = eINSTANCE.getBuyOpportunity_Entity();

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
		 * The meta object literal for the '<em><b>Fob Sale</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__FOB_SALE = eINSTANCE.getSellOpportunity_FobSale();

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
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SELL_OPPORTUNITY__ENTITY = eINSTANCE.getSellOpportunity_Entity();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.BuyMarketImpl <em>Buy Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.BuyMarketImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyMarket()
		 * @generated
		 */
		EClass BUY_MARKET = eINSTANCE.getBuyMarket();

		/**
		 * The meta object literal for the '<em><b>Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUY_MARKET__MARKET = eINSTANCE.getBuyMarket_Market();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SellMarketImpl <em>Sell Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SellMarketImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellMarket()
		 * @generated
		 */
		EClass SELL_MARKET = eINSTANCE.getSellMarket();

		/**
		 * The meta object literal for the '<em><b>Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SELL_MARKET__MARKET = eINSTANCE.getSellMarket_Market();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.BuyReferenceImpl <em>Buy Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.BuyReferenceImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyReference()
		 * @generated
		 */
		EClass BUY_REFERENCE = eINSTANCE.getBuyReference();

		/**
		 * The meta object literal for the '<em><b>Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUY_REFERENCE__SLOT = eINSTANCE.getBuyReference_Slot();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SellReferenceImpl <em>Sell Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SellReferenceImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellReference()
		 * @generated
		 */
		EClass SELL_REFERENCE = eINSTANCE.getSellReference();

		/**
		 * The meta object literal for the '<em><b>Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SELL_REFERENCE__SLOT = eINSTANCE.getSellReference_Slot();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowImpl <em>Base Case Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.BaseCaseRowImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBaseCaseRow()
		 * @generated
		 */
		EClass BASE_CASE_ROW = eINSTANCE.getBaseCaseRow();

		/**
		 * The meta object literal for the '<em><b>Buy Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_CASE_ROW__BUY_OPTION = eINSTANCE.getBaseCaseRow_BuyOption();

		/**
		 * The meta object literal for the '<em><b>Sell Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_CASE_ROW__SELL_OPTION = eINSTANCE.getBaseCaseRow_SellOption();

		/**
		 * The meta object literal for the '<em><b>Shipping</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_CASE_ROW__SHIPPING = eINSTANCE.getBaseCaseRow_Shipping();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl <em>Partial Case Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPartialCaseRow()
		 * @generated
		 */
		EClass PARTIAL_CASE_ROW = eINSTANCE.getPartialCaseRow();

		/**
		 * The meta object literal for the '<em><b>Buy Options</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARTIAL_CASE_ROW__BUY_OPTIONS = eINSTANCE.getPartialCaseRow_BuyOptions();

		/**
		 * The meta object literal for the '<em><b>Sell Options</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARTIAL_CASE_ROW__SELL_OPTIONS = eINSTANCE.getPartialCaseRow_SellOptions();

		/**
		 * The meta object literal for the '<em><b>Shipping</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARTIAL_CASE_ROW__SHIPPING = eINSTANCE.getPartialCaseRow_Shipping();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ShippingOptionImpl <em>Shipping Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ShippingOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getShippingOption()
		 * @generated
		 */
		EClass SHIPPING_OPTION = eINSTANCE.getShippingOption();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.FleetShippingOptionImpl <em>Fleet Shipping Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.FleetShippingOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getFleetShippingOption()
		 * @generated
		 */
		EClass FLEET_SHIPPING_OPTION = eINSTANCE.getFleetShippingOption();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_SHIPPING_OPTION__VESSEL = eINSTANCE.getFleetShippingOption_Vessel();

		/**
		 * The meta object literal for the '<em><b>Hire Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLEET_SHIPPING_OPTION__HIRE_COST = eINSTANCE.getFleetShippingOption_HireCost();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_SHIPPING_OPTION__ENTITY = eINSTANCE.getFleetShippingOption_Entity();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.RoundTripShippingOptionImpl <em>Round Trip Shipping Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.RoundTripShippingOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getRoundTripShippingOption()
		 * @generated
		 */
		EClass ROUND_TRIP_SHIPPING_OPTION = eINSTANCE.getRoundTripShippingOption();

		/**
		 * The meta object literal for the '<em><b>Vessel Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUND_TRIP_SHIPPING_OPTION__VESSEL_CLASS = eINSTANCE.getRoundTripShippingOption_VesselClass();

		/**
		 * The meta object literal for the '<em><b>Hire Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUND_TRIP_SHIPPING_OPTION__HIRE_COST = eINSTANCE.getRoundTripShippingOption_HireCost();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.NominatedShippingOptionImpl <em>Nominated Shipping Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.NominatedShippingOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getNominatedShippingOption()
		 * @generated
		 */
		EClass NOMINATED_SHIPPING_OPTION = eINSTANCE.getNominatedShippingOption();

		/**
		 * The meta object literal for the '<em><b>Nominated Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOMINATED_SHIPPING_OPTION__NOMINATED_VESSEL = eINSTANCE.getNominatedShippingOption_NominatedVessel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.AnalysisResultRowImpl <em>Analysis Result Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalysisResultRowImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAnalysisResultRow()
		 * @generated
		 */
		EClass ANALYSIS_RESULT_ROW = eINSTANCE.getAnalysisResultRow();

		/**
		 * The meta object literal for the '<em><b>Buy Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYSIS_RESULT_ROW__BUY_OPTION = eINSTANCE.getAnalysisResultRow_BuyOption();

		/**
		 * The meta object literal for the '<em><b>Sell Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYSIS_RESULT_ROW__SELL_OPTION = eINSTANCE.getAnalysisResultRow_SellOption();

		/**
		 * The meta object literal for the '<em><b>Result Detail</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYSIS_RESULT_ROW__RESULT_DETAIL = eINSTANCE.getAnalysisResultRow_ResultDetail();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.OptionRule <em>Option Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.OptionRule
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptionRule()
		 * @generated
		 */
		EClass OPTION_RULE = eINSTANCE.getOptionRule();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTION_RULE__NAME = eINSTANCE.getOptionRule_Name();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.AnalysisResultDetailImpl <em>Analysis Result Detail</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalysisResultDetailImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAnalysisResultDetail()
		 * @generated
		 */
		EClass ANALYSIS_RESULT_DETAIL = eINSTANCE.getAnalysisResultDetail();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ProfitAndLossResultImpl <em>Profit And Loss Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ProfitAndLossResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getProfitAndLossResult()
		 * @generated
		 */
		EClass PROFIT_AND_LOSS_RESULT = eINSTANCE.getProfitAndLossResult();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFIT_AND_LOSS_RESULT__VALUE = eINSTANCE.getProfitAndLossResult_Value();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenResultImpl <em>Break Even Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.BreakEvenResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBreakEvenResult()
		 * @generated
		 */
		EClass BREAK_EVEN_RESULT = eINSTANCE.getBreakEvenResult();

		/**
		 * The meta object literal for the '<em><b>Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BREAK_EVEN_RESULT__PRICE = eINSTANCE.getBreakEvenResult_Price();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ModeOptionRuleImpl <em>Mode Option Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ModeOptionRuleImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getModeOptionRule()
		 * @generated
		 */
		EClass MODE_OPTION_RULE = eINSTANCE.getModeOptionRule();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl <em>Option Analysis Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptionAnalysisModel()
		 * @generated
		 */
		EClass OPTION_ANALYSIS_MODEL = eINSTANCE.getOptionAnalysisModel();

		/**
		 * The meta object literal for the '<em><b>Base Case</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTION_ANALYSIS_MODEL__BASE_CASE = eINSTANCE.getOptionAnalysisModel_BaseCase();

		/**
		 * The meta object literal for the '<em><b>Buys</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTION_ANALYSIS_MODEL__BUYS = eINSTANCE.getOptionAnalysisModel_Buys();

		/**
		 * The meta object literal for the '<em><b>Sells</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTION_ANALYSIS_MODEL__SELLS = eINSTANCE.getOptionAnalysisModel_Sells();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl <em>Base Case</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBaseCase()
		 * @generated
		 */
		EClass BASE_CASE = eINSTANCE.getBaseCase();

		/**
		 * The meta object literal for the '<em><b>Base Case</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_CASE__BASE_CASE = eINSTANCE.getBaseCase_BaseCase();

		/**
		 * The meta object literal for the '<em><b>Profit And Loss</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_CASE__PROFIT_AND_LOSS = eINSTANCE.getBaseCase_ProfitAndLoss();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseImpl <em>Partial Case</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.PartialCaseImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPartialCase()
		 * @generated
		 */
		EClass PARTIAL_CASE = eINSTANCE.getPartialCase();

		/**
		 * The meta object literal for the '<em><b>Partial Case</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARTIAL_CASE__PARTIAL_CASE = eINSTANCE.getPartialCase_PartialCase();

		/**
		 * The meta object literal for the '<em><b>Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTION_ANALYSIS_MODEL__RULES = eINSTANCE.getOptionAnalysisModel_Rules();

		/**
		 * The meta object literal for the '<em><b>Partial Case</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTION_ANALYSIS_MODEL__PARTIAL_CASE = eINSTANCE.getOptionAnalysisModel_PartialCase();

		/**
		 * The meta object literal for the '<em><b>Result Sets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTION_ANALYSIS_MODEL__RESULT_SETS = eINSTANCE.getOptionAnalysisModel_ResultSets();

		/**
		 * The meta object literal for the '<em><b>Use Target PNL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTION_ANALYSIS_MODEL__USE_TARGET_PNL = eINSTANCE.getOptionAnalysisModel_UseTargetPNL();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ResultSetImpl <em>Result Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ResultSetImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getResultSet()
		 * @generated
		 */
		EClass RESULT_SET = eINSTANCE.getResultSet();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT_SET__ROWS = eINSTANCE.getResultSet_Rows();

		/**
		 * The meta object literal for the '<em><b>Profit And Loss</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_SET__PROFIT_AND_LOSS = eINSTANCE.getResultSet_ProfitAndLoss();

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
