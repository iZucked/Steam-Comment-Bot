package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class CharterCostFitnessComponent<T> extends
		AbstractCargoSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {

	private IVesselProvider vesselProvider;
	private IPortTypeProvider portTypeProvider;

	public CharterCostFitnessComponent(String name,
			CargoSchedulerFitnessCore<T> core) {
		super(name, core);
	}

	@Override
	public void init(IOptimisationData<T> data) {
		this.vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		this.portTypeProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class);
	}

	@Override
	public void evaluateSequence(IResource resource, ISequence<T> sequence,
			IAnnotatedSequence<T> annotatedSequence, boolean newSequence) {
		final IVessel vessel = vesselProvider.getVessel(resource);
		long hireCost = 0;
		switch (vessel.getVesselInstanceType()) {
			case SPOT_CHARTER:
				// Check whether there are any load ports in the sequence
				int loadTime = 0;
				boolean foundLoadPort = false;
				for (final T element : sequence) {
					final PortType portType = portTypeProvider.getPortType(element);
					if (portType.equals(portType.Load)) {
						// load port located
						loadTime = annotatedSequence.getAnnotation(element, 
								SchedulerConstants.AI_visitInfo, 
								IPortVisitEvent.class).getStartTime(); //TODO should this be .getEndTime instead?
						foundLoadPort = true;
						break;
					}
				}
				
				if (foundLoadPort) {
					// check time of arrival at end port.
					// TODO change startendrequirementprovider to have hooks into the sequence elements
					// instead of doing this (but then this is probably quicker than a hashmap lookup)
					final T lastElement = sequence.get(sequence.size() - 1);
					final int arrivalTime = annotatedSequence.getAnnotation(lastElement, 
							SchedulerConstants.AI_visitInfo, IPortVisitEvent.class).getStartTime(); //TODO check this is arrival time at last port
					final int delta = arrivalTime - loadTime;
					hireCost = delta * vessel.getVesselClass().getHourlyCharterPrice();
				}
				
				break;
			case TIME_CHARTER:
				//TODO not implemented
				break;
		}
		
		updateFitness(resource, hireCost, newSequence);
	}

}
