package com.mmxlabs.models.lng.transformer.ui.inventory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.jdt.annotation.NonNull;

public class InventoryFileName {
	public static @NonNull String getName(final @NonNull LocalDate startDate, @NonNull String prefix, @NonNull String postfix) {
		String result = prefix;
		final LocalDateTime currentDateTime = LocalDateTime.now();
		result += "master_";
		result += DateTimeFormatter.ofPattern("ddMMyy").format(startDate);
		result += "_" + DateTimeFormatter.ofPattern("HHmm").format(currentDateTime);
		result += postfix;
		return result;
	}
}
