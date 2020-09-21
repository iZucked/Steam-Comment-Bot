package com.mmxlabs.models.lng.transformer.ui.inventory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.jdt.annotation.NonNull;

public class InventoryFileName {
	public static @NonNull String getName(@NonNull String prefix, @NonNull String postfix) {
		String result = prefix;
		result += postfix;
		return result;
	}
}
