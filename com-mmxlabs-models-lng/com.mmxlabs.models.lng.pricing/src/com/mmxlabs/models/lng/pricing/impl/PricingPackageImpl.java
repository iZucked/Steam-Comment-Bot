/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DatePoint;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.HolidayCalendarEntry;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PortsExpressionMap;
import com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.YearMonthPointContainer;
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
	private EClass costModelEClass = null;

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
	private EClass suezCanalTugBandEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass suezCanalTariffEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass suezCanalTariffBandEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unitConversionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass datePointContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass datePointEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass yearMonthPointContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass yearMonthPointEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractYearMonthCurveEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass commodityCurveEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass charterCurveEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass bunkerFuelCurveEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass currencyCurveEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass marketIndexEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass holidayCalendarEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass holidayCalendarEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass settleStrategyEClass = null;

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
		Object registeredPricingPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		PricingPackageImpl thePricingPackage = registeredPricingPackage instanceof PricingPackageImpl ? (PricingPackageImpl)registeredPricingPackage : new PricingPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		DateTimePackage.eINSTANCE.eClass();
		FleetPackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();
		PortPackage.eINSTANCE.eClass();

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
	@Override
	public EClass getPricingModel() {
		return pricingModelEClass;
	}

	

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPricingModel_CurrencyCurves() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPricingModel_CommodityCurves() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPricingModel_CharterCurves() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPricingModel_BunkerFuelCurves() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPricingModel_ConversionFactors() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPricingModel_MarketCurveDataVersion() {
		return (EAttribute)pricingModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPricingModel_SettledPrices() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPricingModel_MarketIndices() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPricingModel_HolidayCalendars() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPricingModel_SettleStrategies() {
		return (EReference)pricingModelEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDataIndex() {
		return dataIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataIndex_Points() {
		return (EReference)dataIndexEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDerivedIndex() {
		return derivedIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDerivedIndex_Expression() {
		return (EAttribute)derivedIndexEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIndexPoint() {
		return indexPointEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getIndexPoint_Date() {
		return (EAttribute)indexPointEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getIndexPoint_Value() {
		return (EAttribute)indexPointEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIndex() {
		return indexEClass;
	}

	

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCostModel() {
		return costModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRouteCost_RouteOption() {
		return (EAttribute)routeCostEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRouteCost_Vessels() {
		return (EReference)routeCostEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRouteCost_LadenCost() {
		return (EAttribute)routeCostEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRouteCost_BallastCost() {
		return (EAttribute)routeCostEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBaseFuelCost() {
		return baseFuelCostEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBaseFuelCost_Fuel() {
		return (EReference)baseFuelCostEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseFuelCost_Expression() {
		return (EAttribute)baseFuelCostEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPortCost() {
		return portCostEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortCost_Ports() {
		return (EReference)portCostEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortCost_Entries() {
		return (EReference)portCostEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPortCost_ReferenceCapacity() {
		return (EAttribute)portCostEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPortCostEntry() {
		return portCostEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPortCostEntry_Activity() {
		return (EAttribute)portCostEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPortCostEntry_Cost() {
		return (EAttribute)portCostEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCooldownPrice() {
		return cooldownPriceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCooldownPrice_Lumpsum() {
		return (EAttribute)cooldownPriceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCostModel_RouteCosts() {
		return (EReference)costModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCostModel_PortCosts() {
		return (EReference)costModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCostModel_CooldownCosts() {
		return (EReference)costModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCostModel_BaseFuelCosts() {
		return (EReference)costModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCostModel_PanamaCanalTariff() {
		return (EReference)costModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCostModel_SuezCanalTariff() {
		return (EReference)costModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRouteCost() {
		return routeCostEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPortsExpressionMap() {
		return portsExpressionMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortsExpressionMap_Ports() {
		return (EReference)portsExpressionMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPortsExpressionMap_Expression() {
		return (EAttribute)portsExpressionMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPortsSplitExpressionMap() {
		return portsSplitExpressionMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortsSplitExpressionMap_Ports() {
		return (EReference)portsSplitExpressionMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPortsSplitExpressionMap_Expression1() {
		return (EAttribute)portsSplitExpressionMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPortsSplitExpressionMap_Expression2() {
		return (EAttribute)portsSplitExpressionMapEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPanamaCanalTariff() {
		return panamaCanalTariffEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPanamaCanalTariff_Bands() {
		return (EReference)panamaCanalTariffEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPanamaCanalTariff_MarkupRate() {
		return (EAttribute)panamaCanalTariffEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPanamaCanalTariffBand() {
		return panamaCanalTariffBandEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPanamaCanalTariffBand_LadenTariff() {
		return (EAttribute)panamaCanalTariffBandEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPanamaCanalTariffBand_BallastTariff() {
		return (EAttribute)panamaCanalTariffBandEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPanamaCanalTariffBand_BallastRoundtripTariff() {
		return (EAttribute)panamaCanalTariffBandEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPanamaCanalTariffBand_BandStart() {
		return (EAttribute)panamaCanalTariffBandEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPanamaCanalTariffBand_BandEnd() {
		return (EAttribute)panamaCanalTariffBandEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSuezCanalTugBand() {
		return suezCanalTugBandEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSuezCanalTugBand_Tugs() {
		return (EAttribute)suezCanalTugBandEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSuezCanalTugBand_BandStart() {
		return (EAttribute)suezCanalTugBandEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSuezCanalTugBand_BandEnd() {
		return (EAttribute)suezCanalTugBandEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSuezCanalTariff() {
		return suezCanalTariffEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSuezCanalTariff_Bands() {
		return (EReference)suezCanalTariffEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSuezCanalTariff_TugBands() {
		return (EReference)suezCanalTariffEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSuezCanalTariff_TugCost() {
		return (EAttribute)suezCanalTariffEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSuezCanalTariff_FixedCosts() {
		return (EAttribute)suezCanalTariffEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSuezCanalTariff_DiscountFactor() {
		return (EAttribute)suezCanalTariffEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSuezCanalTariff_SdrToUSD() {
		return (EAttribute)suezCanalTariffEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSuezCanalTariffBand() {
		return suezCanalTariffBandEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSuezCanalTariffBand_LadenTariff() {
		return (EAttribute)suezCanalTariffBandEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSuezCanalTariffBand_BallastTariff() {
		return (EAttribute)suezCanalTariffBandEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSuezCanalTariffBand_BandStart() {
		return (EAttribute)suezCanalTariffBandEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSuezCanalTariffBand_BandEnd() {
		return (EAttribute)suezCanalTariffBandEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getUnitConversion() {
		return unitConversionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitConversion_From() {
		return (EAttribute)unitConversionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitConversion_To() {
		return (EAttribute)unitConversionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUnitConversion_Factor() {
		return (EAttribute)unitConversionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDatePointContainer() {
		return datePointContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDatePointContainer_Points() {
		return (EReference)datePointContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDatePoint() {
		return datePointEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDatePoint_Date() {
		return (EAttribute)datePointEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDatePoint_Value() {
		return (EAttribute)datePointEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getYearMonthPointContainer() {
		return yearMonthPointContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getYearMonthPointContainer_Points() {
		return (EReference)yearMonthPointContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getYearMonthPoint() {
		return yearMonthPointEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getYearMonthPoint_Date() {
		return (EAttribute)yearMonthPointEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getYearMonthPoint_Value() {
		return (EAttribute)yearMonthPointEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAbstractYearMonthCurve() {
		return abstractYearMonthCurveEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractYearMonthCurve_CurrencyUnit() {
		return (EAttribute)abstractYearMonthCurveEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractYearMonthCurve_VolumeUnit() {
		return (EAttribute)abstractYearMonthCurveEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractYearMonthCurve_Expression() {
		return (EAttribute)abstractYearMonthCurveEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCommodityCurve() {
		return commodityCurveEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCommodityCurve_MarketIndex() {
		return (EReference)commodityCurveEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCharterCurve() {
		return charterCurveEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBunkerFuelCurve() {
		return bunkerFuelCurveEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCurrencyCurve() {
		return currencyCurveEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMarketIndex() {
		return marketIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMarketIndex_SettleCalendar() {
		return (EReference)marketIndexEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getHolidayCalendarEntry() {
		return holidayCalendarEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getHolidayCalendarEntry_Date() {
		return (EAttribute)holidayCalendarEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getHolidayCalendarEntry_Comment() {
		return (EAttribute)holidayCalendarEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getHolidayCalendar() {
		return holidayCalendarEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getHolidayCalendar_Entries() {
		return (EReference)holidayCalendarEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getHolidayCalendar_Description() {
		return (EAttribute)holidayCalendarEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSettleStrategy() {
		return settleStrategyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSettleStrategy_DayOfTheMonth() {
		return (EAttribute)settleStrategyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSettleStrategy_LastDayOfTheMonth() {
		return (EAttribute)settleStrategyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSettleStrategy_UseCalendarMonth() {
		return (EAttribute)settleStrategyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSettleStrategy_SettlePeriod() {
		return (EAttribute)settleStrategyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSettleStrategy_SettlePeriodUnit() {
		return (EAttribute)settleStrategyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSettleStrategy_SettleStartMonthsPrior() {
		return (EAttribute)settleStrategyEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
		createEReference(pricingModelEClass, PRICING_MODEL__CURRENCY_CURVES);
		createEReference(pricingModelEClass, PRICING_MODEL__COMMODITY_CURVES);
		createEReference(pricingModelEClass, PRICING_MODEL__CHARTER_CURVES);
		createEReference(pricingModelEClass, PRICING_MODEL__BUNKER_FUEL_CURVES);
		createEReference(pricingModelEClass, PRICING_MODEL__CONVERSION_FACTORS);
		createEAttribute(pricingModelEClass, PRICING_MODEL__MARKET_CURVE_DATA_VERSION);
		createEReference(pricingModelEClass, PRICING_MODEL__SETTLED_PRICES);
		createEReference(pricingModelEClass, PRICING_MODEL__MARKET_INDICES);
		createEReference(pricingModelEClass, PRICING_MODEL__HOLIDAY_CALENDARS);
		createEReference(pricingModelEClass, PRICING_MODEL__SETTLE_STRATEGIES);

		dataIndexEClass = createEClass(DATA_INDEX);
		createEReference(dataIndexEClass, DATA_INDEX__POINTS);

		derivedIndexEClass = createEClass(DERIVED_INDEX);
		createEAttribute(derivedIndexEClass, DERIVED_INDEX__EXPRESSION);

		indexPointEClass = createEClass(INDEX_POINT);
		createEAttribute(indexPointEClass, INDEX_POINT__DATE);
		createEAttribute(indexPointEClass, INDEX_POINT__VALUE);

		indexEClass = createEClass(INDEX);

		costModelEClass = createEClass(COST_MODEL);
		createEReference(costModelEClass, COST_MODEL__ROUTE_COSTS);
		createEReference(costModelEClass, COST_MODEL__PORT_COSTS);
		createEReference(costModelEClass, COST_MODEL__COOLDOWN_COSTS);
		createEReference(costModelEClass, COST_MODEL__BASE_FUEL_COSTS);
		createEReference(costModelEClass, COST_MODEL__PANAMA_CANAL_TARIFF);
		createEReference(costModelEClass, COST_MODEL__SUEZ_CANAL_TARIFF);

		routeCostEClass = createEClass(ROUTE_COST);
		createEAttribute(routeCostEClass, ROUTE_COST__ROUTE_OPTION);
		createEReference(routeCostEClass, ROUTE_COST__VESSELS);
		createEAttribute(routeCostEClass, ROUTE_COST__LADEN_COST);
		createEAttribute(routeCostEClass, ROUTE_COST__BALLAST_COST);

		baseFuelCostEClass = createEClass(BASE_FUEL_COST);
		createEReference(baseFuelCostEClass, BASE_FUEL_COST__FUEL);
		createEAttribute(baseFuelCostEClass, BASE_FUEL_COST__EXPRESSION);

		portCostEClass = createEClass(PORT_COST);
		createEReference(portCostEClass, PORT_COST__PORTS);
		createEReference(portCostEClass, PORT_COST__ENTRIES);
		createEAttribute(portCostEClass, PORT_COST__REFERENCE_CAPACITY);

		portCostEntryEClass = createEClass(PORT_COST_ENTRY);
		createEAttribute(portCostEntryEClass, PORT_COST_ENTRY__ACTIVITY);
		createEAttribute(portCostEntryEClass, PORT_COST_ENTRY__COST);

		cooldownPriceEClass = createEClass(COOLDOWN_PRICE);
		createEAttribute(cooldownPriceEClass, COOLDOWN_PRICE__LUMPSUM);

		portsExpressionMapEClass = createEClass(PORTS_EXPRESSION_MAP);
		createEReference(portsExpressionMapEClass, PORTS_EXPRESSION_MAP__PORTS);
		createEAttribute(portsExpressionMapEClass, PORTS_EXPRESSION_MAP__EXPRESSION);

		portsSplitExpressionMapEClass = createEClass(PORTS_SPLIT_EXPRESSION_MAP);
		createEReference(portsSplitExpressionMapEClass, PORTS_SPLIT_EXPRESSION_MAP__PORTS);
		createEAttribute(portsSplitExpressionMapEClass, PORTS_SPLIT_EXPRESSION_MAP__EXPRESSION1);
		createEAttribute(portsSplitExpressionMapEClass, PORTS_SPLIT_EXPRESSION_MAP__EXPRESSION2);

		panamaCanalTariffEClass = createEClass(PANAMA_CANAL_TARIFF);
		createEReference(panamaCanalTariffEClass, PANAMA_CANAL_TARIFF__BANDS);
		createEAttribute(panamaCanalTariffEClass, PANAMA_CANAL_TARIFF__MARKUP_RATE);

		panamaCanalTariffBandEClass = createEClass(PANAMA_CANAL_TARIFF_BAND);
		createEAttribute(panamaCanalTariffBandEClass, PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF);
		createEAttribute(panamaCanalTariffBandEClass, PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF);
		createEAttribute(panamaCanalTariffBandEClass, PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF);
		createEAttribute(panamaCanalTariffBandEClass, PANAMA_CANAL_TARIFF_BAND__BAND_START);
		createEAttribute(panamaCanalTariffBandEClass, PANAMA_CANAL_TARIFF_BAND__BAND_END);

		suezCanalTugBandEClass = createEClass(SUEZ_CANAL_TUG_BAND);
		createEAttribute(suezCanalTugBandEClass, SUEZ_CANAL_TUG_BAND__TUGS);
		createEAttribute(suezCanalTugBandEClass, SUEZ_CANAL_TUG_BAND__BAND_START);
		createEAttribute(suezCanalTugBandEClass, SUEZ_CANAL_TUG_BAND__BAND_END);

		suezCanalTariffEClass = createEClass(SUEZ_CANAL_TARIFF);
		createEReference(suezCanalTariffEClass, SUEZ_CANAL_TARIFF__BANDS);
		createEReference(suezCanalTariffEClass, SUEZ_CANAL_TARIFF__TUG_BANDS);
		createEAttribute(suezCanalTariffEClass, SUEZ_CANAL_TARIFF__TUG_COST);
		createEAttribute(suezCanalTariffEClass, SUEZ_CANAL_TARIFF__FIXED_COSTS);
		createEAttribute(suezCanalTariffEClass, SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR);
		createEAttribute(suezCanalTariffEClass, SUEZ_CANAL_TARIFF__SDR_TO_USD);

		suezCanalTariffBandEClass = createEClass(SUEZ_CANAL_TARIFF_BAND);
		createEAttribute(suezCanalTariffBandEClass, SUEZ_CANAL_TARIFF_BAND__LADEN_TARIFF);
		createEAttribute(suezCanalTariffBandEClass, SUEZ_CANAL_TARIFF_BAND__BALLAST_TARIFF);
		createEAttribute(suezCanalTariffBandEClass, SUEZ_CANAL_TARIFF_BAND__BAND_START);
		createEAttribute(suezCanalTariffBandEClass, SUEZ_CANAL_TARIFF_BAND__BAND_END);

		unitConversionEClass = createEClass(UNIT_CONVERSION);
		createEAttribute(unitConversionEClass, UNIT_CONVERSION__FROM);
		createEAttribute(unitConversionEClass, UNIT_CONVERSION__TO);
		createEAttribute(unitConversionEClass, UNIT_CONVERSION__FACTOR);

		datePointContainerEClass = createEClass(DATE_POINT_CONTAINER);
		createEReference(datePointContainerEClass, DATE_POINT_CONTAINER__POINTS);

		datePointEClass = createEClass(DATE_POINT);
		createEAttribute(datePointEClass, DATE_POINT__DATE);
		createEAttribute(datePointEClass, DATE_POINT__VALUE);

		yearMonthPointContainerEClass = createEClass(YEAR_MONTH_POINT_CONTAINER);
		createEReference(yearMonthPointContainerEClass, YEAR_MONTH_POINT_CONTAINER__POINTS);

		yearMonthPointEClass = createEClass(YEAR_MONTH_POINT);
		createEAttribute(yearMonthPointEClass, YEAR_MONTH_POINT__DATE);
		createEAttribute(yearMonthPointEClass, YEAR_MONTH_POINT__VALUE);

		abstractYearMonthCurveEClass = createEClass(ABSTRACT_YEAR_MONTH_CURVE);
		createEAttribute(abstractYearMonthCurveEClass, ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT);
		createEAttribute(abstractYearMonthCurveEClass, ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT);
		createEAttribute(abstractYearMonthCurveEClass, ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION);

		commodityCurveEClass = createEClass(COMMODITY_CURVE);
		createEReference(commodityCurveEClass, COMMODITY_CURVE__MARKET_INDEX);

		charterCurveEClass = createEClass(CHARTER_CURVE);

		bunkerFuelCurveEClass = createEClass(BUNKER_FUEL_CURVE);

		currencyCurveEClass = createEClass(CURRENCY_CURVE);

		marketIndexEClass = createEClass(MARKET_INDEX);
		createEReference(marketIndexEClass, MARKET_INDEX__SETTLE_CALENDAR);

		holidayCalendarEntryEClass = createEClass(HOLIDAY_CALENDAR_ENTRY);
		createEAttribute(holidayCalendarEntryEClass, HOLIDAY_CALENDAR_ENTRY__DATE);
		createEAttribute(holidayCalendarEntryEClass, HOLIDAY_CALENDAR_ENTRY__COMMENT);

		holidayCalendarEClass = createEClass(HOLIDAY_CALENDAR);
		createEReference(holidayCalendarEClass, HOLIDAY_CALENDAR__ENTRIES);
		createEAttribute(holidayCalendarEClass, HOLIDAY_CALENDAR__DESCRIPTION);

		settleStrategyEClass = createEClass(SETTLE_STRATEGY);
		createEAttribute(settleStrategyEClass, SETTLE_STRATEGY__DAY_OF_THE_MONTH);
		createEAttribute(settleStrategyEClass, SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH);
		createEAttribute(settleStrategyEClass, SETTLE_STRATEGY__USE_CALENDAR_MONTH);
		createEAttribute(settleStrategyEClass, SETTLE_STRATEGY__SETTLE_PERIOD);
		createEAttribute(settleStrategyEClass, SETTLE_STRATEGY__SETTLE_PERIOD_UNIT);
		createEAttribute(settleStrategyEClass, SETTLE_STRATEGY__SETTLE_START_MONTHS_PRIOR);
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
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);

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
		costModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		routeCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		baseFuelCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portCostEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		cooldownPriceEClass.getESuperTypes().add(this.getPortsExpressionMap());
		portsExpressionMapEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portsSplitExpressionMapEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		datePointContainerEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		datePointContainerEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		yearMonthPointContainerEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		yearMonthPointContainerEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		abstractYearMonthCurveEClass.getESuperTypes().add(this.getYearMonthPointContainer());
		commodityCurveEClass.getESuperTypes().add(this.getAbstractYearMonthCurve());
		charterCurveEClass.getESuperTypes().add(this.getAbstractYearMonthCurve());
		bunkerFuelCurveEClass.getESuperTypes().add(this.getAbstractYearMonthCurve());
		currencyCurveEClass.getESuperTypes().add(this.getAbstractYearMonthCurve());
		marketIndexEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		holidayCalendarEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		settleStrategyEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());

		// Initialize classes and features; add operations and parameters
		initEClass(pricingModelEClass, PricingModel.class, "PricingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPricingModel_CurrencyCurves(), this.getCurrencyCurve(), null, "currencyCurves", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_CommodityCurves(), this.getCommodityCurve(), null, "commodityCurves", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_CharterCurves(), this.getCharterCurve(), null, "charterCurves", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_BunkerFuelCurves(), this.getBunkerFuelCurve(), null, "bunkerFuelCurves", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_ConversionFactors(), this.getUnitConversion(), null, "conversionFactors", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPricingModel_MarketCurveDataVersion(), ecorePackage.getEString(), "marketCurveDataVersion", null, 0, 1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_SettledPrices(), this.getDatePointContainer(), null, "settledPrices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_MarketIndices(), this.getMarketIndex(), null, "marketIndices", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_HolidayCalendars(), this.getHolidayCalendar(), null, "holidayCalendars", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPricingModel_SettleStrategies(), this.getSettleStrategy(), null, "settleStrategies", null, 0, -1, PricingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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

		initEClass(costModelEClass, CostModel.class, "CostModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCostModel_RouteCosts(), this.getRouteCost(), null, "routeCosts", null, 0, -1, CostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCostModel_PortCosts(), this.getPortCost(), null, "portCosts", null, 0, -1, CostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCostModel_CooldownCosts(), this.getCooldownPrice(), null, "cooldownCosts", null, 0, -1, CostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCostModel_BaseFuelCosts(), this.getBaseFuelCost(), null, "baseFuelCosts", null, 0, -1, CostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCostModel_PanamaCanalTariff(), this.getPanamaCanalTariff(), null, "panamaCanalTariff", null, 0, 1, CostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCostModel_SuezCanalTariff(), this.getSuezCanalTariff(), null, "suezCanalTariff", null, 0, 1, CostModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(routeCostEClass, RouteCost.class, "RouteCost", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRouteCost_RouteOption(), thePortPackage.getRouteOption(), "routeOption", null, 0, 1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(theFleetPackage.getVessel());
		g1.getETypeArguments().add(g2);
		initEReference(getRouteCost_Vessels(), g1, null, "vessels", null, 1, -1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouteCost_LadenCost(), ecorePackage.getEInt(), "ladenCost", null, 1, 1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouteCost_BallastCost(), ecorePackage.getEInt(), "ballastCost", null, 1, 1, RouteCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseFuelCostEClass, BaseFuelCost.class, "BaseFuelCost", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBaseFuelCost_Fuel(), theFleetPackage.getBaseFuel(), null, "fuel", null, 1, 1, BaseFuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseFuelCost_Expression(), ecorePackage.getEString(), "expression", null, 0, 1, BaseFuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portCostEClass, PortCost.class, "PortCost", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortCost_Ports(), g1, null, "ports", null, 0, -1, PortCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortCost_Entries(), this.getPortCostEntry(), null, "entries", null, 0, -1, PortCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortCost_ReferenceCapacity(), ecorePackage.getEInt(), "referenceCapacity", null, 1, 1, PortCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(portCostEClass, ecorePackage.getEInt(), "getPortCost", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theFleetPackage.getVessel(), "vessel", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theTypesPackage.getPortCapability(), "activity", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(portCostEntryEClass, PortCostEntry.class, "PortCostEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPortCostEntry_Activity(), theTypesPackage.getPortCapability(), "activity", null, 1, 1, PortCostEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortCostEntry_Cost(), ecorePackage.getEInt(), "cost", null, 1, 1, PortCostEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cooldownPriceEClass, CooldownPrice.class, "CooldownPrice", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCooldownPrice_Lumpsum(), ecorePackage.getEBoolean(), "lumpsum", null, 0, 1, CooldownPrice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portsExpressionMapEClass, PortsExpressionMap.class, "PortsExpressionMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortsExpressionMap_Ports(), g1, null, "ports", null, 1, -1, PortsExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortsExpressionMap_Expression(), ecorePackage.getEString(), "expression", null, 1, 1, PortsExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portsSplitExpressionMapEClass, PortsSplitExpressionMap.class, "PortsSplitExpressionMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortsSplitExpressionMap_Ports(), g1, null, "ports", null, 1, -1, PortsSplitExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortsSplitExpressionMap_Expression1(), ecorePackage.getEString(), "expression1", null, 1, 1, PortsSplitExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortsSplitExpressionMap_Expression2(), ecorePackage.getEString(), "expression2", null, 1, 1, PortsSplitExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(panamaCanalTariffEClass, PanamaCanalTariff.class, "PanamaCanalTariff", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPanamaCanalTariff_Bands(), this.getPanamaCanalTariffBand(), null, "bands", null, 0, -1, PanamaCanalTariff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPanamaCanalTariff_MarkupRate(), ecorePackage.getEDouble(), "markupRate", null, 0, 1, PanamaCanalTariff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(panamaCanalTariffBandEClass, PanamaCanalTariffBand.class, "PanamaCanalTariffBand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPanamaCanalTariffBand_LadenTariff(), ecorePackage.getEDouble(), "ladenTariff", null, 0, 1, PanamaCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPanamaCanalTariffBand_BallastTariff(), ecorePackage.getEDouble(), "ballastTariff", null, 0, 1, PanamaCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPanamaCanalTariffBand_BallastRoundtripTariff(), ecorePackage.getEDouble(), "ballastRoundtripTariff", null, 0, 1, PanamaCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPanamaCanalTariffBand_BandStart(), ecorePackage.getEInt(), "bandStart", null, 0, 1, PanamaCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPanamaCanalTariffBand_BandEnd(), ecorePackage.getEInt(), "bandEnd", null, 0, 1, PanamaCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(suezCanalTugBandEClass, SuezCanalTugBand.class, "SuezCanalTugBand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSuezCanalTugBand_Tugs(), ecorePackage.getEInt(), "tugs", null, 0, 1, SuezCanalTugBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSuezCanalTugBand_BandStart(), ecorePackage.getEInt(), "bandStart", null, 0, 1, SuezCanalTugBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSuezCanalTugBand_BandEnd(), ecorePackage.getEInt(), "bandEnd", null, 0, 1, SuezCanalTugBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(suezCanalTariffEClass, SuezCanalTariff.class, "SuezCanalTariff", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSuezCanalTariff_Bands(), this.getSuezCanalTariffBand(), null, "bands", null, 0, -1, SuezCanalTariff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSuezCanalTariff_TugBands(), this.getSuezCanalTugBand(), null, "tugBands", null, 0, -1, SuezCanalTariff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSuezCanalTariff_TugCost(), ecorePackage.getEDouble(), "tugCost", null, 0, 1, SuezCanalTariff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSuezCanalTariff_FixedCosts(), ecorePackage.getEDouble(), "fixedCosts", null, 0, 1, SuezCanalTariff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSuezCanalTariff_DiscountFactor(), ecorePackage.getEDouble(), "discountFactor", null, 0, 1, SuezCanalTariff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSuezCanalTariff_SdrToUSD(), ecorePackage.getEString(), "sdrToUSD", null, 0, 1, SuezCanalTariff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(suezCanalTariffBandEClass, SuezCanalTariffBand.class, "SuezCanalTariffBand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSuezCanalTariffBand_LadenTariff(), ecorePackage.getEDouble(), "ladenTariff", null, 0, 1, SuezCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSuezCanalTariffBand_BallastTariff(), ecorePackage.getEDouble(), "ballastTariff", null, 0, 1, SuezCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSuezCanalTariffBand_BandStart(), ecorePackage.getEInt(), "bandStart", null, 0, 1, SuezCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSuezCanalTariffBand_BandEnd(), ecorePackage.getEInt(), "bandEnd", null, 0, 1, SuezCanalTariffBand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unitConversionEClass, UnitConversion.class, "UnitConversion", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUnitConversion_From(), ecorePackage.getEString(), "from", null, 0, 1, UnitConversion.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitConversion_To(), ecorePackage.getEString(), "to", null, 0, 1, UnitConversion.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitConversion_Factor(), ecorePackage.getEDouble(), "factor", null, 0, 1, UnitConversion.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(datePointContainerEClass, DatePointContainer.class, "DatePointContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDatePointContainer_Points(), this.getDatePoint(), null, "points", null, 1, -1, DatePointContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(datePointEClass, DatePoint.class, "DatePoint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDatePoint_Date(), theDateTimePackage.getLocalDate(), "date", null, 0, 1, DatePoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDatePoint_Value(), ecorePackage.getEDouble(), "value", null, 0, 1, DatePoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(yearMonthPointContainerEClass, YearMonthPointContainer.class, "YearMonthPointContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getYearMonthPointContainer_Points(), this.getYearMonthPoint(), null, "points", null, 1, -1, YearMonthPointContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(yearMonthPointContainerEClass, ecorePackage.getEDoubleObject(), "valueForMonth", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theDateTimePackage.getYearMonth(), "month", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(yearMonthPointEClass, YearMonthPoint.class, "YearMonthPoint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getYearMonthPoint_Date(), theDateTimePackage.getYearMonth(), "date", null, 0, 1, YearMonthPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getYearMonthPoint_Value(), ecorePackage.getEDouble(), "value", null, 0, 1, YearMonthPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractYearMonthCurveEClass, AbstractYearMonthCurve.class, "AbstractYearMonthCurve", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAbstractYearMonthCurve_CurrencyUnit(), ecorePackage.getEString(), "currencyUnit", null, 0, 1, AbstractYearMonthCurve.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractYearMonthCurve_VolumeUnit(), ecorePackage.getEString(), "volumeUnit", null, 0, 1, AbstractYearMonthCurve.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractYearMonthCurve_Expression(), ecorePackage.getEString(), "expression", null, 1, 1, AbstractYearMonthCurve.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(commodityCurveEClass, CommodityCurve.class, "CommodityCurve", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCommodityCurve_MarketIndex(), this.getMarketIndex(), null, "marketIndex", null, 0, 1, CommodityCurve.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(charterCurveEClass, CharterCurve.class, "CharterCurve", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(bunkerFuelCurveEClass, BunkerFuelCurve.class, "BunkerFuelCurve", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(currencyCurveEClass, CurrencyCurve.class, "CurrencyCurve", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(marketIndexEClass, MarketIndex.class, "MarketIndex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMarketIndex_SettleCalendar(), this.getHolidayCalendar(), null, "settleCalendar", null, 0, 1, MarketIndex.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(holidayCalendarEntryEClass, HolidayCalendarEntry.class, "HolidayCalendarEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHolidayCalendarEntry_Date(), theDateTimePackage.getLocalDate(), "date", null, 0, 1, HolidayCalendarEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHolidayCalendarEntry_Comment(), ecorePackage.getEString(), "comment", null, 0, 1, HolidayCalendarEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(holidayCalendarEClass, HolidayCalendar.class, "HolidayCalendar", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getHolidayCalendar_Entries(), this.getHolidayCalendarEntry(), null, "entries", null, 0, -1, HolidayCalendar.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHolidayCalendar_Description(), ecorePackage.getEString(), "description", null, 0, 1, HolidayCalendar.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(settleStrategyEClass, SettleStrategy.class, "SettleStrategy", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSettleStrategy_DayOfTheMonth(), ecorePackage.getEInt(), "dayOfTheMonth", null, 0, 1, SettleStrategy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSettleStrategy_LastDayOfTheMonth(), ecorePackage.getEBoolean(), "lastDayOfTheMonth", null, 0, 1, SettleStrategy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSettleStrategy_UseCalendarMonth(), ecorePackage.getEBoolean(), "useCalendarMonth", null, 0, 1, SettleStrategy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSettleStrategy_SettlePeriod(), ecorePackage.getEInt(), "settlePeriod", null, 0, 1, SettleStrategy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSettleStrategy_SettlePeriodUnit(), theTypesPackage.getTimePeriod(), "settlePeriodUnit", null, 0, 1, SettleStrategy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSettleStrategy_SettleStartMonthsPrior(), ecorePackage.getEInt(), "settleStartMonthsPrior", null, 0, 1, SettleStrategy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
		  (getSuezCanalTugBand_Tugs(),
		   source,
		   new String[] {
			   "unitPrefix", "$",
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getSuezCanalTugBand_BandStart(),
		   source,
		   new String[] {
			   "unitSuffix", "m3\r\n",
			   "formatString", "##,###,##0"
		   });
		addAnnotation
		  (getSuezCanalTugBand_BandEnd(),
		   source,
		   new String[] {
			   "unitSuffix", "m3\r\n",
			   "formatString", "##,###,##0"
		   });
		addAnnotation
		  (getSuezCanalTariff_TugCost(),
		   source,
		   new String[] {
			   "unitPrefix", "$",
			   "formatString", "##,###,##0"
		   });
		addAnnotation
		  (getSuezCanalTariff_FixedCosts(),
		   source,
		   new String[] {
			   "unitPrefix", "$",
			   "formatString", "##,###,##0"
		   });
		addAnnotation
		  (getSuezCanalTariff_DiscountFactor(),
		   source,
		   new String[] {
			   "scale", "100",
			   "formatString", "##0.#",
			   "unit", "%"
		   });
		addAnnotation
		  (getSuezCanalTariffBand_LadenTariff(),
		   source,
		   new String[] {
			   "unitPrefix", "SDR",
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getSuezCanalTariffBand_BallastTariff(),
		   source,
		   new String[] {
			   "unitPrefix", "SDR",
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getSuezCanalTariffBand_BandStart(),
		   source,
		   new String[] {
			   "unitSuffix", "m3\r\n",
			   "formatString", "##,###,##0"
		   });
		addAnnotation
		  (getSuezCanalTariffBand_BandEnd(),
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

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/pricing/expressionType</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExpressionTypeAnnotations() {
		String source = "http://www.mmxlabs.com/models/pricing/expressionType";
		addAnnotation
		  (getBaseFuelCost_Expression(),
		   source,
		   new String[] {
			   "type", "basefuel"
		   });
		addAnnotation
		  (getSuezCanalTariff_SdrToUSD(),
		   source,
		   new String[] {
			   "type", "currency"
		   });
	}

} //PricingPackageImpl
