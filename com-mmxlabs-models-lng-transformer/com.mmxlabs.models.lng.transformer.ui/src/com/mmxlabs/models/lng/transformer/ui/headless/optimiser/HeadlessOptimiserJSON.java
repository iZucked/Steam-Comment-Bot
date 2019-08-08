/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.util.ArrayList;
import java.util.List;

public class HeadlessOptimiserJSON {

	private String type;
	private Meta meta;
	private Params params;
	private List<Metrics> runs;

	public HeadlessOptimiserJSON() {
		this.runs = new ArrayList<Metrics>();
	}
	
	public static class OptioniserMetrics {
		private int solutionsFound;
		private int solutionsReturned;

		public int getSolutionsFound() {
			return solutionsFound;
		}

		public void setSolutionsFound(int solutionsFound) {
			this.solutionsFound = solutionsFound;
		}

		public int getSolutionsReturned() {
			return solutionsReturned;
		}

		public void setSolutionsReturned(int solutionsReturned) {
			this.solutionsReturned = solutionsReturned;
		}
	}

	public static class OptioniserProperties {
		private int iterations;
		private String options;
		
		public String getOptions() {
			return options;
		}

		public void setOptions(String options) {
			this.options = options;
		}

		public int getIterations() {
			return iterations;
		}

		public void setIterations(int iterations) {
			this.iterations = iterations;
		}

		public String[] getLoadIds() {
			return loadIds;
		}

		public void setLoadIds(String[] loadIds) {
			this.loadIds = loadIds;
		}

		public String[] getDischargeIds() {
			return dischargeIds;
		}

		public void setDischargeIds(String[] dischargeIds) {
			this.dischargeIds = dischargeIds;
		}

		public String[] getEventIds() {
			return eventIds;
		}

		public void setEventIds(String[] eventIds) {
			this.eventIds = eventIds;
		}

		private String[] loadIds;
		private String[] dischargeIds;
		private String[] eventIds;
	}

	public static class Metrics {
		private long runtime;
		private OptioniserMetrics optioniserMetrics;

		public long getRuntime() {
			return runtime;
		}

		public void setRuntime(long runtime) {
			this.runtime = runtime;
		}

		public OptioniserMetrics getOptioniserMetrics() {
			return optioniserMetrics;
		}

		public void setOptioniserMetrics(OptioniserMetrics optioniserMetrics) {
			this.optioniserMetrics = optioniserMetrics;
		}

		public List<Stage> getStages() {
			return stages;
		}

		public void setStages(List<Stage> stages) {
			this.stages = stages;
		}

		private List<Stage> stages;
	}

	public static class Stage {
		private String stage;

		public String getStage() {
			return stage;
		}

		public void setStage(String stage) {
			this.stage = stage;
		}

		public long getRuntime() {
			return runtime;
		}

		public void setRuntime(long runtime) {
			this.runtime = runtime;
		}

		private long runtime;

	}

	public static class Meta {
		private String machineType;
		private String scenario;

		public String getScenario() {
			return scenario;
		}

		public void setScenario(String scenario) {
			this.scenario = scenario;
		}

		public String getMachineType() {
			return machineType;
		}

		public void setMachineType(String machineType) {
			this.machineType = machineType;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getClient() {
			return client;
		}

		public void setClient(String client) {
			this.client = client;
		}

		private String version;
		private String client;
	}

	public static class Params {
		private int cores;
		private OptioniserProperties optioniserProperties;

		public int getCores() {
			return cores;
		}

		public void setCores(int cores) {
			this.cores = cores;
		}

		public OptioniserProperties getOptioniserProperties() {
			return optioniserProperties;
		}

		public void setOptioniserProperties(OptioniserProperties optioniserProperties) {
			this.optioniserProperties = optioniserProperties;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}

	public List<Metrics> getRuns() {
		return runs;
	}

	public void clearRuns() {
		runs.clear();
	}
	
	public void addRun(Metrics metrics) {
		runs.add(metrics);
	}
}
