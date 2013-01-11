/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.components.portcost.impl.PortCostAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
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
	private String vesselProviderKey;
	private String portSlotProviderKey;
	
	private IPortCostProvider portCostProvider;
	private IVesselProvider vesselProvider;
	private IPortSlotProvider portSlotProvider;
	

	private IVessel currentVessel;
	private long sequenceAccumulator = 0;
	
	public PortCostFitnessComponent(String name, IFitnessCore core, String portCostProviderKey, String vesselProviderKey, String portSlotProviderKey) {
		super(name, core);
		this.portCostProviderKey = portCostProviderKey;
		this.vesselProviderKey = vesselProviderKey;
		this.portSlotProviderKey = portSlotProviderKey;
	}
	
	@Override
	public void init(IOptimisationData data) {
		super.init(data);
		setPortCostProvider(data.getDataComponentProvider(portCostProviderKey, IPortCostProvider.class));
		setVesselProvider(data.getDataComponentProvider(vesselProviderKey, IVesselProvider.class));
		setPortSlotProvider(data.getDataComponentProvider(portSlotProviderKey, IPortSlotProvider.class));
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
			final IPortSlot slot = details.getOptions().getPortSlot();
			long portCost = portCostProvider.getPortCost(slot.getPort(), currentVessel, 
					slot.getPortType());
			sequenceAccumulator += portCost;
		}
		
		return true;
	}

	@Override
	protected boolean reallyAnnotateObject(Object object, int time,IAnnotatedSolution solution) {
		if (object instanceof PortDetails) {
			final PortDetails details = (PortDetails) object;
			final IPortSlot slot = details.getOptions().getPortSlot();
			final long cost = portCostProvider.getPortCost(slot.getPort(), currentVessel, 
					slot.getPortType());
			
			solution.getElementAnnotations().setAnnotation(portSlotProvider.getElement(slot), SchedulerConstants.AI_portCostInfo, new PortCostAnnotation(cost));
			
			sequenceAccumulator += cost;
		}
		
		return true;
	}

	@Override
	protected long endSequenceAndGetCost() {
		currentVessel = null;
		return sequenceAccumulator / Calculator.ScaleFactor;
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

	public IPortSlotProvider getPortSlotProvider() {
		return portSlotProvider;
	}

	public void setPortSlotProvider(IPortSlotProvider portSlotProvider) {
		this.portSlotProvider = portSlotProvider;
	}
}
