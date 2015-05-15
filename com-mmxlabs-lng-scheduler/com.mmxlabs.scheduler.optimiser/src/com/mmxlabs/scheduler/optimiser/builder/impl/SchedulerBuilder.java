/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.CheckingIndexingContext;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.impl.OptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.IXYPortDistanceCalculator;
import com.mmxlabs.scheduler.optimiser.components.DefaultSpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.BaseFuel;
import com.mmxlabs.scheduler.optimiser.components.impl.Cargo;
import com.mmxlabs.scheduler.optimiser.components.impl.CargoShortEnd;
import com.mmxlabs.scheduler.optimiser.components.impl.DefaultVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.GeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.impl.GeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptions;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadOption;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.impl.Port;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.TotalVolumeLimit;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEvent;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.XYPort;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVRangeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IShortCargoReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ISlotGroupCountProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Implementation of {@link ISchedulerBuilder}
 * 
 * @author Simon Goodall
 */
public final class SchedulerBuilder implements ISchedulerBuilder {

	@NonNull
	private final List<IBuilderExtension> extensions = new LinkedList<IBuilderExtension>();

	@NonNull
	private final List<IResource> resources = new ArrayList<IResource>();

	@NonNull
	private final List<ISequenceElement> sequenceElements = new ArrayList<ISequenceElement>();

	@NonNull
	private final List<IVesselClass> vesselClasses = new LinkedList<IVesselClass>();

	@NonNull
	private final List<IVessel> vessels = new LinkedList<>();

	@NonNull
	private final List<IVesselAvailability> vesselAvailabilities = new LinkedList<>();

	@NonNull
	private final List<IVesselAvailability> realVesselAvailabilities = new LinkedList<>();

	@NonNull
	private final List<ICargo> cargoes = new LinkedList<ICargo>();

	@NonNull
	private final List<ILoadOption> loadSlots = new LinkedList<ILoadOption>();

	@NonNull
	private final List<IDischargeOption> dischargeSlots = new LinkedList<IDischargeOption>();

	@NonNull
	private final List<ITimeWindow> timeWindows = new LinkedList<ITimeWindow>();

	/**
	 * List of end slots, which need to be corrected in getOptimisationData to have the latest time in them
	 */
	@NonNull
	private final List<Pair<ISequenceElement, PortSlot>> endSlots = new LinkedList<>();

	@NonNull
	private final List<MutableTimeWindow> openEndDateWindows = new LinkedList<>();

	/**
	 * A "virtual" port which is zero distance from all other ports, to be used in cases where a vessel can be in any location. This can be replaced with a real location at a later date, after running
	 * an optimisation.
	 */
	private IPort ANYWHERE;

	@NonNull
	private final List<IPort> ports = new LinkedList<IPort>();

	/**
	 * A field for tracking the time at which the last time window closes
	 */
	private int endOfLatestWindow = 0;

	/**
	 * Tracks elements that are not shipped.
	 */
	@NonNull
	private final List<ISequenceElement> unshippedElements = new ArrayList<ISequenceElement>();

	/*
	 * Constraint-tracking data structures; constraints created through the builder are applied at the very end, in case they affect things created after them.
	 */

	/**
	 * Tracks forward adjacency constraints; value must follow key. The reverse of {@link #reverseAdjacencyConstraints}
	 */
	@NonNull
	private final Map<IPortSlot, IPortSlot> forwardAdjacencyConstraints = new HashMap<IPortSlot, IPortSlot>();

	/**
	 * Tracks forward adjacency constraints; key must follow value. The reverse of {@link #forwardAdjacencyConstraints}
	 */
	@NonNull
	private final Map<IPortSlot, IPortSlot> reverseAdjacencyConstraints = new HashMap<IPortSlot, IPortSlot>();

	@NonNull
	private final Map<IPort, List<TotalVolumeLimit>> loadLimits = new HashMap<IPort, List<TotalVolumeLimit>>();

	@NonNull
	private final Map<IPort, List<TotalVolumeLimit>> dischargeLimits = new HashMap<IPort, List<TotalVolumeLimit>>();

	/**
	 * The slots for vessel events which have been generated; these are stored so that in {@link #buildVesselEvents()} they can have some extra post-processing done to set up any constraints
	 */
	@NonNull
	private final List<VesselEventPortSlot> vesselEvents = new LinkedList<>();

	@NonNull
	private final Map<IPortSlot, Set<IVesselAvailability>> slotVesselAvailabilityRestrictions = new HashMap<>();

	@NonNull
	private final Map<IPortSlot, Set<IVesselClass>> slotVesselClassRestrictions = new HashMap<>();

	@NonNull
	private final IIndexingContext indexingContext = new CheckingIndexingContext();

	@Inject
	@NonNull
	private IXYPortDistanceCalculator distanceProvider;

	@Inject
	@NonNull
	private IVesselProviderEditor vesselProvider;

	@Inject
	@NonNull
	private IPortProviderEditor portProvider;

	@Inject
	@NonNull
	private IPortSlotProviderEditor portSlotsProvider;

	@Inject
	@NonNull
	private IOrderedSequenceElementsDataComponentProviderEditor orderedSequenceElementsEditor;

	@Inject
	@NonNull
	private ITimeWindowDataComponentProviderEditor timeWindowProvider;

	@Inject
	@NonNull
	private IndexedMultiMatrixProvider<IPort, Integer> portDistanceProvider;

	@Inject
	@NonNull
	private IPortTypeProviderEditor portTypeProvider;

	@Inject
	@NonNull
	private IElementDurationProviderEditor elementDurationsProvider;

	@Inject
	@NonNull
	private IResourceAllocationConstraintDataComponentProviderEditor resourceAllocationProvider;

	@Inject
	@NonNull
	private IStartEndRequirementProviderEditor startEndRequirementProvider;

	@Inject
	@NonNull
	private IPortExclusionProviderEditor portExclusionProvider;

	@Inject
	@NonNull
	private IReturnElementProviderEditor returnElementProvider;

	@Inject
	@NonNull
	private IRouteCostProviderEditor routeCostProvider;

	@Inject
	@NonNull
	private ITotalVolumeLimitEditor totalVolumeLimits;

	@Inject
	@NonNull
	private IDiscountCurveProviderEditor discountCurveProvider;

	@Inject
	@NonNull
	private IPortCostProviderEditor portCostProvider;

	@Inject
	@NonNull
	private IPortCVProviderEditor portCVProvider;

	@Inject
	@NonNull
	private IPortCVRangeProviderEditor portCVRangeProvider;

	/**
	 * Keeps track of calculators
	 */
	@Inject
	@NonNull
	private ICalculatorProviderEditor calculatorProvider;

	@Inject
	@NonNull
	private IOptionalElementsProviderEditor optionalElements;

	@Inject
	@NonNull
	private ISpotMarketSlotsProviderEditor spotMarketSlots;

	@Inject
	@NonNull
	private Provider<IMatrixEditor<IPort, Integer>> matrixEditorProvider;

	@Inject
	@NonNull
	private ISlotGroupCountProviderEditor slotGroupCountProvider;

	@Inject
	@NonNull
	private IVirtualVesselSlotProviderEditor virtualVesselSlotProviderEditor;

	@Inject
	@NonNull
	private IDateKeyProviderEditor dateKeyProviderEditor;

	@Inject
	@NonNull
	private ICharterMarketProviderEditor charterMarketProviderEditor;

	@Inject
	@NonNull
	private IShortCargoReturnElementProviderEditor shortCargoReturnElementProviderEditor;

	@Inject
	@NonNull
	private IMarkToMarketProviderEditor markToMarketProviderEditor;

	@Inject
	@NonNull
	private INominatedVesselProviderEditor nominatedVesselProviderEditor;

	@Inject
	@NonNull
	private IShippingHoursRestrictionProviderEditor shippingHoursRestrictionProviderEditor;

	@NonNull
	private final Map<IPort, MarkToMarket> desPurchaseMTMPortMap = new HashMap<IPort, MarkToMarket>();

	@NonNull
	private final Map<IPort, MarkToMarket> desSaleMTMPortMap = new HashMap<IPort, MarkToMarket>();

	@NonNull
	private final Map<IPort, MarkToMarket> fobSaleMTMPortMap = new HashMap<IPort, MarkToMarket>();

	@NonNull
	private final Map<IPort, MarkToMarket> fobPurchaseMTMPortMap = new HashMap<IPort, MarkToMarket>();
	/**
	 * Constant used during end date of scenario calculations - {@link #minDaysFromLastEventToEnd} days extra after last date. See code in {@link SchedulerBuilder#getOptimisationData()}
	 * 
	 */
	public static int minDaysFromLastEventToEnd = 10;

	/**
	 * Fake vessel class for virtual elements.
	 */
	private IVesselClass virtualClass;
	/**
	 * Fake vessel class for cargo shorts.
	 */
	private IVessel shortCargoVessel;
	private IVesselAvailability shortCargoVesselAvailability;

	/**
	 * Map between a virtual sequence element and the virtual {@link IVesselEvent} instance representing it.
	 */
	@NonNull
	private final Map<ISequenceElement, IVesselAvailability> virtualVesselAvailabilityMap = new HashMap<ISequenceElement, IVesselAvailability>();

	public SchedulerBuilder() {
		indexingContext.registerType(SequenceElement.class);
		indexingContext.registerType(Port.class);
		indexingContext.registerType(Resource.class);
	}

	// @Inject to trigger call after constructor
	@Inject
	public void init() {
		// Create the anywhere port
		ANYWHERE = createPort("ANYWHERE", false, new ICooldownCalculator() {
			@Override
			public void prepareEvaluation(final ISequences sequences) {
			}

			@Override
			public long calculateCooldownCost(final IVesselClass vesselClass, final IPort port, final int cv, final int time) {
				return 0;
			}
		}, "UTC"/* no timezone */, 0, Integer.MAX_VALUE);

		// setup fake vessels for virtual elements.
		virtualClass = createVesselClass("virtual", 0, 0, Long.MAX_VALUE, 0, createBaseFuel("fakeFuel", 0), 0, 0, 0, 0);
	}

	/**
	 */
	@Override
	@NonNull
	public ILoadSlot createLoadSlot(final String id, final @NonNull IPort port, final ITimeWindow window, final long minVolumeInM3, final long maxVolumeInM3, final ILoadPriceCalculator loadContract,
			final int cargoCVValue, final int durationHours, final boolean cooldownSet, final boolean cooldownForbidden, final int pricingDate, final PricingEventType pricingEvent,
			final boolean optional, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {

		if (!ports.contains(port)) {
			throw new IllegalArgumentException("IPort was not created by this builder");
		}
		if (!timeWindows.contains(window)) {
			throw new IllegalArgumentException("ITimeWindow was not created by this builder");
		}

		final LoadSlot slot = new LoadSlot();

		slot.setCooldownForbidden(cooldownForbidden);
		slot.setCooldownSet(cooldownSet);
		final ISequenceElement element = configureLoadOption(slot, id, port, window, minVolumeInM3, maxVolumeInM3, loadContract, cargoCVValue, pricingDate, pricingEvent, optional, isSpotMarketSlot,
				isVolumeLimitInM3);

		elementDurationsProvider.setElementDuration(element, durationHours);

		return slot;
	}

	/**
	 */
	@Override
	@NonNull
	public ILoadOption createDESPurchaseLoadSlot(final String id, @Nullable IPort port, final ITimeWindow window, final long minVolume, final long maxVolume,
			final ILoadPriceCalculator priceCalculator, final int cargoCVValue, final int durationHours, final int pricingDate, final PricingEventType pricingEvent, final boolean slotIsOptional,
			final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {

		if (port == null) {
			port = ANYWHERE;
		}

		final LoadOption slot = new LoadOption();
		final ISequenceElement element = configureLoadOption(slot, id, port, window, minVolume, maxVolume, priceCalculator, cargoCVValue, pricingDate, pricingEvent, slotIsOptional, isSpotMarketSlot,
				isVolumeLimitInM3);

		elementDurationsProvider.setElementDuration(element, durationHours);

		unshippedElements.add(element);

		createVirtualVesselAvailability(element, VesselInstanceType.DES_PURCHASE);

		return slot;
	}

	@NonNull
	private ISequenceElement configureLoadOption(@NonNull final LoadOption slot, @NonNull final String id, final IPort port, final ITimeWindow window, final long minVolume, final long maxVolume,
			final ILoadPriceCalculator priceCalculator, final int cargoCVValue, final int pricingDate, final PricingEventType pricingEvent, final boolean optional, final boolean isSpotMarketSlot,
			final boolean isVolumeLimitInM3) {
		slot.setId(id);
		slot.setPort(port);
		slot.setTimeWindow(window);
		if (isVolumeLimitInM3) {
			slot.setMinLoadVolume(minVolume);
			slot.setMaxLoadVolume(maxVolume == 0 ? Long.MAX_VALUE : maxVolume);
			slot.setVolumeSetInM3(true);
		} else {
			slot.setMinLoadVolumeMMBTU(minVolume);
			slot.setMaxLoadVolumeMMBTU(maxVolume == 0 ? Long.MAX_VALUE : maxVolume);
			slot.setVolumeSetInM3(false);
		}
		// slot.setPurchasePriceCurve(pricePerMMBTu);
		slot.setLoadPriceCalculator(priceCalculator);
		slot.setCargoCVValue(cargoCVValue);
		slot.setPricingDate(pricingDate);
		slot.setPricingEvent(pricingEvent);

		loadSlots.add(slot);

		// Create a sequence element against this load slot
		final SequenceElement element = new SequenceElement(indexingContext, id + "-" + port.getName());

		optionalElements.setOptional(element, optional);

		spotMarketSlots.setSpotMarketSlot(element, isSpotMarketSlot);

		sequenceElements.add(element);

		// Register the port with the element
		portProvider.setPortForElement(port, element);

		portTypeProvider.setPortType(element, PortType.Load);

		portSlotsProvider.setPortSlot(element, slot);

		timeWindowProvider.setTimeWindows(element, Collections.singletonList(window));

		addSlotToVolumeConstraints(slot);

		calculatorProvider.addLoadPriceCalculator(priceCalculator);

		// The load option has a fixed cv value (as opposed to the discharge option) so we can do the conversion here
		if (slot.isVolumeSetInM3()) {
			slot.setMinLoadVolumeMMBTU(Calculator.convertM3ToMMBTuWithOverflowProtection(slot.getMinLoadVolume(), slot.getCargoCVValue()));
			slot.setMaxLoadVolumeMMBTU(Calculator.convertM3ToMMBTuWithOverflowProtection(slot.getMaxLoadVolume(), slot.getCargoCVValue()));
		} else {
			slot.setMinLoadVolume(Calculator.convertMMBTuToM3(slot.getMinLoadVolumeMMBTU(), slot.getCargoCVValue()));
			slot.setMaxLoadVolume(Calculator.convertMMBTuToM3(slot.getMaxLoadVolumeMMBTU(), slot.getCargoCVValue()));
		}

		return element;
	}

	/**
	 */
	@Override
	@NonNull
	public IDischargeOption createFOBSaleDischargeSlot(final String id, @Nullable IPort port, final ITimeWindow window, final long minVolume, final long maxVolume, final long minCvValue,
			final long maxCvValue, final ISalesPriceCalculator priceCalculator, final int durationHours, final int pricingDate, final PricingEventType pricingEvent, final boolean slotIsOptional,
			final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {

		if (port == null) {
			port = ANYWHERE;
		}

		final DischargeOption slot = new DischargeOption();

		final ISequenceElement element = configureDischargeOption(slot, id, port, window, minVolume, maxVolume, minCvValue, maxCvValue, priceCalculator, pricingDate, pricingEvent, slotIsOptional,
				isSpotMarketSlot, isVolumeLimitInM3);

		elementDurationsProvider.setElementDuration(element, durationHours);

		unshippedElements.add(element);

		createVirtualVesselAvailability(element, VesselInstanceType.FOB_SALE);

		return slot;
	}

	@NonNull
	private ISequenceElement configureDischargeOption(@NonNull final DischargeOption slot, final String id, final IPort port, final ITimeWindow window, final long minVolume, final long maxVolume,
			final long minCvValue, final long maxCvValue, final ISalesPriceCalculator priceCalculator, final int pricingDate, final PricingEventType pricingEvent, final boolean optional,
			final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {
		slot.setId(id);
		slot.setPort(port);
		slot.setTimeWindow(window);
		if (isVolumeLimitInM3) {
			slot.setMinDischargeVolume(minVolume);
			slot.setMaxDischargeVolume(maxVolume == 0 ? Long.MAX_VALUE : maxVolume);
			slot.setVolumeSetInM3(true);
		} else {
			slot.setMinDischargeVolumeMMBTU(minVolume);
			slot.setMaxDischargeVolumeMMBTU(maxVolume == 0 ? Long.MAX_VALUE : maxVolume);
			slot.setVolumeSetInM3(false);
		}
		slot.setMinCvValue(minCvValue);
		slot.setMaxCvValue(maxCvValue);
		slot.setDischargePriceCalculator(priceCalculator);
		slot.setPricingDate(pricingDate);
		slot.setPricingEvent(pricingEvent);

		dischargeSlots.add(slot);

		// Create a sequence element against this discharge slot
		final SequenceElement element = new SequenceElement(indexingContext, id + "-" + port.getName());

		optionalElements.setOptional(element, optional);

		spotMarketSlots.setSpotMarketSlot(element, isSpotMarketSlot);

		sequenceElements.add(element);

		// Register the port with the element
		portProvider.setPortForElement(port, element);

		portSlotsProvider.setPortSlot(element, slot);

		portTypeProvider.setPortType(element, PortType.Discharge);

		timeWindowProvider.setTimeWindows(element, Collections.singletonList(window));

		addSlotToVolumeConstraints(slot);

		calculatorProvider.addSalesPriceCalculator(priceCalculator);

		return element;
	}

	/**
	 */
	@Override
	@NonNull
	public IDischargeSlot createDischargeSlot(final String id, @NonNull final IPort port, final ITimeWindow window, final long minVolumeInM3, final long maxVolumeInM3, final long minCvValue,
			final long maxCvValue, final ISalesPriceCalculator pricePerMMBTu, final int durationHours, final int pricingDate, final PricingEventType pricingEvent, final boolean optional,
			final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {
		if (!ports.contains(port)) {
			throw new IllegalArgumentException("IPort was not created by this builder");
		}
		if (!timeWindows.contains(window)) {
			throw new IllegalArgumentException("ITimeWindow was not created by this builder");
		}

		final DischargeSlot slot = new DischargeSlot();

		final ISequenceElement element = configureDischargeOption(slot, id, port, window, minVolumeInM3, maxVolumeInM3, minCvValue, maxCvValue, pricePerMMBTu, pricingDate, pricingEvent, optional,
				isSpotMarketSlot, isVolumeLimitInM3);

		elementDurationsProvider.setElementDuration(element, durationHours);

		return slot;
	}

	/**
	 */
	@SuppressWarnings("null")
	@Override
	@NonNull
	public ICargo createCargo(final boolean allowRewiring, final IPortSlot... slots) {
		return createCargo(Lists.newArrayList(slots), allowRewiring);
	}

	/**
	 */
	@Override
	@NonNull
	public ICargo createCargo(final Collection<IPortSlot> slots, final boolean allowRewiring) {

		final Cargo cargo = new Cargo(new ArrayList<IPortSlot>(slots));
		cargoes.add(cargo);

		// Fix slot pairing if we disallow re-wiring or this is a complex cargo (more than just one load and one discharge)
		if (!allowRewiring || slots.size() > 2) {

			IPortSlot prevSlot = null;
			for (final IPortSlot slot : slots) {
				if (prevSlot != null && slot != null) {
					constrainSlotAdjacency(prevSlot, slot);
				}
				prevSlot = slot;
			}
		}

		return cargo;
	}

	@Override
	@NonNull
	public IPort createPort(final String name, final boolean arriveCold, final ICooldownCalculator cooldownCalculator, final String timezoneId, final int minCvValue, final int maxCvValue) {

		final Port port = new Port(indexingContext);
		buildPort(port, name, arriveCold, cooldownCalculator, timezoneId, minCvValue, maxCvValue);
		return port;
	}

	/**
	 * Convenience function only used for testing
	 */
	public IPort createPortForTest(@NonNull final String string, final boolean b, @Nullable final ICooldownCalculator object, @NonNull final String string2) {

		return createPort(string, b, object, string2, 0, Integer.MAX_VALUE);
	}

	/**
	 * Create the return elements for each port/resource combination
	 */
	private void createReturnElements() {
		for (final IResource resource : resources) {
			assert resource != null;
			final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
			assert endRequirement != null;
			for (final IPort port : ports) {
				assert port != null;
				returnElementProvider.setReturnElement(resource, port, createReturnElement(resource, port, endRequirement));
			}
		}
	}

	@NonNull
	private ISequenceElement createReturnElement(@NonNull final IResource resource, @NonNull final IPort port, @NonNull final IEndRequirement endRequirement) {
		final String name = "return-to-" + port.getName();
		final EndPortSlot slot = new EndPortSlot(name, port, null, endRequirement.isEndCold(), endRequirement.getTargetHeelInM3());
		final SequenceElement element = new SequenceElement(indexingContext, "return-to-" + port.getName());

		elementDurationsProvider.setElementDuration(element, 0);

		portProvider.setPortForElement(port, element);
		portSlotsProvider.setPortSlot(element, slot);
		// Return elements are always end elements?
		portTypeProvider.setPortType(element, PortType.End);

		if (startEndRequirementProvider.getEndRequirement(resource).getTimeWindow() != null) {
			// We should set the time window for all end elements for this
			// resource
			// to match the end requirement for the resource
			timeWindowProvider.setTimeWindows(element, Collections.singletonList(startEndRequirementProvider.getEndRequirement(resource).getTimeWindow()));
		} else {
			if (vesselProvider.getVesselAvailability(resource).getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER)) {
				// spot charters have no end time window, because their end date
				// is very flexible.
				final List<ITimeWindow> noTimeWindows = Collections.emptyList();
				timeWindowProvider.setTimeWindows(element, noTimeWindows);
			} else {
				// this defers setting the time windows to
				// getOptimisationData(), which will
				// choose a suitable end date for the optimisation and set all
				// the elements in
				// this list to have a time window around that end date
				endSlots.add(new Pair<ISequenceElement, PortSlot>(element, slot));
			}
		}
		return element;
	}

	@Override
	@NonNull
	public IXYPort createPort(final String name, final boolean arriveCold, final ICooldownCalculator cooldownCalculator, final float x, final float y, final String timezoneId, final int minCvValue,
			final int maxCvValue) {

		final XYPort port = new XYPort(indexingContext);
		buildPort(port, name, arriveCold, cooldownCalculator, timezoneId, minCvValue, maxCvValue);
		port.setX(x);
		port.setY(y);

		return port;
	}

	/**
	 * Convenience function only used for testing
	 */
	@NonNull
	public IXYPort createPortForTest(@NonNull final String name, final boolean arriveCold, @Nullable final ICooldownCalculator cooldownCalculator, final float x, final float y, @NonNull final String timezoneId) {

		return createPort(name, arriveCold, cooldownCalculator, x, y, timezoneId, 0, Integer.MAX_VALUE);
	}

	/**
	 * Method to set common properties etc for {@link Port} implementations.
	 * 
	 * @param port
	 * @param name
	 * @param arriveCold
	 * @param cooldownCalculator
	 * @param timezoneId
	 */
	private void buildPort(@NonNull final Port port, final String name, final boolean arriveCold, final ICooldownCalculator cooldownCalculator, final String timezoneId, final long minCvValue,
			final long maxCvValue) {

		port.setName(name);
		port.setShouldVesselsArriveCold(arriveCold);
		port.setCooldownCalculator(cooldownCalculator);
		port.setTimeZoneId(timezoneId);
		port.setMinCvValue(minCvValue);
		port.setMaxCvValue(maxCvValue);
		ports.add(port);

		// Pin variable for null analysis...
		final IPort localANYWHERE = ANYWHERE;
		if (localANYWHERE != null) {
			setPortToPortDistance(port, localANYWHERE, IMultiMatrixProvider.Default_Key, 0);
			setPortToPortDistance(localANYWHERE, port, IMultiMatrixProvider.Default_Key, 0);
		}

		// travel time from A to A should be zero, right?
		this.setPortToPortDistance(port, port, IMultiMatrixProvider.Default_Key, 0);

		calculatorProvider.addCooldownCalculator(cooldownCalculator);
	}

	@Override
	@NonNull
	public ITimeWindow createTimeWindow(final int start, final int end) {

		if (end != Integer.MIN_VALUE && start > end) {
			throw new IllegalArgumentException("Start time is greater than end time!");
		}

		final ITimeWindow window;
		if (end == Integer.MIN_VALUE) {
			final MutableTimeWindow mutableWindow = new MutableTimeWindow(start, end);
			openEndDateWindows.add(mutableWindow);
			window = mutableWindow;
		} else {
			window = new TimeWindow(start, end);
		}

		if (end > endOfLatestWindow) {
			endOfLatestWindow = end;
		}

		timeWindows.add(window);
		return window;
	}

	/**
	 * Create several spot vessels (see also {@code createSpotVessel}), named like namePrefix-1, namePrefix-2, etc
	 * 
	 * @param namePrefix
	 * @param vesselClass
	 * @param count
	 * @return
	 */
	@Override
	public List<IVesselAvailability> createSpotVessels(final String namePrefix, @NonNull final ISpotCharterInMarket spotCharterInMarket) {
		final List<IVesselAvailability> answer = new ArrayList<>(spotCharterInMarket.getAvailabilityCount());
		for (int i = 0; i < spotCharterInMarket.getAvailabilityCount(); i++) {
			answer.add(createSpotVessel(namePrefix + "-" + (i + 1), i, spotCharterInMarket));
		}
		return answer;
	}

	/**
	 * Create a spot charter vessel with no fixed start/end requirements and vessel instance type SPOT_CHARTER
	 * 
	 * @param name
	 * @param vesselClass
	 * @return the spot vessel
	 */
	@Override
	@NonNull
	public IVesselAvailability createSpotVessel(final String name, final int spotIndex, @NonNull final ISpotCharterInMarket spotCharterInMarket) {
		final IStartRequirement start = createStartRequirement(ANYWHERE, null, null);
		final IEndRequirement end = createEndRequirement(Collections.singletonList(ANYWHERE), null, /* endCold */true, 0);
		final IVesselClass vesselClass = spotCharterInMarket.getVesselClass();
		final ICurve dailyCharterInPrice = spotCharterInMarket.getDailyCharterInRateCurve();
		final IVessel spotVessel = createVessel(name, vesselClass, vesselClass.getCargoCapacity());
		// End cold already enforced in VoyagePlanner#getVoyageOptionsAndSetVpoChoices
		return createVesselAvailability(spotVessel, dailyCharterInPrice, VesselInstanceType.SPOT_CHARTER, start, end, spotCharterInMarket, spotIndex);
	}

	@Override
	@NonNull
	public IVessel createVessel(final String name, @NonNull final IVesselClass vesselClass, final long cargoCapacity) {

		if (!vesselClasses.contains(vesselClass)) {
			throw new IllegalArgumentException("IVesselClass was not created using this builder");
		}

		final Vessel vessel = new Vessel(name, vesselClass, cargoCapacity);

		vessels.add(vessel);
		return vessel;
	}

	@Override
	public IHeelOptions createHeelOptions(final long heelInM3, final int heelCVValue, final int heelUnitPrice) {
		return new HeelOptions(heelInM3, heelCVValue, heelUnitPrice);
	}

	@Override
	@NonNull
	public IVesselAvailability createVesselAvailability(@NonNull final IVessel vessel, final ICurve dailyCharterInRate, final VesselInstanceType vesselInstanceType, final IStartRequirement start,
			final IEndRequirement end) {
		return createVesselAvailability(vessel, dailyCharterInRate, vesselInstanceType, start, end, null, -1);
	}

	@NonNull
	private IVesselAvailability createVesselAvailability(@NonNull final IVessel vessel, final ICurve dailyCharterInRate, @NonNull final VesselInstanceType vesselInstanceType,
			@NonNull final IStartRequirement start, @NonNull final IEndRequirement end, @Nullable final ISpotCharterInMarket spotCharterInMarket, final int spotIndex) {
		if (!vessels.contains(vessel)) {
			throw new IllegalArgumentException("IVessel was not created using this builder");
		}

		// if (!ports.contains(startPort)) {
		// throw new IllegalArgumentException(
		// "Start IPort was not created using this builder");
		// }

		// if (!ports.contains(endPort)) {
		// throw new IllegalArgumentException(
		// "End IPort was not created using this builder");
		// }

		final String name = vessel.getName();// + vesselAvailabilities.size();

		final DefaultVesselAvailability vesselAvailability = new DefaultVesselAvailability();
		vesselAvailability.setVessel(vessel);

		vesselAvailability.setVesselInstanceType(vesselInstanceType);

		vesselAvailability.setDailyCharterInRate(dailyCharterInRate);

		vesselAvailability.setStartRequirement(start);
		vesselAvailability.setEndRequirement(end);

		final IResource resource = new Resource(indexingContext, name);

		// Register with provider
		vesselProvider.setVesselAvailabilityResource(resource, vesselAvailability);
		vesselAvailabilities.add(vesselAvailability);
		if (vesselInstanceType != VesselInstanceType.DES_PURCHASE && vesselInstanceType != VesselInstanceType.FOB_SALE) {
			realVesselAvailabilities.add(vesselAvailability);
		}

		resources.add(resource);

		// If no time requirement is specified then the time window is at the
		// very start of the job
		final ITimeWindow startWindow = start.hasTimeRequirement() ? start.getTimeWindow() : createTimeWindow(0, 0);

		final StartPortSlot startSlot = new StartPortSlot(start.getHeelOptions());
		startSlot.setId("start-" + name);
		startSlot.setPort((start.hasPortRequirement() && start.getLocation() != null) ? start.getLocation() : ANYWHERE);

		startSlot.setTimeWindow(startWindow);

		final EndPortSlot endSlot = new EndPortSlot("end-" + name, (end.hasPortRequirement() && end.getLocation() != null) ? end.getLocation() : ANYWHERE, null, end.isEndCold(),
				end.getTargetHeelInM3());

		// Create start/end sequence elements for this route
		final SequenceElement startElement = new SequenceElement(indexingContext, startSlot.getId());
		final SequenceElement endElement = new SequenceElement(indexingContext, endSlot.getId());

		sequenceElements.add(startElement);
		sequenceElements.add(endElement);
		elementDurationsProvider.setElementDuration(endElement, 0);
		elementDurationsProvider.setElementDuration(startElement, 0);

		if (end.hasTimeRequirement() == false) {
			// put end slot into list of slots to patch up later.
			// Fleet vessels and spot vessels both run to the end of the optimisation if they don't have an end date.
			if (!vesselInstanceType.equals(VesselInstanceType.SPOT_CHARTER)) {
				endSlots.add(new Pair<ISequenceElement, PortSlot>(endElement, endSlot));
			}
		} else {
			endSlot.setTimeWindow(end.getTimeWindow());
		}

		timeWindowProvider.setTimeWindows(startElement, Collections.singletonList(startSlot.getTimeWindow()));

		if (end.hasTimeRequirement()) {
			timeWindowProvider.setTimeWindows(endElement, Collections.singletonList(endSlot.getTimeWindow()));
		} // otherwise this will be set in getOptimisationData().

		startElement.setName(startSlot.getId() + "-" + startSlot.getPort().getName());
		endElement.setName(endSlot.getId() + "-" + endSlot.getPort().getName());

		portProvider.setPortForElement(startSlot.getPort(), startElement);
		portProvider.setPortForElement(endSlot.getPort(), endElement);

		portTypeProvider.setPortType(startElement, PortType.Start);
		portTypeProvider.setPortType(endElement, PortType.End);

		portSlotsProvider.setPortSlot(startElement, startSlot);
		portSlotsProvider.setPortSlot(endElement, endSlot);

		resourceAllocationProvider.setAllowedResources(startElement, Collections.singleton(resource));
		// BugzID: 576 allow end element on any vessel, to prevent ResourceAllocationConstraint from disallowing 2opt2s at end

		// resourceAllocationProvider.setAllowedResources(endElement, Collections.singleton(resource));

		startEndRequirementProvider.setStartEndRequirements(resource, start, end);
		startEndRequirementProvider.setStartEndElements(resource, startElement, endElement);

		if (spotCharterInMarket != null) {
			vesselAvailability.setSpotCharterInMarket(spotCharterInMarket);
			vesselAvailability.setSpotIndex(spotIndex);
		}

		return vesselAvailability;
	}

	@NonNull
	public IStartRequirement createStartRequirement() {
		return createStartRequirement(ANYWHERE, null, null);
	}

	@NonNull
	public IEndRequirement createEndRequirement() {
		return createEndRequirement(Collections.singletonList(ANYWHERE), null, false, 0);
	}

	@Override
	@NonNull
	public IStartRequirement createStartRequirement(@Nullable final IPort fixedPort, @Nullable final ITimeWindow timeWindow, @Nullable final IHeelOptions heelOptions) {

		return new StartRequirement(fixedPort == null ? ANYWHERE : fixedPort, fixedPort != null, timeWindow, heelOptions);
	}

	@Override
	@NonNull
	public IEndRequirement createEndRequirement(@Nullable final Collection<IPort> portSet, @Nullable final ITimeWindow timeWindow, final boolean endCold, final long targetHeelInM3) {

		if (portSet == null || portSet.isEmpty()) {
			return new EndRequirement(Collections.singleton(ANYWHERE), false, timeWindow, endCold, targetHeelInM3);
		} else {
			return new EndRequirement(portSet, true, timeWindow, endCold, targetHeelInM3);
		}
	}

	@Override
	public void setPortToPortDistance(@NonNull final IPort from, @NonNull final IPort to, @NonNull final String route, final int distance) {

		if (!ports.contains(from)) {
			throw new IllegalArgumentException("From IPort was not created using this builder");
		}
		if (!ports.contains(to)) {
			throw new IllegalArgumentException("To IPort was not created using this builder");
		}

		if (portDistanceProvider.containsKey(route) == false) {
			/*
			 * This route has not previously been added to the PDP; initialise a blank matrix here?
			 */
			portDistanceProvider.set(route, matrixEditorProvider.get());
		}

		final IMatrixEditor<IPort, Integer> matrix = (IMatrixEditor<IPort, Integer>) portDistanceProvider.get(route);

		matrix.set(from, to, distance);
	}

	/**
	 */
	@Override
	public void setVesselClassRouteCost(final String route, @NonNull final IVesselClass vesselClass, final VesselState state, final long tollPrice) {
		routeCostProvider.setRouteCost(route, vesselClass, state, tollPrice);
	}

	/**
	 */
	@Override
	public void setDefaultRouteCost(final String route, final long defaultPrice) {
		routeCostProvider.setDefaultRouteCost(route, defaultPrice);
	}

	@Override
	public void setVesselClassRouteFuel(final String name, @NonNull final IVesselClass vc, final VesselState vesselState, final long baseFuelInScaledMT, final long nboRateInScaledM3) {
		routeCostProvider.setRouteFuel(name, vc, vesselState, baseFuelInScaledMT, nboRateInScaledM3);
	}

	@Override
	public void setVesselClassRouteTransitTime(final String name, final IVesselClass vc, final int transitTimeInHours) {
		routeCostProvider.setRouteTransitTime(name, vc, transitTimeInHours);
	}

	@Override
	public void setElementDurations(@NonNull final ISequenceElement element, @NonNull final IResource resource, final int duration) {
		elementDurationsProvider.setElementDuration(element, resource, duration);
	}

	@Override
	public IOptimisationData getOptimisationData() {

		final boolean enableCargoShorts = false;

		if (enableCargoShorts) {
			// // TODO: Look up appropriate definition
			final IVesselClass cargoShortsClass = vesselClasses.get(1);// createVesselClass("short-class", OptimiserUnitConvertor.convertToInternalSpeed(18.0),
			assert cargoShortsClass != null;
			IVessel pShortCargoVessel = createVessel("short-vessel", cargoShortsClass, cargoShortsClass.getCargoCapacity());
			shortCargoVessel = pShortCargoVessel;
			assert pShortCargoVessel != null;
			shortCargoVesselAvailability = createVesselAvailability(pShortCargoVessel, new ConstantValueCurve(200000), VesselInstanceType.CARGO_SHORTS, createStartRequirement(),
					createEndRequirement());
		}
		// create return elements before fixing time windows,
		// because the next bit will have to patch up their time windows
		createReturnElements();

		if (enableCargoShorts) {
			createChargoShortReturnElements();
		}

		portDistanceProvider.cacheExtremalValues(ports);

		// Patch up end time windows
		// The return time should be the soonest we can get back to the previous load,
		// presumably in the slowest vessel without going via a canal.

		// TODO what about return to first load rule?

		int latestDischarge = 0;
		IPort loadPort = null, dischargePort = null;
		for (final ICargo cargo : cargoes) {
			if (cargo.getPortSlots().size() > 1) {
				final IPortSlot end = cargo.getPortSlots().get(cargo.getPortSlots().size() - 1);
				final IPortSlot endMinus1 = cargo.getPortSlots().get(cargo.getPortSlots().size() - 2);
				final int endOfDischargeWindow = end.getTimeWindow().getEnd();
				if (endOfDischargeWindow > latestDischarge) {
					latestDischarge = endOfDischargeWindow;
					loadPort = endMinus1.getPort();
					dischargePort = end.getPort();
				}
			}
		}

		// 0 == return to current load,
		// 1 == return to farthest in time load
		// 2== end window
		// 3 == discharge + 60
		final int rule = 3;
		final int latestTime;
		if (rule == 0) {
			/**
			 * The shortest time which the slowest vessel in the fleet can take to get from the latest discharge back to the load for that discharge.
			 */
			int maxFastReturnTime = 0;
			if ((dischargePort != null) && (loadPort != null)) {
				final int returnDistance = portDistanceProvider.getMaximumValue(dischargePort, loadPort);
				// what's the slowest vessel class
				int slowestMaxSpeed = Integer.MAX_VALUE;
				for (final IVesselClass vesselClass : vesselClasses) {
					if (vesselClass == virtualClass) {
						continue;
					}
					slowestMaxSpeed = Math.min(slowestMaxSpeed, vesselClass.getMaxSpeed());
				}
				maxFastReturnTime = Math.max(maxFastReturnTime, Calculator.getTimeFromSpeedDistance(slowestMaxSpeed, returnDistance));
			} else {
				latestDischarge = 0;
				maxFastReturnTime = 0;
			}
			latestTime = Math.max(endOfLatestWindow + (24 * minDaysFromLastEventToEnd), maxFastReturnTime + latestDischarge);
		} else if (rule == 1) {
			/**
			 * The shortest time which the slowest vessel in the fleet can take to get from the latest discharge back to the load for that discharge.
			 */
			int maxFastReturnTime = 0;
			if ((dischargePort != null)) {
				for (final ILoadOption loadSlot : loadSlots) {
					final int returnDistance = portDistanceProvider.getMaximumValue(dischargePort, loadSlot.getPort());
					if (returnDistance == Integer.MAX_VALUE) {
						continue;
					}
					// what's the slowest vessel class
					int slowestMaxSpeed = Integer.MAX_VALUE;
					for (final IVesselClass vesselClass : vesselClasses) {
						if (vesselClass == virtualClass) {
							continue;
						}
						slowestMaxSpeed = Math.min(slowestMaxSpeed, vesselClass.getMaxSpeed());
					}
					maxFastReturnTime = Math.max(maxFastReturnTime, Calculator.getTimeFromSpeedDistance(slowestMaxSpeed, returnDistance));
				}
			} else {
				latestDischarge = 0;
				maxFastReturnTime = 0;
			}
			latestTime = Math.max(endOfLatestWindow + (24 * minDaysFromLastEventToEnd), maxFastReturnTime + latestDischarge);
		} else if (rule == 2) {
			latestTime = Math.max(endOfLatestWindow, latestDischarge);
		} else {
			latestTime = Math.max(endOfLatestWindow, latestDischarge) + 60 * 24;
		}

		for (final Pair<ISequenceElement, PortSlot> elementAndSlot : endSlots) {
			final ITimeWindow endWindow = createTimeWindow(latestTime, latestTime + (35 * 24));
			elementAndSlot.getSecond().setTimeWindow(endWindow);
			timeWindowProvider.setTimeWindows(elementAndSlot.getFirst(), Collections.singletonList(endWindow));
		}
		for (final MutableTimeWindow window : openEndDateWindows) {
			window.setEnd(latestTime);
		}

		// Generate DES Purchase and FOB Sale slot bindings before applying vessel restriction constraints
		doBindDischargeSlotsToDESPurchase();
		doBindLoadSlotsToFOBSale();

		// configure constraints
		applyAdjacencyConstraints();
		applyVesselRestrictionConstraints();

		linkMarkToMarkets();
		// set the self-self distance to zero for all ports to make sure indexed DCP has seen every element.

		// this is no good, because it makes lots of canal choices happen.
		// for (final String s : portDistanceProvider.getKeySet()) {
		// final IMatrixEditor<IPort, Integer> matrix = (IMatrixEditor<IPort, Integer>) portDistanceProvider.get(s);
		// for (final IPort port : ports) {
		// matrix.set(port, port, 0);
		// }
		// }

		// Tell the optional element provider about non-optional elements it may not have seen
		// if this seems a bit ridiculous, yes, it is.
		// TODO think about how this connects with return elements - they aren't optional
		// but they also aren't all required.
		// for (final ISequenceElement element : sequenceElements) {
		// boolean elementOptional = optionalElements.isElementOptional(element);
		// if (!elementOptional) {
		// optionalElements.setOptional(element, elementOptional);
		// }
		// }

		final OptimisationData data = new OptimisationData();

		data.setResources(resources);
		data.setSequenceElements(sequenceElements);

		for (final IBuilderExtension extension : extensions) {
			extension.finishBuilding(data);
		}

		return data;
	}

	/**
	 * Generate a new return option for each load port in the solution.
	 */
	private void createChargoShortReturnElements() {
		for (final ILoadOption loadOption : loadSlots) {
			final ISequenceElement loadElement = portSlotsProvider.getElement(loadOption);

			final String name = "short-return-to-" + loadElement.getName();
			final IPort port = loadOption.getPort();
			final CargoShortEnd returnPortSlot = new CargoShortEnd(name, port);

			final SequenceElement returnElement = new SequenceElement(indexingContext, name);
			timeWindowProvider.setTimeWindows(returnElement, Collections.<ITimeWindow> emptyList());

			elementDurationsProvider.setElementDuration(returnElement, 0);

			portProvider.setPortForElement(port, returnElement);
			portSlotsProvider.setPortSlot(returnElement, returnPortSlot);
			portTypeProvider.setPortType(returnElement, returnPortSlot.getPortType());

			shortCargoReturnElementProviderEditor.setReturnElement(loadElement, loadOption, returnElement);
		}
	}

	@NonNull
	private IVesselAvailability createVirtualVesselAvailability(@NonNull final ISequenceElement element, @NonNull final VesselInstanceType type) {
		assert type == VesselInstanceType.DES_PURCHASE || type == VesselInstanceType.FOB_SALE;
		// create a new resource for each of these guys, and bind them to their resources
		assert virtualClass != null;
		final IVessel virtualVessel = createVessel("virtual-" + element.getName(), virtualClass, virtualClass.getCargoCapacity());
		final IVesselAvailability virtualVesselAvailability = createVesselAvailability(virtualVessel, ZeroCurve.getInstance(), type, createStartRequirement(), createEndRequirement());
		// Bind every slot to its vessel
		final IPortSlot portSlot = portSlotsProvider.getPortSlot(element);
		assert portSlot != null;
		freezeSlotToVesselAvailability(portSlot, virtualVesselAvailability);

		virtualVesselAvailabilityMap.put(element, virtualVesselAvailability);
		virtualVesselSlotProviderEditor.setVesselAvailabilityForElement(virtualVesselAvailability, element);

		return virtualVesselAvailability;
	}

	@Override
	public void buildXYDistances() {
		for (final IPort from : ports) {
			if (!(from instanceof IXYPort)) {
				continue;
			}
			for (final IPort to : ports) {
				if (to instanceof IXYPort) {
					final double dist = distanceProvider.getDistance((IXYPort) from, (IXYPort) to);
					final int iDist = (int) dist;

					final IMatrixEditor<IPort, Integer> matrix = (IMatrixEditor<IPort, Integer>) portDistanceProvider.get(IMultiMatrixProvider.Default_Key);

					matrix.set(from, to, iDist);
				}
			}
		}
	}

	@Override
	public void dispose() {

		vessels.clear();
		realVesselAvailabilities.clear();
		vesselAvailabilities.clear();
		cargoes.clear();
		ports.clear();

		for (final IBuilderExtension extension : extensions) {
			extension.dispose();
		}

		extensions.clear();
	}

	/**
	 */
	@Override
	@NonNull
	public IVesselClass createVesselClass(final String name, final int minSpeed, final int maxSpeed, final long capacityInM3, final long safetyHeelInM3, final IBaseFuel baseFuel,
			final int pilotLightRate, final int warmupTimeHours, final long cooldownVolumeM3, final int minBaseFuelConsumptionPerDay) {

		final VesselClass vesselClass = new VesselClass();
		vesselClass.setName(name);

		vesselClass.setMinSpeed(minSpeed);
		vesselClass.setMaxSpeed(maxSpeed);

		vesselClass.setCargoCapacity(capacityInM3);
		vesselClass.setSafetyHeel(safetyHeelInM3);

		vesselClass.setWarmupTime(warmupTimeHours);
		vesselClass.setCooldownVolume(cooldownVolumeM3);

		vesselClass.setPilotLightRate(pilotLightRate);
		vesselClass.setMinBaseFuelConsumptionInMTPerDay(minBaseFuelConsumptionPerDay);

		vesselClass.setBaseFuel(baseFuel);
		vesselClasses.add(vesselClass);

		return vesselClass;
	}

	@Override
	@NonNull
	public IBaseFuel createBaseFuel(final String name, final int equivalenceFactor) {
		final BaseFuel baseFuel = new BaseFuel(name);
		baseFuel.setEquivalenceFactor(equivalenceFactor);
		return baseFuel;
	}

	/**
	 */
	@Override
	public void setVesselClassStateParameters(@NonNull final IVesselClass vesselClass, final VesselState state, final int nboRateInM3PerDay, final int idleNBORateInM3PerDay,
			final int idleConsumptionRateInMTPerDay, final IConsumptionRateCalculator consumptionRateCalculatorInMTPerDay, final int serviceSpeed) {

		if (!vesselClasses.contains(vesselClass)) {
			throw new IllegalArgumentException("IVesselClass was not created using this builder");
		}

		// Check instance is the same as that used in createVesselClass(..)
		if (!(vesselClass instanceof VesselClass)) {
			throw new IllegalArgumentException("Expected instance of " + VesselClass.class.getCanonicalName());
		}

		final VesselClass vc = (VesselClass) vesselClass;

		vc.setNBORate(state, nboRateInM3PerDay);
		vc.setIdleNBORate(state, idleNBORateInM3PerDay);
		vc.setIdleConsumptionRate(state, idleConsumptionRateInMTPerDay);
		vc.setConsumptionRate(state, consumptionRateCalculatorInMTPerDay);
		vc.setServiceSpeed(state, serviceSpeed);
	}

	/**
	 */
	@Override
	public void setVesselClassPortTypeParameters(@NonNull final IVesselClass vc, final PortType portType, final int inPortConsumptionRateInMTPerDay) {

		((VesselClass) vc).setInPortConsumptionRateInMTPerDay(portType, inPortConsumptionRateInMTPerDay);
	}

	@Override
	public void setVesselClassInaccessiblePorts(@NonNull final IVesselClass vc, final Set<IPort> inaccessiblePorts) {
		this.portExclusionProvider.setExcludedPorts(vc, inaccessiblePorts);
	}

	/**
	 */
	@Override
	public void setVesselInaccessiblePorts(@NonNull final IVessel vessel, final Set<IPort> inaccessiblePorts) {
		this.portExclusionProvider.setExcludedPorts(vessel, inaccessiblePorts);
	}

	/**
	 */
	@Override
	@NonNull
	public IVesselEventPortSlot createCharterOutEvent(final String id, final ITimeWindow arrival, final IPort fromPort, final IPort toPort, final int durationHours, final long maxHeelOut,
			final int heelCVValue, final int heelUnitPrice, final long totalHireRevenue, final long repositioning) {
		return createVesselEvent(id, PortType.CharterOut, arrival, fromPort, toPort, durationHours, maxHeelOut, heelCVValue, heelUnitPrice, totalHireRevenue, repositioning);
	}

	/**
	 */
	@Override
	@NonNull
	public IGeneratedCharterOutVesselEventPortSlot createGeneratedCharterOutEvent(final String id, final ITimeWindow arrival, final IPort fromPort, final IPort toPort, final int durationHours,
			final long maxHeelOut, final int heelCVValue, final int heelUnitPrice, final long hireCost, final long repositioning) {
		return createGeneratedCharterOutVesselEvent(id, arrival, fromPort, toPort, durationHours, maxHeelOut, heelCVValue, heelUnitPrice, hireCost, repositioning);
	}

	@Override
	@NonNull
	public IVesselEventPortSlot createDrydockEvent(final String id, final ITimeWindow arrival, final IPort port, final int durationHours) {
		return createVesselEvent(id, PortType.DryDock, arrival, port, port, durationHours, 0, 0, 0, 0, 0);
	}

	@Override
	@NonNull
	public IVesselEventPortSlot createMaintenanceEvent(final String id, final ITimeWindow arrival, final IPort port, final int durationHours) {
		return createVesselEvent(id, PortType.Maintenance, arrival, port, port, durationHours, 0, 0, 0, 0, 0);
	}

	/**
	 */
	@NonNull
	public IVesselEventPortSlot createVesselEvent(final String id, final PortType portType, final ITimeWindow arrival, final IPort fromPort, final IPort toPort, final int durationHours,
			final long maxHeelOut, final int heelCVValue, final int heelUnitPrice, final long totalHireRevenue, final long repositioning) {
		final VesselEvent event = new VesselEvent();

		// TODO should start port and end port be set on this single sequence
		// element,
		// or should there be a second invisible sequence element for
		// repositioning, and something
		// which rigs the distance to be zero between repositioning elements?

		event.setTimeWindow(arrival);
		event.setDurationHours(durationHours);
		event.setStartPort(fromPort);
		event.setEndPort(toPort);
		event.setMaxHeelOut(maxHeelOut);
		event.setHeelCVValue(heelCVValue);
		event.setHeelUnitPrice(heelUnitPrice);
		event.setHireOutRevenue(totalHireRevenue);
		event.setRepositioning(repositioning);

		final VesselEventPortSlot slot = new VesselEventPortSlot(id, event.getEndPort(), event.getTimeWindow(), event);
		slot.setPortType(portType);
		vesselEvents.add(slot);
		slotVesselAvailabilityRestrictions.put(slot, new HashSet<IVesselAvailability>());
		slotVesselClassRestrictions.put(slot, new HashSet<IVesselClass>());

		buildVesselEvent(slot);
		return slot;
	}

	/**
	 */
	@NonNull
	private IGeneratedCharterOutVesselEventPortSlot createGeneratedCharterOutVesselEvent(final String id, final ITimeWindow arrival, final IPort fromPort, final IPort toPort, final int durationHours,
			final long maxHeelOut, final int heelCVValue, final int heelUnitPrice, final long hireCost, final long repositioning) {
		final GeneratedCharterOutVesselEvent event = new GeneratedCharterOutVesselEvent();

		// TODO should start port and end port be set on this single sequence
		// element,
		// or should there be a second invisible sequence element for
		// repositioning, and something
		// which rigs the distance to be zero between repositioning elements?

		event.setTimeWindow(arrival); // TODO: this may fail...
		event.setDurationHours(durationHours);
		event.setStartPort(fromPort);
		event.setEndPort(toPort);
		event.setMaxHeelOut(maxHeelOut);
		event.setHeelCVValue(heelCVValue);
		event.setHeelUnitPrice(heelUnitPrice);
		event.setHireOutRevenue(hireCost);
		event.setRepositioning(repositioning);

		final GeneratedCharterOutVesselEventPortSlot slot = new GeneratedCharterOutVesselEventPortSlot(id, event.getEndPort(), event.getTimeWindow(), event);
		slot.setPortType(PortType.GeneratedCharterOut);
		return slot;
	}

	@Override
	public void addVesselEventVessel(@NonNull final IVesselEventPortSlot charterOut, final IVesselAvailability vesselAvailability) {
		checkNotNull(charterOut);
		checkNotNull(vesselAvailability);
		if (!vesselAvailabilities.contains(vesselAvailability)) {
			throw new IllegalArgumentException("IVessel was not created using this builder");
		}
		if (!vesselEvents.contains(charterOut)) {
			throw new IllegalArgumentException("ICharterOut was not created using this builder");
		}
		slotVesselAvailabilityRestrictions.get(charterOut).add(vesselAvailability);
	}

	@Override
	public void addVesselEventVesselClass(@NonNull final IVesselEventPortSlot charterOut, final IVesselClass vesselClass) {
		checkNotNull(charterOut);
		checkNotNull(vesselClass);
		if (!vesselClasses.contains(vesselClass)) {
			throw new IllegalArgumentException("IVesselClass was not created using this builder");
		}
		if (!vesselEvents.contains(charterOut)) {
			throw new IllegalArgumentException("ICharterOut was not created using this builder");
		}
		slotVesselClassRestrictions.get(charterOut).add(vesselClass);
	}

	protected void buildVesselEvent(final IVesselEventPortSlot slot) {
		final IVesselEvent vesselEvent = slot.getVesselEvent();

		final SequenceElement endElement = new SequenceElement(indexingContext, slot.getId());

		if (vesselEvent.getStartPort() != vesselEvent.getEndPort()) {
			// We insert two extra elements and slots, so that we go
			// startPort -> ANYWHERE -> endPort
			// this means we also have to fix any sequencing constraints
			// which have already been set up, and replicate the vessel
			// allocation constraints.
			final VesselEventPortSlot startSlot = new VesselEventPortSlot("start-" + slot.getId(), vesselEvent.getStartPort(), slot.getTimeWindow(), vesselEvent);
			final VesselEventPortSlot redirectSlot = new VesselEventPortSlot("redirect-" + slot.getId(), ANYWHERE, slot.getTimeWindow(), vesselEvent);

			startSlot.setPortType(PortType.Other);
			redirectSlot.setPortType(PortType.Virtual);

			final SequenceElement startElement = new SequenceElement(indexingContext, startSlot.getId());
			final SequenceElement redirectElement = new SequenceElement(indexingContext, redirectSlot.getId());

			orderedSequenceElementsEditor.setElementOrder(startElement, redirectElement);
			orderedSequenceElementsEditor.setElementOrder(redirectElement, endElement);

			timeWindowProvider.setTimeWindows(startElement, Collections.singletonList(vesselEvent.getTimeWindow()));
			elementDurationsProvider.setElementDuration(startElement, 0);
			elementDurationsProvider.setElementDuration(redirectElement, 0);

			portSlotsProvider.setPortSlot(startElement, startSlot);
			portSlotsProvider.setPortSlot(redirectElement, redirectSlot);

			portTypeProvider.setPortType(startElement, startSlot.getPortType());
			portTypeProvider.setPortType(redirectElement, redirectSlot.getPortType());

			portProvider.setPortForElement(startSlot.getPort(), startElement);
			portProvider.setPortForElement(redirectSlot.getPort(), redirectElement);

			sequenceElements.add(startElement);
			sequenceElements.add(redirectElement);

			// replicate vessel constraints
			constrainSlotToVesselClasses(startSlot, slotVesselClassRestrictions.get(slot));
			constrainSlotToVesselAvailabilities(startSlot, slotVesselAvailabilityRestrictions.get(slot));

			constrainSlotToVesselClasses(redirectSlot, slotVesselClassRestrictions.get(slot));
			constrainSlotToVesselAvailabilities(redirectSlot, slotVesselAvailabilityRestrictions.get(slot));

			// patch up sequencing constraints
			if (reverseAdjacencyConstraints.containsKey(slot)) {
				// whatever was meant to be before slot should now be before
				// startSlot, and so on
				constrainSlotAdjacency(reverseAdjacencyConstraints.get(slot), startSlot);
				constrainSlotAdjacency(startSlot, redirectSlot);
				constrainSlotAdjacency(redirectSlot, slot);
			}
		}

		sequenceElements.add(endElement);

		timeWindowProvider.setTimeWindows(endElement, Collections.singletonList(vesselEvent.getTimeWindow()));
		portTypeProvider.setPortType(endElement, slot.getPortType());

		elementDurationsProvider.setElementDuration(endElement, vesselEvent.getDurationHours());

		// element needs a port slot

		portSlotsProvider.setPortSlot(endElement, slot);

		portProvider.setPortForElement(slot.getPort(), endElement);

	}

	@Override
	public void addTotalVolumeConstraint(final Set<IPort> ports, final boolean loads, final boolean discharges, final long maximumTotalVolume, final ITimeWindow timeWindow) {
		final TotalVolumeLimit limit = new TotalVolumeLimit();
		limit.setTimeWindow(timeWindow);
		limit.setVolumeLimit(maximumTotalVolume);

		for (final IPort port : ports) {
			if (loads) {
				List<TotalVolumeLimit> limits = loadLimits.get(port);
				if (limits == null) {
					limits = new ArrayList<TotalVolumeLimit>();
				}
				limits.add(limit);
				loadLimits.put(port, limits);
			}

			if (discharges) {
				List<TotalVolumeLimit> limits = dischargeLimits.get(port);
				if (limits == null) {
					limits = new ArrayList<TotalVolumeLimit>();
				}
				limits.add(limit);
				dischargeLimits.put(port, limits);
			}
		}

		for (final ISequenceElement element : sequenceElements) {
			addSlotToVolumeConstraints(portSlotsProvider.getPortSlot(element));
		}

		totalVolumeLimits.addTotalVolumeLimit(limit);
	}

	private void addSlotToVolumeConstraints(final IPortSlot slot) {
		if (slot == null) {
			return;
		}
		final IPort port = slot.getPort();
		final List<TotalVolumeLimit> limits;
		if (slot instanceof ILoadSlot) {
			limits = loadLimits.get(port);
		} else if (slot instanceof IDischargeSlot) {
			limits = dischargeLimits.get(port);
		} else {
			return;
		}
		if (limits != null) {
			for (final TotalVolumeLimit limit : limits) {
				limit.addSlotIfWindowsMatch(slot);
			}
		}
	}

	@Override
	public void setSlotVesselAvailabilityRestriction(final IPortSlot slot, final Set<IVesselAvailability> vesselAvailabilities) {

		assert slot != null;
		constrainSlotToVesselAvailabilities(slot, vesselAvailabilities);
	}

	@Override
	/**
	 * Set a discount curve for the given fitness component name
	 * 
	 * @param name
	 * @param iCurve
	 */
	public void setFitnessComponentDiscountCurve(final String name, final ICurve iCurve) {
		discountCurveProvider.setDiscountCurve(name, iCurve);

	}

	/**
	 * Set up the {@link #orderedSequenceElementsEditor} to apply the constraints described in {@link #forwardAdjacencyConstraints} and {@link #reverseAdjacencyConstraints};
	 */
	private void applyAdjacencyConstraints() {
		for (final Map.Entry<IPortSlot, IPortSlot> entry : forwardAdjacencyConstraints.entrySet()) {
			orderedSequenceElementsEditor.setElementOrder(portSlotsProvider.getElement(entry.getKey()), portSlotsProvider.getElement(entry.getValue()));
			assert reverseAdjacencyConstraints.get(entry.getValue()) == entry.getKey() : "Adjacency constraints have become inconsistent";
		}
	}

	/**
	 * Set up the {@link #resourceAllocationProvider} to apply the constraints described in {@link #slotVesselClassRestrictions} and {@link #slotVesselRestrictions}.
	 */
	private void applyVesselRestrictionConstraints() {
		final HashSet<IPortSlot> restrictedSlots = new HashSet<IPortSlot>();
		restrictedSlots.addAll(slotVesselAvailabilityRestrictions.keySet());
		restrictedSlots.addAll(slotVesselClassRestrictions.keySet());

		for (final IPortSlot slot : restrictedSlots) {

			Set<IVesselAvailability> allowedVessels = slotVesselAvailabilityRestrictions.get(slot);
			if (allowedVessels == null) {
				allowedVessels = Collections.emptySet();
			}

			Set<IVesselClass> allowedVesselClasses = slotVesselClassRestrictions.get(slot);
			if (allowedVesselClasses == null) {
				allowedVesselClasses = Collections.emptySet();
			}

			if (allowedVesselClasses.isEmpty() && allowedVessels.isEmpty()) {
				continue;
			}

			final HashSet<IResource> allowedResources = new HashSet<IResource>();

			for (final IVesselAvailability vesselAvailability : vesselAvailabilities) {

				assert vesselAvailability != null;

				if (allowedVessels.contains(vesselAvailability) || allowedVesselClasses.contains(vesselAvailability.getVessel().getVesselClass())) {
					/*
					 * If this is a vessel event, and it's a spot vessel, don't allow it. In future this should probably be handled as a separate kind of constraint (a vessel instance type
					 * restriction, saying slot X can only go on vessels of types Y and Z)
					 */
					if ((slot instanceof IVesselEventPortSlot) && (vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER)) {
						continue;
					}
					allowedResources.add(vesselProvider.getResource(vesselAvailability));
				}

				if (shortCargoVessel != null) {
					// Cargo shorts only applies to load and discharge slots
					if (loadSlots.contains(slot) || dischargeSlots.contains(slot)) {
						allowedResources.add(vesselProvider.getResource(shortCargoVesselAvailability));
					}
				}
			}
			resourceAllocationProvider.setAllowedResources(portSlotsProvider.getElement(slot), allowedResources);
		}

		// Finally, allow any other slot to be linked to any non-virtual vessel. This helps the optional move generator.
		final HashSet<IResource> allowedResources = new HashSet<IResource>();

		for (final IVesselAvailability vesselAvailability : vesselAvailabilities) {
			assert vesselAvailability != null;

			// Skip virtual vessels
			if (virtualVesselAvailabilityMap.values().contains(vesselAvailability)) {
				continue;
			}
			allowedResources.add(vesselProvider.getResource(vesselAvailability));
		}

		final HashSet<IPortSlot> unrestrictedSlots = new HashSet<IPortSlot>();
		unrestrictedSlots.addAll(loadSlots);
		unrestrictedSlots.addAll(dischargeSlots);
		// Note this might not be the correct behaviour for event slots - although typically we expect them to be part of the restrictedSlots array.
		unrestrictedSlots.addAll(vesselEvents);
		// unrestrictedSlots.removeAll(unshippedElements);
		for (final ISequenceElement unshippedElement : unshippedElements) {
			unrestrictedSlots.add(portSlotsProvider.getPortSlot(unshippedElement));
		}
		unrestrictedSlots.removeAll(restrictedSlots);
		for (final IPortSlot slot : unrestrictedSlots) {
			resourceAllocationProvider.setAllowedResources(portSlotsProvider.getElement(slot), allowedResources);
		}
	}

	@Override
	public void constrainSlotToVesselAvailabilities(@NonNull final IPortSlot slot, @Nullable Set<IVesselAvailability> vesselAvailabilities) {
		if ((vesselAvailabilities == null) || vesselAvailabilities.isEmpty()) {
			slotVesselAvailabilityRestrictions.remove(slot);
		} else {
			slotVesselAvailabilityRestrictions.put(slot, new HashSet<>(vesselAvailabilities));
		}
	}

	@Override
	public void constrainSlotToVesselClasses(@NonNull final IPortSlot slot, @Nullable final Set<IVesselClass> vesselClasses) {
		if ((vesselClasses == null) || vesselClasses.isEmpty()) {
			slotVesselClassRestrictions.remove(slot);
		} else {
			slotVesselClassRestrictions.put(slot, new HashSet<>(vesselClasses));
		}
	}

	@Override
	public void constrainSlotAdjacency(@Nullable final IPortSlot firstSlot, @Nullable final IPortSlot secondSlot) {
		if (firstSlot == null) {
			if (reverseAdjacencyConstraints.containsKey(secondSlot)) {
				final IPortSlot pre = reverseAdjacencyConstraints.get(secondSlot);
				forwardAdjacencyConstraints.remove(pre);
				reverseAdjacencyConstraints.remove(secondSlot);
			}
		} else if (secondSlot == null) {
			if (forwardAdjacencyConstraints.containsKey(firstSlot)) {
				final IPortSlot post = forwardAdjacencyConstraints.get(firstSlot);
				forwardAdjacencyConstraints.remove(firstSlot);
				reverseAdjacencyConstraints.remove(post);
			}
		} else {
			forwardAdjacencyConstraints.put(firstSlot, secondSlot);
			reverseAdjacencyConstraints.put(secondSlot, firstSlot);
		}
	}

	@Override
	public void addBuilderExtension(final IBuilderExtension extension) {
		extensions.add(extension);
	}

	@Override
	public void setPortCost(@NonNull final IPort port, @NonNull final IVessel vessel, final PortType portType, final long cost) {
		portCostProvider.setPortCost(port, vessel, portType, cost);
	}

	private final Map<IDischargeOption, Map<IPort, ITimeWindow>> fobSalesToLoadPorts = new HashMap<>();
	private final Map<ILoadOption, Map<IPort, ITimeWindow>> desPurchasesToDischargePorts = new HashMap<>();

	/**
	 * A {@link Set} of {@link IPortSlot} which cannot be moved to another vessel/resource. This is populated be calls to {@link #freezeSlotToVessel(IPortSlot, IVessel)}.
	 */
	private final Set<IPortSlot> frozenSlots = new HashSet<>();

	@Override
	public void bindDischargeSlotsToDESPurchase(@NonNull final ILoadOption desPurchase, final Map<IPort, ITimeWindow> dischargePortToCompatibleWindow) {
		desPurchasesToDischargePorts.put(desPurchase, new HashMap<>(dischargePortToCompatibleWindow));

	}

	private void doBindDischargeSlotsToDESPurchase() {
		for (final Map.Entry<ILoadOption, Map<IPort, ITimeWindow>> e : desPurchasesToDischargePorts.entrySet()) {
			final ILoadOption desPurchase = e.getKey();
			final Map<IPort, ITimeWindow> dischargePorts = e.getValue();

			final ISequenceElement desElement = portSlotsProvider.getElement(desPurchase);
			assert desElement != null;

			// Look up virtual vessel
			final IVesselAvailability virtualVesselAvailability = virtualVesselAvailabilityMap.get(desElement);

			if (virtualVesselAvailability == null) {
				throw new IllegalArgumentException("DES Purchase is not linked to a virtual vesssel");
			}

			for (final IDischargeOption option : dischargeSlots) {

				// Skip frozen slots
				if (frozenSlots.contains(option)) {
					continue;
				}

				if (option instanceof DischargeSlot) {

					if (dischargePorts.keySet().contains(option.getPort())) {
						// Get current allocation
						final ITimeWindow desPurchaseWindowForPort = dischargePorts.get(option.getPort());

						final List<ITimeWindow> tw2 = timeWindowProvider.getTimeWindows(portSlotsProvider.getElement(option));
						if (matchingWindows(Collections.singletonList(desPurchaseWindowForPort), tw2) || matchingWindows(tw2, Collections.singletonList(desPurchaseWindowForPort))) {

							Set<IVesselAvailability> set = slotVesselAvailabilityRestrictions.get(option);
							if (set == null || set.isEmpty()) {
								set = new HashSet<IVesselAvailability>(realVesselAvailabilities);
							}
							set.add(virtualVesselAvailability);

							constrainSlotToVesselAvailabilities(option, set);
						}
					}
				}
			}
		}
	}

	/**
	 */
	@Override
	public void bindLoadSlotsToFOBSale(@NonNull final IDischargeOption fobSale, final Map<IPort, ITimeWindow> loadPorts) {
		fobSalesToLoadPorts.put(fobSale, new HashMap<>(loadPorts));
	}

	private void doBindLoadSlotsToFOBSale() {

		for (final Map.Entry<IDischargeOption, Map<IPort, ITimeWindow>> e : fobSalesToLoadPorts.entrySet()) {
			final IDischargeOption fobSale = e.getKey();
			final Map<IPort, ITimeWindow> loadPorts = e.getValue();

			final ISequenceElement fobElement = portSlotsProvider.getElement(fobSale);
			assert fobElement != null;

			// Look up virtual vessel
			final IVesselAvailability virtualVesselAvailability = virtualVesselAvailabilityMap.get(fobElement);
			if (virtualVesselAvailability == null) {
				throw new IllegalArgumentException("FOB Sale is not linked to a virtual vesssel");
			}

			for (final ILoadOption option : loadSlots) {

				// Skip frozen slots
				if (frozenSlots.contains(option)) {
					continue;
				}

				if (option instanceof LoadSlot) {

					if (loadPorts.keySet().contains(option.getPort())) {
						// Get current allocation
						final ITimeWindow fobSaleWindowForPort = loadPorts.get(option.getPort());

						final List<ITimeWindow> tw2 = timeWindowProvider.getTimeWindows(portSlotsProvider.getElement(option));
						if (matchingWindows(Collections.singletonList(fobSaleWindowForPort), tw2) || matchingWindows(tw2, Collections.singletonList(fobSaleWindowForPort))) {
							Set<IVesselAvailability> set = slotVesselAvailabilityRestrictions.get(option);
							if (set == null || set.isEmpty()) {
								set = new HashSet<IVesselAvailability>(realVesselAvailabilities);
							}
							set.add(virtualVesselAvailability);

							constrainSlotToVesselAvailabilities(option, set);
						}
					}
				}
			}
		}
	}

	/**
	 * Return true if the time windows overlap in some way.
	 * 
	 * @param tw1
	 * @param tw2
	 * @return
	 */
	private boolean matchingWindows(final List<ITimeWindow> tw1, final List<ITimeWindow> tw2) {

		for (final ITimeWindow t1 : tw1) {
			for (final ITimeWindow t2 : tw2) {
				// End is within
				if (t1.getEnd() >= t2.getStart() && t1.getEnd() <= t2.getEnd()) {
					return true;
				}
				// Start is within
				if (t1.getStart() >= t2.getStart() && t1.getStart() <= t2.getEnd()) {
					return true;
				}
			}

		}

		return false;
	}

	@Override
	public void createSlotGroupCount(final Collection<IPortSlot> slots, final int count) {
		final Collection<ISequenceElement> elements = new ArrayList<ISequenceElement>(slots.size());
		for (final IPortSlot slot : slots) {
			elements.add(portSlotsProvider.getElement(slot));
		}

		slotGroupCountProvider.createSlotGroup(elements, count);
	}

	private static final class ZeroCurve implements ICurve {
		protected ZeroCurve() {

		}

		@NonNull
		private static final ZeroCurve INSTANCE = new ZeroCurve();

		@Override
		public int getValueAtPoint(final int point) {
			return 0;
		}

		@NonNull
		public static ZeroCurve getInstance() {
			return INSTANCE;
		}
	}

	/**
	 */
	@Override
	public void setEarliestDate(@NonNull final DateTime earliestTime) {
		dateKeyProviderEditor.setTimeZero(earliestTime.getMillis());
	}

	/**
	 */
	@Override
	public void createCharterOutCurve(@NonNull final IVesselClass vesselClass, @NonNull final ICurve charterOutCurve, final int minDuration, final Set<IPort> ports) {
		charterMarketProviderEditor.addCharterOutOption(vesselClass, charterOutCurve, minDuration, ports);
	}

	@Override
	public void setGeneratedCharterOutStartTime(final int charterOutStartTime) {
		charterMarketProviderEditor.setCharterOutStartTime(charterOutStartTime);
	}

	/**
	 * @param slot
	 */
	@Override
	public void setSoftRequired(@NonNull final IPortSlot slot) {
		final ISequenceElement element = portSlotsProvider.getElement(slot);
		optionalElements.setSoftRequired(element, true);
	}

	/**
	 */
	@Override
	public void setPortCV(@NonNull final IPort port, final int cv) {
		portCVProvider.setPortCV(port, cv);
	}

	/**
	 */
	@Override
	public void setPortMinCV(@NonNull final IPort port, final int cv) {
		portCVRangeProvider.setPortMinCV(port, cv);
	}

	/**
	 */
	@Override
	public void setPortMaxCV(@NonNull final IPort port, final int cv) {
		portCVRangeProvider.setPortMaxCV(port, cv);
	}

	/**
	 */
	@Override
	@NonNull
	public IMarkToMarket createDESPurchaseMTM(@NonNull final Set<IPort> marketPorts, final int cargoCVValue, @NonNull final ILoadPriceCalculator priceCalculator, @NonNull final IEntity entity) {
		final MarkToMarket mtm = new MarkToMarket(priceCalculator, cargoCVValue, entity);

		for (final IPort port : marketPorts) {
			desPurchaseMTMPortMap.put(port, mtm);
		}
		return mtm;
	}

	/**
	 */
	@Override
	@NonNull
	public IMarkToMarket createFOBSaleMTM(@NonNull final Set<IPort> marketPorts, @NonNull final ISalesPriceCalculator priceCalculator, @NonNull final IEntity entity) {

		final MarkToMarket mtm = new MarkToMarket(priceCalculator, entity);

		for (final IPort port : marketPorts) {
			fobSaleMTMPortMap.put(port, mtm);
		}

		return mtm;
	}

	/**
	 */
	@Override
	@NonNull
	public IMarkToMarket createFOBPurchaseMTM(@NonNull final Set<IPort> marketPorts, final int cargoCVValue, @NonNull final ILoadPriceCalculator priceCalculator, @NonNull final IEntity entity) {
		final MarkToMarket mtm = new MarkToMarket(priceCalculator, cargoCVValue, entity);

		for (final IPort port : marketPorts) {
			fobPurchaseMTMPortMap.put(port, mtm);
		}
		return mtm;
	}

	/**
	 */
	@Override
	@NonNull
	public IMarkToMarket createDESSalesMTM(@NonNull final Set<IPort> marketPorts, @NonNull final ISalesPriceCalculator priceCalculator, @NonNull final IEntity entity) {
		final MarkToMarket mtm = new MarkToMarket(priceCalculator, entity);

		for (final IPort port : marketPorts) {
			desSaleMTMPortMap.put(port, mtm);
		}

		return mtm;
	}

	private void linkMarkToMarkets() {
		for (final ISequenceElement element : sequenceElements) {
			if (element != null) {
				final IPortSlot portSlot = portSlotsProvider.getPortSlot(element);
				final IPort port = portSlot.getPort();

				if (portSlot instanceof ILoadSlot) {
					final IMarkToMarket market = fobSaleMTMPortMap.get(port);
					if (market != null) {
						markToMarketProviderEditor.setMarkToMarketForElement(element, market);
					}
				} else if (portSlot instanceof ILoadOption) {
					final IMarkToMarket market = desSaleMTMPortMap.get(port);
					if (market != null) {
						markToMarketProviderEditor.setMarkToMarketForElement(element, market);
					}
				} else if (portSlot instanceof IDischargeSlot) {
					final IMarkToMarket market = desPurchaseMTMPortMap.get(port);
					if (market != null) {
						markToMarketProviderEditor.setMarkToMarketForElement(element, market);
					}
				} else if (portSlot instanceof IDischargeOption) {
					final IMarkToMarket market = fobPurchaseMTMPortMap.get(port);
					if (market != null) {
						markToMarketProviderEditor.setMarkToMarketForElement(element, market);
					}
				}
			}
		}
	}

	/**
	 */
	@Override
	public void setNominatedVessel(@NonNull final IPortSlot slot, @NonNull final IVessel vessel) {
		final ISequenceElement element = portSlotsProvider.getElement(slot);
		assert element != null;

		final IVesselAvailability resourceVessel = virtualVesselAvailabilityMap.get(element);
		assert resourceVessel != null;

		final IResource resource = vesselProvider.getResource(resourceVessel);
		assert resource != null;

		nominatedVesselProviderEditor.setNominatedVessel(element, resource, vessel);
	}

	/**
	 */
	@Override
	public void setShippingHoursRestriction(@NonNull final IPortSlot slot, @NonNull final ITimeWindow baseTime, final int hours) {
		final ISequenceElement element = portSlotsProvider.getElement(slot);
		assert element != null;
		shippingHoursRestrictionProviderEditor.setShippingHoursRestriction(element, baseTime, hours);
	}

	@Override
	public void setShippingDaysRestrictionReferenceSpeed(@NonNull final IVessel vessel, final VesselState vesselState, final int referenceSpeed) {
		shippingHoursRestrictionProviderEditor.setReferenceSpeed(vessel, vesselState, referenceSpeed);
	}

	@Override
	public void setDivertableDESAllowedRoute(@NonNull final IVesselClass vc, @NonNull final List<String> allowedRoutes) {
		for (final String route : allowedRoutes) {
			assert route != null;
			shippingHoursRestrictionProviderEditor.setDivertableDESAllowedRoute(vc, route);
		}
	}

	@Override
	public void freezeSlotToVesselAvailability(@NonNull final IPortSlot portSlot, @NonNull final IVesselAvailability vesselAvailability) {
		constrainSlotToVesselAvailabilities(portSlot, Collections.singleton(vesselAvailability));
		this.frozenSlots.add(portSlot);
	}

	@Override
	@NonNull
	public ISpotCharterInMarket createSpotCharterInMarket(@NonNull final String name, @NonNull final IVesselClass vesselClass, @NonNull final ICurve dailyCharterInRateCurve,
			final int availabilityCount) {
		return new DefaultSpotCharterInMarket(name, vesselClass, dailyCharterInRateCurve, availabilityCount);
	}

	@Override
	@NonNull
	public SequenceElement createSequenceElement(@NonNull final String name) {
		return new SequenceElement(indexingContext, name);
	}

}
