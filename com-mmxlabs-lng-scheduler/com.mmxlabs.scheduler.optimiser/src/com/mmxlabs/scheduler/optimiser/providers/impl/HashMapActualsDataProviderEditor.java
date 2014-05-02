package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProviderEditor;

public class HashMapActualsDataProviderEditor implements IActualsDataProviderEditor {

	private final Map<IPortSlot, Boolean> actualsPresent = new HashMap<>();

	private final Map<IPortSlot, Integer> arrivalTime = new HashMap<>();
	private final Map<IPortSlot, ITimeWindow> arrivalTimeWindow = new HashMap<>();
	private final Map<IPortSlot, Integer> visitDuration = new HashMap<>();
	private final Map<IPortSlot, Long> portCosts = new HashMap<>();
	private final Map<IPortSlot, Integer> cargoCVValue = new HashMap<>();
	private final Map<IPortSlot, Long> volumeInM3 = new HashMap<>();
	private final Map<IPortSlot, Long> volumeInMMBTu = new HashMap<>();
	private final Map<IPortSlot, Long> baseFuelConsumptionInMT = new HashMap<>();
	private final Map<IPortSlot, Long> startHeelInM3 = new HashMap<>();
	private final Map<IPortSlot, Long> endHeelInM3 = new HashMap<>();

	private final Map<IPortSlot, Integer> lngPricePerMMBTu = new HashMap<>();
	private final Map<IPortSlot, Integer> baseFuelPricePerMT = new HashMap<>();
	private final Map<IPortSlot, Integer> charterRatePerDay = new HashMap<>();
	private final Map<IPortSlot, Long> routeCosts = new HashMap<>();
	private final Map<IPortSlot, Integer> distance = new HashMap<>();

	@Override
	public boolean hasActuals(final IPortSlot slot) {
		if (actualsPresent.containsKey(slot)) {
			return actualsPresent.get(slot);
		}
		return false;
	}

	@Override
	public int getArrivalTime(final IPortSlot slot) {
		return arrivalTime.get(slot);
	}

	@Override
	public ITimeWindow getArrivalTimeWindow(final IPortSlot slot) {
		return arrivalTimeWindow.get(slot);
	}

	@Override
	public int getVisitDuration(final IPortSlot slot) {
		return visitDuration.get(slot);
	}

	@Override
	public int getCVValue(final IPortSlot slot) {
		return cargoCVValue.get(slot);
	}

	@Override
	public long getPortCosts(final IPortSlot slot) {
		return portCosts.get(slot);
	}

	@Override
	public long getVolumeInM3(final IPortSlot slot) {
		return volumeInM3.get(slot);
	}

	@Override
	public long getVolumeInMMBtu(final IPortSlot slot) {
		return volumeInMMBTu.get(slot);
	}

	@Override
	public long getStartHeelInM3(final IPortSlot slot) {
		return startHeelInM3.get(slot);
	}

	@Override
	public long getEndHeelInM3(final IPortSlot slot) {
		return endHeelInM3.get(slot);
	}

	@Override
	public int getBaseFuelPricePerMT(final IPortSlot slot) {
		return baseFuelPricePerMT.get(slot);
	}

	@Override
	public long getNextVoyageBaseFuelConsumptionInMT(final IPortSlot slot) {
		return baseFuelConsumptionInMT.get(slot);
	}

	@Override
	public int getLNGPricePerMMBTu(final IPortSlot slot) {
		return lngPricePerMMBTu.get(slot);
	}

	@Override
	public int getCharterRatePerDay(final IPortSlot slot) {
		return charterRatePerDay.get(slot);
	}

	@Override
	public int getNextVoyageDistance(final IPortSlot slot) {
		return distance.get(slot);
	}

	@Override
	public long getNextVoyageRouteCosts(final IPortSlot slot) {
		return routeCosts.get(slot);
	}

	@Override
	public void createLoadSlotActuals(final ILoadOption slot, final int arrivalTime, final int visitDuration, final long portCosts, final int cargoCV, final long startHeelInM3,
			final long lngLoadVolumeInM3, final long lngLoadVolumeInMMBTu, final int purchasePricePerMMBTu, final long ladenBaseFuelConsumptionInMT, final int baseFuelPricePerMT,
			final int charterRatePerDay, final int distance, final long routeCosts) {

		this.actualsPresent.put(slot, true);

		this.arrivalTime.put(slot, arrivalTime);
		this.arrivalTimeWindow.put(slot, new TimeWindow(arrivalTime, arrivalTime));
		this.visitDuration.put(slot, visitDuration);
		this.portCosts.put(slot, portCosts);
		this.cargoCVValue.put(slot, cargoCV);
		this.volumeInM3.put(slot, lngLoadVolumeInM3);
		this.volumeInMMBTu.put(slot, lngLoadVolumeInMMBTu);
		this.baseFuelConsumptionInMT.put(slot, ladenBaseFuelConsumptionInMT);
		this.startHeelInM3.put(slot, startHeelInM3);
		this.endHeelInM3.put(slot, 0l);
		this.lngPricePerMMBTu.put(slot, purchasePricePerMMBTu);
		this.baseFuelPricePerMT.put(slot, baseFuelPricePerMT);
		this.charterRatePerDay.put(slot, charterRatePerDay);
		this.distance.put(slot, distance);
		this.routeCosts.put(slot, routeCosts);
	}

	@Override
	public void createDischargeSlotActuals(final IDischargeOption slot, final int arrivalTime, final int visitDuration, final long portCosts, final int cargoCV, final long endHeelInM3,
			final long lngDischargeVolumeInM3, final long lngDischargeVolumeInMMBTu, final int salesPricePerMMBTu, final long ballastBaseFuelConsumptionInMT, final int distance, final long routeCosts) {
		this.actualsPresent.put(slot, true);

		this.arrivalTime.put(slot, arrivalTime);
		this.arrivalTimeWindow.put(slot, new TimeWindow(arrivalTime, arrivalTime));
		this.visitDuration.put(slot, visitDuration);
		this.portCosts.put(slot, portCosts);
		this.cargoCVValue.put(slot, cargoCV);
		this.volumeInM3.put(slot, lngDischargeVolumeInM3);
		this.volumeInMMBTu.put(slot, lngDischargeVolumeInMMBTu);
		this.baseFuelConsumptionInMT.put(slot, ballastBaseFuelConsumptionInMT);
		this.startHeelInM3.put(slot, 0l);
		this.endHeelInM3.put(slot, endHeelInM3);
		this.lngPricePerMMBTu.put(slot, salesPricePerMMBTu);
		this.distance.put(slot, distance);
		this.routeCosts.put(slot, routeCosts);
	}
}
