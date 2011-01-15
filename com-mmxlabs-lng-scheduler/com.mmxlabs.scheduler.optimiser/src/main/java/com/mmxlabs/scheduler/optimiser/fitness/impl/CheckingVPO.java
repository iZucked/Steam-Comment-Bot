/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class CheckingVPO<T> implements IVoyagePlanOptimiser<T> {
	final private IVoyagePlanOptimiser<T> reference, delegate;
	
	public CheckingVPO(IVoyagePlanOptimiser<T> reference,
			IVoyagePlanOptimiser<T> delegate) {
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
		
		if (ref.toString().equals(res.toString())  == false) {
			System.err.println("Checking VPO Error: (plans are different)");
			System.err.println("   reference value:" + ref.toString());
			System.err.println("    delegate value:" + res.toString());
		} else if (reference.getBestCost() != delegate.getBestCost()) {
			System.err.println("Checking VPO Error: (Costs are different)");
			System.err.println("    reference cost:" + reference.getBestCost());
			System.err.println("     delegate cost:" + delegate.getBestCost());
		}
		
		return res;
	}

	@Override
	public List<Object> getBasicSequence() {
		return delegate.getBasicSequence();
	}

	@Override
	public void setBasicSequence(List<Object> basicSequence) {
		delegate.setBasicSequence(basicSequence);
		reference.setBasicSequence(basicSequence);
	}

	@Override
	public IVessel getVessel() {
		return delegate.getVessel();
	}

	@Override
	public void setVessel(IVessel vessel) {
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
	public ILNGVoyageCalculator<T> getVoyageCalculator() {
		return delegate.getVoyageCalculator();
	}

	@Override
	public void addChoice(IVoyagePlanChoice choice) {
		reference.addChoice(choice);
		delegate.addChoice(choice);
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser#setArrivalTimes(java.util.List)
	 */
	@Override
	public void setArrivalTimes(List<Integer> currentTimes) {
		reference.setArrivalTimes(currentTimes);
		delegate.setArrivalTimes(currentTimes);	
	}
}
