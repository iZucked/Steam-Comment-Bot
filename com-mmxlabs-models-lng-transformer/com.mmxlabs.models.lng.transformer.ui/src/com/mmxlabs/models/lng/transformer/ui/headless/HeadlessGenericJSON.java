/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.time.LocalDateTime;

/**
 * This class represents a wrapper for JSON output logs from a headless algorithm run.
 * @author simonmcgregor
 *
 * @param <T>
 * @param <U>
 */
public class HeadlessGenericJSON<T extends HeadlessGenericJSON.Params, U extends HeadlessGenericJSON.Metrics> {

	private String type;
	private Meta meta;
	private T params;
	private U metrics;

	public HeadlessGenericJSON() {
	}	

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

	public static class Meta {
		private String machineType;
		private String scenario;
		private LocalDateTime date;


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

		private String version;
		private String client;
	}

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
