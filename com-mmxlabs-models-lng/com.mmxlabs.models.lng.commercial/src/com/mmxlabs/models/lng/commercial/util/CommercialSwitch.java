/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.util;

import com.mmxlabs.models.lng.commercial.*;
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
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS: {
				VolumeTierPriceParameters volumeTierPriceParameters = (VolumeTierPriceParameters)theEObject;
				T result = caseVolumeTierPriceParameters(volumeTierPriceParameters);
				if (result == null) result = caseLNGPriceCalculatorParameters(volumeTierPriceParameters);
				if (result == null) result = caseUUIDObject(volumeTierPriceParameters);
				if (result == null) result = caseMMXObject(volumeTierPriceParameters);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.VOLUME_TIER_SLOT_PARAMS: {
				VolumeTierSlotParams volumeTierSlotParams = (VolumeTierSlotParams)theEObject;
				T result = caseVolumeTierSlotParams(volumeTierSlotParams);
				if (result == null) result = caseSlotContractParams(volumeTierSlotParams);
				if (result == null) result = caseUUIDObject(volumeTierSlotParams);
				if (result == null) result = caseMMXObject(volumeTierSlotParams);
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
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS: {
				DateShiftExpressionPriceParameters dateShiftExpressionPriceParameters = (DateShiftExpressionPriceParameters)theEObject;
				T result = caseDateShiftExpressionPriceParameters(dateShiftExpressionPriceParameters);
				if (result == null) result = caseLNGPriceCalculatorParameters(dateShiftExpressionPriceParameters);
				if (result == null) result = caseUUIDObject(dateShiftExpressionPriceParameters);
				if (result == null) result = caseMMXObject(dateShiftExpressionPriceParameters);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.GENERIC_CHARTER_CONTRACT: {
				GenericCharterContract genericCharterContract = (GenericCharterContract)theEObject;
				T result = caseGenericCharterContract(genericCharterContract);
				if (result == null) result = caseNamedObject(genericCharterContract);
				if (result == null) result = caseUUIDObject(genericCharterContract);
				if (result == null) result = caseMMXObject(genericCharterContract);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.IREPOSITIONING_FEE: {
				IRepositioningFee iRepositioningFee = (IRepositioningFee)theEObject;
				T result = caseIRepositioningFee(iRepositioningFee);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.SIMPLE_REPOSITIONING_FEE_CONTAINER: {
				SimpleRepositioningFeeContainer simpleRepositioningFeeContainer = (SimpleRepositioningFeeContainer)theEObject;
				T result = caseSimpleRepositioningFeeContainer(simpleRepositioningFeeContainer);
				if (result == null) result = caseIRepositioningFee(simpleRepositioningFeeContainer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.IBALLAST_BONUS: {
				IBallastBonus iBallastBonus = (IBallastBonus)theEObject;
				T result = caseIBallastBonus(iBallastBonus);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CONTAINER: {
				SimpleBallastBonusContainer simpleBallastBonusContainer = (SimpleBallastBonusContainer)theEObject;
				T result = caseSimpleBallastBonusContainer(simpleBallastBonusContainer);
				if (result == null) result = caseIBallastBonus(simpleBallastBonusContainer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.MONTHLY_BALLAST_BONUS_CONTAINER: {
				MonthlyBallastBonusContainer monthlyBallastBonusContainer = (MonthlyBallastBonusContainer)theEObject;
				T result = caseMonthlyBallastBonusContainer(monthlyBallastBonusContainer);
				if (result == null) result = caseIBallastBonus(monthlyBallastBonusContainer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.LUMP_SUM_TERM: {
				LumpSumTerm lumpSumTerm = (LumpSumTerm)theEObject;
				T result = caseLumpSumTerm(lumpSumTerm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.NOTIONAL_JOURNEY_TERM: {
				NotionalJourneyTerm notionalJourneyTerm = (NotionalJourneyTerm)theEObject;
				T result = caseNotionalJourneyTerm(notionalJourneyTerm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.BALLAST_BONUS_TERM: {
				BallastBonusTerm ballastBonusTerm = (BallastBonusTerm)theEObject;
				T result = caseBallastBonusTerm(ballastBonusTerm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.LUMP_SUM_BALLAST_BONUS_TERM: {
				LumpSumBallastBonusTerm lumpSumBallastBonusTerm = (LumpSumBallastBonusTerm)theEObject;
				T result = caseLumpSumBallastBonusTerm(lumpSumBallastBonusTerm);
				if (result == null) result = caseBallastBonusTerm(lumpSumBallastBonusTerm);
				if (result == null) result = caseLumpSumTerm(lumpSumBallastBonusTerm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM: {
				NotionalJourneyBallastBonusTerm notionalJourneyBallastBonusTerm = (NotionalJourneyBallastBonusTerm)theEObject;
				T result = caseNotionalJourneyBallastBonusTerm(notionalJourneyBallastBonusTerm);
				if (result == null) result = caseBallastBonusTerm(notionalJourneyBallastBonusTerm);
				if (result == null) result = caseNotionalJourneyTerm(notionalJourneyBallastBonusTerm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.MONTHLY_BALLAST_BONUS_TERM: {
				MonthlyBallastBonusTerm monthlyBallastBonusTerm = (MonthlyBallastBonusTerm)theEObject;
				T result = caseMonthlyBallastBonusTerm(monthlyBallastBonusTerm);
				if (result == null) result = caseNotionalJourneyBallastBonusTerm(monthlyBallastBonusTerm);
				if (result == null) result = caseBallastBonusTerm(monthlyBallastBonusTerm);
				if (result == null) result = caseNotionalJourneyTerm(monthlyBallastBonusTerm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.REPOSITIONING_FEE_TERM: {
				RepositioningFeeTerm repositioningFeeTerm = (RepositioningFeeTerm)theEObject;
				T result = caseRepositioningFeeTerm(repositioningFeeTerm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.LUMP_SUM_REPOSITIONING_FEE_TERM: {
				LumpSumRepositioningFeeTerm lumpSumRepositioningFeeTerm = (LumpSumRepositioningFeeTerm)theEObject;
				T result = caseLumpSumRepositioningFeeTerm(lumpSumRepositioningFeeTerm);
				if (result == null) result = caseRepositioningFeeTerm(lumpSumRepositioningFeeTerm);
				if (result == null) result = caseLumpSumTerm(lumpSumRepositioningFeeTerm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM: {
				OriginPortRepositioningFeeTerm originPortRepositioningFeeTerm = (OriginPortRepositioningFeeTerm)theEObject;
				T result = caseOriginPortRepositioningFeeTerm(originPortRepositioningFeeTerm);
				if (result == null) result = caseRepositioningFeeTerm(originPortRepositioningFeeTerm);
				if (result == null) result = caseNotionalJourneyTerm(originPortRepositioningFeeTerm);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.END_HEEL_OPTIONS: {
				EndHeelOptions endHeelOptions = (EndHeelOptions)theEObject;
				T result = caseEndHeelOptions(endHeelOptions);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.START_HEEL_OPTIONS: {
				StartHeelOptions startHeelOptions = (StartHeelOptions)theEObject;
				T result = caseStartHeelOptions(startHeelOptions);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.REGAS_PRICING_PARAMS: {
				RegasPricingParams regasPricingParams = (RegasPricingParams)theEObject;
				T result = caseRegasPricingParams(regasPricingParams);
				if (result == null) result = caseLNGPriceCalculatorParameters(regasPricingParams);
				if (result == null) result = caseUUIDObject(regasPricingParams);
				if (result == null) result = caseMMXObject(regasPricingParams);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.BUSINESS_UNIT: {
				BusinessUnit businessUnit = (BusinessUnit)theEObject;
				T result = caseBusinessUnit(businessUnit);
				if (result == null) result = caseNamedObject(businessUnit);
				if (result == null) result = caseMMXObject(businessUnit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CommercialPackage.PREFERRED_FORMULAE_WRAPPER: {
				PreferredFormulaeWrapper preferredFormulaeWrapper = (PreferredFormulaeWrapper)theEObject;
				T result = casePreferredFormulaeWrapper(preferredFormulaeWrapper);
				if (result == null) result = caseNamedObject(preferredFormulaeWrapper);
				if (result == null) result = caseMMXObject(preferredFormulaeWrapper);
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
	 * Returns the result of interpreting the object as an instance of '<em>Volume Tier Price Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Volume Tier Price Parameters</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVolumeTierPriceParameters(VolumeTierPriceParameters object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Volume Tier Slot Params</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Volume Tier Slot Params</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVolumeTierSlotParams(VolumeTierSlotParams object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Date Shift Expression Price Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Date Shift Expression Price Parameters</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDateShiftExpressionPriceParameters(DateShiftExpressionPriceParameters object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generic Charter Contract</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generic Charter Contract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGenericCharterContract(GenericCharterContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IRepositioning Fee</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IRepositioning Fee</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIRepositioningFee(IRepositioningFee object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Repositioning Fee Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Repositioning Fee Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimpleRepositioningFeeContainer(SimpleRepositioningFeeContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IBallast Bonus</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IBallast Bonus</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIBallastBonus(IBallastBonus object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Ballast Bonus Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Ballast Bonus Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimpleBallastBonusContainer(SimpleBallastBonusContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Monthly Ballast Bonus Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Monthly Ballast Bonus Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMonthlyBallastBonusContainer(MonthlyBallastBonusContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Lump Sum Term</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Lump Sum Term</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLumpSumTerm(LumpSumTerm object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Notional Journey Term</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Notional Journey Term</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNotionalJourneyTerm(NotionalJourneyTerm object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ballast Bonus Term</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ballast Bonus Term</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBallastBonusTerm(BallastBonusTerm object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Lump Sum Ballast Bonus Term</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Lump Sum Ballast Bonus Term</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLumpSumBallastBonusTerm(LumpSumBallastBonusTerm object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Notional Journey Ballast Bonus Term</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Notional Journey Ballast Bonus Term</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNotionalJourneyBallastBonusTerm(NotionalJourneyBallastBonusTerm object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Monthly Ballast Bonus Term</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Monthly Ballast Bonus Term</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMonthlyBallastBonusTerm(MonthlyBallastBonusTerm object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Repositioning Fee Term</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Repositioning Fee Term</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRepositioningFeeTerm(RepositioningFeeTerm object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Lump Sum Repositioning Fee Term</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Lump Sum Repositioning Fee Term</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLumpSumRepositioningFeeTerm(LumpSumRepositioningFeeTerm object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Origin Port Repositioning Fee Term</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Origin Port Repositioning Fee Term</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOriginPortRepositioningFeeTerm(OriginPortRepositioningFeeTerm object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>End Heel Options</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>End Heel Options</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEndHeelOptions(EndHeelOptions object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Start Heel Options</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Start Heel Options</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStartHeelOptions(StartHeelOptions object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Regas Pricing Params</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Regas Pricing Params</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRegasPricingParams(RegasPricingParams object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Business Unit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Business Unit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBusinessUnit(BusinessUnit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Preferred Formulae Wrapper</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Preferred Formulae Wrapper</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePreferredFormulaeWrapper(PreferredFormulaeWrapper object) {
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
