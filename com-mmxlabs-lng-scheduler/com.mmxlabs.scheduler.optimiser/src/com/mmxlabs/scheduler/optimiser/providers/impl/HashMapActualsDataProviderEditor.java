package com.mmxlabs.scheduler.optimiser.providers.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;

public class HashMapActualsDataProviderEditor implements IActualsDataProvider {

	@Override
	public boolean hasActuals(IPortSlot slot) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getArrivalTime(IPortSlot slot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ITimeWindow getArrivalTimeWindow(IPortSlot slot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getVisitDuration(IPortSlot slot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCVValue(IPortSlot slot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getPortCosts(IPortSlot slot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getVolumeInM3(IPortSlot slot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getVolumeInMMBtu(IPortSlot slot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getStartHeelInM3(IPortSlot slot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getEndHeelInM3(IPortSlot slot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBaseFuelPrice(IPortSlot fromPortSlot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNextVoyageBaseFuelConsumptionInMT(IPortSlot fromPortSlot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLNGPricePerMMBTu(IPortSlot portSlot) {
		// TODO Auto-generated method stub
		return 0;
	}

}
