/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public final class Formatters {

	public static final ICellRenderer objectFormatter = new BaseFormatter();

	public static final ICellRenderer namedObjectFormatter = new BaseFormatter() {
		@Override
		public @Nullable String render(final Object object) {
			if (object instanceof NamedObject) {
				final NamedObject namedObject = (NamedObject) object;
				return namedObject.getName();
			}
			return super.render(object);
		}
	};

	public static final ICellRenderer asLocalDateFormatter = new AsLocalDateFormatter(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
	public static final ICellRenderer asLocalTimeFormatter = new AsLocalDateFormatter(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));

	public static final ICellRenderer asDateTimeFormatterWithTZ = new AsDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT), true);
	public static final ICellRenderer asDateTimeFormatterNoTz = new AsDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT), false);

	public static final IntegerFormatter integerFormatter = new IntegerFormatter();

	public enum DurationMode {
		DAYS_HOURS, DECIMAL
	}

	private static DurationMode DURATION_MODE = DurationMode.DAYS_HOURS;

	public static DurationMode getDurationMode() {
		return DURATION_MODE;
	}

	public static void setDurationMode(@NonNull final DurationMode mode) {
		DURATION_MODE = mode;
	}

	public static @NonNull String formatAsDays(final int hours) {
		switch (DURATION_MODE) {
		case DAYS_HOURS:
			return String.format("%02d:%02d", hours / 24, hours % 24);
		case DECIMAL:
			return String.format("%.01f", (double) hours / 24.0);
		}
		throw new IllegalArgumentException("Unknown duration mode");
	}
}
