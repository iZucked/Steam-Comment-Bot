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
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.HashMapMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.impl.OptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.IXYPortDistanceCalculator;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.ICharterOut;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.Cargo;
import com.mmxlabs.scheduler.optimiser.components.impl.CharterOut;
import com.mmxlabs.scheduler.optimiser.components.impl.CharterOutPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.Port;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.components.impl.StartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.components.impl.XYPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapReturnElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapRouteCostProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapStartEndRequirementEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;

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

	private final IPortProviderEditor portProvider;

	private final IPortSlotProviderEditor<ISequenceElement> portSlotsProvider;

	private final IOrderedSequenceElementsDataComponentProviderEditor<ISequenceElement> orderedSequenceElementsEditor;

	private final ITimeWindowDataComponentProviderEditor timeWindowProvider;

	private final IMultiMatrixEditor<IPort, Integer> portDistanceProvider;

	private final IPortTypeProviderEditor<ISequenceElement> portTypeProvider;

	private final IElementDurationProviderEditor<ISequenceElement> elementDurationsProvider;

	private final IResourceAllocationConstraintDataComponentProviderEditor resourceAllocationProvider;

	private final List<ILoadSlot> loadSlots = new LinkedList<ILoadSlot>();

	private final List<IDischargeSlot> dischargeSlots = new LinkedList<IDischargeSlot>();

	private final List<ITimeWindow> timeWindows = new LinkedList<ITimeWindow>();

	/**
	 * List of end slots, which need to be corrected in getOptimisationData to
	 * have the latest time in them
	 */
	private final List<Pair<ISequenceElement, PortSlot>> endSlots = new LinkedList<Pair<ISequenceElement, PortSlot>>();

	/**
	 * A "virtual" port which is zero distance from all other ports, to be used
	 * in cases where a vessel can be in any location. This can be replaced with
	 * a real location at a later date, after running an optimisation.
	 */
	private final IPort ANYWHERE;

	private final IStartEndRequirementProviderEditor<ISequenceElement> startEndRequirementProvider;

	/**
	 * A field for tracking the time at which the last time window closes
	 */
	private int endOfLatestWindow = 0;

	private final IPortExclusionProviderEditor portExclusionProvider;

	private final Set<ICharterOut> charterOuts = new HashSet<ICharterOut>();
	private final Map<ICharterOut, Set<IVessel>> vesselCharterOuts = new HashMap<ICharterOut, Set<IVessel>>();

	private final Map<ICharterOut, Set<IVesselClass>> vesselClassCharterOuts = new HashMap<ICharterOut, Set<IVesselClass>>();

	private final IReturnElementProviderEditor<ISequenceElement> returnElementProvider;

	private HashMapRouteCostProviderEditor routeCostProvider;

	public SchedulerBuilder() {
		vesselProvider = new HashMapVesselEditor(
				SchedulerConstants.DCP_vesselProvider);
		portProvider = new HashMapPortEditor(
				SchedulerConstants.DCP_portProvider);
		orderedSequenceElementsEditor = new OrderedSequenceElementsDataComponentProvider<ISequenceElement>(
				SchedulerConstants.DCP_orderedElementsProvider);
		timeWindowProvider = new TimeWindowDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider);
		portDistanceProvider = new HashMapMultiMatrixProvider<IPort, Integer>(
				SchedulerConstants.DCP_portDistanceProvider);
		portSlotsProvider = new HashMapPortSlotEditor<ISequenceElement>(
				SchedulerConstants.DCP_portSlotsProvider);
		elementDurationsProvider = new HashMapElementDurationEditor<ISequenceElement>(
				SchedulerConstants.DCP_elementDurationsProvider);
		portTypeProvider = new HashMapPortTypeEditor<ISequenceElement>(
				SchedulerConstants.DCP_portTypeProvider);

		resourceAllocationProvider = new ResourceAllocationConstraintProvider(
				SchedulerConstants.DCP_resourceAllocationProvider);

		startEndRequirementProvider = new HashMapStartEndRequirementEditor<ISequenceElement>(
				SchedulerConstants.DCP_startEndRequirementProvider);

		portExclusionProvider = new HashMapPortExclusionProvider(
				SchedulerConstants.DCP_portExclusionProvider);

		// Create a default matrix entry
		portDistanceProvider.set(IMultiMatrixProvider.Default_Key,
				new HashMapMatrixProvider<IPort, Integer>(
						SchedulerConstants.DCP_portDistanceProvider,
						Integer.MAX_VALUE));

		returnElementProvider = new HashMapReturnElementProviderEditor<ISequenceElement>(
				SchedulerConstants.DCP_returnElementProvider);

		routeCostProvider = new HashMapRouteCostProviderEditor(
				SchedulerConstants.DCP_routePriceProvider,
				IMultiMatrixProvider.Default_Key);

		// Create the anywhere port
		ANYWHERE = createPort("ANYWHERE");

	}

	@Override
	public ILoadSlot createLoadSlot(final String id, final IPort port,
			final ITimeWindow window, final long minVolumeInM3,
			final long maxVolumeInM3, final int pricePerMMBTu,
			final int cargoCVValue, final int durationHours) {

		if (!ports.contains(port)) {
			throw new IllegalArgumentException(
					"IPort was not created by this builder");
		}
		if (!timeWindows.contains(window)) {
			throw new IllegalArgumentException(
					"ITimeWindow was not created by this builder");
		}

		final LoadSlot slot = new LoadSlot();
		slot.setId(id);
		slot.setPort(port);
		slot.setTimeWindow(window);
		slot.setMinLoadVolume(minVolumeInM3);
		slot.setMaxLoadVolume(maxVolumeInM3);
		slot.setPurchasePrice(pricePerMMBTu);
		slot.setCargoCVValue(cargoCVValue);

		loadSlots.add(slot);

		// Create a sequence element against this load slot
		final SequenceElement element = new SequenceElement();
		element.setName(id + "-" + port.getName());
		element.setPortSlot(slot);

		sequenceElements.add(element);

		// Register the port with the element
		portProvider.setPortForElement(port, element);

		portTypeProvider.setPortType(element, PortType.Load);

		portSlotsProvider.setPortSlot(element, slot);

		timeWindowProvider.setTimeWindows(element,
				Collections.singletonList(window));

		elementDurationsProvider.setElementDuration(element, durationHours);
		
		return slot;
	}

	@Override
	public IDischargeSlot createDischargeSlot(final String id,
			final IPort port, final ITimeWindow window,
			final long minVolumeInM3, final long maxVolumeInM3,
			final int pricePerMMBTu, final int durationHours) {

		if (!ports.contains(port)) {
			throw new IllegalArgumentException(
					"IPort was not created by this builder");
		}
		if (!timeWindows.contains(window)) {
			throw new IllegalArgumentException(
					"ITimeWindow was not created by this builder");
		}

		final DischargeSlot slot = new DischargeSlot();
		slot.setId(id);
		slot.setPort(port);
		slot.setTimeWindow(window);
		slot.setMinDischargeVolume(minVolumeInM3);
		slot.setMaxDischargeVolume(maxVolumeInM3);
		slot.setSalesPrice(pricePerMMBTu);

		dischargeSlots.add(slot);

		// Create a sequence element against this discharge slot
		final SequenceElement element = new SequenceElement();
		element.setPortSlot(slot);
		element.setName(id + "-" + port.getName());

		sequenceElements.add(element);

		// Register the port with the element
		portProvider.setPortForElement(port, element);

		portSlotsProvider.setPortSlot(element, slot);

		portTypeProvider.setPortType(element, PortType.Discharge);

		timeWindowProvider.setTimeWindows(element,
				Collections.singletonList(window));

		elementDurationsProvider.setElementDuration(element, durationHours);
		
		return slot;
	}

	@Override
	public ICargo createCargo(final String id, final ILoadSlot loadSlot,
			final IDischargeSlot dischargeSlot) {

		if (!loadSlots.contains(loadSlot)) {
			throw new IllegalArgumentException(
					"ILoadSlot was not created by this builder");
		}
		if (!dischargeSlots.contains(dischargeSlot)) {
			throw new IllegalArgumentException(
					"IDischargeSlot was not created by this builder");
		}

		final Cargo cargo = new Cargo();
		cargo.setLoadSlot(loadSlot);
		cargo.setDischargeSlot(dischargeSlot);

		cargoes.add(cargo);

		final ISequenceElement loadElement = portSlotsProvider
				.getElement(loadSlot);
		final ISequenceElement dischargeElement = portSlotsProvider
				.getElement(dischargeSlot);

		// Set fixed visit order
		orderedSequenceElementsEditor.setElementOrder(loadElement,
				dischargeElement);

		return cargo;
	}

	@Override
	public IPort createPort(final String name) {

		final Port port = new Port();
		port.setName(name);

		ports.add(port);

		/*
		 * ANYWHERE is not present in the /real/ distance matrix, so we must set
		 * its distances here.
		 */
		if (ANYWHERE != null) {
			setPortToPortDistance(port, ANYWHERE,
					IMultiMatrixProvider.Default_Key, 0);
			setPortToPortDistance(ANYWHERE, port,
					IMultiMatrixProvider.Default_Key, 0);
		}

		// travel time from A to A should be zero, right?
		this.setPortToPortDistance(port, port,
				IMultiMatrixProvider.Default_Key, 0);

		// create the return elements to return to this port using the ELSM
		returnElementProvider.setReturnElement(port, createReturnElement(port));

		return port;
	}

	private ISequenceElement createReturnElement(final IPort port) {
		final String name = "return-to-" + port.getName();
		final EndPortSlot slot = new EndPortSlot(name, port, null);
		final SequenceElement element = new SequenceElement("return-to-"
				+ port.getName(), slot);

		portProvider.setPortForElement(port, element);
		portSlotsProvider.setPortSlot(element, slot);
		// Return elements are always end elements?
		portTypeProvider.setPortType(element, PortType.End);
		final List<ITimeWindow> emptyTimeWindowList = Collections.emptyList();
		timeWindowProvider.setTimeWindows(element, emptyTimeWindowList);

		return element;
	}

	@Override
	public IXYPort createPort(final String name, final float x, final float y) {

		final XYPort port = new XYPort();
		port.setName(name);
		port.setX(x);
		port.setY(y);

		ports.add(port);

		if (ANYWHERE != null) {
			setPortToPortDistance(port, ANYWHERE,
					IMultiMatrixProvider.Default_Key, 0);
			setPortToPortDistance(ANYWHERE, port,
					IMultiMatrixProvider.Default_Key, 0);
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
	public IVessel createVessel(final String name,
			final IVesselClass vesselClass, final IStartEndRequirement start,
			final IStartEndRequirement end) {
		return this.createVessel(name, vesselClass, VesselInstanceType.FLEET,
				start, end);
	}

	/**
	 * Create several spot vessels (see also {@code createSpotVessel}), named
	 * like namePrefix-1, namePrefix-2, etc
	 * 
	 * @param namePrefix
	 * @param vesselClass
	 * @param count
	 * @return
	 */
	@Override
	public List<IVessel> createSpotVessels(final String namePrefix,
			final IVesselClass vesselClass, final int count) {
		final List<IVessel> answer = new ArrayList<IVessel>(count);
		for (int i = 0; i < count; i++) {
			answer.add(createSpotVessel(namePrefix + "-" + (i + 1), vesselClass));
		}
		return answer;
	}

	/**
	 * Create a spot charter vessel with no fixed start/end requirements and
	 * vessel instance type SPOT_CHARTER
	 * 
	 * @param name
	 * @param vesselClass
	 * @return the spot vessel
	 */
	@Override
	public IVessel createSpotVessel(final String name,
			final IVesselClass vesselClass) {
		final IStartEndRequirement start = createStartEndRequirement();
		final IStartEndRequirement end = createStartEndRequirement();

		return createVessel(name, vesselClass, VesselInstanceType.SPOT_CHARTER,
				start, end);
	}

	@Override
	public IVessel createVessel(final String name,
			final IVesselClass vesselClass,
			final VesselInstanceType vesselInstanceType,
			final IStartEndRequirement start, final IStartEndRequirement end) {

		if (!vesselClasses.contains(vesselClass)) {
			throw new IllegalArgumentException(
					"IVesselClass was not created using this builder");
		}

		// if (!ports.contains(startPort)) {
		// throw new IllegalArgumentException(
		// "Start IPort was not created using this builder");
		// }

		// if (!ports.contains(endPort)) {
		// throw new IllegalArgumentException(
		// "End IPort was not created using this builder");
		// }

		final Vessel vessel = new Vessel();
		vessel.setName(name);
		vessel.setVesselClass(vesselClass);

		vessel.setVesselInstanceType(vesselInstanceType);

		vessels.add(vessel);

		final IResource resource = new Resource(name);
		resources.add(resource);

		// Register with provider
		vesselProvider.setVesselResource(resource, vessel);

		// If no time requirement is specified then the time window is at the
		// very start of the job
		final ITimeWindow startWindow = start.hasTimeRequirement() ? createTimeWindow(
				start.getTime(), start.getTime()) : createTimeWindow(0, 0);

		final StartPortSlot startSlot = new StartPortSlot();
		startSlot.setId("start-" + name);
		startSlot.setPort(start.hasPortRequirement() ? start.getLocation()
				: ANYWHERE);

		startSlot.setTimeWindow(startWindow);

		final EndPortSlot endSlot = new EndPortSlot();
		endSlot.setId("end-" + name);
		endSlot.setPort(end.hasPortRequirement() ? end.getLocation() : ANYWHERE);

		// Create start/end sequence elements for this route
		final SequenceElement startElement = new SequenceElement();
		final SequenceElement endElement = new SequenceElement();

		sequenceElements.add(startElement);
		sequenceElements.add(endElement);

		if (end.hasTimeRequirement() == false) {
			// put end slot into list of slots to patch up later.
			endSlots.add(new Pair<ISequenceElement, PortSlot>(endElement,
					endSlot));
		} else {
			endSlot.setTimeWindow(createTimeWindow(end.getTime(),
					end.getTime() + 1));
		}

		timeWindowProvider.setTimeWindows(startElement,
				Collections.singletonList(startSlot.getTimeWindow()));

		if (end.hasTimeRequirement()) {
			timeWindowProvider.setTimeWindows(endElement,
					Collections.singletonList(endSlot.getTimeWindow()));
		} // otherwise this will be set in getOptimisationData().

		startElement.setName(startSlot.getId() + "-"
				+ startSlot.getPort().getName());
		endElement.setName(endSlot.getId() + "-" + endSlot.getPort().getName());

		startElement.setPortSlot(startSlot);
		endElement.setPortSlot(endSlot);

		portProvider.setPortForElement(startSlot.getPort(), startElement);
		portProvider.setPortForElement(endSlot.getPort(), endElement);

		portTypeProvider.setPortType(startElement, PortType.Start);
		portTypeProvider.setPortType(endElement, PortType.End);

		portSlotsProvider.setPortSlot(startElement, startSlot);
		portSlotsProvider.setPortSlot(endElement, endSlot);

		resourceAllocationProvider.setAllowedResources(startElement,
				Collections.singleton(resource));
		resourceAllocationProvider.setAllowedResources(endElement,
				Collections.singleton(resource));

		startEndRequirementProvider.setStartEndRequirements(resource, start,
				end);
		startEndRequirementProvider.setStartEndElements(resource, startElement,
				endElement);

		// TODO specify initial vessel state?

		return vessel;
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(final IPort fixedPort) {
		return new StartEndRequirement(fixedPort, true, 0, false);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(final int fixedTime) {
		return new StartEndRequirement(ANYWHERE, false, fixedTime, true);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(
			final IPort fixedPort, final int fixedTime) {
		return new StartEndRequirement(fixedPort, true, fixedTime, true);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement() {
		return new StartEndRequirement(ANYWHERE, false, 0, false);
	}

	@Override
	public void setPortToPortDistance(final IPort from, final IPort to,
			final String route, final int distance) {

		if (!ports.contains(from)) {
			throw new IllegalArgumentException(
					"From IPort was not created using this builder");
		}
		if (!ports.contains(to)) {
			throw new IllegalArgumentException(
					"To IPort was not created using this builder");
		}

		if (portDistanceProvider.containsKey(route) == false) {
			/*
			 * This route has not previously been added to the PDP; initialize a
			 * blank matrix here?
			 */

			portDistanceProvider.set(route,
					new HashMapMatrixProvider<IPort, Integer>(
							SchedulerConstants.DCP_portDistanceProvider,
							Integer.MAX_VALUE));
		}

		final IMatrixEditor<IPort, Integer> matrix = (IMatrixEditor<IPort, Integer>) portDistanceProvider
				.get(route);

		matrix.set(from, to, distance);
	}

	@Override
	public void setVesselClassRouteCost(String route,
			IVesselClass vesselClass, VesselState state, int tollPrice) {
		routeCostProvider.setRouteCost(route, vesselClass, state, tollPrice);
	}
	
	@Override
	public void setDefaultRouteCost(String route, int defaultPrice) {
		routeCostProvider.setDefaultRouteCost(route, defaultPrice);
	}

	@Override
	public void setElementDurations(final ISequenceElement element,
			final IResource resource, final int duration) {
		elementDurationsProvider
				.setElementDuration(element, resource, duration);
	}

	@Override
	public IOptimisationData<ISequenceElement> getOptimisationData() {
		// Patch up end time windows
		final int latestTime = endOfLatestWindow + 24 * 7;
		for (final Pair<ISequenceElement, PortSlot> elementAndSlot : endSlots) {
			final ITimeWindow endWindow = createTimeWindow(latestTime,
					latestTime + 1);
			elementAndSlot.getSecond().setTimeWindow(endWindow);
			timeWindowProvider.setTimeWindows(elementAndSlot.getFirst(),
					Collections.singletonList(endWindow));
		}

		// Create charter out elements
		buildCharterOuts();

		final OptimisationData<ISequenceElement> data = new OptimisationData<ISequenceElement>();

		data.setResources(resources);
		data.setSequenceElements(sequenceElements);

		data.addDataComponentProvider(SchedulerConstants.DCP_vesselProvider,
				vesselProvider);
		data.addDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider, timeWindowProvider);
		data.addDataComponentProvider(
				SchedulerConstants.DCP_portDistanceProvider,
				portDistanceProvider);
		data.addDataComponentProvider(
				SchedulerConstants.DCP_orderedElementsProvider,
				orderedSequenceElementsEditor);
		data.addDataComponentProvider(SchedulerConstants.DCP_portProvider,
				portProvider);
		data.addDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider,
				portSlotsProvider);
		data.addDataComponentProvider(
				SchedulerConstants.DCP_elementDurationsProvider,
				elementDurationsProvider);

		data.addDataComponentProvider(SchedulerConstants.DCP_portTypeProvider,
				portTypeProvider);

		data.addDataComponentProvider(
				SchedulerConstants.DCP_resourceAllocationProvider,
				resourceAllocationProvider);

		data.addDataComponentProvider(
				SchedulerConstants.DCP_startEndRequirementProvider,
				startEndRequirementProvider);

		data.addDataComponentProvider(
				SchedulerConstants.DCP_portExclusionProvider,
				portExclusionProvider);

		data.addDataComponentProvider(
				SchedulerConstants.DCP_returnElementProvider,
				returnElementProvider);

		data.addDataComponentProvider(
				SchedulerConstants.DCP_routePriceProvider, routeCostProvider);

		if (true) {
			for (final IPort from : ports) {
				if (!(from instanceof IXYPort)) {
					continue;
				}
				for (final IPort to : ports) {
					if (to instanceof IXYPort) {
						final double dist = distanceProvider.getDistance(
								(IXYPort) from, (IXYPort) to);
						final int iDist = (int) dist;

						final IMatrixEditor<IPort, Integer> matrix = (IMatrixEditor<IPort, Integer>) portDistanceProvider
								.get(IMultiMatrixProvider.Default_Key);

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
	public IVesselClass createVesselClass(final String name,
			final int minSpeed, final int maxSpeed, final long capacityInM3,
			final int minHeelInM3, final int baseFuelUnitPricePerMT,
			final int baseFuelEquivalenceInM3TOMT) {
		return createVesselClass(name, minSpeed, maxSpeed, capacityInM3,
				minHeelInM3, baseFuelUnitPricePerMT,
				baseFuelEquivalenceInM3TOMT, 0);
	}

	@Override
	public IVesselClass createVesselClass(final String name,
			final int minSpeed, final int maxSpeed, final long capacityInM3,
			final int minHeelInM3, final int baseFuelUnitPricePerMT,
			final int baseFuelEquivalenceInM3TOMT, final int hourlyCharterPrice) {

		final VesselClass vesselClass = new VesselClass();
		vesselClass.setName(name);

		vesselClass.setMinSpeed(minSpeed);
		vesselClass.setMaxSpeed(maxSpeed);

		vesselClass.setCargoCapacity(capacityInM3);
		vesselClass.setMinHeel(minHeelInM3);

		vesselClass.setBaseFuelUnitPrice(baseFuelUnitPricePerMT);
		vesselClass.setBaseFuelConversionFactor(baseFuelEquivalenceInM3TOMT);

		vesselClass.setHourlyCharterPrice(hourlyCharterPrice);

		vesselClasses.add(vesselClass);

		return vesselClass;
	}

	@Override
	public void setVesselClassStateParamaters(
			final IVesselClass vesselClass,
			final VesselState state,
			final int nboRateInM3PerHour,
			final int idleNBORateInM3PerHour,
			final int idleConsumptionRateInMTPerHour,
			final IConsumptionRateCalculator consumptionRateCalculatorInMTPerHour,
			final int nboSpeed) {

		if (!vesselClasses.contains(vesselClass)) {
			throw new IllegalArgumentException(
					"IVesselClass was not created using this builder");
		}

		// Check instance is the same as that used in createVesselClass(..)
		if (!(vesselClass instanceof VesselClass)) {
			throw new IllegalArgumentException("Expected instance of "
					+ VesselClass.class.getCanonicalName());
		}

		final VesselClass vc = (VesselClass) vesselClass;

		vc.setNBORate(state, nboRateInM3PerHour);
		vc.setIdleNBORate(state, idleNBORateInM3PerHour);
		vc.setIdleConsumptionRate(state, idleConsumptionRateInMTPerHour);
		vc.setConsumptionRate(state, consumptionRateCalculatorInMTPerHour);
		vc.setNBOSpeed(state, nboSpeed);
	}

	/**
	 * Like the other setVesselClassStateParameters, but the NBO speed is
	 * calculated automatically.
	 * 
	 * @param vc
	 * @param state
	 * @param nboRateInM3PerHour
	 * @param idleNBORateInM3PerHour
	 * @param idleConsumptionRateInMTPerHour
	 * @param consumptionCalculatorInMTPerHour
	 */
	public void setVesselClassStateParamaters(
			final IVesselClass vc,
			final VesselState state,
			final int nboRateInM3PerHour,
			final int idleNBORateInM3PerHour,
			final int idleConsumptionRateInMTPerHour,
			final InterpolatingConsumptionRateCalculator consumptionCalculatorInMTPerHour) {

		// Convert rate to MT equivalent per hour
		final int nboRateInMTPerHour = (int) Calculator.convertM3ToMT(
				nboRateInM3PerHour, vc.getBaseFuelConversionFactor());

		final int nboSpeed = consumptionCalculatorInMTPerHour
				.getSpeed(nboRateInMTPerHour);

		this.setVesselClassStateParamaters(vc, state, nboRateInM3PerHour,
				idleNBORateInM3PerHour, idleConsumptionRateInMTPerHour,
				consumptionCalculatorInMTPerHour, nboSpeed);
	}

	@Override
	public void setVesselClassInaccessiblePorts(final IVesselClass vc,
			final Set<IPort> inaccessiblePorts) {
		this.portExclusionProvider.setExcludedPorts(vc, inaccessiblePorts);
	}

	@Override
	public ICharterOut createCharterOut(final ITimeWindow arrival,
			final IPort port, final int durationHours) {
		final ICharterOut result = new CharterOut(arrival, durationHours, port);
		charterOuts.add(result);
		vesselCharterOuts.put(result, new HashSet<IVessel>());
		vesselClassCharterOuts.put(result, new HashSet<IVesselClass>());
		return result;
	}

	@Override
	public void addCharterOutVessel(final ICharterOut charterOut,
			final IVessel vessel) {
		if (!vessels.contains(vessel)) {
			throw new IllegalArgumentException(
					"IVessel was not created using this builder");
		}
		if (!charterOuts.contains(charterOut)) {
			throw new IllegalArgumentException(
					"ICharterOut was not created using this builder");
		}
		vesselCharterOuts.get(charterOut).add(vessel);
	}

	@Override
	public void addCharterOutVesselClass(final ICharterOut charterOut,
			final IVesselClass vesselClass) {
		if (!vesselClasses.contains(vesselClass)) {
			throw new IllegalArgumentException(
					"IVesselClass was not created using this builder");
		}
		if (!charterOuts.contains(charterOut)) {
			throw new IllegalArgumentException(
					"ICharterOut was not created using this builder");
		}
		vesselClassCharterOuts.get(charterOut).add(vesselClass);
	}

	protected void buildCharterOuts() {
		int i = 0;
		
		for (final ICharterOut charterOut : charterOuts) {
			
			CharterOutPortSlot slot =
				new CharterOutPortSlot("charter-out-" + i, 
						charterOut.getPort(), 
						charterOut.getTimeWindow());
			
			final SequenceElement element = new SequenceElement("charter-out-"+i, slot);
			
			sequenceElements.add(element); 
			
			timeWindowProvider.setTimeWindows(element,
					Collections.singletonList(charterOut.getTimeWindow()));
			portTypeProvider.setPortType(element, PortType.CharterOut);

			elementDurationsProvider.setElementDuration(element, charterOut.getDurationHours());
			
			//element needs a port slot
			
			portSlotsProvider.setPortSlot(element, slot);
			
			portProvider.setPortForElement(charterOut.getPort(), element);
			
			final Set<IResource> resources = new HashSet<IResource>();
			final Set<IVessel> supportedVessels = vesselCharterOuts
					.get(charterOut);
			final Set<IVesselClass> supportedClasses = vesselClassCharterOuts
					.get(charterOut);
			for (final IVessel vessel : vessels) {
				if (supportedClasses.contains(vessel.getVesselClass())
						|| supportedVessels.contains(vessel)) {
					final IResource resource = vesselProvider
							.getResource(vessel);
					resources.add(resource);
				}
			}

			resourceAllocationProvider.setAllowedResources(element, resources);
			i++;
		}
	}
}
