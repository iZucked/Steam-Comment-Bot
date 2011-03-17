/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.contract.impl;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import scenario.ScenarioPackage;
import scenario.cargo.CargoPackage;
import scenario.cargo.impl.CargoPackageImpl;
import scenario.contract.ContractFactory;
import scenario.contract.ContractModel;
import scenario.contract.ContractPackage;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;
import scenario.contract.TotalVolumeLimit;
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
import scenario.schedule.fleetallocation.FleetallocationPackage;
import scenario.schedule.fleetallocation.impl.FleetallocationPackageImpl;
import scenario.schedule.impl.SchedulePackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ContractPackageImpl extends EPackageImpl implements ContractPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass contractModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass purchaseContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass salesContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass totalVolumeLimitEClass = null;

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
	 * @see scenario.contract.ContractPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ContractPackageImpl() {
		super(eNS_URI, ContractFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ContractPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ContractPackage init() {
		if (isInited) return (ContractPackage)EPackage.Registry.INSTANCE.getEPackage(ContractPackage.eNS_URI);

		// Obtain or create and register package
		ContractPackageImpl theContractPackage = (ContractPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ContractPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ContractPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ScenarioPackageImpl theScenarioPackage = (ScenarioPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ScenarioPackage.eNS_URI) instanceof ScenarioPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ScenarioPackage.eNS_URI) : ScenarioPackage.eINSTANCE);
		FleetPackageImpl theFleetPackage = (FleetPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI) instanceof FleetPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI) : FleetPackage.eINSTANCE);
		SchedulePackageImpl theSchedulePackage = (SchedulePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI) instanceof SchedulePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI) : SchedulePackage.eINSTANCE);
		EventsPackageImpl theEventsPackage = (EventsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(EventsPackage.eNS_URI) instanceof EventsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(EventsPackage.eNS_URI) : EventsPackage.eINSTANCE);
		FleetallocationPackageImpl theFleetallocationPackage = (FleetallocationPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(FleetallocationPackage.eNS_URI) instanceof FleetallocationPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(FleetallocationPackage.eNS_URI) : FleetallocationPackage.eINSTANCE);
		PortPackageImpl thePortPackage = (PortPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI) instanceof PortPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI) : PortPackage.eINSTANCE);
		CargoPackageImpl theCargoPackage = (CargoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI) instanceof CargoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI) : CargoPackage.eINSTANCE);
		MarketPackageImpl theMarketPackage = (MarketPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(MarketPackage.eNS_URI) instanceof MarketPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MarketPackage.eNS_URI) : MarketPackage.eINSTANCE);
		OptimiserPackageImpl theOptimiserPackage = (OptimiserPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI) instanceof OptimiserPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI) : OptimiserPackage.eINSTANCE);
		LsoPackageImpl theLsoPackage = (LsoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI) instanceof LsoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI) : LsoPackage.eINSTANCE);

		// Create package meta-data objects
		theContractPackage.createPackageContents();
		theScenarioPackage.createPackageContents();
		theFleetPackage.createPackageContents();
		theSchedulePackage.createPackageContents();
		theEventsPackage.createPackageContents();
		theFleetallocationPackage.createPackageContents();
		thePortPackage.createPackageContents();
		theCargoPackage.createPackageContents();
		theMarketPackage.createPackageContents();
		theOptimiserPackage.createPackageContents();
		theLsoPackage.createPackageContents();

		// Initialize created meta-data
		theContractPackage.initializePackageContents();
		theScenarioPackage.initializePackageContents();
		theFleetPackage.initializePackageContents();
		theSchedulePackage.initializePackageContents();
		theEventsPackage.initializePackageContents();
		theFleetallocationPackage.initializePackageContents();
		thePortPackage.initializePackageContents();
		theCargoPackage.initializePackageContents();
		theMarketPackage.initializePackageContents();
		theOptimiserPackage.initializePackageContents();
		theLsoPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theContractPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ContractPackage.eNS_URI, theContractPackage);
		return theContractPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getContractModel() {
		return contractModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContractModel_PurchaseContracts() {
		return (EReference)contractModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContractModel_SalesContracts() {
		return (EReference)contractModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContractModel_VolumeConstraints() {
		return (EReference)contractModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPurchaseContract() {
		return purchaseContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSalesContract() {
		return salesContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTotalVolumeLimit() {
		return totalVolumeLimitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTotalVolumeLimit_Ports() {
		return (EReference)totalVolumeLimitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTotalVolumeLimit_MaximumVolume() {
		return (EAttribute)totalVolumeLimitEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTotalVolumeLimit_StartDate() {
		return (EAttribute)totalVolumeLimitEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTotalVolumeLimit_Duration() {
		return (EAttribute)totalVolumeLimitEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTotalVolumeLimit_Repeating() {
		return (EAttribute)totalVolumeLimitEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContractFactory getContractFactory() {
		return (ContractFactory)getEFactoryInstance();
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
		contractModelEClass = createEClass(CONTRACT_MODEL);
		createEReference(contractModelEClass, CONTRACT_MODEL__PURCHASE_CONTRACTS);
		createEReference(contractModelEClass, CONTRACT_MODEL__SALES_CONTRACTS);
		createEReference(contractModelEClass, CONTRACT_MODEL__VOLUME_CONSTRAINTS);

		purchaseContractEClass = createEClass(PURCHASE_CONTRACT);

		salesContractEClass = createEClass(SALES_CONTRACT);

		totalVolumeLimitEClass = createEClass(TOTAL_VOLUME_LIMIT);
		createEReference(totalVolumeLimitEClass, TOTAL_VOLUME_LIMIT__PORTS);
		createEAttribute(totalVolumeLimitEClass, TOTAL_VOLUME_LIMIT__MAXIMUM_VOLUME);
		createEAttribute(totalVolumeLimitEClass, TOTAL_VOLUME_LIMIT__START_DATE);
		createEAttribute(totalVolumeLimitEClass, TOTAL_VOLUME_LIMIT__DURATION);
		createEAttribute(totalVolumeLimitEClass, TOTAL_VOLUME_LIMIT__REPEATING);
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
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(contractModelEClass, ContractModel.class, "ContractModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContractModel_PurchaseContracts(), this.getPurchaseContract(), null, "purchaseContracts", null, 0, -1, ContractModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContractModel_SalesContracts(), this.getSalesContract(), null, "salesContracts", null, 0, -1, ContractModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContractModel_VolumeConstraints(), this.getTotalVolumeLimit(), null, "volumeConstraints", null, 0, -1, ContractModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(purchaseContractEClass, PurchaseContract.class, "PurchaseContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(salesContractEClass, SalesContract.class, "SalesContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(totalVolumeLimitEClass, TotalVolumeLimit.class, "TotalVolumeLimit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTotalVolumeLimit_Ports(), thePortPackage.getPort(), null, "ports", null, 0, -1, TotalVolumeLimit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTotalVolumeLimit_MaximumVolume(), ecorePackage.getELong(), "maximumVolume", null, 0, 1, TotalVolumeLimit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTotalVolumeLimit_StartDate(), ecorePackage.getEDate(), "startDate", null, 1, 1, TotalVolumeLimit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTotalVolumeLimit_Duration(), ecorePackage.getEInt(), "duration", null, 1, 1, TotalVolumeLimit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTotalVolumeLimit_Repeating(), ecorePackage.getEBoolean(), "repeating", null, 1, 1, TotalVolumeLimit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
	}

} //ContractPackageImpl