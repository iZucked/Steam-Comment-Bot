/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.renderers;

import org.eclipse.nebula.widgets.grid.AbstractRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.mmxlabs.models.ui.tabular.TableColourPalette;
import com.mmxlabs.models.ui.tabular.TableColourPalette.ColourElements;
import com.mmxlabs.models.ui.tabular.TableColourPalette.TableItems;

/**
 * The renderer for the empty top left area when both column and row headers are visible.
 *
 * @author chris.gross@us.ibm.com
 * @since 2.0.0
 */
public class TopLeftRenderer extends AbstractRenderer {

	/**
	 * {@inheritDoc}
	 */
	public Point computeSize(GC gc, int wHint, int hHint, Object value) {
		return new Point(wHint, hHint);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(GC gc, Object value) {
		gc.setBackground(TableColourPalette.getInstance().getColourFor(TableItems.TopLeftHeader, ColourElements.Background));

		gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width - 1, getBounds().height + 1);

//		gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Foreground));
//
//		gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height);
//
//		gc.drawLine(getBounds().x, getBounds().y + getBounds().height - 1, getBounds().x + getBounds().width, getBounds().y + getBounds().height - 1);

	}

}
