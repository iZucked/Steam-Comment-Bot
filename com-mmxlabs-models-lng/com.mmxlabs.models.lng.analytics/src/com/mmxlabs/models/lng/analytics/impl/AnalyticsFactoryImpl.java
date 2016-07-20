/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.CargoSandbox;
import com.mmxlabs.models.lng.analytics.CostComponent;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.FuelCost;
import com.mmxlabs.models.lng.analytics.Journey;
import com.mmxlabs.models.lng.analytics.ProvisionalCargo;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.analytics.Visit;
import com.mmxlabs.models.lng.analytics.Voyage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AnalyticsFactoryImpl extends EFactoryImpl implements AnalyticsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AnalyticsFactory init() {
		try {
			AnalyticsFactory theAnalyticsFactory = (AnalyticsFactory)EPackage.Registry.INSTANCE.getEFactory(AnalyticsPackage.eNS_URI);
			if (theAnalyticsFactory != null) {
				return theAnalyticsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AnalyticsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalyticsFactoryImpl() {
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
			case AnalyticsPackage.ANALYTICS_MODEL: return createAnalyticsModel();
			case AnalyticsPackage.UNIT_COST_MATRIX: return createUnitCostMatrix();
			case AnalyticsPackage.UNIT_COST_LINE: return createUnitCostLine();
			case AnalyticsPackage.VOYAGE: return createVoyage();
			case AnalyticsPackage.VISIT: return createVisit();
			case AnalyticsPackage.COST_COMPONENT: return createCostComponent();
			case AnalyticsPackage.FUEL_COST: return createFuelCost();
			case AnalyticsPackage.JOURNEY: return createJourney();
			case AnalyticsPackage.SHIPPING_COST_PLAN: return createShippingCostPlan();
			case AnalyticsPackage.SHIPPING_COST_ROW: return createShippingCostRow();
			case AnalyticsPackage.CARGO_SANDBOX: return createCargoSandbox();
			case AnalyticsPackage.PROVISIONAL_CARGO: return createProvisionalCargo();
			case AnalyticsPackage.BUY_OPPORTUNITY: return createBuyOpportunity();
			case AnalyticsPackage.SELL_OPPORTUNITY: return createSellOpportunity();
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
			case AnalyticsPackage.DESTINATION_TYPE:
				return createDestinationTypeFromString(eDataType, initialValue);
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
			case AnalyticsPackage.DESTINATION_TYPE:
				return convertDestinationTypeToString(eDataType, instanceValue);
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
	public AnalyticsModel createAnalyticsModel() {
		AnalyticsModelImpl analyticsModel = new AnalyticsModelImpl();
		return analyticsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public UnitCostMatrix createUnitCostMatrix() {
		UnitCostMatrixImpl unitCostMatrix = new UnitCostMatrixImpl();
		return unitCostMatrix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public UnitCostLine createUnitCostLine() {
		UnitCostLineImpl unitCostLine = new UnitCostLineImpl();
		return unitCostLine;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Voyage createVoyage() {
		VoyageImpl voyage = new VoyageImpl();
		return voyage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Visit createVisit() {
		VisitImpl visit = new VisitImpl();
		return visit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CostComponent createCostComponent() {
		CostComponentImpl costComponent = new CostComponentImpl();
		return costComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FuelCost createFuelCost() {
		FuelCostImpl fuelCost = new FuelCostImpl();
		return fuelCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Journey createJourney() {
		JourneyImpl journey = new JourneyImpl();
		return journey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ShippingCostPlan createShippingCostPlan() {
		ShippingCostPlanImpl shippingCostPlan = new ShippingCostPlanImpl();
		return shippingCostPlan;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ShippingCostRow createShippingCostRow() {
		ShippingCostRowImpl shippingCostRow = new ShippingCostRowImpl();
		return shippingCostRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoSandbox createCargoSandbox() {
		CargoSandboxImpl cargoSandbox = new CargoSandboxImpl();
		return cargoSandbox;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProvisionalCargo createProvisionalCargo() {
		ProvisionalCargoImpl provisionalCargo = new ProvisionalCargoImpl();
		return provisionalCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BuyOpportunity createBuyOpportunity() {
		BuyOpportunityImpl buyOpportunity = new BuyOpportunityImpl();
		return buyOpportunity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SellOpportunity createSellOpportunity() {
		SellOpportunityImpl sellOpportunity = new SellOpportunityImpl();
		return sellOpportunity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DestinationType createDestinationTypeFromString(EDataType eDataType, String initialValue) {
		DestinationType result = DestinationType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDestinationTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AnalyticsPackage getAnalyticsPackage() {
		return (AnalyticsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AnalyticsPackage getPackage() {
		return AnalyticsPackage.eINSTANCE;
	}

} //AnalyticsFactoryImpl
