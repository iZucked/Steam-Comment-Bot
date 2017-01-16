/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import com.mmxlabs.models.lng.pricing.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PortsExpressionMap;
import com.mmxlabs.models.lng.pricing.PortsPriceMap;
import com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap;
import com.mmxlabs.models.lng.pricing.PortsSplitPriceMap;
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
			case PricingPackage.CURRENCY_INDEX: {
				CurrencyIndex currencyIndex = (CurrencyIndex)theEObject;
				T result = caseCurrencyIndex(currencyIndex);
				if (result == null) result = caseNamedIndexContainer(currencyIndex);
				if (result == null) result = caseUUIDObject(currencyIndex);
				if (result == null) result = caseNamedObject(currencyIndex);
				if (result == null) result = caseMMXObject(currencyIndex);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.COMMODITY_INDEX: {
				CommodityIndex commodityIndex = (CommodityIndex)theEObject;
				T result = caseCommodityIndex(commodityIndex);
				if (result == null) result = caseNamedIndexContainer(commodityIndex);
				if (result == null) result = caseUUIDObject(commodityIndex);
				if (result == null) result = caseNamedObject(commodityIndex);
				if (result == null) result = caseMMXObject(commodityIndex);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.CHARTER_INDEX: {
				CharterIndex charterIndex = (CharterIndex)theEObject;
				T result = caseCharterIndex(charterIndex);
				if (result == null) result = caseNamedIndexContainer(charterIndex);
				if (result == null) result = caseUUIDObject(charterIndex);
				if (result == null) result = caseNamedObject(charterIndex);
				if (result == null) result = caseMMXObject(charterIndex);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.BASE_FUEL_INDEX: {
				BaseFuelIndex baseFuelIndex = (BaseFuelIndex)theEObject;
				T result = caseBaseFuelIndex(baseFuelIndex);
				if (result == null) result = caseNamedIndexContainer(baseFuelIndex);
				if (result == null) result = caseUUIDObject(baseFuelIndex);
				if (result == null) result = caseNamedObject(baseFuelIndex);
				if (result == null) result = caseMMXObject(baseFuelIndex);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.NAMED_INDEX_CONTAINER: {
				NamedIndexContainer<?> namedIndexContainer = (NamedIndexContainer<?>)theEObject;
				T result = caseNamedIndexContainer(namedIndexContainer);
				if (result == null) result = caseUUIDObject(namedIndexContainer);
				if (result == null) result = caseNamedObject(namedIndexContainer);
				if (result == null) result = caseMMXObject(namedIndexContainer);
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
			case PricingPackage.PORTS_PRICE_MAP: {
				PortsPriceMap portsPriceMap = (PortsPriceMap)theEObject;
				T result = casePortsPriceMap(portsPriceMap);
				if (result == null) result = caseMMXObject(portsPriceMap);
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
			case PricingPackage.PORTS_SPLIT_PRICE_MAP: {
				PortsSplitPriceMap portsSplitPriceMap = (PortsSplitPriceMap)theEObject;
				T result = casePortsSplitPriceMap(portsSplitPriceMap);
				if (result == null) result = caseMMXObject(portsSplitPriceMap);
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
	 * Returns the result of interpreting the object as an instance of '<em>Currency Index</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Currency Index</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCurrencyIndex(CurrencyIndex object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Commodity Index</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Commodity Index</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCommodityIndex(CommodityIndex object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Charter Index</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Charter Index</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCharterIndex(CharterIndex object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Base Fuel Index</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Base Fuel Index</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBaseFuelIndex(BaseFuelIndex object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Index Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Index Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <Value> T caseNamedIndexContainer(NamedIndexContainer<Value> object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Ports Price Map</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ports Price Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortsPriceMap(PortsPriceMap object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Ports Split Price Map</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ports Split Price Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortsSplitPriceMap(PortsSplitPriceMap object) {
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
