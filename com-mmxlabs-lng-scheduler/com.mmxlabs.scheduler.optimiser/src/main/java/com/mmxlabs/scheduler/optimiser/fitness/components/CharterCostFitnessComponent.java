package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class CharterCostFitnessComponent<T> extends
		AbstractCargoSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {

	private IVesselProvider vesselProvider;

	final String vesselProviderKey;
	
	final private VoyagePlanIterator vpi = new VoyagePlanIterator();
	
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
			final List<VoyagePlan> plans, final int startTime) {
//		final IVessel vessel = vesselProvider.getVessel(resource);
//		vpi.setVoyagePlans(plans, startTime);
//		long hireCost = 0;
//		switch (vessel.getVesselInstanceType()) {
//		case SPOT_CHARTER:
//			// Check whether there are any load ports in the sequence
//			int loadTime = 0;
//			int endTime = 0;
//			boolean foundLoadPort = false;
//			
//			while (vpi.hasNextObject()) {
//				final Object obj = vpi.nextObject();
//				if (obj instanceof PortDetails) {
//					final PortDetails detail = (PortDetails) obj;
//					if (detail.getPortSlot().getPortType() == PortType.Load) {
//						loadTime = vpi.getCurrentTime();
//						foundLoadPort = true;
//					}
//					endTime = vpi.getCurrentTime();
//				}
//			}
//			
//			hireCost = Calculator.multiply(endTime - loadTime, vessel.getVesselClass()
//					.getHourlyCharterPrice());
//			
////			for (final VoyagePlan plan : plans) {
////				for (final Object obj : plan.getSequence()) {
////					if (obj instanceof PortDetails) {
////						final PortDetails detail = (PortDetails) obj;
////						if (detail.getPortSlot().getPortType() == PortType.Load) {
////							// Start costing from time loading begins
////							loadTime = detail.getStartTime();
////							foundLoadPort = true;
////							break;
////						}
////					}
////				}
////			}
////
////			if (foundLoadPort) {
////				final VoyagePlan plan = plans.get(plans.size() - 1);
////				final Object[] seq = plan.getSequence();
////				final Object obj = seq[seq.length - 1];
////				// We should always finish at a port
////				assert obj instanceof PortDetails;
////				final PortDetails detail = (PortDetails) obj;
////				final int arrivalTime = detail.getStartTime();
////
////				// check time of arrival at end port.
////				final int delta = arrivalTime - loadTime;
////				assert(delta >= 0);
////				hireCost = Calculator.multiply(delta, vessel.getVesselClass()
////						.getHourlyCharterPrice());
////			}
//
//			break;
//		case TIME_CHARTER:
//			// TODO not implemented
//			break;
//		}
		return hireCost;
	}

	boolean active;
	long hireCost;
	int firstLoadTime;
	int lastTime;
	int charterPrice;
	
	@Override
	public boolean shouldIterate() {
		return true;
	}

	@Override
	public void beginIterating(final IResource resource) {
		active = vesselProvider.getVessel(resource).getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER);
		firstLoadTime = -1;
		hireCost = 0;
		charterPrice = vesselProvider.getVessel(resource).getVesselClass().getHourlyCharterPrice();
	}

	@Override
	public void evaluateNextObject(Object object, int startTime) {
		if (object instanceof PortDetails) {
			final PortDetails detail = (PortDetails) object;
			if (detail.getPortSlot().getPortType() == PortType.Load) {
				if (firstLoadTime == -1)
					firstLoadTime = startTime;
			}
			lastTime = startTime;
		}
	}

	@Override
	public void endIterating() {
		if (firstLoadTime != -1) {
			hireCost = Calculator.multiply(lastTime - firstLoadTime, charterPrice);
		}
	}
}
