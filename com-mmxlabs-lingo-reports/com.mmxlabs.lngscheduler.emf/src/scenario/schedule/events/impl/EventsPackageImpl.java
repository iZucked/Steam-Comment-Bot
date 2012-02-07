/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.events.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
import scenario.schedule.events.CharterOutVisit;
import scenario.schedule.events.EventsFactory;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelPurpose;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.FuelUnit;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;
import scenario.schedule.fleetallocation.FleetallocationPackage;
import scenario.schedule.fleetallocation.impl.FleetallocationPackageImpl;
import scenario.schedule.impl.SchedulePackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class EventsPackageImpl extends EPackageImpl implements EventsPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass fuelMixtureEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass fuelQuantityEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass scheduledEventEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass idleEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass journeyEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass portVisitEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass slotVisitEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass charterOutVisitEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass vesselEventVisitEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum fuelUnitEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum fuelPurposeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum fuelTypeEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also performs initialization of the package, or returns the registered package, if one
	 * already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see scenario.schedule.events.EventsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EventsPackageImpl() {
		super(eNS_URI, EventsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>
	 * This method is used to initialize {@link EventsPackage#eINSTANCE} when that field is accessed. Clients should not invoke it directly. Instead, they should simply access that field to obtain the
	 * package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EventsPackage init() {
		if (isInited) {
			return (EventsPackage) EPackage.Registry.INSTANCE.getEPackage(EventsPackage.eNS_URI);
		}

		// Obtain or create and register package
		final EventsPackageImpl theEventsPackage = (EventsPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EventsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
				: new EventsPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		final ScenarioPackageImpl theScenarioPackage = (ScenarioPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ScenarioPackage.eNS_URI) instanceof ScenarioPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(ScenarioPackage.eNS_URI) : ScenarioPackage.eINSTANCE);
		final FleetPackageImpl theFleetPackage = (FleetPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI) instanceof FleetPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(FleetPackage.eNS_URI) : FleetPackage.eINSTANCE);
		final SchedulePackageImpl theSchedulePackage = (SchedulePackageImpl) (EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI) instanceof SchedulePackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(SchedulePackage.eNS_URI) : SchedulePackage.eINSTANCE);
		final FleetallocationPackageImpl theFleetallocationPackage = (FleetallocationPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(FleetallocationPackage.eNS_URI) instanceof FleetallocationPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(FleetallocationPackage.eNS_URI) : FleetallocationPackage.eINSTANCE);
		final PortPackageImpl thePortPackage = (PortPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI) instanceof PortPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(PortPackage.eNS_URI) : PortPackage.eINSTANCE);
		final CargoPackageImpl theCargoPackage = (CargoPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI) instanceof CargoPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(CargoPackage.eNS_URI) : CargoPackage.eINSTANCE);
		final ContractPackageImpl theContractPackage = (ContractPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ContractPackage.eNS_URI) instanceof ContractPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(ContractPackage.eNS_URI) : ContractPackage.eINSTANCE);
		final MarketPackageImpl theMarketPackage = (MarketPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(MarketPackage.eNS_URI) instanceof MarketPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(MarketPackage.eNS_URI) : MarketPackage.eINSTANCE);
		final OptimiserPackageImpl theOptimiserPackage = (OptimiserPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI) instanceof OptimiserPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(OptimiserPackage.eNS_URI) : OptimiserPackage.eINSTANCE);
		final LsoPackageImpl theLsoPackage = (LsoPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI) instanceof LsoPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(LsoPackage.eNS_URI) : LsoPackage.eINSTANCE);

		// Create package meta-data objects
		theEventsPackage.createPackageContents();
		theScenarioPackage.createPackageContents();
		theFleetPackage.createPackageContents();
		theSchedulePackage.createPackageContents();
		theFleetallocationPackage.createPackageContents();
		thePortPackage.createPackageContents();
		theCargoPackage.createPackageContents();
		theContractPackage.createPackageContents();
		theMarketPackage.createPackageContents();
		theOptimiserPackage.createPackageContents();
		theLsoPackage.createPackageContents();

		// Initialize created meta-data
		theEventsPackage.initializePackageContents();
		theScenarioPackage.initializePackageContents();
		theFleetPackage.initializePackageContents();
		theSchedulePackage.initializePackageContents();
		theFleetallocationPackage.initializePackageContents();
		thePortPackage.initializePackageContents();
		theCargoPackage.initializePackageContents();
		theContractPackage.initializePackageContents();
		theMarketPackage.initializePackageContents();
		theOptimiserPackage.initializePackageContents();
		theLsoPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEventsPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EventsPackage.eNS_URI, theEventsPackage);
		return theEventsPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getFuelMixture() {
		return fuelMixtureEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getFuelMixture_FuelUsage() {
		return (EReference) fuelMixtureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getFuelMixture__GetTotalFuelCost() {
		return fuelMixtureEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getFuelQuantity() {
		return fuelQuantityEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getFuelQuantity_FuelType() {
		return (EAttribute) fuelQuantityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getFuelQuantity_Quantity() {
		return (EAttribute) fuelQuantityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getFuelQuantity_UnitPrice() {
		return (EAttribute) fuelQuantityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getFuelQuantity_TotalPrice() {
		return (EAttribute) fuelQuantityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getFuelQuantity_FuelUnit() {
		return (EAttribute) fuelQuantityEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getFuelQuantity_Purpose() {
		return (EAttribute) fuelQuantityEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getScheduledEvent() {
		return scheduledEventEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getScheduledEvent_StartTime() {
		return (EAttribute) scheduledEventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getScheduledEvent_EndTime() {
		return (EAttribute) scheduledEventEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getScheduledEvent__GetEventDuration() {
		return scheduledEventEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getScheduledEvent__GetHireCost() {
		return scheduledEventEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getScheduledEvent__GetLocalStartTime() {
		return scheduledEventEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getScheduledEvent__GetLocalEndTime() {
		return scheduledEventEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getScheduledEvent__GetName() {
		return scheduledEventEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getScheduledEvent__GetDisplayTypeName() {
		return scheduledEventEClass.getEOperations().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getIdle() {
		return idleEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getIdle_VesselState() {
		return (EAttribute) idleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getIdle__GetTotalCost() {
		return idleEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getJourney() {
		return journeyEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getJourney_ToPort() {
		return (EReference) journeyEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getJourney_VesselState() {
		return (EAttribute) journeyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getJourney_Route() {
		return (EAttribute) journeyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getJourney_Speed() {
		return (EAttribute) journeyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getJourney_Distance() {
		return (EAttribute) journeyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getJourney_FromPort() {
		return (EReference) journeyEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getJourney_RouteCost() {
		return (EAttribute) journeyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getJourney__GetTotalCost() {
		return journeyEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getJourney__GetLocalStartTime() {
		return journeyEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getJourney__GetLocalEndTime() {
		return journeyEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getPortVisit() {
		return portVisitEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getPortVisit_Port() {
		return (EReference) portVisitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getPortVisit__GetLocalStartTime() {
		return portVisitEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getPortVisit__GetLocalEndTime() {
		return portVisitEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getPortVisit__GetId() {
		return portVisitEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getPortVisit__GetDisplayTypeName() {
		return portVisitEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getSlotVisit() {
		return slotVisitEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getSlotVisit_Slot() {
		return (EReference) slotVisitEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getSlotVisit_CargoAllocation() {
		return (EReference) slotVisitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getSlotVisit__GetId() {
		return slotVisitEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getSlotVisit__GetDisplayTypeName() {
		return slotVisitEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getSlotVisit__GetName() {
		return slotVisitEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getCharterOutVisit() {
		return charterOutVisitEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getCharterOutVisit_CharterOut() {
		return (EReference) charterOutVisitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getVesselEventVisit() {
		return vesselEventVisitEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getVesselEventVisit_VesselEvent() {
		return (EReference) vesselEventVisitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getVesselEventVisit_Revenue() {
		return (EReference) vesselEventVisitEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getVesselEventVisit__GetId() {
		return vesselEventVisitEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getVesselEventVisit__GetName() {
		return vesselEventVisitEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EOperation getVesselEventVisit__GetDisplayTypeName() {
		return vesselEventVisitEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EEnum getFuelUnit() {
		return fuelUnitEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EEnum getFuelPurpose() {
		return fuelPurposeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EEnum getFuelType() {
		return fuelTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EventsFactory getEventsFactory() {
		return (EventsFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to have no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) {
			return;
		}
		isCreated = true;

		// Create classes and their features
		fuelMixtureEClass = createEClass(FUEL_MIXTURE);
		createEReference(fuelMixtureEClass, FUEL_MIXTURE__FUEL_USAGE);
		createEOperation(fuelMixtureEClass, FUEL_MIXTURE___GET_TOTAL_FUEL_COST);

		fuelQuantityEClass = createEClass(FUEL_QUANTITY);
		createEAttribute(fuelQuantityEClass, FUEL_QUANTITY__FUEL_TYPE);
		createEAttribute(fuelQuantityEClass, FUEL_QUANTITY__QUANTITY);
		createEAttribute(fuelQuantityEClass, FUEL_QUANTITY__UNIT_PRICE);
		createEAttribute(fuelQuantityEClass, FUEL_QUANTITY__TOTAL_PRICE);
		createEAttribute(fuelQuantityEClass, FUEL_QUANTITY__FUEL_UNIT);
		createEAttribute(fuelQuantityEClass, FUEL_QUANTITY__PURPOSE);

		scheduledEventEClass = createEClass(SCHEDULED_EVENT);
		createEAttribute(scheduledEventEClass, SCHEDULED_EVENT__START_TIME);
		createEAttribute(scheduledEventEClass, SCHEDULED_EVENT__END_TIME);
		createEOperation(scheduledEventEClass, SCHEDULED_EVENT___GET_EVENT_DURATION);
		createEOperation(scheduledEventEClass, SCHEDULED_EVENT___GET_HIRE_COST);
		createEOperation(scheduledEventEClass, SCHEDULED_EVENT___GET_LOCAL_START_TIME);
		createEOperation(scheduledEventEClass, SCHEDULED_EVENT___GET_LOCAL_END_TIME);
		createEOperation(scheduledEventEClass, SCHEDULED_EVENT___GET_NAME);
		createEOperation(scheduledEventEClass, SCHEDULED_EVENT___GET_DISPLAY_TYPE_NAME);

		journeyEClass = createEClass(JOURNEY);
		createEAttribute(journeyEClass, JOURNEY__VESSEL_STATE);
		createEAttribute(journeyEClass, JOURNEY__ROUTE);
		createEAttribute(journeyEClass, JOURNEY__SPEED);
		createEAttribute(journeyEClass, JOURNEY__DISTANCE);
		createEAttribute(journeyEClass, JOURNEY__ROUTE_COST);
		createEReference(journeyEClass, JOURNEY__TO_PORT);
		createEReference(journeyEClass, JOURNEY__FROM_PORT);
		createEOperation(journeyEClass, JOURNEY___GET_TOTAL_COST);
		createEOperation(journeyEClass, JOURNEY___GET_LOCAL_START_TIME);
		createEOperation(journeyEClass, JOURNEY___GET_LOCAL_END_TIME);

		portVisitEClass = createEClass(PORT_VISIT);
		createEReference(portVisitEClass, PORT_VISIT__PORT);
		createEOperation(portVisitEClass, PORT_VISIT___GET_LOCAL_START_TIME);
		createEOperation(portVisitEClass, PORT_VISIT___GET_LOCAL_END_TIME);
		createEOperation(portVisitEClass, PORT_VISIT___GET_ID);
		createEOperation(portVisitEClass, PORT_VISIT___GET_DISPLAY_TYPE_NAME);

		idleEClass = createEClass(IDLE);
		createEAttribute(idleEClass, IDLE__VESSEL_STATE);
		createEOperation(idleEClass, IDLE___GET_TOTAL_COST);

		slotVisitEClass = createEClass(SLOT_VISIT);
		createEReference(slotVisitEClass, SLOT_VISIT__CARGO_ALLOCATION);
		createEReference(slotVisitEClass, SLOT_VISIT__SLOT);
		createEOperation(slotVisitEClass, SLOT_VISIT___GET_ID);
		createEOperation(slotVisitEClass, SLOT_VISIT___GET_DISPLAY_TYPE_NAME);
		createEOperation(slotVisitEClass, SLOT_VISIT___GET_NAME);

		vesselEventVisitEClass = createEClass(VESSEL_EVENT_VISIT);
		createEReference(vesselEventVisitEClass, VESSEL_EVENT_VISIT__VESSEL_EVENT);
		createEReference(vesselEventVisitEClass, VESSEL_EVENT_VISIT__REVENUE);
		createEOperation(vesselEventVisitEClass, VESSEL_EVENT_VISIT___GET_ID);
		createEOperation(vesselEventVisitEClass, VESSEL_EVENT_VISIT___GET_NAME);
		createEOperation(vesselEventVisitEClass, VESSEL_EVENT_VISIT___GET_DISPLAY_TYPE_NAME);

		charterOutVisitEClass = createEClass(CHARTER_OUT_VISIT);
		createEReference(charterOutVisitEClass, CHARTER_OUT_VISIT__CHARTER_OUT);

		// Create enums
		fuelUnitEEnum = createEEnum(FUEL_UNIT);
		fuelPurposeEEnum = createEEnum(FUEL_PURPOSE);
		fuelTypeEEnum = createEEnum(FUEL_TYPE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This method is guarded to have no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) {
			return;
		}
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		final ScenarioPackage theScenarioPackage = (ScenarioPackage) EPackage.Registry.INSTANCE.getEPackage(ScenarioPackage.eNS_URI);
		final FleetPackage theFleetPackage = (FleetPackage) EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);
		final PortPackage thePortPackage = (PortPackage) EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);
		final SchedulePackage theSchedulePackage = (SchedulePackage) EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI);
		final CargoPackage theCargoPackage = (CargoPackage) EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		scheduledEventEClass.getESuperTypes().add(theScenarioPackage.getScenarioObject());
		journeyEClass.getESuperTypes().add(this.getScheduledEvent());
		journeyEClass.getESuperTypes().add(this.getFuelMixture());
		portVisitEClass.getESuperTypes().add(this.getScheduledEvent());
		idleEClass.getESuperTypes().add(this.getPortVisit());
		idleEClass.getESuperTypes().add(this.getFuelMixture());
		slotVisitEClass.getESuperTypes().add(this.getPortVisit());
		vesselEventVisitEClass.getESuperTypes().add(this.getPortVisit());
		charterOutVisitEClass.getESuperTypes().add(this.getVesselEventVisit());

		// Initialize classes, features, and operations; add parameters
		initEClass(fuelMixtureEClass, FuelMixture.class, "FuelMixture", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFuelMixture_FuelUsage(), this.getFuelQuantity(), null, "fuelUsage", null, 0, -1, FuelMixture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getFuelMixture__GetTotalFuelCost(), ecorePackage.getELong(), "getTotalFuelCost", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(fuelQuantityEClass, FuelQuantity.class, "FuelQuantity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFuelQuantity_FuelType(), this.getFuelType(), "fuelType", null, 1, 1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelQuantity_Quantity(), ecorePackage.getELong(), "quantity", null, 1, 1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelQuantity_UnitPrice(), ecorePackage.getELong(), "unitPrice", null, 1, 1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelQuantity_TotalPrice(), ecorePackage.getELong(), "totalPrice", null, 1, 1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelQuantity_FuelUnit(), this.getFuelUnit(), "fuelUnit", null, 1, 1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelQuantity_Purpose(), this.getFuelPurpose(), "purpose", null, 1, 1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(scheduledEventEClass, ScheduledEvent.class, "ScheduledEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getScheduledEvent_StartTime(), ecorePackage.getEDate(), "startTime", null, 1, 1, ScheduledEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScheduledEvent_EndTime(), ecorePackage.getEDate(), "endTime", null, 1, 1, ScheduledEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getScheduledEvent__GetEventDuration(), ecorePackage.getEInt(), "getEventDuration", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getScheduledEvent__GetHireCost(), ecorePackage.getELong(), "getHireCost", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getScheduledEvent__GetLocalStartTime(), ecorePackage.getEJavaObject(), "getLocalStartTime", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getScheduledEvent__GetLocalEndTime(), ecorePackage.getEJavaObject(), "getLocalEndTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getScheduledEvent__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getScheduledEvent__GetDisplayTypeName(), ecorePackage.getEString(), "getDisplayTypeName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(journeyEClass, Journey.class, "Journey", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getJourney_VesselState(), theFleetPackage.getVesselState(), "vesselState", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJourney_Route(), ecorePackage.getEString(), "route", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getJourney_Speed(), ecorePackage.getEDouble(), "speed", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getJourney_Distance(), ecorePackage.getEInt(), "distance", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getJourney_RouteCost(), ecorePackage.getELong(), "routeCost", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getJourney_ToPort(), thePortPackage.getPort(), null, "toPort", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJourney_FromPort(), thePortPackage.getPort(), null, "fromPort", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getJourney__GetTotalCost(), ecorePackage.getELong(), "getTotalCost", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getJourney__GetLocalStartTime(), ecorePackage.getEJavaObject(), "getLocalStartTime", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getJourney__GetLocalEndTime(), ecorePackage.getEJavaObject(), "getLocalEndTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(portVisitEClass, PortVisit.class, "PortVisit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPortVisit_Port(), thePortPackage.getPort(), null, "port", null, 1, 1, PortVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getPortVisit__GetLocalStartTime(), ecorePackage.getEJavaObject(), "getLocalStartTime", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getPortVisit__GetLocalEndTime(), ecorePackage.getEJavaObject(), "getLocalEndTime", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getPortVisit__GetId(), ecorePackage.getEString(), "getId", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getPortVisit__GetDisplayTypeName(), ecorePackage.getEString(), "getDisplayTypeName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(idleEClass, Idle.class, "Idle", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIdle_VesselState(), theFleetPackage.getVesselState(), "vesselState", null, 1, 1, Idle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEOperation(getIdle__GetTotalCost(), ecorePackage.getELong(), "getTotalCost", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(slotVisitEClass, SlotVisit.class, "SlotVisit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlotVisit_CargoAllocation(), theSchedulePackage.getCargoAllocation(), null, "cargoAllocation", null, 1, 1, SlotVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotVisit_Slot(), theCargoPackage.getSlot(), null, "slot", null, 1, 1, SlotVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getSlotVisit__GetId(), ecorePackage.getEString(), "getId", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlotVisit__GetDisplayTypeName(), ecorePackage.getEString(), "getDisplayTypeName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlotVisit__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(vesselEventVisitEClass, VesselEventVisit.class, "VesselEventVisit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselEventVisit_VesselEvent(), theFleetPackage.getVesselEvent(), null, "vesselEvent", null, 1, 1, VesselEventVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselEventVisit_Revenue(), theSchedulePackage.getVesselEventRevenue(), null, "revenue", null, 1, 1, VesselEventVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getVesselEventVisit__GetId(), ecorePackage.getEString(), "getId", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselEventVisit__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getVesselEventVisit__GetDisplayTypeName(), ecorePackage.getEString(), "getDisplayTypeName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(charterOutVisitEClass, CharterOutVisit.class, "CharterOutVisit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCharterOutVisit_CharterOut(), theFleetPackage.getCharterOut(), null, "charterOut", null, 1, 1, CharterOutVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(fuelUnitEEnum, FuelUnit.class, "FuelUnit");
		addEEnumLiteral(fuelUnitEEnum, FuelUnit.MT);
		addEEnumLiteral(fuelUnitEEnum, FuelUnit.M3);
		addEEnumLiteral(fuelUnitEEnum, FuelUnit.MMB_TU);

		initEEnum(fuelPurposeEEnum, FuelPurpose.class, "FuelPurpose");
		addEEnumLiteral(fuelPurposeEEnum, FuelPurpose.TRAVEL);
		addEEnumLiteral(fuelPurposeEEnum, FuelPurpose.PILOT_LIGHT);
		addEEnumLiteral(fuelPurposeEEnum, FuelPurpose.IDLE);
		addEEnumLiteral(fuelPurposeEEnum, FuelPurpose.COOLDOWN);

		initEEnum(fuelTypeEEnum, FuelType.class, "FuelType");
		addEEnumLiteral(fuelTypeEEnum, FuelType.FBO);
		addEEnumLiteral(fuelTypeEEnum, FuelType.NBO);
		addEEnumLiteral(fuelTypeEEnum, FuelType.BASE_FUEL);
		addEEnumLiteral(fuelTypeEEnum, FuelType.COOLDOWN);
	}

} // EventsPackageImpl
