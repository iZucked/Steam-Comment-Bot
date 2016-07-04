/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public interface ISlotTransformer extends ITransformerExtension {

	/**
	 * Called when a slot has been transformed. This allows the transformer to get hold of any slot related extension data and do whatever it may want to do with it. This will be called for
	 * <em>every</em> port slot that is transformed, so it is the contract transformer's responsibility to be ready for this.
	 * 
	 * @param modelSlot
	 * @param optimiserSlot
	 */
	public void slotTransformed(@NonNull Slot modelSlot, @NonNull IPortSlot optimiserSlot);

}
