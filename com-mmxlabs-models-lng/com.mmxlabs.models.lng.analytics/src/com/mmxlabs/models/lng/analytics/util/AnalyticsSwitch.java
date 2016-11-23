/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.util;

import com.mmxlabs.models.lng.analytics.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.jdt.annotation.Nullable;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.CargoSandbox;
import com.mmxlabs.models.lng.analytics.CostComponent;
import com.mmxlabs.models.lng.analytics.FuelCost;
import com.mmxlabs.models.lng.analytics.Journey;
import com.mmxlabs.models.lng.analytics.ProvisionalCargo;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.analytics.Visit;
import com.mmxlabs.models.lng.analytics.Voyage;
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
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage
 * @generated
 */
public class AnalyticsSwitch<@Nullable T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static AnalyticsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalyticsSwitch() {
		if (modelPackage == null) {
			modelPackage = AnalyticsPackage.eINSTANCE;
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
			case AnalyticsPackage.ANALYTICS_MODEL: {
				AnalyticsModel analyticsModel = (AnalyticsModel)theEObject;
				T result = caseAnalyticsModel(analyticsModel);
				if (result == null) result = caseUUIDObject(analyticsModel);
				if (result == null) result = caseMMXObject(analyticsModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.UNIT_COST_MATRIX: {
				UnitCostMatrix unitCostMatrix = (UnitCostMatrix)theEObject;
				T result = caseUnitCostMatrix(unitCostMatrix);
				if (result == null) result = caseUUIDObject(unitCostMatrix);
				if (result == null) result = caseNamedObject(unitCostMatrix);
				if (result == null) result = caseMMXObject(unitCostMatrix);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.UNIT_COST_LINE: {
				UnitCostLine unitCostLine = (UnitCostLine)theEObject;
				T result = caseUnitCostLine(unitCostLine);
				if (result == null) result = caseMMXObject(unitCostLine);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.VOYAGE: {
				Voyage voyage = (Voyage)theEObject;
				T result = caseVoyage(voyage);
				if (result == null) result = caseCostComponent(voyage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.VISIT: {
				Visit visit = (Visit)theEObject;
				T result = caseVisit(visit);
				if (result == null) result = caseCostComponent(visit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.COST_COMPONENT: {
				CostComponent costComponent = (CostComponent)theEObject;
				T result = caseCostComponent(costComponent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.FUEL_COST: {
				FuelCost fuelCost = (FuelCost)theEObject;
				T result = caseFuelCost(fuelCost);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.JOURNEY: {
				Journey journey = (Journey)theEObject;
				T result = caseJourney(journey);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SHIPPING_COST_PLAN: {
				ShippingCostPlan shippingCostPlan = (ShippingCostPlan)theEObject;
				T result = caseShippingCostPlan(shippingCostPlan);
				if (result == null) result = caseNamedObject(shippingCostPlan);
				if (result == null) result = caseMMXObject(shippingCostPlan);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SHIPPING_COST_ROW: {
				ShippingCostRow shippingCostRow = (ShippingCostRow)theEObject;
				T result = caseShippingCostRow(shippingCostRow);
				if (result == null) result = caseMMXObject(shippingCostRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.CARGO_SANDBOX: {
				CargoSandbox cargoSandbox = (CargoSandbox)theEObject;
				T result = caseCargoSandbox(cargoSandbox);
				if (result == null) result = caseNamedObject(cargoSandbox);
				if (result == null) result = caseMMXObject(cargoSandbox);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.PROVISIONAL_CARGO: {
				ProvisionalCargo provisionalCargo = (ProvisionalCargo)theEObject;
				T result = caseProvisionalCargo(provisionalCargo);
				if (result == null) result = caseMMXObject(provisionalCargo);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BUY_OPTION: {
				BuyOption buyOption = (BuyOption)theEObject;
				T result = caseBuyOption(buyOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SELL_OPTION: {
				SellOption sellOption = (SellOption)theEObject;
				T result = caseSellOption(sellOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BUY_OPPORTUNITY: {
				BuyOpportunity buyOpportunity = (BuyOpportunity)theEObject;
				T result = caseBuyOpportunity(buyOpportunity);
				if (result == null) result = caseMMXObject(buyOpportunity);
				if (result == null) result = caseBuyOption(buyOpportunity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SELL_OPPORTUNITY: {
				SellOpportunity sellOpportunity = (SellOpportunity)theEObject;
				T result = caseSellOpportunity(sellOpportunity);
				if (result == null) result = caseMMXObject(sellOpportunity);
				if (result == null) result = caseSellOption(sellOpportunity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BUY_MARKET: {
				BuyMarket buyMarket = (BuyMarket)theEObject;
				T result = caseBuyMarket(buyMarket);
				if (result == null) result = caseBuyOption(buyMarket);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SELL_MARKET: {
				SellMarket sellMarket = (SellMarket)theEObject;
				T result = caseSellMarket(sellMarket);
				if (result == null) result = caseSellOption(sellMarket);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BUY_REFERENCE: {
				BuyReference buyReference = (BuyReference)theEObject;
				T result = caseBuyReference(buyReference);
				if (result == null) result = caseBuyOption(buyReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SELL_REFERENCE: {
				SellReference sellReference = (SellReference)theEObject;
				T result = caseSellReference(sellReference);
				if (result == null) result = caseSellOption(sellReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BASE_CASE_ROW: {
				BaseCaseRow baseCaseRow = (BaseCaseRow)theEObject;
				T result = caseBaseCaseRow(baseCaseRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.PARTIAL_CASE_ROW: {
				PartialCaseRow partialCaseRow = (PartialCaseRow)theEObject;
				T result = casePartialCaseRow(partialCaseRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SHIPPING_OPTION: {
				ShippingOption shippingOption = (ShippingOption)theEObject;
				T result = caseShippingOption(shippingOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.FLEET_SHIPPING_OPTION: {
				FleetShippingOption fleetShippingOption = (FleetShippingOption)theEObject;
				T result = caseFleetShippingOption(fleetShippingOption);
				if (result == null) result = caseShippingOption(fleetShippingOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION: {
				OptionalAvailabilityShippingOption optionalAvailabilityShippingOption = (OptionalAvailabilityShippingOption)theEObject;
				T result = caseOptionalAvailabilityShippingOption(optionalAvailabilityShippingOption);
				if (result == null) result = caseFleetShippingOption(optionalAvailabilityShippingOption);
				if (result == null) result = caseShippingOption(optionalAvailabilityShippingOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.ROUND_TRIP_SHIPPING_OPTION: {
				RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption)theEObject;
				T result = caseRoundTripShippingOption(roundTripShippingOption);
				if (result == null) result = caseShippingOption(roundTripShippingOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.NOMINATED_SHIPPING_OPTION: {
				NominatedShippingOption nominatedShippingOption = (NominatedShippingOption)theEObject;
				T result = caseNominatedShippingOption(nominatedShippingOption);
				if (result == null) result = caseShippingOption(nominatedShippingOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.ANALYSIS_RESULT_ROW: {
				AnalysisResultRow analysisResultRow = (AnalysisResultRow)theEObject;
				T result = caseAnalysisResultRow(analysisResultRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.RESULT_CONTAINER: {
				ResultContainer resultContainer = (ResultContainer)theEObject;
				T result = caseResultContainer(resultContainer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.ANALYSIS_RESULT_DETAIL: {
				AnalysisResultDetail analysisResultDetail = (AnalysisResultDetail)theEObject;
				T result = caseAnalysisResultDetail(analysisResultDetail);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.PROFIT_AND_LOSS_RESULT: {
				ProfitAndLossResult profitAndLossResult = (ProfitAndLossResult)theEObject;
				T result = caseProfitAndLossResult(profitAndLossResult);
				if (result == null) result = caseAnalysisResultDetail(profitAndLossResult);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BREAK_EVEN_RESULT: {
				BreakEvenResult breakEvenResult = (BreakEvenResult)theEObject;
				T result = caseBreakEvenResult(breakEvenResult);
				if (result == null) result = caseAnalysisResultDetail(breakEvenResult);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL: {
				OptionAnalysisModel optionAnalysisModel = (OptionAnalysisModel)theEObject;
				T result = caseOptionAnalysisModel(optionAnalysisModel);
				if (result == null) result = caseNamedObject(optionAnalysisModel);
				if (result == null) result = caseMMXObject(optionAnalysisModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.RESULT_SET: {
				ResultSet resultSet = (ResultSet)theEObject;
				T result = caseResultSet(resultSet);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BASE_CASE: {
				BaseCase baseCase = (BaseCase)theEObject;
				T result = caseBaseCase(baseCase);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.PARTIAL_CASE: {
				PartialCase partialCase = (PartialCase)theEObject;
				T result = casePartialCase(partialCase);
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
	public T caseAnalyticsModel(AnalyticsModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Unit Cost Matrix</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Unit Cost Matrix</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUnitCostMatrix(UnitCostMatrix object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Unit Cost Line</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Unit Cost Line</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUnitCostLine(UnitCostLine object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Voyage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Voyage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVoyage(Voyage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Visit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Visit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVisit(Visit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cost Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cost Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCostComponent(CostComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fuel Cost</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fuel Cost</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFuelCost(FuelCost object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Journey</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Journey</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseJourney(Journey object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Shipping Cost Plan</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Shipping Cost Plan</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseShippingCostPlan(ShippingCostPlan object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Shipping Cost Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Shipping Cost Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseShippingCostRow(ShippingCostRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cargo Sandbox</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cargo Sandbox</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCargoSandbox(CargoSandbox object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Provisional Cargo</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Provisional Cargo</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProvisionalCargo(ProvisionalCargo object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Buy Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Buy Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBuyOption(BuyOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sell Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sell Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSellOption(SellOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Buy Opportunity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Buy Opportunity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBuyOpportunity(BuyOpportunity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sell Opportunity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sell Opportunity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSellOpportunity(SellOpportunity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Buy Market</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Buy Market</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBuyMarket(BuyMarket object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sell Market</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sell Market</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSellMarket(SellMarket object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Buy Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Buy Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBuyReference(BuyReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sell Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sell Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSellReference(SellReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Base Case Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Base Case Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBaseCaseRow(BaseCaseRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Partial Case Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Partial Case Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePartialCaseRow(PartialCaseRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Shipping Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseShippingOption(ShippingOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fleet Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fleet Shipping Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFleetShippingOption(FleetShippingOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Optional Availability Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Optional Availability Shipping Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOptionalAvailabilityShippingOption(OptionalAvailabilityShippingOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Round Trip Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Round Trip Shipping Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRoundTripShippingOption(RoundTripShippingOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Nominated Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Nominated Shipping Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNominatedShippingOption(NominatedShippingOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Analysis Result Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Analysis Result Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnalysisResultRow(AnalysisResultRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Result Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Result Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResultContainer(ResultContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Analysis Result Detail</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Analysis Result Detail</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnalysisResultDetail(AnalysisResultDetail object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Profit And Loss Result</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Profit And Loss Result</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProfitAndLossResult(ProfitAndLossResult object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Break Even Result</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Break Even Result</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBreakEvenResult(BreakEvenResult object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Option Analysis Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Option Analysis Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOptionAnalysisModel(OptionAnalysisModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Result Set</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Result Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResultSet(ResultSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Base Case</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Base Case</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBaseCase(BaseCase object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Partial Case</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Partial Case</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePartialCase(PartialCase object) {
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

} //AnalyticsSwitch
