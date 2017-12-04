/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYTICS_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

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
	 * The feature id for the '<em><b>Cargo Allocation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_CONTAINER__CARGO_ALLOCATION = 0;

	/**
	 * The feature id for the '<em><b>Open Slot Allocations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS = 1;

	/**
	 * The feature id for the '<em><b>Slot Allocations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_CONTAINER__SLOT_ALLOCATIONS = 2;

	/**
	 * The feature id for the '<em><b>Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_CONTAINER__EVENTS = 3;

	/**
	 * The number of structural features of the '<em>Result Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_CONTAINER_FEATURE_COUNT = 4;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl <em>Option Analysis Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptionAnalysisModel()
	 * @generated
	 */
	int OPTION_ANALYSIS_MODEL = 21;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Buys</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__BUYS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sells</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__SELLS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Base Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__BASE_CASE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Shipping Templates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Partial Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__PARTIAL_CASE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Result Sets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__RESULT_SETS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Use Target PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__USE_TARGET_PNL = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL__CHILDREN = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Option Analysis Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTION_ANALYSIS_MODEL_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ResultSetImpl <em>Result Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ResultSetImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getResultSet()
	 * @generated
	 */
	int RESULT_SET = 22;

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
	int BASE_CASE = 23;

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
	int PARTIAL_CASE = 24;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ExistingVesselAvailabilityImpl <em>Existing Vessel Availability</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ExistingVesselAvailabilityImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getExistingVesselAvailability()
	 * @generated
	 */
	int EXISTING_VESSEL_AVAILABILITY = 25;

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
	int EXISTING_CHARTER_MARKET_OPTION = 26;

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
	int ABSTRACT_SOLUTION_SET = 27;

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
	 * The feature id for the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__USER_SETTINGS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__OPTIONS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Extra Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET__EXTRA_SLOTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Abstract Solution Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SOLUTION_SET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.ActionableSetPlanImpl <em>Actionable Set Plan</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.ActionableSetPlanImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getActionableSetPlan()
	 * @generated
	 */
	int ACTIONABLE_SET_PLAN = 28;

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
	 * The feature id for the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__USER_SETTINGS = ABSTRACT_SOLUTION_SET__USER_SETTINGS;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__OPTIONS = ABSTRACT_SOLUTION_SET__OPTIONS;

	/**
	 * The feature id for the '<em><b>Extra Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIONABLE_SET_PLAN__EXTRA_SLOTS = ABSTRACT_SOLUTION_SET__EXTRA_SLOTS;

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
	int SLOT_INSERTION_OPTIONS = 29;

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
	 * The feature id for the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__USER_SETTINGS = ABSTRACT_SOLUTION_SET__USER_SETTINGS;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__OPTIONS = ABSTRACT_SOLUTION_SET__OPTIONS;

	/**
	 * The feature id for the '<em><b>Extra Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTIONS__EXTRA_SLOTS = ABSTRACT_SOLUTION_SET__EXTRA_SLOTS;

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
	int SOLUTION_OPTION = 31;

	/**
	 * The feature id for the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION__SCHEDULE_MODEL = 0;

	/**
	 * The number of structural features of the '<em>Solution Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPTION_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionImpl <em>Slot Insertion Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSlotInsertionOption()
	 * @generated
	 */
	int SLOT_INSERTION_OPTION = 30;

	/**
	 * The feature id for the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTION__SCHEDULE_MODEL = SOLUTION_OPTION__SCHEDULE_MODEL;

	/**
	 * The number of structural features of the '<em>Slot Insertion Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_INSERTION_OPTION_FEATURE_COUNT = SOLUTION_OPTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.impl.OptimisationResultImpl <em>Optimisation Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.impl.OptimisationResultImpl
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getOptimisationResult()
	 * @generated
	 */
	int OPTIMISATION_RESULT = 32;

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
	 * The feature id for the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__USER_SETTINGS = ABSTRACT_SOLUTION_SET__USER_SETTINGS;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__OPTIONS = ABSTRACT_SOLUTION_SET__OPTIONS;

	/**
	 * The feature id for the '<em><b>Extra Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT__EXTRA_SLOTS = ABSTRACT_SOLUTION_SET__EXTRA_SLOTS;

	/**
	 * The number of structural features of the '<em>Optimisation Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPTIMISATION_RESULT_FEATURE_COUNT = ABSTRACT_SOLUTION_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.analytics.VolumeMode <em>Volume Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.VolumeMode
	 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVolumeMode()
	 * @generated
	 */
	int VOLUME_MODE = 33;


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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.analytics.ResultContainer#getCargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Cargo Allocation</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultContainer#getCargoAllocation()
	 * @see #getResultContainer()
	 * @generated
	 */
	EReference getResultContainer_CargoAllocation();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.ResultContainer#getOpenSlotAllocations <em>Open Slot Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Open Slot Allocations</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultContainer#getOpenSlotAllocations()
	 * @see #getResultContainer()
	 * @generated
	 */
	EReference getResultContainer_OpenSlotAllocations();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.ResultContainer#getSlotAllocations <em>Slot Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Slot Allocations</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultContainer#getSlotAllocations()
	 * @see #getResultContainer()
	 * @generated
	 */
	EReference getResultContainer_SlotAllocations();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.ResultContainer#getEvents <em>Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Events</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ResultContainer#getEvents()
	 * @see #getResultContainer()
	 * @generated
	 */
	EReference getResultContainer_Events();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getShippingTemplates <em>Shipping Templates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Shipping Templates</em>'.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getShippingTemplates()
	 * @see #getOptionAnalysisModel()
	 * @generated
	 */
	EReference getOptionAnalysisModel_ShippingTemplates();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.analytics.SlotInsertionOption <em>Slot Insertion Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Insertion Option</em>'.
	 * @see com.mmxlabs.models.lng.analytics.SlotInsertionOption
	 * @generated
	 */
	EClass getSlotInsertionOption();

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
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.analytics.VolumeMode <em>Volume Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Volume Mode</em>'.
	 * @see com.mmxlabs.models.lng.analytics.VolumeMode
	 * @generated
	 */
	EEnum getVolumeMode();

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
		 * The meta object literal for the '<em><b>Cargo Allocation</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT_CONTAINER__CARGO_ALLOCATION = eINSTANCE.getResultContainer_CargoAllocation();

		/**
		 * The meta object literal for the '<em><b>Open Slot Allocations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS = eINSTANCE.getResultContainer_OpenSlotAllocations();

		/**
		 * The meta object literal for the '<em><b>Slot Allocations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT_CONTAINER__SLOT_ALLOCATIONS = eINSTANCE.getResultContainer_SlotAllocations();

		/**
		 * The meta object literal for the '<em><b>Events</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT_CONTAINER__EVENTS = eINSTANCE.getResultContainer_Events();

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
		 * The meta object literal for the '<em><b>Shipping Templates</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES = eINSTANCE.getOptionAnalysisModel_ShippingTemplates();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionImpl <em>Slot Insertion Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionImpl
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getSlotInsertionOption()
		 * @generated
		 */
		EClass SLOT_INSERTION_OPTION = eINSTANCE.getSlotInsertionOption();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.analytics.VolumeMode <em>Volume Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.VolumeMode
		 * @see com.mmxlabs.models.lng.analytics.impl.AnalyticsPackageImpl#getVolumeMode()
		 * @generated
		 */
		EEnum VOLUME_MODE = eINSTANCE.getVolumeMode();

	}

} //AnalyticsPackage
