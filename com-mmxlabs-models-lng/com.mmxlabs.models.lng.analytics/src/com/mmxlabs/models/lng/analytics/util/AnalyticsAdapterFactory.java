/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.util;

import com.mmxlabs.models.lng.analytics.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.jdt.annotation.Nullable;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.CargoSandbox;
import com.mmxlabs.models.lng.analytics.CostComponent;
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
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage
 * @generated
 */
public class AnalyticsAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static AnalyticsPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalyticsAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = AnalyticsPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnalyticsSwitch<@Nullable Adapter> modelSwitch =
		new AnalyticsSwitch<@Nullable Adapter>() {
			@Override
			public Adapter caseAnalyticsModel(AnalyticsModel object) {
				return createAnalyticsModelAdapter();
			}
			@Override
			public Adapter caseUnitCostMatrix(UnitCostMatrix object) {
				return createUnitCostMatrixAdapter();
			}
			@Override
			public Adapter caseUnitCostLine(UnitCostLine object) {
				return createUnitCostLineAdapter();
			}
			@Override
			public Adapter caseVoyage(Voyage object) {
				return createVoyageAdapter();
			}
			@Override
			public Adapter caseVisit(Visit object) {
				return createVisitAdapter();
			}
			@Override
			public Adapter caseCostComponent(CostComponent object) {
				return createCostComponentAdapter();
			}
			@Override
			public Adapter caseFuelCost(FuelCost object) {
				return createFuelCostAdapter();
			}
			@Override
			public Adapter caseJourney(Journey object) {
				return createJourneyAdapter();
			}
			@Override
			public Adapter caseShippingCostPlan(ShippingCostPlan object) {
				return createShippingCostPlanAdapter();
			}
			@Override
			public Adapter caseShippingCostRow(ShippingCostRow object) {
				return createShippingCostRowAdapter();
			}
			@Override
			public Adapter caseCargoSandbox(CargoSandbox object) {
				return createCargoSandboxAdapter();
			}
			@Override
			public Adapter caseProvisionalCargo(ProvisionalCargo object) {
				return createProvisionalCargoAdapter();
			}
			@Override
			public Adapter caseBuyOption(BuyOption object) {
				return createBuyOptionAdapter();
			}
			@Override
			public Adapter caseSellOption(SellOption object) {
				return createSellOptionAdapter();
			}
			@Override
			public Adapter caseBuyOpportunity(BuyOpportunity object) {
				return createBuyOpportunityAdapter();
			}
			@Override
			public Adapter caseSellOpportunity(SellOpportunity object) {
				return createSellOpportunityAdapter();
			}
			@Override
			public Adapter caseBuyMarket(BuyMarket object) {
				return createBuyMarketAdapter();
			}
			@Override
			public Adapter caseSellMarket(SellMarket object) {
				return createSellMarketAdapter();
			}
			@Override
			public Adapter caseBuyReference(BuyReference object) {
				return createBuyReferenceAdapter();
			}
			@Override
			public Adapter caseSellReference(SellReference object) {
				return createSellReferenceAdapter();
			}
			@Override
			public Adapter caseBaseCaseRow(BaseCaseRow object) {
				return createBaseCaseRowAdapter();
			}
			@Override
			public Adapter casePartialCaseRow(PartialCaseRow object) {
				return createPartialCaseRowAdapter();
			}
			@Override
			public Adapter caseShippingOption(ShippingOption object) {
				return createShippingOptionAdapter();
			}
			@Override
			public Adapter caseFleetShippingOption(FleetShippingOption object) {
				return createFleetShippingOptionAdapter();
			}
			@Override
			public Adapter caseOptionalAvailabilityShippingOption(OptionalAvailabilityShippingOption object) {
				return createOptionalAvailabilityShippingOptionAdapter();
			}
			@Override
			public Adapter caseRoundTripShippingOption(RoundTripShippingOption object) {
				return createRoundTripShippingOptionAdapter();
			}
			@Override
			public Adapter caseNominatedShippingOption(NominatedShippingOption object) {
				return createNominatedShippingOptionAdapter();
			}
			@Override
			public Adapter caseAnalysisResultRow(AnalysisResultRow object) {
				return createAnalysisResultRowAdapter();
			}
			@Override
			public Adapter caseResultContainer(ResultContainer object) {
				return createResultContainerAdapter();
			}
			@Override
			public Adapter caseOptionRule(OptionRule object) {
				return createOptionRuleAdapter();
			}
			@Override
			public Adapter caseAnalysisResultDetail(AnalysisResultDetail object) {
				return createAnalysisResultDetailAdapter();
			}
			@Override
			public Adapter caseProfitAndLossResult(ProfitAndLossResult object) {
				return createProfitAndLossResultAdapter();
			}
			@Override
			public Adapter caseBreakEvenResult(BreakEvenResult object) {
				return createBreakEvenResultAdapter();
			}
			@Override
			public Adapter caseModeOptionRule(ModeOptionRule object) {
				return createModeOptionRuleAdapter();
			}
			@Override
			public Adapter caseOptionAnalysisModel(OptionAnalysisModel object) {
				return createOptionAnalysisModelAdapter();
			}
			@Override
			public Adapter caseResultSet(ResultSet object) {
				return createResultSetAdapter();
			}
			@Override
			public Adapter caseBaseCase(BaseCase object) {
				return createBaseCaseAdapter();
			}
			@Override
			public Adapter casePartialCase(PartialCase object) {
				return createPartialCaseAdapter();
			}
			@Override
			public Adapter caseMultipleResultGrouper(MultipleResultGrouper object) {
				return createMultipleResultGrouperAdapter();
			}
			@Override
			public Adapter caseMultipleResultGrouperRow(MultipleResultGrouperRow object) {
				return createMultipleResultGrouperRowAdapter();
			}
			@Override
			public Adapter caseMMXObject(MMXObject object) {
				return createMMXObjectAdapter();
			}
			@Override
			public Adapter caseUUIDObject(UUIDObject object) {
				return createUUIDObjectAdapter();
			}
			@Override
			public Adapter caseNamedObject(NamedObject object) {
				return createNamedObjectAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsModel
	 * @generated
	 */
	public Adapter createAnalyticsModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix <em>Unit Cost Matrix</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostMatrix
	 * @generated
	 */
	public Adapter createUnitCostMatrixAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.UnitCostLine <em>Unit Cost Line</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.UnitCostLine
	 * @generated
	 */
	public Adapter createUnitCostLineAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.Voyage <em>Voyage</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.Voyage
	 * @generated
	 */
	public Adapter createVoyageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.Visit <em>Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.Visit
	 * @generated
	 */
	public Adapter createVisitAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.CostComponent <em>Cost Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.CostComponent
	 * @generated
	 */
	public Adapter createCostComponentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.FuelCost <em>Fuel Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.FuelCost
	 * @generated
	 */
	public Adapter createFuelCostAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.Journey <em>Journey</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.Journey
	 * @generated
	 */
	public Adapter createJourneyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan <em>Shipping Cost Plan</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostPlan
	 * @generated
	 */
	public Adapter createShippingCostPlanAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow <em>Shipping Cost Row</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostRow
	 * @generated
	 */
	public Adapter createShippingCostRowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.CargoSandbox <em>Cargo Sandbox</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.CargoSandbox
	 * @generated
	 */
	public Adapter createCargoSandboxAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo <em>Provisional Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ProvisionalCargo
	 * @generated
	 */
	public Adapter createProvisionalCargoAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.BuyOption <em>Buy Option</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.BuyOption
	 * @generated
	 */
	public Adapter createBuyOptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.SellOption <em>Sell Option</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.SellOption
	 * @generated
	 */
	public Adapter createSellOptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity <em>Buy Opportunity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.BuyOpportunity
	 * @generated
	 */
	public Adapter createBuyOpportunityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.SellOpportunity <em>Sell Opportunity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.SellOpportunity
	 * @generated
	 */
	public Adapter createSellOpportunityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.BuyMarket <em>Buy Market</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.BuyMarket
	 * @generated
	 */
	public Adapter createBuyMarketAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.SellMarket <em>Sell Market</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.SellMarket
	 * @generated
	 */
	public Adapter createSellMarketAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.BuyReference <em>Buy Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.BuyReference
	 * @generated
	 */
	public Adapter createBuyReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.SellReference <em>Sell Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.SellReference
	 * @generated
	 */
	public Adapter createSellReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow <em>Base Case Row</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRow
	 * @generated
	 */
	public Adapter createBaseCaseRowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.PartialCaseRow <em>Partial Case Row</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.PartialCaseRow
	 * @generated
	 */
	public Adapter createPartialCaseRowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ShippingOption <em>Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ShippingOption
	 * @generated
	 */
	public Adapter createShippingOptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.FleetShippingOption <em>Fleet Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.FleetShippingOption
	 * @generated
	 */
	public Adapter createFleetShippingOptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption <em>Optional Availability Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption
	 * @generated
	 */
	public Adapter createOptionalAvailabilityShippingOptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption <em>Round Trip Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.RoundTripShippingOption
	 * @generated
	 */
	public Adapter createRoundTripShippingOptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.NominatedShippingOption <em>Nominated Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.NominatedShippingOption
	 * @generated
	 */
	public Adapter createNominatedShippingOptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.AnalysisResultRow <em>Analysis Result Row</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.AnalysisResultRow
	 * @generated
	 */
	public Adapter createAnalysisResultRowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ResultContainer <em>Result Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ResultContainer
	 * @generated
	 */
	public Adapter createResultContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.OptionRule <em>Option Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.OptionRule
	 * @generated
	 */
	public Adapter createOptionRuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.AnalysisResultDetail <em>Analysis Result Detail</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.AnalysisResultDetail
	 * @generated
	 */
	public Adapter createAnalysisResultDetailAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ProfitAndLossResult <em>Profit And Loss Result</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ProfitAndLossResult
	 * @generated
	 */
	public Adapter createProfitAndLossResultAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.BreakEvenResult <em>Break Even Result</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.BreakEvenResult
	 * @generated
	 */
	public Adapter createBreakEvenResultAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ModeOptionRule <em>Mode Option Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ModeOptionRule
	 * @generated
	 */
	public Adapter createModeOptionRuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel <em>Option Analysis Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.OptionAnalysisModel
	 * @generated
	 */
	public Adapter createOptionAnalysisModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ResultSet <em>Result Set</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ResultSet
	 * @generated
	 */
	public Adapter createResultSetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.BaseCase <em>Base Case</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.BaseCase
	 * @generated
	 */
	public Adapter createBaseCaseAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.PartialCase <em>Partial Case</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.PartialCase
	 * @generated
	 */
	public Adapter createPartialCaseAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouper <em>Multiple Result Grouper</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.MultipleResultGrouper
	 * @generated
	 */
	public Adapter createMultipleResultGrouperAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.MultipleResultGrouperRow <em>Multiple Result Grouper Row</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.MultipleResultGrouperRow
	 * @generated
	 */
	public Adapter createMultipleResultGrouperRowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.MMXObject <em>MMX Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.MMXObject
	 * @generated
	 */
	public Adapter createMMXObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.UUIDObject <em>UUID Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.UUIDObject
	 * @generated
	 */
	public Adapter createUUIDObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.NamedObject
	 * @generated
	 */
	public Adapter createNamedObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //AnalyticsAdapterFactory
