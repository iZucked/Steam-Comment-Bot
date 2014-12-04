/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
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
	 * The feature id for the '<em><b>Base Fuel Prices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__BASE_FUEL_PRICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.FleetCostModelImpl <em>Fleet Cost Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.FleetCostModelImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getFleetCostModel()
	 * @generated
	 */
	int FLEET_COST_MODEL = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_COST_MODEL__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Base Fuel Prices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_COST_MODEL__BASE_FUEL_PRICES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Fleet Cost Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_COST_MODEL_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_COST__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl <em>Base Fuel Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getBaseFuelCost()
	 * @generated
	 */
	int BASE_FUEL_COST = 7;

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
	 * The feature id for the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST__INDEX = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Base Fuel Cost</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_COST_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortCostEntryImpl <em>Port Cost Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortCostEntryImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortCostEntry()
	 * @generated
	 */
	int PORT_COST_ENTRY = 9;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsPriceMapImpl <em>Ports Price Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortsPriceMapImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsPriceMap()
	 * @generated
	 */
	int PORTS_PRICE_MAP = 15;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_PRICE_MAP__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_PRICE_MAP__PORTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_PRICE_MAP__INDEX = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Ports Price Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_PRICE_MAP_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__EXTENSIONS = PORTS_PRICE_MAP__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__PORTS = PORTS_PRICE_MAP__PORTS;

	/**
	 * The feature id for the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__INDEX = PORTS_PRICE_MAP__INDEX;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE__EXPRESSION = PORTS_PRICE_MAP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Cooldown Price</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_PRICE_FEATURE_COUNT = PORTS_PRICE_MAP_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CommodityIndexImpl <em>Commodity Index</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CommodityIndexImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCommodityIndex()
	 * @generated
	 */
	int COMMODITY_INDEX = 11;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CharterIndexImpl <em>Charter Index</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CharterIndexImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCharterIndex()
	 * @generated
	 */
	int CHARTER_INDEX = 12;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.NamedIndexContainerImpl <em>Named Index Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.NamedIndexContainerImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getNamedIndexContainer()
	 * @generated
	 */
	int NAMED_INDEX_CONTAINER = 14;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_INDEX_CONTAINER__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_INDEX_CONTAINER__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_INDEX_CONTAINER__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_INDEX_CONTAINER__DATA = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_INDEX_CONTAINER__UNITS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Named Index Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_INDEX_CONTAINER_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_INDEX__EXTENSIONS = NAMED_INDEX_CONTAINER__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_INDEX__UUID = NAMED_INDEX_CONTAINER__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_INDEX__NAME = NAMED_INDEX_CONTAINER__NAME;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_INDEX__DATA = NAMED_INDEX_CONTAINER__DATA;

	/**
	 * The feature id for the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_INDEX__UNITS = NAMED_INDEX_CONTAINER__UNITS;

	/**
	 * The number of structural features of the '<em>Commodity Index</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_INDEX_FEATURE_COUNT = NAMED_INDEX_CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_INDEX__EXTENSIONS = NAMED_INDEX_CONTAINER__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_INDEX__UUID = NAMED_INDEX_CONTAINER__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_INDEX__NAME = NAMED_INDEX_CONTAINER__NAME;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_INDEX__DATA = NAMED_INDEX_CONTAINER__DATA;

	/**
	 * The feature id for the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_INDEX__UNITS = NAMED_INDEX_CONTAINER__UNITS;

	/**
	 * The number of structural features of the '<em>Charter Index</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_INDEX_FEATURE_COUNT = NAMED_INDEX_CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelIndexImpl <em>Base Fuel Index</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.BaseFuelIndexImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getBaseFuelIndex()
	 * @generated
	 */
	int BASE_FUEL_INDEX = 13;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_INDEX__EXTENSIONS = NAMED_INDEX_CONTAINER__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_INDEX__UUID = NAMED_INDEX_CONTAINER__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_INDEX__NAME = NAMED_INDEX_CONTAINER__NAME;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_INDEX__DATA = NAMED_INDEX_CONTAINER__DATA;

	/**
	 * The feature id for the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_INDEX__UNITS = NAMED_INDEX_CONTAINER__UNITS;

	/**
	 * The number of structural features of the '<em>Base Fuel Index</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_INDEX_FEATURE_COUNT = NAMED_INDEX_CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsExpressionMapImpl <em>Ports Expression Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortsExpressionMapImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsExpressionMap()
	 * @generated
	 */
	int PORTS_EXPRESSION_MAP = 16;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsSplitPriceMapImpl <em>Ports Split Price Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortsSplitPriceMapImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsSplitPriceMap()
	 * @generated
	 */
	int PORTS_SPLIT_PRICE_MAP = 17;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_SPLIT_PRICE_MAP__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_SPLIT_PRICE_MAP__PORTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Index H1</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_SPLIT_PRICE_MAP__INDEX_H1 = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Index H2</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_SPLIT_PRICE_MAP__INDEX_H2 = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Ports Split Price Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTS_SPLIT_PRICE_MAP_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsSplitExpressionMapImpl <em>Ports Split Expression Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortsSplitExpressionMapImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsSplitExpressionMap()
	 * @generated
	 */
	int PORTS_SPLIT_EXPRESSION_MAP = 18;

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getBaseFuelPrices <em>Base Fuel Prices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Base Fuel Prices</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getBaseFuelPrices()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_BaseFuelPrices();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.BaseFuelCost#getIndex()
	 * @see #getBaseFuelCost()
	 * @generated
	 */
	EReference getBaseFuelCost_Index();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.CooldownPrice#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CooldownPrice#getExpression()
	 * @see #getCooldownPrice()
	 * @generated
	 */
	EAttribute getCooldownPrice_Expression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.CommodityIndex <em>Commodity Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Commodity Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CommodityIndex
	 * @generated
	 */
	EClass getCommodityIndex();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.CharterIndex <em>Charter Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Charter Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CharterIndex
	 * @generated
	 */
	EClass getCharterIndex();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.BaseFuelIndex <em>Base Fuel Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Fuel Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.BaseFuelIndex
	 * @generated
	 */
	EClass getBaseFuelIndex();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer <em>Named Index Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Index Container</em>'.
	 * @see com.mmxlabs.models.lng.pricing.NamedIndexContainer
	 * @generated
	 */
	EClass getNamedIndexContainer();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getData <em>Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Data</em>'.
	 * @see com.mmxlabs.models.lng.pricing.NamedIndexContainer#getData()
	 * @see #getNamedIndexContainer()
	 * @generated
	 */
	EReference getNamedIndexContainer_Data();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getUnits <em>Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Units</em>'.
	 * @see com.mmxlabs.models.lng.pricing.NamedIndexContainer#getUnits()
	 * @see #getNamedIndexContainer()
	 * @generated
	 */
	EAttribute getNamedIndexContainer_Units();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.PortsPriceMap <em>Ports Price Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ports Price Map</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsPriceMap
	 * @generated
	 */
	EClass getPortsPriceMap();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.PortsPriceMap#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ports</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsPriceMap#getPorts()
	 * @see #getPortsPriceMap()
	 * @generated
	 */
	EReference getPortsPriceMap_Ports();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.PortsPriceMap#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsPriceMap#getIndex()
	 * @see #getPortsPriceMap()
	 * @generated
	 */
	EReference getPortsPriceMap_Index();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.PortsSplitPriceMap <em>Ports Split Price Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ports Split Price Map</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsSplitPriceMap
	 * @generated
	 */
	EClass getPortsSplitPriceMap();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.pricing.PortsSplitPriceMap#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ports</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsSplitPriceMap#getPorts()
	 * @see #getPortsSplitPriceMap()
	 * @generated
	 */
	EReference getPortsSplitPriceMap_Ports();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.PortsSplitPriceMap#getIndexH1 <em>Index H1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Index H1</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsSplitPriceMap#getIndexH1()
	 * @see #getPortsSplitPriceMap()
	 * @generated
	 */
	EReference getPortsSplitPriceMap_IndexH1();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.pricing.PortsSplitPriceMap#getIndexH2 <em>Index H2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Index H2</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PortsSplitPriceMap#getIndexH2()
	 * @see #getPortsSplitPriceMap()
	 * @generated
	 */
	EReference getPortsSplitPriceMap_IndexH2();

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
		 * The meta object literal for the '<em><b>Base Fuel Prices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__BASE_FUEL_PRICES = eINSTANCE.getPricingModel_BaseFuelPrices();

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
		 * The meta object literal for the '<em><b>Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_FUEL_COST__INDEX = eINSTANCE.getBaseFuelCost_Index();

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
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COOLDOWN_PRICE__EXPRESSION = eINSTANCE.getCooldownPrice_Expression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.CommodityIndexImpl <em>Commodity Index</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.CommodityIndexImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCommodityIndex()
		 * @generated
		 */
		EClass COMMODITY_INDEX = eINSTANCE.getCommodityIndex();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.CharterIndexImpl <em>Charter Index</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.CharterIndexImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCharterIndex()
		 * @generated
		 */
		EClass CHARTER_INDEX = eINSTANCE.getCharterIndex();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelIndexImpl <em>Base Fuel Index</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.BaseFuelIndexImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getBaseFuelIndex()
		 * @generated
		 */
		EClass BASE_FUEL_INDEX = eINSTANCE.getBaseFuelIndex();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.NamedIndexContainerImpl <em>Named Index Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.NamedIndexContainerImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getNamedIndexContainer()
		 * @generated
		 */
		EClass NAMED_INDEX_CONTAINER = eINSTANCE.getNamedIndexContainer();

		/**
		 * The meta object literal for the '<em><b>Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NAMED_INDEX_CONTAINER__DATA = eINSTANCE.getNamedIndexContainer_Data();

		/**
		 * The meta object literal for the '<em><b>Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_INDEX_CONTAINER__UNITS = eINSTANCE.getNamedIndexContainer_Units();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsPriceMapImpl <em>Ports Price Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.PortsPriceMapImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsPriceMap()
		 * @generated
		 */
		EClass PORTS_PRICE_MAP = eINSTANCE.getPortsPriceMap();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORTS_PRICE_MAP__PORTS = eINSTANCE.getPortsPriceMap_Ports();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORTS_PRICE_MAP__INDEX = eINSTANCE.getPortsPriceMap_Index();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsSplitPriceMapImpl <em>Ports Split Price Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.PortsSplitPriceMapImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsSplitPriceMap()
		 * @generated
		 */
		EClass PORTS_SPLIT_PRICE_MAP = eINSTANCE.getPortsSplitPriceMap();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORTS_SPLIT_PRICE_MAP__PORTS = eINSTANCE.getPortsSplitPriceMap_Ports();

		/**
		 * The meta object literal for the '<em><b>Index H1</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORTS_SPLIT_PRICE_MAP__INDEX_H1 = eINSTANCE.getPortsSplitPriceMap_IndexH1();

		/**
		 * The meta object literal for the '<em><b>Index H2</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORTS_SPLIT_PRICE_MAP__INDEX_H2 = eINSTANCE.getPortsSplitPriceMap_IndexH2();

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

	}

} //PricingPackage
