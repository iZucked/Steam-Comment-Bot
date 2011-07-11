/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser.lso.impl;

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

import scenario.optimiser.OptimiserPackage;

import scenario.optimiser.impl.OptimiserPackageImpl;

import scenario.optimiser.lso.ConstrainedMoveGeneratorSettings;
import scenario.optimiser.lso.LSOSettings;
import scenario.optimiser.lso.LsoFactory;
import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.MoveGeneratorSettings;
import scenario.optimiser.lso.RandomMoveGeneratorSettings;
import scenario.optimiser.lso.ThresholderSettings;

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
public class LsoPackageImpl extends EPackageImpl implements LsoPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lsoSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass thresholderSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass moveGeneratorSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass randomMoveGeneratorSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constrainedMoveGeneratorSettingsEClass = null;

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
	 * @see scenario.optimiser.lso.LsoPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private LsoPackageImpl() {
		super(eNS_URI, LsoFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link LsoPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static LsoPackage init() {
		if (isInited) return (LsoPackage)EPackage.Registry.INSTANCE.getEPackage(LsoPackage.eNS_URI);

		// Obtain or create and register package
		LsoPackageImpl theLsoPackage = (LsoPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof LsoPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new LsoPackageImpl());

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
		MarketPackageImpl theMarketPackage = (MarketPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(MarketPackage.eNS_URI) instanceof MarketPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MarketPackage.eNS_URI) : MarketPackage.eINSTANCE);
		OptimiserPackageImpl theOptimiserPackage = (OptimiserPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI) instanceof OptimiserPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI) : OptimiserPackage.eINSTANCE);

		// Create package meta-data objects
		theLsoPackage.createPackageContents();
		theScenarioPackage.createPackageContents();
		theFleetPackage.createPackageContents();
		theSchedulePackage.createPackageContents();
		theEventsPackage.createPackageContents();
		theFleetallocationPackage.createPackageContents();
		thePortPackage.createPackageContents();
		theCargoPackage.createPackageContents();
		theContractPackage.createPackageContents();
		theMarketPackage.createPackageContents();
		theOptimiserPackage.createPackageContents();

		// Initialize created meta-data
		theLsoPackage.initializePackageContents();
		theScenarioPackage.initializePackageContents();
		theFleetPackage.initializePackageContents();
		theSchedulePackage.initializePackageContents();
		theEventsPackage.initializePackageContents();
		theFleetallocationPackage.initializePackageContents();
		thePortPackage.initializePackageContents();
		theCargoPackage.initializePackageContents();
		theContractPackage.initializePackageContents();
		theMarketPackage.initializePackageContents();
		theOptimiserPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theLsoPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(LsoPackage.eNS_URI, theLsoPackage);
		return theLsoPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLSOSettings() {
		return lsoSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLSOSettings_NumberOfSteps() {
		return (EAttribute)lsoSettingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLSOSettings_ThresholderSettings() {
		return (EReference)lsoSettingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLSOSettings_MoveGeneratorSettings() {
		return (EReference)lsoSettingsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getThresholderSettings() {
		return thresholderSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getThresholderSettings_Alpha() {
		return (EAttribute)thresholderSettingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getThresholderSettings_InitialAcceptanceRate() {
		return (EAttribute)thresholderSettingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getThresholderSettings_EpochLength() {
		return (EAttribute)thresholderSettingsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMoveGeneratorSettings() {
		return moveGeneratorSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRandomMoveGeneratorSettings() {
		return randomMoveGeneratorSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRandomMoveGeneratorSettings_Using2over2() {
		return (EAttribute)randomMoveGeneratorSettingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRandomMoveGeneratorSettings_Using3over2() {
		return (EAttribute)randomMoveGeneratorSettingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRandomMoveGeneratorSettings_Using4over1() {
		return (EAttribute)randomMoveGeneratorSettingsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRandomMoveGeneratorSettings_Using4over2() {
		return (EAttribute)randomMoveGeneratorSettingsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConstrainedMoveGeneratorSettings() {
		return constrainedMoveGeneratorSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LsoFactory getLsoFactory() {
		return (LsoFactory)getEFactoryInstance();
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
		lsoSettingsEClass = createEClass(LSO_SETTINGS);
		createEAttribute(lsoSettingsEClass, LSO_SETTINGS__NUMBER_OF_STEPS);
		createEReference(lsoSettingsEClass, LSO_SETTINGS__THRESHOLDER_SETTINGS);
		createEReference(lsoSettingsEClass, LSO_SETTINGS__MOVE_GENERATOR_SETTINGS);

		thresholderSettingsEClass = createEClass(THRESHOLDER_SETTINGS);
		createEAttribute(thresholderSettingsEClass, THRESHOLDER_SETTINGS__ALPHA);
		createEAttribute(thresholderSettingsEClass, THRESHOLDER_SETTINGS__INITIAL_ACCEPTANCE_RATE);
		createEAttribute(thresholderSettingsEClass, THRESHOLDER_SETTINGS__EPOCH_LENGTH);

		moveGeneratorSettingsEClass = createEClass(MOVE_GENERATOR_SETTINGS);

		randomMoveGeneratorSettingsEClass = createEClass(RANDOM_MOVE_GENERATOR_SETTINGS);
		createEAttribute(randomMoveGeneratorSettingsEClass, RANDOM_MOVE_GENERATOR_SETTINGS__USING2OVER2);
		createEAttribute(randomMoveGeneratorSettingsEClass, RANDOM_MOVE_GENERATOR_SETTINGS__USING3OVER2);
		createEAttribute(randomMoveGeneratorSettingsEClass, RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER1);
		createEAttribute(randomMoveGeneratorSettingsEClass, RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER2);

		constrainedMoveGeneratorSettingsEClass = createEClass(CONSTRAINED_MOVE_GENERATOR_SETTINGS);
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
		OptimiserPackage theOptimiserPackage = (OptimiserPackage)EPackage.Registry.INSTANCE.getEPackage(OptimiserPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		lsoSettingsEClass.getESuperTypes().add(theOptimiserPackage.getOptimisationSettings());
		randomMoveGeneratorSettingsEClass.getESuperTypes().add(this.getMoveGeneratorSettings());
		constrainedMoveGeneratorSettingsEClass.getESuperTypes().add(this.getMoveGeneratorSettings());

		// Initialize classes, features, and operations; add parameters
		initEClass(lsoSettingsEClass, LSOSettings.class, "LSOSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLSOSettings_NumberOfSteps(), ecorePackage.getEInt(), "numberOfSteps", null, 0, 1, LSOSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLSOSettings_ThresholderSettings(), this.getThresholderSettings(), null, "thresholderSettings", null, 1, 1, LSOSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLSOSettings_MoveGeneratorSettings(), this.getMoveGeneratorSettings(), null, "moveGeneratorSettings", null, 0, 1, LSOSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(thresholderSettingsEClass, ThresholderSettings.class, "ThresholderSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getThresholderSettings_Alpha(), ecorePackage.getEDouble(), "alpha", "0.95", 0, 1, ThresholderSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getThresholderSettings_InitialAcceptanceRate(), ecorePackage.getEDouble(), "initialAcceptanceRate", "0.75", 0, 1, ThresholderSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getThresholderSettings_EpochLength(), ecorePackage.getEInt(), "epochLength", "1000", 0, 1, ThresholderSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(moveGeneratorSettingsEClass, MoveGeneratorSettings.class, "MoveGeneratorSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(randomMoveGeneratorSettingsEClass, RandomMoveGeneratorSettings.class, "RandomMoveGeneratorSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRandomMoveGeneratorSettings_Using2over2(), ecorePackage.getEBoolean(), "using2over2", null, 0, 1, RandomMoveGeneratorSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRandomMoveGeneratorSettings_Using3over2(), ecorePackage.getEBoolean(), "using3over2", null, 0, 1, RandomMoveGeneratorSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRandomMoveGeneratorSettings_Using4over1(), ecorePackage.getEBoolean(), "using4over1", null, 0, 1, RandomMoveGeneratorSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRandomMoveGeneratorSettings_Using4over2(), ecorePackage.getEBoolean(), "using4over2", null, 0, 1, RandomMoveGeneratorSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constrainedMoveGeneratorSettingsEClass, ConstrainedMoveGeneratorSettings.class, "ConstrainedMoveGeneratorSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
	}

} //LsoPackageImpl
