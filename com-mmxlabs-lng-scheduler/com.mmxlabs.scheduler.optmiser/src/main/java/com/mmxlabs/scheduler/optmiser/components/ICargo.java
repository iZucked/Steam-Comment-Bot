package com.mmxlabs.scheduler.optmiser.components;

import com.mmxlabs.optimiser.components.ITimeWindow;

public interface ICargo {

	IPort getLoadPort();
	
	ITimeWindow getLoadWindow();
	
	IPort getDischargePort();
	
	ITimeWindow getDischargeWindow();

	String getId();
}
