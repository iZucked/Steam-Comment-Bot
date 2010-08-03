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
	 * Returns the unit cost of the fuel consumed.
	 * 
	 * @param fuel
	 * @return
	 */
	long getTotalFuelCost(FuelComponent fuel);

	/**
	 * Set the total cost of fuel consumed
	 * 
	 * @param fuel
	 * @param cost
	 */
	void setTotalFuelCost(FuelComponent fuel, long cost);

	// TODO: Get capacity violations - sum abs volume deficit
	// TODO: Get lateness violations - sum abs extra time

	// TODO: Perhaps these should be part of a different interface to keep this
	// one more generic?

	/**
	 * Return the total volume loaded at the load port in this voyage plan
	 */
	long getLoadVolume();

	/**
	 * Set the total volume loaded at the load port in this voyage plan
	 */
	void setLoadVolume(long loadVolume);

	/**
	 * Return the total purchase cost of loaded LNG.
	 */
	long getPurchaseCost();

	/**
	 * Set the total purchase cost of loaded LNG.
	 */
	void setPurchaseCost(long purchaseCost);

	/**
	 * Return the total volume discharged at the discharge port in this voyage
	 * plan
	 */
	long getDischargeVolume();

	/**
	 * Set the total volume discharged at the discharge port in this voyage plan
	 */
	void setDischargeVolume(long dischargeVolume);

	/**
	 * Return the total sales revenue of discharged LNG.
	 */
	long getSalesRevenue();

	/**
	 * Set the total sales revenue of discharged LNG.
	 */
	void setSalesRevenue(long salesRevenue);

}
