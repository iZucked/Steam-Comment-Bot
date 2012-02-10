/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.fleetallocation.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import scenario.ScenarioPackage;
import scenario.cargo.CargoPackage;
import scenario.cargo.impl.CargoPackageImpl;
import scenario.contract.ContractPackage;
import scenario.contract.impl.ContractPackageImpl;
import scenario.fleet.FleetPackage;
import scenario.fleet.impl.FleetPackageImpl;
import scenario.impl.ScenarioPackageImpl;
import scenario.market.MarketPackage;
import scenario.market.impl.MarketPackageImpl;
import scenario.optimiser.OptimiserPackage;
import scenario.optimiser.impl.OptimiserPackageImpl;
import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.impl.LsoPackageImpl;
import scenario.port.PortPackage;
import scenario.port.impl.PortPackageImpl;
import scenario.schedule.SchedulePackage;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.impl.EventsPackageImpl;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetVessel;
import scenario.schedule.fleetallocation.FleetallocationFactory;
import scenario.schedule.fleetallocation.FleetallocationPackage;
import scenario.schedule.fleetallocation.SpotVessel;
import scenario.schedule.impl.SchedulePackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class FleetallocationPackageImpl extends EPackageImpl implements FleetallocationPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass allocatedVesselEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fleetVesselEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spotVesselEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see scenario.schedule.fleetallocation.FleetallocationPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private FleetallocationPackageImpl() {
		super(eNS_URI, FleetallocationFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link FleetallocationPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static FleetallocationPackage init() {
		if (isInited) return (FleetallocationPackage)EPackage.Registry.INSTANCE.getEPackage(FleetallocationPackage.eNS_URI);

		// Obtain or create and register package
		FleetallocationPackageImpl theFleetallocationPackage = (FleetallocationPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof FleetallocationPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new FleetallocationPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ScenarioPackageImpl theScenarioPackage = (ScenarioPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ScenarioPackage.eNS_URI) instanceof ScenarioPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ScenarioPackage.eNS_URI) : ScenarioPackage.eINSTANCE);
		FleetPackageImpl theFleetPackage = (FleetPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI) instanceof FleetPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI) : FleetPackage.eINSTANCE);
		SchedulePackageImpl theSchedulePackage = (SchedulePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI) instanceof SchedulePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI) : SchedulePackage.eINSTANCE);
		EventsPackageImpl theEventsPackage = (EventsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(EventsPackage.eNS_URI) instanceof EventsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(EventsPackage.eNS_URI) : EventsPackage.eINSTANCE);
		PortPackageImpl thePortPackage = (PortPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI) instanceof PortPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI) : PortPackage.eINSTANCE);
		CargoPackageImpl theCargoPackage = (CargoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI) instanceof CargoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI) : CargoPackage.eINSTANCE);
		ContractPackageImpl theContractPackage = (ContractPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ContractPackage.eNS_URI) instanceof ContractPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ContractPackage.eNS_URI) : ContractPackage.eINSTANCE);
		MarketPackageImpl theMarketPackage = (MarketPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(MarketPackage.eNS_URI) instanceof MarketPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MarketPackage.eNS_URI) : MarketPackage.eINSTANCE);
		OptimiserPackageImpl theOptimiserPackage = (OptimiserPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI) instanceof OptimiserPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI) : OptimiserPackage.eINSTANCE);
		LsoPackageImpl theLsoPackage = (LsoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI) instanceof LsoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI) : LsoPackage.eINSTANCE);

		// Create package meta-data objects
		theFleetallocationPackage.createPackageContents();
		theScenarioPackage.createPackageContents();
		theFleetPackage.createPackageContents();
		theSchedulePackage.createPackageContents();
		theEventsPackage.createPackageContents();
		thePortPackage.createPackageContents();
		theCargoPackage.createPackageContents();
		theContractPackage.createPackageContents();
		theMarketPackage.createPackageContents();
		theOptimiserPackage.createPackageContents();
		theLsoPackage.createPackageContents();

		// Initialize created meta-data
		theFleetallocationPackage.initializePackageContents();
		theScenarioPackage.initializePackageContents();
		theFleetPackage.initializePackageContents();
		theSchedulePackage.initializePackageContents();
		theEventsPackage.initializePackageContents();
		thePortPackage.initializePackageContents();
		theCargoPackage.initializePackageContents();
		theContractPackage.initializePackageContents();
		theMarketPackage.initializePackageContents();
		theOptimiserPackage.initializePackageContents();
		theLsoPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theFleetallocationPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(FleetallocationPackage.eNS_URI, theFleetallocationPackage);
		return theFleetallocationPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAllocatedVessel() {
		return allocatedVesselEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getAllocatedVessel__GetHourlyCharterPrice() {
		return allocatedVesselEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getAllocatedVessel__GetName() {
		return allocatedVesselEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFleetVessel() {
		return fleetVesselEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFleetVessel_Vessel() {
		return (EReference)fleetVesselEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getFleetVessel__GetName() {
		return fleetVesselEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSpotVessel() {
		return spotVesselEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotVessel_Index() {
		return (EAttribute)spotVesselEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotVessel_VesselClass() {
		return (EReference)spotVesselEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSpotVessel__GetHourlyCharterPrice() {
		return spotVesselEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getSpotVessel__GetName() {
		return spotVesselEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FleetallocationFactory getFleetallocationFactory() {
		return (FleetallocationFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		allocatedVesselEClass = createEClass(ALLOCATED_VESSEL);
		createEOperation(allocatedVesselEClass, ALLOCATED_VESSEL___GET_HOURLY_CHARTER_PRICE);
		createEOperation(allocatedVesselEClass, ALLOCATED_VESSEL___GET_NAME);

		fleetVesselEClass = createEClass(FLEET_VESSEL);
		createEReference(fleetVesselEClass, FLEET_VESSEL__VESSEL);
		createEOperation(fleetVesselEClass, FLEET_VESSEL___GET_NAME);

		spotVesselEClass = createEClass(SPOT_VESSEL);
		createEAttribute(spotVesselEClass, SPOT_VESSEL__INDEX);
		createEReference(spotVesselEClass, SPOT_VESSEL__VESSEL_CLASS);
		createEOperation(spotVesselEClass, SPOT_VESSEL___GET_HOURLY_CHARTER_PRICE);
		createEOperation(spotVesselEClass, SPOT_VESSEL___GET_NAME);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
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
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		fleetVesselEClass.getESuperTypes().add(this.getAllocatedVessel());
		spotVesselEClass.getESuperTypes().add(this.getAllocatedVessel());

		// Initialize classes, features, and operations; add parameters
		initEClass(allocatedVesselEClass, AllocatedVessel.class, "AllocatedVessel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEOperation(getAllocatedVessel__GetHourlyCharterPrice(), ecorePackage.getEDouble(), "getHourlyCharterPrice", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getAllocatedVessel__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(fleetVesselEClass, FleetVessel.class, "FleetVessel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFleetVessel_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 1, 1, FleetVessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getFleetVessel__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(spotVesselEClass, SpotVessel.class, "SpotVessel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpotVessel_Index(), ecorePackage.getEInt(), "index", null, 1, 1, SpotVessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotVessel_VesselClass(), theFleetPackage.getVesselClass(), null, "vesselClass", null, 1, 1, SpotVessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getSpotVessel__GetHourlyCharterPrice(), ecorePackage.getEDouble(), "getHourlyCharterPrice", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSpotVessel__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);
	}

} // FleetallocationPackageImpl
