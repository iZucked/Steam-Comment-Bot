/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import org.eclipse.nebula.widgets.grid.AbstractRenderer;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextLayout;

/**
 * The row header renderer.
 *
 * @author chris.gross@us.ibm.com
 */
public class NoIndexRowHeaderRenderer extends AbstractRenderer {

	int leftMargin = 6;

	int rightMargin = 8;

	int topMargin = 3;

	int bottomMargin = 3;

	private TextLayout textLayout;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(GC gc, Object value) {
		GridItem item = (GridItem) value;

		// String text = getHeaderText(item);

		gc.setFont(getDisplay().getSystemFont());

		Color background = getHeaderBackground(item);
		if (background == null) {
			background = getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		}
		gc.setBackground(background);

		if (isSelected() && item.getParent().getCellSelectionEnabled()) {
			gc.setBackground(item.getParent().getCellHeaderSelectionBackground());
		}

		gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width, getBounds().height + 1);

		if (!item.getParent().getCellSelectionEnabled()) {
			if (isSelected()) {
				gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
			} else {
				gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
			}

			gc.drawLine(getBounds().x, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y);
			gc.drawLine(getBounds().x, getBounds().y, getBounds().x, getBounds().y + getBounds().height - 1);

			if (!isSelected()) {
				gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
				gc.drawLine(getBounds().x + 1, getBounds().y + 1, getBounds().x + getBounds().width - 2, getBounds().y + 1);
				gc.drawLine(getBounds().x + 1, getBounds().y + 1, getBounds().x + 1, getBounds().y + getBounds().height - 2);
			}

			if (isSelected()) {
				gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
			} else {
				gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
			}
			gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height - 1);
			gc.drawLine(getBounds().x, getBounds().y + getBounds().height - 1, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height - 1);

			if (!isSelected()) {
				gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
				gc.drawLine(getBounds().x + getBounds().width - 2, getBounds().y + 1, getBounds().x + getBounds().width - 2, getBounds().y + getBounds().height - 2);
				gc.drawLine(getBounds().x + 1, getBounds().y + getBounds().height - 2, getBounds().x + getBounds().width - 2, getBounds().y + getBounds().height - 2);
			}
		} else {
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));

			gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height - 1);
			gc.drawLine(getBounds().x, getBounds().y + getBounds().height - 1, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height - 1);
		}

		int x = leftMargin;

		Image image = getHeaderImage(item);

		if (image != null) {
			if (isSelected() && !item.getParent().getCellSelectionEnabled()) {
				gc.drawImage(image, x + 1, getBounds().y + 1 + (getBounds().height - image.getBounds().height) / 2);
				x += 1;
			} else {
				gc.drawImage(image, x, getBounds().y + (getBounds().height - image.getBounds().height) / 2);
			}
			x += image.getBounds().width + 5;
		}

		Color foreground = getHeaderForeground(item);
		if (foreground == null) {
			foreground = getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
		}

		gc.setForeground(foreground);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point computeSize(GC gc, int wHint, int hHint, Object value) {
		GridItem item = (GridItem) value;

		String text = getHeaderText(item);
		Image image = getHeaderImage(item);

		int x = 0;

		x += leftMargin;

		if (image != null) {
			x += image.getBounds().width + 5;
		}

		x += gc.stringExtent(text).x + rightMargin;

		int y = 0;

		y += topMargin;

		if (image != null) {
			y += Math.max(gc.getFontMetrics().getHeight(), image.getBounds().height);
		} else {
			y += gc.getFontMetrics().getHeight();
		}

		y += bottomMargin;

		return new Point(x, y);
	}

	private Image getHeaderImage(GridItem item) {
		return item.getHeaderImage();
	}

	private String getHeaderText(GridItem item) {
		String text = item.getHeaderText();
		if (text == null) {
			text = (item.getParent().indexOf(item) + 1) + "";
		}
		return text;
	}

	private Color getHeaderBackground(GridItem item) {
		return item.getHeaderBackground();
	}

	private Color getHeaderForeground(GridItem item) {
		return item.getHeaderForeground();
	}
}
