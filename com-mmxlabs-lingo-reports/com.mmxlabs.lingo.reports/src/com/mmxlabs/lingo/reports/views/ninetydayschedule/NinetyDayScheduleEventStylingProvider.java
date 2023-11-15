/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.AbstractScheduleEventStylingProvider;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public class NinetyDayScheduleEventStylingProvider implements IScheduleEventStylingProvider {

	private IScheduleEventStylingProvider delegate;
	private static final IScheduleEventStylingProvider defaultProvider = new AbstractScheduleEventStylingProvider("default") { };

	public NinetyDayScheduleEventStylingProvider(final @NonNull IScheduleEventStylingProvider delegate) {
		this.delegate = delegate;
	}

	public NinetyDayScheduleEventStylingProvider() {
		this(defaultProvider);
	}

	public IScheduleEventStylingProvider getDelegate() {
		return delegate;
	}

	public void setDelegate(final IScheduleEventStylingProvider delegate) {
		assert delegate != this;
		if (delegate == null) {
			this.delegate = defaultProvider;
		} else {
			this.delegate = delegate;
		}
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public @Nullable Color getBackgroundColour(final DrawableScheduleEvent e, final Color defaultColour) {
		return delegate.getBackgroundColour(e, defaultColour);
	}

	@Override
	public @Nullable Color getBorderColour(final DrawableScheduleEvent e, final Color defaultColour) {
		return delegate.getBorderColour(e, defaultColour);
	}

	@Override
	public int getBorderThickness(final DrawableScheduleEvent e, final int defaultValue) {
		return delegate.getBorderThickness(e, defaultValue);
	}

	@Override
	public boolean getIsBorderInner(final DrawableScheduleEvent e, final boolean defaultValue) {
		return delegate.getIsBorderInner(e, defaultValue);
	}

	@Override
	public void setID(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getID() {
		return delegate.getID();
	}
}
