/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.ILockedElementsProvider;
import com.mmxlabs.optimiser.common.dcproviders.ILockedElementsProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.HashMapElementDurationEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.OrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.impl.ResourceAllocationConstraintProvider;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedElementDurationEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedLockedElementsEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedOptionalElementsEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedOrderedSequenceElementsEditor;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.providers.*;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultAllowedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultBaseFuelProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultCounterPartyVolumeProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultCounterPartyWindowProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultDistanceProviderImpl;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultExposureDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultExtraIdleTimeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultFOBDESCompatibilityProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultGroupedSlotsConstraintDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultHeelCarrySlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultLazyExpressionManager;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultLockedCargoProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultLongTermVesselSlotCountFitnessProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultMaxSlotConstraintDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultNextLoadDateProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultPaperDealDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultPromptPeriodProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultRoundTripVesselPermissionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultScheduledPurgeProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultSpotCharterInMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultThirdPartyCargoProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultTransferModelDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultVesselCharterCurveProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapActualsDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapAlternativeElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapBaseFuelCurveEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapCancellationFeeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapCharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapElementPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapEntityProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapFullCargoLotProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapLoadPriceCalculatorProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapMarkToMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapMiscCostsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapNominatedVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortCVProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortCooldownDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortVisitDurationProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPriceExpressionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapRouteCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapRouteExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapShipToShipBindingProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapShippingHoursRestrictionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapShortCargoReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapSlotGroupCountProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapSpotMarketSlotsEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapStartEndRequirementEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVirtualVesselSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashSetCalculatorProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.LazyDateKeyProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.HashMapPortCostEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.IndexedElementPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.IndexedPortCVRangeEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.IndexedPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.IndexedPortTypeEditor;

/**
 * {@link Module} implementation providing shared {@link IDataComponentProvider}
 * instances. This Module will return the same instance for the
 * {@link IDataComponentProvider} and any editor subclasses.
 * 
 * @author Simon Goodall
 * 
 */
public class DataComponentProviderModule extends AbstractModule {

	/**
	 * For debug & timing purposes. Switches the indexing DCPs on or off.
	 */
	private final boolean USE_INDEXED_DCPS;

	public DataComponentProviderModule() {
		this(true);
	}

	public DataComponentProviderModule(final boolean useIndexedDCPs) {
		USE_INDEXED_DCPS = useIndexedDCPs;
	}

	@Override
	protected void configure() {

		bind(DefaultDistanceProviderImpl.class).in(Singleton.class);
		bind(IDistanceProvider.class).to(DefaultDistanceProviderImpl.class);
		bind(IDistanceProviderEditor.class).to(DefaultDistanceProviderImpl.class);

		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();

		bind(IVesselProvider.class).toInstance(vesselProvider);
		bind(IVesselProviderEditor.class).toInstance(vesselProvider);

		final IElementPortProviderEditor portProvider;
		final IPortSlotProviderEditor portSlotsProvider;
		final IPortTypeProviderEditor portTypeProvider;
		final IOrderedSequenceElementsDataComponentProviderEditor orderedSequenceElementsEditor;
		final IElementDurationProviderEditor elementDurationsProvider;
		if (USE_INDEXED_DCPS) {
			portProvider = new IndexedElementPortEditor();
			portSlotsProvider = new IndexedPortSlotEditor();
			portTypeProvider = new IndexedPortTypeEditor();

			orderedSequenceElementsEditor = new IndexedOrderedSequenceElementsEditor();

			elementDurationsProvider = new IndexedElementDurationEditor();
		} else {
			portProvider = new HashMapElementPortEditor();
			portSlotsProvider = new HashMapPortSlotEditor();
			portTypeProvider = new HashMapPortTypeEditor();

			orderedSequenceElementsEditor = new OrderedSequenceElementsDataComponentProvider();
			elementDurationsProvider = new HashMapElementDurationEditor();
		}
		bind(IElementPortProvider.class).toInstance(portProvider);
		bind(IElementPortProviderEditor.class).toInstance(portProvider);

		bind(IPortSlotProvider.class).toInstance(portSlotsProvider);
		bind(IPortSlotProviderEditor.class).toInstance(portSlotsProvider);

		bind(IPortTypeProvider.class).toInstance(portTypeProvider);
		bind(IPortTypeProviderEditor.class).toInstance(portTypeProvider);

		bind(IOrderedSequenceElementsDataComponentProvider.class).toInstance(orderedSequenceElementsEditor);
		bind(IOrderedSequenceElementsDataComponentProviderEditor.class).toInstance(orderedSequenceElementsEditor);

		bind(IElementDurationProvider.class).toInstance(elementDurationsProvider);
		bind(IElementDurationProviderEditor.class).toInstance(elementDurationsProvider);

		final IResourceAllocationConstraintDataComponentProviderEditor resourceAllocationProvider = new ResourceAllocationConstraintProvider();
		bind(IResourceAllocationConstraintDataComponentProvider.class).toInstance(resourceAllocationProvider);
		bind(IResourceAllocationConstraintDataComponentProviderEditor.class).toInstance(resourceAllocationProvider);

		final IStartEndRequirementProviderEditor startEndRequirementProvider = new HashMapStartEndRequirementEditor();
		bind(IStartEndRequirementProvider.class).toInstance(startEndRequirementProvider);
		bind(IStartEndRequirementProviderEditor.class).toInstance(startEndRequirementProvider);

		final IPortExclusionProviderEditor portExclusionProvider = new HashMapPortExclusionProvider();
		bind(IPortExclusionProvider.class).toInstance(portExclusionProvider);
		bind(IPortExclusionProviderEditor.class).toInstance(portExclusionProvider);

		final IRouteExclusionProviderEditor routeExclusionProviderEditor = new HashMapRouteExclusionProvider();
		bind(IRouteExclusionProvider.class).toInstance(routeExclusionProviderEditor);
		bind(IRouteExclusionProviderEditor.class).toInstance(routeExclusionProviderEditor);

		final IReturnElementProviderEditor returnElementProvider = new HashMapReturnElementProviderEditor();
		bind(IReturnElementProvider.class).toInstance(returnElementProvider);
		bind(IReturnElementProviderEditor.class).toInstance(returnElementProvider);

		final HashMapRouteCostProviderEditor routeCostProvider = new HashMapRouteCostProviderEditor();
		bind(IRouteCostProvider.class).toInstance(routeCostProvider);
		bind(IRouteCostProviderEditor.class).toInstance(routeCostProvider);

		final HashSetCalculatorProviderEditor calculatorProvider = new HashSetCalculatorProviderEditor();
		bind(ICalculatorProvider.class).toInstance(calculatorProvider);
		bind(ICalculatorProviderEditor.class).toInstance(calculatorProvider);

		final IOptionalElementsProviderEditor optionalElements = new IndexedOptionalElementsEditor();
		bind(IOptionalElementsProvider.class).toInstance(optionalElements);
		bind(IOptionalElementsProviderEditor.class).toInstance(optionalElements);

		final ILockedElementsProviderEditor lockedElements = new IndexedLockedElementsEditor();
		bind(ILockedElementsProviderEditor.class).toInstance(lockedElements);
		bind(ILockedElementsProvider.class).toInstance(lockedElements);

		final ISpotMarketSlotsProviderEditor spotMarketSlots = new HashMapSpotMarketSlotsEditor();
		bind(ISpotMarketSlotsProvider.class).toInstance(spotMarketSlots);
		bind(ISpotMarketSlotsProviderEditor.class).toInstance(spotMarketSlots);

		final IPortCostProviderEditor portCosts = new HashMapPortCostEditor();
		bind(IPortCostProvider.class).toInstance(portCosts);
		bind(IPortCostProviderEditor.class).toInstance(portCosts);

		final IPortCVRangeProviderEditor portCVRangeProvider = new IndexedPortCVRangeEditor();
		bind(IPortCVRangeProvider.class).toInstance(portCVRangeProvider);
		bind(IPortCVRangeProviderEditor.class).toInstance(portCVRangeProvider);

		final ISlotGroupCountProviderEditor slotGroupCountProvider = new HashMapSlotGroupCountProviderEditor();
		bind(ISlotGroupCountProvider.class).toInstance(slotGroupCountProvider);
		bind(ISlotGroupCountProviderEditor.class).toInstance(slotGroupCountProvider);

		final IVirtualVesselSlotProviderEditor virtualVesselSlotProviderEditor = new HashMapVirtualVesselSlotProviderEditor();
		bind(IVirtualVesselSlotProvider.class).toInstance(virtualVesselSlotProviderEditor);
		bind(IVirtualVesselSlotProviderEditor.class).toInstance(virtualVesselSlotProviderEditor);

		final HashMapEntityProviderEditor entityProviderEditor = new HashMapEntityProviderEditor();
		bind(IEntityProvider.class).toInstance(entityProviderEditor);
		bind(HashMapEntityProviderEditor.class).toInstance(entityProviderEditor);

		final LazyDateKeyProviderEditor dateKeyProviderEditor = new LazyDateKeyProviderEditor();
		bind(IDateKeyProvider.class).toInstance(dateKeyProviderEditor);
		bind(IDateKeyProviderEditor.class).toInstance(dateKeyProviderEditor);

		final HashMapShortCargoReturnElementProviderEditor shortCargoReturnElementProvider = new HashMapShortCargoReturnElementProviderEditor();
		bind(IShortCargoReturnElementProvider.class).toInstance(shortCargoReturnElementProvider);
		bind(IShortCargoReturnElementProviderEditor.class).toInstance(shortCargoReturnElementProvider);

		final HashMapCharterMarketProviderEditor charterMarketProviderEditor = new HashMapCharterMarketProviderEditor();
		bind(ICharterMarketProvider.class).toInstance(charterMarketProviderEditor);
		bind(ICharterMarketProviderEditor.class).toInstance(charterMarketProviderEditor);

		final HashMapPortCVProviderEditor portCVProviderEditor = new HashMapPortCVProviderEditor();
		bind(IPortCVProvider.class).toInstance(portCVProviderEditor);
		bind(IPortCVProviderEditor.class).toInstance(portCVProviderEditor);

		final HashMapPortCooldownDataProviderEditor portCooldownDataProviderEditor = new HashMapPortCooldownDataProviderEditor();
		bind(IPortCooldownDataProvider.class).toInstance(portCooldownDataProviderEditor);
		bind(IPortCooldownDataProviderEditor.class).toInstance(portCooldownDataProviderEditor);

		final HashMapAlternativeElementProviderEditor alternativeElementProviderEditor = new HashMapAlternativeElementProviderEditor();
		bind(IAlternativeElementProvider.class).toInstance(alternativeElementProviderEditor);
		bind(IAlternativeElementProviderEditor.class).toInstance(alternativeElementProviderEditor);

		final HashMapShipToShipBindingProviderEditor shipToShipProviderEditor = new HashMapShipToShipBindingProviderEditor();
		bind(IShipToShipBindingProvider.class).toInstance(shipToShipProviderEditor);
		bind(IShipToShipBindingProviderEditor.class).toInstance(shipToShipProviderEditor);

		final HashMapLoadPriceCalculatorProviderEditor loadPriceCalculatorProviderEditor = new HashMapLoadPriceCalculatorProviderEditor();
		bind(ILoadPriceCalculatorProvider.class).toInstance(loadPriceCalculatorProviderEditor);
		bind(ILoadPriceCalculatorProviderEditor.class).toInstance(loadPriceCalculatorProviderEditor);

		final HashMapMarkToMarketProviderEditor markToMarketEditor = new HashMapMarkToMarketProviderEditor();
		bind(IMarkToMarketProvider.class).toInstance(markToMarketEditor);
		bind(IMarkToMarketProviderEditor.class).toInstance(markToMarketEditor);

		final HashMapNominatedVesselProviderEditor nominatedVesselProviderEditor = new HashMapNominatedVesselProviderEditor();
		bind(INominatedVesselProvider.class).toInstance(nominatedVesselProviderEditor);
		bind(INominatedVesselProviderEditor.class).toInstance(nominatedVesselProviderEditor);

		final HashMapShippingHoursRestrictionProviderEditor shippingHoursRestrictionProviderEditor = new HashMapShippingHoursRestrictionProviderEditor();
		bind(IShippingHoursRestrictionProvider.class).toInstance(shippingHoursRestrictionProviderEditor);
		bind(IShippingHoursRestrictionProviderEditor.class).toInstance(shippingHoursRestrictionProviderEditor);

		final HashMapPortVisitDurationProviderEditor portVisitDurationProviderEditor = new HashMapPortVisitDurationProviderEditor();
		bind(IPortVisitDurationProvider.class).toInstance(portVisitDurationProviderEditor);
		bind(IPortVisitDurationProviderEditor.class).toInstance(portVisitDurationProviderEditor);

		final DefaultNextLoadDateProvider nexDateProviderEditor = new DefaultNextLoadDateProvider();
		bind(INextLoadDateProvider.class).toInstance(nexDateProviderEditor);
		bind(INextLoadDateProviderEditor.class).toInstance(nexDateProviderEditor);

		bind(DefaultVesselCharterCurveProvider.class).in(Singleton.class);
		bind(IVesselCharterInRateProvider.class).to(DefaultVesselCharterCurveProvider.class);

		final HashMapMiscCostsProviderEditor miscCostsProviderEditor = new HashMapMiscCostsProviderEditor();
		bind(IMiscCostsProvider.class).toInstance(miscCostsProviderEditor);
		bind(IMiscCostsProviderEditor.class).toInstance(miscCostsProviderEditor);

		final HashMapCancellationFeeProviderEditor cancellationFeeProviderEditor = new HashMapCancellationFeeProviderEditor();
		bind(ICancellationFeeProvider.class).toInstance(cancellationFeeProviderEditor);
		bind(ICancellationFeeProviderEditor.class).toInstance(cancellationFeeProviderEditor);

		bind(HashMapActualsDataProviderEditor.class).in(Singleton.class);
		bind(IActualsDataProvider.class).to(HashMapActualsDataProviderEditor.class);
		bind(IActualsDataProviderEditor.class).to(HashMapActualsDataProviderEditor.class);

		bind(TimeZoneToUtcOffsetProvider.class).in(Singleton.class);
		bind(ITimeZoneToUtcOffsetProvider.class).to(TimeZoneToUtcOffsetProvider.class);

		final HashMapBaseFuelCurveEditor baseFuelCurveEditor = new HashMapBaseFuelCurveEditor();
		bind(IBaseFuelCurveProvider.class).toInstance(baseFuelCurveEditor);
		bind(IBaseFuelCurveProviderEditor.class).toInstance(baseFuelCurveEditor);

		bind(DefaultBaseFuelProvider.class).in(Singleton.class);
		bind(IBaseFuelProvider.class).to(DefaultBaseFuelProvider.class);
		bind(IBaseFuelProviderEditor.class).to(DefaultBaseFuelProvider.class);

		bind(DefaultPromptPeriodProviderEditor.class).in(Singleton.class);
		bind(IPromptPeriodProvider.class).to(DefaultPromptPeriodProviderEditor.class);
		bind(IPromptPeriodProviderEditor.class).to(DefaultPromptPeriodProviderEditor.class);

		bind(DefaultRoundTripVesselPermissionProviderEditor.class).in(Singleton.class);
		bind(IRoundTripVesselPermissionProvider.class).to(DefaultRoundTripVesselPermissionProviderEditor.class);
		bind(IRoundTripVesselPermissionProviderEditor.class).to(DefaultRoundTripVesselPermissionProviderEditor.class);

		bind(DefaultSpotCharterInMarketProviderEditor.class).in(Singleton.class);
		bind(ISpotCharterInMarketProvider.class).to(DefaultSpotCharterInMarketProviderEditor.class);
		bind(ISpotCharterInMarketProviderEditor.class).to(DefaultSpotCharterInMarketProviderEditor.class);

		bind(DefaultFOBDESCompatibilityProviderEditor.class).in(Singleton.class);
		bind(IFOBDESCompatibilityProvider.class).to(DefaultFOBDESCompatibilityProviderEditor.class);
		bind(IFOBDESCompatibilityProviderEditor.class).to(DefaultFOBDESCompatibilityProviderEditor.class);

		bind(DefaultAllowedVesselProvider.class).in(Singleton.class);
		bind(IAllowedVesselProvider.class).to(DefaultAllowedVesselProvider.class);
		bind(IAllowedVesselProviderEditor.class).to(DefaultAllowedVesselProvider.class);

		bind(DefaultExtraIdleTimeProviderEditor.class).in(Singleton.class);
		bind(IExtraIdleTimeProvider.class).to(DefaultExtraIdleTimeProviderEditor.class);
		bind(IExtraIdleTimeProviderEditor.class).to(DefaultExtraIdleTimeProviderEditor.class);

		// Lightweight/longterm/ADP
		bind(DefaultMaxSlotConstraintDataProviderEditor.class).in(Singleton.class);
		bind(IMaxSlotCountConstraintDataProvider.class).to(DefaultMaxSlotConstraintDataProviderEditor.class);
		bind(IMaxSlotConstraintDataProviderEditor.class).to(DefaultMaxSlotConstraintDataProviderEditor.class);

		bind(DefaultLongTermVesselSlotCountFitnessProvider.class).in(Singleton.class);
		bind(IVesselSlotCountFitnessProvider.class).to(DefaultLongTermVesselSlotCountFitnessProvider.class);
		bind(IVesselSlotCountFitnessProviderEditor.class).to(DefaultLongTermVesselSlotCountFitnessProvider.class);

		bind(DefaultLockedCargoProviderEditor.class).in(Singleton.class);
		bind(ILockedCargoProvider.class).to(DefaultLockedCargoProviderEditor.class);
		bind(ILockedCargoProviderEditor.class).to(DefaultLockedCargoProviderEditor.class);

		// Price curves
		bind(HashMapPriceExpressionProviderEditor.class).in(Singleton.class);
		bind(IPriceExpressionProvider.class).to(HashMapPriceExpressionProviderEditor.class);
		bind(IPriceExpressionProviderEditor.class).to(HashMapPriceExpressionProviderEditor.class);

		bind(DefaultLazyExpressionManager.class).in(Singleton.class);
		bind(ILazyExpressionManagerContainer.class).to(DefaultLazyExpressionManager.class);
		bind(ILazyExpressionManagerEditor.class).to(DefaultLazyExpressionManager.class);

		bind(HashMapFullCargoLotProvider.class).in(Singleton.class);
		bind(IFullCargoLotProvider.class).to(HashMapFullCargoLotProvider.class);
		bind(IFullCargoLotProviderEditor.class).to(HashMapFullCargoLotProvider.class);

		bind(DefaultExposureDataProvider.class).in(Singleton.class);
		bind(IExposureDataProvider.class).to(DefaultExposureDataProvider.class);
		bind(IExposureDataProviderEditor.class).to(DefaultExposureDataProvider.class);
		
		bind(DefaultTransferModelDataProviderEditor.class).in(Singleton.class);
		bind(ITransferModelDataProvider.class).to(DefaultTransferModelDataProviderEditor.class);
		bind(ITransferModelDataProviderEditor.class).to(DefaultTransferModelDataProviderEditor.class);

		bind(DefaultPaperDealDataProvider.class).in(Singleton.class);
		bind(IPaperDealDataProvider.class).to(DefaultPaperDealDataProvider.class);
		bind(IPaperDealDataProviderEditor.class).to(DefaultPaperDealDataProvider.class);

		bind(DefaultScheduledPurgeProvider.class).in(Singleton.class);
		bind(IScheduledPurgeProvider.class).to(DefaultScheduledPurgeProvider.class);
		bind(IScheduledPurgeProviderEditor.class).to(DefaultScheduledPurgeProvider.class);

		// Counterparty volume provider
		bind(DefaultCounterPartyVolumeProvider.class).in(Singleton.class);
		bind(ICounterPartyVolumeProvider.class).to(DefaultCounterPartyVolumeProvider.class);
		bind(ICounterPartyVolumeProviderEditor.class).to(DefaultCounterPartyVolumeProvider.class);
		
		// Heel carry slot data provider
		bind(DefaultHeelCarrySlotProviderEditor.class).in(Singleton.class);
		bind(IHeelCarrySlotProvider.class).to(DefaultHeelCarrySlotProviderEditor.class);
		bind(IHeelCarrySlotProviderEditor.class).to(DefaultHeelCarrySlotProviderEditor.class);

		// Counterparty window provider
		bind(DefaultCounterPartyWindowProvider.class).in(Singleton.class);
		bind(ICounterPartyWindowProvider.class).to(DefaultCounterPartyWindowProvider.class);
		bind(ICounterPartyWindowProviderEditor.class).to(DefaultCounterPartyWindowProvider.class);

		// Third-party cargo provider
		bind(DefaultThirdPartyCargoProvider.class).in(Singleton.class);
		bind(IThirdPartyCargoProvider.class).to(DefaultThirdPartyCargoProvider.class);
		bind(IThirdPartyCargoProviderEditor.class).to(DefaultThirdPartyCargoProvider.class);

		// Grouped slots constraints provider
		bind(DefaultGroupedSlotsConstraintDataProviderEditor.class).in(Singleton.class);
		bind(IGroupedSlotsConstraintDataProvider.class).to(DefaultGroupedSlotsConstraintDataProviderEditor.class);
		bind(IGroupedSlotsConstraintDataProviderEditor.class).to(DefaultGroupedSlotsConstraintDataProviderEditor.class);

	}
}
