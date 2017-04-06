/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.analytics.ActionableSet;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.AnalysisResultDetail;
import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.CargoSandbox;
import com.mmxlabs.models.lng.analytics.CostComponent;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.FuelCost;
import com.mmxlabs.models.lng.analytics.Journey;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ProfitAndLossResult;
import com.mmxlabs.models.lng.analytics.ProvisionalCargo;
import com.mmxlabs.models.lng.analytics.ResultContainer;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SlotInsertionOption;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.analytics.Visit;
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.lng.analytics.Voyage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
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
	private EClass unitCostMatrixEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unitCostLineEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass voyageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass visitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass costComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fuelCostEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass journeyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass shippingCostPlanEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass shippingCostRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoSandboxEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass provisionalCargoEClass = null;

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
	private EClass baseCaseRowEClass = null;

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
	private EClass shippingOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fleetShippingOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass optionalAvailabilityShippingOptionEClass = null;

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
	private EClass analysisResultRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resultContainerEClass = null;

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
	private EClass optionAnalysisModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resultSetEClass = null;

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
	private EClass actionableSetPlanEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass actionableSetEClass = null;

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
	private EClass slotInsertionOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum destinationTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum volumeModeEEnum = null;

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
		AnalyticsPackageImpl theAnalyticsPackage = (AnalyticsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof AnalyticsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new AnalyticsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		SchedulePackage.eINSTANCE.eClass();

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
	public EReference getAnalyticsModel_RoundTripMatrices() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAnalyticsModel_SelectedMatrix() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAnalyticsModel_ShippingCostPlans() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAnalyticsModel_CargoSandboxes() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnalyticsModel_OptionModels() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnalyticsModel_InsertionOptions() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnalyticsModel_ActionableSetPlans() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getUnitCostMatrix() {
		return unitCostMatrixEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getUnitCostMatrix_FromPorts() {
		return (EReference)unitCostMatrixEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getUnitCostMatrix_ToPorts() {
		return (EReference)unitCostMatrixEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getUnitCostMatrix_Vessel() {
		return (EReference)unitCostMatrixEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_NotionalDayRate() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_Speed() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_RoundTrip() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_MinimumLoad() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_MaximumLoad() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_MinimumDischarge() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_MaximumDischarge() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_RetainHeel() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_CargoPrice() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_BaseFuelPrice() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_CvValue() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getUnitCostMatrix_CostLines() {
		return (EReference)unitCostMatrixEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getUnitCostMatrix_AllowedRoutes() {
		return (EReference)unitCostMatrixEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_RevenueShare() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_LadenTimeAllowance() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostMatrix_BallastTimeAllowance() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getUnitCostLine() {
		return unitCostLineEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostLine_UnitCost() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostLine_MmbtuDelivered() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getUnitCostLine_From() {
		return (EReference)unitCostLineEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getUnitCostLine_To() {
		return (EReference)unitCostLineEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostLine_Duration() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostLine_VolumeLoaded() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostLine_VolumeDischarged() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostLine_HireCost() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostLine_FuelCost() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostLine_CanalCost() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getUnitCostLine_CostComponents() {
		return (EReference)unitCostLineEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostLine_PortCost() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitCostLine_Profit() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVoyage() {
		return voyageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVoyage_Route() {
		return (EReference)voyageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVoyage_RouteCost() {
		return (EAttribute)voyageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVoyage_Speed() {
		return (EAttribute)voyageEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVoyage_Distance() {
		return (EAttribute)voyageEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVoyage_IdleTime() {
		return (EAttribute)voyageEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVoyage_TravelTime() {
		return (EAttribute)voyageEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVisit() {
		return visitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVisit_PortCost() {
		return (EAttribute)visitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCostComponent() {
		return costComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCostComponent_Duration() {
		return (EAttribute)costComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCostComponent_HireCost() {
		return (EAttribute)costComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCostComponent_FuelCosts() {
		return (EReference)costComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFuelCost() {
		return fuelCostEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFuelCost_Name() {
		return (EAttribute)fuelCostEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFuelCost_Unit() {
		return (EAttribute)fuelCostEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFuelCost_Quantity() {
		return (EAttribute)fuelCostEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFuelCost_Cost() {
		return (EAttribute)fuelCostEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getJourney() {
		return journeyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getJourney_From() {
		return (EReference)journeyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getJourney_To() {
		return (EReference)journeyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getShippingCostPlan() {
		return shippingCostPlanEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getShippingCostPlan_Vessel() {
		return (EReference)shippingCostPlanEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getShippingCostPlan_NotionalDayRate() {
		return (EAttribute)shippingCostPlanEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getShippingCostPlan_BaseFuelPrice() {
		return (EAttribute)shippingCostPlanEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getShippingCostPlan_Rows() {
		return (EReference)shippingCostPlanEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getShippingCostRow() {
		return shippingCostRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getShippingCostRow_Port() {
		return (EReference)shippingCostRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getShippingCostRow_Date() {
		return (EAttribute)shippingCostRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getShippingCostRow_CargoPrice() {
		return (EAttribute)shippingCostRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getShippingCostRow_CvValue() {
		return (EAttribute)shippingCostRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getShippingCostRow_DestinationType() {
		return (EAttribute)shippingCostRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getShippingCostRow_HeelVolume() {
		return (EAttribute)shippingCostRowEClass.getEStructuralFeatures().get(5);
	}

		/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getShippingCostRow_IncludePortCosts() {
		return (EAttribute)shippingCostRowEClass.getEStructuralFeatures().get(6);
	}


	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoSandbox() {
		return cargoSandboxEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoSandbox_Cargoes() {
		return (EReference)cargoSandboxEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProvisionalCargo() {
		return provisionalCargoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getProvisionalCargo_Buy() {
		return (EReference)provisionalCargoEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getProvisionalCargo_Sell() {
		return (EReference)provisionalCargoEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getProvisionalCargo_Vessel() {
		return (EReference)provisionalCargoEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getProvisionalCargo_PortfolioModel() {
		return (EReference)provisionalCargoEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBuyOption() {
		return buyOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSellOption() {
		return sellOptionEClass;
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
	public EAttribute getBuyOpportunity_DesPurchase() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBuyOpportunity_Port() {
		return (EReference)buyOpportunityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBuyOpportunity_Contract() {
		return (EReference)buyOpportunityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_Date() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_PriceExpression() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBuyOpportunity_Entity() {
		return (EReference)buyOpportunityEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBuyOpportunity_Cv() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBuyOpportunity_CancellationExpression() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBuyOpportunity_MiscCosts() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBuyOpportunity_VolumeMode() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBuyOpportunity_VolumeUnits() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBuyOpportunity_MinVolume() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBuyOpportunity_MaxVolume() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(12);
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
	public EAttribute getSellOpportunity_FobSale() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSellOpportunity_Port() {
		return (EReference)sellOpportunityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSellOpportunity_Contract() {
		return (EReference)sellOpportunityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_Date() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_PriceExpression() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSellOpportunity_Entity() {
		return (EReference)sellOpportunityEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSellOpportunity_CancellationExpression() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSellOpportunity_MiscCosts() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSellOpportunity_VolumeMode() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSellOpportunity_VolumeUnits() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSellOpportunity_MinVolume() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSellOpportunity_MaxVolume() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBuyMarket() {
		return buyMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBuyMarket_Market() {
		return (EReference)buyMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSellMarket() {
		return sellMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSellMarket_Market() {
		return (EReference)sellMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBuyReference() {
		return buyReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBuyReference_Slot() {
		return (EReference)buyReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSellReference() {
		return sellReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSellReference_Slot() {
		return (EReference)sellReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBaseCaseRow() {
		return baseCaseRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBaseCaseRow_BuyOption() {
		return (EReference)baseCaseRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBaseCaseRow_SellOption() {
		return (EReference)baseCaseRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBaseCaseRow_Shipping() {
		return (EReference)baseCaseRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPartialCaseRow() {
		return partialCaseRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPartialCaseRow_BuyOptions() {
		return (EReference)partialCaseRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPartialCaseRow_SellOptions() {
		return (EReference)partialCaseRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPartialCaseRow_Shipping() {
		return (EReference)partialCaseRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getShippingOption() {
		return shippingOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFleetShippingOption() {
		return fleetShippingOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetShippingOption_Vessel() {
		return (EReference)fleetShippingOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFleetShippingOption_HireCost() {
		return (EAttribute)fleetShippingOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetShippingOption_Entity() {
		return (EReference)fleetShippingOptionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFleetShippingOption_UseSafetyHeel() {
		return (EAttribute)fleetShippingOptionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOptionalAvailabilityShippingOption() {
		return optionalAvailabilityShippingOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOptionalAvailabilityShippingOption_BallastBonus() {
		return (EAttribute)optionalAvailabilityShippingOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOptionalAvailabilityShippingOption_RepositioningFee() {
		return (EAttribute)optionalAvailabilityShippingOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOptionalAvailabilityShippingOption_Start() {
		return (EAttribute)optionalAvailabilityShippingOptionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOptionalAvailabilityShippingOption_End() {
		return (EAttribute)optionalAvailabilityShippingOptionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptionalAvailabilityShippingOption_StartPort() {
		return (EReference)optionalAvailabilityShippingOptionEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptionalAvailabilityShippingOption_EndPort() {
		return (EReference)optionalAvailabilityShippingOptionEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRoundTripShippingOption() {
		return roundTripShippingOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRoundTripShippingOption_VesselClass() {
		return (EReference)roundTripShippingOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRoundTripShippingOption_HireCost() {
		return (EAttribute)roundTripShippingOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNominatedShippingOption() {
		return nominatedShippingOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNominatedShippingOption_NominatedVessel() {
		return (EReference)nominatedShippingOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnalysisResultRow() {
		return analysisResultRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnalysisResultRow_BuyOption() {
		return (EReference)analysisResultRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnalysisResultRow_SellOption() {
		return (EReference)analysisResultRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnalysisResultRow_ResultDetail() {
		return (EReference)analysisResultRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnalysisResultRow_Shipping() {
		return (EReference)analysisResultRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnalysisResultRow_ResultDetails() {
		return (EReference)analysisResultRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getResultContainer() {
		return resultContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getResultContainer_CargoAllocation() {
		return (EReference)resultContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getResultContainer_OpenSlotAllocations() {
		return (EReference)resultContainerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getResultContainer_SlotAllocations() {
		return (EReference)resultContainerEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getResultContainer_Events() {
		return (EReference)resultContainerEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnalysisResultDetail() {
		return analysisResultDetailEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProfitAndLossResult() {
		return profitAndLossResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProfitAndLossResult_Value() {
		return (EAttribute)profitAndLossResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBreakEvenResult() {
		return breakEvenResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBreakEvenResult_Price() {
		return (EAttribute)breakEvenResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBreakEvenResult_PriceString() {
		return (EAttribute)breakEvenResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBreakEvenResult_CargoPNL() {
		return (EAttribute)breakEvenResultEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOptionAnalysisModel() {
		return optionAnalysisModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptionAnalysisModel_BaseCase() {
		return (EReference)optionAnalysisModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptionAnalysisModel_ShippingTemplates() {
		return (EReference)optionAnalysisModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptionAnalysisModel_Buys() {
		return (EReference)optionAnalysisModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptionAnalysisModel_Sells() {
		return (EReference)optionAnalysisModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBaseCase() {
		return baseCaseEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBaseCase_BaseCase() {
		return (EReference)baseCaseEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBaseCase_ProfitAndLoss() {
		return (EAttribute)baseCaseEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPartialCase() {
		return partialCaseEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPartialCase_PartialCase() {
		return (EReference)partialCaseEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getActionableSetPlan() {
		return actionableSetPlanEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getActionableSetPlan_ActionSets() {
		return (EReference)actionableSetPlanEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getActionableSetPlan_ExtraSlots() {
		return (EReference)actionableSetPlanEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getActionableSet() {
		return actionableSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getActionableSet_ScheduleModel() {
		return (EReference)actionableSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSlotInsertionOptions() {
		return slotInsertionOptionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotInsertionOptions_SlotsInserted() {
		return (EReference)slotInsertionOptionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotInsertionOptions_InsertionOptions() {
		return (EReference)slotInsertionOptionsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotInsertionOptions_ExtraSlots() {
		return (EReference)slotInsertionOptionsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSlotInsertionOption() {
		return slotInsertionOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotInsertionOption_ScheduleModel() {
		return (EReference)slotInsertionOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptionAnalysisModel_PartialCase() {
		return (EReference)optionAnalysisModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptionAnalysisModel_ResultSets() {
		return (EReference)optionAnalysisModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOptionAnalysisModel_UseTargetPNL() {
		return (EAttribute)optionAnalysisModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptionAnalysisModel_Children() {
		return (EReference)optionAnalysisModelEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getResultSet() {
		return resultSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getResultSet_Rows() {
		return (EReference)resultSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getResultSet_ProfitAndLoss() {
		return (EAttribute)resultSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getDestinationType() {
		return destinationTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getVolumeMode() {
		return volumeModeEEnum;
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
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__ROUND_TRIP_MATRICES);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__SELECTED_MATRIX);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__SHIPPING_COST_PLANS);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__CARGO_SANDBOXES);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__OPTION_MODELS);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__INSERTION_OPTIONS);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__ACTIONABLE_SET_PLANS);

		unitCostMatrixEClass = createEClass(UNIT_COST_MATRIX);
		createEReference(unitCostMatrixEClass, UNIT_COST_MATRIX__FROM_PORTS);
		createEReference(unitCostMatrixEClass, UNIT_COST_MATRIX__TO_PORTS);
		createEReference(unitCostMatrixEClass, UNIT_COST_MATRIX__VESSEL);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__NOTIONAL_DAY_RATE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__SPEED);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__ROUND_TRIP);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__MINIMUM_LOAD);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__MAXIMUM_LOAD);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__MINIMUM_DISCHARGE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__MAXIMUM_DISCHARGE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__RETAIN_HEEL);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__CARGO_PRICE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__BASE_FUEL_PRICE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__CV_VALUE);
		createEReference(unitCostMatrixEClass, UNIT_COST_MATRIX__COST_LINES);
		createEReference(unitCostMatrixEClass, UNIT_COST_MATRIX__ALLOWED_ROUTES);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__REVENUE_SHARE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__LADEN_TIME_ALLOWANCE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__BALLAST_TIME_ALLOWANCE);

		unitCostLineEClass = createEClass(UNIT_COST_LINE);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__UNIT_COST);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__MMBTU_DELIVERED);
		createEReference(unitCostLineEClass, UNIT_COST_LINE__FROM);
		createEReference(unitCostLineEClass, UNIT_COST_LINE__TO);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__DURATION);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__VOLUME_LOADED);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__VOLUME_DISCHARGED);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__HIRE_COST);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__FUEL_COST);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__CANAL_COST);
		createEReference(unitCostLineEClass, UNIT_COST_LINE__COST_COMPONENTS);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__PORT_COST);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__PROFIT);

		voyageEClass = createEClass(VOYAGE);
		createEReference(voyageEClass, VOYAGE__ROUTE);
		createEAttribute(voyageEClass, VOYAGE__ROUTE_COST);
		createEAttribute(voyageEClass, VOYAGE__SPEED);
		createEAttribute(voyageEClass, VOYAGE__DISTANCE);
		createEAttribute(voyageEClass, VOYAGE__IDLE_TIME);
		createEAttribute(voyageEClass, VOYAGE__TRAVEL_TIME);

		visitEClass = createEClass(VISIT);
		createEAttribute(visitEClass, VISIT__PORT_COST);

		costComponentEClass = createEClass(COST_COMPONENT);
		createEAttribute(costComponentEClass, COST_COMPONENT__DURATION);
		createEAttribute(costComponentEClass, COST_COMPONENT__HIRE_COST);
		createEReference(costComponentEClass, COST_COMPONENT__FUEL_COSTS);

		fuelCostEClass = createEClass(FUEL_COST);
		createEAttribute(fuelCostEClass, FUEL_COST__NAME);
		createEAttribute(fuelCostEClass, FUEL_COST__UNIT);
		createEAttribute(fuelCostEClass, FUEL_COST__QUANTITY);
		createEAttribute(fuelCostEClass, FUEL_COST__COST);

		journeyEClass = createEClass(JOURNEY);
		createEReference(journeyEClass, JOURNEY__FROM);
		createEReference(journeyEClass, JOURNEY__TO);

		shippingCostPlanEClass = createEClass(SHIPPING_COST_PLAN);
		createEReference(shippingCostPlanEClass, SHIPPING_COST_PLAN__VESSEL);
		createEAttribute(shippingCostPlanEClass, SHIPPING_COST_PLAN__NOTIONAL_DAY_RATE);
		createEAttribute(shippingCostPlanEClass, SHIPPING_COST_PLAN__BASE_FUEL_PRICE);
		createEReference(shippingCostPlanEClass, SHIPPING_COST_PLAN__ROWS);

		shippingCostRowEClass = createEClass(SHIPPING_COST_ROW);
		createEReference(shippingCostRowEClass, SHIPPING_COST_ROW__PORT);
		createEAttribute(shippingCostRowEClass, SHIPPING_COST_ROW__DATE);
		createEAttribute(shippingCostRowEClass, SHIPPING_COST_ROW__CARGO_PRICE);
		createEAttribute(shippingCostRowEClass, SHIPPING_COST_ROW__CV_VALUE);
		createEAttribute(shippingCostRowEClass, SHIPPING_COST_ROW__DESTINATION_TYPE);
		createEAttribute(shippingCostRowEClass, SHIPPING_COST_ROW__HEEL_VOLUME);
		createEAttribute(shippingCostRowEClass, SHIPPING_COST_ROW__INCLUDE_PORT_COSTS);

		cargoSandboxEClass = createEClass(CARGO_SANDBOX);
		createEReference(cargoSandboxEClass, CARGO_SANDBOX__CARGOES);

		provisionalCargoEClass = createEClass(PROVISIONAL_CARGO);
		createEReference(provisionalCargoEClass, PROVISIONAL_CARGO__BUY);
		createEReference(provisionalCargoEClass, PROVISIONAL_CARGO__SELL);
		createEReference(provisionalCargoEClass, PROVISIONAL_CARGO__VESSEL);
		createEReference(provisionalCargoEClass, PROVISIONAL_CARGO__PORTFOLIO_MODEL);

		buyOptionEClass = createEClass(BUY_OPTION);

		sellOptionEClass = createEClass(SELL_OPTION);

		buyOpportunityEClass = createEClass(BUY_OPPORTUNITY);
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
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__VOLUME_UNITS);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__MIN_VOLUME);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__MAX_VOLUME);

		sellOpportunityEClass = createEClass(SELL_OPPORTUNITY);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__FOB_SALE);
		createEReference(sellOpportunityEClass, SELL_OPPORTUNITY__PORT);
		createEReference(sellOpportunityEClass, SELL_OPPORTUNITY__CONTRACT);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__DATE);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__PRICE_EXPRESSION);
		createEReference(sellOpportunityEClass, SELL_OPPORTUNITY__ENTITY);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__CANCELLATION_EXPRESSION);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__MISC_COSTS);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__VOLUME_MODE);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__VOLUME_UNITS);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__MIN_VOLUME);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__MAX_VOLUME);

		buyMarketEClass = createEClass(BUY_MARKET);
		createEReference(buyMarketEClass, BUY_MARKET__MARKET);

		sellMarketEClass = createEClass(SELL_MARKET);
		createEReference(sellMarketEClass, SELL_MARKET__MARKET);

		buyReferenceEClass = createEClass(BUY_REFERENCE);
		createEReference(buyReferenceEClass, BUY_REFERENCE__SLOT);

		sellReferenceEClass = createEClass(SELL_REFERENCE);
		createEReference(sellReferenceEClass, SELL_REFERENCE__SLOT);

		baseCaseRowEClass = createEClass(BASE_CASE_ROW);
		createEReference(baseCaseRowEClass, BASE_CASE_ROW__BUY_OPTION);
		createEReference(baseCaseRowEClass, BASE_CASE_ROW__SELL_OPTION);
		createEReference(baseCaseRowEClass, BASE_CASE_ROW__SHIPPING);

		partialCaseRowEClass = createEClass(PARTIAL_CASE_ROW);
		createEReference(partialCaseRowEClass, PARTIAL_CASE_ROW__BUY_OPTIONS);
		createEReference(partialCaseRowEClass, PARTIAL_CASE_ROW__SELL_OPTIONS);
		createEReference(partialCaseRowEClass, PARTIAL_CASE_ROW__SHIPPING);

		shippingOptionEClass = createEClass(SHIPPING_OPTION);

		fleetShippingOptionEClass = createEClass(FLEET_SHIPPING_OPTION);
		createEReference(fleetShippingOptionEClass, FLEET_SHIPPING_OPTION__VESSEL);
		createEAttribute(fleetShippingOptionEClass, FLEET_SHIPPING_OPTION__HIRE_COST);
		createEReference(fleetShippingOptionEClass, FLEET_SHIPPING_OPTION__ENTITY);
		createEAttribute(fleetShippingOptionEClass, FLEET_SHIPPING_OPTION__USE_SAFETY_HEEL);

		optionalAvailabilityShippingOptionEClass = createEClass(OPTIONAL_AVAILABILITY_SHIPPING_OPTION);
		createEAttribute(optionalAvailabilityShippingOptionEClass, OPTIONAL_AVAILABILITY_SHIPPING_OPTION__BALLAST_BONUS);
		createEAttribute(optionalAvailabilityShippingOptionEClass, OPTIONAL_AVAILABILITY_SHIPPING_OPTION__REPOSITIONING_FEE);
		createEAttribute(optionalAvailabilityShippingOptionEClass, OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START);
		createEAttribute(optionalAvailabilityShippingOptionEClass, OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END);
		createEReference(optionalAvailabilityShippingOptionEClass, OPTIONAL_AVAILABILITY_SHIPPING_OPTION__START_PORT);
		createEReference(optionalAvailabilityShippingOptionEClass, OPTIONAL_AVAILABILITY_SHIPPING_OPTION__END_PORT);

		roundTripShippingOptionEClass = createEClass(ROUND_TRIP_SHIPPING_OPTION);
		createEReference(roundTripShippingOptionEClass, ROUND_TRIP_SHIPPING_OPTION__VESSEL_CLASS);
		createEAttribute(roundTripShippingOptionEClass, ROUND_TRIP_SHIPPING_OPTION__HIRE_COST);

		nominatedShippingOptionEClass = createEClass(NOMINATED_SHIPPING_OPTION);
		createEReference(nominatedShippingOptionEClass, NOMINATED_SHIPPING_OPTION__NOMINATED_VESSEL);

		analysisResultRowEClass = createEClass(ANALYSIS_RESULT_ROW);
		createEReference(analysisResultRowEClass, ANALYSIS_RESULT_ROW__BUY_OPTION);
		createEReference(analysisResultRowEClass, ANALYSIS_RESULT_ROW__SELL_OPTION);
		createEReference(analysisResultRowEClass, ANALYSIS_RESULT_ROW__RESULT_DETAIL);
		createEReference(analysisResultRowEClass, ANALYSIS_RESULT_ROW__SHIPPING);
		createEReference(analysisResultRowEClass, ANALYSIS_RESULT_ROW__RESULT_DETAILS);

		resultContainerEClass = createEClass(RESULT_CONTAINER);
		createEReference(resultContainerEClass, RESULT_CONTAINER__CARGO_ALLOCATION);
		createEReference(resultContainerEClass, RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS);
		createEReference(resultContainerEClass, RESULT_CONTAINER__SLOT_ALLOCATIONS);
		createEReference(resultContainerEClass, RESULT_CONTAINER__EVENTS);

		analysisResultDetailEClass = createEClass(ANALYSIS_RESULT_DETAIL);

		profitAndLossResultEClass = createEClass(PROFIT_AND_LOSS_RESULT);
		createEAttribute(profitAndLossResultEClass, PROFIT_AND_LOSS_RESULT__VALUE);

		breakEvenResultEClass = createEClass(BREAK_EVEN_RESULT);
		createEAttribute(breakEvenResultEClass, BREAK_EVEN_RESULT__PRICE);
		createEAttribute(breakEvenResultEClass, BREAK_EVEN_RESULT__PRICE_STRING);
		createEAttribute(breakEvenResultEClass, BREAK_EVEN_RESULT__CARGO_PNL);

		optionAnalysisModelEClass = createEClass(OPTION_ANALYSIS_MODEL);
		createEReference(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__BUYS);
		createEReference(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__SELLS);
		createEReference(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__BASE_CASE);
		createEReference(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
		createEReference(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
		createEReference(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__RESULT_SETS);
		createEAttribute(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__USE_TARGET_PNL);
		createEReference(optionAnalysisModelEClass, OPTION_ANALYSIS_MODEL__CHILDREN);

		resultSetEClass = createEClass(RESULT_SET);
		createEReference(resultSetEClass, RESULT_SET__ROWS);
		createEAttribute(resultSetEClass, RESULT_SET__PROFIT_AND_LOSS);

		baseCaseEClass = createEClass(BASE_CASE);
		createEReference(baseCaseEClass, BASE_CASE__BASE_CASE);
		createEAttribute(baseCaseEClass, BASE_CASE__PROFIT_AND_LOSS);

		partialCaseEClass = createEClass(PARTIAL_CASE);
		createEReference(partialCaseEClass, PARTIAL_CASE__PARTIAL_CASE);

		actionableSetPlanEClass = createEClass(ACTIONABLE_SET_PLAN);
		createEReference(actionableSetPlanEClass, ACTIONABLE_SET_PLAN__ACTION_SETS);
		createEReference(actionableSetPlanEClass, ACTIONABLE_SET_PLAN__EXTRA_SLOTS);

		actionableSetEClass = createEClass(ACTIONABLE_SET);
		createEReference(actionableSetEClass, ACTIONABLE_SET__SCHEDULE_MODEL);

		slotInsertionOptionsEClass = createEClass(SLOT_INSERTION_OPTIONS);
		createEReference(slotInsertionOptionsEClass, SLOT_INSERTION_OPTIONS__SLOTS_INSERTED);
		createEReference(slotInsertionOptionsEClass, SLOT_INSERTION_OPTIONS__INSERTION_OPTIONS);
		createEReference(slotInsertionOptionsEClass, SLOT_INSERTION_OPTIONS__EXTRA_SLOTS);

		slotInsertionOptionEClass = createEClass(SLOT_INSERTION_OPTION);
		createEReference(slotInsertionOptionEClass, SLOT_INSERTION_OPTION__SCHEDULE_MODEL);

		// Create enums
		destinationTypeEEnum = createEEnum(DESTINATION_TYPE);
		volumeModeEEnum = createEEnum(VOLUME_MODE);
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
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		SpotMarketsPackage theSpotMarketsPackage = (SpotMarketsPackage)EPackage.Registry.INSTANCE.getEPackage(SpotMarketsPackage.eNS_URI);
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);
		SchedulePackage theSchedulePackage = (SchedulePackage)EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		analyticsModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		unitCostMatrixEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		unitCostMatrixEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		unitCostLineEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		voyageEClass.getESuperTypes().add(this.getCostComponent());
		visitEClass.getESuperTypes().add(this.getCostComponent());
		shippingCostPlanEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		shippingCostRowEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		cargoSandboxEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		provisionalCargoEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		buyOpportunityEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		buyOpportunityEClass.getESuperTypes().add(this.getBuyOption());
		sellOpportunityEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		sellOpportunityEClass.getESuperTypes().add(this.getSellOption());
		buyMarketEClass.getESuperTypes().add(this.getBuyOption());
		sellMarketEClass.getESuperTypes().add(this.getSellOption());
		buyReferenceEClass.getESuperTypes().add(this.getBuyOption());
		sellReferenceEClass.getESuperTypes().add(this.getSellOption());
		fleetShippingOptionEClass.getESuperTypes().add(this.getShippingOption());
		optionalAvailabilityShippingOptionEClass.getESuperTypes().add(this.getFleetShippingOption());
		roundTripShippingOptionEClass.getESuperTypes().add(this.getShippingOption());
		nominatedShippingOptionEClass.getESuperTypes().add(this.getShippingOption());
		profitAndLossResultEClass.getESuperTypes().add(this.getAnalysisResultDetail());
		breakEvenResultEClass.getESuperTypes().add(this.getAnalysisResultDetail());
		optionAnalysisModelEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());

		// Initialize classes and features; add operations and parameters
		initEClass(analyticsModelEClass, AnalyticsModel.class, "AnalyticsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAnalyticsModel_RoundTripMatrices(), this.getUnitCostMatrix(), null, "roundTripMatrices", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_SelectedMatrix(), this.getUnitCostMatrix(), null, "selectedMatrix", null, 1, 1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_ShippingCostPlans(), this.getShippingCostPlan(), null, "shippingCostPlans", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_CargoSandboxes(), this.getCargoSandbox(), null, "cargoSandboxes", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_OptionModels(), this.getOptionAnalysisModel(), null, "optionModels", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_InsertionOptions(), this.getSlotInsertionOptions(), null, "insertionOptions", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_ActionableSetPlans(), this.getActionableSetPlan(), null, "actionableSetPlans", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unitCostMatrixEClass, UnitCostMatrix.class, "UnitCostMatrix", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		EGenericType g1 = createEGenericType(theTypesPackage.getAPortSet());
		EGenericType g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getUnitCostMatrix_FromPorts(), g1, null, "fromPorts", null, 0, -1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getUnitCostMatrix_ToPorts(), g1, null, "toPorts", null, 0, -1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnitCostMatrix_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_NotionalDayRate(), ecorePackage.getEInt(), "notionalDayRate", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_Speed(), ecorePackage.getEDouble(), "speed", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_RoundTrip(), ecorePackage.getEBoolean(), "roundTrip", "true", 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_MinimumLoad(), ecorePackage.getEInt(), "minimumLoad", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_MaximumLoad(), ecorePackage.getEInt(), "maximumLoad", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_MinimumDischarge(), ecorePackage.getEInt(), "minimumDischarge", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_MaximumDischarge(), ecorePackage.getEInt(), "maximumDischarge", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_RetainHeel(), ecorePackage.getEInt(), "retainHeel", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_CargoPrice(), ecorePackage.getEDouble(), "cargoPrice", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_BaseFuelPrice(), ecorePackage.getEDouble(), "baseFuelPrice", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_CvValue(), ecorePackage.getEDouble(), "cvValue", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnitCostMatrix_CostLines(), this.getUnitCostLine(), null, "costLines", null, 0, -1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnitCostMatrix_AllowedRoutes(), thePortPackage.getRoute(), null, "allowedRoutes", null, 0, -1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_RevenueShare(), ecorePackage.getEDouble(), "revenueShare", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_LadenTimeAllowance(), ecorePackage.getEDouble(), "ladenTimeAllowance", "0.06", 0, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_BallastTimeAllowance(), ecorePackage.getEDouble(), "ballastTimeAllowance", "0.06", 0, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unitCostLineEClass, UnitCostLine.class, "UnitCostLine", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUnitCostLine_UnitCost(), ecorePackage.getEDouble(), "unitCost", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_MmbtuDelivered(), ecorePackage.getEInt(), "mmbtuDelivered", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnitCostLine_From(), thePortPackage.getPort(), null, "from", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnitCostLine_To(), thePortPackage.getPort(), null, "to", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_Duration(), ecorePackage.getEInt(), "duration", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_VolumeLoaded(), ecorePackage.getEInt(), "volumeLoaded", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_VolumeDischarged(), ecorePackage.getEInt(), "volumeDischarged", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_HireCost(), ecorePackage.getEInt(), "hireCost", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_FuelCost(), ecorePackage.getEInt(), "fuelCost", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_CanalCost(), ecorePackage.getEInt(), "canalCost", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnitCostLine_CostComponents(), this.getCostComponent(), null, "costComponents", null, 0, -1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_PortCost(), ecorePackage.getEInt(), "portCost", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_Profit(), ecorePackage.getEInt(), "profit", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(unitCostLineEClass, ecorePackage.getEInt(), "getTotalCost", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(voyageEClass, Voyage.class, "Voyage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVoyage_Route(), thePortPackage.getRoute(), null, "route", null, 0, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVoyage_RouteCost(), ecorePackage.getEInt(), "routeCost", null, 1, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVoyage_Speed(), ecorePackage.getEDouble(), "speed", null, 1, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVoyage_Distance(), ecorePackage.getEInt(), "distance", null, 1, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVoyage_IdleTime(), ecorePackage.getEInt(), "idleTime", null, 1, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVoyage_TravelTime(), ecorePackage.getEInt(), "travelTime", null, 1, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(visitEClass, Visit.class, "Visit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVisit_PortCost(), ecorePackage.getEInt(), "portCost", null, 1, 1, Visit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(costComponentEClass, CostComponent.class, "CostComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCostComponent_Duration(), ecorePackage.getEInt(), "duration", null, 1, 1, CostComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCostComponent_HireCost(), ecorePackage.getEInt(), "hireCost", null, 1, 1, CostComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCostComponent_FuelCosts(), this.getFuelCost(), null, "fuelCosts", null, 0, -1, CostComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(costComponentEClass, ecorePackage.getEInt(), "getFuelCost", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(fuelCostEClass, FuelCost.class, "FuelCost", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFuelCost_Name(), ecorePackage.getEString(), "name", null, 1, 1, FuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelCost_Unit(), ecorePackage.getEString(), "unit", null, 1, 1, FuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelCost_Quantity(), ecorePackage.getEInt(), "quantity", null, 1, 1, FuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelCost_Cost(), ecorePackage.getEInt(), "cost", null, 1, 1, FuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(journeyEClass, Journey.class, "Journey", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getJourney_From(), thePortPackage.getPort(), null, "from", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJourney_To(), thePortPackage.getPort(), null, "to", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(shippingCostPlanEClass, ShippingCostPlan.class, "ShippingCostPlan", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getShippingCostPlan_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 1, 1, ShippingCostPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShippingCostPlan_NotionalDayRate(), ecorePackage.getEInt(), "notionalDayRate", null, 1, 1, ShippingCostPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShippingCostPlan_BaseFuelPrice(), ecorePackage.getEDouble(), "baseFuelPrice", null, 1, 1, ShippingCostPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getShippingCostPlan_Rows(), this.getShippingCostRow(), null, "rows", null, 0, -1, ShippingCostPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(shippingCostRowEClass, ShippingCostRow.class, "ShippingCostRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getShippingCostRow_Port(), thePortPackage.getPort(), null, "port", null, 0, 1, ShippingCostRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShippingCostRow_Date(), ecorePackage.getEDate(), "date", null, 0, 1, ShippingCostRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShippingCostRow_CargoPrice(), ecorePackage.getEDouble(), "cargoPrice", null, 1, 1, ShippingCostRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShippingCostRow_CvValue(), ecorePackage.getEDouble(), "cvValue", null, 1, 1, ShippingCostRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShippingCostRow_DestinationType(), this.getDestinationType(), "destinationType", null, 0, 1, ShippingCostRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShippingCostRow_HeelVolume(), ecorePackage.getEInt(), "heelVolume", null, 0, 1, ShippingCostRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShippingCostRow_IncludePortCosts(), ecorePackage.getEBoolean(), "includePortCosts", null, 0, 1, ShippingCostRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoSandboxEClass, CargoSandbox.class, "CargoSandbox", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCargoSandbox_Cargoes(), this.getProvisionalCargo(), null, "cargoes", null, 0, -1, CargoSandbox.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(provisionalCargoEClass, ProvisionalCargo.class, "ProvisionalCargo", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProvisionalCargo_Buy(), this.getBuyOpportunity(), null, "buy", null, 0, 1, ProvisionalCargo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProvisionalCargo_Sell(), this.getSellOpportunity(), null, "sell", null, 0, 1, ProvisionalCargo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProvisionalCargo_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 0, 1, ProvisionalCargo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProvisionalCargo_PortfolioModel(), ecorePackage.getEObject(), null, "portfolioModel", null, 0, 1, ProvisionalCargo.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(buyOptionEClass, BuyOption.class, "BuyOption", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(sellOptionEClass, SellOption.class, "SellOption", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(buyOpportunityEClass, BuyOpportunity.class, "BuyOpportunity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBuyOpportunity_DesPurchase(), ecorePackage.getEBoolean(), "desPurchase", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBuyOpportunity_Port(), thePortPackage.getPort(), null, "port", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBuyOpportunity_Contract(), theCommercialPackage.getPurchaseContract(), null, "contract", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_Date(), theDateTimePackage.getLocalDate(), "date", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBuyOpportunity_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_Cv(), ecorePackage.getEDouble(), "cv", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_CancellationExpression(), ecorePackage.getEString(), "cancellationExpression", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_MiscCosts(), ecorePackage.getEInt(), "miscCosts", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_VolumeMode(), this.getVolumeMode(), "volumeMode", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_VolumeUnits(), theTypesPackage.getVolumeUnits(), "volumeUnits", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_MinVolume(), ecorePackage.getEInt(), "minVolume", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_MaxVolume(), ecorePackage.getEInt(), "maxVolume", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sellOpportunityEClass, SellOpportunity.class, "SellOpportunity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSellOpportunity_FobSale(), ecorePackage.getEBoolean(), "fobSale", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSellOpportunity_Port(), thePortPackage.getPort(), null, "port", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSellOpportunity_Contract(), theCommercialPackage.getSalesContract(), null, "contract", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_Date(), theDateTimePackage.getLocalDate(), "date", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSellOpportunity_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_CancellationExpression(), ecorePackage.getEString(), "cancellationExpression", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_MiscCosts(), ecorePackage.getEInt(), "miscCosts", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_VolumeMode(), this.getVolumeMode(), "volumeMode", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_VolumeUnits(), theTypesPackage.getVolumeUnits(), "volumeUnits", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_MinVolume(), ecorePackage.getEInt(), "minVolume", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_MaxVolume(), ecorePackage.getEInt(), "maxVolume", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(buyMarketEClass, BuyMarket.class, "BuyMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBuyMarket_Market(), theSpotMarketsPackage.getSpotMarket(), null, "market", null, 0, 1, BuyMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sellMarketEClass, SellMarket.class, "SellMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSellMarket_Market(), theSpotMarketsPackage.getSpotMarket(), null, "market", null, 0, 1, SellMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(buyReferenceEClass, BuyReference.class, "BuyReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBuyReference_Slot(), theCargoPackage.getLoadSlot(), null, "slot", null, 0, 1, BuyReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sellReferenceEClass, SellReference.class, "SellReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSellReference_Slot(), theCargoPackage.getDischargeSlot(), null, "slot", null, 0, 1, SellReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseCaseRowEClass, BaseCaseRow.class, "BaseCaseRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBaseCaseRow_BuyOption(), this.getBuyOption(), null, "buyOption", null, 0, 1, BaseCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBaseCaseRow_SellOption(), this.getSellOption(), null, "sellOption", null, 0, 1, BaseCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBaseCaseRow_Shipping(), this.getShippingOption(), null, "shipping", null, 0, 1, BaseCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(partialCaseRowEClass, PartialCaseRow.class, "PartialCaseRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPartialCaseRow_BuyOptions(), this.getBuyOption(), null, "buyOptions", null, 0, -1, PartialCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPartialCaseRow_SellOptions(), this.getSellOption(), null, "sellOptions", null, 0, -1, PartialCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPartialCaseRow_Shipping(), this.getShippingOption(), null, "shipping", null, 0, -1, PartialCaseRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(shippingOptionEClass, ShippingOption.class, "ShippingOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(fleetShippingOptionEClass, FleetShippingOption.class, "FleetShippingOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFleetShippingOption_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 0, 1, FleetShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFleetShippingOption_HireCost(), ecorePackage.getEString(), "hireCost", null, 0, 1, FleetShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetShippingOption_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, FleetShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFleetShippingOption_UseSafetyHeel(), ecorePackage.getEBoolean(), "useSafetyHeel", null, 0, 1, FleetShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(optionalAvailabilityShippingOptionEClass, OptionalAvailabilityShippingOption.class, "OptionalAvailabilityShippingOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOptionalAvailabilityShippingOption_BallastBonus(), ecorePackage.getEString(), "ballastBonus", null, 0, 1, OptionalAvailabilityShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptionalAvailabilityShippingOption_RepositioningFee(), ecorePackage.getEString(), "repositioningFee", null, 0, 1, OptionalAvailabilityShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptionalAvailabilityShippingOption_Start(), theDateTimePackage.getLocalDate(), "start", null, 0, 1, OptionalAvailabilityShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptionalAvailabilityShippingOption_End(), theDateTimePackage.getLocalDate(), "end", null, 0, 1, OptionalAvailabilityShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionalAvailabilityShippingOption_StartPort(), thePortPackage.getPort(), null, "startPort", null, 0, 1, OptionalAvailabilityShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionalAvailabilityShippingOption_EndPort(), thePortPackage.getPort(), null, "endPort", null, 0, 1, OptionalAvailabilityShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(roundTripShippingOptionEClass, RoundTripShippingOption.class, "RoundTripShippingOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRoundTripShippingOption_VesselClass(), theFleetPackage.getVesselClass(), null, "vesselClass", null, 0, 1, RoundTripShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRoundTripShippingOption_HireCost(), ecorePackage.getEString(), "hireCost", null, 0, 1, RoundTripShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(nominatedShippingOptionEClass, NominatedShippingOption.class, "NominatedShippingOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNominatedShippingOption_NominatedVessel(), theFleetPackage.getVessel(), null, "nominatedVessel", null, 0, 1, NominatedShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(analysisResultRowEClass, AnalysisResultRow.class, "AnalysisResultRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAnalysisResultRow_BuyOption(), this.getBuyOption(), null, "buyOption", null, 0, 1, AnalysisResultRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalysisResultRow_SellOption(), this.getSellOption(), null, "sellOption", null, 0, 1, AnalysisResultRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalysisResultRow_ResultDetail(), this.getAnalysisResultDetail(), null, "resultDetail", null, 0, 1, AnalysisResultRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalysisResultRow_Shipping(), this.getShippingOption(), null, "shipping", null, 0, 1, AnalysisResultRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalysisResultRow_ResultDetails(), this.getResultContainer(), null, "resultDetails", null, 0, 1, AnalysisResultRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resultContainerEClass, ResultContainer.class, "ResultContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getResultContainer_CargoAllocation(), theSchedulePackage.getCargoAllocation(), null, "cargoAllocation", null, 0, 1, ResultContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getResultContainer_OpenSlotAllocations(), theSchedulePackage.getOpenSlotAllocation(), null, "openSlotAllocations", null, 0, -1, ResultContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getResultContainer_SlotAllocations(), theSchedulePackage.getSlotAllocation(), null, "slotAllocations", null, 0, -1, ResultContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getResultContainer_Events(), theSchedulePackage.getEvent(), null, "events", null, 0, -1, ResultContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(analysisResultDetailEClass, AnalysisResultDetail.class, "AnalysisResultDetail", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(profitAndLossResultEClass, ProfitAndLossResult.class, "ProfitAndLossResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProfitAndLossResult_Value(), ecorePackage.getEDouble(), "value", null, 0, 1, ProfitAndLossResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(breakEvenResultEClass, BreakEvenResult.class, "BreakEvenResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBreakEvenResult_Price(), ecorePackage.getEDouble(), "price", null, 0, 1, BreakEvenResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBreakEvenResult_PriceString(), ecorePackage.getEString(), "priceString", null, 0, 1, BreakEvenResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBreakEvenResult_CargoPNL(), ecorePackage.getEDouble(), "cargoPNL", null, 0, 1, BreakEvenResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(optionAnalysisModelEClass, OptionAnalysisModel.class, "OptionAnalysisModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOptionAnalysisModel_Buys(), this.getBuyOption(), null, "buys", null, 0, -1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionAnalysisModel_Sells(), this.getSellOption(), null, "sells", null, 0, -1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionAnalysisModel_BaseCase(), this.getBaseCase(), null, "baseCase", null, 0, 1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionAnalysisModel_ShippingTemplates(), this.getShippingOption(), null, "shippingTemplates", null, 0, -1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionAnalysisModel_PartialCase(), this.getPartialCase(), null, "partialCase", null, 0, 1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionAnalysisModel_ResultSets(), this.getResultSet(), null, "resultSets", null, 0, -1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptionAnalysisModel_UseTargetPNL(), ecorePackage.getEBoolean(), "useTargetPNL", null, 0, 1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptionAnalysisModel_Children(), this.getOptionAnalysisModel(), null, "children", null, 0, -1, OptionAnalysisModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resultSetEClass, ResultSet.class, "ResultSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getResultSet_Rows(), this.getAnalysisResultRow(), null, "rows", null, 0, -1, ResultSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResultSet_ProfitAndLoss(), ecorePackage.getELong(), "profitAndLoss", null, 0, 1, ResultSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseCaseEClass, BaseCase.class, "BaseCase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBaseCase_BaseCase(), this.getBaseCaseRow(), null, "baseCase", null, 0, -1, BaseCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseCase_ProfitAndLoss(), ecorePackage.getELong(), "profitAndLoss", null, 0, 1, BaseCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(partialCaseEClass, PartialCase.class, "PartialCase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPartialCase_PartialCase(), this.getPartialCaseRow(), null, "partialCase", null, 0, -1, PartialCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(actionableSetPlanEClass, ActionableSetPlan.class, "ActionableSetPlan", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getActionableSetPlan_ActionSets(), this.getActionableSet(), null, "actionSets", null, 0, -1, ActionableSetPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getActionableSetPlan_ExtraSlots(), theCargoPackage.getSlot(), null, "extraSlots", null, 0, -1, ActionableSetPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(actionableSetEClass, ActionableSet.class, "ActionableSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getActionableSet_ScheduleModel(), theSchedulePackage.getScheduleModel(), null, "scheduleModel", null, 0, 1, ActionableSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(slotInsertionOptionsEClass, SlotInsertionOptions.class, "SlotInsertionOptions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlotInsertionOptions_SlotsInserted(), theCargoPackage.getSlot(), null, "slotsInserted", null, 0, -1, SlotInsertionOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotInsertionOptions_InsertionOptions(), this.getSlotInsertionOption(), null, "insertionOptions", null, 0, -1, SlotInsertionOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotInsertionOptions_ExtraSlots(), theCargoPackage.getSlot(), null, "extraSlots", null, 0, -1, SlotInsertionOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(slotInsertionOptionEClass, SlotInsertionOption.class, "SlotInsertionOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlotInsertionOption_ScheduleModel(), theSchedulePackage.getScheduleModel(), null, "scheduleModel", null, 0, 1, SlotInsertionOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(destinationTypeEEnum, DestinationType.class, "DestinationType");
		addEEnumLiteral(destinationTypeEEnum, DestinationType.START);
		addEEnumLiteral(destinationTypeEEnum, DestinationType.END);
		addEEnumLiteral(destinationTypeEEnum, DestinationType.LOAD);
		addEEnumLiteral(destinationTypeEEnum, DestinationType.DISCHARGE);
		addEEnumLiteral(destinationTypeEEnum, DestinationType.OTHER);

		initEEnum(volumeModeEEnum, VolumeMode.class, "VolumeMode");
		addEEnumLiteral(volumeModeEEnum, VolumeMode.NOT_SPECIFIED);
		addEEnumLiteral(volumeModeEEnum, VolumeMode.FIXED);
		addEEnumLiteral(volumeModeEEnum, VolumeMode.RANGE);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
		// http://www.mmxlabs.com/models/pricing/expressionType
		createExpressionTypeAnnotations();
		// http://www.mmxlabs.com/models/mmxcore/validation/NamedObject
		createNamedObjectAnnotations();
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
		  (getUnitCostMatrix_NotionalDayRate(), 
		   source, 
		   new String[] {
			 "unit", "$/day"
		   });	
		addAnnotation
		  (getUnitCostMatrix_Speed(), 
		   source, 
		   new String[] {
			 "unit", "kts"
		   });	
		addAnnotation
		  (getUnitCostMatrix_MinimumLoad(), 
		   source, 
		   new String[] {
			 "unit", "m3"
		   });	
		addAnnotation
		  (getUnitCostMatrix_MaximumLoad(), 
		   source, 
		   new String[] {
			 "unit", "m3"
		   });	
		addAnnotation
		  (getUnitCostMatrix_MinimumDischarge(), 
		   source, 
		   new String[] {
			 "unit", "m3"
		   });	
		addAnnotation
		  (getUnitCostMatrix_MaximumDischarge(), 
		   source, 
		   new String[] {
			 "unit", "m3"
		   });	
		addAnnotation
		  (getUnitCostMatrix_CargoPrice(), 
		   source, 
		   new String[] {
			 "unit", "$/mmbtu"
		   });	
		addAnnotation
		  (getUnitCostMatrix_BaseFuelPrice(), 
		   source, 
		   new String[] {
			 "unit", "$/MT"
		   });	
		addAnnotation
		  (getUnitCostMatrix_RevenueShare(), 
		   source, 
		   new String[] {
			 "scale", "100",
			 "formatString", "###.#",
			 "unit", "%"
		   });	
		addAnnotation
		  (getUnitCostMatrix_LadenTimeAllowance(), 
		   source, 
		   new String[] {
			 "scale", "100",
			 "formatString", "##0.#",
			 "unit", "%"
		   });	
		addAnnotation
		  (getUnitCostMatrix_BallastTimeAllowance(), 
		   source, 
		   new String[] {
			 "scale", "100",
			 "formatString", "##0.#",
			 "unit", "%"
		   });	
		addAnnotation
		  (getShippingCostPlan_NotionalDayRate(), 
		   source, 
		   new String[] {
			 "unit", "$/day"
		   });	
		addAnnotation
		  (getShippingCostPlan_BaseFuelPrice(), 
		   source, 
		   new String[] {
			 "unit", "$/MT"
		   });	
		addAnnotation
		  (getShippingCostRow_CargoPrice(), 
		   source, 
		   new String[] {
			 "unit", "$/mmbtu"
		   });	
		addAnnotation
		  (getBuyOpportunity_Cv(), 
		   source, 
		   new String[] {
			 "formatString", "#0.###"
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
		  (getFleetShippingOption_HireCost(), 
		   source, 
		   new String[] {
			 "type", "charter"
		   });	
		addAnnotation
		  (getOptionalAvailabilityShippingOption_BallastBonus(), 
		   source, 
		   new String[] {
			 "type", "charter"
		   });	
		addAnnotation
		  (getOptionalAvailabilityShippingOption_RepositioningFee(), 
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
