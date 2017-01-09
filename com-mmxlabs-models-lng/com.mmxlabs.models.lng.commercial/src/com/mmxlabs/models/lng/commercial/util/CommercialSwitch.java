/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.SimpleEntityBook;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.commercial.VolumeParams;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

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
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage
 * @generated
 */
public class CommercialSwitch<@Nullable T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static CommercialPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommercialSwitch() {
		if (modelPackage == null) {
			modelPackage = CommercialPackage.eINSTANCE;
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
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case CommercialPackage.COMMERCIAL_MODEL: {
				CommercialModel commercialModel = (CommercialModel)theEObject;
				T result = caseCommercialModel(commercialModel);
				if (result == null) result = caseUUIDObject(commercialModel);
				if (result == null) result = caseMMXObject(commercialModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.BASE_LEGAL_ENTITY: {
				BaseLegalEntity baseLegalEntity = (BaseLegalEntity)theEObject;
				T result = caseBaseLegalEntity(baseLegalEntity);
				if (result == null) result = caseUUIDObject(baseLegalEntity);
				if (result == null) result = caseNamedObject(baseLegalEntity);
				if (result == null) result = caseMMXObject(baseLegalEntity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.LEGAL_ENTITY: {
				LegalEntity legalEntity = (LegalEntity)theEObject;
				T result = caseLegalEntity(legalEntity);
				if (result == null) result = caseBaseLegalEntity(legalEntity);
				if (result == null) result = caseUUIDObject(legalEntity);
				if (result == null) result = caseNamedObject(legalEntity);
				if (result == null) result = caseMMXObject(legalEntity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.CONTRACT: {
				Contract contract = (Contract)theEObject;
				T result = caseContract(contract);
				if (result == null) result = caseUUIDObject(contract);
				if (result == null) result = caseNamedObject(contract);
				if (result == null) result = caseMMXObject(contract);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.SALES_CONTRACT: {
				SalesContract salesContract = (SalesContract)theEObject;
				T result = caseSalesContract(salesContract);
				if (result == null) result = caseContract(salesContract);
				if (result == null) result = caseUUIDObject(salesContract);
				if (result == null) result = caseNamedObject(salesContract);
				if (result == null) result = caseMMXObject(salesContract);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.PURCHASE_CONTRACT: {
				PurchaseContract purchaseContract = (PurchaseContract)theEObject;
				T result = casePurchaseContract(purchaseContract);
				if (result == null) result = caseContract(purchaseContract);
				if (result == null) result = caseUUIDObject(purchaseContract);
				if (result == null) result = caseNamedObject(purchaseContract);
				if (result == null) result = caseMMXObject(purchaseContract);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.TAX_RATE: {
				TaxRate taxRate = (TaxRate)theEObject;
				T result = caseTaxRate(taxRate);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.LNG_PRICE_CALCULATOR_PARAMETERS: {
				LNGPriceCalculatorParameters lngPriceCalculatorParameters = (LNGPriceCalculatorParameters)theEObject;
				T result = caseLNGPriceCalculatorParameters(lngPriceCalculatorParameters);
				if (result == null) result = caseUUIDObject(lngPriceCalculatorParameters);
				if (result == null) result = caseMMXObject(lngPriceCalculatorParameters);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS: {
				ExpressionPriceParameters expressionPriceParameters = (ExpressionPriceParameters)theEObject;
				T result = caseExpressionPriceParameters(expressionPriceParameters);
				if (result == null) result = caseLNGPriceCalculatorParameters(expressionPriceParameters);
				if (result == null) result = caseUUIDObject(expressionPriceParameters);
				if (result == null) result = caseMMXObject(expressionPriceParameters);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.SLOT_CONTRACT_PARAMS: {
				SlotContractParams slotContractParams = (SlotContractParams)theEObject;
				T result = caseSlotContractParams(slotContractParams);
				if (result == null) result = caseUUIDObject(slotContractParams);
				if (result == null) result = caseMMXObject(slotContractParams);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.CONTRACT_EXPRESSION_MAP_ENTRY: {
				ContractExpressionMapEntry contractExpressionMapEntry = (ContractExpressionMapEntry)theEObject;
				T result = caseContractExpressionMapEntry(contractExpressionMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.VOLUME_PARAMS: {
				VolumeParams volumeParams = (VolumeParams)theEObject;
				T result = caseVolumeParams(volumeParams);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.BASE_ENTITY_BOOK: {
				BaseEntityBook baseEntityBook = (BaseEntityBook)theEObject;
				T result = caseBaseEntityBook(baseEntityBook);
				if (result == null) result = caseUUIDObject(baseEntityBook);
				if (result == null) result = caseMMXObject(baseEntityBook);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.SIMPLE_ENTITY_BOOK: {
				SimpleEntityBook simpleEntityBook = (SimpleEntityBook)theEObject;
				T result = caseSimpleEntityBook(simpleEntityBook);
				if (result == null) result = caseBaseEntityBook(simpleEntityBook);
				if (result == null) result = caseUUIDObject(simpleEntityBook);
				if (result == null) result = caseMMXObject(simpleEntityBook);
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
	public T caseCommercialModel(CommercialModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Base Legal Entity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Base Legal Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBaseLegalEntity(BaseLegalEntity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Legal Entity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Legal Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLegalEntity(LegalEntity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Contract</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContract(Contract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sales Contract</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sales Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSalesContract(SalesContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Purchase Contract</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Purchase Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePurchaseContract(PurchaseContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tax Rate</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tax Rate</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTaxRate(TaxRate object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>LNG Price Calculator Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>LNG Price Calculator Parameters</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLNGPriceCalculatorParameters(LNGPriceCalculatorParameters object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Expression Price Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Expression Price Parameters</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExpressionPriceParameters(ExpressionPriceParameters object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Slot Contract Params</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Slot Contract Params</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSlotContractParams(SlotContractParams object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Contract Expression Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Contract Expression Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContractExpressionMapEntry(ContractExpressionMapEntry object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Volume Params</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Volume Params</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVolumeParams(VolumeParams object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Base Entity Book</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Base Entity Book</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBaseEntityBook(BaseEntityBook object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Entity Book</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Entity Book</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimpleEntityBook(SimpleEntityBook object) {
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
	public T caseMMXObject(MMXObject object) {
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
	public T caseUUIDObject(UUIDObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedObject(NamedObject object) {
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
	public T defaultCase(EObject object) {
		return null;
	}

} //CommercialSwitch
