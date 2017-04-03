/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.ICharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public class CharterOutVesselEvent extends VesselEvent implements ICharterOutVesselEvent {

	private IHeelOptionConsumer heelOptionsConsumer;
	private IHeelOptionSupplier heelOptionsSupplier;

	private long hireOutRevenue;
	private long repositioning;
	private long ballastBonus;

	public CharterOutVesselEvent(final ITimeWindow timeWindow, final IPort port, IHeelOptionConsumer heelOptionsConsumer, IHeelOptionSupplier heelOptionsSupplier) {
		super(timeWindow, port, port);
		this.heelOptionsConsumer = heelOptionsConsumer;
		this.heelOptionsSupplier = heelOptionsSupplier;
	}

	public CharterOutVesselEvent(final ITimeWindow timeWindow, final IPort startPort, final IPort endPort, IHeelOptionConsumer heelOptionsConsumer, IHeelOptionSupplier heelOptionsSupplier) {
		super(timeWindow, startPort, endPort);
		this.heelOptionsConsumer = heelOptionsConsumer;
		this.heelOptionsSupplier = heelOptionsSupplier;
	}

	@Override
	public IHeelOptionConsumer getHeelOptionsConsumer() {
		return heelOptionsConsumer;
	}

	public void setHeelOptionsConsumer(IHeelOptionConsumer heelOptionsConsumer) {
		this.heelOptionsConsumer = heelOptionsConsumer;
	}

	@Override
	public IHeelOptionSupplier getHeelOptionsSupplier() {
		return heelOptionsSupplier;
	}

	public void setHeelOptionsSupplier(IHeelOptionSupplier heelOptionsSupplier) {
		this.heelOptionsSupplier = heelOptionsSupplier;
	}

	@Override
	public long getHireOutRevenue() {
		return hireOutRevenue;
	}

	@Override
	public long getRepositioning() {
		return repositioning;
	}

	@Override
	public void setHireOutRevenue(final long hireCost) {
		this.hireOutRevenue = hireCost;
	}

	@Override
	public void setRepositioning(final long repositioning) {
		this.repositioning = repositioning;
	}

	@Override
	public long getBallastBonus() {
		return ballastBonus;
	}

	@Override
	public void setBallastBonus(final long ballastBonus) {
		this.ballastBonus = ballastBonus;
	}
}
