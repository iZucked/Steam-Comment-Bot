package com.mmxlabs.lingo.reports.views.vertical;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.vertical.AbstractVerticalReportVisualiser.Alignment;
import com.mmxlabs.lingo.reports.views.vertical.labellers.IBorderProvider;
import com.mmxlabs.lingo.reports.views.vertical.providers.EventProvider;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.rcp.common.actions.IAdditionalAttributeProvider;

public class DefaultAdditionalAttributeProvider implements IAdditionalAttributeProvider {
	private final ReportNebulaGridManager manager;
	private final GridTableViewer gridViewer;

	public DefaultAdditionalAttributeProvider(final ReportNebulaGridManager manager, final GridTableViewer gridViewer) {
		this.manager = manager;
		this.gridViewer = gridViewer;

	}

	@Override
	@NonNull
	public String @Nullable [] getAdditionalHeaderAttributes(final GridColumn column) {
		// Border around all header cells.
		return new @NonNull String @Nullable [] { "style='border:1 solid #000;'" };
	}

	@Override
	public String[] getAdditionalRowHeaderAttributes(final GridItem item) {
		return new String[] { "" };
	}

	@Override
	@NonNull
	public String @Nullable [] getAdditionalAttributes(final GridItem item, final int i) {

		// Replicate the month divider line.
		// Shoud column lines except for middle columns

		final StringBuilder styleBuilder = new StringBuilder();

		final GridColumn column = gridViewer.getGrid().getColumn(i);

		// Attempt to determine if an object is late or not.
		int border = IBorderProvider.NONE;
		{
			final Pair<LocalDate, Integer> pair = (Pair<LocalDate, Integer>) item.getData();
			if (pair != null) {

				final LocalDate date = pair.getFirst();
				final int index = pair.getSecond();

				final CalendarColumn cc = manager.getCalendarColumn(i);
				final EventProvider provider = cc.getProvider();
				IBorderProvider borderProvider = null;
				if (provider instanceof IBorderProvider) {
					borderProvider = (IBorderProvider) provider;
				}
				final Event[] events = provider.getEvents(date);
				final Event event = index < events.length ? events[index] : null;
				if (event != null) {
					final Color colour = cc.getLabeller().getForeground(date, event);
					if (colour != null) {
						styleBuilder.append(String.format("color: #%2X%2X%2X;", colour.getRed(), colour.getGreen(), colour.getBlue()));
					}
					if (borderProvider != null) {
						border |= borderProvider.getBorders(date, event);
					}
					final Alignment eventTextAlignment = cc.getLabeller().getTextAlignment(date, event);
					switch (eventTextAlignment) {
					case CENTRE:
						styleBuilder.append("text-align: center;");

						break;
					case RIGHT:
						styleBuilder.append("text-align: right;");
						break;

					default:
						break;
					}
				}
			}
		}

		border |= IBorderProvider.RIGHT;

		if (border != IBorderProvider.NONE) {
			if ((border & IBorderProvider.BOTTOM) == IBorderProvider.BOTTOM) {
				styleBuilder.append("border-bottom: 1 solid #000;");
			}
			if ((border & IBorderProvider.TOP) == IBorderProvider.TOP) {
				styleBuilder.append("border-top: 1 solid #000;");
			}
			if ((border & IBorderProvider.RIGHT) == IBorderProvider.RIGHT) {
				styleBuilder.append("border-right: 1 solid #000;");
			}
			if ((border & IBorderProvider.LEFT) == IBorderProvider.LEFT) {
				styleBuilder.append("border-left: 1 solid #000;");
			}
		}

		if (styleBuilder.length() != 0) {
			return new String[] { String.format("style='%s'", styleBuilder.toString()) };
		}
		return null;
	}
	
	@Override
	public int getBorders(final GridItem item, final int i) {
		
		// Replicate the month divider line.
		// Shoud column lines except for middle columns
		
		final StringBuilder styleBuilder = new StringBuilder();
		
		final GridColumn column = gridViewer.getGrid().getColumn(i);
		
		// Attempt to determine if an object is late or not.
		int border = IBorderProvider.NONE;
		{
			final Pair<LocalDate, Integer> pair = (Pair<LocalDate, Integer>) item.getData();
			if (pair != null) {
				
				final LocalDate date = pair.getFirst();
				final int index = pair.getSecond();
				
				final CalendarColumn cc = manager.getCalendarColumn(i);
				final EventProvider provider = cc.getProvider();
				IBorderProvider borderProvider = null;
				if (provider instanceof IBorderProvider) {
					borderProvider = (IBorderProvider) provider;
				}
				final Event[] events = provider.getEvents(date);
				final Event event = index < events.length ? events[index] : null;
				if (event != null) {
					final Color colour = cc.getLabeller().getForeground(date, event);
					if (colour != null) {
						styleBuilder.append(String.format("color: #%2X%2X%2X;", colour.getRed(), colour.getGreen(), colour.getBlue()));
					}
					if (borderProvider != null) {
						border |= borderProvider.getBorders(date, event);
					}
					final Alignment eventTextAlignment = cc.getLabeller().getTextAlignment(date, event);
					switch (eventTextAlignment) {
					case CENTRE:
						styleBuilder.append("text-align: center;");
						
						break;
					case RIGHT:
						styleBuilder.append("text-align: right;");
						break;
						
					default:
						break;
					}
				}
			}
		}
		
		border |= IBorderProvider.RIGHT;

		return border;
	}
	@Override
	public Object getTypedValue(final GridItem item, final int i) {
		
		// Replicate the month divider line.
		// Shoud column lines except for middle columns
		
		final StringBuilder styleBuilder = new StringBuilder();
		
		final GridColumn column = gridViewer.getGrid().getColumn(i);
		
		// Attempt to determine if an object is late or not.
		int border = IBorderProvider.NONE;
		{
			final Pair<LocalDate, Integer> pair = (Pair<LocalDate, Integer>) item.getData();
			if (pair != null) {
				
				final LocalDate date = pair.getFirst();
				final int index = pair.getSecond();
				
				final CalendarColumn cc = manager.getCalendarColumn(i);
				final EventProvider provider = cc.getProvider();
				final Event[] events = provider.getEvents(date);
				final Event event = index < events.length ? events[index] : null;
				if (event != null) {
					// Get the date?
					return date;
				}
			}
		}
		
		
		return null;
	}

	@Override
	public String getTopLeftCellLowerText() {
		return "";
	}

	@Override
	public String getTopLeftCellUpperText() {

		return "";
	}

	@Override
	public String getTopLeftCellText() {
		return "";
	}

	@Override
	public @NonNull String @Nullable [] getAdditionalPreRows() {
		return null;
	}
}