/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.contract.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.contract.Contract;
import scenario.contract.ContractModel;
import scenario.contract.ContractPackage;
import scenario.contract.Entity;
import scenario.contract.FixedPricePurchaseContract;
import scenario.contract.GroupEntity;
import scenario.contract.IndexPricePurchaseContract;
import scenario.contract.NetbackPurchaseContract;
import scenario.contract.ProfitSharingPurchaseContract;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;
import scenario.contract.SimplePurchaseContract;
import scenario.contract.TotalVolumeLimit;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each
 * class of the model, starting with the actual class of the object and proceeding up the inheritance hierarchy until a non-null result is returned, which is the result of the switch. <!--
 * end-user-doc -->
 * 
 * @see scenario.contract.ContractPackage
 * @generated
 */
public class ContractSwitch<T> extends Switch<T> {
	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static ContractPackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ContractSwitch() {
		if (modelPackage == null) {
			modelPackage = ContractPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(final EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(final int classifierID, final EObject theEObject) {
		switch (classifierID) {
		case ContractPackage.CONTRACT_MODEL: {
			final ContractModel contractModel = (ContractModel) theEObject;
			T result = caseContractModel(contractModel);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractPackage.TOTAL_VOLUME_LIMIT: {
			final TotalVolumeLimit totalVolumeLimit = (TotalVolumeLimit) theEObject;
			T result = caseTotalVolumeLimit(totalVolumeLimit);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractPackage.ENTITY: {
			final Entity entity = (Entity) theEObject;
			T result = caseEntity(entity);
			if (result == null) {
				result = caseNamedObject(entity);
			}
			if (result == null) {
				result = caseScenarioObject(entity);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractPackage.CONTRACT: {
			final Contract contract = (Contract) theEObject;
			T result = caseContract(contract);
			if (result == null) {
				result = caseNamedObject(contract);
			}
			if (result == null) {
				result = caseScenarioObject(contract);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractPackage.PURCHASE_CONTRACT: {
			final PurchaseContract purchaseContract = (PurchaseContract) theEObject;
			T result = casePurchaseContract(purchaseContract);
			if (result == null) {
				result = caseContract(purchaseContract);
			}
			if (result == null) {
				result = caseNamedObject(purchaseContract);
			}
			if (result == null) {
				result = caseScenarioObject(purchaseContract);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractPackage.SALES_CONTRACT: {
			final SalesContract salesContract = (SalesContract) theEObject;
			T result = caseSalesContract(salesContract);
			if (result == null) {
				result = caseContract(salesContract);
			}
			if (result == null) {
				result = caseNamedObject(salesContract);
			}
			if (result == null) {
				result = caseScenarioObject(salesContract);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractPackage.FIXED_PRICE_PURCHASE_CONTRACT: {
			final FixedPricePurchaseContract fixedPricePurchaseContract = (FixedPricePurchaseContract) theEObject;
			T result = caseFixedPricePurchaseContract(fixedPricePurchaseContract);
			if (result == null) {
				result = caseSimplePurchaseContract(fixedPricePurchaseContract);
			}
			if (result == null) {
				result = casePurchaseContract(fixedPricePurchaseContract);
			}
			if (result == null) {
				result = caseContract(fixedPricePurchaseContract);
			}
			if (result == null) {
				result = caseNamedObject(fixedPricePurchaseContract);
			}
			if (result == null) {
				result = caseScenarioObject(fixedPricePurchaseContract);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractPackage.INDEX_PRICE_PURCHASE_CONTRACT: {
			final IndexPricePurchaseContract indexPricePurchaseContract = (IndexPricePurchaseContract) theEObject;
			T result = caseIndexPricePurchaseContract(indexPricePurchaseContract);
			if (result == null) {
				result = caseSimplePurchaseContract(indexPricePurchaseContract);
			}
			if (result == null) {
				result = casePurchaseContract(indexPricePurchaseContract);
			}
			if (result == null) {
				result = caseContract(indexPricePurchaseContract);
			}
			if (result == null) {
				result = caseNamedObject(indexPricePurchaseContract);
			}
			if (result == null) {
				result = caseScenarioObject(indexPricePurchaseContract);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractPackage.NETBACK_PURCHASE_CONTRACT: {
			final NetbackPurchaseContract netbackPurchaseContract = (NetbackPurchaseContract) theEObject;
			T result = caseNetbackPurchaseContract(netbackPurchaseContract);
			if (result == null) {
				result = casePurchaseContract(netbackPurchaseContract);
			}
			if (result == null) {
				result = caseContract(netbackPurchaseContract);
			}
			if (result == null) {
				result = caseNamedObject(netbackPurchaseContract);
			}
			if (result == null) {
				result = caseScenarioObject(netbackPurchaseContract);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT: {
			final ProfitSharingPurchaseContract profitSharingPurchaseContract = (ProfitSharingPurchaseContract) theEObject;
			T result = caseProfitSharingPurchaseContract(profitSharingPurchaseContract);
			if (result == null) {
				result = casePurchaseContract(profitSharingPurchaseContract);
			}
			if (result == null) {
				result = caseContract(profitSharingPurchaseContract);
			}
			if (result == null) {
				result = caseNamedObject(profitSharingPurchaseContract);
			}
			if (result == null) {
				result = caseScenarioObject(profitSharingPurchaseContract);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractPackage.SIMPLE_PURCHASE_CONTRACT: {
			final SimplePurchaseContract simplePurchaseContract = (SimplePurchaseContract) theEObject;
			T result = caseSimplePurchaseContract(simplePurchaseContract);
			if (result == null) {
				result = casePurchaseContract(simplePurchaseContract);
			}
			if (result == null) {
				result = caseContract(simplePurchaseContract);
			}
			if (result == null) {
				result = caseNamedObject(simplePurchaseContract);
			}
			if (result == null) {
				result = caseScenarioObject(simplePurchaseContract);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case ContractPackage.GROUP_ENTITY: {
			final GroupEntity groupEntity = (GroupEntity) theEObject;
			T result = caseGroupEntity(groupEntity);
			if (result == null) {
				result = caseEntity(groupEntity);
			}
			if (result == null) {
				result = caseNamedObject(groupEntity);
			}
			if (result == null) {
				result = caseScenarioObject(groupEntity);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContractModel(final ContractModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Purchase Contract</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Purchase Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePurchaseContract(final PurchaseContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sales Contract</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sales Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSalesContract(final SalesContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Total Volume Limit</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Total Volume Limit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTotalVolumeLimit(final TotalVolumeLimit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntity(final Entity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fixed Price Purchase Contract</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result
	 * will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fixed Price Purchase Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFixedPricePurchaseContract(final FixedPricePurchaseContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Index Price Purchase Contract</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result
	 * will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Index Price Purchase Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndexPricePurchaseContract(final IndexPricePurchaseContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Netback Purchase Contract</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Netback Purchase Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNetbackPurchaseContract(final NetbackPurchaseContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Profit Sharing Purchase Contract</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
	 * result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Profit Sharing Purchase Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProfitSharingPurchaseContract(final ProfitSharingPurchaseContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Purchase Contract</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Purchase Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimplePurchaseContract(final SimplePurchaseContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Group Entity</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Group Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGroupEntity(final GroupEntity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Contract</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContract(final Contract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Object</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScenarioObject(final ScenarioObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Object</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedObject(final NamedObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch, but this is the last case anyway. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(final EObject object) {
		return null;
	}

} // ContractSwitch
