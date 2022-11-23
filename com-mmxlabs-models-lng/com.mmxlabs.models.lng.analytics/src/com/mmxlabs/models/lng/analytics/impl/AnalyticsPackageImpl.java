/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AbstractSensitivityResult;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.AnalysisResultDetail;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BaseCaseRowOptions;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.CargoChange;
import com.mmxlabs.models.lng.analytics.CargoPnLResult;
import com.mmxlabs.models.lng.analytics.Change;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.CharterOutOpportunity;
import com.mmxlabs.models.lng.analytics.CommodityCurveOption;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;
import com.mmxlabs.models.lng.analytics.DualModeSolutionOption;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.MTMResult;
import com.mmxlabs.models.lng.analytics.MTMRow;
import com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
import com.mmxlabs.models.lng.analytics.LocalDateTimeHolder;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OpenBuy;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.OpenSlotChange;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRowOptions;
import com.mmxlabs.models.lng.analytics.PortfolioSensitivityResult;
import com.mmxlabs.models.lng.analytics.PositionDescriptor;
import com.mmxlabs.models.lng.analytics.ProfitAndLossResult;
import com.mmxlabs.models.lng.analytics.RealSlotDescriptor;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SandboxResult;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SensitivityModel;
import com.mmxlabs.models.lng.analytics.SensitivitySolutionSet;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SlotDescriptor;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.SlotType;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase;
import com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResult;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet;
import com.mmxlabs.models.lng.analytics.VesselAllocationDescriptor;
import com.mmxlabs.models.lng.analytics.VesselEventChange;
import com.mmxlabs.models.lng.analytics.VesselEventDescriptor;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.VesselEventReference;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
import com.mmxlabs.models.lng.analytics.ViabilityResult;
import com.mmxlabs.models.lng.analytics.ViabilityRow;
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AnalyticsPackageImpl extends EPackageImpl implements AnalyticsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass analyticsModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass buyOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sellOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass openSellEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass openBuyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass buyOpportunityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sellOpportunityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass buyMarketEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sellMarketEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass buyReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sellReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselEventOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselEventReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass charterOutOpportunityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass baseCaseRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass baseCaseRowOptionsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass partialCaseRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass partialCaseRowOptionsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass shippingOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass simpleVesselCharterOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass optionalSimpleVesselCharterOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass roundTripShippingOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nominatedShippingOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fullVesselCharterOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass existingVesselCharterOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass analysisResultDetailEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profitAndLossResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass breakEvenResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractAnalysisModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass optionAnalysisModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sandboxResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass baseCaseEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass partialCaseEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass existingCharterMarketOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractSolutionSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass actionableSetPlanEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotInsertionOptionsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeDescriptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass openSlotChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselEventChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselEventDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass realSlotDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spotMarketSlotDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselAllocationDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass marketVesselAllocationDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fleetVesselAllocationDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass positionDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass viabilityModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass viabilityRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass viabilityResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mtmModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mtmResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mtmRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass breakEvenAnalysisModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass breakEvenAnalysisRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass breakEvenAnalysisResultSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass breakEvenAnalysisResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass localDateTimeHolderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass commodityCurveOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass commodityCurveOverlayEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sensitivityModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sensitivitySolutionSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractSensitivityResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portfolioSensitivityResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoPnLResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass swapValueMatrixModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass swapValueMatrixResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass swapValueMatrixResultSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass solutionOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass optimisationResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dualModeSolutionOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass solutionOptionMicroCaseEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum volumeModeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum slotTypeEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AnalyticsPackageImpl() {
		super(eNS_URI, AnalyticsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link AnalyticsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AnalyticsPackage init() {
		if (isInited) return (AnalyticsPackage)EPackage.Registry.INSTANCE.getEPackage(AnalyticsPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredAnalyticsPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		AnalyticsPackageImpl theAnalyticsPackage = registeredAnalyticsPackage instanceof AnalyticsPackageImpl ? (AnalyticsPackageImpl)registeredAnalyticsPackage : new AnalyticsPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		CargoPackage.eINSTANCE.eClass();
		CommercialPackage.eINSTANCE.eClass();
		DateTimePackage.eINSTANCE.eClass();
		FleetPackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();
		ParametersPackage.eINSTANCE.eClass();
		PortPackage.eINSTANCE.eClass();
		PricingPackage.eINSTANCE.eClass();
		SchedulePackage.eINSTANCE.eClass();
		SpotMarketsPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theAnalyticsPackage.createPackageContents();

		// Initialize created meta-data
		theAnalyticsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAnalyticsPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(AnalyticsPackage.eNS_URI, theAnalyticsPackage);
		return theAnalyticsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAnalyticsModel() {
		return analyticsModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAnalyticsModel_OptionModels() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAnalyticsModel_Optimisations() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAnalyticsModel_ViabilityModel() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAnalyticsModel_MtmModel() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAnalyticsModel_BreakevenModels() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAnalyticsModel_SwapValueMatrixModels() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBuyOption() {
		return buyOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSellOption() {
		return sellOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOpenSell() {
		return openSellEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOpenBuy() {
		return openBuyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBuyOpportunity() {
		return buyOpportunityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_DesPurchase() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBuyOpportunity_Port() {
		return (EReference)buyOpportunityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBuyOpportunity_Contract() {
		return (EReference)buyOpportunityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_Date() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_PriceExpression() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBuyOpportunity_Entity() {
		return (EReference)buyOpportunityEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_Cv() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_CancellationExpression() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_MiscCosts() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_VolumeMode() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_VolumeUnits() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_MinVolume() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_MaxVolume() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_SpecifyWindow() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_WindowSize() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_WindowSizeUnits() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_Name() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSellOpportunity() {
		return sellOpportunityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_FobSale() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSellOpportunity_Port() {
		return (EReference)sellOpportunityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSellOpportunity_Contract() {
		return (EReference)sellOpportunityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_Date() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_PriceExpression() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSellOpportunity_Entity() {
		return (EReference)sellOpportunityEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_CancellationExpression() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_MiscCosts() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_VolumeMode() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_VolumeUnits() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_MinVolume() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_MaxVolume() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_SpecifyWindow() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_WindowSize() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_WindowSizeUnits() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_Name() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBuyMarket() {
		return buyMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBuyMarket_Market() {
		return (EReference)buyMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyMarket_Month() {
		return (EAttribute)buyMarketEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSellMarket() {
		return sellMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSellMarket_Market() {
		return (EReference)sellMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellMarket_Month() {
		return (EAttribute)sellMarketEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBuyReference() {
		return buyReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBuyReference_Slot() {
		return (EReference)buyReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSellReference() {
		return sellReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSellReference_Slot() {
		return (EReference)sellReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselEventOption() {
		return vesselEventOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselEventReference() {
		return vesselEventReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselEventReference_Event() {
		return (EReference)vesselEventReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCharterOutOpportunity() {
		return charterOutOpportunityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutOpportunity_HireCost() {
		return (EAttribute)charterOutOpportunityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterOutOpportunity_Port() {
		return (EReference)charterOutOpportunityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutOpportunity_Date() {
		return (EAttribute)charterOutOpportunityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutOpportunity_Duration() {
		return (EAttribute)charterOutOpportunityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBaseCaseRow() {
		return baseCaseRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBaseCaseRow_BuyOption() {
		return (EReference)baseCaseRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBaseCaseRow_SellOption() {
		return (EReference)baseCaseRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBaseCaseRow_VesselEventOption() {
		return (EReference)baseCaseRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBaseCaseRow_Shipping() {
		return (EReference)baseCaseRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseCaseRow_Optionise() {
		return (EAttribute)baseCaseRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBaseCaseRow_Options() {
		return (EReference)baseCaseRowEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseCaseRow_Freeze() {
		return (EAttribute)baseCaseRowEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBaseCaseRowOptions() {
		return baseCaseRowOptionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseCaseRowOptions_LadenRoute() {
		return (EAttribute)baseCaseRowOptionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseCaseRowOptions_BallastRoute() {
		return (EAttribute)baseCaseRowOptionsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseCaseRowOptions_LadenFuelChoice() {
		return (EAttribute)baseCaseRowOptionsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseCaseRowOptions_BallastFuelChoice() {
		return (EAttribute)baseCaseRowOptionsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseCaseRowOptions_LoadDate() {
		return (EAttribute)baseCaseRowOptionsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseCaseRowOptions_DischargeDate() {
		return (EAttribute)baseCaseRowOptionsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPartialCaseRow() {
		return partialCaseRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPartialCaseRow_BuyOptions() {
		return (EReference)partialCaseRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPartialCaseRow_SellOptions() {
		return (EReference)partialCaseRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPartialCaseRow_VesselEventOptions() {
		return (EReference)partialCaseRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPartialCaseRow_Shipping() {
		return (EReference)partialCaseRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPartialCaseRow_Options() {
		return (EReference)partialCaseRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPartialCaseRow_CommodityCurveOptions() {
		return (EReference)partialCaseRowEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPartialCaseRowOptions() {
		return partialCaseRowOptionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPartialCaseRowOptions_LadenRoutes() {
		return (EAttribute)partialCaseRowOptionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPartialCaseRowOptions_BallastRoutes() {
		return (EAttribute)partialCaseRowOptionsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPartialCaseRowOptions_LadenFuelChoices() {
		return (EAttribute)partialCaseRowOptionsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPartialCaseRowOptions_BallastFuelChoices() {
		return (EAttribute)partialCaseRowOptionsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPartialCaseRowOptions_LoadDates() {
		return (EReference)partialCaseRowOptionsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPartialCaseRowOptions_DischargeDates() {
		return (EReference)partialCaseRowOptionsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getShippingOption() {
		return shippingOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSimpleVesselCharterOption() {
		return simpleVesselCharterOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSimpleVesselCharterOption_Vessel() {
		return (EReference)simpleVesselCharterOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSimpleVesselCharterOption_HireCost() {
		return (EAttribute)simpleVesselCharterOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSimpleVesselCharterOption_Entity() {
		return (EReference)simpleVesselCharterOptionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSimpleVesselCharterOption_UseSafetyHeel() {
		return (EAttribute)simpleVesselCharterOptionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOptionalSimpleVesselCharterOption() {
		return optionalSimpleVesselCharterOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptionalSimpleVesselCharterOption_BallastBonus() {
		return (EAttribute)optionalSimpleVesselCharterOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptionalSimpleVesselCharterOption_RepositioningFee() {
		return (EAttribute)optionalSimpleVesselCharterOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptionalSimpleVesselCharterOption_Start() {
		return (EAttribute)optionalSimpleVesselCharterOptionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptionalSimpleVesselCharterOption_End() {
		return (EAttribute)optionalSimpleVesselCharterOptionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptionalSimpleVesselCharterOption_StartPort() {
		return (EReference)optionalSimpleVesselCharterOptionEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptionalSimpleVesselCharterOption_EndPort() {
		return (EReference)optionalSimpleVesselCharterOptionEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRoundTripShippingOption() {
		return roundTripShippingOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRoundTripShippingOption_Vessel() {
		return (EReference)roundTripShippingOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRoundTripShippingOption_HireCost() {
		return (EAttribute)roundTripShippingOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRoundTripShippingOption_Entity() {
		return (EReference)roundTripShippingOptionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNominatedShippingOption() {
		return nominatedShippingOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNominatedShippingOption_NominatedVessel() {
		return (EReference)nominatedShippingOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFullVesselCharterOption() {
		return fullVesselCharterOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFullVesselCharterOption_VesselCharter() {
		return (EReference)fullVesselCharterOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getExistingVesselCharterOption() {
		return existingVesselCharterOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getExistingVesselCharterOption_VesselCharter() {
		return (EReference)existingVesselCharterOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAnalysisResultDetail() {
		return analysisResultDetailEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProfitAndLossResult() {
		return profitAndLossResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfitAndLossResult_Value() {
		return (EAttribute)profitAndLossResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBreakEvenResult() {
		return breakEvenResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBreakEvenResult_Price() {
		return (EAttribute)breakEvenResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBreakEvenResult_PriceString() {
		return (EAttribute)breakEvenResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBreakEvenResult_CargoPNL() {
		return (EAttribute)breakEvenResultEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAbstractAnalysisModel() {
		return abstractAnalysisModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractAnalysisModel_Buys() {
		return (EReference)abstractAnalysisModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractAnalysisModel_Sells() {
		return (EReference)abstractAnalysisModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractAnalysisModel_VesselEvents() {
		return (EReference)abstractAnalysisModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractAnalysisModel_ShippingTemplates() {
		return (EReference)abstractAnalysisModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractAnalysisModel_CommodityCurves() {
		return (EReference)abstractAnalysisModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOptionAnalysisModel() {
		return optionAnalysisModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptionAnalysisModel_BaseCase() {
		return (EReference)optionAnalysisModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBaseCase() {
		return baseCaseEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBaseCase_BaseCase() {
		return (EReference)baseCaseEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseCase_ProfitAndLoss() {
		return (EAttribute)baseCaseEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseCase_KeepExistingScenario() {
		return (EAttribute)baseCaseEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPartialCase() {
		return partialCaseEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPartialCase_PartialCase() {
		return (EReference)partialCaseEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPartialCase_KeepExistingScenario() {
		return (EAttribute)partialCaseEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getExistingCharterMarketOption() {
		return existingCharterMarketOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getExistingCharterMarketOption_CharterInMarket() {
		return (EReference)existingCharterMarketOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getExistingCharterMarketOption_SpotIndex() {
		return (EAttribute)existingCharterMarketOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAbstractSolutionSet() {
		return abstractSolutionSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractSolutionSet_HasDualModeSolutions() {
		return (EAttribute)abstractSolutionSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractSolutionSet_PortfolioBreakEvenMode() {
		return (EAttribute)abstractSolutionSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractSolutionSet_UserSettings() {
		return (EReference)abstractSolutionSetEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractSolutionSet_Options() {
		return (EReference)abstractSolutionSetEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractSolutionSet_ExtraVesselEvents() {
		return (EReference)abstractSolutionSetEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractSolutionSet_ExtraVesselCharters() {
		return (EReference)abstractSolutionSetEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractSolutionSet_CharterInMarketOverrides() {
		return (EReference)abstractSolutionSetEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractSolutionSet_ExtraCharterInMarkets() {
		return (EReference)abstractSolutionSetEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractSolutionSet_UseScenarioBase() {
		return (EAttribute)abstractSolutionSetEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractSolutionSet_ExtraSlots() {
		return (EReference)abstractSolutionSetEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAbstractSolutionSet_BaseOption() {
		return (EReference)abstractSolutionSetEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getActionableSetPlan() {
		return actionableSetPlanEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSlotInsertionOptions() {
		return slotInsertionOptionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlotInsertionOptions_SlotsInserted() {
		return (EReference)slotInsertionOptionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlotInsertionOptions_EventsInserted() {
		return (EReference)slotInsertionOptionsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChangeDescription() {
		return changeDescriptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeDescription_Changes() {
		return (EReference)changeDescriptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChange() {
		return changeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOpenSlotChange() {
		return openSlotChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOpenSlotChange_SlotDescriptor() {
		return (EReference)openSlotChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoChange() {
		return cargoChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoChange_SlotDescriptors() {
		return (EReference)cargoChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoChange_VesselAllocation() {
		return (EReference)cargoChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoChange_Position() {
		return (EReference)cargoChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselEventChange() {
		return vesselEventChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselEventChange_VesselEventDescriptor() {
		return (EReference)vesselEventChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselEventChange_VesselAllocation() {
		return (EReference)vesselEventChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselEventChange_Position() {
		return (EReference)vesselEventChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselEventDescriptor() {
		return vesselEventDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselEventDescriptor_EventName() {
		return (EAttribute)vesselEventDescriptorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSlotDescriptor() {
		return slotDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotDescriptor_SlotType() {
		return (EAttribute)slotDescriptorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRealSlotDescriptor() {
		return realSlotDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRealSlotDescriptor_SlotName() {
		return (EAttribute)realSlotDescriptorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSpotMarketSlotDescriptor() {
		return spotMarketSlotDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarketSlotDescriptor_Date() {
		return (EAttribute)spotMarketSlotDescriptorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarketSlotDescriptor_MarketName() {
		return (EAttribute)spotMarketSlotDescriptorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarketSlotDescriptor_PortName() {
		return (EAttribute)spotMarketSlotDescriptorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselAllocationDescriptor() {
		return vesselAllocationDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMarketVesselAllocationDescriptor() {
		return marketVesselAllocationDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMarketVesselAllocationDescriptor_MarketName() {
		return (EAttribute)marketVesselAllocationDescriptorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMarketVesselAllocationDescriptor_SpotIndex() {
		return (EAttribute)marketVesselAllocationDescriptorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFleetVesselAllocationDescriptor() {
		return fleetVesselAllocationDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFleetVesselAllocationDescriptor_Name() {
		return (EAttribute)fleetVesselAllocationDescriptorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFleetVesselAllocationDescriptor_CharterIndex() {
		return (EAttribute)fleetVesselAllocationDescriptorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPositionDescriptor() {
		return positionDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPositionDescriptor_After() {
		return (EAttribute)positionDescriptorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPositionDescriptor_Before() {
		return (EAttribute)positionDescriptorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getViabilityModel() {
		return viabilityModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getViabilityModel_Rows() {
		return (EReference)viabilityModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getViabilityModel_Markets() {
		return (EReference)viabilityModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getViabilityRow() {
		return viabilityRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getViabilityRow_BuyOption() {
		return (EReference)viabilityRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getViabilityRow_SellOption() {
		return (EReference)viabilityRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getViabilityRow_Shipping() {
		return (EReference)viabilityRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getViabilityRow_LhsResults() {
		return (EReference)viabilityRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getViabilityRow_RhsResults() {
		return (EReference)viabilityRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getViabilityRow_Target() {
		return (EReference)viabilityRowEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViabilityRow_Price() {
		return (EAttribute)viabilityRowEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViabilityRow_Eta() {
		return (EAttribute)viabilityRowEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViabilityRow_ReferencePrice() {
		return (EAttribute)viabilityRowEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViabilityRow_StartVolume() {
		return (EAttribute)viabilityRowEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getViabilityResult() {
		return viabilityResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getViabilityResult_Target() {
		return (EReference)viabilityResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViabilityResult_EarliestETA() {
		return (EAttribute)viabilityResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViabilityResult_LatestETA() {
		return (EAttribute)viabilityResultEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViabilityResult_EarliestVolume() {
		return (EAttribute)viabilityResultEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViabilityResult_LatestVolume() {
		return (EAttribute)viabilityResultEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViabilityResult_EarliestPrice() {
		return (EAttribute)viabilityResultEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViabilityResult_LatestPrice() {
		return (EAttribute)viabilityResultEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMTMModel() {
		return mtmModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMTMModel_Rows() {
		return (EReference)mtmModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMTMModel_Markets() {
		return (EReference)mtmModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMTMModel_NominalMarkets() {
		return (EReference)mtmModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMTMResult() {
		return mtmResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMTMResult_Target() {
		return (EReference)mtmResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMTMResult_EarliestETA() {
		return (EAttribute)mtmResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMTMResult_EarliestVolume() {
		return (EAttribute)mtmResultEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMTMResult_EarliestPrice() {
		return (EAttribute)mtmResultEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMTMResult_Shipping() {
		return (EReference)mtmResultEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMTMResult_ShippingCost() {
		return (EAttribute)mtmResultEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMTMRow() {
		return mtmRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMTMRow_BuyOption() {
		return (EReference)mtmRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMTMRow_SellOption() {
		return (EReference)mtmRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMTMRow_LhsResults() {
		return (EReference)mtmRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMTMRow_RhsResults() {
		return (EReference)mtmRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMTMRow_Target() {
		return (EReference)mtmRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMTMRow_Price() {
		return (EAttribute)mtmRowEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMTMRow_Eta() {
		return (EAttribute)mtmRowEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMTMRow_ReferencePrice() {
		return (EAttribute)mtmRowEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMTMRow_StartVolume() {
		return (EAttribute)mtmRowEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBreakEvenAnalysisModel() {
		return breakEvenAnalysisModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisModel_Rows() {
		return (EReference)breakEvenAnalysisModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisModel_Markets() {
		return (EReference)breakEvenAnalysisModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBreakEvenAnalysisRow() {
		return breakEvenAnalysisRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisRow_BuyOption() {
		return (EReference)breakEvenAnalysisRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisRow_SellOption() {
		return (EReference)breakEvenAnalysisRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisRow_Shipping() {
		return (EReference)breakEvenAnalysisRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisRow_LhsResults() {
		return (EReference)breakEvenAnalysisRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisRow_RhsResults() {
		return (EReference)breakEvenAnalysisRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisRow_LhsBasedOn() {
		return (EReference)breakEvenAnalysisRowEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisRow_RhsBasedOn() {
		return (EReference)breakEvenAnalysisRowEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBreakEvenAnalysisResultSet() {
		return breakEvenAnalysisResultSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisResultSet_BasedOn() {
		return (EReference)breakEvenAnalysisResultSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisResultSet_Results() {
		return (EReference)breakEvenAnalysisResultSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBreakEvenAnalysisResultSet_Price() {
		return (EAttribute)breakEvenAnalysisResultSetEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBreakEvenAnalysisResult() {
		return breakEvenAnalysisResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBreakEvenAnalysisResult_Target() {
		return (EReference)breakEvenAnalysisResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBreakEvenAnalysisResult_Price() {
		return (EAttribute)breakEvenAnalysisResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBreakEvenAnalysisResult_Eta() {
		return (EAttribute)breakEvenAnalysisResultEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBreakEvenAnalysisResult_ReferencePrice() {
		return (EAttribute)breakEvenAnalysisResultEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLocalDateTimeHolder() {
		return localDateTimeHolderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLocalDateTimeHolder_DateTime() {
		return (EAttribute)localDateTimeHolderEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCommodityCurveOption() {
		return commodityCurveOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCommodityCurveOverlay() {
		return commodityCurveOverlayEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCommodityCurveOverlay_ReferenceCurve() {
		return (EReference)commodityCurveOverlayEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCommodityCurveOverlay_AlternativeCurves() {
		return (EReference)commodityCurveOverlayEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSensitivityModel() {
		return sensitivityModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSensitivityModel_SensitivityModel() {
		return (EReference)sensitivityModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSensitivityModel_SensitivitySolution() {
		return (EReference)sensitivityModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSensitivitySolutionSet() {
		return sensitivitySolutionSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSensitivitySolutionSet_PorfolioPnLResult() {
		return (EReference)sensitivitySolutionSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSensitivitySolutionSet_CargoPnLResults() {
		return (EReference)sensitivitySolutionSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAbstractSensitivityResult() {
		return abstractSensitivityResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractSensitivityResult_MinPnL() {
		return (EAttribute)abstractSensitivityResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractSensitivityResult_MaxPnL() {
		return (EAttribute)abstractSensitivityResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractSensitivityResult_AveragePnL() {
		return (EAttribute)abstractSensitivityResultEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractSensitivityResult_Variance() {
		return (EAttribute)abstractSensitivityResultEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPortfolioSensitivityResult() {
		return portfolioSensitivityResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoPnLResult() {
		return cargoPnLResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoPnLResult_Cargo() {
		return (EReference)cargoPnLResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSwapValueMatrixModel() {
		return swapValueMatrixModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSwapValueMatrixModel_BaseLoad() {
		return (EReference)swapValueMatrixModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSwapValueMatrixModel_BaseDischarge() {
		return (EReference)swapValueMatrixModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSwapValueMatrixModel_BaseVesselCharter() {
		return (EReference)swapValueMatrixModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSwapValueMatrixModel_SwapLoadMarket() {
		return (EReference)swapValueMatrixModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSwapValueMatrixModel_SwapDischargeMarket() {
		return (EReference)swapValueMatrixModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixModel_BaseDischargeMinPrice() {
		return (EAttribute)swapValueMatrixModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixModel_BaseDischargeMaxPrice() {
		return (EAttribute)swapValueMatrixModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixModel_BaseDischargeStepSize() {
		return (EAttribute)swapValueMatrixModelEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixModel_MarketMinPrice() {
		return (EAttribute)swapValueMatrixModelEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixModel_MarketMaxPrice() {
		return (EAttribute)swapValueMatrixModelEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixModel_MarketStepSize() {
		return (EAttribute)swapValueMatrixModelEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixModel_SwapFee() {
		return (EAttribute)swapValueMatrixModelEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSwapValueMatrixModel_SwapValueMatrixResult() {
		return (EReference)swapValueMatrixModelEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSwapValueMatrixResult() {
		return swapValueMatrixResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixResult_BaseDischargePrice() {
		return (EAttribute)swapValueMatrixResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixResult_SwapMarketPrice() {
		return (EAttribute)swapValueMatrixResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixResult_SwapPnlMinusBasePnl() {
		return (EAttribute)swapValueMatrixResultEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixResult_BaseLoadPrice() {
		return (EAttribute)swapValueMatrixResultEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixResult_SwapFobLoadPrice() {
		return (EAttribute)swapValueMatrixResultEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixResult_BaseFobLoadVolume() {
		return (EAttribute)swapValueMatrixResultEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixResult_SwapFobLoadVolume() {
		return (EAttribute)swapValueMatrixResultEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixResult_MarketBuyVolume() {
		return (EAttribute)swapValueMatrixResultEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixResult_MarketSellVolume() {
		return (EAttribute)swapValueMatrixResultEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixResult_BaseDesSellVolume() {
		return (EAttribute)swapValueMatrixResultEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSwapValueMatrixResultSet() {
		return swapValueMatrixResultSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSwapValueMatrixResultSet_Results() {
		return (EReference)swapValueMatrixResultSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSwapValueMatrixResultSet_SwapFee() {
		return (EAttribute)swapValueMatrixResultSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSwapValueMatrixResultSet_GeneratedSpotLoadSlot() {
		return (EReference)swapValueMatrixResultSetEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSwapValueMatrixResultSet_GeneratedSpotDischargeSlot() {
		return (EReference)swapValueMatrixResultSetEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSolutionOption() {
		return solutionOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSolutionOption_ChangeDescription() {
		return (EReference)solutionOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSolutionOption_ScheduleSpecification() {
		return (EReference)solutionOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSolutionOption_ScheduleModel() {
		return (EReference)solutionOptionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOptimisationResult() {
		return optimisationResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDualModeSolutionOption() {
		return dualModeSolutionOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDualModeSolutionOption_MicroBaseCase() {
		return (EReference)dualModeSolutionOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDualModeSolutionOption_MicroTargetCase() {
		return (EReference)dualModeSolutionOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSolutionOptionMicroCase() {
		return solutionOptionMicroCaseEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSolutionOptionMicroCase_ScheduleSpecification() {
		return (EReference)solutionOptionMicroCaseEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSolutionOptionMicroCase_ScheduleModel() {
		return (EReference)solutionOptionMicroCaseEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSolutionOptionMicroCase_ExtraVesselCharters() {
		return (EReference)solutionOptionMicroCaseEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSolutionOptionMicroCase_CharterInMarketOverrides() {
		return (EReference)solutionOptionMicroCaseEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptionAnalysisModel_PartialCase() {
		return (EReference)optionAnalysisModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptionAnalysisModel_Results() {
		return (EReference)optionAnalysisModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptionAnalysisModel_UseTargetPNL() {
		return (EAttribute)optionAnalysisModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptionAnalysisModel_Mode() {
		return (EAttribute)optionAnalysisModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSandboxResult() {
		return sandboxResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getVolumeMode() {
		return volumeModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getSlotType() {
		return slotTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AnalyticsFactory getAnalyticsFactory() {
		return (AnalyticsFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		analyticsModelEClass = createEClass(ANALYTICS_MODEL);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__OPTION_MODELS);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__OPTIMISATIONS);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__VIABILITY_MODEL);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__MTM_MODEL);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__BREAKEVEN_MODELS);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS);

		buyOptionEClass = createEClass(BUY_OPTION);

		sellOptionEClass = createEClass(SELL_OPTION);

		openSellEClass = createEClass(OPEN_SELL);

		openBuyEClass = createEClass(OPEN_BUY);

		buyOpportunityEClass = createEClass(BUY_OPPORTUNITY);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__NAME);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__DES_PURCHASE);
		createEReference(buyOpportunityEClass, BUY_OPPORTUNITY__PORT);
		createEReference(buyOpportunityEClass, BUY_OPPORTUNITY__CONTRACT);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__DATE);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__PRICE_EXPRESSION);
		createEReference(buyOpportunityEClass, BUY_OPPORTUNITY__ENTITY);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__CV);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__CANCELLATION_EXPRESSION);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__MISC_COSTS);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__VOLUME_MODE);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__MIN_VOLUME);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__MAX_VOLUME);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__VOLUME_UNITS);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__SPECIFY_WINDOW);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__WINDOW_SIZE);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__WINDOW_SIZE_UNITS);

		sellOpportunityEClass = createEClass(SELL_OPPORTUNITY);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__NAME);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__FOB_SALE);
		createEReference(sellOpportunityEClass, SELL_OPPORTUNITY__PORT);
		createEReference(sellOpportunityEClass, SELL_OPPORTUNITY__CONTRACT);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__DATE);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__PRICE_EXPRESSION);
		createEReference(sellOpportunityEClass, SELL_OPPORTUNITY__ENTITY);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__CANCELLATION_EXPRESSION);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__MISC_COSTS);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__VOLUME_MODE);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__MIN_VOLUME);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__MAX_VOLUME);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__VOLUME_UNITS);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__SPECIFY_WINDOW);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__WINDOW_SIZE);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__WINDOW_SIZE_UNITS);

		buyMarketEClass = createEClass(BUY_MARKET);
		createEReference(buyMarketEClass, BUY_MARKET__MARKET);
		createEAttribute(buyMarketEClass, BUY_MARKET__MONTH);

		sellMarketEClass = createEClass(SELL_MARKET);
		createEReference(sellMarketEClass, SELL_MARKET__MARKET);
		createEAttribute(sellMarketEClass, SELL_MARKET__MONTH);

		buyReferenceEClass = createEClass(BUY_REFERENCE);
		createEReference(buyReferenceEClass, BUY_REFERENCE__SLOT);

		sellReferenceEClass = createEClass(SELL_REFERENCE);
		createEReference(sellReferenceEClass, SELL_REFERENCE__SLOT);

		vesselEventOptionEClass = createEClass(VESSEL_EVENT_OPTION);

		vesselEventReferenceEClass = createEClass(VESSEL_EVENT_REFERENCE);
		createEReference(vesselEventReferenceEClass, VESSEL_EVENT_REFERENCE__EVENT);

		charterOutOpportunityEClass = createEClass(CHARTER_OUT_OPPORTUNITY);
		createEAttribute(charterOutOpportunityEClass, CHARTER_OUT_OPPORTUNITY__HIRE_COST);
		createEReference(charterOutOpportunityEClass, CHARTER_OUT_OPPORTUNITY__PORT);
		createEAttribute(charterOutOpportunityEClass, CHARTER_OUT_OPPORTUNITY__DATE);
		createEAttribute(charterOutOpportunityEClass, CHARTER_OUT_OPPORTUNITY__DURATION);

		baseCaseRowEClass = createEClass(BASE_CASE_ROW);
		createEReference(baseCaseRowEClass, BASE_CASE_ROW__BUY_OPTION);
		createEReference(baseCaseRowEClass, BASE_CASE_ROW__SELL_OPTION);
		createEReference(baseCaseRowEClass, BASE_CASE_ROW__VESSEL_EVENT_OPTION);
		createEReference(baseCaseRowEClass, BASE_CASE_ROW__SHIPPING);
		createEAttribute(baseCaseRowEClass, BASE_CASE_ROW__OPTIONISE);
		createEReference(baseCaseRowEClass, BASE_CASE_ROW__OPTIONS);
		createEAttribute(baseCaseRowEClass, BASE_CASE_ROW__FREEZE);

		baseCaseRowOptionsEClass = createEClass(BASE_CASE_ROW_OPTIONS);
		createEAttribute(baseCaseRowOptionsEClass, BASE_CASE_ROW_OPTIONS__LADEN_ROUTE);
		createEAttribute(baseCaseRowOptionsEClass, BASE_CASE_ROW_OPTIONS__BALLAST_ROUTE);
		createEAttribute(baseCaseRowOptionsEClass, BASE_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICE);
		createEAttribute(baseCaseRowOptionsEClass, BASE_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICE);
		createEAttribute(baseCaseRowOptionsEClass, BASE_CASE_ROW_OPTIONS__LOAD_DATE);
		createEAttribute(baseCaseRowOptionsEClass, BASE_CASE_ROW_OPTIONS__DISCHARGE_DATE);

		partialCaseRowEClass = createEClass(PARTIAL_CASE_ROW);
		createEReference(partialCaseRowEClass, PARTIAL_CASE_ROW__BUY_OPTIONS);
		createEReference(partialCaseRowEClass, PARTIAL_CASE_ROW__SELL_OPTIONS);
		createEReference(partialCaseRowEClass, PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS);
		createEReference(partialCaseRowEClass, PARTIAL_CASE_ROW__SHIPPING);
		createEReference(partialCaseRowEClass, PARTIAL_CASE_ROW__OPTIONS);
		createEReference(partialCaseRowEClass, PARTIAL_CASE_ROW__COMMODITY_CURVE_OPTIONS);

		partialCaseRowOptionsEClass = createEClass(PARTIAL_CASE_ROW_OPTIONS);
		createEAttribute(partialCaseRowOptionsEClass, PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES);
		createEAttribute(partialCaseRowOptionsEClass, PARTIAL_CASE_ROW_OPTIONS__BALLAST_ROUTES);
		createEAttribute(partialCaseRowOptionsEClass, PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES);
		createEAttribute(partialCaseRowOptionsEClass, PARTIAL_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICES);
		createEReference(partialCaseRowOptionsEClass, PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES);
		createEReference(partialCaseRowOptionsEClass, PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES);

		shippingOptionEClass = createEClass(SHIPPING_OPTION);

		simpleVesselCharterOptionEClass = createEClass(SIMPLE_VESSEL_CHARTER_OPTION);
		createEReference(simpleVesselCharterOptionEClass, SIMPLE_VESSEL_CHARTER_OPTION__VESSEL);
		createEAttribute(simpleVesselCharterOptionEClass, SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST);
		createEReference(simpleVesselCharterOptionEClass, SIMPLE_VESSEL_CHARTER_OPTION__ENTITY);
		createEAttribute(simpleVesselCharterOptionEClass, SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL);

		optionalSimpleVesselCharterOptionEClass = createEClass(OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION);
		createEAttribute(optionalSimpleVesselCharterOptionEClass, OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__BALLAST_BONUS);
		createEAttribute(optionalSimpleVesselCharterOptionEClass, OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__REPOSITIONING_FEE);
		createEAttribute(optionalSimpleVesselCharterOptionEClass, OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START);
		createEAttribute(optionalSimpleVesselCharterOptionEClass, OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END);
		createEReference(optionalSimpleVesselCharterOptionEClass, OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START_PORT);
		createEReference(optionalSimpleVesselCharterOptionEClass, OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END_PORT);

		roundTripShippingOptionEClass = createEClass(ROUND_TRIP_SHIPPING_OPTION);
		createEReference(roundTripShippingOptionEClass, ROUND_TRIP_SHIPPING_OPTION__VESSEL);
		createEAttribute(roundTripShippingOptionEClass, ROUND_TRIP_SHIPPING_OPTION__HIRE_COST);
		createEReference(roundTripShippingOptionEClass, ROUND_TRIP_SHIPPING_OPTION__ENTITY);

		nominatedShippingOptionEClass = createEClass(NOMINATED_SHIPPING_OPTION);
		createEReference(nominatedShippingOptionEClass, NOMINATED_SHIPPING_OPTION__NOMINATED_VESSEL);

		fullVesselCharterOptionEClass = createEClass(FULL_VESSEL_CHARTER_OPTION);
		createEReference(fullVesselCharterOptionEClass, FULL_VESSEL_CHARTER_OPTION__VESSEL_CHARTER);

		existingVesselCharterOptionEClass = createEClass(EXISTING_VESSEL_CHARTER_OPTION);
		createEReference(existingVesselCharterOptionEClass, EXISTING_VESSEL_CHARTER_OPTION__VESSEL_CHARTER);

		analysisResultDetailEClass = createEClass(ANALYSIS_RESULT_DETAIL);

		profitAndLossResultEClass = createEClass(PROFIT_AND_LOSS_RESULT);
		createEAttribute(profitAndLossResultEClass, PROFIT_AND_LOSS_RESULT__VALUE);

		breakEvenResultEClass = createEClass(BREAK_EVEN_RESULT);
		createEAttribute(breakEvenResultEClass, BREAK_EVEN_RESULT__PRICE);
		createEAttribute(breakEvenResultEClass, BREAK_EVEN_RESULT__PRICE_STRING);
		createEAttribute(breakEvenResultEClass, BREAK_EVEN_RESULT__CARGO_PNL);

		abstractAnalysisModelEClass = createEClass(ABSTRACT_ANALYSIS_MODEL);
		createEReference(abstractAnalysisModelEClass, ABSTRACT_ANALYSIS_MODEL__BUYS);
		createEReference(abstractAnalysisModelEClass, ABSTRACT_ANALYSIS_MODEL__SELLS);
		createEReference(abstractAnalysisModelEClass, ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS);
		createEReference(abstractAnalysisModelEClass, ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
		createEReference(abstractAnalysisModelEClass, ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES);

		optionAnalysisModelEClass = createEClass(OPTION_ANALYSIS_MODEL);
		createEReference(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__BASE_CASE);
		createEReference(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
		createEReference(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__RESULTS);
		createEAttribute(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__USE_TARGET_PNL);
		createEAttribute(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__MODE);

		sandboxResultEClass = createEClass(SANDBOX_RESULT);

		baseCaseEClass = createEClass(BASE_CASE);
		createEReference(baseCaseEClass, BASE_CASE__BASE_CASE);
		createEAttribute(baseCaseEClass, BASE_CASE__PROFIT_AND_LOSS);
		createEAttribute(baseCaseEClass, BASE_CASE__KEEP_EXISTING_SCENARIO);

		partialCaseEClass = createEClass(PARTIAL_CASE);
		createEReference(partialCaseEClass, PARTIAL_CASE__PARTIAL_CASE);
		createEAttribute(partialCaseEClass, PARTIAL_CASE__KEEP_EXISTING_SCENARIO);

		existingCharterMarketOptionEClass = createEClass(EXISTING_CHARTER_MARKET_OPTION);
		createEReference(existingCharterMarketOptionEClass, EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET);
		createEAttribute(existingCharterMarketOptionEClass, EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX);

		abstractSolutionSetEClass = createEClass(ABSTRACT_SOLUTION_SET);
		createEAttribute(abstractSolutionSetEClass, ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS);
		createEAttribute(abstractSolutionSetEClass, ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE);
		createEReference(abstractSolutionSetEClass, ABSTRACT_SOLUTION_SET__USER_SETTINGS);
		createEReference(abstractSolutionSetEClass, ABSTRACT_SOLUTION_SET__EXTRA_SLOTS);
		createEReference(abstractSolutionSetEClass, ABSTRACT_SOLUTION_SET__BASE_OPTION);
		createEReference(abstractSolutionSetEClass, ABSTRACT_SOLUTION_SET__OPTIONS);
		createEReference(abstractSolutionSetEClass, ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_EVENTS);
		createEReference(abstractSolutionSetEClass, ABSTRACT_SOLUTION_SET__EXTRA_VESSEL_CHARTERS);
		createEReference(abstractSolutionSetEClass, ABSTRACT_SOLUTION_SET__CHARTER_IN_MARKET_OVERRIDES);
		createEReference(abstractSolutionSetEClass, ABSTRACT_SOLUTION_SET__EXTRA_CHARTER_IN_MARKETS);
		createEAttribute(abstractSolutionSetEClass, ABSTRACT_SOLUTION_SET__USE_SCENARIO_BASE);

		actionableSetPlanEClass = createEClass(ACTIONABLE_SET_PLAN);

		slotInsertionOptionsEClass = createEClass(SLOT_INSERTION_OPTIONS);
		createEReference(slotInsertionOptionsEClass, SLOT_INSERTION_OPTIONS__SLOTS_INSERTED);
		createEReference(slotInsertionOptionsEClass, SLOT_INSERTION_OPTIONS__EVENTS_INSERTED);

		solutionOptionEClass = createEClass(SOLUTION_OPTION);
		createEReference(solutionOptionEClass, SOLUTION_OPTION__CHANGE_DESCRIPTION);
		createEReference(solutionOptionEClass, SOLUTION_OPTION__SCHEDULE_SPECIFICATION);
		createEReference(solutionOptionEClass, SOLUTION_OPTION__SCHEDULE_MODEL);

		optimisationResultEClass = createEClass(OPTIMISATION_RESULT);

		dualModeSolutionOptionEClass = createEClass(DUAL_MODE_SOLUTION_OPTION);
		createEReference(dualModeSolutionOptionEClass, DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE);
		createEReference(dualModeSolutionOptionEClass, DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE);

		solutionOptionMicroCaseEClass = createEClass(SOLUTION_OPTION_MICRO_CASE);
		createEReference(solutionOptionMicroCaseEClass, SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION);
		createEReference(solutionOptionMicroCaseEClass, SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL);
		createEReference(solutionOptionMicroCaseEClass, SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_CHARTERS);
		createEReference(solutionOptionMicroCaseEClass, SOLUTION_OPTION_MICRO_CASE__CHARTER_IN_MARKET_OVERRIDES);

		changeDescriptionEClass = createEClass(CHANGE_DESCRIPTION);
		createEReference(changeDescriptionEClass, CHANGE_DESCRIPTION__CHANGES);

		changeEClass = createEClass(CHANGE);

		openSlotChangeEClass = createEClass(OPEN_SLOT_CHANGE);
		createEReference(openSlotChangeEClass, OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR);

		cargoChangeEClass = createEClass(CARGO_CHANGE);
		createEReference(cargoChangeEClass, CARGO_CHANGE__SLOT_DESCRIPTORS);
		createEReference(cargoChangeEClass, CARGO_CHANGE__VESSEL_ALLOCATION);
		createEReference(cargoChangeEClass, CARGO_CHANGE__POSITION);

		vesselEventChangeEClass = createEClass(VESSEL_EVENT_CHANGE);
		createEReference(vesselEventChangeEClass, VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR);
		createEReference(vesselEventChangeEClass, VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION);
		createEReference(vesselEventChangeEClass, VESSEL_EVENT_CHANGE__POSITION);

		vesselEventDescriptorEClass = createEClass(VESSEL_EVENT_DESCRIPTOR);
		createEAttribute(vesselEventDescriptorEClass, VESSEL_EVENT_DESCRIPTOR__EVENT_NAME);

		slotDescriptorEClass = createEClass(SLOT_DESCRIPTOR);
		createEAttribute(slotDescriptorEClass, SLOT_DESCRIPTOR__SLOT_TYPE);

		realSlotDescriptorEClass = createEClass(REAL_SLOT_DESCRIPTOR);
		createEAttribute(realSlotDescriptorEClass, REAL_SLOT_DESCRIPTOR__SLOT_NAME);

		spotMarketSlotDescriptorEClass = createEClass(SPOT_MARKET_SLOT_DESCRIPTOR);
		createEAttribute(spotMarketSlotDescriptorEClass, SPOT_MARKET_SLOT_DESCRIPTOR__DATE);
		createEAttribute(spotMarketSlotDescriptorEClass, SPOT_MARKET_SLOT_DESCRIPTOR__MARKET_NAME);
		createEAttribute(spotMarketSlotDescriptorEClass, SPOT_MARKET_SLOT_DESCRIPTOR__PORT_NAME);

		vesselAllocationDescriptorEClass = createEClass(VESSEL_ALLOCATION_DESCRIPTOR);

		marketVesselAllocationDescriptorEClass = createEClass(MARKET_VESSEL_ALLOCATION_DESCRIPTOR);
		createEAttribute(marketVesselAllocationDescriptorEClass, MARKET_VESSEL_ALLOCATION_DESCRIPTOR__MARKET_NAME);
		createEAttribute(marketVesselAllocationDescriptorEClass, MARKET_VESSEL_ALLOCATION_DESCRIPTOR__SPOT_INDEX);

		fleetVesselAllocationDescriptorEClass = createEClass(FLEET_VESSEL_ALLOCATION_DESCRIPTOR);
		createEAttribute(fleetVesselAllocationDescriptorEClass, FLEET_VESSEL_ALLOCATION_DESCRIPTOR__NAME);
		createEAttribute(fleetVesselAllocationDescriptorEClass, FLEET_VESSEL_ALLOCATION_DESCRIPTOR__CHARTER_INDEX);

		positionDescriptorEClass = createEClass(POSITION_DESCRIPTOR);
		createEAttribute(positionDescriptorEClass, POSITION_DESCRIPTOR__AFTER);
		createEAttribute(positionDescriptorEClass, POSITION_DESCRIPTOR__BEFORE);

		viabilityModelEClass = createEClass(VIABILITY_MODEL);
		createEReference(viabilityModelEClass, VIABILITY_MODEL__ROWS);
		createEReference(viabilityModelEClass, VIABILITY_MODEL__MARKETS);

		viabilityRowEClass = createEClass(VIABILITY_ROW);
		createEReference(viabilityRowEClass, VIABILITY_ROW__BUY_OPTION);
		createEReference(viabilityRowEClass, VIABILITY_ROW__SELL_OPTION);
		createEReference(viabilityRowEClass, VIABILITY_ROW__SHIPPING);
		createEReference(viabilityRowEClass, VIABILITY_ROW__LHS_RESULTS);
		createEReference(viabilityRowEClass, VIABILITY_ROW__RHS_RESULTS);
		createEReference(viabilityRowEClass, VIABILITY_ROW__TARGET);
		createEAttribute(viabilityRowEClass, VIABILITY_ROW__PRICE);
		createEAttribute(viabilityRowEClass, VIABILITY_ROW__ETA);
		createEAttribute(viabilityRowEClass, VIABILITY_ROW__REFERENCE_PRICE);
		createEAttribute(viabilityRowEClass, VIABILITY_ROW__START_VOLUME);

		viabilityResultEClass = createEClass(VIABILITY_RESULT);
		createEReference(viabilityResultEClass, VIABILITY_RESULT__TARGET);
		createEAttribute(viabilityResultEClass, VIABILITY_RESULT__EARLIEST_ETA);
		createEAttribute(viabilityResultEClass, VIABILITY_RESULT__LATEST_ETA);
		createEAttribute(viabilityResultEClass, VIABILITY_RESULT__EARLIEST_VOLUME);
		createEAttribute(viabilityResultEClass, VIABILITY_RESULT__LATEST_VOLUME);
		createEAttribute(viabilityResultEClass, VIABILITY_RESULT__EARLIEST_PRICE);
		createEAttribute(viabilityResultEClass, VIABILITY_RESULT__LATEST_PRICE);

		mtmModelEClass = createEClass(MTM_MODEL);
		createEReference(mtmModelEClass, MTM_MODEL__ROWS);
		createEReference(mtmModelEClass, MTM_MODEL__MARKETS);
		createEReference(mtmModelEClass, MTM_MODEL__NOMINAL_MARKETS);

		mtmResultEClass = createEClass(MTM_RESULT);
		createEReference(mtmResultEClass, MTM_RESULT__TARGET);
		createEAttribute(mtmResultEClass, MTM_RESULT__EARLIEST_ETA);
		createEAttribute(mtmResultEClass, MTM_RESULT__EARLIEST_VOLUME);
		createEAttribute(mtmResultEClass, MTM_RESULT__EARLIEST_PRICE);
		createEReference(mtmResultEClass, MTM_RESULT__SHIPPING);
		createEAttribute(mtmResultEClass, MTM_RESULT__SHIPPING_COST);

		mtmRowEClass = createEClass(MTM_ROW);
		createEReference(mtmRowEClass, MTM_ROW__BUY_OPTION);
		createEReference(mtmRowEClass, MTM_ROW__SELL_OPTION);
		createEReference(mtmRowEClass, MTM_ROW__LHS_RESULTS);
		createEReference(mtmRowEClass, MTM_ROW__RHS_RESULTS);
		createEReference(mtmRowEClass, MTM_ROW__TARGET);
		createEAttribute(mtmRowEClass, MTM_ROW__PRICE);
		createEAttribute(mtmRowEClass, MTM_ROW__ETA);
		createEAttribute(mtmRowEClass, MTM_ROW__REFERENCE_PRICE);
		createEAttribute(mtmRowEClass, MTM_ROW__START_VOLUME);

		breakEvenAnalysisModelEClass = createEClass(BREAK_EVEN_ANALYSIS_MODEL);
		createEReference(breakEvenAnalysisModelEClass, BREAK_EVEN_ANALYSIS_MODEL__ROWS);
		createEReference(breakEvenAnalysisModelEClass, BREAK_EVEN_ANALYSIS_MODEL__MARKETS);

		breakEvenAnalysisRowEClass = createEClass(BREAK_EVEN_ANALYSIS_ROW);
		createEReference(breakEvenAnalysisRowEClass, BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION);
		createEReference(breakEvenAnalysisRowEClass, BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION);
		createEReference(breakEvenAnalysisRowEClass, BREAK_EVEN_ANALYSIS_ROW__SHIPPING);
		createEReference(breakEvenAnalysisRowEClass, BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS);
		createEReference(breakEvenAnalysisRowEClass, BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS);
		createEReference(breakEvenAnalysisRowEClass, BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON);
		createEReference(breakEvenAnalysisRowEClass, BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON);

		breakEvenAnalysisResultSetEClass = createEClass(BREAK_EVEN_ANALYSIS_RESULT_SET);
		createEReference(breakEvenAnalysisResultSetEClass, BREAK_EVEN_ANALYSIS_RESULT_SET__BASED_ON);
		createEReference(breakEvenAnalysisResultSetEClass, BREAK_EVEN_ANALYSIS_RESULT_SET__RESULTS);
		createEAttribute(breakEvenAnalysisResultSetEClass, BREAK_EVEN_ANALYSIS_RESULT_SET__PRICE);

		breakEvenAnalysisResultEClass = createEClass(BREAK_EVEN_ANALYSIS_RESULT);
		createEReference(breakEvenAnalysisResultEClass, BREAK_EVEN_ANALYSIS_RESULT__TARGET);
		createEAttribute(breakEvenAnalysisResultEClass, BREAK_EVEN_ANALYSIS_RESULT__PRICE);
		createEAttribute(breakEvenAnalysisResultEClass, BREAK_EVEN_ANALYSIS_RESULT__ETA);
		createEAttribute(breakEvenAnalysisResultEClass, BREAK_EVEN_ANALYSIS_RESULT__REFERENCE_PRICE);

		localDateTimeHolderEClass = createEClass(LOCAL_DATE_TIME_HOLDER);
		createEAttribute(localDateTimeHolderEClass, LOCAL_DATE_TIME_HOLDER__DATE_TIME);

		commodityCurveOptionEClass = createEClass(COMMODITY_CURVE_OPTION);

		commodityCurveOverlayEClass = createEClass(COMMODITY_CURVE_OVERLAY);
		createEReference(commodityCurveOverlayEClass, COMMODITY_CURVE_OVERLAY__REFERENCE_CURVE);
		createEReference(commodityCurveOverlayEClass, COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES);

		sensitivityModelEClass = createEClass(SENSITIVITY_MODEL);
		createEReference(sensitivityModelEClass, SENSITIVITY_MODEL__SENSITIVITY_MODEL);
		createEReference(sensitivityModelEClass, SENSITIVITY_MODEL__SENSITIVITY_SOLUTION);

		sensitivitySolutionSetEClass = createEClass(SENSITIVITY_SOLUTION_SET);
		createEReference(sensitivitySolutionSetEClass, SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT);
		createEReference(sensitivitySolutionSetEClass, SENSITIVITY_SOLUTION_SET__CARGO_PN_LRESULTS);

		abstractSensitivityResultEClass = createEClass(ABSTRACT_SENSITIVITY_RESULT);
		createEAttribute(abstractSensitivityResultEClass, ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L);
		createEAttribute(abstractSensitivityResultEClass, ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L);
		createEAttribute(abstractSensitivityResultEClass, ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L);
		createEAttribute(abstractSensitivityResultEClass, ABSTRACT_SENSITIVITY_RESULT__VARIANCE);

		portfolioSensitivityResultEClass = createEClass(PORTFOLIO_SENSITIVITY_RESULT);

		cargoPnLResultEClass = createEClass(CARGO_PN_LRESULT);
		createEReference(cargoPnLResultEClass, CARGO_PN_LRESULT__CARGO);

		swapValueMatrixModelEClass = createEClass(SWAP_VALUE_MATRIX_MODEL);
		createEReference(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__BASE_LOAD);
		createEReference(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE);
		createEReference(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER);
		createEReference(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET);
		createEReference(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET);
		createEAttribute(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MIN_PRICE);
		createEAttribute(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MAX_PRICE);
		createEAttribute(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_STEP_SIZE);
		createEAttribute(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__MARKET_MIN_PRICE);
		createEAttribute(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__MARKET_MAX_PRICE);
		createEAttribute(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__MARKET_STEP_SIZE);
		createEAttribute(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__SWAP_FEE);
		createEReference(swapValueMatrixModelEClass, SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT);

		swapValueMatrixResultEClass = createEClass(SWAP_VALUE_MATRIX_RESULT);
		createEAttribute(swapValueMatrixResultEClass, SWAP_VALUE_MATRIX_RESULT__BASE_DISCHARGE_PRICE);
		createEAttribute(swapValueMatrixResultEClass, SWAP_VALUE_MATRIX_RESULT__SWAP_MARKET_PRICE);
		createEAttribute(swapValueMatrixResultEClass, SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL);
		createEAttribute(swapValueMatrixResultEClass, SWAP_VALUE_MATRIX_RESULT__BASE_LOAD_PRICE);
		createEAttribute(swapValueMatrixResultEClass, SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_PRICE);
		createEAttribute(swapValueMatrixResultEClass, SWAP_VALUE_MATRIX_RESULT__BASE_FOB_LOAD_VOLUME);
		createEAttribute(swapValueMatrixResultEClass, SWAP_VALUE_MATRIX_RESULT__SWAP_FOB_LOAD_VOLUME);
		createEAttribute(swapValueMatrixResultEClass, SWAP_VALUE_MATRIX_RESULT__MARKET_BUY_VOLUME);
		createEAttribute(swapValueMatrixResultEClass, SWAP_VALUE_MATRIX_RESULT__MARKET_SELL_VOLUME);
		createEAttribute(swapValueMatrixResultEClass, SWAP_VALUE_MATRIX_RESULT__BASE_DES_SELL_VOLUME);

		swapValueMatrixResultSetEClass = createEClass(SWAP_VALUE_MATRIX_RESULT_SET);
		createEReference(swapValueMatrixResultSetEClass, SWAP_VALUE_MATRIX_RESULT_SET__RESULTS);
		createEAttribute(swapValueMatrixResultSetEClass, SWAP_VALUE_MATRIX_RESULT_SET__SWAP_FEE);
		createEReference(swapValueMatrixResultSetEClass, SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT);
		createEReference(swapValueMatrixResultSetEClass, SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT);

		// Create enums
		volumeModeEEnum = createEEnum(VOLUME_MODE);
		slotTypeEEnum = createEEnum(SLOT_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		MMXCorePackage theMMXCorePackage = (MMXCorePackage)EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		SpotMarketsPackage theSpotMarketsPackage = (SpotMarketsPackage)EPackage.Registry.INSTANCE.getEPackage(SpotMarketsPackage.eNS_URI);
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);
		ParametersPackage theParametersPackage = (ParametersPackage)EPackage.Registry.INSTANCE.getEPackage(ParametersPackage.eNS_URI);
		SchedulePackage theSchedulePackage = (SchedulePackage)EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI);
		PricingPackage thePricingPackage = (PricingPackage)EPackage.Registry.INSTANCE.getEPackage(PricingPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		analyticsModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		openSellEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		openSellEClass.getESuperTypes().add(this.getSellOption());
		openBuyEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		openBuyEClass.getESuperTypes().add(this.getBuyOption());
		buyOpportunityEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		buyOpportunityEClass.getESuperTypes().add(this.getBuyOption());
		sellOpportunityEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		sellOpportunityEClass.getESuperTypes().add(this.getSellOption());
		buyMarketEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		buyMarketEClass.getESuperTypes().add(this.getBuyOption());
		sellMarketEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		sellMarketEClass.getESuperTypes().add(this.getSellOption());
		buyReferenceEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		buyReferenceEClass.getESuperTypes().add(this.getBuyOption());
		sellReferenceEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		sellReferenceEClass.getESuperTypes().add(this.getSellOption());
		vesselEventReferenceEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		vesselEventReferenceEClass.getESuperTypes().add(this.getVesselEventOption());
		charterOutOpportunityEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		charterOutOpportunityEClass.getESuperTypes().add(this.getVesselEventOption());
		simpleVesselCharterOptionEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		simpleVesselCharterOptionEClass.getESuperTypes().add(this.getShippingOption());
		optionalSimpleVesselCharterOptionEClass.getESuperTypes().add(this.getSimpleVesselCharterOption());
		roundTripShippingOptionEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		roundTripShippingOptionEClass.getESuperTypes().add(this.getShippingOption());
		nominatedShippingOptionEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		nominatedShippingOptionEClass.getESuperTypes().add(this.getShippingOption());
		fullVesselCharterOptionEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		fullVesselCharterOptionEClass.getESuperTypes().add(this.getShippingOption());
		existingVesselCharterOptionEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		existingVesselCharterOptionEClass.getESuperTypes().add(this.getShippingOption());
		profitAndLossResultEClass.getESuperTypes().add(this.getAnalysisResultDetail());
		breakEvenResultEClass.getESuperTypes().add(this.getAnalysisResultDetail());
		abstractAnalysisModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		abstractAnalysisModelEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		optionAnalysisModelEClass.getESuperTypes().add(this.getAbstractAnalysisModel());
		sandboxResultEClass.getESuperTypes().add(this.getAbstractSolutionSet());
		existingCharterMarketOptionEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		existingCharterMarketOptionEClass.getESuperTypes().add(this.getShippingOption());
		abstractSolutionSetEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		abstractSolutionSetEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		actionableSetPlanEClass.getESuperTypes().add(this.getAbstractSolutionSet());
		slotInsertionOptionsEClass.getESuperTypes().add(this.getAbstractSolutionSet());
		optimisationResultEClass.getESuperTypes().add(this.getAbstractSolutionSet());
		dualModeSolutionOptionEClass.getESuperTypes().add(this.getSolutionOption());
		openSlotChangeEClass.getESuperTypes().add(this.getChange());
		cargoChangeEClass.getESuperTypes().add(this.getChange());
		vesselEventChangeEClass.getESuperTypes().add(this.getChange());
		realSlotDescriptorEClass.getESuperTypes().add(this.getSlotDescriptor());
		spotMarketSlotDescriptorEClass.getESuperTypes().add(this.getSlotDescriptor());
		marketVesselAllocationDescriptorEClass.getESuperTypes().add(this.getVesselAllocationDescriptor());
		fleetVesselAllocationDescriptorEClass.getESuperTypes().add(this.getVesselAllocationDescriptor());
		viabilityModelEClass.getESuperTypes().add(this.getAbstractAnalysisModel());
		mtmModelEClass.getESuperTypes().add(this.getAbstractAnalysisModel());
		breakEvenAnalysisModelEClass.getESuperTypes().add(this.getAbstractAnalysisModel());
		commodityCurveOverlayEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		commodityCurveOverlayEClass.getESuperTypes().add(this.getCommodityCurveOption());
		sensitivityModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		sensitivitySolutionSetEClass.getESuperTypes().add(this.getAbstractSolutionSet());
		abstractSensitivityResultEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		portfolioSensitivityResultEClass.getESuperTypes().add(this.getAbstractSensitivityResult());
		cargoPnLResultEClass.getESuperTypes().add(this.getAbstractSensitivityResult());
		swapValueMatrixModelEClass.getESuperTypes().add(this.getAbstractAnalysisModel());
		swapValueMatrixResultEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		swapValueMatrixResultSetEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());

		// Initialize classes and features; add operations and parameters
		initEClass(analyticsModelEClass, AnalyticsModel.class, "AnalyticsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAnalyticsModel_OptionModels(), this.getOptionAnalysisModel(), null, "optionModels", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_Optimisations(), this.getAbstractSolutionSet(), null, "optimisations", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_ViabilityModel(), this.getViabilityModel(), null, "viabilityModel", null, 0, 1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_MtmModel(), this.getMTMModel(), null, "mtmModel", null, 0, 1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_BreakevenModels(), this.getBreakEvenAnalysisModel(), null, "breakevenModels", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_SwapValueMatrixModels(), this.getSwapValueMatrixModel(), null, "swapValueMatrixModels", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(buyOptionEClass, BuyOption.class, "BuyOption", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(sellOptionEClass, SellOption.class, "SellOption", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(openSellEClass, OpenSell.class, "OpenSell", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(openBuyEClass, OpenBuy.class, "OpenBuy", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(buyOpportunityEClass, BuyOpportunity.class, "BuyOpportunity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBuyOpportunity_Name(), ecorePackage.getEString(), "name", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_DesPurchase(), ecorePackage.getEBoolean(), "desPurchase", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBuyOpportunity_Port(), thePortPackage.getPort(), null, "port", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBuyOpportunity_Contract(), theCommercialPackage.getPurchaseContract(), null, "contract", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_Date(), theDateTimePackage.getLocalDate(), "date", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBuyOpportunity_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_Cv(), ecorePackage.getEDouble(), "cv", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_CancellationExpression(), ecorePackage.getEString(), "cancellationExpression", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_MiscCosts(), ecorePackage.getEInt(), "miscCosts", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_VolumeMode(), this.getVolumeMode(), "volumeMode", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_MinVolume(), ecorePackage.getEInt(), "minVolume", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_MaxVolume(), ecorePackage.getEInt(), "maxVolume", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_VolumeUnits(), theTypesPackage.getVolumeUnits(), "volumeUnits", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_SpecifyWindow(), ecorePackage.getEBoolean(), "specifyWindow", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_WindowSize(), ecorePackage.getEInt(), "windowSize", null, 1, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_WindowSizeUnits(), theTypesPackage.getTimePeriod(), "windowSizeUnits", null, 1, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sellOpportunityEClass, SellOpportunity.class, "SellOpportunity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSellOpportunity_Name(), ecorePackage.getEString(), "name", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_FobSale(), ecorePackage.getEBoolean(), "fobSale", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSellOpportunity_Port(), thePortPackage.getPort(), null, "port", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSellOpportunity_Contract(), theCommercialPackage.getSalesContract(), null, "contract", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_Date(), theDateTimePackage.getLocalDate(), "date", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSellOpportunity_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_CancellationExpression(), ecorePackage.getEString(), "cancellationExpression", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_MiscCosts(), ecorePackage.getEInt(), "miscCosts", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_VolumeMode(), this.getVolumeMode(), "volumeMode", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_MinVolume(), ecorePackage.getEInt(), "minVolume", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_MaxVolume(), ecorePackage.getEInt(), "maxVolume", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_VolumeUnits(), theTypesPackage.getVolumeUnits(), "volumeUnits", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_SpecifyWindow(), ecorePackage.getEBoolean(), "specifyWindow", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_WindowSize(), ecorePackage.getEInt(), "windowSize", null, 1, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_WindowSizeUnits(), theTypesPackage.getTimePeriod(), "windowSizeUnits", null, 1, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(buyMarketEClass, BuyMarket.class, "BuyMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBuyMarket_Market(), theSpotMarketsPackage.getSpotMarket(), null, "market", null, 0, 1, BuyMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyMarket_Month(), theDateTimePackage.getYearMonth(), "month", null, 0, 1, BuyMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sellMarketEClass, SellMarket.class, "SellMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSellMarket_Market(), theSpotMarketsPackage.getSpotMarket(), null, "market", null, 0, 1, SellMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellMarket_Month(), theDateTimePackage.getYearMonth(), "month", null, 0, 1, SellMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(buyReferenceEClass, BuyReference.class, "BuyReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBuyReference_Slot(), theCargoPackage.getLoadSlot(), null, "slot", null, 0, 1, BuyReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sellReferenceEClass, SellReference.class, "SellReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSellReference_Slot(), theCargoPackage.getDischargeSlot(), null, "slot", null, 0, 1, SellReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselEventOptionEClass, VesselEventOption.class, "VesselEventOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(vesselEventReferenceEClass, VesselEventReference.class, "VesselEventReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselEventReference_Event(), theCargoPackage.getVesselEvent(), null, "event", null, 0, 1, VesselEventReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(charterOutOpportunityEClass, CharterOutOpportunity.class, "CharterOutOpportunity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCharterOutOpportunity_HireCost(), ecorePackage.getEInt(), "hireCost", null, 0, 1, CharterOutOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterOutOpportunity_Port(), thePortPackage.getPort(), null, "port", null, 0, 1, CharterOutOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutOpportunity_Date(), theDateTimePackage.getLocalDate(), "date", null, 0, 1, CharterOutOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutOpportunity_Duration(), ecorePackage.getEInt(), "duration", null, 0, 1, CharterOutOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseCaseRowEClass, BaseCaseRow.class, "BaseCaseRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBaseCaseRow_BuyOption(), this.getBuyOption(), null, "buyOption", null, 0, 1, BaseCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBaseCaseRow_SellOption(), this.getSellOption(), null, "sellOption", null, 0, 1, BaseCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBaseCaseRow_VesselEventOption(), this.getVesselEventOption(), null, "vesselEventOption", null, 0, 1, BaseCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBaseCaseRow_Shipping(), this.getShippingOption(), null, "shipping", null, 0, 1, BaseCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseCaseRow_Optionise(), ecorePackage.getEBoolean(), "optionise", null, 0, 1, BaseCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBaseCaseRow_Options(), this.getBaseCaseRowOptions(), null, "options", null, 0, 1, BaseCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseCaseRow_Freeze(), ecorePackage.getEBoolean(), "freeze", null, 0, 1, BaseCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseCaseRowOptionsEClass, BaseCaseRowOptions.class, "BaseCaseRowOptions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBaseCaseRowOptions_LadenRoute(), thePortPackage.getRouteOption(), "ladenRoute", null, 0, 1, BaseCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseCaseRowOptions_BallastRoute(), thePortPackage.getRouteOption(), "ballastRoute", null, 0, 1, BaseCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseCaseRowOptions_LadenFuelChoice(), theCargoPackage.getFuelChoice(), "ladenFuelChoice", null, 0, 1, BaseCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseCaseRowOptions_BallastFuelChoice(), theCargoPackage.getFuelChoice(), "ballastFuelChoice", null, 0, 1, BaseCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseCaseRowOptions_LoadDate(), theDateTimePackage.getLocalDateTime(), "loadDate", null, 0, 1, BaseCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseCaseRowOptions_DischargeDate(), theDateTimePackage.getLocalDateTime(), "dischargeDate", null, 0, 1, BaseCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(partialCaseRowEClass, PartialCaseRow.class, "PartialCaseRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPartialCaseRow_BuyOptions(), this.getBuyOption(), null, "buyOptions", null, 0, -1, PartialCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPartialCaseRow_SellOptions(), this.getSellOption(), null, "sellOptions", null, 0, -1, PartialCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPartialCaseRow_VesselEventOptions(), this.getVesselEventOption(), null, "vesselEventOptions", null, 0, -1, PartialCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPartialCaseRow_Shipping(), this.getShippingOption(), null, "shipping", null, 0, -1, PartialCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPartialCaseRow_Options(), this.getPartialCaseRowOptions(), null, "options", null, 0, 1, PartialCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPartialCaseRow_CommodityCurveOptions(), this.getCommodityCurveOption(), null, "commodityCurveOptions", null, 0, -1, PartialCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(partialCaseRowOptionsEClass, PartialCaseRowOptions.class, "PartialCaseRowOptions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPartialCaseRowOptions_LadenRoutes(), thePortPackage.getRouteOption(), "ladenRoutes", null, 0, -1, PartialCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPartialCaseRowOptions_BallastRoutes(), thePortPackage.getRouteOption(), "ballastRoutes", null, 0, -1, PartialCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPartialCaseRowOptions_LadenFuelChoices(), theCargoPackage.getFuelChoice(), "ladenFuelChoices", null, 0, -1, PartialCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPartialCaseRowOptions_BallastFuelChoices(), theCargoPackage.getFuelChoice(), "ballastFuelChoices", null, 0, -1, PartialCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPartialCaseRowOptions_LoadDates(), this.getLocalDateTimeHolder(), null, "loadDates", null, 0, -1, PartialCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPartialCaseRowOptions_DischargeDates(), this.getLocalDateTimeHolder(), null, "dischargeDates", null, 0, -1, PartialCaseRowOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(shippingOptionEClass, ShippingOption.class, "ShippingOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(simpleVesselCharterOptionEClass, SimpleVesselCharterOption.class, "SimpleVesselCharterOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSimpleVesselCharterOption_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 0, 1, SimpleVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSimpleVesselCharterOption_HireCost(), ecorePackage.getEString(), "hireCost", null, 0, 1, SimpleVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSimpleVesselCharterOption_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, SimpleVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSimpleVesselCharterOption_UseSafetyHeel(), ecorePackage.getEBoolean(), "useSafetyHeel", null, 0, 1, SimpleVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(optionalSimpleVesselCharterOptionEClass, OptionalSimpleVesselCharterOption.class, "OptionalSimpleVesselCharterOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOptionalSimpleVesselCharterOption_BallastBonus(), ecorePackage.getEString(), "ballastBonus", null, 0, 1, OptionalSimpleVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptionalSimpleVesselCharterOption_RepositioningFee(), ecorePackage.getEString(), "repositioningFee", null, 0, 1, OptionalSimpleVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptionalSimpleVesselCharterOption_Start(), theDateTimePackage.getLocalDate(), "start", null, 0, 1, OptionalSimpleVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptionalSimpleVesselCharterOption_End(), theDateTimePackage.getLocalDate(), "end", null, 0, 1, OptionalSimpleVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionalSimpleVesselCharterOption_StartPort(), thePortPackage.getPort(), null, "startPort", null, 0, 1, OptionalSimpleVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionalSimpleVesselCharterOption_EndPort(), thePortPackage.getPort(), null, "endPort", null, 0, 1, OptionalSimpleVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(roundTripShippingOptionEClass, RoundTripShippingOption.class, "RoundTripShippingOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRoundTripShippingOption_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 0, 1, RoundTripShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRoundTripShippingOption_HireCost(), ecorePackage.getEString(), "hireCost", null, 0, 1, RoundTripShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRoundTripShippingOption_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, RoundTripShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(nominatedShippingOptionEClass, NominatedShippingOption.class, "NominatedShippingOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNominatedShippingOption_NominatedVessel(), theFleetPackage.getVessel(), null, "nominatedVessel", null, 0, 1, NominatedShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fullVesselCharterOptionEClass, FullVesselCharterOption.class, "FullVesselCharterOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFullVesselCharterOption_VesselCharter(), theCargoPackage.getVesselCharter(), null, "vesselCharter", null, 0, 1, FullVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(existingVesselCharterOptionEClass, ExistingVesselCharterOption.class, "ExistingVesselCharterOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExistingVesselCharterOption_VesselCharter(), theCargoPackage.getVesselCharter(), null, "vesselCharter", null, 0, 1, ExistingVesselCharterOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(analysisResultDetailEClass, AnalysisResultDetail.class, "AnalysisResultDetail", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(profitAndLossResultEClass, ProfitAndLossResult.class, "ProfitAndLossResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProfitAndLossResult_Value(), ecorePackage.getEDouble(), "value", null, 0, 1, ProfitAndLossResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(breakEvenResultEClass, BreakEvenResult.class, "BreakEvenResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBreakEvenResult_Price(), ecorePackage.getEDouble(), "price", null, 0, 1, BreakEvenResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBreakEvenResult_PriceString(), ecorePackage.getEString(), "priceString", null, 0, 1, BreakEvenResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBreakEvenResult_CargoPNL(), ecorePackage.getEDouble(), "cargoPNL", null, 0, 1, BreakEvenResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractAnalysisModelEClass, AbstractAnalysisModel.class, "AbstractAnalysisModel", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAbstractAnalysisModel_Buys(), this.getBuyOption(), null, "buys", null, 0, -1, AbstractAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractAnalysisModel_Sells(), this.getSellOption(), null, "sells", null, 0, -1, AbstractAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractAnalysisModel_VesselEvents(), this.getVesselEventOption(), null, "vesselEvents", null, 0, -1, AbstractAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractAnalysisModel_ShippingTemplates(), this.getShippingOption(), null, "shippingTemplates", null, 0, -1, AbstractAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractAnalysisModel_CommodityCurves(), this.getCommodityCurveOption(), null, "commodityCurves", null, 0, -1, AbstractAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(optionAnalysisModelEClass, OptionAnalysisModel.class, "OptionAnalysisModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOptionAnalysisModel_BaseCase(), this.getBaseCase(), null, "baseCase", null, 0, 1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionAnalysisModel_PartialCase(), this.getPartialCase(), null, "partialCase", null, 0, 1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionAnalysisModel_Results(), this.getAbstractSolutionSet(), null, "results", null, 0, 1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptionAnalysisModel_UseTargetPNL(), ecorePackage.getEBoolean(), "useTargetPNL", null, 0, 1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptionAnalysisModel_Mode(), ecorePackage.getEInt(), "mode", null, 0, 1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sandboxResultEClass, SandboxResult.class, "SandboxResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(baseCaseEClass, BaseCase.class, "BaseCase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBaseCase_BaseCase(), this.getBaseCaseRow(), null, "baseCase", null, 0, -1, BaseCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseCase_ProfitAndLoss(), ecorePackage.getELong(), "profitAndLoss", null, 0, 1, BaseCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseCase_KeepExistingScenario(), ecorePackage.getEBoolean(), "keepExistingScenario", null, 0, 1, BaseCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(partialCaseEClass, PartialCase.class, "PartialCase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPartialCase_PartialCase(), this.getPartialCaseRow(), null, "partialCase", null, 0, -1, PartialCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPartialCase_KeepExistingScenario(), ecorePackage.getEBoolean(), "keepExistingScenario", null, 0, 1, PartialCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(existingCharterMarketOptionEClass, ExistingCharterMarketOption.class, "ExistingCharterMarketOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExistingCharterMarketOption_CharterInMarket(), theSpotMarketsPackage.getCharterInMarket(), null, "charterInMarket", null, 0, 1, ExistingCharterMarketOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExistingCharterMarketOption_SpotIndex(), ecorePackage.getEInt(), "spotIndex", null, 0, 1, ExistingCharterMarketOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractSolutionSetEClass, AbstractSolutionSet.class, "AbstractSolutionSet", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAbstractSolutionSet_HasDualModeSolutions(), ecorePackage.getEBoolean(), "hasDualModeSolutions", null, 0, 1, AbstractSolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractSolutionSet_PortfolioBreakEvenMode(), ecorePackage.getEBoolean(), "portfolioBreakEvenMode", null, 0, 1, AbstractSolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractSolutionSet_UserSettings(), theParametersPackage.getUserSettings(), null, "userSettings", null, 0, 1, AbstractSolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractSolutionSet_ExtraSlots(), theCargoPackage.getSlot(), null, "extraSlots", null, 0, -1, AbstractSolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractSolutionSet_BaseOption(), this.getSolutionOption(), null, "baseOption", null, 0, 1, AbstractSolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractSolutionSet_Options(), this.getSolutionOption(), null, "options", null, 0, -1, AbstractSolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractSolutionSet_ExtraVesselEvents(), theCargoPackage.getVesselEvent(), null, "extraVesselEvents", null, 0, -1, AbstractSolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractSolutionSet_ExtraVesselCharters(), theCargoPackage.getVesselCharter(), null, "extraVesselCharters", null, 0, -1, AbstractSolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractSolutionSet_CharterInMarketOverrides(), theCargoPackage.getCharterInMarketOverride(), null, "charterInMarketOverrides", null, 0, -1, AbstractSolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractSolutionSet_ExtraCharterInMarkets(), theSpotMarketsPackage.getCharterInMarket(), null, "extraCharterInMarkets", null, 0, -1, AbstractSolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractSolutionSet_UseScenarioBase(), ecorePackage.getEBoolean(), "useScenarioBase", "true", 0, 1, AbstractSolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(actionableSetPlanEClass, ActionableSetPlan.class, "ActionableSetPlan", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(slotInsertionOptionsEClass, SlotInsertionOptions.class, "SlotInsertionOptions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlotInsertionOptions_SlotsInserted(), theCargoPackage.getSlot(), null, "slotsInserted", null, 0, -1, SlotInsertionOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotInsertionOptions_EventsInserted(), theCargoPackage.getVesselEvent(), null, "eventsInserted", null, 0, -1, SlotInsertionOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(solutionOptionEClass, SolutionOption.class, "SolutionOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSolutionOption_ChangeDescription(), this.getChangeDescription(), null, "changeDescription", null, 0, 1, SolutionOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSolutionOption_ScheduleSpecification(), theCargoPackage.getScheduleSpecification(), null, "scheduleSpecification", null, 0, 1, SolutionOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSolutionOption_ScheduleModel(), theSchedulePackage.getScheduleModel(), null, "scheduleModel", null, 0, 1, SolutionOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(optimisationResultEClass, OptimisationResult.class, "OptimisationResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dualModeSolutionOptionEClass, DualModeSolutionOption.class, "DualModeSolutionOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDualModeSolutionOption_MicroBaseCase(), this.getSolutionOptionMicroCase(), null, "microBaseCase", null, 0, 1, DualModeSolutionOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDualModeSolutionOption_MicroTargetCase(), this.getSolutionOptionMicroCase(), null, "microTargetCase", null, 0, 1, DualModeSolutionOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(solutionOptionMicroCaseEClass, SolutionOptionMicroCase.class, "SolutionOptionMicroCase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSolutionOptionMicroCase_ScheduleSpecification(), theCargoPackage.getScheduleSpecification(), null, "scheduleSpecification", null, 0, 1, SolutionOptionMicroCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSolutionOptionMicroCase_ScheduleModel(), theSchedulePackage.getScheduleModel(), null, "scheduleModel", null, 0, 1, SolutionOptionMicroCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSolutionOptionMicroCase_ExtraVesselCharters(), theCargoPackage.getVesselCharter(), null, "extraVesselCharters", null, 0, -1, SolutionOptionMicroCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSolutionOptionMicroCase_CharterInMarketOverrides(), theCargoPackage.getCharterInMarketOverride(), null, "charterInMarketOverrides", null, 0, -1, SolutionOptionMicroCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeDescriptionEClass, ChangeDescription.class, "ChangeDescription", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeDescription_Changes(), this.getChange(), null, "changes", null, 0, -1, ChangeDescription.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeEClass, Change.class, "Change", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(openSlotChangeEClass, OpenSlotChange.class, "OpenSlotChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOpenSlotChange_SlotDescriptor(), this.getSlotDescriptor(), null, "slotDescriptor", null, 0, 1, OpenSlotChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoChangeEClass, CargoChange.class, "CargoChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCargoChange_SlotDescriptors(), this.getSlotDescriptor(), null, "slotDescriptors", null, 0, -1, CargoChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoChange_VesselAllocation(), this.getVesselAllocationDescriptor(), null, "vesselAllocation", null, 0, 1, CargoChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoChange_Position(), this.getPositionDescriptor(), null, "position", null, 0, 1, CargoChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselEventChangeEClass, VesselEventChange.class, "VesselEventChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselEventChange_VesselEventDescriptor(), this.getVesselEventDescriptor(), null, "vesselEventDescriptor", null, 0, 1, VesselEventChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselEventChange_VesselAllocation(), this.getVesselAllocationDescriptor(), null, "vesselAllocation", null, 0, 1, VesselEventChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselEventChange_Position(), this.getPositionDescriptor(), null, "position", null, 0, 1, VesselEventChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselEventDescriptorEClass, VesselEventDescriptor.class, "VesselEventDescriptor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselEventDescriptor_EventName(), ecorePackage.getEString(), "eventName", null, 0, 1, VesselEventDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(slotDescriptorEClass, SlotDescriptor.class, "SlotDescriptor", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSlotDescriptor_SlotType(), this.getSlotType(), "slotType", null, 0, 1, SlotDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(realSlotDescriptorEClass, RealSlotDescriptor.class, "RealSlotDescriptor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRealSlotDescriptor_SlotName(), ecorePackage.getEString(), "slotName", null, 0, 1, RealSlotDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spotMarketSlotDescriptorEClass, SpotMarketSlotDescriptor.class, "SpotMarketSlotDescriptor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpotMarketSlotDescriptor_Date(), theDateTimePackage.getYearMonth(), "date", null, 0, 1, SpotMarketSlotDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarketSlotDescriptor_MarketName(), ecorePackage.getEString(), "marketName", null, 0, 1, SpotMarketSlotDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarketSlotDescriptor_PortName(), ecorePackage.getEString(), "portName", null, 0, 1, SpotMarketSlotDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselAllocationDescriptorEClass, VesselAllocationDescriptor.class, "VesselAllocationDescriptor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(marketVesselAllocationDescriptorEClass, MarketVesselAllocationDescriptor.class, "MarketVesselAllocationDescriptor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMarketVesselAllocationDescriptor_MarketName(), ecorePackage.getEString(), "marketName", null, 0, 1, MarketVesselAllocationDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMarketVesselAllocationDescriptor_SpotIndex(), ecorePackage.getEInt(), "spotIndex", null, 0, 1, MarketVesselAllocationDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fleetVesselAllocationDescriptorEClass, FleetVesselAllocationDescriptor.class, "FleetVesselAllocationDescriptor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFleetVesselAllocationDescriptor_Name(), ecorePackage.getEString(), "name", null, 0, 1, FleetVesselAllocationDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFleetVesselAllocationDescriptor_CharterIndex(), ecorePackage.getEInt(), "charterIndex", null, 0, 1, FleetVesselAllocationDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(positionDescriptorEClass, PositionDescriptor.class, "PositionDescriptor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPositionDescriptor_After(), ecorePackage.getEString(), "after", null, 0, 1, PositionDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPositionDescriptor_Before(), ecorePackage.getEString(), "before", null, 0, 1, PositionDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(viabilityModelEClass, ViabilityModel.class, "ViabilityModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getViabilityModel_Rows(), this.getViabilityRow(), null, "rows", null, 0, -1, ViabilityModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getViabilityModel_Markets(), theSpotMarketsPackage.getSpotMarket(), null, "markets", null, 0, -1, ViabilityModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(viabilityRowEClass, ViabilityRow.class, "ViabilityRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getViabilityRow_BuyOption(), this.getBuyOption(), null, "buyOption", null, 0, 1, ViabilityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getViabilityRow_SellOption(), this.getSellOption(), null, "sellOption", null, 0, 1, ViabilityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getViabilityRow_Shipping(), this.getShippingOption(), null, "shipping", null, 0, 1, ViabilityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getViabilityRow_LhsResults(), this.getViabilityResult(), null, "lhsResults", null, 0, -1, ViabilityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getViabilityRow_RhsResults(), this.getViabilityResult(), null, "rhsResults", null, 0, -1, ViabilityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getViabilityRow_Target(), ecorePackage.getEObject(), null, "target", null, 0, 1, ViabilityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViabilityRow_Price(), ecorePackage.getEDouble(), "price", null, 0, 1, ViabilityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViabilityRow_Eta(), theDateTimePackage.getLocalDate(), "eta", null, 0, 1, ViabilityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViabilityRow_ReferencePrice(), ecorePackage.getEDouble(), "referencePrice", null, 0, 1, ViabilityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViabilityRow_StartVolume(), ecorePackage.getELong(), "startVolume", null, 0, 1, ViabilityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(viabilityResultEClass, ViabilityResult.class, "ViabilityResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getViabilityResult_Target(), theSpotMarketsPackage.getSpotMarket(), null, "target", null, 0, 1, ViabilityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViabilityResult_EarliestETA(), theDateTimePackage.getLocalDate(), "earliestETA", null, 0, 1, ViabilityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViabilityResult_LatestETA(), theDateTimePackage.getLocalDate(), "latestETA", null, 0, 1, ViabilityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViabilityResult_EarliestVolume(), ecorePackage.getEInt(), "earliestVolume", null, 0, 1, ViabilityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViabilityResult_LatestVolume(), ecorePackage.getEInt(), "latestVolume", null, 0, 1, ViabilityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViabilityResult_EarliestPrice(), ecorePackage.getEDouble(), "earliestPrice", null, 0, 1, ViabilityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViabilityResult_LatestPrice(), ecorePackage.getEDouble(), "latestPrice", null, 0, 1, ViabilityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mtmModelEClass, MTMModel.class, "MTMModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMTMModel_Rows(), this.getMTMRow(), null, "rows", null, 0, -1, MTMModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMTMModel_Markets(), theSpotMarketsPackage.getSpotMarket(), null, "markets", null, 0, -1, MTMModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMTMModel_NominalMarkets(), theSpotMarketsPackage.getCharterInMarket(), null, "nominalMarkets", null, 0, -1, MTMModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mtmResultEClass, MTMResult.class, "MTMResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMTMResult_Target(), theSpotMarketsPackage.getSpotMarket(), null, "target", null, 0, 1, MTMResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMTMResult_EarliestETA(), theDateTimePackage.getLocalDate(), "earliestETA", null, 0, 1, MTMResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMTMResult_EarliestVolume(), ecorePackage.getEInt(), "earliestVolume", null, 0, 1, MTMResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMTMResult_EarliestPrice(), ecorePackage.getEDouble(), "earliestPrice", null, 0, 1, MTMResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMTMResult_Shipping(), this.getShippingOption(), null, "shipping", null, 0, 1, MTMResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMTMResult_ShippingCost(), ecorePackage.getEDouble(), "shippingCost", null, 0, 1, MTMResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mtmRowEClass, MTMRow.class, "MTMRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMTMRow_BuyOption(), this.getBuyOption(), null, "buyOption", null, 0, 1, MTMRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMTMRow_SellOption(), this.getSellOption(), null, "sellOption", null, 0, 1, MTMRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMTMRow_LhsResults(), this.getMTMResult(), null, "lhsResults", null, 0, -1, MTMRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMTMRow_RhsResults(), this.getMTMResult(), null, "rhsResults", null, 0, -1, MTMRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMTMRow_Target(), ecorePackage.getEObject(), null, "target", null, 0, 1, MTMRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMTMRow_Price(), ecorePackage.getEDouble(), "price", null, 0, 1, MTMRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMTMRow_Eta(), theDateTimePackage.getLocalDate(), "eta", null, 0, 1, MTMRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMTMRow_ReferencePrice(), ecorePackage.getEDouble(), "referencePrice", null, 0, 1, MTMRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMTMRow_StartVolume(), ecorePackage.getELong(), "startVolume", null, 0, 1, MTMRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(breakEvenAnalysisModelEClass, BreakEvenAnalysisModel.class, "BreakEvenAnalysisModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBreakEvenAnalysisModel_Rows(), this.getBreakEvenAnalysisRow(), null, "rows", null, 0, -1, BreakEvenAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBreakEvenAnalysisModel_Markets(), theSpotMarketsPackage.getSpotMarket(), null, "markets", null, 0, -1, BreakEvenAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(breakEvenAnalysisRowEClass, BreakEvenAnalysisRow.class, "BreakEvenAnalysisRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBreakEvenAnalysisRow_BuyOption(), this.getBuyOption(), null, "buyOption", null, 0, 1, BreakEvenAnalysisRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBreakEvenAnalysisRow_SellOption(), this.getSellOption(), null, "sellOption", null, 0, 1, BreakEvenAnalysisRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBreakEvenAnalysisRow_Shipping(), this.getShippingOption(), null, "shipping", null, 0, 1, BreakEvenAnalysisRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBreakEvenAnalysisRow_LhsResults(), this.getBreakEvenAnalysisResultSet(), null, "lhsResults", null, 0, -1, BreakEvenAnalysisRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBreakEvenAnalysisRow_RhsResults(), this.getBreakEvenAnalysisResultSet(), null, "rhsResults", null, 0, -1, BreakEvenAnalysisRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBreakEvenAnalysisRow_LhsBasedOn(), this.getBreakEvenAnalysisResult(), null, "lhsBasedOn", null, 0, 1, BreakEvenAnalysisRow.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBreakEvenAnalysisRow_RhsBasedOn(), this.getBreakEvenAnalysisResult(), null, "rhsBasedOn", null, 0, 1, BreakEvenAnalysisRow.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(breakEvenAnalysisResultSetEClass, BreakEvenAnalysisResultSet.class, "BreakEvenAnalysisResultSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBreakEvenAnalysisResultSet_BasedOn(), this.getBreakEvenAnalysisResult(), null, "basedOn", null, 0, 1, BreakEvenAnalysisResultSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBreakEvenAnalysisResultSet_Results(), this.getBreakEvenAnalysisResult(), null, "results", null, 0, -1, BreakEvenAnalysisResultSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBreakEvenAnalysisResultSet_Price(), ecorePackage.getEDouble(), "price", null, 0, 1, BreakEvenAnalysisResultSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(breakEvenAnalysisResultEClass, BreakEvenAnalysisResult.class, "BreakEvenAnalysisResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBreakEvenAnalysisResult_Target(), ecorePackage.getEObject(), null, "target", null, 0, 1, BreakEvenAnalysisResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBreakEvenAnalysisResult_Price(), ecorePackage.getEDouble(), "price", null, 0, 1, BreakEvenAnalysisResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBreakEvenAnalysisResult_Eta(), theDateTimePackage.getLocalDate(), "eta", null, 0, 1, BreakEvenAnalysisResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBreakEvenAnalysisResult_ReferencePrice(), ecorePackage.getEDouble(), "referencePrice", null, 0, 1, BreakEvenAnalysisResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(localDateTimeHolderEClass, LocalDateTimeHolder.class, "LocalDateTimeHolder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLocalDateTimeHolder_DateTime(), theDateTimePackage.getLocalDateTime(), "dateTime", null, 0, 1, LocalDateTimeHolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(commodityCurveOptionEClass, CommodityCurveOption.class, "CommodityCurveOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(commodityCurveOverlayEClass, CommodityCurveOverlay.class, "CommodityCurveOverlay", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCommodityCurveOverlay_ReferenceCurve(), thePricingPackage.getCommodityCurve(), null, "referenceCurve", null, 0, 1, CommodityCurveOverlay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommodityCurveOverlay_AlternativeCurves(), thePricingPackage.getYearMonthPointContainer(), null, "alternativeCurves", null, 0, -1, CommodityCurveOverlay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sensitivityModelEClass, SensitivityModel.class, "SensitivityModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSensitivityModel_SensitivityModel(), this.getOptionAnalysisModel(), null, "sensitivityModel", null, 0, 1, SensitivityModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSensitivityModel_SensitivitySolution(), this.getSensitivitySolutionSet(), null, "sensitivitySolution", null, 0, 1, SensitivityModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sensitivitySolutionSetEClass, SensitivitySolutionSet.class, "SensitivitySolutionSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSensitivitySolutionSet_PorfolioPnLResult(), this.getPortfolioSensitivityResult(), null, "porfolioPnLResult", null, 0, 1, SensitivitySolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSensitivitySolutionSet_CargoPnLResults(), this.getCargoPnLResult(), null, "cargoPnLResults", null, 0, -1, SensitivitySolutionSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractSensitivityResultEClass, AbstractSensitivityResult.class, "AbstractSensitivityResult", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAbstractSensitivityResult_MinPnL(), ecorePackage.getELong(), "minPnL", null, 0, 1, AbstractSensitivityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractSensitivityResult_MaxPnL(), ecorePackage.getELong(), "maxPnL", null, 0, 1, AbstractSensitivityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractSensitivityResult_AveragePnL(), ecorePackage.getELong(), "averagePnL", null, 0, 1, AbstractSensitivityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractSensitivityResult_Variance(), ecorePackage.getEDouble(), "variance", null, 0, 1, AbstractSensitivityResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portfolioSensitivityResultEClass, PortfolioSensitivityResult.class, "PortfolioSensitivityResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(cargoPnLResultEClass, CargoPnLResult.class, "CargoPnLResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCargoPnLResult_Cargo(), theCargoPackage.getCargo(), null, "cargo", null, 0, 1, CargoPnLResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(swapValueMatrixModelEClass, SwapValueMatrixModel.class, "SwapValueMatrixModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSwapValueMatrixModel_BaseLoad(), this.getBuyReference(), null, "baseLoad", null, 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSwapValueMatrixModel_BaseDischarge(), this.getSellReference(), null, "baseDischarge", null, 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSwapValueMatrixModel_BaseVesselCharter(), this.getExistingVesselCharterOption(), null, "baseVesselCharter", null, 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSwapValueMatrixModel_SwapLoadMarket(), this.getBuyMarket(), null, "swapLoadMarket", null, 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSwapValueMatrixModel_SwapDischargeMarket(), this.getSellMarket(), null, "swapDischargeMarket", null, 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixModel_BaseDischargeMinPrice(), ecorePackage.getEInt(), "baseDischargeMinPrice", null, 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixModel_BaseDischargeMaxPrice(), ecorePackage.getEInt(), "baseDischargeMaxPrice", null, 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixModel_BaseDischargeStepSize(), ecorePackage.getEInt(), "baseDischargeStepSize", "1", 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixModel_MarketMinPrice(), ecorePackage.getEInt(), "marketMinPrice", null, 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixModel_MarketMaxPrice(), ecorePackage.getEInt(), "marketMaxPrice", null, 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixModel_MarketStepSize(), ecorePackage.getEInt(), "marketStepSize", "1", 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixModel_SwapFee(), ecorePackage.getEDouble(), "swapFee", null, 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSwapValueMatrixModel_SwapValueMatrixResult(), this.getSwapValueMatrixResultSet(), null, "swapValueMatrixResult", null, 0, 1, SwapValueMatrixModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(swapValueMatrixResultEClass, SwapValueMatrixResult.class, "SwapValueMatrixResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSwapValueMatrixResult_BaseDischargePrice(), ecorePackage.getEInt(), "baseDischargePrice", null, 0, 1, SwapValueMatrixResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixResult_SwapMarketPrice(), ecorePackage.getEInt(), "swapMarketPrice", null, 0, 1, SwapValueMatrixResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixResult_SwapPnlMinusBasePnl(), ecorePackage.getELong(), "swapPnlMinusBasePnl", null, 0, 1, SwapValueMatrixResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixResult_BaseLoadPrice(), ecorePackage.getEDouble(), "baseLoadPrice", null, 0, 1, SwapValueMatrixResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixResult_SwapFobLoadPrice(), ecorePackage.getEDouble(), "swapFobLoadPrice", null, 0, 1, SwapValueMatrixResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixResult_BaseFobLoadVolume(), ecorePackage.getEInt(), "baseFobLoadVolume", null, 0, 1, SwapValueMatrixResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixResult_SwapFobLoadVolume(), ecorePackage.getEInt(), "swapFobLoadVolume", null, 0, 1, SwapValueMatrixResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixResult_MarketBuyVolume(), ecorePackage.getEInt(), "marketBuyVolume", null, 0, 1, SwapValueMatrixResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixResult_MarketSellVolume(), ecorePackage.getEInt(), "marketSellVolume", null, 0, 1, SwapValueMatrixResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixResult_BaseDesSellVolume(), ecorePackage.getEInt(), "baseDesSellVolume", null, 0, 1, SwapValueMatrixResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(swapValueMatrixResultSetEClass, SwapValueMatrixResultSet.class, "SwapValueMatrixResultSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSwapValueMatrixResultSet_Results(), this.getSwapValueMatrixResult(), null, "results", null, 0, -1, SwapValueMatrixResultSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwapValueMatrixResultSet_SwapFee(), ecorePackage.getEDouble(), "swapFee", null, 0, 1, SwapValueMatrixResultSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSwapValueMatrixResultSet_GeneratedSpotLoadSlot(), theCargoPackage.getSpotLoadSlot(), null, "generatedSpotLoadSlot", null, 0, 1, SwapValueMatrixResultSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSwapValueMatrixResultSet_GeneratedSpotDischargeSlot(), theCargoPackage.getSpotDischargeSlot(), null, "generatedSpotDischargeSlot", null, 0, 1, SwapValueMatrixResultSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(volumeModeEEnum, VolumeMode.class, "VolumeMode");
		addEEnumLiteral(volumeModeEEnum, VolumeMode.NOT_SPECIFIED);
		addEEnumLiteral(volumeModeEEnum, VolumeMode.FIXED);
		addEEnumLiteral(volumeModeEEnum, VolumeMode.RANGE);

		initEEnum(slotTypeEEnum, SlotType.class, "SlotType");
		addEEnumLiteral(slotTypeEEnum, SlotType.FOB_PURCHASE);
		addEEnumLiteral(slotTypeEEnum, SlotType.FOB_SALE);
		addEEnumLiteral(slotTypeEEnum, SlotType.DES_PURCHASE);
		addEEnumLiteral(slotTypeEEnum, SlotType.DES_SALE);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/mmxcore/validation/NamedObject
		createNamedObjectAnnotations();
		// http://www.mmxlabs.com/models/pricing/expressionType
		createExpressionTypeAnnotations();
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/ui/numberFormat</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNumberFormatAnnotations() {
		String source = "http://www.mmxlabs.com/models/ui/numberFormat";
		addAnnotation
		  (getBuyOpportunity_Cv(),
		   source,
		   new String[] {
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getBuyOpportunity_WindowSize(),
		   source,
		   new String[] {
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getBuyOpportunity_WindowSizeUnits(),
		   source,
		   new String[] {
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getSellOpportunity_WindowSize(),
		   source,
		   new String[] {
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getSellOpportunity_WindowSizeUnits(),
		   source,
		   new String[] {
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getSwapValueMatrixModel_SwapFee(),
		   source,
		   new String[] {
			   "formatString", "##.##"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/pricing/expressionType</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExpressionTypeAnnotations() {
		String source = "http://www.mmxlabs.com/models/pricing/expressionType";
		addAnnotation
		  (getBuyOpportunity_PriceExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getBuyOpportunity_CancellationExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getBuyOpportunity_MiscCosts(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getSellOpportunity_PriceExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getSellOpportunity_CancellationExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getSellOpportunity_MiscCosts(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getSimpleVesselCharterOption_HireCost(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
		addAnnotation
		  (getOptionalSimpleVesselCharterOption_BallastBonus(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
		addAnnotation
		  (getOptionalSimpleVesselCharterOption_RepositioningFee(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
		addAnnotation
		  (getRoundTripShippingOption_HireCost(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/mmxcore/validation/NamedObject</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNamedObjectAnnotations() {
		String source = "http://www.mmxlabs.com/models/mmxcore/validation/NamedObject";
		addAnnotation
		  (analyticsModelEClass,
		   source,
		   new String[] {
			   "nonUniqueChildren", "true"
		   });
		addAnnotation
		  (sandboxResultEClass,
		   source,
		   new String[] {
			   "nonUniqueChildren", "true"
		   });
		addAnnotation
		  (abstractSolutionSetEClass,
		   source,
		   new String[] {
			   "nonUniqueChildren", "true"
		   });
		addAnnotation
		  (getAbstractSolutionSet_ExtraSlots(),
		   source,
		   new String[] {
			   "nonUniqueChildren", "true"
		   });
		addAnnotation
		  (getAbstractSolutionSet_ExtraCharterInMarkets(),
		   source,
		   new String[] {
			   "nonUniqueChildren", "true"
		   });
		addAnnotation
		  (actionableSetPlanEClass,
		   source,
		   new String[] {
			   "nonUniqueChildren", "true"
		   });
		addAnnotation
		  (slotInsertionOptionsEClass,
		   source,
		   new String[] {
			   "nonUniqueChildren", "true"
		   });
	}

} //AnalyticsPackageImpl
