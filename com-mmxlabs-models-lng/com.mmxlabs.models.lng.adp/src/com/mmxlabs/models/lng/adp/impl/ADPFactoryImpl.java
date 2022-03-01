/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.*;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ADPFactoryImpl extends EFactoryImpl implements ADPFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ADPFactory init() {
		try {
			ADPFactory theADPFactory = (ADPFactory)EPackage.Registry.INSTANCE.getEFactory(ADPPackage.eNS_URI);
			if (theADPFactory != null) {
				return theADPFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ADPFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ADPFactoryImpl() {
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
			case ADPPackage.ADP_MODEL: return createADPModel();
			case ADPPackage.FLEET_PROFILE: return createFleetProfile();
			case ADPPackage.CONTRACT_PROFILE: return createContractProfile();
			case ADPPackage.PURCHASE_CONTRACT_PROFILE: return createPurchaseContractProfile();
			case ADPPackage.SALES_CONTRACT_PROFILE: return createSalesContractProfile();
			case ADPPackage.SUB_CONTRACT_PROFILE: return createSubContractProfile();
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL: return createCargoSizeDistributionModel();
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL: return createCargoNumberDistributionModel();
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL: return createCargoByQuarterDistributionModel();
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL: return createCargoIntervalDistributionModel();
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL: return createPreDefinedDistributionModel();
			case ADPPackage.PRE_DEFINED_DATE: return createPreDefinedDate();
			case ADPPackage.FLOW_TYPE: return createFlowType();
			case ADPPackage.SUPPLY_FROM_FLOW: return createSupplyFromFlow();
			case ADPPackage.DELIVER_TO_FLOW: return createDeliverToFlow();
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW: return createSupplyFromProfileFlow();
			case ADPPackage.DELIVER_TO_PROFILE_FLOW: return createDeliverToProfileFlow();
			case ADPPackage.SUPPLY_FROM_SPOT_FLOW: return createSupplyFromSpotFlow();
			case ADPPackage.DELIVER_TO_SPOT_FLOW: return createDeliverToSpotFlow();
			case ADPPackage.PROFILE_VESSEL_RESTRICTION: return createProfileVesselRestriction();
			case ADPPackage.SHIPPING_OPTION: return createShippingOption();
			case ADPPackage.MIN_CARGO_CONSTRAINT: return createMinCargoConstraint();
			case ADPPackage.MAX_CARGO_CONSTRAINT: return createMaxCargoConstraint();
			case ADPPackage.PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT: return createPeriodDistributionProfileConstraint();
			case ADPPackage.PERIOD_DISTRIBUTION: return createPeriodDistribution();
			case ADPPackage.TARGET_CARGOES_ON_VESSEL_CONSTRAINT: return createTargetCargoesOnVesselConstraint();
			case ADPPackage.MULL_ENTITY_ROW: return createMullEntityRow();
			case ADPPackage.DES_SALES_MARKET_ALLOCATION_ROW: return createDESSalesMarketAllocationRow();
			case ADPPackage.SALES_CONTRACT_ALLOCATION_ROW: return createSalesContractAllocationRow();
			case ADPPackage.MULL_PROFILE: return createMullProfile();
			case ADPPackage.MULL_SUBPROFILE: return createMullSubprofile();
			case ADPPackage.MULL_CARGO_WRAPPER: return createMullCargoWrapper();
			case ADPPackage.SPACING_PROFILE: return createSpacingProfile();
			case ADPPackage.FOB_SPACING_ALLOCATION: return createFobSpacingAllocation();
			case ADPPackage.DES_SPACING_ALLOCATION: return createDesSpacingAllocation();
			case ADPPackage.DES_SPACING_ROW: return createDesSpacingRow();
			case ADPPackage.SPACING_ALLOCATION: return createSpacingAllocation();
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
			case ADPPackage.INTERVAL_TYPE:
				return createIntervalTypeFromString(eDataType, initialValue);
			case ADPPackage.LNG_VOLUME_UNIT:
				return createLNGVolumeUnitFromString(eDataType, initialValue);
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
			case ADPPackage.INTERVAL_TYPE:
				return convertIntervalTypeToString(eDataType, instanceValue);
			case ADPPackage.LNG_VOLUME_UNIT:
				return convertLNGVolumeUnitToString(eDataType, instanceValue);
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
	public ADPModel createADPModel() {
		ADPModelImpl adpModel = new ADPModelImpl();
		return adpModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FleetProfile createFleetProfile() {
		FleetProfileImpl fleetProfile = new FleetProfileImpl();
		return fleetProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <T extends Slot<U>, U extends Contract> ContractProfile<T, U> createContractProfile() {
		ContractProfileImpl<T, U> contractProfile = new ContractProfileImpl<T, U>();
		return contractProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoSizeDistributionModel createCargoSizeDistributionModel() {
		CargoSizeDistributionModelImpl cargoSizeDistributionModel = new CargoSizeDistributionModelImpl();
		return cargoSizeDistributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoNumberDistributionModel createCargoNumberDistributionModel() {
		CargoNumberDistributionModelImpl cargoNumberDistributionModel = new CargoNumberDistributionModelImpl();
		return cargoNumberDistributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PurchaseContractProfile createPurchaseContractProfile() {
		PurchaseContractProfileImpl purchaseContractProfile = new PurchaseContractProfileImpl();
		return purchaseContractProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SalesContractProfile createSalesContractProfile() {
		SalesContractProfileImpl salesContractProfile = new SalesContractProfileImpl();
		return salesContractProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <T extends Slot<U>, U extends Contract> SubContractProfile<T, U> createSubContractProfile() {
		SubContractProfileImpl<T, U> subContractProfile = new SubContractProfileImpl<T, U>();
		return subContractProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PeriodDistributionProfileConstraint createPeriodDistributionProfileConstraint() {
		PeriodDistributionProfileConstraintImpl periodDistributionProfileConstraint = new PeriodDistributionProfileConstraintImpl();
		return periodDistributionProfileConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PeriodDistribution createPeriodDistribution() {
		PeriodDistributionImpl periodDistribution = new PeriodDistributionImpl();
		return periodDistribution;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoByQuarterDistributionModel createCargoByQuarterDistributionModel() {
		CargoByQuarterDistributionModelImpl cargoByQuarterDistributionModel = new CargoByQuarterDistributionModelImpl();
		return cargoByQuarterDistributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoIntervalDistributionModel createCargoIntervalDistributionModel() {
		CargoIntervalDistributionModelImpl cargoIntervalDistributionModel = new CargoIntervalDistributionModelImpl();
		return cargoIntervalDistributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PreDefinedDistributionModel createPreDefinedDistributionModel() {
		PreDefinedDistributionModelImpl preDefinedDistributionModel = new PreDefinedDistributionModelImpl();
		return preDefinedDistributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PreDefinedDate createPreDefinedDate() {
		PreDefinedDateImpl preDefinedDate = new PreDefinedDateImpl();
		return preDefinedDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FlowType createFlowType() {
		FlowTypeImpl flowType = new FlowTypeImpl();
		return flowType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SupplyFromFlow createSupplyFromFlow() {
		SupplyFromFlowImpl supplyFromFlow = new SupplyFromFlowImpl();
		return supplyFromFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeliverToFlow createDeliverToFlow() {
		DeliverToFlowImpl deliverToFlow = new DeliverToFlowImpl();
		return deliverToFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SupplyFromProfileFlow createSupplyFromProfileFlow() {
		SupplyFromProfileFlowImpl supplyFromProfileFlow = new SupplyFromProfileFlowImpl();
		return supplyFromProfileFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeliverToProfileFlow createDeliverToProfileFlow() {
		DeliverToProfileFlowImpl deliverToProfileFlow = new DeliverToProfileFlowImpl();
		return deliverToProfileFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SupplyFromSpotFlow createSupplyFromSpotFlow() {
		SupplyFromSpotFlowImpl supplyFromSpotFlow = new SupplyFromSpotFlowImpl();
		return supplyFromSpotFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeliverToSpotFlow createDeliverToSpotFlow() {
		DeliverToSpotFlowImpl deliverToSpotFlow = new DeliverToSpotFlowImpl();
		return deliverToSpotFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfileVesselRestriction createProfileVesselRestriction() {
		ProfileVesselRestrictionImpl profileVesselRestriction = new ProfileVesselRestrictionImpl();
		return profileVesselRestriction;
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
	public MinCargoConstraint createMinCargoConstraint() {
		MinCargoConstraintImpl minCargoConstraint = new MinCargoConstraintImpl();
		return minCargoConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MaxCargoConstraint createMaxCargoConstraint() {
		MaxCargoConstraintImpl maxCargoConstraint = new MaxCargoConstraintImpl();
		return maxCargoConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TargetCargoesOnVesselConstraint createTargetCargoesOnVesselConstraint() {
		TargetCargoesOnVesselConstraintImpl targetCargoesOnVesselConstraint = new TargetCargoesOnVesselConstraintImpl();
		return targetCargoesOnVesselConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MullEntityRow createMullEntityRow() {
		MullEntityRowImpl mullEntityRow = new MullEntityRowImpl();
		return mullEntityRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DESSalesMarketAllocationRow createDESSalesMarketAllocationRow() {
		DESSalesMarketAllocationRowImpl desSalesMarketAllocationRow = new DESSalesMarketAllocationRowImpl();
		return desSalesMarketAllocationRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SalesContractAllocationRow createSalesContractAllocationRow() {
		SalesContractAllocationRowImpl salesContractAllocationRow = new SalesContractAllocationRowImpl();
		return salesContractAllocationRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MullProfile createMullProfile() {
		MullProfileImpl mullProfile = new MullProfileImpl();
		return mullProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MullSubprofile createMullSubprofile() {
		MullSubprofileImpl mullSubprofile = new MullSubprofileImpl();
		return mullSubprofile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MullCargoWrapper createMullCargoWrapper() {
		MullCargoWrapperImpl mullCargoWrapper = new MullCargoWrapperImpl();
		return mullCargoWrapper;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpacingProfile createSpacingProfile() {
		SpacingProfileImpl spacingProfile = new SpacingProfileImpl();
		return spacingProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FobSpacingAllocation createFobSpacingAllocation() {
		FobSpacingAllocationImpl fobSpacingAllocation = new FobSpacingAllocationImpl();
		return fobSpacingAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DesSpacingAllocation createDesSpacingAllocation() {
		DesSpacingAllocationImpl desSpacingAllocation = new DesSpacingAllocationImpl();
		return desSpacingAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DesSpacingRow createDesSpacingRow() {
		DesSpacingRowImpl desSpacingRow = new DesSpacingRowImpl();
		return desSpacingRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpacingAllocation createSpacingAllocation() {
		SpacingAllocationImpl spacingAllocation = new SpacingAllocationImpl();
		return spacingAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IntervalType createIntervalTypeFromString(EDataType eDataType, String initialValue) {
		IntervalType result = IntervalType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIntervalTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGVolumeUnit createLNGVolumeUnitFromString(EDataType eDataType, String initialValue) {
		LNGVolumeUnit result = LNGVolumeUnit.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLNGVolumeUnitToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ADPPackage getADPPackage() {
		return (ADPPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ADPPackage getPackage() {
		return ADPPackage.eINSTANCE;
	}

} //ADPFactoryImpl
