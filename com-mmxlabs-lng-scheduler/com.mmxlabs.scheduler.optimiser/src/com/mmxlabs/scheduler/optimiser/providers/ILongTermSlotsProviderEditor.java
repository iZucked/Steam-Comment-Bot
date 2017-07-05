package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public interface ILongTermSlotsProviderEditor extends ILongTermSlotsProvider {

	public void addLongTermSlot(@NonNull IPortSlot element);

}
