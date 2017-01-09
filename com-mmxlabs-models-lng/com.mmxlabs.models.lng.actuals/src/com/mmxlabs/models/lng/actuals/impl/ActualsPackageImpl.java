/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.actuals.ActualsFactory;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.PenaltyType;
import com.mmxlabs.models.lng.actuals.ReturnActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.TypesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ActualsPackageImpl extends EPackageImpl implements ActualsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass actualsModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotActualsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoActualsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass loadActualsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dischargeActualsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass returnActualsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum penaltyTypeEEnum = null;

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
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ActualsPackageImpl() {
		super(eNS_URI, ActualsFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ActualsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ActualsPackage init() {
		if (isInited) return (ActualsPackage)EPackage.Registry.INSTANCE.getEPackage(ActualsPackage.eNS_URI);

		// Obtain or create and register package
		ActualsPackageImpl theActualsPackage = (ActualsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ActualsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ActualsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CargoPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theActualsPackage.createPackageContents();

		// Initialize created meta-data
		theActualsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theActualsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ActualsPackage.eNS_URI, theActualsPackage);
		return theActualsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getActualsModel() {
		return actualsModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getActualsModel_CargoActuals() {
		return (EReference)actualsModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSlotActuals() {
		return slotActualsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_CV() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_PortCharges() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_CapacityCharges() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlotActuals__GetOperationsStartAsDateTime() {
		return slotActualsEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSlotActuals__GetOperationsEndAsDateTime() {
		return slotActualsEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_BaseFuelConsumption() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_PortBaseFuelConsumption() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlotActuals_Route() {
		return (EReference)slotActualsEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_Distance() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_RouteCosts() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_CrewBonus() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlotActuals_Slot() {
		return (EReference)slotActualsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_Counterparty() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_OperationsStart() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_OperationsEnd() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSlotActuals_TitleTransferPoint() {
		return (EReference)slotActualsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_VolumeInM3() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_VolumeInMMBtu() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_PriceDOL() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_Penalty() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_Notes() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoActuals() {
		return cargoActualsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_BaseFuelPrice() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_InsurancePremium() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getCargoActuals__GetSortedActuals() {
		return cargoActualsEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoActuals_Actuals() {
		return (EReference)cargoActualsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoActuals_ReturnActuals() {
		return (EReference)cargoActualsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_ContractYear() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_OperationNumber() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_SubOperationNumber() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_SellerID() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_CargoReference() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_CargoReferenceSeller() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoActuals_Vessel() {
		return (EReference)cargoActualsEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_CharterRatePerDay() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoActuals_Cargo() {
		return (EReference)cargoActualsEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLoadActuals() {
		return loadActualsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLoadActuals_ContractType() {
		return (EAttribute)loadActualsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLoadActuals_StartingHeelM3() {
		return (EAttribute)loadActualsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLoadActuals_StartingHeelMMBTu() {
		return (EAttribute)loadActualsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDischargeActuals() {
		return dischargeActualsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDischargeActuals_DeliveryType() {
		return (EAttribute)dischargeActualsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDischargeActuals_EndHeelM3() {
		return (EAttribute)dischargeActualsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDischargeActuals_EndHeelMMBTu() {
		return (EAttribute)dischargeActualsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getReturnActuals() {
		return returnActualsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getReturnActuals_TitleTransferPoint() {
		return (EReference)returnActualsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getReturnActuals_OperationsStart() {
		return (EAttribute)returnActualsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getReturnActuals_EndHeelM3() {
		return (EAttribute)returnActualsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getReturnActuals__GetOperationsStartAsDateTime() {
		return returnActualsEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getPenaltyType() {
		return penaltyTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ActualsFactory getActualsFactory() {
		return (ActualsFactory)getEFactoryInstance();
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
		actualsModelEClass = createEClass(ACTUALS_MODEL);
		createEReference(actualsModelEClass, ACTUALS_MODEL__CARGO_ACTUALS);

		slotActualsEClass = createEClass(SLOT_ACTUALS);
		createEReference(slotActualsEClass, SLOT_ACTUALS__SLOT);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__COUNTERPARTY);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__OPERATIONS_START);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__OPERATIONS_END);
		createEReference(slotActualsEClass, SLOT_ACTUALS__TITLE_TRANSFER_POINT);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__VOLUME_IN_M3);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__VOLUME_IN_MM_BTU);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__PRICE_DOL);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__PENALTY);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__NOTES);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__CV);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__BASE_FUEL_CONSUMPTION);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION);
		createEReference(slotActualsEClass, SLOT_ACTUALS__ROUTE);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__DISTANCE);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__ROUTE_COSTS);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__CREW_BONUS);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__PORT_CHARGES);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__CAPACITY_CHARGES);
		createEOperation(slotActualsEClass, SLOT_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME);
		createEOperation(slotActualsEClass, SLOT_ACTUALS___GET_OPERATIONS_END_AS_DATE_TIME);

		cargoActualsEClass = createEClass(CARGO_ACTUALS);
		createEReference(cargoActualsEClass, CARGO_ACTUALS__ACTUALS);
		createEReference(cargoActualsEClass, CARGO_ACTUALS__RETURN_ACTUALS);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__CONTRACT_YEAR);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__OPERATION_NUMBER);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__SUB_OPERATION_NUMBER);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__SELLER_ID);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__CARGO_REFERENCE);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__CARGO_REFERENCE_SELLER);
		createEReference(cargoActualsEClass, CARGO_ACTUALS__VESSEL);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__CHARTER_RATE_PER_DAY);
		createEReference(cargoActualsEClass, CARGO_ACTUALS__CARGO);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__BASE_FUEL_PRICE);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__INSURANCE_PREMIUM);
		createEOperation(cargoActualsEClass, CARGO_ACTUALS___GET_SORTED_ACTUALS);

		loadActualsEClass = createEClass(LOAD_ACTUALS);
		createEAttribute(loadActualsEClass, LOAD_ACTUALS__CONTRACT_TYPE);
		createEAttribute(loadActualsEClass, LOAD_ACTUALS__STARTING_HEEL_M3);
		createEAttribute(loadActualsEClass, LOAD_ACTUALS__STARTING_HEEL_MMB_TU);

		dischargeActualsEClass = createEClass(DISCHARGE_ACTUALS);
		createEAttribute(dischargeActualsEClass, DISCHARGE_ACTUALS__DELIVERY_TYPE);
		createEAttribute(dischargeActualsEClass, DISCHARGE_ACTUALS__END_HEEL_M3);
		createEAttribute(dischargeActualsEClass, DISCHARGE_ACTUALS__END_HEEL_MMB_TU);

		returnActualsEClass = createEClass(RETURN_ACTUALS);
		createEReference(returnActualsEClass, RETURN_ACTUALS__TITLE_TRANSFER_POINT);
		createEAttribute(returnActualsEClass, RETURN_ACTUALS__OPERATIONS_START);
		createEAttribute(returnActualsEClass, RETURN_ACTUALS__END_HEEL_M3);
		createEOperation(returnActualsEClass, RETURN_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME);

		// Create enums
		penaltyTypeEEnum = createEEnum(PENALTY_TYPE);
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
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		slotActualsEClass.getESuperTypes().add(theTypesPackage.getITimezoneProvider());
		loadActualsEClass.getESuperTypes().add(this.getSlotActuals());
		dischargeActualsEClass.getESuperTypes().add(this.getSlotActuals());
		returnActualsEClass.getESuperTypes().add(theTypesPackage.getITimezoneProvider());

		// Initialize classes, features, and operations; add parameters
		initEClass(actualsModelEClass, ActualsModel.class, "ActualsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getActualsModel_CargoActuals(), this.getCargoActuals(), null, "cargoActuals", null, 0, -1, ActualsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(slotActualsEClass, SlotActuals.class, "SlotActuals", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlotActuals_Slot(), theCargoPackage.getSlot(), null, "slot", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_Counterparty(), ecorePackage.getEString(), "counterparty", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_OperationsStart(), theDateTimePackage.getLocalDateTime(), "operationsStart", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_OperationsEnd(), theDateTimePackage.getLocalDateTime(), "operationsEnd", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotActuals_TitleTransferPoint(), thePortPackage.getPort(), null, "titleTransferPoint", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_VolumeInM3(), ecorePackage.getEDouble(), "volumeInM3", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_VolumeInMMBtu(), ecorePackage.getEDouble(), "volumeInMMBtu", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_PriceDOL(), ecorePackage.getEDouble(), "priceDOL", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_Penalty(), this.getPenaltyType(), "penalty", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_Notes(), ecorePackage.getEString(), "notes", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_CV(), ecorePackage.getEDouble(), "CV", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_BaseFuelConsumption(), ecorePackage.getEInt(), "baseFuelConsumption", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_PortBaseFuelConsumption(), ecorePackage.getEInt(), "portBaseFuelConsumption", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotActuals_Route(), thePortPackage.getRoute(), null, "route", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_Distance(), ecorePackage.getEInt(), "distance", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_RouteCosts(), ecorePackage.getEInt(), "routeCosts", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_CrewBonus(), ecorePackage.getEInt(), "crewBonus", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_PortCharges(), ecorePackage.getEInt(), "portCharges", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_CapacityCharges(), ecorePackage.getEInt(), "capacityCharges", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getSlotActuals__GetOperationsStartAsDateTime(), theDateTimePackage.getDateTime(), "getOperationsStartAsDateTime", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlotActuals__GetOperationsEndAsDateTime(), theDateTimePackage.getDateTime(), "getOperationsEndAsDateTime", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(cargoActualsEClass, CargoActuals.class, "CargoActuals", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCargoActuals_Actuals(), this.getSlotActuals(), null, "actuals", null, 0, -1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoActuals_ReturnActuals(), this.getReturnActuals(), null, "returnActuals", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_ContractYear(), ecorePackage.getEInt(), "contractYear", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_OperationNumber(), ecorePackage.getEInt(), "operationNumber", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_SubOperationNumber(), ecorePackage.getEInt(), "subOperationNumber", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_SellerID(), ecorePackage.getEString(), "sellerID", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_CargoReference(), ecorePackage.getEString(), "cargoReference", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_CargoReferenceSeller(), ecorePackage.getEString(), "cargoReferenceSeller", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoActuals_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_CharterRatePerDay(), ecorePackage.getEDouble(), "charterRatePerDay", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoActuals_Cargo(), theCargoPackage.getCargo(), null, "cargo", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_BaseFuelPrice(), ecorePackage.getEDouble(), "baseFuelPrice", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_InsurancePremium(), ecorePackage.getEInt(), "insurancePremium", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getCargoActuals__GetSortedActuals(), ecorePackage.getEObject(), "getSortedActuals", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEClass(loadActualsEClass, LoadActuals.class, "LoadActuals", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLoadActuals_ContractType(), ecorePackage.getEString(), "contractType", null, 0, 1, LoadActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLoadActuals_StartingHeelM3(), ecorePackage.getEDouble(), "startingHeelM3", null, 0, 1, LoadActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLoadActuals_StartingHeelMMBTu(), ecorePackage.getEDouble(), "startingHeelMMBTu", null, 0, 1, LoadActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dischargeActualsEClass, DischargeActuals.class, "DischargeActuals", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDischargeActuals_DeliveryType(), ecorePackage.getEString(), "deliveryType", null, 0, 1, DischargeActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDischargeActuals_EndHeelM3(), ecorePackage.getEDouble(), "endHeelM3", null, 0, 1, DischargeActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDischargeActuals_EndHeelMMBTu(), ecorePackage.getEDouble(), "endHeelMMBTu", null, 0, 1, DischargeActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(returnActualsEClass, ReturnActuals.class, "ReturnActuals", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getReturnActuals_TitleTransferPoint(), thePortPackage.getPort(), null, "titleTransferPoint", null, 0, 1, ReturnActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReturnActuals_OperationsStart(), theDateTimePackage.getLocalDateTime(), "operationsStart", null, 0, 1, ReturnActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReturnActuals_EndHeelM3(), ecorePackage.getEDouble(), "endHeelM3", null, 0, 1, ReturnActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getReturnActuals__GetOperationsStartAsDateTime(), theDateTimePackage.getDateTime(), "getOperationsStartAsDateTime", 1, 1, IS_UNIQUE, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(penaltyTypeEEnum, PenaltyType.class, "PenaltyType");
		addEEnumLiteral(penaltyTypeEEnum, PenaltyType.TOP);
		addEEnumLiteral(penaltyTypeEEnum, PenaltyType.NOT_TOP);

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
		  (getSlotActuals_VolumeInM3(), 
		   source, 
		   new String[] {
			 "unit", "m\u00b3",
			 "formatString", "###,##0.###"
		   });	
		addAnnotation
		  (getSlotActuals_VolumeInMMBtu(), 
		   source, 
		   new String[] {
			 "unit", "mmBtu",
			 "formatString", "##,###,##0.###"
		   });	
		addAnnotation
		  (getSlotActuals_PriceDOL(), 
		   source, 
		   new String[] {
			 "unit", "$/mmBtu",
			 "formatString", "#0.###"
		   });	
		addAnnotation
		  (getSlotActuals_CV(), 
		   source, 
		   new String[] {
			 "unit", "mmBtu/m\u00b3",
			 "formatString", "#0.#####"
		   });	
		addAnnotation
		  (getSlotActuals_BaseFuelConsumption(), 
		   source, 
		   new String[] {
			 "unit", "MT",
			 "formatString", "###,##0"
		   });	
		addAnnotation
		  (getSlotActuals_PortBaseFuelConsumption(), 
		   source, 
		   new String[] {
			 "unit", "MT",
			 "formatString", "###,##0"
		   });	
		addAnnotation
		  (getSlotActuals_RouteCosts(), 
		   source, 
		   new String[] {
			 "unit", "$",
			 "formatString", "###,###,##0"
		   });	
		addAnnotation
		  (getSlotActuals_CrewBonus(), 
		   source, 
		   new String[] {
			 "unit", "$",
			 "formatString", "###,###,##0"
		   });	
		addAnnotation
		  (getSlotActuals_PortCharges(), 
		   source, 
		   new String[] {
			 "unit", "$",
			 "formatString", "###,###,##0"
		   });	
		addAnnotation
		  (getSlotActuals_CapacityCharges(), 
		   source, 
		   new String[] {
			 "unit", "$",
			 "formatString", "###,###,##0"
		   });	
		addAnnotation
		  (getCargoActuals_ContractYear(), 
		   source, 
		   new String[] {
			 "formatString", "####"
		   });	
		addAnnotation
		  (getCargoActuals_CharterRatePerDay(), 
		   source, 
		   new String[] {
			 "unit", "$/day",
			 "formatString", "###,##0.##"
		   });	
		addAnnotation
		  (getCargoActuals_BaseFuelPrice(), 
		   source, 
		   new String[] {
			 "unit", "MT/d",
			 "formatString", "#,##0.###"
		   });	
		addAnnotation
		  (getCargoActuals_InsurancePremium(), 
		   source, 
		   new String[] {
			 "unit", "$",
			 "formatString", "###,###,##0"
		   });	
		addAnnotation
		  (getLoadActuals_StartingHeelM3(), 
		   source, 
		   new String[] {
			 "unit", "m\u00b3",
			 "formatString", "###,##0.###"
		   });	
		addAnnotation
		  (getLoadActuals_StartingHeelMMBTu(), 
		   source, 
		   new String[] {
			 "unit", "mmBtu",
			 "formatString", "##,###,##0.###"
		   });	
		addAnnotation
		  (getDischargeActuals_EndHeelM3(), 
		   source, 
		   new String[] {
			 "unit", "m\u00b3",
			 "formatString", "###,##0.###"
		   });	
		addAnnotation
		  (getDischargeActuals_EndHeelMMBTu(), 
		   source, 
		   new String[] {
			 "unit", "mmBtu",
			 "formatString", "##,###,##0.###"
		   });	
		addAnnotation
		  (getReturnActuals_EndHeelM3(), 
		   source, 
		   new String[] {
			 "unit", "m\u00b3",
			 "formatString", "###,##0.###"
		   });
	}

} //ActualsPackageImpl
