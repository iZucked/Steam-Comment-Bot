package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class CharterCostFitnessComponent<T> extends
		AbstractCargoSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {

	private IVesselProvider vesselProvider;

	final String vesselProviderKey;
	
	public CharterCostFitnessComponent(final String name, final String vesselProviderKey,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
		this.vesselProviderKey = vesselProviderKey;
	}

	@Override
	public void init(final IOptimisationData<T> data) {
		this.vesselProvider = data.getDataComponentProvider(
				vesselProviderKey, IVesselProvider.class);
	}

	@Override
	public long rawEvaluateSequence(final IResource resource, final ISequence<T> sequence,
			final List<VoyagePlan> plans) {
		final IVessel vessel = vesselProvider.getVessel(resource);
		long hireCost = 0;
		switch (vessel.getVesselInstanceType()) {
		case SPOT_CHARTER:
			// Check whether there are any load ports in the sequence
			int loadTime = 0;
			boolean foundLoadPort = false;
			for (final VoyagePlan plan : plans) {
				for (final Object obj : plan.getSequence()) {
					if (obj instanceof IPortDetails) {
						final IPortDetails detail = (IPortDetails) obj;
						if (detail.getPortSlot().getPortType() == PortType.Load) {
							// Start costing from time loading begins
							loadTime = detail.getStartTime();
							foundLoadPort = true;
							break;
						}
					}
				}
			}

			if (foundLoadPort) {
				final VoyagePlan plan = plans.get(plans.size() - 1);
				final Object[] seq = plan.getSequence();
				final Object obj = seq[seq.length - 1];
				// We should always finish at a port
				assert obj instanceof IPortDetails;
				final IPortDetails detail = (IPortDetails) obj;
				final int arrivalTime = detail.getStartTime();

				// check time of arrival at end port.
				final int delta = arrivalTime - loadTime;
				assert(delta >= 0);
				hireCost = Calculator.multiply(delta, vessel.getVesselClass()
						.getHourlyCharterPrice());
			}

			break;
		case TIME_CHARTER:
			// TODO not implemented
			break;
		}
		return hireCost;
	}
}
