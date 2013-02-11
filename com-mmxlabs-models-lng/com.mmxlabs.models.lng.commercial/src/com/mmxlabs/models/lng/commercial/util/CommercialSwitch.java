/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.util;

import com.mmxlabs.models.lng.commercial.*;

import com.mmxlabs.models.lng.types.AContract;
import com.mmxlabs.models.lng.types.ALNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.types.ALegalEntity;

import com.mmxlabs.models.lng.types.APurchaseContract;
import com.mmxlabs.models.lng.types.ASalesContract;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

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
public class CommercialSwitch<T> extends Switch<T> {
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
	 * @parameter ePackage the package in question.
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
			case CommercialPackage.LEGAL_ENTITY: {
				LegalEntity legalEntity = (LegalEntity)theEObject;
				T result = caseLegalEntity(legalEntity);
				if (result == null) result = caseALegalEntity(legalEntity);
				if (result == null) result = caseUUIDObject(legalEntity);
				if (result == null) result = caseNamedObject(legalEntity);
				if (result == null) result = caseMMXObject(legalEntity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.CONTRACT: {
				Contract contract = (Contract)theEObject;
				T result = caseContract(contract);
				if (result == null) result = caseAContract(contract);
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
				if (result == null) result = caseASalesContract(salesContract);
				if (result == null) result = caseAContract(salesContract);
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
				if (result == null) result = caseAPurchaseContract(purchaseContract);
				if (result == null) result = caseAContract(purchaseContract);
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
				if (result == null) result = caseALNGPriceCalculatorParameters(lngPriceCalculatorParameters);
				if (result == null) result = caseUUIDObject(lngPriceCalculatorParameters);
				if (result == null) result = caseMMXObject(lngPriceCalculatorParameters);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.FIXED_PRICE_PARAMETERS: {
				FixedPriceParameters fixedPriceParameters = (FixedPriceParameters)theEObject;
				T result = caseFixedPriceParameters(fixedPriceParameters);
				if (result == null) result = caseLNGPriceCalculatorParameters(fixedPriceParameters);
				if (result == null) result = caseALNGPriceCalculatorParameters(fixedPriceParameters);
				if (result == null) result = caseUUIDObject(fixedPriceParameters);
				if (result == null) result = caseMMXObject(fixedPriceParameters);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.INDEX_PRICE_PARAMETERS: {
				IndexPriceParameters indexPriceParameters = (IndexPriceParameters)theEObject;
				T result = caseIndexPriceParameters(indexPriceParameters);
				if (result == null) result = caseLNGPriceCalculatorParameters(indexPriceParameters);
				if (result == null) result = caseALNGPriceCalculatorParameters(indexPriceParameters);
				if (result == null) result = caseUUIDObject(indexPriceParameters);
				if (result == null) result = caseMMXObject(indexPriceParameters);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS: {
				ExpressionPriceParameters expressionPriceParameters = (ExpressionPriceParameters)theEObject;
				T result = caseExpressionPriceParameters(expressionPriceParameters);
				if (result == null) result = caseLNGPriceCalculatorParameters(expressionPriceParameters);
				if (result == null) result = caseALNGPriceCalculatorParameters(expressionPriceParameters);
				if (result == null) result = caseUUIDObject(expressionPriceParameters);
				if (result == null) result = caseMMXObject(expressionPriceParameters);
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
	 * @since 3.0
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
	 * @since 3.0
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
	 * Returns the result of interpreting the object as an instance of '<em>Fixed Price Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fixed Price Parameters</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFixedPriceParameters(FixedPriceParameters object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Index Price Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Index Price Parameters</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndexPriceParameters(IndexPriceParameters object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Expression Price Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * @since 3.0
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
	 * Returns the result of interpreting the object as an instance of '<em>ALegal Entity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ALegal Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseALegalEntity(ALegalEntity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>AContract</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>AContract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAContract(AContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ASales Contract</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ASales Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseASalesContract(ASalesContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>APurchase Contract</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>APurchase Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAPurchaseContract(APurchaseContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ALNG Price Calculator Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ALNG Price Calculator Parameters</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseALNGPriceCalculatorParameters(ALNGPriceCalculatorParameters object) {
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
