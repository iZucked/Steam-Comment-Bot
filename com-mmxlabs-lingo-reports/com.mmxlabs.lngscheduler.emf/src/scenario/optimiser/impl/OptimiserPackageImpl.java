/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.optimiser.impl;


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
import scenario.fleet.FleetPackage;
import scenario.fleet.impl.FleetPackageImpl;
import scenario.impl.ScenarioPackageImpl;
import scenario.market.MarketPackage;
import scenario.market.impl.MarketPackageImpl;
import scenario.optimiser.Constraint;
import scenario.optimiser.Objective;
import scenario.optimiser.Optimisation;
import scenario.optimiser.OptimisationSettings;
import scenario.optimiser.OptimiserFactory;
import scenario.optimiser.OptimiserPackage;
import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.impl.LsoPackageImpl;
import scenario.port.PortPackage;
import scenario.port.impl.PortPackageImpl;
import scenario.schedule.SchedulePackage;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.impl.EventsPackageImpl;
import scenario.schedule.impl.SchedulePackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class OptimiserPackageImpl extends EPackageImpl implements OptimiserPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass optimisationSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass optimisationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass objectiveEClass = null;

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
	 * @see scenario.optimiser.OptimiserPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private OptimiserPackageImpl() {
		super(eNS_URI, OptimiserFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link OptimiserPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static OptimiserPackage init() {
		if (isInited) return (OptimiserPackage)EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI);

		// Obtain or create and register package
		OptimiserPackageImpl theOptimiserPackage = (OptimiserPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof OptimiserPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new OptimiserPackageImpl());

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
		LsoPackageImpl theLsoPackage = (LsoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI) instanceof LsoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI) : LsoPackage.eINSTANCE);

		// Create package meta-data objects
		theOptimiserPackage.createPackageContents();
		theScenarioPackage.createPackageContents();
		theFleetPackage.createPackageContents();
		theSchedulePackage.createPackageContents();
		theEventsPackage.createPackageContents();
		thePortPackage.createPackageContents();
		theCargoPackage.createPackageContents();
		theContractPackage.createPackageContents();
		theMarketPackage.createPackageContents();
		theLsoPackage.createPackageContents();

		// Initialize created meta-data
		theOptimiserPackage.initializePackageContents();
		theScenarioPackage.initializePackageContents();
		theFleetPackage.initializePackageContents();
		theSchedulePackage.initializePackageContents();
		theEventsPackage.initializePackageContents();
		thePortPackage.initializePackageContents();
		theCargoPackage.initializePackageContents();
		theContractPackage.initializePackageContents();
		theMarketPackage.initializePackageContents();
		theLsoPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theOptimiserPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(OptimiserPackage.eNS_URI, theOptimiserPackage);
		return theOptimiserPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOptimisationSettings() {
		return optimisationSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOptimisationSettings_Name() {
		return (EAttribute)optimisationSettingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOptimisationSettings_RandomSeed() {
		return (EAttribute)optimisationSettingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptimisationSettings_Constraints() {
		return (EReference)optimisationSettingsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptimisationSettings_Objectives() {
		return (EReference)optimisationSettingsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOptimisation() {
		return optimisationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptimisation_AllSettings() {
		return (EReference)optimisationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptimisation_CurrentSettings() {
		return (EReference)optimisationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConstraint() {
		return constraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConstraint_Name() {
		return (EAttribute)constraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConstraint_Enabled() {
		return (EAttribute)constraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getObjective() {
		return objectiveEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getObjective_Name() {
		return (EAttribute)objectiveEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getObjective_Weight() {
		return (EAttribute)objectiveEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimiserFactory getOptimiserFactory() {
		return (OptimiserFactory)getEFactoryInstance();
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
		optimisationSettingsEClass = createEClass(OPTIMISATION_SETTINGS);
		createEAttribute(optimisationSettingsEClass, OPTIMISATION_SETTINGS__NAME);
		createEAttribute(optimisationSettingsEClass, OPTIMISATION_SETTINGS__RANDOM_SEED);
		createEReference(optimisationSettingsEClass, OPTIMISATION_SETTINGS__CONSTRAINTS);
		createEReference(optimisationSettingsEClass, OPTIMISATION_SETTINGS__OBJECTIVES);

		optimisationEClass = createEClass(OPTIMISATION);
		createEReference(optimisationEClass, OPTIMISATION__ALL_SETTINGS);
		createEReference(optimisationEClass, OPTIMISATION__CURRENT_SETTINGS);

		constraintEClass = createEClass(CONSTRAINT);
		createEAttribute(constraintEClass, CONSTRAINT__NAME);
		createEAttribute(constraintEClass, CONSTRAINT__ENABLED);

		objectiveEClass = createEClass(OBJECTIVE);
		createEAttribute(objectiveEClass, OBJECTIVE__NAME);
		createEAttribute(objectiveEClass, OBJECTIVE__WEIGHT);
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
		LsoPackage theLsoPackage = (LsoPackage)EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI);

		// Add subpackages
		getESubpackages().add(theLsoPackage);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(optimisationSettingsEClass, OptimisationSettings.class, "OptimisationSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOptimisationSettings_Name(), ecorePackage.getEString(), "name", null, 0, 1, OptimisationSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptimisationSettings_RandomSeed(), ecorePackage.getELong(), "randomSeed", null, 0, 1, OptimisationSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimisationSettings_Constraints(), this.getConstraint(), null, "constraints", null, 0, -1, OptimisationSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimisationSettings_Objectives(), this.getObjective(), null, "objectives", null, 0, -1, OptimisationSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(optimisationEClass, Optimisation.class, "Optimisation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOptimisation_AllSettings(), this.getOptimisationSettings(), null, "allSettings", null, 0, -1, Optimisation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimisation_CurrentSettings(), this.getOptimisationSettings(), null, "currentSettings", null, 0, 1, Optimisation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constraintEClass, Constraint.class, "Constraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConstraint_Name(), ecorePackage.getEString(), "name", null, 0, 1, Constraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConstraint_Enabled(), ecorePackage.getEBoolean(), "enabled", null, 0, 1, Constraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(objectiveEClass, Objective.class, "Objective", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getObjective_Name(), ecorePackage.getEString(), "name", null, 0, 1, Objective.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getObjective_Weight(), ecorePackage.getEDouble(), "weight", null, 0, 1, Objective.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
	}

} //OptimiserPackageImpl