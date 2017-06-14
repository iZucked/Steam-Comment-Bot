/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.CheckingIndexingContext;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
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
import com.mmxlabs.scheduler.optimiser.components.DefaultSpotCharterInMarket;
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
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.BaseFuel;
import com.mmxlabs.scheduler.optimiser.components.impl.Cargo;
import com.mmxlabs.scheduler.optimiser.components.impl.CharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.impl.CharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.DefaultVesselAvailability;
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
import com.mmxlabs.scheduler.optimiser.components.impl.TotalVolumeLimit;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEvent;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContract;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitEditor;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProviderEditor;
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
	 * A "virtual" port which is zero distance from all other ports, to be used in cases where a vessel can be in any location. This can be replaced with a real location at a later date, after running
	 * an optimisation.
	 */
	private IPort ANYWHERE;

	// @NonNull
	// private final List<IPort> ports = new LinkedList<IPort>();

	/**
	 * A field for tracking the time at which the last time window closes
	 */
	private final int endOfLatestWindow = 0;

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
	private final List<IVesselEventPortSlot> vesselEvents = new LinkedList<>();

	@NonNull
	private final IIndexingContext indexingContext = new CheckingIndexingContext();

	@Inject
	@NonNull
	private IVesselProviderEditor vesselProvider;

	@Inject
	@NonNull
	private IPortProvider portProvider;

	@Inject
	@NonNull
	private IElementPortProviderEditor elementPortProvider;

	@Inject
	@NonNull
	private IPortSlotProviderEditor portSlotsProvider;

	@Inject
	@NonNull
	private IDistanceMatrixProvider portDistanceProvider;

	@Inject
	@NonNull
	private IOrderedSequenceElementsDataComponentProviderEditor orderedSequenceElementsEditor;

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
	private IRouteExclusionProviderEditor routeExclusionProvider;

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
	private IPortCooldownDataProviderEditor cooldownDataProvider;

	@Inject
	@NonNull
	private IOptionalElementsProviderEditor optionalElements;

	@Inject
	@NonNull
	private ILockedElementsProviderEditor lockedElements;

	@Inject
	@NonNull
	private ISpotMarketSlotsProviderEditor spotMarketSlots;

	@Inject
	@NonNull
	private ISpotCharterInMarketProviderEditor spotCharterInMarketProviderEditor;

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

	@Inject
	private @NonNull IRoundTripVesselPermissionProviderEditor roundTripVesselPermissionProviderEditor;

	@Inject
	private @NonNull IAllowedVesselProviderEditor allowedVesselProviderEditor;

	@Inject
	private IFOBDESCompatibilityProviderEditor fobdesCompatibilityProviderEditor;

	private final List<@NonNull IPortSlot> permittedRoundTripVesselSlots = new LinkedList<>();

	@NonNull
	private final Map<IPort, MarkToMarket> desPurchaseMTMPortMap = new HashMap<IPort, MarkToMarket>();

	@NonNull
	private final Map<IPort, MarkToMarket> desSaleMTMPortMap = new HashMap<IPort, MarkToMarket>();

	@NonNull
	private final Map<IPort, MarkToMarket> fobSaleMTMPortMap = new HashMap<IPort, MarkToMarket>();

	@NonNull
	private final Map<IPort, MarkToMarket> fobPurchaseMTMPortMap = new HashMap<IPort, MarkToMarket>();

	@Inject
	@NonNull
	private IPromptPeriodProviderEditor promptPeriodProviderEditor;

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
	 * Map between a virtual sequence element and the virtual {@link IVesselEvent} instance representing it.
	 */
	@NonNull
	private final Map<ISequenceElement, IVesselAvailability> virtualVesselAvailabilityMap = new HashMap<ISequenceElement, IVesselAvailability>();

	public SchedulerBuilder() {
		indexingContext.registerType(SequenceElement.class);
		indexingContext.registerType(Resource.class);
	}

	// @Inject to trigger call after constructor
	@Inject
	public void init() {
		// Create the anywhere port
		ANYWHERE = portProvider.getAnywherePort();

		// setup fake vessels for virtual elements.
		virtualClass = createVesselClass("virtual", 0, 0, Long.MAX_VALUE, 0, createBaseFuel("fakeFuel", 0), 0, 0, 0, 0, false);
	}

	/**
	 */
	@Override
	@NonNull
	public ILoadSlot createLoadSlot(final @NonNull String id, final @NonNull IPort port, final ITimeWindow window, final long minVolumeInM3, final long maxVolumeInM3,
			final ILoadPriceCalculator loadContract, final int cargoCVValue, final int durationHours, final boolean cooldownSet, final boolean cooldownForbidden, final int pricingDate,
			final PricingEventType pricingEvent, final boolean optional, final boolean locked, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {

		final LoadSlot slot = new LoadSlot(id, port, window, isVolumeLimitInM3, minVolumeInM3, maxVolumeInM3, loadContract, cargoCVValue, cooldownSet, cooldownForbidden);

		final ISequenceElement element = configureLoadOption(slot, minVolumeInM3, maxVolumeInM3, loadContract, cargoCVValue, pricingDate, pricingEvent, optional, locked, isSpotMarketSlot,
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
			final boolean locked, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {

		if (port == null) {
			port = ANYWHERE;
		}

		final LoadOption slot = new LoadOption(id, port, window, isVolumeLimitInM3, minVolume, maxVolume, priceCalculator, cargoCVValue);
		final ISequenceElement element = configureLoadOption(slot, minVolume, maxVolume, priceCalculator, cargoCVValue, pricingDate, pricingEvent, slotIsOptional, locked, isSpotMarketSlot,
				isVolumeLimitInM3);

		elementDurationsProvider.setElementDuration(element, durationHours);

		unshippedElements.add(element);

		createVirtualVesselAvailability(element, VesselInstanceType.DES_PURCHASE);

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

		addSlotToVolumeConstraints(slot);

		calculatorProvider.addLoadPriceCalculator(priceCalculator);

		return element;
	}

	/**
	 */
	@Override
	@NonNull
	public IDischargeOption createFOBSaleDischargeSlot(final String id, @Nullable IPort port, final ITimeWindow window, final long minVolume, final long maxVolume, final long minCvValue,
			final long maxCvValue, final ISalesPriceCalculator priceCalculator, final int durationHours, final int pricingDate, final PricingEventType pricingEvent, final boolean slotIsOptional,
			final boolean slotIsLocked, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {

		if (port == null) {
			port = ANYWHERE;
		}
		assert port != null;

		final DischargeOption slot = new DischargeOption(id, port, window, isVolumeLimitInM3, minVolume, maxVolume, minCvValue, maxCvValue, priceCalculator);
		slot.setPricingDate(pricingDate);

		final ISequenceElement element = configureDischargeOption(slot, minVolume, maxVolume, minCvValue, maxCvValue, priceCalculator, pricingDate, pricingEvent, slotIsOptional, slotIsLocked,
				isSpotMarketSlot, isVolumeLimitInM3);

		elementDurationsProvider.setElementDuration(element, durationHours);

		unshippedElements.add(element);

		createVirtualVesselAvailability(element, VesselInstanceType.FOB_SALE);

		return slot;
	}

	@NonNull
	private ISequenceElement configureDischargeOption(@NonNull final DischargeOption slot,
			// final String id, final IPort port, final ITimeWindow window,
			final long minVolume, final long maxVolume, final long minCvValue, final long maxCvValue, final ISalesPriceCalculator priceCalculator, final int pricingDate,
			final PricingEventType pricingEvent, final boolean optional, final boolean locked, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {
		// slot.setId(id);
		// slot.setPort(port);
		// slot.setTimeWindow(window);
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
			final boolean isLockedSlot, final boolean isSpotMarketSlot, final boolean isVolumeLimitInM3) {

		final DischargeSlot slot = new DischargeSlot(id, port, window, isVolumeLimitInM3, minVolumeInM3, maxVolumeInM3, pricePerMMBTu, minCvValue, maxCvValue);
		slot.setPricingDate(pricingDate);

		final ISequenceElement element = configureDischargeOption(slot, minVolumeInM3, maxVolumeInM3, minCvValue, maxCvValue, pricePerMMBTu, pricingDate, pricingEvent, optional, isLockedSlot,
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
			if (vesselProvider.getVesselAvailability(resource).getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER)) {
				// spot charters have no end time window, because their end date
				// is very flexible.
				// spotVesselEndWindows.add((MutableTimeWindow)endRequirement.getTimeWindow());
			} else if (vesselProvider.getVesselAvailability(resource).getVesselInstanceType().equals(VesselInstanceType.ROUND_TRIP)) {
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
		final IVesselClass vesselClass = spotCharterInMarket.getVesselClass();
		final IStartRequirement start = createStartRequirement(ANYWHERE, false, null,
				createHeelSupplier(vesselClass.getSafetyHeel(), vesselClass.getSafetyHeel(), 0, new ConstantHeelPriceCalculator(0)));
		final IEndRequirement end;
		if (spotCharterInMarket.getEndRequirement() != null) {
			end = spotCharterInMarket.getEndRequirement();
		} else {
			end = createEndRequirement(Collections.singletonList(ANYWHERE), false, null,
					createHeelConsumer(vesselClass.getSafetyHeel(), vesselClass.getSafetyHeel(), VesselTankState.MUST_BE_COLD, new ConstantHeelPriceCalculator(0)), false);
		}
		final ILongCurve dailyCharterInPrice = spotCharterInMarket.getDailyCharterInRateCurve();
		final IVessel spotVessel = createVessel(name, vesselClass, vesselClass.getCargoCapacity());

		// End cold already enforced in VoyagePlanner#getVoyageOptionsAndSetVpoChoices
		final IVesselAvailability spotAvailability = createVesselAvailability(spotVessel, dailyCharterInPrice, VesselInstanceType.SPOT_CHARTER, start, end, spotCharterInMarket,
				spotCharterInMarket.getBallastBonusContract(), spotIndex, new ZeroLongCurve(), true);
		spotCharterInMarketProviderEditor.addSpotMarketAvailability(spotAvailability, spotCharterInMarket, spotIndex);

		return spotAvailability;
	}

	@Override
	@NonNull
	public IVessel createVessel(final @NonNull String name, @NonNull final IVesselClass vesselClass, final long cargoCapacity) {

		if (!vesselClasses.contains(vesselClass)) {
			throw new IllegalArgumentException("IVesselClass was not created using this builder");
		}

		final Vessel vessel = new Vessel(name, vesselClass, cargoCapacity);

		vessels.add(vessel);
		return vessel;
	}

	@Override
	public @NonNull IHeelOptionSupplier createHeelSupplier(long minHeelInM3, long maxHeelInM3, int heelCVValue, @NonNull IHeelPriceCalculator heelPriceCalculator) {
		return new HeelOptionSupplier(minHeelInM3, maxHeelInM3, heelCVValue, heelPriceCalculator);

	}

	@Override
	public @NonNull IHeelOptionConsumer createHeelConsumer(long minHeelInM3, long maxHeelInM3, @NonNull VesselTankState tankState, @NonNull IHeelPriceCalculator heelPriceCalculator) {
		return new HeelOptionConsumer(minHeelInM3, maxHeelInM3, tankState, heelPriceCalculator);
	}

	@Override
	@NonNull
	public IVesselAvailability createVesselAvailability(@NonNull final IVessel vessel, final ILongCurve dailyCharterInRate, final VesselInstanceType vesselInstanceType, final IStartRequirement start,
			final IEndRequirement end, IBallastBonusContract ballastBonusContract, final ILongCurve repositioningFee, final boolean isOptional) {
		return createVesselAvailability(vessel, dailyCharterInRate, vesselInstanceType, start, end, null, ballastBonusContract, -1, repositioningFee, isOptional);
	}

	@NonNull
	private IVesselAvailability createVesselAvailability(@NonNull final IVessel vessel, final ILongCurve dailyCharterInRate, @NonNull final VesselInstanceType vesselInstanceType,
			@NonNull final IStartRequirement start, @NonNull final IEndRequirement end, @Nullable final ISpotCharterInMarket spotCharterInMarket, IBallastBonusContract ballastBonusContract,
			final int spotIndex, final ILongCurve repositioningFee, final boolean isOptional) {
		if (!vessels.contains(vessel)) {
			throw new IllegalArgumentException("IVessel was not created using this builder");
		}

		final String name = vessel.getName();// + vesselAvailabilities.size();

		final DefaultVesselAvailability vesselAvailability = new DefaultVesselAvailability(vessel, vesselInstanceType);

		vesselAvailability.setDailyCharterInRate(dailyCharterInRate);

		vesselAvailability.setStartRequirement(start);
		vesselAvailability.setEndRequirement(end);

		final IResource resource = new Resource(indexingContext, name);

		// Register with provider
		vesselProvider.setVesselAvailabilityResource(resource, vesselAvailability);
		vesselProvider.setResourceOptional(resource, vesselAvailability.isOptional());
		vesselAvailabilities.add(vesselAvailability);
		if (vesselInstanceType != VesselInstanceType.DES_PURCHASE && vesselInstanceType != VesselInstanceType.FOB_SALE) {
			realVesselAvailabilities.add(vesselAvailability);
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
			// Fleet vessels and spot vessels both run to the end of the optimisation if they don't have an end date.
			if (!vesselInstanceType.equals(VesselInstanceType.SPOT_CHARTER) && !vesselInstanceType.equals(VesselInstanceType.ROUND_TRIP)) {
				fullyOpenEndDateWindows.add((MutableTimeWindow) end.getTimeWindow());
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
		// BugzID: 576 allow end element on any vessel, to prevent ResourceAllocationConstraint from disallowing 2opt2s at end

		// resourceAllocationProvider.setAllowedResources(endElement, Collections.singleton(resource));

		startEndRequirementProvider.setStartEndRequirements(resource, start, end);
		startEndRequirementProvider.setStartEndElements(resource, startElement, endElement);

		if (spotCharterInMarket != null) {
			vesselAvailability.setSpotCharterInMarket(spotCharterInMarket);
			vesselAvailability.setSpotIndex(spotIndex);
		}

		vesselAvailability.setOptional(isOptional);
		vesselAvailability.setRepositioningFee(repositioningFee);

		vesselAvailability.setBallastBonusContract(ballastBonusContract);
		return vesselAvailability;
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
		return createEndRequirement(Collections.singletonList(ANYWHERE), false, null, createHeelConsumer(0, 0, VesselTankState.MUST_BE_WARM, new ConstantHeelPriceCalculator(0)), false);
	}

	@Override
	public @NonNull IEndRequirement createEndRequirement(@Nullable Collection<IPort> portSet, boolean hasTimeRequirement, @Nullable ITimeWindow timeWindow, IHeelOptionConsumer heelConsumer,
			boolean isOpenEnded) {

		if (portSet == null || portSet.isEmpty()) {
			return new EndRequirement(Collections.singleton(ANYWHERE), false, hasTimeRequirement, timeWindow, heelConsumer, isOpenEnded);
		} else {
			return new EndRequirement(portSet, true, hasTimeRequirement, timeWindow, heelConsumer, isOpenEnded);
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

		// Patch up end time windows
		// The return time should be the soonest we can get back to the previous load,
		// presumably in the slowest vessel without going via a canal.

		// TODO what about return to first load rule?

		int latestDischarge = 0;
		IPort loadPort = null, dischargePort = null;
		for (final ICargo cargo : cargoes) {
			final List<IPortSlot> portSlots = cargo.getPortSlots();
			if (portSlots.size() > 1) {
				final IPortSlot end = portSlots.get(portSlots.size() - 1);
				final IPortSlot endMinus1 = portSlots.get(portSlots.size() - 2);
				@Nullable
				final ITimeWindow timeWindow = end.getTimeWindow();
				if (timeWindow != null) {
					final int endOfDischargeWindow = timeWindow.getExclusiveEnd();
					if (endOfDischargeWindow > latestDischarge) {
						latestDischarge = endOfDischargeWindow;
						loadPort = endMinus1.getPort();
						dischargePort = end.getPort();
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
			} else if (rule == 3) {
				latestTime = Math.max(endOfLatestWindow, latestDischarge) + 60 * 24;
			} else if (rule == 4) {

				// Include all time windows *except* spot market slots (These need to be registered externally)
				final OptionalInt optionalMax = sequenceElements.stream() //
						.filter(element -> !spotMarketSlots.isSpotMarketSlot(element)) //
						.map(element -> portSlotsProvider.getPortSlot(element)) //
						.filter(portSlot -> portSlot.getTimeWindow() != null) //
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
			latestTime = promptPeriodProviderEditor.getEndOfSchedulingPeriod();
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

		// Generate DES Purchase and FOB Sale slot bindings before applying vessel restriction constraints
		doBindDischargeSlotsToDESPurchase();
		doBindLoadSlotsToFOBSale();

		// configure constraints
		applyAdjacencyConstraints();

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
	private void createRoundtripCargoReturnElements() {
		// for (final IPortSlot portSlot : permittedRoundTripVesselSlots) {
		for (final IPortSlot portSlot : loadSlots) {
			if (portSlot instanceof ILoadOption) {
				final ILoadOption loadOption = (ILoadOption) portSlot;

				final ISequenceElement loadElement = portSlotsProvider.getElement(loadOption);

				final String name = "short-return-to-" + loadElement.getName();
				final IPort port = loadOption.getPort();
				final RoundTripCargoEnd returnPortSlot = new RoundTripCargoEnd(name, port);

				final SequenceElement returnElement = new SequenceElement(indexingContext, name);

				elementDurationsProvider.setElementDuration(returnElement, 0);

				elementPortProvider.setPortForElement(port, returnElement);
				portSlotsProvider.setPortSlot(returnElement, returnPortSlot);
				portTypeProvider.setPortType(returnElement, returnPortSlot.getPortType());

				shortCargoReturnElementProviderEditor.setReturnElement(loadElement, loadOption, returnElement);
			}
		}
	}

	@NonNull
	private IVesselAvailability createVirtualVesselAvailability(@NonNull final ISequenceElement element, @NonNull final VesselInstanceType type) {
		assert type == VesselInstanceType.DES_PURCHASE || type == VesselInstanceType.FOB_SALE;
		// create a new resource for each of these guys, and bind them to their resources
		assert virtualClass != null;
		final IVessel virtualVessel = createVessel("virtual-" + type.toString() + "-" + element.getName(), virtualClass, virtualClass.getCargoCapacity());
		final IVesselAvailability virtualVesselAvailability = createVesselAvailability(virtualVessel, new ZeroLongCurve(), type, createStartRequirement(), createEndRequirement(), null,
				new ZeroLongCurve(), true);
		// Bind every slot to its vessel
		final IPortSlot portSlot = portSlotsProvider.getPortSlot(element);
		assert portSlot != null;
		freezeSlotToVesselAvailability(portSlot, virtualVesselAvailability);

		virtualVesselAvailabilityMap.put(element, virtualVesselAvailability);
		virtualVesselSlotProviderEditor.setVesselAvailabilityForElement(virtualVesselAvailability, element);

		return virtualVesselAvailability;
	}

	@Override
	public void dispose() {

		vessels.clear();
		realVesselAvailabilities.clear();
		vesselAvailabilities.clear();
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
	public IVesselClass createVesselClass(final String name, final int minSpeed, final int maxSpeed, final long capacityInM3, final long safetyHeelInM3, final IBaseFuel baseFuel,
			final int pilotLightRate, final int warmupTimeHours, final long cooldownVolumeM3, final int minBaseFuelConsumptionPerDay, final boolean hasReliqCapability) {

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

		vesselClass.setHasReliqCapability(hasReliqCapability);

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
			final int idleConsumptionRateInMTPerDay, final IConsumptionRateCalculator consumptionRateCalculatorInMTPerDay, final int serviceSpeed, final int inPortNBORateInM3PerDay) {

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
		vc.setInPortNBORate(state, inPortNBORateInM3PerDay);
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

	@Override
	public void setVesselInaccessibleRoutes(@NonNull final IVessel vessel, final Set<ERouteOption> inaccessibleRoutes) {
		this.routeExclusionProvider.setExcludedRoutes(vessel, inaccessibleRoutes);
	}

	@Override
	public void setVesselClassInaccessibleRoutes(@NonNull final IVesselClass vesselClass, final Set<ERouteOption> inaccessibleRoutes) {
		this.routeExclusionProvider.setExcludedRoutes(vesselClass, inaccessibleRoutes);
	}

	@Override
	@NonNull
	public ICharterOutVesselEventPortSlot createCharterOutEvent(final String id, final ITimeWindow arrival, final IPort fromPort, final IPort toPort, final int durationHours,
			final IHeelOptionConsumer heelConsumer, IHeelOptionSupplier heelSupplier, final long totalHireRevenue, final long repositioning, final long ballastBonus, boolean optional) {
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

		if (event.getStartPort() != event.getEndPort()) {
			final List<@NonNull ISequenceElement> eventSequenceElements = new LinkedList<>();
			final List<@NonNull IPortSlot> eventPortSlots = new LinkedList<>();

			final SplitCharterOutVesselEventStartPortSlot startSlot = new SplitCharterOutVesselEventStartPortSlot("start-" + id, PortType.Other, event.getStartPort(), event.getTimeWindow(), event);

			HeelOptionConsumer redirectConsumer = new HeelOptionConsumer(0, Long.MAX_VALUE, VesselTankState.EITHER, ConstantHeelPriceCalculator.ZERO);

			final VesselEventPortSlot redirectSlot = new VesselEventPortSlot("redirect-" + id, PortType.Virtual, ANYWHERE, null, event, redirectConsumer);

			final SplitCharterOutVesselEventEndPortSlot endSlot = new SplitCharterOutVesselEventEndPortSlot(id, PortType.CharterOut, event.getEndPort(), event.getTimeWindow(), event);
			// final VesselEventPortSlot startSlot = new CharterOutVesselEventPortSlot("start-" + slot.getId(), PortType.Other, vesselEvent.getStartPort(), slot.getTimeWindow(),
			// charterOutVesselEventPortSlot.getVesselEvent());
			// final VesselEventPortSlot redirectSlot = new VesselEventPortSlot("redirect-" + slot.getId(), PortType.Virtual, ANYWHERE, null, vesselEvent);

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

			endSlot.setEventPortSlots(eventPortSlots);
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

	// @Override
	// public void setSlotVesselAvailabilityRestriction(final IPortSlot slot, final Set<IVesselAvailability> vesselAvailabilities) {
	//
	// assert slot != null;
	// constrainSlotToVesselAvailabilities(slot, vesselAvailabilities);
	// }

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
			// Allow the DES Purchase on it's own resource
			@NonNull
			final IResource resource = vesselProvider.getResource(virtualVesselAvailability);
			fobdesCompatibilityProviderEditor.permitElementOnResource(desElement, desPurchase, resource, virtualVesselAvailability);

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
							fobdesCompatibilityProviderEditor.permitElementOnResource(element, option, resource, virtualVesselAvailability);
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

			// Allow the FOB Sale on it's own resource
			@NonNull
			final IResource resource = vesselProvider.getResource(virtualVesselAvailability);
			fobdesCompatibilityProviderEditor.permitElementOnResource(fobElement, fobSale, resource, virtualVesselAvailability);

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
							fobdesCompatibilityProviderEditor.permitElementOnResource(element, option, resource, virtualVesselAvailability);
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
			// End is within
			if (tw1.getExclusiveEnd() > tw2.getInclusiveStart() && tw1.getExclusiveEnd() - 1 < tw2.getExclusiveEnd()) {
				return true;
			}
			// Start is within
			if (tw1.getInclusiveStart() >= tw2.getInclusiveStart() && tw1.getInclusiveStart() < tw2.getExclusiveEnd()) {
				return true;
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

	private static final class ZeroLongCurve implements ILongCurve {
		protected ZeroLongCurve() {

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
	public void createCharterOutCurve(@NonNull final IVesselClass vesselClass, @NonNull final ILongCurve charterOutCurve, final int minDuration, final Set<IPort> ports) {
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
	public void setDivertableDESAllowedRoute(@NonNull final ILoadOption loadOption, @NonNull final List<ERouteOption> allowedRoutes) {
		for (final ERouteOption route : allowedRoutes) {
			assert route != null;
			shippingHoursRestrictionProviderEditor.setDivertableDESAllowedRoute(loadOption, route);
		}
	}

	@Override
	public void freezeSlotToVesselAvailability(@NonNull final IPortSlot portSlot, @NonNull final IVesselAvailability vesselAvailability) {
		this.frozenSlots.add(portSlot);
		final ISequenceElement element = portSlotsProvider.getElement(portSlot);
		final IResource resource = vesselProvider.getResource(vesselAvailability);

		resourceAllocationProvider.setAllowedResources(element, Collections.singleton(resource));
	}

	@Override
	@NonNull
	public ISpotCharterInMarket createSpotCharterInMarket(@NonNull final String name, @NonNull final IVesselClass vesselClass, @NonNull final ILongCurve dailyCharterInRateCurve,
			final int availabilityCount, @Nullable IEndRequirement endRequirement, @Nullable IBallastBonusContract ballastBonusContract) {
		return new DefaultSpotCharterInMarket(name, vesselClass, dailyCharterInRateCurve, availabilityCount, endRequirement, ballastBonusContract);
	}

	@Override
	@NonNull
	public SequenceElement createSequenceElement(@NonNull final String name) {
		return new SequenceElement(indexingContext, name);
	}

	@Override
	@NonNull
	public IVesselAvailability createRoundTripCargoVessel(@NonNull final String name, @NonNull final ISpotCharterInMarket spotCharterInMarket) {

		final IVesselClass roundTripCargoVesselClass = spotCharterInMarket.getVesselClass();

		final IVessel roundTripCargoVessel = createVessel(name, roundTripCargoVesselClass, roundTripCargoVesselClass.getCargoCapacity());

		final IStartRequirement start = createStartRequirement(ANYWHERE, false, null,
				createHeelSupplier(roundTripCargoVesselClass.getSafetyHeel(), roundTripCargoVesselClass.getSafetyHeel(), 0, new ConstantHeelPriceCalculator(0)));
		final IEndRequirement end = createEndRequirement(Collections.singletonList(ANYWHERE), false, null,
				createHeelConsumer(roundTripCargoVesselClass.getSafetyHeel(), roundTripCargoVesselClass.getSafetyHeel(), VesselTankState.MUST_BE_COLD, new ConstantHeelPriceCalculator(0)), false);

		final IVesselAvailability vesselAvailability = createVesselAvailability(roundTripCargoVessel, spotCharterInMarket.getDailyCharterInRateCurve(), VesselInstanceType.ROUND_TRIP, start, end,
				spotCharterInMarket, null, -1, new ZeroLongCurve(), true);

		spotCharterInMarketProviderEditor.addSpotMarketAvailability(vesselAvailability, spotCharterInMarket, -1);
		return vesselAvailability;
	}

	@Override
	public void bindSlotsToRoundTripVessel(@NonNull final IVesselAvailability roundTripCargoVesselAvailability, @NonNull final IPortSlot @NonNull... slots) {

		ISequenceElement prevElement = null;
		for (final IPortSlot slot : slots) {
			final ISequenceElement element = portSlotsProvider.getElement(slot);
			final IResource resource = vesselProvider.getResource(roundTripCargoVesselAvailability);

			roundTripVesselPermissionProviderEditor.permitElementOnResource(element, slot, resource, roundTripCargoVesselAvailability);

			permittedRoundTripVesselSlots.add(slot);

			if (prevElement != null) {
				roundTripVesselPermissionProviderEditor.makeBoundPair(prevElement, element);
			}
			prevElement = element;
		}
	}

	@Override
	public void setVesselAndClassPermissions(@NonNull final IPortSlot portSlot, @Nullable final List<@NonNull IVessel> permittedVessels,
			@Nullable final List<@NonNull IVesselClass> permittedVesselClasses) {
		allowedVesselProviderEditor.setPermittedVesselAndClasses(portSlot, permittedVessels, permittedVesselClasses);
	}

}
