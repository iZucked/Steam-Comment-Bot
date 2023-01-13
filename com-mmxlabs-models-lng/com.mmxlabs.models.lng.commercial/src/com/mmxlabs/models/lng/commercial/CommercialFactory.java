/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
	 * Returns a new object of class '<em>Volume Tier Price Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Volume Tier Price Parameters</em>'.
	 * @generated
	 */
	VolumeTierPriceParameters createVolumeTierPriceParameters();

	/**
	 * Returns a new object of class '<em>Volume Tier Slot Params</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Volume Tier Slot Params</em>'.
	 * @generated
	 */
	VolumeTierSlotParams createVolumeTierSlotParams();

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
	 * Returns a new object of class '<em>Generic Charter Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Generic Charter Contract</em>'.
	 * @generated
	 */
	GenericCharterContract createGenericCharterContract();

	/**
	 * Returns a new object of class '<em>Simple Repositioning Fee Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Simple Repositioning Fee Container</em>'.
	 * @generated
	 */
	SimpleRepositioningFeeContainer createSimpleRepositioningFeeContainer();

	/**
	 * Returns a new object of class '<em>Simple Ballast Bonus Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Simple Ballast Bonus Container</em>'.
	 * @generated
	 */
	SimpleBallastBonusContainer createSimpleBallastBonusContainer();

	/**
	 * Returns a new object of class '<em>Monthly Ballast Bonus Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Monthly Ballast Bonus Container</em>'.
	 * @generated
	 */
	MonthlyBallastBonusContainer createMonthlyBallastBonusContainer();

	/**
	 * Returns a new object of class '<em>Ballast Bonus Term</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ballast Bonus Term</em>'.
	 * @generated
	 */
	BallastBonusTerm createBallastBonusTerm();

	/**
	 * Returns a new object of class '<em>Lump Sum Ballast Bonus Term</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Lump Sum Ballast Bonus Term</em>'.
	 * @generated
	 */
	LumpSumBallastBonusTerm createLumpSumBallastBonusTerm();

	/**
	 * Returns a new object of class '<em>Notional Journey Ballast Bonus Term</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Notional Journey Ballast Bonus Term</em>'.
	 * @generated
	 */
	NotionalJourneyBallastBonusTerm createNotionalJourneyBallastBonusTerm();

	/**
	 * Returns a new object of class '<em>Monthly Ballast Bonus Term</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Monthly Ballast Bonus Term</em>'.
	 * @generated
	 */
	MonthlyBallastBonusTerm createMonthlyBallastBonusTerm();

	/**
	 * Returns a new object of class '<em>Repositioning Fee Term</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Repositioning Fee Term</em>'.
	 * @generated
	 */
	RepositioningFeeTerm createRepositioningFeeTerm();

	/**
	 * Returns a new object of class '<em>Lump Sum Repositioning Fee Term</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Lump Sum Repositioning Fee Term</em>'.
	 * @generated
	 */
	LumpSumRepositioningFeeTerm createLumpSumRepositioningFeeTerm();

	/**
	 * Returns a new object of class '<em>Origin Port Repositioning Fee Term</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Origin Port Repositioning Fee Term</em>'.
	 * @generated
	 */
	OriginPortRepositioningFeeTerm createOriginPortRepositioningFeeTerm();

	/**
	 * Returns a new object of class '<em>End Heel Options</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>End Heel Options</em>'.
	 * @generated
	 */
	EndHeelOptions createEndHeelOptions();

	/**
	 * Returns a new object of class '<em>Start Heel Options</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Start Heel Options</em>'.
	 * @generated
	 */
	StartHeelOptions createStartHeelOptions();

	/**
	 * Returns a new object of class '<em>Regas Pricing Params</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Regas Pricing Params</em>'.
	 * @generated
	 */
	RegasPricingParams createRegasPricingParams();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	CommercialPackage getCommercialPackage();

} //CommercialFactory
