/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.ZonedDateTime;
import java.util.OptionalInt;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;

@NonNullByDefault
public interface IAssignableElementDateProvider {

	@Nullable
	ZonedDateTime getSlotWindowStart(Slot slot);

	@Nullable
	ZonedDateTime getSlotWindowEnd(Slot slot);

	OptionalInt getSlotDurationInHours(Slot slot);

	// ZonedDateTime getEventWindowStart();
	// ZonedDateTime getEventWindowEnd();
	// int getEventDurationInHours();

}
