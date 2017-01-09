/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
	 * The feature id for the '<em><b>Currency Indices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__CURRENCY_INDICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

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
	 * The feature id for the '<em><b>Base Fuel Prices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__BASE_FUEL_PRICES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Conversion Factors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL__CONVERSION_FACTORS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICING_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

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
	int ROUTE_COST = 11;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl <em>Base Fuel Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getBaseFuelCost()
	 * @generated
	 */
	int BASE_FUEL_COST = 12;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl <em>Port Cost</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortCostImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortCost()
	 * @generated
	 */
	int PORT_COST = 13;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortCostEntryImpl <em>Port Cost Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortCostEntryImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortCostEntry()
	 * @generated
	 */
	int PORT_COST_ENTRY = 14;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsPriceMapImpl <em>Ports Price Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortsPriceMapImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsPriceMap()
	 * @generated
	 */
	int PORTS_PRICE_MAP = 16;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CooldownPriceImpl <em>Cooldown Price</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CooldownPriceImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCooldownPrice()
	 * @generated
	 */
	int COOLDOWN_PRICE = 15;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CommodityIndexImpl <em>Commodity Index</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CommodityIndexImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCommodityIndex()
	 * @generated
	 */
	int COMMODITY_INDEX = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CharterIndexImpl <em>Charter Index</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CharterIndexImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCharterIndex()
	 * @generated
	 */
	int CHARTER_INDEX = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.NamedIndexContainerImpl <em>Named Index Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.NamedIndexContainerImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getNamedIndexContainer()
	 * @generated
	 */
	int NAMED_INDEX_CONTAINER = 9;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelIndexImpl <em>Base Fuel Index</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.BaseFuelIndexImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getBaseFuelIndex()
	 * @generated
	 */
	int BASE_FUEL_INDEX = 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsExpressionMapImpl <em>Ports Expression Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortsExpressionMapImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsExpressionMap()
	 * @generated
	 */
	int PORTS_EXPRESSION_MAP = 17;

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
	 * The feature id for the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_INDEX_CONTAINER__CURRENCY_UNIT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_INDEX_CONTAINER__VOLUME_UNIT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Named Index Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_INDEX_CONTAINER_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CurrencyIndexImpl <em>Currency Index</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CurrencyIndexImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCurrencyIndex()
	 * @generated
	 */
	int CURRENCY_INDEX = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_INDEX__EXTENSIONS = NAMED_INDEX_CONTAINER__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_INDEX__UUID = NAMED_INDEX_CONTAINER__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_INDEX__NAME = NAMED_INDEX_CONTAINER__NAME;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_INDEX__DATA = NAMED_INDEX_CONTAINER__DATA;

	/**
	 * The feature id for the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_INDEX__CURRENCY_UNIT = NAMED_INDEX_CONTAINER__CURRENCY_UNIT;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_INDEX__VOLUME_UNIT = NAMED_INDEX_CONTAINER__VOLUME_UNIT;

	/**
	 * The number of structural features of the '<em>Currency Index</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CURRENCY_INDEX_FEATURE_COUNT = NAMED_INDEX_CONTAINER_FEATURE_COUNT + 0;

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
	 * The feature id for the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_INDEX__CURRENCY_UNIT = NAMED_INDEX_CONTAINER__CURRENCY_UNIT;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_INDEX__VOLUME_UNIT = NAMED_INDEX_CONTAINER__VOLUME_UNIT;

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
	 * The feature id for the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_INDEX__CURRENCY_UNIT = NAMED_INDEX_CONTAINER__CURRENCY_UNIT;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_INDEX__VOLUME_UNIT = NAMED_INDEX_CONTAINER__VOLUME_UNIT;

	/**
	 * The number of structural features of the '<em>Charter Index</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_INDEX_FEATURE_COUNT = NAMED_INDEX_CONTAINER_FEATURE_COUNT + 0;

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
	 * The feature id for the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_INDEX__CURRENCY_UNIT = NAMED_INDEX_CONTAINER__CURRENCY_UNIT;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_INDEX__VOLUME_UNIT = NAMED_INDEX_CONTAINER__VOLUME_UNIT;

	/**
	 * The number of structural features of the '<em>Base Fuel Index</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_INDEX_FEATURE_COUNT = NAMED_INDEX_CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.CostModelImpl <em>Cost Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.CostModelImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCostModel()
	 * @generated
	 */
	int COST_MODEL = 10;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PortsSplitPriceMapImpl <em>Ports Split Price Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PortsSplitPriceMapImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPortsSplitPriceMap()
	 * @generated
	 */
	int PORTS_SPLIT_PRICE_MAP = 18;

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
	int PORTS_SPLIT_EXPRESSION_MAP = 19;

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
	int PANAMA_CANAL_TARIFF = 20;

	/**
	 * The feature id for the '<em><b>Bands</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF__BANDS = 0;

	/**
	 * The feature id for the '<em><b>Available From</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF__AVAILABLE_FROM = 1;

	/**
	 * The feature id for the '<em><b>Markup Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF__MARKUP_RATE = 2;

	/**
	 * The number of structural features of the '<em>Panama Canal Tariff</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PANAMA_CANAL_TARIFF_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffBandImpl <em>Panama Canal Tariff Band</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffBandImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getPanamaCanalTariffBand()
	 * @generated
	 */
	int PANAMA_CANAL_TARIFF_BAND = 21;

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
	int SUEZ_CANAL_TUG_BAND = 22;

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
	int SUEZ_CANAL_TARIFF = 23;

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
	 * The feature id for the '<em><b>Mooring Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF__MOORING_COST = 3;

	/**
	 * The feature id for the '<em><b>Pilotage Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF__PILOTAGE_COST = 4;

	/**
	 * The feature id for the '<em><b>Disbursements</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF__DISBURSEMENTS = 5;

	/**
	 * The feature id for the '<em><b>Discount Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR = 6;

	/**
	 * The feature id for the '<em><b>Sdr To USD</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF__SDR_TO_USD = 7;

	/**
	 * The number of structural features of the '<em>Suez Canal Tariff</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUEZ_CANAL_TARIFF_FEATURE_COUNT = 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffBandImpl <em>Suez Canal Tariff Band</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffBandImpl
	 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getSuezCanalTariffBand()
	 * @generated
	 */
	int SUEZ_CANAL_TARIFF_BAND = 24;

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
	int UNIT_CONVERSION = 25;

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.PricingModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel
	 * @generated
	 */
	EClass getPricingModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.pricing.PricingModel#getCurrencyIndices <em>Currency Indices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Currency Indices</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel#getCurrencyIndices()
	 * @see #getPricingModel()
	 * @generated
	 */
	EReference getPricingModel_CurrencyIndices();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.pricing.CurrencyIndex <em>Currency Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Currency Index</em>'.
	 * @see com.mmxlabs.models.lng.pricing.CurrencyIndex
	 * @generated
	 */
	EClass getCurrencyIndex();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getCurrencyUnit <em>Currency Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Currency Unit</em>'.
	 * @see com.mmxlabs.models.lng.pricing.NamedIndexContainer#getCurrencyUnit()
	 * @see #getNamedIndexContainer()
	 * @generated
	 */
	EAttribute getNamedIndexContainer_CurrencyUnit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getVolumeUnit <em>Volume Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Unit</em>'.
	 * @see com.mmxlabs.models.lng.pricing.NamedIndexContainer#getVolumeUnit()
	 * @see #getNamedIndexContainer()
	 * @generated
	 */
	EAttribute getNamedIndexContainer_VolumeUnit();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getAvailableFrom <em>Available From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Available From</em>'.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getAvailableFrom()
	 * @see #getPanamaCanalTariff()
	 * @generated
	 */
	EAttribute getPanamaCanalTariff_AvailableFrom();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getMooringCost <em>Mooring Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mooring Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariff#getMooringCost()
	 * @see #getSuezCanalTariff()
	 * @generated
	 */
	EAttribute getSuezCanalTariff_MooringCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getPilotageCost <em>Pilotage Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pilotage Cost</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariff#getPilotageCost()
	 * @see #getSuezCanalTariff()
	 * @generated
	 */
	EAttribute getSuezCanalTariff_PilotageCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getDisbursements <em>Disbursements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Disbursements</em>'.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariff#getDisbursements()
	 * @see #getSuezCanalTariff()
	 * @generated
	 */
	EAttribute getSuezCanalTariff_Disbursements();

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
		 * The meta object literal for the '<em><b>Currency Indices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__CURRENCY_INDICES = eINSTANCE.getPricingModel_CurrencyIndices();

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
		 * The meta object literal for the '<em><b>Base Fuel Prices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__BASE_FUEL_PRICES = eINSTANCE.getPricingModel_BaseFuelPrices();

		/**
		 * The meta object literal for the '<em><b>Conversion Factors</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRICING_MODEL__CONVERSION_FACTORS = eINSTANCE.getPricingModel_ConversionFactors();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.pricing.impl.CurrencyIndexImpl <em>Currency Index</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.pricing.impl.CurrencyIndexImpl
		 * @see com.mmxlabs.models.lng.pricing.impl.PricingPackageImpl#getCurrencyIndex()
		 * @generated
		 */
		EClass CURRENCY_INDEX = eINSTANCE.getCurrencyIndex();

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
		 * The meta object literal for the '<em><b>Lumpsum</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COOLDOWN_PRICE__LUMPSUM = eINSTANCE.getCooldownPrice_Lumpsum();

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
		 * The meta object literal for the '<em><b>Currency Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_INDEX_CONTAINER__CURRENCY_UNIT = eINSTANCE.getNamedIndexContainer_CurrencyUnit();

		/**
		 * The meta object literal for the '<em><b>Volume Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_INDEX_CONTAINER__VOLUME_UNIT = eINSTANCE.getNamedIndexContainer_VolumeUnit();

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
		 * The meta object literal for the '<em><b>Available From</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PANAMA_CANAL_TARIFF__AVAILABLE_FROM = eINSTANCE.getPanamaCanalTariff_AvailableFrom();

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
		 * The meta object literal for the '<em><b>Mooring Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TARIFF__MOORING_COST = eINSTANCE.getSuezCanalTariff_MooringCost();

		/**
		 * The meta object literal for the '<em><b>Pilotage Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TARIFF__PILOTAGE_COST = eINSTANCE.getSuezCanalTariff_PilotageCost();

		/**
		 * The meta object literal for the '<em><b>Disbursements</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUEZ_CANAL_TARIFF__DISBURSEMENTS = eINSTANCE.getSuezCanalTariff_Disbursements();

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

	}

} //PricingPackage
