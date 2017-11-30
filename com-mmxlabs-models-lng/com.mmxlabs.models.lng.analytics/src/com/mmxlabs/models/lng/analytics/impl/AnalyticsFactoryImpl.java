/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.SellOpportunity;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AnalyticsFactoryImpl extends EFactoryImpl implements AnalyticsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AnalyticsFactory init() {
		try {
			AnalyticsFactory theAnalyticsFactory = (AnalyticsFactory)EPackage.Registry.INSTANCE.getEFactory(AnalyticsPackage.eNS_URI);
			if (theAnalyticsFactory != null) {
				return theAnalyticsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AnalyticsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalyticsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case AnalyticsPackage.ANALYTICS_MODEL: return createAnalyticsModel();
			case AnalyticsPackage.BUY_OPPORTUNITY: return createBuyOpportunity();
			case AnalyticsPackage.SELL_OPPORTUNITY: return createSellOpportunity();
			case AnalyticsPackage.BUY_MARKET: return createBuyMarket();
			case AnalyticsPackage.SELL_MARKET: return createSellMarket();
			case AnalyticsPackage.BUY_REFERENCE: return createBuyReference();
			case AnalyticsPackage.SELL_REFERENCE: return createSellReference();
			case AnalyticsPackage.BASE_CASE_ROW: return createBaseCaseRow();
			case AnalyticsPackage.PARTIAL_CASE_ROW: return createPartialCaseRow();
			case AnalyticsPackage.SHIPPING_OPTION: return createShippingOption();
			case AnalyticsPackage.FLEET_SHIPPING_OPTION: return createFleetShippingOption();
			case AnalyticsPackage.OPTIONAL_AVAILABILITY_SHIPPING_OPTION: return createOptionalAvailabilityShippingOption();
			case AnalyticsPackage.ROUND_TRIP_SHIPPING_OPTION: return createRoundTripShippingOption();
			case AnalyticsPackage.NOMINATED_SHIPPING_OPTION: return createNominatedShippingOption();
			case AnalyticsPackage.ANALYSIS_RESULT_ROW: return createAnalysisResultRow();
			case AnalyticsPackage.RESULT_CONTAINER: return createResultContainer();
			case AnalyticsPackage.ANALYSIS_RESULT_DETAIL: return createAnalysisResultDetail();
			case AnalyticsPackage.PROFIT_AND_LOSS_RESULT: return createProfitAndLossResult();
			case AnalyticsPackage.BREAK_EVEN_RESULT: return createBreakEvenResult();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL: return createOptionAnalysisModel();
			case AnalyticsPackage.RESULT_SET: return createResultSet();
			case AnalyticsPackage.BASE_CASE: return createBaseCase();
			case AnalyticsPackage.PARTIAL_CASE: return createPartialCase();
			case AnalyticsPackage.EXISTING_VESSEL_AVAILABILITY: return createExistingVesselAvailability();
			case AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION: return createExistingCharterMarketOption();
			case AnalyticsPackage.ACTIONABLE_SET_PLAN: return createActionableSetPlan();
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS: return createSlotInsertionOptions();
			case AnalyticsPackage.SLOT_INSERTION_OPTION: return createSlotInsertionOption();
			case AnalyticsPackage.SOLUTION_OPTION: return createSolutionOption();
			case AnalyticsPackage.OPTIMISATION_RESULT: return createOptimisationResult();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case AnalyticsPackage.VOLUME_MODE:
				return createVolumeModeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case AnalyticsPackage.VOLUME_MODE:
				return convertVolumeModeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AnalyticsModel createAnalyticsModel() {
		AnalyticsModelImpl analyticsModel = new AnalyticsModelImpl();
		return analyticsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BuyOpportunity createBuyOpportunity() {
		BuyOpportunityImpl buyOpportunity = new BuyOpportunityImpl();
		return buyOpportunity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SellOpportunity createSellOpportunity() {
		SellOpportunityImpl sellOpportunity = new SellOpportunityImpl();
		return sellOpportunity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuyMarket createBuyMarket() {
		BuyMarketImpl buyMarket = new BuyMarketImpl();
		return buyMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SellMarket createSellMarket() {
		SellMarketImpl sellMarket = new SellMarketImpl();
		return sellMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuyReference createBuyReference() {
		BuyReferenceImpl buyReference = new BuyReferenceImpl();
		return buyReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SellReference createSellReference() {
		SellReferenceImpl sellReference = new SellReferenceImpl();
		return sellReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseCaseRow createBaseCaseRow() {
		BaseCaseRowImpl baseCaseRow = new BaseCaseRowImpl();
		return baseCaseRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PartialCaseRow createPartialCaseRow() {
		PartialCaseRowImpl partialCaseRow = new PartialCaseRowImpl();
		return partialCaseRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShippingOption createShippingOption() {
		ShippingOptionImpl shippingOption = new ShippingOptionImpl();
		return shippingOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetShippingOption createFleetShippingOption() {
		FleetShippingOptionImpl fleetShippingOption = new FleetShippingOptionImpl();
		return fleetShippingOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptionalAvailabilityShippingOption createOptionalAvailabilityShippingOption() {
		OptionalAvailabilityShippingOptionImpl optionalAvailabilityShippingOption = new OptionalAvailabilityShippingOptionImpl();
		return optionalAvailabilityShippingOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RoundTripShippingOption createRoundTripShippingOption() {
		RoundTripShippingOptionImpl roundTripShippingOption = new RoundTripShippingOptionImpl();
		return roundTripShippingOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NominatedShippingOption createNominatedShippingOption() {
		NominatedShippingOptionImpl nominatedShippingOption = new NominatedShippingOptionImpl();
		return nominatedShippingOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalysisResultRow createAnalysisResultRow() {
		AnalysisResultRowImpl analysisResultRow = new AnalysisResultRowImpl();
		return analysisResultRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultContainer createResultContainer() {
		ResultContainerImpl resultContainer = new ResultContainerImpl();
		return resultContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalysisResultDetail createAnalysisResultDetail() {
		AnalysisResultDetailImpl analysisResultDetail = new AnalysisResultDetailImpl();
		return analysisResultDetail;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossResult createProfitAndLossResult() {
		ProfitAndLossResultImpl profitAndLossResult = new ProfitAndLossResultImpl();
		return profitAndLossResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BreakEvenResult createBreakEvenResult() {
		BreakEvenResultImpl breakEvenResult = new BreakEvenResultImpl();
		return breakEvenResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptionAnalysisModel createOptionAnalysisModel() {
		OptionAnalysisModelImpl optionAnalysisModel = new OptionAnalysisModelImpl();
		return optionAnalysisModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultSet createResultSet() {
		ResultSetImpl resultSet = new ResultSetImpl();
		return resultSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseCase createBaseCase() {
		BaseCaseImpl baseCase = new BaseCaseImpl();
		return baseCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PartialCase createPartialCase() {
		PartialCaseImpl partialCase = new PartialCaseImpl();
		return partialCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExistingVesselAvailability createExistingVesselAvailability() {
		ExistingVesselAvailabilityImpl existingVesselAvailability = new ExistingVesselAvailabilityImpl();
		return existingVesselAvailability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExistingCharterMarketOption createExistingCharterMarketOption() {
		ExistingCharterMarketOptionImpl existingCharterMarketOption = new ExistingCharterMarketOptionImpl();
		return existingCharterMarketOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ActionableSetPlan createActionableSetPlan() {
		ActionableSetPlanImpl actionableSetPlan = new ActionableSetPlanImpl();
		return actionableSetPlan;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotInsertionOptions createSlotInsertionOptions() {
		SlotInsertionOptionsImpl slotInsertionOptions = new SlotInsertionOptionsImpl();
		return slotInsertionOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotInsertionOption createSlotInsertionOption() {
		SlotInsertionOptionImpl slotInsertionOption = new SlotInsertionOptionImpl();
		return slotInsertionOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SolutionOption createSolutionOption() {
		SolutionOptionImpl solutionOption = new SolutionOptionImpl();
		return solutionOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimisationResult createOptimisationResult() {
		OptimisationResultImpl optimisationResult = new OptimisationResultImpl();
		return optimisationResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VolumeMode createVolumeModeFromString(EDataType eDataType, String initialValue) {
		VolumeMode result = VolumeMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVolumeModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AnalyticsPackage getAnalyticsPackage() {
		return (AnalyticsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AnalyticsPackage getPackage() {
		return AnalyticsPackage.eINSTANCE;
	}

} //AnalyticsFactoryImpl
