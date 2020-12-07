/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.util;

import com.mmxlabs.models.lng.adp.*;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.jdt.annotation.Nullable;

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
 * @see com.mmxlabs.models.lng.adp.ADPPackage
 * @generated
 */
public class ADPSwitch<@Nullable T1> extends Switch<T1> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ADPPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ADPSwitch() {
		if (modelPackage == null) {
			modelPackage = ADPPackage.eINSTANCE;
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
	protected T1 doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case ADPPackage.ADP_MODEL: {
				ADPModel adpModel = (ADPModel)theEObject;
				T1 result = caseADPModel(adpModel);
				if (result == null) result = caseUUIDObject(adpModel);
				if (result == null) result = caseMMXObject(adpModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.FLEET_PROFILE: {
				FleetProfile fleetProfile = (FleetProfile)theEObject;
				T1 result = caseFleetProfile(fleetProfile);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CONTRACT_PROFILE: {
				ContractProfile<?, ?> contractProfile = (ContractProfile<?, ?>)theEObject;
				T1 result = caseContractProfile(contractProfile);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.PURCHASE_CONTRACT_PROFILE: {
				PurchaseContractProfile purchaseContractProfile = (PurchaseContractProfile)theEObject;
				T1 result = casePurchaseContractProfile(purchaseContractProfile);
				if (result == null) result = caseContractProfile(purchaseContractProfile);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.SALES_CONTRACT_PROFILE: {
				SalesContractProfile salesContractProfile = (SalesContractProfile)theEObject;
				T1 result = caseSalesContractProfile(salesContractProfile);
				if (result == null) result = caseContractProfile(salesContractProfile);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.SUB_CONTRACT_PROFILE: {
				SubContractProfile<?, ?> subContractProfile = (SubContractProfile<?, ?>)theEObject;
				T1 result = caseSubContractProfile(subContractProfile);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CUSTOM_SUB_PROFILE_ATTRIBUTES: {
				CustomSubProfileAttributes customSubProfileAttributes = (CustomSubProfileAttributes)theEObject;
				T1 result = caseCustomSubProfileAttributes(customSubProfileAttributes);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.DISTRIBUTION_MODEL: {
				DistributionModel distributionModel = (DistributionModel)theEObject;
				T1 result = caseDistributionModel(distributionModel);
				if (result == null) result = caseMMXObject(distributionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL: {
				CargoSizeDistributionModel cargoSizeDistributionModel = (CargoSizeDistributionModel)theEObject;
				T1 result = caseCargoSizeDistributionModel(cargoSizeDistributionModel);
				if (result == null) result = caseDistributionModel(cargoSizeDistributionModel);
				if (result == null) result = caseMMXObject(cargoSizeDistributionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL: {
				CargoNumberDistributionModel cargoNumberDistributionModel = (CargoNumberDistributionModel)theEObject;
				T1 result = caseCargoNumberDistributionModel(cargoNumberDistributionModel);
				if (result == null) result = caseDistributionModel(cargoNumberDistributionModel);
				if (result == null) result = caseMMXObject(cargoNumberDistributionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL: {
				CargoByQuarterDistributionModel cargoByQuarterDistributionModel = (CargoByQuarterDistributionModel)theEObject;
				T1 result = caseCargoByQuarterDistributionModel(cargoByQuarterDistributionModel);
				if (result == null) result = caseDistributionModel(cargoByQuarterDistributionModel);
				if (result == null) result = caseMMXObject(cargoByQuarterDistributionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL: {
				CargoIntervalDistributionModel cargoIntervalDistributionModel = (CargoIntervalDistributionModel)theEObject;
				T1 result = caseCargoIntervalDistributionModel(cargoIntervalDistributionModel);
				if (result == null) result = caseDistributionModel(cargoIntervalDistributionModel);
				if (result == null) result = caseMMXObject(cargoIntervalDistributionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL: {
				PreDefinedDistributionModel preDefinedDistributionModel = (PreDefinedDistributionModel)theEObject;
				T1 result = casePreDefinedDistributionModel(preDefinedDistributionModel);
				if (result == null) result = caseDistributionModel(preDefinedDistributionModel);
				if (result == null) result = caseMMXObject(preDefinedDistributionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.PRE_DEFINED_DATE: {
				PreDefinedDate preDefinedDate = (PreDefinedDate)theEObject;
				T1 result = casePreDefinedDate(preDefinedDate);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.FLOW_TYPE: {
				FlowType flowType = (FlowType)theEObject;
				T1 result = caseFlowType(flowType);
				if (result == null) result = caseSubProfileConstraint(flowType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.SUPPLY_FROM_FLOW: {
				SupplyFromFlow supplyFromFlow = (SupplyFromFlow)theEObject;
				T1 result = caseSupplyFromFlow(supplyFromFlow);
				if (result == null) result = caseFlowType(supplyFromFlow);
				if (result == null) result = caseSubProfileConstraint(supplyFromFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.DELIVER_TO_FLOW: {
				DeliverToFlow deliverToFlow = (DeliverToFlow)theEObject;
				T1 result = caseDeliverToFlow(deliverToFlow);
				if (result == null) result = caseFlowType(deliverToFlow);
				if (result == null) result = caseSubProfileConstraint(deliverToFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW: {
				SupplyFromProfileFlow supplyFromProfileFlow = (SupplyFromProfileFlow)theEObject;
				T1 result = caseSupplyFromProfileFlow(supplyFromProfileFlow);
				if (result == null) result = caseSupplyFromFlow(supplyFromProfileFlow);
				if (result == null) result = caseFlowType(supplyFromProfileFlow);
				if (result == null) result = caseSubProfileConstraint(supplyFromProfileFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.DELIVER_TO_PROFILE_FLOW: {
				DeliverToProfileFlow deliverToProfileFlow = (DeliverToProfileFlow)theEObject;
				T1 result = caseDeliverToProfileFlow(deliverToProfileFlow);
				if (result == null) result = caseDeliverToFlow(deliverToProfileFlow);
				if (result == null) result = caseFlowType(deliverToProfileFlow);
				if (result == null) result = caseSubProfileConstraint(deliverToProfileFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.SUPPLY_FROM_SPOT_FLOW: {
				SupplyFromSpotFlow supplyFromSpotFlow = (SupplyFromSpotFlow)theEObject;
				T1 result = caseSupplyFromSpotFlow(supplyFromSpotFlow);
				if (result == null) result = caseSupplyFromFlow(supplyFromSpotFlow);
				if (result == null) result = caseFlowType(supplyFromSpotFlow);
				if (result == null) result = caseSubProfileConstraint(supplyFromSpotFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.DELIVER_TO_SPOT_FLOW: {
				DeliverToSpotFlow deliverToSpotFlow = (DeliverToSpotFlow)theEObject;
				T1 result = caseDeliverToSpotFlow(deliverToSpotFlow);
				if (result == null) result = caseDeliverToFlow(deliverToSpotFlow);
				if (result == null) result = caseFlowType(deliverToSpotFlow);
				if (result == null) result = caseSubProfileConstraint(deliverToSpotFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.PROFILE_VESSEL_RESTRICTION: {
				ProfileVesselRestriction profileVesselRestriction = (ProfileVesselRestriction)theEObject;
				T1 result = caseProfileVesselRestriction(profileVesselRestriction);
				if (result == null) result = caseSubProfileConstraint(profileVesselRestriction);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.SHIPPING_OPTION: {
				ShippingOption shippingOption = (ShippingOption)theEObject;
				T1 result = caseShippingOption(shippingOption);
				if (result == null) result = caseSubProfileConstraint(shippingOption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.PROFILE_CONSTRAINT: {
				ProfileConstraint profileConstraint = (ProfileConstraint)theEObject;
				T1 result = caseProfileConstraint(profileConstraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.SUB_PROFILE_CONSTRAINT: {
				SubProfileConstraint subProfileConstraint = (SubProfileConstraint)theEObject;
				T1 result = caseSubProfileConstraint(subProfileConstraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.MIN_CARGO_CONSTRAINT: {
				MinCargoConstraint minCargoConstraint = (MinCargoConstraint)theEObject;
				T1 result = caseMinCargoConstraint(minCargoConstraint);
				if (result == null) result = caseProfileConstraint(minCargoConstraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.MAX_CARGO_CONSTRAINT: {
				MaxCargoConstraint maxCargoConstraint = (MaxCargoConstraint)theEObject;
				T1 result = caseMaxCargoConstraint(maxCargoConstraint);
				if (result == null) result = caseProfileConstraint(maxCargoConstraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT: {
				PeriodDistributionProfileConstraint periodDistributionProfileConstraint = (PeriodDistributionProfileConstraint)theEObject;
				T1 result = casePeriodDistributionProfileConstraint(periodDistributionProfileConstraint);
				if (result == null) result = caseProfileConstraint(periodDistributionProfileConstraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.PERIOD_DISTRIBUTION: {
				PeriodDistribution periodDistribution = (PeriodDistribution)theEObject;
				T1 result = casePeriodDistribution(periodDistribution);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.FLEET_CONSTRAINT: {
				FleetConstraint fleetConstraint = (FleetConstraint)theEObject;
				T1 result = caseFleetConstraint(fleetConstraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT: {
				TargetCargoesOnVesselConstraint targetCargoesOnVesselConstraint = (TargetCargoesOnVesselConstraint)theEObject;
				T1 result = caseTargetCargoesOnVesselConstraint(targetCargoesOnVesselConstraint);
				if (result == null) result = caseFleetConstraint(targetCargoesOnVesselConstraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.INVENTORY_PROFILE: {
				InventoryProfile inventoryProfile = (InventoryProfile)theEObject;
				T1 result = caseInventoryProfile(inventoryProfile);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW: {
				InventoryADPEntityRow inventoryADPEntityRow = (InventoryADPEntityRow)theEObject;
				T1 result = caseInventoryADPEntityRow(inventoryADPEntityRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.MARKET_ALLOCATION_ROW: {
				MarketAllocationRow marketAllocationRow = (MarketAllocationRow)theEObject;
				T1 result = caseMarketAllocationRow(marketAllocationRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CONTRACT_ALLOCATION_ROW: {
				ContractAllocationRow contractAllocationRow = (ContractAllocationRow)theEObject;
				T1 result = caseContractAllocationRow(contractAllocationRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.MULTIPLE_INVENTORY_PROFILE: {
				MultipleInventoryProfile multipleInventoryProfile = (MultipleInventoryProfile)theEObject;
				T1 result = caseMultipleInventoryProfile(multipleInventoryProfile);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.INVENTORY_SUBPROFILE: {
				InventorySubprofile inventorySubprofile = (InventorySubprofile)theEObject;
				T1 result = caseInventorySubprofile(inventorySubprofile);
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
	public T1 caseADPModel(ADPModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fleet Profile</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fleet Profile</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseFleetProfile(FleetProfile object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Contract Profile</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Contract Profile</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends Slot<U>, U extends Contract> T1 caseContractProfile(ContractProfile<T, U> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Distribution Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Distribution Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDistributionModel(DistributionModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Period Distribution Profile Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Period Distribution Profile Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePeriodDistributionProfileConstraint(PeriodDistributionProfileConstraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Period Distribution</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Period Distribution</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePeriodDistribution(PeriodDistribution object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cargo Size Distribution Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cargo Size Distribution Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCargoSizeDistributionModel(CargoSizeDistributionModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cargo Number Distribution Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cargo Number Distribution Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCargoNumberDistributionModel(CargoNumberDistributionModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Purchase Contract Profile</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Purchase Contract Profile</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePurchaseContractProfile(PurchaseContractProfile object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sales Contract Profile</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sales Contract Profile</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSalesContractProfile(SalesContractProfile object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sub Contract Profile</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sub Contract Profile</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends Slot<U>, U extends Contract> T1 caseSubContractProfile(SubContractProfile<T, U> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Custom Sub Profile Attributes</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Custom Sub Profile Attributes</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCustomSubProfileAttributes(CustomSubProfileAttributes object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cargo By Quarter Distribution Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cargo By Quarter Distribution Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCargoByQuarterDistributionModel(CargoByQuarterDistributionModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cargo Interval Distribution Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cargo Interval Distribution Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCargoIntervalDistributionModel(CargoIntervalDistributionModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Pre Defined Distribution Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Pre Defined Distribution Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePreDefinedDistributionModel(PreDefinedDistributionModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Pre Defined Date</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Pre Defined Date</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePreDefinedDate(PreDefinedDate object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Flow Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Flow Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseFlowType(FlowType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Supply From Flow</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Supply From Flow</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSupplyFromFlow(SupplyFromFlow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deliver To Flow</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deliver To Flow</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDeliverToFlow(DeliverToFlow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Supply From Profile Flow</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Supply From Profile Flow</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSupplyFromProfileFlow(SupplyFromProfileFlow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deliver To Profile Flow</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deliver To Profile Flow</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDeliverToProfileFlow(DeliverToProfileFlow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Supply From Spot Flow</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Supply From Spot Flow</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSupplyFromSpotFlow(SupplyFromSpotFlow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deliver To Spot Flow</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deliver To Spot Flow</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDeliverToSpotFlow(DeliverToSpotFlow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Profile Vessel Restriction</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Profile Vessel Restriction</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseProfileVesselRestriction(ProfileVesselRestriction object) {
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
	public T1 caseShippingOption(ShippingOption object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Profile Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Profile Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseProfileConstraint(ProfileConstraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sub Profile Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sub Profile Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSubProfileConstraint(SubProfileConstraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Min Cargo Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Min Cargo Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseMinCargoConstraint(MinCargoConstraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Max Cargo Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Max Cargo Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseMaxCargoConstraint(MaxCargoConstraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fleet Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fleet Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseFleetConstraint(FleetConstraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Target Cargoes On Vessel Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Target Cargoes On Vessel Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseTargetCargoesOnVesselConstraint(TargetCargoesOnVesselConstraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Inventory Profile</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Inventory Profile</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseInventoryProfile(InventoryProfile object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Inventory ADP Entity Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Inventory ADP Entity Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseInventoryADPEntityRow(InventoryADPEntityRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Market Allocation Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Market Allocation Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseMarketAllocationRow(MarketAllocationRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Contract Allocation Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Contract Allocation Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseContractAllocationRow(ContractAllocationRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Multiple Inventory Profile</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Multiple Inventory Profile</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseMultipleInventoryProfile(MultipleInventoryProfile object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Inventory Subprofile</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Inventory Subprofile</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseInventorySubprofile(InventorySubprofile object) {
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
	public T1 caseMMXObject(MMXObject object) {
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
	public T1 caseUUIDObject(UUIDObject object) {
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
	public T1 defaultCase(EObject object) {
		return null;
	}

} //ADPSwitch