/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
	 * Returns a new object of class '<em>Sales Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sales Contract</em>'.
	 * @generated
	 */
	SalesContract createSalesContract();

	/**
	 * Returns a new object of class '<em>Purchase Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Purchase Contract</em>'.
	 * @generated
	 */
	PurchaseContract createPurchaseContract();

	/**
	 * Returns a new object of class '<em>Tax Rate</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tax Rate</em>'.
	 * @generated
	 */
	TaxRate createTaxRate();

	/**
	 * Returns a new object of class '<em>Expression Price Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Expression Price Parameters</em>'.
	 * @generated
	 */
	ExpressionPriceParameters createExpressionPriceParameters();

	/**
	 * Returns a new object of class '<em>Contract Expression Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Contract Expression Map Entry</em>'.
	 * @generated
	 */
	ContractExpressionMapEntry createContractExpressionMapEntry();

	/**
	 * Returns a new object of class '<em>Simple Entity Book</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Simple Entity Book</em>'.
	 * @generated
	 */
	SimpleEntityBook createSimpleEntityBook();

	/**
	 * Returns a new object of class '<em>Date Shift Expression Price Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Date Shift Expression Price Parameters</em>'.
	 * @generated
	 */
	DateShiftExpressionPriceParameters createDateShiftExpressionPriceParameters();

	/**
	 * Returns a new object of class '<em>Rule Based Ballast Bonus Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rule Based Ballast Bonus Contract</em>'.
	 * @generated
	 */
	RuleBasedBallastBonusContract createRuleBasedBallastBonusContract();

	/**
	 * Returns a new object of class '<em>Lump Sum Ballast Bonus Contract Line</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Lump Sum Ballast Bonus Contract Line</em>'.
	 * @generated
	 */
	LumpSumBallastBonusContractLine createLumpSumBallastBonusContractLine();

	/**
	 * Returns a new object of class '<em>Notional Journey Ballast Bonus Contract Line</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Notional Journey Ballast Bonus Contract Line</em>'.
	 * @generated
	 */
	NotionalJourneyBallastBonusContractLine createNotionalJourneyBallastBonusContractLine();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	CommercialPackage getCommercialPackage();

} //CommercialFactory
