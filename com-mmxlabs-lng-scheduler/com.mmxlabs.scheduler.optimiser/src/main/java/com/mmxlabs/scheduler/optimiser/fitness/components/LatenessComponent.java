package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.List;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness
 * based on lateness.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class LatenessComponent<T> extends
		AbstractCargoSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {

//	private VoyagePlanIterator voyagePlanIterator = 
//		new VoyagePlanIterator();
	
	public LatenessComponent(final String name,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
	}

	@Override
	public final long rawEvaluateSequence(final IResource resource,
			final ISequence<T> sequence, final List<VoyagePlan> plans, final int startTime) {
//		if (plans.size() == 0) return 0;
//		long lateness = 0;
////		if (true) {
//		voyagePlanIterator.setVoyagePlans(plans, startTime);
//		while (voyagePlanIterator.hasNextObject()) {
//			final Object obj = voyagePlanIterator.nextObject();
//			if (obj instanceof PortDetails) {
//				final PortDetails detail = (PortDetails) obj;
//				final ITimeWindow tw = detail.getPortSlot().getTimeWindow();
//				final int arrival = voyagePlanIterator.getCurrentTime();
//				
//				if (tw != null && arrival > tw.getEnd()) {
//					lateness += arrival - tw.getEnd();
//				}
//				
//			}
//		}
////		} else {
////		for (final VoyagePlan plan : plans) {
////			for (final Object obj : plan.getSequence()) {
////				if (obj instanceof PortDetails) {
////					final PortDetails detail = (PortDetails) obj;
////					final int arrival = detail.getStartTime();
////					final ITimeWindow tw = detail.getPortSlot().getTimeWindow();
////
////					if (tw != null && arrival > tw.getEnd()) {
////						lateness += arrival - tw.getEnd();
////					}
////				}
////			}
////		}
////		}
//		// TODO: Hack in a weighting
//		lateness *= 1000000;

		return lateness;
	}

	@Override
	public void init(final IOptimisationData<T> data) {
		
	}

	@Override
	public final boolean shouldIterate() {
		return true;
	}

	long lateness = 0;
	
	@Override
	public final void beginIterating(final IResource resource) {
		lateness = 0;
	}

	@Override
	public final void evaluateNextObject(final Object object, final int startTime) {
		if (object instanceof PortDetails) {
			final PortDetails detail = (PortDetails) object;
			final ITimeWindow tw = detail.getPortSlot().getTimeWindow();
			
			if (tw != null && startTime > tw.getEnd()) {
				lateness += startTime - tw.getEnd();
			}
		}
	}

	@Override
	public final void endIterating() {
		lateness *= 1000000;
	}
}
