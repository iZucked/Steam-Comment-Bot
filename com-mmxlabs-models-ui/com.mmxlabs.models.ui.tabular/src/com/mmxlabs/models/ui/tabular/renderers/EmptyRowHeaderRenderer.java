/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.renderers;

import org.eclipse.nebula.widgets.grid.AbstractRenderer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.mmxlabs.models.ui.tabular.TableColourPalette;
import com.mmxlabs.models.ui.tabular.TableColourPalette.ColourElements;
import com.mmxlabs.models.ui.tabular.TableColourPalette.TableItems;

/**
 * The empty row header renderer.
 *
 * @author chris.gross@us.ibm.com
 * @since 2.0.0
 */
public class EmptyRowHeaderRenderer extends AbstractRenderer
{

    /** 
     * {@inheritDoc}
     */
    @Override
	public void paint(GC gc, Object value)
    {

        gc.setBackground(TableColourPalette.getInstance().getColourFor(TableItems.ColumnHeaders, ColourElements.Background));


        gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width, getBounds().height + 1);

        gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Foreground));

        Grid grid = (Grid) value;
        
        if (!grid.getCellSelectionEnabled())
        {
        		// Draw bottom line
            gc.drawLine(getBounds().x, getBounds().y + getBounds().height - 1, getBounds().x + getBounds().width,
                        getBounds().y + getBounds().height - 1);
            
            // Drow right line
            gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y, getBounds().x
                                                                              + getBounds().width - 1,

                        getBounds().y + getBounds().height - 1);
        }
        else
        {
        	if (!TableColourPalette.getInstance().SANDBOX_WHITER_THEME) {
	            gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Foreground));
	
	
	            gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y, getBounds().x
	                                                                              + getBounds().width - 1,
	                        getBounds().y + getBounds().height - 1);
	            gc.drawLine(getBounds().x, getBounds().y + getBounds().height - 1, getBounds().x
	                                                                               + getBounds().width - 1,
	                        getBounds().y + getBounds().height - 1);
        	}
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
	public Point computeSize(GC gc, int wHint, int hHint, Object value)
    {
        return new Point(wHint, hHint);
    }

}
