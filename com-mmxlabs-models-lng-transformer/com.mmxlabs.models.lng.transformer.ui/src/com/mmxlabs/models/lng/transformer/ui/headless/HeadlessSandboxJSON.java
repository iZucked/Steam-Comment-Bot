/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.util.List;
import java.util.Map;

import com.mmxlabs.common.caches.MemoryUsageInfo;

public class HeadlessSandboxJSON extends HeadlessGenericJSON<HeadlessSandboxJSON.Params, HeadlessSandboxJSON.Metrics> {

	public HeadlessSandboxJSON() {
	}

	/**
	 * Additional metrics information for optioniser runs.
	 */
	public static class Metrics extends HeadlessGenericJSON.Metrics {
		private Map<String, MemoryUsageInfo> memoryUsage;

		public Map<String, MemoryUsageInfo> getMemoryUsage() {
			return memoryUsage;
		}

		public void setMemoryUsage(Map<String, MemoryUsageInfo> memoryUsage) {
			this.memoryUsage = memoryUsage;
		}

	}

	/**
	 * Optioniser stage information
	 */
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

	/**
	 * Additional parameter information for optioniser runs
	 */
	public static class Params extends HeadlessGenericJSON.Params {

	}

}
