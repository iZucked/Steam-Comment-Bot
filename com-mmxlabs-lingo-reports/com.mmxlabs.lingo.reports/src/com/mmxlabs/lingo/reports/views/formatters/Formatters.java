/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public final class Formatters {

	public static final ICellRenderer objectFormatter = new BaseFormatter();

	public static final ICellRenderer namedObjectFormatter = new BaseFormatter() {
		@Override
		public @Nullable String render(final Object object) {
			if (object instanceof RouteOption) {
				RouteOption routeOption = (RouteOption) object;
				return PortModelLabeller.getName(routeOption);
			}
			if (object instanceof NamedObject) {
				final NamedObject namedObject = (NamedObject) object;
				return namedObject.getName();
			}
			return super.render(object);
		}
	};

	public static final ICellRenderer asLocalDateFormatter = new AsLocalDateFormatter(DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter());
	public static final ICellRenderer asLocalTimeFormatter = new AsLocalDateFormatter(DateTimeFormatsProvider.INSTANCE.createTimeStringDisplayFormatter());

	public static final ICellRenderer asDateTimeFormatterWithTZ = new AsDateTimeFormatter(DateTimeFormatsProvider.INSTANCE.createDateTimeStringDisplayFormatter(), true);
	public static final ICellRenderer asDateTimeFormatterNoTz = new AsDateTimeFormatter(DateTimeFormatsProvider.INSTANCE.createDateTimeStringDisplayFormatter(), false);

	public static final IntegerFormatter integerFormatter = new IntegerFormatter();

	public enum DurationMode {
		DAYS_HOURS_COLON, DAYS_HOURS_HUMAN, DECIMAL,
	}

	private static DurationMode durationMode = DurationMode.DAYS_HOURS_COLON;

	public static DurationMode getDurationMode() {
		return durationMode;
	}

	public static void setDurationMode(@NonNull final DurationMode mode) {
		durationMode = mode;
	}

	public static @NonNull String formatAsDays(final int hours) {
		return formatAsDays(durationMode, hours);
	}

	public static @NonNull String formatAsDays(DurationMode mode, final double hours) {
		switch (mode) {
		case DECIMAL:
			return String.format("%.01f", hours / 24.0);
		default:
			return formatAsDays(mode, Math.round(hours));
		}
	}
	
	public static @NonNull String formatAsDays(DurationMode mode, final long hours) {
		switch (mode) {
		case DAYS_HOURS_COLON:
			return String.format("%02d:%02d", hours / 24, hours % 24);
		case DAYS_HOURS_HUMAN:
			if ((hours / 24) > 0) {
				return String.format("%dd %dh", hours / 24, hours % 24);
			} else {
				return String.format("%dh", hours);
			}
		case DECIMAL:
			return String.format("%.01f", (double) hours / 24.0);
		}
		throw new IllegalArgumentException("Unknown duration mode");
	}
}
