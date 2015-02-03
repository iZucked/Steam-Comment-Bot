/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
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
	public List<IOptionsSequenceElement> getBasicSequence() {
		return delegate.getBasicSequence();
	}

	@Override
	public void setBasicSequence(final List<IOptionsSequenceElement> basicSequence) {
		delegate.setBasicSequence(basicSequence);
		reference.setBasicSequence(basicSequence);
	}

	@Override
	public IVessel getVessel() {
		return delegate.getVessel();
	}

	/**
	 * @param vessel
	 * @param vesselStartTime
	 */
	@Override
	public void setVessel(final IVessel vessel, final IResource resource, final int baseFuelPricePerMT) {
		delegate.setVessel(vessel, resource, baseFuelPricePerMT);
		reference.setVessel(vessel, resource, baseFuelPricePerMT);
	}

	@Override
	public void setBaseFuelPricePerMT(int baseFuelPricePerMT) {
		delegate.setBaseFuelPricePerMT(baseFuelPricePerMT);
		reference.setBaseFuelPricePerMT(baseFuelPricePerMT);
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
	public void setPortTimesRecord(final IPortTimesRecord portTimesRecord) {
		reference.setPortTimesRecord(portTimesRecord);
		delegate.setPortTimesRecord(portTimesRecord);
	}

	@Override
	public void setStartHeel(long heelVolumeInM3) {
		delegate.setStartHeel(heelVolumeInM3);
		reference.setStartHeel(heelVolumeInM3);
	}

	@Override
	public long getStartHeel() {
		return delegate.getStartHeel();
	}

	@Override
	public void setVesselCharterInRatePerDay(int charterInRatePerDay) {
		delegate.setVesselCharterInRatePerDay(charterInRatePerDay);
		reference.setVesselCharterInRatePerDay(charterInRatePerDay);
	}
}
