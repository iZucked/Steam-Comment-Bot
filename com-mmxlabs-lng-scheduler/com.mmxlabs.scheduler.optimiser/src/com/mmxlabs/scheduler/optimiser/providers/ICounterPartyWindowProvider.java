package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@NonNullByDefault
public interface ICounterPartyWindowProvider {

	boolean isCounterPartyWindow(IPortSlot portSlot);
}
