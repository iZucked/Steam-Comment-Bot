/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
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
import com.mmxlabs.models.lng.pricing.UnitConversion;
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
	private EClass currencyIndexEClass = null;

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
	private EClass costModelEClass = null;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass panamaCanalTariffEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass panamaCanalTariffBandEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unitConversionEClass = null;

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
		DateTimePackage.eINSTANCE.eClass();
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
	public EReference getPricingModel_CurrencyIndices() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_CommodityIndices() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_CharterIndices() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_BaseFuelPrices() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPricingModel_ConversionFactors() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(4);
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
	public EClass getCurrencyIndex() {
		return currencyIndexEClass;
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
	public EAttribute getCooldownPrice_Lumpsum() {
		return (EAttribute)cooldownPriceEClass.getEStructuralFeatures().get(0);
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
	public EAttribute getNamedIndexContainer_CurrencyUnit() {
		return (EAttribute)namedIndexContainerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNamedIndexContainer_VolumeUnit() {
		return (EAttribute)namedIndexContainerEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCostModel() {
		return costModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCostModel_RouteCosts() {
		return (EReference)costModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCostModel_PortCosts() {
		return (EReference)costModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCostModel_CooldownCosts() {
		return (EReference)costModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCostModel_BaseFuelCosts() {
		return (EReference)costModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCostModel_PanamaCanalTariff() {
		return (EReference)costModelEClass.getEStructuralFeatures().get(4);
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
	public EClass getPanamaCanalTariff() {
		return panamaCanalTariffEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPanamaCanalTariff_Bands() {
		return (EReference)panamaCanalTariffEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPanamaCanalTariff_AvailableFrom() {
		return (EAttribute)panamaCanalTariffEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPanamaCanalTariff_MarkupRate() {
		return (EAttribute)panamaCanalTariffEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPanamaCanalTariffBand() {
		return panamaCanalTariffBandEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPanamaCanalTariffBand_LadenTariff() {
		return (EAttribute)panamaCanalTariffBandEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPanamaCanalTariffBand_BallastTariff() {
		return (EAttribute)panamaCanalTariffBandEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPanamaCanalTariffBand_BallastRoundtripTariff() {
		return (EAttribute)panamaCanalTariffBandEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPanamaCanalTariffBand_BandStart() {
		return (EAttribute)panamaCanalTariffBandEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPanamaCanalTariffBand_BandEnd() {
		return (EAttribute)panamaCanalTariffBandEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUnitConversion() {
		return unitConversionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitConversion_From() {
		return (EAttribute)unitConversionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitConversion_To() {
		return (EAttribute)unitConversionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitConversion_Factor() {
		return (EAttribute)unitConversionEClass.getEStructuralFeatures().get(2);
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
		createEReference(pricingModelEClass, PRICING_MODEL__CURRENCY_INDICES);
		createEReference(pricingModelEClass, PRICING_MODEL__COMMODITY_INDICES);
		createEReference(pricingModelEClass, PRICING_MODEL__CHARTER_INDICES);
		createEReference(pricingModelEClass, PRICING_MODEL__BASE_FUEL_PRICES);
		createEReference(pricingModelEClass, PRICING_MODEL__CONVERSION_FACTORS);

		dataIndexEClass = createEClass(DATA_INDEX);
		createEReference(dataIndexEClass, DATA_INDEX__POINTS);

		derivedIndexEClass = createEClass(DERIVED_INDEX);
		createEAttribute(derivedIndexEClass, DERIVED_INDEX__EXPRESSION);

		indexPointEClass = createEClass(INDEX_POINT);
		createEAttribute(indexPointEClass, INDEX_POINT__DATE);
		createEAttribute(indexPointEClass, INDEX_POINT__VALUE);

		indexEClass = createEClass(INDEX);

		currencyIndexEClass = createEClass(CURRENCY_INDEX);

		commodityIndexEClass = createEClass(COMMODITY_INDEX);

		charterIndexEClass = createEClass(CHARTER_INDEX);

		baseFuelIndexEClass = createEClass(BASE_FUEL_INDEX);

		namedIndexContainerEClass = createEClass(NAMED_INDEX_CONTAINER);
		createEReference(namedIndexContainerEClass, NAMED_INDEX_CONTAINER__DATA);
		createEAttribute(namedIndexContainerEClass, NAMED_INDEX_CONTAINER__CURRENCY_UNIT);
		createEAttribute(namedIndexContainerEClass, NAMED_INDEX_CONTAINER__VOLUME_UNIT);

		costModelEClass = createEClass(COST_MODEL);
		createEReference(costModelEClass, COST_MODEL__ROUTE_COSTS);
		createEReference(costModelEClass, COST_MODEL__PORT_COSTS);
		createEReference(costModelEClass, COST_MODEL__COOLDOWN_COSTS);
		createEReference(costModelEClass, COST_MODEL__BASE_FUEL_COSTS);
		createEReference(costModelEClass, COST_MODEL__PANAMA_CANAL_TARIFF);

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
		createEAttribute(cooldownPriceEClass, COOLDOWN_PRICE__LUMPSUM);

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

		panamaCanalTariffEClass = createEClass(PANAMA_CANAL_TARIFF);
		createEReference(panamaCanalTariffEClass, PANAMA_CANAL_TARIFF__BANDS);
		createEAttribute(panamaCanalTariffEClass, PANAMA_CANAL_TARIFF__AVAILABLE_FROM);
		createEAttribute(panamaCanalTariffEClass, PANAMA_CANAL_TARIFF__MARKUP_RATE);

		panamaCanalTariffBandEClass = createEClass(PANAMA_CANAL_TARIFF_BAND);
		createEAttribute(panamaCanalTariffBandEClass, PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF);
		createEAttribute(panamaCanalTariffBandEClass, PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF);
		createEAttribute(panamaCanalTariffBandEClass, PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF);
		createEAttribute(panamaCanalTariffBandEClass, PANAMA_CANAL_TARIFF_BAND__BAND_START);
		createEAttribute(panamaCanalTariffBandEClass, PANAMA_CANAL_TARIFF_BAND__BAND_END);

		unitConversionEClass = createEClass(UNIT_CONVERSION);
		createEAttribute(unitConversionEClass, UNIT_CONVERSION__FROM);
		createEAttribute(unitConversionEClass, UNIT_CONVERSION__TO);
		createEAttribute(unitConversionEClass, UNIT_CONVERSION__FACTOR);
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
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
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
		g1 = createEGenericType(this.getNamedIndexContainer());
		g2 = createEGenericType(ecorePackage.getEDoubleObject());
		g1.getETypeArguments().add(g2);
		currencyIndexEClass.getEGenericSuperTypes().add(g1);
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
		costModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		routeCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		baseFuelCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		cooldownPriceEClass.getESuperTypes().add(this.getPortsExpressionMap());
		portsPriceMapEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portsExpressionMapEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portsSplitPriceMapEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portsSplitExpressionMapEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());

		// Initialize classes and features; add operations and parameters
		initEClass(pricingModelEClass, PricingModel.class, "PricingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPricingModel_CurrencyIndices(), this.getCurrencyIndex(), null, "currencyIndices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_CommodityIndices(), this.getCommodityIndex(), null, "commodityIndices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_CharterIndices(), this.getCharterIndex(), null, "charterIndices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_BaseFuelPrices(), this.getBaseFuelIndex(), null, "baseFuelPrices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_ConversionFactors(), this.getUnitConversion(), null, "conversionFactors", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataIndexEClass, DataIndex.class, "DataIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getIndexPoint());
		g2 = createEGenericType(dataIndexEClass_Value);
		g1.getETypeArguments().add(g2);
		initEReference(getDataIndex_Points(), g1, null, "points", null, 0, -1, DataIndex.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(derivedIndexEClass, DerivedIndex.class, "DerivedIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDerivedIndex_Expression(), ecorePackage.getEString(), "expression", null, 1, 1, DerivedIndex.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indexPointEClass, IndexPoint.class, "IndexPoint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIndexPoint_Date(), theDateTimePackage.getYearMonth(), "date", null, 0, 1, IndexPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(indexPointEClass_Value);
		initEAttribute(getIndexPoint_Value(), g1, "value", null, 1, 1, IndexPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indexEClass, Index.class, "Index", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = addEOperation(indexEClass, null, "getValueForMonth", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theDateTimePackage.getYearMonth(), "date", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(indexEClass_Value);
		initEOperation(op, g1);

		addEOperation(indexEClass, theDateTimePackage.getYearMonth(), "getDates", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(indexEClass, null, "getForwardValueForMonth", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theDateTimePackage.getYearMonth(), "date", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(indexEClass_Value);
		initEOperation(op, g1);

		op = addEOperation(indexEClass, null, "getBackwardsValueForMonth", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theDateTimePackage.getYearMonth(), "date", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(indexEClass_Value);
		initEOperation(op, g1);

		initEClass(currencyIndexEClass, CurrencyIndex.class, "CurrencyIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(commodityIndexEClass, CommodityIndex.class, "CommodityIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(charterIndexEClass, CharterIndex.class, "CharterIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(baseFuelIndexEClass, BaseFuelIndex.class, "BaseFuelIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(namedIndexContainerEClass, NamedIndexContainer.class, "NamedIndexContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getIndex());
		g2 = createEGenericType(namedIndexContainerEClass_Value);
		g1.getETypeArguments().add(g2);
		initEReference(getNamedIndexContainer_Data(), g1, null, "data", null, 1, 1, NamedIndexContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedIndexContainer_CurrencyUnit(), ecorePackage.getEString(), "currencyUnit", null, 0, 1, NamedIndexContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedIndexContainer_VolumeUnit(), ecorePackage.getEString(), "volumeUnit", null, 0, 1, NamedIndexContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(costModelEClass, CostModel.class, "CostModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCostModel_RouteCosts(), this.getRouteCost(), null, "routeCosts", null, 0, -1, CostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCostModel_PortCosts(), this.getPortCost(), null, "portCosts", null, 0, -1, CostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCostModel_CooldownCosts(), this.getCooldownPrice(), null, "cooldownCosts", null, 0, -1, CostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCostModel_BaseFuelCosts(), this.getBaseFuelCost(), null, "baseFuelCosts", null, 0, -1, CostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCostModel_PanamaCanalTariff(), this.getPanamaCanalTariff(), null, "panamaCanalTariff", null, 0, 1, CostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
		initEAttribute(getCooldownPrice_Lumpsum(), ecorePackage.getEBoolean(), "lumpsum", null, 0, 1, CooldownPrice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portsPriceMapEClass, PortsPriceMap.class, "PortsPriceMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortsPriceMap_Ports(), g1, null, "ports", null, 1, -1, PortsPriceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortsPriceMap_Index(), this.getCommodityIndex(), null, "index", null, 1, 1, PortsPriceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portsExpressionMapEClass, PortsExpressionMap.class, "PortsExpressionMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortsExpressionMap_Ports(), g1, null, "ports", null, 1, -1, PortsExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortsExpressionMap_Expression(), ecorePackage.getEString(), "expression", null, 1, 1, PortsExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portsSplitPriceMapEClass, PortsSplitPriceMap.class, "PortsSplitPriceMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortsSplitPriceMap_Ports(), g1, null, "ports", null, 1, -1, PortsSplitPriceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortsSplitPriceMap_IndexH1(), this.getCommodityIndex(), null, "indexH1", null, 1, 1, PortsSplitPriceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortsSplitPriceMap_IndexH2(), this.getCommodityIndex(), null, "indexH2", null, 1, 1, PortsSplitPriceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portsSplitExpressionMapEClass, PortsSplitExpressionMap.class, "PortsSplitExpressionMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortsSplitExpressionMap_Ports(), g1, null, "ports", null, 1, -1, PortsSplitExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortsSplitExpressionMap_Expression1(), ecorePackage.getEString(), "expression1", null, 1, 1, PortsSplitExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortsSplitExpressionMap_Expression2(), ecorePackage.getEString(), "expression2", null, 1, 1, PortsSplitExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(panamaCanalTariffEClass, PanamaCanalTariff.class, "PanamaCanalTariff", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPanamaCanalTariff_Bands(), this.getPanamaCanalTariffBand(), null, "bands", null, 0, -1, PanamaCanalTariff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPanamaCanalTariff_AvailableFrom(), theDateTimePackage.getLocalDate(), "availableFrom", null, 0, 1, PanamaCanalTariff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPanamaCanalTariff_MarkupRate(), ecorePackage.getEDouble(), "markupRate", null, 0, 1, PanamaCanalTariff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(panamaCanalTariffBandEClass, PanamaCanalTariffBand.class, "PanamaCanalTariffBand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPanamaCanalTariffBand_LadenTariff(), ecorePackage.getEDouble(), "ladenTariff", null, 0, 1, PanamaCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPanamaCanalTariffBand_BallastTariff(), ecorePackage.getEDouble(), "ballastTariff", null, 0, 1, PanamaCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPanamaCanalTariffBand_BallastRoundtripTariff(), ecorePackage.getEDouble(), "ballastRoundtripTariff", null, 0, 1, PanamaCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPanamaCanalTariffBand_BandStart(), ecorePackage.getEInt(), "bandStart", null, 0, 1, PanamaCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPanamaCanalTariffBand_BandEnd(), ecorePackage.getEInt(), "bandEnd", null, 0, 1, PanamaCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unitConversionEClass, UnitConversion.class, "UnitConversion", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUnitConversion_From(), ecorePackage.getEString(), "from", null, 0, 1, UnitConversion.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitConversion_To(), ecorePackage.getEString(), "to", null, 0, 1, UnitConversion.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitConversion_Factor(), ecorePackage.getEDouble(), "factor", null, 0, 1, UnitConversion.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
		addAnnotation
		  (getPanamaCanalTariff_MarkupRate(), 
		   source, 
		   new String[] {
			 "scale", "100",
			 "formatString", "##0.#",
			 "unit", "%"
		   });	
		addAnnotation
		  (getPanamaCanalTariffBand_LadenTariff(), 
		   source, 
		   new String[] {
			 "unitPrefix", "$",
			 "formatString", "#0.###"
		   });	
		addAnnotation
		  (getPanamaCanalTariffBand_BallastTariff(), 
		   source, 
		   new String[] {
			 "unitPrefix", "$",
			 "formatString", "#0.###"
		   });	
		addAnnotation
		  (getPanamaCanalTariffBand_BallastRoundtripTariff(), 
		   source, 
		   new String[] {
			 "unitPrefix", "$",
			 "formatString", "#0.###"
		   });	
		addAnnotation
		  (getPanamaCanalTariffBand_BandStart(), 
		   source, 
		   new String[] {
			 "unitSuffix", "m3\r\n",
			 "formatString", "##,###,##0"
		   });	
		addAnnotation
		  (getPanamaCanalTariffBand_BandEnd(), 
		   source, 
		   new String[] {
			 "unitSuffix", "m3\r\n",
			 "formatString", "##,###,##0"
		   });	
		addAnnotation
		  (getUnitConversion_Factor(), 
		   source, 
		   new String[] {
			 "formatString", "######0.######"
		   });
		addAnnotation
		  (getUnitConversion_Factor(), 
		   new boolean[] { true },
		   "http://www.mmxlabs.com/models/ui/numberFormat",
		   new String[] {
			 "scale", "100",
			 "formatString", "##0.#",
			 "unit", "%"
		   });
	}

} //PricingPackageImpl
