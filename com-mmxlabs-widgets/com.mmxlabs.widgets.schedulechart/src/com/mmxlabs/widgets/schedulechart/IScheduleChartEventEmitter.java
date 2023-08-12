package com.mmxlabs.widgets.schedulechart;

import java.util.function.Consumer;

public interface IScheduleChartEventEmitter {
	void fireScheduleEvent(Consumer<IScheduleChartEventListener> f);
}
