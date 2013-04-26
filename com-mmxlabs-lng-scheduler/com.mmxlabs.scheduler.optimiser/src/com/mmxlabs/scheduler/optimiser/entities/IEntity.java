/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities;

/**
 * Simple interface describing a contractual entity.
 * 
 * @author hinton
 * @since 2.0
 * 
 */
public interface IEntity {
	public String getName();

	/**
	 * Get the price which we would buy from shipping for, if we were selling at this price.
	 * 
	 * @param dischargePricePerMMBTU
	 * @return
	 */
	public int getDownstreamTransferPrice(int dischargePricePerM3, int cvValue);

	/**
	 * Get the price which we would buy from our supplier from, if we were selling at this price.
	 * 
	 * @param loadPricePerM3
	 * @param cvValue
	 * @return
	 */
	public int getUpstreamTransferPrice(int loadPricePerM3, int cvValue);

	/**
	 * Get the taxed contribution to group P&L, if the entity as a whole were to take this profit before tax. Should take into account tax rate and ownership %.
	 * 
	 * @param downstreamTotalPretaxProfit
	 * @param time
	 * @return
	 */
	public long getTaxedProfit(long downstreamTotalPretaxProfit, int time);
}
