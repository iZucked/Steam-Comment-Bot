/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.fleet.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import scenario.ScenarioPackage;

import scenario.cargo.CargoPackage;

import scenario.cargo.impl.CargoPackageImpl;

import scenario.contract.ContractPackage;

import scenario.contract.impl.ContractPackageImpl;

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

import scenario.schedule.fleet.AllocatedVessel;
import scenario.schedule.fleet.FleetFactory;
import scenario.schedule.fleet.FleetPackage;
import scenario.schedule.fleet.FleetVessel;
import scenario.schedule.fleet.SpotVessel;

import scenario.schedule.impl.SchedulePackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FleetPackageImpl extends EPackageImpl implements FleetPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass allocatedVesselEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fleetVesselEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see scenario.schedule.fleet.FleetPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private FleetPackageImpl() {
		super(eNS_URI, FleetFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link FleetPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static FleetPackage init() {
		if (isInited) return (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);

		// Obtain or create and register package
		FleetPackageImpl theFleetPackage = (FleetPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof FleetPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new FleetPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ScenarioPackageImpl theScenarioPackage = (ScenarioPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ScenarioPackage.eNS_URI) instanceof ScenarioPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ScenarioPackage.eNS_URI) : ScenarioPackage.eINSTANCE);
		scenario.fleet.impl.FleetPackageImpl theFleetPackage_1 = (scenario.fleet.impl.FleetPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(scenario.fleet.FleetPackage.eNS_URI) instanceof scenario.fleet.impl.FleetPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(scenario.fleet.FleetPackage.eNS_URI) : scenario.fleet.FleetPackage.eINSTANCE);
		SchedulePackageImpl theSchedulePackage = (SchedulePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI) instanceof SchedulePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI) : SchedulePackage.eINSTANCE);
		EventsPackageImpl theEventsPackage = (EventsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(EventsPackage.eNS_URI) instanceof EventsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(EventsPackage.eNS_URI) : EventsPackage.eINSTANCE);
		PortPackageImpl thePortPackage = (PortPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI) instanceof PortPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI) : PortPackage.eINSTANCE);
		CargoPackageImpl theCargoPackage = (CargoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI) instanceof CargoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI) : CargoPackage.eINSTANCE);
		ContractPackageImpl theContractPackage = (ContractPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ContractPackage.eNS_URI) instanceof ContractPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ContractPackage.eNS_URI) : ContractPackage.eINSTANCE);
		MarketPackageImpl theMarketPackage = (MarketPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(MarketPackage.eNS_URI) instanceof MarketPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MarketPackage.eNS_URI) : MarketPackage.eINSTANCE);
		OptimiserPackageImpl theOptimiserPackage = (OptimiserPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI) instanceof OptimiserPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI) : OptimiserPackage.eINSTANCE);
		LsoPackageImpl theLsoPackage = (LsoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI) instanceof LsoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI) : LsoPackage.eINSTANCE);

		// Create package meta-data objects
		theFleetPackage.createPackageContents();
		theScenarioPackage.createPackageContents();
		theFleetPackage_1.createPackageContents();
		theSchedulePackage.createPackageContents();
		theEventsPackage.createPackageContents();
		thePortPackage.createPackageContents();
		theCargoPackage.createPackageContents();
		theContractPackage.createPackageContents();
		theMarketPackage.createPackageContents();
		theOptimiserPackage.createPackageContents();
		theLsoPackage.createPackageContents();

		// Initialize created meta-data
		theFleetPackage.initializePackageContents();
		theScenarioPackage.initializePackageContents();
		theFleetPackage_1.initializePackageContents();
		theSchedulePackage.initializePackageContents();
		theEventsPackage.initializePackageContents();
		thePortPackage.initializePackageContents();
		theCargoPackage.initializePackageContents();
		theContractPackage.initializePackageContents();
		theMarketPackage.initializePackageContents();
		theOptimiserPackage.initializePackageContents();
		theLsoPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theFleetPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(FleetPackage.eNS_URI, theFleetPackage);
		return theFleetPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAllocatedVessel() {
		return allocatedVesselEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFleetVessel() {
		return fleetVesselEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetVessel_Vessel() {
		return (EReference)fleetVesselEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpotVessel() {
		return spotVesselEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpotVessel_Index() {
		return (EAttribute)spotVesselEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotVessel_VesselClass() {
		return (EReference)spotVesselEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetFactory getFleetFactory() {
		return (FleetFactory)getEFactoryInstance();
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
		allocatedVesselEClass = createEClass(ALLOCATED_VESSEL);

		fleetVesselEClass = createEClass(FLEET_VESSEL);
		createEReference(fleetVesselEClass, FLEET_VESSEL__VESSEL);

		spotVesselEClass = createEClass(SPOT_VESSEL);
		createEAttribute(spotVesselEClass, SPOT_VESSEL__INDEX);
		createEReference(spotVesselEClass, SPOT_VESSEL__VESSEL_CLASS);
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
		scenario.fleet.FleetPackage theFleetPackage_1 = (scenario.fleet.FleetPackage)EPackage.Registry.INSTANCE.getEPackage(scenario.fleet.FleetPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		fleetVesselEClass.getESuperTypes().add(this.getAllocatedVessel());
		spotVesselEClass.getESuperTypes().add(this.getAllocatedVessel());

		// Initialize classes and features; add operations and parameters
		initEClass(allocatedVesselEClass, AllocatedVessel.class, "AllocatedVessel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(fleetVesselEClass, FleetVessel.class, "FleetVessel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFleetVessel_Vessel(), theFleetPackage_1.getVessel(), null, "vessel", null, 1, 1, FleetVessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spotVesselEClass, SpotVessel.class, "SpotVessel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpotVessel_Index(), ecorePackage.getEInt(), "index", null, 1, 1, SpotVessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotVessel_VesselClass(), theFleetPackage_1.getVesselClass(), null, "vesselClass", null, 1, 1, SpotVessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
	}

} //FleetPackageImpl
