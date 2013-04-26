/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import javax.inject.Inject;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness based on lateness.
 * 
 * @author Simon Goodall
 * 
 */
public final class LatenessComponent extends AbstractPerRouteSchedulerFitnessComponent implements IFitnessComponent {

	private long accumulator = 0;
	private final String dcpStartendrequirementprovider;
	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	public IStartEndRequirementProvider getStartEndRequirementProvider() {
		return startEndRequirementProvider;
	}

	public void setStartEndRequirementProvider(final IStartEndRequirementProvider startEndRequirementProvider) {
		this.startEndRequirementProvider = startEndRequirementProvider;
	}

	public LatenessComponent(final String name, final String dcpStartendrequirementprovider, final CargoSchedulerFitnessCore core) {
		super(name, core);
		this.dcpStartendrequirementprovider = dcpStartendrequirementprovider;
	}

	@Override
	public void init(final IOptimisationData data) {
		setStartEndRequirementProvider(data.getDataComponentProvider(dcpStartendrequirementprovider, IStartEndRequirementProvider.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		accumulator = 0;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyEvaluateObject(java.lang.Object, int)
	 */
	@Override
	protected boolean reallyEvaluateObject(final Object object, final int time) {
		if (object instanceof PortDetails) {
			final PortDetails detail = (PortDetails) object;
			final IPortSlot portSlot = detail.getOptions().getPortSlot();
			final ITimeWindow tw;

			if (portSlot instanceof StartPortSlot) {
				final IStartEndRequirement req = startEndRequirementProvider.getStartRequirement(currentResource);
				tw = req.getTimeWindow();
			} else if (portSlot instanceof EndPortSlot) {
				final IStartEndRequirement req = startEndRequirementProvider.getEndRequirement(currentResource);
				tw = req.getTimeWindow();
			} else {
				tw = portSlot.getTimeWindow();
			}

			if ((tw != null) && (time > tw.getEnd())) {
				// addDiscountedValue(time, 1000000*(time - tw.getEnd()));
				accumulator += getDiscountedValue(time, (time - tw.getEnd()));
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#endSequenceAndGetCost()
	 */
	@Override
	protected long endSequenceAndGetCost() {
		return accumulator;
	}
}
