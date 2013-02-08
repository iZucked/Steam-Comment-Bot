/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CharterCostModel;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DESPurchaseMarket;
import com.mmxlabs.models.lng.pricing.DESSalesMarket;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.FOBPurchasesMarket;
import com.mmxlabs.models.lng.pricing.FOBSalesMarket;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.LNGFixedPriceParameters;
import com.mmxlabs.models.lng.pricing.LNGIndexPriceParameters;
import com.mmxlabs.models.lng.pricing.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.pricing.LNGPriceExpressionParameters;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SpotAvailability;
import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.pricing.SpotMarketGroup;
import com.mmxlabs.models.lng.pricing.SpotType;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PricingPackageImpl extends EPackageImpl implements PricingPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pricingModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataIndexEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass derivedIndexEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indexPointEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indexEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fleetCostModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass routeCostEClass = null;

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
	private EClass baseFuelCostEClass = null;

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
	private EClass portCostEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portCostEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cooldownPriceEClass = null;

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
	private EClass lngPriceCalculatorParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lngFixedPriceParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lngIndexPriceParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lngPriceExpressionParametersEClass = null;

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
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private PricingPackageImpl() {
		super(eNS_URI, PricingFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link PricingPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static PricingPackage init() {
		if (isInited) return (PricingPackage)EPackage.Registry.INSTANCE.getEPackage(PricingPackage.eNS_URI);

		// Obtain or create and register package
		PricingPackageImpl thePricingPackage = (PricingPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PricingPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new PricingPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		TypesPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		thePricingPackage.createPackageContents();

		// Initialize created meta-data
		thePricingPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePricingPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(PricingPackage.eNS_URI, thePricingPackage);
		return thePricingPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPricingModel() {
		return pricingModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_CommodityIndices() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_CharterIndices() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_FleetCost() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_RouteCosts() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_PortCosts() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_CooldownPrices() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_DesPurchaseSpotMarket() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_DesSalesSpotMarket() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_FobPurchasesSpotMarket() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_FobSalesSpotMarket() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataIndex() {
		return dataIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataIndex_Points() {
		return (EReference)dataIndexEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDerivedIndex() {
		return derivedIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDerivedIndex_Expression() {
		return (EAttribute)derivedIndexEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndexPoint() {
		return indexPointEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexPoint_Date() {
		return (EAttribute)indexPointEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexPoint_Value() {
		return (EAttribute)indexPointEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndex() {
		return indexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFleetCostModel() {
		return fleetCostModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetCostModel_CharterCosts() {
		return (EReference)fleetCostModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetCostModel_BaseFuelPrices() {
		return (EReference)fleetCostModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRouteCost() {
		return routeCostEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRouteCost_Route() {
		return (EReference)routeCostEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRouteCost_VesselClass() {
		return (EReference)routeCostEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRouteCost_LadenCost() {
		return (EAttribute)routeCostEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRouteCost_BallastCost() {
		return (EAttribute)routeCostEClass.getEStructuralFeatures().get(3);
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
		return (EReference)charterCostModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCharterCostModel_CharterInPrice() {
		return (EReference)charterCostModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCharterCostModel_CharterOutPrice() {
		return (EReference)charterCostModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCharterCostModel_SpotCharterCount() {
		return (EAttribute)charterCostModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCharterCostModel_MinCharterOutDuration() {
		return (EAttribute)charterCostModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBaseFuelCost() {
		return baseFuelCostEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBaseFuelCost_Fuel() {
		return (EReference)baseFuelCostEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBaseFuelCost_Price() {
		return (EAttribute)baseFuelCostEClass.getEStructuralFeatures().get(1);
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
		return (EReference)spotMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpotMarket_MinQuantity() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpotMarket_MaxQuantity() {
		return (EAttribute)spotMarketEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPortCost() {
		return portCostEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPortCost_Ports() {
		return (EReference)portCostEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPortCost_Entries() {
		return (EReference)portCostEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPortCost_ReferenceCapacity() {
		return (EAttribute)portCostEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPortCostEntry() {
		return portCostEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPortCostEntry_Activity() {
		return (EAttribute)portCostEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPortCostEntry_Cost() {
		return (EAttribute)portCostEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCooldownPrice() {
		return cooldownPriceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCooldownPrice_Ports() {
		return (EReference)cooldownPriceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCooldownPrice_Index() {
		return (EReference)cooldownPriceEClass.getEStructuralFeatures().get(1);
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
	public EReference getDESPurchaseMarket_Contract() {
		return (EReference)desPurchaseMarketEClass.getEStructuralFeatures().get(2);
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
	public EReference getDESSalesMarket_Contract() {
		return (EReference)desSalesMarketEClass.getEStructuralFeatures().get(1);
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
	public EReference getFOBPurchasesMarket_Contract() {
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
	public EReference getFOBSalesMarket_LoadPort() {
		return (EReference)fobSalesMarketEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFOBSalesMarket_Contract() {
		return (EReference)fobSalesMarketEClass.getEStructuralFeatures().get(1);
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
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLNGPriceCalculatorParameters() {
		return lngPriceCalculatorParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLNGFixedPriceParameters() {
		return lngFixedPriceParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLNGFixedPriceParameters_PricePerMMBTU() {
		return (EAttribute)lngFixedPriceParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @generated
	 * <!-- end-user-doc -->
	 * @since 3.0
	 */
	public EClass getLNGIndexPriceParameters() {
		return lngIndexPriceParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLNGIndexPriceParameters_Index() {
		return (EReference)lngIndexPriceParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLNGIndexPriceParameters_Multiplier() {
		return (EAttribute)lngIndexPriceParametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLNGIndexPriceParameters_Constant() {
		return (EAttribute)lngIndexPriceParametersEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLNGPriceExpressionParameters() {
		return lngPriceExpressionParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLNGPriceExpressionParameters_PriceExpression() {
		return (EAttribute)lngPriceExpressionParametersEClass.getEStructuralFeatures().get(0);
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
	public PricingFactory getPricingFactory() {
		return (PricingFactory)getEFactoryInstance();
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
		pricingModelEClass = createEClass(PRICING_MODEL);
		createEReference(pricingModelEClass, PRICING_MODEL__COMMODITY_INDICES);
		createEReference(pricingModelEClass, PRICING_MODEL__CHARTER_INDICES);
		createEReference(pricingModelEClass, PRICING_MODEL__FLEET_COST);
		createEReference(pricingModelEClass, PRICING_MODEL__ROUTE_COSTS);
		createEReference(pricingModelEClass, PRICING_MODEL__PORT_COSTS);
		createEReference(pricingModelEClass, PRICING_MODEL__COOLDOWN_PRICES);
		createEReference(pricingModelEClass, PRICING_MODEL__DES_PURCHASE_SPOT_MARKET);
		createEReference(pricingModelEClass, PRICING_MODEL__DES_SALES_SPOT_MARKET);
		createEReference(pricingModelEClass, PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET);
		createEReference(pricingModelEClass, PRICING_MODEL__FOB_SALES_SPOT_MARKET);

		dataIndexEClass = createEClass(DATA_INDEX);
		createEReference(dataIndexEClass, DATA_INDEX__POINTS);

		derivedIndexEClass = createEClass(DERIVED_INDEX);
		createEAttribute(derivedIndexEClass, DERIVED_INDEX__EXPRESSION);

		indexPointEClass = createEClass(INDEX_POINT);
		createEAttribute(indexPointEClass, INDEX_POINT__DATE);
		createEAttribute(indexPointEClass, INDEX_POINT__VALUE);

		indexEClass = createEClass(INDEX);

		fleetCostModelEClass = createEClass(FLEET_COST_MODEL);
		createEReference(fleetCostModelEClass, FLEET_COST_MODEL__CHARTER_COSTS);
		createEReference(fleetCostModelEClass, FLEET_COST_MODEL__BASE_FUEL_PRICES);

		routeCostEClass = createEClass(ROUTE_COST);
		createEReference(routeCostEClass, ROUTE_COST__ROUTE);
		createEReference(routeCostEClass, ROUTE_COST__VESSEL_CLASS);
		createEAttribute(routeCostEClass, ROUTE_COST__LADEN_COST);
		createEAttribute(routeCostEClass, ROUTE_COST__BALLAST_COST);

		charterCostModelEClass = createEClass(CHARTER_COST_MODEL);
		createEReference(charterCostModelEClass, CHARTER_COST_MODEL__VESSEL_CLASSES);
		createEReference(charterCostModelEClass, CHARTER_COST_MODEL__CHARTER_IN_PRICE);
		createEReference(charterCostModelEClass, CHARTER_COST_MODEL__CHARTER_OUT_PRICE);
		createEAttribute(charterCostModelEClass, CHARTER_COST_MODEL__SPOT_CHARTER_COUNT);
		createEAttribute(charterCostModelEClass, CHARTER_COST_MODEL__MIN_CHARTER_OUT_DURATION);

		baseFuelCostEClass = createEClass(BASE_FUEL_COST);
		createEReference(baseFuelCostEClass, BASE_FUEL_COST__FUEL);
		createEAttribute(baseFuelCostEClass, BASE_FUEL_COST__PRICE);

		spotMarketGroupEClass = createEClass(SPOT_MARKET_GROUP);
		createEReference(spotMarketGroupEClass, SPOT_MARKET_GROUP__AVAILABILITY);
		createEAttribute(spotMarketGroupEClass, SPOT_MARKET_GROUP__TYPE);
		createEReference(spotMarketGroupEClass, SPOT_MARKET_GROUP__MARKETS);

		spotMarketEClass = createEClass(SPOT_MARKET);
		createEReference(spotMarketEClass, SPOT_MARKET__AVAILABILITY);
		createEAttribute(spotMarketEClass, SPOT_MARKET__MIN_QUANTITY);
		createEAttribute(spotMarketEClass, SPOT_MARKET__MAX_QUANTITY);

		portCostEClass = createEClass(PORT_COST);
		createEReference(portCostEClass, PORT_COST__PORTS);
		createEReference(portCostEClass, PORT_COST__ENTRIES);
		createEAttribute(portCostEClass, PORT_COST__REFERENCE_CAPACITY);

		portCostEntryEClass = createEClass(PORT_COST_ENTRY);
		createEAttribute(portCostEntryEClass, PORT_COST_ENTRY__ACTIVITY);
		createEAttribute(portCostEntryEClass, PORT_COST_ENTRY__COST);

		cooldownPriceEClass = createEClass(COOLDOWN_PRICE);
		createEReference(cooldownPriceEClass, COOLDOWN_PRICE__PORTS);
		createEReference(cooldownPriceEClass, COOLDOWN_PRICE__INDEX);

		desPurchaseMarketEClass = createEClass(DES_PURCHASE_MARKET);
		createEAttribute(desPurchaseMarketEClass, DES_PURCHASE_MARKET__CV);
		createEReference(desPurchaseMarketEClass, DES_PURCHASE_MARKET__DESTINATION_PORTS);
		createEReference(desPurchaseMarketEClass, DES_PURCHASE_MARKET__CONTRACT);

		desSalesMarketEClass = createEClass(DES_SALES_MARKET);
		createEReference(desSalesMarketEClass, DES_SALES_MARKET__NOTIONAL_PORT);
		createEReference(desSalesMarketEClass, DES_SALES_MARKET__CONTRACT);

		fobPurchasesMarketEClass = createEClass(FOB_PURCHASES_MARKET);
		createEReference(fobPurchasesMarketEClass, FOB_PURCHASES_MARKET__NOTIONAL_PORT);
		createEAttribute(fobPurchasesMarketEClass, FOB_PURCHASES_MARKET__CV);
		createEReference(fobPurchasesMarketEClass, FOB_PURCHASES_MARKET__CONTRACT);

		fobSalesMarketEClass = createEClass(FOB_SALES_MARKET);
		createEReference(fobSalesMarketEClass, FOB_SALES_MARKET__LOAD_PORT);
		createEReference(fobSalesMarketEClass, FOB_SALES_MARKET__CONTRACT);

		spotAvailabilityEClass = createEClass(SPOT_AVAILABILITY);
		createEAttribute(spotAvailabilityEClass, SPOT_AVAILABILITY__CONSTANT);
		createEReference(spotAvailabilityEClass, SPOT_AVAILABILITY__CURVE);

		lngPriceCalculatorParametersEClass = createEClass(LNG_PRICE_CALCULATOR_PARAMETERS);

		lngFixedPriceParametersEClass = createEClass(LNG_FIXED_PRICE_PARAMETERS);
		createEAttribute(lngFixedPriceParametersEClass, LNG_FIXED_PRICE_PARAMETERS__PRICE_PER_MMBTU);

		lngIndexPriceParametersEClass = createEClass(LNG_INDEX_PRICE_PARAMETERS);
		createEReference(lngIndexPriceParametersEClass, LNG_INDEX_PRICE_PARAMETERS__INDEX);
		createEAttribute(lngIndexPriceParametersEClass, LNG_INDEX_PRICE_PARAMETERS__MULTIPLIER);
		createEAttribute(lngIndexPriceParametersEClass, LNG_INDEX_PRICE_PARAMETERS__CONSTANT);

		lngPriceExpressionParametersEClass = createEClass(LNG_PRICE_EXPRESSION_PARAMETERS);
		createEAttribute(lngPriceExpressionParametersEClass, LNG_PRICE_EXPRESSION_PARAMETERS__PRICE_EXPRESSION);

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

		// Create type parameters
		ETypeParameter dataIndexEClass_Value = addETypeParameter(dataIndexEClass, "Value");
		ETypeParameter derivedIndexEClass_Value = addETypeParameter(derivedIndexEClass, "Value");
		ETypeParameter indexPointEClass_Value = addETypeParameter(indexPointEClass, "Value");
		ETypeParameter indexEClass_Value = addETypeParameter(indexEClass, "Value");

		// Set bounds for type parameters

		// Add supertypes to classes
		pricingModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		EGenericType g1 = createEGenericType(this.getIndex());
		EGenericType g2 = createEGenericType(dataIndexEClass_Value);
		g1.getETypeArguments().add(g2);
		dataIndexEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getIndex());
		g2 = createEGenericType(derivedIndexEClass_Value);
		g1.getETypeArguments().add(g2);
		derivedIndexEClass.getEGenericSuperTypes().add(g1);
		indexEClass.getESuperTypes().add(theTypesPackage.getAIndex());
		fleetCostModelEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		routeCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		charterCostModelEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		baseFuelCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		spotMarketGroupEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		spotMarketEClass.getESuperTypes().add(theTypesPackage.getASpotMarket());
		portCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		cooldownPriceEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		desPurchaseMarketEClass.getESuperTypes().add(this.getSpotMarket());
		desSalesMarketEClass.getESuperTypes().add(this.getSpotMarket());
		fobPurchasesMarketEClass.getESuperTypes().add(this.getSpotMarket());
		fobSalesMarketEClass.getESuperTypes().add(this.getSpotMarket());
		lngPriceCalculatorParametersEClass.getESuperTypes().add(theTypesPackage.getALNGPriceCalculatorParameters());
		lngFixedPriceParametersEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());
		lngIndexPriceParametersEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());
		lngPriceExpressionParametersEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());

		// Initialize classes and features; add operations and parameters
		initEClass(pricingModelEClass, PricingModel.class, "PricingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getIndex());
		g2 = createEGenericType(ecorePackage.getEDoubleObject());
		g1.getETypeArguments().add(g2);
		initEReference(getPricingModel_CommodityIndices(), g1, null, "commodityIndices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getIndex());
		g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		initEReference(getPricingModel_CharterIndices(), g1, null, "charterIndices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_FleetCost(), this.getFleetCostModel(), null, "fleetCost", null, 1, 1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_RouteCosts(), this.getRouteCost(), null, "routeCosts", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_PortCosts(), this.getPortCost(), null, "portCosts", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_CooldownPrices(), this.getCooldownPrice(), null, "cooldownPrices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_DesPurchaseSpotMarket(), this.getSpotMarketGroup(), null, "desPurchaseSpotMarket", null, 1, 1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_DesSalesSpotMarket(), this.getSpotMarketGroup(), null, "desSalesSpotMarket", null, 1, 1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_FobPurchasesSpotMarket(), this.getSpotMarketGroup(), null, "fobPurchasesSpotMarket", null, 1, 1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_FobSalesSpotMarket(), this.getSpotMarketGroup(), null, "fobSalesSpotMarket", null, 1, 1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataIndexEClass, DataIndex.class, "DataIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getIndexPoint());
		g2 = createEGenericType(dataIndexEClass_Value);
		g1.getETypeArguments().add(g2);
		initEReference(getDataIndex_Points(), g1, null, "points", null, 0, -1, DataIndex.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(derivedIndexEClass, DerivedIndex.class, "DerivedIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDerivedIndex_Expression(), ecorePackage.getEString(), "expression", null, 1, 1, DerivedIndex.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indexPointEClass, IndexPoint.class, "IndexPoint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIndexPoint_Date(), ecorePackage.getEDate(), "date", null, 1, 1, IndexPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(indexPointEClass_Value);
		initEAttribute(getIndexPoint_Value(), g1, "value", null, 1, 1, IndexPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indexEClass, Index.class, "Index", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = addEOperation(indexEClass, null, "getValueForMonth", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEDate(), "date", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(indexEClass_Value);
		initEOperation(op, g1);

		addEOperation(indexEClass, ecorePackage.getEDate(), "getDates", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(indexEClass, null, "getForwardValueForMonth", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEDate(), "date", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(indexEClass_Value);
		initEOperation(op, g1);

		op = addEOperation(indexEClass, null, "getBackwardsValueForMonth", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEDate(), "date", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(indexEClass_Value);
		initEOperation(op, g1);

		initEClass(fleetCostModelEClass, FleetCostModel.class, "FleetCostModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFleetCostModel_CharterCosts(), this.getCharterCostModel(), null, "charterCosts", null, 0, -1, FleetCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetCostModel_BaseFuelPrices(), this.getBaseFuelCost(), null, "baseFuelPrices", null, 0, -1, FleetCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(routeCostEClass, RouteCost.class, "RouteCost", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRouteCost_Route(), theTypesPackage.getARoute(), null, "route", null, 1, 1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRouteCost_VesselClass(), theTypesPackage.getAVesselClass(), null, "vesselClass", null, 1, 1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouteCost_LadenCost(), ecorePackage.getEInt(), "ladenCost", null, 1, 1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouteCost_BallastCost(), ecorePackage.getEInt(), "ballastCost", null, 1, 1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(charterCostModelEClass, CharterCostModel.class, "CharterCostModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCharterCostModel_VesselClasses(), theTypesPackage.getAVesselClass(), null, "vesselClasses", null, 0, -1, CharterCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getIndex());
		g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		initEReference(getCharterCostModel_CharterInPrice(), g1, null, "charterInPrice", null, 1, 1, CharterCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getIndex());
		g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		initEReference(getCharterCostModel_CharterOutPrice(), g1, null, "charterOutPrice", null, 1, 1, CharterCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterCostModel_SpotCharterCount(), ecorePackage.getEInt(), "spotCharterCount", null, 1, 1, CharterCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterCostModel_MinCharterOutDuration(), ecorePackage.getEInt(), "minCharterOutDuration", null, 1, 1, CharterCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseFuelCostEClass, BaseFuelCost.class, "BaseFuelCost", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBaseFuelCost_Fuel(), theTypesPackage.getABaseFuel(), null, "fuel", null, 1, 1, BaseFuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseFuelCost_Price(), ecorePackage.getEDouble(), "price", null, 1, 1, BaseFuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spotMarketGroupEClass, SpotMarketGroup.class, "SpotMarketGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSpotMarketGroup_Availability(), this.getSpotAvailability(), null, "availability", null, 1, 1, SpotMarketGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarketGroup_Type(), this.getSpotType(), "type", null, 1, 1, SpotMarketGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpotMarketGroup_Markets(), this.getSpotMarket(), null, "markets", null, 0, -1, SpotMarketGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spotMarketEClass, SpotMarket.class, "SpotMarket", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSpotMarket_Availability(), this.getSpotAvailability(), null, "availability", null, 1, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_MinQuantity(), ecorePackage.getEInt(), "minQuantity", null, 1, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpotMarket_MaxQuantity(), ecorePackage.getEInt(), "maxQuantity", null, 1, 1, SpotMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(spotMarketEClass, theTypesPackage.getAContract(), "getContract", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(portCostEClass, PortCost.class, "PortCost", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPortCost_Ports(), theTypesPackage.getAPortSet(), null, "ports", null, 0, -1, PortCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortCost_Entries(), this.getPortCostEntry(), null, "entries", null, 0, -1, PortCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortCost_ReferenceCapacity(), ecorePackage.getEInt(), "referenceCapacity", null, 1, 1, PortCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(portCostEClass, ecorePackage.getEInt(), "getPortCost", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theTypesPackage.getAVesselClass(), "vesselClass", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theTypesPackage.getPortCapability(), "activity", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(portCostEntryEClass, PortCostEntry.class, "PortCostEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPortCostEntry_Activity(), theTypesPackage.getPortCapability(), "activity", null, 1, 1, PortCostEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortCostEntry_Cost(), ecorePackage.getEInt(), "cost", null, 1, 1, PortCostEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cooldownPriceEClass, CooldownPrice.class, "CooldownPrice", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCooldownPrice_Ports(), theTypesPackage.getAPortSet(), null, "ports", null, 0, -1, CooldownPrice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getIndex());
		g2 = createEGenericType(ecorePackage.getEDoubleObject());
		g1.getETypeArguments().add(g2);
		initEReference(getCooldownPrice_Index(), g1, null, "index", null, 1, 1, CooldownPrice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(desPurchaseMarketEClass, DESPurchaseMarket.class, "DESPurchaseMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDESPurchaseMarket_Cv(), ecorePackage.getEDouble(), "cv", null, 0, 1, DESPurchaseMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDESPurchaseMarket_DestinationPorts(), theTypesPackage.getAPortSet(), null, "destinationPorts", null, 0, -1, DESPurchaseMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDESPurchaseMarket_Contract(), theTypesPackage.getAPurchaseContract(), null, "contract", null, 1, 1, DESPurchaseMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(desSalesMarketEClass, DESSalesMarket.class, "DESSalesMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDESSalesMarket_NotionalPort(), theTypesPackage.getAPort(), null, "notionalPort", null, 1, 1, DESSalesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDESSalesMarket_Contract(), theTypesPackage.getASalesContract(), null, "contract", null, 1, 1, DESSalesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fobPurchasesMarketEClass, FOBPurchasesMarket.class, "FOBPurchasesMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFOBPurchasesMarket_NotionalPort(), theTypesPackage.getAPort(), null, "notionalPort", null, 1, 1, FOBPurchasesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFOBPurchasesMarket_Cv(), ecorePackage.getEDouble(), "cv", null, 0, 1, FOBPurchasesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFOBPurchasesMarket_Contract(), theTypesPackage.getAPurchaseContract(), null, "contract", null, 1, 1, FOBPurchasesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fobSalesMarketEClass, FOBSalesMarket.class, "FOBSalesMarket", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFOBSalesMarket_LoadPort(), theTypesPackage.getAPort(), null, "loadPort", null, 1, 1, FOBSalesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFOBSalesMarket_Contract(), theTypesPackage.getASalesContract(), null, "contract", null, 1, 1, FOBSalesMarket.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spotAvailabilityEClass, SpotAvailability.class, "SpotAvailability", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpotAvailability_Constant(), ecorePackage.getEInt(), "constant", null, 0, 1, SpotAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getDataIndex());
		g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		initEReference(getSpotAvailability_Curve(), g1, null, "curve", null, 1, 1, SpotAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lngPriceCalculatorParametersEClass, LNGPriceCalculatorParameters.class, "LNGPriceCalculatorParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(lngFixedPriceParametersEClass, LNGFixedPriceParameters.class, "LNGFixedPriceParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLNGFixedPriceParameters_PricePerMMBTU(), ecorePackage.getEDouble(), "pricePerMMBTU", "0", 1, 1, LNGFixedPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lngIndexPriceParametersEClass, LNGIndexPriceParameters.class, "LNGIndexPriceParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLNGIndexPriceParameters_Index(), theTypesPackage.getAIndex(), null, "index", null, 1, 1, LNGIndexPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLNGIndexPriceParameters_Multiplier(), ecorePackage.getEDouble(), "multiplier", "1", 1, 1, LNGIndexPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLNGIndexPriceParameters_Constant(), ecorePackage.getEDouble(), "constant", "0", 1, 1, LNGIndexPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lngPriceExpressionParametersEClass, LNGPriceExpressionParameters.class, "LNGPriceExpressionParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLNGPriceExpressionParameters_PriceExpression(), ecorePackage.getEString(), "priceExpression", "", 0, 1, LNGPriceExpressionParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(spotTypeEEnum, SpotType.class, "SpotType");
		addEEnumLiteral(spotTypeEEnum, SpotType.FOB_SALE);
		addEEnumLiteral(spotTypeEEnum, SpotType.DES_PURCHASE);
		addEEnumLiteral(spotTypeEEnum, SpotType.DES_SALE);
		addEEnumLiteral(spotTypeEEnum, SpotType.FOB_PURCHASE);

		// Create resource
		createResource(eNS_URI);
	}

} //PricingPackageImpl
