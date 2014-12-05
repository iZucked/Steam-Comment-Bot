package com.mmxlabs.lingo.reports.views.vertical;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.vertical.AbstractVerticalCalendarReportView.ReportNebulaGridManager;
import com.mmxlabs.models.lng.schedule.Event;

public class CalendarColumnLabelProvider extends GridColumnLabelProvider {
	protected EventProvider provider;
	protected EventLabelProvider labeller;
	protected ReportNebulaGridManager manager;
	protected DateFormat df;

	protected HashMap<Date, Event[]> cache = new HashMap<>();

	public CalendarColumnLabelProvider(final DateFormat df, final EventProvider provider, final EventLabelProvider labeller, final ReportNebulaGridManager manager) {
		this.df = df;
		this.provider = provider;
		this.labeller = labeller;
		this.manager = manager;
	}

	public Event getData(final Pair<Date, Integer> key) {
		final Date date = key.getFirst();
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

	@Override
	public String getRowHeaderText(final Object element) {
		final Pair<Date, Integer> pair = (Pair<Date, Integer>) element;
		final Date date = pair.getFirst();
		return df.format(date);
	}

	@Override
	public String getText(final Object element) {
		final Pair<Date, Integer> pair = (Pair<Date, Integer>) element;
		final Date date = pair.getFirst();
		return labeller.getText(date, getData(pair));
	}

	@Override
	public Font getFont(final Object element) {
		final Pair<Date, Integer> pair = (Pair<Date, Integer>) element;
		final Date date = pair.getFirst();
		return labeller.getFont(date, getData(pair));
	}

	@Override
	public Color getBackground(final Object element) {
		@SuppressWarnings("unchecked")
		final Pair<Date, Integer> pair = (Pair<Date, Integer>) element;
		final Date date = pair.getFirst();
		return labeller.getBackground(date, getData(pair));
	}

	@Override
	public Color getForeground(final Object element) {
		final Pair<Date, Integer> pair = (Pair<Date, Integer>) element;
		final Date date = pair.getFirst();
		return labeller.getForeground(date, getData(pair));
	}

	@Override
	public void update(final ViewerCell cell) {
		super.update(cell);
		manager.updateCell(cell);
	}

}
