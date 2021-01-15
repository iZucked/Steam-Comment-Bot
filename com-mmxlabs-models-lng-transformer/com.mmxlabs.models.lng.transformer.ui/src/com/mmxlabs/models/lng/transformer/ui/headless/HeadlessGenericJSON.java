/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * This class represents a wrapper for JSON output logs from a headless algorithm run.
 * @author simonmcgregor
 *
 * @param <T>
 * @param <U>
 */
public abstract class HeadlessGenericJSON<T extends HeadlessGenericJSON.Params, U extends HeadlessGenericJSON.Metrics> {

	private String type;
	private Meta meta;
	@SuppressWarnings("null")
	private T params;
	@SuppressWarnings("null")
	private U metrics;

	/**
	 * Base class for metrics information; this should be subclassed to store detailed
	 * metrics for a particular algorithm.
	 * 
	 * @author simonmcgregor
	 *
	 */
	public static class Metrics {
		private long runtime;
		private long seed;
		
		
		public long getSeed() {
			return seed;
		}

		public void setSeed(long seed) {
			this.seed = seed;
		}

		public long getRuntime() {
			return runtime;
		}

		public void setRuntime(long runtime) {
			this.runtime = runtime;
		}

	}

	/**
	 * General information about the context in which the algorithm was run. 
	 * @author simonmcgregor
	 *
	 */
	public static class Meta {
		private String machineType;
		private String scenario;
		private String checkSum;
		private LocalDateTime date;
		private String useCase;
		private String version;
		private String client;		
		private Map<String, String> customInfo;
		private long maxHeapSize;

		public LocalDateTime getDate() {
			return date;
		}

		public void setDate(LocalDateTime date) {
			this.date = date;
		}

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

		public Map<String, String> getCustomInfo() {
			return customInfo;
		}

		public void setCustomInfo(Map<String, String> customInfo) {
			this.customInfo = customInfo;
		}

		public long getMaxHeapSize() {
			return maxHeapSize;
		}

		public void setMaxHeapSize(long maxHeapSize) {
			this.maxHeapSize = maxHeapSize;
		}

		public String getCheckSum() {
			return checkSum;
		}

		public void setCheckSum(String checkSum) {
			this.checkSum = checkSum;
		}

		public String getUseCase() {
			return useCase;
		}

		public void setUseCase(String useCase) {
			this.useCase = useCase;
		}
	}

	/**
	 * Parameters for the algorithm being run. Should be subclassed to 
	 * store detailed parameters for a particular algorithm.
	 * 
	 * @author simonmcgregor
	 *
	 */
	public static class Params {
		private int cores;

		public int getCores() {
			return cores;
		}

		public void setCores(int cores) {
			this.cores = cores;
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

	public T getParams() {
		return params;
	}

	public void setParams(T params) {
		this.params = params;
	}

	public U getMetrics() {
		return metrics;
	}

	public void setMetrics(U metrics) {
		this.metrics = metrics;
	}
}
