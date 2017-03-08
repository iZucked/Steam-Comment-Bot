/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.util.Map;

import org.apache.commons.cli.Options;

/**
 * Simple class to pass into {@link Options#parseAndSet(String[], Object)}
 * 
 * @author Simon Goodall
 * 
 */

public final class SettingsOverride {

	// Lateness
	Map<String, Integer> latenessMap;

	public void setlatenessMap(Map<String, Integer> latenessMap) {
		this.latenessMap = latenessMap;
	}

	public Map<String, Integer> getlatenessMap() {
		return latenessMap;
	}

	// Similarity
	Map<String, Integer> similarityMap;

	public void setSimilarityMap(Map<String, Integer> similarityMap) {
		this.similarityMap = similarityMap;
	}

	public Map<String, Integer> getSimilarityMap() {
		return similarityMap;
	}
	
	// Move Frequency
	Map<String, Double> moveMap;
	
	public void setMoveMap(Map<String, Double> moveMap) {
		this.moveMap = moveMap;
	}

	public Map<String, Double> getMoveMap() {
		return moveMap;
	}


	private int iterations = 30000;
	private int seed = 1;

	private String scenario;
	private String output;
	private String outputPath;

	private String json;
	private String outputName;

	

	private int actionPlanTotalEvals = 5_000_000;
	private int actionPlanInRunEvals = 1_500_000;
	private int actionPlanMaxSearchDepth = 5_000;
	private boolean actionPlanVerboseLogger = false;


	private int idleTimeLow = 2_500;
	private int idleTimeHigh = 10_000;
	private int idleTimeEnd = 10_000;

	private boolean equalMoveDistributions = true;

	private boolean useLegacyCheck = false;
	private boolean useGuidedMoves = false;

	public boolean useLegacyCheck() {
		return useLegacyCheck;
	}

	public void setUseLegacyCheck(boolean useLegacyCheck) {
		this.useLegacyCheck = useLegacyCheck;
	}

	public boolean useGuidedMoves() {
		return useGuidedMoves;
	}

	public void setUseGuidedMoves(boolean useGuidedMoves) {
		this.useGuidedMoves = useGuidedMoves;
	}

	public boolean isEqualMoveDistributions() {
		return equalMoveDistributions;
	}

	public void setEqualMoveDistributions(boolean equalMoveDistributions) {
		this.equalMoveDistributions = equalMoveDistributions;
	}



	private boolean useRouletteWheel = false;

	public boolean isUseRouletteWheel() {
		return useRouletteWheel;
	}

	public void setUseRouletteWheel(boolean useRouletteWheel) {
		this.useRouletteWheel = useRouletteWheel;
	}

	public int getIdleTimeEnd() {
		return idleTimeEnd;
	}

	public void setIdleTimeEnd(int idleTimeEnd) {
		this.idleTimeEnd = idleTimeEnd;
	}

	public int getIdleTimeLow() {
		return idleTimeLow;
	}

	public void setIdleTimeLow(int idleTimeLow) {
		this.idleTimeLow = idleTimeLow;
	}

	public int getIdleTimeHigh() {
		return idleTimeHigh;
	}

	public void setIdleTimeHigh(int idleTimeHigh) {
		this.idleTimeHigh = idleTimeHigh;
	}

	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public final String getScenario() {
		return scenario;
	}

	public final void setScenario(final String scenario) {
		this.scenario = scenario;
	}

	public final int getIterations() {
		return iterations;
	}

	public final void setIterations(final int iterations) {
		this.iterations = iterations;
	}

	public final int getSeed() {
		return seed;
	}

	public final String getJSON() {
		return json;
	}

	public final void setSeed(final int seed) {
		this.seed = seed;
	}

	protected String getOutput() {
		return output;
	}

	protected void setOutput(String output) {
		this.output = output;
	}

	protected void setJSON(String json) {
		this.json = json;
	}

	public int getActionPlanTotalEvals() {
		return actionPlanTotalEvals;
	}

	public void setActionPlanTotalEvals(int actionPlanTotalEvals) {
		this.actionPlanTotalEvals = actionPlanTotalEvals;
	}

	public int getActionPlanInRunEvals() {
		return actionPlanInRunEvals;
	}

	public void setActionPlanInRunEvals(int actionPlanInRunEvals) {
		this.actionPlanInRunEvals = actionPlanInRunEvals;
	}

	public int getActionPlanMaxSearchDepth() {
		return this.actionPlanMaxSearchDepth;
	}

	public void setActionPlanMaxSearchDepth(int actionPlanMaxSearchDepth) {
		this.actionPlanMaxSearchDepth = actionPlanMaxSearchDepth;
	}


	public boolean isActionPlanVerboseLogger() {
		return actionPlanVerboseLogger;
	}

	public void setActionPlanVerboseLogger(boolean actionPlanVerboseLogger) {
		this.actionPlanVerboseLogger = actionPlanVerboseLogger;
	}

}
