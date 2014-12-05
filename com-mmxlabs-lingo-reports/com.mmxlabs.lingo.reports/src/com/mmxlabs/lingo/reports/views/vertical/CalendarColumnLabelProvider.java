package com.mmxlabs.lingo.reports.views.vertical;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.nebula.jface.gridviewer.GridColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * This class allows for convenient column label provider creation in a calendar grid: the provider is initialised with a particular data object (e.g. a sequence from a schedule) and will delegate
 * cell formatting & contents to methods based on the data object and the date.
 */
public abstract class CalendarColumnLabelProvider<T> extends GridColumnLabelProvider {
	protected T data;
	protected DateFormat df;

	public CalendarColumnLabelProvider(final DateFormat df, final T object) {
		this.df = df;
		data = object;
	}

	public T getData() {
		return data;
	}

	@Override
	public String getRowHeaderText(final Object element) {

		final Date date = (Date) element;
		return df.format(date);
	}

	@Override
	public String getText(final Object element) {
		return getText((Date) element, data);
	}

	@Override
	public Font getFont(final Object element) {
		return getFont((Date) element, data);
	}

	@Override
	public Color getBackground(final Object element) {
		return getBackground((Date) element, data);
	}

	@Override
	public Color getForeground(final Object element) {
		return getForeground((Date) element, data);
	}

	/** Returns the text content of the cell. */
	abstract protected String getText(Date element, T object);

	/** Returns the desired font of the cell. */
	protected Font getFont(final Date element, final T object) {
		return null;
	}

	/** Returns the desired font of the cell. */
	protected Color getBackground(final Date element, final T object) {
		return null;
	}

	/** Returns the desired font of the cell. */
	protected Color getForeground(final Date element, final T object) {
		return null;
	}

}
