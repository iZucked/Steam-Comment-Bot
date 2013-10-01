package com.mmxlabs.models.lng.transformer.inject;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

/**
 * The {@link IDESPurchaseSlotBindingsGenerator} is an optional component of the {@link LNGScenarioTransformer} which, if present, can be used to determine how FOB Sales and DES Purchases are bound to
 * paired ports
 * 
 * @author Simon Goodall
 * @since 6.2
 * 
 */
public interface IDESPurchaseSlotBindingsGenerator {

	/**
	 * Determine how to bind this DES Purchase to sales opportunities. Must call appropriate methods on the {@link ISchedulerBuilder} instance.
	 * 
	 * @param builder
	 *            The {@link ISchedulerBuilder} to call
	 * @param loadSlot
	 *            The Model LoadSlot instance
	 * @param load
	 *            The optimiser load slot instance
	 */
	void bindDischargeSlotsToDESPurchase(ISchedulerBuilder builder, LoadSlot loadSlot, ILoadOption load);

}
