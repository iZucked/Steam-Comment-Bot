/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import com.mmxlabs.models.lng.pricing.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
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
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.pricing.PricingPackage
 * @generated
 */
public class PricingSwitch<@Nullable T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static PricingPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingSwitch() {
		if (modelPackage == null) {
			modelPackage = PricingPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case PricingPackage.PRICING_MODEL: {
				PricingModel pricingModel = (PricingModel)theEObject;
				T result = casePricingModel(pricingModel);
				if (result == null) result = caseUUIDObject(pricingModel);
				if (result == null) result = caseMMXObject(pricingModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.DATA_INDEX: {
				DataIndex<?> dataIndex = (DataIndex<?>)theEObject;
				T result = caseDataIndex(dataIndex);
				if (result == null) result = caseIndex(dataIndex);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.DERIVED_INDEX: {
				DerivedIndex<?> derivedIndex = (DerivedIndex<?>)theEObject;
				T result = caseDerivedIndex(derivedIndex);
				if (result == null) result = caseIndex(derivedIndex);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.INDEX_POINT: {
				IndexPoint<?> indexPoint = (IndexPoint<?>)theEObject;
				T result = caseIndexPoint(indexPoint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.INDEX: {
				Index<?> index = (Index<?>)theEObject;
				T result = caseIndex(index);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.COST_MODEL: {
				CostModel costModel = (CostModel)theEObject;
				T result = caseCostModel(costModel);
				if (result == null) result = caseUUIDObject(costModel);
				if (result == null) result = caseMMXObject(costModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.ROUTE_COST: {
				RouteCost routeCost = (RouteCost)theEObject;
				T result = caseRouteCost(routeCost);
				if (result == null) result = caseMMXObject(routeCost);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.BASE_FUEL_COST: {
				BaseFuelCost baseFuelCost = (BaseFuelCost)theEObject;
				T result = caseBaseFuelCost(baseFuelCost);
				if (result == null) result = caseMMXObject(baseFuelCost);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.PORT_COST: {
				PortCost portCost = (PortCost)theEObject;
				T result = casePortCost(portCost);
				if (result == null) result = caseMMXObject(portCost);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.PORT_COST_ENTRY: {
				PortCostEntry portCostEntry = (PortCostEntry)theEObject;
				T result = casePortCostEntry(portCostEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.COOLDOWN_PRICE: {
				CooldownPrice cooldownPrice = (CooldownPrice)theEObject;
				T result = caseCooldownPrice(cooldownPrice);
				if (result == null) result = casePortsExpressionMap(cooldownPrice);
				if (result == null) result = caseMMXObject(cooldownPrice);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.PORTS_EXPRESSION_MAP: {
				PortsExpressionMap portsExpressionMap = (PortsExpressionMap)theEObject;
				T result = casePortsExpressionMap(portsExpressionMap);
				if (result == null) result = caseMMXObject(portsExpressionMap);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.PORTS_SPLIT_EXPRESSION_MAP: {
				PortsSplitExpressionMap portsSplitExpressionMap = (PortsSplitExpressionMap)theEObject;
				T result = casePortsSplitExpressionMap(portsSplitExpressionMap);
				if (result == null) result = caseMMXObject(portsSplitExpressionMap);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.PANAMA_CANAL_TARIFF: {
				PanamaCanalTariff panamaCanalTariff = (PanamaCanalTariff)theEObject;
				T result = casePanamaCanalTariff(panamaCanalTariff);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND: {
				PanamaCanalTariffBand panamaCanalTariffBand = (PanamaCanalTariffBand)theEObject;
				T result = casePanamaCanalTariffBand(panamaCanalTariffBand);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.SUEZ_CANAL_TUG_BAND: {
				SuezCanalTugBand suezCanalTugBand = (SuezCanalTugBand)theEObject;
				T result = caseSuezCanalTugBand(suezCanalTugBand);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.SUEZ_CANAL_TARIFF: {
				SuezCanalTariff suezCanalTariff = (SuezCanalTariff)theEObject;
				T result = caseSuezCanalTariff(suezCanalTariff);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.SUEZ_CANAL_TARIFF_BAND: {
				SuezCanalTariffBand suezCanalTariffBand = (SuezCanalTariffBand)theEObject;
				T result = caseSuezCanalTariffBand(suezCanalTariffBand);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.UNIT_CONVERSION: {
				UnitConversion unitConversion = (UnitConversion)theEObject;
				T result = caseUnitConversion(unitConversion);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.DATE_POINT_CONTAINER: {
				DatePointContainer datePointContainer = (DatePointContainer)theEObject;
				T result = caseDatePointContainer(datePointContainer);
				if (result == null) result = caseUUIDObject(datePointContainer);
				if (result == null) result = caseNamedObject(datePointContainer);
				if (result == null) result = caseMMXObject(datePointContainer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.DATE_POINT: {
				DatePoint datePoint = (DatePoint)theEObject;
				T result = caseDatePoint(datePoint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.YEAR_MONTH_POINT_CONTAINER: {
				YearMonthPointContainer yearMonthPointContainer = (YearMonthPointContainer)theEObject;
				T result = caseYearMonthPointContainer(yearMonthPointContainer);
				if (result == null) result = caseUUIDObject(yearMonthPointContainer);
				if (result == null) result = caseNamedObject(yearMonthPointContainer);
				if (result == null) result = caseMMXObject(yearMonthPointContainer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.YEAR_MONTH_POINT: {
				YearMonthPoint yearMonthPoint = (YearMonthPoint)theEObject;
				T result = caseYearMonthPoint(yearMonthPoint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.ABSTRACT_YEAR_MONTH_CURVE: {
				AbstractYearMonthCurve abstractYearMonthCurve = (AbstractYearMonthCurve)theEObject;
				T result = caseAbstractYearMonthCurve(abstractYearMonthCurve);
				if (result == null) result = caseYearMonthPointContainer(abstractYearMonthCurve);
				if (result == null) result = caseUUIDObject(abstractYearMonthCurve);
				if (result == null) result = caseNamedObject(abstractYearMonthCurve);
				if (result == null) result = caseMMXObject(abstractYearMonthCurve);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.COMMODITY_CURVE: {
				CommodityCurve commodityCurve = (CommodityCurve)theEObject;
				T result = caseCommodityCurve(commodityCurve);
				if (result == null) result = caseAbstractYearMonthCurve(commodityCurve);
				if (result == null) result = caseYearMonthPointContainer(commodityCurve);
				if (result == null) result = caseUUIDObject(commodityCurve);
				if (result == null) result = caseNamedObject(commodityCurve);
				if (result == null) result = caseMMXObject(commodityCurve);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.CHARTER_CURVE: {
				CharterCurve charterCurve = (CharterCurve)theEObject;
				T result = caseCharterCurve(charterCurve);
				if (result == null) result = caseAbstractYearMonthCurve(charterCurve);
				if (result == null) result = caseYearMonthPointContainer(charterCurve);
				if (result == null) result = caseUUIDObject(charterCurve);
				if (result == null) result = caseNamedObject(charterCurve);
				if (result == null) result = caseMMXObject(charterCurve);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.BUNKER_FUEL_CURVE: {
				BunkerFuelCurve bunkerFuelCurve = (BunkerFuelCurve)theEObject;
				T result = caseBunkerFuelCurve(bunkerFuelCurve);
				if (result == null) result = caseAbstractYearMonthCurve(bunkerFuelCurve);
				if (result == null) result = caseYearMonthPointContainer(bunkerFuelCurve);
				if (result == null) result = caseUUIDObject(bunkerFuelCurve);
				if (result == null) result = caseNamedObject(bunkerFuelCurve);
				if (result == null) result = caseMMXObject(bunkerFuelCurve);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.CURRENCY_CURVE: {
				CurrencyCurve currencyCurve = (CurrencyCurve)theEObject;
				T result = caseCurrencyCurve(currencyCurve);
				if (result == null) result = caseAbstractYearMonthCurve(currencyCurve);
				if (result == null) result = caseYearMonthPointContainer(currencyCurve);
				if (result == null) result = caseUUIDObject(currencyCurve);
				if (result == null) result = caseNamedObject(currencyCurve);
				if (result == null) result = caseMMXObject(currencyCurve);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.MARKET_INDEX: {
				MarketIndex marketIndex = (MarketIndex)theEObject;
				T result = caseMarketIndex(marketIndex);
				if (result == null) result = caseNamedObject(marketIndex);
				if (result == null) result = caseMMXObject(marketIndex);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.PRICING_CALENDAR_ENTRY: {
				PricingCalendarEntry pricingCalendarEntry = (PricingCalendarEntry)theEObject;
				T result = casePricingCalendarEntry(pricingCalendarEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.PRICING_CALENDAR: {
				PricingCalendar pricingCalendar = (PricingCalendar)theEObject;
				T result = casePricingCalendar(pricingCalendar);
				if (result == null) result = caseNamedObject(pricingCalendar);
				if (result == null) result = caseMMXObject(pricingCalendar);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.HOLIDAY_CALENDAR_ENTRY: {
				HolidayCalendarEntry holidayCalendarEntry = (HolidayCalendarEntry)theEObject;
				T result = caseHolidayCalendarEntry(holidayCalendarEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.HOLIDAY_CALENDAR: {
				HolidayCalendar holidayCalendar = (HolidayCalendar)theEObject;
				T result = caseHolidayCalendar(holidayCalendar);
				if (result == null) result = caseNamedObject(holidayCalendar);
				if (result == null) result = caseMMXObject(holidayCalendar);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.SETTLE_STRATEGY: {
				SettleStrategy settleStrategy = (SettleStrategy)theEObject;
				T result = caseSettleStrategy(settleStrategy);
				if (result == null) result = caseNamedObject(settleStrategy);
				if (result == null) result = caseMMXObject(settleStrategy);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePricingModel(PricingModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Index</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Index</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <Value> T caseDataIndex(DataIndex<Value> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Derived Index</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Derived Index</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <Value> T caseDerivedIndex(DerivedIndex<Value> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Index Point</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Index Point</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <Value> T caseIndexPoint(IndexPoint<Value> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Index</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Index</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <Value> T caseIndex(Index<Value> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Route Cost</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Route Cost</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRouteCost(RouteCost object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Base Fuel Cost</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Base Fuel Cost</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBaseFuelCost(BaseFuelCost object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Port Cost</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Port Cost</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortCost(PortCost object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Port Cost Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Port Cost Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortCostEntry(PortCostEntry object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cooldown Price</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cooldown Price</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCooldownPrice(CooldownPrice object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cost Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cost Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCostModel(CostModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ports Expression Map</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ports Expression Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortsExpressionMap(PortsExpressionMap object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ports Split Expression Map</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ports Split Expression Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortsSplitExpressionMap(PortsSplitExpressionMap object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Panama Canal Tariff</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Panama Canal Tariff</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePanamaCanalTariff(PanamaCanalTariff object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Panama Canal Tariff Band</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Panama Canal Tariff Band</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePanamaCanalTariffBand(PanamaCanalTariffBand object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Suez Canal Tug Band</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Suez Canal Tug Band</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSuezCanalTugBand(SuezCanalTugBand object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Suez Canal Tariff</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Suez Canal Tariff</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSuezCanalTariff(SuezCanalTariff object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Suez Canal Tariff Band</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Suez Canal Tariff Band</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSuezCanalTariffBand(SuezCanalTariffBand object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Unit Conversion</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Unit Conversion</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUnitConversion(UnitConversion object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Date Point Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Date Point Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDatePointContainer(DatePointContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Date Point</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Date Point</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDatePoint(DatePoint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Year Month Point Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Year Month Point Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseYearMonthPointContainer(YearMonthPointContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Year Month Point</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Year Month Point</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseYearMonthPoint(YearMonthPoint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Year Month Curve</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Year Month Curve</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractYearMonthCurve(AbstractYearMonthCurve object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Commodity Curve</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Commodity Curve</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCommodityCurve(CommodityCurve object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Charter Curve</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Charter Curve</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCharterCurve(CharterCurve object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Bunker Fuel Curve</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Bunker Fuel Curve</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBunkerFuelCurve(BunkerFuelCurve object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Currency Curve</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Currency Curve</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCurrencyCurve(CurrencyCurve object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Market Index</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Market Index</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMarketIndex(MarketIndex object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Calendar Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Calendar Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePricingCalendarEntry(PricingCalendarEntry object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Calendar</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Calendar</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePricingCalendar(PricingCalendar object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Holiday Calendar Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Holiday Calendar Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseHolidayCalendarEntry(HolidayCalendarEntry object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Holiday Calendar</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Holiday Calendar</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseHolidayCalendar(HolidayCalendar object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Settle Strategy</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Settle Strategy</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSettleStrategy(SettleStrategy object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>MMX Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MMX Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMMXObject(MMXObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UUID Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UUID Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUUIDObject(UUIDObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedObject(NamedObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //PricingSwitch
