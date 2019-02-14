/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

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
	int BUY_OPPORTUNITY = 3;

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
	 * The feature id for the '<em><b>Cv</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__CV = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__CANCELLATION_EXPRESSION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Misc Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__MISC_COSTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Volume Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__VOLUME_MODE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Volume Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__VOLUME_UNITS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Min Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__MIN_VOLUME = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Max Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY__MAX_VOLUME = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The number of structural features of the '<em>Buy Opportunity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_OPPORTUNITY_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SellOpportunityImpl <em>Sell Opportunity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SellOpportunityImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSellOpportunity()
	 * @generated
	 */
	int SELL_OPPORTUNITY = 4;

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
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__CANCELLATION_EXPRESSION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Misc Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__MISC_COSTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Volume Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__VOLUME_MODE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Volume Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__VOLUME_UNITS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Min Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__MIN_VOLUME = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Max Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY__MAX_VOLUME = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The number of structural features of the '<em>Sell Opportunity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_OPPORTUNITY_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BuyMarketImpl <em>Buy Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BuyMarketImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBuyMarket()
	 * @generated
	 */
	int BUY_MARKET = 5;

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
	int SELL_MARKET = 6;

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
	int BUY_REFERENCE = 7;

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
	int SELL_REFERENCE = 8;

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
	int BASE_CASE_ROW = 9;

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
	 * The feature id for the '<em><b>Shipping</b></em>' reference.
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
	int PARTIAL_CASE_ROW = 10;

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
	 * The feature id for the '<em><b>Shipping</b></em>' reference list.
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
	int SHIPPING_OPTION = 11;

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
	int FLEET_SHIPPING_OPTION = 12;

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
	 * The feature id for the '<em><b>Use Safety Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_SHIPPING_OPTION__USE_SAFETY_HEEL = SHIPPING_OPTION_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Fleet Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_SHIPPING_OPTION_FEATURE_COUNT = SHIPPING_OPTION_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OptionalAvailabilityShippingOptionImpl <em>Optional Availability Shipping Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OptionalAvailabilityShippingOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptionalAvailabilityShippingOption()
	 * @generated
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION = 13;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION__VESSEL = FLEET_SHIPPING_OPTION__VESSEL;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION__HIRE_COST = FLEET_SHIPPING_OPTION__HIRE_COST;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION__ENTITY = FLEET_SHIPPING_OPTION__ENTITY;

	/**
	 * The feature id for the '<em><b>Use Safety Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION__USE_SAFETY_HEEL = FLEET_SHIPPING_OPTION__USE_SAFETY_HEEL;

	/**
	 * The feature id for the '<em><b>Ballast Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION__BALLAST_BONUS = FLEET_SHIPPING_OPTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION__REPOSITIONING_FEE = FLEET_SHIPPING_OPTION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START = FLEET_SHIPPING_OPTION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END = FLEET_SHIPPING_OPTION_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Start Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START_PORT = FLEET_SHIPPING_OPTION_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>End Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END_PORT = FLEET_SHIPPING_OPTION_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Optional Availability Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIONAL_AVAILABILITY_SHIPPING_OPTION_FEATURE_COUNT = FLEET_SHIPPING_OPTION_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.RoundTripShippingOptionImpl <em>Round Trip Shipping Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.RoundTripShippingOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getRoundTripShippingOption()
	 * @generated
	 */
	int ROUND_TRIP_SHIPPING_OPTION = 14;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUND_TRIP_SHIPPING_OPTION__VESSEL = SHIPPING_OPTION_FEATURE_COUNT + 0;

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
	int NOMINATED_SHIPPING_OPTION = 15;

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
	int ANALYSIS_RESULT_ROW = 16;

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
	 * The feature id for the '<em><b>Shipping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_RESULT_ROW__SHIPPING = 3;

	/**
	 * The feature id for the '<em><b>Result Details</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_RESULT_ROW__RESULT_DETAILS = 4;

	/**
	 * The number of structural features of the '<em>Analysis Result Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_RESULT_ROW_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ResultContainerImpl <em>Result Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ResultContainerImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getResultContainer()
	 * @generated
	 */
	int RESULT_CONTAINER = 17;

	/**
	 * The feature id for the '<em><b>Cargo Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_CONTAINER__CARGO_ALLOCATION = 0;

	/**
	 * The feature id for the '<em><b>Open Slot Allocations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS = 1;

	/**
	 * The feature id for the '<em><b>Slot Allocations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_CONTAINER__SLOT_ALLOCATIONS = 2;

	/**
	 * The number of structural features of the '<em>Result Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_CONTAINER_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.AnalysisResultDetailImpl <em>Analysis Result Detail</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalysisResultDetailImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAnalysisResultDetail()
	 * @generated
	 */
	int ANALYSIS_RESULT_DETAIL = 18;

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
	int PROFIT_AND_LOSS_RESULT = 19;

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
	int BREAK_EVEN_RESULT = 20;

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
	int ABSTRACT_ANALYSIS_MODEL = 21;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Buys</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__BUYS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sells</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__SELLS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Abstract Analysis Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl <em>Option Analysis Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptionAnalysisModel()
	 * @generated
	 */
	int OPTION_ANALYSIS_MODEL = 22;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__EXTENSIONS = ABSTRACT_ANALYSIS_MODEL__EXTENSIONS;

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
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES = ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES;

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
	 * The feature id for the '<em><b>Base Case Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Results</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__RESULTS = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Use Target PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__USE_TARGET_PNL = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__CHILDREN = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Option Analysis Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL_FEATURE_COUNT = ABSTRACT_ANALYSIS_MODEL_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ResultSetImpl <em>Result Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ResultSetImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getResultSet()
	 * @generated
	 */
	int RESULT_SET = 23;

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
	 * The feature id for the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET__SCHEDULE_MODEL = 2;

	/**
	 * The number of structural features of the '<em>Result Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ResultImpl <em>Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getResult()
	 * @generated
	 */
	int RESULT = 24;

	/**
	 * The feature id for the '<em><b>Extra Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT__EXTRA_SLOTS = 0;

	/**
	 * The feature id for the '<em><b>Result Sets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT__RESULT_SETS = 1;

	/**
	 * The feature id for the '<em><b>Extra Vessel Availabilities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT__EXTRA_VESSEL_AVAILABILITIES = 2;

	/**
	 * The feature id for the '<em><b>Charter In Market Overrides</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT__CHARTER_IN_MARKET_OVERRIDES = 3;

	/**
	 * The feature id for the '<em><b>Extra Charter In Markets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT__EXTRA_CHARTER_IN_MARKETS = 4;

	/**
	 * The number of structural features of the '<em>Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl <em>Base Case</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getBaseCase()
	 * @generated
	 */
	int BASE_CASE = 25;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseImpl <em>Partial Case</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.PartialCaseImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPartialCase()
	 * @generated
	 */
	int PARTIAL_CASE = 26;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.NewVesselAvailabilityImpl <em>New Vessel Availability</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.NewVesselAvailabilityImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getNewVesselAvailability()
	 * @generated
	 */
	int NEW_VESSEL_AVAILABILITY = 27;

	/**
	 * The feature id for the '<em><b>Vessel Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NEW_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY = SHIPPING_OPTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>New Vessel Availability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NEW_VESSEL_AVAILABILITY_FEATURE_COUNT = SHIPPING_OPTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ExistingVesselAvailabilityImpl <em>Existing Vessel Availability</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ExistingVesselAvailabilityImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getExistingVesselAvailability()
	 * @generated
	 */
	int EXISTING_VESSEL_AVAILABILITY = 28;

	/**
	 * The feature id for the '<em><b>Vessel Availability</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY = SHIPPING_OPTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Existing Vessel Availability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_VESSEL_AVAILABILITY_FEATURE_COUNT = SHIPPING_OPTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ExistingCharterMarketOptionImpl <em>Existing Charter Market Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ExistingCharterMarketOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getExistingCharterMarketOption()
	 * @generated
	 */
	int EXISTING_CHARTER_MARKET_OPTION = 29;

	/**
	 * The feature id for the '<em><b>Charter In Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET = SHIPPING_OPTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX = SHIPPING_OPTION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Existing Charter Market Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXISTING_CHARTER_MARKET_OPTION_FEATURE_COUNT = SHIPPING_OPTION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl <em>Abstract Solution Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getAbstractSolutionSet()
	 * @generated
	 */
	int ABSTRACT_SOLUTION_SET = 30;

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
	 * The number of structural features of the '<em>Abstract Solution Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ActionableSetPlanImpl <em>Actionable Set Plan</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ActionableSetPlanImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getActionableSetPlan()
	 * @generated
	 */
	int ACTIONABLE_SET_PLAN = 31;

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
	 * The number of structural features of the '<em>Actionable Set Plan</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN_FEATURE_COUNT = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionsImpl <em>Slot Insertion Options</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionsImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSlotInsertionOptions()
	 * @generated
	 */
	int SLOT_INSERTION_OPTIONS = 32;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionImpl <em>Solution Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SolutionOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSolutionOption()
	 * @generated
	 */
	int SOLUTION_OPTION = 33;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ChangeDescriptionImpl <em>Change Description</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ChangeDescriptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getChangeDescription()
	 * @generated
	 */
	int CHANGE_DESCRIPTION = 37;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ChangeImpl <em>Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ChangeImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getChange()
	 * @generated
	 */
	int CHANGE = 38;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OpenSlotChangeImpl <em>Open Slot Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OpenSlotChangeImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOpenSlotChange()
	 * @generated
	 */
	int OPEN_SLOT_CHANGE = 39;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.CargoChangeImpl <em>Cargo Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.CargoChangeImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getCargoChange()
	 * @generated
	 */
	int CARGO_CHANGE = 40;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselEventChangeImpl <em>Vessel Event Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.VesselEventChangeImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselEventChange()
	 * @generated
	 */
	int VESSEL_EVENT_CHANGE = 41;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselEventDescriptorImpl <em>Vessel Event Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.VesselEventDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselEventDescriptor()
	 * @generated
	 */
	int VESSEL_EVENT_DESCRIPTOR = 42;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SlotDescriptorImpl <em>Slot Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SlotDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSlotDescriptor()
	 * @generated
	 */
	int SLOT_DESCRIPTOR = 43;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.RealSlotDescriptorImpl <em>Real Slot Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.RealSlotDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getRealSlotDescriptor()
	 * @generated
	 */
	int REAL_SLOT_DESCRIPTOR = 44;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SpotMarketSlotDescriptorImpl <em>Spot Market Slot Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SpotMarketSlotDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSpotMarketSlotDescriptor()
	 * @generated
	 */
	int SPOT_MARKET_SLOT_DESCRIPTOR = 45;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.VesselAllocationDescriptorImpl <em>Vessel Allocation Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.VesselAllocationDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVesselAllocationDescriptor()
	 * @generated
	 */
	int VESSEL_ALLOCATION_DESCRIPTOR = 46;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MarketVesselAllocationDescriptorImpl <em>Market Vessel Allocation Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MarketVesselAllocationDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMarketVesselAllocationDescriptor()
	 * @generated
	 */
	int MARKET_VESSEL_ALLOCATION_DESCRIPTOR = 47;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.FleetVesselAllocationDescriptorImpl <em>Fleet Vessel Allocation Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.FleetVesselAllocationDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getFleetVesselAllocationDescriptor()
	 * @generated
	 */
	int FLEET_VESSEL_ALLOCATION_DESCRIPTOR = 48;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.PositionDescriptorImpl <em>Position Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.PositionDescriptorImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getPositionDescriptor()
	 * @generated
	 */
	int POSITION_DESCRIPTOR = 49;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OptimisationResultImpl <em>Optimisation Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OptimisationResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptimisationResult()
	 * @generated
	 */
	int OPTIMISATION_RESULT = 34;

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
	 * The number of structural features of the '<em>Optimisation Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT_FEATURE_COUNT = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.DualModeSolutionOptionImpl <em>Dual Mode Solution Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.DualModeSolutionOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getDualModeSolutionOption()
	 * @generated
	 */
	int DUAL_MODE_SOLUTION_OPTION = 35;

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
	int SOLUTION_OPTION_MICRO_CASE = 36;

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
	 * The feature id for the '<em><b>Extra Vessel Availabilities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_AVAILABILITIES = 2;

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
	int VIABILITY_MODEL = 50;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__EXTENSIONS = ABSTRACT_ANALYSIS_MODEL__EXTENSIONS;

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
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIABILITY_MODEL__SHIPPING_TEMPLATES = ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl <em>Viability Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getViabilityRow()
	 * @generated
	 */
	int VIABILITY_ROW = 51;

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
	int VIABILITY_RESULT = 52;

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
	int MTM_MODEL = 53;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__EXTENSIONS = ABSTRACT_ANALYSIS_MODEL__EXTENSIONS;

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
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_MODEL__SHIPPING_TEMPLATES = ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES;

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
	int MTM_RESULT = 54;

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
	 * The number of structural features of the '<em>MTM Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MTM_RESULT_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.MTMRowImpl <em>MTM Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.MTMRowImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getMTMRow()
	 * @generated
	 */
	int MTM_ROW = 55;

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
	int BREAK_EVEN_ANALYSIS_MODEL = 56;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__EXTENSIONS = ABSTRACT_ANALYSIS_MODEL__EXTENSIONS;

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
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EVEN_ANALYSIS_MODEL__SHIPPING_TEMPLATES = ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES;

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
	int BREAK_EVEN_ANALYSIS_ROW = 57;

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
	int BREAK_EVEN_ANALYSIS_RESULT_SET = 58;

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
	int BREAK_EVEN_ANALYSIS_RESULT = 59;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.VolumeMode <em>Volume Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.VolumeMode
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVolumeMode()
	 * @generated
	 */
	int VOLUME_MODE = 60;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.SlotType <em>Slot Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.SlotType
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSlotType()
	 * @generated
	 */
	int SLOT_TYPE = 61;


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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.FleetShippingOption#isUseSafetyHeel <em>Use Safety Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Safety Heel</em>'.
	 * @see com.mmxlabs.models.lng.analytics.FleetShippingOption#isUseSafetyHeel()
	 * @see #getFleetShippingOption()
	 * @generated
	 */
	EAttribute getFleetShippingOption_UseSafetyHeel();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption <em>Optional Availability Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Optional Availability Shipping Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption
	 * @generated
	 */
	EClass getOptionalAvailabilityShippingOption();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getBallastBonus <em>Ballast Bonus</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Bonus</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getBallastBonus()
	 * @see #getOptionalAvailabilityShippingOption()
	 * @generated
	 */
	EAttribute getOptionalAvailabilityShippingOption_BallastBonus();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getRepositioningFee <em>Repositioning Fee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Repositioning Fee</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getRepositioningFee()
	 * @see #getOptionalAvailabilityShippingOption()
	 * @generated
	 */
	EAttribute getOptionalAvailabilityShippingOption_RepositioningFee();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getStart()
	 * @see #getOptionalAvailabilityShippingOption()
	 * @generated
	 */
	EAttribute getOptionalAvailabilityShippingOption_Start();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getEnd <em>End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getEnd()
	 * @see #getOptionalAvailabilityShippingOption()
	 * @generated
	 */
	EAttribute getOptionalAvailabilityShippingOption_End();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getStartPort <em>Start Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Start Port</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getStartPort()
	 * @see #getOptionalAvailabilityShippingOption()
	 * @generated
	 */
	EReference getOptionalAvailabilityShippingOption_StartPort();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getEndPort <em>End Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>End Port</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getEndPort()
	 * @see #getOptionalAvailabilityShippingOption()
	 * @generated
	 */
	EReference getOptionalAvailabilityShippingOption_EndPort();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getShipping <em>Shipping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Shipping</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalysisResultRow#getShipping()
	 * @see #getAnalysisResultRow()
	 * @generated
	 */
	EReference getAnalysisResultRow_Shipping();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow#getResultDetails <em>Result Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Result Details</em>'.
	 * @see com.mmxlabs.models.lng.analytics.AnalysisResultRow#getResultDetails()
	 * @see #getAnalysisResultRow()
	 * @generated
	 */
	EReference getAnalysisResultRow_ResultDetails();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ResultContainer <em>Result Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Result Container</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultContainer
	 * @generated
	 */
	EClass getResultContainer();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ResultContainer#getCargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo Allocation</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultContainer#getCargoAllocation()
	 * @see #getResultContainer()
	 * @generated
	 */
	EReference getResultContainer_CargoAllocation();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.ResultContainer#getOpenSlotAllocations <em>Open Slot Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Open Slot Allocations</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultContainer#getOpenSlotAllocations()
	 * @see #getResultContainer()
	 * @generated
	 */
	EReference getResultContainer_OpenSlotAllocations();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.analytics.ResultContainer#getSlotAllocations <em>Slot Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Slot Allocations</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultContainer#getSlotAllocations()
	 * @see #getResultContainer()
	 * @generated
	 */
	EReference getResultContainer_SlotAllocations();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.NewVesselAvailability <em>New Vessel Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>New Vessel Availability</em>'.
	 * @see com.mmxlabs.models.lng.analytics.NewVesselAvailability
	 * @generated
	 */
	EClass getNewVesselAvailability();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.NewVesselAvailability#getVesselAvailability <em>Vessel Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Vessel Availability</em>'.
	 * @see com.mmxlabs.models.lng.analytics.NewVesselAvailability#getVesselAvailability()
	 * @see #getNewVesselAvailability()
	 * @generated
	 */
	EReference getNewVesselAvailability_VesselAvailability();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.ExistingVesselAvailability <em>Existing Vessel Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Existing Vessel Availability</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ExistingVesselAvailability
	 * @generated
	 */
	EClass getExistingVesselAvailability();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.analytics.ExistingVesselAvailability#getVesselAvailability <em>Vessel Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Availability</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ExistingVesselAvailability#getVesselAvailability()
	 * @see #getExistingVesselAvailability()
	 * @generated
	 */
	EReference getExistingVesselAvailability_VesselAvailability();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getExtraVesselAvailabilities <em>Extra Vessel Availabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extra Vessel Availabilities</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getExtraVesselAvailabilities()
	 * @see #getSolutionOptionMicroCase()
	 * @generated
	 */
	EReference getSolutionOptionMicroCase_ExtraVesselAvailabilities();

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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBaseCaseResult <em>Base Case Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Base Case Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBaseCaseResult()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EReference getOptionAnalysisModel_BaseCaseResult();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getChildren()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EReference getOptionAnalysisModel_Children();

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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.ResultSet#getScheduleModel <em>Schedule Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Schedule Model</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultSet#getScheduleModel()
	 * @see #getResultSet()
	 * @generated
	 */
	EReference getResultSet_ScheduleModel();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.Result <em>Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Result</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Result
	 * @generated
	 */
	EClass getResult();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.Result#getExtraSlots <em>Extra Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extra Slots</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Result#getExtraSlots()
	 * @see #getResult()
	 * @generated
	 */
	EReference getResult_ExtraSlots();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.Result#getResultSets <em>Result Sets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Result Sets</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Result#getResultSets()
	 * @see #getResult()
	 * @generated
	 */
	EReference getResult_ResultSets();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.Result#getExtraVesselAvailabilities <em>Extra Vessel Availabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extra Vessel Availabilities</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Result#getExtraVesselAvailabilities()
	 * @see #getResult()
	 * @generated
	 */
	EReference getResult_ExtraVesselAvailabilities();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.Result#getCharterInMarketOverrides <em>Charter In Market Overrides</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Charter In Market Overrides</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Result#getCharterInMarketOverrides()
	 * @see #getResult()
	 * @generated
	 */
	EReference getResult_CharterInMarketOverrides();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.Result#getExtraCharterInMarkets <em>Extra Charter In Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extra Charter In Markets</em>'.
	 * @see com.mmxlabs.models.lng.analytics.Result#getExtraCharterInMarkets()
	 * @see #getResult()
	 * @generated
	 */
	EReference getResult_ExtraCharterInMarkets();

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
		 * The meta object literal for the '<em><b>Shipping</b></em>' reference feature.
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
		 * The meta object literal for the '<em><b>Shipping</b></em>' reference list feature.
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
		 * The meta object literal for the '<em><b>Use Safety Heel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLEET_SHIPPING_OPTION__USE_SAFETY_HEEL = eINSTANCE.getFleetShippingOption_UseSafetyHeel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.OptionalAvailabilityShippingOptionImpl <em>Optional Availability Shipping Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.OptionalAvailabilityShippingOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptionalAvailabilityShippingOption()
		 * @generated
		 */
		EClass OPTIONAL_AVAILABILITY_SHIPPING_OPTION = eINSTANCE.getOptionalAvailabilityShippingOption();

		/**
		 * The meta object literal for the '<em><b>Ballast Bonus</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIONAL_AVAILABILITY_SHIPPING_OPTION__BALLAST_BONUS = eINSTANCE.getOptionalAvailabilityShippingOption_BallastBonus();

		/**
		 * The meta object literal for the '<em><b>Repositioning Fee</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIONAL_AVAILABILITY_SHIPPING_OPTION__REPOSITIONING_FEE = eINSTANCE.getOptionalAvailabilityShippingOption_RepositioningFee();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START = eINSTANCE.getOptionalAvailabilityShippingOption_Start();

		/**
		 * The meta object literal for the '<em><b>End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END = eINSTANCE.getOptionalAvailabilityShippingOption_End();

		/**
		 * The meta object literal for the '<em><b>Start Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START_PORT = eINSTANCE.getOptionalAvailabilityShippingOption_StartPort();

		/**
		 * The meta object literal for the '<em><b>End Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END_PORT = eINSTANCE.getOptionalAvailabilityShippingOption_EndPort();

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
		 * The meta object literal for the '<em><b>Shipping</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYSIS_RESULT_ROW__SHIPPING = eINSTANCE.getAnalysisResultRow_Shipping();

		/**
		 * The meta object literal for the '<em><b>Result Details</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYSIS_RESULT_ROW__RESULT_DETAILS = eINSTANCE.getAnalysisResultRow_ResultDetails();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ResultContainerImpl <em>Result Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ResultContainerImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getResultContainer()
		 * @generated
		 */
		EClass RESULT_CONTAINER = eINSTANCE.getResultContainer();

		/**
		 * The meta object literal for the '<em><b>Cargo Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT_CONTAINER__CARGO_ALLOCATION = eINSTANCE.getResultContainer_CargoAllocation();

		/**
		 * The meta object literal for the '<em><b>Open Slot Allocations</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS = eINSTANCE.getResultContainer_OpenSlotAllocations();

		/**
		 * The meta object literal for the '<em><b>Slot Allocations</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT_CONTAINER__SLOT_ALLOCATIONS = eINSTANCE.getResultContainer_SlotAllocations();

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
		 * The meta object literal for the '<em><b>Shipping Templates</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES = eINSTANCE.getAbstractAnalysisModel_ShippingTemplates();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.NewVesselAvailabilityImpl <em>New Vessel Availability</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.NewVesselAvailabilityImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getNewVesselAvailability()
		 * @generated
		 */
		EClass NEW_VESSEL_AVAILABILITY = eINSTANCE.getNewVesselAvailability();

		/**
		 * The meta object literal for the '<em><b>Vessel Availability</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NEW_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY = eINSTANCE.getNewVesselAvailability_VesselAvailability();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ExistingVesselAvailabilityImpl <em>Existing Vessel Availability</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ExistingVesselAvailabilityImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getExistingVesselAvailability()
		 * @generated
		 */
		EClass EXISTING_VESSEL_AVAILABILITY = eINSTANCE.getExistingVesselAvailability();

		/**
		 * The meta object literal for the '<em><b>Vessel Availability</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXISTING_VESSEL_AVAILABILITY__VESSEL_AVAILABILITY = eINSTANCE.getExistingVesselAvailability_VesselAvailability();

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
		 * The meta object literal for the '<em><b>Extra Vessel Availabilities</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_AVAILABILITIES = eINSTANCE.getSolutionOptionMicroCase_ExtraVesselAvailabilities();

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
		 * The meta object literal for the '<em><b>Base Case Result</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT = eINSTANCE.getOptionAnalysisModel_BaseCaseResult();

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
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTION_ANALYSIS_MODEL__CHILDREN = eINSTANCE.getOptionAnalysisModel_Children();

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
		 * The meta object literal for the '<em><b>Schedule Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT_SET__SCHEDULE_MODEL = eINSTANCE.getResultSet_ScheduleModel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.ResultImpl <em>Result</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.ResultImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getResult()
		 * @generated
		 */
		EClass RESULT = eINSTANCE.getResult();

		/**
		 * The meta object literal for the '<em><b>Extra Slots</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT__EXTRA_SLOTS = eINSTANCE.getResult_ExtraSlots();

		/**
		 * The meta object literal for the '<em><b>Result Sets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT__RESULT_SETS = eINSTANCE.getResult_ResultSets();

		/**
		 * The meta object literal for the '<em><b>Extra Vessel Availabilities</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT__EXTRA_VESSEL_AVAILABILITIES = eINSTANCE.getResult_ExtraVesselAvailabilities();

		/**
		 * The meta object literal for the '<em><b>Charter In Market Overrides</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT__CHARTER_IN_MARKET_OVERRIDES = eINSTANCE.getResult_CharterInMarketOverrides();

		/**
		 * The meta object literal for the '<em><b>Extra Charter In Markets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT__EXTRA_CHARTER_IN_MARKETS = eINSTANCE.getResult_ExtraCharterInMarkets();

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
