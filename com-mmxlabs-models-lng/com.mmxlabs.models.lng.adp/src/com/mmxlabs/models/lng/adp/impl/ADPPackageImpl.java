/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel;
import com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel;
import com.mmxlabs.models.lng.adp.CargoNumberDistributionModel;
import com.mmxlabs.models.lng.adp.CargoSizeDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.CustomSubProfileAttributes;
import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.DeliverToFlow;
import com.mmxlabs.models.lng.adp.DeliverToProfileFlow;
import com.mmxlabs.models.lng.adp.DeliverToSpotFlow;
import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.FleetConstraint;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.adp.FlowType;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.MaxCargoConstraint;
import com.mmxlabs.models.lng.adp.MinCargoConstraint;
import com.mmxlabs.models.lng.adp.MullAllocationRow;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;
import com.mmxlabs.models.lng.adp.PreDefinedDate;
import com.mmxlabs.models.lng.adp.PreDefinedDistributionModel;
import com.mmxlabs.models.lng.adp.ProfileConstraint;
import com.mmxlabs.models.lng.adp.ProfileVesselRestriction;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.ShippingOption;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.SubProfileConstraint;
import com.mmxlabs.models.lng.adp.SupplyFromFlow;
import com.mmxlabs.models.lng.adp.SupplyFromProfileFlow;
import com.mmxlabs.models.lng.adp.SupplyFromSpotFlow;
import com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
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
public class ADPPackageImpl extends EPackageImpl implements ADPPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass adpModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fleetProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass contractProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass distributionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass periodDistributionProfileConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass periodDistributionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoSizeDistributionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoNumberDistributionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass purchaseContractProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass salesContractProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass subContractProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass customSubProfileAttributesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoByQuarterDistributionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoIntervalDistributionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass preDefinedDistributionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass preDefinedDateEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass flowTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass supplyFromFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deliverToFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass supplyFromProfileFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deliverToProfileFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass supplyFromSpotFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deliverToSpotFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profileVesselRestrictionEClass = null;

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
	private EClass profileConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass subProfileConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass minCargoConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass maxCargoConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fleetConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass targetCargoesOnVesselConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mullEntityRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass desSalesMarketAllocationRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass salesContractAllocationRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mullProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mullSubprofileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mullAllocationRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum intervalTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum lngVolumeUnitEEnum = null;

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
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ADPPackageImpl() {
		super(eNS_URI, ADPFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ADPPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ADPPackage init() {
		if (isInited) return (ADPPackage)EPackage.Registry.INSTANCE.getEPackage(ADPPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredADPPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		ADPPackageImpl theADPPackage = registeredADPPackage instanceof ADPPackageImpl ? (ADPPackageImpl)registeredADPPackage : new ADPPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		CargoPackage.eINSTANCE.eClass();
		CommercialPackage.eINSTANCE.eClass();
		DateTimePackage.eINSTANCE.eClass();
		FleetPackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();
		PortPackage.eINSTANCE.eClass();
		PricingPackage.eINSTANCE.eClass();
		SchedulePackage.eINSTANCE.eClass();
		SpotMarketsPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theADPPackage.createPackageContents();

		// Initialize created meta-data
		theADPPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theADPPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ADPPackage.eNS_URI, theADPPackage);
		return theADPPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getADPModel() {
		return adpModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getADPModel_YearStart() {
		return (EAttribute)adpModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getADPModel_YearEnd() {
		return (EAttribute)adpModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getADPModel_PurchaseContractProfiles() {
		return (EReference)adpModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getADPModel_SalesContractProfiles() {
		return (EReference)adpModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getADPModel_FleetProfile() {
		return (EReference)adpModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getADPModel_MullProfile() {
		return (EReference)adpModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFleetProfile() {
		return fleetProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFleetProfile_Constraints() {
		return (EReference)fleetProfileEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFleetProfile_DefaultNominalMarket() {
		return (EReference)fleetProfileEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getContractProfile() {
		return contractProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContractProfile_Contract() {
		return (EReference)contractProfileEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContractProfile_ContractCode() {
		return (EAttribute)contractProfileEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContractProfile_Custom() {
		return (EAttribute)contractProfileEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContractProfile_Enabled() {
		return (EAttribute)contractProfileEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContractProfile_TotalVolume() {
		return (EAttribute)contractProfileEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContractProfile_VolumeUnit() {
		return (EAttribute)contractProfileEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContractProfile_SubProfiles() {
		return (EReference)contractProfileEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContractProfile_Constraints() {
		return (EReference)contractProfileEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDistributionModel() {
		return distributionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDistributionModel_VolumePerCargo() {
		return (EAttribute)distributionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDistributionModel_VolumeUnit() {
		return (EAttribute)distributionModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getDistributionModel__GetModelOrContractVolumePerCargo() {
		return distributionModelEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getDistributionModel__GetModelOrContractVolumeUnit() {
		return distributionModelEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPeriodDistributionProfileConstraint() {
		return periodDistributionProfileConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPeriodDistributionProfileConstraint_Distributions() {
		return (EReference)periodDistributionProfileConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPeriodDistribution() {
		return periodDistributionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPeriodDistribution_Range() {
		return (EAttribute)periodDistributionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPeriodDistribution_MinCargoes() {
		return (EAttribute)periodDistributionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPeriodDistribution_MaxCargoes() {
		return (EAttribute)periodDistributionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoSizeDistributionModel() {
		return cargoSizeDistributionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoSizeDistributionModel_Exact() {
		return (EAttribute)cargoSizeDistributionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoNumberDistributionModel() {
		return cargoNumberDistributionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoNumberDistributionModel_NumberOfCargoes() {
		return (EAttribute)cargoNumberDistributionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPurchaseContractProfile() {
		return purchaseContractProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSalesContractProfile() {
		return salesContractProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSubContractProfile() {
		return subContractProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSubContractProfile_Name() {
		return (EAttribute)subContractProfileEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSubContractProfile_ContractType() {
		return (EAttribute)subContractProfileEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSubContractProfile_DistributionModel() {
		return (EReference)subContractProfileEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSubContractProfile_SlotTemplateId() {
		return (EAttribute)subContractProfileEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSubContractProfile_CustomAttribs() {
		return (EReference)subContractProfileEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSubContractProfile_NominatedVessel() {
		return (EReference)subContractProfileEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSubContractProfile_ShippingDays() {
		return (EAttribute)subContractProfileEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSubContractProfile_Constraints() {
		return (EReference)subContractProfileEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSubContractProfile_WindowSize() {
		return (EAttribute)subContractProfileEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSubContractProfile_WindowSizeUnits() {
		return (EAttribute)subContractProfileEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSubContractProfile_Port() {
		return (EReference)subContractProfileEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCustomSubProfileAttributes() {
		return customSubProfileAttributesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoByQuarterDistributionModel() {
		return cargoByQuarterDistributionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoByQuarterDistributionModel_Q1() {
		return (EAttribute)cargoByQuarterDistributionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoByQuarterDistributionModel_Q2() {
		return (EAttribute)cargoByQuarterDistributionModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoByQuarterDistributionModel_Q3() {
		return (EAttribute)cargoByQuarterDistributionModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoByQuarterDistributionModel_Q4() {
		return (EAttribute)cargoByQuarterDistributionModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoIntervalDistributionModel() {
		return cargoIntervalDistributionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoIntervalDistributionModel_Quantity() {
		return (EAttribute)cargoIntervalDistributionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoIntervalDistributionModel_IntervalType() {
		return (EAttribute)cargoIntervalDistributionModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoIntervalDistributionModel_Spacing() {
		return (EAttribute)cargoIntervalDistributionModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPreDefinedDistributionModel() {
		return preDefinedDistributionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPreDefinedDistributionModel_Dates() {
		return (EReference)preDefinedDistributionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPreDefinedDate() {
		return preDefinedDateEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPreDefinedDate_Date() {
		return (EAttribute)preDefinedDateEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFlowType() {
		return flowTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSupplyFromFlow() {
		return supplyFromFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDeliverToFlow() {
		return deliverToFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSupplyFromProfileFlow() {
		return supplyFromProfileFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSupplyFromProfileFlow_Profile() {
		return (EReference)supplyFromProfileFlowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSupplyFromProfileFlow_SubProfile() {
		return (EReference)supplyFromProfileFlowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDeliverToProfileFlow() {
		return deliverToProfileFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDeliverToProfileFlow_Profile() {
		return (EReference)deliverToProfileFlowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDeliverToProfileFlow_SubProfile() {
		return (EReference)deliverToProfileFlowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSupplyFromSpotFlow() {
		return supplyFromSpotFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSupplyFromSpotFlow_Market() {
		return (EReference)supplyFromSpotFlowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDeliverToSpotFlow() {
		return deliverToSpotFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDeliverToSpotFlow_Market() {
		return (EReference)deliverToSpotFlowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProfileVesselRestriction() {
		return profileVesselRestrictionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getProfileVesselRestriction_Vessels() {
		return (EReference)profileVesselRestrictionEClass.getEStructuralFeatures().get(0);
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
	public EReference getShippingOption_VesselAssignmentType() {
		return (EReference)shippingOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getShippingOption_SpotIndex() {
		return (EAttribute)shippingOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getShippingOption_Vessel() {
		return (EReference)shippingOptionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProfileConstraint() {
		return profileConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSubProfileConstraint() {
		return subProfileConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMinCargoConstraint() {
		return minCargoConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMinCargoConstraint_MinCargoes() {
		return (EAttribute)minCargoConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMinCargoConstraint_IntervalType() {
		return (EAttribute)minCargoConstraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMaxCargoConstraint() {
		return maxCargoConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMaxCargoConstraint_MaxCargoes() {
		return (EAttribute)maxCargoConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMaxCargoConstraint_IntervalType() {
		return (EAttribute)maxCargoConstraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFleetConstraint() {
		return fleetConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTargetCargoesOnVesselConstraint() {
		return targetCargoesOnVesselConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTargetCargoesOnVesselConstraint_Vessel() {
		return (EReference)targetCargoesOnVesselConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTargetCargoesOnVesselConstraint_TargetNumberOfCargoes() {
		return (EAttribute)targetCargoesOnVesselConstraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTargetCargoesOnVesselConstraint_IntervalType() {
		return (EAttribute)targetCargoesOnVesselConstraintEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTargetCargoesOnVesselConstraint_Weight() {
		return (EAttribute)targetCargoesOnVesselConstraintEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMullEntityRow() {
		return mullEntityRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMullEntityRow_Entity() {
		return (EReference)mullEntityRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMullEntityRow_InitialAllocation() {
		return (EAttribute)mullEntityRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMullEntityRow_RelativeEntitlement() {
		return (EAttribute)mullEntityRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMullEntityRow_DesSalesMarketAllocationRows() {
		return (EReference)mullEntityRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMullEntityRow_Ports() {
		return (EReference)mullEntityRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMullEntityRow_SalesContractAllocationRows() {
		return (EReference)mullEntityRowEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDESSalesMarketAllocationRow() {
		return desSalesMarketAllocationRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDESSalesMarketAllocationRow_DesSalesMarket() {
		return (EReference)desSalesMarketAllocationRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSalesContractAllocationRow() {
		return salesContractAllocationRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSalesContractAllocationRow_Contract() {
		return (EReference)salesContractAllocationRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMullProfile() {
		return mullProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMullProfile_WindowSize() {
		return (EAttribute)mullProfileEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMullProfile_VolumeFlex() {
		return (EAttribute)mullProfileEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMullProfile_Inventories() {
		return (EReference)mullProfileEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMullProfile_FullCargoLotValue() {
		return (EAttribute)mullProfileEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMullSubprofile() {
		return mullSubprofileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMullSubprofile_Inventory() {
		return (EReference)mullSubprofileEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMullSubprofile_EntityTable() {
		return (EReference)mullSubprofileEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMullAllocationRow() {
		return mullAllocationRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMullAllocationRow_Weight() {
		return (EAttribute)mullAllocationRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMullAllocationRow_Vessels() {
		return (EReference)mullAllocationRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getIntervalType() {
		return intervalTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getLNGVolumeUnit() {
		return lngVolumeUnitEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ADPFactory getADPFactory() {
		return (ADPFactory)getEFactoryInstance();
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
		adpModelEClass = createEClass(ADP_MODEL);
		createEAttribute(adpModelEClass, ADP_MODEL__YEAR_START);
		createEAttribute(adpModelEClass, ADP_MODEL__YEAR_END);
		createEReference(adpModelEClass, ADP_MODEL__PURCHASE_CONTRACT_PROFILES);
		createEReference(adpModelEClass, ADP_MODEL__SALES_CONTRACT_PROFILES);
		createEReference(adpModelEClass, ADP_MODEL__FLEET_PROFILE);
		createEReference(adpModelEClass, ADP_MODEL__MULL_PROFILE);

		fleetProfileEClass = createEClass(FLEET_PROFILE);
		createEReference(fleetProfileEClass, FLEET_PROFILE__CONSTRAINTS);
		createEReference(fleetProfileEClass, FLEET_PROFILE__DEFAULT_NOMINAL_MARKET);

		contractProfileEClass = createEClass(CONTRACT_PROFILE);
		createEReference(contractProfileEClass, CONTRACT_PROFILE__CONTRACT);
		createEAttribute(contractProfileEClass, CONTRACT_PROFILE__CONTRACT_CODE);
		createEAttribute(contractProfileEClass, CONTRACT_PROFILE__CUSTOM);
		createEAttribute(contractProfileEClass, CONTRACT_PROFILE__ENABLED);
		createEAttribute(contractProfileEClass, CONTRACT_PROFILE__TOTAL_VOLUME);
		createEAttribute(contractProfileEClass, CONTRACT_PROFILE__VOLUME_UNIT);
		createEReference(contractProfileEClass, CONTRACT_PROFILE__SUB_PROFILES);
		createEReference(contractProfileEClass, CONTRACT_PROFILE__CONSTRAINTS);

		purchaseContractProfileEClass = createEClass(PURCHASE_CONTRACT_PROFILE);

		salesContractProfileEClass = createEClass(SALES_CONTRACT_PROFILE);

		subContractProfileEClass = createEClass(SUB_CONTRACT_PROFILE);
		createEAttribute(subContractProfileEClass, SUB_CONTRACT_PROFILE__NAME);
		createEAttribute(subContractProfileEClass, SUB_CONTRACT_PROFILE__CONTRACT_TYPE);
		createEReference(subContractProfileEClass, SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL);
		createEAttribute(subContractProfileEClass, SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID);
		createEReference(subContractProfileEClass, SUB_CONTRACT_PROFILE__NOMINATED_VESSEL);
		createEAttribute(subContractProfileEClass, SUB_CONTRACT_PROFILE__SHIPPING_DAYS);
		createEReference(subContractProfileEClass, SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS);
		createEReference(subContractProfileEClass, SUB_CONTRACT_PROFILE__CONSTRAINTS);
		createEAttribute(subContractProfileEClass, SUB_CONTRACT_PROFILE__WINDOW_SIZE);
		createEAttribute(subContractProfileEClass, SUB_CONTRACT_PROFILE__WINDOW_SIZE_UNITS);
		createEReference(subContractProfileEClass, SUB_CONTRACT_PROFILE__PORT);

		customSubProfileAttributesEClass = createEClass(CUSTOM_SUB_PROFILE_ATTRIBUTES);

		distributionModelEClass = createEClass(DISTRIBUTION_MODEL);
		createEAttribute(distributionModelEClass, DISTRIBUTION_MODEL__VOLUME_PER_CARGO);
		createEAttribute(distributionModelEClass, DISTRIBUTION_MODEL__VOLUME_UNIT);
		createEOperation(distributionModelEClass, DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO);
		createEOperation(distributionModelEClass, DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT);

		cargoSizeDistributionModelEClass = createEClass(CARGO_SIZE_DISTRIBUTION_MODEL);
		createEAttribute(cargoSizeDistributionModelEClass, CARGO_SIZE_DISTRIBUTION_MODEL__EXACT);

		cargoNumberDistributionModelEClass = createEClass(CARGO_NUMBER_DISTRIBUTION_MODEL);
		createEAttribute(cargoNumberDistributionModelEClass, CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES);

		cargoByQuarterDistributionModelEClass = createEClass(CARGO_BY_QUARTER_DISTRIBUTION_MODEL);
		createEAttribute(cargoByQuarterDistributionModelEClass, CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q1);
		createEAttribute(cargoByQuarterDistributionModelEClass, CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q2);
		createEAttribute(cargoByQuarterDistributionModelEClass, CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q3);
		createEAttribute(cargoByQuarterDistributionModelEClass, CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q4);

		cargoIntervalDistributionModelEClass = createEClass(CARGO_INTERVAL_DISTRIBUTION_MODEL);
		createEAttribute(cargoIntervalDistributionModelEClass, CARGO_INTERVAL_DISTRIBUTION_MODEL__QUANTITY);
		createEAttribute(cargoIntervalDistributionModelEClass, CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE);
		createEAttribute(cargoIntervalDistributionModelEClass, CARGO_INTERVAL_DISTRIBUTION_MODEL__SPACING);

		preDefinedDistributionModelEClass = createEClass(PRE_DEFINED_DISTRIBUTION_MODEL);
		createEReference(preDefinedDistributionModelEClass, PRE_DEFINED_DISTRIBUTION_MODEL__DATES);

		preDefinedDateEClass = createEClass(PRE_DEFINED_DATE);
		createEAttribute(preDefinedDateEClass, PRE_DEFINED_DATE__DATE);

		flowTypeEClass = createEClass(FLOW_TYPE);

		supplyFromFlowEClass = createEClass(SUPPLY_FROM_FLOW);

		deliverToFlowEClass = createEClass(DELIVER_TO_FLOW);

		supplyFromProfileFlowEClass = createEClass(SUPPLY_FROM_PROFILE_FLOW);
		createEReference(supplyFromProfileFlowEClass, SUPPLY_FROM_PROFILE_FLOW__PROFILE);
		createEReference(supplyFromProfileFlowEClass, SUPPLY_FROM_PROFILE_FLOW__SUB_PROFILE);

		deliverToProfileFlowEClass = createEClass(DELIVER_TO_PROFILE_FLOW);
		createEReference(deliverToProfileFlowEClass, DELIVER_TO_PROFILE_FLOW__PROFILE);
		createEReference(deliverToProfileFlowEClass, DELIVER_TO_PROFILE_FLOW__SUB_PROFILE);

		supplyFromSpotFlowEClass = createEClass(SUPPLY_FROM_SPOT_FLOW);
		createEReference(supplyFromSpotFlowEClass, SUPPLY_FROM_SPOT_FLOW__MARKET);

		deliverToSpotFlowEClass = createEClass(DELIVER_TO_SPOT_FLOW);
		createEReference(deliverToSpotFlowEClass, DELIVER_TO_SPOT_FLOW__MARKET);

		profileVesselRestrictionEClass = createEClass(PROFILE_VESSEL_RESTRICTION);
		createEReference(profileVesselRestrictionEClass, PROFILE_VESSEL_RESTRICTION__VESSELS);

		shippingOptionEClass = createEClass(SHIPPING_OPTION);
		createEReference(shippingOptionEClass, SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE);
		createEAttribute(shippingOptionEClass, SHIPPING_OPTION__SPOT_INDEX);
		createEReference(shippingOptionEClass, SHIPPING_OPTION__VESSEL);

		profileConstraintEClass = createEClass(PROFILE_CONSTRAINT);

		subProfileConstraintEClass = createEClass(SUB_PROFILE_CONSTRAINT);

		minCargoConstraintEClass = createEClass(MIN_CARGO_CONSTRAINT);
		createEAttribute(minCargoConstraintEClass, MIN_CARGO_CONSTRAINT__MIN_CARGOES);
		createEAttribute(minCargoConstraintEClass, MIN_CARGO_CONSTRAINT__INTERVAL_TYPE);

		maxCargoConstraintEClass = createEClass(MAX_CARGO_CONSTRAINT);
		createEAttribute(maxCargoConstraintEClass, MAX_CARGO_CONSTRAINT__MAX_CARGOES);
		createEAttribute(maxCargoConstraintEClass, MAX_CARGO_CONSTRAINT__INTERVAL_TYPE);

		periodDistributionProfileConstraintEClass = createEClass(PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT);
		createEReference(periodDistributionProfileConstraintEClass, PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT__DISTRIBUTIONS);

		periodDistributionEClass = createEClass(PERIOD_DISTRIBUTION);
		createEAttribute(periodDistributionEClass, PERIOD_DISTRIBUTION__RANGE);
		createEAttribute(periodDistributionEClass, PERIOD_DISTRIBUTION__MIN_CARGOES);
		createEAttribute(periodDistributionEClass, PERIOD_DISTRIBUTION__MAX_CARGOES);

		fleetConstraintEClass = createEClass(FLEET_CONSTRAINT);

		targetCargoesOnVesselConstraintEClass = createEClass(TARGET_CARGOES_ON_VESSEL_CONSTRAINT);
		createEReference(targetCargoesOnVesselConstraintEClass, TARGET_CARGOES_ON_VESSEL_CONSTRAINT__VESSEL);
		createEAttribute(targetCargoesOnVesselConstraintEClass, TARGET_CARGOES_ON_VESSEL_CONSTRAINT__TARGET_NUMBER_OF_CARGOES);
		createEAttribute(targetCargoesOnVesselConstraintEClass, TARGET_CARGOES_ON_VESSEL_CONSTRAINT__INTERVAL_TYPE);
		createEAttribute(targetCargoesOnVesselConstraintEClass, TARGET_CARGOES_ON_VESSEL_CONSTRAINT__WEIGHT);

		mullEntityRowEClass = createEClass(MULL_ENTITY_ROW);
		createEReference(mullEntityRowEClass, MULL_ENTITY_ROW__ENTITY);
		createEAttribute(mullEntityRowEClass, MULL_ENTITY_ROW__INITIAL_ALLOCATION);
		createEAttribute(mullEntityRowEClass, MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT);
		createEReference(mullEntityRowEClass, MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS);
		createEReference(mullEntityRowEClass, MULL_ENTITY_ROW__PORTS);
		createEReference(mullEntityRowEClass, MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS);

		desSalesMarketAllocationRowEClass = createEClass(DES_SALES_MARKET_ALLOCATION_ROW);
		createEReference(desSalesMarketAllocationRowEClass, DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET);

		salesContractAllocationRowEClass = createEClass(SALES_CONTRACT_ALLOCATION_ROW);
		createEReference(salesContractAllocationRowEClass, SALES_CONTRACT_ALLOCATION_ROW__CONTRACT);

		mullProfileEClass = createEClass(MULL_PROFILE);
		createEAttribute(mullProfileEClass, MULL_PROFILE__WINDOW_SIZE);
		createEAttribute(mullProfileEClass, MULL_PROFILE__VOLUME_FLEX);
		createEReference(mullProfileEClass, MULL_PROFILE__INVENTORIES);
		createEAttribute(mullProfileEClass, MULL_PROFILE__FULL_CARGO_LOT_VALUE);

		mullSubprofileEClass = createEClass(MULL_SUBPROFILE);
		createEReference(mullSubprofileEClass, MULL_SUBPROFILE__INVENTORY);
		createEReference(mullSubprofileEClass, MULL_SUBPROFILE__ENTITY_TABLE);

		mullAllocationRowEClass = createEClass(MULL_ALLOCATION_ROW);
		createEAttribute(mullAllocationRowEClass, MULL_ALLOCATION_ROW__WEIGHT);
		createEReference(mullAllocationRowEClass, MULL_ALLOCATION_ROW__VESSELS);

		// Create enums
		intervalTypeEEnum = createEEnum(INTERVAL_TYPE);
		lngVolumeUnitEEnum = createEEnum(LNG_VOLUME_UNIT);
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
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		SpotMarketsPackage theSpotMarketsPackage = (SpotMarketsPackage)EPackage.Registry.INSTANCE.getEPackage(SpotMarketsPackage.eNS_URI);
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);

		// Create type parameters
		ETypeParameter contractProfileEClass_T = addETypeParameter(contractProfileEClass, "T");
		ETypeParameter contractProfileEClass_U = addETypeParameter(contractProfileEClass, "U");
		ETypeParameter subContractProfileEClass_T = addETypeParameter(subContractProfileEClass, "T");
		ETypeParameter subContractProfileEClass_U = addETypeParameter(subContractProfileEClass, "U");

		// Set bounds for type parameters
		EGenericType g1 = createEGenericType(theCargoPackage.getSlot());
		EGenericType g2 = createEGenericType(contractProfileEClass_U);
		g1.getETypeArguments().add(g2);
		contractProfileEClass_T.getEBounds().add(g1);
		g1 = createEGenericType(theCommercialPackage.getContract());
		contractProfileEClass_U.getEBounds().add(g1);
		g1 = createEGenericType(theCargoPackage.getSlot());
		g2 = createEGenericType(subContractProfileEClass_U);
		g1.getETypeArguments().add(g2);
		subContractProfileEClass_T.getEBounds().add(g1);
		g1 = createEGenericType(theCommercialPackage.getContract());
		subContractProfileEClass_U.getEBounds().add(g1);

		// Add supertypes to classes
		adpModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		g1 = createEGenericType(this.getContractProfile());
		g2 = createEGenericType(theCargoPackage.getLoadSlot());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(theCommercialPackage.getPurchaseContract());
		g1.getETypeArguments().add(g2);
		purchaseContractProfileEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getContractProfile());
		g2 = createEGenericType(theCargoPackage.getDischargeSlot());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(theCommercialPackage.getSalesContract());
		g1.getETypeArguments().add(g2);
		salesContractProfileEClass.getEGenericSuperTypes().add(g1);
		distributionModelEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		cargoSizeDistributionModelEClass.getESuperTypes().add(this.getDistributionModel());
		cargoNumberDistributionModelEClass.getESuperTypes().add(this.getDistributionModel());
		cargoByQuarterDistributionModelEClass.getESuperTypes().add(this.getDistributionModel());
		cargoIntervalDistributionModelEClass.getESuperTypes().add(this.getDistributionModel());
		preDefinedDistributionModelEClass.getESuperTypes().add(this.getDistributionModel());
		flowTypeEClass.getESuperTypes().add(this.getSubProfileConstraint());
		supplyFromFlowEClass.getESuperTypes().add(this.getFlowType());
		deliverToFlowEClass.getESuperTypes().add(this.getFlowType());
		supplyFromProfileFlowEClass.getESuperTypes().add(this.getSupplyFromFlow());
		deliverToProfileFlowEClass.getESuperTypes().add(this.getDeliverToFlow());
		supplyFromSpotFlowEClass.getESuperTypes().add(this.getSupplyFromFlow());
		deliverToSpotFlowEClass.getESuperTypes().add(this.getDeliverToFlow());
		profileVesselRestrictionEClass.getESuperTypes().add(this.getSubProfileConstraint());
		shippingOptionEClass.getESuperTypes().add(this.getSubProfileConstraint());
		minCargoConstraintEClass.getESuperTypes().add(this.getProfileConstraint());
		maxCargoConstraintEClass.getESuperTypes().add(this.getProfileConstraint());
		periodDistributionProfileConstraintEClass.getESuperTypes().add(this.getProfileConstraint());
		targetCargoesOnVesselConstraintEClass.getESuperTypes().add(this.getFleetConstraint());
		desSalesMarketAllocationRowEClass.getESuperTypes().add(this.getMullAllocationRow());
		salesContractAllocationRowEClass.getESuperTypes().add(this.getMullAllocationRow());

		// Initialize classes, features, and operations; add parameters
		initEClass(adpModelEClass, ADPModel.class, "ADPModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getADPModel_YearStart(), theDateTimePackage.getYearMonth(), "yearStart", null, 0, 1, ADPModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getADPModel_YearEnd(), theDateTimePackage.getYearMonth(), "yearEnd", null, 0, 1, ADPModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getADPModel_PurchaseContractProfiles(), this.getPurchaseContractProfile(), null, "purchaseContractProfiles", null, 0, -1, ADPModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getADPModel_SalesContractProfiles(), this.getSalesContractProfile(), null, "salesContractProfiles", null, 0, -1, ADPModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getADPModel_FleetProfile(), this.getFleetProfile(), null, "fleetProfile", null, 0, 1, ADPModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getADPModel_MullProfile(), this.getMullProfile(), null, "mullProfile", null, 0, 1, ADPModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fleetProfileEClass, FleetProfile.class, "FleetProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFleetProfile_Constraints(), this.getFleetConstraint(), null, "constraints", null, 0, -1, FleetProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetProfile_DefaultNominalMarket(), theSpotMarketsPackage.getCharterInMarket(), null, "defaultNominalMarket", null, 0, 1, FleetProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(contractProfileEClass, ContractProfile.class, "ContractProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(contractProfileEClass_U);
		initEReference(getContractProfile_Contract(), g1, null, "contract", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContractProfile_ContractCode(), ecorePackage.getEString(), "contractCode", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContractProfile_Custom(), ecorePackage.getEBoolean(), "custom", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContractProfile_Enabled(), ecorePackage.getEBoolean(), "enabled", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContractProfile_TotalVolume(), ecorePackage.getEDouble(), "totalVolume", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContractProfile_VolumeUnit(), this.getLNGVolumeUnit(), "volumeUnit", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getSubContractProfile());
		g2 = createEGenericType(contractProfileEClass_T);
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(contractProfileEClass_U);
		g1.getETypeArguments().add(g2);
		initEReference(getContractProfile_SubProfiles(), g1, null, "subProfiles", null, 0, -1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContractProfile_Constraints(), this.getProfileConstraint(), null, "constraints", null, 0, -1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(purchaseContractProfileEClass, PurchaseContractProfile.class, "PurchaseContractProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(salesContractProfileEClass, SalesContractProfile.class, "SalesContractProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(subContractProfileEClass, SubContractProfile.class, "SubContractProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSubContractProfile_Name(), ecorePackage.getEString(), "name", null, 0, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSubContractProfile_ContractType(), theCommercialPackage.getContractType(), "contractType", null, 0, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSubContractProfile_DistributionModel(), this.getDistributionModel(), null, "distributionModel", null, 0, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSubContractProfile_SlotTemplateId(), ecorePackage.getEString(), "slotTemplateId", null, 0, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSubContractProfile_NominatedVessel(), theFleetPackage.getVessel(), null, "nominatedVessel", null, 0, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSubContractProfile_ShippingDays(), ecorePackage.getEInt(), "shippingDays", null, 0, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSubContractProfile_CustomAttribs(), this.getCustomSubProfileAttributes(), null, "customAttribs", null, 0, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSubContractProfile_Constraints(), this.getSubProfileConstraint(), null, "constraints", null, 0, -1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSubContractProfile_WindowSize(), ecorePackage.getEInt(), "windowSize", "1", 1, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSubContractProfile_WindowSizeUnits(), theTypesPackage.getTimePeriod(), "windowSizeUnits", "MONTHS", 1, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSubContractProfile_Port(), thePortPackage.getPort(), null, "port", null, 1, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(customSubProfileAttributesEClass, CustomSubProfileAttributes.class, "CustomSubProfileAttributes", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(distributionModelEClass, DistributionModel.class, "DistributionModel", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDistributionModel_VolumePerCargo(), ecorePackage.getEDouble(), "volumePerCargo", null, 0, 1, DistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDistributionModel_VolumeUnit(), this.getLNGVolumeUnit(), "volumeUnit", null, 0, 1, DistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getDistributionModel__GetModelOrContractVolumePerCargo(), ecorePackage.getEDouble(), "getModelOrContractVolumePerCargo", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getDistributionModel__GetModelOrContractVolumeUnit(), this.getLNGVolumeUnit(), "getModelOrContractVolumeUnit", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(cargoSizeDistributionModelEClass, CargoSizeDistributionModel.class, "CargoSizeDistributionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCargoSizeDistributionModel_Exact(), ecorePackage.getEBoolean(), "exact", null, 0, 1, CargoSizeDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoNumberDistributionModelEClass, CargoNumberDistributionModel.class, "CargoNumberDistributionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCargoNumberDistributionModel_NumberOfCargoes(), ecorePackage.getEInt(), "numberOfCargoes", null, 0, 1, CargoNumberDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoByQuarterDistributionModelEClass, CargoByQuarterDistributionModel.class, "CargoByQuarterDistributionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCargoByQuarterDistributionModel_Q1(), ecorePackage.getEInt(), "q1", null, 0, 1, CargoByQuarterDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoByQuarterDistributionModel_Q2(), ecorePackage.getEInt(), "q2", null, 0, 1, CargoByQuarterDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoByQuarterDistributionModel_Q3(), ecorePackage.getEInt(), "q3", null, 0, 1, CargoByQuarterDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoByQuarterDistributionModel_Q4(), ecorePackage.getEInt(), "q4", null, 0, 1, CargoByQuarterDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoIntervalDistributionModelEClass, CargoIntervalDistributionModel.class, "CargoIntervalDistributionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCargoIntervalDistributionModel_Quantity(), ecorePackage.getEInt(), "quantity", null, 0, 1, CargoIntervalDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoIntervalDistributionModel_IntervalType(), this.getIntervalType(), "intervalType", null, 0, 1, CargoIntervalDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoIntervalDistributionModel_Spacing(), ecorePackage.getEInt(), "spacing", "1", 0, 1, CargoIntervalDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(preDefinedDistributionModelEClass, PreDefinedDistributionModel.class, "PreDefinedDistributionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPreDefinedDistributionModel_Dates(), this.getPreDefinedDate(), null, "dates", null, 0, -1, PreDefinedDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(preDefinedDateEClass, PreDefinedDate.class, "PreDefinedDate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPreDefinedDate_Date(), theDateTimePackage.getLocalDate(), "date", null, 0, 1, PreDefinedDate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(flowTypeEClass, FlowType.class, "FlowType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(supplyFromFlowEClass, SupplyFromFlow.class, "SupplyFromFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(deliverToFlowEClass, DeliverToFlow.class, "DeliverToFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(supplyFromProfileFlowEClass, SupplyFromProfileFlow.class, "SupplyFromProfileFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSupplyFromProfileFlow_Profile(), this.getPurchaseContractProfile(), null, "profile", null, 0, 1, SupplyFromProfileFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getSubContractProfile());
		g2 = createEGenericType(theCargoPackage.getLoadSlot());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(theCommercialPackage.getPurchaseContract());
		g1.getETypeArguments().add(g2);
		initEReference(getSupplyFromProfileFlow_SubProfile(), g1, null, "subProfile", null, 0, 1, SupplyFromProfileFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deliverToProfileFlowEClass, DeliverToProfileFlow.class, "DeliverToProfileFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeliverToProfileFlow_Profile(), this.getSalesContractProfile(), null, "profile", null, 0, 1, DeliverToProfileFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getSubContractProfile());
		g2 = createEGenericType(theCargoPackage.getDischargeSlot());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(theCommercialPackage.getSalesContract());
		g1.getETypeArguments().add(g2);
		initEReference(getDeliverToProfileFlow_SubProfile(), g1, null, "subProfile", null, 0, 1, DeliverToProfileFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(supplyFromSpotFlowEClass, SupplyFromSpotFlow.class, "SupplyFromSpotFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSupplyFromSpotFlow_Market(), theSpotMarketsPackage.getSpotMarket(), null, "market", null, 0, 1, SupplyFromSpotFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deliverToSpotFlowEClass, DeliverToSpotFlow.class, "DeliverToSpotFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeliverToSpotFlow_Market(), theSpotMarketsPackage.getSpotMarket(), null, "market", null, 0, 1, DeliverToSpotFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(profileVesselRestrictionEClass, ProfileVesselRestriction.class, "ProfileVesselRestriction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProfileVesselRestriction_Vessels(), theFleetPackage.getVessel(), null, "vessels", null, 0, -1, ProfileVesselRestriction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(shippingOptionEClass, ShippingOption.class, "ShippingOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getShippingOption_VesselAssignmentType(), theTypesPackage.getVesselAssignmentType(), null, "vesselAssignmentType", null, 0, 1, ShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShippingOption_SpotIndex(), ecorePackage.getEInt(), "spotIndex", null, 0, 1, ShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getShippingOption_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 0, 1, ShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(profileConstraintEClass, ProfileConstraint.class, "ProfileConstraint", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(subProfileConstraintEClass, SubProfileConstraint.class, "SubProfileConstraint", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(minCargoConstraintEClass, MinCargoConstraint.class, "MinCargoConstraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMinCargoConstraint_MinCargoes(), ecorePackage.getEInt(), "minCargoes", null, 0, 1, MinCargoConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMinCargoConstraint_IntervalType(), this.getIntervalType(), "intervalType", "YEARLY", 0, 1, MinCargoConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(maxCargoConstraintEClass, MaxCargoConstraint.class, "MaxCargoConstraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMaxCargoConstraint_MaxCargoes(), ecorePackage.getEInt(), "maxCargoes", null, 0, 1, MaxCargoConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMaxCargoConstraint_IntervalType(), this.getIntervalType(), "intervalType", "YEARLY", 0, 1, MaxCargoConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(periodDistributionProfileConstraintEClass, PeriodDistributionProfileConstraint.class, "PeriodDistributionProfileConstraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPeriodDistributionProfileConstraint_Distributions(), this.getPeriodDistribution(), null, "distributions", null, 0, -1, PeriodDistributionProfileConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(periodDistributionEClass, PeriodDistribution.class, "PeriodDistribution", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPeriodDistribution_Range(), theDateTimePackage.getYearMonth(), "range", null, 0, -1, PeriodDistribution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPeriodDistribution_MinCargoes(), ecorePackage.getEInt(), "minCargoes", null, 0, 1, PeriodDistribution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPeriodDistribution_MaxCargoes(), ecorePackage.getEInt(), "maxCargoes", null, 0, 1, PeriodDistribution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fleetConstraintEClass, FleetConstraint.class, "FleetConstraint", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(targetCargoesOnVesselConstraintEClass, TargetCargoesOnVesselConstraint.class, "TargetCargoesOnVesselConstraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTargetCargoesOnVesselConstraint_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 0, 1, TargetCargoesOnVesselConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTargetCargoesOnVesselConstraint_TargetNumberOfCargoes(), ecorePackage.getEInt(), "targetNumberOfCargoes", null, 0, 1, TargetCargoesOnVesselConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTargetCargoesOnVesselConstraint_IntervalType(), this.getIntervalType(), "intervalType", "YEARLY", 0, 1, TargetCargoesOnVesselConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTargetCargoesOnVesselConstraint_Weight(), ecorePackage.getEInt(), "weight", null, 0, 1, TargetCargoesOnVesselConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mullEntityRowEClass, MullEntityRow.class, "MullEntityRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMullEntityRow_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, MullEntityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMullEntityRow_InitialAllocation(), ecorePackage.getEString(), "initialAllocation", null, 0, 1, MullEntityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMullEntityRow_RelativeEntitlement(), ecorePackage.getEDouble(), "relativeEntitlement", null, 0, 1, MullEntityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMullEntityRow_DesSalesMarketAllocationRows(), this.getDESSalesMarketAllocationRow(), null, "desSalesMarketAllocationRows", null, 0, -1, MullEntityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMullEntityRow_Ports(), thePortPackage.getPort(), null, "ports", null, 0, -1, MullEntityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMullEntityRow_SalesContractAllocationRows(), this.getSalesContractAllocationRow(), null, "salesContractAllocationRows", null, 0, -1, MullEntityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(desSalesMarketAllocationRowEClass, DESSalesMarketAllocationRow.class, "DESSalesMarketAllocationRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDESSalesMarketAllocationRow_DesSalesMarket(), theSpotMarketsPackage.getDESSalesMarket(), null, "desSalesMarket", null, 0, 1, DESSalesMarketAllocationRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(salesContractAllocationRowEClass, SalesContractAllocationRow.class, "SalesContractAllocationRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSalesContractAllocationRow_Contract(), theCommercialPackage.getSalesContract(), null, "contract", null, 0, 1, SalesContractAllocationRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mullProfileEClass, MullProfile.class, "MullProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMullProfile_WindowSize(), ecorePackage.getEInt(), "windowSize", null, 0, 1, MullProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMullProfile_VolumeFlex(), ecorePackage.getEInt(), "volumeFlex", null, 0, 1, MullProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMullProfile_Inventories(), this.getMullSubprofile(), null, "inventories", null, 0, -1, MullProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMullProfile_FullCargoLotValue(), ecorePackage.getEInt(), "fullCargoLotValue", null, 0, 1, MullProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mullSubprofileEClass, MullSubprofile.class, "MullSubprofile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMullSubprofile_Inventory(), theCargoPackage.getInventory(), null, "inventory", null, 0, 1, MullSubprofile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMullSubprofile_EntityTable(), this.getMullEntityRow(), null, "entityTable", null, 0, -1, MullSubprofile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mullAllocationRowEClass, MullAllocationRow.class, "MullAllocationRow", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMullAllocationRow_Weight(), ecorePackage.getEInt(), "weight", null, 0, 1, MullAllocationRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMullAllocationRow_Vessels(), theFleetPackage.getVessel(), null, "vessels", null, 0, -1, MullAllocationRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(intervalTypeEEnum, IntervalType.class, "IntervalType");
		addEEnumLiteral(intervalTypeEEnum, IntervalType.WEEKLY);
		addEEnumLiteral(intervalTypeEEnum, IntervalType.MONTHLY);
		addEEnumLiteral(intervalTypeEEnum, IntervalType.BIMONTHLY);
		addEEnumLiteral(intervalTypeEEnum, IntervalType.QUARTERLY);
		addEEnumLiteral(intervalTypeEEnum, IntervalType.YEARLY);

		initEEnum(lngVolumeUnitEEnum, LNGVolumeUnit.class, "LNGVolumeUnit");
		addEEnumLiteral(lngVolumeUnitEEnum, LNGVolumeUnit.M3);
		addEEnumLiteral(lngVolumeUnitEEnum, LNGVolumeUnit.MT);
		addEEnumLiteral(lngVolumeUnitEEnum, LNGVolumeUnit.MMBTU);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
		// http://www.mmxlabs.com/models/featureOverride
		createFeatureOverrideAnnotations();
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
		  (getSubContractProfile_WindowSize(),
		   source,
		   new String[] {
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getSubContractProfile_WindowSizeUnits(),
		   source,
		   new String[] {
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getMullEntityRow_RelativeEntitlement(),
		   source,
		   new String[] {
			   "formatString", "###.########"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/featureOverride</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createFeatureOverrideAnnotations() {
		String source = "http://www.mmxlabs.com/models/featureOverride";
		addAnnotation
		  (periodDistributionEClass,
		   source,
		   new String[] {
		   });
	}

} //ADPPackageImpl
