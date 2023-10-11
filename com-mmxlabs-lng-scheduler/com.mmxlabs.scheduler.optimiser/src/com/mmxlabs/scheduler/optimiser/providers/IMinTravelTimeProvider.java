package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@NonNullByDefault
public interface IMinTravelTimeProvider {

	@Nullable
	Integer getMinTravelTime(IPortSlot slot);
}
