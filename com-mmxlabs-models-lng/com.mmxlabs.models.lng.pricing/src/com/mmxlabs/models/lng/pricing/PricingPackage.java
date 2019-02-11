/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Currency Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__CURRENCY_CURVES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Commodity Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__COMMODITY_CURVES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Charter Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__CHARTER_CURVES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Bunker Fuel Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__BUNKER_FUEL_CURVES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Conversion Factors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__CONVERSION_FACTORS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Market Curve Data Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__MARKET_CURVE_DATA_VERSION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Settled Prices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__SETTLED_PRICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Market Indices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__MARKET_INDICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Holiday Calendars</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__HOLIDAY_CALENDARS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Settle Strategies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__SETTLE_STRATEGIES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

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
	 * The number of structural features of the '<em>Index</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_FEATURE_COUNT = 0;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.RouteCostImpl <em>Route Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.RouteCostImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getRouteCost()
	 * @generated
	 */
	int ROUTE_COST = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl <em>Base Fuel Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getBaseFuelCost()
	 * @generated
	 */
	int BASE_FUEL_COST = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl <em>Port Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortCostImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortCost()
	 * @generated
	 */
	int PORT_COST = 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortCostEntryImpl <em>Port Cost Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortCostEntryImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortCostEntry()
	 * @generated
	 */
	int PORT_COST_ENTRY = 9;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CooldownPriceImpl <em>Cooldown Price</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CooldownPriceImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCooldownPrice()
	 * @generated
	 */
	int COOLDOWN_PRICE = 10;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsExpressionMapImpl <em>Ports Expression Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortsExpressionMapImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsExpressionMap()
	 * @generated
	 */
	int PORTS_EXPRESSION_MAP = 11;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CostModelImpl <em>Cost Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CostModelImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCostModel()
	 * @generated
	 */
	int COST_MODEL = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Route Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_MODEL__ROUTE_COSTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Port Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_MODEL__PORT_COSTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Cooldown Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_MODEL__COOLDOWN_COSTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Base Fuel Costs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_MODEL__BASE_FUEL_COSTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Panama Canal Tariff</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_MODEL__PANAMA_CANAL_TARIFF = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Suez Canal Tariff</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_MODEL__SUEZ_CANAL_TARIFF = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Cost Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COST_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Route Option</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST__ROUTE_OPTION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST__VESSELS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST__FUEL = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST__EXPRESSION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Base Fuel Cost</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COST__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_EXPRESSION_MAP__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_EXPRESSION_MAP__PORTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_EXPRESSION_MAP__EXPRESSION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Ports Expression Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_EXPRESSION_MAP_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__EXTENSIONS = PORTS_EXPRESSION_MAP__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__PORTS = PORTS_EXPRESSION_MAP__PORTS;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__EXPRESSION = PORTS_EXPRESSION_MAP__EXPRESSION;

	/**
	 * The feature id for the '<em><b>Lumpsum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__LUMPSUM = PORTS_EXPRESSION_MAP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Cooldown Price</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE_FEATURE_COUNT = PORTS_EXPRESSION_MAP_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsSplitExpressionMapImpl <em>Ports Split Expression Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortsSplitExpressionMapImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsSplitExpressionMap()
	 * @generated
	 */
	int PORTS_SPLIT_EXPRESSION_MAP = 12;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_SPLIT_EXPRESSION_MAP__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_SPLIT_EXPRESSION_MAP__PORTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Expression1</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_SPLIT_EXPRESSION_MAP__EXPRESSION1 = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Expression2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_SPLIT_EXPRESSION_MAP__EXPRESSION2 = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Ports Split Expression Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_SPLIT_EXPRESSION_MAP_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffImpl <em>Panama Canal Tariff</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPanamaCanalTariff()
	 * @generated
	 */
	int PANAMA_CANAL_TARIFF = 13;

	/**
	 * The feature id for the '<em><b>Bands</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF__BANDS = 0;

	/**
	 * The feature id for the '<em><b>Markup Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF__MARKUP_RATE = 1;

	/**
	 * The number of structural features of the '<em>Panama Canal Tariff</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffBandImpl <em>Panama Canal Tariff Band</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffBandImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPanamaCanalTariffBand()
	 * @generated
	 */
	int PANAMA_CANAL_TARIFF_BAND = 14;

	/**
	 * The feature id for the '<em><b>Laden Tariff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF = 0;

	/**
	 * The feature id for the '<em><b>Ballast Tariff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF = 1;

	/**
	 * The feature id for the '<em><b>Ballast Roundtrip Tariff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF = 2;

	/**
	 * The feature id for the '<em><b>Band Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF_BAND__BAND_START = 3;

	/**
	 * The feature id for the '<em><b>Band End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF_BAND__BAND_END = 4;

	/**
	 * The number of structural features of the '<em>Panama Canal Tariff Band</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF_BAND_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTugBandImpl <em>Suez Canal Tug Band</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.SuezCanalTugBandImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSuezCanalTugBand()
	 * @generated
	 */
	int SUEZ_CANAL_TUG_BAND = 15;

	/**
	 * The feature id for the '<em><b>Tugs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TUG_BAND__TUGS = 0;

	/**
	 * The feature id for the '<em><b>Band Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TUG_BAND__BAND_START = 1;

	/**
	 * The feature id for the '<em><b>Band End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TUG_BAND__BAND_END = 2;

	/**
	 * The number of structural features of the '<em>Suez Canal Tug Band</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TUG_BAND_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl <em>Suez Canal Tariff</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSuezCanalTariff()
	 * @generated
	 */
	int SUEZ_CANAL_TARIFF = 16;

	/**
	 * The feature id for the '<em><b>Bands</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF__BANDS = 0;

	/**
	 * The feature id for the '<em><b>Tug Bands</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF__TUG_BANDS = 1;

	/**
	 * The feature id for the '<em><b>Tug Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF__TUG_COST = 2;

	/**
	 * The feature id for the '<em><b>Fixed Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF__FIXED_COSTS = 3;

	/**
	 * The feature id for the '<em><b>Discount Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR = 4;

	/**
	 * The feature id for the '<em><b>Sdr To USD</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF__SDR_TO_USD = 5;

	/**
	 * The number of structural features of the '<em>Suez Canal Tariff</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffBandImpl <em>Suez Canal Tariff Band</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffBandImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSuezCanalTariffBand()
	 * @generated
	 */
	int SUEZ_CANAL_TARIFF_BAND = 17;

	/**
	 * The feature id for the '<em><b>Laden Tariff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF_BAND__LADEN_TARIFF = 0;

	/**
	 * The feature id for the '<em><b>Ballast Tariff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF_BAND__BALLAST_TARIFF = 1;

	/**
	 * The feature id for the '<em><b>Band Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF_BAND__BAND_START = 2;

	/**
	 * The feature id for the '<em><b>Band End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF_BAND__BAND_END = 3;

	/**
	 * The number of structural features of the '<em>Suez Canal Tariff Band</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF_BAND_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.UnitConversionImpl <em>Unit Conversion</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.UnitConversionImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getUnitConversion()
	 * @generated
	 */
	int UNIT_CONVERSION = 18;

	/**
	 * The feature id for the '<em><b>From</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_CONVERSION__FROM = 0;

	/**
	 * The feature id for the '<em><b>To</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_CONVERSION__TO = 1;

	/**
	 * The feature id for the '<em><b>Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_CONVERSION__FACTOR = 2;

	/**
	 * The number of structural features of the '<em>Unit Conversion</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_CONVERSION_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.DatePointContainerImpl <em>Date Point Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.DatePointContainerImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDatePointContainer()
	 * @generated
	 */
	int DATE_POINT_CONTAINER = 19;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_POINT_CONTAINER__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_POINT_CONTAINER__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_POINT_CONTAINER__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Points</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_POINT_CONTAINER__POINTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Date Point Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_POINT_CONTAINER_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.DatePointImpl <em>Date Point</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.DatePointImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDatePoint()
	 * @generated
	 */
	int DATE_POINT = 20;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_POINT__DATE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_POINT__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Date Point</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_POINT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.YearMonthPointContainerImpl <em>Year Month Point Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.YearMonthPointContainerImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getYearMonthPointContainer()
	 * @generated
	 */
	int YEAR_MONTH_POINT_CONTAINER = 21;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int YEAR_MONTH_POINT_CONTAINER__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int YEAR_MONTH_POINT_CONTAINER__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int YEAR_MONTH_POINT_CONTAINER__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Points</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int YEAR_MONTH_POINT_CONTAINER__POINTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Year Month Point Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int YEAR_MONTH_POINT_CONTAINER_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.YearMonthPointImpl <em>Year Month Point</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.YearMonthPointImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getYearMonthPoint()
	 * @generated
	 */
	int YEAR_MONTH_POINT = 22;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int YEAR_MONTH_POINT__DATE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int YEAR_MONTH_POINT__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Year Month Point</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int YEAR_MONTH_POINT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.AbstractYearMonthCurveImpl <em>Abstract Year Month Curve</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.AbstractYearMonthCurveImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getAbstractYearMonthCurve()
	 * @generated
	 */
	int ABSTRACT_YEAR_MONTH_CURVE = 23;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_YEAR_MONTH_CURVE__EXTENSIONS = YEAR_MONTH_POINT_CONTAINER__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_YEAR_MONTH_CURVE__UUID = YEAR_MONTH_POINT_CONTAINER__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_YEAR_MONTH_CURVE__NAME = YEAR_MONTH_POINT_CONTAINER__NAME;

	/**
	 * The feature id for the '<em><b>Points</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_YEAR_MONTH_CURVE__POINTS = YEAR_MONTH_POINT_CONTAINER__POINTS;

	/**
	 * The feature id for the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT = YEAR_MONTH_POINT_CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT = YEAR_MONTH_POINT_CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION = YEAR_MONTH_POINT_CONTAINER_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Abstract Year Month Curve</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_YEAR_MONTH_CURVE_FEATURE_COUNT = YEAR_MONTH_POINT_CONTAINER_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CommodityCurveImpl <em>Commodity Curve</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CommodityCurveImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCommodityCurve()
	 * @generated
	 */
	int COMMODITY_CURVE = 24;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE__EXTENSIONS = ABSTRACT_YEAR_MONTH_CURVE__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE__UUID = ABSTRACT_YEAR_MONTH_CURVE__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE__NAME = ABSTRACT_YEAR_MONTH_CURVE__NAME;

	/**
	 * The feature id for the '<em><b>Points</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE__POINTS = ABSTRACT_YEAR_MONTH_CURVE__POINTS;

	/**
	 * The feature id for the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE__CURRENCY_UNIT = ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE__VOLUME_UNIT = ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE__EXPRESSION = ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION;

	/**
	 * The feature id for the '<em><b>Market Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE__MARKET_INDEX = ABSTRACT_YEAR_MONTH_CURVE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Commodity Curve</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE_FEATURE_COUNT = ABSTRACT_YEAR_MONTH_CURVE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CharterCurveImpl <em>Charter Curve</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CharterCurveImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCharterCurve()
	 * @generated
	 */
	int CHARTER_CURVE = 25;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_CURVE__EXTENSIONS = ABSTRACT_YEAR_MONTH_CURVE__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_CURVE__UUID = ABSTRACT_YEAR_MONTH_CURVE__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_CURVE__NAME = ABSTRACT_YEAR_MONTH_CURVE__NAME;

	/**
	 * The feature id for the '<em><b>Points</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_CURVE__POINTS = ABSTRACT_YEAR_MONTH_CURVE__POINTS;

	/**
	 * The feature id for the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_CURVE__CURRENCY_UNIT = ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_CURVE__VOLUME_UNIT = ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_CURVE__EXPRESSION = ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION;

	/**
	 * The number of structural features of the '<em>Charter Curve</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_CURVE_FEATURE_COUNT = ABSTRACT_YEAR_MONTH_CURVE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.BunkerFuelCurveImpl <em>Bunker Fuel Curve</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.BunkerFuelCurveImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getBunkerFuelCurve()
	 * @generated
	 */
	int BUNKER_FUEL_CURVE = 26;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNKER_FUEL_CURVE__EXTENSIONS = ABSTRACT_YEAR_MONTH_CURVE__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNKER_FUEL_CURVE__UUID = ABSTRACT_YEAR_MONTH_CURVE__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNKER_FUEL_CURVE__NAME = ABSTRACT_YEAR_MONTH_CURVE__NAME;

	/**
	 * The feature id for the '<em><b>Points</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNKER_FUEL_CURVE__POINTS = ABSTRACT_YEAR_MONTH_CURVE__POINTS;

	/**
	 * The feature id for the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNKER_FUEL_CURVE__CURRENCY_UNIT = ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNKER_FUEL_CURVE__VOLUME_UNIT = ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNKER_FUEL_CURVE__EXPRESSION = ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION;

	/**
	 * The number of structural features of the '<em>Bunker Fuel Curve</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNKER_FUEL_CURVE_FEATURE_COUNT = ABSTRACT_YEAR_MONTH_CURVE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CurrencyCurveImpl <em>Currency Curve</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CurrencyCurveImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCurrencyCurve()
	 * @generated
	 */
	int CURRENCY_CURVE = 27;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_CURVE__EXTENSIONS = ABSTRACT_YEAR_MONTH_CURVE__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_CURVE__UUID = ABSTRACT_YEAR_MONTH_CURVE__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_CURVE__NAME = ABSTRACT_YEAR_MONTH_CURVE__NAME;

	/**
	 * The feature id for the '<em><b>Points</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_CURVE__POINTS = ABSTRACT_YEAR_MONTH_CURVE__POINTS;

	/**
	 * The feature id for the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_CURVE__CURRENCY_UNIT = ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_CURVE__VOLUME_UNIT = ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_CURVE__EXPRESSION = ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION;

	/**
	 * The number of structural features of the '<em>Currency Curve</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_CURVE_FEATURE_COUNT = ABSTRACT_YEAR_MONTH_CURVE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.MarketIndexImpl <em>Market Index</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.MarketIndexImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getMarketIndex()
	 * @generated
	 */
	int MARKET_INDEX = 28;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_INDEX__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_INDEX__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Settle Calendar</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_INDEX__SETTLE_CALENDAR = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Market Index</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_INDEX_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.HolidayCalendarEntryImpl <em>Holiday Calendar Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.HolidayCalendarEntryImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getHolidayCalendarEntry()
	 * @generated
	 */
	int HOLIDAY_CALENDAR_ENTRY = 29;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLIDAY_CALENDAR_ENTRY__DATE = 0;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLIDAY_CALENDAR_ENTRY__COMMENT = 1;

	/**
	 * The number of structural features of the '<em>Holiday Calendar Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLIDAY_CALENDAR_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.HolidayCalendarImpl <em>Holiday Calendar</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.HolidayCalendarImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getHolidayCalendar()
	 * @generated
	 */
	int HOLIDAY_CALENDAR = 30;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLIDAY_CALENDAR__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLIDAY_CALENDAR__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLIDAY_CALENDAR__ENTRIES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLIDAY_CALENDAR__DESCRIPTION = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Holiday Calendar</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOLIDAY_CALENDAR_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl <em>Settle Strategy</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSettleStrategy()
	 * @generated
	 */
	int SETTLE_STRATEGY = 31;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTLE_STRATEGY__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTLE_STRATEGY__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Day Of The Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTLE_STRATEGY__DAY_OF_THE_MONTH = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Last Day Of The Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Use Calendar Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTLE_STRATEGY__USE_CALENDAR_MONTH = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Settle Period</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTLE_STRATEGY__SETTLE_PERIOD = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Settle Period Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTLE_STRATEGY__SETTLE_PERIOD_UNIT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Settle Start Months Prior</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTLE_STRATEGY__SETTLE_START_MONTHS_PRIOR = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Settle Strategy</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTLE_STRATEGY_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 6;

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getCurrencyCurves <em>Currency Curves</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Currency Curves</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getCurrencyCurves()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_CurrencyCurves();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getCommodityCurves <em>Commodity Curves</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Commodity Curves</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getCommodityCurves()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_CommodityCurves();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getCharterCurves <em>Charter Curves</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Charter Curves</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getCharterCurves()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_CharterCurves();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getBunkerFuelCurves <em>Bunker Fuel Curves</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Bunker Fuel Curves</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getBunkerFuelCurves()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_BunkerFuelCurves();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getConversionFactors <em>Conversion Factors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Conversion Factors</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getConversionFactors()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_ConversionFactors();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PricingModel#getMarketCurveDataVersion <em>Market Curve Data Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Market Curve Data Version</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getMarketCurveDataVersion()
	 * @see #getPricingModel()
	 * @generated
	 */
	EAttribute getPricingModel_MarketCurveDataVersion();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getSettledPrices <em>Settled Prices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Settled Prices</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getSettledPrices()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_SettledPrices();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getMarketIndices <em>Market Indices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Market Indices</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getMarketIndices()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_MarketIndices();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getHolidayCalendars <em>Holiday Calendars</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Holiday Calendars</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getHolidayCalendars()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_HolidayCalendars();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getSettleStrategies <em>Settle Strategies</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Settle Strategies</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getSettleStrategies()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_SettleStrategies();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.RouteCost <em>Route Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Route Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.RouteCost
	 * @generated
	 */
	EClass getRouteCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.RouteCost#getRouteOption <em>Route Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route Option</em>'.
	 * @see com.mmxlabs.models.lng.pricing.RouteCost#getRouteOption()
	 * @see #getRouteCost()
	 * @generated
	 */
	EAttribute getRouteCost_RouteOption();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.RouteCost#getVessels <em>Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessels</em>'.
	 * @see com.mmxlabs.models.lng.pricing.RouteCost#getVessels()
	 * @see #getRouteCost()
	 * @generated
	 */
	EReference getRouteCost_Vessels();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see com.mmxlabs.models.lng.pricing.BaseFuelCost#getExpression()
	 * @see #getBaseFuelCost()
	 * @generated
	 */
	EAttribute getBaseFuelCost_Expression();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.CooldownPrice#isLumpsum <em>Lumpsum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lumpsum</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CooldownPrice#isLumpsum()
	 * @see #getCooldownPrice()
	 * @generated
	 */
	EAttribute getCooldownPrice_Lumpsum();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.CostModel <em>Cost Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cost Model</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CostModel
	 * @generated
	 */
	EClass getCostModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.CostModel#getRouteCosts <em>Route Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Route Costs</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CostModel#getRouteCosts()
	 * @see #getCostModel()
	 * @generated
	 */
	EReference getCostModel_RouteCosts();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.CostModel#getPortCosts <em>Port Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Port Costs</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CostModel#getPortCosts()
	 * @see #getCostModel()
	 * @generated
	 */
	EReference getCostModel_PortCosts();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.CostModel#getCooldownCosts <em>Cooldown Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cooldown Costs</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CostModel#getCooldownCosts()
	 * @see #getCostModel()
	 * @generated
	 */
	EReference getCostModel_CooldownCosts();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.CostModel#getBaseFuelCosts <em>Base Fuel Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Base Fuel Costs</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CostModel#getBaseFuelCosts()
	 * @see #getCostModel()
	 * @generated
	 */
	EReference getCostModel_BaseFuelCosts();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.pricing.CostModel#getPanamaCanalTariff <em>Panama Canal Tariff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Panama Canal Tariff</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CostModel#getPanamaCanalTariff()
	 * @see #getCostModel()
	 * @generated
	 */
	EReference getCostModel_PanamaCanalTariff();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.pricing.CostModel#getSuezCanalTariff <em>Suez Canal Tariff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Suez Canal Tariff</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CostModel#getSuezCanalTariff()
	 * @see #getCostModel()
	 * @generated
	 */
	EReference getCostModel_SuezCanalTariff();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.PortsExpressionMap <em>Ports Expression Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ports Expression Map</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsExpressionMap
	 * @generated
	 */
	EClass getPortsExpressionMap();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.PortsExpressionMap#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ports</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsExpressionMap#getPorts()
	 * @see #getPortsExpressionMap()
	 * @generated
	 */
	EReference getPortsExpressionMap_Ports();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PortsExpressionMap#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsExpressionMap#getExpression()
	 * @see #getPortsExpressionMap()
	 * @generated
	 */
	EAttribute getPortsExpressionMap_Expression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap <em>Ports Split Expression Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ports Split Expression Map</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap
	 * @generated
	 */
	EClass getPortsSplitExpressionMap();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ports</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap#getPorts()
	 * @see #getPortsSplitExpressionMap()
	 * @generated
	 */
	EReference getPortsSplitExpressionMap_Ports();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap#getExpression1 <em>Expression1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression1</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap#getExpression1()
	 * @see #getPortsSplitExpressionMap()
	 * @generated
	 */
	EAttribute getPortsSplitExpressionMap_Expression1();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap#getExpression2 <em>Expression2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression2</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap#getExpression2()
	 * @see #getPortsSplitExpressionMap()
	 * @generated
	 */
	EAttribute getPortsSplitExpressionMap_Expression2();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff <em>Panama Canal Tariff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Panama Canal Tariff</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariff
	 * @generated
	 */
	EClass getPanamaCanalTariff();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getBands <em>Bands</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Bands</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getBands()
	 * @see #getPanamaCanalTariff()
	 * @generated
	 */
	EReference getPanamaCanalTariff_Bands();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getMarkupRate <em>Markup Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Markup Rate</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getMarkupRate()
	 * @see #getPanamaCanalTariff()
	 * @generated
	 */
	EAttribute getPanamaCanalTariff_MarkupRate();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand <em>Panama Canal Tariff Band</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Panama Canal Tariff Band</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand
	 * @generated
	 */
	EClass getPanamaCanalTariffBand();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getLadenTariff <em>Laden Tariff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden Tariff</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getLadenTariff()
	 * @see #getPanamaCanalTariffBand()
	 * @generated
	 */
	EAttribute getPanamaCanalTariffBand_LadenTariff();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBallastTariff <em>Ballast Tariff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Tariff</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBallastTariff()
	 * @see #getPanamaCanalTariffBand()
	 * @generated
	 */
	EAttribute getPanamaCanalTariffBand_BallastTariff();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBallastRoundtripTariff <em>Ballast Roundtrip Tariff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Roundtrip Tariff</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBallastRoundtripTariff()
	 * @see #getPanamaCanalTariffBand()
	 * @generated
	 */
	EAttribute getPanamaCanalTariffBand_BallastRoundtripTariff();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandStart <em>Band Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Band Start</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandStart()
	 * @see #getPanamaCanalTariffBand()
	 * @generated
	 */
	EAttribute getPanamaCanalTariffBand_BandStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandEnd <em>Band End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Band End</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandEnd()
	 * @see #getPanamaCanalTariffBand()
	 * @generated
	 */
	EAttribute getPanamaCanalTariffBand_BandEnd();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.SuezCanalTugBand <em>Suez Canal Tug Band</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Suez Canal Tug Band</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTugBand
	 * @generated
	 */
	EClass getSuezCanalTugBand();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTugBand#getTugs <em>Tugs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Tugs</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTugBand#getTugs()
	 * @see #getSuezCanalTugBand()
	 * @generated
	 */
	EAttribute getSuezCanalTugBand_Tugs();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTugBand#getBandStart <em>Band Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Band Start</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTugBand#getBandStart()
	 * @see #getSuezCanalTugBand()
	 * @generated
	 */
	EAttribute getSuezCanalTugBand_BandStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTugBand#getBandEnd <em>Band End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Band End</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTugBand#getBandEnd()
	 * @see #getSuezCanalTugBand()
	 * @generated
	 */
	EAttribute getSuezCanalTugBand_BandEnd();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff <em>Suez Canal Tariff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Suez Canal Tariff</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariff
	 * @generated
	 */
	EClass getSuezCanalTariff();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getBands <em>Bands</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Bands</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariff#getBands()
	 * @see #getSuezCanalTariff()
	 * @generated
	 */
	EReference getSuezCanalTariff_Bands();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getTugBands <em>Tug Bands</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tug Bands</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariff#getTugBands()
	 * @see #getSuezCanalTariff()
	 * @generated
	 */
	EReference getSuezCanalTariff_TugBands();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getTugCost <em>Tug Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Tug Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariff#getTugCost()
	 * @see #getSuezCanalTariff()
	 * @generated
	 */
	EAttribute getSuezCanalTariff_TugCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getFixedCosts <em>Fixed Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fixed Costs</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariff#getFixedCosts()
	 * @see #getSuezCanalTariff()
	 * @generated
	 */
	EAttribute getSuezCanalTariff_FixedCosts();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getDiscountFactor <em>Discount Factor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discount Factor</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariff#getDiscountFactor()
	 * @see #getSuezCanalTariff()
	 * @generated
	 */
	EAttribute getSuezCanalTariff_DiscountFactor();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getSdrToUSD <em>Sdr To USD</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sdr To USD</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariff#getSdrToUSD()
	 * @see #getSuezCanalTariff()
	 * @generated
	 */
	EAttribute getSuezCanalTariff_SdrToUSD();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariffBand <em>Suez Canal Tariff Band</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Suez Canal Tariff Band</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariffBand
	 * @generated
	 */
	EClass getSuezCanalTariffBand();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariffBand#getLadenTariff <em>Laden Tariff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden Tariff</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariffBand#getLadenTariff()
	 * @see #getSuezCanalTariffBand()
	 * @generated
	 */
	EAttribute getSuezCanalTariffBand_LadenTariff();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariffBand#getBallastTariff <em>Ballast Tariff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Tariff</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariffBand#getBallastTariff()
	 * @see #getSuezCanalTariffBand()
	 * @generated
	 */
	EAttribute getSuezCanalTariffBand_BallastTariff();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariffBand#getBandStart <em>Band Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Band Start</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariffBand#getBandStart()
	 * @see #getSuezCanalTariffBand()
	 * @generated
	 */
	EAttribute getSuezCanalTariffBand_BandStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariffBand#getBandEnd <em>Band End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Band End</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariffBand#getBandEnd()
	 * @see #getSuezCanalTariffBand()
	 * @generated
	 */
	EAttribute getSuezCanalTariffBand_BandEnd();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.UnitConversion <em>Unit Conversion</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unit Conversion</em>'.
	 * @see com.mmxlabs.models.lng.pricing.UnitConversion
	 * @generated
	 */
	EClass getUnitConversion();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.UnitConversion#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>From</em>'.
	 * @see com.mmxlabs.models.lng.pricing.UnitConversion#getFrom()
	 * @see #getUnitConversion()
	 * @generated
	 */
	EAttribute getUnitConversion_From();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.UnitConversion#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>To</em>'.
	 * @see com.mmxlabs.models.lng.pricing.UnitConversion#getTo()
	 * @see #getUnitConversion()
	 * @generated
	 */
	EAttribute getUnitConversion_To();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.UnitConversion#getFactor <em>Factor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Factor</em>'.
	 * @see com.mmxlabs.models.lng.pricing.UnitConversion#getFactor()
	 * @see #getUnitConversion()
	 * @generated
	 */
	EAttribute getUnitConversion_Factor();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.DatePointContainer <em>Date Point Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Date Point Container</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DatePointContainer
	 * @generated
	 */
	EClass getDatePointContainer();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.DatePointContainer#getPoints <em>Points</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Points</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DatePointContainer#getPoints()
	 * @see #getDatePointContainer()
	 * @generated
	 */
	EReference getDatePointContainer_Points();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.DatePoint <em>Date Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Date Point</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DatePoint
	 * @generated
	 */
	EClass getDatePoint();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.DatePoint#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DatePoint#getDate()
	 * @see #getDatePoint()
	 * @generated
	 */
	EAttribute getDatePoint_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.DatePoint#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see com.mmxlabs.models.lng.pricing.DatePoint#getValue()
	 * @see #getDatePoint()
	 * @generated
	 */
	EAttribute getDatePoint_Value();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.YearMonthPointContainer <em>Year Month Point Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Year Month Point Container</em>'.
	 * @see com.mmxlabs.models.lng.pricing.YearMonthPointContainer
	 * @generated
	 */
	EClass getYearMonthPointContainer();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.YearMonthPointContainer#getPoints <em>Points</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Points</em>'.
	 * @see com.mmxlabs.models.lng.pricing.YearMonthPointContainer#getPoints()
	 * @see #getYearMonthPointContainer()
	 * @generated
	 */
	EReference getYearMonthPointContainer_Points();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.YearMonthPoint <em>Year Month Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Year Month Point</em>'.
	 * @see com.mmxlabs.models.lng.pricing.YearMonthPoint
	 * @generated
	 */
	EClass getYearMonthPoint();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.YearMonthPoint#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.pricing.YearMonthPoint#getDate()
	 * @see #getYearMonthPoint()
	 * @generated
	 */
	EAttribute getYearMonthPoint_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.YearMonthPoint#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see com.mmxlabs.models.lng.pricing.YearMonthPoint#getValue()
	 * @see #getYearMonthPoint()
	 * @generated
	 */
	EAttribute getYearMonthPoint_Value();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve <em>Abstract Year Month Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Year Month Curve</em>'.
	 * @see com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve
	 * @generated
	 */
	EClass getAbstractYearMonthCurve();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getCurrencyUnit <em>Currency Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Currency Unit</em>'.
	 * @see com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getCurrencyUnit()
	 * @see #getAbstractYearMonthCurve()
	 * @generated
	 */
	EAttribute getAbstractYearMonthCurve_CurrencyUnit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getVolumeUnit <em>Volume Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Unit</em>'.
	 * @see com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getVolumeUnit()
	 * @see #getAbstractYearMonthCurve()
	 * @generated
	 */
	EAttribute getAbstractYearMonthCurve_VolumeUnit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve#getExpression()
	 * @see #getAbstractYearMonthCurve()
	 * @generated
	 */
	EAttribute getAbstractYearMonthCurve_Expression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.CommodityCurve <em>Commodity Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Commodity Curve</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CommodityCurve
	 * @generated
	 */
	EClass getCommodityCurve();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.CommodityCurve#getMarketIndex <em>Market Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CommodityCurve#getMarketIndex()
	 * @see #getCommodityCurve()
	 * @generated
	 */
	EReference getCommodityCurve_MarketIndex();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.CharterCurve <em>Charter Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Charter Curve</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CharterCurve
	 * @generated
	 */
	EClass getCharterCurve();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.BunkerFuelCurve <em>Bunker Fuel Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bunker Fuel Curve</em>'.
	 * @see com.mmxlabs.models.lng.pricing.BunkerFuelCurve
	 * @generated
	 */
	EClass getBunkerFuelCurve();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.CurrencyCurve <em>Currency Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Currency Curve</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CurrencyCurve
	 * @generated
	 */
	EClass getCurrencyCurve();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.MarketIndex <em>Market Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Market Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.MarketIndex
	 * @generated
	 */
	EClass getMarketIndex();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.MarketIndex#getSettleCalendar <em>Settle Calendar</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Settle Calendar</em>'.
	 * @see com.mmxlabs.models.lng.pricing.MarketIndex#getSettleCalendar()
	 * @see #getMarketIndex()
	 * @generated
	 */
	EReference getMarketIndex_SettleCalendar();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.HolidayCalendarEntry <em>Holiday Calendar Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Holiday Calendar Entry</em>'.
	 * @see com.mmxlabs.models.lng.pricing.HolidayCalendarEntry
	 * @generated
	 */
	EClass getHolidayCalendarEntry();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.HolidayCalendarEntry#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.pricing.HolidayCalendarEntry#getDate()
	 * @see #getHolidayCalendarEntry()
	 * @generated
	 */
	EAttribute getHolidayCalendarEntry_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.HolidayCalendarEntry#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see com.mmxlabs.models.lng.pricing.HolidayCalendarEntry#getComment()
	 * @see #getHolidayCalendarEntry()
	 * @generated
	 */
	EAttribute getHolidayCalendarEntry_Comment();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.HolidayCalendar <em>Holiday Calendar</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Holiday Calendar</em>'.
	 * @see com.mmxlabs.models.lng.pricing.HolidayCalendar
	 * @generated
	 */
	EClass getHolidayCalendar();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.HolidayCalendar#getEntries <em>Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entries</em>'.
	 * @see com.mmxlabs.models.lng.pricing.HolidayCalendar#getEntries()
	 * @see #getHolidayCalendar()
	 * @generated
	 */
	EReference getHolidayCalendar_Entries();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.HolidayCalendar#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see com.mmxlabs.models.lng.pricing.HolidayCalendar#getDescription()
	 * @see #getHolidayCalendar()
	 * @generated
	 */
	EAttribute getHolidayCalendar_Description();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.SettleStrategy <em>Settle Strategy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Settle Strategy</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SettleStrategy
	 * @generated
	 */
	EClass getSettleStrategy();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getDayOfTheMonth <em>Day Of The Month</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Day Of The Month</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SettleStrategy#getDayOfTheMonth()
	 * @see #getSettleStrategy()
	 * @generated
	 */
	EAttribute getSettleStrategy_DayOfTheMonth();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#isLastDayOfTheMonth <em>Last Day Of The Month</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Day Of The Month</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SettleStrategy#isLastDayOfTheMonth()
	 * @see #getSettleStrategy()
	 * @generated
	 */
	EAttribute getSettleStrategy_LastDayOfTheMonth();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#isUseCalendarMonth <em>Use Calendar Month</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Calendar Month</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SettleStrategy#isUseCalendarMonth()
	 * @see #getSettleStrategy()
	 * @generated
	 */
	EAttribute getSettleStrategy_UseCalendarMonth();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getSettlePeriod <em>Settle Period</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Settle Period</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SettleStrategy#getSettlePeriod()
	 * @see #getSettleStrategy()
	 * @generated
	 */
	EAttribute getSettleStrategy_SettlePeriod();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getSettlePeriodUnit <em>Settle Period Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Settle Period Unit</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SettleStrategy#getSettlePeriodUnit()
	 * @see #getSettleStrategy()
	 * @generated
	 */
	EAttribute getSettleStrategy_SettlePeriodUnit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getSettleStartMonthsPrior <em>Settle Start Months Prior</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Settle Start Months Prior</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SettleStrategy#getSettleStartMonthsPrior()
	 * @see #getSettleStrategy()
	 * @generated
	 */
	EAttribute getSettleStrategy_SettleStartMonthsPrior();

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
		 * The meta object literal for the '<em><b>Currency Curves</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__CURRENCY_CURVES = eINSTANCE.getPricingModel_CurrencyCurves();

		/**
		 * The meta object literal for the '<em><b>Commodity Curves</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__COMMODITY_CURVES = eINSTANCE.getPricingModel_CommodityCurves();

		/**
		 * The meta object literal for the '<em><b>Charter Curves</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__CHARTER_CURVES = eINSTANCE.getPricingModel_CharterCurves();

		/**
		 * The meta object literal for the '<em><b>Bunker Fuel Curves</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__BUNKER_FUEL_CURVES = eINSTANCE.getPricingModel_BunkerFuelCurves();

		/**
		 * The meta object literal for the '<em><b>Conversion Factors</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__CONVERSION_FACTORS = eINSTANCE.getPricingModel_ConversionFactors();

		/**
		 * The meta object literal for the '<em><b>Market Curve Data Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRICING_MODEL__MARKET_CURVE_DATA_VERSION = eINSTANCE.getPricingModel_MarketCurveDataVersion();

		/**
		 * The meta object literal for the '<em><b>Settled Prices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__SETTLED_PRICES = eINSTANCE.getPricingModel_SettledPrices();

		/**
		 * The meta object literal for the '<em><b>Market Indices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__MARKET_INDICES = eINSTANCE.getPricingModel_MarketIndices();

		/**
		 * The meta object literal for the '<em><b>Holiday Calendars</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__HOLIDAY_CALENDARS = eINSTANCE.getPricingModel_HolidayCalendars();

		/**
		 * The meta object literal for the '<em><b>Settle Strategies</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__SETTLE_STRATEGIES = eINSTANCE.getPricingModel_SettleStrategies();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.RouteCostImpl <em>Route Cost</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.RouteCostImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getRouteCost()
		 * @generated
		 */
		EClass ROUTE_COST = eINSTANCE.getRouteCost();

		/**
		 * The meta object literal for the '<em><b>Route Option</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_COST__ROUTE_OPTION = eINSTANCE.getRouteCost_RouteOption();

		/**
		 * The meta object literal for the '<em><b>Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTE_COST__VESSELS = eINSTANCE.getRouteCost_Vessels();

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
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_FUEL_COST__EXPRESSION = eINSTANCE.getBaseFuelCost_Expression();

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
		 * The meta object literal for the '<em><b>Lumpsum</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COOLDOWN_PRICE__LUMPSUM = eINSTANCE.getCooldownPrice_Lumpsum();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.CostModelImpl <em>Cost Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.CostModelImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCostModel()
		 * @generated
		 */
		EClass COST_MODEL = eINSTANCE.getCostModel();

		/**
		 * The meta object literal for the '<em><b>Route Costs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COST_MODEL__ROUTE_COSTS = eINSTANCE.getCostModel_RouteCosts();

		/**
		 * The meta object literal for the '<em><b>Port Costs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COST_MODEL__PORT_COSTS = eINSTANCE.getCostModel_PortCosts();

		/**
		 * The meta object literal for the '<em><b>Cooldown Costs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COST_MODEL__COOLDOWN_COSTS = eINSTANCE.getCostModel_CooldownCosts();

		/**
		 * The meta object literal for the '<em><b>Base Fuel Costs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COST_MODEL__BASE_FUEL_COSTS = eINSTANCE.getCostModel_BaseFuelCosts();

		/**
		 * The meta object literal for the '<em><b>Panama Canal Tariff</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COST_MODEL__PANAMA_CANAL_TARIFF = eINSTANCE.getCostModel_PanamaCanalTariff();

		/**
		 * The meta object literal for the '<em><b>Suez Canal Tariff</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COST_MODEL__SUEZ_CANAL_TARIFF = eINSTANCE.getCostModel_SuezCanalTariff();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsExpressionMapImpl <em>Ports Expression Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.PortsExpressionMapImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsExpressionMap()
		 * @generated
		 */
		EClass PORTS_EXPRESSION_MAP = eINSTANCE.getPortsExpressionMap();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORTS_EXPRESSION_MAP__PORTS = eINSTANCE.getPortsExpressionMap_Ports();

		/**
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORTS_EXPRESSION_MAP__EXPRESSION = eINSTANCE.getPortsExpressionMap_Expression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsSplitExpressionMapImpl <em>Ports Split Expression Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.PortsSplitExpressionMapImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsSplitExpressionMap()
		 * @generated
		 */
		EClass PORTS_SPLIT_EXPRESSION_MAP = eINSTANCE.getPortsSplitExpressionMap();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORTS_SPLIT_EXPRESSION_MAP__PORTS = eINSTANCE.getPortsSplitExpressionMap_Ports();

		/**
		 * The meta object literal for the '<em><b>Expression1</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORTS_SPLIT_EXPRESSION_MAP__EXPRESSION1 = eINSTANCE.getPortsSplitExpressionMap_Expression1();

		/**
		 * The meta object literal for the '<em><b>Expression2</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORTS_SPLIT_EXPRESSION_MAP__EXPRESSION2 = eINSTANCE.getPortsSplitExpressionMap_Expression2();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffImpl <em>Panama Canal Tariff</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPanamaCanalTariff()
		 * @generated
		 */
		EClass PANAMA_CANAL_TARIFF = eINSTANCE.getPanamaCanalTariff();

		/**
		 * The meta object literal for the '<em><b>Bands</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PANAMA_CANAL_TARIFF__BANDS = eINSTANCE.getPanamaCanalTariff_Bands();

		/**
		 * The meta object literal for the '<em><b>Markup Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PANAMA_CANAL_TARIFF__MARKUP_RATE = eINSTANCE.getPanamaCanalTariff_MarkupRate();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffBandImpl <em>Panama Canal Tariff Band</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffBandImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPanamaCanalTariffBand()
		 * @generated
		 */
		EClass PANAMA_CANAL_TARIFF_BAND = eINSTANCE.getPanamaCanalTariffBand();

		/**
		 * The meta object literal for the '<em><b>Laden Tariff</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF = eINSTANCE.getPanamaCanalTariffBand_LadenTariff();

		/**
		 * The meta object literal for the '<em><b>Ballast Tariff</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF = eINSTANCE.getPanamaCanalTariffBand_BallastTariff();

		/**
		 * The meta object literal for the '<em><b>Ballast Roundtrip Tariff</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF = eINSTANCE.getPanamaCanalTariffBand_BallastRoundtripTariff();

		/**
		 * The meta object literal for the '<em><b>Band Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PANAMA_CANAL_TARIFF_BAND__BAND_START = eINSTANCE.getPanamaCanalTariffBand_BandStart();

		/**
		 * The meta object literal for the '<em><b>Band End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PANAMA_CANAL_TARIFF_BAND__BAND_END = eINSTANCE.getPanamaCanalTariffBand_BandEnd();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTugBandImpl <em>Suez Canal Tug Band</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.SuezCanalTugBandImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSuezCanalTugBand()
		 * @generated
		 */
		EClass SUEZ_CANAL_TUG_BAND = eINSTANCE.getSuezCanalTugBand();

		/**
		 * The meta object literal for the '<em><b>Tugs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TUG_BAND__TUGS = eINSTANCE.getSuezCanalTugBand_Tugs();

		/**
		 * The meta object literal for the '<em><b>Band Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TUG_BAND__BAND_START = eINSTANCE.getSuezCanalTugBand_BandStart();

		/**
		 * The meta object literal for the '<em><b>Band End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TUG_BAND__BAND_END = eINSTANCE.getSuezCanalTugBand_BandEnd();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl <em>Suez Canal Tariff</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSuezCanalTariff()
		 * @generated
		 */
		EClass SUEZ_CANAL_TARIFF = eINSTANCE.getSuezCanalTariff();

		/**
		 * The meta object literal for the '<em><b>Bands</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUEZ_CANAL_TARIFF__BANDS = eINSTANCE.getSuezCanalTariff_Bands();

		/**
		 * The meta object literal for the '<em><b>Tug Bands</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUEZ_CANAL_TARIFF__TUG_BANDS = eINSTANCE.getSuezCanalTariff_TugBands();

		/**
		 * The meta object literal for the '<em><b>Tug Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TARIFF__TUG_COST = eINSTANCE.getSuezCanalTariff_TugCost();

		/**
		 * The meta object literal for the '<em><b>Fixed Costs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TARIFF__FIXED_COSTS = eINSTANCE.getSuezCanalTariff_FixedCosts();

		/**
		 * The meta object literal for the '<em><b>Discount Factor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR = eINSTANCE.getSuezCanalTariff_DiscountFactor();

		/**
		 * The meta object literal for the '<em><b>Sdr To USD</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TARIFF__SDR_TO_USD = eINSTANCE.getSuezCanalTariff_SdrToUSD();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffBandImpl <em>Suez Canal Tariff Band</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffBandImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSuezCanalTariffBand()
		 * @generated
		 */
		EClass SUEZ_CANAL_TARIFF_BAND = eINSTANCE.getSuezCanalTariffBand();

		/**
		 * The meta object literal for the '<em><b>Laden Tariff</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TARIFF_BAND__LADEN_TARIFF = eINSTANCE.getSuezCanalTariffBand_LadenTariff();

		/**
		 * The meta object literal for the '<em><b>Ballast Tariff</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TARIFF_BAND__BALLAST_TARIFF = eINSTANCE.getSuezCanalTariffBand_BallastTariff();

		/**
		 * The meta object literal for the '<em><b>Band Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TARIFF_BAND__BAND_START = eINSTANCE.getSuezCanalTariffBand_BandStart();

		/**
		 * The meta object literal for the '<em><b>Band End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TARIFF_BAND__BAND_END = eINSTANCE.getSuezCanalTariffBand_BandEnd();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.UnitConversionImpl <em>Unit Conversion</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.UnitConversionImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getUnitConversion()
		 * @generated
		 */
		EClass UNIT_CONVERSION = eINSTANCE.getUnitConversion();

		/**
		 * The meta object literal for the '<em><b>From</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_CONVERSION__FROM = eINSTANCE.getUnitConversion_From();

		/**
		 * The meta object literal for the '<em><b>To</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_CONVERSION__TO = eINSTANCE.getUnitConversion_To();

		/**
		 * The meta object literal for the '<em><b>Factor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNIT_CONVERSION__FACTOR = eINSTANCE.getUnitConversion_Factor();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.DatePointContainerImpl <em>Date Point Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.DatePointContainerImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDatePointContainer()
		 * @generated
		 */
		EClass DATE_POINT_CONTAINER = eINSTANCE.getDatePointContainer();

		/**
		 * The meta object literal for the '<em><b>Points</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATE_POINT_CONTAINER__POINTS = eINSTANCE.getDatePointContainer_Points();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.DatePointImpl <em>Date Point</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.DatePointImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getDatePoint()
		 * @generated
		 */
		EClass DATE_POINT = eINSTANCE.getDatePoint();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATE_POINT__DATE = eINSTANCE.getDatePoint_Date();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATE_POINT__VALUE = eINSTANCE.getDatePoint_Value();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.YearMonthPointContainerImpl <em>Year Month Point Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.YearMonthPointContainerImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getYearMonthPointContainer()
		 * @generated
		 */
		EClass YEAR_MONTH_POINT_CONTAINER = eINSTANCE.getYearMonthPointContainer();

		/**
		 * The meta object literal for the '<em><b>Points</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference YEAR_MONTH_POINT_CONTAINER__POINTS = eINSTANCE.getYearMonthPointContainer_Points();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.YearMonthPointImpl <em>Year Month Point</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.YearMonthPointImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getYearMonthPoint()
		 * @generated
		 */
		EClass YEAR_MONTH_POINT = eINSTANCE.getYearMonthPoint();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute YEAR_MONTH_POINT__DATE = eINSTANCE.getYearMonthPoint_Date();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute YEAR_MONTH_POINT__VALUE = eINSTANCE.getYearMonthPoint_Value();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.AbstractYearMonthCurveImpl <em>Abstract Year Month Curve</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.AbstractYearMonthCurveImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getAbstractYearMonthCurve()
		 * @generated
		 */
		EClass ABSTRACT_YEAR_MONTH_CURVE = eINSTANCE.getAbstractYearMonthCurve();

		/**
		 * The meta object literal for the '<em><b>Currency Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT = eINSTANCE.getAbstractYearMonthCurve_CurrencyUnit();

		/**
		 * The meta object literal for the '<em><b>Volume Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT = eINSTANCE.getAbstractYearMonthCurve_VolumeUnit();

		/**
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION = eINSTANCE.getAbstractYearMonthCurve_Expression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.CommodityCurveImpl <em>Commodity Curve</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.CommodityCurveImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCommodityCurve()
		 * @generated
		 */
		EClass COMMODITY_CURVE = eINSTANCE.getCommodityCurve();

		/**
		 * The meta object literal for the '<em><b>Market Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMODITY_CURVE__MARKET_INDEX = eINSTANCE.getCommodityCurve_MarketIndex();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.CharterCurveImpl <em>Charter Curve</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.CharterCurveImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCharterCurve()
		 * @generated
		 */
		EClass CHARTER_CURVE = eINSTANCE.getCharterCurve();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.BunkerFuelCurveImpl <em>Bunker Fuel Curve</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.BunkerFuelCurveImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getBunkerFuelCurve()
		 * @generated
		 */
		EClass BUNKER_FUEL_CURVE = eINSTANCE.getBunkerFuelCurve();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.CurrencyCurveImpl <em>Currency Curve</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.CurrencyCurveImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCurrencyCurve()
		 * @generated
		 */
		EClass CURRENCY_CURVE = eINSTANCE.getCurrencyCurve();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.MarketIndexImpl <em>Market Index</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.MarketIndexImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getMarketIndex()
		 * @generated
		 */
		EClass MARKET_INDEX = eINSTANCE.getMarketIndex();

		/**
		 * The meta object literal for the '<em><b>Settle Calendar</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKET_INDEX__SETTLE_CALENDAR = eINSTANCE.getMarketIndex_SettleCalendar();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.HolidayCalendarEntryImpl <em>Holiday Calendar Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.HolidayCalendarEntryImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getHolidayCalendarEntry()
		 * @generated
		 */
		EClass HOLIDAY_CALENDAR_ENTRY = eINSTANCE.getHolidayCalendarEntry();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HOLIDAY_CALENDAR_ENTRY__DATE = eINSTANCE.getHolidayCalendarEntry_Date();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HOLIDAY_CALENDAR_ENTRY__COMMENT = eINSTANCE.getHolidayCalendarEntry_Comment();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.HolidayCalendarImpl <em>Holiday Calendar</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.HolidayCalendarImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getHolidayCalendar()
		 * @generated
		 */
		EClass HOLIDAY_CALENDAR = eINSTANCE.getHolidayCalendar();

		/**
		 * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HOLIDAY_CALENDAR__ENTRIES = eINSTANCE.getHolidayCalendar_Entries();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HOLIDAY_CALENDAR__DESCRIPTION = eINSTANCE.getHolidayCalendar_Description();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl <em>Settle Strategy</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSettleStrategy()
		 * @generated
		 */
		EClass SETTLE_STRATEGY = eINSTANCE.getSettleStrategy();

		/**
		 * The meta object literal for the '<em><b>Day Of The Month</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SETTLE_STRATEGY__DAY_OF_THE_MONTH = eINSTANCE.getSettleStrategy_DayOfTheMonth();

		/**
		 * The meta object literal for the '<em><b>Last Day Of The Month</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH = eINSTANCE.getSettleStrategy_LastDayOfTheMonth();

		/**
		 * The meta object literal for the '<em><b>Use Calendar Month</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SETTLE_STRATEGY__USE_CALENDAR_MONTH = eINSTANCE.getSettleStrategy_UseCalendarMonth();

		/**
		 * The meta object literal for the '<em><b>Settle Period</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SETTLE_STRATEGY__SETTLE_PERIOD = eINSTANCE.getSettleStrategy_SettlePeriod();

		/**
		 * The meta object literal for the '<em><b>Settle Period Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SETTLE_STRATEGY__SETTLE_PERIOD_UNIT = eINSTANCE.getSettleStrategy_SettlePeriodUnit();

		/**
		 * The meta object literal for the '<em><b>Settle Start Months Prior</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SETTLE_STRATEGY__SETTLE_START_MONTHS_PRIOR = eINSTANCE.getSettleStrategy_SettleStartMonthsPrior();


	}

} //PricingPackage
