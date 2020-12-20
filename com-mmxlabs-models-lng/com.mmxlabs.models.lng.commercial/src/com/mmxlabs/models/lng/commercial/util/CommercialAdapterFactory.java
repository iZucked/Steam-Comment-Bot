/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.util;

import com.mmxlabs.models.lng.commercial.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.BallastBonusCharterContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusCharterContract;
import com.mmxlabs.models.lng.commercial.SimpleCharterContract;
import com.mmxlabs.models.lng.commercial.SimpleEntityBook;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.commercial.VolumeParams;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage
 * @generated
 */
public class CommercialAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static CommercialPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommercialAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = CommercialPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CommercialSwitch<@Nullable Adapter> modelSwitch =
		new CommercialSwitch<@Nullable Adapter>() {
			@Override
			public Adapter caseCommercialModel(CommercialModel object) {
				return createCommercialModelAdapter();
			}
			@Override
			public Adapter caseBaseLegalEntity(BaseLegalEntity object) {
				return createBaseLegalEntityAdapter();
			}
			@Override
			public Adapter caseLegalEntity(LegalEntity object) {
				return createLegalEntityAdapter();
			}
			@Override
			public Adapter caseContract(Contract object) {
				return createContractAdapter();
			}
			@Override
			public Adapter caseSalesContract(SalesContract object) {
				return createSalesContractAdapter();
			}
			@Override
			public Adapter casePurchaseContract(PurchaseContract object) {
				return createPurchaseContractAdapter();
			}
			@Override
			public Adapter caseTaxRate(TaxRate object) {
				return createTaxRateAdapter();
			}
			@Override
			public Adapter caseLNGPriceCalculatorParameters(LNGPriceCalculatorParameters object) {
				return createLNGPriceCalculatorParametersAdapter();
			}
			@Override
			public Adapter caseExpressionPriceParameters(ExpressionPriceParameters object) {
				return createExpressionPriceParametersAdapter();
			}
			@Override
			public Adapter caseSlotContractParams(SlotContractParams object) {
				return createSlotContractParamsAdapter();
			}
			@Override
			public Adapter caseContractExpressionMapEntry(ContractExpressionMapEntry object) {
				return createContractExpressionMapEntryAdapter();
			}
			@Override
			public Adapter caseVolumeParams(VolumeParams object) {
				return createVolumeParamsAdapter();
			}
			@Override
			public Adapter caseBaseEntityBook(BaseEntityBook object) {
				return createBaseEntityBookAdapter();
			}
			@Override
			public Adapter caseSimpleEntityBook(SimpleEntityBook object) {
				return createSimpleEntityBookAdapter();
			}
			@Override
			public Adapter caseDateShiftExpressionPriceParameters(DateShiftExpressionPriceParameters object) {
				return createDateShiftExpressionPriceParametersAdapter();
			}
			@Override
			public Adapter caseBallastBonusContract(BallastBonusContract object) {
				return createBallastBonusContractAdapter();
			}
			@Override
			public Adapter caseRuleBasedBallastBonusContract(RuleBasedBallastBonusContract object) {
				return createRuleBasedBallastBonusContractAdapter();
			}
			@Override
			public Adapter caseBallastBonusContractLine(BallastBonusContractLine object) {
				return createBallastBonusContractLineAdapter();
			}
			@Override
			public Adapter caseLumpSumBallastBonusContractLine(LumpSumBallastBonusContractLine object) {
				return createLumpSumBallastBonusContractLineAdapter();
			}
			@Override
			public Adapter caseNotionalJourneyBallastBonusContractLine(NotionalJourneyBallastBonusContractLine object) {
				return createNotionalJourneyBallastBonusContractLineAdapter();
			}
			@Override
			public Adapter caseCharterContract(CharterContract object) {
				return createCharterContractAdapter();
			}
			@Override
			public Adapter caseSimpleCharterContract(SimpleCharterContract object) {
				return createSimpleCharterContractAdapter();
			}
			@Override
			public Adapter caseBallastBonusCharterContract(BallastBonusCharterContract object) {
				return createBallastBonusCharterContractAdapter();
			}
			@Override
			public Adapter caseSimpleBallastBonusCharterContract(SimpleBallastBonusCharterContract object) {
				return createSimpleBallastBonusCharterContractAdapter();
			}
			@Override
			public Adapter caseMonthlyBallastBonusContractLine(MonthlyBallastBonusContractLine object) {
				return createMonthlyBallastBonusContractLineAdapter();
			}
			@Override
			public Adapter caseMonthlyBallastBonusContract(MonthlyBallastBonusContract object) {
				return createMonthlyBallastBonusContractAdapter();
			}
			@Override
			public Adapter caseMonthlyBallastBonusCharterContract(MonthlyBallastBonusCharterContract object) {
				return createMonthlyBallastBonusCharterContractAdapter();
			}
			@Override
			public Adapter caseMMXObject(MMXObject object) {
				return createMMXObjectAdapter();
			}
			@Override
			public Adapter caseUUIDObject(UUIDObject object) {
				return createUUIDObjectAdapter();
			}
			@Override
			public Adapter caseNamedObject(NamedObject object) {
				return createNamedObjectAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.CommercialModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.CommercialModel
	 * @generated
	 */
	public Adapter createCommercialModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity <em>Base Legal Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.BaseLegalEntity
	 * @generated
	 */
	public Adapter createBaseLegalEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.LegalEntity <em>Legal Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.LegalEntity
	 * @generated
	 */
	public Adapter createLegalEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.Contract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.Contract
	 * @generated
	 */
	public Adapter createContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.SalesContract <em>Sales Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.SalesContract
	 * @generated
	 */
	public Adapter createSalesContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.PurchaseContract <em>Purchase Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.PurchaseContract
	 * @generated
	 */
	public Adapter createPurchaseContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.TaxRate <em>Tax Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.TaxRate
	 * @generated
	 */
	public Adapter createTaxRateAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters <em>LNG Price Calculator Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters
	 * @generated
	 */
	public Adapter createLNGPriceCalculatorParametersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.ExpressionPriceParameters <em>Expression Price Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.ExpressionPriceParameters
	 * @generated
	 */
	public Adapter createExpressionPriceParametersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.SlotContractParams <em>Slot Contract Params</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.SlotContractParams
	 * @generated
	 */
	public Adapter createSlotContractParamsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry <em>Contract Expression Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry
	 * @generated
	 */
	public Adapter createContractExpressionMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.VolumeParams <em>Volume Params</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.VolumeParams
	 * @generated
	 */
	public Adapter createVolumeParamsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.BaseEntityBook <em>Base Entity Book</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.BaseEntityBook
	 * @generated
	 */
	public Adapter createBaseEntityBookAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.SimpleEntityBook <em>Simple Entity Book</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.SimpleEntityBook
	 * @generated
	 */
	public Adapter createSimpleEntityBookAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters <em>Date Shift Expression Price Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters
	 * @generated
	 */
	public Adapter createDateShiftExpressionPriceParametersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.BallastBonusContract <em>Ballast Bonus Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.BallastBonusContract
	 * @generated
	 */
	public Adapter createBallastBonusContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract <em>Rule Based Ballast Bonus Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract
	 * @generated
	 */
	public Adapter createRuleBasedBallastBonusContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.BallastBonusContractLine <em>Ballast Bonus Contract Line</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.BallastBonusContractLine
	 * @generated
	 */
	public Adapter createBallastBonusContractLineAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine <em>Lump Sum Ballast Bonus Contract Line</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine
	 * @generated
	 */
	public Adapter createLumpSumBallastBonusContractLineAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine <em>Notional Journey Ballast Bonus Contract Line</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine
	 * @generated
	 */
	public Adapter createNotionalJourneyBallastBonusContractLineAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.CharterContract <em>Charter Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.CharterContract
	 * @generated
	 */
	public Adapter createCharterContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.SimpleCharterContract <em>Simple Charter Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.SimpleCharterContract
	 * @generated
	 */
	public Adapter createSimpleCharterContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.BallastBonusCharterContract <em>Ballast Bonus Charter Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.BallastBonusCharterContract
	 * @generated
	 */
	public Adapter createBallastBonusCharterContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.SimpleBallastBonusCharterContract <em>Simple Ballast Bonus Charter Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.SimpleBallastBonusCharterContract
	 * @generated
	 */
	public Adapter createSimpleBallastBonusCharterContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine <em>Monthly Ballast Bonus Contract Line</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine
	 * @generated
	 */
	public Adapter createMonthlyBallastBonusContractLineAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContract <em>Monthly Ballast Bonus Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContract
	 * @generated
	 */
	public Adapter createMonthlyBallastBonusContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusCharterContract <em>Monthly Ballast Bonus Charter Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusCharterContract
	 * @generated
	 */
	public Adapter createMonthlyBallastBonusCharterContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.MMXObject <em>MMX Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.MMXObject
	 * @generated
	 */
	public Adapter createMMXObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.UUIDObject <em>UUID Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.UUIDObject
	 * @generated
	 */
	public Adapter createUUIDObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.NamedObject
	 * @generated
	 */
	public Adapter createNamedObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //CommercialAdapterFactory
