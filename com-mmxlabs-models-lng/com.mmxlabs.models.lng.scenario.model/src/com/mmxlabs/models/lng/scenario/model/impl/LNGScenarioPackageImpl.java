/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.scenario.model.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
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
	private EClass lngReferenceModelEClass = null;

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
		ActualsPackage.eINSTANCE.eClass();
		AnalyticsPackage.eINSTANCE.eClass();
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
	public EReference getLNGScenarioModel_CargoModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_ScheduleModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_ActualsModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLNGScenarioModel_PromptPeriodStart() {
		return (EAttribute)lngScenarioModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLNGScenarioModel_PromptPeriodEnd() {
		return (EAttribute)lngScenarioModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_ReferenceModel() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGScenarioModel_UserSettings() {
		return (EReference)lngScenarioModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLNGReferenceModel() {
		return lngReferenceModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGReferenceModel_PortModel() {
		return (EReference)lngReferenceModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGReferenceModel_FleetModel() {
		return (EReference)lngReferenceModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGReferenceModel_PricingModel() {
		return (EReference)lngReferenceModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGReferenceModel_CommercialModel() {
		return (EReference)lngReferenceModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGReferenceModel_SpotMarketsModel() {
		return (EReference)lngReferenceModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGReferenceModel_AnalyticsModel() {
		return (EReference)lngReferenceModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGReferenceModel_CostModel() {
		return (EReference)lngReferenceModelEClass.getEStructuralFeatures().get(6);
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
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__CARGO_MODEL);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__SCHEDULE_MODEL);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__ACTUALS_MODEL);
		createEAttribute(lngScenarioModelEClass, LNG_SCENARIO_MODEL__PROMPT_PERIOD_START);
		createEAttribute(lngScenarioModelEClass, LNG_SCENARIO_MODEL__PROMPT_PERIOD_END);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__REFERENCE_MODEL);
		createEReference(lngScenarioModelEClass, LNG_SCENARIO_MODEL__USER_SETTINGS);

		lngReferenceModelEClass = createEClass(LNG_REFERENCE_MODEL);
		createEReference(lngReferenceModelEClass, LNG_REFERENCE_MODEL__PORT_MODEL);
		createEReference(lngReferenceModelEClass, LNG_REFERENCE_MODEL__FLEET_MODEL);
		createEReference(lngReferenceModelEClass, LNG_REFERENCE_MODEL__PRICING_MODEL);
		createEReference(lngReferenceModelEClass, LNG_REFERENCE_MODEL__COMMERCIAL_MODEL);
		createEReference(lngReferenceModelEClass, LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL);
		createEReference(lngReferenceModelEClass, LNG_REFERENCE_MODEL__ANALYTICS_MODEL);
		createEReference(lngReferenceModelEClass, LNG_REFERENCE_MODEL__COST_MODEL);
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
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);
		SchedulePackage theSchedulePackage = (SchedulePackage)EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI);
		ActualsPackage theActualsPackage = (ActualsPackage)EPackage.Registry.INSTANCE.getEPackage(ActualsPackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		ParametersPackage theParametersPackage = (ParametersPackage)EPackage.Registry.INSTANCE.getEPackage(ParametersPackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);
		PricingPackage thePricingPackage = (PricingPackage)EPackage.Registry.INSTANCE.getEPackage(PricingPackage.eNS_URI);
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		SpotMarketsPackage theSpotMarketsPackage = (SpotMarketsPackage)EPackage.Registry.INSTANCE.getEPackage(SpotMarketsPackage.eNS_URI);
		AnalyticsPackage theAnalyticsPackage = (AnalyticsPackage)EPackage.Registry.INSTANCE.getEPackage(AnalyticsPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		lngScenarioModelEClass.getESuperTypes().add(theMMXCorePackage.getMMXRootObject());
		lngReferenceModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());

		// Initialize classes, features, and operations; add parameters
		initEClass(lngScenarioModelEClass, LNGScenarioModel.class, "LNGScenarioModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLNGScenarioModel_CargoModel(), theCargoPackage.getCargoModel(), null, "cargoModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGScenarioModel_ScheduleModel(), theSchedulePackage.getScheduleModel(), null, "scheduleModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGScenarioModel_ActualsModel(), theActualsPackage.getActualsModel(), null, "actualsModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLNGScenarioModel_PromptPeriodStart(), theDateTimePackage.getLocalDate(), "promptPeriodStart", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLNGScenarioModel_PromptPeriodEnd(), theDateTimePackage.getLocalDate(), "promptPeriodEnd", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGScenarioModel_ReferenceModel(), this.getLNGReferenceModel(), null, "referenceModel", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGScenarioModel_UserSettings(), theParametersPackage.getUserSettings(), null, "userSettings", null, 0, 1, LNGScenarioModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lngReferenceModelEClass, LNGReferenceModel.class, "LNGReferenceModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLNGReferenceModel_PortModel(), thePortPackage.getPortModel(), null, "portModel", null, 0, 1, LNGReferenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGReferenceModel_FleetModel(), theFleetPackage.getFleetModel(), null, "fleetModel", null, 0, 1, LNGReferenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGReferenceModel_PricingModel(), thePricingPackage.getPricingModel(), null, "pricingModel", null, 0, 1, LNGReferenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGReferenceModel_CommercialModel(), theCommercialPackage.getCommercialModel(), null, "commercialModel", null, 0, 1, LNGReferenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGReferenceModel_SpotMarketsModel(), theSpotMarketsPackage.getSpotMarketsModel(), null, "spotMarketsModel", null, 0, 1, LNGReferenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGReferenceModel_AnalyticsModel(), theAnalyticsPackage.getAnalyticsModel(), null, "analyticsModel", null, 0, 1, LNGReferenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLNGReferenceModel_CostModel(), thePricingPackage.getCostModel(), null, "costModel", null, 0, 1, LNGReferenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //LNGScenarioPackageImpl
