/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

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
		AbstractPerRouteSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {
	
	private static final int PENALTY = 1000000;
	private long accumulator = 0;

	public LatenessComponent(final String name,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
	}


	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		accumulator = 0;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyEvaluateObject(java.lang.Object, int)
	 */
	@Override
	protected boolean reallyEvaluateObject(Object object, int time) {
		if (object instanceof PortDetails) {
			final PortDetails detail = (PortDetails) object;
			final ITimeWindow tw = detail.getPortSlot().getTimeWindow();
			
			if (tw != null && time > tw.getEnd()) {
//				addDiscountedValue(time, 1000000*(time - tw.getEnd()));
				accumulator  += getDiscountedValue(time, (long)PENALTY * (time - tw.getEnd()));
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#endSequenceAndGetCost()
	 */
	@Override
	protected long endSequenceAndGetCost() {
		return accumulator;
	}
}
