package com.mmxlabs.scheduler.optimiser.builder.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.IXYPortDistanceCalculator;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
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
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.Port;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.components.impl.StartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselClass;
import com.mmxlabs.scheduler.optimiser.components.impl.XYPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;
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
	 * List of end slots, which need to be corrected in getOptimisationData to have the latest time in them
	 */
	private final List<PortSlot> endSlots = new LinkedList<PortSlot>();
	
	/**
	 * A "virtual" port which is zero distance from all other ports, to be used in cases where a vessel can be in any location.
	 * This can be replaced with a real location at a later date, after running an optimisation.
	 */
	private final IPort ANYWHERE;

	private final IStartEndRequirementProviderEditor<ISequenceElement> startEndRequirementProvider;

	/**
	 * A field for tracking the time at which the last time window closes
	 */
	private int endOfLatestWindow = 0;
	
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
		
		// Create a default matrix entry
		portDistanceProvider.set(IMultiMatrixProvider.Default_Key,
				new HashMapMatrixProvider<IPort, Integer>(
						SchedulerConstants.DCP_portDistanceProvider,
						Integer.MAX_VALUE));
		
		// Create the anywhere port
		ANYWHERE = createPort("ANYWHERE");
	}

	@Override
	public ILoadSlot createLoadSlot(final String id, final IPort port,
			final ITimeWindow window, final long minVolume,
			final long maxVolume, final int price) {

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
		slot.setMinLoadVolume(minVolume);
		slot.setMaxLoadVolume(maxVolume);
		slot.setPurchasePrice(price);

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

		return slot;
	}

	@Override
	public IDischargeSlot createDischargeSlot(final String id,
			final IPort port, final ITimeWindow window, final long minVolume,
			final long maxVolume, final int price) {

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
		slot.setMinDischargeVolume(minVolume);
		slot.setMaxDischargeVolume(maxVolume);
		slot.setSalesPrice(price);

		dischargeSlots.add(slot);

		// Create a sequence element against this discharge slot
		final SequenceElement element = new SequenceElement();
		element.setPortSlot(slot);
		element.setName(id + "-" + port.getName());

		sequenceElements.add(element);

		// Register the port with the element
		portProvider.setPortForElement(port, element);

		portSlotsProvider.setPortSlot(element, slot);

		portTypeProvider.setPortType(element,
				PortType.Discharge);

		timeWindowProvider.setTimeWindows(element,
				Collections.singletonList(window));

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
		 * ANYWHERE is not present in the /real/ distance matrix, so we must set its distances here.
		 */
		if (ANYWHERE != null) {
			setPortToPortDistance(port, ANYWHERE, IMultiMatrixProvider.Default_Key, 0);
			setPortToPortDistance(ANYWHERE, port, IMultiMatrixProvider.Default_Key, 0);
		}
		
		return port;
	}

	@Override
	public IXYPort createPort(final String name, final float x, final float y) {

		final XYPort port = new XYPort();
		port.setName(name);
		port.setX(x);
		port.setY(y);

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
			endOfLatestWindow  = end;
		}
		
		timeWindows.add(window);
		return window;
	}

	public IVessel createVessel(final String name, final IVesselClass vesselClass, final IStartEndRequirement start,
			final IStartEndRequirement end) {
		return this.createVessel(name, vesselClass, VesselInstanceType.FLEET, start, end);
	}
	
	@Override
	public IVessel createVessel(final String name,
			final IVesselClass vesselClass, final VesselInstanceType vesselInstanceType, 
			IStartEndRequirement start, IStartEndRequirement end){

		if (!vesselClasses.contains(vesselClass)) {
			throw new IllegalArgumentException(
					"IVesselClass was not created using this builder");
		}

//		if (!ports.contains(startPort)) {
//			throw new IllegalArgumentException(
//					"Start IPort was not created using this builder");
//		}

//		if (!ports.contains(endPort)) {
//			throw new IllegalArgumentException(
//					"End IPort was not created using this builder");
//		}

		final Vessel vessel = new Vessel();
		vessel.setName(name);
		vessel.setVesselClass(vesselClass);

		vessel.setVesselInstanceType(vesselInstanceType);
		
		vessels.add(vessel);

		final IResource resource = new Resource(name);
		resources.add(resource);

		// Register with provider
		vesselProvider.setVesselResource(resource, vessel);
		
		// If no time requirement is specified then the time window is at the very start of the job
		ITimeWindow startWindow = 
			start.hasTimeRequirement() ? 
					createTimeWindow(start.getTime(), start.getTime()) :
						createTimeWindow(0, 0);
		
		PortSlot startSlot = new PortSlot();
		startSlot.setId("start-" + name);
		startSlot.setPort(start.hasPortRequirement() ? start.getLocation() : ANYWHERE);
		
		startSlot.setTimeWindow(startWindow);

		PortSlot endSlot = new PortSlot();
		endSlot.setId("end-" + name);
		endSlot.setPort(end.hasPortRequirement() ? end.getLocation() : ANYWHERE);
		
		if (end.hasTimeRequirement() == false) {
			//put end slot into list of slots to patch up later.
			endSlots.add(endSlot);
		} else {			
			endSlot.setTimeWindow(
					createTimeWindow(end.getTime(), end.getTime() + 1)
			);
		}

		

		// Create start/end sequence elements for this route
		SequenceElement startElement = new SequenceElement();
		SequenceElement endElement = new SequenceElement();
		
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
		
		timeWindowProvider.setTimeWindows(startElement, Collections.singletonList(startSlot.getTimeWindow()));
		timeWindowProvider.setTimeWindows(endElement, Collections.singletonList(endSlot.getTimeWindow()));
		
		resourceAllocationProvider.setAllowedResources(startElement, Collections.singleton(resource));
		resourceAllocationProvider.setAllowedResources(endElement, Collections.singleton(resource));
		
		startEndRequirementProvider.setStartEndRequirements(resource, start, end);
		startEndRequirementProvider.setStartEndElements(resource, startElement, endElement);
		
//		TODO specify initial vessel state?
		
		return vessel;
	}
	
	@Override
	public IStartEndRequirement createStartEndRequirement(IPort fixedPort) {
		return new StartEndRequirement(fixedPort, true, 0, false);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(int fixedTime) {
		return new StartEndRequirement(ANYWHERE, false, fixedTime, true);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(IPort fixedPort,
			int fixedTime) {
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

		final IMatrixEditor<IPort, Integer> matrix = (IMatrixEditor<IPort, Integer>) portDistanceProvider
				.get(route);

		matrix.set(from, to, distance);
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
		for (PortSlot slot : endSlots) {
			ITimeWindow endWindow = createTimeWindow(latestTime, latestTime+1);
			slot.setTimeWindow(endWindow);
			timeWindowProvider.setTimeWindows(slot, Collections.singletonList(endWindow));
		}
		
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
	public IVesselClass createVesselClass(String name, int minSpeed,
			int maxSpeed, long capacity, int minHeel, int baseFuelUnitPrice) {
		return createVesselClass(name, minSpeed, maxSpeed, capacity, minHeel, baseFuelUnitPrice, 0);
	}

	@Override
	public IVesselClass createVesselClass(final String name,
			final int minSpeed, final int maxSpeed, final long capacity,
			final int minHeel, int baseFuelUnitPrice, int hourlyCharterPrice) {

		final VesselClass vesselClass = new VesselClass();
		vesselClass.setName(name);

		vesselClass.setMinSpeed(minSpeed);
		vesselClass.setMaxSpeed(maxSpeed);

		vesselClass.setCargoCapacity(capacity);
		vesselClass.setMinHeel(minHeel);

		vesselClass.setBaseFuelUnitPrice(baseFuelUnitPrice);

		vesselClass.setHourlyCharterPrice(hourlyCharterPrice);
		
		vesselClasses.add(vesselClass);

		return vesselClass;
	}

	@Override
	public void setVesselClassStateParamaters(final IVesselClass vesselClass,
			final VesselState state, final int nboRate, final int idleNBORate,
			final int idleConsumptionRate,
			final IConsumptionRateCalculator consumptionRateCalculator,
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

		vc.setNBORate(state, nboRate);
		vc.setIdleNBORate(state, idleNBORate);
		vc.setIdleConsumptionRate(state, idleConsumptionRate);
		vc.setConsumptionRate(state, consumptionRateCalculator);
		vc.setNBOSpeed(state, nboSpeed);
	}

	/**
	 * Like the other setVesselClassStateParameters, but the NBO speed is calculated automatically.
	 * 
	 * @param vc
	 * @param state
	 * @param nboRate
	 * @param idleNBORate
	 * @param idleConsumptionRate
	 * @param consumptionCalculator
	 */
	public void setVesselClassStateParamaters(IVesselClass vc,
			VesselState state, int nboRate, int idleNBORate,
			int idleConsumptionRate,
			InterpolatingConsumptionRateCalculator consumptionCalculator) {
		//TODO there is something stupid going on with units here.
		final int nboSpeed = consumptionCalculator.getSpeed(nboRate);
		this.setVesselClassStateParamaters(vc, state, nboRate, idleNBORate, idleConsumptionRate, consumptionCalculator, nboSpeed);
	}
}
