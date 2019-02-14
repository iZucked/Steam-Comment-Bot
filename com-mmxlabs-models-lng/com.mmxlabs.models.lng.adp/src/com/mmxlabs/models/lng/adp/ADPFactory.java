/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.adp.ADPPackage
 * @generated
 */
public interface ADPFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ADPFactory eINSTANCE = com.mmxlabs.models.lng.adp.impl.ADPFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	ADPModel createADPModel();

	/**
	 * Returns a new object of class '<em>Fleet Profile</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fleet Profile</em>'.
	 * @generated
	 */
	FleetProfile createFleetProfile();

	/**
	 * Returns a new object of class '<em>Contract Profile</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Contract Profile</em>'.
	 * @generated
	 */
	<T extends Slot<U>, U extends Contract> ContractProfile<T, U> createContractProfile();

	/**
	 * Returns a new object of class '<em>Cargo Size Distribution Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo Size Distribution Model</em>'.
	 * @generated
	 */
	CargoSizeDistributionModel createCargoSizeDistributionModel();

	/**
	 * Returns a new object of class '<em>Cargo Number Distribution Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo Number Distribution Model</em>'.
	 * @generated
	 */
	CargoNumberDistributionModel createCargoNumberDistributionModel();

	/**
	 * Returns a new object of class '<em>Purchase Contract Profile</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Purchase Contract Profile</em>'.
	 * @generated
	 */
	PurchaseContractProfile createPurchaseContractProfile();

	/**
	 * Returns a new object of class '<em>Sales Contract Profile</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sales Contract Profile</em>'.
	 * @generated
	 */
	SalesContractProfile createSalesContractProfile();

	/**
	 * Returns a new object of class '<em>Sub Contract Profile</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sub Contract Profile</em>'.
	 * @generated
	 */
	<T extends Slot<U>, U extends Contract> SubContractProfile<T, U> createSubContractProfile();

	/**
	 * Returns a new object of class '<em>Period Distribution Profile Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Period Distribution Profile Constraint</em>'.
	 * @generated
	 */
	PeriodDistributionProfileConstraint createPeriodDistributionProfileConstraint();

	/**
	 * Returns a new object of class '<em>Period Distribution</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Period Distribution</em>'.
	 * @generated
	 */
	PeriodDistribution createPeriodDistribution();

	/**
	 * Returns a new object of class '<em>Cargo By Quarter Distribution Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo By Quarter Distribution Model</em>'.
	 * @generated
	 */
	CargoByQuarterDistributionModel createCargoByQuarterDistributionModel();

	/**
	 * Returns a new object of class '<em>Cargo Interval Distribution Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo Interval Distribution Model</em>'.
	 * @generated
	 */
	CargoIntervalDistributionModel createCargoIntervalDistributionModel();

	/**
	 * Returns a new object of class '<em>Pre Defined Distribution Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Pre Defined Distribution Model</em>'.
	 * @generated
	 */
	PreDefinedDistributionModel createPreDefinedDistributionModel();

	/**
	 * Returns a new object of class '<em>Pre Defined Date</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Pre Defined Date</em>'.
	 * @generated
	 */
	PreDefinedDate createPreDefinedDate();

	/**
	 * Returns a new object of class '<em>Flow Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Flow Type</em>'.
	 * @generated
	 */
	FlowType createFlowType();

	/**
	 * Returns a new object of class '<em>Supply From Flow</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Supply From Flow</em>'.
	 * @generated
	 */
	SupplyFromFlow createSupplyFromFlow();

	/**
	 * Returns a new object of class '<em>Deliver To Flow</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Deliver To Flow</em>'.
	 * @generated
	 */
	DeliverToFlow createDeliverToFlow();

	/**
	 * Returns a new object of class '<em>Supply From Profile Flow</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Supply From Profile Flow</em>'.
	 * @generated
	 */
	SupplyFromProfileFlow createSupplyFromProfileFlow();

	/**
	 * Returns a new object of class '<em>Deliver To Profile Flow</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Deliver To Profile Flow</em>'.
	 * @generated
	 */
	DeliverToProfileFlow createDeliverToProfileFlow();

	/**
	 * Returns a new object of class '<em>Supply From Spot Flow</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Supply From Spot Flow</em>'.
	 * @generated
	 */
	SupplyFromSpotFlow createSupplyFromSpotFlow();

	/**
	 * Returns a new object of class '<em>Deliver To Spot Flow</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Deliver To Spot Flow</em>'.
	 * @generated
	 */
	DeliverToSpotFlow createDeliverToSpotFlow();

	/**
	 * Returns a new object of class '<em>Profile Vessel Restriction</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Profile Vessel Restriction</em>'.
	 * @generated
	 */
	ProfileVesselRestriction createProfileVesselRestriction();

	/**
	 * Returns a new object of class '<em>Shipping Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Shipping Option</em>'.
	 * @generated
	 */
	ShippingOption createShippingOption();

	/**
	 * Returns a new object of class '<em>Min Cargo Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Min Cargo Constraint</em>'.
	 * @generated
	 */
	MinCargoConstraint createMinCargoConstraint();

	/**
	 * Returns a new object of class '<em>Max Cargo Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Max Cargo Constraint</em>'.
	 * @generated
	 */
	MaxCargoConstraint createMaxCargoConstraint();

	/**
	 * Returns a new object of class '<em>Target Cargoes On Vessel Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Target Cargoes On Vessel Constraint</em>'.
	 * @generated
	 */
	TargetCargoesOnVesselConstraint createTargetCargoesOnVesselConstraint();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ADPPackage getADPPackage();

} //ADPFactory
