/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.util.List;

public class HeadlessOptioniserJSON extends HeadlessGenericJSON<HeadlessOptioniserJSON.Params, HeadlessOptioniserJSON.Metrics>{


	public HeadlessOptioniserJSON() {
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

	public static class Metrics extends HeadlessGenericJSON.Metrics {
		private OptioniserMetrics optioniserMetrics;
		private List<Stage> stages;

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

	public static class Params extends HeadlessGenericJSON.Params {
		private OptioniserProperties optioniserProperties;


		public OptioniserProperties getOptioniserProperties() {
			return optioniserProperties;
		}

		public void setOptioniserProperties(OptioniserProperties optioniserProperties) {
			this.optioniserProperties = optioniserProperties;
		}
	}



}
