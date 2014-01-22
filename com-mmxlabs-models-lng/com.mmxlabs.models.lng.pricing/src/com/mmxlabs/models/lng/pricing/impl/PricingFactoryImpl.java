/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;

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
			PricingFactory thePricingFactory = (PricingFactory)EPackage.Registry.INSTANCE.getEFactory(PricingPackage.eNS_URI);
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
			case PricingPackage.BASE_FUEL_COST: return createBaseFuelCost();
			case PricingPackage.PORT_COST: return createPortCost();
			case PricingPackage.PORT_COST_ENTRY: return createPortCostEntry();
			case PricingPackage.COOLDOWN_PRICE: return createCooldownPrice();
			case PricingPackage.COMMODITY_INDEX: return createCommodityIndex();
			case PricingPackage.CHARTER_INDEX: return createCharterIndex();
			case PricingPackage.BASE_FUEL_INDEX: return createBaseFuelIndex();
			case PricingPackage.NAMED_INDEX_CONTAINER: return createNamedIndexContainer();
			case PricingPackage.PORTS_PRICE_MAP: return createPortsPriceMap();
			case PricingPackage.PORTS_EXPRESSION_MAP: return createPortsExpressionMap();
			case PricingPackage.PORTS_SPLIT_PRICE_MAP: return createPortsSplitPriceMap();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
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
	public BaseFuelCost createBaseFuelCost() {
		BaseFuelCostImpl baseFuelCost = new BaseFuelCostImpl();
		return baseFuelCost;
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
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityIndex createCommodityIndex() {
		CommodityIndexImpl commodityIndex = new CommodityIndexImpl();
		return commodityIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterIndex createCharterIndex() {
		CharterIndexImpl charterIndex = new CharterIndexImpl();
		return charterIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseFuelIndex createBaseFuelIndex() {
		BaseFuelIndexImpl baseFuelIndex = new BaseFuelIndexImpl();
		return baseFuelIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <Value> NamedIndexContainer<Value> createNamedIndexContainer() {
		NamedIndexContainerImpl<Value> namedIndexContainer = new NamedIndexContainerImpl<Value>();
		return namedIndexContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortsPriceMap createPortsPriceMap() {
		PortsPriceMapImpl portsPriceMap = new PortsPriceMapImpl();
		return portsPriceMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortsExpressionMap createPortsExpressionMap() {
		PortsExpressionMapImpl portsExpressionMap = new PortsExpressionMapImpl();
		return portsExpressionMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortsSplitPriceMap createPortsSplitPriceMap() {
		PortsSplitPriceMapImpl portsSplitPriceMap = new PortsSplitPriceMapImpl();
		return portsSplitPriceMap;
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
