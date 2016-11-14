package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.fitness.impl.PlanEvaluationData;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
/**
 * Interface for InPortBoilOffHelper injection
 * @author NSteadman
 */
public interface IBoilOffHelper {
	
	public long getNBORate(IVessel vessel, PortType portStatus);
	
	public void setBoilOffCompensation(boolean boilOffCompensation);
	
	public boolean isBoilOffCompensation();

	long getPortVisitNBOInM3(IVessel vessel, IPortSlot portSlot, AllocationRecord record);


}
