/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class GeneratedCharterOutOptionCache {

	private final class CacheKey {

		// Hashcode / Equals fields
		final int loadStartTime;
		final long loadStartingHeel;
		final int ballastStartTime;
		final int availableTime;
		final IPortSlot firstLoad;
		final IPortSlot discharge;
		final IPortSlot nextLoad;
		final IVesselAvailability vesselAvailability;
		final int hashCode;

		public CacheKey(final int loadStartTime, final long loadStartingHeel, final int ballastStartTime, final int availableTime, final IPortSlot firstLoad, final IPortSlot discharge,
				final IPortSlot nextLoad, final IVesselAvailability vesselAvailability) {
			this.loadStartTime = loadStartTime;
			this.loadStartingHeel = loadStartingHeel;
			this.ballastStartTime = ballastStartTime;
			this.availableTime = availableTime;
			this.firstLoad = firstLoad;
			this.discharge = discharge;
			this.nextLoad = nextLoad;
			this.vesselAvailability = vesselAvailability;
			this.hashCode = createHashCode();
		}

		private final int createHashCode() {
			int result = 1;
			final int prime = 31;
			result = (prime * result) + loadStartTime;
			result = (prime * result) + (int) loadStartingHeel;
			result = (prime * result) + ballastStartTime;
			result = (prime * result) + availableTime;
			result = (prime * result) + firstLoad.hashCode();
			result = (prime * result) + discharge.hashCode();
			result = (prime * result) + nextLoad.hashCode();
			result = (prime * result) + vesselAvailability.hashCode();
			return result;
		}

		@Override
		public final int hashCode() {
			return this.hashCode;
		}

		public void printKey() {
			System.out.println(
					this.ballastStartTime + "  - " + this.availableTime + "  - " + this.firstLoad + "  - " + this.discharge + "  - " + this.nextLoad + "  - " + this.vesselAvailability + "  - ");
		}

		@Override
		public final boolean equals(final Object obj) {
			if (obj instanceof CacheKey) {
				final CacheKey other = (CacheKey) obj;
				return loadStartTime == other.loadStartTime && loadStartingHeel == other.loadStartingHeel && ballastStartTime == other.ballastStartTime && availableTime == other.availableTime
						&& firstLoad == other.firstLoad && discharge == other.discharge && nextLoad == other.nextLoad && vesselAvailability == other.vesselAvailability;
			}
			return false;
		}
	}

	private Map<CacheKey, Pair<List<Pair<VoyagePlan, IPortTimesRecord>>, GeneratedCharterOutOption>> cachedSolutions = new HashMap<>();

	public GeneratedCharterOutOptionCache() {

	}

	public void addSplitVoyagePlans(final int loadStartTime, final long loadStartingHeel, final int ballastStartTime, final int availableTime, final IPortSlot firstLoad, final IPortSlot discharge,
			final IPortSlot nextLoad, final IVesselAvailability vesselAvailability, final Pair<List<Pair<VoyagePlan, IPortTimesRecord>>, GeneratedCharterOutOption> solution) {
		cachedSolutions.put(new CacheKey(loadStartTime, loadStartingHeel, ballastStartTime, availableTime, firstLoad, discharge, nextLoad, vesselAvailability), solution);
	}

	public void printKey(final int loadStartTime, final long loadStartingHeel, final int ballastStartTime, final int availableTime, final IPortSlot firstLoad, final IPortSlot discharge,
			final IPortSlot nextLoad, final IVesselAvailability vesselAvailability) {
		CacheKey c = new CacheKey(loadStartTime, loadStartingHeel, ballastStartTime, availableTime, firstLoad, discharge, nextLoad, vesselAvailability);
		c.printKey();
	}

	public List<Pair<VoyagePlan, IPortTimesRecord>> getSplitVoyagePlans(final int loadStartTime, final long loadStartingHeel, final int ballastStartTime, final int availableTime,
			final IPortSlot firstLoad, final IPortSlot discharge, final IPortSlot nextLoad, final IVesselAvailability vesselAvailability) {
		Pair<List<Pair<VoyagePlan, IPortTimesRecord>>, GeneratedCharterOutOption> storedSolution = cachedSolutions
				.get(new CacheKey(loadStartTime, loadStartingHeel, ballastStartTime, availableTime, firstLoad, discharge, nextLoad, vesselAvailability));
		if (storedSolution == null) {
			return null;
		} else {
			List<Pair<VoyagePlan, IPortTimesRecord>> resetOptionsSolution = applyStoredOptionsToGCO(storedSolution);
			return resetOptionsSolution;
		}
	}

	/**
	 * Applies stored information about the portslot that can change between iterations
	 * 
	 * @param storedSolution
	 * @return
	 */
	private List<Pair<VoyagePlan, IPortTimesRecord>> applyStoredOptionsToGCO(Pair<List<Pair<VoyagePlan, IPortTimesRecord>>, GeneratedCharterOutOption> storedSolution) {
		GeneratedCharterOutOption gcoo = storedSolution.getSecond();
		for (Pair<VoyagePlan, IPortTimesRecord> plans : storedSolution.getFirst()) {
			for (IDetailsSequenceElement vd : plans.getFirst().getSequence()) {
				if (vd instanceof PortDetails) {
					PortOptions options = ((PortDetails) vd).getOptions();
					if (options.getPortSlot() instanceof IGeneratedCharterOutVesselEventPortSlot) {
						options.setVessel(gcoo.getGCOVessel());
						options.setVisitDuration(gcoo.getGCODuration());

						IGeneratedCharterOutVesselEventPortSlot slot = (IGeneratedCharterOutVesselEventPortSlot) options.getPortSlot();
						assert false; // IS THIS STILL VALID NOW ID IS FIXED ?
						// slot.setId(gcoo.getGCOID());
						slot.setPort(gcoo.getGCOPort());

						IGeneratedCharterOutVesselEvent event = slot.getVesselEvent();
						event.setDurationHours(gcoo.getGCOEventDurationHours());
						event.setHireOutRevenue(gcoo.getGCOEventHireOutRevenue());
						event.setStartPort(gcoo.getGCOEventStartPort());
						event.setEndPort(gcoo.getGCOEventEndPort());
						event.setHeelOptions(gcoo.getGCOEventHeelPrice(), gcoo.getGCOEventHeelCV(), gcoo.getGCOEventHeelVolume());
					}
				}
			}
		}
		return storedSolution.getFirst();
	}

	public boolean isSolutionCached(final int loadStartTime, final long loadStartingHeel, final int ballastStartTime, final int availableTime, final IPortSlot firstLoad, final IPortSlot discharge,
			final IPortSlot nextLoad, final IVesselAvailability vesselAvailability) {
		CacheKey c = new CacheKey(loadStartTime, loadStartingHeel, ballastStartTime, availableTime, firstLoad, discharge, nextLoad, vesselAvailability);
		if (cachedSolutions.containsKey(c)) {
			return true;
		} else {
			return false;
		}
	}
}
