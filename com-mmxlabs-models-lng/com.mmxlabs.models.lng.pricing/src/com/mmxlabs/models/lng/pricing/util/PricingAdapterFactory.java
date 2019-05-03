/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import com.mmxlabs.models.lng.pricing.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PortsExpressionMap;
import com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
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
	protected PricingSwitch<@Nullable Adapter> modelSwitch =
		new PricingSwitch<@Nullable Adapter>() {
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
			public Adapter caseCostModel(CostModel object) {
				return createCostModelAdapter();
			}
			@Override
			public Adapter caseRouteCost(RouteCost object) {
				return createRouteCostAdapter();
			}
			@Override
			public Adapter caseBaseFuelCost(BaseFuelCost object) {
				return createBaseFuelCostAdapter();
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
			public Adapter casePortsExpressionMap(PortsExpressionMap object) {
				return createPortsExpressionMapAdapter();
			}
			@Override
			public Adapter casePortsSplitExpressionMap(PortsSplitExpressionMap object) {
				return createPortsSplitExpressionMapAdapter();
			}
			@Override
			public Adapter casePanamaCanalTariff(PanamaCanalTariff object) {
				return createPanamaCanalTariffAdapter();
			}
			@Override
			public Adapter casePanamaCanalTariffBand(PanamaCanalTariffBand object) {
				return createPanamaCanalTariffBandAdapter();
			}
			@Override
			public Adapter caseSuezCanalTugBand(SuezCanalTugBand object) {
				return createSuezCanalTugBandAdapter();
			}
			@Override
			public Adapter caseSuezCanalTariff(SuezCanalTariff object) {
				return createSuezCanalTariffAdapter();
			}
			@Override
			public Adapter caseSuezCanalTariffBand(SuezCanalTariffBand object) {
				return createSuezCanalTariffBandAdapter();
			}
			@Override
			public Adapter caseUnitConversion(UnitConversion object) {
				return createUnitConversionAdapter();
			}
			@Override
			public Adapter caseDatePointContainer(DatePointContainer object) {
				return createDatePointContainerAdapter();
			}
			@Override
			public Adapter caseDatePoint(DatePoint object) {
				return createDatePointAdapter();
			}
			@Override
			public Adapter caseYearMonthPointContainer(YearMonthPointContainer object) {
				return createYearMonthPointContainerAdapter();
			}
			@Override
			public Adapter caseYearMonthPoint(YearMonthPoint object) {
				return createYearMonthPointAdapter();
			}
			@Override
			public Adapter caseAbstractYearMonthCurve(AbstractYearMonthCurve object) {
				return createAbstractYearMonthCurveAdapter();
			}
			@Override
			public Adapter caseCommodityCurve(CommodityCurve object) {
				return createCommodityCurveAdapter();
			}
			@Override
			public Adapter caseCharterCurve(CharterCurve object) {
				return createCharterCurveAdapter();
			}
			@Override
			public Adapter caseBunkerFuelCurve(BunkerFuelCurve object) {
				return createBunkerFuelCurveAdapter();
			}
			@Override
			public Adapter caseCurrencyCurve(CurrencyCurve object) {
				return createCurrencyCurveAdapter();
			}
			@Override
			public Adapter caseMarketIndex(MarketIndex object) {
				return createMarketIndexAdapter();
			}
			@Override
			public Adapter caseHolidayCalendarEntry(HolidayCalendarEntry object) {
				return createHolidayCalendarEntryAdapter();
			}
			@Override
			public Adapter caseHolidayCalendar(HolidayCalendar object) {
				return createHolidayCalendarAdapter();
			}
			@Override
			public Adapter caseSettleStrategy(SettleStrategy object) {
				return createSettleStrategyAdapter();
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
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.CostModel <em>Cost Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.CostModel
	 * @generated
	 */
	public Adapter createCostModelAdapter() {
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
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.PortsExpressionMap <em>Ports Expression Map</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.PortsExpressionMap
	 * @generated
	 */
	public Adapter createPortsExpressionMapAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap <em>Ports Split Expression Map</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap
	 * @generated
	 */
	public Adapter createPortsSplitExpressionMapAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff <em>Panama Canal Tariff</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariff
	 * @generated
	 */
	public Adapter createPanamaCanalTariffAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand <em>Panama Canal Tariff Band</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand
	 * @generated
	 */
	public Adapter createPanamaCanalTariffBandAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.SuezCanalTugBand <em>Suez Canal Tug Band</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTugBand
	 * @generated
	 */
	public Adapter createSuezCanalTugBandAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff <em>Suez Canal Tariff</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariff
	 * @generated
	 */
	public Adapter createSuezCanalTariffAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariffBand <em>Suez Canal Tariff Band</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.SuezCanalTariffBand
	 * @generated
	 */
	public Adapter createSuezCanalTariffBandAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.UnitConversion <em>Unit Conversion</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.UnitConversion
	 * @generated
	 */
	public Adapter createUnitConversionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.DatePointContainer <em>Date Point Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.DatePointContainer
	 * @generated
	 */
	public Adapter createDatePointContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.DatePoint <em>Date Point</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.DatePoint
	 * @generated
	 */
	public Adapter createDatePointAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.YearMonthPointContainer <em>Year Month Point Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.YearMonthPointContainer
	 * @generated
	 */
	public Adapter createYearMonthPointContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.YearMonthPoint <em>Year Month Point</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.YearMonthPoint
	 * @generated
	 */
	public Adapter createYearMonthPointAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve <em>Abstract Year Month Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve
	 * @generated
	 */
	public Adapter createAbstractYearMonthCurveAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.CommodityCurve <em>Commodity Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.CommodityCurve
	 * @generated
	 */
	public Adapter createCommodityCurveAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.CharterCurve <em>Charter Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.CharterCurve
	 * @generated
	 */
	public Adapter createCharterCurveAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.BunkerFuelCurve <em>Bunker Fuel Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.BunkerFuelCurve
	 * @generated
	 */
	public Adapter createBunkerFuelCurveAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.CurrencyCurve <em>Currency Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.CurrencyCurve
	 * @generated
	 */
	public Adapter createCurrencyCurveAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.MarketIndex <em>Market Index</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.MarketIndex
	 * @generated
	 */
	public Adapter createMarketIndexAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.HolidayCalendarEntry <em>Holiday Calendar Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.HolidayCalendarEntry
	 * @generated
	 */
	public Adapter createHolidayCalendarEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.HolidayCalendar <em>Holiday Calendar</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.HolidayCalendar
	 * @generated
	 */
	public Adapter createHolidayCalendarAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.pricing.SettleStrategy <em>Settle Strategy</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.pricing.SettleStrategy
	 * @generated
	 */
	public Adapter createSettleStrategyAdapter() {
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
