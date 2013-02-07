/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CharterCostModel;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SpotType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PricingFactoryImpl extends EFactoryImpl implements PricingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static PricingFactory init() {
		try {
			PricingFactory thePricingFactory = (PricingFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.mmxlabs.com/models/lng/pricing/1/"); 
			if (thePricingFactory != null) {
				return thePricingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new PricingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case PricingPackage.PRICING_MODEL: return createPricingModel();
			case PricingPackage.DATA_INDEX: return createDataIndex();
			case PricingPackage.DERIVED_INDEX: return createDerivedIndex();
			case PricingPackage.INDEX_POINT: return createIndexPoint();
			case PricingPackage.FLEET_COST_MODEL: return createFleetCostModel();
			case PricingPackage.ROUTE_COST: return createRouteCost();
			case PricingPackage.CHARTER_COST_MODEL: return createCharterCostModel();
			case PricingPackage.BASE_FUEL_COST: return createBaseFuelCost();
			case PricingPackage.SPOT_MARKET_GROUP: return createSpotMarketGroup();
			case PricingPackage.PORT_COST: return createPortCost();
			case PricingPackage.PORT_COST_ENTRY: return createPortCostEntry();
			case PricingPackage.COOLDOWN_PRICE: return createCooldownPrice();
			case PricingPackage.DES_PURCHASE_MARKET: return createDESPurchaseMarket();
			case PricingPackage.DES_SALES_MARKET: return createDESSalesMarket();
			case PricingPackage.FOB_PURCHASES_MARKET: return createFOBPurchasesMarket();
			case PricingPackage.FOB_SALES_MARKET: return createFOBSalesMarket();
			case PricingPackage.SPOT_AVAILABILITY: return createSpotAvailability();
			case PricingPackage.LNG_PRICE_CALCULATOR_PARAMETERS: return createLNGPriceCalculatorParameters();
			case PricingPackage.LNG_FIXED_PRICE_PARAMETERS: return createLNGFixedPriceParameters();
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS: return createLNGIndexPriceParameters();
			case PricingPackage.LNG_PRICE_EXPRESSION_PARAMETERS: return createLNGPriceExpressionParameters();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case PricingPackage.SPOT_TYPE:
				return createSpotTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case PricingPackage.SPOT_TYPE:
				return convertSpotTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingModel createPricingModel() {
		PricingModelImpl pricingModel = new PricingModelImpl();
		return pricingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <Value> DataIndex<Value> createDataIndex() {
		DataIndexImpl<Value> dataIndex = new DataIndexImpl<Value>();
		return dataIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <Value> DerivedIndex<Value> createDerivedIndex() {
		DerivedIndexImpl<Value> derivedIndex = new DerivedIndexImpl<Value>();
		return derivedIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <Value> IndexPoint<Value> createIndexPoint() {
		IndexPointImpl<Value> indexPoint = new IndexPointImpl<Value>();
		return indexPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetCostModel createFleetCostModel() {
		FleetCostModelImpl fleetCostModel = new FleetCostModelImpl();
		return fleetCostModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RouteCost createRouteCost() {
		RouteCostImpl routeCost = new RouteCostImpl();
		return routeCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterCostModel createCharterCostModel() {
		CharterCostModelImpl charterCostModel = new CharterCostModelImpl();
		return charterCostModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseFuelCost createBaseFuelCost() {
		BaseFuelCostImpl baseFuelCost = new BaseFuelCostImpl();
		return baseFuelCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketGroup createSpotMarketGroup() {
		SpotMarketGroupImpl spotMarketGroup = new SpotMarketGroupImpl();
		return spotMarketGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortCost createPortCost() {
		PortCostImpl portCost = new PortCostImpl();
		return portCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortCostEntry createPortCostEntry() {
		PortCostEntryImpl portCostEntry = new PortCostEntryImpl();
		return portCostEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CooldownPrice createCooldownPrice() {
		CooldownPriceImpl cooldownPrice = new CooldownPriceImpl();
		return cooldownPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DESPurchaseMarket createDESPurchaseMarket() {
		DESPurchaseMarketImpl desPurchaseMarket = new DESPurchaseMarketImpl();
		return desPurchaseMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DESSalesMarket createDESSalesMarket() {
		DESSalesMarketImpl desSalesMarket = new DESSalesMarketImpl();
		return desSalesMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FOBPurchasesMarket createFOBPurchasesMarket() {
		FOBPurchasesMarketImpl fobPurchasesMarket = new FOBPurchasesMarketImpl();
		return fobPurchasesMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FOBSalesMarket createFOBSalesMarket() {
		FOBSalesMarketImpl fobSalesMarket = new FOBSalesMarketImpl();
		return fobSalesMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotAvailability createSpotAvailability() {
		SpotAvailabilityImpl spotAvailability = new SpotAvailabilityImpl();
		return spotAvailability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGPriceCalculatorParameters createLNGPriceCalculatorParameters() {
		LNGPriceCalculatorParametersImpl lngPriceCalculatorParameters = new LNGPriceCalculatorParametersImpl();
		return lngPriceCalculatorParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGFixedPriceParameters createLNGFixedPriceParameters() {
		LNGFixedPriceParametersImpl lngFixedPriceParameters = new LNGFixedPriceParametersImpl();
		return lngFixedPriceParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGIndexPriceParameters createLNGIndexPriceParameters() {
		LNGIndexPriceParametersImpl lngIndexPriceParameters = new LNGIndexPriceParametersImpl();
		return lngIndexPriceParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGPriceExpressionParameters createLNGPriceExpressionParameters() {
		LNGPriceExpressionParametersImpl lngPriceExpressionParameters = new LNGPriceExpressionParametersImpl();
		return lngPriceExpressionParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotType createSpotTypeFromString(EDataType eDataType, String initialValue) {
		SpotType result = SpotType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSpotTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingPackage getPricingPackage() {
		return (PricingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static PricingPackage getPackage() {
		return PricingPackage.eINSTANCE;
	}

} //PricingFactoryImpl
