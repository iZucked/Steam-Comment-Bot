/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.TimePeriod;

public class SpotSlotUtils {

	public static @NonNull String getKeyForDate(final @NonNull LocalDate date) {
		return String.format("%04d-%02d", date.getYear(), date.getMonthValue());
	}

	public static @NonNull String getSpotSlotName(@NonNull final SpotMarket market, @NonNull final LocalDate date, @NonNull final Set<@NonNull String> usedNames) {
		final String yearMonthString = getKeyForDate(date);
		final String namePrefix = market.getName() + "-" + yearMonthString + "-";

		// Avoid ID clash
		int counter = 0;
		String name = namePrefix + counter;
		while (usedNames.contains(name)) {
			name = namePrefix + (++counter);
		}
		return name;
	}

	public static <T extends SpotSlot & Slot> void setSpotSlotWindow(@NonNull final T slot, @NonNull final LocalDate cal) {
		// Set back to start of month
		slot.setWindowStart(cal.withDayOfMonth(1));
		slot.setWindowStartTime(0);

		slot.setWindowSize(1);
		slot.setWindowSizeUnits(TimePeriod.MONTHS);
	}

	public static @NonNull Set<@NonNull String> getUsedLoadNames(@NonNull CargoModel cargoModel) {
		final Set<@NonNull String> usedIDStrings = new HashSet<>();
		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
			String name = slot.getName();
			if (name != null && !name.isEmpty()) {
				usedIDStrings.add(name);
			}
		}
		return usedIDStrings;
	}

	public static @NonNull Set<@NonNull String> getUsedDischargeNames(@NonNull CargoModel cargoModel) {
		final Set<@NonNull String> usedIDStrings = new HashSet<>();
		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
			String name = slot.getName();
			if (name != null && !name.isEmpty()) {
				usedIDStrings.add(name);
			}
		}
		return usedIDStrings;
	}
}
