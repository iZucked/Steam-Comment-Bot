/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.labellers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.vertical.ReportNebulaGridManager;
import com.mmxlabs.lingo.reports.views.vertical.providers.EventProvider;
import com.mmxlabs.models.lng.schedule.Event;

public class LocalDateColumnLabelProvider extends GridColumnLabelProvider {
	protected EventProvider provider;
	protected EventLabelProvider labeller;
	protected ReportNebulaGridManager manager;
	protected DateTimeFormatter df;

	protected HashMap<LocalDate, Event[]> cache = new HashMap<>();

	public LocalDateColumnLabelProvider(@NonNull final DateTimeFormatter df, @NonNull final EventProvider provider, @NonNull final EventLabelProvider labeller,
			@NonNull final ReportNebulaGridManager manager) {
		this.df = df;
		this.provider = provider;
		this.labeller = labeller;
		this.manager = manager;
	}

	public Event getData(@NonNull final Pair<LocalDate, Integer> key) {
		final LocalDate date = key.getFirst();
		final Integer index = key.getSecond();
		final Event[] result;
		if (cache.containsKey(date)) {
			result = (Event[]) cache.get(date);
		} else {
			result = provider.getEvents(date);
			cache.put(date, result);
		}
		if (result == null || result.length <= index) {
			return null;
		}
		return result[index];
	}

	public Event[] getData(@NonNull LocalDate date) {
		final Event[] result;
		if (cache.containsKey(date)) {
			result = (Event[]) cache.get(date);
		} else {
			result = provider.getEvents(date);
			cache.put(date, result);
		}
		if (result == null) {
			return null;
		}
		return result;
	}

	@Override
	public String getRowHeaderText(final Object element) {
		final Pair<LocalDate, Integer> pair = castPair(element);
		final LocalDate date = pair.getFirst();
		return date.format(df);
	}

	@Override
	public String getText(final Object element) {
		final Pair<LocalDate, Integer> pair = castPair(element);
		final LocalDate date = pair.getFirst();

		if (manager.isCollapseEvents()) {
			Event[] events = getData(pair.getFirst());
			if (events != null) {
				StringBuilder sb = new StringBuilder();
				boolean first = true;
				for (Event e : events) {
					if (!first) {
						sb.append("; ");
					}
					sb.append(labeller.getText(date, e));
					first = false;
				}
				return sb.toString();
			}
		}
		return labeller.getText(date, getData(pair));

	}

	@Override
	public Font getFont(final Object element) {
		final Pair<LocalDate, Integer> pair = castPair(element);
		final LocalDate date = pair.getFirst();
		return labeller.getFont(date, getData(pair));
	}

	@Override
	public Color getBackground(final Object element) {
		@SuppressWarnings("unchecked")
		final Pair<LocalDate, Integer> pair = castPair(element);
		final LocalDate date = pair.getFirst();
		return labeller.getBackground(date, getData(pair));
	}

	@Override
	public Color getForeground(final Object element) {
		final Pair<LocalDate, Integer> pair = castPair(element);
		final LocalDate date = pair.getFirst();
		return labeller.getForeground(date, getData(pair));
	}

	@Override
	public void update(final ViewerCell cell) {
		super.update(cell);
		manager.updateCell(cell);
	}

	protected Pair<LocalDate, Integer> castPair(final Object element) {
		return (Pair<LocalDate, Integer>) element;
	}
}
