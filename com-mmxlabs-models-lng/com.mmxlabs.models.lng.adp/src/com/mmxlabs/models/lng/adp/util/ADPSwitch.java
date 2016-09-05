/**
 */
package com.mmxlabs.models.lng.adp.util;

import com.mmxlabs.models.lng.adp.*;

import com.mmxlabs.models.lng.cargo.Slot;
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
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CONTRACT_PROFILE: {
				ContractProfile<?> contractProfile = (ContractProfile<?>)theEObject;
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
				SubContractProfile<?> subContractProfile = (SubContractProfile<?>)theEObject;
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
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL: {
				CargoSizeDistributionModel cargoSizeDistributionModel = (CargoSizeDistributionModel)theEObject;
				T1 result = caseCargoSizeDistributionModel(cargoSizeDistributionModel);
				if (result == null) result = caseDistributionModel(cargoSizeDistributionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL: {
				CargoNumberDistributionModel cargoNumberDistributionModel = (CargoNumberDistributionModel)theEObject;
				T1 result = caseCargoNumberDistributionModel(cargoNumberDistributionModel);
				if (result == null) result = caseDistributionModel(cargoNumberDistributionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL: {
				CargoByQuarterDistributionModel cargoByQuarterDistributionModel = (CargoByQuarterDistributionModel)theEObject;
				T1 result = caseCargoByQuarterDistributionModel(cargoByQuarterDistributionModel);
				if (result == null) result = caseDistributionModel(cargoByQuarterDistributionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL: {
				CargoIntervalDistributionModel cargoIntervalDistributionModel = (CargoIntervalDistributionModel)theEObject;
				T1 result = caseCargoIntervalDistributionModel(cargoIntervalDistributionModel);
				if (result == null) result = caseDistributionModel(cargoIntervalDistributionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.BINDING_RULE: {
				BindingRule bindingRule = (BindingRule)theEObject;
				T1 result = caseBindingRule(bindingRule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.FLOW_TYPE: {
				FlowType flowType = (FlowType)theEObject;
				T1 result = caseFlowType(flowType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.SUPPLY_FROM_FLOW: {
				SupplyFromFlow supplyFromFlow = (SupplyFromFlow)theEObject;
				T1 result = caseSupplyFromFlow(supplyFromFlow);
				if (result == null) result = caseFlowType(supplyFromFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.DELIVER_TO_FLOW: {
				DeliverToFlow deliverToFlow = (DeliverToFlow)theEObject;
				T1 result = caseDeliverToFlow(deliverToFlow);
				if (result == null) result = caseFlowType(deliverToFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW: {
				SupplyFromProfileFlow supplyFromProfileFlow = (SupplyFromProfileFlow)theEObject;
				T1 result = caseSupplyFromProfileFlow(supplyFromProfileFlow);
				if (result == null) result = caseSupplyFromFlow(supplyFromProfileFlow);
				if (result == null) result = caseFlowType(supplyFromProfileFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.DELIVER_TO_PROFILE_FLOW: {
				DeliverToProfileFlow deliverToProfileFlow = (DeliverToProfileFlow)theEObject;
				T1 result = caseDeliverToProfileFlow(deliverToProfileFlow);
				if (result == null) result = caseDeliverToFlow(deliverToProfileFlow);
				if (result == null) result = caseFlowType(deliverToProfileFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.SUPPLY_FROM_SPOT_FLOW: {
				SupplyFromSpotFlow supplyFromSpotFlow = (SupplyFromSpotFlow)theEObject;
				T1 result = caseSupplyFromSpotFlow(supplyFromSpotFlow);
				if (result == null) result = caseSupplyFromFlow(supplyFromSpotFlow);
				if (result == null) result = caseFlowType(supplyFromSpotFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.DELIVER_TO_SPOT_FLOW: {
				DeliverToSpotFlow deliverToSpotFlow = (DeliverToSpotFlow)theEObject;
				T1 result = caseDeliverToSpotFlow(deliverToSpotFlow);
				if (result == null) result = caseDeliverToFlow(deliverToSpotFlow);
				if (result == null) result = caseFlowType(deliverToSpotFlow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ADPPackage.SHIPPING_OPTION: {
				ShippingOption shippingOption = (ShippingOption)theEObject;
				T1 result = caseShippingOption(shippingOption);
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
	public <T extends Slot> T1 caseContractProfile(ContractProfile<T> object) {
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
	public <T extends Slot> T1 caseSubContractProfile(SubContractProfile<T> object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Binding Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Binding Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseBindingRule(BindingRule object) {
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