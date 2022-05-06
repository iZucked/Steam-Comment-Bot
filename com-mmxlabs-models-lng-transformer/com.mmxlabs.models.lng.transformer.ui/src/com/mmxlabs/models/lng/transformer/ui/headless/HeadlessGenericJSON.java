/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.time.LocalDateTime;
import java.util.List;
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
	private ScenarioMeta scenarioMeta;
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
	
	public static class ScenarioMeta {
		private int numCargoes;				//number of cargoes total
		private int numVessels;				//number of available vessels
		private int numLoads;				//number of loads
		private int numDischarges;			//number of discharges
		private int numSalesContracts;		//number of sales contracts
		private int numPurchaseContracts;	//number of purchase contracts
		private int shippedCargoes;  		//number of shipped cargos: number of FOB-DES cargos
		private int charterInSpotsCount;					//charter in markets description
		private int charterOutSpotsCount;					//charter out markets description
		private List<Integer> minTimeWindows;				//minimum time windows for each vessel availability
		private List<Integer> maxTimeWindows;				//maximum time windows for each vessel availability
		private Map<String, Integer> spotMarketOptions;		//spot market description
		private Map<String, Integer> vesselEvents;  		//vessel events description
		private Map<String, Integer> loadTimeWindow;		//time window of each load slot in hours
		private Map<String, Integer> dischargeTimeWindow;	//time window of each discharge slot in hours
		private Map<Integer, Integer> loadSlotDuration;		//duration of each load slot in hours
		private Map<Integer, Integer> dischargeSlotDuration;	//duration of each discharge slot in hours
		
		public int getNumCargoes() {
			return numCargoes;
		}

		public void setNumCargoes(int numCargoes) {
			this.numCargoes = numCargoes;
		}

		public int getNumVessels() {
			return numVessels;
		}

		public void setNumVessels(int numVessels) {
			this.numVessels = numVessels;
		}
		
		public int getNumLoads() {
			return numLoads;
		}

		public void setNumLoads(int numLoads) {
			this.numLoads = numLoads;
		}

		public int getNumDischarges() {
			return numDischarges;
		}

		public void setNumDischarges(int numDischarges) {
			this.numDischarges = numDischarges;
		}

		public int getNumSalesContracts() {
			return numSalesContracts;
		}

		public void setNumSalesContracts(int numSalesContracts) {
			this.numSalesContracts = numSalesContracts;
		}

		public int getNumPurchaseContracts() {
			return numPurchaseContracts;
		}

		public void setNumPurchaseContracts(int numPurchaseContracts) {
			this.numPurchaseContracts = numPurchaseContracts;
		}

		public int getShippedCargoes() {
			return shippedCargoes;
		}

		public void setShippedCargoes(int shippedCargoes) {
			this.shippedCargoes = shippedCargoes;
		}

		public int getCharterInSpotsCount() {
			return charterInSpotsCount;
		}

		public void setCharterInSpotsCount(int charterInSpotsCount) {
			this.charterInSpotsCount = charterInSpotsCount;
		}

		public int getCharterOutSpotsCount() {
			return charterOutSpotsCount;
		}

		public void setCharterOutSpotsCount(int charterOutSpotsCount) {
			this.charterOutSpotsCount = charterOutSpotsCount;
		}

		public List<Integer> getMinTimeWindows() {
			return minTimeWindows;
		}

		public void setMinTimeWindows(List<Integer> minTimeWindows) {
			this.minTimeWindows = minTimeWindows;
		}

		public List<Integer> getMaxTimeWindows() {
			return maxTimeWindows;
		}

		public void setMaxTimeWindows(List<Integer> maxTimeWindows) {
			this.maxTimeWindows = maxTimeWindows;
		}

		public Map<String, Integer> getSpotMarketOptions() {
			return spotMarketOptions;
		}

		public void setSpotMarketOptions(Map<String, Integer> spotMarketOptions) {
			this.spotMarketOptions = spotMarketOptions;
		}

		public Map<String, Integer> getVesselEvents() {
			return vesselEvents;
		}

		public void setVesselEvents(Map<String, Integer> vesselEvents) {
			this.vesselEvents = vesselEvents;
		}

		public Map<String, Integer> getLoadTimeWindow() {
			return loadTimeWindow;
		}

		public void setLoadTimeWindow(Map<String, Integer> loadTimeWindow) {
			this.loadTimeWindow = loadTimeWindow;
		}

		public Map<String, Integer> getDischargeTimeWindow() {
			return dischargeTimeWindow;
		}

		public void setDischargeTimeWindow(Map<String, Integer> dischargeTimeWindow) {
			this.dischargeTimeWindow = dischargeTimeWindow;
		}

		public Map<Integer, Integer> getLoadSlotDuration() {
			return loadSlotDuration;
		}

		public void setLoadSlotDuration(Map<Integer, Integer> loadSlotDuration) {
			this.loadSlotDuration = loadSlotDuration;
		}

		public Map<Integer, Integer> getDischargeSlotDuration() {
			return dischargeSlotDuration;
		}

		public void setDischargeSlotDuration(Map<Integer, Integer> dischargeSlotDuration) {
			this.dischargeSlotDuration = dischargeSlotDuration;
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

	public ScenarioMeta getScenarioMeta() {
		return scenarioMeta;
	}

	public void setScenarioMeta(ScenarioMeta scenarioMeta) {
		this.scenarioMeta = scenarioMeta;
	}
}
