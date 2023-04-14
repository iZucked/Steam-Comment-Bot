/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.util;

import com.mmxlabs.models.lng.commercial.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.LumpSumTerm;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleEntityBook;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
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
	protected CommercialSwitch<Adapter> modelSwitch =
		new CommercialSwitch<Adapter>() {
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
			public Adapter caseVolumeTierPriceParameters(VolumeTierPriceParameters object) {
				return createVolumeTierPriceParametersAdapter();
			}
			@Override
			public Adapter caseVolumeTierSlotParams(VolumeTierSlotParams object) {
				return createVolumeTierSlotParamsAdapter();
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
			public Adapter caseGenericCharterContract(GenericCharterContract object) {
				return createGenericCharterContractAdapter();
			}
			@Override
			public Adapter caseIRepositioningFee(IRepositioningFee object) {
				return createIRepositioningFeeAdapter();
			}
			@Override
			public Adapter caseSimpleRepositioningFeeContainer(SimpleRepositioningFeeContainer object) {
				return createSimpleRepositioningFeeContainerAdapter();
			}
			@Override
			public Adapter caseIBallastBonus(IBallastBonus object) {
				return createIBallastBonusAdapter();
			}
			@Override
			public Adapter caseSimpleBallastBonusContainer(SimpleBallastBonusContainer object) {
				return createSimpleBallastBonusContainerAdapter();
			}
			@Override
			public Adapter caseMonthlyBallastBonusContainer(MonthlyBallastBonusContainer object) {
				return createMonthlyBallastBonusContainerAdapter();
			}
			@Override
			public Adapter caseLumpSumTerm(LumpSumTerm object) {
				return createLumpSumTermAdapter();
			}
			@Override
			public Adapter caseNotionalJourneyTerm(NotionalJourneyTerm object) {
				return createNotionalJourneyTermAdapter();
			}
			@Override
			public Adapter caseBallastBonusTerm(BallastBonusTerm object) {
				return createBallastBonusTermAdapter();
			}
			@Override
			public Adapter caseLumpSumBallastBonusTerm(LumpSumBallastBonusTerm object) {
				return createLumpSumBallastBonusTermAdapter();
			}
			@Override
			public Adapter caseNotionalJourneyBallastBonusTerm(NotionalJourneyBallastBonusTerm object) {
				return createNotionalJourneyBallastBonusTermAdapter();
			}
			@Override
			public Adapter caseMonthlyBallastBonusTerm(MonthlyBallastBonusTerm object) {
				return createMonthlyBallastBonusTermAdapter();
			}
			@Override
			public Adapter caseRepositioningFeeTerm(RepositioningFeeTerm object) {
				return createRepositioningFeeTermAdapter();
			}
			@Override
			public Adapter caseLumpSumRepositioningFeeTerm(LumpSumRepositioningFeeTerm object) {
				return createLumpSumRepositioningFeeTermAdapter();
			}
			@Override
			public Adapter caseOriginPortRepositioningFeeTerm(OriginPortRepositioningFeeTerm object) {
				return createOriginPortRepositioningFeeTermAdapter();
			}
			@Override
			public Adapter caseEndHeelOptions(EndHeelOptions object) {
				return createEndHeelOptionsAdapter();
			}
			@Override
			public Adapter caseStartHeelOptions(StartHeelOptions object) {
				return createStartHeelOptionsAdapter();
			}
			@Override
			public Adapter caseRegasPricingParams(RegasPricingParams object) {
				return createRegasPricingParamsAdapter();
			}
			@Override
			public Adapter caseBusinessUnit(BusinessUnit object) {
				return createBusinessUnitAdapter();
			}
			@Override
			public Adapter casePreferredPricingBasesWrapper(PreferredPricingBasesWrapper object) {
				return createPreferredPricingBasesWrapperAdapter();
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
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters <em>Volume Tier Price Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters
	 * @generated
	 */
	public Adapter createVolumeTierPriceParametersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.VolumeTierSlotParams <em>Volume Tier Slot Params</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.VolumeTierSlotParams
	 * @generated
	 */
	public Adapter createVolumeTierSlotParamsAdapter() {
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
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract <em>Generic Charter Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.GenericCharterContract
	 * @generated
	 */
	public Adapter createGenericCharterContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.IRepositioningFee <em>IRepositioning Fee</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.IRepositioningFee
	 * @generated
	 */
	public Adapter createIRepositioningFeeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer <em>Simple Repositioning Fee Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer
	 * @generated
	 */
	public Adapter createSimpleRepositioningFeeContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.IBallastBonus <em>IBallast Bonus</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.IBallastBonus
	 * @generated
	 */
	public Adapter createIBallastBonusAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer <em>Simple Ballast Bonus Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer
	 * @generated
	 */
	public Adapter createSimpleBallastBonusContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer <em>Monthly Ballast Bonus Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer
	 * @generated
	 */
	public Adapter createMonthlyBallastBonusContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.LumpSumTerm <em>Lump Sum Term</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.LumpSumTerm
	 * @generated
	 */
	public Adapter createLumpSumTermAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm <em>Notional Journey Term</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyTerm
	 * @generated
	 */
	public Adapter createNotionalJourneyTermAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.BallastBonusTerm <em>Ballast Bonus Term</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.BallastBonusTerm
	 * @generated
	 */
	public Adapter createBallastBonusTermAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm <em>Lump Sum Ballast Bonus Term</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm
	 * @generated
	 */
	public Adapter createLumpSumBallastBonusTermAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm <em>Notional Journey Ballast Bonus Term</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm
	 * @generated
	 */
	public Adapter createNotionalJourneyBallastBonusTermAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm <em>Monthly Ballast Bonus Term</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm
	 * @generated
	 */
	public Adapter createMonthlyBallastBonusTermAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.RepositioningFeeTerm <em>Repositioning Fee Term</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.RepositioningFeeTerm
	 * @generated
	 */
	public Adapter createRepositioningFeeTermAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm <em>Lump Sum Repositioning Fee Term</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm
	 * @generated
	 */
	public Adapter createLumpSumRepositioningFeeTermAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm <em>Origin Port Repositioning Fee Term</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm
	 * @generated
	 */
	public Adapter createOriginPortRepositioningFeeTermAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.EndHeelOptions <em>End Heel Options</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.EndHeelOptions
	 * @generated
	 */
	public Adapter createEndHeelOptionsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.StartHeelOptions <em>Start Heel Options</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.StartHeelOptions
	 * @generated
	 */
	public Adapter createStartHeelOptionsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.RegasPricingParams <em>Regas Pricing Params</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.RegasPricingParams
	 * @generated
	 */
	public Adapter createRegasPricingParamsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.BusinessUnit <em>Business Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.BusinessUnit
	 * @generated
	 */
	public Adapter createBusinessUnitAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.commercial.PreferredPricingBasesWrapper <em>Preferred Pricing Bases Wrapper</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.commercial.PreferredPricingBasesWrapper
	 * @generated
	 */
	public Adapter createPreferredPricingBasesWrapperAdapter() {
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
