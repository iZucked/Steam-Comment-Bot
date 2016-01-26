/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	AnalyticsPackage getAnalyticsPackage();

} //AnalyticsFactory
