/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
	 * @since 5.0
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
	public EAttribute getBaseFuelCost_Price() {
		return (EAttribute)baseFuelCostEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBaseFuelCost_Index() {
		return (EReference)baseFuelCostEClass.getEStructuralFeatures().get(2);
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
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCommodityIndex() {
		return commodityIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommodityIndex_Data() {
		return (EReference)commodityIndexEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCharterIndex() {
		return charterIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCharterIndex_Data() {
		return (EReference)charterIndexEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBaseFuelIndex() {
		return baseFuelIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNamedIndexContainer() {
		return namedIndexContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
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
		createEAttribute(baseFuelCostEClass, BASE_FUEL_COST__PRICE);
		createEReference(baseFuelCostEClass, BASE_FUEL_COST__INDEX);

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

		commodityIndexEClass = createEClass(COMMODITY_INDEX);
		createEReference(commodityIndexEClass, COMMODITY_INDEX__DATA);

		charterIndexEClass = createEClass(CHARTER_INDEX);
		createEReference(charterIndexEClass, CHARTER_INDEX__DATA);

		baseFuelIndexEClass = createEClass(BASE_FUEL_INDEX);

		namedIndexContainerEClass = createEClass(NAMED_INDEX_CONTAINER);
		createEReference(namedIndexContainerEClass, NAMED_INDEX_CONTAINER__DATA);
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
		indexEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		indexEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		fleetCostModelEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		routeCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		baseFuelCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		cooldownPriceEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		commodityIndexEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		commodityIndexEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		charterIndexEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		charterIndexEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		g1 = createEGenericType(this.getNamedIndexContainer());
		g2 = createEGenericType(ecorePackage.getEDoubleObject());
		g1.getETypeArguments().add(g2);
		baseFuelIndexEClass.getEGenericSuperTypes().add(g1);
		namedIndexContainerEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		namedIndexContainerEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());

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
		initEAttribute(getBaseFuelCost_Price(), ecorePackage.getEDouble(), "price", null, 1, 1, BaseFuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
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
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getCooldownPrice_Ports(), g1, null, "ports", null, 0, -1, CooldownPrice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCooldownPrice_Index(), this.getCommodityIndex(), null, "index", null, 1, 1, CooldownPrice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(commodityIndexEClass, CommodityIndex.class, "CommodityIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getIndex());
		g2 = createEGenericType(ecorePackage.getEDoubleObject());
		g1.getETypeArguments().add(g2);
		initEReference(getCommodityIndex_Data(), g1, null, "data", null, 0, 1, CommodityIndex.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(charterIndexEClass, CharterIndex.class, "CharterIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getIndex());
		g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		initEReference(getCharterIndex_Data(), g1, null, "data", null, 0, 1, CharterIndex.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseFuelIndexEClass, BaseFuelIndex.class, "BaseFuelIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		addEOperation(baseFuelIndexEClass, ecorePackage.getEDouble(), "getPrice", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(namedIndexContainerEClass, NamedIndexContainer.class, "NamedIndexContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getIndex());
		g2 = createEGenericType(namedIndexContainerEClass_Value);
		g1.getETypeArguments().add(g2);
		initEReference(getNamedIndexContainer_Data(), g1, null, "data", null, 0, 1, NamedIndexContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //PricingPackageImpl
