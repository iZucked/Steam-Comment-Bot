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

	public static String[] moveFrequencyParameters = new String[] { "insert-optional-frequency", "remove-optional-frequency", "swap-segments-frequency", "move-segments-frequency",
			"swap-tails-frequency", "shuffle-elements-frequency",

	};
	public static String[] latenessComponentParameters = new String[] { "lcp-set-prompt-period", "lcp-set-prompt-lowWeight", "lcp-set-prompt-highWeight", "lcp-set-midTerm-period",
			"lcp-set-midTerm-lowWeight", "lcp-set-midTerm-highWeight", "lcp-set-beyond-period", "lcp-set-beyond-lowWeight", "lcp-set-beyond-highWeight", };

	public static String[] similarityComponentParameters = new String[] { "scp-set-low-thresh", "scp-set-low-weight", "scp-set-med-thresh", "scp-set-med-weight", "scp-set-high-thresh",
			"scp-set-high-weight", "scp-set-outOfBounds-weight", };

	private int iterations = 30000;
	private int seed = 1;

	private String scenario;
	private String output;
	private String outputPath;

	private String json;
	private String outputName;

	Map<String, Integer> latenessParameterMap;
	Map<String, Integer> similarityParameterMap;
	Map<String, Double> moveFrequencyParameterMap;

	private int actionPlanTotalEvals = 5_000_000;
	private int actionPlanInRunEvals = 1_500_000;
	private int actionPlanMaxSearchDepth = 5_000;
	private boolean actionPlanVerboseLogger = false;

	private boolean movesUseLoopingSCMG = false;

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

	public Map<String, Double> getMoveFrequencyParameterMap() {
		return moveFrequencyParameterMap;
	}

	public void setMoveFrequencyParameterMap(Map<String, Double> moveFrequencyParameterMap) {
		this.moveFrequencyParameterMap = moveFrequencyParameterMap;
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

	public void setlatenessParameterMap(Map<String, Integer> latenessParameterMap) {
		this.latenessParameterMap = latenessParameterMap;
	}

	public Map<String, Integer> getlatenessParameterMap() {
		return latenessParameterMap;
	}

	public void setSimilarityParameterMap(Map<String, Integer> similarityParameterMap) {
		this.similarityParameterMap = similarityParameterMap;
	}

	public Map<String, Integer> getSimilarityParameterMap() {
		return similarityParameterMap;
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

	public boolean isMovesUseLoopingSCMG() {
		return movesUseLoopingSCMG;
	}

	public void setMovesUseLoopingSCMG(boolean movesUseLoopingSCMG) {
		this.movesUseLoopingSCMG = movesUseLoopingSCMG;
	}

	public boolean isActionPlanVerboseLogger() {
		return actionPlanVerboseLogger;
	}

	public void setActionPlanVerboseLogger(boolean actionPlanVerboseLogger) {
		this.actionPlanVerboseLogger = actionPlanVerboseLogger;
	}

}
