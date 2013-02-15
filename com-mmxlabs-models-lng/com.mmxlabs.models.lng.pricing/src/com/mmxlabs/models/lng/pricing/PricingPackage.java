/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.types.TypesPackage;
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
	 * The feature id for the '<em><b>Commodity Indices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__COMMODITY_INDICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Charter Indices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__CHARTER_INDICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Fleet Cost</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__FLEET_COST = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Route Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__ROUTE_COSTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Port Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__PORT_COSTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Cooldown Prices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__COOLDOWN_PRICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Des Purchase Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__DES_PURCHASE_SPOT_MARKET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Des Sales Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__DES_SALES_SPOT_MARKET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Fob Purchases Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Fob Sales Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__FOB_SALES_SPOT_MARKET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

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
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX__OTHER_NAMES = TypesPackage.AINDEX__OTHER_NAMES;

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
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INDEX__OTHER_NAMES = INDEX__OTHER_NAMES;

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
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DERIVED_INDEX__OTHER_NAMES = INDEX__OTHER_NAMES;

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
	 * The feature id for the '<em><b>Route</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST__ROUTE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST__VESSEL_CLASS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Laden Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST__LADEN_COST = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Ballast Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST__BALLAST_COST = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Route Cost</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

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
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Vessel Classes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__VESSEL_CLASSES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Charter In Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__CHARTER_IN_PRICE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Charter Out Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__CHARTER_OUT_PRICE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Spot Charter Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__SPOT_CHARTER_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Min Charter Out Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__MIN_CHARTER_OUT_DURATION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Charter Cost Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

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
	 * The feature id for the '<em><b>Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST__FUEL = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketGroupImpl <em>Spot Market Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.SpotMarketGroupImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSpotMarketGroup()
	 * @generated
	 */
	int SPOT_MARKET_GROUP = 9;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_GROUP__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_GROUP__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_GROUP__AVAILABILITY = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_GROUP__TYPE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Markets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_GROUP__MARKETS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Spot Market Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_GROUP_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl <em>Spot Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.SpotMarketImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSpotMarket()
	 * @generated
	 */
	int SPOT_MARKET = 10;

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
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__OTHER_NAMES = TypesPackage.ASPOT_MARKET__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__AVAILABILITY = TypesPackage.ASPOT_MARKET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__MIN_QUANTITY = TypesPackage.ASPOT_MARKET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__MAX_QUANTITY = TypesPackage.ASPOT_MARKET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Spot Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_FEATURE_COUNT = TypesPackage.ASPOT_MARKET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl <em>Port Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortCostImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortCost()
	 * @generated
	 */
	int PORT_COST = 11;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST__PORTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST__ENTRIES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Reference Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST__REFERENCE_CAPACITY = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Port Cost</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortCostEntryImpl <em>Port Cost Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortCostEntryImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortCostEntry()
	 * @generated
	 */
	int PORT_COST_ENTRY = 12;

	/**
	 * The feature id for the '<em><b>Activity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST_ENTRY__ACTIVITY = 0;

	/**
	 * The feature id for the '<em><b>Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST_ENTRY__COST = 1;

	/**
	 * The number of structural features of the '<em>Port Cost Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CooldownPriceImpl <em>Cooldown Price</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CooldownPriceImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCooldownPrice()
	 * @generated
	 */
	int COOLDOWN_PRICE = 13;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__PORTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__INDEX = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Cooldown Price</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.DESPurchaseMarketImpl <em>DES Purchase Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.DESPurchaseMarketImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDESPurchaseMarket()
	 * @generated
	 */
	int DES_PURCHASE_MARKET = 14;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__EXTENSIONS = SPOT_MARKET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__PROXIES = SPOT_MARKET__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__UUID = SPOT_MARKET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__NAME = SPOT_MARKET__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__OTHER_NAMES = SPOT_MARKET__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__AVAILABILITY = SPOT_MARKET__AVAILABILITY;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__MIN_QUANTITY = SPOT_MARKET__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__MAX_QUANTITY = SPOT_MARKET__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Cv</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__CV = SPOT_MARKET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Destination Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__DESTINATION_PORTS = SPOT_MARKET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__CONTRACT = SPOT_MARKET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>DES Purchase Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET_FEATURE_COUNT = SPOT_MARKET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.DESSalesMarketImpl <em>DES Sales Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.DESSalesMarketImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDESSalesMarket()
	 * @generated
	 */
	int DES_SALES_MARKET = 15;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__EXTENSIONS = SPOT_MARKET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__PROXIES = SPOT_MARKET__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__UUID = SPOT_MARKET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__NAME = SPOT_MARKET__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__OTHER_NAMES = SPOT_MARKET__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__AVAILABILITY = SPOT_MARKET__AVAILABILITY;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__MIN_QUANTITY = SPOT_MARKET__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__MAX_QUANTITY = SPOT_MARKET__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Notional Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__NOTIONAL_PORT = SPOT_MARKET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__CONTRACT = SPOT_MARKET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>DES Sales Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET_FEATURE_COUNT = SPOT_MARKET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.FOBPurchasesMarketImpl <em>FOB Purchases Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.FOBPurchasesMarketImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getFOBPurchasesMarket()
	 * @generated
	 */
	int FOB_PURCHASES_MARKET = 16;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__EXTENSIONS = SPOT_MARKET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__PROXIES = SPOT_MARKET__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__UUID = SPOT_MARKET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__NAME = SPOT_MARKET__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__OTHER_NAMES = SPOT_MARKET__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__AVAILABILITY = SPOT_MARKET__AVAILABILITY;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__MIN_QUANTITY = SPOT_MARKET__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__MAX_QUANTITY = SPOT_MARKET__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Notional Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__NOTIONAL_PORT = SPOT_MARKET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Cv</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__CV = SPOT_MARKET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__CONTRACT = SPOT_MARKET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>FOB Purchases Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET_FEATURE_COUNT = SPOT_MARKET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.FOBSalesMarketImpl <em>FOB Sales Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.FOBSalesMarketImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getFOBSalesMarket()
	 * @generated
	 */
	int FOB_SALES_MARKET = 17;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__EXTENSIONS = SPOT_MARKET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__PROXIES = SPOT_MARKET__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__UUID = SPOT_MARKET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__NAME = SPOT_MARKET__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__OTHER_NAMES = SPOT_MARKET__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__AVAILABILITY = SPOT_MARKET__AVAILABILITY;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__MIN_QUANTITY = SPOT_MARKET__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__MAX_QUANTITY = SPOT_MARKET__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Load Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__LOAD_PORT = SPOT_MARKET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__CONTRACT = SPOT_MARKET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>FOB Sales Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET_FEATURE_COUNT = SPOT_MARKET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.SpotAvailabilityImpl <em>Spot Availability</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.SpotAvailabilityImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSpotAvailability()
	 * @generated
	 */
	int SPOT_AVAILABILITY = 18;

	/**
	 * The feature id for the '<em><b>Constant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_AVAILABILITY__CONSTANT = 0;

	/**
	 * The feature id for the '<em><b>Curve</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_AVAILABILITY__CURVE = 1;

	/**
	 * The number of structural features of the '<em>Spot Availability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_AVAILABILITY_FEATURE_COUNT = 2;

	/**
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.SpotType <em>Spot Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.SpotType
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSpotType()
	 * @generated
	 */
	int SPOT_TYPE = 19;


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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getPortCosts <em>Port Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Port Costs</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getPortCosts()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_PortCosts();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getCooldownPrices <em>Cooldown Prices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cooldown Prices</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getCooldownPrices()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_CooldownPrices();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.pricing.PricingModel#getDesPurchaseSpotMarket <em>Des Purchase Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Des Purchase Spot Market</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getDesPurchaseSpotMarket()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_DesPurchaseSpotMarket();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.pricing.PricingModel#getDesSalesSpotMarket <em>Des Sales Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Des Sales Spot Market</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getDesSalesSpotMarket()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_DesSalesSpotMarket();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.pricing.PricingModel#getFobPurchasesSpotMarket <em>Fob Purchases Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fob Purchases Spot Market</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getFobPurchasesSpotMarket()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_FobPurchasesSpotMarket();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.pricing.PricingModel#getFobSalesSpotMarket <em>Fob Sales Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fob Sales Spot Market</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getFobSalesSpotMarket()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_FobSalesSpotMarket();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.RouteCost#getRoute <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Route</em>'.
	 * @see com.mmxlabs.models.lng.pricing.RouteCost#getRoute()
	 * @see #getRouteCost()
	 * @generated
	 */
	EReference getRouteCost_Route();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.RouteCost#getVesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Class</em>'.
	 * @see com.mmxlabs.models.lng.pricing.RouteCost#getVesselClass()
	 * @see #getRouteCost()
	 * @generated
	 */
	EReference getRouteCost_VesselClass();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.RouteCost#getLadenCost <em>Laden Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.RouteCost#getLadenCost()
	 * @see #getRouteCost()
	 * @generated
	 */
	EAttribute getRouteCost_LadenCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.RouteCost#getBallastCost <em>Ballast Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.RouteCost#getBallastCost()
	 * @see #getRouteCost()
	 * @generated
	 */
	EAttribute getRouteCost_BallastCost();

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
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getVesselClasses <em>Vessel Classes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessel Classes</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CharterCostModel#getVesselClasses()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EReference getCharterCostModel_VesselClasses();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getSpotCharterCount <em>Spot Charter Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Charter Count</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CharterCostModel#getSpotCharterCount()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EAttribute getCharterCostModel_SpotCharterCount();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getMinCharterOutDuration <em>Min Charter Out Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Charter Out Duration</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CharterCostModel#getMinCharterOutDuration()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EAttribute getCharterCostModel_MinCharterOutDuration();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getFuel <em>Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Fuel</em>'.
	 * @see com.mmxlabs.models.lng.pricing.BaseFuelCost#getFuel()
	 * @see #getBaseFuelCost()
	 * @generated
	 */
	EReference getBaseFuelCost_Fuel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see com.mmxlabs.models.lng.pricing.BaseFuelCost#getPrice()
	 * @see #getBaseFuelCost()
	 * @generated
	 */
	EAttribute getBaseFuelCost_Price();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.SpotMarketGroup <em>Spot Market Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Market Group</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarketGroup
	 * @generated
	 */
	EClass getSpotMarketGroup();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.pricing.SpotMarketGroup#getAvailability <em>Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Availability</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarketGroup#getAvailability()
	 * @see #getSpotMarketGroup()
	 * @generated
	 */
	EReference getSpotMarketGroup_Availability();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SpotMarketGroup#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarketGroup#getType()
	 * @see #getSpotMarketGroup()
	 * @generated
	 */
	EAttribute getSpotMarketGroup_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.SpotMarketGroup#getMarkets <em>Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Markets</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarketGroup#getMarkets()
	 * @see #getSpotMarketGroup()
	 * @generated
	 */
	EReference getSpotMarketGroup_Markets();

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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.pricing.SpotMarket#getAvailability <em>Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Availability</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarket#getAvailability()
	 * @see #getSpotMarket()
	 * @generated
	 */
	EReference getSpotMarket_Availability();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PortCost#getEntries <em>Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entries</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortCost#getEntries()
	 * @see #getPortCost()
	 * @generated
	 */
	EReference getPortCost_Entries();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PortCost#getReferenceCapacity <em>Reference Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reference Capacity</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortCost#getReferenceCapacity()
	 * @see #getPortCost()
	 * @generated
	 */
	EAttribute getPortCost_ReferenceCapacity();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.PortCostEntry <em>Port Cost Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port Cost Entry</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortCostEntry
	 * @generated
	 */
	EClass getPortCostEntry();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PortCostEntry#getActivity <em>Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Activity</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortCostEntry#getActivity()
	 * @see #getPortCostEntry()
	 * @generated
	 */
	EAttribute getPortCostEntry_Activity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PortCostEntry#getCost <em>Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortCostEntry#getCost()
	 * @see #getPortCostEntry()
	 * @generated
	 */
	EAttribute getPortCostEntry_Cost();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.CooldownPrice <em>Cooldown Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cooldown Price</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CooldownPrice
	 * @generated
	 */
	EClass getCooldownPrice();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.CooldownPrice#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ports</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CooldownPrice#getPorts()
	 * @see #getCooldownPrice()
	 * @generated
	 */
	EReference getCooldownPrice_Ports();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.CooldownPrice#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CooldownPrice#getIndex()
	 * @see #getCooldownPrice()
	 * @generated
	 */
	EReference getCooldownPrice_Index();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.DESPurchaseMarket <em>DES Purchase Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>DES Purchase Market</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DESPurchaseMarket
	 * @generated
	 */
	EClass getDESPurchaseMarket();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.DESPurchaseMarket#getCv <em>Cv</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cv</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DESPurchaseMarket#getCv()
	 * @see #getDESPurchaseMarket()
	 * @generated
	 */
	EAttribute getDESPurchaseMarket_Cv();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.DESPurchaseMarket#getDestinationPorts <em>Destination Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Destination Ports</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DESPurchaseMarket#getDestinationPorts()
	 * @see #getDESPurchaseMarket()
	 * @generated
	 */
	EReference getDESPurchaseMarket_DestinationPorts();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.DESPurchaseMarket#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DESPurchaseMarket#getContract()
	 * @see #getDESPurchaseMarket()
	 * @generated
	 */
	EReference getDESPurchaseMarket_Contract();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.DESSalesMarket <em>DES Sales Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>DES Sales Market</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DESSalesMarket
	 * @generated
	 */
	EClass getDESSalesMarket();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.DESSalesMarket#getNotionalPort <em>Notional Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Notional Port</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DESSalesMarket#getNotionalPort()
	 * @see #getDESSalesMarket()
	 * @generated
	 */
	EReference getDESSalesMarket_NotionalPort();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.DESSalesMarket#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DESSalesMarket#getContract()
	 * @see #getDESSalesMarket()
	 * @generated
	 */
	EReference getDESSalesMarket_Contract();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.FOBPurchasesMarket <em>FOB Purchases Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>FOB Purchases Market</em>'.
	 * @see com.mmxlabs.models.lng.pricing.FOBPurchasesMarket
	 * @generated
	 */
	EClass getFOBPurchasesMarket();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.FOBPurchasesMarket#getNotionalPort <em>Notional Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Notional Port</em>'.
	 * @see com.mmxlabs.models.lng.pricing.FOBPurchasesMarket#getNotionalPort()
	 * @see #getFOBPurchasesMarket()
	 * @generated
	 */
	EReference getFOBPurchasesMarket_NotionalPort();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.FOBPurchasesMarket#getCv <em>Cv</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cv</em>'.
	 * @see com.mmxlabs.models.lng.pricing.FOBPurchasesMarket#getCv()
	 * @see #getFOBPurchasesMarket()
	 * @generated
	 */
	EAttribute getFOBPurchasesMarket_Cv();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.FOBPurchasesMarket#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.pricing.FOBPurchasesMarket#getContract()
	 * @see #getFOBPurchasesMarket()
	 * @generated
	 */
	EReference getFOBPurchasesMarket_Contract();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.FOBSalesMarket <em>FOB Sales Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>FOB Sales Market</em>'.
	 * @see com.mmxlabs.models.lng.pricing.FOBSalesMarket
	 * @generated
	 */
	EClass getFOBSalesMarket();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.FOBSalesMarket#getLoadPort <em>Load Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Port</em>'.
	 * @see com.mmxlabs.models.lng.pricing.FOBSalesMarket#getLoadPort()
	 * @see #getFOBSalesMarket()
	 * @generated
	 */
	EReference getFOBSalesMarket_LoadPort();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.FOBSalesMarket#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.pricing.FOBSalesMarket#getContract()
	 * @see #getFOBSalesMarket()
	 * @generated
	 */
	EReference getFOBSalesMarket_Contract();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.SpotAvailability <em>Spot Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Availability</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotAvailability
	 * @generated
	 */
	EClass getSpotAvailability();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SpotAvailability#getConstant <em>Constant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Constant</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotAvailability#getConstant()
	 * @see #getSpotAvailability()
	 * @generated
	 */
	EAttribute getSpotAvailability_Constant();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.pricing.SpotAvailability#getCurve <em>Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Curve</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SpotAvailability#getCurve()
	 * @see #getSpotAvailability()
	 * @generated
	 */
	EReference getSpotAvailability_Curve();

	/**
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
	 * @since 3.0
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
		 * The meta object literal for the '<em><b>Port Costs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__PORT_COSTS = eINSTANCE.getPricingModel_PortCosts();

		/**
		 * The meta object literal for the '<em><b>Cooldown Prices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__COOLDOWN_PRICES = eINSTANCE.getPricingModel_CooldownPrices();

		/**
		 * The meta object literal for the '<em><b>Des Purchase Spot Market</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__DES_PURCHASE_SPOT_MARKET = eINSTANCE.getPricingModel_DesPurchaseSpotMarket();

		/**
		 * The meta object literal for the '<em><b>Des Sales Spot Market</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__DES_SALES_SPOT_MARKET = eINSTANCE.getPricingModel_DesSalesSpotMarket();

		/**
		 * The meta object literal for the '<em><b>Fob Purchases Spot Market</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET = eINSTANCE.getPricingModel_FobPurchasesSpotMarket();

		/**
		 * The meta object literal for the '<em><b>Fob Sales Spot Market</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__FOB_SALES_SPOT_MARKET = eINSTANCE.getPricingModel_FobSalesSpotMarket();

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
		 * The meta object literal for the '<em><b>Route</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTE_COST__ROUTE = eINSTANCE.getRouteCost_Route();

		/**
		 * The meta object literal for the '<em><b>Vessel Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTE_COST__VESSEL_CLASS = eINSTANCE.getRouteCost_VesselClass();

		/**
		 * The meta object literal for the '<em><b>Laden Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_COST__LADEN_COST = eINSTANCE.getRouteCost_LadenCost();

		/**
		 * The meta object literal for the '<em><b>Ballast Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_COST__BALLAST_COST = eINSTANCE.getRouteCost_BallastCost();

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
		 * The meta object literal for the '<em><b>Vessel Classes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_COST_MODEL__VESSEL_CLASSES = eINSTANCE.getCharterCostModel_VesselClasses();

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
		 * The meta object literal for the '<em><b>Spot Charter Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_COST_MODEL__SPOT_CHARTER_COUNT = eINSTANCE.getCharterCostModel_SpotCharterCount();

		/**
		 * The meta object literal for the '<em><b>Min Charter Out Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_COST_MODEL__MIN_CHARTER_OUT_DURATION = eINSTANCE.getCharterCostModel_MinCharterOutDuration();

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
		 * The meta object literal for the '<em><b>Fuel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_FUEL_COST__FUEL = eINSTANCE.getBaseFuelCost_Fuel();

		/**
		 * The meta object literal for the '<em><b>Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_FUEL_COST__PRICE = eINSTANCE.getBaseFuelCost_Price();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketGroupImpl <em>Spot Market Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.SpotMarketGroupImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSpotMarketGroup()
		 * @generated
		 */
		EClass SPOT_MARKET_GROUP = eINSTANCE.getSpotMarketGroup();

		/**
		 * The meta object literal for the '<em><b>Availability</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_MARKET_GROUP__AVAILABILITY = eINSTANCE.getSpotMarketGroup_Availability();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPOT_MARKET_GROUP__TYPE = eINSTANCE.getSpotMarketGroup_Type();

		/**
		 * The meta object literal for the '<em><b>Markets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_MARKET_GROUP__MARKETS = eINSTANCE.getSpotMarketGroup_Markets();

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
		 * The meta object literal for the '<em><b>Availability</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_MARKET__AVAILABILITY = eINSTANCE.getSpotMarket_Availability();

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
		 * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_COST__ENTRIES = eINSTANCE.getPortCost_Entries();

		/**
		 * The meta object literal for the '<em><b>Reference Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT_COST__REFERENCE_CAPACITY = eINSTANCE.getPortCost_ReferenceCapacity();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.PortCostEntryImpl <em>Port Cost Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.PortCostEntryImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortCostEntry()
		 * @generated
		 */
		EClass PORT_COST_ENTRY = eINSTANCE.getPortCostEntry();

		/**
		 * The meta object literal for the '<em><b>Activity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT_COST_ENTRY__ACTIVITY = eINSTANCE.getPortCostEntry_Activity();

		/**
		 * The meta object literal for the '<em><b>Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT_COST_ENTRY__COST = eINSTANCE.getPortCostEntry_Cost();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.CooldownPriceImpl <em>Cooldown Price</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.CooldownPriceImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCooldownPrice()
		 * @generated
		 */
		EClass COOLDOWN_PRICE = eINSTANCE.getCooldownPrice();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COOLDOWN_PRICE__PORTS = eINSTANCE.getCooldownPrice_Ports();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COOLDOWN_PRICE__INDEX = eINSTANCE.getCooldownPrice_Index();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.DESPurchaseMarketImpl <em>DES Purchase Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.DESPurchaseMarketImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDESPurchaseMarket()
		 * @generated
		 */
		EClass DES_PURCHASE_MARKET = eINSTANCE.getDESPurchaseMarket();

		/**
		 * The meta object literal for the '<em><b>Cv</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DES_PURCHASE_MARKET__CV = eINSTANCE.getDESPurchaseMarket_Cv();

		/**
		 * The meta object literal for the '<em><b>Destination Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DES_PURCHASE_MARKET__DESTINATION_PORTS = eINSTANCE.getDESPurchaseMarket_DestinationPorts();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DES_PURCHASE_MARKET__CONTRACT = eINSTANCE.getDESPurchaseMarket_Contract();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.DESSalesMarketImpl <em>DES Sales Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.DESSalesMarketImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDESSalesMarket()
		 * @generated
		 */
		EClass DES_SALES_MARKET = eINSTANCE.getDESSalesMarket();

		/**
		 * The meta object literal for the '<em><b>Notional Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DES_SALES_MARKET__NOTIONAL_PORT = eINSTANCE.getDESSalesMarket_NotionalPort();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DES_SALES_MARKET__CONTRACT = eINSTANCE.getDESSalesMarket_Contract();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.FOBPurchasesMarketImpl <em>FOB Purchases Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.FOBPurchasesMarketImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getFOBPurchasesMarket()
		 * @generated
		 */
		EClass FOB_PURCHASES_MARKET = eINSTANCE.getFOBPurchasesMarket();

		/**
		 * The meta object literal for the '<em><b>Notional Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FOB_PURCHASES_MARKET__NOTIONAL_PORT = eINSTANCE.getFOBPurchasesMarket_NotionalPort();

		/**
		 * The meta object literal for the '<em><b>Cv</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FOB_PURCHASES_MARKET__CV = eINSTANCE.getFOBPurchasesMarket_Cv();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FOB_PURCHASES_MARKET__CONTRACT = eINSTANCE.getFOBPurchasesMarket_Contract();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.FOBSalesMarketImpl <em>FOB Sales Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.FOBSalesMarketImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getFOBSalesMarket()
		 * @generated
		 */
		EClass FOB_SALES_MARKET = eINSTANCE.getFOBSalesMarket();

		/**
		 * The meta object literal for the '<em><b>Load Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FOB_SALES_MARKET__LOAD_PORT = eINSTANCE.getFOBSalesMarket_LoadPort();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FOB_SALES_MARKET__CONTRACT = eINSTANCE.getFOBSalesMarket_Contract();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.SpotAvailabilityImpl <em>Spot Availability</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.SpotAvailabilityImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSpotAvailability()
		 * @generated
		 */
		EClass SPOT_AVAILABILITY = eINSTANCE.getSpotAvailability();

		/**
		 * The meta object literal for the '<em><b>Constant</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPOT_AVAILABILITY__CONSTANT = eINSTANCE.getSpotAvailability_Constant();

		/**
		 * The meta object literal for the '<em><b>Curve</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_AVAILABILITY__CURVE = eINSTANCE.getSpotAvailability_Curve();

		/**
		 * @since 3.0
		 * @since 3.0
		 * @since 3.0
		 * @since 3.0
		 * @since 3.0
		 * @since 3.0
		 * @since 3.0
		 * @since 3.0
		 * @since 3.0
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
