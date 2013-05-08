/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.CheckingIndexingContext;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
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
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.impl.OptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.IXYPortDistanceCalculator;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.Cargo;
import com.mmxlabs.scheduler.optimiser.components.impl.CargoShortEnd;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadOption;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.Port;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.components.impl.StartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.TotalVolumeLimit;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEvent;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.XYPort;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitEditor;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IShortCargoReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ISlotGroupCountProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Implementation of {@link ISchedulerBuilder}
 * 
 * @author Simon Goodall
 * 
 */
public final class SchedulerBuilder implements ISchedulerBuilder {
	/**
	 * For debug & timing purposes. Switches the indexing DCPs on or off.
	 */
	// private static final boolean USE_INDEXED_DCPS = true;

	private final List<IBuilderExtension> extensions = new LinkedList<IBuilderExtension>();

	private final List<IResource> resources = new ArrayList<IResource>();

	private final List<ISequenceElement> sequenceElements = new ArrayList<ISequenceElement>();

	private final List<IVesselClass> vesselClasses = new LinkedList<IVesselClass>();

	private final List<IVessel> vessels = new LinkedList<IVessel>();
	private final List<IVessel> realVessels = new LinkedList<IVessel>();

	private final List<ICargo> cargoes = new LinkedList<ICargo>();

	private final List<ILoadOption> loadSlots = new LinkedList<ILoadOption>();

	private final List<IDischargeOption> dischargeSlots = new LinkedList<IDischargeOption>();

	private final List<ITimeWindow> timeWindows = new LinkedList<ITimeWindow>();

	/**
	 * List of end slots, which need to be corrected in getOptimisationData to have the latest time in them
	 */
	private final List<Pair<ISequenceElement, PortSlot>> endSlots = new LinkedList<Pair<ISequenceElement, PortSlot>>();

	/**
	 * A "virtual" port which is zero distance from all other ports, to be used in cases where a vessel can be in any location. This can be replaced with a real location at a later date, after running
	 * an optimisation.
	 */
	private IPort ANYWHERE;

	private final List<IPort> ports = new LinkedList<IPort>();

	/**
	 * A field for tracking the time at which the last time window closes
	 */
	private int endOfLatestWindow = 0;

	/**
	 * Tracks elements that are not shipped.
	 */
	private final List<ISequenceElement> unshippedElements = new ArrayList<ISequenceElement>();
	/*
	 * Constraint-tracking data structures; constraints created through the builder are applied at the very end, in case they affect things created after them.
	 */

	/**
	 * Tracks forward adjacency constraints; value must follow key. The reverse of {@link #reverseAdjacencyConstraints}
	 */
	private final Map<IPortSlot, IPortSlot> forwardAdjacencyConstraints = new HashMap<IPortSlot, IPortSlot>();

	/**
	 * Tracks forward adjacency constraints; key must follow value. The reverse of {@link #forwardAdjacencyConstraints}
	 */
	private final Map<IPortSlot, IPortSlot> reverseAdjacencyConstraints = new HashMap<IPortSlot, IPortSlot>();

	private final Map<IPort, List<TotalVolumeLimit>> loadLimits = new HashMap<IPort, List<TotalVolumeLimit>>();

	private final Map<IPort, List<TotalVolumeLimit>> dischargeLimits = new HashMap<IPort, List<TotalVolumeLimit>>();

	/**
	 * The slots for vessel events which have been generated; these are stored so that in {@link #buildVesselEvents()} they can have some extra post-processing done to set up any constraints
	 */
	private final List<VesselEventPortSlot> vesselEvents = new LinkedList<VesselEventPortSlot>();

	private final Map<IPortSlot, Set<IVessel>> slotVesselRestrictions = new HashMap<IPortSlot, Set<IVessel>>();

	private final Map<IPortSlot, Set<IVesselClass>> slotVesselClassRestrictions = new HashMap<IPortSlot, Set<IVesselClass>>();

	private final IIndexingContext indexingContext = new CheckingIndexingContext();

	@Inject
	private IXYPortDistanceCalculator distanceProvider;

	@Inject
	private IVesselProviderEditor vesselProvider;

	@Inject
	private IPortProviderEditor portProvider;

	@Inject
	private IPortSlotProviderEditor portSlotsProvider;

	@Inject
	private IOrderedSequenceElementsDataComponentProviderEditor orderedSequenceElementsEditor;

	@Inject
	private ITimeWindowDataComponentProviderEditor timeWindowProvider;

	@Inject
	private IndexedMultiMatrixProvider<IPort, Integer> portDistanceProvider;

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
	private IReturnElementProviderEditor returnElementProvider;

	@Inject
	private IRouteCostProviderEditor routeCostProvider;

	@Inject
	private ITotalVolumeLimitEditor totalVolumeLimits;

	@Inject
	private IDiscountCurveProviderEditor discountCurveProvider;

	@Inject
	private IPortCostProviderEditor portCostProvider;

	@Inject
	private IPortCVProviderEditor portCVProvider;

	/**
	 * Keeps track of calculators
	 */
	@Inject
	private ICalculatorProviderEditor calculatorProvider;

	@Inject
	private IOptionalElementsProviderEditor optionalElements;

	@Inject
	private Provider<IMatrixEditor<IPort, Integer>> matrixEditorProvider;

	@Inject
	private ISlotGroupCountProviderEditor slotGroupCountProvider;

	@Inject
	private IVirtualVesselSlotProviderEditor virtualVesselSlotProviderEditor;

	@Inject
	private IDateKeyProviderEditor dateKeyProviderEditor;

	@Inject
	private ICharterMarketProviderEditor charterMarketProviderEditor;

	@Inject
	private IAlternativeElementProvider alternativeElementProvider;

	@Inject
	private IShortCargoReturnElementProviderEditor shortCargoReturnElementProviderEditor;

	/**
	 * Fake vessel class for virtual elements.
	 */
	private IVesselClass virtualClass;
	/**
	 * Fake vessel class for cargo shorts.
	 */
	// private IVesselClass cargoShortsClass;
	// private IVessel cargoShortsVessel;
	private IVessel shortCargoVessel;

	/**
	 * Map between a virtual sequence element and the virtual {@link IVesselEvent} instance representing it.
	 */
	private final Map<ISequenceElement, IVessel> virtualVesselMap = new HashMap<ISequenceElement, IVessel>();

	public SchedulerBuilder() {
		indexingContext.registerType(SequenceElement.class);
		indexingContext.registerType(Port.class);
		indexingContext.registerType(Resource.class);
		indexingContext.registerType(Vessel.class);
	}

	// @Inject to trigger call after constructor
	@Inject
	public void init() {
		// Create the anywhere port
		ANYWHERE = createPort("ANYWHERE", false, new ICooldownPriceCalculator() {
			@Override
			public void prepareEvaluation(final ISequences sequences) {
			}

			@Override
			public int calculateCooldownUnitPrice(final ILoadSlot slot, final int time) {
				return 0;
			}

			@Override
			public int calculateCooldownUnitPrice(final int time) {
				return 0;
			}
		});

		// setup fake vessels for virtual elements.
		virtualClass = createVesselClass("virtual", 0, 0, Long.MAX_VALUE, 0, 0, 0, 0, 0, 0);
	}

	@Override
	public ILoadSlot createLoadSlot(final String id, final IPort port, final ITimeWindow window, final long minVolumeInM3, final long maxVolumeInM3, final ILoadPriceCalculator loadContract,
			final int cargoCVValue, final int durationHours, final boolean cooldownSet, final boolean cooldownForbidden, final boolean optional) {

		if (!ports.contains(port)) {
			throw new IllegalArgumentException("IPort was not created by this builder");
		}
		if (!timeWindows.contains(window)) {
			throw new IllegalArgumentException("ITimeWindow was not created by this builder");
		}

		final LoadSlot slot = new LoadSlot();

		slot.setCooldownForbidden(cooldownForbidden);
		slot.setCooldownSet(cooldownSet);
		final ISequenceElement element = configureLoadOption(slot, id, port, window, minVolumeInM3, maxVolumeInM3, loadContract, cargoCVValue, optional);

		elementDurationsProvider.setElementDuration(element, durationHours);

		return slot;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public ILoadOption createDESPurchaseLoadSlot(final String id, IPort port, final ITimeWindow window, final long minVolume, final long maxVolume, final ILoadPriceCalculator priceCalculator,
			final int cargoCVValue, final boolean slotIsOptional) {

		if (port == null) {
			port = ANYWHERE;
		}

		final LoadOption slot = new LoadOption();
		final ISequenceElement element = configureLoadOption(slot, id, port, window, minVolume, maxVolume, priceCalculator, cargoCVValue, slotIsOptional);

		unshippedElements.add(element);

		createVirtualVessel(element, VesselInstanceType.DES_PURCHASE);

		return slot;
	}

	private ISequenceElement configureLoadOption(final LoadOption slot, final String id, final IPort port, final ITimeWindow window, final long minVolumeInM3, final long maxVolumeInM3,
			final ILoadPriceCalculator priceCalculator, final int cargoCVValue, final boolean optional) {
		slot.setId(id);
		slot.setPort(port);
		slot.setTimeWindow(window);
		slot.setMinLoadVolume(minVolumeInM3);
		slot.setMaxLoadVolume(maxVolumeInM3);
		// slot.setPurchasePriceCurve(pricePerMMBTu);
		slot.setLoadPriceCalculator(priceCalculator);
		slot.setCargoCVValue(cargoCVValue);

		loadSlots.add(slot);

		// Create a sequence element against this load slot
		final SequenceElement element = new SequenceElement(indexingContext);
		element.setName(id + "-" + port.getName());

		optionalElements.setOptional(element, optional);

		sequenceElements.add(element);

		// Register the port with the element
		portProvider.setPortForElement(port, element);

		portTypeProvider.setPortType(element, PortType.Load);

		portSlotsProvider.setPortSlot(element, slot);

		timeWindowProvider.setTimeWindows(element, Collections.singletonList(window));

		addSlotToVolumeConstraints(slot);

		calculatorProvider.addLoadPriceCalculator(priceCalculator);

		return element;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public IDischargeOption createFOBSaleDischargeSlot(final String id, IPort port, final ITimeWindow window, final long minVolume, final long maxVolume, final long minCvValue, final long maxCvValue,
			final ISalesPriceCalculator priceCalculator, final boolean slotIsOptional) {

		if (port == null) {
			port = ANYWHERE;
		}

		final DischargeOption slot = new DischargeOption();

		final ISequenceElement element = configureDischargeOption(slot, id, port, window, minVolume, maxVolume, minCvValue, maxCvValue, priceCalculator, slotIsOptional);

		unshippedElements.add(element);

		createVirtualVessel(element, VesselInstanceType.FOB_SALE);

		return slot;
	}

	private ISequenceElement configureDischargeOption(final DischargeOption slot, final String id, final IPort port, final ITimeWindow window, final long minVolumeInM3, final long maxVolumeInM3,
			final long minCvValue, final long maxCvValue, final ISalesPriceCalculator priceCalculator, final boolean optional) {
		slot.setId(id);
		slot.setPort(port);
		slot.setTimeWindow(window);
		slot.setMinDischargeVolume(minVolumeInM3);
		slot.setMaxDischargeVolume(maxVolumeInM3);
		slot.setMinCvValue(minCvValue);
		slot.setMaxCvValue(maxCvValue);
		slot.setDischargePriceCalculator(priceCalculator);

		dischargeSlots.add(slot);

		// Create a sequence element against this discharge slot
		final SequenceElement element = new SequenceElement(indexingContext);
		element.setName(id + "-" + port.getName());

		optionalElements.setOptional(element, optional);
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
	 * @since 2.0
	 */
	@Override
	public IDischargeSlot createDischargeSlot(final String id, final IPort port, final ITimeWindow window, final long minVolumeInM3, final long maxVolumeInM3, final long minCvValue,
			final long maxCvValue, final ISalesPriceCalculator pricePerMMBTu, final int durationHours, final boolean optional) {

		if (!ports.contains(port)) {
			throw new IllegalArgumentException("IPort was not created by this builder");
		}
		if (!timeWindows.contains(window)) {
			throw new IllegalArgumentException("ITimeWindow was not created by this builder");
		}

		final DischargeSlot slot = new DischargeSlot();

		final ISequenceElement element = configureDischargeOption(slot, id, port, window, minVolumeInM3, maxVolumeInM3, minCvValue, maxCvValue, pricePerMMBTu, optional);

		elementDurationsProvider.setElementDuration(element, durationHours);

		return slot;
	}

	@Override
	public ICargo createCargo(final boolean allowRewiring, final IPortSlot... slots) {
		return createCargo(Lists.newArrayList(slots), allowRewiring);
	}

	@Override
	public ICargo createCargo(final Collection<IPortSlot> slots, final boolean allowRewiring) {

		final Cargo cargo = new Cargo(new ArrayList<IPortSlot>(slots));
		cargoes.add(cargo);

		// Fix slot pairing if we disallow re-wiring or this is a complex cargo (more than just one load and one discharge)
		if (!allowRewiring || slots.size() > 2) {

			IPortSlot prevSlot = null;
			for (final IPortSlot slot : slots) {
				if (prevSlot != null) {
					constrainSlotAdjacency(prevSlot, slot);
				}
				prevSlot = slot;
			}
		}

		return cargo;
	}

	@Override
	public IPort createPort(final String name, final boolean arriveCold, final ICooldownPriceCalculator cooldownPriceCalculator) {

		final Port port = new Port(indexingContext);
		buildPort(port, name, arriveCold, cooldownPriceCalculator);
		return port;
	}

	/**
	 * Create the return elements for each port/resource combination
	 */
	private void createReturnElements() {
		for (final IResource resource : resources) {
			for (final IPort port : ports) {
				returnElementProvider.setReturnElement(resource, port, createReturnElement(resource, port));
			}
		}
	}

	private ISequenceElement createReturnElement(final IResource resource, final IPort port) {
		final String name = "return-to-" + port.getName();
		final EndPortSlot slot = new EndPortSlot(name, port, null);
		final SequenceElement element = new SequenceElement(indexingContext, "return-to-" + port.getName());

		// set element duration to 1 hour, just so it's visible on the chart
		elementDurationsProvider.setElementDuration(element, 1);

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
			if (vesselProvider.getVessel(resource).getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER)) {
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
	public IXYPort createPort(final String name, final boolean arriveCold, final ICooldownPriceCalculator cooldownPriceCalculator, final float x, final float y) {

		final XYPort port = new XYPort(indexingContext);
		buildPort(port, name, arriveCold, cooldownPriceCalculator);
		port.setX(x);
		port.setY(y);

		return port;
	}

	/**
	 * Method to set common properties etc for {@link Port} implementations.
	 * 
	 * @param port
	 * @param name
	 * @param arriveCold
	 * @param cooldownPriceCalculator
	 */
	private void buildPort(final Port port, final String name, final boolean arriveCold, final ICooldownPriceCalculator cooldownPriceCalculator) {

		port.setName(name);
		port.setShouldVesselsArriveCold(arriveCold);
		port.setCooldownPriceCalculator(cooldownPriceCalculator);
		ports.add(port);

		if (ANYWHERE != null) {
			setPortToPortDistance(port, ANYWHERE, IMultiMatrixProvider.Default_Key, 0);
			setPortToPortDistance(ANYWHERE, port, IMultiMatrixProvider.Default_Key, 0);
		}

		// travel time from A to A should be zero, right?
		this.setPortToPortDistance(port, port, IMultiMatrixProvider.Default_Key, 0);

		calculatorProvider.addCooldownPriceCalculator(cooldownPriceCalculator);
	}

	@Override
	public ITimeWindow createTimeWindow(final int start, final int end) {

		if (start > end) {
			throw new IllegalArgumentException("Start time is greater than end time!");
		}
		
		final TimeWindow window = new TimeWindow(start, end);

		if (end > endOfLatestWindow) {
			endOfLatestWindow = end;
		}

		timeWindows.add(window);
		return window;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public IVessel createVessel(final String name, final IVesselClass vesselClass, final ICurve hourlyCharterInRate, final IStartEndRequirement start, final IStartEndRequirement end,
			final long heelLimit, final int heelCVValue, final int heelUnitPrice) {
		return this.createVessel(name, vesselClass, hourlyCharterInRate, VesselInstanceType.FLEET, start, end, heelLimit, heelCVValue, heelUnitPrice);
	}

	/**
	 * Create several spot vessels (see also {@code createSpotVessel}), named like namePrefix-1, namePrefix-2, etc
	 * 
	 * @param namePrefix
	 * @param vesselClass
	 * @param count
	 * @return
	 * @since 2.0
	 */
	@Override
	public List<IVessel> createSpotVessels(final String namePrefix, final IVesselClass vesselClass, final int count, final ICurve hourlyCharterPrice) {
		final List<IVessel> answer = new ArrayList<IVessel>(count);
		for (int i = 0; i < count; i++) {
			answer.add(createSpotVessel(namePrefix + "-" + (i + 1), vesselClass, hourlyCharterPrice));
		}
		return answer;
	}

	/**
	 * Create a spot charter vessel with no fixed start/end requirements and vessel instance type SPOT_CHARTER
	 * 
	 * @param name
	 * @param vesselClass
	 * @return the spot vessel
	 * @since 2.0
	 */
	@Override
	public IVessel createSpotVessel(final String name, final IVesselClass vesselClass, final ICurve hourlyCharterInPrice) {
		final IStartEndRequirement start = createStartEndRequirement();
		final IStartEndRequirement end = createStartEndRequirement();

		return createVessel(name, vesselClass, hourlyCharterInPrice, VesselInstanceType.SPOT_CHARTER, start, end, 0, 0, 0);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public IVessel createVessel(final String name, final IVesselClass vesselClass, final ICurve hourlyCharterInRate, final VesselInstanceType vesselInstanceType, final IStartEndRequirement start,
			final IStartEndRequirement end, final long heelLimit, final int heelCVValue, final int heelUnitPrice) {

		if (!vesselClasses.contains(vesselClass)) {
			throw new IllegalArgumentException("IVesselClass was not created using this builder");
		}

		// if (!ports.contains(startPort)) {
		// throw new IllegalArgumentException(
		// "Start IPort was not created using this builder");
		// }

		// if (!ports.contains(endPort)) {
		// throw new IllegalArgumentException(
		// "End IPort was not created using this builder");
		// }

		final Vessel vessel = new Vessel(indexingContext);
		vessel.setName(name);
		vessel.setVesselClass(vesselClass);

		vessel.setVesselInstanceType(vesselInstanceType);

		vessel.setHourlyCharterInPrice(hourlyCharterInRate);

		vessels.add(vessel);
		if (vesselInstanceType != VesselInstanceType.DES_PURCHASE && vesselInstanceType != VesselInstanceType.FOB_SALE) {
			realVessels.add(vessel);
		}

		final IResource resource = new Resource(indexingContext, name);
		resources.add(resource);

		// Register with provider
		vesselProvider.setVesselResource(resource, vessel);

		// If no time requirement is specified then the time window is at the
		// very start of the job
		final ITimeWindow startWindow = start.hasTimeRequirement() ? start.getTimeWindow() : createTimeWindow(0, 0);

		final StartPortSlot startSlot = new StartPortSlot(heelLimit, heelCVValue, heelUnitPrice);
		startSlot.setId("start-" + name);
		startSlot.setPort((start.hasPortRequirement() && start.getLocation() != null) ? start.getLocation() : ANYWHERE);

		startSlot.setTimeWindow(startWindow);

		final EndPortSlot endSlot = new EndPortSlot();
		endSlot.setId("end-" + name);
		endSlot.setPort((end.hasPortRequirement() && end.getLocation() != null) ? end.getLocation() : ANYWHERE);

		// Create start/end sequence elements for this route
		final SequenceElement startElement = new SequenceElement(indexingContext);
		final SequenceElement endElement = new SequenceElement(indexingContext);

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

		// TODO specify initial vessel state?

		return vessel;
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(final IPort fixedPort) {
		return new StartEndRequirement(fixedPort, null, true, null);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(final Collection<IPort> portSet) {
		return new StartEndRequirement(null, portSet, true, null);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(final ITimeWindow timeWindow) {
		return new StartEndRequirement(ANYWHERE, null, false, timeWindow);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(final IPort fixedPort, final ITimeWindow timeWindow) {
		return new StartEndRequirement(fixedPort, null, true, timeWindow);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(final Collection<IPort> portSet, final ITimeWindow timeWindow) {
		return new StartEndRequirement(null, portSet, true, timeWindow);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement() {
		return new StartEndRequirement(ANYWHERE, null, false, null);
	}

	@Override
	public void setPortToPortDistance(final IPort from, final IPort to, final String route, final int distance) {

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
	 * @since 2.0
	 */
	@Override
	public void setVesselClassRouteCost(final String route, final IVesselClass vesselClass, final VesselState state, final long tollPrice) {
		routeCostProvider.setRouteCost(route, vesselClass, state, tollPrice);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setDefaultRouteCost(final String route, final long defaultPrice) {
		routeCostProvider.setDefaultRouteCost(route, defaultPrice);
	}

	@Override
	public void setVesselClassRouteFuel(final String name, final IVesselClass vc, final VesselState vesselState, final long baseFuelInScaledMT, final long nboRateInScaledM3) {
		routeCostProvider.setRouteFuel(name, vc, vesselState, baseFuelInScaledMT, nboRateInScaledM3);
	}

	@Override
	public void setVesselClassRouteTransitTime(final String name, final IVesselClass vc, final int transitTimeInHours) {
		routeCostProvider.setRouteTransitTime(name, vc, transitTimeInHours);
	}

	@Override
	public void setElementDurations(final ISequenceElement element, final IResource resource, final int duration) {
		elementDurationsProvider.setElementDuration(element, resource, duration);
	}

	@Override
	public IOptimisationData getOptimisationData() {

		final boolean enableCargoShorts = false;

		if (enableCargoShorts) {
			// // TODO: Look up appropriate definition
			final IVesselClass cargoShortsClass = vesselClasses.get(1);// createVesselClass("short-class", OptimiserUnitConvertor.convertToInternalSpeed(18.0),
			shortCargoVessel = createVessel("short-vessel", cargoShortsClass, new ConstantValueCurve(200000), VesselInstanceType.CARGO_SHORTS, createStartEndRequirement(),
					createStartEndRequirement(), 0, 0, 0);
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
		/**
		 * The shortest time which the slowest vessel in the fleet can take to get from the latest discharge back to the load for that discharge.
		 */
		final int maxFastReturnTime;
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

			maxFastReturnTime = Calculator.getTimeFromSpeedDistance(slowestMaxSpeed, returnDistance);
		} else {
			maxFastReturnTime = 0;
			latestDischarge = 0;
		}
		final int latestTime = Math.max(endOfLatestWindow + (24 * 10), maxFastReturnTime + latestDischarge);
		for (final Pair<ISequenceElement, PortSlot> elementAndSlot : endSlots) {
			final ITimeWindow endWindow = createTimeWindow(latestTime, latestTime + (35 * 24));
			elementAndSlot.getSecond().setTimeWindow(endWindow);
			timeWindowProvider.setTimeWindows(elementAndSlot.getFirst(), Collections.singletonList(endWindow));
		}

		// Create charter out elements
		buildVesselEvents();

		// configure constraints
		applyAdjacencyConstraints();
		applyVesselRestrictionConstraints();

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

		data.addDataComponentProvider(SchedulerConstants.DCP_discountCurveProvider, discountCurveProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_alternativeElementProvider, alternativeElementProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_vesselProvider, vesselProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_timeWindowProvider, timeWindowProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_portDistanceProvider, portDistanceProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_orderedElementsProvider, orderedSequenceElementsEditor);
		data.addDataComponentProvider(SchedulerConstants.DCP_portProvider, portProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, portSlotsProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_elementDurationsProvider, elementDurationsProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, portTypeProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_portCostProvider, portCostProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_portCVProvider, portCVProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_resourceAllocationProvider, resourceAllocationProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, startEndRequirementProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_portExclusionProvider, portExclusionProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_returnElementProvider, returnElementProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_routePriceProvider, routeCostProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_totalVolumeLimitProvider, totalVolumeLimits);

		data.addDataComponentProvider(SchedulerConstants.DCP_calculatorProvider, calculatorProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_optionalElementsProvider, optionalElements);

		data.addDataComponentProvider(SchedulerConstants.DCP_slotGroupProvider, slotGroupCountProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_virtualVesselSlotProvider, virtualVesselSlotProviderEditor);

		data.addDataComponentProvider(SchedulerConstants.DCP_dateKeyProvider, dateKeyProviderEditor);

		data.addDataComponentProvider(SchedulerConstants.DCP_shortCargoReturnElementProvider, shortCargoReturnElementProviderEditor);

		for (final IBuilderExtension extension : extensions) {
			for (final Pair<String, IDataComponentProvider> provider : extension.createDataComponentProviders(data)) {
				data.addDataComponentProvider(provider.getFirst(), provider.getSecond());
			}
		}

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

	private IVessel createVirtualVessel(final ISequenceElement element, final VesselInstanceType type) {
		assert type == VesselInstanceType.DES_PURCHASE || type == VesselInstanceType.FOB_SALE;
		// create a new resource for each of these guys, and bind them to their resources
		final IVessel virtualVessel = createVessel("virtual-" + element.getName(), virtualClass, ZeroCurve.getInstance(), type, createStartEndRequirement(), createStartEndRequirement(), 0l, 0, 0);
		// Bind every slot to its vessel
		constrainSlotToVessels(portSlotsProvider.getPortSlot(element), Collections.singleton(virtualVessel));

		virtualVesselMap.put(element, virtualVessel);
		virtualVesselSlotProviderEditor.setVesselForElement(virtualVessel, element);

		return virtualVessel;
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

		// TODO: Make sure we haven't passed any of these by ref into the
		// IOptData object
		// Passed into IOptData - resources.clear();
		// Passed into IOptData - sequenceElements.clear();
		vessels.clear();
		cargoes.clear();
		ports.clear();

		for (final IBuilderExtension extension : extensions) {
			extension.dispose();
		}

		extensions.clear();
		// TODO: Null provider refs
	}

	/**
	 * @since 2.0
	 */
	@Override
	public IVesselClass createVesselClass(final String name, final int minSpeed, final int maxSpeed, final long capacityInM3, final long minHeelInM3, final int baseFuelUnitPricePerMT,
			final int baseFuelEquivalenceInM3TOMT, final int pilotLightRate, final int warmupTimeHours, final long cooldownVolumeM3) {

		final VesselClass vesselClass = new VesselClass();
		vesselClass.setName(name);

		vesselClass.setMinSpeed(minSpeed);
		vesselClass.setMaxSpeed(maxSpeed);

		vesselClass.setCargoCapacity(capacityInM3);
		vesselClass.setMinHeel(minHeelInM3);

		vesselClass.setBaseFuelUnitPrice(baseFuelUnitPricePerMT);
		vesselClass.setBaseFuelConversionFactor(baseFuelEquivalenceInM3TOMT);

		vesselClass.setWarmupTime(warmupTimeHours);
		vesselClass.setCooldownVolume(cooldownVolumeM3);

		vesselClass.setPilotLightRate(pilotLightRate);

		vesselClasses.add(vesselClass);

		return vesselClass;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setVesselClassStateParameters(final IVesselClass vesselClass, final VesselState state, final int nboRateInM3PerHour, final int idleNBORateInM3PerHour,
			final int idleConsumptionRateInMTPerHour, final IConsumptionRateCalculator consumptionRateCalculatorInMTPerHour, final int nboSpeed) {

		if (!vesselClasses.contains(vesselClass)) {
			throw new IllegalArgumentException("IVesselClass was not created using this builder");
		}

		// Check instance is the same as that used in createVesselClass(..)
		if (!(vesselClass instanceof VesselClass)) {
			throw new IllegalArgumentException("Expected instance of " + VesselClass.class.getCanonicalName());
		}

		final VesselClass vc = (VesselClass) vesselClass;

		vc.setNBORate(state, nboRateInM3PerHour);
		vc.setIdleNBORate(state, idleNBORateInM3PerHour);
		vc.setIdleConsumptionRate(state, idleConsumptionRateInMTPerHour);
		vc.setConsumptionRate(state, consumptionRateCalculatorInMTPerHour);
		vc.setMinNBOSpeed(state, nboSpeed);

	}

	/**
	 * Like the other setVesselClassStateParameters, but the NBO speed is calculated automatically.
	 * 
	 * @param vc
	 * @param state
	 * @param nboRateInM3PerHour
	 * @param idleNBORateInM3PerHour
	 * @param idleConsumptionRateInMTPerHour
	 * @param consumptionCalculatorInMTPerHour
	 * @since 2.0
	 */

	@Override
	public void setVesselClassStateParameters(final IVesselClass vc, final VesselState state, final int nboRateInM3PerHour, final int idleNBORateInM3PerHour, final int idleConsumptionRateInMTPerHour,
			final IConsumptionRateCalculator consumptionCalculatorInMTPerHour) {

		// Convert rate to MT equivalent per hour
		final int nboRateInMTPerHour = (int) Calculator.convertM3ToMT(nboRateInM3PerHour, vc.getBaseFuelConversionFactor());

		final int nboSpeed = consumptionCalculatorInMTPerHour.getSpeed(nboRateInMTPerHour);

		this.setVesselClassStateParameters(vc, state, nboRateInM3PerHour, idleNBORateInM3PerHour, idleConsumptionRateInMTPerHour, consumptionCalculatorInMTPerHour, nboSpeed);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setVesselClassPortTypeParameters(final IVesselClass vc, final PortType portType, final int inPortConsumptionRateInMTPerHour) {

		((VesselClass) vc).setInPortConsumptionRate(portType, inPortConsumptionRateInMTPerHour);
	}

	@Override
	public void setVesselClassInaccessiblePorts(final IVesselClass vc, final Set<IPort> inaccessiblePorts) {
		this.portExclusionProvider.setExcludedPorts(vc, inaccessiblePorts);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public IVesselEventPortSlot createCharterOutEvent(final String id, final ITimeWindow arrival, final IPort fromPort, final IPort toPort, final int durationHours, final long maxHeelOut,
			final int heelCVValue, final int heelUnitPrice, final long hireCost, final long repositioning) {
		return createVesselEvent(id, PortType.CharterOut, arrival, fromPort, toPort, durationHours, maxHeelOut, heelCVValue, heelUnitPrice, hireCost, repositioning);
	}

	@Override
	public IVesselEventPortSlot createDrydockEvent(final String id, final ITimeWindow arrival, final IPort port, final int durationHours) {
		return createVesselEvent(id, PortType.DryDock, arrival, port, port, durationHours, 0, 0, 0, 0, 0);
	}

	@Override
	public IVesselEventPortSlot createMaintenanceEvent(final String id, final ITimeWindow arrival, final IPort port, final int durationHours) {
		return createVesselEvent(id, PortType.Maintenance, arrival, port, port, durationHours, 0, 0, 0, 0, 0);
	}

	/**
	 * @since 2.0
	 */
	public IVesselEventPortSlot createVesselEvent(final String id, final PortType portType, final ITimeWindow arrival, final IPort fromPort, final IPort toPort, final int durationHours,
			final long maxHeelOut, final int heelCVValue, final int heelUnitPrice, final long hireCost, final long repositioning) {
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
		event.setHireCost(hireCost);
		event.setRepositioning(repositioning);

		final VesselEventPortSlot slot = new VesselEventPortSlot(id, event.getEndPort(), event.getTimeWindow(), event);

		vesselEvents.add(slot);
		slot.setPortType(portType);
		slotVesselRestrictions.put(slot, new HashSet<IVessel>());
		slotVesselClassRestrictions.put(slot, new HashSet<IVesselClass>());
		return slot;
	}

	@Override
	public void addVesselEventVessel(final IVesselEventPortSlot charterOut, final IVessel vessel) {
		if (!vessels.contains(vessel)) {
			throw new IllegalArgumentException("IVessel was not created using this builder");
		}
		if (!vesselEvents.contains(charterOut)) {
			throw new IllegalArgumentException("ICharterOut was not created using this builder");
		}
		slotVesselRestrictions.get(charterOut).add(vessel);
	}

	@Override
	public void addVesselEventVesselClass(final IVesselEventPortSlot charterOut, final IVesselClass vesselClass) {
		if (!vesselClasses.contains(vesselClass)) {
			throw new IllegalArgumentException("IVesselClass was not created using this builder");
		}
		if (!vesselEvents.contains(charterOut)) {
			throw new IllegalArgumentException("ICharterOut was not created using this builder");
		}
		slotVesselClassRestrictions.get(charterOut).add(vesselClass);
	}

	protected void buildVesselEvents() {
		// int i = 0;

		for (final IVesselEventPortSlot slot : vesselEvents) {
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
				constrainSlotToVessels(startSlot, slotVesselRestrictions.get(slot));

				constrainSlotToVesselClasses(redirectSlot, slotVesselClassRestrictions.get(slot));
				constrainSlotToVessels(redirectSlot, slotVesselRestrictions.get(slot));

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

			// i++;
		}
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
	public void setCargoVesselRestriction(final Collection<IPortSlot> cargoSlots, final Set<IVessel> vessels) {

		for (final IPortSlot slot : cargoSlots) {
			constrainSlotToVessels(slot, vessels);

		}
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
		restrictedSlots.addAll(slotVesselRestrictions.keySet());
		restrictedSlots.addAll(slotVesselClassRestrictions.keySet());

		for (final IPortSlot slot : restrictedSlots) {

			Set<IVessel> allowedVessels = slotVesselRestrictions.get(slot);
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

			for (final IVessel vessel : vessels) {
				if (allowedVessels.contains(vessel) || allowedVesselClasses.contains(vessel.getVesselClass())) {
					/*
					 * If this is a vessel event, and it's a spot vessel, don't allow it. In future this should probably be handled as a separate kind of constraint (a vessel instance type
					 * restriction, saying slot X can only go on vessels of types Y and Z)
					 */
					if ((slot instanceof IVesselEventPortSlot) && (vessel.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER)) {
						continue;
					}
					allowedResources.add(vesselProvider.getResource(vessel));
				}

				if (shortCargoVessel != null) {
					// Cargo shorts only applies to load and discharge slots
					if (loadSlots.contains(slot) || dischargeSlots.contains(slot)) {
						allowedResources.add(vesselProvider.getResource(shortCargoVessel));
					}
				}
			}
			resourceAllocationProvider.setAllowedResources(portSlotsProvider.getElement(slot), allowedResources);
		}

		// Finally, allow any other slot to be linked to any non-virtual vessel. This helps the optional move generator.
		final HashSet<IResource> allowedResources = new HashSet<IResource>();

		for (final IVessel vessel : vessels) {
			// Skip virtual vessels
			if (virtualVesselMap.values().contains(vessel)) {
				continue;
			}
			allowedResources.add(vesselProvider.getResource(vessel));
		}

		final HashSet<IPortSlot> unrestrictedSlots = new HashSet<IPortSlot>();
		unrestrictedSlots.addAll(loadSlots);
		unrestrictedSlots.addAll(dischargeSlots);
		unrestrictedSlots.addAll(vesselEvents);
		unrestrictedSlots.removeAll(unshippedElements);
		unrestrictedSlots.removeAll(restrictedSlots);
		for (final IPortSlot slot : unrestrictedSlots) {
			resourceAllocationProvider.setAllowedResources(portSlotsProvider.getElement(slot), allowedResources);
		}
	}

	@Override
	public void constrainSlotToVessels(final IPortSlot slot, final Set<IVessel> vessels) {
		if ((vessels == null) || vessels.isEmpty()) {
			slotVesselRestrictions.remove(slot);
		} else {
			slotVesselRestrictions.put(slot, vessels);
		}
	}

	@Override
	public void constrainSlotToVesselClasses(final IPortSlot slot, final Set<IVesselClass> vesselClasses) {
		if ((vesselClasses == null) || vesselClasses.isEmpty()) {
			slotVesselClassRestrictions.remove(slot);
		} else {
			slotVesselClassRestrictions.put(slot, vesselClasses);
		}
	}

	@Override
	public void constrainSlotAdjacency(final IPortSlot firstSlot, final IPortSlot secondSlot) {
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
	public void setPortCost(final IPort port, final IVessel vessel, final PortType portType, final long cost) {
		portCostProvider.setPortCost(port, vessel, portType, cost);
	}

	@Override
	public void bindDischargeSlotsToDESPurchase(final ILoadOption desPurchase, final Collection<IPort> dischargePorts) {

		final ISequenceElement desElement = portSlotsProvider.getElement(desPurchase);

		// Look up virtual vessel
		final IVessel virtualVessel = virtualVesselMap.get(desElement);
		if (virtualVessel == null) {
			throw new IllegalArgumentException("DES Purchase is not linked to a virtual vesssel");
		}

		final List<ITimeWindow> tw1 = timeWindowProvider.getTimeWindows(portSlotsProvider.getElement(desPurchase));
		for (final IDischargeOption option : dischargeSlots) {

			if (option instanceof DischargeSlot) {

				if (dischargePorts.contains(option.getPort())) {
					// Get current allocation

					final List<ITimeWindow> tw2 = timeWindowProvider.getTimeWindows(portSlotsProvider.getElement(option));
					if (true || matchingWindows(tw1, tw2) || matchingWindows(tw2, tw1)) {

						Set<IVessel> set = slotVesselRestrictions.get(option);
						if (set == null || set.isEmpty()) {
							set = new HashSet<IVessel>(realVessels);
						}
						set.add(virtualVessel);

						constrainSlotToVessels(option, set);
					}
				}
			}

		}
	}

	@Override
	public void bindLoadSlotsToFOBSale(final IDischargeOption fobSale, final IPort loadPort) {

		final ISequenceElement desElement = portSlotsProvider.getElement(fobSale);

		// Look up virtual vessel
		final IVessel virtualVessel = virtualVesselMap.get(desElement);
		if (virtualVessel == null) {
			throw new IllegalArgumentException("FOB Sale is not linked to a virtual vesssel");
		}
		final List<ITimeWindow> tw1 = timeWindowProvider.getTimeWindows(portSlotsProvider.getElement(fobSale));

		for (final ILoadOption option : loadSlots) {

			if (option instanceof LoadSlot) {

				if (loadPort == option.getPort()) {
					// Get current allocation

					final List<ITimeWindow> tw2 = timeWindowProvider.getTimeWindows(portSlotsProvider.getElement(option));
					if (true || matchingWindows(tw1, tw2) || matchingWindows(tw2, tw1)) {
						Set<IVessel> set = slotVesselRestrictions.get(option);
						if (set == null || set.isEmpty()) {
							set = new HashSet<IVessel>(realVessels);
						}
						set.add(virtualVessel);

						constrainSlotToVessels(option, set);
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

		private static final ZeroCurve INSTANCE = new ZeroCurve();

		@Override
		public int getValueAtPoint(final int point) {
			return 0;
		}

		public static ZeroCurve getInstance() {
			return INSTANCE;
		}
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setEarliestDate(final Date earliestTime) {
		dateKeyProviderEditor.setTimeZero(earliestTime.getTime());
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void createCharterOutCurve(final IVesselClass vesselClass, final ICurve charterOutCurve, final int minDuration) {
		charterMarketProviderEditor.addCharterOutOption(vesselClass, charterOutCurve, minDuration);

	}

	/**
	 * @param slot
	 * @since 2.0
	 */
	@Override
	public void setSoftRequired(final IPortSlot slot) {
		final ISequenceElement element = portSlotsProvider.getElement(slot);
		optionalElements.setSoftRequired(element, true);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setPortCV(final IPort port, final int cv) {
		portCVProvider.setPortCV(port, cv);
	}

}
