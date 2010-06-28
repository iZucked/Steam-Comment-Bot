package com.mmxlabs.scheduler.optimiser.voyage;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * A {@link IVoyagePlan} represents a sequence of visits to ports (
 * {@link IPortSlot}) and corresponding voyages {@link IVoyageDetails}) such
 * that the start and end port is a load port with a single discharge and
 * multiple other "waypoint" destinations along the way. Alternatively, the
 * first port could also be a "state" port where the state of the vessel is set
 * (such as the initial conditions). The {@link IVoyagePlan} combines the
 * information in the {@link IPortSlot} and {@link IVoyageDetails} objects to
 * determine how much fuel is consumed in total and what the load and discharge
 * volumes should be along with associated costs.
 * 
 * @author Simon Goodall
 * 
 */
public interface IVoyagePlan {

	/**
	 * Returns an array of {@link IPortSlot}s interleaved with
	 * {@link IVoyageDetails}.
	 * 
	 * @return
	 */
	Object[] getSequence();

	void setSequence(Object[] sequence);

	/**
	 * Returns the total fuel consumption for the voyage plan.
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelConsumption(FuelComponent fuel);

	void setFuelConsumption(FuelComponent fuel, long consumption);

	/**
	 * Returns the total cost of the fuel consumed.
	 * 
	 * @param fuel
	 * @return
	 */
	long getFuelCost(FuelComponent fuel);

	void setFuelCost(FuelComponent fuel, long cost);

	// TODO: Get capacity violations - sum abs volume deficit
	// TODO: Get lateness violations - sum abs extra time

	// TODO: Perhaps these should be part of a different interface to keep this
	// one more generic?
	long getLoadVolume();

	void setLoadVolume(long loadVolume);

	long getPurchaseCost();

	void setPurchaseCost(long purchaseCost);

	long getDischargeVolume();

	void setDischargeVolume(long dischargeVolume);

	long getSalesRevenue();

	void setSalesRevenue(long salesRevenue);

}
