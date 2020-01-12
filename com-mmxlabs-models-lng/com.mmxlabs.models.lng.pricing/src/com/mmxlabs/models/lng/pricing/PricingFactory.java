/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.pricing.PricingPackage
 * @generated
 */
public interface PricingFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PricingFactory eINSTANCE = com.mmxlabs.models.lng.pricing.impl.PricingFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	PricingModel createPricingModel();

	/**
	 * Returns a new object of class '<em>Data Index</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Index</em>'.
	 * @generated
	 */
	<Value> DataIndex<Value> createDataIndex();

	/**
	 * Returns a new object of class '<em>Derived Index</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Derived Index</em>'.
	 * @generated
	 */
	<Value> DerivedIndex<Value> createDerivedIndex();

	/**
	 * Returns a new object of class '<em>Index Point</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Index Point</em>'.
	 * @generated
	 */
	<Value> IndexPoint<Value> createIndexPoint();

	/**
	 * Returns a new object of class '<em>Cost Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cost Model</em>'.
	 * @generated
	 */
	CostModel createCostModel();

	/**
	 * Returns a new object of class '<em>Route Cost</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Route Cost</em>'.
	 * @generated
	 */
	RouteCost createRouteCost();

	/**
	 * Returns a new object of class '<em>Base Fuel Cost</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Base Fuel Cost</em>'.
	 * @generated
	 */
	BaseFuelCost createBaseFuelCost();

	/**
	 * Returns a new object of class '<em>Port Cost</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Port Cost</em>'.
	 * @generated
	 */
	PortCost createPortCost();

	/**
	 * Returns a new object of class '<em>Port Cost Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Port Cost Entry</em>'.
	 * @generated
	 */
	PortCostEntry createPortCostEntry();

	/**
	 * Returns a new object of class '<em>Cooldown Price</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cooldown Price</em>'.
	 * @generated
	 */
	CooldownPrice createCooldownPrice();

	/**
	 * Returns a new object of class '<em>Ports Expression Map</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ports Expression Map</em>'.
	 * @generated
	 */
	PortsExpressionMap createPortsExpressionMap();

	/**
	 * Returns a new object of class '<em>Ports Split Expression Map</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ports Split Expression Map</em>'.
	 * @generated
	 */
	PortsSplitExpressionMap createPortsSplitExpressionMap();

	/**
	 * Returns a new object of class '<em>Panama Canal Tariff</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Panama Canal Tariff</em>'.
	 * @generated
	 */
	PanamaCanalTariff createPanamaCanalTariff();

	/**
	 * Returns a new object of class '<em>Panama Canal Tariff Band</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Panama Canal Tariff Band</em>'.
	 * @generated
	 */
	PanamaCanalTariffBand createPanamaCanalTariffBand();

	/**
	 * Returns a new object of class '<em>Suez Canal Tug Band</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Suez Canal Tug Band</em>'.
	 * @generated
	 */
	SuezCanalTugBand createSuezCanalTugBand();

	/**
	 * Returns a new object of class '<em>Suez Canal Tariff</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Suez Canal Tariff</em>'.
	 * @generated
	 */
	SuezCanalTariff createSuezCanalTariff();

	/**
	 * Returns a new object of class '<em>Suez Canal Tariff Band</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Suez Canal Tariff Band</em>'.
	 * @generated
	 */
	SuezCanalTariffBand createSuezCanalTariffBand();

	/**
	 * Returns a new object of class '<em>Unit Conversion</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unit Conversion</em>'.
	 * @generated
	 */
	UnitConversion createUnitConversion();

	/**
	 * Returns a new object of class '<em>Date Point Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Date Point Container</em>'.
	 * @generated
	 */
	DatePointContainer createDatePointContainer();

	/**
	 * Returns a new object of class '<em>Date Point</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Date Point</em>'.
	 * @generated
	 */
	DatePoint createDatePoint();

	
	/**
	 * Returns a new object of class '<em>Year Month Point Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Year Month Point Container</em>'.
	 * @generated
	 */
	YearMonthPointContainer createYearMonthPointContainer();

	/**
	 * Returns a new object of class '<em>Year Month Point</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Year Month Point</em>'.
	 * @generated
	 */
	YearMonthPoint createYearMonthPoint();

	/**
	 * Returns a new object of class '<em>Commodity Curve</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Commodity Curve</em>'.
	 * @generated
	 */
	CommodityCurve createCommodityCurve();

	/**
	 * Returns a new object of class '<em>Charter Curve</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Charter Curve</em>'.
	 * @generated
	 */
	CharterCurve createCharterCurve();

	/**
	 * Returns a new object of class '<em>Bunker Fuel Curve</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Bunker Fuel Curve</em>'.
	 * @generated
	 */
	BunkerFuelCurve createBunkerFuelCurve();

	/**
	 * Returns a new object of class '<em>Currency Curve</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Currency Curve</em>'.
	 * @generated
	 */
	CurrencyCurve createCurrencyCurve();

	/**
	 * Returns a new object of class '<em>Market Index</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Market Index</em>'.
	 * @generated
	 */
	MarketIndex createMarketIndex();

	/**
	 * Returns a new object of class '<em>Calendar Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Calendar Entry</em>'.
	 * @generated
	 */
	PricingCalendarEntry createPricingCalendarEntry();

	/**
	 * Returns a new object of class '<em>Calendar</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Calendar</em>'.
	 * @generated
	 */
	PricingCalendar createPricingCalendar();

	/**
	 * Returns a new object of class '<em>Holiday Calendar Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Holiday Calendar Entry</em>'.
	 * @generated
	 */
	HolidayCalendarEntry createHolidayCalendarEntry();

	/**
	 * Returns a new object of class '<em>Holiday Calendar</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Holiday Calendar</em>'.
	 * @generated
	 */
	HolidayCalendar createHolidayCalendar();

	/**
	 * Returns a new object of class '<em>Settle Strategy</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Settle Strategy</em>'.
	 * @generated
	 */
	SettleStrategy createSettleStrategy();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	PricingPackage getPricingPackage();

} //PricingFactory
