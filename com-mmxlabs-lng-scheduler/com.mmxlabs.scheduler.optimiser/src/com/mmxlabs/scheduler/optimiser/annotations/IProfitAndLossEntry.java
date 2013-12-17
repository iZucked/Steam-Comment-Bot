/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

/**
 * A single P&L entry.
 * 
 * @author hinton
 * @since 2.0
 * 
 */
public interface IProfitAndLossEntry {
	/**
	 * The entity with which this entry is connected.
	 * 
	 * @return
	 */
	public IEntity getEntity();

	/**
	 * @return The final contribution to group profit from this entry.
	 */
	public long getFinalGroupValue();

	/**
	 * @return The final contribution to pre-tax group profit from this entry.
	 * @since 8.0
	 */
	public long getFinalGroupValuePreTax();

	/**
	 * @return an {@link IDetailTree} of computation details for this entry
	 */
	public IDetailTree getDetails();

}
