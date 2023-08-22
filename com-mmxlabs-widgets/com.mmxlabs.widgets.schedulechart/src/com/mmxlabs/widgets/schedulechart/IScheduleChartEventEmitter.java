/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.function.Consumer;

public interface IScheduleChartEventEmitter {

	boolean addScheduleEventListener(IScheduleChartEventListener l);

	boolean removeScheduleEventListener(IScheduleChartEventListener l);

	void fireScheduleEvent(Consumer<IScheduleChartEventListener> f);

}
