/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import com.mmxlabs.models.lng.pricing.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CharterCostModel;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.types.AIndex;
import com.mmxlabs.models.lng.types.ALNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.pricing.PricingPackage
 * @generated
 */
public class PricingAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static PricingPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = PricingPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PricingSwitch<Adapter> modelSwitch =
		new PricingSwitch<Adapter>() {
			@Override
			public Adapter casePricingModel(PricingModel object) {
				return createPricingModelAdapter();
			}
			@Override
			public <Value> Adapter caseDataIndex(DataIndex<Value> object) {
				return createDataIndexAdapter();
			}
			@Override
			public <Value> Adapter caseDerivedIndex(DerivedIndex<Value> object) {
				return createDerivedIndexAdapter();
			}
			@Override
			public <Value> Adapter caseIndexPoint(IndexPoint<Value> object) {
				return createIndexPointAdapter();
			}
			@Override
			public <Value> Adapter caseIndex(Index<Value> object) {
				return createIndexAdapter();
			}
			@Override
			public Adapter caseFleetCostModel(FleetCostModel object) {
				return createFleetCostModelAdapter();
			}
			@Override
			public Adapter caseRouteCost(RouteCost object) {
				return createRouteCostAdapter();
			}
			@Override
			public Adapter caseCharterCostModel(CharterCostModel object) {
				return createCharterCostModelAdapter();
			}
			@Override
			public Adapter caseBaseFuelCost(BaseFuelCost object) {
				return createBaseFuelCostAdapter();
			}
			@Override
			public Adapter caseSpotMarketGroup(SpotMarketGroup object) {
				return createSpotMarketGroupAdapter();
			}
			@Override
			public Adapter caseSpotMarket(SpotMarket object) {
				return createSpotMarketAdapter();
			}
			@Override
			public Adapter casePortCost(PortCost object) {
				return createPortCostAdapter();
			}
			@Override
			public Adapter casePortCostEntry(PortCostEntry object) {
				return createPortCostEntryAdapter();
			}
			@Override
			public Adapter caseCooldownPrice(CooldownPrice object) {
				return createCooldownPriceAdapter();
			}
			@Override
			public Adapter caseDESPurchaseMarket(DESPurchaseMarket object) {
				return createDESPurchaseMarketAdapter();
			}
			@Override
			public Adapter caseDESSalesMarket(DESSalesMarket object) {
				return createDESSalesMarketAdapter();
			}
			@Override
			public Adapter caseFOBPurchasesMarket(FOBPurchasesMarket object) {
				return createFOBPurchasesMarketAdapter();
			}
			@Override
			public Adapter caseFOBSalesMarket(FOBSalesMarket object) {
				return createFOBSalesMarketAdapter();
			}
			@Override
			public Adapter caseSpotAvailability(SpotAvailability object) {
				return createSpotAvailabilityAdapter();
			}
			@Override
			public Adapter caseLNGPriceCalculatorParameters(LNGPriceCalculatorParameters object) {
				return createLNGPriceCalculatorParametersAdapter();
			}
			@Override
			public Adapter caseLNGFixedPriceParameters(LNGFixedPriceParameters object) {
				return createLNGFixedPriceParametersAdapter();
			}
			@Override
			public Adapter caseLNGIndexPriceParameters(LNGIndexPriceParameters object) {
				return createLNGIndexPriceParametersAdapter();
			}
			@Override
			public Adapter caseLNGPriceExpressionParameters(LNGPriceExpressionParameters object) {
				return createLNGPriceExpressionParametersAdapter();
			}
			@Override
			public Adapter caseMMXObject(MMXObject object) {
				return createMMXObjectAdapter();
			}
			@Override
			public Adapter caseUUIDObject(UUIDObject object) {
				return createUUIDObjectAdapter();
			}
			@Override
			public Adapter caseNamedObject(NamedObject object) {
				return createNamedObjectAdapter();
			}
			@Override
			public Adapter caseAIndex(AIndex object) {
				return createAIndexAdapter();
			}
			@Override
			public Adapter caseASpotMarket(ASpotMarket object) {
				return createASpotMarketAdapter();
			}
			@Override
			public Adapter caseALNGPriceCalculatorParameters(ALNGPriceCalculatorParameters object) {
				return createALNGPriceCalculatorParametersAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.PricingModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.PricingModel
	 * @generated
	 */
	public Adapter createPricingModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.DataIndex <em>Data Index</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.DataIndex
	 * @generated
	 */
	public Adapter createDataIndexAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.DerivedIndex <em>Derived Index</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.DerivedIndex
	 * @generated
	 */
	public Adapter createDerivedIndexAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.IndexPoint <em>Index Point</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.IndexPoint
	 * @generated
	 */
	public Adapter createIndexPointAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.Index <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.Index
	 * @generated
	 */
	public Adapter createIndexAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.FleetCostModel <em>Fleet Cost Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.FleetCostModel
	 * @generated
	 */
	public Adapter createFleetCostModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.RouteCost <em>Route Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.RouteCost
	 * @generated
	 */
	public Adapter createRouteCostAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.CharterCostModel <em>Charter Cost Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.CharterCostModel
	 * @generated
	 */
	public Adapter createCharterCostModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.BaseFuelCost <em>Base Fuel Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.BaseFuelCost
	 * @generated
	 */
	public Adapter createBaseFuelCostAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.SpotMarketGroup <em>Spot Market Group</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarketGroup
	 * @generated
	 */
	public Adapter createSpotMarketGroupAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.SpotMarket <em>Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.SpotMarket
	 * @generated
	 */
	public Adapter createSpotMarketAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.PortCost <em>Port Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.PortCost
	 * @generated
	 */
	public Adapter createPortCostAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.PortCostEntry <em>Port Cost Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.PortCostEntry
	 * @generated
	 */
	public Adapter createPortCostEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.CooldownPrice <em>Cooldown Price</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.CooldownPrice
	 * @generated
	 */
	public Adapter createCooldownPriceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.DESPurchaseMarket <em>DES Purchase Market</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.DESPurchaseMarket
	 * @generated
	 */
	public Adapter createDESPurchaseMarketAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.DESSalesMarket <em>DES Sales Market</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.DESSalesMarket
	 * @generated
	 */
	public Adapter createDESSalesMarketAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.FOBPurchasesMarket <em>FOB Purchases Market</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.FOBPurchasesMarket
	 * @generated
	 */
	public Adapter createFOBPurchasesMarketAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.FOBSalesMarket <em>FOB Sales Market</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.FOBSalesMarket
	 * @generated
	 */
	public Adapter createFOBSalesMarketAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.SpotAvailability <em>Spot Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.SpotAvailability
	 * @generated
	 */
	public Adapter createSpotAvailabilityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.LNGPriceCalculatorParameters <em>LNG Price Calculator Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.LNGPriceCalculatorParameters
	 * @generated
	 */
	public Adapter createLNGPriceCalculatorParametersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.LNGFixedPriceParameters <em>LNG Fixed Price Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.LNGFixedPriceParameters
	 * @generated
	 */
	public Adapter createLNGFixedPriceParametersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.LNGIndexPriceParameters <em>LNG Index Price Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.LNGIndexPriceParameters
	 * @generated
	 */
	public Adapter createLNGIndexPriceParametersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.LNGPriceExpressionParameters <em>LNG Price Expression Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.LNGPriceExpressionParameters
	 * @generated
	 */
	public Adapter createLNGPriceExpressionParametersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.MMXObject <em>MMX Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.MMXObject
	 * @generated
	 */
	public Adapter createMMXObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.UUIDObject <em>UUID Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.UUIDObject
	 * @generated
	 */
	public Adapter createUUIDObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.NamedObject
	 * @generated
	 */
	public Adapter createNamedObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.types.AIndex <em>AIndex</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.types.AIndex
	 * @generated
	 */
	public Adapter createAIndexAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.types.ASpotMarket <em>ASpot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.types.ASpotMarket
	 * @generated
	 */
	public Adapter createASpotMarketAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.types.ALNGPriceCalculatorParameters <em>ALNG Price Calculator Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.types.ALNGPriceCalculatorParameters
	 * @generated
	 */
	public Adapter createALNGPriceCalculatorParametersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //PricingAdapterFactory
