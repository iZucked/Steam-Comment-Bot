package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

public interface ICharterOut {
	public ITimeWindow getTimeWindow();
	public int getDurationHours();
	public IPort getPort();
}
