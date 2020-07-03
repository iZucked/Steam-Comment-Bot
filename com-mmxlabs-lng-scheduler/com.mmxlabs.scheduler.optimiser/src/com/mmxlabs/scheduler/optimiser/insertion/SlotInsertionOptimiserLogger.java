/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.insertion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.caches.MemoryUsageInfo;

/**
 * Class to implement simple logging of an optioniser run. Note: This is not thread-safe.
 * 
 * @author Simon Goodall
 *
 */
public class SlotInsertionOptimiserLogger {

	public static final String STAGE_NON_SHIPPED_PAIRS = "non-shipped-pairs";
	public static final String STAGE_FULL_SEARCH = "full-search";
	public static final String STAGE_PROCESS_SOLUTIONS = "process-solutions";
	private static final int DEFAULT_LOGGING_INTERVAL = 100000;			
	
	private long startTime = 0L;
	private long endTime = 0L;

	private List<String> stages = new ArrayList<>();
	private Map<String, Long> stageStartTimes = new HashMap<>();
	private Map<String, Long> stageEndTimes = new HashMap<>();
	private Map<String, MemoryUsageInfo> heapUsages = new HashMap<>();

	private int solutionsFound = 0;

	public Map<String, MemoryUsageInfo> getIterationHeapUsages() {
		return heapUsages;
	}

	public int getSolutionsFound() {
		return solutionsFound;
	}

	public void setSolutionsFound(int solutionsFound) {
		this.solutionsFound = solutionsFound;
	}

	public void begin() {
		startTime = System.currentTimeMillis();
		logCurrentMemoryUsage("Start of optioniser run");
	}

	public void done() {
		endTime = System.currentTimeMillis();
	}

	public void beginStage(String stage) {
		stages.add(stage);
	}

	public void doneStage(String stage) {
		stageEndTimes.put(stage, System.currentTimeMillis());
		logCurrentMemoryUsage(String.format("End of stage %s", stage));
	}

	public long getRuntime() {
		return endTime - startTime;
	}

	public long getRuntime(String stage) {
		return stageEndTimes.getOrDefault(stage, 0L) - stageStartTimes.getOrDefault(stage, 0L);
	}
	
	public void logCurrentMemoryUsage(String label) {
		Runtime runtime = Runtime.getRuntime();
		
		heapUsages.put(label, new MemoryUsageInfo(runtime.freeMemory(), runtime.totalMemory()));
	}

	public List<String> getStages() {
		return stages;
	}
	
	public void aggregate(List<SlotInsertionOptimiserLogger> slotLoggers) {
		aggregateLoggers(this, slotLoggers);
	}
	
	private static void aggregateLoggers(SlotInsertionOptimiserLogger result, List<SlotInsertionOptimiserLogger> slotLoggers) {
		for (var logger : slotLoggers) {			
			
			//Update startTime + endTime.
			if (result.startTime == 0 || result.startTime > logger.startTime) {
				result.startTime = logger.startTime;
			}
			if (result.endTime == 0 || result.endTime < logger.endTime) {
				result.endTime = logger.endTime;
			}
			
			//Go through stages maps.
			for (var stage : logger.getStages()) {			
				if (!result.stages.contains(stage)) {
					result.stages.add(stage);
				}
				
				if (result.stageStartTimes.get(stage) == null) {
					result.stageStartTimes.put(stage, logger.stageStartTimes.get(stage));
				}	
				
				if (result.stageStartTimes.get(stage) != null && logger.stageStartTimes.get(stage) != null &&
						result.stageStartTimes.get(stage) > logger.stageStartTimes.get(stage)) {
					result.stageStartTimes.put(stage, logger.stageStartTimes.get(stage));
				}
				if (result.stageEndTimes.get(stage) == null) {
					result.stageEndTimes.put(stage, logger.stageEndTimes.get(stage));
				}
				if (result.stageEndTimes.get(stage) != null && logger.stageEndTimes.get(stage) != null &&
						result.stageEndTimes.get(stage) < logger.stageEndTimes.get(stage)) {
					result.stageEndTimes.put(stage, logger.stageEndTimes.get(stage));
				}
			}

		}	
	}

	/**
	 * Returns the number of iterations to wait before logging memory usage.
	 * @return
	 */
	public int getLoggingInterval() {
		return DEFAULT_LOGGING_INTERVAL;
	}



}