/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.CargoSandbox;
import com.mmxlabs.models.lng.analytics.CostComponent;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.FuelCost;
import com.mmxlabs.models.lng.analytics.Journey;
import com.mmxlabs.models.lng.analytics.ProvisionalCargo;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.analytics.Visit;
import com.mmxlabs.models.lng.analytics.Voyage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
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
	private EEnum destinationTypeEEnum = null;

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
		CommercialPackage.eINSTANCE.eClass();
		FleetPackage.eINSTANCE.eClass();

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
	public EReference getBuyOpportunity_Port() {
		return (EReference)buyOpportunityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBuyOpportunity_Contract() {
		return (EReference)buyOpportunityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_Date() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBuyOpportunity_PriceExpression() {
		return (EAttribute)buyOpportunityEClass.getEStructuralFeatures().get(3);
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
	public EReference getSellOpportunity_Port() {
		return (EReference)sellOpportunityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSellOpportunity_Contract() {
		return (EReference)sellOpportunityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_Date() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSellOpportunity_PriceExpression() {
		return (EAttribute)sellOpportunityEClass.getEStructuralFeatures().get(3);
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

		buyOpportunityEClass = createEClass(BUY_OPPORTUNITY);
		createEReference(buyOpportunityEClass, BUY_OPPORTUNITY__PORT);
		createEReference(buyOpportunityEClass, BUY_OPPORTUNITY__CONTRACT);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__DATE);
		createEAttribute(buyOpportunityEClass, BUY_OPPORTUNITY__PRICE_EXPRESSION);

		sellOpportunityEClass = createEClass(SELL_OPPORTUNITY);
		createEReference(sellOpportunityEClass, SELL_OPPORTUNITY__PORT);
		createEReference(sellOpportunityEClass, SELL_OPPORTUNITY__CONTRACT);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__DATE);
		createEAttribute(sellOpportunityEClass, SELL_OPPORTUNITY__PRICE_EXPRESSION);

		// Create enums
		destinationTypeEEnum = createEEnum(DESTINATION_TYPE);
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
		sellOpportunityEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());

		// Initialize classes and features; add operations and parameters
		initEClass(analyticsModelEClass, AnalyticsModel.class, "AnalyticsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAnalyticsModel_RoundTripMatrices(), this.getUnitCostMatrix(), null, "roundTripMatrices", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_SelectedMatrix(), this.getUnitCostMatrix(), null, "selectedMatrix", null, 1, 1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_ShippingCostPlans(), this.getShippingCostPlan(), null, "shippingCostPlans", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnalyticsModel_CargoSandboxes(), this.getCargoSandbox(), null, "cargoSandboxes", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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

		initEClass(buyOpportunityEClass, BuyOpportunity.class, "BuyOpportunity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBuyOpportunity_Port(), thePortPackage.getPort(), null, "port", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBuyOpportunity_Contract(), theCommercialPackage.getPurchaseContract(), null, "contract", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_Date(), theDateTimePackage.getLocalDate(), "date", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuyOpportunity_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, BuyOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sellOpportunityEClass, SellOpportunity.class, "SellOpportunity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSellOpportunity_Port(), thePortPackage.getPort(), null, "port", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSellOpportunity_Contract(), theCommercialPackage.getSalesContract(), null, "contract", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_Date(), theDateTimePackage.getLocalDate(), "date", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSellOpportunity_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, SellOpportunity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(destinationTypeEEnum, DestinationType.class, "DestinationType");
		addEEnumLiteral(destinationTypeEEnum, DestinationType.START);
		addEEnumLiteral(destinationTypeEEnum, DestinationType.END);
		addEEnumLiteral(destinationTypeEEnum, DestinationType.LOAD);
		addEEnumLiteral(destinationTypeEEnum, DestinationType.DISCHARGE);
		addEEnumLiteral(destinationTypeEEnum, DestinationType.OTHER);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
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
	}

} //AnalyticsPackageImpl
