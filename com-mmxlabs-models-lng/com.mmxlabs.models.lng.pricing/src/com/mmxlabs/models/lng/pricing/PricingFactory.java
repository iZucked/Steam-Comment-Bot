/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
	 * Returns a new object of class '<em>Fleet Cost Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fleet Cost Model</em>'.
	 * @generated
	 */
	FleetCostModel createFleetCostModel();

	/**
	 * Returns a new object of class '<em>Route Cost</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Route Cost</em>'.
	 * @generated
	 */
	RouteCost createRouteCost();

	/**
	 * Returns a new object of class '<em>Charter Cost Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Charter Cost Model</em>'.
	 * @generated
	 */
	CharterCostModel createCharterCostModel();

	/**
	 * Returns a new object of class '<em>Base Fuel Cost</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Base Fuel Cost</em>'.
	 * @generated
	 */
	BaseFuelCost createBaseFuelCost();

	/**
	 * Returns a new object of class '<em>Spot Market Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spot Market Group</em>'.
	 * @generated
	 */
	SpotMarketGroup createSpotMarketGroup();

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
	 * Returns a new object of class '<em>DES Purchase Market</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>DES Purchase Market</em>'.
	 * @generated
	 */
	DESPurchaseMarket createDESPurchaseMarket();

	/**
	 * Returns a new object of class '<em>DES Sales Market</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>DES Sales Market</em>'.
	 * @generated
	 */
	DESSalesMarket createDESSalesMarket();

	/**
	 * Returns a new object of class '<em>FOB Purchases Market</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>FOB Purchases Market</em>'.
	 * @generated
	 */
	FOBPurchasesMarket createFOBPurchasesMarket();

	/**
	 * Returns a new object of class '<em>FOB Sales Market</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>FOB Sales Market</em>'.
	 * @generated
	 */
	FOBSalesMarket createFOBSalesMarket();

	/**
	 * Returns a new object of class '<em>Spot Availability</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spot Availability</em>'.
	 * @generated
	 */
	SpotAvailability createSpotAvailability();

	/**
	 * Returns a new object of class '<em>LNG Price Calculator Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>LNG Price Calculator Parameters</em>'.
	 * @generated
	 */
	LNGPriceCalculatorParameters createLNGPriceCalculatorParameters();

	/**
	 * Returns a new object of class '<em>LNG Fixed Price Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>LNG Fixed Price Parameters</em>'.
	 * @generated
	 */
	LNGFixedPriceParameters createLNGFixedPriceParameters();

	/**
	 * Returns a new object of class '<em>LNG Index Price Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>LNG Index Price Parameters</em>'.
	 * @generated
	 */
	LNGIndexPriceParameters createLNGIndexPriceParameters();

	/**
	 * Returns a new object of class '<em>LNG Price Expression Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>LNG Price Expression Parameters</em>'.
	 * @generated
	 */
	LNGPriceExpressionParameters createLNGPriceExpressionParameters();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	PricingPackage getPricingPackage();

} //PricingFactory
