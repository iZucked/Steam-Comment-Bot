/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.lng.types.TypesPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see com.mmxlabs.models.lng.pricing.PricingFactory
 * @model kind="package"
 * @generated
 */
public interface PricingPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "pricing";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/pricing/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.pricing";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PricingPackage eINSTANCE = com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingModelImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPricingModel()
	 * @generated
	 */
	int PRICING_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Commodity Indices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__COMMODITY_INDICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Charter Indices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__CHARTER_INDICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Fleet Cost</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__FLEET_COST = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Route Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__ROUTE_COSTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Port Costs</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__PORT_COSTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.IndexImpl <em>Index</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.IndexImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getIndex()
	 * @generated
	 */
	int INDEX = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX__EXTENSIONS = TypesPackage.AINDEX__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX__PROXIES = TypesPackage.AINDEX__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX__UUID = TypesPackage.AINDEX__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX__NAME = TypesPackage.AINDEX__NAME;

	/**
	 * The number of structural features of the '<em>Index</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_FEATURE_COUNT = TypesPackage.AINDEX_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.DataIndexImpl <em>Data Index</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.DataIndexImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDataIndex()
	 * @generated
	 */
	int DATA_INDEX = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INDEX__EXTENSIONS = INDEX__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INDEX__PROXIES = INDEX__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INDEX__UUID = INDEX__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INDEX__NAME = INDEX__NAME;

	/**
	 * The feature id for the '<em><b>Points</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INDEX__POINTS = INDEX_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Data Index</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INDEX_FEATURE_COUNT = INDEX_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.DerivedIndexImpl <em>Derived Index</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.DerivedIndexImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDerivedIndex()
	 * @generated
	 */
	int DERIVED_INDEX = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DERIVED_INDEX__EXTENSIONS = INDEX__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DERIVED_INDEX__PROXIES = INDEX__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DERIVED_INDEX__UUID = INDEX__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DERIVED_INDEX__NAME = INDEX__NAME;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DERIVED_INDEX__EXPRESSION = INDEX_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Derived Index</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DERIVED_INDEX_FEATURE_COUNT = INDEX_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.IndexPointImpl <em>Index Point</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.IndexPointImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getIndexPoint()
	 * @generated
	 */
	int INDEX_POINT = 3;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_POINT__DATE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_POINT__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Index Point</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_POINT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.FleetCostModelImpl <em>Fleet Cost Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.FleetCostModelImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getFleetCostModel()
	 * @generated
	 */
	int FLEET_COST_MODEL = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_COST_MODEL__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_COST_MODEL__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Charter Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_COST_MODEL__CHARTER_COSTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Base Fuel Prices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_COST_MODEL__BASE_FUEL_PRICES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Fleet Cost Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_COST_MODEL_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.RouteCostImpl <em>Route Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.RouteCostImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getRouteCost()
	 * @generated
	 */
	int ROUTE_COST = 6;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Routes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST__ROUTES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Route Cost</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CharterCostModelImpl <em>Charter Cost Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CharterCostModelImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCharterCostModel()
	 * @generated
	 */
	int CHARTER_COST_MODEL = 7;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__VESSELS = 0;

	/**
	 * The feature id for the '<em><b>Charter In Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__CHARTER_IN_PRICE = 1;

	/**
	 * The feature id for the '<em><b>Charter Out Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__CHARTER_OUT_PRICE = 2;

	/**
	 * The feature id for the '<em><b>Spot Charter Count</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__SPOT_CHARTER_COUNT = 3;

	/**
	 * The number of structural features of the '<em>Charter Cost Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl <em>Base Fuel Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getBaseFuelCost()
	 * @generated
	 */
	int BASE_FUEL_COST = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST__FUELS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST__PRICE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Base Fuel Cost</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl <em>Spot Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSpotMarket()
	 * @generated
	 */
	int SPOT_MARKET = 9;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__EXTENSIONS = TypesPackage.ASPOT_MARKET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__PROXIES = TypesPackage.ASPOT_MARKET__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__UUID = TypesPackage.ASPOT_MARKET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__NAME = TypesPackage.ASPOT_MARKET__NAME;

	/**
	 * The feature id for the '<em><b>Availability</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__AVAILABILITY = TypesPackage.ASPOT_MARKET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__PORTS = TypesPackage.ASPOT_MARKET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__MIN_QUANTITY = TypesPackage.ASPOT_MARKET_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__MAX_QUANTITY = TypesPackage.ASPOT_MARKET_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Spot Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_FEATURE_COUNT = TypesPackage.ASPOT_MARKET_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl <em>Port Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortCostImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortCost()
	 * @generated
	 */
	int PORT_COST = 10;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST__PORTS = 0;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST__VESSELS = 1;

	/**
	 * The feature id for the '<em><b>Laden Cost</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST__LADEN_COST = 2;

	/**
	 * The feature id for the '<em><b>Ballast Cost</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST__BALLAST_COST = 3;

	/**
	 * The number of structural features of the '<em>Port Cost</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.SpotType <em>Spot Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.SpotType
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSpotType()
	 * @generated
	 */
	int SPOT_TYPE = 11;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.PricingModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel
	 * @generated
	 */
	EClass getPricingModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getCommodityIndices <em>Commodity Indices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Commodity Indices</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getCommodityIndices()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_CommodityIndices();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getCharterIndices <em>Charter Indices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Charter Indices</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getCharterIndices()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_CharterIndices();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.pricing.PricingModel#getFleetCost <em>Fleet Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fleet Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getFleetCost()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_FleetCost();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getRouteCosts <em>Route Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Route Costs</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getRouteCosts()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_RouteCosts();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getPortCosts <em>Port Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Port Costs</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getPortCosts()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_PortCosts();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.DataIndex <em>Data Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DataIndex
	 * @generated
	 */
	EClass getDataIndex();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.DataIndex#getPoints <em>Points</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Points</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DataIndex#getPoints()
	 * @see #getDataIndex()
	 * @generated
	 */
	EReference getDataIndex_Points();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.DerivedIndex <em>Derived Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Derived Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DerivedIndex
	 * @generated
	 */
	EClass getDerivedIndex();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.DerivedIndex#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DerivedIndex#getExpression()
	 * @see #getDerivedIndex()
	 * @generated
	 */
	EAttribute getDerivedIndex_Expression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.IndexPoint <em>Index Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Index Point</em>'.
	 * @see com.mmxlabs.models.lng.pricing.IndexPoint
	 * @generated
	 */
	EClass getIndexPoint();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.IndexPoint#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.pricing.IndexPoint#getDate()
	 * @see #getIndexPoint()
	 * @generated
	 */
	EAttribute getIndexPoint_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.IndexPoint#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see com.mmxlabs.models.lng.pricing.IndexPoint#getValue()
	 * @see #getIndexPoint()
	 * @generated
	 */
	EAttribute getIndexPoint_Value();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.Index <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.Index
	 * @generated
	 */
	EClass getIndex();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.FleetCostModel <em>Fleet Cost Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fleet Cost Model</em>'.
	 * @see com.mmxlabs.models.lng.pricing.FleetCostModel
	 * @generated
	 */
	EClass getFleetCostModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.FleetCostModel#getCharterCosts <em>Charter Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Charter Costs</em>'.
	 * @see com.mmxlabs.models.lng.pricing.FleetCostModel#getCharterCosts()
	 * @see #getFleetCostModel()
	 * @generated
	 */
	EReference getFleetCostModel_CharterCosts();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.FleetCostModel#getBaseFuelPrices <em>Base Fuel Prices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Base Fuel Prices</em>'.
	 * @see com.mmxlabs.models.lng.pricing.FleetCostModel#getBaseFuelPrices()
	 * @see #getFleetCostModel()
	 * @generated
	 */
	EReference getFleetCostModel_BaseFuelPrices();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.RouteCost <em>Route Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Route Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.RouteCost
	 * @generated
	 */
	EClass getRouteCost();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.RouteCost#getRoutes <em>Routes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Routes</em>'.
	 * @see com.mmxlabs.models.lng.pricing.RouteCost#getRoutes()
	 * @see #getRouteCost()
	 * @generated
	 */
	EReference getRouteCost_Routes();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.CharterCostModel <em>Charter Cost Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Charter Cost Model</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CharterCostModel
	 * @generated
	 */
	EClass getCharterCostModel();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getVessels <em>Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessels</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CharterCostModel#getVessels()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EReference getCharterCostModel_Vessels();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getCharterInPrice <em>Charter In Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Charter In Price</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CharterCostModel#getCharterInPrice()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EReference getCharterCostModel_CharterInPrice();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getCharterOutPrice <em>Charter Out Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Charter Out Price</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CharterCostModel#getCharterOutPrice()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EReference getCharterCostModel_CharterOutPrice();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getSpotCharterCount <em>Spot Charter Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Spot Charter Count</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CharterCostModel#getSpotCharterCount()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EReference getCharterCostModel_SpotCharterCount();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.BaseFuelCost <em>Base Fuel Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Fuel Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.BaseFuelCost
	 * @generated
	 */
	EClass getBaseFuelCost();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getFuels <em>Fuels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Fuels</em>'.
	 * @see com.mmxlabs.models.lng.pricing.BaseFuelCost#getFuels()
	 * @see #getBaseFuelCost()
	 * @generated
	 */
	EReference getBaseFuelCost_Fuels();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Price</em>'.
	 * @see com.mmxlabs.models.lng.pricing.BaseFuelCost#getPrice()
	 * @see #getBaseFuelCost()
	 * @generated
	 */
	EReference getBaseFuelCost_Price();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.SpotMarket <em>Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Market</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarket
	 * @generated
	 */
	EClass getSpotMarket();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.SpotMarket#getAvailability <em>Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Availability</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarket#getAvailability()
	 * @see #getSpotMarket()
	 * @generated
	 */
	EReference getSpotMarket_Availability();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.SpotMarket#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ports</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarket#getPorts()
	 * @see #getSpotMarket()
	 * @generated
	 */
	EReference getSpotMarket_Ports();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SpotMarket#getMinQuantity <em>Min Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Quantity</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarket#getMinQuantity()
	 * @see #getSpotMarket()
	 * @generated
	 */
	EAttribute getSpotMarket_MinQuantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SpotMarket#getMaxQuantity <em>Max Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Quantity</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarket#getMaxQuantity()
	 * @see #getSpotMarket()
	 * @generated
	 */
	EAttribute getSpotMarket_MaxQuantity();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.PortCost <em>Port Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortCost
	 * @generated
	 */
	EClass getPortCost();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.PortCost#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ports</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortCost#getPorts()
	 * @see #getPortCost()
	 * @generated
	 */
	EReference getPortCost_Ports();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.PortCost#getVessels <em>Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessels</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortCost#getVessels()
	 * @see #getPortCost()
	 * @generated
	 */
	EReference getPortCost_Vessels();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.PortCost#getLadenCost <em>Laden Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Laden Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortCost#getLadenCost()
	 * @see #getPortCost()
	 * @generated
	 */
	EReference getPortCost_LadenCost();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.PortCost#getBallastCost <em>Ballast Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Ballast Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortCost#getBallastCost()
	 * @see #getPortCost()
	 * @generated
	 */
	EReference getPortCost_BallastCost();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.pricing.SpotType <em>Spot Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Spot Type</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotType
	 * @generated
	 */
	EEnum getSpotType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PricingFactory getPricingFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingModelImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPricingModel()
		 * @generated
		 */
		EClass PRICING_MODEL = eINSTANCE.getPricingModel();

		/**
		 * The meta object literal for the '<em><b>Commodity Indices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__COMMODITY_INDICES = eINSTANCE.getPricingModel_CommodityIndices();

		/**
		 * The meta object literal for the '<em><b>Charter Indices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__CHARTER_INDICES = eINSTANCE.getPricingModel_CharterIndices();

		/**
		 * The meta object literal for the '<em><b>Fleet Cost</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__FLEET_COST = eINSTANCE.getPricingModel_FleetCost();

		/**
		 * The meta object literal for the '<em><b>Route Costs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__ROUTE_COSTS = eINSTANCE.getPricingModel_RouteCosts();

		/**
		 * The meta object literal for the '<em><b>Port Costs</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__PORT_COSTS = eINSTANCE.getPricingModel_PortCosts();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.DataIndexImpl <em>Data Index</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.DataIndexImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDataIndex()
		 * @generated
		 */
		EClass DATA_INDEX = eINSTANCE.getDataIndex();

		/**
		 * The meta object literal for the '<em><b>Points</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_INDEX__POINTS = eINSTANCE.getDataIndex_Points();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.DerivedIndexImpl <em>Derived Index</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.DerivedIndexImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDerivedIndex()
		 * @generated
		 */
		EClass DERIVED_INDEX = eINSTANCE.getDerivedIndex();

		/**
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DERIVED_INDEX__EXPRESSION = eINSTANCE.getDerivedIndex_Expression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.IndexPointImpl <em>Index Point</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.IndexPointImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getIndexPoint()
		 * @generated
		 */
		EClass INDEX_POINT = eINSTANCE.getIndexPoint();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEX_POINT__DATE = eINSTANCE.getIndexPoint_Date();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEX_POINT__VALUE = eINSTANCE.getIndexPoint_Value();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.IndexImpl <em>Index</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.IndexImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getIndex()
		 * @generated
		 */
		EClass INDEX = eINSTANCE.getIndex();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.FleetCostModelImpl <em>Fleet Cost Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.FleetCostModelImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getFleetCostModel()
		 * @generated
		 */
		EClass FLEET_COST_MODEL = eINSTANCE.getFleetCostModel();

		/**
		 * The meta object literal for the '<em><b>Charter Costs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_COST_MODEL__CHARTER_COSTS = eINSTANCE.getFleetCostModel_CharterCosts();

		/**
		 * The meta object literal for the '<em><b>Base Fuel Prices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_COST_MODEL__BASE_FUEL_PRICES = eINSTANCE.getFleetCostModel_BaseFuelPrices();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.RouteCostImpl <em>Route Cost</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.RouteCostImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getRouteCost()
		 * @generated
		 */
		EClass ROUTE_COST = eINSTANCE.getRouteCost();

		/**
		 * The meta object literal for the '<em><b>Routes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTE_COST__ROUTES = eINSTANCE.getRouteCost_Routes();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.CharterCostModelImpl <em>Charter Cost Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.CharterCostModelImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCharterCostModel()
		 * @generated
		 */
		EClass CHARTER_COST_MODEL = eINSTANCE.getCharterCostModel();

		/**
		 * The meta object literal for the '<em><b>Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_COST_MODEL__VESSELS = eINSTANCE.getCharterCostModel_Vessels();

		/**
		 * The meta object literal for the '<em><b>Charter In Price</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_COST_MODEL__CHARTER_IN_PRICE = eINSTANCE.getCharterCostModel_CharterInPrice();

		/**
		 * The meta object literal for the '<em><b>Charter Out Price</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_COST_MODEL__CHARTER_OUT_PRICE = eINSTANCE.getCharterCostModel_CharterOutPrice();

		/**
		 * The meta object literal for the '<em><b>Spot Charter Count</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_COST_MODEL__SPOT_CHARTER_COUNT = eINSTANCE.getCharterCostModel_SpotCharterCount();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl <em>Base Fuel Cost</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getBaseFuelCost()
		 * @generated
		 */
		EClass BASE_FUEL_COST = eINSTANCE.getBaseFuelCost();

		/**
		 * The meta object literal for the '<em><b>Fuels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_FUEL_COST__FUELS = eINSTANCE.getBaseFuelCost_Fuels();

		/**
		 * The meta object literal for the '<em><b>Price</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_FUEL_COST__PRICE = eINSTANCE.getBaseFuelCost_Price();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl <em>Spot Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSpotMarket()
		 * @generated
		 */
		EClass SPOT_MARKET = eINSTANCE.getSpotMarket();

		/**
		 * The meta object literal for the '<em><b>Availability</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_MARKET__AVAILABILITY = eINSTANCE.getSpotMarket_Availability();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_MARKET__PORTS = eINSTANCE.getSpotMarket_Ports();

		/**
		 * The meta object literal for the '<em><b>Min Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPOT_MARKET__MIN_QUANTITY = eINSTANCE.getSpotMarket_MinQuantity();

		/**
		 * The meta object literal for the '<em><b>Max Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPOT_MARKET__MAX_QUANTITY = eINSTANCE.getSpotMarket_MaxQuantity();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl <em>Port Cost</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.PortCostImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortCost()
		 * @generated
		 */
		EClass PORT_COST = eINSTANCE.getPortCost();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_COST__PORTS = eINSTANCE.getPortCost_Ports();

		/**
		 * The meta object literal for the '<em><b>Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_COST__VESSELS = eINSTANCE.getPortCost_Vessels();

		/**
		 * The meta object literal for the '<em><b>Laden Cost</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_COST__LADEN_COST = eINSTANCE.getPortCost_LadenCost();

		/**
		 * The meta object literal for the '<em><b>Ballast Cost</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_COST__BALLAST_COST = eINSTANCE.getPortCost_BallastCost();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.SpotType <em>Spot Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.SpotType
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSpotType()
		 * @generated
		 */
		EEnum SPOT_TYPE = eINSTANCE.getSpotType();

	}

} //PricingPackage
