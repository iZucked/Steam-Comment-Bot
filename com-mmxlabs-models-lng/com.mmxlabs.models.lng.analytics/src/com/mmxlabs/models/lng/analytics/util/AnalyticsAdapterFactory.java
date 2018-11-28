/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.scenario.service.ui.dnd.IChangeSource;

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
			public Adapter caseAbstractAnalysisModel(AbstractAnalysisModel object) {
				return createAbstractAnalysisModelAdapter();
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
			public Adapter caseResult(Result object) {
				return createResultAdapter();
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
			public Adapter caseNewVesselAvailability(NewVesselAvailability object) {
				return createNewVesselAvailabilityAdapter();
			}
			@Override
			public Adapter caseExistingVesselAvailability(ExistingVesselAvailability object) {
				return createExistingVesselAvailabilityAdapter();
			}
			@Override
			public Adapter caseExistingCharterMarketOption(ExistingCharterMarketOption object) {
				return createExistingCharterMarketOptionAdapter();
			}
			@Override
			public Adapter caseAbstractSolutionSet(AbstractSolutionSet object) {
				return createAbstractSolutionSetAdapter();
			}
			@Override
			public Adapter caseActionableSetPlan(ActionableSetPlan object) {
				return createActionableSetPlanAdapter();
			}
			@Override
			public Adapter caseSlotInsertionOptions(SlotInsertionOptions object) {
				return createSlotInsertionOptionsAdapter();
			}
			@Override
			public Adapter caseSolutionOption(SolutionOption object) {
				return createSolutionOptionAdapter();
			}
			@Override
			public Adapter caseOptimisationResult(OptimisationResult object) {
				return createOptimisationResultAdapter();
			}
			@Override
			public Adapter caseChangeDescription(ChangeDescription object) {
				return createChangeDescriptionAdapter();
			}
			@Override
			public Adapter caseChange(Change object) {
				return createChangeAdapter();
			}
			@Override
			public Adapter caseOpenSlotChange(OpenSlotChange object) {
				return createOpenSlotChangeAdapter();
			}
			@Override
			public Adapter caseCargoChange(CargoChange object) {
				return createCargoChangeAdapter();
			}
			@Override
			public Adapter caseVesselEventChange(VesselEventChange object) {
				return createVesselEventChangeAdapter();
			}
			@Override
			public Adapter caseVesselEventDescriptor(VesselEventDescriptor object) {
				return createVesselEventDescriptorAdapter();
			}
			@Override
			public Adapter caseSlotDescriptor(SlotDescriptor object) {
				return createSlotDescriptorAdapter();
			}
			@Override
			public Adapter caseRealSlotDescriptor(RealSlotDescriptor object) {
				return createRealSlotDescriptorAdapter();
			}
			@Override
			public Adapter caseSpotMarketSlotDescriptor(SpotMarketSlotDescriptor object) {
				return createSpotMarketSlotDescriptorAdapter();
			}
			@Override
			public Adapter caseVesselAllocationDescriptor(VesselAllocationDescriptor object) {
				return createVesselAllocationDescriptorAdapter();
			}
			@Override
			public Adapter caseMarketVesselAllocationDescriptor(MarketVesselAllocationDescriptor object) {
				return createMarketVesselAllocationDescriptorAdapter();
			}
			@Override
			public Adapter caseFleetVesselAllocationDescriptor(FleetVesselAllocationDescriptor object) {
				return createFleetVesselAllocationDescriptorAdapter();
			}
			@Override
			public Adapter casePositionDescriptor(PositionDescriptor object) {
				return createPositionDescriptorAdapter();
			}
			@Override
			public Adapter caseViabilityModel(ViabilityModel object) {
				return createViabilityModelAdapter();
			}
			@Override
			public Adapter caseViabilityRow(ViabilityRow object) {
				return createViabilityRowAdapter();
			}
			@Override
			public Adapter caseViabilityResult(ViabilityResult object) {
				return createViabilityResultAdapter();
			}
			@Override
			public Adapter caseMTMModel(MTMModel object) {
				return createMTMModelAdapter();
			}
			@Override
			public Adapter caseMTMResult(MTMResult object) {
				return createMTMResultAdapter();
			}
			@Override
			public Adapter caseMTMRow(MTMRow object) {
				return createMTMRowAdapter();
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
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel <em>Abstract Analysis Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.AbstractAnalysisModel
	 * @generated
	 */
	public Adapter createAbstractAnalysisModelAdapter() {
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
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.Result <em>Result</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.Result
	 * @generated
	 */
	public Adapter createResultAdapter() {
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
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.NewVesselAvailability <em>New Vessel Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.NewVesselAvailability
	 * @generated
	 */
	public Adapter createNewVesselAvailabilityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ExistingVesselAvailability <em>Existing Vessel Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ExistingVesselAvailability
	 * @generated
	 */
	public Adapter createExistingVesselAvailabilityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption <em>Existing Charter Market Option</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption
	 * @generated
	 */
	public Adapter createExistingCharterMarketOptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet <em>Abstract Solution Set</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.AbstractSolutionSet
	 * @generated
	 */
	public Adapter createAbstractSolutionSetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ActionableSetPlan <em>Actionable Set Plan</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ActionableSetPlan
	 * @generated
	 */
	public Adapter createActionableSetPlanAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.SlotInsertionOptions <em>Slot Insertion Options</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.SlotInsertionOptions
	 * @generated
	 */
	public Adapter createSlotInsertionOptionsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ChangeDescription <em>Change Description</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ChangeDescription
	 * @generated
	 */
	public Adapter createChangeDescriptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.Change <em>Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.Change
	 * @generated
	 */
	public Adapter createChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.OpenSlotChange <em>Open Slot Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.OpenSlotChange
	 * @generated
	 */
	public Adapter createOpenSlotChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.CargoChange <em>Cargo Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.CargoChange
	 * @generated
	 */
	public Adapter createCargoChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.VesselEventChange <em>Vessel Event Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.VesselEventChange
	 * @generated
	 */
	public Adapter createVesselEventChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.VesselEventDescriptor <em>Vessel Event Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.VesselEventDescriptor
	 * @generated
	 */
	public Adapter createVesselEventDescriptorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.SlotDescriptor <em>Slot Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.SlotDescriptor
	 * @generated
	 */
	public Adapter createSlotDescriptorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.RealSlotDescriptor <em>Real Slot Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.RealSlotDescriptor
	 * @generated
	 */
	public Adapter createRealSlotDescriptorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor <em>Spot Market Slot Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor
	 * @generated
	 */
	public Adapter createSpotMarketSlotDescriptorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.VesselAllocationDescriptor <em>Vessel Allocation Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.VesselAllocationDescriptor
	 * @generated
	 */
	public Adapter createVesselAllocationDescriptorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor <em>Market Vessel Allocation Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor
	 * @generated
	 */
	public Adapter createMarketVesselAllocationDescriptorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor <em>Fleet Vessel Allocation Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor
	 * @generated
	 */
	public Adapter createFleetVesselAllocationDescriptorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.PositionDescriptor <em>Position Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.PositionDescriptor
	 * @generated
	 */
	public Adapter createPositionDescriptorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ViabilityModel <em>Viability Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityModel
	 * @generated
	 */
	public Adapter createViabilityModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ViabilityRow <em>Viability Row</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityRow
	 * @generated
	 */
	public Adapter createViabilityRowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.ViabilityResult <em>Viability Result</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.ViabilityResult
	 * @generated
	 */
	public Adapter createViabilityResultAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.MTMModel <em>MTM Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.MTMModel
	 * @generated
	 */
	public Adapter createMTMModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.MTMResult <em>MTM Result</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.MTMResult
	 * @generated
	 */
	public Adapter createMTMResultAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.MTMRow <em>MTM Row</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.MTMRow
	 * @generated
	 */
	public Adapter createMTMRowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.SolutionOption <em>Solution Option</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.SolutionOption
	 * @generated
	 */
	public Adapter createSolutionOptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.analytics.OptimisationResult <em>Optimisation Result</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.analytics.OptimisationResult
	 * @generated
	 */
	public Adapter createOptimisationResultAdapter() {
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
