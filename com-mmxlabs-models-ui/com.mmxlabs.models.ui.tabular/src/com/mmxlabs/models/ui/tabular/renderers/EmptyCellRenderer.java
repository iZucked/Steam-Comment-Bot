/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.renderers;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.mmxlabs.models.ui.tabular.TableColourPalette;

/**
 * The empty cell renderer.
 *
 * @author chris.gross@us.ibm.com
 * @since 2.0.0
 */
public class EmptyCellRenderer extends GridCellRenderer {

	/**
	 * {@inheritDoc}
	 */
	public void paint(GC gc, Object value) {

		Grid table = null;
		if (value instanceof Grid)
			table = (Grid) value;

		GridItem item;
		if (value instanceof GridItem) {
			item = (GridItem) value;
			table = item.getParent();
		}

		boolean drawBackground = true;

		if (isSelected()) {
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
		} else {
			if (table.isEnabled()) {
				drawBackground = false;
			} else {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			}
			gc.setForeground(table.getForeground());
		}

		if (drawBackground)
			gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width + 1, getBounds().height);

		if (table.getLinesVisible()) {
			if (!TableColourPalette.getInstance().SANDBOX_WHITER_THEME) {
				gc.setForeground(table.getLineColor());
				gc.drawLine(getBounds().x, getBounds().y + getBounds().height, getBounds().x + getBounds().width, getBounds().y + getBounds().height);
				gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Point computeSize(GC gc, int wHint, int hHint, Object value) {
		return new Point(wHint, hHint);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean notify(int event, Point point, Object value) {
		return false;
	}

}
