/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.renderers;

import org.eclipse.nebula.widgets.grid.AbstractRenderer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.mmxlabs.models.ui.tabular.TableColourPalette;
import com.mmxlabs.models.ui.tabular.TableColourPalette.ColourElements;
import com.mmxlabs.models.ui.tabular.TableColourPalette.TableItems;

/**
 * A renderer for the last empty column header.
 *
 * @author chris.gross@us.ibm.com
 * @since 2.0.0
 */
public class EmptyColumnHeaderRenderer extends AbstractRenderer
{

    /** 
     * {@inheritDoc}
     */
    public Point computeSize(GC gc, int wHint, int hHint, Object value)
    {
        return new Point(wHint, hHint);
    }

    /** 
     * {@inheritDoc}
     */
    public void paint(GC gc, Object value)
    {
        gc.setBackground(TableColourPalette.getInstance().getColourFor(TableItems.ColumnHeaders, ColourElements.Background));

        gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width + 1,
                         getBounds().height + 1);

    }

}
