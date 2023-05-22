/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PortsExpressionMap;
import com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PricingFactoryImpl extends EFactoryImpl implements PricingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static PricingFactory init() {
		try {
			PricingFactory thePricingFactory = (PricingFactory)EPackage.Registry.INSTANCE.getEFactory(PricingPackage.eNS_URI);
			if (thePricingFactory != null) {
				return thePricingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new PricingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingFactoryImpl() {
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
			case PricingPackage.PRICING_MODEL: return createPricingModel();
			case PricingPackage.DATA_INDEX: return createDataIndex();
			case PricingPackage.DERIVED_INDEX: return createDerivedIndex();
			case PricingPackage.INDEX_POINT: return createIndexPoint();
			case PricingPackage.COST_MODEL: return createCostModel();
			case PricingPackage.ROUTE_COST: return createRouteCost();
			case PricingPackage.BASE_FUEL_COST: return createBaseFuelCost();
			case PricingPackage.PORT_COST: return createPortCost();
			case PricingPackage.PORT_COST_ENTRY: return createPortCostEntry();
			case PricingPackage.COOLDOWN_PRICE_ENTRY: return createCooldownPriceEntry();
			case PricingPackage.COOLDOWN_PRICE: return createCooldownPrice();
			case PricingPackage.PORTS_EXPRESSION_MAP: return createPortsExpressionMap();
			case PricingPackage.PORTS_SPLIT_EXPRESSION_MAP: return createPortsSplitExpressionMap();
			case PricingPackage.PANAMA_CANAL_TARIFF: return createPanamaCanalTariff();
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND: return createPanamaCanalTariffBand();
			case PricingPackage.PANAMA_TARIFF_V2: return createPanamaTariffV2();
			case PricingPackage.SUEZ_CANAL_TUG_BAND: return createSuezCanalTugBand();
			case PricingPackage.SUEZ_CANAL_TARIFF: return createSuezCanalTariff();
			case PricingPackage.SUEZ_CANAL_TARIFF_BAND: return createSuezCanalTariffBand();
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE: return createSuezCanalRouteRebate();
			case PricingPackage.UNIT_CONVERSION: return createUnitConversion();
			case PricingPackage.DATE_POINT_CONTAINER: return createDatePointContainer();
			case PricingPackage.DATE_POINT: return createDatePoint();
			case PricingPackage.YEAR_MONTH_POINT_CONTAINER: return createYearMonthPointContainer();
			case PricingPackage.YEAR_MONTH_POINT: return createYearMonthPoint();
			case PricingPackage.COMMODITY_CURVE: return createCommodityCurve();
			case PricingPackage.CHARTER_CURVE: return createCharterCurve();
			case PricingPackage.BUNKER_FUEL_CURVE: return createBunkerFuelCurve();
			case PricingPackage.CURRENCY_CURVE: return createCurrencyCurve();
			case PricingPackage.MARKET_INDEX: return createMarketIndex();
			case PricingPackage.PRICING_CALENDAR_ENTRY: return createPricingCalendarEntry();
			case PricingPackage.PRICING_CALENDAR: return createPricingCalendar();
			case PricingPackage.HOLIDAY_CALENDAR_ENTRY: return createHolidayCalendarEntry();
			case PricingPackage.HOLIDAY_CALENDAR: return createHolidayCalendar();
			case PricingPackage.SETTLE_STRATEGY: return createSettleStrategy();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PricingModel createPricingModel() {
		PricingModelImpl pricingModel = new PricingModelImpl();
		return pricingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <Value> DataIndex<Value> createDataIndex() {
		DataIndexImpl<Value> dataIndex = new DataIndexImpl<Value>();
		return dataIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <Value> DerivedIndex<Value> createDerivedIndex() {
		DerivedIndexImpl<Value> derivedIndex = new DerivedIndexImpl<Value>();
		return derivedIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <Value> IndexPoint<Value> createIndexPoint() {
		IndexPointImpl<Value> indexPoint = new IndexPointImpl<Value>();
		return indexPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CostModel createCostModel() {
		CostModelImpl costModel = new CostModelImpl();
		return costModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RouteCost createRouteCost() {
		RouteCostImpl routeCost = new RouteCostImpl();
		return routeCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseFuelCost createBaseFuelCost() {
		BaseFuelCostImpl baseFuelCost = new BaseFuelCostImpl();
		return baseFuelCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortCost createPortCost() {
		PortCostImpl portCost = new PortCostImpl();
		return portCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortCostEntry createPortCostEntry() {
		PortCostEntryImpl portCostEntry = new PortCostEntryImpl();
		return portCostEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CooldownPriceEntry createCooldownPriceEntry() {
		CooldownPriceEntryImpl cooldownPriceEntry = new CooldownPriceEntryImpl();
		return cooldownPriceEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CooldownPrice createCooldownPrice() {
		CooldownPriceImpl cooldownPrice = new CooldownPriceImpl();
		return cooldownPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortsExpressionMap createPortsExpressionMap() {
		PortsExpressionMapImpl portsExpressionMap = new PortsExpressionMapImpl();
		return portsExpressionMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortsSplitExpressionMap createPortsSplitExpressionMap() {
		PortsSplitExpressionMapImpl portsSplitExpressionMap = new PortsSplitExpressionMapImpl();
		return portsSplitExpressionMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PanamaCanalTariff createPanamaCanalTariff() {
		PanamaCanalTariffImpl panamaCanalTariff = new PanamaCanalTariffImpl();
		return panamaCanalTariff;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PanamaCanalTariffBand createPanamaCanalTariffBand() {
		PanamaCanalTariffBandImpl panamaCanalTariffBand = new PanamaCanalTariffBandImpl();
		return panamaCanalTariffBand;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PanamaTariffV2 createPanamaTariffV2() {
		PanamaTariffV2Impl panamaTariffV2 = new PanamaTariffV2Impl();
		return panamaTariffV2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SuezCanalTugBand createSuezCanalTugBand() {
		SuezCanalTugBandImpl suezCanalTugBand = new SuezCanalTugBandImpl();
		return suezCanalTugBand;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SuezCanalTariff createSuezCanalTariff() {
		SuezCanalTariffImpl suezCanalTariff = new SuezCanalTariffImpl();
		return suezCanalTariff;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SuezCanalTariffBand createSuezCanalTariffBand() {
		SuezCanalTariffBandImpl suezCanalTariffBand = new SuezCanalTariffBandImpl();
		return suezCanalTariffBand;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SuezCanalRouteRebate createSuezCanalRouteRebate() {
		SuezCanalRouteRebateImpl suezCanalRouteRebate = new SuezCanalRouteRebateImpl();
		return suezCanalRouteRebate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public UnitConversion createUnitConversion() {
		UnitConversionImpl unitConversion = new UnitConversionImpl();
		return unitConversion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DatePointContainer createDatePointContainer() {
		DatePointContainerImpl datePointContainer = new DatePointContainerImpl();
		return datePointContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DatePoint createDatePoint() {
		DatePointImpl datePoint = new DatePointImpl();
		return datePoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public YearMonthPointContainer createYearMonthPointContainer() {
		YearMonthPointContainerImpl yearMonthPointContainer = new YearMonthPointContainerImpl();
		return yearMonthPointContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public YearMonthPoint createYearMonthPoint() {
		YearMonthPointImpl yearMonthPoint = new YearMonthPointImpl();
		return yearMonthPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CommodityCurve createCommodityCurve() {
		CommodityCurveImpl commodityCurve = new CommodityCurveImpl();
		return commodityCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterCurve createCharterCurve() {
		CharterCurveImpl charterCurve = new CharterCurveImpl();
		return charterCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BunkerFuelCurve createBunkerFuelCurve() {
		BunkerFuelCurveImpl bunkerFuelCurve = new BunkerFuelCurveImpl();
		return bunkerFuelCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CurrencyCurve createCurrencyCurve() {
		CurrencyCurveImpl currencyCurve = new CurrencyCurveImpl();
		return currencyCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MarketIndex createMarketIndex() {
		MarketIndexImpl marketIndex = new MarketIndexImpl();
		return marketIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PricingCalendarEntry createPricingCalendarEntry() {
		PricingCalendarEntryImpl pricingCalendarEntry = new PricingCalendarEntryImpl();
		return pricingCalendarEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PricingCalendar createPricingCalendar() {
		PricingCalendarImpl pricingCalendar = new PricingCalendarImpl();
		return pricingCalendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public HolidayCalendarEntry createHolidayCalendarEntry() {
		HolidayCalendarEntryImpl holidayCalendarEntry = new HolidayCalendarEntryImpl();
		return holidayCalendarEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public HolidayCalendar createHolidayCalendar() {
		HolidayCalendarImpl holidayCalendar = new HolidayCalendarImpl();
		return holidayCalendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SettleStrategy createSettleStrategy() {
		SettleStrategyImpl settleStrategy = new SettleStrategyImpl();
		return settleStrategy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PricingPackage getPricingPackage() {
		return (PricingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static PricingPackage getPackage() {
		return PricingPackage.eINSTANCE;
	}

} //PricingFactoryImpl
