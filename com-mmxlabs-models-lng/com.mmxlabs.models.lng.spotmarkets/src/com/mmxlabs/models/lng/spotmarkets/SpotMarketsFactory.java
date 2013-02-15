/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.spotmarkets;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage
 * @generated
 */
public interface SpotMarketsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SpotMarketsFactory eINSTANCE = com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	SpotMarketsModel createSpotMarketsModel();

	/**
	 * Returns a new object of class '<em>Charter Cost Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Charter Cost Model</em>'.
	 * @generated
	 */
	CharterCostModel createCharterCostModel();

	/**
	 * Returns a new object of class '<em>Spot Market Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spot Market Group</em>'.
	 * @generated
	 */
	SpotMarketGroup createSpotMarketGroup();

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
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SpotMarketsPackage getSpotMarketsPackage();

} //SpotMarketsFactory
