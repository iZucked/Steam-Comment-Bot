/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage
 * @generated
 */
public interface AnalyticsFactory extends EFactory {

	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AnalyticsFactory eINSTANCE = com.mmxlabs.models.lng.analytics.impl.AnalyticsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	AnalyticsModel createAnalyticsModel();

	/**
	 * Returns a new object of class '<em>Open Sell</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Open Sell</em>'.
	 * @generated
	 */
	OpenSell createOpenSell();

	/**
	 * Returns a new object of class '<em>Open Buy</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Open Buy</em>'.
	 * @generated
	 */
	OpenBuy createOpenBuy();

	/**
	 * Returns a new object of class '<em>Buy Opportunity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Buy Opportunity</em>'.
	 * @generated
	 */
	BuyOpportunity createBuyOpportunity();

	/**
	 * Returns a new object of class '<em>Sell Opportunity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sell Opportunity</em>'.
	 * @generated
	 */
	SellOpportunity createSellOpportunity();

	/**
	 * Returns a new object of class '<em>Buy Market</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Buy Market</em>'.
	 * @generated
	 */
	BuyMarket createBuyMarket();

	/**
	 * Returns a new object of class '<em>Sell Market</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sell Market</em>'.
	 * @generated
	 */
	SellMarket createSellMarket();

	/**
	 * Returns a new object of class '<em>Buy Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Buy Reference</em>'.
	 * @generated
	 */
	BuyReference createBuyReference();

	/**
	 * Returns a new object of class '<em>Sell Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sell Reference</em>'.
	 * @generated
	 */
	SellReference createSellReference();

	/**
	 * Returns a new object of class '<em>Vessel Event Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Event Option</em>'.
	 * @generated
	 */
	VesselEventOption createVesselEventOption();

	/**
	 * Returns a new object of class '<em>Vessel Event Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Event Reference</em>'.
	 * @generated
	 */
	VesselEventReference createVesselEventReference();

	/**
	 * Returns a new object of class '<em>Charter Out Opportunity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Charter Out Opportunity</em>'.
	 * @generated
	 */
	CharterOutOpportunity createCharterOutOpportunity();

	/**
	 * Returns a new object of class '<em>Base Case Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Base Case Row</em>'.
	 * @generated
	 */
	BaseCaseRow createBaseCaseRow();

	/**
	 * Returns a new object of class '<em>Base Case Row Options</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Base Case Row Options</em>'.
	 * @generated
	 */
	BaseCaseRowOptions createBaseCaseRowOptions();

	/**
	 * Returns a new object of class '<em>Partial Case Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Partial Case Row</em>'.
	 * @generated
	 */
	PartialCaseRow createPartialCaseRow();

	/**
	 * Returns a new object of class '<em>Partial Case Row Options</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Partial Case Row Options</em>'.
	 * @generated
	 */
	PartialCaseRowOptions createPartialCaseRowOptions();

	/**
	 * Returns a new object of class '<em>Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Shipping Option</em>'.
	 * @generated
	 */
	ShippingOption createShippingOption();

	/**
	 * Returns a new object of class '<em>Simple Vessel Charter Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Simple Vessel Charter Option</em>'.
	 * @generated
	 */
	SimpleVesselCharterOption createSimpleVesselCharterOption();

	/**
	 * Returns a new object of class '<em>Optional Simple Vessel Charter Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Optional Simple Vessel Charter Option</em>'.
	 * @generated
	 */
	OptionalSimpleVesselCharterOption createOptionalSimpleVesselCharterOption();

	/**
	 * Returns a new object of class '<em>Round Trip Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Round Trip Shipping Option</em>'.
	 * @generated
	 */
	RoundTripShippingOption createRoundTripShippingOption();

	/**
	 * Returns a new object of class '<em>Nominated Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Nominated Shipping Option</em>'.
	 * @generated
	 */
	NominatedShippingOption createNominatedShippingOption();

	/**
	 * Returns a new object of class '<em>Full Vessel Charter Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Full Vessel Charter Option</em>'.
	 * @generated
	 */
	FullVesselCharterOption createFullVesselCharterOption();

	/**
	 * Returns a new object of class '<em>Existing Vessel Charter Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Existing Vessel Charter Option</em>'.
	 * @generated
	 */
	ExistingVesselCharterOption createExistingVesselCharterOption();

	/**
	 * Returns a new object of class '<em>Analysis Result Detail</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Analysis Result Detail</em>'.
	 * @generated
	 */
	AnalysisResultDetail createAnalysisResultDetail();

	/**
	 * Returns a new object of class '<em>Profit And Loss Result</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Profit And Loss Result</em>'.
	 * @generated
	 */
	ProfitAndLossResult createProfitAndLossResult();

	/**
	 * Returns a new object of class '<em>Break Even Result</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Break Even Result</em>'.
	 * @generated
	 */
	BreakEvenResult createBreakEvenResult();

	/**
	 * Returns a new object of class '<em>Option Analysis Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Option Analysis Model</em>'.
	 * @generated
	 */
	OptionAnalysisModel createOptionAnalysisModel();

	/**
	 * Returns a new object of class '<em>Sandbox Result</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sandbox Result</em>'.
	 * @generated
	 */
	SandboxResult createSandboxResult();

	/**
	 * Returns a new object of class '<em>Base Case</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Base Case</em>'.
	 * @generated
	 */
	BaseCase createBaseCase();

	/**
	 * Returns a new object of class '<em>Partial Case</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Partial Case</em>'.
	 * @generated
	 */
	PartialCase createPartialCase();

	/**
	 * Returns a new object of class '<em>Existing Charter Market Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Existing Charter Market Option</em>'.
	 * @generated
	 */
	ExistingCharterMarketOption createExistingCharterMarketOption();

	/**
	 * Returns a new object of class '<em>Actionable Set Plan</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Actionable Set Plan</em>'.
	 * @generated
	 */
	ActionableSetPlan createActionableSetPlan();

	/**
	 * Returns a new object of class '<em>Slot Insertion Options</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Slot Insertion Options</em>'.
	 * @generated
	 */
	SlotInsertionOptions createSlotInsertionOptions();

	/**
	 * Returns a new object of class '<em>Solution Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Solution Option</em>'.
	 * @generated
	 */
	SolutionOption createSolutionOption();

	/**
	 * Returns a new object of class '<em>Optimisation Result</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Optimisation Result</em>'.
	 * @generated
	 */
	OptimisationResult createOptimisationResult();

	/**
	 * Returns a new object of class '<em>Dual Mode Solution Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Dual Mode Solution Option</em>'.
	 * @generated
	 */
	DualModeSolutionOption createDualModeSolutionOption();

	/**
	 * Returns a new object of class '<em>Solution Option Micro Case</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Solution Option Micro Case</em>'.
	 * @generated
	 */
	SolutionOptionMicroCase createSolutionOptionMicroCase();

	/**
	 * Returns a new object of class '<em>Change Description</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Description</em>'.
	 * @generated
	 */
	ChangeDescription createChangeDescription();

	/**
	 * Returns a new object of class '<em>Open Slot Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Open Slot Change</em>'.
	 * @generated
	 */
	OpenSlotChange createOpenSlotChange();

	/**
	 * Returns a new object of class '<em>Cargo Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo Change</em>'.
	 * @generated
	 */
	CargoChange createCargoChange();

	/**
	 * Returns a new object of class '<em>Vessel Event Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Event Change</em>'.
	 * @generated
	 */
	VesselEventChange createVesselEventChange();

	/**
	 * Returns a new object of class '<em>Vessel Event Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Event Descriptor</em>'.
	 * @generated
	 */
	VesselEventDescriptor createVesselEventDescriptor();

	/**
	 * Returns a new object of class '<em>Real Slot Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Real Slot Descriptor</em>'.
	 * @generated
	 */
	RealSlotDescriptor createRealSlotDescriptor();

	/**
	 * Returns a new object of class '<em>Spot Market Slot Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spot Market Slot Descriptor</em>'.
	 * @generated
	 */
	SpotMarketSlotDescriptor createSpotMarketSlotDescriptor();

	/**
	 * Returns a new object of class '<em>Vessel Allocation Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Allocation Descriptor</em>'.
	 * @generated
	 */
	VesselAllocationDescriptor createVesselAllocationDescriptor();

	/**
	 * Returns a new object of class '<em>Market Vessel Allocation Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Market Vessel Allocation Descriptor</em>'.
	 * @generated
	 */
	MarketVesselAllocationDescriptor createMarketVesselAllocationDescriptor();

	/**
	 * Returns a new object of class '<em>Fleet Vessel Allocation Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fleet Vessel Allocation Descriptor</em>'.
	 * @generated
	 */
	FleetVesselAllocationDescriptor createFleetVesselAllocationDescriptor();

	/**
	 * Returns a new object of class '<em>Position Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Position Descriptor</em>'.
	 * @generated
	 */
	PositionDescriptor createPositionDescriptor();

	/**
	 * Returns a new object of class '<em>Viability Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Viability Model</em>'.
	 * @generated
	 */
	ViabilityModel createViabilityModel();

	/**
	 * Returns a new object of class '<em>Viability Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Viability Row</em>'.
	 * @generated
	 */
	ViabilityRow createViabilityRow();

	/**
	 * Returns a new object of class '<em>Viability Result</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Viability Result</em>'.
	 * @generated
	 */
	ViabilityResult createViabilityResult();

	/**
	 * Returns a new object of class '<em>MTM Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MTM Model</em>'.
	 * @generated
	 */
	MTMModel createMTMModel();

	/**
	 * Returns a new object of class '<em>MTM Result</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MTM Result</em>'.
	 * @generated
	 */
	MTMResult createMTMResult();

	/**
	 * Returns a new object of class '<em>MTM Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MTM Row</em>'.
	 * @generated
	 */
	MTMRow createMTMRow();

	/**
	 * Returns a new object of class '<em>Break Even Analysis Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Break Even Analysis Model</em>'.
	 * @generated
	 */
	BreakEvenAnalysisModel createBreakEvenAnalysisModel();

	/**
	 * Returns a new object of class '<em>Break Even Analysis Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Break Even Analysis Row</em>'.
	 * @generated
	 */
	BreakEvenAnalysisRow createBreakEvenAnalysisRow();

	/**
	 * Returns a new object of class '<em>Break Even Analysis Result Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Break Even Analysis Result Set</em>'.
	 * @generated
	 */
	BreakEvenAnalysisResultSet createBreakEvenAnalysisResultSet();

	/**
	 * Returns a new object of class '<em>Break Even Analysis Result</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Break Even Analysis Result</em>'.
	 * @generated
	 */
	BreakEvenAnalysisResult createBreakEvenAnalysisResult();

	/**
	 * Returns a new object of class '<em>Local Date Time Holder</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Local Date Time Holder</em>'.
	 * @generated
	 */
	LocalDateTimeHolder createLocalDateTimeHolder();

	/**
	 * Returns a new object of class '<em>Commodity Curve Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Commodity Curve Option</em>'.
	 * @generated
	 */
	CommodityCurveOption createCommodityCurveOption();

	/**
	 * Returns a new object of class '<em>Commodity Curve Overlay</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Commodity Curve Overlay</em>'.
	 * @generated
	 */
	CommodityCurveOverlay createCommodityCurveOverlay();

	/**
	 * Returns a new object of class '<em>Sensitivity Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sensitivity Model</em>'.
	 * @generated
	 */
	SensitivityModel createSensitivityModel();

	/**
	 * Returns a new object of class '<em>Sensitivity Solution Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sensitivity Solution Set</em>'.
	 * @generated
	 */
	SensitivitySolutionSet createSensitivitySolutionSet();

	/**
	 * Returns a new object of class '<em>Portfolio Sensitivity Result</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Portfolio Sensitivity Result</em>'.
	 * @generated
	 */
	PortfolioSensitivityResult createPortfolioSensitivityResult();

	/**
	 * Returns a new object of class '<em>Cargo Pn LResult</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo Pn LResult</em>'.
	 * @generated
	 */
	CargoPnLResult createCargoPnLResult();

	/**
	 * Returns a new object of class '<em>Swap Value Matrix Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Swap Value Matrix Model</em>'.
	 * @generated
	 */
	SwapValueMatrixModel createSwapValueMatrixModel();

	/**
	 * Returns a new object of class '<em>Swap Value Matrix Result</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Swap Value Matrix Result</em>'.
	 * @generated
	 */
	SwapValueMatrixResult createSwapValueMatrixResult();

	/**
	 * Returns a new object of class '<em>Swap Value Matrix Result Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Swap Value Matrix Result Set</em>'.
	 * @generated
	 */
	SwapValueMatrixResultSet createSwapValueMatrixResultSet();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	AnalyticsPackage getAnalyticsPackage();

} //AnalyticsFactory
