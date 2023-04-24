/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder.impl;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.CheckingIndexingContext;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.ILockedElementsProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.impl.OptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.ICharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterOutMarket;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.BaseFuel;
import com.mmxlabs.scheduler.optimiser.components.impl.Cargo;
import com.mmxlabs.scheduler.optimiser.components.impl.CharterLengthEvent;
import com.mmxlabs.scheduler.optimiser.components.impl.CharterLengthPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.CharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.impl.CharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.DefaultSpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.impl.DefaultVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.EndRequirementEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadOption;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.impl.Port;
import com.mmxlabs.scheduler.optimiser.components.impl.RoundTripCargoEnd;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.components.impl.SplitCharterOutVesselEventEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.SplitCharterOutVesselEventStartPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEvent;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.CharterRateToCharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICharterLengthElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICounterPartyVolumeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IFOBDESCompatibilityProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVRangeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortCooldownDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRoundTripVesselPermissionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteExclusionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IScheduledPurgeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IShortCargoReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ISlotGroupCountProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ISpotCharterInMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.shared.port.IDistanceMatrixProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProvider;

/**
 * Implementation of {@link ISchedulerBuilder}
 * 
 * @author Simon Goodall
 */
public class SchedulerBuilder implements ISchedulerBuilder {

	@Inject
	private Injector injector;

	@Inject
	private IBaseFuelProviderEditor baseFuelProvider;

	@NonNull
	private final List<IBuilderExtension> extensions = new LinkedList<>();

	@NonNull
	private final List<IResource> resources = new ArrayList<>();

	@NonNull
	private final List<ISequenceElement> sequenceElements = new ArrayList<>();

	@NonNull
	protected final List<IVessel> vessels = new LinkedList<>();

	@NonNull
	private final List<IVesselCharter> vesselCharters = new LinkedList<>();

	@NonNull
	private final List<IVesselCharter> realVesselCharters = new LinkedList<>();

	@NonNull
	private final List<ICargo> cargoes = new LinkedList<>();

	@NonNull
	private final List<ILoadOption> loadSlots = new LinkedList<>();

	@NonNull
	private final List<IDischargeOption> dischargeSlots = new LinkedList<>();

	/**
	 * Mutable windows which need the upper bound setting.
	 */
	@NonNull
	private final List<MutableTimeWindow> partiallyOpenEndDateWindows = new LinkedList<>();

	/**
	 * Mutable windows needing both bounds to be set.
	 */
	@NonNull
	private final List<MutableTimeWindow> fullyOpenEndDateWindows = new LinkedList<>();

	/**
	 * A "virtual" port which is zero distance from all other ports, to be used in
	 * cases where a vessel can be in any location. This can be replaced with a real
	 * location at a later date, after running an optimisation.
	 */
	protected IPort ANYWHERE;

	/**
	 * A field for tracking the time at which the last time window closes
	 */
	private final int endOfLatestWindow = 0;

	/**
	 * Tracks elements that are not shipped.
	 */
	@NonNull
	private final List<ISequenceElement> unshippedElements = new ArrayList<>();

	/*
	 * Constraint-tracking data structures; constraints created through the builder
	 * are applied at the very end, in case they affect things created after them.
	 */

	/**
	 * Tracks forward adjacency constraints; value must follow key. The reverse of
	 * {@link #reverseAdjacencyConstraints}
	 */
	@NonNull
	private final Map<IPortSlot, IPortSlot> forwardAdjacencyConstraints = new HashMap<>();

	/**
	 * Tracks forward adjacency constraints; key must follow value. The reverse of
	 * {@link #forwardAdjacencyConstraints}
	 */
	@NonNull
	private final Map<IPortSlot, IPortSlot> reverseAdjacencyConstraints = new HashMap<>();

	/**
	 * The slots for vessel events which have been generated; these are stored so
	 * that in {@link #buildVesselEvents()} they can have some extra post-processing
	 * done to set up any constraints
	 */
	@NonNull
	private final List<IVesselEventPortSlot> vesselEvents = new LinkedList<>();

	@NonNull
	private final IIndexingContext indexingContext = new CheckingIndexingContext();

	@Inject
	private IVesselProviderEditor vesselProvider;

	@Inject
	private IPortProvider portProvider;

	@Inject
	private IElementPortProviderEditor elementPortProvider;

	@Inject
	private IPortSlotProviderEditor portSlotsProvider;

	@Inject
	private IDistanceMatrixProvider portDistanceProvider;

	@Inject
	private IOrderedSequenceElementsDataComponentProviderEditor orderedSequenceElementsEditor;

	@Inject
	private IPortTypeProviderEditor portTypeProvider;

	@Inject
	private IElementDurationProviderEditor elementDurationsProvider;

	@Inject
	private IResourceAllocationConstraintDataComponentProviderEditor resourceAllocationProvider;

	@Inject
	private IStartEndRequirementProviderEditor startEndRequirementProvider;

	@Inject
	private IPortExclusionProviderEditor portExclusionProvider;

	@Inject
	private IRouteExclusionProviderEditor routeExclusionProvider;

	@Inject
	private IReturnElementProviderEditor returnElementProvider;

	@Inject
	private IRouteCostProviderEditor routeCostProvider;

	@Inject
	private IPortCostProviderEditor portCostProvider;

	@Inject
	private IPortCVProviderEditor portCVProvider;

	@Inject
	private IPortCVRangeProviderEditor portCVRangeProvider;

	/**
	 * Keeps track of calculators
	 */
	@Inject
	private ICalculatorProviderEditor calculatorProvider;

	@Inject
	private IPortCooldownDataProviderEditor cooldownDataProvider;

	@Inject
	private IOptionalElementsProviderEditor optionalElements;

	@Inject
	private ILockedElementsProviderEditor lockedElements;

	@Inject
	private ISpotMarketSlotsProviderEditor spotMarketSlots;

	@Inject
	private ISpotCharterInMarketProviderEditor spotCharterInMarketProviderEditor;

	@Inject
	private ISlotGroupCountProviderEditor slotGroupCountProvider;

	@Inject
	private IVirtualVesselSlotProviderEditor virtualVesselSlotProviderEditor;

	@Inject
	private IDateKeyProviderEditor dateKeyProviderEditor;

	@Inject
	private ICharterMarketProviderEditor charterMarketProviderEditor;

	@Inject
	private IShortCargoReturnElementProviderEditor shortCargoReturnElementProviderEditor;

	@Inject
	private IMarkToMarketProviderEditor markToMarketProviderEditor;

	@Inject
	private INominatedVesselProviderEditor nominatedVesselProviderEditor;

	@Inject
	private IShippingHoursRestrictionProviderEditor shippingHoursRestrictionProviderEditor;

	@Inject
	private IRoundTripVesselPermissionProviderEditor roundTripVesselPermissionProviderEditor;

	@Inject
	private IAllowedVesselProviderEditor allowedVesselProviderEditor;

	@Inject
	private IFOBDESCompatibilityProviderEditor fobdesCompatibilityProviderEditor;

	@Inject
	private IScheduledPurgeProviderEditor scheduledPurgeProvider;

	@Inject
	private ICounterPartyVolumeProviderEditor counterPartyVolumeProvider;

	@NonNull
	private final Map<IPort, MarkToMarket> desPurchaseMTMPortMap = new HashMap<>();

	@NonNull
	private final Map<IPort, MarkToMarket> desSaleMTMPortMap = new HashMap<>();

	@NonNull
	private final Map<IPort, MarkToMarket> fobSaleMTMPortMap = new HashMap<>();

	@NonNull
	private final Map<IPort, MarkToMarket> fobPurchaseMTMPortMap = new HashMap<>();

	@Inject
	private IPromptPeriodProviderEditor promptPeriodProviderEditor;

	@Inject
	private ICharterRateCalculator charterRateCalculator;

	@Inject
	private ICharterLengthElementProviderEditor charterLengthElementProviderEditor;

	/**
	 * Constant used during end date of scenario calculations -
	 * {@link #minDaysFromLastEventToEnd} days extra after last date. See code in
	 * {@link SchedulerBuilder#getOptimisationData()}
	 * 
	 */
	public static int minDaysFromLastEventToEnd = 10;

	/**
	 * Fake vessel for virtual elements.
	 */
	private IVessel virtualVessel;

	/**
	 * Map between a virtual sequence element and the virtual {@link IVesselEvent}
	 * instance representing it.
	 */

	private final @NonNull Map<ISequenceElement, IVesselCharter> virtualVesselCharterMap = new HashMap<>();

	public SchedulerBuilder() {
		indexingContext.registerType(SequenceElement.class);
		indexingContext.registerType(Resource.class);
		indexingContext.registerType(IBaseFuel.class);
		// Manual "hack" to get index 0 for the LNG base fuel before other versions are
		// registered
		final int lngIndex = indexingContext.assignIndex(IBaseFuel.LNG);
		assert lngIndex == IBaseFuel.LNG.getIndex();
	}

	// @Inject to trigger call after constructor
	@Inject
	public void init() {
		// Create the anywhere port
		ANYWHERE = portProvider.getAnywherePort();

		// setup fake vessels for virtual elements.
		virtualVessel = createVessel("virtual", 0, 0, Long.MAX_VALUE, 0, createBaseFuel("fakeFuel", 0), createBaseFuel("fakeIdleFuel", 0), createBaseFuel("fakeInPortFuel", 0),
				createBaseFuel("fakePilotLightFuel", 0), 0, 0, 0, 0, 0, false);
	}

	public IVessel getVirtualVessel() {
		return virtualVessel;
	}

	public IVessel createVirtualMarkerVessel(final String name, final long capacity) {
		return createVessel(name, 0, 0, capacity, 0, virtualVessel.getTravelBaseFuel(), virtualVessel.getIdleBaseFuel(), virtualVessel.getInPortBaseFuel(), virtualVessel.getPilotLightBaseFuel(), 0, 0,
				0, 0, 0, false);
	}

	/**
	 */
	@Override
	@NonNull
	public ILoadSlot createLoadSlot(final @NonNull String id, final @NonNull IPort port, final ITimeWindow window, final long minVolumeInM3, final long maxVolumeInM3,
			final ILoadPriceCalculator loadContract, final int cargoCVValue, final int durationHours, final boolean cooldownSet, final boolean cooldownForbidden, final boolean purgeScheduled,
			final int pricingDate, final PricingEventType pricingEvent, final boolean optional, final boolean locked, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3,
			final boolean cancelled) {

		final boolean fooLocked = locked || cancelled;

		final LoadSlot slot = new LoadSlot(id, port, window, isVolumeLimitInM3, minVolumeInM3, maxVolumeInM3, loadContract, cargoCVValue, cooldownSet, cooldownForbidden);

		final ISequenceElement element = configureLoadOption(slot, minVolumeInM3, maxVolumeInM3, loadContract, cargoCVValue, pricingDate, pricingEvent, optional, fooLocked, isSpotMarketSlot,
				isVolumeLimitInM3);

		elementDurationsProvider.setElementDuration(element, durationHours);
		if (purgeScheduled) {
			scheduledPurgeProvider.setPurgeScheduled(element, slot);
		}
		return slot;
	}

	/**
	 */
	@Override
	@NonNull
	public ILoadOption createDESPurchaseLoadSlot(final String id, @Nullable IPort port, final ITimeWindow window, final long minVolume, final long maxVolume,
			final ILoadPriceCalculator priceCalculator, final int cargoCVValue, final int durationHours, final int pricingDate, final PricingEventType pricingEvent, final boolean slotIsOptional,
			final boolean locked, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3, final boolean cancelled) {

		if (port == null) {
			port = ANYWHERE;
		}

		final boolean fooLocked = locked || cancelled;

		final LoadOption slot = new LoadOption(id, port, window, isVolumeLimitInM3, minVolume, maxVolume, priceCalculator, cargoCVValue);
		final ISequenceElement element = configureLoadOption(slot, minVolume, maxVolume, priceCalculator, cargoCVValue, pricingDate, pricingEvent, slotIsOptional, fooLocked, isSpotMarketSlot,
				isVolumeLimitInM3);

		elementDurationsProvider.setElementDuration(element, durationHours);

		unshippedElements.add(element);

		createVirtualVesselCharter(element, VesselInstanceType.DES_PURCHASE);

		return slot;
	}

	@NonNull
	private ISequenceElement configureLoadOption(@NonNull final LoadOption slot, final long minVolume, final long maxVolume, final ILoadPriceCalculator priceCalculator, final int cargoCVValue,
			final int pricingDate, final PricingEventType pricingEvent, final boolean optional, final boolean locked, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {

		slot.setVolumeLimits(isVolumeLimitInM3, minVolume, maxVolume == 0 ? Long.MAX_VALUE : maxVolume);

		// slot.setPurchasePriceCurve(pricePerMMBTu);
		slot.setLoadPriceCalculator(priceCalculator);
		slot.setCargoCVValue(cargoCVValue);
		slot.setPricingDate(pricingDate);
		slot.setPricingEvent(pricingEvent);

		loadSlots.add(slot);

		// Create a sequence element against this load slot
		final SequenceElement element = new SequenceElement(indexingContext, slot.getId() + "-" + slot.getPort().getName());

		optionalElements.setOptional(element, optional);

		lockedElements.setLocked(element, locked);

		sequenceElements.add(element);

		// Register the port with the element
		elementPortProvider.setPortForElement(slot.getPort(), element);

		portTypeProvider.setPortType(element, PortType.Load);

		portSlotsProvider.setPortSlot(element, slot);

		calculatorProvider.addLoadPriceCalculator(priceCalculator);

		return element;
	}

	/**
	 */
	@Override
	@NonNull
	public IDischargeOption createFOBSaleDischargeSlot(final String id, @Nullable IPort port, final ITimeWindow window, final long minVolume, final long maxVolume, final long minCvValue,
			final long maxCvValue, final ISalesPriceCalculator priceCalculator, final int durationHours, final int pricingDate, final PricingEventType pricingEvent, final boolean slotIsOptional,
			final boolean slotIsLocked, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3, final boolean slotIsCancelled) {

		if (port == null) {
			port = ANYWHERE;
		}
		assert port != null;
		final boolean locked = slotIsLocked || slotIsCancelled;

		final DischargeOption slot = new DischargeOption(id, port, window, isVolumeLimitInM3, minVolume, maxVolume, minCvValue, maxCvValue, priceCalculator);
		slot.setPricingDate(pricingDate);

		final ISequenceElement element = configureDischargeOption(slot, minVolume, maxVolume, minCvValue, maxCvValue, priceCalculator, pricingDate, pricingEvent, slotIsOptional, locked,
				isSpotMarketSlot, isVolumeLimitInM3);

		elementDurationsProvider.setElementDuration(element, durationHours);

		unshippedElements.add(element);

		createVirtualVesselCharter(element, VesselInstanceType.FOB_SALE);

		return slot;
	}

	@NonNull
	private ISequenceElement configureDischargeOption(@NonNull final DischargeOption slot,
			// final String id, final IPort port, final ITimeWindow window,
			final long minVolume, final long maxVolume, final long minCvValue, final long maxCvValue, final ISalesPriceCalculator priceCalculator, final int pricingDate,
			final PricingEventType pricingEvent, final boolean optional, final boolean locked, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {
		slot.setVolumeLimits(isVolumeLimitInM3, minVolume, maxVolume == 0 ? Long.MAX_VALUE : maxVolume);
		slot.setMinCvValue(minCvValue);
		slot.setMaxCvValue(maxCvValue);
		slot.setDischargePriceCalculator(priceCalculator);
		slot.setPricingDate(pricingDate);
		slot.setPricingEvent(pricingEvent);

		dischargeSlots.add(slot);

		// Create a sequence element against this discharge slot
		final SequenceElement element = new SequenceElement(indexingContext, slot.getId() + "-" + slot.getPort().getName());

		optionalElements.setOptional(element, optional);

		lockedElements.setLocked(element, locked);

		sequenceElements.add(element);

		// Register the port with the element
		elementPortProvider.setPortForElement(slot.getPort(), element);

		portSlotsProvider.setPortSlot(element, slot);

		portTypeProvider.setPortType(element, PortType.Discharge);

		calculatorProvider.addSalesPriceCalculator(priceCalculator);

		return element;
	}

	/**
	 */
	@Override
	@NonNull
	public IDischargeSlot createDischargeSlot(final String id, @NonNull final IPort port, final ITimeWindow window, final long minVolumeInM3, final long maxVolumeInM3, final long minCvValue,
			final long maxCvValue, final ISalesPriceCalculator pricePerMMBTu, final int durationHours, final int pricingDate, final PricingEventType pricingEvent, final boolean optional,
			final boolean isLockedSlot, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3, final boolean isCancelledSlot) {

		final DischargeSlot slot = new DischargeSlot(id, port, window, isVolumeLimitInM3, minVolumeInM3, maxVolumeInM3, pricePerMMBTu, minCvValue, maxCvValue);
		slot.setPricingDate(pricingDate);
		final boolean locked = isLockedSlot || isCancelledSlot;

		final ISequenceElement element = configureDischargeOption(slot, minVolumeInM3, maxVolumeInM3, minCvValue, maxCvValue, pricePerMMBTu, pricingDate, pricingEvent, optional, locked,
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
	public ICargo createCargo(final @NonNull Collection<@NonNull IPortSlot> slots, final boolean allowRewiring) {

		final Cargo cargo = new Cargo(new ArrayList<>(slots));
		cargoes.add(cargo);

		// Fix slot pairing if we disallow re-wiring or this is a complex cargo (more
		// than just one load and one discharge)
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

	/**
	 * Create the return elements for each port/resource combination
	 */
	private void createReturnElements() {
		for (final IResource resource : resources) {
			assert resource != null;
			final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
			assert endRequirement != null;
			for (final IPort port : portProvider.getAllPorts()) {
				assert port != null;
				returnElementProvider.setReturnElement(resource, port, createReturnElement(resource, port, endRequirement));
			}
		}
	}

	@NonNull
	private ISequenceElement createReturnElement(@NonNull final IResource resource, @NonNull final IPort port, @NonNull final IEndRequirement endRequirement) {
		final String name = "return-to-" + port.getName();
		final IEndPortSlot endPortSlot = new EndRequirementEndPortSlot(name, port, endRequirement);
		final SequenceElement element = new SequenceElement(indexingContext, "return-to-" + port.getName());

		elementDurationsProvider.setElementDuration(element, 0);

		elementPortProvider.setPortForElement(port, element);
		portSlotsProvider.setPortSlot(element, endPortSlot);
		// Return elements are always end elements?
		portTypeProvider.setPortType(element, PortType.End);

		if (endRequirement.hasTimeRequirement()) {
			// We should set the time window for all end elements for this
			// resource to match the end requirement for the resource
			// endPortSlot.setTimeWindow(startEndRequirementProvider.getEndRequirement(resource).getTimeWindow());
		} else {
			if (vesselProvider.getVesselCharter(resource).getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER)) {
				// spot charters have no end time window, because their end date
				// is very flexible.
				// spotVesselEndWindows.add((MutableTimeWindow)endRequirement.getTimeWindow());
			} else if (vesselProvider.getVesselCharter(resource).getVesselInstanceType().equals(VesselInstanceType.ROUND_TRIP)) {
				// spot charters have no end time window, because their end date
				// is very flexible.
			} else {
				// this defers setting the time windows to
				// getOptimisationData(), which will
				// choose a suitable end date for the optimisation and set all
				// the elements in
				// this list to have a time window around that end date
				// endSlotWindows.add((MutableTimeWindow) endPortSlot.getTimeWindow());
			}
		}
		return element;
	}

	private void createCharterLengthLocationElements() {
		for (IVesselEventPortSlot vesselEventSlot : vesselEvents) {
			if (vesselEventSlot instanceof CharterLengthPortSlot charterLengthSlot) {
				for (IPort port : portProvider.getAllPorts()) {
					assert port != null;
					charterLengthElementProviderEditor.setCharterLengthLocationElement(charterLengthSlot.getVesselEvent(), port,
							createCharterLengthLocationElement(charterLengthSlot.getId(), charterLengthSlot, port));
				}
			}
		}
	}

	private ISequenceElement createCharterLengthLocationElement(final String eventName, final CharterLengthPortSlot portSlot, final IPort port) {
		final String name = String.format("charter-length-%s-at-%s", eventName, port.getName());
		final SequenceElement element = new SequenceElement(indexingContext, name);
		final CharterLengthEvent event = (CharterLengthEvent) portSlot.getVesselEvent();
		final CharterLengthPortSlot charterLengthPortSlot = new CharterLengthPortSlot(name, event.getTimeWindow(), port, event);
		charterLengthPortSlot.setEventSequenceElements(portSlot.getEventSequenceElements());
		elementDurationsProvider.setElementDuration(element, event.getDurationHours());
		elementPortProvider.setPortForElement(port, element);
		portSlotsProvider.setPortSlot(element, charterLengthPortSlot);
		portTypeProvider.setPortType(element, PortType.CharterLength);
		charterLengthElementProviderEditor.setOriginalCharterLengthPortSlot(portSlot, charterLengthPortSlot);
		return element;
	}

	/**
	 * Method to set common properties etc for {@link Port} implementations.
	 * 
	 * @param port
	 * @param arriveCold
	 * @param cooldownCalculator
	 */
	@Override
	public void setPortCooldownData(@NonNull final IPort port, final boolean arriveCold, final ICooldownCalculator cooldownCalculator) {

		cooldownDataProvider.setShouldVesselsArriveCold(port, arriveCold);
		cooldownDataProvider.setCooldownCalculator(port, cooldownCalculator);
		calculatorProvider.addCooldownCalculator(cooldownCalculator);
	}

	@Override
	public void addPartiallyOpenEndWindow(final MutableTimeWindow window) {
		partiallyOpenEndDateWindows.add(window);
	}

	@Override
	public void addOpenEndWindow(final MutableTimeWindow window) {
		fullyOpenEndDateWindows.add(window);
	}

	/**
	 * Create several spot vessels (see also {@code createSpotVessel}), named like
	 * namePrefix-1, namePrefix-2, etc
	 * 
	 * @param namePrefix
	 * @param spotCharterInMarket
	 * @param count
	 * @return
	 */
	@Override
	public List<IVesselCharter> createSpotVessels(final String namePrefix, @NonNull final ISpotCharterInMarket spotCharterInMarket) {
		final List<IVesselCharter> answer = new ArrayList<>(spotCharterInMarket.getAvailabilityCount());
		for (int i = 0; i < spotCharterInMarket.getAvailabilityCount(); i++) {
			answer.add(createSpotVessel(namePrefix + "-" + (i + 1), i, spotCharterInMarket));
		}
		return answer;
	}

	/**
	 * Create a spot charter vessel with no fixed start/end requirements and vessel
	 * instance type SPOT_CHARTER
	 * 
	 * @param name
	 * @param spotCharterInMarket
	 * @return the spot vessel
	 */
	@Override
	@NonNull
	public IVesselCharter createSpotVessel(final String name, final int spotIndex, @NonNull final ISpotCharterInMarket spotCharterInMarket) {
		final IVessel vessel = spotCharterInMarket.getVessel();
		final IStartRequirement start;
		if (spotCharterInMarket.getStartRequirement() != null) {
			start = spotCharterInMarket.getStartRequirement();
		} else {
			start = createStartRequirement(ANYWHERE, false, null, createHeelSupplier(vessel.getSafetyHeel(), vessel.getSafetyHeel(), 0, new ConstantHeelPriceCalculator(0)));
		}
		final IEndRequirement end;
		if (spotCharterInMarket.getEndRequirement() != null) {
			end = spotCharterInMarket.getEndRequirement();
		} else {
			end = createEndRequirement(Collections.singletonList(ANYWHERE), false, new TimeWindow(0, Integer.MAX_VALUE),
					createHeelConsumer(vessel.getSafetyHeel(), vessel.getSafetyHeel(), VesselTankState.MUST_BE_COLD, new ConstantHeelPriceCalculator(0), false));
		}
		final ILongCurve dailyCharterInPrice = spotCharterInMarket.getDailyCharterInRateCurve();

		// End cold already enforced in VoyagePlanner#getVoyageOptionsAndSetVpoChoices
		final IVesselCharter spotAvailability = createVesselCharter(vessel, dailyCharterInPrice, VesselInstanceType.SPOT_CHARTER, start, end, spotCharterInMarket,
				spotCharterInMarket.getCharterContract(), spotIndex, spotCharterInMarket.getRepositioningFee(), true);
		spotCharterInMarketProviderEditor.addSpotMarketAvailability(spotAvailability, spotCharterInMarket, spotIndex);

		return spotAvailability;
	}

	@Override
	public @NonNull IHeelOptionSupplier createHeelSupplier(final long minHeelInM3, final long maxHeelInM3, final int heelCVValue, @NonNull final IHeelPriceCalculator heelPriceCalculator) {
		return new HeelOptionSupplier(minHeelInM3, maxHeelInM3, heelCVValue, heelPriceCalculator);
	}

	@Override
	public @NonNull IHeelOptionConsumer createHeelConsumer(final long minHeelInM3, final long maxHeelInM3, @NonNull final VesselTankState tankState,
			@NonNull final IHeelPriceCalculator heelPriceCalculator, final boolean useLastHeelPrice) {
		return new HeelOptionConsumer(minHeelInM3, maxHeelInM3, tankState, heelPriceCalculator, useLastHeelPrice);
	}

	@Override
	@NonNull
	public IVesselCharter createVesselCharter(@NonNull final IVessel vessel, final ILongCurve dailyCharterInRate, final VesselInstanceType vesselInstanceType, final IStartRequirement start,
			final IEndRequirement end, final ICharterContract charterContract, final ILongCurve repositioningFee, final boolean isOptional) {
		return createVesselCharter(vessel, dailyCharterInRate, vesselInstanceType, start, end, null, charterContract, -1, repositioningFee, isOptional);
	}

	private IVesselCharter createVesselCharter(@NonNull final IVessel vessel, final String name, final ILongCurve dailyCharterInRate, final VesselInstanceType vesselInstanceType,
			final IStartRequirement start, final IEndRequirement end, final ICharterContract charterContract, final ILongCurve repositioningFee, final boolean isOptional) {
		return createVesselCharter(vessel, name, dailyCharterInRate, vesselInstanceType, start, end, null, charterContract, -1, repositioningFee, isOptional);
	}

	@NonNull
	private IVesselCharter createVesselCharter(@NonNull final IVessel vessel, final ILongCurve dailyCharterInRate, @NonNull final VesselInstanceType vesselInstanceType,
			@NonNull final IStartRequirement start, @NonNull final IEndRequirement end, @Nullable final ISpotCharterInMarket spotCharterInMarket, final ICharterContract charterContract,
			final int spotIndex, final ILongCurve positioningFee, final boolean isOptional) {
		return createVesselCharter(vessel, vessel.getName(), dailyCharterInRate, vesselInstanceType, start, end, spotCharterInMarket, charterContract, spotIndex, positioningFee, isOptional);
	}

	private IVesselCharter createVesselCharter(@NonNull final IVessel vessel, final String name, final ILongCurve dailyCharterInRate, @NonNull final VesselInstanceType vesselInstanceType,
			@NonNull final IStartRequirement start, @NonNull final IEndRequirement end, @Nullable final ISpotCharterInMarket spotCharterInMarket, final ICharterContract charterContract,
			final int spotIndex, final ILongCurve positioningFee, final boolean isOptional) {
		if (!vessels.contains(vessel)) {
			throw new IllegalArgumentException("IVessel was not created using this builder");
		}

		final DefaultVesselCharter vesselCharter = new DefaultVesselCharter(vessel, vesselInstanceType);

		vesselCharter.setDailyCharterInRate(dailyCharterInRate);

		final ICharterCostCalculator charterCostCalculator = injector.getInstance(ICharterCostCalculator.class);

		charterCostCalculator.setCharterRateCurve(dailyCharterInRate);
		if (charterCostCalculator instanceof CharterRateToCharterCostCalculator) {
			final CharterRateToCharterCostCalculator charterRateToCharterCostCalculator = (CharterRateToCharterCostCalculator) charterCostCalculator;
			charterRateToCharterCostCalculator.initialise(vesselCharter, this.charterRateCalculator);
		}
		vesselCharter.setCharterCostCalculator(charterCostCalculator);

		vesselCharter.setStartRequirement(start);
		vesselCharter.setEndRequirement(end);

		final IResource resource = new Resource(indexingContext, name);

		// Register with provider
		vesselProvider.setVesselCharterResource(resource, vesselCharter);
		vesselProvider.setResourceOptional(resource, vesselCharter.isOptional());
		vesselCharters.add(vesselCharter);
		if (vesselInstanceType != VesselInstanceType.DES_PURCHASE && vesselInstanceType != VesselInstanceType.FOB_SALE) {
			realVesselCharters.add(vesselCharter);
		}

		resources.add(resource);

		// If no time requirement is specified then the time window is at the
		// very start of the job

		final ITimeWindow startWindow = start.getTimeWindow();
		// assert startWindow != null;

		final IPort startPort = (start.hasPortRequirement() && start.getLocation() != null) ? start.getLocation() : ANYWHERE;
		assert startPort != null;

		final StartPortSlot startSlot = new StartPortSlot("start-" + name, startPort, startWindow, start.getHeelOptions());

		final SequenceElement startElement = new SequenceElement(indexingContext, startSlot.getId());

		final IEndPortSlot endSlot = new EndRequirementEndPortSlot("end-" + name, //
				(end.hasPortRequirement() && end.getLocation() != null) ? end.getLocation() : ANYWHERE, //
				end);

		// Create start/end sequence elements for this route
		final SequenceElement endElement = new SequenceElement(indexingContext, endSlot.getId());

		sequenceElements.add(startElement);
		sequenceElements.add(endElement);
		elementDurationsProvider.setElementDuration(endElement, 0);
		elementDurationsProvider.setElementDuration(startElement, 0);

		if (end.hasTimeRequirement() == false) {
			// put end slot into list of slots to patch up later.
			// Fleet vessels and spot vessels both run to the end of the optimisation if
			// they don't have an end date.
			if (!vesselInstanceType.equals(VesselInstanceType.SPOT_CHARTER) && !vesselInstanceType.equals(VesselInstanceType.ROUND_TRIP)) {
				if (end.isMinDurationSet() || end.isMaxDurationSet()) {
					partiallyOpenEndDateWindows.add((MutableTimeWindow) end.getTimeWindow());
				} else {
					fullyOpenEndDateWindows.add((MutableTimeWindow) end.getTimeWindow());
				}
			}
		}

		startElement.setName(startSlot.getId() + "-" + startSlot.getPort().getName());
		endElement.setName(endSlot.getId() + "-" + endSlot.getPort().getName());

		elementPortProvider.setPortForElement(startSlot.getPort(), startElement);
		elementPortProvider.setPortForElement(endSlot.getPort(), endElement);

		portTypeProvider.setPortType(startElement, PortType.Start);
		portTypeProvider.setPortType(endElement, PortType.End);

		portSlotsProvider.setPortSlot(startElement, startSlot);
		portSlotsProvider.setPortSlot(endElement, endSlot);

		resourceAllocationProvider.setAllowedResources(startElement, Collections.singleton(resource));
		// BugzID: 576 allow end element on any vessel, to prevent
		// ResourceAllocationConstraint from disallowing 2opt2s at end

		// resourceAllocationProvider.setAllowedResources(endElement,
		// Collections.singleton(resource));

		startEndRequirementProvider.setStartEndRequirements(resource, start, end);
		startEndRequirementProvider.setStartEndElements(resource, startElement, endElement);

		if (spotCharterInMarket != null) {
			vesselCharter.setSpotCharterInMarket(spotCharterInMarket);
			vesselCharter.setSpotIndex(spotIndex);
		}

		vesselCharter.setOptional(isOptional);
		vesselCharter.setRepositioningFee(positioningFee);

		vesselCharter.setCharterContract(charterContract);
		return vesselCharter;
	}

	@NonNull
	public IStartRequirement createStartRequirement() {
		return createStartRequirement(ANYWHERE, false, null, null);
	}

	@Override
	@NonNull
	public IStartRequirement createStartRequirement(@Nullable final IPort fixedPort, final boolean hasTimeRequirement, final @Nullable ITimeWindow timeWindow,
			@Nullable final IHeelOptionSupplier heelOptions) {

		return new StartRequirement(fixedPort == null ? ANYWHERE : fixedPort, fixedPort != null, hasTimeRequirement, timeWindow, heelOptions);
	}

	@NonNull
	public IEndRequirement createEndRequirement() {
		return createEndRequirement(Collections.singletonList(ANYWHERE), false, new MutableTimeWindow(0, Integer.MAX_VALUE),
				createHeelConsumer(0, 0, VesselTankState.MUST_BE_WARM, new ConstantHeelPriceCalculator(0), false));
	}

	@Override
	public @NonNull IEndRequirement createEndRequirement(@Nullable final Collection<IPort> portSet, final boolean hasTimeRequirement, @NonNull final ITimeWindow timeWindow,
			final IHeelOptionConsumer heelConsumer) {

		if (portSet == null || portSet.isEmpty()) {
			return new EndRequirement(Collections.singleton(ANYWHERE), false, hasTimeRequirement, timeWindow, heelConsumer);
		} else {
			return new EndRequirement(portSet, true, hasTimeRequirement, timeWindow, heelConsumer);
		}
	}

	/**
	 */
	@Override
	public void setVesselRouteCost(final @NonNull ERouteOption route, @NonNull final IVessel vessel, final IRouteCostProvider.@NonNull CostType costType, final @NonNull ILongCurve tollPrice) {
		routeCostProvider.setRouteCost(route, vessel, costType, tollPrice);
	}

	/**
	 */
	@Override
	public void setDefaultRouteCost(final ERouteOption route, final @NonNull ILongCurve defaultPrice) {
		routeCostProvider.setDefaultRouteCost(route, defaultPrice);
	}

	@Override
	public void setVesselRouteFuel(final @NonNull ERouteOption route, @NonNull final IVessel vessel, final VesselState vesselState, final long baseFuelInScaledMT, final long nboRateInScaledM3) {
		routeCostProvider.setRouteFuel(route, vessel, vesselState, baseFuelInScaledMT, nboRateInScaledM3);
	}

	@Override
	public void setVesselRouteTransitTime(final @NonNull ERouteOption route, final @NonNull IVessel vessel, final int transitTimeInHours) {
		routeCostProvider.setRouteTransitTime(route, vessel, transitTimeInHours);
	}

	@Override
	public void setElementDurations(@NonNull final ISequenceElement element, @NonNull final IResource resource, final int duration) {
		elementDurationsProvider.setElementDuration(element, resource, duration);
	}

	@Override
	public IOptimisationData getOptimisationData() {

		// create return elements before fixing time windows,
		// because the next bit will have to patch up their time windows
		createReturnElements();

		createRoundtripCargoReturnElements();
		
		createCharterLengthLocationElements();

		// Patch up end time windows
		// The return time should be the soonest we can get back to the previous load,
		// presumably in the slowest vessel without going via a canal.

		// TODO what about return to first load rule?

		int latestDischarge = 0;
		IPort loadPort = null;
		IPort dischargePort = null;
		for (final ICargo cargo : cargoes) {
			final List<IPortSlot> portSlots = cargo.getPortSlots();
			if (portSlots.size() > 1) {
				final IPortSlot end = portSlots.get(portSlots.size() - 1);
				final IPortSlot endMinus1 = portSlots.get(portSlots.size() - 2);
				@Nullable
				final ITimeWindow timeWindow = end.getTimeWindow();
				if (timeWindow != null) {
					final int endOfDischargeWindow = timeWindow.getExclusiveEnd();
					if (endOfDischargeWindow != Integer.MAX_VALUE) {
						if (endOfDischargeWindow > latestDischarge) {
							latestDischarge = endOfDischargeWindow;
							loadPort = endMinus1.getPort();
							dischargePort = end.getPort();
						}
					}
				}
			}
		}

		final int windowAdder;
		final int latestTime;
		if (promptPeriodProviderEditor.getEndOfSchedulingPeriod() == Integer.MAX_VALUE) {

			// 0 == return to current load,
			// 1 == return to farthest in time load
			// 2== end window
			// 3 == discharge + 60
			// 4 == discharge (minus spot) + 60
			final int rule = 4;

			if (rule == 0) {
				/**
				 * The shortest time which the slowest vessel in the fleet can take to get from
				 * the latest discharge back to the load for that discharge.
				 */
				int maxFastReturnTime = 0;
				if ((dischargePort != null) && (loadPort != null)) {
					final int returnDistance = portDistanceProvider.getMaximumValue(dischargePort, loadPort);
					// what's the slowest vessel
					int slowestMaxSpeed = Integer.MAX_VALUE;
					for (final IVessel vessel : vessels) {
						if (vessel == virtualVessel) {
							continue;
						}
						slowestMaxSpeed = Math.min(slowestMaxSpeed, vessel.getMaxSpeed());
					}
					maxFastReturnTime = Math.max(maxFastReturnTime, Calculator.getTimeFromSpeedDistance(slowestMaxSpeed, returnDistance));
				} else {
					latestDischarge = 0;
					maxFastReturnTime = 0;
				}
				latestTime = Math.max(endOfLatestWindow + (24 * minDaysFromLastEventToEnd), maxFastReturnTime + latestDischarge);
			} else if (rule == 1) {
				/**
				 * The shortest time which the slowest vessel in the fleet can take to get from
				 * the latest discharge back to the load for that discharge.
				 */
				int maxFastReturnTime = 0;
				if ((dischargePort != null)) {
					for (final ILoadOption loadSlot : loadSlots) {
						final int returnDistance = portDistanceProvider.getMaximumValue(dischargePort, loadSlot.getPort());
						if (returnDistance == Integer.MAX_VALUE) {
							continue;
						}
						// what's the slowest vessel
						int slowestMaxSpeed = Integer.MAX_VALUE;
						for (final IVessel vessel : vessels) {
							if (vessel == virtualVessel) {
								continue;
							}
							slowestMaxSpeed = Math.min(slowestMaxSpeed, vessel.getMaxSpeed());
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
			} else if (rule == 3) {
				latestTime = Math.max(endOfLatestWindow, latestDischarge) + 60 * 24;
			} else if (rule == 4) {

				// Include all time windows *except* spot market slots (These need to be
				// registered externally)
				final OptionalInt optionalMax = sequenceElements.stream() //
						.filter(element -> !spotMarketSlots.isSpotMarketSlot(element)) //
						.map(element -> portSlotsProvider.getPortSlot(element)) //
						.filter(portSlot -> portSlot.getTimeWindow() != null) //
						.filter(portSlot -> portSlot.getTimeWindow().getExclusiveEnd() != Integer.MAX_VALUE) //
						.mapToInt(portSlot -> portSlot.getTimeWindow().getExclusiveEnd()) //
						.max();
				final int lastFoundTime = optionalMax.isPresent() ? optionalMax.getAsInt() : 0;

				latestTime = Math.max(lastFoundTime, latestDischarge) + 60 * 24;
			} else {
				// Invalid rule
				assert false;
			}
			windowAdder = (rule == 3 || rule == 4) ? 0 : 35 * 24;
			promptPeriodProviderEditor.setEndOfSchedulingPeriod(latestTime);
		} else {
			windowAdder = 0;
			latestTime = promptPeriodProviderEditor.getEndOfSchedulingPeriod() + 1;
		}
		startEndRequirementProvider.setNotionalEndTime(latestTime);

		for (final MutableTimeWindow window : fullyOpenEndDateWindows) {
			if (window != null) {
				window.setInclusiveStart(latestTime - 1);
				window.setExclusiveEnd(latestTime + windowAdder);
			}
		}
		for (final MutableTimeWindow window : partiallyOpenEndDateWindows) {
			if (window != null) {
				window.setExclusiveEnd(latestTime);
			}
		}

		// Generate DES Purchase and FOB Sale slot bindings before applying vessel
		// restriction constraints
		doBindDischargeSlotsToDESPurchase();
		doBindLoadSlotsToFOBSale();

		// configure constraints
		applyAdjacencyConstraints();

		linkMarkToMarkets();
		// set the self-self distance to zero for all ports to make sure indexed DCP has
		// seen every element.

		// this is no good, because it makes lots of canal choices happen.
		// for (final String s : portDistanceProvider.getKeySet()) {
		// final IMatrixEditor<IPort, Integer> matrix = (IMatrixEditor<IPort, Integer>)
		// portDistanceProvider.get(s);
		// for (final IPort port : ports) {
		// matrix.set(port, port, 0);
		// }
		// }

		// Tell the optional element provider about non-optional elements it may not
		// have seen
		// if this seems a bit ridiculous, yes, it is.
		// TODO think about how this connects with return elements - they aren't
		// optional
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
	private void createRoundtripCargoReturnElements() {

		for (final ILoadOption loadOption : loadSlots) {
			final ISequenceElement loadElement = portSlotsProvider.getElement(loadOption);
			for (final IPort port : portProvider.getAllPorts()) {
				final String name = "short-return-to-" + loadElement.getName() + "-" + port.getName();
				for (final ISpotCharterInMarket market : spotCharterInMarketProviderEditor.getSpotCharterInMarkets()) {

					if (!market.isNominal()) {
						continue;
					}

					final IVesselCharter availability = spotCharterInMarketProviderEditor.getSpotMarketAvailability(market, -1);
					final RoundTripCargoEnd returnPortSlot = new RoundTripCargoEnd(name, port, market.getEndRequirement().getHeelOptions());

					final SequenceElement returnElement = new SequenceElement(indexingContext, name);

					elementDurationsProvider.setElementDuration(returnElement, 0);

					elementPortProvider.setPortForElement(port, returnElement);
					portSlotsProvider.setPortSlot(returnElement, returnPortSlot);
					portTypeProvider.setPortType(returnElement, returnPortSlot.getPortType());

					shortCargoReturnElementProviderEditor.setReturnElement(vesselProvider.getResource(availability), loadElement, port, returnElement);
				}
			}
		}
	}

	@NonNull
	private IVesselCharter createVirtualVesselCharter(@NonNull final ISequenceElement element, @NonNull final VesselInstanceType type) {
		assert type == VesselInstanceType.DES_PURCHASE || type == VesselInstanceType.FOB_SALE;
		// create a new resource for each of these guys, and bind them to their
		// resources
		assert virtualVessel != null;
		final IVesselCharter virtualVesselCharter = createVesselCharter(virtualVessel, "virtual-" + element.getName(), ZeroLongCurve.getInstance(), type, createStartRequirement(),
				createEndRequirement(), null, new ZeroLongCurve(), true);
		// Bind every slot to its vessel
		final IPortSlot portSlot = portSlotsProvider.getPortSlot(element);
		assert portSlot != null;
		freezeSlotToVesselCharter(portSlot, virtualVesselCharter);

		virtualVesselCharterMap.put(element, virtualVesselCharter);
		virtualVesselSlotProviderEditor.setVesselCharterForElement(virtualVesselCharter, element);

		return virtualVesselCharter;
	}

	@Override
	public void dispose() {

		vessels.clear();
		realVesselCharters.clear();
		vesselCharters.clear();
		cargoes.clear();

		for (final IBuilderExtension extension : extensions) {
			extension.dispose();
		}

		extensions.clear();
	}

	/**
	 */
	@Override
	@NonNull
	public IVessel createVessel(final String name, final int minSpeed, final int maxSpeed, final long capacityInM3, final long safetyHeelInM3, final IBaseFuel baseFuel, final IBaseFuel idleBaseFuel,
			final IBaseFuel inPortBaseFuel, final IBaseFuel pilotLightBaseFuel, final int pilotLightRate, final int warmupTimeHours, final int purgeTimeHours, final long cooldownVolumeM3,
			final int minBaseFuelConsumptionPerDay, final boolean hasReliqCapability) {

		final Vessel vessel = new Vessel(name, capacityInM3);

		vessel.setMinSpeed(minSpeed);
		vessel.setMaxSpeed(maxSpeed);

		vessel.setSafetyHeel(safetyHeelInM3);

		vessel.setWarmupTime(warmupTimeHours);
		vessel.setPurgeTime(purgeTimeHours);
		vessel.setCooldownVolume(cooldownVolumeM3);

		vessel.setPilotLightRate(pilotLightRate);
		vessel.setMinBaseFuelConsumptionInMTPerDay(minBaseFuelConsumptionPerDay);

		vessel.setTravelBaseFuel(baseFuel);
		vessel.setIdleBaseFuel(idleBaseFuel);
		vessel.setPilotLightBaseFuel(pilotLightBaseFuel);
		vessel.setInPortBaseFuel(inPortBaseFuel);

		vessel.setHasReliqCapability(hasReliqCapability);

		vessels.add(vessel);

		return vessel;
	}

	@Override
	@NonNull
	public IBaseFuel createBaseFuel(final String name, final int equivalenceFactor) {
		final BaseFuel baseFuel = new BaseFuel(indexingContext, name);
		baseFuel.setEquivalenceFactor(equivalenceFactor);
		baseFuelProvider.registerBaseFuel(baseFuel);

		return baseFuel;
	}

	/**
	 */
	@Override
	public void setVesselStateParameters(@NonNull final IVessel vessel, final VesselState state, final int nboRateInM3PerDay, final int idleNBORateInM3PerDay, final int idleConsumptionRateInMTPerDay,
			final IConsumptionRateCalculator consumptionRateCalculatorInMTPerDay, final int serviceSpeed, final int inPortNBORateInM3PerDay) {

		if (!vessels.contains(vessel)) {
			throw new IllegalArgumentException("IVessel was not created using this builder");
		}

		// Check instance is the same as that used in createVessel(..)
		if (!(vessel instanceof Vessel)) {
			throw new IllegalArgumentException("Expected instance of " + Vessel.class.getCanonicalName());
		}

		final Vessel vesselEditor = (Vessel) vessel;

		vesselEditor.setNBORate(state, nboRateInM3PerDay);
		vesselEditor.setIdleNBORate(state, idleNBORateInM3PerDay);
		vesselEditor.setIdleConsumptionRate(state, idleConsumptionRateInMTPerDay);
		vesselEditor.setConsumptionRate(state, consumptionRateCalculatorInMTPerDay);
		vesselEditor.setServiceSpeed(state, serviceSpeed);
		vesselEditor.setInPortNBORate(state, inPortNBORateInM3PerDay);
	}

	/**
	 */
	@Override
	public void setVesselPortTypeParameters(@NonNull final IVessel vc, final PortType portType, final int inPortConsumptionRateInMTPerDay) {

		((Vessel) vc).setInPortConsumptionRateInMTPerDay(portType, inPortConsumptionRateInMTPerDay);
	}

	@Override
	public void setVesselInaccessiblePorts(@NonNull final IVessel vessel, final Set<IPort> inaccessiblePorts) {
		this.portExclusionProvider.setExcludedPorts(vessel, inaccessiblePorts);
	}

	@Override
	public void setVesselInaccessibleRoutes(@NonNull final IVessel vessel, final Set<ERouteOption> inaccessibleRoutes) {
		this.routeExclusionProvider.setExcludedRoutes(vessel, inaccessibleRoutes);
	}

	@Override
	@NonNull
	public ICharterOutVesselEventPortSlot createCharterOutEvent(final String id, final ITimeWindow arrival, final IPort fromPort, final IPort toPort, final int durationHours,
			final IHeelOptionConsumer heelConsumer, final IHeelOptionSupplier heelSupplier, final long totalHireRevenue, final long repositioning, final long ballastBonus, final boolean optional) {
		final CharterOutVesselEvent event = new CharterOutVesselEvent(arrival, fromPort, toPort, heelConsumer, heelSupplier);

		// TODO should start port and end port be set on this single sequence
		// element,
		// or should there be a second invisible sequence element for
		// repositioning, and something
		// which rigs the distance to be zero between repositioning elements?

		event.setTimeWindow(arrival);
		event.setDurationHours(durationHours);
		event.setStartPort(fromPort);
		event.setEndPort(toPort);
		event.setHireOutRevenue(totalHireRevenue);
		event.setRepositioning(repositioning);
		event.setBallastBonus(ballastBonus);

		if (true || event.getStartPort() != event.getEndPort()) {
			final List<@NonNull ISequenceElement> eventSequenceElements = new LinkedList<>();
			final List<@NonNull IPortSlot> eventPortSlots = new LinkedList<>();

			final SplitCharterOutVesselEventStartPortSlot startSlot = new SplitCharterOutVesselEventStartPortSlot("start-" + id, PortType.Other, event.getStartPort(), event.getTimeWindow(), event);

			final HeelOptionConsumer redirectConsumer = new HeelOptionConsumer(0, Long.MAX_VALUE, VesselTankState.EITHER, ConstantHeelPriceCalculator.ZERO, false);

			final VesselEventPortSlot redirectSlot = new VesselEventPortSlot("redirect-" + id, PortType.Virtual, ANYWHERE, null, event, redirectConsumer);

			final SplitCharterOutVesselEventEndPortSlot endSlot = new SplitCharterOutVesselEventEndPortSlot(id, PortType.CharterOut, event.getEndPort(), event.getTimeWindow(), event);
			// final VesselEventPortSlot startSlot = new
			// CharterOutVesselEventPortSlot("start-" + slot.getId(), PortType.Other,
			// vesselEvent.getStartPort(), slot.getTimeWindow(),
			// charterOutVesselEventPortSlot.getVesselEvent());
			// final VesselEventPortSlot redirectSlot = new VesselEventPortSlot("redirect-"
			// + slot.getId(), PortType.Virtual, ANYWHERE, null, vesselEvent);

			final SequenceElement startElement = new SequenceElement(indexingContext, startSlot.getId());
			final SequenceElement redirectElement = new SequenceElement(indexingContext, redirectSlot.getId());
			final SequenceElement endElement = new SequenceElement(indexingContext, endSlot.getId());
			orderedSequenceElementsEditor.setElementOrder(startElement, redirectElement);
			orderedSequenceElementsEditor.setElementOrder(redirectElement, endElement);

			elementDurationsProvider.setElementDuration(startElement, 0);
			elementDurationsProvider.setElementDuration(redirectElement, 0);
			elementDurationsProvider.setElementDuration(endElement, durationHours);

			portSlotsProvider.setPortSlot(startElement, startSlot);
			portSlotsProvider.setPortSlot(redirectElement, redirectSlot);
			portSlotsProvider.setPortSlot(endElement, endSlot);

			portTypeProvider.setPortType(startElement, startSlot.getPortType());
			portTypeProvider.setPortType(redirectElement, redirectSlot.getPortType());
			portTypeProvider.setPortType(endElement, endSlot.getPortType());

			elementPortProvider.setPortForElement(startSlot.getPort(), startElement);
			elementPortProvider.setPortForElement(redirectSlot.getPort(), redirectElement);
			elementPortProvider.setPortForElement(endSlot.getPort(), endElement);

			sequenceElements.add(startElement);
			sequenceElements.add(redirectElement);
			sequenceElements.add(endElement);

			optionalElements.setOptional(startElement, optional);
			optionalElements.setOptional(redirectElement, optional);
			optionalElements.setOptional(endElement, optional);

			eventPortSlots.add(startSlot);
			eventPortSlots.add(redirectSlot);
			eventPortSlots.add(endSlot);
			eventSequenceElements.add(startElement);
			eventSequenceElements.add(redirectElement);
			eventSequenceElements.add(endElement);

			startSlot.setEventPortSlots(eventPortSlots);
			redirectSlot.setEventPortSlots(eventPortSlots);
			endSlot.setEventPortSlots(eventPortSlots);
			startSlot.setEventSequenceElements(eventSequenceElements);
			redirectSlot.setEventSequenceElements(eventSequenceElements);
			endSlot.setEventSequenceElements(eventSequenceElements);

			// patch up sequencing constraints
			if (reverseAdjacencyConstraints.containsKey(endSlot)) {
				// whatever was meant to be before slot should now be before
				// startSlot, and so on
				constrainSlotAdjacency(reverseAdjacencyConstraints.get(endSlot), startSlot);
				constrainSlotAdjacency(startSlot, redirectSlot);
				constrainSlotAdjacency(redirectSlot, endSlot);
			}

			return endSlot;
		} else {
			final CharterOutVesselEventPortSlot slot = new CharterOutVesselEventPortSlot(id, PortType.CharterOut, event.getEndPort(), event.getTimeWindow(), event);
			vesselEvents.add(slot);
			final SequenceElement endElement = new SequenceElement(indexingContext, slot.getId());
			slot.setEventPortSlots(Collections.singletonList(slot));
			slot.setEventSequenceElements(Collections.singletonList(endElement));
			sequenceElements.add(endElement);
			portTypeProvider.setPortType(endElement, slot.getPortType());
			elementDurationsProvider.setElementDuration(endElement, durationHours);
			portSlotsProvider.setPortSlot(endElement, slot);
			elementPortProvider.setPortForElement(slot.getPort(), endElement);

			optionalElements.setOptional(endElement, optional);

			return slot;
		}
	}

	@Override
	@NonNull
	public IVesselEventPortSlot createDrydockEvent(final String id, final ITimeWindow arrival, final IPort port, final int durationHours) {
		return createVesselEvent(id, PortType.DryDock, arrival, port, port, durationHours);
	}

	@Override
	@NonNull
	public IVesselEventPortSlot createMaintenanceEvent(final String id, final ITimeWindow arrival, final IPort port, final int durationHours) {
		return createVesselEvent(id, PortType.Maintenance, arrival, port, port, durationHours);
	}

	@Override
	@NonNull
	public IVesselEventPortSlot createCharterLengthEvent(final String id, final ITimeWindow arrival, final IPort port, final int durationHours) {
		final CharterLengthEvent event = new CharterLengthEvent(arrival, port);
		event.setDurationHours(durationHours);
		final CharterLengthPortSlot slot = new CharterLengthPortSlot(id, arrival, port, event);
		vesselEvents.add(slot);
		buildVesselEvent(slot);
		return slot;
	}

	/**
	 */
	@NonNull
	public IVesselEventPortSlot createVesselEvent(final String id, final PortType portType, final ITimeWindow arrival, final IPort fromPort, final IPort toPort, final int durationHours) {
		final VesselEvent event = new VesselEvent(arrival, fromPort, toPort);

		// TODO should start port and end port be set on this single sequence
		// element,
		// or should there be a second invisible sequence element for
		// repositioning, and something
		// which rigs the distance to be zero between repositioning elements?

		event.setTimeWindow(arrival);
		event.setDurationHours(durationHours);
		event.setStartPort(fromPort);
		event.setEndPort(toPort);

		final VesselEventPortSlot slot = new VesselEventPortSlot(id, portType, event.getEndPort(), event.getTimeWindow(), event);
		vesselEvents.add(slot);

		buildVesselEvent(slot);
		return slot;
	}

	protected void buildVesselEvent(final VesselEventPortSlot slot) {
		final IVesselEvent vesselEvent = slot.getVesselEvent();

		final SequenceElement endElement = new SequenceElement(indexingContext, slot.getId());

		final List<@NonNull ISequenceElement> eventSequenceElements = new LinkedList<>();
		final List<@NonNull IPortSlot> eventPortSlots = new LinkedList<>();

		eventPortSlots.add(slot);
		eventSequenceElements.add(endElement);

		slot.setEventPortSlots(eventPortSlots);
		slot.setEventSequenceElements(eventSequenceElements);

		sequenceElements.add(endElement);

		portTypeProvider.setPortType(endElement, slot.getPortType());

		elementDurationsProvider.setElementDuration(endElement, vesselEvent.getDurationHours());

		// element needs a port slot

		portSlotsProvider.setPortSlot(endElement, slot);

		elementPortProvider.setPortForElement(slot.getPort(), endElement);
	}

	/**
	 * Set up the {@link #orderedSequenceElementsEditor} to apply the constraints
	 * described in {@link #forwardAdjacencyConstraints} and
	 * {@link #reverseAdjacencyConstraints};
	 */
	private void applyAdjacencyConstraints() {
		for (final Map.Entry<IPortSlot, IPortSlot> entry : forwardAdjacencyConstraints.entrySet()) {
			orderedSequenceElementsEditor.setElementOrder(portSlotsProvider.getElement(entry.getKey()), portSlotsProvider.getElement(entry.getValue()));
			assert reverseAdjacencyConstraints.get(entry.getValue()) == entry.getKey() : "Adjacency constraints have become inconsistent";
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
	 * A {@link Set} of {@link IPortSlot} which cannot be moved to another
	 * vessel/resource. This is populated be calls to
	 * {@link #freezeSlotToVessel(IPortSlot, IVessel)}.
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
			final IVesselCharter virtualVesselCharter = virtualVesselCharterMap.get(desElement);

			if (virtualVesselCharter == null) {
				throw new IllegalArgumentException("DES Purchase is not linked to a virtual vessel");
			}
			// Allow the DES Purchase on it's own resource
			@NonNull
			final IResource resource = vesselProvider.getResource(virtualVesselCharter);
			fobdesCompatibilityProviderEditor.permitElementOnResource(desElement, desPurchase, resource, virtualVesselCharter);

			for (final IDischargeOption option : dischargeSlots) {

				// Skip frozen slots - already bound
				if (frozenSlots.contains(option)) {
					continue;
				}

				if (option instanceof DischargeSlot) {

					if (dischargePorts.keySet().contains(option.getPort())) {
						// Get current allocation
						final ITimeWindow desPurchaseWindowForPort = dischargePorts.get(option.getPort());

						final ITimeWindow tw2 = option.getTimeWindow();
						if (matchingWindows(desPurchaseWindowForPort, tw2) || matchingWindows(tw2, desPurchaseWindowForPort)) {

							final ISequenceElement element = portSlotsProvider.getElement(option);
							fobdesCompatibilityProviderEditor.permitElementOnResource(element, option, resource, virtualVesselCharter);
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
			final IVesselCharter virtualVesselCharter = virtualVesselCharterMap.get(fobElement);
			if (virtualVesselCharter == null) {
				throw new IllegalArgumentException("FOB Sale is not linked to a virtual vessel");
			}

			// Allow the FOB Sale on it's own resource
			@NonNull
			final IResource resource = vesselProvider.getResource(virtualVesselCharter);
			fobdesCompatibilityProviderEditor.permitElementOnResource(fobElement, fobSale, resource, virtualVesselCharter);

			for (final ILoadOption option : loadSlots) {

				// Skip frozen slots - already bound
				if (frozenSlots.contains(option)) {
					continue;
				}

				if (option instanceof LoadSlot) {

					if (loadPorts.keySet().contains(option.getPort())) {
						// Get current allocation
						final ITimeWindow fobSaleWindowForPort = loadPorts.get(option.getPort());

						final ITimeWindow tw2 = option.getTimeWindow();
						if (matchingWindows(fobSaleWindowForPort, tw2) || matchingWindows(tw2, fobSaleWindowForPort)) {
							final ISequenceElement element = portSlotsProvider.getElement(option);
							fobdesCompatibilityProviderEditor.permitElementOnResource(element, option, resource, virtualVesselCharter);
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
	private boolean matchingWindows(final @Nullable ITimeWindow tw1, final @Nullable ITimeWindow tw2) {

		if (tw1 != null && tw2 != null) {
			return tw1.overlaps(tw2);
		}

		return false;
	}

	@Override
	public void createSlotGroupCount(final Collection<IPortSlot> slots, final int count) {
		final Collection<ISequenceElement> elements = new ArrayList<>(slots.size());
		for (final IPortSlot slot : slots) {
			elements.add(portSlotsProvider.getElement(slot));
		}

		slotGroupCountProvider.createSlotGroup(elements, count);
	}

	private static final class ZeroLongCurve implements ILongCurve {
		private ZeroLongCurve() {

		}

		@NonNull
		private static final ZeroLongCurve INSTANCE = new ZeroLongCurve();

		@Override
		public long getValueAtPoint(final int point) {
			return 0;
		}

		@NonNull
		public static ZeroLongCurve getInstance() {
			return INSTANCE;
		}
	}

	/**
	 */
	@Override
	public void setEarliestDate(@NonNull final ZonedDateTime earliestTime) {
		dateKeyProviderEditor.setTimeZero(Instant.from(earliestTime).toEpochMilli());
	}

	/**
	 */
	@Override
	public void createCharterOutCurve(@NonNull final IVessel vessel, @NonNull final ILongCurve charterOutCurve, final int minDuration, final int maxDuration, final Set<IPort> ports,
			final @NonNull ISpotCharterOutMarket market) {
		charterMarketProviderEditor.addCharterOutOption(vessel, charterOutCurve, minDuration, maxDuration, ports, market);
	}

	@Override
	public void setGeneratedCharterOutStartTime(final int charterOutStartTime) {
		charterMarketProviderEditor.setCharterOutStartTime(charterOutStartTime);
	}

	@Override
	public void setGeneratedCharterOutEndTime(final int charterOutEndTime) {
		charterMarketProviderEditor.setCharterOutEndTime(charterOutEndTime);
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

		final IVesselCharter resourceVessel = virtualVesselCharterMap.get(element);
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
	public void setShippingDaysRestrictionReferenceSpeed(@NonNull final IPortSlot slot, @NonNull final IVessel vessel, final VesselState vesselState, final int referenceSpeed) {
		shippingHoursRestrictionProviderEditor.setReferenceSpeed(slot, vessel, vesselState, referenceSpeed);
	}

	@Override
	public void setDivertibleDESAllowedRoute(@NonNull final ILoadOption loadOption, @NonNull final List<ERouteOption> allowedRoutes) {
		for (final ERouteOption route : allowedRoutes) {
			assert route != null;
			shippingHoursRestrictionProviderEditor.setDivertibleDESAllowedRoute(loadOption, route);
		}
	}

	@Override
	public void setDivertibleFOBAllowedRoute(@NonNull final IDischargeOption fobSale, @NonNull final List<ERouteOption> allowedRoutes) {
		for (final ERouteOption route : allowedRoutes) {
			assert route != null;
			shippingHoursRestrictionProviderEditor.setDivertibleFOBAllowedRoute(fobSale, route);
		}
	}

	@Override
	public void freezeSlotToVesselCharter(@NonNull final IPortSlot portSlot, @NonNull final IVesselCharter vesselCharter) {
		this.frozenSlots.add(portSlot);
		final ISequenceElement element = portSlotsProvider.getElement(portSlot);
		final IResource resource = vesselProvider.getResource(vesselCharter);

		resourceAllocationProvider.setAllowedResources(element, Collections.singleton(resource));
	}

	@Override
	@NonNull
	public ISpotCharterInMarket createSpotCharterInMarket(@NonNull final String name, @NonNull final IVessel vessel, @NonNull final ILongCurve dailyCharterInRateCurve, //
			final boolean nominal, final int availabilityCount, @Nullable final IStartRequirement startRequirement, @Nullable final IEndRequirement endRequirement, //
			@Nullable final ICharterContract charterContract, final ILongCurve repositioningFee) {
		return new DefaultSpotCharterInMarket(name, vessel, dailyCharterInRateCurve, nominal, availabilityCount, startRequirement, endRequirement, charterContract, repositioningFee);
	}

	@Override
	@NonNull
	public SequenceElement createSequenceElement(@NonNull final String name) {
		return new SequenceElement(indexingContext, name);
	}

	@Override
	@NonNull
	public IVesselCharter createRoundTripCargoVessel(@NonNull final String name, @NonNull final ISpotCharterInMarket spotCharterInMarket) {

		final IVessel roundTripCargoVessel = spotCharterInMarket.getVessel();
		final IStartRequirement start;
		if (spotCharterInMarket.getStartRequirement() != null) {
			start = spotCharterInMarket.getStartRequirement();
		} else {
			start = createStartRequirement(ANYWHERE, false, null, //
					createHeelSupplier(roundTripCargoVessel.getSafetyHeel(), roundTripCargoVessel.getSafetyHeel(), 0, new ConstantHeelPriceCalculator(0)));
		}
		final IEndRequirement end;
		if (spotCharterInMarket.getEndRequirement() != null) {
			end = spotCharterInMarket.getEndRequirement();
		} else {
			end = createEndRequirement(Collections.singletonList(ANYWHERE), false, new TimeWindow(0, Integer.MAX_VALUE), //
					createHeelConsumer(roundTripCargoVessel.getSafetyHeel(), roundTripCargoVessel.getSafetyHeel(), //
							VesselTankState.MUST_BE_COLD, new ConstantHeelPriceCalculator(0), false));
		}

		// final IVesselCharter spotAvailability = createVesselCharter(vessel,
		// dailyCharterInPrice, VesselInstanceType.SPOT_CHARTER, start, end,
		// spotCharterInMarket,
		// spotCharterInMarket.getCharterContract(), spotIndex,
		// spotCharterInMarket.getRepositioningFee(), true);
		// spotCharterInMarketProviderEditor.addSpotMarketAvailability(spotAvailability,
		// spotCharterInMarket, spotIndex);

		final IVesselCharter vesselCharter = createVesselCharter(roundTripCargoVessel, spotCharterInMarket.getDailyCharterInRateCurve(), //
				VesselInstanceType.ROUND_TRIP, start, end, spotCharterInMarket, spotCharterInMarket.getCharterContract(), -1, spotCharterInMarket.getRepositioningFee(), true);

		spotCharterInMarketProviderEditor.addSpotMarketAvailability(vesselCharter, spotCharterInMarket, -1);
		return vesselCharter;
	}

	@Override
	public void bindSlotsToRoundTripVessel(@NonNull final IVesselCharter roundTripCargoVesselCharter, @NonNull final IPortSlot @NonNull... slots) {

		ISequenceElement prevElement = null;
		for (final IPortSlot slot : slots) {
			final ISequenceElement element = portSlotsProvider.getElement(slot);
			final IResource resource = vesselProvider.getResource(roundTripCargoVesselCharter);

			roundTripVesselPermissionProviderEditor.permitElementOnResource(element, slot, resource, roundTripCargoVesselCharter);

			if (prevElement != null) {
				roundTripVesselPermissionProviderEditor.makeBoundPair(prevElement, element);
			}
			prevElement = element;
		}
	}

	@Override
	public void setVesselPermissions(@NonNull final IPortSlot portSlot, @Nullable final List<@NonNull IVessel> permittedVessels, final boolean isPermitted) {
		Set<IVessel> allowedVessels = new HashSet<>(permittedVessels);
		if (!isPermitted) {
			final Set<IVessel> permitted = new HashSet<>(vessels);
			permitted.removeAll(allowedVessels);
			allowedVessels = permitted;
		}
		// if (!allowedVessels.isEmpty()) {
		allowedVesselProviderEditor.setPermittedVesselAndClasses(portSlot, allowedVessels);
		// }
	}

	@Override
	public void setSuezRouteRebate(@NonNull final IPort from, @NonNull final IPort to, final long rebateFactor) {
		routeCostProvider.setSuezRouteRebateFactor(from, to, rebateFactor);
	}
}
