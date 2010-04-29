package com.mmxlabs.scheduler.optmiser.builder.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.components.impl.TimeWindow;
import com.mmxlabs.optimiser.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;
import com.mmxlabs.optimiser.dcproviders.ITimeWindowDataComponentProviderEditor;
import com.mmxlabs.optimiser.dcproviders.impl.OrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.dcproviders.impl.TimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.impl.Resource;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.optimiser.scenario.common.IMatrixEditor;
import com.mmxlabs.optimiser.scenario.common.impl.HashMapMatrixProvider;
import com.mmxlabs.optimiser.scenario.impl.OptimisationData;
import com.mmxlabs.scheduler.optmiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optmiser.builder.SchedulerConstants;
import com.mmxlabs.scheduler.optmiser.components.ICargo;
import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optmiser.components.IVessel;
import com.mmxlabs.scheduler.optmiser.components.impl.Cargo;
import com.mmxlabs.scheduler.optmiser.components.impl.Port;
import com.mmxlabs.scheduler.optmiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optmiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optmiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optmiser.providers.ISequenceElementProviderEditor;
import com.mmxlabs.scheduler.optmiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optmiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optmiser.providers.impl.HashMapSequenceElementProviderEditor;
import com.mmxlabs.scheduler.optmiser.providers.impl.HashMapVesselEditor;

public class SchedulerBuilder implements ISchedulerBuilder {

	private final List<IResource> resources = new ArrayList<IResource>();

	private final List<ISequenceElement> sequenceElements = new ArrayList<ISequenceElement>();

	private final List<IVessel> vessels = new LinkedList<IVessel>();

	private final List<ICargo> cargoes = new LinkedList<ICargo>();

	private final List<IPort> ports = new LinkedList<IPort>();

	private final IVesselProviderEditor vesselProvider;

	private final IPortProviderEditor portProvider;

	private final ISequenceElementProviderEditor scheduleElementProvider;

	private final IOrderedSequenceElementsDataComponentProviderEditor<ISequenceElement> orderedSequenceElementsEditor;

	private final ITimeWindowDataComponentProviderEditor timeWindowProvider;

	private final IMatrixEditor<IPort, Integer> portDistanceProvider;

	public SchedulerBuilder() {
		vesselProvider = new HashMapVesselEditor(
				SchedulerConstants.DCP_vesselProvider);
		portProvider = new HashMapPortEditor(
				SchedulerConstants.DCP_portProvider);
		scheduleElementProvider = new HashMapSequenceElementProviderEditor(
				SchedulerConstants.DCP_sequenceElementProvider);
		orderedSequenceElementsEditor = new OrderedSequenceElementsDataComponentProvider<ISequenceElement>(
				SchedulerConstants.DCP_orderedElementsProvider);
		timeWindowProvider = new TimeWindowDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider);
		portDistanceProvider = new HashMapMatrixProvider<IPort, Integer>(
				SchedulerConstants.DCP_portDistanceProvider);
	}

	@Override
	public ICargo createCargo(final IPort loadPort,
			final ITimeWindow loadWindow, final IPort dischargePort,
			final ITimeWindow dischargeWindow) {

		final Cargo cargo = new Cargo();
		cargo.setLoadPort(loadPort);
		cargo.setLoadWindow(loadWindow);
		cargo.setDischargePort(dischargePort);
		cargo.setDischargeWindow(dischargeWindow);

		cargoes.add(cargo);

		// Create sequence elements
		final SequenceElement loadElement = new SequenceElement();
		loadElement.setPort(loadPort);
		loadElement.setCargo(cargo);
		loadElement.setName(cargo.getId() + "-" + loadPort.getName());

		sequenceElements.add(loadElement);

		final SequenceElement dischargeElement = new SequenceElement();
		dischargeElement.setPort(dischargePort);
		dischargeElement.setCargo(cargo);
		dischargeElement.setName(cargo.getId() + "-" + dischargePort.getName());

		sequenceElements.add(dischargeElement);

		// Register sequence element with sequence element provider
		scheduleElementProvider
				.setSequenceElement(cargo, loadPort, loadElement);
		scheduleElementProvider.setSequenceElement(cargo, dischargePort,
				dischargeElement);

		// Set fixed visit order
		orderedSequenceElementsEditor.setElementOrder(loadElement,
				dischargeElement);

		// Register the port with the element
		portProvider.setPortForElement(loadPort, loadElement);
		portProvider.setPortForElement(dischargePort, dischargeElement);

		return cargo;
	}

	@Override
	public IPort createPort(final String name) {

		final Port port = new Port();
		port.setName(name);

		ports.add(port);

		return port;
	}

	@Override
	public ITimeWindow createTimeWindow(final int start, final int end) {
		return new TimeWindow(start, end);
	}

	@Override
	public IVessel createVessel(final String name) {

		final Vessel vessel = new Vessel();
		vessel.setName(name);

		vessels.add(vessel);

		final IResource resource = new Resource();
		resources.add(resource);

		// Register with provider
		vesselProvider.setVesselResource(resource, vessel);

		return vessel;
	}

	@Override
	public void setPortToPortDistance(final IPort from, final IPort to,
			final int distance) {

		portDistanceProvider.set(from, to, distance);
	}

	@Override
	public IOptimisationData<ISequenceElement> getOptimisationData() {

		final OptimisationData<ISequenceElement> data = new OptimisationData<ISequenceElement>();

		data.setResources(resources);
		data.setSequenceElements(sequenceElements);

		data.addDataComponentProvider(SchedulerConstants.DCP_vesselProvider,
				vesselProvider);
		data.addDataComponentProvider(
				SchedulerConstants.DCP_sequenceElementProvider,
				scheduleElementProvider);
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

		return data;
	}
}
