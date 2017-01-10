/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselType;
import com.mmxlabs.models.lng.cargo.VesselTypeGroup;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
	private EEnum cargoTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum vesselTypeEEnum = null;

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
		CargoPackageImpl theCargoPackage = (CargoPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof CargoPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new CargoPackageImpl());

		isInited = true;

		// Initialize simple dependencies
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
	public EClass getCargo() {
		return cargoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCargo_AllowRewiring() {
		return (EAttribute)cargoEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargo_Slots() {
		return (EReference)cargoEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getCargo__GetCargoType() {
		return cargoEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getCargo__GetSortedSlots() {
		return cargoEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getCargo__GetLoadName() {
		return cargoEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSlot() {
		return slotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_WindowStart() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_WindowStartTime() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_WindowSize() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_WindowSizeUnits() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_WindowFlex() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_WindowFlexUnits() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlot_Port() {
		return (EReference)slotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlot_Contract() {
		return (EReference)slotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_Duration() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_VolumeLimitsUnit() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_MinQuantity() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_MaxQuantity() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_Optional() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_PriceExpression() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlot_Cargo() {
		return (EReference)slotEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_PricingEvent() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_PricingDate() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_Notes() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_Divertible() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_ShippingDaysRestriction() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlot_Entity() {
		return (EReference)slotEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlot_RestrictedContracts() {
		return (EReference)slotEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlot_RestrictedPorts() {
		return (EReference)slotEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_RestrictedListsArePermissive() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_Hedges() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(24);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_MiscCosts() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(25);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_CancellationExpression() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(27);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_OverrideRestrictions() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(28);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlot_NominatedVessel() {
		return (EReference)slotEClass.getEStructuralFeatures().get(29);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlot_Locked() {
		return (EAttribute)slotEClass.getEStructuralFeatures().get(30);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlot_AllowedVessels() {
		return (EReference)slotEClass.getEStructuralFeatures().get(26);
	}	
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrPortDuration() {
		return slotEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrContractMinQuantity() {
		return slotEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrContractMaxQuantity() {
		return slotEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrContractVolumeLimitsUnit() {
		return slotEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetWindowEndWithSlotOrPortTime() {
		return slotEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetWindowStartWithSlotOrPortTime() {
		return slotEClass.getEOperations().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetWindowEndWithSlotOrPortTimeWithFlex() {
		return slotEClass.getEOperations().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetWindowStartWithSlotOrPortTimeWithFlex() {
		return slotEClass.getEOperations().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrPortWindowSize() {
		return slotEClass.getEOperations().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrPortWindowSizeUnits() {
		return slotEClass.getEOperations().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetWindowSizeInHours() {
		return slotEClass.getEOperations().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrDelegatedEntity() {
		return slotEClass.getEOperations().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrContractRestrictedContracts() {
		return slotEClass.getEOperations().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrContractRestrictedPorts() {
		return slotEClass.getEOperations().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrContractRestrictedListsArePermissive() {
		return slotEClass.getEOperations().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrContractCancellationExpression() {
		return slotEClass.getEOperations().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotOrDelegatedPricingEvent() {
		return slotEClass.getEOperations().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetPricingDateAsDateTime() {
		return slotEClass.getEOperations().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlot__GetSlotContractParams() {
		return slotEClass.getEOperations().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLoadSlot() {
		return loadSlotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLoadSlot_CargoCV() {
		return (EAttribute)loadSlotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLoadSlot_ArriveCold() {
		return (EAttribute)loadSlotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLoadSlot_DESPurchase() {
		return (EAttribute)loadSlotEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLoadSlot_TransferFrom() {
		return (EReference)loadSlotEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLoadSlot_SalesDeliveryType() {
		return (EAttribute)loadSlotEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getLoadSlot__GetSlotOrDelegatedCV() {
		return loadSlotEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getLoadSlot__GetSlotOrContractDeliveryType() {
		return loadSlotEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDischargeSlot() {
		return dischargeSlotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDischargeSlot_FOBSale() {
		return (EAttribute)dischargeSlotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDischargeSlot_PurchaseDeliveryType() {
		return (EAttribute)dischargeSlotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDischargeSlot_TransferTo() {
		return (EReference)dischargeSlotEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDischargeSlot_MinCvValue() {
		return (EAttribute)dischargeSlotEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDischargeSlot_MaxCvValue() {
		return (EAttribute)dischargeSlotEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDischargeSlot__GetSlotOrContractMinCv() {
		return dischargeSlotEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDischargeSlot__GetSlotOrContractMaxCv() {
		return dischargeSlotEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDischargeSlot__GetSlotOrContractDeliveryType() {
		return dischargeSlotEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCargoModel() {
		return cargoModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoModel_LoadSlots() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoModel_DischargeSlots() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoModel_Cargoes() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoModel_CargoGroups() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoModel_VesselAvailabilities() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoModel_VesselEvents() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoModel_VesselTypeGroups() {
		return (EReference)cargoModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpotSlot() {
		return spotSlotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotSlot_Market() {
		return (EReference)spotSlotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpotLoadSlot() {
		return spotLoadSlotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpotDischargeSlot() {
		return spotDischargeSlotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCargoGroup() {
		return cargoGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoGroup_Cargoes() {
		return (EReference)cargoGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselAvailability() {
		return vesselAvailabilityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_Fleet() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailability_Vessel() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_TimeCharterRate() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailability_StartAt() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_StartAfter() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_StartBy() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailability_EndAt() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_EndAfter() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_EndBy() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailability_StartHeel() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailability_EndHeel() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_ForceHireCostOnlyEndRule() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_Optional() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_RepositioningFee() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_BallastBonus() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getVesselAvailability__GetStartByAsDateTime() {
		return vesselAvailabilityEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getVesselAvailability__GetStartAfterAsDateTime() {
		return vesselAvailabilityEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getVesselAvailability__GetEndByAsDateTime() {
		return vesselAvailabilityEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getVesselAvailability__GetEndAfterAsDateTime() {
		return vesselAvailabilityEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailability_Entity() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselEvent() {
		return vesselEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselEvent_DurationInDays() {
		return (EAttribute)vesselEventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselEvent_AllowedVessels() {
		return (EReference)vesselEventEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselEvent_Port() {
		return (EReference)vesselEventEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselEvent_StartAfter() {
		return (EAttribute)vesselEventEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselEvent_StartBy() {
		return (EAttribute)vesselEventEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getVesselEvent__GetStartByAsDateTime() {
		return vesselEventEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getVesselEvent__GetStartAfterAsDateTime() {
		return vesselEventEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMaintenanceEvent() {
		return maintenanceEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDryDockEvent() {
		return dryDockEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCharterOutEvent() {
		return charterOutEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCharterOutEvent_RelocateTo() {
		return (EReference)charterOutEventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCharterOutEvent_HeelOptions() {
		return (EReference)charterOutEventEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCharterOutEvent_RepositioningFee() {
		return (EAttribute)charterOutEventEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCharterOutEvent_HireRate() {
		return (EAttribute)charterOutEventEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCharterOutEvent_BallastBonus() {
		return (EAttribute)charterOutEventEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getCharterOutEvent__GetEndPort() {
		return charterOutEventEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAssignableElement() {
		return assignableElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssignableElement_SpotIndex() {
		return (EAttribute)assignableElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssignableElement_SequenceHint() {
		return (EAttribute)assignableElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssignableElement_Locked() {
		return (EAttribute)assignableElementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAssignableElement_VesselAssignmentType() {
		return (EReference)assignableElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselTypeGroup() {
		return vesselTypeGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselTypeGroup_VesselType() {
		return (EAttribute)vesselTypeGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEndHeelOptions() {
		return endHeelOptionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEndHeelOptions_TargetEndHeel() {
		return (EAttribute)endHeelOptionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getCargoType() {
		return cargoTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getVesselType() {
		return vesselTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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

		cargoEClass = createEClass(CARGO);
		createEAttribute(cargoEClass, CARGO__ALLOW_REWIRING);
		createEReference(cargoEClass, CARGO__SLOTS);
		createEOperation(cargoEClass, CARGO___GET_CARGO_TYPE);
		createEOperation(cargoEClass, CARGO___GET_SORTED_SLOTS);
		createEOperation(cargoEClass, CARGO___GET_LOAD_NAME);

		slotEClass = createEClass(SLOT);
		createEReference(slotEClass, SLOT__CONTRACT);
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
		createEAttribute(slotEClass, SLOT__OPTIONAL);
		createEAttribute(slotEClass, SLOT__PRICE_EXPRESSION);
		createEReference(slotEClass, SLOT__CARGO);
		createEAttribute(slotEClass, SLOT__PRICING_EVENT);
		createEAttribute(slotEClass, SLOT__PRICING_DATE);
		createEAttribute(slotEClass, SLOT__NOTES);
		createEAttribute(slotEClass, SLOT__DIVERTIBLE);
		createEAttribute(slotEClass, SLOT__SHIPPING_DAYS_RESTRICTION);
		createEReference(slotEClass, SLOT__ENTITY);
		createEReference(slotEClass, SLOT__RESTRICTED_CONTRACTS);
		createEReference(slotEClass, SLOT__RESTRICTED_PORTS);
		createEAttribute(slotEClass, SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE);
		createEAttribute(slotEClass, SLOT__HEDGES);
		createEAttribute(slotEClass, SLOT__MISC_COSTS);
		createEReference(slotEClass, SLOT__ALLOWED_VESSELS);
		createEAttribute(slotEClass, SLOT__CANCELLATION_EXPRESSION);
		createEAttribute(slotEClass, SLOT__OVERRIDE_RESTRICTIONS);
		createEReference(slotEClass, SLOT__NOMINATED_VESSEL);
		createEAttribute(slotEClass, SLOT__LOCKED);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_PORT_DURATION);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_CONTRACT_VOLUME_LIMITS_UNIT);
		createEOperation(slotEClass, SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME);
		createEOperation(slotEClass, SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME);
		createEOperation(slotEClass, SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX);
		createEOperation(slotEClass, SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE_UNITS);
		createEOperation(slotEClass, SLOT___GET_WINDOW_SIZE_IN_HOURS);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATED_ENTITY);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_CONTRACTS);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_PORTS);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_LISTS_ARE_PERMISSIVE);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_CONTRACT_CANCELLATION_EXPRESSION);
		createEOperation(slotEClass, SLOT___GET_SLOT_OR_DELEGATED_PRICING_EVENT);
		createEOperation(slotEClass, SLOT___GET_PRICING_DATE_AS_DATE_TIME);
		createEOperation(slotEClass, SLOT___GET_SLOT_CONTRACT_PARAMS);

		loadSlotEClass = createEClass(LOAD_SLOT);
		createEAttribute(loadSlotEClass, LOAD_SLOT__CARGO_CV);
		createEAttribute(loadSlotEClass, LOAD_SLOT__ARRIVE_COLD);
		createEAttribute(loadSlotEClass, LOAD_SLOT__DES_PURCHASE);
		createEReference(loadSlotEClass, LOAD_SLOT__TRANSFER_FROM);
		createEAttribute(loadSlotEClass, LOAD_SLOT__SALES_DELIVERY_TYPE);
		createEOperation(loadSlotEClass, LOAD_SLOT___GET_SLOT_OR_DELEGATED_CV);
		createEOperation(loadSlotEClass, LOAD_SLOT___GET_SLOT_OR_CONTRACT_DELIVERY_TYPE);

		dischargeSlotEClass = createEClass(DISCHARGE_SLOT);
		createEAttribute(dischargeSlotEClass, DISCHARGE_SLOT__FOB_SALE);
		createEAttribute(dischargeSlotEClass, DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE);
		createEReference(dischargeSlotEClass, DISCHARGE_SLOT__TRANSFER_TO);
		createEAttribute(dischargeSlotEClass, DISCHARGE_SLOT__MIN_CV_VALUE);
		createEAttribute(dischargeSlotEClass, DISCHARGE_SLOT__MAX_CV_VALUE);
		createEOperation(dischargeSlotEClass, DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MIN_CV);
		createEOperation(dischargeSlotEClass, DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MAX_CV);
		createEOperation(dischargeSlotEClass, DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_DELIVERY_TYPE);

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
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__BALLAST_BONUS);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_START_BY_AS_DATE_TIME);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_START_AFTER_AS_DATE_TIME);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_END_BY_AS_DATE_TIME);
		createEOperation(vesselAvailabilityEClass, VESSEL_AVAILABILITY___GET_END_AFTER_AS_DATE_TIME);

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
		createEReference(charterOutEventEClass, CHARTER_OUT_EVENT__RELOCATE_TO);
		createEAttribute(charterOutEventEClass, CHARTER_OUT_EVENT__HIRE_RATE);
		createEReference(charterOutEventEClass, CHARTER_OUT_EVENT__HEEL_OPTIONS);
		createEAttribute(charterOutEventEClass, CHARTER_OUT_EVENT__BALLAST_BONUS);
		createEAttribute(charterOutEventEClass, CHARTER_OUT_EVENT__REPOSITIONING_FEE);
		createEOperation(charterOutEventEClass, CHARTER_OUT_EVENT___GET_END_PORT);

		assignableElementEClass = createEClass(ASSIGNABLE_ELEMENT);
		createEAttribute(assignableElementEClass, ASSIGNABLE_ELEMENT__SEQUENCE_HINT);
		createEReference(assignableElementEClass, ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
		createEAttribute(assignableElementEClass, ASSIGNABLE_ELEMENT__SPOT_INDEX);
		createEAttribute(assignableElementEClass, ASSIGNABLE_ELEMENT__LOCKED);

		vesselTypeGroupEClass = createEClass(VESSEL_TYPE_GROUP);
		createEAttribute(vesselTypeGroupEClass, VESSEL_TYPE_GROUP__VESSEL_TYPE);

		endHeelOptionsEClass = createEClass(END_HEEL_OPTIONS);
		createEAttribute(endHeelOptionsEClass, END_HEEL_OPTIONS__TARGET_END_HEEL);

		// Create enums
		cargoTypeEEnum = createEEnum(CARGO_TYPE);
		vesselTypeEEnum = createEEnum(VESSEL_TYPE);
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
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);
		SpotMarketsPackage theSpotMarketsPackage = (SpotMarketsPackage)EPackage.Registry.INSTANCE.getEPackage(SpotMarketsPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		cargoModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		cargoEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		cargoEClass.getESuperTypes().add(this.getAssignableElement());
		slotEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		slotEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		slotEClass.getESuperTypes().add(theTypesPackage.getITimezoneProvider());
		loadSlotEClass.getESuperTypes().add(this.getSlot());
		dischargeSlotEClass.getESuperTypes().add(this.getSlot());
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
		EGenericType g1 = createEGenericType(theTypesPackage.getAVesselSet());
		EGenericType g2 = createEGenericType(theFleetPackage.getVessel());
		g1.getETypeArguments().add(g2);
		vesselTypeGroupEClass.getEGenericSuperTypes().add(g1);

		// Initialize classes, features, and operations; add parameters
		initEClass(cargoModelEClass, CargoModel.class, "CargoModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCargoModel_LoadSlots(), this.getLoadSlot(), null, "loadSlots", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_DischargeSlots(), this.getDischargeSlot(), null, "dischargeSlots", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_Cargoes(), this.getCargo(), null, "cargoes", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_CargoGroups(), this.getCargoGroup(), null, "cargoGroups", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_VesselAvailabilities(), this.getVesselAvailability(), null, "vesselAvailabilities", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_VesselEvents(), this.getVesselEvent(), null, "vesselEvents", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoModel_VesselTypeGroups(), this.getVesselTypeGroup(), null, "vesselTypeGroups", null, 0, -1, CargoModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoEClass, Cargo.class, "Cargo", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCargo_AllowRewiring(), ecorePackage.getEBoolean(), "allowRewiring", "false", 1, 1, Cargo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargo_Slots(), this.getSlot(), this.getSlot_Cargo(), "slots", null, 0, -1, Cargo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getCargo__GetCargoType(), this.getCargoType(), "getCargoType", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getCargo__GetSortedSlots(), this.getSlot(), "getSortedSlots", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getCargo__GetLoadName(), ecorePackage.getEString(), "getLoadName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(slotEClass, Slot.class, "Slot", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlot_Contract(), theCommercialPackage.getContract(), null, "contract", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
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
		initEAttribute(getSlot_Optional(), ecorePackage.getEBoolean(), "optional", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlot_Cargo(), this.getCargo(), this.getCargo_Slots(), "cargo", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_PricingEvent(), theCommercialPackage.getPricingEvent(), "pricingEvent", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_PricingDate(), theDateTimePackage.getLocalDate(), "pricingDate", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_Notes(), ecorePackage.getEString(), "notes", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_Divertible(), ecorePackage.getEBoolean(), "divertible", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_ShippingDaysRestriction(), ecorePackage.getEInt(), "shippingDaysRestriction", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlot_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 1, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlot_RestrictedContracts(), theCommercialPackage.getContract(), null, "restrictedContracts", null, 0, -1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlot_RestrictedPorts(), thePortPackage.getPort(), null, "restrictedPorts", null, 0, -1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_RestrictedListsArePermissive(), ecorePackage.getEBoolean(), "restrictedListsArePermissive", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_Hedges(), ecorePackage.getEInt(), "hedges", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_MiscCosts(), ecorePackage.getEInt(), "miscCosts", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(theFleetPackage.getVessel());
		g1.getETypeArguments().add(g2);
		initEReference(getSlot_AllowedVessels(), g1, null, "allowedVessels", null, 0, -1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_CancellationExpression(), ecorePackage.getEString(), "cancellationExpression", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_OverrideRestrictions(), ecorePackage.getEBoolean(), "overrideRestrictions", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlot_NominatedVessel(), theFleetPackage.getVessel(), null, "nominatedVessel", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlot_Locked(), ecorePackage.getEBoolean(), "locked", null, 0, 1, Slot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrPortDuration(), ecorePackage.getEInt(), "getSlotOrPortDuration", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrContractMinQuantity(), ecorePackage.getEInt(), "getSlotOrContractMinQuantity", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrContractMaxQuantity(), ecorePackage.getEInt(), "getSlotOrContractMaxQuantity", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrContractVolumeLimitsUnit(), theTypesPackage.getVolumeUnits(), "getSlotOrContractVolumeLimitsUnit", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetWindowEndWithSlotOrPortTime(), theDateTimePackage.getDateTime(), "getWindowEndWithSlotOrPortTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetWindowStartWithSlotOrPortTime(), theDateTimePackage.getDateTime(), "getWindowStartWithSlotOrPortTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetWindowEndWithSlotOrPortTimeWithFlex(), theDateTimePackage.getDateTime(), "getWindowEndWithSlotOrPortTimeWithFlex", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetWindowStartWithSlotOrPortTimeWithFlex(), theDateTimePackage.getDateTime(), "getWindowStartWithSlotOrPortTimeWithFlex", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrPortWindowSize(), ecorePackage.getEInt(), "getSlotOrPortWindowSize", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrPortWindowSizeUnits(), theTypesPackage.getTimePeriod(), "getSlotOrPortWindowSizeUnits", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetWindowSizeInHours(), ecorePackage.getEInt(), "getWindowSizeInHours", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegatedEntity(), theCommercialPackage.getBaseLegalEntity(), "getSlotOrDelegatedEntity", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrContractRestrictedContracts(), theCommercialPackage.getContract(), "getSlotOrContractRestrictedContracts", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrContractRestrictedPorts(), thePortPackage.getPort(), "getSlotOrContractRestrictedPorts", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrContractRestrictedListsArePermissive(), ecorePackage.getEBoolean(), "getSlotOrContractRestrictedListsArePermissive", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrContractCancellationExpression(), ecorePackage.getEString(), "getSlotOrContractCancellationExpression", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotOrDelegatedPricingEvent(), theCommercialPackage.getPricingEvent(), "getSlotOrDelegatedPricingEvent", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetPricingDateAsDateTime(), theDateTimePackage.getDateTime(), "getPricingDateAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlot__GetSlotContractParams(), theCommercialPackage.getSlotContractParams(), "getSlotContractParams", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(loadSlotEClass, LoadSlot.class, "LoadSlot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLoadSlot_CargoCV(), ecorePackage.getEDouble(), "cargoCV", null, 1, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLoadSlot_ArriveCold(), ecorePackage.getEBoolean(), "arriveCold", null, 1, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLoadSlot_DESPurchase(), ecorePackage.getEBoolean(), "DESPurchase", null, 0, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLoadSlot_TransferFrom(), this.getDischargeSlot(), this.getDischargeSlot_TransferTo(), "transferFrom", null, 0, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLoadSlot_SalesDeliveryType(), theTypesPackage.getCargoDeliveryType(), "salesDeliveryType", "Any", 0, 1, LoadSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getLoadSlot__GetSlotOrDelegatedCV(), ecorePackage.getEDouble(), "getSlotOrDelegatedCV", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getLoadSlot__GetSlotOrContractDeliveryType(), theTypesPackage.getCargoDeliveryType(), "getSlotOrContractDeliveryType", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(dischargeSlotEClass, DischargeSlot.class, "DischargeSlot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDischargeSlot_FOBSale(), ecorePackage.getEBoolean(), "FOBSale", null, 0, 1, DischargeSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDischargeSlot_PurchaseDeliveryType(), theTypesPackage.getCargoDeliveryType(), "PurchaseDeliveryType", "Any", 0, 1, DischargeSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDischargeSlot_TransferTo(), this.getLoadSlot(), this.getLoadSlot_TransferFrom(), "transferTo", null, 0, 1, DischargeSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDischargeSlot_MinCvValue(), ecorePackage.getEDouble(), "minCvValue", null, 0, 1, DischargeSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDischargeSlot_MaxCvValue(), ecorePackage.getEDouble(), "maxCvValue", null, 0, 1, DischargeSlot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getDischargeSlot__GetSlotOrContractMinCv(), ecorePackage.getEDouble(), "getSlotOrContractMinCv", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getDischargeSlot__GetSlotOrContractMaxCv(), ecorePackage.getEDouble(), "getSlotOrContractMaxCv", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getDischargeSlot__GetSlotOrContractDeliveryType(), theTypesPackage.getCargoDeliveryType(), "getSlotOrContractDeliveryType", 0, 1, IS_UNIQUE, IS_ORDERED);

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
		initEReference(getVesselAvailability_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_TimeCharterRate(), ecorePackage.getEString(), "timeCharterRate", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getVesselAvailability_StartAt(), g1, null, "startAt", null, 0, -1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_StartAfter(), theDateTimePackage.getLocalDateTime(), "startAfter", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_StartBy(), theDateTimePackage.getLocalDateTime(), "startBy", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getVesselAvailability_EndAt(), g1, null, "endAt", null, 0, -1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_EndAfter(), theDateTimePackage.getLocalDateTime(), "endAfter", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_EndBy(), theDateTimePackage.getLocalDateTime(), "endBy", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailability_StartHeel(), theFleetPackage.getHeelOptions(), null, "startHeel", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailability_EndHeel(), this.getEndHeelOptions(), null, "endHeel", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_ForceHireCostOnlyEndRule(), ecorePackage.getEBoolean(), "forceHireCostOnlyEndRule", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_RepositioningFee(), ecorePackage.getEString(), "repositioningFee", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_BallastBonus(), ecorePackage.getEString(), "ballastBonus", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getVesselAvailability__GetStartByAsDateTime(), theDateTimePackage.getDateTime(), "getStartByAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__GetStartAfterAsDateTime(), theDateTimePackage.getDateTime(), "getStartAfterAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__GetEndByAsDateTime(), theDateTimePackage.getDateTime(), "getEndByAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselAvailability__GetEndAfterAsDateTime(), theDateTimePackage.getDateTime(), "getEndAfterAsDateTime", 0, 1, IS_UNIQUE, IS_ORDERED);

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
		initEReference(getCharterOutEvent_RelocateTo(), thePortPackage.getPort(), null, "relocateTo", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutEvent_HireRate(), ecorePackage.getEInt(), "hireRate", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterOutEvent_HeelOptions(), theFleetPackage.getHeelOptions(), null, "heelOptions", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutEvent_BallastBonus(), ecorePackage.getEInt(), "ballastBonus", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutEvent_RepositioningFee(), ecorePackage.getEInt(), "repositioningFee", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getCharterOutEvent__GetEndPort(), thePortPackage.getPort(), "getEndPort", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(assignableElementEClass, AssignableElement.class, "AssignableElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAssignableElement_SequenceHint(), ecorePackage.getEInt(), "sequenceHint", null, 0, 1, AssignableElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssignableElement_VesselAssignmentType(), theTypesPackage.getVesselAssignmentType(), null, "vesselAssignmentType", null, 0, 1, AssignableElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssignableElement_SpotIndex(), ecorePackage.getEInt(), "spotIndex", null, 0, 1, AssignableElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssignableElement_Locked(), ecorePackage.getEBoolean(), "locked", null, 0, 1, AssignableElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselTypeGroupEClass, VesselTypeGroup.class, "VesselTypeGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselTypeGroup_VesselType(), this.getVesselType(), "vesselType", null, 1, 1, VesselTypeGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(endHeelOptionsEClass, EndHeelOptions.class, "EndHeelOptions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEndHeelOptions_TargetEndHeel(), ecorePackage.getEInt(), "targetEndHeel", null, 0, 1, EndHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(cargoTypeEEnum, CargoType.class, "CargoType");
		addEEnumLiteral(cargoTypeEEnum, CargoType.FLEET);
		addEEnumLiteral(cargoTypeEEnum, CargoType.FOB);
		addEEnumLiteral(cargoTypeEEnum, CargoType.DES);

		initEEnum(vesselTypeEEnum, VesselType.class, "VesselType");
		addEEnumLiteral(vesselTypeEEnum, VesselType.OWNED);
		addEEnumLiteral(vesselTypeEEnum, VesselType.TIME_CHARTERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
		// http://www.mmxlabs.com/models/pricing/expressionType
		createExpressionTypeAnnotations();
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
			 "formatString", "-##0"
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
		  (getSlot_ShippingDaysRestriction(), 
		   source, 
		   new String[] {
			 "unit", "days",
			 "formatString", "###"
		   });	
		addAnnotation
		  (getSlot_Hedges(), 
		   source, 
		   new String[] {
			 "unitPrefix", "$",
			 "formatString", "-###,###,##0"
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
		  (getVesselAvailability_BallastBonus(), 
		   source, 
		   new String[] {
			 "unit", "$/day"
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
			 "unit", "$"
		   });	
		addAnnotation
		  (getCharterOutEvent_RepositioningFee(), 
		   source, 
		   new String[] {
			 "unit", "$",
			 "formatString", "###,##0"
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
		  (getVesselAvailability_BallastBonus(), 
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
	}

} //CargoPackageImpl
