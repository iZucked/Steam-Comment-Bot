package com.mmxlabs.scheduler.optmiser.components.impl;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.scheduler.optmiser.components.ICargo;
import com.mmxlabs.scheduler.optmiser.components.IPort;

public class Cargo implements ICargo {

	private IPort loadPort;

	private IPort dischargePort;

	private ITimeWindow loadWindow;

	private ITimeWindow dischargeWindow;

	private String id;

	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public IPort getDischargePort() {
		return dischargePort;
	}

	@Override
	public ITimeWindow getDischargeWindow() {
		return dischargeWindow;
	}

	@Override
	public IPort getLoadPort() {
		return loadPort;
	}

	@Override
	public ITimeWindow getLoadWindow() {
		return loadWindow;
	}

	public void setLoadPort(final IPort loadPort) {
		this.loadPort = loadPort;
	}

	public void setDischargePort(final IPort dischargePort) {
		this.dischargePort = dischargePort;
	}

	public void setLoadWindow(final ITimeWindow loadWindow) {
		this.loadWindow = loadWindow;
	}

	public void setDischargeWindow(final ITimeWindow dischargeWindow) {
		this.dischargeWindow = dischargeWindow;
	}

	@Override
	public String getId() {
		return id;
	}

}
