/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.market;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import scenario.ScenarioPackage;

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
 * @see scenario.market.MarketFactory
 * @model kind="package"
 * @generated
 */
public interface MarketPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "market";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf/market";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.market";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MarketPackage eINSTANCE = scenario.market.impl.MarketPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.market.impl.MarketModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.market.impl.MarketModelImpl
	 * @see scenario.market.impl.MarketPackageImpl#getMarketModel()
	 * @generated
	 */
	int MARKET_MODEL = 1;

	/**
	 * The meta object id for the '{@link scenario.market.impl.StepwisePriceCurveImpl <em>Stepwise Price Curve</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.market.impl.StepwisePriceCurveImpl
	 * @see scenario.market.impl.MarketPackageImpl#getStepwisePriceCurve()
	 * @generated
	 */
	int STEPWISE_PRICE_CURVE = 2;

	/**
	 * The meta object id for the '{@link scenario.market.impl.StepwisePriceImpl <em>Stepwise Price</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.market.impl.StepwisePriceImpl
	 * @see scenario.market.impl.MarketPackageImpl#getStepwisePrice()
	 * @generated
	 */
	int STEPWISE_PRICE = 3;

	/**
	 * The meta object id for the '{@link scenario.market.impl.MarketImpl <em>Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.market.impl.MarketImpl
	 * @see scenario.market.impl.MarketPackageImpl#getMarket()
	 * @generated
	 */
	int MARKET = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET__NAME = ScenarioPackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Price Curve</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET__PRICE_CURVE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_FEATURE_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET___GET_CONTAINER = ScenarioPackage.NAMED_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_OPERATION_COUNT = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Markets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_MODEL__MARKETS = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_MODEL_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_MODEL_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Prices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEPWISE_PRICE_CURVE__PRICES = 0;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEPWISE_PRICE_CURVE__DEFAULT_VALUE = 1;

	/**
	 * The number of structural features of the '<em>Stepwise Price Curve</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEPWISE_PRICE_CURVE_FEATURE_COUNT = 2;

	/**
	 * The operation id for the '<em>Get Value At Date</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEPWISE_PRICE_CURVE___GET_VALUE_AT_DATE__DATE = 0;

	/**
	 * The number of operations of the '<em>Stepwise Price Curve</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEPWISE_PRICE_CURVE_OPERATION_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEPWISE_PRICE__DATE = 0;

	/**
	 * The feature id for the '<em><b>Price From Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEPWISE_PRICE__PRICE_FROM_DATE = 1;

	/**
	 * The number of structural features of the '<em>Stepwise Price</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEPWISE_PRICE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Stepwise Price</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEPWISE_PRICE_OPERATION_COUNT = 0;

	/**
	 * Returns the meta object for class '{@link scenario.market.MarketModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see scenario.market.MarketModel
	 * @generated
	 */
	EClass getMarketModel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.market.MarketModel#getMarkets <em>Markets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Markets</em>'.
	 * @see scenario.market.MarketModel#getMarkets()
	 * @see #getMarketModel()
	 * @generated
	 */
	EReference getMarketModel_Markets();

	/**
	 * Returns the meta object for class '{@link scenario.market.StepwisePriceCurve <em>Stepwise Price Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Stepwise Price Curve</em>'.
	 * @see scenario.market.StepwisePriceCurve
	 * @generated
	 */
	EClass getStepwisePriceCurve();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.market.StepwisePriceCurve#getPrices <em>Prices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Prices</em>'.
	 * @see scenario.market.StepwisePriceCurve#getPrices()
	 * @see #getStepwisePriceCurve()
	 * @generated
	 */
	EReference getStepwisePriceCurve_Prices();

	/**
	 * Returns the meta object for the attribute '{@link scenario.market.StepwisePriceCurve#getDefaultValue <em>Default Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Value</em>'.
	 * @see scenario.market.StepwisePriceCurve#getDefaultValue()
	 * @see #getStepwisePriceCurve()
	 * @generated
	 */
	EAttribute getStepwisePriceCurve_DefaultValue();

	/**
	 * Returns the meta object for the '{@link scenario.market.StepwisePriceCurve#getValueAtDate(java.util.Date) <em>Get Value At Date</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Value At Date</em>' operation.
	 * @see scenario.market.StepwisePriceCurve#getValueAtDate(java.util.Date)
	 * @generated
	 */
	EOperation getStepwisePriceCurve__GetValueAtDate__Date();

	/**
	 * Returns the meta object for class '{@link scenario.market.StepwisePrice <em>Stepwise Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Stepwise Price</em>'.
	 * @see scenario.market.StepwisePrice
	 * @generated
	 */
	EClass getStepwisePrice();

	/**
	 * Returns the meta object for the attribute '{@link scenario.market.StepwisePrice#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see scenario.market.StepwisePrice#getDate()
	 * @see #getStepwisePrice()
	 * @generated
	 */
	EAttribute getStepwisePrice_Date();

	/**
	 * Returns the meta object for the attribute '{@link scenario.market.StepwisePrice#getPriceFromDate <em>Price From Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price From Date</em>'.
	 * @see scenario.market.StepwisePrice#getPriceFromDate()
	 * @see #getStepwisePrice()
	 * @generated
	 */
	EAttribute getStepwisePrice_PriceFromDate();

	/**
	 * Returns the meta object for class '{@link scenario.market.Market <em>Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Market</em>'.
	 * @see scenario.market.Market
	 * @generated
	 */
	EClass getMarket();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.market.Market#getPriceCurve <em>Price Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Price Curve</em>'.
	 * @see scenario.market.Market#getPriceCurve()
	 * @see #getMarket()
	 * @generated
	 */
	EReference getMarket_PriceCurve();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MarketFactory getMarketFactory();

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
		 * The meta object literal for the '{@link scenario.market.impl.MarketModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.market.impl.MarketModelImpl
		 * @see scenario.market.impl.MarketPackageImpl#getMarketModel()
		 * @generated
		 */
		EClass MARKET_MODEL = eINSTANCE.getMarketModel();

		/**
		 * The meta object literal for the '<em><b>Markets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKET_MODEL__MARKETS = eINSTANCE.getMarketModel_Markets();

		/**
		 * The meta object literal for the '{@link scenario.market.impl.StepwisePriceCurveImpl <em>Stepwise Price Curve</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.market.impl.StepwisePriceCurveImpl
		 * @see scenario.market.impl.MarketPackageImpl#getStepwisePriceCurve()
		 * @generated
		 */
		EClass STEPWISE_PRICE_CURVE = eINSTANCE.getStepwisePriceCurve();

		/**
		 * The meta object literal for the '<em><b>Prices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STEPWISE_PRICE_CURVE__PRICES = eINSTANCE.getStepwisePriceCurve_Prices();

		/**
		 * The meta object literal for the '<em><b>Default Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STEPWISE_PRICE_CURVE__DEFAULT_VALUE = eINSTANCE.getStepwisePriceCurve_DefaultValue();

		/**
		 * The meta object literal for the '<em><b>Get Value At Date</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation STEPWISE_PRICE_CURVE___GET_VALUE_AT_DATE__DATE = eINSTANCE.getStepwisePriceCurve__GetValueAtDate__Date();

		/**
		 * The meta object literal for the '{@link scenario.market.impl.StepwisePriceImpl <em>Stepwise Price</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.market.impl.StepwisePriceImpl
		 * @see scenario.market.impl.MarketPackageImpl#getStepwisePrice()
		 * @generated
		 */
		EClass STEPWISE_PRICE = eINSTANCE.getStepwisePrice();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STEPWISE_PRICE__DATE = eINSTANCE.getStepwisePrice_Date();

		/**
		 * The meta object literal for the '<em><b>Price From Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STEPWISE_PRICE__PRICE_FROM_DATE = eINSTANCE.getStepwisePrice_PriceFromDate();

		/**
		 * The meta object literal for the '{@link scenario.market.impl.MarketImpl <em>Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.market.impl.MarketImpl
		 * @see scenario.market.impl.MarketPackageImpl#getMarket()
		 * @generated
		 */
		EClass MARKET = eINSTANCE.getMarket();

		/**
		 * The meta object literal for the '<em><b>Price Curve</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKET__PRICE_CURVE = eINSTANCE.getMarket_PriceCurve();

	}

} //MarketPackage
