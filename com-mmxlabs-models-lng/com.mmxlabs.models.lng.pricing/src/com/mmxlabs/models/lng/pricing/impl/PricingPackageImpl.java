/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PortsExpressionMap;
import com.mmxlabs.models.lng.pricing.PortsPriceMap;
import com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap;
import com.mmxlabs.models.lng.pricing.PortsSplitPriceMap;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
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
	private EClass baseFuelCostEClass = null;

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
	private EClass commodityIndexEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass charterIndexEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass baseFuelIndexEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedIndexContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portsPriceMapEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portsExpressionMapEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portsSplitPriceMapEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portsSplitExpressionMapEClass = null;

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
		FleetPackage.eINSTANCE.eClass();

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
	public EReference getPricingModel_BaseFuelPrices() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(6);
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
	public EReference getFleetCostModel_BaseFuelPrices() {
		return (EReference)fleetCostModelEClass.getEStructuralFeatures().get(0);
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
	public EReference getBaseFuelCost_Index() {
		return (EReference)baseFuelCostEClass.getEStructuralFeatures().get(1);
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
	public EReference getCooldownPrice_Index() {
		return (EReference)cooldownPriceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCooldownPrice_Lumpsum() {
		return (EAttribute)cooldownPriceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCommodityIndex() {
		return commodityIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCharterIndex() {
		return charterIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBaseFuelIndex() {
		return baseFuelIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNamedIndexContainer() {
		return namedIndexContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNamedIndexContainer_Data() {
		return (EReference)namedIndexContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNamedIndexContainer_Units() {
		return (EAttribute)namedIndexContainerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPortsPriceMap() {
		return portsPriceMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPortsPriceMap_Ports() {
		return (EReference)portsPriceMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPortsPriceMap_Index() {
		return (EReference)portsPriceMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPortsExpressionMap() {
		return portsExpressionMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPortsExpressionMap_Ports() {
		return (EReference)portsExpressionMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPortsExpressionMap_Expression() {
		return (EAttribute)portsExpressionMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPortsSplitPriceMap() {
		return portsSplitPriceMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPortsSplitPriceMap_Ports() {
		return (EReference)portsSplitPriceMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPortsSplitPriceMap_IndexH1() {
		return (EReference)portsSplitPriceMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPortsSplitPriceMap_IndexH2() {
		return (EReference)portsSplitPriceMapEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPortsSplitExpressionMap() {
		return portsSplitExpressionMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPortsSplitExpressionMap_Ports() {
		return (EReference)portsSplitExpressionMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPortsSplitExpressionMap_Expression1() {
		return (EAttribute)portsSplitExpressionMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPortsSplitExpressionMap_Expression2() {
		return (EAttribute)portsSplitExpressionMapEClass.getEStructuralFeatures().get(2);
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
		createEReference(pricingModelEClass, PRICING_MODEL__BASE_FUEL_PRICES);

		dataIndexEClass = createEClass(DATA_INDEX);
		createEReference(dataIndexEClass, DATA_INDEX__POINTS);

		derivedIndexEClass = createEClass(DERIVED_INDEX);
		createEAttribute(derivedIndexEClass, DERIVED_INDEX__EXPRESSION);

		indexPointEClass = createEClass(INDEX_POINT);
		createEAttribute(indexPointEClass, INDEX_POINT__DATE);
		createEAttribute(indexPointEClass, INDEX_POINT__VALUE);

		indexEClass = createEClass(INDEX);

		fleetCostModelEClass = createEClass(FLEET_COST_MODEL);
		createEReference(fleetCostModelEClass, FLEET_COST_MODEL__BASE_FUEL_PRICES);

		routeCostEClass = createEClass(ROUTE_COST);
		createEReference(routeCostEClass, ROUTE_COST__ROUTE);
		createEReference(routeCostEClass, ROUTE_COST__VESSEL_CLASS);
		createEAttribute(routeCostEClass, ROUTE_COST__LADEN_COST);
		createEAttribute(routeCostEClass, ROUTE_COST__BALLAST_COST);

		baseFuelCostEClass = createEClass(BASE_FUEL_COST);
		createEReference(baseFuelCostEClass, BASE_FUEL_COST__FUEL);
		createEReference(baseFuelCostEClass, BASE_FUEL_COST__INDEX);

		portCostEClass = createEClass(PORT_COST);
		createEReference(portCostEClass, PORT_COST__PORTS);
		createEReference(portCostEClass, PORT_COST__ENTRIES);
		createEAttribute(portCostEClass, PORT_COST__REFERENCE_CAPACITY);

		portCostEntryEClass = createEClass(PORT_COST_ENTRY);
		createEAttribute(portCostEntryEClass, PORT_COST_ENTRY__ACTIVITY);
		createEAttribute(portCostEntryEClass, PORT_COST_ENTRY__COST);

		cooldownPriceEClass = createEClass(COOLDOWN_PRICE);
		createEReference(cooldownPriceEClass, COOLDOWN_PRICE__INDEX);
		createEAttribute(cooldownPriceEClass, COOLDOWN_PRICE__LUMPSUM);

		commodityIndexEClass = createEClass(COMMODITY_INDEX);

		charterIndexEClass = createEClass(CHARTER_INDEX);

		baseFuelIndexEClass = createEClass(BASE_FUEL_INDEX);

		namedIndexContainerEClass = createEClass(NAMED_INDEX_CONTAINER);
		createEReference(namedIndexContainerEClass, NAMED_INDEX_CONTAINER__DATA);
		createEAttribute(namedIndexContainerEClass, NAMED_INDEX_CONTAINER__UNITS);

		portsPriceMapEClass = createEClass(PORTS_PRICE_MAP);
		createEReference(portsPriceMapEClass, PORTS_PRICE_MAP__PORTS);
		createEReference(portsPriceMapEClass, PORTS_PRICE_MAP__INDEX);

		portsExpressionMapEClass = createEClass(PORTS_EXPRESSION_MAP);
		createEReference(portsExpressionMapEClass, PORTS_EXPRESSION_MAP__PORTS);
		createEAttribute(portsExpressionMapEClass, PORTS_EXPRESSION_MAP__EXPRESSION);

		portsSplitPriceMapEClass = createEClass(PORTS_SPLIT_PRICE_MAP);
		createEReference(portsSplitPriceMapEClass, PORTS_SPLIT_PRICE_MAP__PORTS);
		createEReference(portsSplitPriceMapEClass, PORTS_SPLIT_PRICE_MAP__INDEX_H1);
		createEReference(portsSplitPriceMapEClass, PORTS_SPLIT_PRICE_MAP__INDEX_H2);

		portsSplitExpressionMapEClass = createEClass(PORTS_SPLIT_EXPRESSION_MAP);
		createEReference(portsSplitExpressionMapEClass, PORTS_SPLIT_EXPRESSION_MAP__PORTS);
		createEAttribute(portsSplitExpressionMapEClass, PORTS_SPLIT_EXPRESSION_MAP__EXPRESSION1);
		createEAttribute(portsSplitExpressionMapEClass, PORTS_SPLIT_EXPRESSION_MAP__EXPRESSION2);
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
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);

		// Create type parameters
		ETypeParameter dataIndexEClass_Value = addETypeParameter(dataIndexEClass, "Value");
		ETypeParameter derivedIndexEClass_Value = addETypeParameter(derivedIndexEClass, "Value");
		ETypeParameter indexPointEClass_Value = addETypeParameter(indexPointEClass, "Value");
		ETypeParameter indexEClass_Value = addETypeParameter(indexEClass, "Value");
		ETypeParameter namedIndexContainerEClass_Value = addETypeParameter(namedIndexContainerEClass, "Value");

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
		fleetCostModelEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		routeCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		baseFuelCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		cooldownPriceEClass.getESuperTypes().add(this.getPortsExpressionMap());
		g1 = createEGenericType(this.getNamedIndexContainer());
		g2 = createEGenericType(ecorePackage.getEDoubleObject());
		g1.getETypeArguments().add(g2);
		commodityIndexEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getNamedIndexContainer());
		g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		charterIndexEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getNamedIndexContainer());
		g2 = createEGenericType(ecorePackage.getEDoubleObject());
		g1.getETypeArguments().add(g2);
		baseFuelIndexEClass.getEGenericSuperTypes().add(g1);
		namedIndexContainerEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		namedIndexContainerEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		portsPriceMapEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portsExpressionMapEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portsSplitPriceMapEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portsSplitExpressionMapEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());

		// Initialize classes and features; add operations and parameters
		initEClass(pricingModelEClass, PricingModel.class, "PricingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPricingModel_CommodityIndices(), this.getCommodityIndex(), null, "commodityIndices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_CharterIndices(), this.getCharterIndex(), null, "charterIndices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_FleetCost(), this.getFleetCostModel(), null, "fleetCost", null, 1, 1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_RouteCosts(), this.getRouteCost(), null, "routeCosts", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_PortCosts(), this.getPortCost(), null, "portCosts", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_CooldownPrices(), this.getCooldownPrice(), null, "cooldownPrices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_BaseFuelPrices(), this.getBaseFuelIndex(), null, "baseFuelPrices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
		initEReference(getFleetCostModel_BaseFuelPrices(), this.getBaseFuelCost(), null, "baseFuelPrices", null, 0, -1, FleetCostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(routeCostEClass, RouteCost.class, "RouteCost", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRouteCost_Route(), thePortPackage.getRoute(), null, "route", null, 1, 1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRouteCost_VesselClass(), theFleetPackage.getVesselClass(), null, "vesselClass", null, 1, 1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouteCost_LadenCost(), ecorePackage.getEInt(), "ladenCost", null, 1, 1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouteCost_BallastCost(), ecorePackage.getEInt(), "ballastCost", null, 1, 1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseFuelCostEClass, BaseFuelCost.class, "BaseFuelCost", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBaseFuelCost_Fuel(), theFleetPackage.getBaseFuel(), null, "fuel", null, 1, 1, BaseFuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBaseFuelCost_Index(), this.getBaseFuelIndex(), null, "index", null, 1, 1, BaseFuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portCostEClass, PortCost.class, "PortCost", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortCost_Ports(), g1, null, "ports", null, 0, -1, PortCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortCost_Entries(), this.getPortCostEntry(), null, "entries", null, 0, -1, PortCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortCost_ReferenceCapacity(), ecorePackage.getEInt(), "referenceCapacity", null, 1, 1, PortCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(portCostEClass, ecorePackage.getEInt(), "getPortCost", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theFleetPackage.getVesselClass(), "vesselClass", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theTypesPackage.getPortCapability(), "activity", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(portCostEntryEClass, PortCostEntry.class, "PortCostEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPortCostEntry_Activity(), theTypesPackage.getPortCapability(), "activity", null, 1, 1, PortCostEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortCostEntry_Cost(), ecorePackage.getEInt(), "cost", null, 1, 1, PortCostEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cooldownPriceEClass, CooldownPrice.class, "CooldownPrice", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCooldownPrice_Index(), this.getCommodityIndex(), null, "index", null, 1, 1, CooldownPrice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCooldownPrice_Lumpsum(), ecorePackage.getEBoolean(), "lumpsum", null, 0, 1, CooldownPrice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(commodityIndexEClass, CommodityIndex.class, "CommodityIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(charterIndexEClass, CharterIndex.class, "CharterIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(baseFuelIndexEClass, BaseFuelIndex.class, "BaseFuelIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(namedIndexContainerEClass, NamedIndexContainer.class, "NamedIndexContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getIndex());
		g2 = createEGenericType(namedIndexContainerEClass_Value);
		g1.getETypeArguments().add(g2);
		initEReference(getNamedIndexContainer_Data(), g1, null, "data", null, 0, 1, NamedIndexContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedIndexContainer_Units(), ecorePackage.getEString(), "units", null, 0, 1, NamedIndexContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portsPriceMapEClass, PortsPriceMap.class, "PortsPriceMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortsPriceMap_Ports(), g1, null, "ports", null, 0, -1, PortsPriceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortsPriceMap_Index(), this.getCommodityIndex(), null, "index", null, 1, 1, PortsPriceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portsExpressionMapEClass, PortsExpressionMap.class, "PortsExpressionMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortsExpressionMap_Ports(), g1, null, "ports", null, 0, -1, PortsExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortsExpressionMap_Expression(), ecorePackage.getEString(), "expression", null, 0, 1, PortsExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portsSplitPriceMapEClass, PortsSplitPriceMap.class, "PortsSplitPriceMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortsSplitPriceMap_Ports(), g1, null, "ports", null, 0, -1, PortsSplitPriceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortsSplitPriceMap_IndexH1(), this.getCommodityIndex(), null, "indexH1", null, 1, 1, PortsSplitPriceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortsSplitPriceMap_IndexH2(), this.getCommodityIndex(), null, "indexH2", null, 1, 1, PortsSplitPriceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portsSplitExpressionMapEClass, PortsSplitExpressionMap.class, "PortsSplitExpressionMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortsSplitExpressionMap_Ports(), g1, null, "ports", null, 0, -1, PortsSplitExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortsSplitExpressionMap_Expression1(), ecorePackage.getEString(), "expression1", null, 0, 1, PortsSplitExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortsSplitExpressionMap_Expression2(), ecorePackage.getEString(), "expression2", null, 0, 1, PortsSplitExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
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
		  (getRouteCost_LadenCost(), 
		   source, 
		   new String[] {
			 "unitPrefix", "$",
			 "formatString", "##,###,##0"
		   });		
		addAnnotation
		  (getRouteCost_BallastCost(), 
		   source, 
		   new String[] {
			 "unitPrefix", "$",
			 "formatString", "##,###,##0"
		   });			
		addAnnotation
		  (getPortCost_ReferenceCapacity(), 
		   source, 
		   new String[] {
			 "unit", "m\u00b3",
			 "formatString", "#,###,##0"
		   });		
		addAnnotation
		  (getPortCostEntry_Cost(), 
		   source, 
		   new String[] {
			 "unitPrefix", "$",
			 "formatString", "##,###,##0"
		   });
	}

} //PricingPackageImpl
