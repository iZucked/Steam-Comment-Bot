/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class TotalsTransformer {

	@NonNull
	private static final String TOTAL_COST = "Total Cost";

	@NonNull
	private static final String TYPE_COST = "Cost";

	@NonNull
	public static final String TYPE_TIME = "Days, hours";

	public static class RowData {
		public RowData(final String scheduleName, final String component, final String type, final long fitness, Long deltaFitness, final boolean minimise) {
			super();
			this.scheduleName = scheduleName;
			this.component = component;
			this.type = type;
			this.fitness = fitness;
			this.deltaFitness = deltaFitness;
			this.minimise = minimise;
		}

		public final String scheduleName;
		public final String component;
		public final String type;
		public final long fitness;
		public final Long deltaFitness;
		public final boolean minimise;
	}

	public List<RowData> transform(final Schedule schedule, final String scheduleName, @Nullable List<RowData> pinnedData) {

		final List<RowData> output = new LinkedList<>();
		/**
		 * Stores the total fuel costs for each type of fuel - this may not be the detailed output we want, I don't know
		 */
		final Map<Fuel, Long> totalFuelCosts = new HashMap<Fuel, Long>();

		long distance = 0l;
		long totalCost = 0l;
		long canals = 0;
		long hire = 0;
		long portCost = 0;

		long lateness = 0;
		long capacityViolations = 0;

		for (final Sequence seq : schedule.getSequences()) {

			int vesselCapacity = Integer.MAX_VALUE;
			final VesselAvailability vesselAvailability = seq.getVesselAvailability();
			if (vesselAvailability != null) {
				final Vessel vessel = vesselAvailability.getVessel();
				vesselCapacity = vessel.getVesselOrVesselClassCapacity();
			} else {
				final CharterInMarket charterInMarket = seq.getCharterInMarket();
				if (charterInMarket != null) {
					final VesselClass vesselClass = charterInMarket.getVesselClass();
					if (vesselClass != null) {
						vesselCapacity = vesselClass.getCapacity();
					}
				}
			}

			for (final Event evt : seq.getEvents()) {
				hire += evt.getCharterCost();
				totalCost += evt.getCharterCost();
				if (evt instanceof FuelUsage) {
					final FuelUsage mix = (FuelUsage) evt;
					// add up fuel components from mixture
					for (final FuelQuantity fq : mix.getFuels()) {
						Long sumSoFar = totalFuelCosts.get(fq.getFuel());
						if (sumSoFar == null)
							sumSoFar = 0l;
						totalFuelCosts.put(fq.getFuel(), sumSoFar + fq.getCost());
						totalCost += fq.getCost();
					}
				}
				if (evt instanceof Journey) {
					final Journey journey = (Journey) evt;
					distance += journey.getDistance();
					canals += journey.getToll();
					totalCost += journey.getToll();
				}
				if (evt instanceof PortVisit) {
					final int cost = ((PortVisit) evt).getPortCost();
					portCost += cost;
					totalCost += cost;
				}

				if (evt instanceof SlotVisit) {
					final SlotVisit visit = (SlotVisit) evt;
					lateness += LatenessUtils.getLatenessInHours(visit);
					final SlotAllocation slotAllocation = visit.getSlotAllocation();
					if (slotAllocation.getSlot() != null) {
						final Slot slot = slotAllocation.getSlot();
						final int minQuantity = slot.getMinQuantity();
						final int maxQuantity = slot.getMaxQuantity();
						if (maxQuantity != 0 && maxQuantity < slotAllocation.getVolumeTransferred()) {
							capacityViolations++;
						} else if (minQuantity != 0 && minQuantity > slotAllocation.getVolumeTransferred()) {
							capacityViolations++;
						} else if (vesselCapacity < slotAllocation.getVolumeTransferred()) {
							capacityViolations++;
						}
					}

				} else if (evt instanceof VesselEventVisit) {
					final VesselEventVisit vev = (VesselEventVisit) evt;
					lateness += LatenessUtils.getLatenessInHours(vev);
				}
			}
		}

		EObject object = schedule.eContainer();
		while ((object != null) && !(object instanceof MMXRootObject)) {
			if (object instanceof EObject) {
				object = ((EObject) object).eContainer();
			}
		}

		for (final Entry<Fuel, Long> entry : totalFuelCosts.entrySet()) {
			output.add(createRow(scheduleName, entry.getKey().toString(), TYPE_COST, entry.getValue(), true, pinnedData));
		}

		output.add(createRow(scheduleName, "Canal Fees", TYPE_COST, canals, true, pinnedData));
		output.add(createRow(scheduleName, "Charter Fees", TYPE_COST, hire, true, pinnedData));
		output.add(createRow(scheduleName, "Distance", TYPE_COST, distance, true, pinnedData));

		output.add(createRow(scheduleName, "Port Costs", TYPE_COST, portCost, true, pinnedData));
		output.add(createRow(scheduleName, "Lateness", TYPE_TIME, lateness, true, pinnedData));
		output.add(createRow(scheduleName, "Capacity", "Count", capacityViolations, true, pinnedData));

		output.add(createRow(scheduleName, TOTAL_COST, TYPE_COST, totalCost, true, pinnedData));

		return output;
	}

	private RowData createRow(final String scenarioInstanceName, @NonNull final String component, final String type, final long value, final boolean minimise,
			@Nullable final List<RowData> pinnedData) {
		return new RowData(scenarioInstanceName, component, type, value, getDelta(component, value, pinnedData), minimise);
	}

	@Nullable
	private Long getDelta(@NonNull final String component, final long fitness, @Nullable final List<RowData> pinnedData) {
		if (pinnedData == null) {
			return null;
		}
		for (final RowData data : pinnedData) {
			if (component.equals(data.component)) {
				return data.fitness - fitness;
			}
		}
		return null;
	}
}
