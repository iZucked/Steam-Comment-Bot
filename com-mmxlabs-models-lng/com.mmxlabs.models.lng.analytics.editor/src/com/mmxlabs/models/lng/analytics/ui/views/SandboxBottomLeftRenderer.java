package com.mmxlabs.models.lng.analytics.ui.views;

import org.eclipse.nebula.widgets.grid.internal.DefaultColumnFooterRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;

import com.mmxlabs.models.ui.tabular.TableColourPalette;
import com.mmxlabs.models.ui.tabular.TableColourPalette.ColourElements;
import com.mmxlabs.models.ui.tabular.TableColourPalette.TableItems;

final class SandboxBottomLeftRenderer extends DefaultColumnFooterRenderer {
	@Override
	public void paint(GC gc, Object value) {
		
		gc.setBackground( TableColourPalette.getInstance().getColourFor(TableItems.RowHeader, ColourElements.Background));

		gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));

		gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		gc.drawLine(getBounds().x, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y);
		gc.drawLine(getBounds().x, getBounds().y + getBounds().height, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height);
		gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height);
	}
}