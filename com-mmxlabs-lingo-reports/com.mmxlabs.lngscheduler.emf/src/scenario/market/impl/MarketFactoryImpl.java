/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.market.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.market.Index;
import scenario.market.MarketFactory;
import scenario.market.MarketModel;
import scenario.market.MarketPackage;
import scenario.market.StepwisePrice;
import scenario.market.StepwisePriceCurve;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MarketFactoryImpl extends EFactoryImpl implements MarketFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MarketFactory init() {
		try {
			MarketFactory theMarketFactory = (MarketFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf2/market"); 
			if (theMarketFactory != null) {
				return theMarketFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MarketFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MarketFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case MarketPackage.INDEX: return createIndex();
			case MarketPackage.MARKET_MODEL: return createMarketModel();
			case MarketPackage.STEPWISE_PRICE_CURVE: return createStepwisePriceCurve();
			case MarketPackage.STEPWISE_PRICE: return createStepwisePrice();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index createIndex() {
		IndexImpl index = new IndexImpl();
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MarketModel createMarketModel() {
		MarketModelImpl marketModel = new MarketModelImpl();
		return marketModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StepwisePriceCurve createStepwisePriceCurve() {
		StepwisePriceCurveImpl stepwisePriceCurve = new StepwisePriceCurveImpl();
		return stepwisePriceCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StepwisePrice createStepwisePrice() {
		StepwisePriceImpl stepwisePrice = new StepwisePriceImpl();
		return stepwisePrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MarketPackage getMarketPackage() {
		return (MarketPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MarketPackage getPackage() {
		return MarketPackage.eINSTANCE;
	}

} //MarketFactoryImpl
