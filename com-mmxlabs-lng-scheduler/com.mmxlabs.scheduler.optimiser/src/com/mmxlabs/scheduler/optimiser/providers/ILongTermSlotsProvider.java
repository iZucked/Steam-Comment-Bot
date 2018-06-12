/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * {@link IDataComponentProvider} interface to provide {@link IPortSlot} information for long term slots.
 * 
 * @author achurchill
 */
public interface ILongTermSlotsProvider extends IDataComponentProvider {

	/**
	 * Returns the long term slots
	 * 
	 * @return
	 */
	@NonNull
	Collection<IPortSlot> getLongTermSlots();

	/**
	 * Is this slot a long term slot?
	 * @param portSlot
	 * @return
	 */
	boolean isLongTermSlot(@NonNull IPortSlot portSlot);
}
