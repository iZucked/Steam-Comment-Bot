/**
 */
package com.mmxlabs.models.lng.scenario.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class LNGScenarioPackageImpl extends EPackageImpl implements LNGScenarioPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lngScenarioModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lngPortfolioModelEClass = null;

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
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private LNGScenarioPackageImpl() {
		super(eNS_URI, LNGScenarioFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link LNGScenarioPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static LNGScenarioPackage init() {
		if (isInited) return (LNGScenarioPackage)EPackage.Registry.INSTANCE.getEPackage(LNGScenarioPackage.eNS_URI);

		// Obtain or create and register package
		LNGScenarioPackageImpl theLNGScenarioPackage = (LNGScenarioPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof LNGScenarioPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new LNGScenarioPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		AnalyticsPackage.eINSTANCE.eClass();
		AssignmentPackage.eINSTANCE.eClass();
		ParametersPackage.eINSTANCE.eClass();
		SchedulePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theLNGScenarioPackage.createPackageContents();

		// Initialize created meta-data
		theLNGScenarioPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theLNGScenarioPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(LNGScenarioPackage.eNS_URI, theLNGScenarioPackage);
		return theLNGScenarioPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLNGScenarioModel() {
		return lngScenarioModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_PortModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_FleetModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_PricingModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_CommercialModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_SpotMarketsModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_ParametersModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_AnalyticsModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_PortfolioModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLNGPortfolioModel() {
		return lngPortfolioModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGPortfolioModel_ScenarioFleetModel() {
		return (EReference)lngPortfolioModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGPortfolioModel_CargoModel() {
		return (EReference)lngPortfolioModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGPortfolioModel_AssignmentModel() {
		return (EReference)lngPortfolioModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGPortfolioModel_ScheduleModel() {
		return (EReference)lngPortfolioModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGScenarioFactory getLNGScenarioFactory() {
		return (LNGScenarioFactory)getEFactoryInstance();
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
		lngScenarioModelEClass = createEClass(LNG_SCENARIO_MODEL);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__PORT_MODEL);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__FLEET_MODEL);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__PRICING_MODEL);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__COMMERCIAL_MODEL);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__PARAMETERS_MODEL);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__ANALYTICS_MODEL);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__PORTFOLIO_MODEL);

		lngPortfolioModelEClass = createEClass(LNG_PORTFOLIO_MODEL);
		createEReference(lngPortfolioModelEClass, LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL);
		createEReference(lngPortfolioModelEClass, LNG_PORTFOLIO_MODEL__CARGO_MODEL);
		createEReference(lngPortfolioModelEClass, LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL);
		createEReference(lngPortfolioModelEClass, LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL);
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
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);
		PricingPackage thePricingPackage = (PricingPackage)EPackage.Registry.INSTANCE.getEPackage(PricingPackage.eNS_URI);
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		SpotMarketsPackage theSpotMarketsPackage = (SpotMarketsPackage)EPackage.Registry.INSTANCE.getEPackage(SpotMarketsPackage.eNS_URI);
		ParametersPackage theParametersPackage = (ParametersPackage)EPackage.Registry.INSTANCE.getEPackage(ParametersPackage.eNS_URI);
		AnalyticsPackage theAnalyticsPackage = (AnalyticsPackage)EPackage.Registry.INSTANCE.getEPackage(AnalyticsPackage.eNS_URI);
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);
		AssignmentPackage theAssignmentPackage = (AssignmentPackage)EPackage.Registry.INSTANCE.getEPackage(AssignmentPackage.eNS_URI);
		SchedulePackage theSchedulePackage = (SchedulePackage)EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		lngScenarioModelEClass.getESuperTypes().add(theMMXCorePackage.getMMXRootObject());
		lngPortfolioModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());

		// Initialize classes, features, and operations; add parameters
		initEClass(lngScenarioModelEClass, LNGScenarioModel.class, "LNGScenarioModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLNGScenarioModel_PortModel(), thePortPackage.getPortModel(), null, "portModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGScenarioModel_FleetModel(), theFleetPackage.getFleetModel(), null, "fleetModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGScenarioModel_PricingModel(), thePricingPackage.getPricingModel(), null, "pricingModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGScenarioModel_CommercialModel(), theCommercialPackage.getCommercialModel(), null, "commercialModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGScenarioModel_SpotMarketsModel(), theSpotMarketsPackage.getSpotMarketsModel(), null, "spotMarketsModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGScenarioModel_ParametersModel(), theParametersPackage.getParametersModel(), null, "parametersModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGScenarioModel_AnalyticsModel(), theAnalyticsPackage.getAnalyticsModel(), null, "analyticsModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGScenarioModel_PortfolioModel(), this.getLNGPortfolioModel(), null, "portfolioModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lngPortfolioModelEClass, LNGPortfolioModel.class, "LNGPortfolioModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLNGPortfolioModel_ScenarioFleetModel(), theFleetPackage.getScenarioFleetModel(), null, "scenarioFleetModel", null, 0, 1, LNGPortfolioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGPortfolioModel_CargoModel(), theCargoPackage.getCargoModel(), null, "cargoModel", null, 0, 1, LNGPortfolioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGPortfolioModel_AssignmentModel(), theAssignmentPackage.getAssignmentModel(), null, "assignmentModel", null, 0, 1, LNGPortfolioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGPortfolioModel_ScheduleModel(), theSchedulePackage.getScheduleModel(), null, "scheduleModel", null, 0, 1, LNGPortfolioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //LNGScenarioPackageImpl
