/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
	 * Returns a new object of class '<em>Unit Cost Matrix</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unit Cost Matrix</em>'.
	 * @generated
	 */
	UnitCostMatrix createUnitCostMatrix();

	/**
	 * Returns a new object of class '<em>Unit Cost Line</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unit Cost Line</em>'.
	 * @generated
	 */
	UnitCostLine createUnitCostLine();

	/**
	 * Returns a new object of class '<em>Voyage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Voyage</em>'.
	 * @generated
	 */
	Voyage createVoyage();

	/**
	 * Returns a new object of class '<em>Visit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Visit</em>'.
	 * @generated
	 */
	Visit createVisit();

	/**
	 * Returns a new object of class '<em>Cost Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cost Component</em>'.
	 * @generated
	 */
	CostComponent createCostComponent();

	/**
	 * Returns a new object of class '<em>Fuel Cost</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fuel Cost</em>'.
	 * @generated
	 */
	FuelCost createFuelCost();

	/**
	 * Returns a new object of class '<em>Journey</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Journey</em>'.
	 * @generated
	 */
	Journey createJourney();

	/**
	 * Returns a new object of class '<em>Shipping Cost Plan</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Shipping Cost Plan</em>'.
	 * @generated
	 */
	ShippingCostPlan createShippingCostPlan();

	/**
	 * Returns a new object of class '<em>Shipping Cost Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Shipping Cost Row</em>'.
	 * @generated
	 */
	ShippingCostRow createShippingCostRow();

	/**
	 * Returns a new object of class '<em>Cargo Sandbox</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo Sandbox</em>'.
	 * @generated
	 */
	CargoSandbox createCargoSandbox();

	/**
	 * Returns a new object of class '<em>Provisional Cargo</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Provisional Cargo</em>'.
	 * @generated
	 */
	ProvisionalCargo createProvisionalCargo();

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
	 * Returns a new object of class '<em>Base Case Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Base Case Row</em>'.
	 * @generated
	 */
	BaseCaseRow createBaseCaseRow();

	/**
	 * Returns a new object of class '<em>Partial Case Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Partial Case Row</em>'.
	 * @generated
	 */
	PartialCaseRow createPartialCaseRow();

	/**
	 * Returns a new object of class '<em>Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Shipping Option</em>'.
	 * @generated
	 */
	ShippingOption createShippingOption();

	/**
	 * Returns a new object of class '<em>Fleet Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fleet Shipping Option</em>'.
	 * @generated
	 */
	FleetShippingOption createFleetShippingOption();

	/**
	 * Returns a new object of class '<em>Optional Availability Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Optional Availability Shipping Option</em>'.
	 * @generated
	 */
	OptionalAvailabilityShippingOption createOptionalAvailabilityShippingOption();

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
	 * Returns a new object of class '<em>Analysis Result Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Analysis Result Row</em>'.
	 * @generated
	 */
	AnalysisResultRow createAnalysisResultRow();

	/**
	 * Returns a new object of class '<em>Result Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Result Container</em>'.
	 * @generated
	 */
	ResultContainer createResultContainer();

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
	 * Returns a new object of class '<em>Result Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Result Set</em>'.
	 * @generated
	 */
	ResultSet createResultSet();

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
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	AnalyticsPackage getAnalyticsPackage();

} //AnalyticsFactory
