/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.widgets;

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * A class to a grey input hint message to a text control when the input is
 * empty. Taken from a StackOverflow answer
 * 
 * Taken from
 * https://stackoverflow.com/questions/38073463/how-to-display-a-hint-message-in-an-swt-styledtext
 *
 */
public class TextEmptyMessageWrapper {

	private static final int MARGIN_X = 4;
	private static final int MARGIN_Y = 2;
	private static final int[] INVALIDATE_EVENTS = { SWT.Activate, SWT.Deactivate, SWT.Show, SWT.Hide };

	private final Text textWidget;
	private final Listener invalidateListener;
	private final Color textColor;
	private String message;
	private int verticalOffsetLines;

	public static TextEmptyMessageWrapper wrap(final Text textWidget, final String message) {
		final TextEmptyMessageWrapper stm = new TextEmptyMessageWrapper(textWidget);
		stm.setMessage(message);
		return stm;
	}

	public TextEmptyMessageWrapper(final Text textWidget) {
		this.textWidget = Objects.requireNonNull(textWidget);
		this.invalidateListener = this::handleInvalidatedEvent;
		this.textColor = getTextColor();
		this.message = "";
		initialize();
	}

	public void setMessage(final String message) {
		this.message = Objects.requireNonNull(message);
		this.textWidget.redraw();
	}

	public boolean isMessageShowing() {
		return !message.isEmpty() && textWidget.getText().isEmpty();
	}

	private void initialize() {
		textWidget.addListener(SWT.Paint, this::handlePaintEvent);
		textWidget.addListener(SWT.Resize, event -> textWidget.redraw());
		textWidget.addListener(SWT.Dispose, this::handleDispose);
		for (final int eventType : INVALIDATE_EVENTS) {
			textWidget.getDisplay().addFilter(eventType, invalidateListener);
		}

		textWidget.addModifyListener(event -> textWidget.redraw());
	}

	private Color getTextColor() {
		return textWidget.getDisplay().getSystemColor(SWT.COLOR_GRAY);
	}

	private void handlePaintEvent(final Event event) {
		if (isMessageShowing()) {
			drawHint(event.gc, event.x, event.y);
		}
	}

	private void handleDispose(final Event event) {
		for (final int eventType : INVALIDATE_EVENTS) {
			textWidget.getDisplay().removeFilter(eventType, invalidateListener);
		}
	}

	private void handleInvalidatedEvent(final Event event) {
		textWidget.redraw();
	}

	private void drawHint(final GC gc, final int x, final int y) {
		final int verticalOffset = verticalOffsetLines * gc.getFontMetrics().getHeight();
		gc.setForeground(textColor);
		gc.drawText(message, x + MARGIN_X, y + MARGIN_Y - verticalOffset, SWT.DRAW_DELIMITER | SWT.DRAW_TRANSPARENT);
	}

}