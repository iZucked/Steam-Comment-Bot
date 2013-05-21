/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;

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
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory
 * @model kind="package"
 * @generated
 */
public interface SpotMarketsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "spotmarkets";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/spotmarkets/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.spotmarkets";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SpotMarketsPackage eINSTANCE = com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsModelImpl
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getSpotMarketsModel()
	 * @generated
	 */
	int SPOT_MARKETS_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKETS_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKETS_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Des Purchase Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Des Sales Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Fob Purchases Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Fob Sales Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Chartering Spot Markets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKETS_MODEL__CHARTERING_SPOT_MARKETS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKETS_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterCostModelImpl <em>Charter Cost Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.CharterCostModelImpl
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getCharterCostModel()
	 * @generated
	 */
	int CHARTER_COST_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_COST_MODEL__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketGroupImpl <em>Spot Market Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketGroupImpl
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getSpotMarketGroup()
	 * @generated
	 */
	int SPOT_MARKET_GROUP = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_GROUP__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketImpl <em>Spot Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketImpl
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getSpotMarket()
	 * @generated
	 */
	int SPOT_MARKET = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__AVAILABILITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__MIN_QUANTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__MAX_QUANTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Price Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET__PRICE_INFO = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Spot Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.DESPurchaseMarketImpl <em>DES Purchase Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.DESPurchaseMarketImpl
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getDESPurchaseMarket()
	 * @generated
	 */
	int DES_PURCHASE_MARKET = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__EXTENSIONS = SPOT_MARKET__EXTENSIONS;

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
	 * The feature id for the '<em><b>Price Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET__PRICE_INFO = SPOT_MARKET__PRICE_INFO;

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
	 * The number of structural features of the '<em>DES Purchase Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_PURCHASE_MARKET_FEATURE_COUNT = SPOT_MARKET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.DESSalesMarketImpl <em>DES Sales Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.DESSalesMarketImpl
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getDESSalesMarket()
	 * @generated
	 */
	int DES_SALES_MARKET = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__EXTENSIONS = SPOT_MARKET__EXTENSIONS;

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
	 * The feature id for the '<em><b>Price Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__PRICE_INFO = SPOT_MARKET__PRICE_INFO;

	/**
	 * The feature id for the '<em><b>Notional Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET__NOTIONAL_PORT = SPOT_MARKET_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>DES Sales Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET_FEATURE_COUNT = SPOT_MARKET_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.FOBPurchasesMarketImpl <em>FOB Purchases Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.FOBPurchasesMarketImpl
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getFOBPurchasesMarket()
	 * @generated
	 */
	int FOB_PURCHASES_MARKET = 6;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__EXTENSIONS = SPOT_MARKET__EXTENSIONS;

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
	 * The feature id for the '<em><b>Price Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET__PRICE_INFO = SPOT_MARKET__PRICE_INFO;

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
	 * The number of structural features of the '<em>FOB Purchases Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_PURCHASES_MARKET_FEATURE_COUNT = SPOT_MARKET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.FOBSalesMarketImpl <em>FOB Sales Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.FOBSalesMarketImpl
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getFOBSalesMarket()
	 * @generated
	 */
	int FOB_SALES_MARKET = 7;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__EXTENSIONS = SPOT_MARKET__EXTENSIONS;

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
	 * The feature id for the '<em><b>Price Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__PRICE_INFO = SPOT_MARKET__PRICE_INFO;

	/**
	 * The feature id for the '<em><b>Load Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET__LOAD_PORT = SPOT_MARKET_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>FOB Sales Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOB_SALES_MARKET_FEATURE_COUNT = SPOT_MARKET_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotAvailabilityImpl <em>Spot Availability</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotAvailabilityImpl
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getSpotAvailability()
	 * @generated
	 */
	int SPOT_AVAILABILITY = 8;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.spotmarkets.SpotType <em>Spot Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotType
	 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getSpotType()
	 * @generated
	 */
	int SPOT_TYPE = 9;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel
	 * @generated
	 */
	EClass getSpotMarketsModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDesPurchaseSpotMarket <em>Des Purchase Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Des Purchase Spot Market</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDesPurchaseSpotMarket()
	 * @see #getSpotMarketsModel()
	 * @generated
	 */
	EReference getSpotMarketsModel_DesPurchaseSpotMarket();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDesSalesSpotMarket <em>Des Sales Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Des Sales Spot Market</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDesSalesSpotMarket()
	 * @see #getSpotMarketsModel()
	 * @generated
	 */
	EReference getSpotMarketsModel_DesSalesSpotMarket();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getFobPurchasesSpotMarket <em>Fob Purchases Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fob Purchases Spot Market</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getFobPurchasesSpotMarket()
	 * @see #getSpotMarketsModel()
	 * @generated
	 */
	EReference getSpotMarketsModel_FobPurchasesSpotMarket();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getFobSalesSpotMarket <em>Fob Sales Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fob Sales Spot Market</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getFobSalesSpotMarket()
	 * @see #getSpotMarketsModel()
	 * @generated
	 */
	EReference getSpotMarketsModel_FobSalesSpotMarket();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getCharteringSpotMarkets <em>Chartering Spot Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Chartering Spot Markets</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getCharteringSpotMarkets()
	 * @see #getSpotMarketsModel()
	 * @generated
	 */
	EReference getSpotMarketsModel_CharteringSpotMarkets();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel <em>Charter Cost Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Charter Cost Model</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.CharterCostModel
	 * @generated
	 */
	EClass getCharterCostModel();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getVesselClasses <em>Vessel Classes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessel Classes</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getVesselClasses()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EReference getCharterCostModel_VesselClasses();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getCharterInPrice <em>Charter In Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Charter In Price</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getCharterInPrice()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EReference getCharterCostModel_CharterInPrice();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getCharterOutPrice <em>Charter Out Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Charter Out Price</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getCharterOutPrice()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EReference getCharterCostModel_CharterOutPrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getSpotCharterCount <em>Spot Charter Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Charter Count</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getSpotCharterCount()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EAttribute getCharterCostModel_SpotCharterCount();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getMinCharterOutDuration <em>Min Charter Out Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Charter Out Duration</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getMinCharterOutDuration()
	 * @see #getCharterCostModel()
	 * @generated
	 */
	EAttribute getCharterCostModel_MinCharterOutDuration();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup <em>Spot Market Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Market Group</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup
	 * @generated
	 */
	EClass getSpotMarketGroup();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup#getAvailability <em>Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Availability</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup#getAvailability()
	 * @see #getSpotMarketGroup()
	 * @generated
	 */
	EReference getSpotMarketGroup_Availability();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup#getType()
	 * @see #getSpotMarketGroup()
	 * @generated
	 */
	EAttribute getSpotMarketGroup_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup#getMarkets <em>Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Markets</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup#getMarkets()
	 * @see #getSpotMarketGroup()
	 * @generated
	 */
	EReference getSpotMarketGroup_Markets();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket <em>Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Market</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarket
	 * @generated
	 */
	EClass getSpotMarket();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getAvailability <em>Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Availability</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarket#getAvailability()
	 * @see #getSpotMarket()
	 * @generated
	 */
	EReference getSpotMarket_Availability();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getMinQuantity <em>Min Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Quantity</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarket#getMinQuantity()
	 * @see #getSpotMarket()
	 * @generated
	 */
	EAttribute getSpotMarket_MinQuantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getMaxQuantity <em>Max Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Quantity</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarket#getMaxQuantity()
	 * @see #getSpotMarket()
	 * @generated
	 */
	EAttribute getSpotMarket_MaxQuantity();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getPriceInfo <em>Price Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Price Info</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarket#getPriceInfo()
	 * @see #getSpotMarket()
	 * @generated
	 */
	EReference getSpotMarket_PriceInfo();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket <em>DES Purchase Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>DES Purchase Market</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket
	 * @generated
	 */
	EClass getDESPurchaseMarket();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket#getCv <em>Cv</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cv</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket#getCv()
	 * @see #getDESPurchaseMarket()
	 * @generated
	 */
	EAttribute getDESPurchaseMarket_Cv();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket#getDestinationPorts <em>Destination Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Destination Ports</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket#getDestinationPorts()
	 * @see #getDESPurchaseMarket()
	 * @generated
	 */
	EReference getDESPurchaseMarket_DestinationPorts();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.spotmarkets.DESSalesMarket <em>DES Sales Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>DES Sales Market</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.DESSalesMarket
	 * @generated
	 */
	EClass getDESSalesMarket();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.spotmarkets.DESSalesMarket#getNotionalPort <em>Notional Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Notional Port</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.DESSalesMarket#getNotionalPort()
	 * @see #getDESSalesMarket()
	 * @generated
	 */
	EReference getDESSalesMarket_NotionalPort();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket <em>FOB Purchases Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>FOB Purchases Market</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket
	 * @generated
	 */
	EClass getFOBPurchasesMarket();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket#getNotionalPort <em>Notional Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Notional Port</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket#getNotionalPort()
	 * @see #getFOBPurchasesMarket()
	 * @generated
	 */
	EReference getFOBPurchasesMarket_NotionalPort();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket#getCv <em>Cv</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cv</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket#getCv()
	 * @see #getFOBPurchasesMarket()
	 * @generated
	 */
	EAttribute getFOBPurchasesMarket_Cv();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket <em>FOB Sales Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>FOB Sales Market</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket
	 * @generated
	 */
	EClass getFOBSalesMarket();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket#getLoadPort <em>Load Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Port</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket#getLoadPort()
	 * @see #getFOBSalesMarket()
	 * @generated
	 */
	EReference getFOBSalesMarket_LoadPort();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability <em>Spot Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Availability</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotAvailability
	 * @generated
	 */
	EClass getSpotAvailability();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getConstant <em>Constant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Constant</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getConstant()
	 * @see #getSpotAvailability()
	 * @generated
	 */
	EAttribute getSpotAvailability_Constant();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getCurve <em>Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Curve</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getCurve()
	 * @see #getSpotAvailability()
	 * @generated
	 */
	EReference getSpotAvailability_Curve();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.spotmarkets.SpotType <em>Spot Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Spot Type</em>'.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotType
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
	SpotMarketsFactory getSpotMarketsFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsModelImpl
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getSpotMarketsModel()
		 * @generated
		 */
		EClass SPOT_MARKETS_MODEL = eINSTANCE.getSpotMarketsModel();

		/**
		 * The meta object literal for the '<em><b>Des Purchase Spot Market</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET = eINSTANCE.getSpotMarketsModel_DesPurchaseSpotMarket();

		/**
		 * The meta object literal for the '<em><b>Des Sales Spot Market</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET = eINSTANCE.getSpotMarketsModel_DesSalesSpotMarket();

		/**
		 * The meta object literal for the '<em><b>Fob Purchases Spot Market</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET = eINSTANCE.getSpotMarketsModel_FobPurchasesSpotMarket();

		/**
		 * The meta object literal for the '<em><b>Fob Sales Spot Market</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET = eINSTANCE.getSpotMarketsModel_FobSalesSpotMarket();

		/**
		 * The meta object literal for the '<em><b>Chartering Spot Markets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_MARKETS_MODEL__CHARTERING_SPOT_MARKETS = eINSTANCE.getSpotMarketsModel_CharteringSpotMarkets();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterCostModelImpl <em>Charter Cost Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.CharterCostModelImpl
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getCharterCostModel()
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
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_COST_MODEL__MIN_CHARTER_OUT_DURATION = eINSTANCE.getCharterCostModel_MinCharterOutDuration();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketGroupImpl <em>Spot Market Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketGroupImpl
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getSpotMarketGroup()
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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketImpl <em>Spot Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketImpl
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getSpotMarket()
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
		 * The meta object literal for the '<em><b>Price Info</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_MARKET__PRICE_INFO = eINSTANCE.getSpotMarket_PriceInfo();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.DESPurchaseMarketImpl <em>DES Purchase Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.DESPurchaseMarketImpl
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getDESPurchaseMarket()
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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.DESSalesMarketImpl <em>DES Sales Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.DESSalesMarketImpl
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getDESSalesMarket()
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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.FOBPurchasesMarketImpl <em>FOB Purchases Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.FOBPurchasesMarketImpl
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getFOBPurchasesMarket()
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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.FOBSalesMarketImpl <em>FOB Sales Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.FOBSalesMarketImpl
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getFOBSalesMarket()
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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotAvailabilityImpl <em>Spot Availability</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotAvailabilityImpl
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getSpotAvailability()
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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.spotmarkets.SpotType <em>Spot Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.spotmarkets.SpotType
		 * @see com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsPackageImpl#getSpotType()
		 * @generated
		 */
		EEnum SPOT_TYPE = eINSTANCE.getSpotType();

	}

} //SpotMarketsPackage
