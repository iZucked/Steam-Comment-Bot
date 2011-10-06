/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.HashMapElementDurationEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.OrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.impl.ResourceAllocationConstraintProvider;
import com.mmxlabs.optimiser.common.dcproviders.impl.TimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedElementDurationEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedOrderedSequenceElementsEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedTimeWindowEditor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.impl.OptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.IXYPortDistanceCalculator;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.Cargo;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
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
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISimpleLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.MarketPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.NetbackContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.ProfitSharingContract;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitEditor;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ArrayListCargoAllocationEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapDiscountCurveEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapRouteCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapStartEndRequirementEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.IndexedPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.IndexedPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.indexed.IndexedPortTypeEditor;

/**
 * Implementation of {@link ISchedulerBuilder}
 * 
 * @author Simon Goodall
 * 
 */
public final class SchedulerBuilder implements ISchedulerBuilder {

	private final IXYPortDistanceCalculator distanceProvider = new XYPortEuclideanDistanceCalculator();

	private final List<IResource> resources = new ArrayList<IResource>();

	private final List<ISequenceElement> sequenceElements = new ArrayList<ISequenceElement>();

	private final List<IVesselClass> vesselClasses = new LinkedList<IVesselClass>();

	private final List<IVessel> vessels = new LinkedList<IVessel>();

	private final List<ICargo> cargoes = new LinkedList<ICargo>();

	private final List<IPort> ports = new LinkedList<IPort>();

	private final IVesselProviderEditor vesselProvider;

	private final IPortProviderEditor<ISequenceElement> portProvider;

	private final IPortSlotProviderEditor<ISequenceElement> portSlotsProvider;

	private final IOrderedSequenceElementsDataComponentProviderEditor<ISequenceElement> orderedSequenceElementsEditor;

	private final ITimeWindowDataComponentProviderEditor<ISequenceElement> timeWindowProvider;

	private final IndexedMultiMatrixProvider<IPort, Integer> portDistanceProvider;

	private final IPortTypeProviderEditor<ISequenceElement> portTypeProvider;

	private final IElementDurationProviderEditor<ISequenceElement> elementDurationsProvider;

	private final IResourceAllocationConstraintDataComponentProviderEditor<ISequenceElement> resourceAllocationProvider;

	private final List<ILoadSlot> loadSlots = new LinkedList<ILoadSlot>();

	private final List<IDischargeSlot> dischargeSlots = new LinkedList<IDischargeSlot>();

	private final List<ITimeWindow> timeWindows = new LinkedList<ITimeWindow>();

	/**
	 * List of end slots, which need to be corrected in getOptimisationData to have the latest time in them
	 */
	private final List<Pair<ISequenceElement, PortSlot>> endSlots = new LinkedList<Pair<ISequenceElement, PortSlot>>();

	/**
	 * A "virtual" port which is zero distance from all other ports, to be used in cases where a vessel can be in any location. This can be replaced with a real location at a later date, after running
	 * an optimisation.
	 */
	private final IPort ANYWHERE;

	private final IStartEndRequirementProviderEditor<ISequenceElement> startEndRequirementProvider;

	/**
	 * A field for tracking the time at which the last time window closes
	 */
	private int endOfLatestWindow = 0;

	private final IPortExclusionProviderEditor portExclusionProvider;

	/**
	 * The slots for vessel events which have been generated; these are stored so that in {@link #buildVesselEvents()} they can have some extra post-processing done to set up any constraints
	 */
	private final List<VesselEventPortSlot> vesselEvents = new LinkedList<VesselEventPortSlot>();

	private final Map<IPortSlot, Set<IVessel>> slotVesselRestrictions = new HashMap<IPortSlot, Set<IVessel>>();

	private final Map<IPortSlot, Set<IVesselClass>> slotVesselClassRestrictions = new HashMap<IPortSlot, Set<IVesselClass>>();

	private final IReturnElementProviderEditor<ISequenceElement> returnElementProvider;

	private final HashMapRouteCostProviderEditor routeCostProvider;

	private final IIndexingContext indexingContext = new SimpleIndexingContext();

	/**
	 * For debug & timing purposes. Switches the indexing DCPs on or off.
	 */
	private static final boolean USE_INDEXED_DCPS = true;

	private final Map<IPort, List<TotalVolumeLimit>> loadLimits = new HashMap<IPort, List<TotalVolumeLimit>>();

	private final Map<IPort, List<TotalVolumeLimit>> dischargeLimits = new HashMap<IPort, List<TotalVolumeLimit>>();

	private final ITotalVolumeLimitEditor<ISequenceElement> totalVolumeLimits;

	private final IDiscountCurveProviderEditor discountCurveProvider = new HashMapDiscountCurveEditor(SchedulerConstants.DCP_discountCurveProvider);

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

	public SchedulerBuilder() {
		indexingContext.registerType(SequenceElement.class);
		indexingContext.registerType(Port.class);
		indexingContext.registerType(Resource.class);
		indexingContext.registerType(Vessel.class);

		vesselProvider = new HashMapVesselEditor(SchedulerConstants.DCP_vesselProvider);

		portDistanceProvider = new IndexedMultiMatrixProvider<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider);

		if (USE_INDEXED_DCPS) {
			portProvider = new IndexedPortEditor<ISequenceElement>(SchedulerConstants.DCP_portProvider);
			portSlotsProvider = new IndexedPortSlotEditor<ISequenceElement>(SchedulerConstants.DCP_portSlotsProvider);
			portTypeProvider = new IndexedPortTypeEditor<ISequenceElement>(SchedulerConstants.DCP_portTypeProvider);

			timeWindowProvider = new IndexedTimeWindowEditor<ISequenceElement>(SchedulerConstants.DCP_timeWindowProvider);
			orderedSequenceElementsEditor = new IndexedOrderedSequenceElementsEditor<ISequenceElement>(SchedulerConstants.DCP_orderedElementsProvider);

			elementDurationsProvider = new IndexedElementDurationEditor<ISequenceElement>(SchedulerConstants.DCP_elementDurationsProvider);

			// Create a default matrix entry
			portDistanceProvider.set(IMultiMatrixProvider.Default_Key, new IndexedMatrixEditor<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider, Integer.MAX_VALUE));
		} else {

			elementDurationsProvider = new HashMapElementDurationEditor<ISequenceElement>(SchedulerConstants.DCP_elementDurationsProvider);

			orderedSequenceElementsEditor = new OrderedSequenceElementsDataComponentProvider<ISequenceElement>(SchedulerConstants.DCP_orderedElementsProvider);

			timeWindowProvider = new TimeWindowDataComponentProvider(SchedulerConstants.DCP_timeWindowProvider);

			portTypeProvider = new HashMapPortTypeEditor<ISequenceElement>(SchedulerConstants.DCP_portTypeProvider);

			portSlotsProvider = new HashMapPortSlotEditor<ISequenceElement>(SchedulerConstants.DCP_portSlotsProvider);

			portProvider = new HashMapPortEditor(SchedulerConstants.DCP_portProvider);

			// Create a default matrix entry
			portDistanceProvider.set(IMultiMatrixProvider.Default_Key, new HashMapMatrixProvider<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider, Integer.MAX_VALUE));
		}

		totalVolumeLimits = new ArrayListCargoAllocationEditor<ISequenceElement>(SchedulerConstants.DCP_totalVolumeLimitProvider);

		resourceAllocationProvider = new ResourceAllocationConstraintProvider<ISequenceElement>(SchedulerConstants.DCP_resourceAllocationProvider);

		startEndRequirementProvider = new HashMapStartEndRequirementEditor<ISequenceElement>(SchedulerConstants.DCP_startEndRequirementProvider);

		portExclusionProvider = new HashMapPortExclusionProvider(SchedulerConstants.DCP_portExclusionProvider);

		returnElementProvider = new HashMapReturnElementProviderEditor<ISequenceElement>(SchedulerConstants.DCP_returnElementProvider);

		routeCostProvider = new HashMapRouteCostProviderEditor(SchedulerConstants.DCP_routePriceProvider, IMultiMatrixProvider.Default_Key);

		// Create the anywhere port
		ANYWHERE = createPort("ANYWHERE", false, new FixedPriceContract(0));

	}

	@Override
	public ILoadSlot createLoadSlot(final String id, final IPort port, final ITimeWindow window, final long minVolumeInM3, final long maxVolumeInM3, final ILoadPriceCalculator loadContract,
			final int cargoCVValue, final int durationHours, boolean cooldownSet, boolean cooldownForbidden) {

		if (!ports.contains(port)) {
			throw new IllegalArgumentException("IPort was not created by this builder");
		}
		if (!timeWindows.contains(window)) {
			throw new IllegalArgumentException("ITimeWindow was not created by this builder");
		}

		final LoadSlot slot = new LoadSlot();
		slot.setId(id);
		slot.setPort(port);
		slot.setTimeWindow(window);
		slot.setMinLoadVolume(minVolumeInM3);
		slot.setMaxLoadVolume(maxVolumeInM3);
		// slot.setPurchasePriceCurve(pricePerMMBTu);
		slot.setLoadPriceCalculator(loadContract);
		slot.setCargoCVValue(cargoCVValue);
		slot.setCooldownForbidden(cooldownForbidden);
		slot.setCooldownSet(cooldownSet);

		loadSlots.add(slot);

		// Create a sequence element against this load slot
		final SequenceElement element = new SequenceElement(indexingContext);
		element.setName(id + "-" + port.getName());
		element.setPortSlot(slot);

		sequenceElements.add(element);

		// Register the port with the element
		portProvider.setPortForElement(port, element);

		portTypeProvider.setPortType(element, PortType.Load);

		portSlotsProvider.setPortSlot(element, slot);

		timeWindowProvider.setTimeWindows(element, Collections.singletonList(window));

		elementDurationsProvider.setElementDuration(element, durationHours);

		addSlotToVolumeConstraints(slot);

		return slot;
	}

	@Override
	public IDischargeSlot createDischargeSlot(final String id, final IPort port, final ITimeWindow window, final long minVolumeInM3, final long maxVolumeInM3, final ICurve pricePerMMBTu,
			final int durationHours) {

		if (!ports.contains(port)) {
			throw new IllegalArgumentException("IPort was not created by this builder");
		}
		if (!timeWindows.contains(window)) {
			throw new IllegalArgumentException("ITimeWindow was not created by this builder");
		}

		final DischargeSlot slot = new DischargeSlot();
		slot.setId(id);
		slot.setPort(port);
		slot.setTimeWindow(window);
		slot.setMinDischargeVolume(minVolumeInM3);
		slot.setMaxDischargeVolume(maxVolumeInM3);
		slot.setSalesPriceCurve(pricePerMMBTu);

		dischargeSlots.add(slot);

		// Create a sequence element against this discharge slot
		final SequenceElement element = new SequenceElement(indexingContext);
		element.setPortSlot(slot);
		element.setName(id + "-" + port.getName());

		sequenceElements.add(element);

		// Register the port with the element
		portProvider.setPortForElement(port, element);

		portSlotsProvider.setPortSlot(element, slot);

		portTypeProvider.setPortType(element, PortType.Discharge);

		timeWindowProvider.setTimeWindows(element, Collections.singletonList(window));

		elementDurationsProvider.setElementDuration(element, durationHours);

		addSlotToVolumeConstraints(slot);

		return slot;
	}

	@Override
	public ICargo createCargo(final String id, final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot) {

		if (!loadSlots.contains(loadSlot)) {
			throw new IllegalArgumentException("ILoadSlot was not created by this builder");
		}
		if (!dischargeSlots.contains(dischargeSlot)) {
			throw new IllegalArgumentException("IDischargeSlot was not created by this builder");
		}

		final Cargo cargo = new Cargo();
		cargo.setId(id);
		cargo.setLoadSlot(loadSlot);
		cargo.setDischargeSlot(dischargeSlot);

		cargoes.add(cargo);

		final ISequenceElement loadElement = portSlotsProvider.getElement(loadSlot);
		final ISequenceElement dischargeElement = portSlotsProvider.getElement(dischargeSlot);

		constrainSlotAdjacency(loadSlot, dischargeSlot);

		// // Set fixed visit order
		// orderedSequenceElementsEditor.setElementOrder(loadElement,
		// dischargeElement);

		return cargo;
	}

	@Override
	public IPort createPort(final String name, final boolean arriveCold, final ISimpleLoadPriceCalculator cooldownPriceCalculator) {

		final Port port = new Port(indexingContext);
		port.setName(name);

		port.setCooldownPriceCalculator(cooldownPriceCalculator);

		port.setShouldVesselsArriveCold(arriveCold);

		ports.add(port);

		/*
		 * ANYWHERE is not present in the /real/ distance matrix, so we must set its distances here.
		 */
		if (ANYWHERE != null) {
			setPortToPortDistance(port, ANYWHERE, IMultiMatrixProvider.Default_Key, 0);
			setPortToPortDistance(ANYWHERE, port, IMultiMatrixProvider.Default_Key, 0);
		}

		// travel time from A to A should be zero, right?
		this.setPortToPortDistance(port, port, IMultiMatrixProvider.Default_Key, 0);

		// create the return elements to return to this port using the ELSM

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
		final SequenceElement element = new SequenceElement(indexingContext, "return-to-" + port.getName(), slot);

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
	public IXYPort createPort(final String name, final boolean arriveCold, final ISimpleLoadPriceCalculator cooldownPriceCalculator, final float x, final float y) {

		final XYPort port = new XYPort(indexingContext);
		port.setName(name);
		port.setX(x);
		port.setY(y);
		port.setShouldVesselsArriveCold(arriveCold);
		port.setCooldownPriceCalculator(cooldownPriceCalculator);
		ports.add(port);

		if (ANYWHERE != null) {
			setPortToPortDistance(port, ANYWHERE, IMultiMatrixProvider.Default_Key, 0);
			setPortToPortDistance(ANYWHERE, port, IMultiMatrixProvider.Default_Key, 0);
		}

		return port;
	}

	@Override
	public ITimeWindow createTimeWindow(final int start, final int end) {

		final TimeWindow window = new TimeWindow(start, end);

		if (end > endOfLatestWindow) {
			endOfLatestWindow = end;
		}

		timeWindows.add(window);
		return window;
	}

	@Override
	public IVessel createVessel(final String name, final IVesselClass vesselClass, int hourlyCharterOutRate, final IStartEndRequirement start, final IStartEndRequirement end, final long heelLimit,
			final int heelCVValue, final int heelUnitPrice) {
		return this.createVessel(name, vesselClass, hourlyCharterOutRate, VesselInstanceType.FLEET, start, end, heelLimit, heelCVValue, heelUnitPrice);
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
	public List<IVessel> createSpotVessels(final String namePrefix, final IVesselClass vesselClass, final int count) {
		final List<IVessel> answer = new ArrayList<IVessel>(count);
		for (int i = 0; i < count; i++) {
			answer.add(createSpotVessel(namePrefix + "-" + (i + 1), vesselClass));
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
	public IVessel createSpotVessel(final String name, final IVesselClass vesselClass) {
		final IStartEndRequirement start = createStartEndRequirement();
		final IStartEndRequirement end = createStartEndRequirement();

		return createVessel(name, vesselClass, 0, VesselInstanceType.SPOT_CHARTER, start, end, 0, 0, 0);
	}

	@Override
	public IVessel createVessel(final String name, final IVesselClass vesselClass, int hourlyCharterOutRate, final VesselInstanceType vesselInstanceType, final IStartEndRequirement start,
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

		vessel.setHourlyCharterOutPrice(hourlyCharterOutRate);

		vessels.add(vessel);

		final IResource resource = new Resource(indexingContext, name);
		resources.add(resource);

		// Register with provider
		vesselProvider.setVesselResource(resource, vessel);

		// If no time requirement is specified then the time window is at the
		// very start of the job
		final ITimeWindow startWindow = start.hasTimeRequirement() ? start.getTimeWindow() : createTimeWindow(0, 0);

		final StartPortSlot startSlot = new StartPortSlot(heelLimit, heelCVValue, heelUnitPrice);
		startSlot.setId("start-" + name);
		startSlot.setPort(start.hasPortRequirement() ? start.getLocation() : ANYWHERE);

		startSlot.setTimeWindow(startWindow);

		final EndPortSlot endSlot = new EndPortSlot();
		endSlot.setId("end-" + name);
		endSlot.setPort(end.hasPortRequirement() ? end.getLocation() : ANYWHERE);

		// Create start/end sequence elements for this route
		final SequenceElement startElement = new SequenceElement(indexingContext);
		final SequenceElement endElement = new SequenceElement(indexingContext);

		sequenceElements.add(startElement);
		sequenceElements.add(endElement);
		elementDurationsProvider.setElementDuration(endElement, 0);
		elementDurationsProvider.setElementDuration(startElement, 0);

		if (end.hasTimeRequirement() == false) {
			// put end slot into list of slots to patch up later.
			// Only fleet vessels have the late end window set.
			if (vesselInstanceType.equals(VesselInstanceType.FLEET))
				endSlots.add(new Pair<ISequenceElement, PortSlot>(endElement, endSlot));
		} else {
			endSlot.setTimeWindow(end.getTimeWindow());
		}

		timeWindowProvider.setTimeWindows(startElement, Collections.singletonList(startSlot.getTimeWindow()));

		if (end.hasTimeRequirement()) {
			timeWindowProvider.setTimeWindows(endElement, Collections.singletonList(endSlot.getTimeWindow()));
		} // otherwise this will be set in getOptimisationData().

		startElement.setName(startSlot.getId() + "-" + startSlot.getPort().getName());
		endElement.setName(endSlot.getId() + "-" + endSlot.getPort().getName());

		startElement.setPortSlot(startSlot);
		endElement.setPortSlot(endSlot);

		portProvider.setPortForElement(startSlot.getPort(), startElement);
		portProvider.setPortForElement(endSlot.getPort(), endElement);

		portTypeProvider.setPortType(startElement, PortType.Start);
		portTypeProvider.setPortType(endElement, PortType.End);

		portSlotsProvider.setPortSlot(startElement, startSlot);
		portSlotsProvider.setPortSlot(endElement, endSlot);

		resourceAllocationProvider.setAllowedResources(startElement, Collections.singleton(resource));
		resourceAllocationProvider.setAllowedResources(endElement, Collections.singleton(resource));

		startEndRequirementProvider.setStartEndRequirements(resource, start, end);
		startEndRequirementProvider.setStartEndElements(resource, startElement, endElement);

		// TODO specify initial vessel state?

		return vessel;
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(final IPort fixedPort) {
		return new StartEndRequirement(fixedPort, true, null);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(final ITimeWindow timeWindow) {
		return new StartEndRequirement(ANYWHERE, false, timeWindow);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(final IPort fixedPort, final ITimeWindow timeWindow) {
		return new StartEndRequirement(fixedPort, true, timeWindow);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement() {
		return new StartEndRequirement(ANYWHERE, false, null);
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
			if (USE_INDEXED_DCPS) {
				portDistanceProvider.set(route, new IndexedMatrixEditor<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider, Integer.MAX_VALUE));
			} else {
				portDistanceProvider.set(route, new HashMapMatrixProvider<IPort, Integer>(SchedulerConstants.DCP_portDistanceProvider, Integer.MAX_VALUE));
			}
		}

		final IMatrixEditor<IPort, Integer> matrix = (IMatrixEditor<IPort, Integer>) portDistanceProvider.get(route);

		matrix.set(from, to, distance);
	}

	@Override
	public void setVesselClassRouteCost(final String route, final IVesselClass vesselClass, final VesselState state, final int tollPrice) {
		routeCostProvider.setRouteCost(route, vesselClass, state, tollPrice);
	}

	@Override
	public void setDefaultRouteCost(final String route, final int defaultPrice) {
		routeCostProvider.setDefaultRouteCost(route, defaultPrice);
	}

	@Override
	public void setVesselClassRouteTimeAndFuel(final String name, final IVesselClass vc, final int transitTimeInHours, final long baseFuelInScaledMT) {
		routeCostProvider.setRouteTimeAndFuel(name, vc, transitTimeInHours, baseFuelInScaledMT);
	}

	@Override
	public void setElementDurations(final ISequenceElement element, final IResource resource, final int duration) {
		elementDurationsProvider.setElementDuration(element, resource, duration);
	}

	@Override
	public IOptimisationData<ISequenceElement> getOptimisationData() {
		// create return elements before fixing time windows,
		// because the next bit will have to patch up their time windows
		createReturnElements();

		portDistanceProvider.cacheExtremalValues(ports);

		// Patch up end time windows
		// The return time should be the soonest we can get back to the previous load,
		// presumably in the slowest vessel without going via a canal.

		// TODO what about return to first load rule?

		int latestDischarge = 0;
		IPort loadPort = null, dischargePort = null;
		for (final ICargo cargo : cargoes) {
			final int endOfDischargeWindow = cargo.getDischargeSlot().getTimeWindow().getEnd();
			if (endOfDischargeWindow > latestDischarge) {
				latestDischarge = endOfDischargeWindow;
				loadPort = cargo.getLoadSlot().getPort();
				dischargePort = cargo.getDischargeSlot().getPort();
			}
		}

		final int returnDistance = portDistanceProvider.getMaximumValue(dischargePort, loadPort);
		// what's the slowest vessel class
		int slowestMaxSpeed = Integer.MAX_VALUE;
		for (final IVesselClass vesselClass : vesselClasses) {
			slowestMaxSpeed = Math.min(slowestMaxSpeed, vesselClass.getMaxSpeed());
		}

		final int maxFastReturnTime = Calculator.getTimeFromSpeedDistance(slowestMaxSpeed, returnDistance);

		final int latestTime = Math.max(endOfLatestWindow, maxFastReturnTime + latestDischarge);
		for (final Pair<ISequenceElement, PortSlot> elementAndSlot : endSlots) {
			final ITimeWindow endWindow = createTimeWindow(latestTime, latestTime + 1);
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



		final OptimisationData<ISequenceElement> data = new OptimisationData<ISequenceElement>();

		data.setResources(resources);
		data.setSequenceElements(sequenceElements);

		data.addDataComponentProvider(SchedulerConstants.DCP_discountCurveProvider, discountCurveProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_vesselProvider, vesselProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_timeWindowProvider, timeWindowProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_portDistanceProvider, portDistanceProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_orderedElementsProvider, orderedSequenceElementsEditor);
		data.addDataComponentProvider(SchedulerConstants.DCP_portProvider, portProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, portSlotsProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_elementDurationsProvider, elementDurationsProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, portTypeProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_resourceAllocationProvider, resourceAllocationProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, startEndRequirementProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_portExclusionProvider, portExclusionProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_returnElementProvider, returnElementProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_routePriceProvider, routeCostProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_totalVolumeLimitProvider, totalVolumeLimits);

		if (true) {
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

		return data;
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

		// TODO: Null provider refs
	}

	@Override
	public IVesselClass createVesselClass(final String name, final int minSpeed, final int maxSpeed, final long capacityInM3, final int minHeelInM3, final int baseFuelUnitPricePerMT,
			final int baseFuelEquivalenceInM3TOMT, final int pilotLightRate, final int hourlyCharterPrice, final int warmupTimeHours, final int cooldownTimeHours, final int cooldownVolumeM3) {

		final VesselClass vesselClass = new VesselClass();
		vesselClass.setName(name);

		vesselClass.setMinSpeed(minSpeed);
		vesselClass.setMaxSpeed(maxSpeed);

		vesselClass.setCargoCapacity(capacityInM3);
		vesselClass.setMinHeel(minHeelInM3);

		vesselClass.setBaseFuelUnitPrice(baseFuelUnitPricePerMT);
		vesselClass.setBaseFuelConversionFactor(baseFuelEquivalenceInM3TOMT);

		vesselClass.setHourlyCharterInPrice(hourlyCharterPrice);

		vesselClass.setCooldownTime(cooldownTimeHours);
		vesselClass.setWarmupTime(warmupTimeHours);
		vesselClass.setCooldownVolume(cooldownVolumeM3);

		vesselClass.setPilotLightRate(pilotLightRate);

		vesselClasses.add(vesselClass);

		return vesselClass;
	}

	@Override
	public void setVesselClassStateParamaters(final IVesselClass vesselClass, final VesselState state, final int nboRateInM3PerHour, final int idleNBORateInM3PerHour,
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
	 */
	public void setVesselClassStateParamaters(final IVesselClass vc, final VesselState state, final int nboRateInM3PerHour, final int idleNBORateInM3PerHour, final int idleConsumptionRateInMTPerHour,
			final IConsumptionRateCalculator consumptionCalculatorInMTPerHour) {

		// Convert rate to MT equivalent per hour
		final int nboRateInMTPerHour = (int) Calculator.convertM3ToMT(nboRateInM3PerHour, vc.getBaseFuelConversionFactor());

		final int nboSpeed = consumptionCalculatorInMTPerHour.getSpeed(nboRateInMTPerHour);

		this.setVesselClassStateParamaters(vc, state, nboRateInM3PerHour, idleNBORateInM3PerHour, idleConsumptionRateInMTPerHour, consumptionCalculatorInMTPerHour, nboSpeed);
	}

	@Override
	public void setVesselClassInaccessiblePorts(final IVesselClass vc, final Set<IPort> inaccessiblePorts) {
		this.portExclusionProvider.setExcludedPorts(vc, inaccessiblePorts);
	}

	@Override
	public IVesselEventPortSlot createCharterOutEvent(final String id, final ITimeWindow arrival, final IPort fromPort, final IPort toPort, final int durationHours, final long maxHeelOut,
			final int heelCVValue, int heelUnitPrice) {
		return createVesselEvent(id, PortType.CharterOut, arrival, fromPort, toPort, durationHours, maxHeelOut, heelCVValue, heelUnitPrice);
	}

	@Override
	public IVesselEventPortSlot createDrydockEvent(final String id, final ITimeWindow arrival, final IPort port, final int durationHours) {
		return createVesselEvent(id, PortType.DryDock, arrival, port, port, durationHours, 0, 0, 0);
	}

	public IVesselEventPortSlot createVesselEvent(final String id, final PortType portType, final ITimeWindow arrival, final IPort fromPort, IPort toPort, final int durationHours,
			final long maxHeelOut, final int heelCVValue, int heelUnitPrice) {
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
		int i = 0;

		for (final IVesselEventPortSlot slot : vesselEvents) {
			final IVesselEvent vesselEvent = slot.getVesselEvent();

			final SequenceElement endElement = new SequenceElement(indexingContext, slot.getId(), slot);

			if (vesselEvent.getStartPort() != vesselEvent.getEndPort()) {
				// We insert two extra elements and slots, so that we go
				// startPort -> ANYWHERE -> endPort
				// this means we also have to fix any sequencing constraints
				// which have already been set up, and replicate the vessel
				// allocation constraints.
				final VesselEventPortSlot startSlot = new VesselEventPortSlot("start-" + slot.getId(), vesselEvent.getStartPort(), slot.getTimeWindow(), vesselEvent);
				final VesselEventPortSlot redirectSlot = new VesselEventPortSlot("redirect-" + slot.getId(), ANYWHERE, slot.getTimeWindow(), vesselEvent);

				startSlot.setPortType(PortType.Waypoint);
				redirectSlot.setPortType(PortType.Virtual);

				final SequenceElement startElement = new SequenceElement(indexingContext, startSlot.getId(), startSlot);
				final SequenceElement redirectElement = new SequenceElement(indexingContext, redirectSlot.getId(), redirectSlot);

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

			// final Set<IResource> resources = new HashSet<IResource>();
			// final Set<IVessel> supportedVessels = slotVesselRestrictions
			// .get(slot);
			// final Set<IVesselClass> supportedClasses =
			// slotVesselClassRestrictions
			// .get(slot);
			// for (final IVessel vessel : vessels) {
			// if (vessel.getVesselInstanceType() !=
			// VesselInstanceType.SPOT_CHARTER
			// && (supportedClasses.contains(vessel.getVesselClass()) ||
			// supportedVessels
			// .contains(vessel))) {
			// final IResource resource = vesselProvider
			// .getResource(vessel);
			// resources.add(resource);
			// }
			// }
			//
			// resourceAllocationProvider.setAllowedResources(endElement,
			// resources);
			//
			// if (startElement != null) {
			// resourceAllocationProvider.setAllowedResources(startElement,
			// resources);
			// }
			//
			// if (redirectElement != null) {
			// resourceAllocationProvider.setAllowedResources(redirectElement,
			// resources);
			// }

			i++;
		}
	}

	@Override
	public void addTotalVolumeConstraint(final Set<IPort> ports, final boolean loads, final boolean discharges, final long maximumTotalVolume, final ITimeWindow timeWindow) {
		TotalVolumeLimit limit = new TotalVolumeLimit();
		limit.setTimeWindow(timeWindow);
		limit.setVolumeLimit(maximumTotalVolume);

		for (final IPort port : ports) {
			if (loads) {
				List<TotalVolumeLimit> limits = loadLimits.get(port);
				if (limits == null)
					limits = new ArrayList<TotalVolumeLimit>();
				limits.add(limit);
				loadLimits.put(port, limits);
			}

			if (discharges) {
				List<TotalVolumeLimit> limits = dischargeLimits.get(port);
				if (limits == null)
					limits = new ArrayList<TotalVolumeLimit>();
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
		if (slot == null)
			return;
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
	public void setCargoVesselRestriction(ICargo cargo, Set<IVessel> vessels) {
		final ILoadSlot loadSlot = cargo.getLoadSlot();
		final IDischargeSlot dischargeSlot = cargo.getDischargeSlot();

		constrainSlotToVessels(loadSlot, vessels);
		constrainSlotToVessels(dischargeSlot, vessels);

		// final Collection<IResource> resources = new HashSet<IResource>();
		// for (final IVessel v : vessels) {
		// resources.add(vesselProvider.getResource(v));
		// }
		// resourceAllocationProvider.setAllowedResources(
		// portSlotsProvider.getElement(loadSlot), resources);
		// resourceAllocationProvider.setAllowedResources(
		// portSlotsProvider.getElement(dischargeSlot), resources);
	}

	@Override
	public ILoadPriceCalculator createFixedPriceContract(int pricePerMMBTU) {
		return new FixedPriceContract(pricePerMMBTU);
	}

	@Override
	public ILoadPriceCalculator createMarketPriceContract(ICurve index) {
		return new MarketPriceContract(index);
	}

	@Override
	public ILoadPriceCalculator createProfitSharingContract(ICurve actualMarket, ICurve referenceMarket, int alpha, int beta, int gamma) {
		return new ProfitSharingContract(actualMarket, referenceMarket, alpha, beta, gamma);
	}

	@Override
	public ILoadPriceCalculator createNetbackContract(int buyersMargin) {
		return new NetbackContract(buyersMargin, portDistanceProvider);
	}

	@Override
	/**
	 * Set a discount curve for the given fitness component name
	 * 
	 * @param name
	 * @param iCurve
	 */
	public void setFitnessComponentDiscountCurve(String name, ICurve iCurve) {
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

			if (allowedVesselClasses.isEmpty() && allowedVessels.isEmpty())
				continue;

			final HashSet<IResource> allowedResources = new HashSet<IResource>();

			for (final IVessel vessel : vessels) {
				if (allowedVessels.contains(vessel) || allowedVesselClasses.contains(vessel.getVesselClass())) {
					/*
					 * If this is a vessel event, and it's a spot vessel, don't allow it. In future this should probably be handled as a separate kind of constraint (a vessel instance type
					 * restriction, saying slot X can only go on vessels of types Y and Z)
					 */
					if (slot instanceof IVesselEventPortSlot && vessel.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER)
						continue;
					allowedResources.add(vesselProvider.getResource(vessel));
				}
			}

			resourceAllocationProvider.setAllowedResources(portSlotsProvider.getElement(slot), allowedResources);
		}
	}

	@Override
	public void constrainSlotToVessels(final IPortSlot slot, final Set<IVessel> vessels) {
		if (vessels == null || vessels.isEmpty())
			slotVesselRestrictions.remove(slot);
		else
			slotVesselRestrictions.put(slot, vessels);
	}

	@Override
	public void constrainSlotToVesselClasses(final IPortSlot slot, final Set<IVesselClass> vesselClasses) {
		if (vesselClasses == null || vesselClasses.isEmpty())
			slotVesselClassRestrictions.remove(slot);
		else
			slotVesselClassRestrictions.put(slot, vesselClasses);
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
}
