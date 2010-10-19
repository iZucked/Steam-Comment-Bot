/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package scenario.market;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
	int MARKET_MODEL = 0;

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
	 * The meta object id for the '{@link scenario.market.impl.MarketImpl <em>Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.market.impl.MarketImpl
	 * @see scenario.market.impl.MarketPackageImpl#getMarket()
	 * @generated
	 */
	int MARKET = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET__NAME = 0;

	/**
	 * The feature id for the '<em><b>Forward Price Curve</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET__FORWARD_PRICE_CURVE = 1;

	/**
	 * The number of structural features of the '<em>Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link scenario.market.impl.ForwardPriceImpl <em>Forward Price</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.market.impl.ForwardPriceImpl
	 * @see scenario.market.impl.MarketPackageImpl#getForwardPrice()
	 * @generated
	 */
	int FORWARD_PRICE = 2;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FORWARD_PRICE__DATE = 0;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FORWARD_PRICE__PRICE = 1;

	/**
	 * The number of structural features of the '<em>Forward Price</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FORWARD_PRICE_FEATURE_COUNT = 2;


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
	 * Returns the meta object for class '{@link scenario.market.Market <em>Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Market</em>'.
	 * @see scenario.market.Market
	 * @generated
	 */
	EClass getMarket();

	/**
	 * Returns the meta object for the attribute '{@link scenario.market.Market#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.market.Market#getName()
	 * @see #getMarket()
	 * @generated
	 */
	EAttribute getMarket_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.market.Market#getForwardPriceCurve <em>Forward Price Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Forward Price Curve</em>'.
	 * @see scenario.market.Market#getForwardPriceCurve()
	 * @see #getMarket()
	 * @generated
	 */
	EReference getMarket_ForwardPriceCurve();

	/**
	 * Returns the meta object for class '{@link scenario.market.ForwardPrice <em>Forward Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Forward Price</em>'.
	 * @see scenario.market.ForwardPrice
	 * @generated
	 */
	EClass getForwardPrice();

	/**
	 * Returns the meta object for the attribute '{@link scenario.market.ForwardPrice#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see scenario.market.ForwardPrice#getDate()
	 * @see #getForwardPrice()
	 * @generated
	 */
	EAttribute getForwardPrice_Date();

	/**
	 * Returns the meta object for the attribute '{@link scenario.market.ForwardPrice#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see scenario.market.ForwardPrice#getPrice()
	 * @see #getForwardPrice()
	 * @generated
	 */
	EAttribute getForwardPrice_Price();

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
		 * The meta object literal for the '{@link scenario.market.impl.MarketImpl <em>Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.market.impl.MarketImpl
		 * @see scenario.market.impl.MarketPackageImpl#getMarket()
		 * @generated
		 */
		EClass MARKET = eINSTANCE.getMarket();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKET__NAME = eINSTANCE.getMarket_Name();

		/**
		 * The meta object literal for the '<em><b>Forward Price Curve</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKET__FORWARD_PRICE_CURVE = eINSTANCE.getMarket_ForwardPriceCurve();

		/**
		 * The meta object literal for the '{@link scenario.market.impl.ForwardPriceImpl <em>Forward Price</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.market.impl.ForwardPriceImpl
		 * @see scenario.market.impl.MarketPackageImpl#getForwardPrice()
		 * @generated
		 */
		EClass FORWARD_PRICE = eINSTANCE.getForwardPrice();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FORWARD_PRICE__DATE = eINSTANCE.getForwardPrice_Date();

		/**
		 * The meta object literal for the '<em><b>Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FORWARD_PRICE__PRICE = eINSTANCE.getForwardPrice_Price();

	}

} //MarketPackage
