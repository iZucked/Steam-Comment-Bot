/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
			case AnalyticsPackage.OPEN_SELL: return createOpenSell();
			case AnalyticsPackage.OPEN_BUY: return createOpenBuy();
			case AnalyticsPackage.BUY_OPPORTUNITY: return createBuyOpportunity();
			case AnalyticsPackage.SELL_OPPORTUNITY: return createSellOpportunity();
			case AnalyticsPackage.BUY_MARKET: return createBuyMarket();
			case AnalyticsPackage.SELL_MARKET: return createSellMarket();
			case AnalyticsPackage.BUY_REFERENCE: return createBuyReference();
			case AnalyticsPackage.SELL_REFERENCE: return createSellReference();
			case AnalyticsPackage.VESSEL_EVENT_OPTION: return createVesselEventOption();
			case AnalyticsPackage.VESSEL_EVENT_REFERENCE: return createVesselEventReference();
			case AnalyticsPackage.CHARTER_OUT_OPPORTUNITY: return createCharterOutOpportunity();
			case AnalyticsPackage.BASE_CASE_ROW: return createBaseCaseRow();
			case AnalyticsPackage.PARTIAL_CASE_ROW: return createPartialCaseRow();
			case AnalyticsPackage.SHIPPING_OPTION: return createShippingOption();
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION: return createSimpleVesselCharterOption();
			case AnalyticsPackage.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION: return createOptionalSimpleVesselCharterOption();
			case AnalyticsPackage.ROUND_TRIP_SHIPPING_OPTION: return createRoundTripShippingOption();
			case AnalyticsPackage.NOMINATED_SHIPPING_OPTION: return createNominatedShippingOption();
			case AnalyticsPackage.FULL_VESSEL_CHARTER_OPTION: return createFullVesselCharterOption();
			case AnalyticsPackage.EXISTING_VESSEL_CHARTER_OPTION: return createExistingVesselCharterOption();
			case AnalyticsPackage.ANALYSIS_RESULT_DETAIL: return createAnalysisResultDetail();
			case AnalyticsPackage.PROFIT_AND_LOSS_RESULT: return createProfitAndLossResult();
			case AnalyticsPackage.BREAK_EVEN_RESULT: return createBreakEvenResult();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL: return createOptionAnalysisModel();
			case AnalyticsPackage.SANDBOX_RESULT: return createSandboxResult();
			case AnalyticsPackage.BASE_CASE: return createBaseCase();
			case AnalyticsPackage.PARTIAL_CASE: return createPartialCase();
			case AnalyticsPackage.EXISTING_CHARTER_MARKET_OPTION: return createExistingCharterMarketOption();
			case AnalyticsPackage.ACTIONABLE_SET_PLAN: return createActionableSetPlan();
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS: return createSlotInsertionOptions();
			case AnalyticsPackage.SOLUTION_OPTION: return createSolutionOption();
			case AnalyticsPackage.OPTIMISATION_RESULT: return createOptimisationResult();
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION: return createDualModeSolutionOption();
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE: return createSolutionOptionMicroCase();
			case AnalyticsPackage.CHANGE_DESCRIPTION: return createChangeDescription();
			case AnalyticsPackage.OPEN_SLOT_CHANGE: return createOpenSlotChange();
			case AnalyticsPackage.CARGO_CHANGE: return createCargoChange();
			case AnalyticsPackage.VESSEL_EVENT_CHANGE: return createVesselEventChange();
			case AnalyticsPackage.VESSEL_EVENT_DESCRIPTOR: return createVesselEventDescriptor();
			case AnalyticsPackage.REAL_SLOT_DESCRIPTOR: return createRealSlotDescriptor();
			case AnalyticsPackage.SPOT_MARKET_SLOT_DESCRIPTOR: return createSpotMarketSlotDescriptor();
			case AnalyticsPackage.VESSEL_ALLOCATION_DESCRIPTOR: return createVesselAllocationDescriptor();
			case AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR: return createMarketVesselAllocationDescriptor();
			case AnalyticsPackage.FLEET_VESSEL_ALLOCATION_DESCRIPTOR: return createFleetVesselAllocationDescriptor();
			case AnalyticsPackage.POSITION_DESCRIPTOR: return createPositionDescriptor();
			case AnalyticsPackage.VIABILITY_MODEL: return createViabilityModel();
			case AnalyticsPackage.VIABILITY_ROW: return createViabilityRow();
			case AnalyticsPackage.VIABILITY_RESULT: return createViabilityResult();
			case AnalyticsPackage.MTM_MODEL: return createMTMModel();
			case AnalyticsPackage.MTM_RESULT: return createMTMResult();
			case AnalyticsPackage.MTM_ROW: return createMTMRow();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL: return createBreakEvenAnalysisModel();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW: return createBreakEvenAnalysisRow();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET: return createBreakEvenAnalysisResultSet();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT: return createBreakEvenAnalysisResult();
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
			case AnalyticsPackage.SLOT_TYPE:
				return createSlotTypeFromString(eDataType, initialValue);
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
			case AnalyticsPackage.SLOT_TYPE:
				return convertSlotTypeToString(eDataType, instanceValue);
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
	public OpenSell createOpenSell() {
		OpenSellImpl openSell = new OpenSellImpl();
		return openSell;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OpenBuy createOpenBuy() {
		OpenBuyImpl openBuy = new OpenBuyImpl();
		return openBuy;
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
	@Override
	public BuyMarket createBuyMarket() {
		BuyMarketImpl buyMarket = new BuyMarketImpl();
		return buyMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SellMarket createSellMarket() {
		SellMarketImpl sellMarket = new SellMarketImpl();
		return sellMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BuyReference createBuyReference() {
		BuyReferenceImpl buyReference = new BuyReferenceImpl();
		return buyReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SellReference createSellReference() {
		SellReferenceImpl sellReference = new SellReferenceImpl();
		return sellReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselEventOption createVesselEventOption() {
		VesselEventOptionImpl vesselEventOption = new VesselEventOptionImpl();
		return vesselEventOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselEventReference createVesselEventReference() {
		VesselEventReferenceImpl vesselEventReference = new VesselEventReferenceImpl();
		return vesselEventReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterOutOpportunity createCharterOutOpportunity() {
		CharterOutOpportunityImpl charterOutOpportunity = new CharterOutOpportunityImpl();
		return charterOutOpportunity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseCaseRow createBaseCaseRow() {
		BaseCaseRowImpl baseCaseRow = new BaseCaseRowImpl();
		return baseCaseRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PartialCaseRow createPartialCaseRow() {
		PartialCaseRowImpl partialCaseRow = new PartialCaseRowImpl();
		return partialCaseRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ShippingOption createShippingOption() {
		ShippingOptionImpl shippingOption = new ShippingOptionImpl();
		return shippingOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimpleVesselCharterOption createSimpleVesselCharterOption() {
		SimpleVesselCharterOptionImpl simpleVesselCharterOption = new SimpleVesselCharterOptionImpl();
		return simpleVesselCharterOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OptionalSimpleVesselCharterOption createOptionalSimpleVesselCharterOption() {
		OptionalSimpleVesselCharterOptionImpl optionalSimpleVesselCharterOption = new OptionalSimpleVesselCharterOptionImpl();
		return optionalSimpleVesselCharterOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RoundTripShippingOption createRoundTripShippingOption() {
		RoundTripShippingOptionImpl roundTripShippingOption = new RoundTripShippingOptionImpl();
		return roundTripShippingOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NominatedShippingOption createNominatedShippingOption() {
		NominatedShippingOptionImpl nominatedShippingOption = new NominatedShippingOptionImpl();
		return nominatedShippingOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FullVesselCharterOption createFullVesselCharterOption() {
		FullVesselCharterOptionImpl fullVesselCharterOption = new FullVesselCharterOptionImpl();
		return fullVesselCharterOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExistingVesselCharterOption createExistingVesselCharterOption() {
		ExistingVesselCharterOptionImpl existingVesselCharterOption = new ExistingVesselCharterOptionImpl();
		return existingVesselCharterOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AnalysisResultDetail createAnalysisResultDetail() {
		AnalysisResultDetailImpl analysisResultDetail = new AnalysisResultDetailImpl();
		return analysisResultDetail;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfitAndLossResult createProfitAndLossResult() {
		ProfitAndLossResultImpl profitAndLossResult = new ProfitAndLossResultImpl();
		return profitAndLossResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BreakEvenResult createBreakEvenResult() {
		BreakEvenResultImpl breakEvenResult = new BreakEvenResultImpl();
		return breakEvenResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OptionAnalysisModel createOptionAnalysisModel() {
		OptionAnalysisModelImpl optionAnalysisModel = new OptionAnalysisModelImpl();
		return optionAnalysisModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SandboxResult createSandboxResult() {
		SandboxResultImpl sandboxResult = new SandboxResultImpl();
		return sandboxResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseCase createBaseCase() {
		BaseCaseImpl baseCase = new BaseCaseImpl();
		return baseCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PartialCase createPartialCase() {
		PartialCaseImpl partialCase = new PartialCaseImpl();
		return partialCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExistingCharterMarketOption createExistingCharterMarketOption() {
		ExistingCharterMarketOptionImpl existingCharterMarketOption = new ExistingCharterMarketOptionImpl();
		return existingCharterMarketOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ActionableSetPlan createActionableSetPlan() {
		ActionableSetPlanImpl actionableSetPlan = new ActionableSetPlanImpl();
		return actionableSetPlan;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotInsertionOptions createSlotInsertionOptions() {
		SlotInsertionOptionsImpl slotInsertionOptions = new SlotInsertionOptionsImpl();
		return slotInsertionOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeDescription createChangeDescription() {
		ChangeDescriptionImpl changeDescription = new ChangeDescriptionImpl();
		return changeDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OpenSlotChange createOpenSlotChange() {
		OpenSlotChangeImpl openSlotChange = new OpenSlotChangeImpl();
		return openSlotChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoChange createCargoChange() {
		CargoChangeImpl cargoChange = new CargoChangeImpl();
		return cargoChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselEventChange createVesselEventChange() {
		VesselEventChangeImpl vesselEventChange = new VesselEventChangeImpl();
		return vesselEventChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselEventDescriptor createVesselEventDescriptor() {
		VesselEventDescriptorImpl vesselEventDescriptor = new VesselEventDescriptorImpl();
		return vesselEventDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RealSlotDescriptor createRealSlotDescriptor() {
		RealSlotDescriptorImpl realSlotDescriptor = new RealSlotDescriptorImpl();
		return realSlotDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpotMarketSlotDescriptor createSpotMarketSlotDescriptor() {
		SpotMarketSlotDescriptorImpl spotMarketSlotDescriptor = new SpotMarketSlotDescriptorImpl();
		return spotMarketSlotDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselAllocationDescriptor createVesselAllocationDescriptor() {
		VesselAllocationDescriptorImpl vesselAllocationDescriptor = new VesselAllocationDescriptorImpl();
		return vesselAllocationDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MarketVesselAllocationDescriptor createMarketVesselAllocationDescriptor() {
		MarketVesselAllocationDescriptorImpl marketVesselAllocationDescriptor = new MarketVesselAllocationDescriptorImpl();
		return marketVesselAllocationDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FleetVesselAllocationDescriptor createFleetVesselAllocationDescriptor() {
		FleetVesselAllocationDescriptorImpl fleetVesselAllocationDescriptor = new FleetVesselAllocationDescriptorImpl();
		return fleetVesselAllocationDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PositionDescriptor createPositionDescriptor() {
		PositionDescriptorImpl positionDescriptor = new PositionDescriptorImpl();
		return positionDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ViabilityModel createViabilityModel() {
		ViabilityModelImpl viabilityModel = new ViabilityModelImpl();
		return viabilityModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ViabilityRow createViabilityRow() {
		ViabilityRowImpl viabilityRow = new ViabilityRowImpl();
		return viabilityRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ViabilityResult createViabilityResult() {
		ViabilityResultImpl viabilityResult = new ViabilityResultImpl();
		return viabilityResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MTMModel createMTMModel() {
		MTMModelImpl mtmModel = new MTMModelImpl();
		return mtmModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MTMResult createMTMResult() {
		MTMResultImpl mtmResult = new MTMResultImpl();
		return mtmResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MTMRow createMTMRow() {
		MTMRowImpl mtmRow = new MTMRowImpl();
		return mtmRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BreakEvenAnalysisModel createBreakEvenAnalysisModel() {
		BreakEvenAnalysisModelImpl breakEvenAnalysisModel = new BreakEvenAnalysisModelImpl();
		return breakEvenAnalysisModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BreakEvenAnalysisRow createBreakEvenAnalysisRow() {
		BreakEvenAnalysisRowImpl breakEvenAnalysisRow = new BreakEvenAnalysisRowImpl();
		return breakEvenAnalysisRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BreakEvenAnalysisResultSet createBreakEvenAnalysisResultSet() {
		BreakEvenAnalysisResultSetImpl breakEvenAnalysisResultSet = new BreakEvenAnalysisResultSetImpl();
		return breakEvenAnalysisResultSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BreakEvenAnalysisResult createBreakEvenAnalysisResult() {
		BreakEvenAnalysisResultImpl breakEvenAnalysisResult = new BreakEvenAnalysisResultImpl();
		return breakEvenAnalysisResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SolutionOption createSolutionOption() {
		SolutionOptionImpl solutionOption = new SolutionOptionImpl();
		return solutionOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OptimisationResult createOptimisationResult() {
		OptimisationResultImpl optimisationResult = new OptimisationResultImpl();
		return optimisationResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DualModeSolutionOption createDualModeSolutionOption() {
		DualModeSolutionOptionImpl dualModeSolutionOption = new DualModeSolutionOptionImpl();
		return dualModeSolutionOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SolutionOptionMicroCase createSolutionOptionMicroCase() {
		SolutionOptionMicroCaseImpl solutionOptionMicroCase = new SolutionOptionMicroCaseImpl();
		return solutionOptionMicroCase;
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
	public SlotType createSlotTypeFromString(EDataType eDataType, String initialValue) {
		SlotType result = SlotType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSlotTypeToString(EDataType eDataType, Object instanceValue) {
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
