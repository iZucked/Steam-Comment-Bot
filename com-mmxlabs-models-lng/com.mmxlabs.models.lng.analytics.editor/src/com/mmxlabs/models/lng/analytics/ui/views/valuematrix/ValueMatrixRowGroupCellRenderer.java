package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.Transform;

import com.mmxlabs.models.ui.tabular.TableColourPalette;
import com.mmxlabs.models.ui.tabular.TableColourPalette.ColourElements;
import com.mmxlabs.models.ui.tabular.TableColourPalette.TableItems;

public class ValueMatrixRowGroupCellRenderer extends GridCellRenderer {
	int leftMargin = 3;

	int rightMargin = 3;
	int topMargin = 6;

	int bottomMargin = 6;

	int arrowMargin = 6;

	int imageSpacing = 3;

	private TextLayout textLayout;

	public ValueMatrixRowGroupCellRenderer() {
		setAlignment(SWT.CENTER);
	}

	@Override
	public boolean notify(int event, Point point, Object value) {
		return false;
	}

	@Override
	public void paint(GC gc, Object value) {
		final GridItem item = (GridItem) value;

		gc.setFont(item.getFont());

		boolean flat = true;
		boolean drawSelected = isMouseDown() && isHover();

		gc.setBackground(TableColourPalette.getInstance().getColourFor(TableItems.RowHeader, ColourElements.Background));
		if (flat && isSelected()) {
			gc.setBackground(item.getParent().getCellHeaderSelectionBackground());
		}

		gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width, getBounds().height);

		int pushedDrawingOffset = 0;
		if (drawSelected) {
			pushedDrawingOffset = 1;
		}

		int x = leftMargin;
		int y;
		if (item.getImage() != null) {
			x += item.getImage().getBounds().width + imageSpacing;
		}

		int width = getBounds().width - x;
		width -= rightMargin;
		gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.RowHeader, ColourElements.Foreground));
		final String itemText = item.getText();
		int len = gc.stringExtent(itemText).y;
		int len2 = gc.stringExtent(itemText).x;
		y = getBounds().y + getBounds().height/2 + len2/2 + topMargin - bottomMargin;
		if (len < width) {
			x += (width - len) / 2;
		}

		if (!isWordWrap()) {
			final Transform t = new Transform(gc.getDevice());
			t.translate(getBounds().x + x + pushedDrawingOffset, y + pushedDrawingOffset);
			t.rotate(-90.0f);
			gc.setTransform(t);
			gc.drawString(itemText, 0, 0, true);
			gc.setTransform(null);
			t.dispose();
		} else {
			getTextLayout(gc, item);
			textLayout.setWidth(width < 1 ? 1 : width);
			textLayout.setText(itemText);
			Transform t = new Transform(gc.getDevice());
			t.translate(getBounds().x + x + pushedDrawingOffset, y + pushedDrawingOffset);
			t.rotate(-90.0f);
			gc.setTransform(t);
			textLayout.draw(gc, 0, 0);
		}
		if (!flat) {

			gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Background));

			gc.drawLine(getBounds().x, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y);
			gc.drawLine(getBounds().x, getBounds().y, getBounds().x, getBounds().y + getBounds().height - 1);

			if (!drawSelected) {
				gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Background));
				gc.drawLine(getBounds().x + 1, getBounds().y + 1, getBounds().x + getBounds().width - 2, getBounds().y + 1);
				gc.drawLine(getBounds().x + 1, getBounds().y + 1, getBounds().x + 1, getBounds().y + getBounds().height - 2);
			}

			gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Background));
			gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height - 1);
			gc.drawLine(getBounds().x, getBounds().y + getBounds().height - 1, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height - 1);

			if (!drawSelected) {
				gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Background));
				gc.drawLine(getBounds().x + getBounds().width - 2, getBounds().y + 1, getBounds().x + getBounds().width - 2, getBounds().y + getBounds().height - 2);
				gc.drawLine(getBounds().x + 1, getBounds().y + getBounds().height - 2, getBounds().x + getBounds().width - 2, getBounds().y + getBounds().height - 2);
			}

		} else {
			gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Background));

			gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height - 1);
			gc.drawLine(getBounds().x, getBounds().y + getBounds().height - 1, getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height - 1);
		}
	}

	@Override
	public Point computeSize(GC gc, int wHint, int hHint, Object value) {
		final GridItem item = (GridItem) value;
		gc.setFont(item.getFont());

		int x = leftMargin + gc.getFontMetrics().getHeight() + rightMargin;
		int y = topMargin;

		if (item.getImage() != null) {
			x += item.getImage().getBounds().width + imageSpacing;
			y = Math.max(y, topMargin + item.getImage().getBounds().height + bottomMargin);
		}
		String itemText = item.getText();
		if (!isWordWrap()) {
			y += gc.stringExtent(itemText).x;
		} else {
			int plainTextWidth = (wHint == SWT.DEFAULT ? getBounds().height : hHint)  - y - bottomMargin;
			getTextLayout(gc, item);
			textLayout.setText(itemText);
			textLayout.setWidth(plainTextWidth < 1 ? 1 : plainTextWidth);

			y += plainTextWidth + bottomMargin;

			int textHeight = leftMargin;
			textHeight += textLayout.getBounds().width;
			textHeight += rightMargin;
			x = Math.max(x, textHeight);
		}

//		y += computeControlSize(item).y;

		return new Point(x, y);
	}

//	private Point computeControlSize(final GridItem item) {
//		if (item.get)
//	}

	private void getTextLayout(final GC gc, final GridItem item) {
		if (textLayout == null) {
			textLayout = new TextLayout(gc.getDevice());
			textLayout.setFont(gc.getFont());
			item.getParent().addDisposeListener(e -> textLayout.dispose());
		}
		textLayout.setAlignment(getAlignment());
	}

}
