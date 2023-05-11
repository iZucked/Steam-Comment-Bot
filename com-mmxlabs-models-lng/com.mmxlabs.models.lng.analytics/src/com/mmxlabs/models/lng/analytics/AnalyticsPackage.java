/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
	 * The feature id for the '<em><b>Option Models</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__OPTION_MODELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Optimisations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__OPTIMISATIONS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Viability Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__VIABILITY_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Mtm Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__MTM_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Breakeven Models</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__BREAKEVEN_MODELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Swap Value Matrix Models</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Marketability Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL__MARKETABILITY_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.BuyOption <em>Buy Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.BuyOption
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyOption()
	 * @generated
	 */
	int BUY_OPTION = 1;

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
	int SELL_OPTION = 2;

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
	int BUY_OPPORTUNITY = 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OpenBuyImpl <em>Open Buy</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OpenBuyImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOpenBuy()
	 * @generated
	 */
	int OPEN_BUY = 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SellOpportunityImpl <em>Sell Opportunity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SellOpportunityImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellOpportunity()
	 * @generated
	 */
	int SELL_OPPORTUNITY = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OpenSellImpl <em>Open Sell</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OpenSellImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOpenSell()
	 * @generated
	 */
	int OPEN_SELL = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SELL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SELL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The number of structural features of the '<em>Open Sell</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SELL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_BUY__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_BUY__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The number of structural features of the '<em>Open Buy</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_BUY_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Des Purchase</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__DES_PURCHASE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__PORT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__CONTRACT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__DATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__PRICE_EXPRESSION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Cv</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__CV = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__CANCELLATION_EXPRESSION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Misc Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__MISC_COSTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Volume Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__VOLUME_MODE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Min Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__MIN_VOLUME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Max Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__MAX_VOLUME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Volume Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__VOLUME_UNITS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Specify Window</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__SPECIFY_WINDOW = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__WINDOW_SIZE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Window Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__WINDOW_SIZE_UNITS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 16;

	/**
	 * The number of structural features of the '<em>Buy Opportunity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Fob Sale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__FOB_SALE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__PORT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__CONTRACT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__DATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__PRICE_EXPRESSION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__CANCELLATION_EXPRESSION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Misc Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__MISC_COSTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Volume Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__VOLUME_MODE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Min Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__MIN_VOLUME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Max Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__MAX_VOLUME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Volume Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__VOLUME_UNITS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Specify Window</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__SPECIFY_WINDOW = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__WINDOW_SIZE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Window Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__WINDOW_SIZE_UNITS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 15;

	/**
	 * The number of structural features of the '<em>Sell Opportunity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 16;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BuyMarketImpl <em>Buy Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BuyMarketImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyMarket()
	 * @generated
	 */
	int BUY_MARKET = 7;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_MARKET__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_MARKET__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_MARKET__MARKET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_MARKET__MONTH = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Buy Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_MARKET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SellMarketImpl <em>Sell Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SellMarketImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellMarket()
	 * @generated
	 */
	int SELL_MARKET = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_MARKET__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_MARKET__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_MARKET__MARKET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_MARKET__MONTH = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Sell Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_MARKET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BuyReferenceImpl <em>Buy Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BuyReferenceImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyReference()
	 * @generated
	 */
	int BUY_REFERENCE = 9;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_REFERENCE__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_REFERENCE__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_REFERENCE__SLOT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Buy Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_REFERENCE_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SellReferenceImpl <em>Sell Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SellReferenceImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellReference()
	 * @generated
	 */
	int SELL_REFERENCE = 10;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_REFERENCE__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_REFERENCE__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_REFERENCE__SLOT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Sell Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_REFERENCE_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselEventOptionImpl <em>Vessel Event Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.VesselEventOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselEventOption()
	 * @generated
	 */
	int VESSEL_EVENT_OPTION = 11;

	/**
	 * The number of structural features of the '<em>Vessel Event Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_OPTION_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselEventReferenceImpl <em>Vessel Event Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.VesselEventReferenceImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselEventReference()
	 * @generated
	 */
	int VESSEL_EVENT_REFERENCE = 12;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_REFERENCE__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_REFERENCE__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_REFERENCE__EVENT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Vessel Event Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_REFERENCE_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.CharterOutOpportunityImpl <em>Charter Out Opportunity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.CharterOutOpportunityImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCharterOutOpportunity()
	 * @generated
	 */
	int CHARTER_OUT_OPPORTUNITY = 13;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_OPPORTUNITY__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_OPPORTUNITY__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_OPPORTUNITY__HIRE_COST = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_OPPORTUNITY__PORT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_OPPORTUNITY__DATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_OPPORTUNITY__DURATION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Charter Out Opportunity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_OPPORTUNITY_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowImpl <em>Base Case Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BaseCaseRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBaseCaseRow()
	 * @generated
	 */
	int BASE_CASE_ROW = 14;

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
	 * The feature id for the '<em><b>Vessel Event Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW__VESSEL_EVENT_OPTION = 2;

	/**
	 * The feature id for the '<em><b>Shipping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW__SHIPPING = 3;

	/**
	 * The feature id for the '<em><b>Optionise</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW__OPTIONISE = 4;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW__OPTIONS = 5;

	/**
	 * The feature id for the '<em><b>Freeze</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW__FREEZE = 6;

	/**
	 * The number of structural features of the '<em>Base Case Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowOptionsImpl <em>Base Case Row Options</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BaseCaseRowOptionsImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBaseCaseRowOptions()
	 * @generated
	 */
	int BASE_CASE_ROW_OPTIONS = 15;

	/**
	 * The feature id for the '<em><b>Laden Route</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW_OPTIONS__LADEN_ROUTE = 0;

	/**
	 * The feature id for the '<em><b>Ballast Route</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW_OPTIONS__BALLAST_ROUTE = 1;

	/**
	 * The feature id for the '<em><b>Laden Fuel Choice</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICE = 2;

	/**
	 * The feature id for the '<em><b>Ballast Fuel Choice</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICE = 3;

	/**
	 * The feature id for the '<em><b>Load Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW_OPTIONS__LOAD_DATE = 4;

	/**
	 * The feature id for the '<em><b>Discharge Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW_OPTIONS__DISCHARGE_DATE = 5;

	/**
	 * The number of structural features of the '<em>Base Case Row Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_ROW_OPTIONS_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl <em>Partial Case Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPartialCaseRow()
	 * @generated
	 */
	int PARTIAL_CASE_ROW = 16;

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
	 * The feature id for the '<em><b>Vessel Event Options</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS = 2;

	/**
	 * The feature id for the '<em><b>Shipping</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW__SHIPPING = 3;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW__OPTIONS = 4;

	/**
	 * The feature id for the '<em><b>Commodity Curve Options</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW__COMMODITY_CURVE_OPTIONS = 5;

	/**
	 * The number of structural features of the '<em>Partial Case Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowOptionsImpl <em>Partial Case Row Options</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.PartialCaseRowOptionsImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPartialCaseRowOptions()
	 * @generated
	 */
	int PARTIAL_CASE_ROW_OPTIONS = 17;

	/**
	 * The feature id for the '<em><b>Laden Routes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES = 0;

	/**
	 * The feature id for the '<em><b>Ballast Routes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW_OPTIONS__BALLAST_ROUTES = 1;

	/**
	 * The feature id for the '<em><b>Laden Fuel Choices</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES = 2;

	/**
	 * The feature id for the '<em><b>Ballast Fuel Choices</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICES = 3;

	/**
	 * The feature id for the '<em><b>Load Dates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES = 4;

	/**
	 * The feature id for the '<em><b>Discharge Dates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES = 5;

	/**
	 * The number of structural features of the '<em>Partial Case Row Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_ROW_OPTIONS_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ShippingOptionImpl <em>Shipping Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ShippingOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getShippingOption()
	 * @generated
	 */
	int SHIPPING_OPTION = 18;

	/**
	 * The number of structural features of the '<em>Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SimpleVesselCharterOptionImpl <em>Simple Vessel Charter Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SimpleVesselCharterOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSimpleVesselCharterOption()
	 * @generated
	 */
	int SIMPLE_VESSEL_CHARTER_OPTION = 19;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_VESSEL_CHARTER_OPTION__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_VESSEL_CHARTER_OPTION__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_VESSEL_CHARTER_OPTION__VESSEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_VESSEL_CHARTER_OPTION__ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Use Safety Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Simple Vessel Charter Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_VESSEL_CHARTER_OPTION_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OptionalSimpleVesselCharterOptionImpl <em>Optional Simple Vessel Charter Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OptionalSimpleVesselCharterOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptionalSimpleVesselCharterOption()
	 * @generated
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION = 20;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__EXTENSIONS = SIMPLE_VESSEL_CHARTER_OPTION__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__UUID = SIMPLE_VESSEL_CHARTER_OPTION__UUID;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__VESSEL = SIMPLE_VESSEL_CHARTER_OPTION__VESSEL;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST = SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__ENTITY = SIMPLE_VESSEL_CHARTER_OPTION__ENTITY;

	/**
	 * The feature id for the '<em><b>Use Safety Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL = SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL;

	/**
	 * The feature id for the '<em><b>Ballast Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__BALLAST_BONUS = SIMPLE_VESSEL_CHARTER_OPTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__REPOSITIONING_FEE = SIMPLE_VESSEL_CHARTER_OPTION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START = SIMPLE_VESSEL_CHARTER_OPTION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END = SIMPLE_VESSEL_CHARTER_OPTION_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Start Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START_PORT = SIMPLE_VESSEL_CHARTER_OPTION_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>End Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END_PORT = SIMPLE_VESSEL_CHARTER_OPTION_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Optional Simple Vessel Charter Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION_FEATURE_COUNT = SIMPLE_VESSEL_CHARTER_OPTION_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.RoundTripShippingOptionImpl <em>Round Trip Shipping Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.RoundTripShippingOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getRoundTripShippingOption()
	 * @generated
	 */
	int ROUND_TRIP_SHIPPING_OPTION = 21;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUND_TRIP_SHIPPING_OPTION__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUND_TRIP_SHIPPING_OPTION__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUND_TRIP_SHIPPING_OPTION__VESSEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUND_TRIP_SHIPPING_OPTION__HIRE_COST = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUND_TRIP_SHIPPING_OPTION__ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Round Trip Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUND_TRIP_SHIPPING_OPTION_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.NominatedShippingOptionImpl <em>Nominated Shipping Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.NominatedShippingOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getNominatedShippingOption()
	 * @generated
	 */
	int NOMINATED_SHIPPING_OPTION = 22;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATED_SHIPPING_OPTION__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATED_SHIPPING_OPTION__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Nominated Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATED_SHIPPING_OPTION__NOMINATED_VESSEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Nominated Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOMINATED_SHIPPING_OPTION_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.FullVesselCharterOptionImpl <em>Full Vessel Charter Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.FullVesselCharterOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getFullVesselCharterOption()
	 * @generated
	 */
	int FULL_VESSEL_CHARTER_OPTION = 23;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FULL_VESSEL_CHARTER_OPTION__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FULL_VESSEL_CHARTER_OPTION__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Vessel Charter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Full Vessel Charter Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FULL_VESSEL_CHARTER_OPTION_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ExistingVesselCharterOptionImpl <em>Existing Vessel Charter Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ExistingVesselCharterOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getExistingVesselCharterOption()
	 * @generated
	 */
	int EXISTING_VESSEL_CHARTER_OPTION = 24;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_VESSEL_CHARTER_OPTION__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_VESSEL_CHARTER_OPTION__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Vessel Charter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_VESSEL_CHARTER_OPTION__VESSEL_CHARTER = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Existing Vessel Charter Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_VESSEL_CHARTER_OPTION_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.AnalysisResultDetailImpl <em>Analysis Result Detail</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalysisResultDetailImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAnalysisResultDetail()
	 * @generated
	 */
	int ANALYSIS_RESULT_DETAIL = 25;

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
	int PROFIT_AND_LOSS_RESULT = 26;

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
	int BREAK_EVEN_RESULT = 27;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_RESULT__PRICE = ANALYSIS_RESULT_DETAIL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Price String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_RESULT__PRICE_STRING = ANALYSIS_RESULT_DETAIL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Cargo PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_RESULT__CARGO_PNL = ANALYSIS_RESULT_DETAIL_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Break Even Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_RESULT_FEATURE_COUNT = ANALYSIS_RESULT_DETAIL_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl <em>Abstract Analysis Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAbstractAnalysisModel()
	 * @generated
	 */
	int ABSTRACT_ANALYSIS_MODEL = 28;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Buys</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__BUYS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Sells</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__SELLS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Commodity Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Abstract Analysis Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl <em>Marketability Result Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityResultContainer()
	 * @generated
	 */
	int MARKETABILITY_RESULT_CONTAINER = 78;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl <em>Option Analysis Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptionAnalysisModel()
	 * @generated
	 */
	int OPTION_ANALYSIS_MODEL = 29;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__EXTENSIONS = ABSTRACT_ANALYSIS_MODEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__UUID = ABSTRACT_ANALYSIS_MODEL__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__NAME = ABSTRACT_ANALYSIS_MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Buys</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__BUYS = ABSTRACT_ANALYSIS_MODEL__BUYS;

	/**
	 * The feature id for the '<em><b>Sells</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__SELLS = ABSTRACT_ANALYSIS_MODEL__SELLS;

	/**
	 * The feature id for the '<em><b>Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__VESSEL_EVENTS = ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS;

	/**
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES = ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES;

	/**
	 * The feature id for the '<em><b>Commodity Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__COMMODITY_CURVES = ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES;

	/**
	 * The feature id for the '<em><b>Base Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__BASE_CASE = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Partial Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__PARTIAL_CASE = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Results</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__RESULTS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Use Target PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__USE_TARGET_PNL = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__MODE = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Option Analysis Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL_FEATURE_COUNT = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl <em>Base Case</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBaseCase()
	 * @generated
	 */
	int BASE_CASE = 31;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseImpl <em>Partial Case</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.PartialCaseImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPartialCase()
	 * @generated
	 */
	int PARTIAL_CASE = 32;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ExistingCharterMarketOptionImpl <em>Existing Charter Market Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ExistingCharterMarketOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getExistingCharterMarketOption()
	 * @generated
	 */
	int EXISTING_CHARTER_MARKET_OPTION = 33;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl <em>Abstract Solution Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAbstractSolutionSet()
	 * @generated
	 */
	int ABSTRACT_SOLUTION_SET = 34;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ActionableSetPlanImpl <em>Actionable Set Plan</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ActionableSetPlanImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getActionableSetPlan()
	 * @generated
	 */
	int ACTIONABLE_SET_PLAN = 35;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionsImpl <em>Slot Insertion Options</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionsImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSlotInsertionOptions()
	 * @generated
	 */
	int SLOT_INSERTION_OPTIONS = 36;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionImpl <em>Solution Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SolutionOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSolutionOption()
	 * @generated
	 */
	int SOLUTION_OPTION = 37;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ChangeDescriptionImpl <em>Change Description</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ChangeDescriptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getChangeDescription()
	 * @generated
	 */
	int CHANGE_DESCRIPTION = 41;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ChangeImpl <em>Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ChangeImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getChange()
	 * @generated
	 */
	int CHANGE = 42;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OpenSlotChangeImpl <em>Open Slot Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OpenSlotChangeImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOpenSlotChange()
	 * @generated
	 */
	int OPEN_SLOT_CHANGE = 43;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.CargoChangeImpl <em>Cargo Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.CargoChangeImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCargoChange()
	 * @generated
	 */
	int CARGO_CHANGE = 44;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselEventChangeImpl <em>Vessel Event Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.VesselEventChangeImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselEventChange()
	 * @generated
	 */
	int VESSEL_EVENT_CHANGE = 45;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselEventDescriptorImpl <em>Vessel Event Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.VesselEventDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselEventDescriptor()
	 * @generated
	 */
	int VESSEL_EVENT_DESCRIPTOR = 46;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SlotDescriptorImpl <em>Slot Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SlotDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSlotDescriptor()
	 * @generated
	 */
	int SLOT_DESCRIPTOR = 47;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.RealSlotDescriptorImpl <em>Real Slot Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.RealSlotDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getRealSlotDescriptor()
	 * @generated
	 */
	int REAL_SLOT_DESCRIPTOR = 48;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SpotMarketSlotDescriptorImpl <em>Spot Market Slot Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SpotMarketSlotDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSpotMarketSlotDescriptor()
	 * @generated
	 */
	int SPOT_MARKET_SLOT_DESCRIPTOR = 49;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselAllocationDescriptorImpl <em>Vessel Allocation Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.VesselAllocationDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselAllocationDescriptor()
	 * @generated
	 */
	int VESSEL_ALLOCATION_DESCRIPTOR = 50;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketVesselAllocationDescriptorImpl <em>Market Vessel Allocation Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MarketVesselAllocationDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketVesselAllocationDescriptor()
	 * @generated
	 */
	int MARKET_VESSEL_ALLOCATION_DESCRIPTOR = 51;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.FleetVesselAllocationDescriptorImpl <em>Fleet Vessel Allocation Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.FleetVesselAllocationDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getFleetVesselAllocationDescriptor()
	 * @generated
	 */
	int FLEET_VESSEL_ALLOCATION_DESCRIPTOR = 52;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.PositionDescriptorImpl <em>Position Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.PositionDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPositionDescriptor()
	 * @generated
	 */
	int POSITION_DESCRIPTOR = 53;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OptimisationResultImpl <em>Optimisation Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OptimisationResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptimisationResult()
	 * @generated
	 */
	int OPTIMISATION_RESULT = 38;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.DualModeSolutionOptionImpl <em>Dual Mode Solution Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.DualModeSolutionOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getDualModeSolutionOption()
	 * @generated
	 */
	int DUAL_MODE_SOLUTION_OPTION = 39;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Has Dual Mode Solutions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Portfolio Break Even Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__USER_SETTINGS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Extra Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__EXTRA_SLOTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Base Option</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__BASE_OPTION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__OPTIONS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Extra Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Extra Vessel Charters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_CHARTERS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Charter In Market Overrides</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Extra Charter In Markets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Use Scenario Base</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The number of structural features of the '<em>Abstract Solution Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SandboxResultImpl <em>Sandbox Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SandboxResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSandboxResult()
	 * @generated
	 */
	int SANDBOX_RESULT = 30;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__EXTENSIONS = ABSTRACT_SOLUTION_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__UUID = ABSTRACT_SOLUTION_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__NAME = ABSTRACT_SOLUTION_SET__NAME;

	/**
	 * The feature id for the '<em><b>Has Dual Mode Solutions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__HAS_DUAL_MODE_SOLUTIONS = ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS;

	/**
	 * The feature id for the '<em><b>Portfolio Break Even Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__PORTFOLIO_BREAK_EVEN_MODE = ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE;

	/**
	 * The feature id for the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__USER_SETTINGS = ABSTRACT_SOLUTION_SET__USER_SETTINGS;

	/**
	 * The feature id for the '<em><b>Extra Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__EXTRA_SLOTS = ABSTRACT_SOLUTION_SET__EXTRA_SLOTS;

	/**
	 * The feature id for the '<em><b>Base Option</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__BASE_OPTION = ABSTRACT_SOLUTION_SET__BASE_OPTION;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__OPTIONS = ABSTRACT_SOLUTION_SET__OPTIONS;

	/**
	 * The feature id for the '<em><b>Extra Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__EXTRA_VESSEL_EVENTS = ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS;

	/**
	 * The feature id for the '<em><b>Extra Vessel Charters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__EXTRA_VESSEL_CHARTERS = ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_CHARTERS;

	/**
	 * The feature id for the '<em><b>Charter In Market Overrides</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__CHARTER_IN_MARKET_OVERRIDES = ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES;

	/**
	 * The feature id for the '<em><b>Extra Charter In Markets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__EXTRA_CHARTER_IN_MARKETS = ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS;

	/**
	 * The feature id for the '<em><b>Use Scenario Base</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT__USE_SCENARIO_BASE = ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE;

	/**
	 * The number of structural features of the '<em>Sandbox Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SANDBOX_RESULT_FEATURE_COUNT = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 0;

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
	 * The feature id for the '<em><b>Keep Existing Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE__KEEP_EXISTING_SCENARIO = 2;

	/**
	 * The number of structural features of the '<em>Base Case</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_CASE_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Partial Case</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE__PARTIAL_CASE = 0;

	/**
	 * The feature id for the '<em><b>Keep Existing Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE__KEEP_EXISTING_SCENARIO = 1;

	/**
	 * The number of structural features of the '<em>Partial Case</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARTIAL_CASE_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_CHARTER_MARKET_OPTION__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_CHARTER_MARKET_OPTION__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Charter In Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Existing Charter Market Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_CHARTER_MARKET_OPTION_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__EXTENSIONS = ABSTRACT_SOLUTION_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__UUID = ABSTRACT_SOLUTION_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__NAME = ABSTRACT_SOLUTION_SET__NAME;

	/**
	 * The feature id for the '<em><b>Has Dual Mode Solutions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__HAS_DUAL_MODE_SOLUTIONS = ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS;

	/**
	 * The feature id for the '<em><b>Portfolio Break Even Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__PORTFOLIO_BREAK_EVEN_MODE = ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE;

	/**
	 * The feature id for the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__USER_SETTINGS = ABSTRACT_SOLUTION_SET__USER_SETTINGS;

	/**
	 * The feature id for the '<em><b>Extra Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__EXTRA_SLOTS = ABSTRACT_SOLUTION_SET__EXTRA_SLOTS;

	/**
	 * The feature id for the '<em><b>Base Option</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__BASE_OPTION = ABSTRACT_SOLUTION_SET__BASE_OPTION;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__OPTIONS = ABSTRACT_SOLUTION_SET__OPTIONS;

	/**
	 * The feature id for the '<em><b>Extra Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__EXTRA_VESSEL_EVENTS = ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS;

	/**
	 * The feature id for the '<em><b>Extra Vessel Charters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__EXTRA_VESSEL_CHARTERS = ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_CHARTERS;

	/**
	 * The feature id for the '<em><b>Charter In Market Overrides</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__CHARTER_IN_MARKET_OVERRIDES = ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES;

	/**
	 * The feature id for the '<em><b>Extra Charter In Markets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__EXTRA_CHARTER_IN_MARKETS = ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS;

	/**
	 * The feature id for the '<em><b>Use Scenario Base</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__USE_SCENARIO_BASE = ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE;

	/**
	 * The number of structural features of the '<em>Actionable Set Plan</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN_FEATURE_COUNT = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__EXTENSIONS = ABSTRACT_SOLUTION_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__UUID = ABSTRACT_SOLUTION_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__NAME = ABSTRACT_SOLUTION_SET__NAME;

	/**
	 * The feature id for the '<em><b>Has Dual Mode Solutions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__HAS_DUAL_MODE_SOLUTIONS = ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS;

	/**
	 * The feature id for the '<em><b>Portfolio Break Even Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__PORTFOLIO_BREAK_EVEN_MODE = ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE;

	/**
	 * The feature id for the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__USER_SETTINGS = ABSTRACT_SOLUTION_SET__USER_SETTINGS;

	/**
	 * The feature id for the '<em><b>Extra Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__EXTRA_SLOTS = ABSTRACT_SOLUTION_SET__EXTRA_SLOTS;

	/**
	 * The feature id for the '<em><b>Base Option</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__BASE_OPTION = ABSTRACT_SOLUTION_SET__BASE_OPTION;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__OPTIONS = ABSTRACT_SOLUTION_SET__OPTIONS;

	/**
	 * The feature id for the '<em><b>Extra Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__EXTRA_VESSEL_EVENTS = ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS;

	/**
	 * The feature id for the '<em><b>Extra Vessel Charters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__EXTRA_VESSEL_CHARTERS = ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_CHARTERS;

	/**
	 * The feature id for the '<em><b>Charter In Market Overrides</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__CHARTER_IN_MARKET_OVERRIDES = ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES;

	/**
	 * The feature id for the '<em><b>Extra Charter In Markets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__EXTRA_CHARTER_IN_MARKETS = ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS;

	/**
	 * The feature id for the '<em><b>Use Scenario Base</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__USE_SCENARIO_BASE = ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE;

	/**
	 * The feature id for the '<em><b>Slots Inserted</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__SLOTS_INSERTED = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Events Inserted</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__EVENTS_INSERTED = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Slot Insertion Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS_FEATURE_COUNT = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Change Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION__CHANGE_DESCRIPTION = 0;

	/**
	 * The feature id for the '<em><b>Schedule Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION__SCHEDULE_SPECIFICATION = 1;

	/**
	 * The feature id for the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION__SCHEDULE_MODEL = 2;

	/**
	 * The number of structural features of the '<em>Solution Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__EXTENSIONS = ABSTRACT_SOLUTION_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__UUID = ABSTRACT_SOLUTION_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__NAME = ABSTRACT_SOLUTION_SET__NAME;

	/**
	 * The feature id for the '<em><b>Has Dual Mode Solutions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__HAS_DUAL_MODE_SOLUTIONS = ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS;

	/**
	 * The feature id for the '<em><b>Portfolio Break Even Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__PORTFOLIO_BREAK_EVEN_MODE = ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE;

	/**
	 * The feature id for the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__USER_SETTINGS = ABSTRACT_SOLUTION_SET__USER_SETTINGS;

	/**
	 * The feature id for the '<em><b>Extra Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__EXTRA_SLOTS = ABSTRACT_SOLUTION_SET__EXTRA_SLOTS;

	/**
	 * The feature id for the '<em><b>Base Option</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__BASE_OPTION = ABSTRACT_SOLUTION_SET__BASE_OPTION;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__OPTIONS = ABSTRACT_SOLUTION_SET__OPTIONS;

	/**
	 * The feature id for the '<em><b>Extra Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__EXTRA_VESSEL_EVENTS = ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS;

	/**
	 * The feature id for the '<em><b>Extra Vessel Charters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__EXTRA_VESSEL_CHARTERS = ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_CHARTERS;

	/**
	 * The feature id for the '<em><b>Charter In Market Overrides</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__CHARTER_IN_MARKET_OVERRIDES = ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES;

	/**
	 * The feature id for the '<em><b>Extra Charter In Markets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__EXTRA_CHARTER_IN_MARKETS = ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS;

	/**
	 * The feature id for the '<em><b>Use Scenario Base</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__USE_SCENARIO_BASE = ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE;

	/**
	 * The number of structural features of the '<em>Optimisation Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT_FEATURE_COUNT = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Change Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DUAL_MODE_SOLUTION_OPTION__CHANGE_DESCRIPTION = SOLUTION_OPTION__CHANGE_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Schedule Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DUAL_MODE_SOLUTION_OPTION__SCHEDULE_SPECIFICATION = SOLUTION_OPTION__SCHEDULE_SPECIFICATION;

	/**
	 * The feature id for the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DUAL_MODE_SOLUTION_OPTION__SCHEDULE_MODEL = SOLUTION_OPTION__SCHEDULE_MODEL;

	/**
	 * The feature id for the '<em><b>Micro Base Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE = SOLUTION_OPTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Micro Target Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE = SOLUTION_OPTION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Dual Mode Solution Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DUAL_MODE_SOLUTION_OPTION_FEATURE_COUNT = SOLUTION_OPTION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionMicroCaseImpl <em>Solution Option Micro Case</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SolutionOptionMicroCaseImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSolutionOptionMicroCase()
	 * @generated
	 */
	int SOLUTION_OPTION_MICRO_CASE = 40;

	/**
	 * The feature id for the '<em><b>Schedule Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION = 0;

	/**
	 * The feature id for the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Extra Vessel Charters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_CHARTERS = 2;

	/**
	 * The feature id for the '<em><b>Charter In Market Overrides</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION_MICRO_CASE__CHARTER_IN_MARKET_OVERRIDES = 3;

	/**
	 * The number of structural features of the '<em>Solution Option Micro Case</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION_MICRO_CASE_FEATURE_COUNT = 4;

	/**
	 * The feature id for the '<em><b>Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_DESCRIPTION__CHANGES = 0;

	/**
	 * The number of structural features of the '<em>Change Description</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_DESCRIPTION_FEATURE_COUNT = 1;

	/**
	 * The number of structural features of the '<em>Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_FEATURE_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Slot Descriptor</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR = CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Open Slot Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SLOT_CHANGE_FEATURE_COUNT = CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Slot Descriptors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_CHANGE__SLOT_DESCRIPTORS = CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vessel Allocation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_CHANGE__VESSEL_ALLOCATION = CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_CHANGE__POSITION = CHANGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Cargo Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_CHANGE_FEATURE_COUNT = CHANGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Vessel Event Descriptor</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR = CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vessel Allocation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION = CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_CHANGE__POSITION = CHANGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Vessel Event Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_CHANGE_FEATURE_COUNT = CHANGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Event Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_DESCRIPTOR__EVENT_NAME = 0;

	/**
	 * The number of structural features of the '<em>Vessel Event Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_DESCRIPTOR_FEATURE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Slot Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_DESCRIPTOR__SLOT_TYPE = 0;

	/**
	 * The number of structural features of the '<em>Slot Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_DESCRIPTOR_FEATURE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Slot Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_SLOT_DESCRIPTOR__SLOT_TYPE = SLOT_DESCRIPTOR__SLOT_TYPE;

	/**
	 * The feature id for the '<em><b>Slot Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_SLOT_DESCRIPTOR__SLOT_NAME = SLOT_DESCRIPTOR_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Real Slot Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_SLOT_DESCRIPTOR_FEATURE_COUNT = SLOT_DESCRIPTOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Slot Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_SLOT_DESCRIPTOR__SLOT_TYPE = SLOT_DESCRIPTOR__SLOT_TYPE;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_SLOT_DESCRIPTOR__DATE = SLOT_DESCRIPTOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Market Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_SLOT_DESCRIPTOR__MARKET_NAME = SLOT_DESCRIPTOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Port Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_SLOT_DESCRIPTOR__PORT_NAME = SLOT_DESCRIPTOR_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Spot Market Slot Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_MARKET_SLOT_DESCRIPTOR_FEATURE_COUNT = SLOT_DESCRIPTOR_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Vessel Allocation Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_ALLOCATION_DESCRIPTOR_FEATURE_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Market Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_VESSEL_ALLOCATION_DESCRIPTOR__MARKET_NAME = VESSEL_ALLOCATION_DESCRIPTOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_VESSEL_ALLOCATION_DESCRIPTOR__SPOT_INDEX = VESSEL_ALLOCATION_DESCRIPTOR_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Market Vessel Allocation Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_VESSEL_ALLOCATION_DESCRIPTOR_FEATURE_COUNT = VESSEL_ALLOCATION_DESCRIPTOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_VESSEL_ALLOCATION_DESCRIPTOR__NAME = VESSEL_ALLOCATION_DESCRIPTOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Charter Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_VESSEL_ALLOCATION_DESCRIPTOR__CHARTER_INDEX = VESSEL_ALLOCATION_DESCRIPTOR_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Fleet Vessel Allocation Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_VESSEL_ALLOCATION_DESCRIPTOR_FEATURE_COUNT = VESSEL_ALLOCATION_DESCRIPTOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POSITION_DESCRIPTOR__AFTER = 0;

	/**
	 * The feature id for the '<em><b>Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POSITION_DESCRIPTOR__BEFORE = 1;

	/**
	 * The number of structural features of the '<em>Position Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POSITION_DESCRIPTOR_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ViabilityModelImpl <em>Viability Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ViabilityModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getViabilityModel()
	 * @generated
	 */
	int VIABILITY_MODEL = 54;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__EXTENSIONS = ABSTRACT_ANALYSIS_MODEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__UUID = ABSTRACT_ANALYSIS_MODEL__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__NAME = ABSTRACT_ANALYSIS_MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Buys</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__BUYS = ABSTRACT_ANALYSIS_MODEL__BUYS;

	/**
	 * The feature id for the '<em><b>Sells</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__SELLS = ABSTRACT_ANALYSIS_MODEL__SELLS;

	/**
	 * The feature id for the '<em><b>Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__VESSEL_EVENTS = ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS;

	/**
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__SHIPPING_TEMPLATES = ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES;

	/**
	 * The feature id for the '<em><b>Commodity Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__COMMODITY_CURVES = ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__ROWS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Markets</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__MARKETS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Viability Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL_FEATURE_COUNT = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityModelImpl <em>Marketability Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityModel()
	 * @generated
	 */
	int MARKETABILITY_MODEL = 75;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl <em>Marketability Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityRow()
	 * @generated
	 */
	int MARKETABILITY_ROW = 76;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultImpl <em>Marketability Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityResult()
	 * @generated
	 */
	int MARKETABILITY_RESULT = 77;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityEventImpl <em>Marketability Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityEventImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityEvent()
	 * @generated
	 */
	int MARKETABILITY_EVENT = 79;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityAssignableElementImpl <em>Marketability Assignable Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityAssignableElementImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityAssignableElement()
	 * @generated
	 */
	int MARKETABILITY_ASSIGNABLE_ELEMENT = 80;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityEndEventImpl <em>Marketability End Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityEndEventImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityEndEvent()
	 * @generated
	 */
	int MARKETABILITY_END_EVENT = 81;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl <em>Viability Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getViabilityRow()
	 * @generated
	 */
	int VIABILITY_ROW = 55;

	/**
	 * The feature id for the '<em><b>Buy Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_ROW__BUY_OPTION = 0;

	/**
	 * The feature id for the '<em><b>Sell Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_ROW__SELL_OPTION = 1;

	/**
	 * The feature id for the '<em><b>Shipping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_ROW__SHIPPING = 2;

	/**
	 * The feature id for the '<em><b>Lhs Results</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_ROW__LHS_RESULTS = 3;

	/**
	 * The feature id for the '<em><b>Rhs Results</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_ROW__RHS_RESULTS = 4;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_ROW__TARGET = 5;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_ROW__PRICE = 6;

	/**
	 * The feature id for the '<em><b>Eta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_ROW__ETA = 7;

	/**
	 * The feature id for the '<em><b>Reference Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_ROW__REFERENCE_PRICE = 8;

	/**
	 * The feature id for the '<em><b>Start Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_ROW__START_VOLUME = 9;

	/**
	 * The number of structural features of the '<em>Viability Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_ROW_FEATURE_COUNT = 10;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ViabilityResultImpl <em>Viability Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ViabilityResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getViabilityResult()
	 * @generated
	 */
	int VIABILITY_RESULT = 56;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_RESULT__TARGET = 0;

	/**
	 * The feature id for the '<em><b>Earliest ETA</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_RESULT__EARLIEST_ETA = 1;

	/**
	 * The feature id for the '<em><b>Latest ETA</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_RESULT__LATEST_ETA = 2;

	/**
	 * The feature id for the '<em><b>Earliest Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_RESULT__EARLIEST_VOLUME = 3;

	/**
	 * The feature id for the '<em><b>Latest Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_RESULT__LATEST_VOLUME = 4;

	/**
	 * The feature id for the '<em><b>Earliest Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_RESULT__EARLIEST_PRICE = 5;

	/**
	 * The feature id for the '<em><b>Latest Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_RESULT__LATEST_PRICE = 6;

	/**
	 * The number of structural features of the '<em>Viability Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_RESULT_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MTMModelImpl <em>MTM Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MTMModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMTMModel()
	 * @generated
	 */
	int MTM_MODEL = 57;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__EXTENSIONS = ABSTRACT_ANALYSIS_MODEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__UUID = ABSTRACT_ANALYSIS_MODEL__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__NAME = ABSTRACT_ANALYSIS_MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Buys</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__BUYS = ABSTRACT_ANALYSIS_MODEL__BUYS;

	/**
	 * The feature id for the '<em><b>Sells</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__SELLS = ABSTRACT_ANALYSIS_MODEL__SELLS;

	/**
	 * The feature id for the '<em><b>Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__VESSEL_EVENTS = ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS;

	/**
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__SHIPPING_TEMPLATES = ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES;

	/**
	 * The feature id for the '<em><b>Commodity Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__COMMODITY_CURVES = ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__ROWS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Markets</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__MARKETS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Nominal Markets</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__NOMINAL_MARKETS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>MTM Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL_FEATURE_COUNT = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MTMResultImpl <em>MTM Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MTMResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMTMResult()
	 * @generated
	 */
	int MTM_RESULT = 58;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_RESULT__TARGET = 0;

	/**
	 * The feature id for the '<em><b>Earliest ETA</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_RESULT__EARLIEST_ETA = 1;

	/**
	 * The feature id for the '<em><b>Earliest Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_RESULT__EARLIEST_VOLUME = 2;

	/**
	 * The feature id for the '<em><b>Earliest Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_RESULT__EARLIEST_PRICE = 3;

	/**
	 * The feature id for the '<em><b>Shipping</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_RESULT__SHIPPING = 4;

	/**
	 * The feature id for the '<em><b>Shipping Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_RESULT__SHIPPING_COST = 5;

	/**
	 * The feature id for the '<em><b>Original Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_RESULT__ORIGINAL_VOLUME = 6;

	/**
	 * The feature id for the '<em><b>Original Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_RESULT__ORIGINAL_PRICE = 7;

	/**
	 * The feature id for the '<em><b>Total Shipping Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_RESULT__TOTAL_SHIPPING_COST = 8;

	/**
	 * The number of structural features of the '<em>MTM Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_RESULT_FEATURE_COUNT = 9;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MTMRowImpl <em>MTM Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MTMRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMTMRow()
	 * @generated
	 */
	int MTM_ROW = 59;

	/**
	 * The feature id for the '<em><b>Buy Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_ROW__BUY_OPTION = 0;

	/**
	 * The feature id for the '<em><b>Sell Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_ROW__SELL_OPTION = 1;

	/**
	 * The feature id for the '<em><b>Lhs Results</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_ROW__LHS_RESULTS = 2;

	/**
	 * The feature id for the '<em><b>Rhs Results</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_ROW__RHS_RESULTS = 3;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_ROW__TARGET = 4;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_ROW__PRICE = 5;

	/**
	 * The feature id for the '<em><b>Eta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_ROW__ETA = 6;

	/**
	 * The feature id for the '<em><b>Reference Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_ROW__REFERENCE_PRICE = 7;

	/**
	 * The feature id for the '<em><b>Start Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_ROW__START_VOLUME = 8;

	/**
	 * The number of structural features of the '<em>MTM Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_ROW_FEATURE_COUNT = 9;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisModelImpl <em>Break Even Analysis Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBreakEvenAnalysisModel()
	 * @generated
	 */
	int BREAK_EVEN_ANALYSIS_MODEL = 60;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__EXTENSIONS = ABSTRACT_ANALYSIS_MODEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__UUID = ABSTRACT_ANALYSIS_MODEL__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__NAME = ABSTRACT_ANALYSIS_MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Buys</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__BUYS = ABSTRACT_ANALYSIS_MODEL__BUYS;

	/**
	 * The feature id for the '<em><b>Sells</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__SELLS = ABSTRACT_ANALYSIS_MODEL__SELLS;

	/**
	 * The feature id for the '<em><b>Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__VESSEL_EVENTS = ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS;

	/**
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__SHIPPING_TEMPLATES = ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES;

	/**
	 * The feature id for the '<em><b>Commodity Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__COMMODITY_CURVES = ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__ROWS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Markets</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__MARKETS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Break Even Analysis Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL_FEATURE_COUNT = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisRowImpl <em>Break Even Analysis Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBreakEvenAnalysisRow()
	 * @generated
	 */
	int BREAK_EVEN_ANALYSIS_ROW = 61;

	/**
	 * The feature id for the '<em><b>Buy Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION = 0;

	/**
	 * The feature id for the '<em><b>Sell Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION = 1;

	/**
	 * The feature id for the '<em><b>Shipping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_ROW__SHIPPING = 2;

	/**
	 * The feature id for the '<em><b>Lhs Results</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS = 3;

	/**
	 * The feature id for the '<em><b>Rhs Results</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS = 4;

	/**
	 * The feature id for the '<em><b>Lhs Based On</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON = 5;

	/**
	 * The feature id for the '<em><b>Rhs Based On</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON = 6;

	/**
	 * The number of structural features of the '<em>Break Even Analysis Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_ROW_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultSetImpl <em>Break Even Analysis Result Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultSetImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBreakEvenAnalysisResultSet()
	 * @generated
	 */
	int BREAK_EVEN_ANALYSIS_RESULT_SET = 62;

	/**
	 * The feature id for the '<em><b>Based On</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_RESULT_SET__BASED_ON = 0;

	/**
	 * The feature id for the '<em><b>Results</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_RESULT_SET__RESULTS = 1;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_RESULT_SET__PRICE = 2;

	/**
	 * The number of structural features of the '<em>Break Even Analysis Result Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_RESULT_SET_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultImpl <em>Break Even Analysis Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBreakEvenAnalysisResult()
	 * @generated
	 */
	int BREAK_EVEN_ANALYSIS_RESULT = 63;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_RESULT__TARGET = 0;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_RESULT__PRICE = 1;

	/**
	 * The feature id for the '<em><b>Eta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_RESULT__ETA = 2;

	/**
	 * The feature id for the '<em><b>Reference Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_RESULT__REFERENCE_PRICE = 3;

	/**
	 * The number of structural features of the '<em>Break Even Analysis Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_RESULT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.LocalDateTimeHolderImpl <em>Local Date Time Holder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.LocalDateTimeHolderImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getLocalDateTimeHolder()
	 * @generated
	 */
	int LOCAL_DATE_TIME_HOLDER = 64;

	/**
	 * The feature id for the '<em><b>Date Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_DATE_TIME_HOLDER__DATE_TIME = 0;

	/**
	 * The number of structural features of the '<em>Local Date Time Holder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_DATE_TIME_HOLDER_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.CommodityCurveOptionImpl <em>Commodity Curve Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.CommodityCurveOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCommodityCurveOption()
	 * @generated
	 */
	int COMMODITY_CURVE_OPTION = 65;

	/**
	 * The number of structural features of the '<em>Commodity Curve Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE_OPTION_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.CommodityCurveOverlayImpl <em>Commodity Curve Overlay</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.CommodityCurveOverlayImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCommodityCurveOverlay()
	 * @generated
	 */
	int COMMODITY_CURVE_OVERLAY = 66;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE_OVERLAY__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE_OVERLAY__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Reference Curve</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE_OVERLAY__REFERENCE_CURVE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Alternative Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Commodity Curve Overlay</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMODITY_CURVE_OVERLAY_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SensitivityModelImpl <em>Sensitivity Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SensitivityModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSensitivityModel()
	 * @generated
	 */
	int SENSITIVITY_MODEL = 67;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Sensitivity Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_MODEL__SENSITIVITY_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sensitivity Solution</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_MODEL__SENSITIVITY_SOLUTION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Sensitivity Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SensitivitySolutionSetImpl <em>Sensitivity Solution Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SensitivitySolutionSetImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSensitivitySolutionSet()
	 * @generated
	 */
	int SENSITIVITY_SOLUTION_SET = 68;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__EXTENSIONS = ABSTRACT_SOLUTION_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__UUID = ABSTRACT_SOLUTION_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__NAME = ABSTRACT_SOLUTION_SET__NAME;

	/**
	 * The feature id for the '<em><b>Has Dual Mode Solutions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS = ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS;

	/**
	 * The feature id for the '<em><b>Portfolio Break Even Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE = ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE;

	/**
	 * The feature id for the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__USER_SETTINGS = ABSTRACT_SOLUTION_SET__USER_SETTINGS;

	/**
	 * The feature id for the '<em><b>Extra Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__EXTRA_SLOTS = ABSTRACT_SOLUTION_SET__EXTRA_SLOTS;

	/**
	 * The feature id for the '<em><b>Base Option</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__BASE_OPTION = ABSTRACT_SOLUTION_SET__BASE_OPTION;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__OPTIONS = ABSTRACT_SOLUTION_SET__OPTIONS;

	/**
	 * The feature id for the '<em><b>Extra Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__EXTRA_VESSEL_EVENTS = ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS;

	/**
	 * The feature id for the '<em><b>Extra Vessel Charters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__EXTRA_VESSEL_CHARTERS = ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_CHARTERS;

	/**
	 * The feature id for the '<em><b>Charter In Market Overrides</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES = ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES;

	/**
	 * The feature id for the '<em><b>Extra Charter In Markets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS = ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS;

	/**
	 * The feature id for the '<em><b>Use Scenario Base</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__USE_SCENARIO_BASE = ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE;

	/**
	 * The feature id for the '<em><b>Porfolio Pn LResult</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Cargo Pn LResults</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET__CARGO_PN_LRESULTS = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Sensitivity Solution Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SENSITIVITY_SOLUTION_SET_FEATURE_COUNT = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.AbstractSensitivityResultImpl <em>Abstract Sensitivity Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.AbstractSensitivityResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAbstractSensitivityResult()
	 * @generated
	 */
	int ABSTRACT_SENSITIVITY_RESULT = 69;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SENSITIVITY_RESULT__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SENSITIVITY_RESULT__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Min Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Max Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Average Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Variance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SENSITIVITY_RESULT__VARIANCE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Abstract Sensitivity Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SENSITIVITY_RESULT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.PortfolioSensitivityResultImpl <em>Portfolio Sensitivity Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.PortfolioSensitivityResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPortfolioSensitivityResult()
	 * @generated
	 */
	int PORTFOLIO_SENSITIVITY_RESULT = 70;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTFOLIO_SENSITIVITY_RESULT__EXTENSIONS = ABSTRACT_SENSITIVITY_RESULT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTFOLIO_SENSITIVITY_RESULT__UUID = ABSTRACT_SENSITIVITY_RESULT__UUID;

	/**
	 * The feature id for the '<em><b>Min Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTFOLIO_SENSITIVITY_RESULT__MIN_PN_L = ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L;

	/**
	 * The feature id for the '<em><b>Max Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTFOLIO_SENSITIVITY_RESULT__MAX_PN_L = ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L;

	/**
	 * The feature id for the '<em><b>Average Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTFOLIO_SENSITIVITY_RESULT__AVERAGE_PN_L = ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L;

	/**
	 * The feature id for the '<em><b>Variance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTFOLIO_SENSITIVITY_RESULT__VARIANCE = ABSTRACT_SENSITIVITY_RESULT__VARIANCE;

	/**
	 * The number of structural features of the '<em>Portfolio Sensitivity Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORTFOLIO_SENSITIVITY_RESULT_FEATURE_COUNT = ABSTRACT_SENSITIVITY_RESULT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.CargoPnLResultImpl <em>Cargo Pn LResult</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.CargoPnLResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCargoPnLResult()
	 * @generated
	 */
	int CARGO_PN_LRESULT = 71;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_PN_LRESULT__EXTENSIONS = ABSTRACT_SENSITIVITY_RESULT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_PN_LRESULT__UUID = ABSTRACT_SENSITIVITY_RESULT__UUID;

	/**
	 * The feature id for the '<em><b>Min Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_PN_LRESULT__MIN_PN_L = ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L;

	/**
	 * The feature id for the '<em><b>Max Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_PN_LRESULT__MAX_PN_L = ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L;

	/**
	 * The feature id for the '<em><b>Average Pn L</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_PN_LRESULT__AVERAGE_PN_L = ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L;

	/**
	 * The feature id for the '<em><b>Variance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_PN_LRESULT__VARIANCE = ABSTRACT_SENSITIVITY_RESULT__VARIANCE;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_PN_LRESULT__CARGO = ABSTRACT_SENSITIVITY_RESULT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Cargo Pn LResult</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_PN_LRESULT_FEATURE_COUNT = ABSTRACT_SENSITIVITY_RESULT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl <em>Swap Value Matrix Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixModel()
	 * @generated
	 */
	int SWAP_VALUE_MATRIX_MODEL = 72;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_MODEL__EXTENSIONS = ABSTRACT_ANALYSIS_MODEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_MODEL__UUID = ABSTRACT_ANALYSIS_MODEL__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_MODEL__NAME = ABSTRACT_ANALYSIS_MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Buys</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_MODEL__BUYS = ABSTRACT_ANALYSIS_MODEL__BUYS;

	/**
	 * The feature id for the '<em><b>Sells</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_MODEL__SELLS = ABSTRACT_ANALYSIS_MODEL__SELLS;

	/**
	 * The feature id for the '<em><b>Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_MODEL__VESSEL_EVENTS = ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS;

	/**
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_MODEL__SHIPPING_TEMPLATES = ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES;

	/**
	 * The feature id for the '<em><b>Commodity Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_MODEL__COMMODITY_CURVES = ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES;

	/**
	 * The feature id for the '<em><b>Swap Value Matrix Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_MODEL__PARAMETERS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Swap Value Matrix Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_MODEL_FEATURE_COUNT = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl <em>Swap Value Matrix Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixResult()
	 * @generated
	 */
	int SWAP_VALUE_MATRIX_RESULT = 73;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Swap Pnl Minus Base Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Base Preceding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Base Succeeding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Swap Preceding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Swap Succeeding Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Base Cargo</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT__BASE_CARGO = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Swap Diversion Cargo</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT__SWAP_DIVERSION_CARGO = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Swap Backfill Cargo</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Swap Value Matrix Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultSetImpl <em>Swap Value Matrix Result Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultSetImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixResultSet()
	 * @generated
	 */
	int SWAP_VALUE_MATRIX_RESULT_SET = 74;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT_SET__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT_SET__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Results</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT_SET__RESULTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Swap Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT_SET__SWAP_FEE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Generated Spot Load Slot</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Generated Spot Discharge Slot</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Non Vessel Charter Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT_SET__NON_VESSEL_CHARTER_PNL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Swap Value Matrix Result Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_RESULT_SET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL__EXTENSIONS = ABSTRACT_ANALYSIS_MODEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL__UUID = ABSTRACT_ANALYSIS_MODEL__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL__NAME = ABSTRACT_ANALYSIS_MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Buys</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL__BUYS = ABSTRACT_ANALYSIS_MODEL__BUYS;

	/**
	 * The feature id for the '<em><b>Sells</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL__SELLS = ABSTRACT_ANALYSIS_MODEL__SELLS;

	/**
	 * The feature id for the '<em><b>Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL__VESSEL_EVENTS = ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS;

	/**
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL__SHIPPING_TEMPLATES = ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES;

	/**
	 * The feature id for the '<em><b>Commodity Curves</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL__COMMODITY_CURVES = ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL__ROWS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Markets</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL__MARKETS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Vessel Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL__VESSEL_SPEED = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Marketability Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_MODEL_FEATURE_COUNT = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Buy Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_ROW__BUY_OPTION = 0;

	/**
	 * The feature id for the '<em><b>Sell Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_ROW__SELL_OPTION = 1;

	/**
	 * The feature id for the '<em><b>Shipping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_ROW__SHIPPING = 2;

	/**
	 * The feature id for the '<em><b>Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_ROW__RESULT = 3;

	/**
	 * The number of structural features of the '<em>Marketability Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_ROW_FEATURE_COUNT = 4;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_RESULT__TARGET = 0;

	/**
	 * The feature id for the '<em><b>Earliest ETA</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_RESULT__EARLIEST_ETA = 1;

	/**
	 * The feature id for the '<em><b>Latest ETA</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_RESULT__LATEST_ETA = 2;

	/**
	 * The number of structural features of the '<em>Marketability Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_RESULT_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Rhs Results</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_RESULT_CONTAINER__RHS_RESULTS = 0;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_RESULT_CONTAINER__NEXT_EVENT = 1;

	/**
	 * The feature id for the '<em><b>Buy Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_RESULT_CONTAINER__BUY_DATE = 2;

	/**
	 * The feature id for the '<em><b>Sell Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_RESULT_CONTAINER__SELL_DATE = 3;

	/**
	 * The feature id for the '<em><b>Laden Panama</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA = 4;

	/**
	 * The feature id for the '<em><b>Ballast Panama</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA = 5;

	/**
	 * The number of structural features of the '<em>Marketability Result Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_RESULT_CONTAINER_FEATURE_COUNT = 6;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_EVENT__START = 0;

	/**
	 * The number of structural features of the '<em>Marketability Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_EVENT_FEATURE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_ASSIGNABLE_ELEMENT__START = MARKETABILITY_EVENT__START;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_ASSIGNABLE_ELEMENT__ELEMENT = MARKETABILITY_EVENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Marketability Assignable Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_ASSIGNABLE_ELEMENT_FEATURE_COUNT = MARKETABILITY_EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_END_EVENT__START = MARKETABILITY_EVENT__START;

	/**
	 * The number of structural features of the '<em>Marketability End Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKETABILITY_END_EVENT_FEATURE_COUNT = MARKETABILITY_EVENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl <em>Swap Value Matrix Parameters</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixParameters()
	 * @generated
	 */
	int SWAP_VALUE_MATRIX_PARAMETERS = 82;

	/**
	 * The feature id for the '<em><b>Base Load</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_PARAMETERS__BASE_LOAD = 0;

	/**
	 * The feature id for the '<em><b>Base Discharge</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_PARAMETERS__BASE_DISCHARGE = 1;

	/**
	 * The feature id for the '<em><b>Base Vessel Charter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_PARAMETERS__BASE_VESSEL_CHARTER = 2;

	/**
	 * The feature id for the '<em><b>Base Price Range</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_PARAMETERS__BASE_PRICE_RANGE = 3;

	/**
	 * The feature id for the '<em><b>Swap Load Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_PARAMETERS__SWAP_LOAD_MARKET = 4;

	/**
	 * The feature id for the '<em><b>Swap Discharge Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_PARAMETERS__SWAP_DISCHARGE_MARKET = 5;

	/**
	 * The feature id for the '<em><b>Swap Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_PARAMETERS__SWAP_FEE = 6;

	/**
	 * The feature id for the '<em><b>Swap Price Range</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_PARAMETERS__SWAP_PRICE_RANGE = 7;

	/**
	 * The number of structural features of the '<em>Swap Value Matrix Parameters</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_PARAMETERS_FEATURE_COUNT = 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.RangeImpl <em>Range</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.RangeImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getRange()
	 * @generated
	 */
	int RANGE = 83;

	/**
	 * The feature id for the '<em><b>Min</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANGE__MIN = 0;

	/**
	 * The feature id for the '<em><b>Max</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANGE__MAX = 1;

	/**
	 * The feature id for the '<em><b>Step Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANGE__STEP_SIZE = 2;

	/**
	 * The number of structural features of the '<em>Range</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANGE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixCargoResultImpl <em>Swap Value Matrix Cargo Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixCargoResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixCargoResult()
	 * @generated
	 */
	int SWAP_VALUE_MATRIX_CARGO_RESULT = 84;

	/**
	 * The feature id for the '<em><b>Load Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_CARGO_RESULT__LOAD_PRICE = 0;

	/**
	 * The feature id for the '<em><b>Discharge Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_CARGO_RESULT__DISCHARGE_PRICE = 1;

	/**
	 * The feature id for the '<em><b>Purchase Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_CARGO_RESULT__PURCHASE_COST = 2;

	/**
	 * The feature id for the '<em><b>Sales Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_CARGO_RESULT__SALES_REVENUE = 3;

	/**
	 * The feature id for the '<em><b>Additional Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_CARGO_RESULT__ADDITIONAL_PNL = 4;

	/**
	 * The feature id for the '<em><b>Total Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_CARGO_RESULT__TOTAL_PNL = 5;

	/**
	 * The number of structural features of the '<em>Swap Value Matrix Cargo Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_CARGO_RESULT_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixShippedCargoResultImpl <em>Swap Value Matrix Shipped Cargo Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixShippedCargoResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixShippedCargoResult()
	 * @generated
	 */
	int SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT = 85;

	/**
	 * The feature id for the '<em><b>Load Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__LOAD_PRICE = SWAP_VALUE_MATRIX_CARGO_RESULT__LOAD_PRICE;

	/**
	 * The feature id for the '<em><b>Discharge Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__DISCHARGE_PRICE = SWAP_VALUE_MATRIX_CARGO_RESULT__DISCHARGE_PRICE;

	/**
	 * The feature id for the '<em><b>Purchase Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__PURCHASE_COST = SWAP_VALUE_MATRIX_CARGO_RESULT__PURCHASE_COST;

	/**
	 * The feature id for the '<em><b>Sales Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__SALES_REVENUE = SWAP_VALUE_MATRIX_CARGO_RESULT__SALES_REVENUE;

	/**
	 * The feature id for the '<em><b>Additional Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__ADDITIONAL_PNL = SWAP_VALUE_MATRIX_CARGO_RESULT__ADDITIONAL_PNL;

	/**
	 * The feature id for the '<em><b>Total Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__TOTAL_PNL = SWAP_VALUE_MATRIX_CARGO_RESULT__TOTAL_PNL;

	/**
	 * The feature id for the '<em><b>Shipping Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__SHIPPING_COST = SWAP_VALUE_MATRIX_CARGO_RESULT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Load Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__LOAD_VOLUME = SWAP_VALUE_MATRIX_CARGO_RESULT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Discharge Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__DISCHARGE_VOLUME = SWAP_VALUE_MATRIX_CARGO_RESULT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Swap Value Matrix Shipped Cargo Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT_FEATURE_COUNT = SWAP_VALUE_MATRIX_CARGO_RESULT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixNonShippedCargoResultImpl <em>Swap Value Matrix Non Shipped Cargo Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixNonShippedCargoResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixNonShippedCargoResult()
	 * @generated
	 */
	int SWAP_VALUE_MATRIX_NON_SHIPPED_CARGO_RESULT = 86;

	/**
	 * The feature id for the '<em><b>Load Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_NON_SHIPPED_CARGO_RESULT__LOAD_PRICE = SWAP_VALUE_MATRIX_CARGO_RESULT__LOAD_PRICE;

	/**
	 * The feature id for the '<em><b>Discharge Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_NON_SHIPPED_CARGO_RESULT__DISCHARGE_PRICE = SWAP_VALUE_MATRIX_CARGO_RESULT__DISCHARGE_PRICE;

	/**
	 * The feature id for the '<em><b>Purchase Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_NON_SHIPPED_CARGO_RESULT__PURCHASE_COST = SWAP_VALUE_MATRIX_CARGO_RESULT__PURCHASE_COST;

	/**
	 * The feature id for the '<em><b>Sales Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_NON_SHIPPED_CARGO_RESULT__SALES_REVENUE = SWAP_VALUE_MATRIX_CARGO_RESULT__SALES_REVENUE;

	/**
	 * The feature id for the '<em><b>Additional Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_NON_SHIPPED_CARGO_RESULT__ADDITIONAL_PNL = SWAP_VALUE_MATRIX_CARGO_RESULT__ADDITIONAL_PNL;

	/**
	 * The feature id for the '<em><b>Total Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_NON_SHIPPED_CARGO_RESULT__TOTAL_PNL = SWAP_VALUE_MATRIX_CARGO_RESULT__TOTAL_PNL;

	/**
	 * The feature id for the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_NON_SHIPPED_CARGO_RESULT__VOLUME = SWAP_VALUE_MATRIX_CARGO_RESULT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Swap Value Matrix Non Shipped Cargo Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWAP_VALUE_MATRIX_NON_SHIPPED_CARGO_RESULT_FEATURE_COUNT = SWAP_VALUE_MATRIX_CARGO_RESULT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.VolumeMode <em>Volume Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.VolumeMode
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVolumeMode()
	 * @generated
	 */
	int VOLUME_MODE = 87;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.SlotType <em>Slot Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.SlotType
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSlotType()
	 * @generated
	 */
	int SLOT_TYPE = 88;


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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getOptionModels <em>Option Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Option Models</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel#getOptionModels()
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	EReference getAnalyticsModel_OptionModels();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getOptimisations <em>Optimisations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Optimisations</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel#getOptimisations()
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	EReference getAnalyticsModel_Optimisations();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getViabilityModel <em>Viability Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Viability Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel#getViabilityModel()
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	EReference getAnalyticsModel_ViabilityModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getMtmModel <em>Mtm Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Mtm Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel#getMtmModel()
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	EReference getAnalyticsModel_MtmModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getBreakevenModels <em>Breakeven Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Breakeven Models</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel#getBreakevenModels()
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	EReference getAnalyticsModel_BreakevenModels();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getSwapValueMatrixModels <em>Swap Value Matrix Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Swap Value Matrix Models</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel#getSwapValueMatrixModels()
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	EReference getAnalyticsModel_SwapValueMatrixModels();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getMarketabilityModel <em>Marketability Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Marketability Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel#getMarketabilityModel()
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	EReference getAnalyticsModel_MarketabilityModel();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.OpenSell <em>Open Sell</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Open Sell</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OpenSell
	 * @generated
	 */
	EClass getOpenSell();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.OpenBuy <em>Open Buy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Open Buy</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OpenBuy
	 * @generated
	 */
	EClass getOpenBuy();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getCv <em>Cv</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cv</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getCv()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_Cv();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getCancellationExpression <em>Cancellation Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cancellation Expression</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getCancellationExpression()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_CancellationExpression();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getMiscCosts <em>Misc Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Misc Costs</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getMiscCosts()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_MiscCosts();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getVolumeMode <em>Volume Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Mode</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getVolumeMode()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_VolumeMode();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getVolumeUnits <em>Volume Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Units</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getVolumeUnits()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_VolumeUnits();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getMinVolume <em>Min Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getMinVolume()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_MinVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getMaxVolume <em>Max Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getMaxVolume()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_MaxVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#isSpecifyWindow <em>Specify Window</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Specify Window</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#isSpecifyWindow()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_SpecifyWindow();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getWindowSize <em>Window Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Size</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getWindowSize()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_WindowSize();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getWindowSizeUnits <em>Window Size Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Size Units</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getWindowSizeUnits()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_WindowSizeUnits();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity#getName()
	 * @see #getBuyOpportunity()
	 * @generated
	 */
	EAttribute getBuyOpportunity_Name();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getCancellationExpression <em>Cancellation Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cancellation Expression</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getCancellationExpression()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_CancellationExpression();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getMiscCosts <em>Misc Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Misc Costs</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getMiscCosts()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_MiscCosts();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getVolumeMode <em>Volume Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Mode</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getVolumeMode()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_VolumeMode();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getVolumeUnits <em>Volume Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Units</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getVolumeUnits()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_VolumeUnits();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getMinVolume <em>Min Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getMinVolume()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_MinVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getMaxVolume <em>Max Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getMaxVolume()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_MaxVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#isSpecifyWindow <em>Specify Window</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Specify Window</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#isSpecifyWindow()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_SpecifyWindow();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getWindowSize <em>Window Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Size</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getWindowSize()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_WindowSize();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getWindowSizeUnits <em>Window Size Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Size Units</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getWindowSizeUnits()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_WindowSizeUnits();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellOpportunity#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity#getName()
	 * @see #getSellOpportunity()
	 * @generated
	 */
	EAttribute getSellOpportunity_Name();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BuyMarket#getMonth <em>Month</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Month</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BuyMarket#getMonth()
	 * @see #getBuyMarket()
	 * @generated
	 */
	EAttribute getBuyMarket_Month();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SellMarket#getMonth <em>Month</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Month</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SellMarket#getMonth()
	 * @see #getSellMarket()
	 * @generated
	 */
	EAttribute getSellMarket_Month();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.VesselEventOption <em>Vessel Event Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Event Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VesselEventOption
	 * @generated
	 */
	EClass getVesselEventOption();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.VesselEventReference <em>Vessel Event Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Event Reference</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VesselEventReference
	 * @generated
	 */
	EClass getVesselEventReference();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.VesselEventReference#getEvent <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Event</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VesselEventReference#getEvent()
	 * @see #getVesselEventReference()
	 * @generated
	 */
	EReference getVesselEventReference_Event();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity <em>Charter Out Opportunity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Charter Out Opportunity</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CharterOutOpportunity
	 * @generated
	 */
	EClass getCharterOutOpportunity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getHireCost <em>Hire Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hire Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getHireCost()
	 * @see #getCharterOutOpportunity()
	 * @generated
	 */
	EAttribute getCharterOutOpportunity_HireCost();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getPort()
	 * @see #getCharterOutOpportunity()
	 * @generated
	 */
	EReference getCharterOutOpportunity_Port();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getDate()
	 * @see #getCharterOutOpportunity()
	 * @generated
	 */
	EAttribute getCharterOutOpportunity_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CharterOutOpportunity#getDuration()
	 * @see #getCharterOutOpportunity()
	 * @generated
	 */
	EAttribute getCharterOutOpportunity_Duration();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getVesselEventOption <em>Vessel Event Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Event Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRow#getVesselEventOption()
	 * @see #getBaseCaseRow()
	 * @generated
	 */
	EReference getBaseCaseRow_VesselEventOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getShipping <em>Shipping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Shipping</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRow#getShipping()
	 * @see #getBaseCaseRow()
	 * @generated
	 */
	EReference getBaseCaseRow_Shipping();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#isOptionise <em>Optionise</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Optionise</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRow#isOptionise()
	 * @see #getBaseCaseRow()
	 * @generated
	 */
	EAttribute getBaseCaseRow_Optionise();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getOptions <em>Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Options</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRow#getOptions()
	 * @see #getBaseCaseRow()
	 * @generated
	 */
	EReference getBaseCaseRow_Options();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#isFreeze <em>Freeze</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Freeze</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRow#isFreeze()
	 * @see #getBaseCaseRow()
	 * @generated
	 */
	EAttribute getBaseCaseRow_Freeze();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions <em>Base Case Row Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Case Row Options</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRowOptions
	 * @generated
	 */
	EClass getBaseCaseRowOptions();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenRoute <em>Laden Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden Route</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenRoute()
	 * @see #getBaseCaseRowOptions()
	 * @generated
	 */
	EAttribute getBaseCaseRowOptions_LadenRoute();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastRoute <em>Ballast Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Route</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastRoute()
	 * @see #getBaseCaseRowOptions()
	 * @generated
	 */
	EAttribute getBaseCaseRowOptions_BallastRoute();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenFuelChoice <em>Laden Fuel Choice</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden Fuel Choice</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLadenFuelChoice()
	 * @see #getBaseCaseRowOptions()
	 * @generated
	 */
	EAttribute getBaseCaseRowOptions_LadenFuelChoice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastFuelChoice <em>Ballast Fuel Choice</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Fuel Choice</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getBallastFuelChoice()
	 * @see #getBaseCaseRowOptions()
	 * @generated
	 */
	EAttribute getBaseCaseRowOptions_BallastFuelChoice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLoadDate <em>Load Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Date</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getLoadDate()
	 * @see #getBaseCaseRowOptions()
	 * @generated
	 */
	EAttribute getBaseCaseRowOptions_LoadDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getDischargeDate <em>Discharge Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discharge Date</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRowOptions#getDischargeDate()
	 * @see #getBaseCaseRowOptions()
	 * @generated
	 */
	EAttribute getBaseCaseRowOptions_DischargeDate();

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
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getVesselEventOptions <em>Vessel Event Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessel Event Options</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRow#getVesselEventOptions()
	 * @see #getPartialCaseRow()
	 * @generated
	 */
	EReference getPartialCaseRow_VesselEventOptions();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getShipping <em>Shipping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Shipping</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRow#getShipping()
	 * @see #getPartialCaseRow()
	 * @generated
	 */
	EReference getPartialCaseRow_Shipping();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getOptions <em>Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Options</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRow#getOptions()
	 * @see #getPartialCaseRow()
	 * @generated
	 */
	EReference getPartialCaseRow_Options();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.PartialCaseRow#getCommodityCurveOptions <em>Commodity Curve Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Commodity Curve Options</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRow#getCommodityCurveOptions()
	 * @see #getPartialCaseRow()
	 * @generated
	 */
	EReference getPartialCaseRow_CommodityCurveOptions();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions <em>Partial Case Row Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Partial Case Row Options</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRowOptions
	 * @generated
	 */
	EClass getPartialCaseRowOptions();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getLadenRoutes <em>Laden Routes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Laden Routes</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getLadenRoutes()
	 * @see #getPartialCaseRowOptions()
	 * @generated
	 */
	EAttribute getPartialCaseRowOptions_LadenRoutes();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getBallastRoutes <em>Ballast Routes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Ballast Routes</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getBallastRoutes()
	 * @see #getPartialCaseRowOptions()
	 * @generated
	 */
	EAttribute getPartialCaseRowOptions_BallastRoutes();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getLadenFuelChoices <em>Laden Fuel Choices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Laden Fuel Choices</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getLadenFuelChoices()
	 * @see #getPartialCaseRowOptions()
	 * @generated
	 */
	EAttribute getPartialCaseRowOptions_LadenFuelChoices();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getBallastFuelChoices <em>Ballast Fuel Choices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Ballast Fuel Choices</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getBallastFuelChoices()
	 * @see #getPartialCaseRowOptions()
	 * @generated
	 */
	EAttribute getPartialCaseRowOptions_BallastFuelChoices();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getLoadDates <em>Load Dates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Load Dates</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getLoadDates()
	 * @see #getPartialCaseRowOptions()
	 * @generated
	 */
	EReference getPartialCaseRowOptions_LoadDates();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getDischargeDates <em>Discharge Dates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Discharge Dates</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRowOptions#getDischargeDates()
	 * @see #getPartialCaseRowOptions()
	 * @generated
	 */
	EReference getPartialCaseRowOptions_DischargeDates();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption <em>Simple Vessel Charter Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Simple Vessel Charter Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption
	 * @generated
	 */
	EClass getSimpleVesselCharterOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption#getVessel()
	 * @see #getSimpleVesselCharterOption()
	 * @generated
	 */
	EReference getSimpleVesselCharterOption_Vessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption#getHireCost <em>Hire Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hire Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption#getHireCost()
	 * @see #getSimpleVesselCharterOption()
	 * @generated
	 */
	EAttribute getSimpleVesselCharterOption_HireCost();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption#getEntity()
	 * @see #getSimpleVesselCharterOption()
	 * @generated
	 */
	EReference getSimpleVesselCharterOption_Entity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption#isUseSafetyHeel <em>Use Safety Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Safety Heel</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption#isUseSafetyHeel()
	 * @see #getSimpleVesselCharterOption()
	 * @generated
	 */
	EAttribute getSimpleVesselCharterOption_UseSafetyHeel();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption <em>Optional Simple Vessel Charter Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Optional Simple Vessel Charter Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption
	 * @generated
	 */
	EClass getOptionalSimpleVesselCharterOption();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getBallastBonus <em>Ballast Bonus</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Bonus</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getBallastBonus()
	 * @see #getOptionalSimpleVesselCharterOption()
	 * @generated
	 */
	EAttribute getOptionalSimpleVesselCharterOption_BallastBonus();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getRepositioningFee <em>Repositioning Fee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Repositioning Fee</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getRepositioningFee()
	 * @see #getOptionalSimpleVesselCharterOption()
	 * @generated
	 */
	EAttribute getOptionalSimpleVesselCharterOption_RepositioningFee();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getStart()
	 * @see #getOptionalSimpleVesselCharterOption()
	 * @generated
	 */
	EAttribute getOptionalSimpleVesselCharterOption_Start();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getEnd <em>End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getEnd()
	 * @see #getOptionalSimpleVesselCharterOption()
	 * @generated
	 */
	EAttribute getOptionalSimpleVesselCharterOption_End();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getStartPort <em>Start Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Start Port</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getStartPort()
	 * @see #getOptionalSimpleVesselCharterOption()
	 * @generated
	 */
	EReference getOptionalSimpleVesselCharterOption_StartPort();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getEndPort <em>End Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>End Port</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption#getEndPort()
	 * @see #getOptionalSimpleVesselCharterOption()
	 * @generated
	 */
	EReference getOptionalSimpleVesselCharterOption_EndPort();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getVessel()
	 * @see #getRoundTripShippingOption()
	 * @generated
	 */
	EReference getRoundTripShippingOption_Vessel();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getEntity()
	 * @see #getRoundTripShippingOption()
	 * @generated
	 */
	EReference getRoundTripShippingOption_Entity();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.FullVesselCharterOption <em>Full Vessel Charter Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Full Vessel Charter Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FullVesselCharterOption
	 * @generated
	 */
	EClass getFullVesselCharterOption();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.FullVesselCharterOption#getVesselCharter <em>Vessel Charter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Vessel Charter</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FullVesselCharterOption#getVesselCharter()
	 * @see #getFullVesselCharterOption()
	 * @generated
	 */
	EReference getFullVesselCharterOption_VesselCharter();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption <em>Existing Vessel Charter Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Existing Vessel Charter Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption
	 * @generated
	 */
	EClass getExistingVesselCharterOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption#getVesselCharter <em>Vessel Charter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Charter</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption#getVesselCharter()
	 * @see #getExistingVesselCharterOption()
	 * @generated
	 */
	EReference getExistingVesselCharterOption_VesselCharter();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BreakEvenResult#getPriceString <em>Price String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price String</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenResult#getPriceString()
	 * @see #getBreakEvenResult()
	 * @generated
	 */
	EAttribute getBreakEvenResult_PriceString();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BreakEvenResult#getCargoPNL <em>Cargo PNL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo PNL</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenResult#getCargoPNL()
	 * @see #getBreakEvenResult()
	 * @generated
	 */
	EAttribute getBreakEvenResult_CargoPNL();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel <em>Abstract Analysis Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Analysis Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractAnalysisModel
	 * @generated
	 */
	EClass getAbstractAnalysisModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getBuys <em>Buys</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Buys</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getBuys()
	 * @see #getAbstractAnalysisModel()
	 * @generated
	 */
	EReference getAbstractAnalysisModel_Buys();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getSells <em>Sells</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sells</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getSells()
	 * @see #getAbstractAnalysisModel()
	 * @generated
	 */
	EReference getAbstractAnalysisModel_Sells();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getVesselEvents <em>Vessel Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vessel Events</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getVesselEvents()
	 * @see #getAbstractAnalysisModel()
	 * @generated
	 */
	EReference getAbstractAnalysisModel_VesselEvents();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getShippingTemplates <em>Shipping Templates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Shipping Templates</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getShippingTemplates()
	 * @see #getAbstractAnalysisModel()
	 * @generated
	 */
	EReference getAbstractAnalysisModel_ShippingTemplates();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getCommodityCurves <em>Commodity Curves</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Commodity Curves</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractAnalysisModel#getCommodityCurves()
	 * @see #getAbstractAnalysisModel()
	 * @generated
	 */
	EReference getAbstractAnalysisModel_CommodityCurves();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer <em>Marketability Result Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Marketability Result Container</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityResultContainer
	 * @generated
	 */
	EClass getMarketabilityResultContainer();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getRhsResults <em>Rhs Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rhs Results</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getRhsResults()
	 * @see #getMarketabilityResultContainer()
	 * @generated
	 */
	EReference getMarketabilityResultContainer_RhsResults();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getNextEvent <em>Next Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Next Event</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getNextEvent()
	 * @see #getMarketabilityResultContainer()
	 * @generated
	 */
	EReference getMarketabilityResultContainer_NextEvent();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getLadenPanama <em>Laden Panama</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden Panama</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getLadenPanama()
	 * @see #getMarketabilityResultContainer()
	 * @generated
	 */
	EAttribute getMarketabilityResultContainer_LadenPanama();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getBallastPanama <em>Ballast Panama</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Panama</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getBallastPanama()
	 * @see #getMarketabilityResultContainer()
	 * @generated
	 */
	EAttribute getMarketabilityResultContainer_BallastPanama();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.MarketabilityEvent <em>Marketability Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Marketability Event</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityEvent
	 * @generated
	 */
	EClass getMarketabilityEvent();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MarketabilityEvent#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityEvent#getStart()
	 * @see #getMarketabilityEvent()
	 * @generated
	 */
	EAttribute getMarketabilityEvent_Start();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.MarketabilityAssignableElement <em>Marketability Assignable Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Marketability Assignable Element</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityAssignableElement
	 * @generated
	 */
	EClass getMarketabilityAssignableElement();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.MarketabilityAssignableElement#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityAssignableElement#getElement()
	 * @see #getMarketabilityAssignableElement()
	 * @generated
	 */
	EReference getMarketabilityAssignableElement_Element();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.MarketabilityEndEvent <em>Marketability End Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Marketability End Event</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityEndEvent
	 * @generated
	 */
	EClass getMarketabilityEndEvent();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters <em>Swap Value Matrix Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Swap Value Matrix Parameters</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters
	 * @generated
	 */
	EClass getSwapValueMatrixParameters();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseLoad <em>Base Load</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Base Load</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseLoad()
	 * @see #getSwapValueMatrixParameters()
	 * @generated
	 */
	EReference getSwapValueMatrixParameters_BaseLoad();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseDischarge <em>Base Discharge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Base Discharge</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseDischarge()
	 * @see #getSwapValueMatrixParameters()
	 * @generated
	 */
	EReference getSwapValueMatrixParameters_BaseDischarge();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseVesselCharter <em>Base Vessel Charter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Base Vessel Charter</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseVesselCharter()
	 * @see #getSwapValueMatrixParameters()
	 * @generated
	 */
	EReference getSwapValueMatrixParameters_BaseVesselCharter();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBasePriceRange <em>Base Price Range</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Base Price Range</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBasePriceRange()
	 * @see #getSwapValueMatrixParameters()
	 * @generated
	 */
	EReference getSwapValueMatrixParameters_BasePriceRange();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapLoadMarket <em>Swap Load Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Swap Load Market</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapLoadMarket()
	 * @see #getSwapValueMatrixParameters()
	 * @generated
	 */
	EReference getSwapValueMatrixParameters_SwapLoadMarket();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapDischargeMarket <em>Swap Discharge Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Swap Discharge Market</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapDischargeMarket()
	 * @see #getSwapValueMatrixParameters()
	 * @generated
	 */
	EReference getSwapValueMatrixParameters_SwapDischargeMarket();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapFee <em>Swap Fee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Swap Fee</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapFee()
	 * @see #getSwapValueMatrixParameters()
	 * @generated
	 */
	EAttribute getSwapValueMatrixParameters_SwapFee();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapPriceRange <em>Swap Price Range</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Swap Price Range</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapPriceRange()
	 * @see #getSwapValueMatrixParameters()
	 * @generated
	 */
	EReference getSwapValueMatrixParameters_SwapPriceRange();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.Range <em>Range</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Range</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Range
	 * @generated
	 */
	EClass getRange();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.Range#getMin <em>Min</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Range#getMin()
	 * @see #getRange()
	 * @generated
	 */
	EAttribute getRange_Min();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.Range#getMax <em>Max</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Range#getMax()
	 * @see #getRange()
	 * @generated
	 */
	EAttribute getRange_Max();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.Range#getStepSize <em>Step Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Step Size</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Range#getStepSize()
	 * @see #getRange()
	 * @generated
	 */
	EAttribute getRange_StepSize();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult <em>Swap Value Matrix Cargo Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Swap Value Matrix Cargo Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult
	 * @generated
	 */
	EClass getSwapValueMatrixCargoResult();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getLoadPrice <em>Load Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getLoadPrice()
	 * @see #getSwapValueMatrixCargoResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixCargoResult_LoadPrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getDischargePrice <em>Discharge Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discharge Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getDischargePrice()
	 * @see #getSwapValueMatrixCargoResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixCargoResult_DischargePrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getPurchaseCost <em>Purchase Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Purchase Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getPurchaseCost()
	 * @see #getSwapValueMatrixCargoResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixCargoResult_PurchaseCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getSalesRevenue <em>Sales Revenue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sales Revenue</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getSalesRevenue()
	 * @see #getSwapValueMatrixCargoResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixCargoResult_SalesRevenue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getAdditionalPnl <em>Additional Pnl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Additional Pnl</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getAdditionalPnl()
	 * @see #getSwapValueMatrixCargoResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixCargoResult_AdditionalPnl();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getTotalPnl <em>Total Pnl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Total Pnl</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult#getTotalPnl()
	 * @see #getSwapValueMatrixCargoResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixCargoResult_TotalPnl();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult <em>Swap Value Matrix Shipped Cargo Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Swap Value Matrix Shipped Cargo Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult
	 * @generated
	 */
	EClass getSwapValueMatrixShippedCargoResult();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getShippingCost <em>Shipping Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shipping Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getShippingCost()
	 * @see #getSwapValueMatrixShippedCargoResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixShippedCargoResult_ShippingCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getLoadVolume <em>Load Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getLoadVolume()
	 * @see #getSwapValueMatrixShippedCargoResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixShippedCargoResult_LoadVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getDischargeVolume <em>Discharge Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discharge Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult#getDischargeVolume()
	 * @see #getSwapValueMatrixShippedCargoResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixShippedCargoResult_DischargeVolume();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixNonShippedCargoResult <em>Swap Value Matrix Non Shipped Cargo Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Swap Value Matrix Non Shipped Cargo Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixNonShippedCargoResult
	 * @generated
	 */
	EClass getSwapValueMatrixNonShippedCargoResult();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixNonShippedCargoResult#getVolume <em>Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixNonShippedCargoResult#getVolume()
	 * @see #getSwapValueMatrixNonShippedCargoResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixNonShippedCargoResult_Volume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getBuyDate <em>Buy Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Buy Date</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getBuyDate()
	 * @see #getMarketabilityResultContainer()
	 * @generated
	 */
	EAttribute getMarketabilityResultContainer_BuyDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getSellDate <em>Sell Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sell Date</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getSellDate()
	 * @see #getMarketabilityResultContainer()
	 * @generated
	 */
	EAttribute getMarketabilityResultContainer_SellDate();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BaseCase#isKeepExistingScenario <em>Keep Existing Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Keep Existing Scenario</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BaseCase#isKeepExistingScenario()
	 * @see #getBaseCase()
	 * @generated
	 */
	EAttribute getBaseCase_KeepExistingScenario();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.PartialCase#isKeepExistingScenario <em>Keep Existing Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Keep Existing Scenario</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PartialCase#isKeepExistingScenario()
	 * @see #getPartialCase()
	 * @generated
	 */
	EAttribute getPartialCase_KeepExistingScenario();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption <em>Existing Charter Market Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Existing Charter Market Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption
	 * @generated
	 */
	EClass getExistingCharterMarketOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption#getCharterInMarket <em>Charter In Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Charter In Market</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption#getCharterInMarket()
	 * @see #getExistingCharterMarketOption()
	 * @generated
	 */
	EReference getExistingCharterMarketOption_CharterInMarket();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption#getSpotIndex <em>Spot Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Index</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption#getSpotIndex()
	 * @see #getExistingCharterMarketOption()
	 * @generated
	 */
	EAttribute getExistingCharterMarketOption_SpotIndex();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet <em>Abstract Solution Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Solution Set</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet
	 * @generated
	 */
	EClass getAbstractSolutionSet();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#isHasDualModeSolutions <em>Has Dual Mode Solutions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Has Dual Mode Solutions</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet#isHasDualModeSolutions()
	 * @see #getAbstractSolutionSet()
	 * @generated
	 */
	EAttribute getAbstractSolutionSet_HasDualModeSolutions();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#isPortfolioBreakEvenMode <em>Portfolio Break Even Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Portfolio Break Even Mode</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet#isPortfolioBreakEvenMode()
	 * @see #getAbstractSolutionSet()
	 * @generated
	 */
	EAttribute getAbstractSolutionSet_PortfolioBreakEvenMode();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getUserSettings <em>User Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>User Settings</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getUserSettings()
	 * @see #getAbstractSolutionSet()
	 * @generated
	 */
	EReference getAbstractSolutionSet_UserSettings();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getOptions <em>Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Options</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getOptions()
	 * @see #getAbstractSolutionSet()
	 * @generated
	 */
	EReference getAbstractSolutionSet_Options();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getExtraVesselEvents <em>Extra Vessel Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extra Vessel Events</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getExtraVesselEvents()
	 * @see #getAbstractSolutionSet()
	 * @generated
	 */
	EReference getAbstractSolutionSet_ExtraVesselEvents();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getExtraVesselCharters <em>Extra Vessel Charters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extra Vessel Charters</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getExtraVesselCharters()
	 * @see #getAbstractSolutionSet()
	 * @generated
	 */
	EReference getAbstractSolutionSet_ExtraVesselCharters();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getCharterInMarketOverrides <em>Charter In Market Overrides</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Charter In Market Overrides</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getCharterInMarketOverrides()
	 * @see #getAbstractSolutionSet()
	 * @generated
	 */
	EReference getAbstractSolutionSet_CharterInMarketOverrides();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getExtraCharterInMarkets <em>Extra Charter In Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extra Charter In Markets</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getExtraCharterInMarkets()
	 * @see #getAbstractSolutionSet()
	 * @generated
	 */
	EReference getAbstractSolutionSet_ExtraCharterInMarkets();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#isUseScenarioBase <em>Use Scenario Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Scenario Base</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet#isUseScenarioBase()
	 * @see #getAbstractSolutionSet()
	 * @generated
	 */
	EAttribute getAbstractSolutionSet_UseScenarioBase();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getExtraSlots <em>Extra Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extra Slots</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getExtraSlots()
	 * @see #getAbstractSolutionSet()
	 * @generated
	 */
	EReference getAbstractSolutionSet_ExtraSlots();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getBaseOption <em>Base Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Base Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet#getBaseOption()
	 * @see #getAbstractSolutionSet()
	 * @generated
	 */
	EReference getAbstractSolutionSet_BaseOption();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ActionableSetPlan <em>Actionable Set Plan</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Actionable Set Plan</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ActionableSetPlan
	 * @generated
	 */
	EClass getActionableSetPlan();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SlotInsertionOptions <em>Slot Insertion Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Insertion Options</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SlotInsertionOptions
	 * @generated
	 */
	EClass getSlotInsertionOptions();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.SlotInsertionOptions#getSlotsInserted <em>Slots Inserted</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Slots Inserted</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SlotInsertionOptions#getSlotsInserted()
	 * @see #getSlotInsertionOptions()
	 * @generated
	 */
	EReference getSlotInsertionOptions_SlotsInserted();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.SlotInsertionOptions#getEventsInserted <em>Events Inserted</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Events Inserted</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SlotInsertionOptions#getEventsInserted()
	 * @see #getSlotInsertionOptions()
	 * @generated
	 */
	EReference getSlotInsertionOptions_EventsInserted();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ChangeDescription <em>Change Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Description</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ChangeDescription
	 * @generated
	 */
	EClass getChangeDescription();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.ChangeDescription#getChanges <em>Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Changes</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ChangeDescription#getChanges()
	 * @see #getChangeDescription()
	 * @generated
	 */
	EReference getChangeDescription_Changes();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.Change <em>Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Change
	 * @generated
	 */
	EClass getChange();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.OpenSlotChange <em>Open Slot Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Open Slot Change</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OpenSlotChange
	 * @generated
	 */
	EClass getOpenSlotChange();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.OpenSlotChange#getSlotDescriptor <em>Slot Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Slot Descriptor</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OpenSlotChange#getSlotDescriptor()
	 * @see #getOpenSlotChange()
	 * @generated
	 */
	EReference getOpenSlotChange_SlotDescriptor();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.CargoChange <em>Cargo Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo Change</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CargoChange
	 * @generated
	 */
	EClass getCargoChange();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.CargoChange#getSlotDescriptors <em>Slot Descriptors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Slot Descriptors</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CargoChange#getSlotDescriptors()
	 * @see #getCargoChange()
	 * @generated
	 */
	EReference getCargoChange_SlotDescriptors();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.CargoChange#getVesselAllocation <em>Vessel Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Vessel Allocation</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CargoChange#getVesselAllocation()
	 * @see #getCargoChange()
	 * @generated
	 */
	EReference getCargoChange_VesselAllocation();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.CargoChange#getPosition <em>Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Position</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CargoChange#getPosition()
	 * @see #getCargoChange()
	 * @generated
	 */
	EReference getCargoChange_Position();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.VesselEventChange <em>Vessel Event Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Event Change</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VesselEventChange
	 * @generated
	 */
	EClass getVesselEventChange();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.VesselEventChange#getVesselEventDescriptor <em>Vessel Event Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Vessel Event Descriptor</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VesselEventChange#getVesselEventDescriptor()
	 * @see #getVesselEventChange()
	 * @generated
	 */
	EReference getVesselEventChange_VesselEventDescriptor();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.VesselEventChange#getVesselAllocation <em>Vessel Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Vessel Allocation</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VesselEventChange#getVesselAllocation()
	 * @see #getVesselEventChange()
	 * @generated
	 */
	EReference getVesselEventChange_VesselAllocation();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.VesselEventChange#getPosition <em>Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Position</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VesselEventChange#getPosition()
	 * @see #getVesselEventChange()
	 * @generated
	 */
	EReference getVesselEventChange_Position();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.VesselEventDescriptor <em>Vessel Event Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Event Descriptor</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VesselEventDescriptor
	 * @generated
	 */
	EClass getVesselEventDescriptor();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.VesselEventDescriptor#getEventName <em>Event Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Event Name</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VesselEventDescriptor#getEventName()
	 * @see #getVesselEventDescriptor()
	 * @generated
	 */
	EAttribute getVesselEventDescriptor_EventName();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SlotDescriptor <em>Slot Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Descriptor</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SlotDescriptor
	 * @generated
	 */
	EClass getSlotDescriptor();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SlotDescriptor#getSlotType <em>Slot Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Slot Type</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SlotDescriptor#getSlotType()
	 * @see #getSlotDescriptor()
	 * @generated
	 */
	EAttribute getSlotDescriptor_SlotType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.RealSlotDescriptor <em>Real Slot Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Real Slot Descriptor</em>'.
	 * @see com.mmxlabs.models.lng.analytics.RealSlotDescriptor
	 * @generated
	 */
	EClass getRealSlotDescriptor();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.RealSlotDescriptor#getSlotName <em>Slot Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Slot Name</em>'.
	 * @see com.mmxlabs.models.lng.analytics.RealSlotDescriptor#getSlotName()
	 * @see #getRealSlotDescriptor()
	 * @generated
	 */
	EAttribute getRealSlotDescriptor_SlotName();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor <em>Spot Market Slot Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Market Slot Descriptor</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor
	 * @generated
	 */
	EClass getSpotMarketSlotDescriptor();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getDate()
	 * @see #getSpotMarketSlotDescriptor()
	 * @generated
	 */
	EAttribute getSpotMarketSlotDescriptor_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getMarketName <em>Market Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Market Name</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getMarketName()
	 * @see #getSpotMarketSlotDescriptor()
	 * @generated
	 */
	EAttribute getSpotMarketSlotDescriptor_MarketName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getPortName <em>Port Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Port Name</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor#getPortName()
	 * @see #getSpotMarketSlotDescriptor()
	 * @generated
	 */
	EAttribute getSpotMarketSlotDescriptor_PortName();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.VesselAllocationDescriptor <em>Vessel Allocation Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Allocation Descriptor</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VesselAllocationDescriptor
	 * @generated
	 */
	EClass getVesselAllocationDescriptor();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor <em>Market Vessel Allocation Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Market Vessel Allocation Descriptor</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor
	 * @generated
	 */
	EClass getMarketVesselAllocationDescriptor();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor#getMarketName <em>Market Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Market Name</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor#getMarketName()
	 * @see #getMarketVesselAllocationDescriptor()
	 * @generated
	 */
	EAttribute getMarketVesselAllocationDescriptor_MarketName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor#getSpotIndex <em>Spot Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Index</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor#getSpotIndex()
	 * @see #getMarketVesselAllocationDescriptor()
	 * @generated
	 */
	EAttribute getMarketVesselAllocationDescriptor_SpotIndex();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor <em>Fleet Vessel Allocation Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fleet Vessel Allocation Descriptor</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor
	 * @generated
	 */
	EClass getFleetVesselAllocationDescriptor();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor#getName()
	 * @see #getFleetVesselAllocationDescriptor()
	 * @generated
	 */
	EAttribute getFleetVesselAllocationDescriptor_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor#getCharterIndex <em>Charter Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Charter Index</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor#getCharterIndex()
	 * @see #getFleetVesselAllocationDescriptor()
	 * @generated
	 */
	EAttribute getFleetVesselAllocationDescriptor_CharterIndex();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.PositionDescriptor <em>Position Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Position Descriptor</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PositionDescriptor
	 * @generated
	 */
	EClass getPositionDescriptor();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.PositionDescriptor#getAfter <em>After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>After</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PositionDescriptor#getAfter()
	 * @see #getPositionDescriptor()
	 * @generated
	 */
	EAttribute getPositionDescriptor_After();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.PositionDescriptor#getBefore <em>Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Before</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PositionDescriptor#getBefore()
	 * @see #getPositionDescriptor()
	 * @generated
	 */
	EAttribute getPositionDescriptor_Before();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ViabilityModel <em>Viability Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Viability Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityModel
	 * @generated
	 */
	EClass getViabilityModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.ViabilityModel#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityModel#getRows()
	 * @see #getViabilityModel()
	 * @generated
	 */
	EReference getViabilityModel_Rows();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.ViabilityModel#getMarkets <em>Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Markets</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityModel#getMarkets()
	 * @see #getViabilityModel()
	 * @generated
	 */
	EReference getViabilityModel_Markets();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.MarketabilityModel <em>Marketability Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Marketability Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityModel
	 * @generated
	 */
	EClass getMarketabilityModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.MarketabilityModel#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityModel#getRows()
	 * @see #getMarketabilityModel()
	 * @generated
	 */
	EReference getMarketabilityModel_Rows();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.MarketabilityModel#getMarkets <em>Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Markets</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityModel#getMarkets()
	 * @see #getMarketabilityModel()
	 * @generated
	 */
	EReference getMarketabilityModel_Markets();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MarketabilityModel#getVesselSpeed <em>Vessel Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel Speed</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityModel#getVesselSpeed()
	 * @see #getMarketabilityModel()
	 * @generated
	 */
	EAttribute getMarketabilityModel_VesselSpeed();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow <em>Marketability Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Marketability Row</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityRow
	 * @generated
	 */
	EClass getMarketabilityRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getBuyOption <em>Buy Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Buy Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityRow#getBuyOption()
	 * @see #getMarketabilityRow()
	 * @generated
	 */
	EReference getMarketabilityRow_BuyOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getSellOption <em>Sell Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sell Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityRow#getSellOption()
	 * @see #getMarketabilityRow()
	 * @generated
	 */
	EReference getMarketabilityRow_SellOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getShipping <em>Shipping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Shipping</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityRow#getShipping()
	 * @see #getMarketabilityRow()
	 * @generated
	 */
	EReference getMarketabilityRow_Shipping();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getResult <em>Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityRow#getResult()
	 * @see #getMarketabilityRow()
	 * @generated
	 */
	EReference getMarketabilityRow_Result();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.MarketabilityResult <em>Marketability Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Marketability Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityResult
	 * @generated
	 */
	EClass getMarketabilityResult();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityResult#getTarget()
	 * @see #getMarketabilityResult()
	 * @generated
	 */
	EReference getMarketabilityResult_Target();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getEarliestETA <em>Earliest ETA</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Earliest ETA</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityResult#getEarliestETA()
	 * @see #getMarketabilityResult()
	 * @generated
	 */
	EAttribute getMarketabilityResult_EarliestETA();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MarketabilityResult#getLatestETA <em>Latest ETA</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Latest ETA</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MarketabilityResult#getLatestETA()
	 * @see #getMarketabilityResult()
	 * @generated
	 */
	EAttribute getMarketabilityResult_LatestETA();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ViabilityRow <em>Viability Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Viability Row</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow
	 * @generated
	 */
	EClass getViabilityRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getBuyOption <em>Buy Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Buy Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow#getBuyOption()
	 * @see #getViabilityRow()
	 * @generated
	 */
	EReference getViabilityRow_BuyOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getSellOption <em>Sell Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sell Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow#getSellOption()
	 * @see #getViabilityRow()
	 * @generated
	 */
	EReference getViabilityRow_SellOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getShipping <em>Shipping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Shipping</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow#getShipping()
	 * @see #getViabilityRow()
	 * @generated
	 */
	EReference getViabilityRow_Shipping();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getLhsResults <em>Lhs Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Lhs Results</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow#getLhsResults()
	 * @see #getViabilityRow()
	 * @generated
	 */
	EReference getViabilityRow_LhsResults();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getRhsResults <em>Rhs Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rhs Results</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow#getRhsResults()
	 * @see #getViabilityRow()
	 * @generated
	 */
	EReference getViabilityRow_RhsResults();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow#getTarget()
	 * @see #getViabilityRow()
	 * @generated
	 */
	EReference getViabilityRow_Target();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow#getPrice()
	 * @see #getViabilityRow()
	 * @generated
	 */
	EAttribute getViabilityRow_Price();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getEta <em>Eta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Eta</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow#getEta()
	 * @see #getViabilityRow()
	 * @generated
	 */
	EAttribute getViabilityRow_Eta();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getReferencePrice <em>Reference Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reference Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow#getReferencePrice()
	 * @see #getViabilityRow()
	 * @generated
	 */
	EAttribute getViabilityRow_ReferencePrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ViabilityRow#getStartVolume <em>Start Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow#getStartVolume()
	 * @see #getViabilityRow()
	 * @generated
	 */
	EAttribute getViabilityRow_StartVolume();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ViabilityResult <em>Viability Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Viability Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityResult
	 * @generated
	 */
	EClass getViabilityResult();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityResult#getTarget()
	 * @see #getViabilityResult()
	 * @generated
	 */
	EReference getViabilityResult_Target();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestETA <em>Earliest ETA</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Earliest ETA</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestETA()
	 * @see #getViabilityResult()
	 * @generated
	 */
	EAttribute getViabilityResult_EarliestETA();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestETA <em>Latest ETA</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Latest ETA</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestETA()
	 * @see #getViabilityResult()
	 * @generated
	 */
	EAttribute getViabilityResult_LatestETA();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestVolume <em>Earliest Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Earliest Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestVolume()
	 * @see #getViabilityResult()
	 * @generated
	 */
	EAttribute getViabilityResult_EarliestVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestVolume <em>Latest Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Latest Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestVolume()
	 * @see #getViabilityResult()
	 * @generated
	 */
	EAttribute getViabilityResult_LatestVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestPrice <em>Earliest Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Earliest Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityResult#getEarliestPrice()
	 * @see #getViabilityResult()
	 * @generated
	 */
	EAttribute getViabilityResult_EarliestPrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestPrice <em>Latest Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Latest Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityResult#getLatestPrice()
	 * @see #getViabilityResult()
	 * @generated
	 */
	EAttribute getViabilityResult_LatestPrice();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.MTMModel <em>MTM Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>MTM Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMModel
	 * @generated
	 */
	EClass getMTMModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.MTMModel#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMModel#getRows()
	 * @see #getMTMModel()
	 * @generated
	 */
	EReference getMTMModel_Rows();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.MTMModel#getMarkets <em>Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Markets</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMModel#getMarkets()
	 * @see #getMTMModel()
	 * @generated
	 */
	EReference getMTMModel_Markets();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.MTMModel#getNominalMarkets <em>Nominal Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Nominal Markets</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMModel#getNominalMarkets()
	 * @see #getMTMModel()
	 * @generated
	 */
	EReference getMTMModel_NominalMarkets();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.MTMResult <em>MTM Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>MTM Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMResult
	 * @generated
	 */
	EClass getMTMResult();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.MTMResult#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMResult#getTarget()
	 * @see #getMTMResult()
	 * @generated
	 */
	EReference getMTMResult_Target();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MTMResult#getEarliestETA <em>Earliest ETA</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Earliest ETA</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMResult#getEarliestETA()
	 * @see #getMTMResult()
	 * @generated
	 */
	EAttribute getMTMResult_EarliestETA();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MTMResult#getEarliestVolume <em>Earliest Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Earliest Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMResult#getEarliestVolume()
	 * @see #getMTMResult()
	 * @generated
	 */
	EAttribute getMTMResult_EarliestVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MTMResult#getEarliestPrice <em>Earliest Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Earliest Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMResult#getEarliestPrice()
	 * @see #getMTMResult()
	 * @generated
	 */
	EAttribute getMTMResult_EarliestPrice();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.MTMResult#getShipping <em>Shipping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Shipping</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMResult#getShipping()
	 * @see #getMTMResult()
	 * @generated
	 */
	EReference getMTMResult_Shipping();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MTMResult#getShippingCost <em>Shipping Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shipping Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMResult#getShippingCost()
	 * @see #getMTMResult()
	 * @generated
	 */
	EAttribute getMTMResult_ShippingCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MTMResult#getOriginalVolume <em>Original Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Original Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMResult#getOriginalVolume()
	 * @see #getMTMResult()
	 * @generated
	 */
	EAttribute getMTMResult_OriginalVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MTMResult#getOriginalPrice <em>Original Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Original Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMResult#getOriginalPrice()
	 * @see #getMTMResult()
	 * @generated
	 */
	EAttribute getMTMResult_OriginalPrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MTMResult#getTotalShippingCost <em>Total Shipping Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Total Shipping Cost</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMResult#getTotalShippingCost()
	 * @see #getMTMResult()
	 * @generated
	 */
	EAttribute getMTMResult_TotalShippingCost();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.MTMRow <em>MTM Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>MTM Row</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMRow
	 * @generated
	 */
	EClass getMTMRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.MTMRow#getBuyOption <em>Buy Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Buy Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMRow#getBuyOption()
	 * @see #getMTMRow()
	 * @generated
	 */
	EReference getMTMRow_BuyOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.MTMRow#getSellOption <em>Sell Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sell Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMRow#getSellOption()
	 * @see #getMTMRow()
	 * @generated
	 */
	EReference getMTMRow_SellOption();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.MTMRow#getLhsResults <em>Lhs Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Lhs Results</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMRow#getLhsResults()
	 * @see #getMTMRow()
	 * @generated
	 */
	EReference getMTMRow_LhsResults();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.MTMRow#getRhsResults <em>Rhs Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rhs Results</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMRow#getRhsResults()
	 * @see #getMTMRow()
	 * @generated
	 */
	EReference getMTMRow_RhsResults();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.MTMRow#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMRow#getTarget()
	 * @see #getMTMRow()
	 * @generated
	 */
	EReference getMTMRow_Target();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MTMRow#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMRow#getPrice()
	 * @see #getMTMRow()
	 * @generated
	 */
	EAttribute getMTMRow_Price();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MTMRow#getEta <em>Eta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Eta</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMRow#getEta()
	 * @see #getMTMRow()
	 * @generated
	 */
	EAttribute getMTMRow_Eta();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MTMRow#getReferencePrice <em>Reference Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reference Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMRow#getReferencePrice()
	 * @see #getMTMRow()
	 * @generated
	 */
	EAttribute getMTMRow_ReferencePrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.MTMRow#getStartVolume <em>Start Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Volume</em>'.
	 * @see com.mmxlabs.models.lng.analytics.MTMRow#getStartVolume()
	 * @see #getMTMRow()
	 * @generated
	 */
	EAttribute getMTMRow_StartVolume();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel <em>Break Even Analysis Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Break Even Analysis Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel
	 * @generated
	 */
	EClass getBreakEvenAnalysisModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel#getRows()
	 * @see #getBreakEvenAnalysisModel()
	 * @generated
	 */
	EReference getBreakEvenAnalysisModel_Rows();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel#getMarkets <em>Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Markets</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel#getMarkets()
	 * @see #getBreakEvenAnalysisModel()
	 * @generated
	 */
	EReference getBreakEvenAnalysisModel_Markets();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow <em>Break Even Analysis Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Break Even Analysis Row</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow
	 * @generated
	 */
	EClass getBreakEvenAnalysisRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getBuyOption <em>Buy Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Buy Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getBuyOption()
	 * @see #getBreakEvenAnalysisRow()
	 * @generated
	 */
	EReference getBreakEvenAnalysisRow_BuyOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getSellOption <em>Sell Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sell Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getSellOption()
	 * @see #getBreakEvenAnalysisRow()
	 * @generated
	 */
	EReference getBreakEvenAnalysisRow_SellOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getShipping <em>Shipping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Shipping</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getShipping()
	 * @see #getBreakEvenAnalysisRow()
	 * @generated
	 */
	EReference getBreakEvenAnalysisRow_Shipping();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getLhsResults <em>Lhs Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Lhs Results</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getLhsResults()
	 * @see #getBreakEvenAnalysisRow()
	 * @generated
	 */
	EReference getBreakEvenAnalysisRow_LhsResults();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getRhsResults <em>Rhs Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rhs Results</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getRhsResults()
	 * @see #getBreakEvenAnalysisRow()
	 * @generated
	 */
	EReference getBreakEvenAnalysisRow_RhsResults();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getLhsBasedOn <em>Lhs Based On</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Lhs Based On</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getLhsBasedOn()
	 * @see #getBreakEvenAnalysisRow()
	 * @generated
	 */
	EReference getBreakEvenAnalysisRow_LhsBasedOn();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getRhsBasedOn <em>Rhs Based On</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Rhs Based On</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow#getRhsBasedOn()
	 * @see #getBreakEvenAnalysisRow()
	 * @generated
	 */
	EReference getBreakEvenAnalysisRow_RhsBasedOn();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet <em>Break Even Analysis Result Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Break Even Analysis Result Set</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet
	 * @generated
	 */
	EClass getBreakEvenAnalysisResultSet();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet#getBasedOn <em>Based On</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Based On</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet#getBasedOn()
	 * @see #getBreakEvenAnalysisResultSet()
	 * @generated
	 */
	EReference getBreakEvenAnalysisResultSet_BasedOn();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet#getResults <em>Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Results</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet#getResults()
	 * @see #getBreakEvenAnalysisResultSet()
	 * @generated
	 */
	EReference getBreakEvenAnalysisResultSet_Results();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet#getPrice()
	 * @see #getBreakEvenAnalysisResultSet()
	 * @generated
	 */
	EAttribute getBreakEvenAnalysisResultSet_Price();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult <em>Break Even Analysis Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Break Even Analysis Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult
	 * @generated
	 */
	EClass getBreakEvenAnalysisResult();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult#getTarget()
	 * @see #getBreakEvenAnalysisResult()
	 * @generated
	 */
	EReference getBreakEvenAnalysisResult_Target();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult#getPrice()
	 * @see #getBreakEvenAnalysisResult()
	 * @generated
	 */
	EAttribute getBreakEvenAnalysisResult_Price();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult#getEta <em>Eta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Eta</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult#getEta()
	 * @see #getBreakEvenAnalysisResult()
	 * @generated
	 */
	EAttribute getBreakEvenAnalysisResult_Eta();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult#getReferencePrice <em>Reference Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reference Price</em>'.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult#getReferencePrice()
	 * @see #getBreakEvenAnalysisResult()
	 * @generated
	 */
	EAttribute getBreakEvenAnalysisResult_ReferencePrice();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.LocalDateTimeHolder <em>Local Date Time Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Local Date Time Holder</em>'.
	 * @see com.mmxlabs.models.lng.analytics.LocalDateTimeHolder
	 * @generated
	 */
	EClass getLocalDateTimeHolder();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.LocalDateTimeHolder#getDateTime <em>Date Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date Time</em>'.
	 * @see com.mmxlabs.models.lng.analytics.LocalDateTimeHolder#getDateTime()
	 * @see #getLocalDateTimeHolder()
	 * @generated
	 */
	EAttribute getLocalDateTimeHolder_DateTime();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.CommodityCurveOption <em>Commodity Curve Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Commodity Curve Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CommodityCurveOption
	 * @generated
	 */
	EClass getCommodityCurveOption();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.CommodityCurveOverlay <em>Commodity Curve Overlay</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Commodity Curve Overlay</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CommodityCurveOverlay
	 * @generated
	 */
	EClass getCommodityCurveOverlay();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.CommodityCurveOverlay#getReferenceCurve <em>Reference Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference Curve</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CommodityCurveOverlay#getReferenceCurve()
	 * @see #getCommodityCurveOverlay()
	 * @generated
	 */
	EReference getCommodityCurveOverlay_ReferenceCurve();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.CommodityCurveOverlay#getAlternativeCurves <em>Alternative Curves</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Alternative Curves</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CommodityCurveOverlay#getAlternativeCurves()
	 * @see #getCommodityCurveOverlay()
	 * @generated
	 */
	EReference getCommodityCurveOverlay_AlternativeCurves();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SensitivityModel <em>Sensitivity Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sensitivity Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SensitivityModel
	 * @generated
	 */
	EClass getSensitivityModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SensitivityModel#getSensitivityModel <em>Sensitivity Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Sensitivity Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SensitivityModel#getSensitivityModel()
	 * @see #getSensitivityModel()
	 * @generated
	 */
	EReference getSensitivityModel_SensitivityModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SensitivityModel#getSensitivitySolution <em>Sensitivity Solution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Sensitivity Solution</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SensitivityModel#getSensitivitySolution()
	 * @see #getSensitivityModel()
	 * @generated
	 */
	EReference getSensitivityModel_SensitivitySolution();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SensitivitySolutionSet <em>Sensitivity Solution Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sensitivity Solution Set</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SensitivitySolutionSet
	 * @generated
	 */
	EClass getSensitivitySolutionSet();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SensitivitySolutionSet#getPorfolioPnLResult <em>Porfolio Pn LResult</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Porfolio Pn LResult</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SensitivitySolutionSet#getPorfolioPnLResult()
	 * @see #getSensitivitySolutionSet()
	 * @generated
	 */
	EReference getSensitivitySolutionSet_PorfolioPnLResult();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.SensitivitySolutionSet#getCargoPnLResults <em>Cargo Pn LResults</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargo Pn LResults</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SensitivitySolutionSet#getCargoPnLResults()
	 * @see #getSensitivitySolutionSet()
	 * @generated
	 */
	EReference getSensitivitySolutionSet_CargoPnLResults();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult <em>Abstract Sensitivity Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Sensitivity Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSensitivityResult
	 * @generated
	 */
	EClass getAbstractSensitivityResult();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getMinPnL <em>Min Pn L</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Pn L</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getMinPnL()
	 * @see #getAbstractSensitivityResult()
	 * @generated
	 */
	EAttribute getAbstractSensitivityResult_MinPnL();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getMaxPnL <em>Max Pn L</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Pn L</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getMaxPnL()
	 * @see #getAbstractSensitivityResult()
	 * @generated
	 */
	EAttribute getAbstractSensitivityResult_MaxPnL();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getAveragePnL <em>Average Pn L</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Average Pn L</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getAveragePnL()
	 * @see #getAbstractSensitivityResult()
	 * @generated
	 */
	EAttribute getAbstractSensitivityResult_AveragePnL();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getVariance <em>Variance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Variance</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSensitivityResult#getVariance()
	 * @see #getAbstractSensitivityResult()
	 * @generated
	 */
	EAttribute getAbstractSensitivityResult_Variance();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.PortfolioSensitivityResult <em>Portfolio Sensitivity Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Portfolio Sensitivity Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.PortfolioSensitivityResult
	 * @generated
	 */
	EClass getPortfolioSensitivityResult();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.CargoPnLResult <em>Cargo Pn LResult</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo Pn LResult</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CargoPnLResult
	 * @generated
	 */
	EClass getCargoPnLResult();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.CargoPnLResult#getCargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo</em>'.
	 * @see com.mmxlabs.models.lng.analytics.CargoPnLResult#getCargo()
	 * @see #getCargoPnLResult()
	 * @generated
	 */
	EReference getCargoPnLResult_Cargo();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel <em>Swap Value Matrix Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Swap Value Matrix Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixModel
	 * @generated
	 */
	EClass getSwapValueMatrixModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Parameters</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getParameters()
	 * @see #getSwapValueMatrixModel()
	 * @generated
	 */
	EReference getSwapValueMatrixModel_Parameters();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapValueMatrixResult <em>Swap Value Matrix Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Swap Value Matrix Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapValueMatrixResult()
	 * @see #getSwapValueMatrixModel()
	 * @generated
	 */
	EReference getSwapValueMatrixModel_SwapValueMatrixResult();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult <em>Swap Value Matrix Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Swap Value Matrix Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResult
	 * @generated
	 */
	EClass getSwapValueMatrixResult();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPnlMinusBasePnl <em>Swap Pnl Minus Base Pnl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Swap Pnl Minus Base Pnl</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPnlMinusBasePnl()
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixResult_SwapPnlMinusBasePnl();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBasePrecedingPnl <em>Base Preceding Pnl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Preceding Pnl</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBasePrecedingPnl()
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixResult_BasePrecedingPnl();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseSucceedingPnl <em>Base Succeeding Pnl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Succeeding Pnl</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseSucceedingPnl()
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixResult_BaseSucceedingPnl();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPrecedingPnl <em>Swap Preceding Pnl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Swap Preceding Pnl</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapPrecedingPnl()
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixResult_SwapPrecedingPnl();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapSucceedingPnl <em>Swap Succeeding Pnl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Swap Succeeding Pnl</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapSucceedingPnl()
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 */
	EAttribute getSwapValueMatrixResult_SwapSucceedingPnl();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseCargo <em>Base Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Base Cargo</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getBaseCargo()
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 */
	EReference getSwapValueMatrixResult_BaseCargo();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapDiversionCargo <em>Swap Diversion Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Swap Diversion Cargo</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapDiversionCargo()
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 */
	EReference getSwapValueMatrixResult_SwapDiversionCargo();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillCargo <em>Swap Backfill Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Swap Backfill Cargo</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResult#getSwapBackfillCargo()
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 */
	EReference getSwapValueMatrixResult_SwapBackfillCargo();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet <em>Swap Value Matrix Result Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Swap Value Matrix Result Set</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet
	 * @generated
	 */
	EClass getSwapValueMatrixResultSet();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getResults <em>Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Results</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getResults()
	 * @see #getSwapValueMatrixResultSet()
	 * @generated
	 */
	EReference getSwapValueMatrixResultSet_Results();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getSwapFee <em>Swap Fee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Swap Fee</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getSwapFee()
	 * @see #getSwapValueMatrixResultSet()
	 * @generated
	 */
	EAttribute getSwapValueMatrixResultSet_SwapFee();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getGeneratedSpotLoadSlot <em>Generated Spot Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Generated Spot Load Slot</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getGeneratedSpotLoadSlot()
	 * @see #getSwapValueMatrixResultSet()
	 * @generated
	 */
	EReference getSwapValueMatrixResultSet_GeneratedSpotLoadSlot();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getGeneratedSpotDischargeSlot <em>Generated Spot Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Generated Spot Discharge Slot</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getGeneratedSpotDischargeSlot()
	 * @see #getSwapValueMatrixResultSet()
	 * @generated
	 */
	EReference getSwapValueMatrixResultSet_GeneratedSpotDischargeSlot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getNonVesselCharterPnl <em>Non Vessel Charter Pnl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Non Vessel Charter Pnl</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet#getNonVesselCharterPnl()
	 * @see #getSwapValueMatrixResultSet()
	 * @generated
	 */
	EAttribute getSwapValueMatrixResultSet_NonVesselCharterPnl();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SolutionOption <em>Solution Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Solution Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SolutionOption
	 * @generated
	 */
	EClass getSolutionOption();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SolutionOption#getChangeDescription <em>Change Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Change Description</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SolutionOption#getChangeDescription()
	 * @see #getSolutionOption()
	 * @generated
	 */
	EReference getSolutionOption_ChangeDescription();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SolutionOption#getScheduleSpecification <em>Schedule Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Schedule Specification</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SolutionOption#getScheduleSpecification()
	 * @see #getSolutionOption()
	 * @generated
	 */
	EReference getSolutionOption_ScheduleSpecification();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SolutionOption#getScheduleModel <em>Schedule Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Schedule Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SolutionOption#getScheduleModel()
	 * @see #getSolutionOption()
	 * @generated
	 */
	EReference getSolutionOption_ScheduleModel();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.OptimisationResult <em>Optimisation Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Optimisation Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptimisationResult
	 * @generated
	 */
	EClass getOptimisationResult();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.DualModeSolutionOption <em>Dual Mode Solution Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dual Mode Solution Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.DualModeSolutionOption
	 * @generated
	 */
	EClass getDualModeSolutionOption();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.DualModeSolutionOption#getMicroBaseCase <em>Micro Base Case</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Micro Base Case</em>'.
	 * @see com.mmxlabs.models.lng.analytics.DualModeSolutionOption#getMicroBaseCase()
	 * @see #getDualModeSolutionOption()
	 * @generated
	 */
	EReference getDualModeSolutionOption_MicroBaseCase();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.DualModeSolutionOption#getMicroTargetCase <em>Micro Target Case</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Micro Target Case</em>'.
	 * @see com.mmxlabs.models.lng.analytics.DualModeSolutionOption#getMicroTargetCase()
	 * @see #getDualModeSolutionOption()
	 * @generated
	 */
	EReference getDualModeSolutionOption_MicroTargetCase();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase <em>Solution Option Micro Case</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Solution Option Micro Case</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase
	 * @generated
	 */
	EClass getSolutionOptionMicroCase();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getScheduleSpecification <em>Schedule Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Schedule Specification</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getScheduleSpecification()
	 * @see #getSolutionOptionMicroCase()
	 * @generated
	 */
	EReference getSolutionOptionMicroCase_ScheduleSpecification();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getScheduleModel <em>Schedule Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Schedule Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getScheduleModel()
	 * @see #getSolutionOptionMicroCase()
	 * @generated
	 */
	EReference getSolutionOptionMicroCase_ScheduleModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getExtraVesselCharters <em>Extra Vessel Charters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extra Vessel Charters</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getExtraVesselCharters()
	 * @see #getSolutionOptionMicroCase()
	 * @generated
	 */
	EReference getSolutionOptionMicroCase_ExtraVesselCharters();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getCharterInMarketOverrides <em>Charter In Market Overrides</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Charter In Market Overrides</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getCharterInMarketOverrides()
	 * @see #getSolutionOptionMicroCase()
	 * @generated
	 */
	EReference getSolutionOptionMicroCase_CharterInMarketOverrides();

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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getResults <em>Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Results</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getResults()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EReference getOptionAnalysisModel_Results();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getMode <em>Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mode</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getMode()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EAttribute getOptionAnalysisModel_Mode();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SandboxResult <em>Sandbox Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sandbox Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SandboxResult
	 * @generated
	 */
	EClass getSandboxResult();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.analytics.VolumeMode <em>Volume Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Volume Mode</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VolumeMode
	 * @generated
	 */
	EEnum getVolumeMode();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.analytics.SlotType <em>Slot Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Slot Type</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SlotType
	 * @generated
	 */
	EEnum getSlotType();

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
		 * The meta object literal for the '<em><b>Option Models</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYTICS_MODEL__OPTION_MODELS = eINSTANCE.getAnalyticsModel_OptionModels();

		/**
		 * The meta object literal for the '<em><b>Optimisations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYTICS_MODEL__OPTIMISATIONS = eINSTANCE.getAnalyticsModel_Optimisations();

		/**
		 * The meta object literal for the '<em><b>Viability Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYTICS_MODEL__VIABILITY_MODEL = eINSTANCE.getAnalyticsModel_ViabilityModel();

		/**
		 * The meta object literal for the '<em><b>Mtm Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYTICS_MODEL__MTM_MODEL = eINSTANCE.getAnalyticsModel_MtmModel();

		/**
		 * The meta object literal for the '<em><b>Breakeven Models</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYTICS_MODEL__BREAKEVEN_MODELS = eINSTANCE.getAnalyticsModel_BreakevenModels();

		/**
		 * The meta object literal for the '<em><b>Swap Value Matrix Models</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS = eINSTANCE.getAnalyticsModel_SwapValueMatrixModels();

		/**
		 * The meta object literal for the '<em><b>Marketability Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYTICS_MODEL__MARKETABILITY_MODEL = eINSTANCE.getAnalyticsModel_MarketabilityModel();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.OpenSellImpl <em>Open Sell</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.OpenSellImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOpenSell()
		 * @generated
		 */
		EClass OPEN_SELL = eINSTANCE.getOpenSell();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.OpenBuyImpl <em>Open Buy</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.OpenBuyImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOpenBuy()
		 * @generated
		 */
		EClass OPEN_BUY = eINSTANCE.getOpenBuy();

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
		 * The meta object literal for the '<em><b>Cv</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__CV = eINSTANCE.getBuyOpportunity_Cv();

		/**
		 * The meta object literal for the '<em><b>Cancellation Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__CANCELLATION_EXPRESSION = eINSTANCE.getBuyOpportunity_CancellationExpression();

		/**
		 * The meta object literal for the '<em><b>Misc Costs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__MISC_COSTS = eINSTANCE.getBuyOpportunity_MiscCosts();

		/**
		 * The meta object literal for the '<em><b>Volume Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__VOLUME_MODE = eINSTANCE.getBuyOpportunity_VolumeMode();

		/**
		 * The meta object literal for the '<em><b>Volume Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__VOLUME_UNITS = eINSTANCE.getBuyOpportunity_VolumeUnits();

		/**
		 * The meta object literal for the '<em><b>Min Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__MIN_VOLUME = eINSTANCE.getBuyOpportunity_MinVolume();

		/**
		 * The meta object literal for the '<em><b>Max Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__MAX_VOLUME = eINSTANCE.getBuyOpportunity_MaxVolume();

		/**
		 * The meta object literal for the '<em><b>Specify Window</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__SPECIFY_WINDOW = eINSTANCE.getBuyOpportunity_SpecifyWindow();

		/**
		 * The meta object literal for the '<em><b>Window Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__WINDOW_SIZE = eINSTANCE.getBuyOpportunity_WindowSize();

		/**
		 * The meta object literal for the '<em><b>Window Size Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__WINDOW_SIZE_UNITS = eINSTANCE.getBuyOpportunity_WindowSizeUnits();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_OPPORTUNITY__NAME = eINSTANCE.getBuyOpportunity_Name();

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
		 * The meta object literal for the '<em><b>Cancellation Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__CANCELLATION_EXPRESSION = eINSTANCE.getSellOpportunity_CancellationExpression();

		/**
		 * The meta object literal for the '<em><b>Misc Costs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__MISC_COSTS = eINSTANCE.getSellOpportunity_MiscCosts();

		/**
		 * The meta object literal for the '<em><b>Volume Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__VOLUME_MODE = eINSTANCE.getSellOpportunity_VolumeMode();

		/**
		 * The meta object literal for the '<em><b>Volume Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__VOLUME_UNITS = eINSTANCE.getSellOpportunity_VolumeUnits();

		/**
		 * The meta object literal for the '<em><b>Min Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__MIN_VOLUME = eINSTANCE.getSellOpportunity_MinVolume();

		/**
		 * The meta object literal for the '<em><b>Max Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__MAX_VOLUME = eINSTANCE.getSellOpportunity_MaxVolume();

		/**
		 * The meta object literal for the '<em><b>Specify Window</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__SPECIFY_WINDOW = eINSTANCE.getSellOpportunity_SpecifyWindow();

		/**
		 * The meta object literal for the '<em><b>Window Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__WINDOW_SIZE = eINSTANCE.getSellOpportunity_WindowSize();

		/**
		 * The meta object literal for the '<em><b>Window Size Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__WINDOW_SIZE_UNITS = eINSTANCE.getSellOpportunity_WindowSizeUnits();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_OPPORTUNITY__NAME = eINSTANCE.getSellOpportunity_Name();

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
		 * The meta object literal for the '<em><b>Month</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUY_MARKET__MONTH = eINSTANCE.getBuyMarket_Month();

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
		 * The meta object literal for the '<em><b>Month</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SELL_MARKET__MONTH = eINSTANCE.getSellMarket_Month();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselEventOptionImpl <em>Vessel Event Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.VesselEventOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselEventOption()
		 * @generated
		 */
		EClass VESSEL_EVENT_OPTION = eINSTANCE.getVesselEventOption();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselEventReferenceImpl <em>Vessel Event Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.VesselEventReferenceImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselEventReference()
		 * @generated
		 */
		EClass VESSEL_EVENT_REFERENCE = eINSTANCE.getVesselEventReference();

		/**
		 * The meta object literal for the '<em><b>Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_EVENT_REFERENCE__EVENT = eINSTANCE.getVesselEventReference_Event();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.CharterOutOpportunityImpl <em>Charter Out Opportunity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.CharterOutOpportunityImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCharterOutOpportunity()
		 * @generated
		 */
		EClass CHARTER_OUT_OPPORTUNITY = eINSTANCE.getCharterOutOpportunity();

		/**
		 * The meta object literal for the '<em><b>Hire Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT_OPPORTUNITY__HIRE_COST = eINSTANCE.getCharterOutOpportunity_HireCost();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_OUT_OPPORTUNITY__PORT = eINSTANCE.getCharterOutOpportunity_Port();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT_OPPORTUNITY__DATE = eINSTANCE.getCharterOutOpportunity_Date();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT_OPPORTUNITY__DURATION = eINSTANCE.getCharterOutOpportunity_Duration();

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
		 * The meta object literal for the '<em><b>Vessel Event Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_CASE_ROW__VESSEL_EVENT_OPTION = eINSTANCE.getBaseCaseRow_VesselEventOption();

		/**
		 * The meta object literal for the '<em><b>Shipping</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_CASE_ROW__SHIPPING = eINSTANCE.getBaseCaseRow_Shipping();

		/**
		 * The meta object literal for the '<em><b>Optionise</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_CASE_ROW__OPTIONISE = eINSTANCE.getBaseCaseRow_Optionise();

		/**
		 * The meta object literal for the '<em><b>Options</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_CASE_ROW__OPTIONS = eINSTANCE.getBaseCaseRow_Options();

		/**
		 * The meta object literal for the '<em><b>Freeze</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_CASE_ROW__FREEZE = eINSTANCE.getBaseCaseRow_Freeze();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowOptionsImpl <em>Base Case Row Options</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.BaseCaseRowOptionsImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBaseCaseRowOptions()
		 * @generated
		 */
		EClass BASE_CASE_ROW_OPTIONS = eINSTANCE.getBaseCaseRowOptions();

		/**
		 * The meta object literal for the '<em><b>Laden Route</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_CASE_ROW_OPTIONS__LADEN_ROUTE = eINSTANCE.getBaseCaseRowOptions_LadenRoute();

		/**
		 * The meta object literal for the '<em><b>Ballast Route</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_CASE_ROW_OPTIONS__BALLAST_ROUTE = eINSTANCE.getBaseCaseRowOptions_BallastRoute();

		/**
		 * The meta object literal for the '<em><b>Laden Fuel Choice</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICE = eINSTANCE.getBaseCaseRowOptions_LadenFuelChoice();

		/**
		 * The meta object literal for the '<em><b>Ballast Fuel Choice</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICE = eINSTANCE.getBaseCaseRowOptions_BallastFuelChoice();

		/**
		 * The meta object literal for the '<em><b>Load Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_CASE_ROW_OPTIONS__LOAD_DATE = eINSTANCE.getBaseCaseRowOptions_LoadDate();

		/**
		 * The meta object literal for the '<em><b>Discharge Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_CASE_ROW_OPTIONS__DISCHARGE_DATE = eINSTANCE.getBaseCaseRowOptions_DischargeDate();

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
		 * The meta object literal for the '<em><b>Vessel Event Options</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS = eINSTANCE.getPartialCaseRow_VesselEventOptions();

		/**
		 * The meta object literal for the '<em><b>Shipping</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARTIAL_CASE_ROW__SHIPPING = eINSTANCE.getPartialCaseRow_Shipping();

		/**
		 * The meta object literal for the '<em><b>Options</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARTIAL_CASE_ROW__OPTIONS = eINSTANCE.getPartialCaseRow_Options();

		/**
		 * The meta object literal for the '<em><b>Commodity Curve Options</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARTIAL_CASE_ROW__COMMODITY_CURVE_OPTIONS = eINSTANCE.getPartialCaseRow_CommodityCurveOptions();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowOptionsImpl <em>Partial Case Row Options</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.PartialCaseRowOptionsImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPartialCaseRowOptions()
		 * @generated
		 */
		EClass PARTIAL_CASE_ROW_OPTIONS = eINSTANCE.getPartialCaseRowOptions();

		/**
		 * The meta object literal for the '<em><b>Laden Routes</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES = eINSTANCE.getPartialCaseRowOptions_LadenRoutes();

		/**
		 * The meta object literal for the '<em><b>Ballast Routes</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARTIAL_CASE_ROW_OPTIONS__BALLAST_ROUTES = eINSTANCE.getPartialCaseRowOptions_BallastRoutes();

		/**
		 * The meta object literal for the '<em><b>Laden Fuel Choices</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES = eINSTANCE.getPartialCaseRowOptions_LadenFuelChoices();

		/**
		 * The meta object literal for the '<em><b>Ballast Fuel Choices</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARTIAL_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICES = eINSTANCE.getPartialCaseRowOptions_BallastFuelChoices();

		/**
		 * The meta object literal for the '<em><b>Load Dates</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES = eINSTANCE.getPartialCaseRowOptions_LoadDates();

		/**
		 * The meta object literal for the '<em><b>Discharge Dates</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES = eINSTANCE.getPartialCaseRowOptions_DischargeDates();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SimpleVesselCharterOptionImpl <em>Simple Vessel Charter Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SimpleVesselCharterOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSimpleVesselCharterOption()
		 * @generated
		 */
		EClass SIMPLE_VESSEL_CHARTER_OPTION = eINSTANCE.getSimpleVesselCharterOption();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SIMPLE_VESSEL_CHARTER_OPTION__VESSEL = eINSTANCE.getSimpleVesselCharterOption_Vessel();

		/**
		 * The meta object literal for the '<em><b>Hire Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST = eINSTANCE.getSimpleVesselCharterOption_HireCost();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SIMPLE_VESSEL_CHARTER_OPTION__ENTITY = eINSTANCE.getSimpleVesselCharterOption_Entity();

		/**
		 * The meta object literal for the '<em><b>Use Safety Heel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL = eINSTANCE.getSimpleVesselCharterOption_UseSafetyHeel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.OptionalSimpleVesselCharterOptionImpl <em>Optional Simple Vessel Charter Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.OptionalSimpleVesselCharterOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptionalSimpleVesselCharterOption()
		 * @generated
		 */
		EClass OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION = eINSTANCE.getOptionalSimpleVesselCharterOption();

		/**
		 * The meta object literal for the '<em><b>Ballast Bonus</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__BALLAST_BONUS = eINSTANCE.getOptionalSimpleVesselCharterOption_BallastBonus();

		/**
		 * The meta object literal for the '<em><b>Repositioning Fee</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__REPOSITIONING_FEE = eINSTANCE.getOptionalSimpleVesselCharterOption_RepositioningFee();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START = eINSTANCE.getOptionalSimpleVesselCharterOption_Start();

		/**
		 * The meta object literal for the '<em><b>End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END = eINSTANCE.getOptionalSimpleVesselCharterOption_End();

		/**
		 * The meta object literal for the '<em><b>Start Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START_PORT = eINSTANCE.getOptionalSimpleVesselCharterOption_StartPort();

		/**
		 * The meta object literal for the '<em><b>End Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END_PORT = eINSTANCE.getOptionalSimpleVesselCharterOption_EndPort();

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
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUND_TRIP_SHIPPING_OPTION__VESSEL = eINSTANCE.getRoundTripShippingOption_Vessel();

		/**
		 * The meta object literal for the '<em><b>Hire Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUND_TRIP_SHIPPING_OPTION__HIRE_COST = eINSTANCE.getRoundTripShippingOption_HireCost();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUND_TRIP_SHIPPING_OPTION__ENTITY = eINSTANCE.getRoundTripShippingOption_Entity();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.FullVesselCharterOptionImpl <em>Full Vessel Charter Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.FullVesselCharterOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getFullVesselCharterOption()
		 * @generated
		 */
		EClass FULL_VESSEL_CHARTER_OPTION = eINSTANCE.getFullVesselCharterOption();

		/**
		 * The meta object literal for the '<em><b>Vessel Charter</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER = eINSTANCE.getFullVesselCharterOption_VesselCharter();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ExistingVesselCharterOptionImpl <em>Existing Vessel Charter Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ExistingVesselCharterOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getExistingVesselCharterOption()
		 * @generated
		 */
		EClass EXISTING_VESSEL_CHARTER_OPTION = eINSTANCE.getExistingVesselCharterOption();

		/**
		 * The meta object literal for the '<em><b>Vessel Charter</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXISTING_VESSEL_CHARTER_OPTION__VESSEL_CHARTER = eINSTANCE.getExistingVesselCharterOption_VesselCharter();

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
		 * The meta object literal for the '<em><b>Price String</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BREAK_EVEN_RESULT__PRICE_STRING = eINSTANCE.getBreakEvenResult_PriceString();

		/**
		 * The meta object literal for the '<em><b>Cargo PNL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BREAK_EVEN_RESULT__CARGO_PNL = eINSTANCE.getBreakEvenResult_CargoPNL();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl <em>Abstract Analysis Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAbstractAnalysisModel()
		 * @generated
		 */
		EClass ABSTRACT_ANALYSIS_MODEL = eINSTANCE.getAbstractAnalysisModel();

		/**
		 * The meta object literal for the '<em><b>Buys</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_ANALYSIS_MODEL__BUYS = eINSTANCE.getAbstractAnalysisModel_Buys();

		/**
		 * The meta object literal for the '<em><b>Sells</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_ANALYSIS_MODEL__SELLS = eINSTANCE.getAbstractAnalysisModel_Sells();

		/**
		 * The meta object literal for the '<em><b>Vessel Events</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS = eINSTANCE.getAbstractAnalysisModel_VesselEvents();

		/**
		 * The meta object literal for the '<em><b>Shipping Templates</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES = eINSTANCE.getAbstractAnalysisModel_ShippingTemplates();

		/**
		 * The meta object literal for the '<em><b>Commodity Curves</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES = eINSTANCE.getAbstractAnalysisModel_CommodityCurves();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl <em>Marketability Result Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityResultContainer()
		 * @generated
		 */
		EClass MARKETABILITY_RESULT_CONTAINER = eINSTANCE.getMarketabilityResultContainer();

		/**
		 * The meta object literal for the '<em><b>Rhs Results</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKETABILITY_RESULT_CONTAINER__RHS_RESULTS = eINSTANCE.getMarketabilityResultContainer_RhsResults();

		/**
		 * The meta object literal for the '<em><b>Next Event</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKETABILITY_RESULT_CONTAINER__NEXT_EVENT = eINSTANCE.getMarketabilityResultContainer_NextEvent();

		/**
		 * The meta object literal for the '<em><b>Laden Panama</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA = eINSTANCE.getMarketabilityResultContainer_LadenPanama();

		/**
		 * The meta object literal for the '<em><b>Ballast Panama</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA = eINSTANCE.getMarketabilityResultContainer_BallastPanama();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityEventImpl <em>Marketability Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityEventImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityEvent()
		 * @generated
		 */
		EClass MARKETABILITY_EVENT = eINSTANCE.getMarketabilityEvent();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKETABILITY_EVENT__START = eINSTANCE.getMarketabilityEvent_Start();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityAssignableElementImpl <em>Marketability Assignable Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityAssignableElementImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityAssignableElement()
		 * @generated
		 */
		EClass MARKETABILITY_ASSIGNABLE_ELEMENT = eINSTANCE.getMarketabilityAssignableElement();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKETABILITY_ASSIGNABLE_ELEMENT__ELEMENT = eINSTANCE.getMarketabilityAssignableElement_Element();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityEndEventImpl <em>Marketability End Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityEndEventImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityEndEvent()
		 * @generated
		 */
		EClass MARKETABILITY_END_EVENT = eINSTANCE.getMarketabilityEndEvent();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl <em>Swap Value Matrix Parameters</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixParameters()
		 * @generated
		 */
		EClass SWAP_VALUE_MATRIX_PARAMETERS = eINSTANCE.getSwapValueMatrixParameters();

		/**
		 * The meta object literal for the '<em><b>Base Load</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_PARAMETERS__BASE_LOAD = eINSTANCE.getSwapValueMatrixParameters_BaseLoad();

		/**
		 * The meta object literal for the '<em><b>Base Discharge</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_PARAMETERS__BASE_DISCHARGE = eINSTANCE.getSwapValueMatrixParameters_BaseDischarge();

		/**
		 * The meta object literal for the '<em><b>Base Vessel Charter</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_PARAMETERS__BASE_VESSEL_CHARTER = eINSTANCE.getSwapValueMatrixParameters_BaseVesselCharter();

		/**
		 * The meta object literal for the '<em><b>Base Price Range</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_PARAMETERS__BASE_PRICE_RANGE = eINSTANCE.getSwapValueMatrixParameters_BasePriceRange();

		/**
		 * The meta object literal for the '<em><b>Swap Load Market</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_PARAMETERS__SWAP_LOAD_MARKET = eINSTANCE.getSwapValueMatrixParameters_SwapLoadMarket();

		/**
		 * The meta object literal for the '<em><b>Swap Discharge Market</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_PARAMETERS__SWAP_DISCHARGE_MARKET = eINSTANCE.getSwapValueMatrixParameters_SwapDischargeMarket();

		/**
		 * The meta object literal for the '<em><b>Swap Fee</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_PARAMETERS__SWAP_FEE = eINSTANCE.getSwapValueMatrixParameters_SwapFee();

		/**
		 * The meta object literal for the '<em><b>Swap Price Range</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_PARAMETERS__SWAP_PRICE_RANGE = eINSTANCE.getSwapValueMatrixParameters_SwapPriceRange();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.RangeImpl <em>Range</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.RangeImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getRange()
		 * @generated
		 */
		EClass RANGE = eINSTANCE.getRange();

		/**
		 * The meta object literal for the '<em><b>Min</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RANGE__MIN = eINSTANCE.getRange_Min();

		/**
		 * The meta object literal for the '<em><b>Max</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RANGE__MAX = eINSTANCE.getRange_Max();

		/**
		 * The meta object literal for the '<em><b>Step Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RANGE__STEP_SIZE = eINSTANCE.getRange_StepSize();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixCargoResultImpl <em>Swap Value Matrix Cargo Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixCargoResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixCargoResult()
		 * @generated
		 */
		EClass SWAP_VALUE_MATRIX_CARGO_RESULT = eINSTANCE.getSwapValueMatrixCargoResult();

		/**
		 * The meta object literal for the '<em><b>Load Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_CARGO_RESULT__LOAD_PRICE = eINSTANCE.getSwapValueMatrixCargoResult_LoadPrice();

		/**
		 * The meta object literal for the '<em><b>Discharge Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_CARGO_RESULT__DISCHARGE_PRICE = eINSTANCE.getSwapValueMatrixCargoResult_DischargePrice();

		/**
		 * The meta object literal for the '<em><b>Purchase Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_CARGO_RESULT__PURCHASE_COST = eINSTANCE.getSwapValueMatrixCargoResult_PurchaseCost();

		/**
		 * The meta object literal for the '<em><b>Sales Revenue</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_CARGO_RESULT__SALES_REVENUE = eINSTANCE.getSwapValueMatrixCargoResult_SalesRevenue();

		/**
		 * The meta object literal for the '<em><b>Additional Pnl</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_CARGO_RESULT__ADDITIONAL_PNL = eINSTANCE.getSwapValueMatrixCargoResult_AdditionalPnl();

		/**
		 * The meta object literal for the '<em><b>Total Pnl</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_CARGO_RESULT__TOTAL_PNL = eINSTANCE.getSwapValueMatrixCargoResult_TotalPnl();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixShippedCargoResultImpl <em>Swap Value Matrix Shipped Cargo Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixShippedCargoResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixShippedCargoResult()
		 * @generated
		 */
		EClass SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT = eINSTANCE.getSwapValueMatrixShippedCargoResult();

		/**
		 * The meta object literal for the '<em><b>Shipping Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__SHIPPING_COST = eINSTANCE.getSwapValueMatrixShippedCargoResult_ShippingCost();

		/**
		 * The meta object literal for the '<em><b>Load Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__LOAD_VOLUME = eINSTANCE.getSwapValueMatrixShippedCargoResult_LoadVolume();

		/**
		 * The meta object literal for the '<em><b>Discharge Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__DISCHARGE_VOLUME = eINSTANCE.getSwapValueMatrixShippedCargoResult_DischargeVolume();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixNonShippedCargoResultImpl <em>Swap Value Matrix Non Shipped Cargo Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixNonShippedCargoResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixNonShippedCargoResult()
		 * @generated
		 */
		EClass SWAP_VALUE_MATRIX_NON_SHIPPED_CARGO_RESULT = eINSTANCE.getSwapValueMatrixNonShippedCargoResult();

		/**
		 * The meta object literal for the '<em><b>Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_NON_SHIPPED_CARGO_RESULT__VOLUME = eINSTANCE.getSwapValueMatrixNonShippedCargoResult_Volume();

		/**
		 * The meta object literal for the '<em><b>Buy Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKETABILITY_RESULT_CONTAINER__BUY_DATE = eINSTANCE.getMarketabilityResultContainer_BuyDate();

		/**
		 * The meta object literal for the '<em><b>Sell Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKETABILITY_RESULT_CONTAINER__SELL_DATE = eINSTANCE.getMarketabilityResultContainer_SellDate();

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
		 * The meta object literal for the '<em><b>Keep Existing Scenario</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_CASE__KEEP_EXISTING_SCENARIO = eINSTANCE.getBaseCase_KeepExistingScenario();

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
		 * The meta object literal for the '<em><b>Keep Existing Scenario</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARTIAL_CASE__KEEP_EXISTING_SCENARIO = eINSTANCE.getPartialCase_KeepExistingScenario();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ExistingCharterMarketOptionImpl <em>Existing Charter Market Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ExistingCharterMarketOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getExistingCharterMarketOption()
		 * @generated
		 */
		EClass EXISTING_CHARTER_MARKET_OPTION = eINSTANCE.getExistingCharterMarketOption();

		/**
		 * The meta object literal for the '<em><b>Charter In Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET = eINSTANCE.getExistingCharterMarketOption_CharterInMarket();

		/**
		 * The meta object literal for the '<em><b>Spot Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX = eINSTANCE.getExistingCharterMarketOption_SpotIndex();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl <em>Abstract Solution Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAbstractSolutionSet()
		 * @generated
		 */
		EClass ABSTRACT_SOLUTION_SET = eINSTANCE.getAbstractSolutionSet();

		/**
		 * The meta object literal for the '<em><b>Has Dual Mode Solutions</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS = eINSTANCE.getAbstractSolutionSet_HasDualModeSolutions();

		/**
		 * The meta object literal for the '<em><b>Portfolio Break Even Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE = eINSTANCE.getAbstractSolutionSet_PortfolioBreakEvenMode();

		/**
		 * The meta object literal for the '<em><b>User Settings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_SOLUTION_SET__USER_SETTINGS = eINSTANCE.getAbstractSolutionSet_UserSettings();

		/**
		 * The meta object literal for the '<em><b>Options</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_SOLUTION_SET__OPTIONS = eINSTANCE.getAbstractSolutionSet_Options();

		/**
		 * The meta object literal for the '<em><b>Extra Vessel Events</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS = eINSTANCE.getAbstractSolutionSet_ExtraVesselEvents();

		/**
		 * The meta object literal for the '<em><b>Extra Vessel Charters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_CHARTERS = eINSTANCE.getAbstractSolutionSet_ExtraVesselCharters();

		/**
		 * The meta object literal for the '<em><b>Charter In Market Overrides</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES = eINSTANCE.getAbstractSolutionSet_CharterInMarketOverrides();

		/**
		 * The meta object literal for the '<em><b>Extra Charter In Markets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS = eINSTANCE.getAbstractSolutionSet_ExtraCharterInMarkets();

		/**
		 * The meta object literal for the '<em><b>Use Scenario Base</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE = eINSTANCE.getAbstractSolutionSet_UseScenarioBase();

		/**
		 * The meta object literal for the '<em><b>Extra Slots</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_SOLUTION_SET__EXTRA_SLOTS = eINSTANCE.getAbstractSolutionSet_ExtraSlots();

		/**
		 * The meta object literal for the '<em><b>Base Option</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_SOLUTION_SET__BASE_OPTION = eINSTANCE.getAbstractSolutionSet_BaseOption();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ActionableSetPlanImpl <em>Actionable Set Plan</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ActionableSetPlanImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getActionableSetPlan()
		 * @generated
		 */
		EClass ACTIONABLE_SET_PLAN = eINSTANCE.getActionableSetPlan();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionsImpl <em>Slot Insertion Options</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionsImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSlotInsertionOptions()
		 * @generated
		 */
		EClass SLOT_INSERTION_OPTIONS = eINSTANCE.getSlotInsertionOptions();

		/**
		 * The meta object literal for the '<em><b>Slots Inserted</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_INSERTION_OPTIONS__SLOTS_INSERTED = eINSTANCE.getSlotInsertionOptions_SlotsInserted();

		/**
		 * The meta object literal for the '<em><b>Events Inserted</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_INSERTION_OPTIONS__EVENTS_INSERTED = eINSTANCE.getSlotInsertionOptions_EventsInserted();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ChangeDescriptionImpl <em>Change Description</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ChangeDescriptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getChangeDescription()
		 * @generated
		 */
		EClass CHANGE_DESCRIPTION = eINSTANCE.getChangeDescription();

		/**
		 * The meta object literal for the '<em><b>Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_DESCRIPTION__CHANGES = eINSTANCE.getChangeDescription_Changes();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ChangeImpl <em>Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ChangeImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getChange()
		 * @generated
		 */
		EClass CHANGE = eINSTANCE.getChange();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.OpenSlotChangeImpl <em>Open Slot Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.OpenSlotChangeImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOpenSlotChange()
		 * @generated
		 */
		EClass OPEN_SLOT_CHANGE = eINSTANCE.getOpenSlotChange();

		/**
		 * The meta object literal for the '<em><b>Slot Descriptor</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR = eINSTANCE.getOpenSlotChange_SlotDescriptor();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.CargoChangeImpl <em>Cargo Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.CargoChangeImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCargoChange()
		 * @generated
		 */
		EClass CARGO_CHANGE = eINSTANCE.getCargoChange();

		/**
		 * The meta object literal for the '<em><b>Slot Descriptors</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_CHANGE__SLOT_DESCRIPTORS = eINSTANCE.getCargoChange_SlotDescriptors();

		/**
		 * The meta object literal for the '<em><b>Vessel Allocation</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_CHANGE__VESSEL_ALLOCATION = eINSTANCE.getCargoChange_VesselAllocation();

		/**
		 * The meta object literal for the '<em><b>Position</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_CHANGE__POSITION = eINSTANCE.getCargoChange_Position();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselEventChangeImpl <em>Vessel Event Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.VesselEventChangeImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselEventChange()
		 * @generated
		 */
		EClass VESSEL_EVENT_CHANGE = eINSTANCE.getVesselEventChange();

		/**
		 * The meta object literal for the '<em><b>Vessel Event Descriptor</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR = eINSTANCE.getVesselEventChange_VesselEventDescriptor();

		/**
		 * The meta object literal for the '<em><b>Vessel Allocation</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION = eINSTANCE.getVesselEventChange_VesselAllocation();

		/**
		 * The meta object literal for the '<em><b>Position</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_EVENT_CHANGE__POSITION = eINSTANCE.getVesselEventChange_Position();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselEventDescriptorImpl <em>Vessel Event Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.VesselEventDescriptorImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselEventDescriptor()
		 * @generated
		 */
		EClass VESSEL_EVENT_DESCRIPTOR = eINSTANCE.getVesselEventDescriptor();

		/**
		 * The meta object literal for the '<em><b>Event Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_EVENT_DESCRIPTOR__EVENT_NAME = eINSTANCE.getVesselEventDescriptor_EventName();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SlotDescriptorImpl <em>Slot Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SlotDescriptorImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSlotDescriptor()
		 * @generated
		 */
		EClass SLOT_DESCRIPTOR = eINSTANCE.getSlotDescriptor();

		/**
		 * The meta object literal for the '<em><b>Slot Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_DESCRIPTOR__SLOT_TYPE = eINSTANCE.getSlotDescriptor_SlotType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.RealSlotDescriptorImpl <em>Real Slot Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.RealSlotDescriptorImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getRealSlotDescriptor()
		 * @generated
		 */
		EClass REAL_SLOT_DESCRIPTOR = eINSTANCE.getRealSlotDescriptor();

		/**
		 * The meta object literal for the '<em><b>Slot Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REAL_SLOT_DESCRIPTOR__SLOT_NAME = eINSTANCE.getRealSlotDescriptor_SlotName();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SpotMarketSlotDescriptorImpl <em>Spot Market Slot Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SpotMarketSlotDescriptorImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSpotMarketSlotDescriptor()
		 * @generated
		 */
		EClass SPOT_MARKET_SLOT_DESCRIPTOR = eINSTANCE.getSpotMarketSlotDescriptor();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPOT_MARKET_SLOT_DESCRIPTOR__DATE = eINSTANCE.getSpotMarketSlotDescriptor_Date();

		/**
		 * The meta object literal for the '<em><b>Market Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPOT_MARKET_SLOT_DESCRIPTOR__MARKET_NAME = eINSTANCE.getSpotMarketSlotDescriptor_MarketName();

		/**
		 * The meta object literal for the '<em><b>Port Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPOT_MARKET_SLOT_DESCRIPTOR__PORT_NAME = eINSTANCE.getSpotMarketSlotDescriptor_PortName();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselAllocationDescriptorImpl <em>Vessel Allocation Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.VesselAllocationDescriptorImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselAllocationDescriptor()
		 * @generated
		 */
		EClass VESSEL_ALLOCATION_DESCRIPTOR = eINSTANCE.getVesselAllocationDescriptor();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketVesselAllocationDescriptorImpl <em>Market Vessel Allocation Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.MarketVesselAllocationDescriptorImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketVesselAllocationDescriptor()
		 * @generated
		 */
		EClass MARKET_VESSEL_ALLOCATION_DESCRIPTOR = eINSTANCE.getMarketVesselAllocationDescriptor();

		/**
		 * The meta object literal for the '<em><b>Market Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKET_VESSEL_ALLOCATION_DESCRIPTOR__MARKET_NAME = eINSTANCE.getMarketVesselAllocationDescriptor_MarketName();

		/**
		 * The meta object literal for the '<em><b>Spot Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKET_VESSEL_ALLOCATION_DESCRIPTOR__SPOT_INDEX = eINSTANCE.getMarketVesselAllocationDescriptor_SpotIndex();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.FleetVesselAllocationDescriptorImpl <em>Fleet Vessel Allocation Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.FleetVesselAllocationDescriptorImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getFleetVesselAllocationDescriptor()
		 * @generated
		 */
		EClass FLEET_VESSEL_ALLOCATION_DESCRIPTOR = eINSTANCE.getFleetVesselAllocationDescriptor();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLEET_VESSEL_ALLOCATION_DESCRIPTOR__NAME = eINSTANCE.getFleetVesselAllocationDescriptor_Name();

		/**
		 * The meta object literal for the '<em><b>Charter Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLEET_VESSEL_ALLOCATION_DESCRIPTOR__CHARTER_INDEX = eINSTANCE.getFleetVesselAllocationDescriptor_CharterIndex();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.PositionDescriptorImpl <em>Position Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.PositionDescriptorImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPositionDescriptor()
		 * @generated
		 */
		EClass POSITION_DESCRIPTOR = eINSTANCE.getPositionDescriptor();

		/**
		 * The meta object literal for the '<em><b>After</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POSITION_DESCRIPTOR__AFTER = eINSTANCE.getPositionDescriptor_After();

		/**
		 * The meta object literal for the '<em><b>Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POSITION_DESCRIPTOR__BEFORE = eINSTANCE.getPositionDescriptor_Before();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ViabilityModelImpl <em>Viability Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ViabilityModelImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getViabilityModel()
		 * @generated
		 */
		EClass VIABILITY_MODEL = eINSTANCE.getViabilityModel();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIABILITY_MODEL__ROWS = eINSTANCE.getViabilityModel_Rows();

		/**
		 * The meta object literal for the '<em><b>Markets</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIABILITY_MODEL__MARKETS = eINSTANCE.getViabilityModel_Markets();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityModelImpl <em>Marketability Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityModelImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityModel()
		 * @generated
		 */
		EClass MARKETABILITY_MODEL = eINSTANCE.getMarketabilityModel();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKETABILITY_MODEL__ROWS = eINSTANCE.getMarketabilityModel_Rows();

		/**
		 * The meta object literal for the '<em><b>Markets</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKETABILITY_MODEL__MARKETS = eINSTANCE.getMarketabilityModel_Markets();

		/**
		 * The meta object literal for the '<em><b>Vessel Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKETABILITY_MODEL__VESSEL_SPEED = eINSTANCE.getMarketabilityModel_VesselSpeed();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl <em>Marketability Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityRow()
		 * @generated
		 */
		EClass MARKETABILITY_ROW = eINSTANCE.getMarketabilityRow();

		/**
		 * The meta object literal for the '<em><b>Buy Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKETABILITY_ROW__BUY_OPTION = eINSTANCE.getMarketabilityRow_BuyOption();

		/**
		 * The meta object literal for the '<em><b>Sell Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKETABILITY_ROW__SELL_OPTION = eINSTANCE.getMarketabilityRow_SellOption();

		/**
		 * The meta object literal for the '<em><b>Shipping</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKETABILITY_ROW__SHIPPING = eINSTANCE.getMarketabilityRow_Shipping();

		/**
		 * The meta object literal for the '<em><b>Result</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKETABILITY_ROW__RESULT = eINSTANCE.getMarketabilityRow_Result();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultImpl <em>Marketability Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.MarketabilityResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketabilityResult()
		 * @generated
		 */
		EClass MARKETABILITY_RESULT = eINSTANCE.getMarketabilityResult();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKETABILITY_RESULT__TARGET = eINSTANCE.getMarketabilityResult_Target();

		/**
		 * The meta object literal for the '<em><b>Earliest ETA</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKETABILITY_RESULT__EARLIEST_ETA = eINSTANCE.getMarketabilityResult_EarliestETA();

		/**
		 * The meta object literal for the '<em><b>Latest ETA</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKETABILITY_RESULT__LATEST_ETA = eINSTANCE.getMarketabilityResult_LatestETA();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl <em>Viability Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getViabilityRow()
		 * @generated
		 */
		EClass VIABILITY_ROW = eINSTANCE.getViabilityRow();

		/**
		 * The meta object literal for the '<em><b>Buy Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIABILITY_ROW__BUY_OPTION = eINSTANCE.getViabilityRow_BuyOption();

		/**
		 * The meta object literal for the '<em><b>Sell Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIABILITY_ROW__SELL_OPTION = eINSTANCE.getViabilityRow_SellOption();

		/**
		 * The meta object literal for the '<em><b>Shipping</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIABILITY_ROW__SHIPPING = eINSTANCE.getViabilityRow_Shipping();

		/**
		 * The meta object literal for the '<em><b>Lhs Results</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIABILITY_ROW__LHS_RESULTS = eINSTANCE.getViabilityRow_LhsResults();

		/**
		 * The meta object literal for the '<em><b>Rhs Results</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIABILITY_ROW__RHS_RESULTS = eINSTANCE.getViabilityRow_RhsResults();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIABILITY_ROW__TARGET = eINSTANCE.getViabilityRow_Target();

		/**
		 * The meta object literal for the '<em><b>Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIABILITY_ROW__PRICE = eINSTANCE.getViabilityRow_Price();

		/**
		 * The meta object literal for the '<em><b>Eta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIABILITY_ROW__ETA = eINSTANCE.getViabilityRow_Eta();

		/**
		 * The meta object literal for the '<em><b>Reference Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIABILITY_ROW__REFERENCE_PRICE = eINSTANCE.getViabilityRow_ReferencePrice();

		/**
		 * The meta object literal for the '<em><b>Start Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIABILITY_ROW__START_VOLUME = eINSTANCE.getViabilityRow_StartVolume();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ViabilityResultImpl <em>Viability Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ViabilityResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getViabilityResult()
		 * @generated
		 */
		EClass VIABILITY_RESULT = eINSTANCE.getViabilityResult();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIABILITY_RESULT__TARGET = eINSTANCE.getViabilityResult_Target();

		/**
		 * The meta object literal for the '<em><b>Earliest ETA</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIABILITY_RESULT__EARLIEST_ETA = eINSTANCE.getViabilityResult_EarliestETA();

		/**
		 * The meta object literal for the '<em><b>Latest ETA</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIABILITY_RESULT__LATEST_ETA = eINSTANCE.getViabilityResult_LatestETA();

		/**
		 * The meta object literal for the '<em><b>Earliest Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIABILITY_RESULT__EARLIEST_VOLUME = eINSTANCE.getViabilityResult_EarliestVolume();

		/**
		 * The meta object literal for the '<em><b>Latest Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIABILITY_RESULT__LATEST_VOLUME = eINSTANCE.getViabilityResult_LatestVolume();

		/**
		 * The meta object literal for the '<em><b>Earliest Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIABILITY_RESULT__EARLIEST_PRICE = eINSTANCE.getViabilityResult_EarliestPrice();

		/**
		 * The meta object literal for the '<em><b>Latest Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIABILITY_RESULT__LATEST_PRICE = eINSTANCE.getViabilityResult_LatestPrice();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.MTMModelImpl <em>MTM Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.MTMModelImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMTMModel()
		 * @generated
		 */
		EClass MTM_MODEL = eINSTANCE.getMTMModel();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MTM_MODEL__ROWS = eINSTANCE.getMTMModel_Rows();

		/**
		 * The meta object literal for the '<em><b>Markets</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MTM_MODEL__MARKETS = eINSTANCE.getMTMModel_Markets();

		/**
		 * The meta object literal for the '<em><b>Nominal Markets</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MTM_MODEL__NOMINAL_MARKETS = eINSTANCE.getMTMModel_NominalMarkets();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.MTMResultImpl <em>MTM Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.MTMResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMTMResult()
		 * @generated
		 */
		EClass MTM_RESULT = eINSTANCE.getMTMResult();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MTM_RESULT__TARGET = eINSTANCE.getMTMResult_Target();

		/**
		 * The meta object literal for the '<em><b>Earliest ETA</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MTM_RESULT__EARLIEST_ETA = eINSTANCE.getMTMResult_EarliestETA();

		/**
		 * The meta object literal for the '<em><b>Earliest Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MTM_RESULT__EARLIEST_VOLUME = eINSTANCE.getMTMResult_EarliestVolume();

		/**
		 * The meta object literal for the '<em><b>Earliest Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MTM_RESULT__EARLIEST_PRICE = eINSTANCE.getMTMResult_EarliestPrice();

		/**
		 * The meta object literal for the '<em><b>Shipping</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MTM_RESULT__SHIPPING = eINSTANCE.getMTMResult_Shipping();

		/**
		 * The meta object literal for the '<em><b>Shipping Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MTM_RESULT__SHIPPING_COST = eINSTANCE.getMTMResult_ShippingCost();

		/**
		 * The meta object literal for the '<em><b>Original Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MTM_RESULT__ORIGINAL_VOLUME = eINSTANCE.getMTMResult_OriginalVolume();

		/**
		 * The meta object literal for the '<em><b>Original Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MTM_RESULT__ORIGINAL_PRICE = eINSTANCE.getMTMResult_OriginalPrice();

		/**
		 * The meta object literal for the '<em><b>Total Shipping Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MTM_RESULT__TOTAL_SHIPPING_COST = eINSTANCE.getMTMResult_TotalShippingCost();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.MTMRowImpl <em>MTM Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.MTMRowImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMTMRow()
		 * @generated
		 */
		EClass MTM_ROW = eINSTANCE.getMTMRow();

		/**
		 * The meta object literal for the '<em><b>Buy Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MTM_ROW__BUY_OPTION = eINSTANCE.getMTMRow_BuyOption();

		/**
		 * The meta object literal for the '<em><b>Sell Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MTM_ROW__SELL_OPTION = eINSTANCE.getMTMRow_SellOption();

		/**
		 * The meta object literal for the '<em><b>Lhs Results</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MTM_ROW__LHS_RESULTS = eINSTANCE.getMTMRow_LhsResults();

		/**
		 * The meta object literal for the '<em><b>Rhs Results</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MTM_ROW__RHS_RESULTS = eINSTANCE.getMTMRow_RhsResults();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MTM_ROW__TARGET = eINSTANCE.getMTMRow_Target();

		/**
		 * The meta object literal for the '<em><b>Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MTM_ROW__PRICE = eINSTANCE.getMTMRow_Price();

		/**
		 * The meta object literal for the '<em><b>Eta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MTM_ROW__ETA = eINSTANCE.getMTMRow_Eta();

		/**
		 * The meta object literal for the '<em><b>Reference Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MTM_ROW__REFERENCE_PRICE = eINSTANCE.getMTMRow_ReferencePrice();

		/**
		 * The meta object literal for the '<em><b>Start Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MTM_ROW__START_VOLUME = eINSTANCE.getMTMRow_StartVolume();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisModelImpl <em>Break Even Analysis Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisModelImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBreakEvenAnalysisModel()
		 * @generated
		 */
		EClass BREAK_EVEN_ANALYSIS_MODEL = eINSTANCE.getBreakEvenAnalysisModel();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_MODEL__ROWS = eINSTANCE.getBreakEvenAnalysisModel_Rows();

		/**
		 * The meta object literal for the '<em><b>Markets</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_MODEL__MARKETS = eINSTANCE.getBreakEvenAnalysisModel_Markets();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisRowImpl <em>Break Even Analysis Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisRowImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBreakEvenAnalysisRow()
		 * @generated
		 */
		EClass BREAK_EVEN_ANALYSIS_ROW = eINSTANCE.getBreakEvenAnalysisRow();

		/**
		 * The meta object literal for the '<em><b>Buy Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION = eINSTANCE.getBreakEvenAnalysisRow_BuyOption();

		/**
		 * The meta object literal for the '<em><b>Sell Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION = eINSTANCE.getBreakEvenAnalysisRow_SellOption();

		/**
		 * The meta object literal for the '<em><b>Shipping</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_ROW__SHIPPING = eINSTANCE.getBreakEvenAnalysisRow_Shipping();

		/**
		 * The meta object literal for the '<em><b>Lhs Results</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS = eINSTANCE.getBreakEvenAnalysisRow_LhsResults();

		/**
		 * The meta object literal for the '<em><b>Rhs Results</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS = eINSTANCE.getBreakEvenAnalysisRow_RhsResults();

		/**
		 * The meta object literal for the '<em><b>Lhs Based On</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON = eINSTANCE.getBreakEvenAnalysisRow_LhsBasedOn();

		/**
		 * The meta object literal for the '<em><b>Rhs Based On</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON = eINSTANCE.getBreakEvenAnalysisRow_RhsBasedOn();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultSetImpl <em>Break Even Analysis Result Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultSetImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBreakEvenAnalysisResultSet()
		 * @generated
		 */
		EClass BREAK_EVEN_ANALYSIS_RESULT_SET = eINSTANCE.getBreakEvenAnalysisResultSet();

		/**
		 * The meta object literal for the '<em><b>Based On</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_RESULT_SET__BASED_ON = eINSTANCE.getBreakEvenAnalysisResultSet_BasedOn();

		/**
		 * The meta object literal for the '<em><b>Results</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_RESULT_SET__RESULTS = eINSTANCE.getBreakEvenAnalysisResultSet_Results();

		/**
		 * The meta object literal for the '<em><b>Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BREAK_EVEN_ANALYSIS_RESULT_SET__PRICE = eINSTANCE.getBreakEvenAnalysisResultSet_Price();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultImpl <em>Break Even Analysis Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBreakEvenAnalysisResult()
		 * @generated
		 */
		EClass BREAK_EVEN_ANALYSIS_RESULT = eINSTANCE.getBreakEvenAnalysisResult();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BREAK_EVEN_ANALYSIS_RESULT__TARGET = eINSTANCE.getBreakEvenAnalysisResult_Target();

		/**
		 * The meta object literal for the '<em><b>Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BREAK_EVEN_ANALYSIS_RESULT__PRICE = eINSTANCE.getBreakEvenAnalysisResult_Price();

		/**
		 * The meta object literal for the '<em><b>Eta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BREAK_EVEN_ANALYSIS_RESULT__ETA = eINSTANCE.getBreakEvenAnalysisResult_Eta();

		/**
		 * The meta object literal for the '<em><b>Reference Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BREAK_EVEN_ANALYSIS_RESULT__REFERENCE_PRICE = eINSTANCE.getBreakEvenAnalysisResult_ReferencePrice();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.LocalDateTimeHolderImpl <em>Local Date Time Holder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.LocalDateTimeHolderImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getLocalDateTimeHolder()
		 * @generated
		 */
		EClass LOCAL_DATE_TIME_HOLDER = eINSTANCE.getLocalDateTimeHolder();

		/**
		 * The meta object literal for the '<em><b>Date Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCAL_DATE_TIME_HOLDER__DATE_TIME = eINSTANCE.getLocalDateTimeHolder_DateTime();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.CommodityCurveOptionImpl <em>Commodity Curve Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.CommodityCurveOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCommodityCurveOption()
		 * @generated
		 */
		EClass COMMODITY_CURVE_OPTION = eINSTANCE.getCommodityCurveOption();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.CommodityCurveOverlayImpl <em>Commodity Curve Overlay</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.CommodityCurveOverlayImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCommodityCurveOverlay()
		 * @generated
		 */
		EClass COMMODITY_CURVE_OVERLAY = eINSTANCE.getCommodityCurveOverlay();

		/**
		 * The meta object literal for the '<em><b>Reference Curve</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMODITY_CURVE_OVERLAY__REFERENCE_CURVE = eINSTANCE.getCommodityCurveOverlay_ReferenceCurve();

		/**
		 * The meta object literal for the '<em><b>Alternative Curves</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES = eINSTANCE.getCommodityCurveOverlay_AlternativeCurves();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SensitivityModelImpl <em>Sensitivity Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SensitivityModelImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSensitivityModel()
		 * @generated
		 */
		EClass SENSITIVITY_MODEL = eINSTANCE.getSensitivityModel();

		/**
		 * The meta object literal for the '<em><b>Sensitivity Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SENSITIVITY_MODEL__SENSITIVITY_MODEL = eINSTANCE.getSensitivityModel_SensitivityModel();

		/**
		 * The meta object literal for the '<em><b>Sensitivity Solution</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SENSITIVITY_MODEL__SENSITIVITY_SOLUTION = eINSTANCE.getSensitivityModel_SensitivitySolution();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SensitivitySolutionSetImpl <em>Sensitivity Solution Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SensitivitySolutionSetImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSensitivitySolutionSet()
		 * @generated
		 */
		EClass SENSITIVITY_SOLUTION_SET = eINSTANCE.getSensitivitySolutionSet();

		/**
		 * The meta object literal for the '<em><b>Porfolio Pn LResult</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT = eINSTANCE.getSensitivitySolutionSet_PorfolioPnLResult();

		/**
		 * The meta object literal for the '<em><b>Cargo Pn LResults</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SENSITIVITY_SOLUTION_SET__CARGO_PN_LRESULTS = eINSTANCE.getSensitivitySolutionSet_CargoPnLResults();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.AbstractSensitivityResultImpl <em>Abstract Sensitivity Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.AbstractSensitivityResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAbstractSensitivityResult()
		 * @generated
		 */
		EClass ABSTRACT_SENSITIVITY_RESULT = eINSTANCE.getAbstractSensitivityResult();

		/**
		 * The meta object literal for the '<em><b>Min Pn L</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L = eINSTANCE.getAbstractSensitivityResult_MinPnL();

		/**
		 * The meta object literal for the '<em><b>Max Pn L</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L = eINSTANCE.getAbstractSensitivityResult_MaxPnL();

		/**
		 * The meta object literal for the '<em><b>Average Pn L</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L = eINSTANCE.getAbstractSensitivityResult_AveragePnL();

		/**
		 * The meta object literal for the '<em><b>Variance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_SENSITIVITY_RESULT__VARIANCE = eINSTANCE.getAbstractSensitivityResult_Variance();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.PortfolioSensitivityResultImpl <em>Portfolio Sensitivity Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.PortfolioSensitivityResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPortfolioSensitivityResult()
		 * @generated
		 */
		EClass PORTFOLIO_SENSITIVITY_RESULT = eINSTANCE.getPortfolioSensitivityResult();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.CargoPnLResultImpl <em>Cargo Pn LResult</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.CargoPnLResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCargoPnLResult()
		 * @generated
		 */
		EClass CARGO_PN_LRESULT = eINSTANCE.getCargoPnLResult();

		/**
		 * The meta object literal for the '<em><b>Cargo</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_PN_LRESULT__CARGO = eINSTANCE.getCargoPnLResult_Cargo();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl <em>Swap Value Matrix Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixModel()
		 * @generated
		 */
		EClass SWAP_VALUE_MATRIX_MODEL = eINSTANCE.getSwapValueMatrixModel();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_MODEL__PARAMETERS = eINSTANCE.getSwapValueMatrixModel_Parameters();

		/**
		 * The meta object literal for the '<em><b>Swap Value Matrix Result</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT = eINSTANCE.getSwapValueMatrixModel_SwapValueMatrixResult();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl <em>Swap Value Matrix Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixResult()
		 * @generated
		 */
		EClass SWAP_VALUE_MATRIX_RESULT = eINSTANCE.getSwapValueMatrixResult();

		/**
		 * The meta object literal for the '<em><b>Swap Pnl Minus Base Pnl</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL = eINSTANCE.getSwapValueMatrixResult_SwapPnlMinusBasePnl();

		/**
		 * The meta object literal for the '<em><b>Base Preceding Pnl</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL = eINSTANCE.getSwapValueMatrixResult_BasePrecedingPnl();

		/**
		 * The meta object literal for the '<em><b>Base Succeeding Pnl</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL = eINSTANCE.getSwapValueMatrixResult_BaseSucceedingPnl();

		/**
		 * The meta object literal for the '<em><b>Swap Preceding Pnl</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL = eINSTANCE.getSwapValueMatrixResult_SwapPrecedingPnl();

		/**
		 * The meta object literal for the '<em><b>Swap Succeeding Pnl</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL = eINSTANCE.getSwapValueMatrixResult_SwapSucceedingPnl();

		/**
		 * The meta object literal for the '<em><b>Base Cargo</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_RESULT__BASE_CARGO = eINSTANCE.getSwapValueMatrixResult_BaseCargo();

		/**
		 * The meta object literal for the '<em><b>Swap Diversion Cargo</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_RESULT__SWAP_DIVERSION_CARGO = eINSTANCE.getSwapValueMatrixResult_SwapDiversionCargo();

		/**
		 * The meta object literal for the '<em><b>Swap Backfill Cargo</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO = eINSTANCE.getSwapValueMatrixResult_SwapBackfillCargo();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultSetImpl <em>Swap Value Matrix Result Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultSetImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSwapValueMatrixResultSet()
		 * @generated
		 */
		EClass SWAP_VALUE_MATRIX_RESULT_SET = eINSTANCE.getSwapValueMatrixResultSet();

		/**
		 * The meta object literal for the '<em><b>Results</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_RESULT_SET__RESULTS = eINSTANCE.getSwapValueMatrixResultSet_Results();

		/**
		 * The meta object literal for the '<em><b>Swap Fee</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_RESULT_SET__SWAP_FEE = eINSTANCE.getSwapValueMatrixResultSet_SwapFee();

		/**
		 * The meta object literal for the '<em><b>Generated Spot Load Slot</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT = eINSTANCE.getSwapValueMatrixResultSet_GeneratedSpotLoadSlot();

		/**
		 * The meta object literal for the '<em><b>Generated Spot Discharge Slot</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT = eINSTANCE.getSwapValueMatrixResultSet_GeneratedSpotDischargeSlot();

		/**
		 * The meta object literal for the '<em><b>Non Vessel Charter Pnl</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWAP_VALUE_MATRIX_RESULT_SET__NON_VESSEL_CHARTER_PNL = eINSTANCE.getSwapValueMatrixResultSet_NonVesselCharterPnl();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionImpl <em>Solution Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SolutionOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSolutionOption()
		 * @generated
		 */
		EClass SOLUTION_OPTION = eINSTANCE.getSolutionOption();

		/**
		 * The meta object literal for the '<em><b>Change Description</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION_OPTION__CHANGE_DESCRIPTION = eINSTANCE.getSolutionOption_ChangeDescription();

		/**
		 * The meta object literal for the '<em><b>Schedule Specification</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION_OPTION__SCHEDULE_SPECIFICATION = eINSTANCE.getSolutionOption_ScheduleSpecification();

		/**
		 * The meta object literal for the '<em><b>Schedule Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION_OPTION__SCHEDULE_MODEL = eINSTANCE.getSolutionOption_ScheduleModel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.OptimisationResultImpl <em>Optimisation Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.OptimisationResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptimisationResult()
		 * @generated
		 */
		EClass OPTIMISATION_RESULT = eINSTANCE.getOptimisationResult();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.DualModeSolutionOptionImpl <em>Dual Mode Solution Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.DualModeSolutionOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getDualModeSolutionOption()
		 * @generated
		 */
		EClass DUAL_MODE_SOLUTION_OPTION = eINSTANCE.getDualModeSolutionOption();

		/**
		 * The meta object literal for the '<em><b>Micro Base Case</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE = eINSTANCE.getDualModeSolutionOption_MicroBaseCase();

		/**
		 * The meta object literal for the '<em><b>Micro Target Case</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE = eINSTANCE.getDualModeSolutionOption_MicroTargetCase();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionMicroCaseImpl <em>Solution Option Micro Case</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SolutionOptionMicroCaseImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSolutionOptionMicroCase()
		 * @generated
		 */
		EClass SOLUTION_OPTION_MICRO_CASE = eINSTANCE.getSolutionOptionMicroCase();

		/**
		 * The meta object literal for the '<em><b>Schedule Specification</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION = eINSTANCE.getSolutionOptionMicroCase_ScheduleSpecification();

		/**
		 * The meta object literal for the '<em><b>Schedule Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL = eINSTANCE.getSolutionOptionMicroCase_ScheduleModel();

		/**
		 * The meta object literal for the '<em><b>Extra Vessel Charters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_CHARTERS = eINSTANCE.getSolutionOptionMicroCase_ExtraVesselCharters();

		/**
		 * The meta object literal for the '<em><b>Charter In Market Overrides</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION_OPTION_MICRO_CASE__CHARTER_IN_MARKET_OVERRIDES = eINSTANCE.getSolutionOptionMicroCase_CharterInMarketOverrides();

		/**
		 * The meta object literal for the '<em><b>Partial Case</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTION_ANALYSIS_MODEL__PARTIAL_CASE = eINSTANCE.getOptionAnalysisModel_PartialCase();

		/**
		 * The meta object literal for the '<em><b>Results</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTION_ANALYSIS_MODEL__RESULTS = eINSTANCE.getOptionAnalysisModel_Results();

		/**
		 * The meta object literal for the '<em><b>Use Target PNL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTION_ANALYSIS_MODEL__USE_TARGET_PNL = eINSTANCE.getOptionAnalysisModel_UseTargetPNL();

		/**
		 * The meta object literal for the '<em><b>Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTION_ANALYSIS_MODEL__MODE = eINSTANCE.getOptionAnalysisModel_Mode();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SandboxResultImpl <em>Sandbox Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SandboxResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSandboxResult()
		 * @generated
		 */
		EClass SANDBOX_RESULT = eINSTANCE.getSandboxResult();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.VolumeMode <em>Volume Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.VolumeMode
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVolumeMode()
		 * @generated
		 */
		EEnum VOLUME_MODE = eINSTANCE.getVolumeMode();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.SlotType <em>Slot Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.SlotType
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSlotType()
		 * @generated
		 */
		EEnum SLOT_TYPE = eINSTANCE.getSlotType();

	}

} //AnalyticsPackage
