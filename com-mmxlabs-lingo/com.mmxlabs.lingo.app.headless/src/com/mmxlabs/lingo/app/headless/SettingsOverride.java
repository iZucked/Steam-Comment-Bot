/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
	public static String[] latenessComponentParameters = new String[] {
		"lcp-set-prompt-period",
		"lcp-set-prompt-lowWeight",
		"lcp-set-prompt-highWeight",
		"lcp-set-midTerm-period",
		"lcp-set-midTerm-lowWeight",
		"lcp-set-midTerm-highWeight",
		"lcp-set-beyond-period",
		"lcp-set-beyond-lowWeight",
		"lcp-set-beyond-highWeight",
	};
	
	public static String[] similarityComponentParameters = new String[] {
		"scp-set-low-thresh",
		"scp-set-low-weight",
		"scp-set-med-thresh",
		"scp-set-med-weight",
		"scp-set-high-thresh",
		"scp-set-high-weight",
		"scp-set-outOfBounds-weight",
	};
	
	private int iterations = 30000;
	private int seed = 1;

	private String scenario;
	private String output;
	private String outputPath;
	

	private String json;
	private String outputName;

	Map<String, Integer> latenessParameterMap;
	Map<String, Integer> similarityParameterMap;
	
	private int actionPlanTotalEvals = 5_000_000;
	private int actionPlanInRunEvals = 1_500_000;
	private int actionPlanMaxSearchDepth = 5_000;

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
	
}
