package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.components.ITimeWindow;

public interface ICargo {

	IPort getLoadPort();
	
	ITimeWindow getLoadWindow();
	
	IPort getDischargePort();
	
	ITimeWindow getDischargeWindow();

	String getId();
}
