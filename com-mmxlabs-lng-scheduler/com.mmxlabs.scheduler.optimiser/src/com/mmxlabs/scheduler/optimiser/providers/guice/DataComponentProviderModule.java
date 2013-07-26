/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.HashMapElementDurationEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.OrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.impl.ResourceAllocationConstraintProvider;
import com.mmxlabs.optimiser.common.dcproviders.impl.TimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedElementDurationEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedOptionalElementsEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedOrderedSequenceElementsEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedTimeWindowEditor;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.IXYPortDistanceCalculator;
import com.mmxlabs.scheduler.optimiser.builder.impl.XYPortEuclideanDistanceCalculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitEditor;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitProvider;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ArrayListVolumeAllocationEditor;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IShortCargoReturnElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShortCargoReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ISlotGroupCountProvider;
import com.mmxlabs.scheduler.optimiser.providers.ISlotGroupCountProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapAlternativeElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapCharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapDiscountCurveEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapEntityProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapMarkToMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortCVProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapRouteCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapShipToShipBindingProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapShortCargoReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapSlotGroupCountProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapStartEndRequirementEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVirtualVesselSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashSetCalculatorProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.LazyDateKeyProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.IndexedPortCostEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.IndexedPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.IndexedPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.IndexedPortTypeEditor;

/**
 * {@link Module} implementation providing shared {@link IDataComponentProvider} instances. This Module will return the same instance for the {@link IDataComponentProvider} and any editor subclasses.
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

		bind(IXYPortDistanceCalculator.class).to(XYPortEuclideanDistanceCalculator.class);
		bind(XYPortEuclideanDistanceCalculator.class).in(Singleton.class);

		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor(SchedulerConstants.DCP_vesselProvider);

		bind(IVesselProvider.class).toInstance(vesselProvider);
		bind(IVesselProviderEditor.class).toInstance(vesselProvider);

		final IndexedMultiMatrixProvider<IPort, Integer> portDistanceProvider = new IndexedMultiMatrixProvider<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider);
		bind(new TypeLiteral<IMultiMatrixEditor<IPort, Integer>>() {
		}).toInstance(portDistanceProvider);
		bind(new TypeLiteral<IMultiMatrixProvider<IPort, Integer>>() {
		}).toInstance(portDistanceProvider);
		bind(new TypeLiteral<IndexedMultiMatrixProvider<IPort, Integer>>() {
		}).toInstance(portDistanceProvider);

		final IPortProviderEditor portProvider;
		final IPortSlotProviderEditor portSlotsProvider;
		final IPortTypeProviderEditor portTypeProvider;
		final ITimeWindowDataComponentProviderEditor timeWindowProvider;
		final IOrderedSequenceElementsDataComponentProviderEditor orderedSequenceElementsEditor;
		final IElementDurationProviderEditor elementDurationsProvider;
		if (USE_INDEXED_DCPS) {
			portProvider = new IndexedPortEditor(SchedulerConstants.DCP_portProvider);
			portSlotsProvider = new IndexedPortSlotEditor(SchedulerConstants.DCP_portSlotsProvider);
			portTypeProvider = new IndexedPortTypeEditor(SchedulerConstants.DCP_portTypeProvider);

			timeWindowProvider = new IndexedTimeWindowEditor(SchedulerConstants.DCP_timeWindowProvider);
			orderedSequenceElementsEditor = new IndexedOrderedSequenceElementsEditor(SchedulerConstants.DCP_orderedElementsProvider);

			elementDurationsProvider = new IndexedElementDurationEditor(SchedulerConstants.DCP_elementDurationsProvider);

			// Create a default matrix entry
			portDistanceProvider.set(IMultiMatrixProvider.Default_Key, new IndexedMatrixEditor<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider, Integer.MAX_VALUE));
		} else {
			portProvider = new HashMapPortEditor(SchedulerConstants.DCP_portProvider);
			portSlotsProvider = new HashMapPortSlotEditor(SchedulerConstants.DCP_portSlotsProvider);
			portTypeProvider = new HashMapPortTypeEditor(SchedulerConstants.DCP_portTypeProvider);

			timeWindowProvider = new TimeWindowDataComponentProvider(SchedulerConstants.DCP_timeWindowProvider);
			orderedSequenceElementsEditor = new OrderedSequenceElementsDataComponentProvider(SchedulerConstants.DCP_orderedElementsProvider);
			elementDurationsProvider = new HashMapElementDurationEditor(SchedulerConstants.DCP_elementDurationsProvider);

			// Create a default matrix entry
			portDistanceProvider.set(IMultiMatrixProvider.Default_Key, new HashMapMatrixProvider<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider, Integer.MAX_VALUE));
		}
		bind(IPortProvider.class).toInstance(portProvider);
		bind(IPortProviderEditor.class).toInstance(portProvider);

		bind(IPortSlotProvider.class).toInstance(portSlotsProvider);
		bind(IPortSlotProviderEditor.class).toInstance(portSlotsProvider);

		bind(IPortTypeProvider.class).toInstance(portTypeProvider);
		bind(IPortTypeProviderEditor.class).toInstance(portTypeProvider);

		bind(ITimeWindowDataComponentProvider.class).toInstance(timeWindowProvider);
		bind(ITimeWindowDataComponentProviderEditor.class).toInstance(timeWindowProvider);

		bind(IOrderedSequenceElementsDataComponentProvider.class).toInstance(orderedSequenceElementsEditor);
		bind(IOrderedSequenceElementsDataComponentProviderEditor.class).toInstance(orderedSequenceElementsEditor);

		bind(IElementDurationProvider.class).toInstance(elementDurationsProvider);
		bind(IElementDurationProviderEditor.class).toInstance(elementDurationsProvider);

		final IResourceAllocationConstraintDataComponentProviderEditor resourceAllocationProvider = new ResourceAllocationConstraintProvider(SchedulerConstants.DCP_resourceAllocationProvider);
		bind(IResourceAllocationConstraintDataComponentProvider.class).toInstance(resourceAllocationProvider);
		bind(IResourceAllocationConstraintDataComponentProviderEditor.class).toInstance(resourceAllocationProvider);

		final IStartEndRequirementProviderEditor startEndRequirementProvider = new HashMapStartEndRequirementEditor(SchedulerConstants.DCP_startEndRequirementProvider);
		bind(IStartEndRequirementProvider.class).toInstance(startEndRequirementProvider);
		bind(IStartEndRequirementProviderEditor.class).toInstance(startEndRequirementProvider);

		final IPortExclusionProviderEditor portExclusionProvider = new HashMapPortExclusionProvider(SchedulerConstants.DCP_portExclusionProvider);
		bind(IPortExclusionProvider.class).toInstance(portExclusionProvider);
		bind(IPortExclusionProviderEditor.class).toInstance(portExclusionProvider);

		final IReturnElementProviderEditor returnElementProvider = new HashMapReturnElementProviderEditor(SchedulerConstants.DCP_returnElementProvider);
		bind(IReturnElementProvider.class).toInstance(returnElementProvider);
		bind(IReturnElementProviderEditor.class).toInstance(returnElementProvider);

		final HashMapRouteCostProviderEditor routeCostProvider = new HashMapRouteCostProviderEditor(SchedulerConstants.DCP_routePriceProvider, IMultiMatrixProvider.Default_Key);
		bind(IRouteCostProvider.class).toInstance(routeCostProvider);
		bind(IRouteCostProviderEditor.class).toInstance(routeCostProvider);

		final ITotalVolumeLimitEditor totalVolumeLimits = new ArrayListVolumeAllocationEditor(SchedulerConstants.DCP_totalVolumeLimitProvider);
		bind(ITotalVolumeLimitProvider.class).toInstance(totalVolumeLimits);
		bind(ITotalVolumeLimitEditor.class).toInstance(totalVolumeLimits);

		final IDiscountCurveProviderEditor discountCurveProvider = new HashMapDiscountCurveEditor(SchedulerConstants.DCP_discountCurveProvider);
		bind(IDiscountCurveProvider.class).toInstance(discountCurveProvider);
		bind(IDiscountCurveProviderEditor.class).toInstance(discountCurveProvider);

		final HashSetCalculatorProviderEditor calculatorProvider = new HashSetCalculatorProviderEditor(SchedulerConstants.DCP_calculatorProvider);
		bind(ICalculatorProvider.class).toInstance(calculatorProvider);
		bind(ICalculatorProviderEditor.class).toInstance(calculatorProvider);

		final IOptionalElementsProviderEditor optionalElements = new IndexedOptionalElementsEditor(SchedulerConstants.DCP_optionalElementsProvider);
		bind(IOptionalElementsProvider.class).toInstance(optionalElements);
		bind(IOptionalElementsProviderEditor.class).toInstance(optionalElements);

		final IPortCostProviderEditor portCosts = new IndexedPortCostEditor(SchedulerConstants.DCP_portCostProvider);
		bind(IPortCostProvider.class).toInstance(portCosts);
		bind(IPortCostProviderEditor.class).toInstance(portCosts);

		final ISlotGroupCountProviderEditor slotGroupCountProvider = new HashMapSlotGroupCountProviderEditor(SchedulerConstants.DCP_slotGroupProvider);
		bind(ISlotGroupCountProvider.class).toInstance(slotGroupCountProvider);
		bind(ISlotGroupCountProviderEditor.class).toInstance(slotGroupCountProvider);

		final IVirtualVesselSlotProviderEditor virtualVesselSlotProviderEditor = new HashMapVirtualVesselSlotProviderEditor(SchedulerConstants.DCP_virtualVesselSlotProvider);
		bind(IVirtualVesselSlotProvider.class).toInstance(virtualVesselSlotProviderEditor);
		bind(IVirtualVesselSlotProviderEditor.class).toInstance(virtualVesselSlotProviderEditor);

		final HashMapEntityProviderEditor entityProviderEditor = new HashMapEntityProviderEditor(SchedulerConstants.DCP_entityProvider);
		bind(IEntityProvider.class).toInstance(entityProviderEditor);
		bind(HashMapEntityProviderEditor.class).toInstance(entityProviderEditor);

		final LazyDateKeyProviderEditor dateKeyProviderEditor = new LazyDateKeyProviderEditor(SchedulerConstants.DCP_dateKeyProvider);
		bind(IDateKeyProvider.class).toInstance(dateKeyProviderEditor);
		bind(IDateKeyProviderEditor.class).toInstance(dateKeyProviderEditor);
		
		final HashMapShortCargoReturnElementProviderEditor shortCargoReturnElementProvider = new HashMapShortCargoReturnElementProviderEditor(SchedulerConstants.DCP_shortCargoReturnElementProvider);
		bind(IShortCargoReturnElementProvider.class).toInstance(shortCargoReturnElementProvider);
		bind(IShortCargoReturnElementProviderEditor.class).toInstance(shortCargoReturnElementProvider);
		
		final HashMapCharterMarketProviderEditor charterMarketProviderEditor = new HashMapCharterMarketProviderEditor(SchedulerConstants.DCP_charterMarketProvider);
		bind(ICharterMarketProvider.class).toInstance(charterMarketProviderEditor);
		bind(ICharterMarketProviderEditor.class).toInstance(charterMarketProviderEditor);
		
		final HashMapPortCVProviderEditor portCVProviderEditor = new HashMapPortCVProviderEditor(SchedulerConstants.DCP_portCVProvider);
		bind(IPortCVProvider.class).toInstance(portCVProviderEditor);
		bind(IPortCVProviderEditor.class).toInstance(portCVProviderEditor);
		
		final HashMapAlternativeElementProviderEditor alternativeElementProviderEditor = new HashMapAlternativeElementProviderEditor(SchedulerConstants.DCP_alternativeElementProvider);
		bind(IAlternativeElementProvider.class).toInstance(alternativeElementProviderEditor);
		bind(IAlternativeElementProviderEditor.class).toInstance(alternativeElementProviderEditor);
		
		final HashMapShipToShipBindingProviderEditor shipToShipProviderEditor = new HashMapShipToShipBindingProviderEditor(SchedulerConstants.DCP_shipToShipElementProvider);
		bind(IShipToShipBindingProvider.class).toInstance(shipToShipProviderEditor);
		bind(IShipToShipBindingProviderEditor.class).toInstance(shipToShipProviderEditor);
		
		final HashMapMarkToMarketProviderEditor markToMarketEditor = new HashMapMarkToMarketProviderEditor(SchedulerConstants.DCP_markToMarketElementProvider);
		bind(IMarkToMarketProvider.class).toInstance(markToMarketEditor);
		bind(IMarkToMarketProviderEditor.class).toInstance(markToMarketEditor);
	
	}

	/**
	 * A provider for new {@link IMatrixEditor} instances.
	 * 
	 * @return
	 */
	@Provides
	public IMatrixEditor<IPort, Integer> getIMatrixEditor() {
		if (USE_INDEXED_DCPS) {
			return new IndexedMatrixEditor<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider, Integer.MAX_VALUE);
		} else {
			return new HashMapMatrixProvider<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider, Integer.MAX_VALUE);
		}
	}
}
