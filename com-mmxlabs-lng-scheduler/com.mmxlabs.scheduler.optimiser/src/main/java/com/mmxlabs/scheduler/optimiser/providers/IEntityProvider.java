/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

/**
 * DCP linking IPortSlot instances with IEntity instances. ICalculators are not linked, because of the fixed price override creating a bogus calculator.
 * 
 * TODO: think about this; the fixed price override could be implemented in the calculator with a suitable DCP for checking it. Not sure which is better.
 * 
 * @author hinton
 * @since 2.0
 * 
 */
public interface IEntityProvider extends IDataComponentProvider {
	/**
	 * @return The IEntity responsible for shipping
	 */
	public IEntity getShippingEntity();

	/**
	 * Get the entity which owns contracts at the given slot.
	 * 
	 * @param calculator
	 *            a calculator
	 * @return the entity which owns the contract at the given slot
	 */
	public IEntity getEntityForSlot(final IPortSlot slot);
}
