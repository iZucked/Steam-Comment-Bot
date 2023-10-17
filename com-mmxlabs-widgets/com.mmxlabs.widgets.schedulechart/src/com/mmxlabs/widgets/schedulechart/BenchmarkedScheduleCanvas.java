/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.widgets.schedulechart.providers.ScheduleChartProviders;

public class BenchmarkedScheduleCanvas extends ScheduleCanvas {
	
	private static final Logger LOG = LoggerFactory.getLogger(BenchmarkedScheduleCanvas.class);
	
	private long totalTime = 0;
	private int numRepaints = 0;

	public BenchmarkedScheduleCanvas(Composite parent, ScheduleChartProviders providers) {
		super(parent, providers);
	}
	
	public BenchmarkedScheduleCanvas(Composite parent, ScheduleChartProviders providers, IScheduleChartSettings settings) {
		super(parent, providers, settings);
	}

	@Override
	protected void repaint(GC gc) {
		long startTime = System.nanoTime();
		super.repaint(gc);
		long endTime = System.nanoTime();
		totalTime += (endTime - startTime);
		numRepaints++;
		System.out.println("Num repaints: " + numRepaints + ", Avg repaint time: " + (totalTime / numRepaints) + "ns");
	}
	

}
