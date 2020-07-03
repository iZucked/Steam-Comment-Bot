/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFacilityType;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.StartHeelOptions;
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.cargo.VesselType;
import com.mmxlabs.models.lng.cargo.VesselTypeGroup;
import com.mmxlabs.models.lng.cargo.VoyageSpecification;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import org.eclipse.emf.common.util.URI;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CargoPackageImpl extends EPackageImpl implements CargoPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass loadSlotEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dischargeSlotEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spotSlotEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spotLoadSlotEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spotDischargeSlotEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselAvailabilityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass maintenanceEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dryDockEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass charterOutEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass assignableElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselTypeGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass endHeelOptionsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass startHeelOptionsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass inventoryEventRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass inventoryCapacityRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass inventoryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass canalBookingSlotEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass canalBookingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scheduleSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nonShippedCargoSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselScheduleSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scheduleSpecificationEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselEventSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass voyageSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass charterInMarketOverrideEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass paperDealEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass buyPaperDealEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sellPaperDealEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dealSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum cargoTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum vesselTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum eVesselTankStateEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum inventoryFacilityTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum inventoryFrequencyEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum paperPricingTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType schedulingTimeWindowEDataType = null;

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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private CargoPackageImpl() {
		super(eNS_URI, CargoFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link CargoPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static CargoPackage init() {
		if (isInited) return (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredCargoPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		CargoPackageImpl theCargoPackage = registeredCargoPackage instanceof CargoPackageImpl ? (CargoPackageImpl)registeredCargoPackage : new CargoPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		CommercialPackage.eINSTANCE.eClass();
		DateTimePackage.eINSTANCE.eClass();
		FleetPackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();
		PortPackage.eINSTANCE.eClass();
		PricingPackage.eINSTANCE.eClass();
		SpotMarketsPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theCargoPackage.createPackageContents();

		// Initialize created meta-data
		theCargoPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theCargoPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(CargoPackage.eNS_URI, theCargoPackage);
		return theCargoPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargo() {
		return cargoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargo_AllowRewiring() {
		return (EAttribute)cargoEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargo_Slots() {
		return (EReference)cargoEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getCargo__GetCargoType() {
		return cargoEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getCargo__GetSortedSlots() {
		return cargoEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getCargo__GetLoadName() {
		return cargoEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSlot() {
		return slotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_WindowStart() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_WindowStartTime() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_WindowSize() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_WindowSizeUnits() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_WindowFlex() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_WindowFlexUnits() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlot_Port() {
		return (EReference)slotEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlot_Contract() {
		return (EReference)slotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_Counterparty() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_Cn() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_Duration() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_VolumeLimitsUnit() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_MinQuantity() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_MaxQuantity() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_OperationalTolerance() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_FullCargoLot() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_Optional() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_PriceExpression() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlot_Cargo() {
		return (EReference)slotEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_PricingEvent() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_PricingDate() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_Notes() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_ShippingDaysRestriction() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlot_Entity() {
		return (EReference)slotEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlot_RestrictedContracts() {
		return (EReference)slotEClass.getEStructuralFeatures().get(24);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_RestrictedContractsArePermissive() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(25);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_RestrictedContractsOverride() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(26);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlot_RestrictedPorts() {
		return (EReference)slotEClass.getEStructuralFeatures().get(27);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_RestrictedPortsArePermissive() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(28);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_RestrictedPortsOverride() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(29);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlot_RestrictedSlots() {
		return (EReference)slotEClass.getEStructuralFeatures().get(33);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_RestrictedSlotsArePermissive() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(34);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlot_RestrictedVessels() {
		return (EReference)slotEClass.getEStructuralFeatures().get(30);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_RestrictedVesselsArePermissive() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(31);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_RestrictedVesselsOverride() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(32);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_MiscCosts() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(35);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_CancellationExpression() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(36);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlot_NominatedVessel() {
		return (EReference)slotEClass.getEStructuralFeatures().get(37);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_Locked() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(38);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_Cancelled() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(39);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlot_WindowCounterParty() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(40);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateMinQuantity() {
		return slotEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateMaxQuantity() {
		return slotEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateOperationalTolerance() {
		return slotEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateVolumeLimitsUnit() {
		return slotEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateEntity() {
		return slotEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateCancellationExpression() {
		return slotEClass.getEOperations().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegatePricingEvent() {
		return slotEClass.getEOperations().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetPricingDateAsDateTime() {
		return slotEClass.getEOperations().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotContractParams() {
		return slotEClass.getEOperations().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateCounterparty() {
		return slotEClass.getEOperations().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateCN() {
		return slotEClass.getEOperations().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateShippingDaysRestriction() {
		return slotEClass.getEOperations().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateFullCargoLot() {
		return slotEClass.getEOperations().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateContractRestrictionsArePermissive() {
		return slotEClass.getEOperations().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegatePortRestrictionsArePermissive() {
		return slotEClass.getEOperations().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateVesselRestrictionsArePermissive() {
		return slotEClass.getEOperations().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateContractRestrictions() {
		return slotEClass.getEOperations().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegatePortRestrictions() {
		return slotEClass.getEOperations().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateVesselRestrictions() {
		return slotEClass.getEOperations().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSchedulingTimeWindow() {
		return slotEClass.getEOperations().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlot__GetSlotOrDelegateDaysBuffer() {
		return slotEClass.getEOperations().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLoadSlot() {
		return loadSlotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLoadSlot_CargoCV() {
		return (EAttribute)loadSlotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLoadSlot_SchedulePurge() {
		return (EAttribute)loadSlotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLoadSlot_ArriveCold() {
		return (EAttribute)loadSlotEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLoadSlot_DESPurchase() {
		return (EAttribute)loadSlotEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getLoadSlot_TransferFrom() {
		return (EReference)loadSlotEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLoadSlot_SalesDeliveryType() {
		return (EAttribute)loadSlotEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLoadSlot_DesPurchaseDealType() {
		return (EAttribute)loadSlotEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLoadSlot_VolumeCounterParty() {
		return (EAttribute)loadSlotEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getLoadSlot__GetSlotOrDelegateCV() {
		return loadSlotEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getLoadSlot__GetSlotOrDelegateDeliveryType() {
		return loadSlotEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getLoadSlot__GetSlotOrDelegateDESPurchaseDealType() {
		return loadSlotEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDischargeSlot() {
		return dischargeSlotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDischargeSlot_FOBSale() {
		return (EAttribute)dischargeSlotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDischargeSlot_PurchaseDeliveryType() {
		return (EAttribute)dischargeSlotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDischargeSlot_TransferTo() {
		return (EReference)dischargeSlotEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDischargeSlot_MinCvValue() {
		return (EAttribute)dischargeSlotEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDischargeSlot_MaxCvValue() {
		return (EAttribute)dischargeSlotEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDischargeSlot_FobSaleDealType() {
		return (EAttribute)dischargeSlotEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getDischargeSlot__GetSlotOrDelegateMinCv() {
		return dischargeSlotEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getDischargeSlot__GetSlotOrDelegateMaxCv() {
		return dischargeSlotEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getDischargeSlot__GetSlotOrDelegateDeliveryType() {
		return dischargeSlotEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getDischargeSlot__GetSlotOrDelegateFOBSaleDealType() {
		return dischargeSlotEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoModel() {
		return cargoModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_LoadSlots() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_DischargeSlots() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_Cargoes() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_CargoGroups() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_VesselAvailabilities() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_VesselEvents() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_VesselTypeGroups() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_InventoryModels() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_CanalBookings() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_CharterInMarketOverrides() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_PaperDeals() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoModel_DealSets() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSpotSlot() {
		return spotSlotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotSlot_Market() {
		return (EReference)spotSlotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSpotLoadSlot() {
		return spotLoadSlotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSpotDischargeSlot() {
		return spotDischargeSlotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoGroup() {
		return cargoGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoGroup_Cargoes() {
		return (EReference)cargoGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselAvailability() {
		return vesselAvailabilityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_Fleet() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselAvailability_Vessel() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_TimeCharterRate() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselAvailability_StartAt() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_StartAfter() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_StartBy() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselAvailability_EndAt() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_EndAfter() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_EndBy() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselAvailability_StartHeel() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselAvailability_EndHeel() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_ForceHireCostOnlyEndRule() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_Optional() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_RepositioningFee() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselAvailability_BallastBonusContract() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_CharterNumber() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselAvailability_CharterContract() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_MinDuration() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselAvailability_MaxDuration() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselAvailability__GetStartByAsDateTime() {
		return vesselAvailabilityEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselAvailability__GetStartAfterAsDateTime() {
		return vesselAvailabilityEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselAvailability__GetEndByAsDateTime() {
		return vesselAvailabilityEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselAvailability__GetEndAfterAsDateTime() {
		return vesselAvailabilityEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselAvailability__GetCharterOrDelegateBallastBonusContract() {
		return vesselAvailabilityEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselAvailability__GetCharterOrDelegateMinDuration() {
		return vesselAvailabilityEClass.getEOperations().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselAvailability__GetCharterOrDelegateMaxDuration() {
		return vesselAvailabilityEClass.getEOperations().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselAvailability__Jsonid() {
		return vesselAvailabilityEClass.getEOperations().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselAvailability__GetCharterOrDelegateRepositioningFee() {
		return vesselAvailabilityEClass.getEOperations().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselAvailability_Entity() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselEvent() {
		return vesselEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselEvent_DurationInDays() {
		return (EAttribute)vesselEventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselEvent_AllowedVessels() {
		return (EReference)vesselEventEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselEvent_Port() {
		return (EReference)vesselEventEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselEvent_StartAfter() {
		return (EAttribute)vesselEventEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselEvent_StartBy() {
		return (EAttribute)vesselEventEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselEvent__GetStartByAsDateTime() {
		return vesselEventEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselEvent__GetStartAfterAsDateTime() {
		return vesselEventEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMaintenanceEvent() {
		return maintenanceEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDryDockEvent() {
		return dryDockEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCharterOutEvent() {
		return charterOutEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutEvent_Optional() {
		return (EAttribute)charterOutEventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterOutEvent_RelocateTo() {
		return (EReference)charterOutEventEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutEvent_RepositioningFee() {
		return (EAttribute)charterOutEventEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterOutEvent_RequiredHeel() {
		return (EReference)charterOutEventEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterOutEvent_AvailableHeel() {
		return (EReference)charterOutEventEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutEvent_HireRate() {
		return (EAttribute)charterOutEventEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutEvent_BallastBonus() {
		return (EAttribute)charterOutEventEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getCharterOutEvent__GetEndPort() {
		return charterOutEventEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAssignableElement() {
		return assignableElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAssignableElement_SpotIndex() {
		return (EAttribute)assignableElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAssignableElement_SequenceHint() {
		return (EAttribute)assignableElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAssignableElement_Locked() {
		return (EAttribute)assignableElementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAssignableElement_VesselAssignmentType() {
		return (EReference)assignableElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselTypeGroup() {
		return vesselTypeGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselTypeGroup_VesselType() {
		return (EAttribute)vesselTypeGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEndHeelOptions() {
		return endHeelOptionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEndHeelOptions_TankState() {
		return (EAttribute)endHeelOptionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEndHeelOptions_MinimumEndHeel() {
		return (EAttribute)endHeelOptionsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEndHeelOptions_MaximumEndHeel() {
		return (EAttribute)endHeelOptionsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEndHeelOptions_UseLastHeelPrice() {
		return (EAttribute)endHeelOptionsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEndHeelOptions_PriceExpression() {
		return (EAttribute)endHeelOptionsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getStartHeelOptions() {
		return startHeelOptionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStartHeelOptions_CvValue() {
		return (EAttribute)startHeelOptionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStartHeelOptions_MinVolumeAvailable() {
		return (EAttribute)startHeelOptionsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStartHeelOptions_MaxVolumeAvailable() {
		return (EAttribute)startHeelOptionsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStartHeelOptions_PriceExpression() {
		return (EAttribute)startHeelOptionsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getInventoryEventRow() {
		return inventoryEventRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryEventRow_StartDate() {
		return (EAttribute)inventoryEventRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryEventRow_EndDate() {
		return (EAttribute)inventoryEventRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryEventRow_Period() {
		return (EAttribute)inventoryEventRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryEventRow_CounterParty() {
		return (EAttribute)inventoryEventRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryEventRow_Reliability() {
		return (EAttribute)inventoryEventRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryEventRow_Volume() {
		return (EAttribute)inventoryEventRowEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryEventRow_ForecastDate() {
		return (EAttribute)inventoryEventRowEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryEventRow_VolumeLow() {
		return (EAttribute)inventoryEventRowEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryEventRow_VolumeHigh() {
		return (EAttribute)inventoryEventRowEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getInventoryEventRow__GetReliableVolume() {
		return inventoryEventRowEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getInventoryCapacityRow() {
		return inventoryCapacityRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryCapacityRow_Date() {
		return (EAttribute)inventoryCapacityRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryCapacityRow_MinVolume() {
		return (EAttribute)inventoryCapacityRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventoryCapacityRow_MaxVolume() {
		return (EAttribute)inventoryCapacityRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getInventory() {
		return inventoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getInventory_Feeds() {
		return (EReference)inventoryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getInventory_Offtakes() {
		return (EReference)inventoryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getInventory_Capacities() {
		return (EReference)inventoryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getInventory_FacilityType() {
		return (EAttribute)inventoryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getInventory_Port() {
		return (EReference)inventoryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCanalBookingSlot() {
		return canalBookingSlotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCanalBookingSlot_RouteOption() {
		return (EAttribute)canalBookingSlotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCanalBookingSlot_BookingDate() {
		return (EAttribute)canalBookingSlotEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCanalBookingSlot_CanalEntrance() {
		return (EAttribute)canalBookingSlotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCanalBookingSlot_Slot() {
		return (EReference)canalBookingSlotEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCanalBookingSlot_Notes() {
		return (EAttribute)canalBookingSlotEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCanalBookings() {
		return canalBookingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCanalBookings_CanalBookingSlots() {
		return (EReference)canalBookingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCanalBookings_StrictBoundaryOffsetDays() {
		return (EAttribute)canalBookingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCanalBookings_RelaxedBoundaryOffsetDays() {
		return (EAttribute)canalBookingsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCanalBookings_ArrivalMarginHours() {
		return (EAttribute)canalBookingsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCanalBookings_FlexibleBookingAmountNorthbound() {
		return (EAttribute)canalBookingsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCanalBookings_FlexibleBookingAmountSouthbound() {
		return (EAttribute)canalBookingsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCanalBookings_NorthboundMaxIdleDays() {
		return (EAttribute)canalBookingsEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCanalBookings_SouthboundMaxIdleDays() {
		return (EAttribute)canalBookingsEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getScheduleSpecification() {
		return scheduleSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getScheduleSpecification_VesselScheduleSpecifications() {
		return (EReference)scheduleSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getScheduleSpecification_NonShippedCargoSpecifications() {
		return (EReference)scheduleSpecificationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getScheduleSpecification_OpenEvents() {
		return (EReference)scheduleSpecificationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNonShippedCargoSpecification() {
		return nonShippedCargoSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNonShippedCargoSpecification_SlotSpecifications() {
		return (EReference)nonShippedCargoSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselScheduleSpecification() {
		return vesselScheduleSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselScheduleSpecification_VesselAllocation() {
		return (EReference)vesselScheduleSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselScheduleSpecification_SpotIndex() {
		return (EAttribute)vesselScheduleSpecificationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselScheduleSpecification_Events() {
		return (EReference)vesselScheduleSpecificationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getScheduleSpecificationEvent() {
		return scheduleSpecificationEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselEventSpecification() {
		return vesselEventSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselEventSpecification_VesselEvent() {
		return (EReference)vesselEventSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVoyageSpecification() {
		return voyageSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSlotSpecification() {
		return slotSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlotSpecification_Slot() {
		return (EReference)slotSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCharterInMarketOverride() {
		return charterInMarketOverrideEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterInMarketOverride_CharterInMarket() {
		return (EReference)charterInMarketOverrideEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarketOverride_SpotIndex() {
		return (EAttribute)charterInMarketOverrideEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterInMarketOverride_StartHeel() {
		return (EReference)charterInMarketOverrideEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarketOverride_StartDate() {
		return (EAttribute)charterInMarketOverrideEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterInMarketOverride_EndPort() {
		return (EReference)charterInMarketOverrideEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarketOverride_EndDate() {
		return (EAttribute)charterInMarketOverrideEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterInMarketOverride_EndHeel() {
		return (EReference)charterInMarketOverrideEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarketOverride_IncludeBallastBonus() {
		return (EAttribute)charterInMarketOverrideEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarketOverride_MinDuration() {
		return (EAttribute)charterInMarketOverrideEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarketOverride_MaxDuration() {
		return (EAttribute)charterInMarketOverrideEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getCharterInMarketOverride__GetStartDateAsDateTime() {
		return charterInMarketOverrideEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getCharterInMarketOverride__GetEndDateAsDateTime() {
		return charterInMarketOverrideEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getCharterInMarketOverride__GetLocalOrDelegateMinDuration() {
		return charterInMarketOverrideEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getCharterInMarketOverride__GetLocalOrDelegateMaxDuration() {
		return charterInMarketOverrideEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPaperDeal() {
		return paperDealEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPaperDeal_Quantity() {
		return (EAttribute)paperDealEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPaperDeal_PricingMonth() {
		return (EAttribute)paperDealEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPaperDeal_StartDate() {
		return (EAttribute)paperDealEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPaperDeal_EndDate() {
		return (EAttribute)paperDealEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPaperDeal_Entity() {
		return (EReference)paperDealEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPaperDeal_Year() {
		return (EAttribute)paperDealEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPaperDeal_Comment() {
		return (EAttribute)paperDealEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPaperDeal_Price() {
		return (EAttribute)paperDealEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPaperDeal_PricingType() {
		return (EAttribute)paperDealEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPaperDeal_Index() {
		return (EAttribute)paperDealEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPaperDeal_Instrument() {
		return (EReference)paperDealEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBuyPaperDeal() {
		return buyPaperDealEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSellPaperDeal() {
		return sellPaperDealEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDealSet() {
		return dealSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDealSet_Slots() {
		return (EReference)dealSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDealSet_PaperDeals() {
		return (EReference)dealSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getCargoType() {
		return cargoTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getVesselType() {
		return vesselTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getEVesselTankState() {
		return eVesselTankStateEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getInventoryFacilityType() {
		return inventoryFacilityTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getInventoryFrequency() {
		return inventoryFrequencyEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getPaperPricingType() {
		return paperPricingTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getSchedulingTimeWindow() {
		return schedulingTimeWindowEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoFactory getCargoFactory() {
		return (CargoFactory)getEFactoryInstance();
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
		cargoModelEClass = createEClass(CARGO_MODEL);
		createEReference(cargoModelEClass, CARGO_MODEL__LOAD_SLOTS);
		createEReference(cargoModelEClass, CARGO_MODEL__DISCHARGE_SLOTS);
		createEReference(cargoModelEClass, CARGO_MODEL__CARGOES);
		createEReference(cargoModelEClass, CARGO_MODEL__CARGO_GROUPS);
		createEReference(cargoModelEClass, CARGO_MODEL__VESSEL_AVAILABILITIES);
		createEReference(cargoModelEClass, CARGO_MODEL__VESSEL_EVENTS);
		createEReference(cargoModelEClass, CARGO_MODEL__VESSEL_TYPE_GROUPS);
		createEReference(cargoModelEClass, CARGO_MODEL__INVENTORY_MODELS);
		createEReference(cargoModelEClass, CARGO_MODEL__CANAL_BOOKINGS);
		createEReference(cargoModelEClass, CARGO_MODEL__CHARTER_IN_MARKET_OVERRIDES);
		createEReference(cargoModelEClass, CARGO_MODEL__PAPER_DEALS);
		createEReference(cargoModelEClass, CARGO_MODEL__DEAL_SETS);

		cargoEClass = createEClass(CARGO);
		createEAttribute(cargoEClass, CARGO__ALLOW_REWIRING);
		createEReference(cargoEClass, CARGO__SLOTS);
		createEOperation(cargoEClass, CARGO___GET_CARGO_TYPE);
		createEOperation(cargoEClass, CARGO___GET_SORTED_SLOTS);
		createEOperation(cargoEClass, CARGO___GET_LOAD_NAME);

		slotEClass = createEClass(SLOT);
		createEReference(slotEClass, SLOT__CONTRACT);
		createEAttribute(slotEClass, SLOT__COUNTERPARTY);
		createEAttribute(slotEClass, SLOT__CN);
		createEReference(slotEClass, SLOT__PORT);
		createEAttribute(slotEClass, SLOT__WINDOW_START);
		createEAttribute(slotEClass, SLOT__WINDOW_START_TIME);
		createEAttribute(slotEClass, SLOT__WINDOW_SIZE);
		createEAttribute(slotEClass, SLOT__WINDOW_SIZE_UNITS);
		createEAttribute(slotEClass, SLOT__WINDOW_FLEX);
		createEAttribute(slotEClass, SLOT__WINDOW_FLEX_UNITS);
		createEAttribute(slotEClass, SLOT__DURATION);
		createEAttribute(slotEClass, SLOT__VOLUME_LIMITS_UNIT);
		createEAttribute(slotEClass, SLOT__MIN_QUANTITY);
		createEAttribute(slotEClass, SLOT__MAX_QUANTITY);
		createEAttribute(slotEClass, SLOT__OPERATIONAL_TOLERANCE);
		createEAttribute(slotEClass, SLOT__FULL_CARGO_LOT);
		createEAttribute(slotEClass, SLOT__OPTIONAL);
		createEAttribute(slotEClass, SLOT__PRICE_EXPRESSION);
		createEReference(slotEClass, SLOT__CARGO);
		createEAttribute(slotEClass, SLOT__PRICING_EVENT);
		createEAttribute(slotEClass, SLOT__PRICING_DATE);
		createEAttribute(slotEClass, SLOT__NOTES);
		createEAttribute(slotEClass, SLOT__SHIPPING_DAYS_RESTRICTION);
		createEReference(slotEClass, SLOT__ENTITY);
		createEReference(slotEClass, SLOT__RESTRICTED_CONTRACTS);
		createEAttribute(slotEClass, SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE);
		createEAttribute(slotEClass, SLOT__RESTRICTED_CONTRACTS_OVERRIDE);
		createEReference(slotEClass, SLOT__RESTRICTED_PORTS);
		createEAttribute(slotEClass, SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE);
		createEAttribute(slotEClass, SLOT__RESTRICTED_PORTS_OVERRIDE);
		createEReference(slotEClass, SLOT__RESTRICTED_VESSELS);
		createEAttribute(slotEClass, SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE);
		createEAttribute(slotEClass, SLOT__RESTRICTED_VESSELS_OVERRIDE);
		createEReference(slotEClass, SLOT__RESTRICTED_SLOTS);
		createEAttribute(slotEClass, SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE);
		createEAttribute(slotEClass, SLOT__MISC_COSTS);
		createEAttribute(slotEClass, SLOT__CANCELLATION_EXPRESSION);
		createEReference(slotEClass, SLOT__NOMINATED_VESSEL);
		createEAttribute(slotEClass, SLOT__LOCKED);
		createEAttribute(slotEClass, SLOT__CANCELLED);
		createEAttribute(slotEClass, SLOT__WINDOW_COUNTER_PARTY);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_ENTITY);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT);
		createEOperation(slotEClass, SLOT___GET_PRICING_DATE_AS_DATE_TIME);
		createEOperation(slotEClass, SLOT___GET_SLOT_CONTRACT_PARAMS);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_CN);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS_ARE_PERMISSIVE);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS_ARE_PERMISSIVE);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS_ARE_PERMISSIVE);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS);
		createEOperation(slotEClass, SLOT___GET_SCHEDULING_TIME_WINDOW);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATE_DAYS_BUFFER);

		loadSlotEClass = createEClass(LOAD_SLOT);
		createEAttribute(loadSlotEClass, LOAD_SLOT__CARGO_CV);
		createEAttribute(loadSlotEClass, LOAD_SLOT__SCHEDULE_PURGE);
		createEAttribute(loadSlotEClass, LOAD_SLOT__ARRIVE_COLD);
		createEAttribute(loadSlotEClass, LOAD_SLOT__DES_PURCHASE);
		createEReference(loadSlotEClass, LOAD_SLOT__TRANSFER_FROM);
		createEAttribute(loadSlotEClass, LOAD_SLOT__SALES_DELIVERY_TYPE);
		createEAttribute(loadSlotEClass, LOAD_SLOT__DES_PURCHASE_DEAL_TYPE);
		createEAttribute(loadSlotEClass, LOAD_SLOT__VOLUME_COUNTER_PARTY);
		createEOperation(loadSlotEClass, LOAD_SLOT___GET_SLOT_OR_DELEGATE_CV);
		createEOperation(loadSlotEClass, LOAD_SLOT___GET_SLOT_OR_DELEGATE_DELIVERY_TYPE);
		createEOperation(loadSlotEClass, LOAD_SLOT___GET_SLOT_OR_DELEGATE_DES_PURCHASE_DEAL_TYPE);

		dischargeSlotEClass = createEClass(DISCHARGE_SLOT);
		createEAttribute(dischargeSlotEClass, DISCHARGE_SLOT__FOB_SALE);
		createEAttribute(dischargeSlotEClass, DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE);
		createEReference(dischargeSlotEClass, DISCHARGE_SLOT__TRANSFER_TO);
		createEAttribute(dischargeSlotEClass, DISCHARGE_SLOT__MIN_CV_VALUE);
		createEAttribute(dischargeSlotEClass, DISCHARGE_SLOT__MAX_CV_VALUE);
		createEAttribute(dischargeSlotEClass, DISCHARGE_SLOT__FOB_SALE_DEAL_TYPE);
		createEOperation(dischargeSlotEClass, DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MIN_CV);
		createEOperation(dischargeSlotEClass, DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MAX_CV);
		createEOperation(dischargeSlotEClass, DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_DELIVERY_TYPE);
		createEOperation(dischargeSlotEClass, DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_FOB_SALE_DEAL_TYPE);

		spotSlotEClass = createEClass(SPOT_SLOT);
		createEReference(spotSlotEClass, SPOT_SLOT__MARKET);

		spotLoadSlotEClass = createEClass(SPOT_LOAD_SLOT);

		spotDischargeSlotEClass = createEClass(SPOT_DISCHARGE_SLOT);

		cargoGroupEClass = createEClass(CARGO_GROUP);
		createEReference(cargoGroupEClass, CARGO_GROUP__CARGOES);

		vesselAvailabilityEClass = createEClass(VESSEL_AVAILABILITY);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__FLEET);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__OPTIONAL);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__VESSEL);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__CHARTER_NUMBER);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__ENTITY);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__TIME_CHARTER_RATE);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__START_AT);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__START_AFTER);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__START_BY);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__END_AT);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__END_AFTER);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__END_BY);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__START_HEEL);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__END_HEEL);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__REPOSITIONING_FEE);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__CHARTER_CONTRACT);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__MIN_DURATION);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__MAX_DURATION);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_START_BY_AS_DATE_TIME);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_START_AFTER_AS_DATE_TIME);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_END_BY_AS_DATE_TIME);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_END_AFTER_AS_DATE_TIME);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_BALLAST_BONUS_CONTRACT);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_MIN_DURATION);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_MAX_DURATION);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_ENTITY);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_REPOSITIONING_FEE);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___JSONID);

		vesselEventEClass = createEClass(VESSEL_EVENT);
		createEAttribute(vesselEventEClass, VESSEL_EVENT__DURATION_IN_DAYS);
		createEReference(vesselEventEClass, VESSEL_EVENT__ALLOWED_VESSELS);
		createEReference(vesselEventEClass, VESSEL_EVENT__PORT);
		createEAttribute(vesselEventEClass, VESSEL_EVENT__START_AFTER);
		createEAttribute(vesselEventEClass, VESSEL_EVENT__START_BY);
		createEOperation(vesselEventEClass, VESSEL_EVENT___GET_START_BY_AS_DATE_TIME);
		createEOperation(vesselEventEClass, VESSEL_EVENT___GET_START_AFTER_AS_DATE_TIME);

		maintenanceEventEClass = createEClass(MAINTENANCE_EVENT);

		dryDockEventEClass = createEClass(DRY_DOCK_EVENT);

		charterOutEventEClass = createEClass(CHARTER_OUT_EVENT);
		createEAttribute(charterOutEventEClass, CHARTER_OUT_EVENT__OPTIONAL);
		createEReference(charterOutEventEClass, CHARTER_OUT_EVENT__RELOCATE_TO);
		createEAttribute(charterOutEventEClass, CHARTER_OUT_EVENT__HIRE_RATE);
		createEAttribute(charterOutEventEClass, CHARTER_OUT_EVENT__BALLAST_BONUS);
		createEAttribute(charterOutEventEClass, CHARTER_OUT_EVENT__REPOSITIONING_FEE);
		createEReference(charterOutEventEClass, CHARTER_OUT_EVENT__REQUIRED_HEEL);
		createEReference(charterOutEventEClass, CHARTER_OUT_EVENT__AVAILABLE_HEEL);
		createEOperation(charterOutEventEClass, CHARTER_OUT_EVENT___GET_END_PORT);

		assignableElementEClass = createEClass(ASSIGNABLE_ELEMENT);
		createEAttribute(assignableElementEClass, ASSIGNABLE_ELEMENT__SEQUENCE_HINT);
		createEReference(assignableElementEClass, ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
		createEAttribute(assignableElementEClass, ASSIGNABLE_ELEMENT__SPOT_INDEX);
		createEAttribute(assignableElementEClass, ASSIGNABLE_ELEMENT__LOCKED);

		vesselTypeGroupEClass = createEClass(VESSEL_TYPE_GROUP);
		createEAttribute(vesselTypeGroupEClass, VESSEL_TYPE_GROUP__VESSEL_TYPE);

		endHeelOptionsEClass = createEClass(END_HEEL_OPTIONS);
		createEAttribute(endHeelOptionsEClass, END_HEEL_OPTIONS__TANK_STATE);
		createEAttribute(endHeelOptionsEClass, END_HEEL_OPTIONS__MINIMUM_END_HEEL);
		createEAttribute(endHeelOptionsEClass, END_HEEL_OPTIONS__MAXIMUM_END_HEEL);
		createEAttribute(endHeelOptionsEClass, END_HEEL_OPTIONS__USE_LAST_HEEL_PRICE);
		createEAttribute(endHeelOptionsEClass, END_HEEL_OPTIONS__PRICE_EXPRESSION);

		startHeelOptionsEClass = createEClass(START_HEEL_OPTIONS);
		createEAttribute(startHeelOptionsEClass, START_HEEL_OPTIONS__CV_VALUE);
		createEAttribute(startHeelOptionsEClass, START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE);
		createEAttribute(startHeelOptionsEClass, START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE);
		createEAttribute(startHeelOptionsEClass, START_HEEL_OPTIONS__PRICE_EXPRESSION);

		inventoryEventRowEClass = createEClass(INVENTORY_EVENT_ROW);
		createEAttribute(inventoryEventRowEClass, INVENTORY_EVENT_ROW__START_DATE);
		createEAttribute(inventoryEventRowEClass, INVENTORY_EVENT_ROW__END_DATE);
		createEAttribute(inventoryEventRowEClass, INVENTORY_EVENT_ROW__PERIOD);
		createEAttribute(inventoryEventRowEClass, INVENTORY_EVENT_ROW__COUNTER_PARTY);
		createEAttribute(inventoryEventRowEClass, INVENTORY_EVENT_ROW__RELIABILITY);
		createEAttribute(inventoryEventRowEClass, INVENTORY_EVENT_ROW__VOLUME);
		createEAttribute(inventoryEventRowEClass, INVENTORY_EVENT_ROW__FORECAST_DATE);
		createEAttribute(inventoryEventRowEClass, INVENTORY_EVENT_ROW__VOLUME_LOW);
		createEAttribute(inventoryEventRowEClass, INVENTORY_EVENT_ROW__VOLUME_HIGH);
		createEOperation(inventoryEventRowEClass, INVENTORY_EVENT_ROW___GET_RELIABLE_VOLUME);

		inventoryCapacityRowEClass = createEClass(INVENTORY_CAPACITY_ROW);
		createEAttribute(inventoryCapacityRowEClass, INVENTORY_CAPACITY_ROW__DATE);
		createEAttribute(inventoryCapacityRowEClass, INVENTORY_CAPACITY_ROW__MIN_VOLUME);
		createEAttribute(inventoryCapacityRowEClass, INVENTORY_CAPACITY_ROW__MAX_VOLUME);

		inventoryEClass = createEClass(INVENTORY);
		createEReference(inventoryEClass, INVENTORY__PORT);
		createEReference(inventoryEClass, INVENTORY__FEEDS);
		createEReference(inventoryEClass, INVENTORY__OFFTAKES);
		createEReference(inventoryEClass, INVENTORY__CAPACITIES);
		createEAttribute(inventoryEClass, INVENTORY__FACILITY_TYPE);

		canalBookingSlotEClass = createEClass(CANAL_BOOKING_SLOT);
		createEAttribute(canalBookingSlotEClass, CANAL_BOOKING_SLOT__ROUTE_OPTION);
		createEAttribute(canalBookingSlotEClass, CANAL_BOOKING_SLOT__CANAL_ENTRANCE);
		createEAttribute(canalBookingSlotEClass, CANAL_BOOKING_SLOT__BOOKING_DATE);
		createEReference(canalBookingSlotEClass, CANAL_BOOKING_SLOT__SLOT);
		createEAttribute(canalBookingSlotEClass, CANAL_BOOKING_SLOT__NOTES);

		canalBookingsEClass = createEClass(CANAL_BOOKINGS);
		createEReference(canalBookingsEClass, CANAL_BOOKINGS__CANAL_BOOKING_SLOTS);
		createEAttribute(canalBookingsEClass, CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS);
		createEAttribute(canalBookingsEClass, CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS);
		createEAttribute(canalBookingsEClass, CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS);
		createEAttribute(canalBookingsEClass, CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND);
		createEAttribute(canalBookingsEClass, CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND);
		createEAttribute(canalBookingsEClass, CANAL_BOOKINGS__NORTHBOUND_MAX_IDLE_DAYS);
		createEAttribute(canalBookingsEClass, CANAL_BOOKINGS__SOUTHBOUND_MAX_IDLE_DAYS);

		scheduleSpecificationEClass = createEClass(SCHEDULE_SPECIFICATION);
		createEReference(scheduleSpecificationEClass, SCHEDULE_SPECIFICATION__VESSEL_SCHEDULE_SPECIFICATIONS);
		createEReference(scheduleSpecificationEClass, SCHEDULE_SPECIFICATION__NON_SHIPPED_CARGO_SPECIFICATIONS);
		createEReference(scheduleSpecificationEClass, SCHEDULE_SPECIFICATION__OPEN_EVENTS);

		nonShippedCargoSpecificationEClass = createEClass(NON_SHIPPED_CARGO_SPECIFICATION);
		createEReference(nonShippedCargoSpecificationEClass, NON_SHIPPED_CARGO_SPECIFICATION__SLOT_SPECIFICATIONS);

		vesselScheduleSpecificationEClass = createEClass(VESSEL_SCHEDULE_SPECIFICATION);
		createEReference(vesselScheduleSpecificationEClass, VESSEL_SCHEDULE_SPECIFICATION__VESSEL_ALLOCATION);
		createEAttribute(vesselScheduleSpecificationEClass, VESSEL_SCHEDULE_SPECIFICATION__SPOT_INDEX);
		createEReference(vesselScheduleSpecificationEClass, VESSEL_SCHEDULE_SPECIFICATION__EVENTS);

		scheduleSpecificationEventEClass = createEClass(SCHEDULE_SPECIFICATION_EVENT);

		vesselEventSpecificationEClass = createEClass(VESSEL_EVENT_SPECIFICATION);
		createEReference(vesselEventSpecificationEClass, VESSEL_EVENT_SPECIFICATION__VESSEL_EVENT);

		voyageSpecificationEClass = createEClass(VOYAGE_SPECIFICATION);

		slotSpecificationEClass = createEClass(SLOT_SPECIFICATION);
		createEReference(slotSpecificationEClass, SLOT_SPECIFICATION__SLOT);

		charterInMarketOverrideEClass = createEClass(CHARTER_IN_MARKET_OVERRIDE);
		createEReference(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE__CHARTER_IN_MARKET);
		createEAttribute(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE__SPOT_INDEX);
		createEReference(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE__START_HEEL);
		createEAttribute(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE__START_DATE);
		createEReference(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE__END_PORT);
		createEAttribute(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE__END_DATE);
		createEReference(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE__END_HEEL);
		createEAttribute(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE__INCLUDE_BALLAST_BONUS);
		createEAttribute(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION);
		createEAttribute(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION);
		createEOperation(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE___GET_START_DATE_AS_DATE_TIME);
		createEOperation(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE___GET_END_DATE_AS_DATE_TIME);
		createEOperation(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE___GET_LOCAL_OR_DELEGATE_MIN_DURATION);
		createEOperation(charterInMarketOverrideEClass, CHARTER_IN_MARKET_OVERRIDE___GET_LOCAL_OR_DELEGATE_MAX_DURATION);

		paperDealEClass = createEClass(PAPER_DEAL);
		createEAttribute(paperDealEClass, PAPER_DEAL__PRICE);
		createEAttribute(paperDealEClass, PAPER_DEAL__PRICING_TYPE);
		createEAttribute(paperDealEClass, PAPER_DEAL__INDEX);
		createEReference(paperDealEClass, PAPER_DEAL__INSTRUMENT);
		createEAttribute(paperDealEClass, PAPER_DEAL__QUANTITY);
		createEAttribute(paperDealEClass, PAPER_DEAL__PRICING_MONTH);
		createEAttribute(paperDealEClass, PAPER_DEAL__START_DATE);
		createEAttribute(paperDealEClass, PAPER_DEAL__END_DATE);
		createEReference(paperDealEClass, PAPER_DEAL__ENTITY);
		createEAttribute(paperDealEClass, PAPER_DEAL__YEAR);
		createEAttribute(paperDealEClass, PAPER_DEAL__COMMENT);

		buyPaperDealEClass = createEClass(BUY_PAPER_DEAL);

		sellPaperDealEClass = createEClass(SELL_PAPER_DEAL);

		dealSetEClass = createEClass(DEAL_SET);
		createEReference(dealSetEClass, DEAL_SET__SLOTS);
		createEReference(dealSetEClass, DEAL_SET__PAPER_DEALS);

		// Create enums
		cargoTypeEEnum = createEEnum(CARGO_TYPE);
		vesselTypeEEnum = createEEnum(VESSEL_TYPE);
		eVesselTankStateEEnum = createEEnum(EVESSEL_TANK_STATE);
		inventoryFacilityTypeEEnum = createEEnum(INVENTORY_FACILITY_TYPE);
		inventoryFrequencyEEnum = createEEnum(INVENTORY_FREQUENCY);
		paperPricingTypeEEnum = createEEnum(PAPER_PRICING_TYPE);

		// Create data types
		schedulingTimeWindowEDataType = createEDataType(SCHEDULING_TIME_WINDOW);
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
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);
		SpotMarketsPackage theSpotMarketsPackage = (SpotMarketsPackage)EPackage.Registry.INSTANCE.getEPackage(SpotMarketsPackage.eNS_URI);
		PricingPackage thePricingPackage = (PricingPackage)EPackage.Registry.INSTANCE.getEPackage(PricingPackage.eNS_URI);

		// Create type parameters
		ETypeParameter slotEClass_T = addETypeParameter(slotEClass, "T");

		// Set bounds for type parameters
		EGenericType g1 = createEGenericType(theCommercialPackage.getContract());
		slotEClass_T.getEBounds().add(g1);

		// Add supertypes to classes
		cargoModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		cargoEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		cargoEClass.getESuperTypes().add(this.getAssignableElement());
		slotEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		slotEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		slotEClass.getESuperTypes().add(theTypesPackage.getITimezoneProvider());
		g1 = createEGenericType(this.getSlot());
		EGenericType g2 = createEGenericType(theCommercialPackage.getPurchaseContract());
		g1.getETypeArguments().add(g2);
		loadSlotEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getSlot());
		g2 = createEGenericType(theCommercialPackage.getSalesContract());
		g1.getETypeArguments().add(g2);
		dischargeSlotEClass.getEGenericSuperTypes().add(g1);
		spotSlotEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		spotLoadSlotEClass.getESuperTypes().add(this.getLoadSlot());
		spotLoadSlotEClass.getESuperTypes().add(this.getSpotSlot());
		spotDischargeSlotEClass.getESuperTypes().add(this.getDischargeSlot());
		spotDischargeSlotEClass.getESuperTypes().add(this.getSpotSlot());
		cargoGroupEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		vesselAvailabilityEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		vesselAvailabilityEClass.getESuperTypes().add(theTypesPackage.getVesselAssignmentType());
		vesselEventEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		vesselEventEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		vesselEventEClass.getESuperTypes().add(theTypesPackage.getITimezoneProvider());
		vesselEventEClass.getESuperTypes().add(this.getAssignableElement());
		maintenanceEventEClass.getESuperTypes().add(this.getVesselEvent());
		dryDockEventEClass.getESuperTypes().add(this.getVesselEvent());
		charterOutEventEClass.getESuperTypes().add(this.getVesselEvent());
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(theFleetPackage.getVessel());
		g1.getETypeArguments().add(g2);
		vesselTypeGroupEClass.getEGenericSuperTypes().add(g1);
		startHeelOptionsEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		inventoryEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		canalBookingSlotEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		canalBookingsEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		vesselEventSpecificationEClass.getESuperTypes().add(this.getScheduleSpecificationEvent());
		voyageSpecificationEClass.getESuperTypes().add(this.getScheduleSpecificationEvent());
		slotSpecificationEClass.getESuperTypes().add(this.getScheduleSpecificationEvent());
		charterInMarketOverrideEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		charterInMarketOverrideEClass.getESuperTypes().add(theTypesPackage.getVesselAssignmentType());
		paperDealEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		buyPaperDealEClass.getESuperTypes().add(this.getPaperDeal());
		sellPaperDealEClass.getESuperTypes().add(this.getPaperDeal());
		dealSetEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());

		// Initialize classes, features, and operations; add parameters
		initEClass(cargoModelEClass, CargoModel.class, "CargoModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCargoModel_LoadSlots(), this.getLoadSlot(), null, "loadSlots", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_DischargeSlots(), this.getDischargeSlot(), null, "dischargeSlots", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_Cargoes(), this.getCargo(), null, "cargoes", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_CargoGroups(), this.getCargoGroup(), null, "cargoGroups", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_VesselAvailabilities(), this.getVesselAvailability(), null, "vesselAvailabilities", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_VesselEvents(), this.getVesselEvent(), null, "vesselEvents", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_VesselTypeGroups(), this.getVesselTypeGroup(), null, "vesselTypeGroups", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_InventoryModels(), this.getInventory(), null, "inventoryModels", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_CanalBookings(), this.getCanalBookings(), null, "canalBookings", null, 0, 1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_CharterInMarketOverrides(), this.getCharterInMarketOverride(), null, "charterInMarketOverrides", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_PaperDeals(), this.getPaperDeal(), null, "paperDeals", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_DealSets(), this.getDealSet(), null, "dealSets", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoEClass, Cargo.class, "Cargo", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCargo_AllowRewiring(), ecorePackage.getEBoolean(), "allowRewiring", "false", 1, 1, Cargo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getSlot());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getCargo_Slots(), g1, this.getSlot_Cargo(), "slots", null, 0, -1, Cargo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getCargo__GetCargoType(), this.getCargoType(), "getCargoType", 1, 1, IS_UNIQUE, IS_ORDERED);

		EOperation op = initEOperation(getCargo__GetSortedSlots(), null, "getSortedSlots", 0, -1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(this.getSlot());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEOperation(op, g1);

		initEOperation(getCargo__GetLoadName(), ecorePackage.getEString(), "getLoadName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(slotEClass, Slot.class, "Slot", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(slotEClass_T);
		initEReference(getSlot_Contract(), g1, null, "contract", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_Counterparty(), ecorePackage.getEString(), "counterparty", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_Cn(), ecorePackage.getEString(), "cn", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlot_Port(), thePortPackage.getPort(), null, "port", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_WindowStart(), theDateTimePackage.getLocalDate(), "windowStart", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_WindowStartTime(), ecorePackage.getEInt(), "windowStartTime", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_WindowSize(), ecorePackage.getEInt(), "windowSize", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_WindowSizeUnits(), theTypesPackage.getTimePeriod(), "windowSizeUnits", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_WindowFlex(), ecorePackage.getEInt(), "windowFlex", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_WindowFlexUnits(), theTypesPackage.getTimePeriod(), "windowFlexUnits", "HOURS", 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_Duration(), ecorePackage.getEInt(), "duration", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_VolumeLimitsUnit(), theTypesPackage.getVolumeUnits(), "volumeLimitsUnit", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_MinQuantity(), ecorePackage.getEInt(), "minQuantity", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_MaxQuantity(), ecorePackage.getEInt(), "maxQuantity", "140000", 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_OperationalTolerance(), ecorePackage.getEDouble(), "operationalTolerance", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_FullCargoLot(), ecorePackage.getEBoolean(), "fullCargoLot", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_Optional(), ecorePackage.getEBoolean(), "optional", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlot_Cargo(), this.getCargo(), this.getCargo_Slots(), "cargo", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_PricingEvent(), theCommercialPackage.getPricingEvent(), "pricingEvent", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_PricingDate(), theDateTimePackage.getLocalDate(), "pricingDate", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_Notes(), ecorePackage.getEString(), "notes", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_ShippingDaysRestriction(), ecorePackage.getEInt(), "shippingDaysRestriction", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlot_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlot_RestrictedContracts(), theCommercialPackage.getContract(), null, "restrictedContracts", null, 0, -1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_RestrictedContractsArePermissive(), ecorePackage.getEBoolean(), "restrictedContractsArePermissive", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_RestrictedContractsOverride(), ecorePackage.getEBoolean(), "restrictedContractsOverride", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getSlot_RestrictedPorts(), g1, null, "restrictedPorts", null, 0, -1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_RestrictedPortsArePermissive(), ecorePackage.getEBoolean(), "restrictedPortsArePermissive", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_RestrictedPortsOverride(), ecorePackage.getEBoolean(), "restrictedPortsOverride", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(theFleetPackage.getVessel());
		g1.getETypeArguments().add(g2);
		initEReference(getSlot_RestrictedVessels(), g1, null, "restrictedVessels", null, 0, -1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_RestrictedVesselsArePermissive(), ecorePackage.getEBoolean(), "restrictedVesselsArePermissive", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_RestrictedVesselsOverride(), ecorePackage.getEBoolean(), "restrictedVesselsOverride", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getSlot());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getSlot_RestrictedSlots(), g1, null, "restrictedSlots", null, 0, -1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_RestrictedSlotsArePermissive(), ecorePackage.getEBoolean(), "restrictedSlotsArePermissive", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_MiscCosts(), ecorePackage.getEInt(), "miscCosts", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_CancellationExpression(), ecorePackage.getEString(), "cancellationExpression", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlot_NominatedVessel(), theFleetPackage.getVessel(), null, "nominatedVessel", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_Locked(), ecorePackage.getEBoolean(), "locked", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_Cancelled(), ecorePackage.getEBoolean(), "cancelled", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_WindowCounterParty(), ecorePackage.getEBoolean(), "windowCounterParty", "false", 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateMinQuantity(), ecorePackage.getEInt(), "getSlotOrDelegateMinQuantity", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateMaxQuantity(), ecorePackage.getEInt(), "getSlotOrDelegateMaxQuantity", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateOperationalTolerance(), ecorePackage.getEDouble(), "getSlotOrDelegateOperationalTolerance", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateVolumeLimitsUnit(), theTypesPackage.getVolumeUnits(), "getSlotOrDelegateVolumeLimitsUnit", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateEntity(), theCommercialPackage.getBaseLegalEntity(), "getSlotOrDelegateEntity", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateCancellationExpression(), ecorePackage.getEString(), "getSlotOrDelegateCancellationExpression", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegatePricingEvent(), theCommercialPackage.getPricingEvent(), "getSlotOrDelegatePricingEvent", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetPricingDateAsDateTime(), theDateTimePackage.getDateTime(), "getPricingDateAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotContractParams(), theCommercialPackage.getSlotContractParams(), "getSlotContractParams", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateCounterparty(), ecorePackage.getEString(), "getSlotOrDelegateCounterparty", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateCN(), ecorePackage.getEString(), "getSlotOrDelegateCN", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateShippingDaysRestriction(), ecorePackage.getEInt(), "getSlotOrDelegateShippingDaysRestriction", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateFullCargoLot(), ecorePackage.getEBoolean(), "getSlotOrDelegateFullCargoLot", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateContractRestrictionsArePermissive(), ecorePackage.getEBoolean(), "getSlotOrDelegateContractRestrictionsArePermissive", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegatePortRestrictionsArePermissive(), ecorePackage.getEBoolean(), "getSlotOrDelegatePortRestrictionsArePermissive", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateVesselRestrictionsArePermissive(), ecorePackage.getEBoolean(), "getSlotOrDelegateVesselRestrictionsArePermissive", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateContractRestrictions(), theCommercialPackage.getContract(), "getSlotOrDelegateContractRestrictions", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getSlot__GetSlotOrDelegatePortRestrictions(), null, "getSlotOrDelegatePortRestrictions", 0, -1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEOperation(op, g1);

		op = initEOperation(getSlot__GetSlotOrDelegateVesselRestrictions(), null, "getSlotOrDelegateVesselRestrictions", 0, -1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(theFleetPackage.getVessel());
		g1.getETypeArguments().add(g2);
		initEOperation(op, g1);

		initEOperation(getSlot__GetSchedulingTimeWindow(), this.getSchedulingTimeWindow(), "getSchedulingTimeWindow", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegateDaysBuffer(), ecorePackage.getEInt(), "getSlotOrDelegateDaysBuffer", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(loadSlotEClass, LoadSlot.class, "LoadSlot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLoadSlot_CargoCV(), ecorePackage.getEDouble(), "cargoCV", null, 1, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLoadSlot_SchedulePurge(), ecorePackage.getEBoolean(), "schedulePurge", null, 1, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLoadSlot_ArriveCold(), ecorePackage.getEBoolean(), "arriveCold", null, 1, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLoadSlot_DESPurchase(), ecorePackage.getEBoolean(), "DESPurchase", null, 0, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLoadSlot_TransferFrom(), this.getDischargeSlot(), this.getDischargeSlot_TransferTo(), "transferFrom", null, 0, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLoadSlot_SalesDeliveryType(), theTypesPackage.getCargoDeliveryType(), "salesDeliveryType", "Any", 0, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLoadSlot_DesPurchaseDealType(), theTypesPackage.getDESPurchaseDealType(), "desPurchaseDealType", null, 0, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLoadSlot_VolumeCounterParty(), ecorePackage.getEBoolean(), "volumeCounterParty", null, 0, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getLoadSlot__GetSlotOrDelegateCV(), ecorePackage.getEDouble(), "getSlotOrDelegateCV", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getLoadSlot__GetSlotOrDelegateDeliveryType(), theTypesPackage.getCargoDeliveryType(), "getSlotOrDelegateDeliveryType", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getLoadSlot__GetSlotOrDelegateDESPurchaseDealType(), theTypesPackage.getDESPurchaseDealType(), "getSlotOrDelegateDESPurchaseDealType", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(dischargeSlotEClass, DischargeSlot.class, "DischargeSlot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDischargeSlot_FOBSale(), ecorePackage.getEBoolean(), "FOBSale", null, 0, 1, DischargeSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDischargeSlot_PurchaseDeliveryType(), theTypesPackage.getCargoDeliveryType(), "PurchaseDeliveryType", "Any", 0, 1, DischargeSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDischargeSlot_TransferTo(), this.getLoadSlot(), this.getLoadSlot_TransferFrom(), "transferTo", null, 0, 1, DischargeSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDischargeSlot_MinCvValue(), ecorePackage.getEDouble(), "minCvValue", null, 0, 1, DischargeSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDischargeSlot_MaxCvValue(), ecorePackage.getEDouble(), "maxCvValue", null, 0, 1, DischargeSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDischargeSlot_FobSaleDealType(), theTypesPackage.getFOBSaleDealType(), "fobSaleDealType", null, 0, 1, DischargeSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getDischargeSlot__GetSlotOrDelegateMinCv(), ecorePackage.getEDouble(), "getSlotOrDelegateMinCv", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getDischargeSlot__GetSlotOrDelegateMaxCv(), ecorePackage.getEDouble(), "getSlotOrDelegateMaxCv", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getDischargeSlot__GetSlotOrDelegateDeliveryType(), theTypesPackage.getCargoDeliveryType(), "getSlotOrDelegateDeliveryType", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getDischargeSlot__GetSlotOrDelegateFOBSaleDealType(), theTypesPackage.getFOBSaleDealType(), "getSlotOrDelegateFOBSaleDealType", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(spotSlotEClass, SpotSlot.class, "SpotSlot", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSpotSlot_Market(), theSpotMarketsPackage.getSpotMarket(), null, "market", null, 1, 1, SpotSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spotLoadSlotEClass, SpotLoadSlot.class, "SpotLoadSlot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(spotDischargeSlotEClass, SpotDischargeSlot.class, "SpotDischargeSlot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(cargoGroupEClass, CargoGroup.class, "CargoGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCargoGroup_Cargoes(), this.getCargo(), null, "cargoes", null, 0, -1, CargoGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselAvailabilityEClass, VesselAvailability.class, "VesselAvailability", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselAvailability_Fleet(), ecorePackage.getEBoolean(), "fleet", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_Optional(), ecorePackage.getEBoolean(), "optional", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailability_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_CharterNumber(), ecorePackage.getEInt(), "charterNumber", "1", 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailability_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_TimeCharterRate(), ecorePackage.getEString(), "timeCharterRate", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailability_StartAt(), thePortPackage.getPort(), null, "startAt", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_StartAfter(), theDateTimePackage.getLocalDateTime(), "startAfter", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_StartBy(), theDateTimePackage.getLocalDateTime(), "startBy", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getVesselAvailability_EndAt(), g1, null, "endAt", null, 0, -1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_EndAfter(), theDateTimePackage.getLocalDateTime(), "endAfter", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_EndBy(), theDateTimePackage.getLocalDateTime(), "endBy", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailability_StartHeel(), this.getStartHeelOptions(), null, "startHeel", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailability_EndHeel(), this.getEndHeelOptions(), null, "endHeel", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_ForceHireCostOnlyEndRule(), ecorePackage.getEBoolean(), "forceHireCostOnlyEndRule", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_RepositioningFee(), ecorePackage.getEString(), "repositioningFee", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailability_BallastBonusContract(), theCommercialPackage.getBallastBonusContract(), null, "ballastBonusContract", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailability_CharterContract(), theCommercialPackage.getCharterContract(), null, "charterContract", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_MinDuration(), ecorePackage.getEInt(), "minDuration", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_MaxDuration(), ecorePackage.getEInt(), "maxDuration", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getVesselAvailability__GetStartByAsDateTime(), theDateTimePackage.getDateTime(), "getStartByAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__GetStartAfterAsDateTime(), theDateTimePackage.getDateTime(), "getStartAfterAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__GetEndByAsDateTime(), theDateTimePackage.getDateTime(), "getEndByAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__GetEndAfterAsDateTime(), theDateTimePackage.getDateTime(), "getEndAfterAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__GetCharterOrDelegateBallastBonusContract(), theCommercialPackage.getBallastBonusContract(), "getCharterOrDelegateBallastBonusContract", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__GetCharterOrDelegateMinDuration(), ecorePackage.getEInt(), "getCharterOrDelegateMinDuration", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__GetCharterOrDelegateMaxDuration(), ecorePackage.getEInt(), "getCharterOrDelegateMaxDuration", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__GetCharterOrDelegateEntity(), theCommercialPackage.getBaseLegalEntity(), "getCharterOrDelegateEntity", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__GetCharterOrDelegateRepositioningFee(), ecorePackage.getEString(), "getCharterOrDelegateRepositioningFee", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__Jsonid(), ecorePackage.getEString(), "jsonid", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(vesselEventEClass, VesselEvent.class, "VesselEvent", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselEvent_DurationInDays(), ecorePackage.getEInt(), "durationInDays", null, 1, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(theFleetPackage.getVessel());
		g1.getETypeArguments().add(g2);
		initEReference(getVesselEvent_AllowedVessels(), g1, null, "allowedVessels", null, 0, -1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselEvent_Port(), thePortPackage.getPort(), null, "port", null, 1, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselEvent_StartAfter(), theDateTimePackage.getLocalDateTime(), "startAfter", null, 0, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselEvent_StartBy(), theDateTimePackage.getLocalDateTime(), "startBy", null, 0, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getVesselEvent__GetStartByAsDateTime(), theDateTimePackage.getDateTime(), "getStartByAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselEvent__GetStartAfterAsDateTime(), theDateTimePackage.getDateTime(), "getStartAfterAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(maintenanceEventEClass, MaintenanceEvent.class, "MaintenanceEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dryDockEventEClass, DryDockEvent.class, "DryDockEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(charterOutEventEClass, CharterOutEvent.class, "CharterOutEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCharterOutEvent_Optional(), ecorePackage.getEBoolean(), "optional", null, 0, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterOutEvent_RelocateTo(), thePortPackage.getPort(), null, "relocateTo", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutEvent_HireRate(), ecorePackage.getEInt(), "hireRate", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutEvent_BallastBonus(), ecorePackage.getEInt(), "ballastBonus", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutEvent_RepositioningFee(), ecorePackage.getEInt(), "repositioningFee", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterOutEvent_RequiredHeel(), this.getEndHeelOptions(), null, "requiredHeel", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterOutEvent_AvailableHeel(), this.getStartHeelOptions(), null, "availableHeel", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getCharterOutEvent__GetEndPort(), thePortPackage.getPort(), "getEndPort", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(assignableElementEClass, AssignableElement.class, "AssignableElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAssignableElement_SequenceHint(), ecorePackage.getEInt(), "sequenceHint", null, 0, 1, AssignableElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssignableElement_VesselAssignmentType(), theTypesPackage.getVesselAssignmentType(), null, "vesselAssignmentType", null, 0, 1, AssignableElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssignableElement_SpotIndex(), ecorePackage.getEInt(), "spotIndex", null, 0, 1, AssignableElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssignableElement_Locked(), ecorePackage.getEBoolean(), "locked", null, 0, 1, AssignableElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselTypeGroupEClass, VesselTypeGroup.class, "VesselTypeGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselTypeGroup_VesselType(), this.getVesselType(), "vesselType", null, 1, 1, VesselTypeGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(endHeelOptionsEClass, EndHeelOptions.class, "EndHeelOptions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEndHeelOptions_TankState(), this.getEVesselTankState(), "tankState", null, 0, 1, EndHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEndHeelOptions_MinimumEndHeel(), ecorePackage.getEInt(), "minimumEndHeel", null, 0, 1, EndHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEndHeelOptions_MaximumEndHeel(), ecorePackage.getEInt(), "maximumEndHeel", null, 0, 1, EndHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEndHeelOptions_UseLastHeelPrice(), ecorePackage.getEBoolean(), "useLastHeelPrice", null, 0, 1, EndHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEndHeelOptions_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, EndHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(startHeelOptionsEClass, StartHeelOptions.class, "StartHeelOptions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStartHeelOptions_CvValue(), ecorePackage.getEDouble(), "cvValue", null, 1, 1, StartHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStartHeelOptions_MinVolumeAvailable(), ecorePackage.getEDouble(), "minVolumeAvailable", null, 1, 1, StartHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStartHeelOptions_MaxVolumeAvailable(), ecorePackage.getEDouble(), "maxVolumeAvailable", null, 1, 1, StartHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStartHeelOptions_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, StartHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(inventoryEventRowEClass, InventoryEventRow.class, "InventoryEventRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInventoryEventRow_StartDate(), theDateTimePackage.getLocalDate(), "startDate", null, 0, 1, InventoryEventRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInventoryEventRow_EndDate(), theDateTimePackage.getLocalDate(), "endDate", null, 0, 1, InventoryEventRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInventoryEventRow_Period(), this.getInventoryFrequency(), "period", null, 0, 1, InventoryEventRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInventoryEventRow_CounterParty(), ecorePackage.getEString(), "counterParty", null, 0, 1, InventoryEventRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInventoryEventRow_Reliability(), ecorePackage.getEDouble(), "reliability", "100.0", 0, 1, InventoryEventRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInventoryEventRow_Volume(), ecorePackage.getEInt(), "volume", null, 0, 1, InventoryEventRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInventoryEventRow_ForecastDate(), theDateTimePackage.getLocalDate(), "forecastDate", null, 0, 1, InventoryEventRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInventoryEventRow_VolumeLow(), ecorePackage.getEInt(), "volumeLow", "0", 0, 1, InventoryEventRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInventoryEventRow_VolumeHigh(), ecorePackage.getEInt(), "volumeHigh", "0", 0, 1, InventoryEventRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getInventoryEventRow__GetReliableVolume(), ecorePackage.getEInt(), "getReliableVolume", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(inventoryCapacityRowEClass, InventoryCapacityRow.class, "InventoryCapacityRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInventoryCapacityRow_Date(), theDateTimePackage.getLocalDate(), "date", null, 0, 1, InventoryCapacityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInventoryCapacityRow_MinVolume(), ecorePackage.getEInt(), "minVolume", null, 0, 1, InventoryCapacityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInventoryCapacityRow_MaxVolume(), ecorePackage.getEInt(), "maxVolume", null, 0, 1, InventoryCapacityRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(inventoryEClass, Inventory.class, "Inventory", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInventory_Port(), thePortPackage.getPort(), null, "port", null, 0, 1, Inventory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInventory_Feeds(), this.getInventoryEventRow(), null, "feeds", null, 0, -1, Inventory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInventory_Offtakes(), this.getInventoryEventRow(), null, "offtakes", null, 0, -1, Inventory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInventory_Capacities(), this.getInventoryCapacityRow(), null, "capacities", null, 0, -1, Inventory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInventory_FacilityType(), this.getInventoryFacilityType(), "facilityType", null, 0, 1, Inventory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(canalBookingSlotEClass, CanalBookingSlot.class, "CanalBookingSlot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCanalBookingSlot_RouteOption(), thePortPackage.getRouteOption(), "routeOption", null, 1, 1, CanalBookingSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCanalBookingSlot_CanalEntrance(), thePortPackage.getCanalEntry(), "canalEntrance", null, 0, 1, CanalBookingSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCanalBookingSlot_BookingDate(), theDateTimePackage.getLocalDate(), "bookingDate", null, 1, 1, CanalBookingSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCanalBookingSlot_Slot(), this.getSlot(), null, "slot", null, 0, 1, CanalBookingSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCanalBookingSlot_Notes(), ecorePackage.getEString(), "notes", null, 0, 1, CanalBookingSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(canalBookingsEClass, CanalBookings.class, "CanalBookings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCanalBookings_CanalBookingSlots(), this.getCanalBookingSlot(), null, "canalBookingSlots", null, 0, -1, CanalBookings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCanalBookings_StrictBoundaryOffsetDays(), ecorePackage.getEInt(), "strictBoundaryOffsetDays", null, 0, 1, CanalBookings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCanalBookings_RelaxedBoundaryOffsetDays(), ecorePackage.getEInt(), "relaxedBoundaryOffsetDays", null, 0, 1, CanalBookings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCanalBookings_ArrivalMarginHours(), ecorePackage.getEInt(), "arrivalMarginHours", null, 0, 1, CanalBookings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCanalBookings_FlexibleBookingAmountNorthbound(), ecorePackage.getEInt(), "flexibleBookingAmountNorthbound", null, 0, 1, CanalBookings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCanalBookings_FlexibleBookingAmountSouthbound(), ecorePackage.getEInt(), "flexibleBookingAmountSouthbound", null, 0, 1, CanalBookings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCanalBookings_NorthboundMaxIdleDays(), ecorePackage.getEInt(), "northboundMaxIdleDays", null, 0, 1, CanalBookings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCanalBookings_SouthboundMaxIdleDays(), ecorePackage.getEInt(), "southboundMaxIdleDays", null, 0, 1, CanalBookings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scheduleSpecificationEClass, ScheduleSpecification.class, "ScheduleSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getScheduleSpecification_VesselScheduleSpecifications(), this.getVesselScheduleSpecification(), null, "vesselScheduleSpecifications", null, 0, -1, ScheduleSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScheduleSpecification_NonShippedCargoSpecifications(), this.getNonShippedCargoSpecification(), null, "nonShippedCargoSpecifications", null, 0, -1, ScheduleSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScheduleSpecification_OpenEvents(), this.getScheduleSpecificationEvent(), null, "openEvents", null, 0, -1, ScheduleSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(nonShippedCargoSpecificationEClass, NonShippedCargoSpecification.class, "NonShippedCargoSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNonShippedCargoSpecification_SlotSpecifications(), this.getSlotSpecification(), null, "slotSpecifications", null, 0, -1, NonShippedCargoSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselScheduleSpecificationEClass, VesselScheduleSpecification.class, "VesselScheduleSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselScheduleSpecification_VesselAllocation(), theTypesPackage.getVesselAssignmentType(), null, "vesselAllocation", null, 0, 1, VesselScheduleSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselScheduleSpecification_SpotIndex(), ecorePackage.getEInt(), "spotIndex", null, 0, 1, VesselScheduleSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselScheduleSpecification_Events(), this.getScheduleSpecificationEvent(), null, "events", null, 0, -1, VesselScheduleSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scheduleSpecificationEventEClass, ScheduleSpecificationEvent.class, "ScheduleSpecificationEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(vesselEventSpecificationEClass, VesselEventSpecification.class, "VesselEventSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselEventSpecification_VesselEvent(), this.getVesselEvent(), null, "vesselEvent", null, 0, 1, VesselEventSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(voyageSpecificationEClass, VoyageSpecification.class, "VoyageSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(slotSpecificationEClass, SlotSpecification.class, "SlotSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlotSpecification_Slot(), this.getSlot(), null, "slot", null, 0, 1, SlotSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(charterInMarketOverrideEClass, CharterInMarketOverride.class, "CharterInMarketOverride", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCharterInMarketOverride_CharterInMarket(), theSpotMarketsPackage.getCharterInMarket(), null, "charterInMarket", null, 0, 1, CharterInMarketOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarketOverride_SpotIndex(), ecorePackage.getEInt(), "spotIndex", null, 0, 1, CharterInMarketOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterInMarketOverride_StartHeel(), this.getStartHeelOptions(), null, "startHeel", null, 1, 1, CharterInMarketOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarketOverride_StartDate(), theDateTimePackage.getLocalDateTime(), "startDate", null, 0, 1, CharterInMarketOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterInMarketOverride_EndPort(), thePortPackage.getPort(), null, "endPort", null, 0, 1, CharterInMarketOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarketOverride_EndDate(), theDateTimePackage.getLocalDateTime(), "endDate", null, 0, 1, CharterInMarketOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterInMarketOverride_EndHeel(), this.getEndHeelOptions(), null, "endHeel", null, 0, 1, CharterInMarketOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarketOverride_IncludeBallastBonus(), ecorePackage.getEBoolean(), "includeBallastBonus", null, 0, 1, CharterInMarketOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarketOverride_MinDuration(), ecorePackage.getEInt(), "minDuration", null, 1, 1, CharterInMarketOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarketOverride_MaxDuration(), ecorePackage.getEInt(), "maxDuration", null, 1, 1, CharterInMarketOverride.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getCharterInMarketOverride__GetStartDateAsDateTime(), theDateTimePackage.getDateTime(), "getStartDateAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getCharterInMarketOverride__GetEndDateAsDateTime(), theDateTimePackage.getDateTime(), "getEndDateAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getCharterInMarketOverride__GetLocalOrDelegateMinDuration(), ecorePackage.getEInt(), "getLocalOrDelegateMinDuration", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getCharterInMarketOverride__GetLocalOrDelegateMaxDuration(), ecorePackage.getEInt(), "getLocalOrDelegateMaxDuration", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(paperDealEClass, PaperDeal.class, "PaperDeal", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPaperDeal_Price(), ecorePackage.getEDouble(), "price", null, 0, 1, PaperDeal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPaperDeal_PricingType(), this.getPaperPricingType(), "pricingType", null, 0, 1, PaperDeal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPaperDeal_Index(), ecorePackage.getEString(), "index", null, 0, 1, PaperDeal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPaperDeal_Instrument(), thePricingPackage.getSettleStrategy(), null, "instrument", null, 0, 1, PaperDeal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPaperDeal_Quantity(), ecorePackage.getEDouble(), "quantity", null, 0, 1, PaperDeal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPaperDeal_PricingMonth(), theDateTimePackage.getYearMonth(), "pricingMonth", null, 0, 1, PaperDeal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPaperDeal_StartDate(), theDateTimePackage.getLocalDate(), "startDate", null, 0, 1, PaperDeal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPaperDeal_EndDate(), theDateTimePackage.getLocalDate(), "endDate", null, 0, 1, PaperDeal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPaperDeal_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, PaperDeal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPaperDeal_Year(), ecorePackage.getEInt(), "year", null, 0, 1, PaperDeal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPaperDeal_Comment(), ecorePackage.getEString(), "comment", null, 0, 1, PaperDeal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(buyPaperDealEClass, BuyPaperDeal.class, "BuyPaperDeal", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(sellPaperDealEClass, SellPaperDeal.class, "SellPaperDeal", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dealSetEClass, DealSet.class, "DealSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getSlot());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getDealSet_Slots(), g1, null, "slots", null, 0, -1, DealSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDealSet_PaperDeals(), this.getPaperDeal(), null, "paperDeals", null, 0, -1, DealSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(cargoTypeEEnum, CargoType.class, "CargoType");
		addEEnumLiteral(cargoTypeEEnum, CargoType.FLEET);
		addEEnumLiteral(cargoTypeEEnum, CargoType.FOB);
		addEEnumLiteral(cargoTypeEEnum, CargoType.DES);

		initEEnum(vesselTypeEEnum, VesselType.class, "VesselType");
		addEEnumLiteral(vesselTypeEEnum, VesselType.OWNED);
		addEEnumLiteral(vesselTypeEEnum, VesselType.TIME_CHARTERED);

		initEEnum(eVesselTankStateEEnum, EVesselTankState.class, "EVesselTankState");
		addEEnumLiteral(eVesselTankStateEEnum, EVesselTankState.EITHER);
		addEEnumLiteral(eVesselTankStateEEnum, EVesselTankState.MUST_BE_COLD);
		addEEnumLiteral(eVesselTankStateEEnum, EVesselTankState.MUST_BE_WARM);

		initEEnum(inventoryFacilityTypeEEnum, InventoryFacilityType.class, "InventoryFacilityType");
		addEEnumLiteral(inventoryFacilityTypeEEnum, InventoryFacilityType.UPSTREAM);
		addEEnumLiteral(inventoryFacilityTypeEEnum, InventoryFacilityType.HUB);
		addEEnumLiteral(inventoryFacilityTypeEEnum, InventoryFacilityType.DOWNSTREAM);

		initEEnum(inventoryFrequencyEEnum, InventoryFrequency.class, "InventoryFrequency");
		addEEnumLiteral(inventoryFrequencyEEnum, InventoryFrequency.CARGO);
		addEEnumLiteral(inventoryFrequencyEEnum, InventoryFrequency.HOURLY);
		addEEnumLiteral(inventoryFrequencyEEnum, InventoryFrequency.DAILY);
		addEEnumLiteral(inventoryFrequencyEEnum, InventoryFrequency.MONTHLY);
		addEEnumLiteral(inventoryFrequencyEEnum, InventoryFrequency.LEVEL);

		initEEnum(paperPricingTypeEEnum, PaperPricingType.class, "PaperPricingType");
		addEEnumLiteral(paperPricingTypeEEnum, PaperPricingType.PERIOD_AVG);
		addEEnumLiteral(paperPricingTypeEEnum, PaperPricingType.CALENDAR);
		addEEnumLiteral(paperPricingTypeEEnum, PaperPricingType.INSTRUMENT);

		// Initialize data types
		initEDataType(schedulingTimeWindowEDataType, SchedulingTimeWindow.class, "SchedulingTimeWindow", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/mmxcore/validation/NamedObject
		createNamedObjectAnnotations();
		// http://www.mmxlabs.com/models/featureOverride
		createFeatureOverrideAnnotations();
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
		// http://www.mmxlabs.com/models/pricing/expressionType
		createExpressionTypeAnnotations();
		// http://www.mmxlabs.com/models/ui/featureEnablement
		createFeatureEnablementAnnotations();
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
		  (getCargoModel_LoadSlots(),
		   source,
		   new String[] {
			   "nonUniqueChildren", "true"
		   });
		addAnnotation
		  (getCargoModel_DischargeSlots(),
		   source,
		   new String[] {
			   "nonUniqueChildren", "true"
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
		  (slotEClass,
		   source,
		   new String[] {
		   });
		addAnnotation
		  (loadSlotEClass,
		   source,
		   new String[] {
		   });
		addAnnotation
		  (dischargeSlotEClass,
		   source,
		   new String[] {
		   });
		addAnnotation
		  (spotLoadSlotEClass,
		   source,
		   new String[] {
		   });
		addAnnotation
		  (spotDischargeSlotEClass,
		   source,
		   new String[] {
		   });
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
		  (getSlot_WindowSize(),
		   source,
		   new String[] {
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getSlot_WindowSizeUnits(),
		   source,
		   new String[] {
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getSlot_WindowFlex(),
		   source,
		   new String[] {
			   "formatString", "##0"
		   });
		addAnnotation
		  (getSlot_WindowFlexUnits(),
		   source,
		   new String[] {
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getSlot_Duration(),
		   source,
		   new String[] {
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getSlot_MinQuantity(),
		   source,
		   new String[] {
			   "formatString", "#,###,##0"
		   });
		addAnnotation
		  (getSlot_MaxQuantity(),
		   source,
		   new String[] {
			   "formatString", "#,###,##0"
		   });
		addAnnotation
		  (getSlot_OperationalTolerance(),
		   source,
		   new String[] {
			   "scale", "100",
			   "formatString", "##0.#",
			   "unit", "%",
			   "exportFormatString", "#.###",
			   "unitPrefix", "\u00b1"
		   });
		addAnnotation
		  (getSlot_ShippingDaysRestriction(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "###"
		   });
		addAnnotation
		  (getSlot_MiscCosts(),
		   source,
		   new String[] {
			   "unitPrefix", "$",
			   "formatString", "-###,###,##0"
		   });
		addAnnotation
		  (getLoadSlot_CargoCV(),
		   source,
		   new String[] {
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getDischargeSlot_MinCvValue(),
		   source,
		   new String[] {
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getDischargeSlot_MaxCvValue(),
		   source,
		   new String[] {
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getVesselAvailability_CharterNumber(),
		   source,
		   new String[] {
			   "formatString", "#0"
		   });
		addAnnotation
		  (getVesselAvailability_TimeCharterRate(),
		   source,
		   new String[] {
			   "unit", "$/day"
		   });
		addAnnotation
		  (getVesselAvailability_RepositioningFee(),
		   source,
		   new String[] {
			   "unit", "$/day"
		   });
		addAnnotation
		  (getVesselAvailability_MinDuration(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getVesselAvailability_MaxDuration(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getVesselEvent_DurationInDays(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getCharterOutEvent_HireRate(),
		   source,
		   new String[] {
			   "unit", "$/day",
			   "formatString", "###,##0"
		   });
		addAnnotation
		  (getCharterOutEvent_BallastBonus(),
		   source,
		   new String[] {
			   "unit", "$",
			   "formatString", "##,###,##0"
		   });
		addAnnotation
		  (getCharterOutEvent_RepositioningFee(),
		   source,
		   new String[] {
			   "unit", "$",
			   "formatString", "##,###,##0"
		   });
		addAnnotation
		  (getEndHeelOptions_MinimumEndHeel(),
		   source,
		   new String[] {
			   "unit", "m\u00b3",
			   "formatString", "###,##0.###"
		   });
		addAnnotation
		  (getEndHeelOptions_MaximumEndHeel(),
		   source,
		   new String[] {
			   "unit", "m\u00b3",
			   "formatString", "###,##0.###"
		   });
		addAnnotation
		  (getStartHeelOptions_CvValue(),
		   source,
		   new String[] {
			   "unit", "mmBtu/m\u00b3",
			   "formatString", "#0.######"
		   });
		addAnnotation
		  (getStartHeelOptions_MinVolumeAvailable(),
		   source,
		   new String[] {
			   "unit", "m\u00b3",
			   "formatString", "###,##0.###"
		   });
		addAnnotation
		  (getStartHeelOptions_MaxVolumeAvailable(),
		   source,
		   new String[] {
			   "unit", "m\u00b3",
			   "formatString", "###,##0.###"
		   });
		addAnnotation
		  (getCharterInMarketOverride_MinDuration(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getCharterInMarketOverride_MaxDuration(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getPaperDeal_Year(),
		   source,
		   new String[] {
			   "formatString", "####"
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
		  (getSlot_PriceExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getSlot_CancellationExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getVesselAvailability_TimeCharterRate(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
		addAnnotation
		  (getVesselAvailability_RepositioningFee(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
		addAnnotation
		  (getCharterOutEvent_HireRate(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
		addAnnotation
		  (getEndHeelOptions_PriceExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getStartHeelOptions_PriceExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getPaperDeal_Price(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getPaperDeal_Index(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/ui/featureEnablement</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createFeatureEnablementAnnotations() {
		String source = "http://www.mmxlabs.com/models/ui/featureEnablement";
		addAnnotation
		  (getLoadSlot_SchedulePurge(),
		   source,
		   new String[] {
			   "feature", "purge"
		   });
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getVesselAvailability__GetCharterOrDelegateEntity() {
		return vesselAvailabilityEClass.getEOperations().get(7);
	}
} //CargoPackageImpl
