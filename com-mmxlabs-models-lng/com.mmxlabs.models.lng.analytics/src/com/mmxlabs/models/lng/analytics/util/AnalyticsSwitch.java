/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.models.lng.analytics.SellOpportunity;
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
			case AnalyticsPackage.OPEN_SELL: {
				OpenSell openSell = (OpenSell)theEObject;
				T result = caseOpenSell(openSell);
				if (result == null) result = caseUUIDObject(openSell);
				if (result == null) result = caseSellOption(openSell);
				if (result == null) result = caseMMXObject(openSell);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.OPEN_BUY: {
				OpenBuy openBuy = (OpenBuy)theEObject;
				T result = caseOpenBuy(openBuy);
				if (result == null) result = caseUUIDObject(openBuy);
				if (result == null) result = caseBuyOption(openBuy);
				if (result == null) result = caseMMXObject(openBuy);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BUY_OPPORTUNITY: {
				BuyOpportunity buyOpportunity = (BuyOpportunity)theEObject;
				T result = caseBuyOpportunity(buyOpportunity);
				if (result == null) result = caseUUIDObject(buyOpportunity);
				if (result == null) result = caseBuyOption(buyOpportunity);
				if (result == null) result = caseMMXObject(buyOpportunity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SELL_OPPORTUNITY: {
				SellOpportunity sellOpportunity = (SellOpportunity)theEObject;
				T result = caseSellOpportunity(sellOpportunity);
				if (result == null) result = caseUUIDObject(sellOpportunity);
				if (result == null) result = caseSellOption(sellOpportunity);
				if (result == null) result = caseMMXObject(sellOpportunity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BUY_MARKET: {
				BuyMarket buyMarket = (BuyMarket)theEObject;
				T result = caseBuyMarket(buyMarket);
				if (result == null) result = caseUUIDObject(buyMarket);
				if (result == null) result = caseBuyOption(buyMarket);
				if (result == null) result = caseMMXObject(buyMarket);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SELL_MARKET: {
				SellMarket sellMarket = (SellMarket)theEObject;
				T result = caseSellMarket(sellMarket);
				if (result == null) result = caseUUIDObject(sellMarket);
				if (result == null) result = caseSellOption(sellMarket);
				if (result == null) result = caseMMXObject(sellMarket);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BUY_REFERENCE: {
				BuyReference buyReference = (BuyReference)theEObject;
				T result = caseBuyReference(buyReference);
				if (result == null) result = caseUUIDObject(buyReference);
				if (result == null) result = caseBuyOption(buyReference);
				if (result == null) result = caseMMXObject(buyReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SELL_REFERENCE: {
				SellReference sellReference = (SellReference)theEObject;
				T result = caseSellReference(sellReference);
				if (result == null) result = caseUUIDObject(sellReference);
				if (result == null) result = caseSellOption(sellReference);
				if (result == null) result = caseMMXObject(sellReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.VESSEL_EVENT_OPTION: {
				VesselEventOption vesselEventOption = (VesselEventOption)theEObject;
				T result = caseVesselEventOption(vesselEventOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.VESSEL_EVENT_REFERENCE: {
				VesselEventReference vesselEventReference = (VesselEventReference)theEObject;
				T result = caseVesselEventReference(vesselEventReference);
				if (result == null) result = caseUUIDObject(vesselEventReference);
				if (result == null) result = caseVesselEventOption(vesselEventReference);
				if (result == null) result = caseMMXObject(vesselEventReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.CHARTER_OUT_OPPORTUNITY: {
				CharterOutOpportunity charterOutOpportunity = (CharterOutOpportunity)theEObject;
				T result = caseCharterOutOpportunity(charterOutOpportunity);
				if (result == null) result = caseUUIDObject(charterOutOpportunity);
				if (result == null) result = caseVesselEventOption(charterOutOpportunity);
				if (result == null) result = caseMMXObject(charterOutOpportunity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BASE_CASE_ROW: {
				BaseCaseRow baseCaseRow = (BaseCaseRow)theEObject;
				T result = caseBaseCaseRow(baseCaseRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BASE_CASE_ROW_OPTIONS: {
				BaseCaseRowOptions baseCaseRowOptions = (BaseCaseRowOptions)theEObject;
				T result = caseBaseCaseRowOptions(baseCaseRowOptions);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.PARTIAL_CASE_ROW: {
				PartialCaseRow partialCaseRow = (PartialCaseRow)theEObject;
				T result = casePartialCaseRow(partialCaseRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.PARTIAL_CASE_ROW_OPTIONS: {
				PartialCaseRowOptions partialCaseRowOptions = (PartialCaseRowOptions)theEObject;
				T result = casePartialCaseRowOptions(partialCaseRowOptions);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SHIPPING_OPTION: {
				ShippingOption shippingOption = (ShippingOption)theEObject;
				T result = caseShippingOption(shippingOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION: {
				SimpleVesselCharterOption simpleVesselCharterOption = (SimpleVesselCharterOption)theEObject;
				T result = caseSimpleVesselCharterOption(simpleVesselCharterOption);
				if (result == null) result = caseUUIDObject(simpleVesselCharterOption);
				if (result == null) result = caseShippingOption(simpleVesselCharterOption);
				if (result == null) result = caseMMXObject(simpleVesselCharterOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION: {
				OptionalSimpleVesselCharterOption optionalSimpleVesselCharterOption = (OptionalSimpleVesselCharterOption)theEObject;
				T result = caseOptionalSimpleVesselCharterOption(optionalSimpleVesselCharterOption);
				if (result == null) result = caseSimpleVesselCharterOption(optionalSimpleVesselCharterOption);
				if (result == null) result = caseUUIDObject(optionalSimpleVesselCharterOption);
				if (result == null) result = caseShippingOption(optionalSimpleVesselCharterOption);
				if (result == null) result = caseMMXObject(optionalSimpleVesselCharterOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.ROUND_TRIP_SHIPPING_OPTION: {
				RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption)theEObject;
				T result = caseRoundTripShippingOption(roundTripShippingOption);
				if (result == null) result = caseUUIDObject(roundTripShippingOption);
				if (result == null) result = caseShippingOption(roundTripShippingOption);
				if (result == null) result = caseMMXObject(roundTripShippingOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.NOMINATED_SHIPPING_OPTION: {
				NominatedShippingOption nominatedShippingOption = (NominatedShippingOption)theEObject;
				T result = caseNominatedShippingOption(nominatedShippingOption);
				if (result == null) result = caseUUIDObject(nominatedShippingOption);
				if (result == null) result = caseShippingOption(nominatedShippingOption);
				if (result == null) result = caseMMXObject(nominatedShippingOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.FULL_VESSEL_CHARTER_OPTION: {
				FullVesselCharterOption fullVesselCharterOption = (FullVesselCharterOption)theEObject;
				T result = caseFullVesselCharterOption(fullVesselCharterOption);
				if (result == null) result = caseUUIDObject(fullVesselCharterOption);
				if (result == null) result = caseShippingOption(fullVesselCharterOption);
				if (result == null) result = caseMMXObject(fullVesselCharterOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.EXISTING_VESSEL_CHARTER_OPTION: {
				ExistingVesselCharterOption existingVesselCharterOption = (ExistingVesselCharterOption)theEObject;
				T result = caseExistingVesselCharterOption(existingVesselCharterOption);
				if (result == null) result = caseUUIDObject(existingVesselCharterOption);
				if (result == null) result = caseShippingOption(existingVesselCharterOption);
				if (result == null) result = caseMMXObject(existingVesselCharterOption);
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
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL: {
				AbstractAnalysisModel abstractAnalysisModel = (AbstractAnalysisModel)theEObject;
				T result = caseAbstractAnalysisModel(abstractAnalysisModel);
				if (result == null) result = caseUUIDObject(abstractAnalysisModel);
				if (result == null) result = caseNamedObject(abstractAnalysisModel);
				if (result == null) result = caseMMXObject(abstractAnalysisModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL: {
				OptionAnalysisModel optionAnalysisModel = (OptionAnalysisModel)theEObject;
				T result = caseOptionAnalysisModel(optionAnalysisModel);
				if (result == null) result = caseAbstractAnalysisModel(optionAnalysisModel);
				if (result == null) result = caseUUIDObject(optionAnalysisModel);
				if (result == null) result = caseNamedObject(optionAnalysisModel);
				if (result == null) result = caseMMXObject(optionAnalysisModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SANDBOX_RESULT: {
				SandboxResult sandboxResult = (SandboxResult)theEObject;
				T result = caseSandboxResult(sandboxResult);
				if (result == null) result = caseAbstractSolutionSet(sandboxResult);
				if (result == null) result = caseUUIDObject(sandboxResult);
				if (result == null) result = caseNamedObject(sandboxResult);
				if (result == null) result = caseMMXObject(sandboxResult);
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
			case AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION: {
				ExistingCharterMarketOption existingCharterMarketOption = (ExistingCharterMarketOption)theEObject;
				T result = caseExistingCharterMarketOption(existingCharterMarketOption);
				if (result == null) result = caseUUIDObject(existingCharterMarketOption);
				if (result == null) result = caseShippingOption(existingCharterMarketOption);
				if (result == null) result = caseMMXObject(existingCharterMarketOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET: {
				AbstractSolutionSet abstractSolutionSet = (AbstractSolutionSet)theEObject;
				T result = caseAbstractSolutionSet(abstractSolutionSet);
				if (result == null) result = caseUUIDObject(abstractSolutionSet);
				if (result == null) result = caseNamedObject(abstractSolutionSet);
				if (result == null) result = caseMMXObject(abstractSolutionSet);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.ACTIONABLE_SET_PLAN: {
				ActionableSetPlan actionableSetPlan = (ActionableSetPlan)theEObject;
				T result = caseActionableSetPlan(actionableSetPlan);
				if (result == null) result = caseAbstractSolutionSet(actionableSetPlan);
				if (result == null) result = caseUUIDObject(actionableSetPlan);
				if (result == null) result = caseNamedObject(actionableSetPlan);
				if (result == null) result = caseMMXObject(actionableSetPlan);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS: {
				SlotInsertionOptions slotInsertionOptions = (SlotInsertionOptions)theEObject;
				T result = caseSlotInsertionOptions(slotInsertionOptions);
				if (result == null) result = caseAbstractSolutionSet(slotInsertionOptions);
				if (result == null) result = caseUUIDObject(slotInsertionOptions);
				if (result == null) result = caseNamedObject(slotInsertionOptions);
				if (result == null) result = caseMMXObject(slotInsertionOptions);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SOLUTION_OPTION: {
				SolutionOption solutionOption = (SolutionOption)theEObject;
				T result = caseSolutionOption(solutionOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.OPTIMISATION_RESULT: {
				OptimisationResult optimisationResult = (OptimisationResult)theEObject;
				T result = caseOptimisationResult(optimisationResult);
				if (result == null) result = caseAbstractSolutionSet(optimisationResult);
				if (result == null) result = caseUUIDObject(optimisationResult);
				if (result == null) result = caseNamedObject(optimisationResult);
				if (result == null) result = caseMMXObject(optimisationResult);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION: {
				DualModeSolutionOption dualModeSolutionOption = (DualModeSolutionOption)theEObject;
				T result = caseDualModeSolutionOption(dualModeSolutionOption);
				if (result == null) result = caseSolutionOption(dualModeSolutionOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE: {
				SolutionOptionMicroCase solutionOptionMicroCase = (SolutionOptionMicroCase)theEObject;
				T result = caseSolutionOptionMicroCase(solutionOptionMicroCase);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.CHANGE_DESCRIPTION: {
				ChangeDescription changeDescription = (ChangeDescription)theEObject;
				T result = caseChangeDescription(changeDescription);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.CHANGE: {
				Change change = (Change)theEObject;
				T result = caseChange(change);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.OPEN_SLOT_CHANGE: {
				OpenSlotChange openSlotChange = (OpenSlotChange)theEObject;
				T result = caseOpenSlotChange(openSlotChange);
				if (result == null) result = caseChange(openSlotChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.CARGO_CHANGE: {
				CargoChange cargoChange = (CargoChange)theEObject;
				T result = caseCargoChange(cargoChange);
				if (result == null) result = caseChange(cargoChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.VESSEL_EVENT_CHANGE: {
				VesselEventChange vesselEventChange = (VesselEventChange)theEObject;
				T result = caseVesselEventChange(vesselEventChange);
				if (result == null) result = caseChange(vesselEventChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.VESSEL_EVENT_DESCRIPTOR: {
				VesselEventDescriptor vesselEventDescriptor = (VesselEventDescriptor)theEObject;
				T result = caseVesselEventDescriptor(vesselEventDescriptor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SLOT_DESCRIPTOR: {
				SlotDescriptor slotDescriptor = (SlotDescriptor)theEObject;
				T result = caseSlotDescriptor(slotDescriptor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.REAL_SLOT_DESCRIPTOR: {
				RealSlotDescriptor realSlotDescriptor = (RealSlotDescriptor)theEObject;
				T result = caseRealSlotDescriptor(realSlotDescriptor);
				if (result == null) result = caseSlotDescriptor(realSlotDescriptor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR: {
				SpotMarketSlotDescriptor spotMarketSlotDescriptor = (SpotMarketSlotDescriptor)theEObject;
				T result = caseSpotMarketSlotDescriptor(spotMarketSlotDescriptor);
				if (result == null) result = caseSlotDescriptor(spotMarketSlotDescriptor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.VESSEL_ALLOCATION_DESCRIPTOR: {
				VesselAllocationDescriptor vesselAllocationDescriptor = (VesselAllocationDescriptor)theEObject;
				T result = caseVesselAllocationDescriptor(vesselAllocationDescriptor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR: {
				MarketVesselAllocationDescriptor marketVesselAllocationDescriptor = (MarketVesselAllocationDescriptor)theEObject;
				T result = caseMarketVesselAllocationDescriptor(marketVesselAllocationDescriptor);
				if (result == null) result = caseVesselAllocationDescriptor(marketVesselAllocationDescriptor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.FLEET_VESSEL_ALLOCATION_DESCRIPTOR: {
				FleetVesselAllocationDescriptor fleetVesselAllocationDescriptor = (FleetVesselAllocationDescriptor)theEObject;
				T result = caseFleetVesselAllocationDescriptor(fleetVesselAllocationDescriptor);
				if (result == null) result = caseVesselAllocationDescriptor(fleetVesselAllocationDescriptor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.POSITION_DESCRIPTOR: {
				PositionDescriptor positionDescriptor = (PositionDescriptor)theEObject;
				T result = casePositionDescriptor(positionDescriptor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.VIABILITY_MODEL: {
				ViabilityModel viabilityModel = (ViabilityModel)theEObject;
				T result = caseViabilityModel(viabilityModel);
				if (result == null) result = caseAbstractAnalysisModel(viabilityModel);
				if (result == null) result = caseUUIDObject(viabilityModel);
				if (result == null) result = caseNamedObject(viabilityModel);
				if (result == null) result = caseMMXObject(viabilityModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.VIABILITY_ROW: {
				ViabilityRow viabilityRow = (ViabilityRow)theEObject;
				T result = caseViabilityRow(viabilityRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.VIABILITY_RESULT: {
				ViabilityResult viabilityResult = (ViabilityResult)theEObject;
				T result = caseViabilityResult(viabilityResult);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.MTM_MODEL: {
				MTMModel mtmModel = (MTMModel)theEObject;
				T result = caseMTMModel(mtmModel);
				if (result == null) result = caseAbstractAnalysisModel(mtmModel);
				if (result == null) result = caseUUIDObject(mtmModel);
				if (result == null) result = caseNamedObject(mtmModel);
				if (result == null) result = caseMMXObject(mtmModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.MTM_RESULT: {
				MTMResult mtmResult = (MTMResult)theEObject;
				T result = caseMTMResult(mtmResult);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.MTM_ROW: {
				MTMRow mtmRow = (MTMRow)theEObject;
				T result = caseMTMRow(mtmRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL: {
				BreakEvenAnalysisModel breakEvenAnalysisModel = (BreakEvenAnalysisModel)theEObject;
				T result = caseBreakEvenAnalysisModel(breakEvenAnalysisModel);
				if (result == null) result = caseAbstractAnalysisModel(breakEvenAnalysisModel);
				if (result == null) result = caseUUIDObject(breakEvenAnalysisModel);
				if (result == null) result = caseNamedObject(breakEvenAnalysisModel);
				if (result == null) result = caseMMXObject(breakEvenAnalysisModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW: {
				BreakEvenAnalysisRow breakEvenAnalysisRow = (BreakEvenAnalysisRow)theEObject;
				T result = caseBreakEvenAnalysisRow(breakEvenAnalysisRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET: {
				BreakEvenAnalysisResultSet breakEvenAnalysisResultSet = (BreakEvenAnalysisResultSet)theEObject;
				T result = caseBreakEvenAnalysisResultSet(breakEvenAnalysisResultSet);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT: {
				BreakEvenAnalysisResult breakEvenAnalysisResult = (BreakEvenAnalysisResult)theEObject;
				T result = caseBreakEvenAnalysisResult(breakEvenAnalysisResult);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AnalyticsPackage.LOCAL_DATE_TIME_HOLDER: {
				LocalDateTimeHolder localDateTimeHolder = (LocalDateTimeHolder)theEObject;
				T result = caseLocalDateTimeHolder(localDateTimeHolder);
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
	 * Returns the result of interpreting the object as an instance of '<em>Open Sell</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Open Sell</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOpenSell(OpenSell object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Open Buy</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Open Buy</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOpenBuy(OpenBuy object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Event Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Event Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselEventOption(VesselEventOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Event Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Event Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselEventReference(VesselEventReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Charter Out Opportunity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Charter Out Opportunity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCharterOutOpportunity(CharterOutOpportunity object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Base Case Row Options</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Base Case Row Options</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBaseCaseRowOptions(BaseCaseRowOptions object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Partial Case Row Options</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Partial Case Row Options</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePartialCaseRowOptions(PartialCaseRowOptions object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Simple Vessel Charter Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Vessel Charter Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimpleVesselCharterOption(SimpleVesselCharterOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Optional Simple Vessel Charter Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Optional Simple Vessel Charter Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOptionalSimpleVesselCharterOption(OptionalSimpleVesselCharterOption object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Full Vessel Charter Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Full Vessel Charter Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFullVesselCharterOption(FullVesselCharterOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Existing Vessel Charter Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Existing Vessel Charter Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExistingVesselCharterOption(ExistingVesselCharterOption object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Analysis Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Analysis Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractAnalysisModel(AbstractAnalysisModel object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Sandbox Result</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sandbox Result</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSandboxResult(SandboxResult object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Existing Charter Market Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Existing Charter Market Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExistingCharterMarketOption(ExistingCharterMarketOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Solution Set</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Solution Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractSolutionSet(AbstractSolutionSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Actionable Set Plan</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Actionable Set Plan</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseActionableSetPlan(ActionableSetPlan object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Slot Insertion Options</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Slot Insertion Options</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSlotInsertionOptions(SlotInsertionOptions object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Change Description</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Change Description</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseChangeDescription(ChangeDescription object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseChange(Change object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Open Slot Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Open Slot Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOpenSlotChange(OpenSlotChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cargo Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cargo Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCargoChange(CargoChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Event Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Event Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselEventChange(VesselEventChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Event Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Event Descriptor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselEventDescriptor(VesselEventDescriptor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Slot Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Slot Descriptor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSlotDescriptor(SlotDescriptor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Real Slot Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Real Slot Descriptor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRealSlotDescriptor(RealSlotDescriptor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Spot Market Slot Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Spot Market Slot Descriptor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSpotMarketSlotDescriptor(SpotMarketSlotDescriptor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Allocation Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Allocation Descriptor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselAllocationDescriptor(VesselAllocationDescriptor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Market Vessel Allocation Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Market Vessel Allocation Descriptor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMarketVesselAllocationDescriptor(MarketVesselAllocationDescriptor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fleet Vessel Allocation Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fleet Vessel Allocation Descriptor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFleetVesselAllocationDescriptor(FleetVesselAllocationDescriptor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Position Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Position Descriptor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePositionDescriptor(PositionDescriptor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Viability Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Viability Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseViabilityModel(ViabilityModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Viability Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Viability Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseViabilityRow(ViabilityRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Viability Result</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Viability Result</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseViabilityResult(ViabilityResult object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>MTM Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MTM Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMTMModel(MTMModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>MTM Result</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MTM Result</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMTMResult(MTMResult object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>MTM Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MTM Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMTMRow(MTMRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Break Even Analysis Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Break Even Analysis Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBreakEvenAnalysisModel(BreakEvenAnalysisModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Break Even Analysis Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Break Even Analysis Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBreakEvenAnalysisRow(BreakEvenAnalysisRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Break Even Analysis Result Set</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Break Even Analysis Result Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBreakEvenAnalysisResultSet(BreakEvenAnalysisResultSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Break Even Analysis Result</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Break Even Analysis Result</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBreakEvenAnalysisResult(BreakEvenAnalysisResult object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Local Date Time Holder</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Local Date Time Holder</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLocalDateTimeHolder(LocalDateTimeHolder object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Solution Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Solution Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSolutionOption(SolutionOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Optimisation Result</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Optimisation Result</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOptimisationResult(OptimisationResult object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Dual Mode Solution Option</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Dual Mode Solution Option</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDualModeSolutionOption(DualModeSolutionOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Solution Option Micro Case</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Solution Option Micro Case</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSolutionOptionMicroCase(SolutionOptionMicroCase object) {
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
