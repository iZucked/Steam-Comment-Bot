/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class CheckingVPO implements IVoyagePlanOptimiser {
	final private IVoyagePlanOptimiser reference, delegate;
	private static final Logger log = LoggerFactory.getLogger(CheckingVPO.class);

	public CheckingVPO(final IVoyagePlanOptimiser reference, final IVoyagePlanOptimiser delegate) {
		super();
		this.reference = reference;
		this.delegate = delegate;
	}

	@Override
	public void init() {
		reference.init();
		delegate.init();
	}

	@Override
	public void reset() {
		reference.reset();
		delegate.reset();
	}

	@Override
	public void dispose() {
		reference.dispose();
		delegate.dispose();
	}

	@Override
	public VoyagePlan optimise() {
		final VoyagePlan ref = reference.optimise();
		final VoyagePlan res = delegate.optimise();

		if (ref.toString().equals(res.toString()) == false) {
			log.error("Checking VPO Error: (plans are different)");
			log.error("   reference value:" + ref.toString());
			log.error("    delegate value:" + res.toString());
		} else if (reference.getBestCost() != delegate.getBestCost()) {
			log.error("Checking VPO Error: (Costs are different)");
			log.error("    reference cost:" + reference.getBestCost());
			log.error("     delegate cost:" + delegate.getBestCost());
		}

		return res;
	}

	@Override
	public List<Object> getBasicSequence() {
		return delegate.getBasicSequence();
	}

	@Override
	public void setBasicSequence(final List<Object> basicSequence) {
		delegate.setBasicSequence(basicSequence);
		reference.setBasicSequence(basicSequence);
	}

	@Override
	public IVessel getVessel() {
		return delegate.getVessel();
	}

	@Override
	public void setVessel(final IVessel vessel) {
		delegate.setVessel(vessel);
		reference.setVessel(vessel);
	}

	@Override
	public long getBestCost() {
		return delegate.getBestCost();
	}

	@Override
	public VoyagePlan getBestPlan() {
		return delegate.getBestPlan();
	}

	@Override
	public ILNGVoyageCalculator getVoyageCalculator() {
		return delegate.getVoyageCalculator();
	}

	@Override
	public void addChoice(final IVoyagePlanChoice choice) {
		reference.addChoice(choice);
		delegate.addChoice(choice);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser#setArrivalTimes(java.util.List)
	 */
	@Override
	public void setArrivalTimes(final List<Integer> currentTimes) {
		reference.setArrivalTimes(currentTimes);
		delegate.setArrivalTimes(currentTimes);
	}
}
