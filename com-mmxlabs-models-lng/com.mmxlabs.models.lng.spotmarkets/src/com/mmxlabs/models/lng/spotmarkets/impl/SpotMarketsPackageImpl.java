/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotCharterMarket;
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
	private EClass charterOutMarketEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass charterInMarketEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spotCharterMarketEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass charterOutMarketParametersEClass = null;

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
		Object registeredSpotMarketsPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		SpotMarketsPackageImpl theSpotMarketsPackage = registeredSpotMarketsPackage instanceof SpotMarketsPackageImpl ? (SpotMarketsPackageImpl)registeredSpotMarketsPackage : new SpotMarketsPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		CommercialPackage.eINSTANCE.eClass();
		FleetPackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();
		PortPackage.eINSTANCE.eClass();
		PricingPackage.eINSTANCE.eClass();
		DateTimePackage.eINSTANCE.eClass();

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
	@Override
	public EClass getSpotMarketsModel() {
		return spotMarketsModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarketsModel_DesPurchaseSpotMarket() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarketsModel_DesSalesSpotMarket() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarketsModel_FobPurchasesSpotMarket() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarketsModel_FobSalesSpotMarket() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarketsModel_CharterInMarkets() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarketsModel_CharterOutMarkets() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarketsModel_CharterOutMarketParameters() {
		return (EReference)spotMarketsModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSpotMarketGroup() {
		return spotMarketGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarketGroup_Availability() {
		return (EReference)spotMarketGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarketGroup_Type() {
		return (EAttribute)spotMarketGroupEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarketGroup_Markets() {
		return (EReference)spotMarketGroupEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSpotMarket() {
		return spotMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarket_Enabled() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarket_Availability() {
		return (EReference)spotMarketEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarket_MinQuantity() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarket_MaxQuantity() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarket_VolumeLimitsUnit() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarket_PriceInfo() {
		return (EReference)spotMarketEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarket_Entity() {
		return (EReference)spotMarketEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarket_PricingEvent() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarket_RestrictedPorts() {
		return (EReference)spotMarketEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarket_RestrictedPortsArePermissive() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarket_RestrictedVessels() {
		return (EReference)spotMarketEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarket_RestrictedVesselsArePermissive() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotMarket_RestrictedContracts() {
		return (EReference)spotMarketEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarket_RestrictedContractsArePermissive() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotMarket_Mtm() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDESPurchaseMarket() {
		return desPurchaseMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDESPurchaseMarket_Cv() {
		return (EAttribute)desPurchaseMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDESPurchaseMarket_DestinationPorts() {
		return (EReference)desPurchaseMarketEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDESSalesMarket() {
		return desSalesMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDESSalesMarket_NotionalPort() {
		return (EReference)desSalesMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFOBPurchasesMarket() {
		return fobPurchasesMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFOBPurchasesMarket_NotionalPort() {
		return (EReference)fobPurchasesMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFOBPurchasesMarket_Cv() {
		return (EAttribute)fobPurchasesMarketEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFOBPurchasesMarket_MarketPorts() {
		return (EReference)fobPurchasesMarketEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFOBSalesMarket() {
		return fobSalesMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFOBSalesMarket_OriginPorts() {
		return (EReference)fobSalesMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSpotAvailability() {
		return spotAvailabilityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotAvailability_Constant() {
		return (EAttribute)spotAvailabilityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSpotAvailability_Curve() {
		return (EReference)spotAvailabilityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCharterOutMarket() {
		return charterOutMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutMarket_MinCharterOutDuration() {
		return (EAttribute)charterOutMarketEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterOutMarket_AvailablePorts() {
		return (EReference)charterOutMarketEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterOutMarket_Vessels() {
		return (EReference)charterOutMarketEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutMarket_MaxCharterOutDuration() {
		return (EAttribute)charterOutMarketEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutMarket_CharterOutRate() {
		return (EAttribute)charterOutMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCharterInMarket() {
		return charterInMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterInMarket_Vessel() {
		return (EReference)charterInMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarket_Nominal() {
		return (EAttribute)charterInMarketEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarket_MinDuration() {
		return (EAttribute)charterInMarketEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarket_MaxDuration() {
		return (EAttribute)charterInMarketEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarket_Mtm() {
		return (EAttribute)charterInMarketEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSpotCharterMarket() {
		return spotCharterMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpotCharterMarket_Enabled() {
		return (EAttribute)spotCharterMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCharterOutMarketParameters() {
		return charterOutMarketParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutMarketParameters_CharterOutStartDate() {
		return (EAttribute)charterOutMarketParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterOutMarketParameters_CharterOutEndDate() {
		return (EAttribute)charterOutMarketParametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarket_SpotCharterCount() {
		return (EAttribute)charterInMarketEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarket_OverrideInaccessibleRoutes() {
		return (EAttribute)charterInMarketEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarket_InaccessibleRoutes() {
		return (EAttribute)charterInMarketEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharterInMarket_CharterContract() {
		return (EReference)charterInMarketEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarket_RepositioningFee() {
		return (EAttribute)charterInMarketEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCharterInMarket_CharterInRate() {
		return (EAttribute)charterInMarketEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getSpotType() {
		return spotTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
		createEReference(spotMarketsModelEClass, SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS);
		createEReference(spotMarketsModelEClass, SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS);
		createEReference(spotMarketsModelEClass, SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS);

		spotMarketGroupEClass = createEClass(SPOT_MARKET_GROUP);
		createEReference(spotMarketGroupEClass, SPOT_MARKET_GROUP__AVAILABILITY);
		createEAttribute(spotMarketGroupEClass, SPOT_MARKET_GROUP__TYPE);
		createEReference(spotMarketGroupEClass, SPOT_MARKET_GROUP__MARKETS);

		spotMarketEClass = createEClass(SPOT_MARKET);
		createEAttribute(spotMarketEClass, SPOT_MARKET__ENABLED);
		createEReference(spotMarketEClass, SPOT_MARKET__AVAILABILITY);
		createEAttribute(spotMarketEClass, SPOT_MARKET__MIN_QUANTITY);
		createEAttribute(spotMarketEClass, SPOT_MARKET__MAX_QUANTITY);
		createEAttribute(spotMarketEClass, SPOT_MARKET__VOLUME_LIMITS_UNIT);
		createEReference(spotMarketEClass, SPOT_MARKET__PRICE_INFO);
		createEReference(spotMarketEClass, SPOT_MARKET__ENTITY);
		createEAttribute(spotMarketEClass, SPOT_MARKET__PRICING_EVENT);
		createEReference(spotMarketEClass, SPOT_MARKET__RESTRICTED_CONTRACTS);
		createEAttribute(spotMarketEClass, SPOT_MARKET__RESTRICTED_CONTRACTS_ARE_PERMISSIVE);
		createEReference(spotMarketEClass, SPOT_MARKET__RESTRICTED_PORTS);
		createEAttribute(spotMarketEClass, SPOT_MARKET__RESTRICTED_PORTS_ARE_PERMISSIVE);
		createEReference(spotMarketEClass, SPOT_MARKET__RESTRICTED_VESSELS);
		createEAttribute(spotMarketEClass, SPOT_MARKET__RESTRICTED_VESSELS_ARE_PERMISSIVE);
		createEAttribute(spotMarketEClass, SPOT_MARKET__MTM);

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

		charterOutMarketEClass = createEClass(CHARTER_OUT_MARKET);
		createEAttribute(charterOutMarketEClass, CHARTER_OUT_MARKET__CHARTER_OUT_RATE);
		createEAttribute(charterOutMarketEClass, CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION);
		createEReference(charterOutMarketEClass, CHARTER_OUT_MARKET__AVAILABLE_PORTS);
		createEReference(charterOutMarketEClass, CHARTER_OUT_MARKET__VESSELS);
		createEAttribute(charterOutMarketEClass, CHARTER_OUT_MARKET__MAX_CHARTER_OUT_DURATION);

		charterInMarketEClass = createEClass(CHARTER_IN_MARKET);
		createEReference(charterInMarketEClass, CHARTER_IN_MARKET__VESSEL);
		createEAttribute(charterInMarketEClass, CHARTER_IN_MARKET__CHARTER_IN_RATE);
		createEAttribute(charterInMarketEClass, CHARTER_IN_MARKET__SPOT_CHARTER_COUNT);
		createEAttribute(charterInMarketEClass, CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES);
		createEAttribute(charterInMarketEClass, CHARTER_IN_MARKET__INACCESSIBLE_ROUTES);
		createEReference(charterInMarketEClass, CHARTER_IN_MARKET__CHARTER_CONTRACT);
		createEAttribute(charterInMarketEClass, CHARTER_IN_MARKET__REPOSITIONING_FEE);
		createEAttribute(charterInMarketEClass, CHARTER_IN_MARKET__NOMINAL);
		createEAttribute(charterInMarketEClass, CHARTER_IN_MARKET__MIN_DURATION);
		createEAttribute(charterInMarketEClass, CHARTER_IN_MARKET__MAX_DURATION);
		createEAttribute(charterInMarketEClass, CHARTER_IN_MARKET__MTM);

		spotCharterMarketEClass = createEClass(SPOT_CHARTER_MARKET);
		createEAttribute(spotCharterMarketEClass, SPOT_CHARTER_MARKET__ENABLED);

		charterOutMarketParametersEClass = createEClass(CHARTER_OUT_MARKET_PARAMETERS);
		createEAttribute(charterOutMarketParametersEClass, CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE);
		createEAttribute(charterOutMarketParametersEClass, CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE);

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
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);
		PricingPackage thePricingPackage = (PricingPackage)EPackage.Registry.INSTANCE.getEPackage(PricingPackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		spotMarketsModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		spotMarketGroupEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		spotMarketEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		spotMarketEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		desPurchaseMarketEClass.getESuperTypes().add(this.getSpotMarket());
		desSalesMarketEClass.getESuperTypes().add(this.getSpotMarket());
		fobPurchasesMarketEClass.getESuperTypes().add(this.getSpotMarket());
		fobSalesMarketEClass.getESuperTypes().add(this.getSpotMarket());
		charterOutMarketEClass.getESuperTypes().add(this.getSpotCharterMarket());
		charterOutMarketEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		charterInMarketEClass.getESuperTypes().add(this.getSpotCharterMarket());
		charterInMarketEClass.getESuperTypes().add(theTypesPackage.getVesselAssignmentType());
		charterInMarketEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());

		// Initialize classes and features; add operations and parameters
		initEClass(spotMarketsModelEClass, SpotMarketsModel.class, "SpotMarketsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSpotMarketsModel_DesPurchaseSpotMarket(), this.getSpotMarketGroup(), null, "desPurchaseSpotMarket", null, 1, 1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketsModel_DesSalesSpotMarket(), this.getSpotMarketGroup(), null, "desSalesSpotMarket", null, 1, 1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketsModel_FobPurchasesSpotMarket(), this.getSpotMarketGroup(), null, "fobPurchasesSpotMarket", null, 1, 1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketsModel_FobSalesSpotMarket(), this.getSpotMarketGroup(), null, "fobSalesSpotMarket", null, 1, 1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketsModel_CharterInMarkets(), this.getCharterInMarket(), null, "charterInMarkets", null, 0, -1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketsModel_CharterOutMarkets(), this.getCharterOutMarket(), null, "charterOutMarkets", null, 0, -1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketsModel_CharterOutMarketParameters(), this.getCharterOutMarketParameters(), null, "charterOutMarketParameters", null, 0, 1, SpotMarketsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spotMarketGroupEClass, SpotMarketGroup.class, "SpotMarketGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSpotMarketGroup_Availability(), this.getSpotAvailability(), null, "availability", null, 1, 1, SpotMarketGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarketGroup_Type(), this.getSpotType(), "type", null, 1, 1, SpotMarketGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketGroup_Markets(), this.getSpotMarket(), null, "markets", null, 0, -1, SpotMarketGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spotMarketEClass, SpotMarket.class, "SpotMarket", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpotMarket_Enabled(), ecorePackage.getEBoolean(), "enabled", "true", 0, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarket_Availability(), this.getSpotAvailability(), null, "availability", null, 1, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_MinQuantity(), ecorePackage.getEInt(), "minQuantity", null, 1, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_MaxQuantity(), ecorePackage.getEInt(), "maxQuantity", null, 1, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_VolumeLimitsUnit(), theTypesPackage.getVolumeUnits(), "volumeLimitsUnit", null, 1, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarket_PriceInfo(), theCommercialPackage.getLNGPriceCalculatorParameters(), null, "priceInfo", null, 0, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarket_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_PricingEvent(), theCommercialPackage.getPricingEvent(), "pricingEvent", null, 0, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarket_RestrictedContracts(), theCommercialPackage.getContract(), null, "restrictedContracts", null, 0, -1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_RestrictedContractsArePermissive(), ecorePackage.getEBoolean(), "restrictedContractsArePermissive", null, 0, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(theTypesPackage.getAPortSet());
		EGenericType g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getSpotMarket_RestrictedPorts(), g1, null, "restrictedPorts", null, 0, -1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_RestrictedPortsArePermissive(), ecorePackage.getEBoolean(), "restrictedPortsArePermissive", null, 0, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(theFleetPackage.getVessel());
		g1.getETypeArguments().add(g2);
		initEReference(getSpotMarket_RestrictedVessels(), g1, null, "restrictedVessels", null, 0, -1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_RestrictedVesselsArePermissive(), ecorePackage.getEBoolean(), "restrictedVesselsArePermissive", null, 0, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_Mtm(), ecorePackage.getEBoolean(), "mtm", null, 0, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(desPurchaseMarketEClass, DESPurchaseMarket.class, "DESPurchaseMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDESPurchaseMarket_Cv(), ecorePackage.getEDouble(), "cv", null, 0, 1, DESPurchaseMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
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

		initEClass(charterOutMarketEClass, CharterOutMarket.class, "CharterOutMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCharterOutMarket_CharterOutRate(), ecorePackage.getEString(), "charterOutRate", null, 0, 1, CharterOutMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutMarket_MinCharterOutDuration(), ecorePackage.getEInt(), "minCharterOutDuration", null, 1, 1, CharterOutMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getCharterOutMarket_AvailablePorts(), g1, null, "availablePorts", null, 0, -1, CharterOutMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(theFleetPackage.getVessel());
		g1.getETypeArguments().add(g2);
		initEReference(getCharterOutMarket_Vessels(), g1, null, "vessels", null, 0, -1, CharterOutMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutMarket_MaxCharterOutDuration(), ecorePackage.getEInt(), "maxCharterOutDuration", null, 1, 1, CharterOutMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(charterInMarketEClass, CharterInMarket.class, "CharterInMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCharterInMarket_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 0, 1, CharterInMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarket_CharterInRate(), ecorePackage.getEString(), "charterInRate", null, 0, 1, CharterInMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarket_SpotCharterCount(), ecorePackage.getEInt(), "spotCharterCount", null, 1, 1, CharterInMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarket_OverrideInaccessibleRoutes(), ecorePackage.getEBoolean(), "overrideInaccessibleRoutes", null, 0, 1, CharterInMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarket_InaccessibleRoutes(), thePortPackage.getRouteOption(), "inaccessibleRoutes", null, 0, -1, CharterInMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterInMarket_CharterContract(), theCommercialPackage.getCharterContract(), null, "charterContract", null, 0, 1, CharterInMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarket_RepositioningFee(), ecorePackage.getEString(), "repositioningFee", null, 1, 1, CharterInMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarket_Nominal(), ecorePackage.getEBoolean(), "nominal", "true", 0, 1, CharterInMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarket_MinDuration(), ecorePackage.getEInt(), "minDuration", null, 1, 1, CharterInMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarket_MaxDuration(), ecorePackage.getEInt(), "maxDuration", null, 1, 1, CharterInMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterInMarket_Mtm(), ecorePackage.getEBoolean(), "mtm", null, 0, 1, CharterInMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(charterInMarketEClass, ecorePackage.getEInt(), "getMarketOrContractMinDuration", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(charterInMarketEClass, ecorePackage.getEInt(), "getMarketOrContractMaxDuration", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(spotCharterMarketEClass, SpotCharterMarket.class, "SpotCharterMarket", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpotCharterMarket_Enabled(), ecorePackage.getEBoolean(), "enabled", "true", 0, 1, SpotCharterMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(charterOutMarketParametersEClass, CharterOutMarketParameters.class, "CharterOutMarketParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCharterOutMarketParameters_CharterOutStartDate(), theDateTimePackage.getLocalDate(), "charterOutStartDate", null, 0, 1, CharterOutMarketParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutMarketParameters_CharterOutEndDate(), theDateTimePackage.getLocalDate(), "charterOutEndDate", null, 0, 1, CharterOutMarketParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(spotTypeEEnum, SpotType.class, "SpotType");
		addEEnumLiteral(spotTypeEEnum, SpotType.FOB_SALE);
		addEEnumLiteral(spotTypeEEnum, SpotType.DES_PURCHASE);
		addEEnumLiteral(spotTypeEEnum, SpotType.DES_SALE);
		addEEnumLiteral(spotTypeEEnum, SpotType.FOB_PURCHASE);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
		// http://www.mmxlabs.com/models/pricing/expressionType
		createExpressionTypeAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/ui/numberFormat</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNumberFormatAnnotations() {
		String source = "http://www.mmxlabs.com/models/ui/numberFormat";
		addAnnotation
		  (getSpotMarket_MinQuantity(),
		   source,
		   new String[] {
			   "formatString", "#,###,##0"
		   });
		addAnnotation
		  (getSpotMarket_MaxQuantity(),
		   source,
		   new String[] {
			   "formatString", "#,###,##0"
		   });
		addAnnotation
		  (getDESPurchaseMarket_Cv(),
		   source,
		   new String[] {
			   "unit", "mmBtu/m\u00b3",
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getFOBPurchasesMarket_Cv(),
		   source,
		   new String[] {
			   "unit", "mmBtu/m\u00b3",
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getCharterOutMarket_CharterOutRate(),
		   source,
		   new String[] {
			   "unit", "$/day"
		   });
		addAnnotation
		  (getCharterInMarket_CharterInRate(),
		   source,
		   new String[] {
			   "unit", "$/day"
		   });
		addAnnotation
		  (getCharterInMarket_RepositioningFee(),
		   source,
		   new String[] {
			   "unit", "$/day"
		   });
		addAnnotation
		  (getCharterInMarket_MinDuration(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getCharterInMarket_MaxDuration(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "##0"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/pricing/expressionType</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExpressionTypeAnnotations() {
		String source = "http://www.mmxlabs.com/models/pricing/expressionType";
		addAnnotation
		  (getCharterOutMarket_CharterOutRate(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
		addAnnotation
		  (getCharterInMarket_CharterInRate(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
		addAnnotation
		  (getCharterInMarket_RepositioningFee(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
	}

} //SpotMarketsPackageImpl
