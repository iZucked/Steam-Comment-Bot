package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * Fitness component which accumulates port costs associated with sequences.
 * 
 * @author hinton
 *
 */
public class PortCostFitnessComponent extends AbstractPerRouteSchedulerFitnessComponent {

	private String portCostProviderKey;
	private IPortCostProvider portCostProvider;
	private IVesselProvider vesselProvider;
	private String vesselProviderKey;

	private IVessel currentVessel;
	private long sequenceAccumulator = 0;
	
	public PortCostFitnessComponent(String name, IFitnessCore core, String portCostProviderKey, String vesselProviderKey) {
		super(name, core);
		this.portCostProviderKey = portCostProviderKey;
		this.vesselProviderKey = vesselProviderKey;
	}
	
	@Override
	public void init(IOptimisationData data) {
		super.init(data);
		setPortCostProvider(data.getDataComponentProvider(portCostProviderKey, IPortCostProvider.class));
		setVesselProvider(data.getDataComponentProvider(vesselProviderKey, IVesselProvider.class));
	}

	@Override
	protected boolean reallyStartSequence(IResource resource) {
		currentVessel = vesselProvider.getVessel(resource);
		sequenceAccumulator = 0;
		return true;
	}

	@Override
	protected boolean reallyEvaluateObject(Object object, int time) {
		if (object instanceof PortDetails) {
			final PortDetails details = (PortDetails) object;
			sequenceAccumulator += portCostProvider.getPortCost(details.getPortSlot().getPort(), currentVessel, 
					details.getPortSlot().getPortType());
		}
		
		return true;
	}

	@Override
	protected long endSequenceAndGetCost() {
		currentVessel = null;
		return sequenceAccumulator;
	}

	public IPortCostProvider getPortCostProvider() {
		return portCostProvider;
	}

	public void setPortCostProvider(IPortCostProvider portCostProvider) {
		this.portCostProvider = portCostProvider;
	}

	public IVesselProvider getVesselProvider() {
		return vesselProvider;
	}

	public void setVesselProvider(IVesselProvider vesselProvider) {
		this.vesselProvider = vesselProvider;
	}
}
