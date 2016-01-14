package com.mmxlabs.scheduler.optimiser.curves;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class SlotPriceWindowData {

	ILoadOption loadOption;
	IDischargeOption dischargeOption;
	IPortTimeWindowsRecord record;
	int start;
	int end;
	
}