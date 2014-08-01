/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SpotMarketsPackageImpl extends EPackageImpl implements SpotMarketsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spotMarketsModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass charterCostModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spotMarketGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spotMarketEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass desPurchaseMarketEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass desSalesMarketEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fobPurchasesMarketEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fobSalesMarketEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spotAvailabilityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass charterOutStartDateEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum spotTypeEEnum = null;

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
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SpotMarketsPackageImpl() {
		super(eNS_URI, SpotMarketsFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link SpotMarketsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SpotMarketsPackage init() {
		if (isInited) return (SpotMarketsPackage)EPackage.Registry.INSTANCE.getEPackage(SpotMarketsPackage.eNS_URI);

		// Obtain or create and register package
		SpotMarketsPackageImpl theSpotMarketsPackage = (SpotMarketsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SpotMarketsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SpotMarketsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CommercialPackage.eINSTANCE.eClass();
		PricingPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theSpotMarketsPackage.createPackageContents();

		// Initialize created meta-data
		theSpotMarketsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSpotMarketsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SpotMarketsPackage.eNS_URI, theSpotMarketsPackage);
		return theSpotMarketsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpotMarketsModel() {
		return spotMarketsModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotMarketsModel_DesPurchaseSpotMarket() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotMarketsModel_DesSalesSpotMarket() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotMarketsModel_FobPurchasesSpotMarket() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotMarketsModel_FobSalesSpotMarket() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotMarketsModel_CharteringSpotMarkets() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotMarketsModel_CharterOutStartDate() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCharterCostModel() {
		return charterCostModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCharterCostModel_VesselClasses() {
		return (EReference)charterCostModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCharterCostModel_CharterInPrice() {
		return (EReference)charterCostModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCharterCostModel_CharterOutPrice() {
		return (EReference)charterCostModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCharterCostModel_SpotCharterCount() {
		return (EAttribute)charterCostModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCharterCostModel_MinCharterOutDuration() {
		return (EAttribute)charterCostModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCharterCostModel_Enabled() {
		return (EAttribute)charterCostModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpotMarketGroup() {
		return spotMarketGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotMarketGroup_Availability() {
		return (EReference)spotMarketGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpotMarketGroup_Type() {
		return (EAttribute)spotMarketGroupEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotMarketGroup_Markets() {
		return (EReference)spotMarketGroupEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpotMarket() {
		return spotMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotMarket_Availability() {
		return (EReference)spotMarketEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpotMarket_MinQuantity() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpotMarket_MaxQuantity() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotMarket_PriceInfo() {
		return (EReference)spotMarketEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotMarket_Entity() {
		return (EReference)spotMarketEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpotMarket_Enabled() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDESPurchaseMarket() {
		return desPurchaseMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDESPurchaseMarket_Cv() {
		return (EAttribute)desPurchaseMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDESPurchaseMarket_DestinationPorts() {
		return (EReference)desPurchaseMarketEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDESSalesMarket() {
		return desSalesMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDESSalesMarket_NotionalPort() {
		return (EReference)desSalesMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFOBPurchasesMarket() {
		return fobPurchasesMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFOBPurchasesMarket_NotionalPort() {
		return (EReference)fobPurchasesMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFOBPurchasesMarket_Cv() {
		return (EAttribute)fobPurchasesMarketEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFOBPurchasesMarket_MarketPorts() {
		return (EReference)fobPurchasesMarketEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFOBSalesMarket() {
		return fobSalesMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFOBSalesMarket_OriginPorts() {
		return (EReference)fobSalesMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpotAvailability() {
		return spotAvailabilityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpotAvailability_Constant() {
		return (EAttribute)spotAvailabilityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpotAvailability_Curve() {
		return (EReference)spotAvailabilityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCharterOutStartDate() {
		return charterOutStartDateEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCharterOutStartDate_CharterOutStartDate() {
		return (EAttribute)charterOutStartDateEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSpotType() {
		return spotTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketsFactory getSpotMarketsFactory() {
		return (SpotMarketsFactory)getEFactoryInstance();
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
		spotMarketsModelEClass = createEClass(SPOT_MARKETS_MODEL);
		createEReference(spotMarketsModelEClass, SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET);
		createEReference(spotMarketsModelEClass, SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET);
		createEReference(spotMarketsModelEClass, SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET);
		createEReference(spotMarketsModelEClass, SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET);
		createEReference(spotMarketsModelEClass, SPOT_MARKETS_MODEL__CHARTERING_SPOT_MARKETS);
		createEReference(spotMarketsModelEClass, SPOT_MARKETS_MODEL__CHARTER_OUT_START_DATE);

		charterCostModelEClass = createEClass(CHARTER_COST_MODEL);
		createEAttribute(charterCostModelEClass, CHARTER_COST_MODEL__ENABLED);
		createEReference(charterCostModelEClass, CHARTER_COST_MODEL__VESSEL_CLASSES);
		createEReference(charterCostModelEClass, CHARTER_COST_MODEL__CHARTER_IN_PRICE);
		createEReference(charterCostModelEClass, CHARTER_COST_MODEL__CHARTER_OUT_PRICE);
		createEAttribute(charterCostModelEClass, CHARTER_COST_MODEL__SPOT_CHARTER_COUNT);
		createEAttribute(charterCostModelEClass, CHARTER_COST_MODEL__MIN_CHARTER_OUT_DURATION);

		spotMarketGroupEClass = createEClass(SPOT_MARKET_GROUP);
		createEReference(spotMarketGroupEClass, SPOT_MARKET_GROUP__AVAILABILITY);
		createEAttribute(spotMarketGroupEClass, SPOT_MARKET_GROUP__TYPE);
		createEReference(spotMarketGroupEClass, SPOT_MARKET_GROUP__MARKETS);

		spotMarketEClass = createEClass(SPOT_MARKET);
		createEAttribute(spotMarketEClass, SPOT_MARKET__ENABLED);
		createEReference(spotMarketEClass, SPOT_MARKET__AVAILABILITY);
		createEAttribute(spotMarketEClass, SPOT_MARKET__MIN_QUANTITY);
		createEAttribute(spotMarketEClass, SPOT_MARKET__MAX_QUANTITY);
		createEReference(spotMarketEClass, SPOT_MARKET__PRICE_INFO);
		createEReference(spotMarketEClass, SPOT_MARKET__ENTITY);

		desPurchaseMarketEClass = createEClass(DES_PURCHASE_MARKET);
		createEAttribute(desPurchaseMarketEClass, DES_PURCHASE_MARKET__CV);
		createEReference(desPurchaseMarketEClass, DES_PURCHASE_MARKET__DESTINATION_PORTS);

		desSalesMarketEClass = createEClass(DES_SALES_MARKET);
		createEReference(desSalesMarketEClass, DES_SALES_MARKET__NOTIONAL_PORT);

		fobPurchasesMarketEClass = createEClass(FOB_PURCHASES_MARKET);
		createEReference(fobPurchasesMarketEClass, FOB_PURCHASES_MARKET__NOTIONAL_PORT);
		createEAttribute(fobPurchasesMarketEClass, FOB_PURCHASES_MARKET__CV);
		createEReference(fobPurchasesMarketEClass, FOB_PURCHASES_MARKET__MARKET_PORTS);

		fobSalesMarketEClass = createEClass(FOB_SALES_MARKET);
		createEReference(fobSalesMarketEClass, FOB_SALES_MARKET__ORIGIN_PORTS);

		spotAvailabilityEClass = createEClass(SPOT_AVAILABILITY);
		createEAttribute(spotAvailabilityEClass, SPOT_AVAILABILITY__CONSTANT);
		createEReference(spotAvailabilityEClass, SPOT_AVAILABILITY__CURVE);

		charterOutStartDateEClass = createEClass(CHARTER_OUT_START_DATE);
		createEAttribute(charterOutStartDateEClass, CHARTER_OUT_START_DATE__CHARTER_OUT_START_DATE);

		// Create enums
		spotTypeEEnum = createEEnum(SPOT_TYPE);
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
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);
		PricingPackage thePricingPackage = (PricingPackage)EPackage.Registry.INSTANCE.getEPackage(PricingPackage.eNS_URI);
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		spotMarketsModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		charterCostModelEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		spotMarketGroupEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		spotMarketEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		spotMarketEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		desPurchaseMarketEClass.getESuperTypes().add(this.getSpotMarket());
		desSalesMarketEClass.getESuperTypes().add(this.getSpotMarket());
		fobPurchasesMarketEClass.getESuperTypes().add(this.getSpotMarket());
		fobSalesMarketEClass.getESuperTypes().add(this.getSpotMarket());

		// Initialize classes and features; add operations and parameters
		initEClass(spotMarketsModelEClass, SpotMarketsModel.class, "SpotMarketsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSpotMarketsModel_DesPurchaseSpotMarket(), this.getSpotMarketGroup(), null, "desPurchaseSpotMarket", null, 1, 1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketsModel_DesSalesSpotMarket(), this.getSpotMarketGroup(), null, "desSalesSpotMarket", null, 1, 1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketsModel_FobPurchasesSpotMarket(), this.getSpotMarketGroup(), null, "fobPurchasesSpotMarket", null, 1, 1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketsModel_FobSalesSpotMarket(), this.getSpotMarketGroup(), null, "fobSalesSpotMarket", null, 1, 1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketsModel_CharteringSpotMarkets(), this.getCharterCostModel(), null, "charteringSpotMarkets", null, 0, -1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketsModel_CharterOutStartDate(), this.getCharterOutStartDate(), null, "charterOutStartDate", null, 0, 1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(charterCostModelEClass, CharterCostModel.class, "CharterCostModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCharterCostModel_Enabled(), ecorePackage.getEBoolean(), "enabled", "true", 0, 1, CharterCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterCostModel_VesselClasses(), theFleetPackage.getVesselClass(), null, "vesselClasses", null, 0, -1, CharterCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterCostModel_CharterInPrice(), thePricingPackage.getCharterIndex(), null, "charterInPrice", null, 1, 1, CharterCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterCostModel_CharterOutPrice(), thePricingPackage.getCharterIndex(), null, "charterOutPrice", null, 1, 1, CharterCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterCostModel_SpotCharterCount(), ecorePackage.getEInt(), "spotCharterCount", null, 1, 1, CharterCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterCostModel_MinCharterOutDuration(), ecorePackage.getEInt(), "minCharterOutDuration", null, 1, 1, CharterCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spotMarketGroupEClass, SpotMarketGroup.class, "SpotMarketGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSpotMarketGroup_Availability(), this.getSpotAvailability(), null, "availability", null, 1, 1, SpotMarketGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarketGroup_Type(), this.getSpotType(), "type", null, 1, 1, SpotMarketGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketGroup_Markets(), this.getSpotMarket(), null, "markets", null, 0, -1, SpotMarketGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spotMarketEClass, SpotMarket.class, "SpotMarket", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpotMarket_Enabled(), ecorePackage.getEBoolean(), "enabled", "true", 0, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarket_Availability(), this.getSpotAvailability(), null, "availability", null, 1, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_MinQuantity(), ecorePackage.getEInt(), "minQuantity", null, 1, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_MaxQuantity(), ecorePackage.getEInt(), "maxQuantity", null, 1, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarket_PriceInfo(), theCommercialPackage.getLNGPriceCalculatorParameters(), null, "priceInfo", null, 0, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarket_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(desPurchaseMarketEClass, DESPurchaseMarket.class, "DESPurchaseMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDESPurchaseMarket_Cv(), ecorePackage.getEDouble(), "cv", null, 0, 1, DESPurchaseMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(theTypesPackage.getAPortSet());
		EGenericType g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getDESPurchaseMarket_DestinationPorts(), g1, null, "destinationPorts", null, 0, -1, DESPurchaseMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(desSalesMarketEClass, DESSalesMarket.class, "DESSalesMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDESSalesMarket_NotionalPort(), thePortPackage.getPort(), null, "notionalPort", null, 1, 1, DESSalesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fobPurchasesMarketEClass, FOBPurchasesMarket.class, "FOBPurchasesMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFOBPurchasesMarket_NotionalPort(), thePortPackage.getPort(), null, "notionalPort", null, 1, 1, FOBPurchasesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFOBPurchasesMarket_Cv(), ecorePackage.getEDouble(), "cv", null, 0, 1, FOBPurchasesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getFOBPurchasesMarket_MarketPorts(), g1, null, "marketPorts", null, 0, -1, FOBPurchasesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fobSalesMarketEClass, FOBSalesMarket.class, "FOBSalesMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getFOBSalesMarket_OriginPorts(), g1, null, "originPorts", null, 0, -1, FOBSalesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spotAvailabilityEClass, SpotAvailability.class, "SpotAvailability", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpotAvailability_Constant(), ecorePackage.getEInt(), "constant", null, 0, 1, SpotAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(thePricingPackage.getDataIndex());
		g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		initEReference(getSpotAvailability_Curve(), g1, null, "curve", null, 1, 1, SpotAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(charterOutStartDateEClass, CharterOutStartDate.class, "CharterOutStartDate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCharterOutStartDate_CharterOutStartDate(), ecorePackage.getEDate(), "charterOutStartDate", null, 0, 1, CharterOutStartDate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(spotTypeEEnum, SpotType.class, "SpotType");
		addEEnumLiteral(spotTypeEEnum, SpotType.FOB_SALE);
		addEEnumLiteral(spotTypeEEnum, SpotType.DES_PURCHASE);
		addEEnumLiteral(spotTypeEEnum, SpotType.DES_SALE);
		addEEnumLiteral(spotTypeEEnum, SpotType.FOB_PURCHASE);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/lng/ui/datetime
		createDatetimeAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/lng/ui/datetime</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createDatetimeAnnotations() {
		String source = "http://www.mmxlabs.com/models/lng/ui/datetime";		
		addAnnotation
		  (getCharterOutStartDate_CharterOutStartDate(), 
		   source, 
		   new String[] {
			 "showTime", "false"
		   });
	}

} //SpotMarketsPackageImpl
