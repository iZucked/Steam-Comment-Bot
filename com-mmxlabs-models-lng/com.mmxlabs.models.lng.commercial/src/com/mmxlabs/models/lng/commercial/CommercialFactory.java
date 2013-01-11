/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage
 * @generated
 */
public interface CommercialFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CommercialFactory eINSTANCE = com.mmxlabs.models.lng.commercial.impl.CommercialFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	CommercialModel createCommercialModel();

	/**
	 * Returns a new object of class '<em>Legal Entity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Legal Entity</em>'.
	 * @generated
	 */
	LegalEntity createLegalEntity();

	/**
	 * Returns a new object of class '<em>Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Contract</em>'.
	 * @generated
	 */
	Contract createContract();

	/**
	 * Returns a new object of class '<em>Fixed Price Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fixed Price Contract</em>'.
	 * @generated
	 */
	FixedPriceContract createFixedPriceContract();

	/**
	 * Returns a new object of class '<em>Index Price Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Index Price Contract</em>'.
	 * @generated
	 */
	IndexPriceContract createIndexPriceContract();

	/**
	 * Returns a new object of class '<em>Netback Purchase Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Netback Purchase Contract</em>'.
	 * @generated
	 */
	NetbackPurchaseContract createNetbackPurchaseContract();

	/**
	 * Returns a new object of class '<em>Profit Share Purchase Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Profit Share Purchase Contract</em>'.
	 * @generated
	 */
	ProfitSharePurchaseContract createProfitSharePurchaseContract();

	/**
	 * Returns a new object of class '<em>Notional Ballast Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Notional Ballast Parameters</em>'.
	 * @generated
	 */
	NotionalBallastParameters createNotionalBallastParameters();

	/**
	 * Returns a new object of class '<em>Redirection Purchase Contract</em>'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Redirection Purchase Contract</em>'.
	 * @generated
	 */
	RedirectionPurchaseContract createRedirectionPurchaseContract();

	/**
	 * Returns a new object of class '<em>Price Expression Contract</em>'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Price Expression Contract</em>'.
	 * @generated
	 */
	PriceExpressionContract createPriceExpressionContract();

	/**
	 * Returns a new object of class '<em>Redirection Contract Original Date</em>'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Redirection Contract Original Date</em>'.
	 * @generated
	 */
	RedirectionContractOriginalDate createRedirectionContractOriginalDate();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	CommercialPackage getCommercialPackage();

} //CommercialFactory
