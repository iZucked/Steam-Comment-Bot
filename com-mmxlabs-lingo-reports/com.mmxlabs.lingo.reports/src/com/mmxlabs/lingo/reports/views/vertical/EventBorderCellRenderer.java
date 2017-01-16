/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.views.vertical.AbstractVerticalReportVisualiser;
import com.mmxlabs.lingo.reports.views.vertical.AbstractVerticalReportVisualiser.Alignment;
import com.mmxlabs.lingo.reports.views.vertical.ReportNebulaGridManager;
import com.mmxlabs.lingo.reports.views.vertical.labellers.IBorderProvider;
import com.mmxlabs.lingo.reports.views.vertical.providers.EventProvider;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Class to draw vertical report cells with borders
 * 
 */
@SuppressWarnings("restriction")
public class EventBorderCellRenderer extends DefaultCellRenderer {
	private final ReportNebulaGridManager manager;
	private final AbstractVerticalReportVisualiser verticalReportVisualiser;
	private final IBorderProvider borderProvider;
	private final EventProvider eventProvider;

	public EventBorderCellRenderer(final ReportNebulaGridManager manager, final AbstractVerticalReportVisualiser verticalReportVisualiser, @Nullable final IBorderProvider borderProvider,
			@NonNull final EventProvider eventProvider) {
		this.manager = manager;
		this.verticalReportVisualiser = verticalReportVisualiser;
		this.borderProvider = borderProvider;
		this.eventProvider = eventProvider;
	}

	@Override
	public Point computeSize(final GC gc, final int wHint, final int hHint, final Object value) {
		final Point sizeResult = super.computeSize(gc, wHint, hHint, value);
		if (value instanceof GridItem) {
			final Object data = ((GridItem) value).getData();
			if (data != null) {
				data.toString();
			}
		}
		return sizeResult;
	}

	@Override
	public void paint(final GC gc, final Object value) {
		{
			final int alignment = getAlignment();
			final GridItem item = (GridItem) value;

			final Pair<LocalDate, Integer> pair = (Pair<LocalDate, Integer>) item.getData();
			final Event[] events = eventProvider.getEvents(pair.getFirst());
			if (events.length > 0 && pair.getSecond() < events.length) {
				final Event evt = events[pair.getSecond()];
				final Alignment align = verticalReportVisualiser.getEventTextAlignment(pair.getFirst(), evt);

				switch (align) {
				case CENTRE:
					setAlignment(SWT.CENTER);
					break;
				case LEFT:
					setAlignment(SWT.LEFT);
					break;
				case RIGHT:
					setAlignment(SWT.RIGHT);
					break;
				default:
					break;

				}
			}
			super.paint(gc, value);
			setAlignment(alignment);
		}

		final GridItem gridItem = (GridItem) value;
		int border = IBorderProvider.NONE;
		if (borderProvider != null) {
			final Pair<LocalDate, Integer> pair = (Pair<LocalDate, Integer>) gridItem.getData();
			final Event[] events = eventProvider.getEvents(pair.getFirst());
			if (events != null && events.length > 0 && pair.getSecond() < events.length) {
				border |= borderProvider.getBorders(pair.getFirst(), events[pair.getSecond()]);
			}
		}
		border |= IBorderProvider.RIGHT;

		if (border != IBorderProvider.NONE) {
			final Rectangle bounds = getBounds();
			gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			if ((border & IBorderProvider.BOTTOM) == IBorderProvider.BOTTOM) {
				gc.drawLine(bounds.x, bounds.y + bounds.height, bounds.x + bounds.width, bounds.y + bounds.height);
			}
			if ((border & IBorderProvider.TOP) == IBorderProvider.TOP) {
				gc.drawLine(bounds.x, bounds.y - 1, bounds.x + bounds.width, bounds.y - 1);
			}
			if ((border & IBorderProvider.RIGHT) == IBorderProvider.RIGHT) {
				gc.drawLine(bounds.x + bounds.width - 1, bounds.y, bounds.x + bounds.width - 1, bounds.y + bounds.height);
			}
			if ((border & IBorderProvider.LEFT) == IBorderProvider.LEFT) {
				gc.drawLine(bounds.x - 1, bounds.y, bounds.x - 1, bounds.y + bounds.height);
			}
		}
	}
}
