/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.market.impl;

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
import scenario.market.Index;
import scenario.market.MarketFactory;
import scenario.market.MarketModel;
import scenario.market.MarketPackage;
import scenario.market.StepwisePrice;
import scenario.market.StepwisePriceCurve;
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
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class MarketPackageImpl extends EPackageImpl implements MarketPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indexEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass marketModelEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stepwisePriceCurveEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stepwisePriceEClass = null;

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
	 * @see scenario.market.MarketPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MarketPackageImpl() {
		super(eNS_URI, MarketFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link MarketPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MarketPackage init() {
		if (isInited) return (MarketPackage)EPackage.Registry.INSTANCE.getEPackage(MarketPackage.eNS_URI);

		// Obtain or create and register package
		MarketPackageImpl theMarketPackage = (MarketPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MarketPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MarketPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ScenarioPackageImpl theScenarioPackage = (ScenarioPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ScenarioPackage.eNS_URI) instanceof ScenarioPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ScenarioPackage.eNS_URI) : ScenarioPackage.eINSTANCE);
		FleetPackageImpl theFleetPackage = (FleetPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI) instanceof FleetPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI) : FleetPackage.eINSTANCE);
		SchedulePackageImpl theSchedulePackage = (SchedulePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI) instanceof SchedulePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI) : SchedulePackage.eINSTANCE);
		EventsPackageImpl theEventsPackage = (EventsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(EventsPackage.eNS_URI) instanceof EventsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(EventsPackage.eNS_URI) : EventsPackage.eINSTANCE);
		FleetallocationPackageImpl theFleetallocationPackage = (FleetallocationPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(FleetallocationPackage.eNS_URI) instanceof FleetallocationPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(FleetallocationPackage.eNS_URI) : FleetallocationPackage.eINSTANCE);
		PortPackageImpl thePortPackage = (PortPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI) instanceof PortPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI) : PortPackage.eINSTANCE);
		CargoPackageImpl theCargoPackage = (CargoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI) instanceof CargoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI) : CargoPackage.eINSTANCE);
		ContractPackageImpl theContractPackage = (ContractPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ContractPackage.eNS_URI) instanceof ContractPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ContractPackage.eNS_URI) : ContractPackage.eINSTANCE);
		OptimiserPackageImpl theOptimiserPackage = (OptimiserPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI) instanceof OptimiserPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI) : OptimiserPackage.eINSTANCE);
		LsoPackageImpl theLsoPackage = (LsoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI) instanceof LsoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI) : LsoPackage.eINSTANCE);

		// Create package meta-data objects
		theMarketPackage.createPackageContents();
		theScenarioPackage.createPackageContents();
		theFleetPackage.createPackageContents();
		theSchedulePackage.createPackageContents();
		theEventsPackage.createPackageContents();
		theFleetallocationPackage.createPackageContents();
		thePortPackage.createPackageContents();
		theCargoPackage.createPackageContents();
		theContractPackage.createPackageContents();
		theOptimiserPackage.createPackageContents();
		theLsoPackage.createPackageContents();

		// Initialize created meta-data
		theMarketPackage.initializePackageContents();
		theScenarioPackage.initializePackageContents();
		theFleetPackage.initializePackageContents();
		theSchedulePackage.initializePackageContents();
		theEventsPackage.initializePackageContents();
		theFleetallocationPackage.initializePackageContents();
		thePortPackage.initializePackageContents();
		theCargoPackage.initializePackageContents();
		theContractPackage.initializePackageContents();
		theOptimiserPackage.initializePackageContents();
		theLsoPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMarketPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MarketPackage.eNS_URI, theMarketPackage);
		return theMarketPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIndex() {
		return indexEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getIndex_PriceCurve() {
		return (EReference)indexEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMarketModel() {
		return marketModelEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMarketModel_Indices() {
		return (EReference)marketModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getStepwisePriceCurve() {
		return stepwisePriceCurveEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getStepwisePriceCurve_Prices() {
		return (EReference)stepwisePriceCurveEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStepwisePriceCurve_DefaultValue() {
		return (EAttribute)stepwisePriceCurveEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getStepwisePriceCurve__GetValueAtDate__Date() {
		return stepwisePriceCurveEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getStepwisePrice() {
		return stepwisePriceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStepwisePrice_Date() {
		return (EAttribute)stepwisePriceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStepwisePrice_PriceFromDate() {
		return (EAttribute)stepwisePriceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MarketFactory getMarketFactory() {
		return (MarketFactory)getEFactoryInstance();
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
		indexEClass = createEClass(INDEX);
		createEReference(indexEClass, INDEX__PRICE_CURVE);

		marketModelEClass = createEClass(MARKET_MODEL);
		createEReference(marketModelEClass, MARKET_MODEL__INDICES);

		stepwisePriceCurveEClass = createEClass(STEPWISE_PRICE_CURVE);
		createEAttribute(stepwisePriceCurveEClass, STEPWISE_PRICE_CURVE__DEFAULT_VALUE);
		createEReference(stepwisePriceCurveEClass, STEPWISE_PRICE_CURVE__PRICES);
		createEOperation(stepwisePriceCurveEClass, STEPWISE_PRICE_CURVE___GET_VALUE_AT_DATE__DATE);

		stepwisePriceEClass = createEClass(STEPWISE_PRICE);
		createEAttribute(stepwisePriceEClass, STEPWISE_PRICE__DATE);
		createEAttribute(stepwisePriceEClass, STEPWISE_PRICE__PRICE_FROM_DATE);
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
		ScenarioPackage theScenarioPackage = (ScenarioPackage)EPackage.Registry.INSTANCE.getEPackage(ScenarioPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		indexEClass.getESuperTypes().add(theScenarioPackage.getNamedObject());

		// Initialize classes, features, and operations; add parameters
		initEClass(indexEClass, Index.class, "Index", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIndex_PriceCurve(), this.getStepwisePriceCurve(), null, "priceCurve", null, 1, 1, Index.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(marketModelEClass, MarketModel.class, "MarketModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMarketModel_Indices(), this.getIndex(), null, "indices", null, 0, -1, MarketModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(stepwisePriceCurveEClass, StepwisePriceCurve.class, "StepwisePriceCurve", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStepwisePriceCurve_DefaultValue(), ecorePackage.getEFloat(), "defaultValue", null, 0, 1, StepwisePriceCurve.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStepwisePriceCurve_Prices(), this.getStepwisePrice(), null, "prices", null, 0, -1, StepwisePriceCurve.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = initEOperation(getStepwisePriceCurve__GetValueAtDate__Date(), ecorePackage.getEFloat(), "getValueAtDate", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEDate(), "date", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(stepwisePriceEClass, StepwisePrice.class, "StepwisePrice", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStepwisePrice_Date(), ecorePackage.getEDate(), "date", null, 1, 1, StepwisePrice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStepwisePrice_PriceFromDate(), ecorePackage.getEFloat(), "priceFromDate", null, 1, 1, StepwisePrice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
	}

} // MarketPackageImpl
