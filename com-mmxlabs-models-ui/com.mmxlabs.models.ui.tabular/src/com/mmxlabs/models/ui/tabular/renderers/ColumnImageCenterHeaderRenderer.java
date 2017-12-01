/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.renderers;

import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridHeaderRenderer;
import org.eclipse.nebula.widgets.grid.internal.SortArrowRenderer;
import org.eclipse.nebula.widgets.grid.internal.TextUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.ui.tabular.TableColourPalette;
import com.mmxlabs.models.ui.tabular.TableColourPalette.ColourElements;
import com.mmxlabs.models.ui.tabular.TableColourPalette.TableItems;

/**
 * The column header renderer.
 *
 * @author chris.gross@us.ibm.com
 * @since 2.0.0
 */
@SuppressWarnings("restriction")
public class ColumnImageCenterHeaderRenderer extends GridHeaderRenderer {

	int leftMargin = 6;

	int rightMargin = 6;

	int topMargin = 3;

	int bottomMargin = 3;

	int arrowMargin = 6;

	int imageSpacing = 3;

	private boolean forceCenter = false;

	private SortArrowRenderer arrowRenderer = new SortArrowRenderer();

	private TextLayout textLayout;

	/**
	 * {@inheritDoc}
	 */
	public Point computeSize(GC gc, int wHint, int hHint, Object value) {
		GridColumn column = (GridColumn) value;

		gc.setFont(column.getHeaderFont());

		int x = leftMargin;
		int y = topMargin + gc.getFontMetrics().getHeight() + bottomMargin;

		if (column.getImage() != null) {
			x += column.getImage().getBounds().width + imageSpacing;

			y = Math.max(y, topMargin + column.getImage().getBounds().height + bottomMargin);
		}
		if (!isWordWrap()) {
			x += gc.stringExtent(column.getText()).x + rightMargin;
		} else {
			int plainTextWidth;
			if (wHint == SWT.DEFAULT)
				plainTextWidth = getBounds().width - x - rightMargin;
			else
				plainTextWidth = wHint - x - rightMargin;

			getTextLayout(gc, column);
			textLayout.setText(column.getText());
			textLayout.setWidth(plainTextWidth < 1 ? 1 : plainTextWidth);

			x += plainTextWidth + rightMargin;

			int textHeight = topMargin;
			textHeight += textLayout.getBounds().height;
			textHeight += bottomMargin;

			y = Math.max(y, textHeight);
		}

		y += computeControlSize(column).y;

		return new Point(x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(GC gc, Object value) {
		GridColumn column = (GridColumn) value;

		// set the font to be used to display the text.
		gc.setFont(column.getHeaderFont());

		boolean flat = true;// (column.getParent().getCellSelectionEnabled() && !column.getMoveable());

		boolean drawSelected = ((isMouseDown() && isHover()));

		gc.setBackground(TableColourPalette.getInstance().getColourFor(TableItems.ColumnHeaders, ColourElements.Background));

		if (flat && isSelected()) {
			gc.setBackground(column.getParent().getCellHeaderSelectionBackground());
		}

		gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width, getBounds().height);

		int pushedDrawingOffset = 0;
		if (drawSelected) {
			pushedDrawingOffset = 1;
		}

		int x = leftMargin;
		if (column.getImage() != null) {
			Rectangle bounds = getBounds();
			Rectangle imgBounds = column.getImage().getBounds();
			int width = bounds.width / 2;
			width -= imgBounds.width / 2;
			int height = bounds.height / 2;
			height -= imgBounds.height / 2;

			x = width > 0 ? bounds.x + width : bounds.x;
			int y = height > 0 ? bounds.y + height : bounds.y;

			gc.drawImage(column.getImage(), x, y);
		}
		x = leftMargin;
		int width = getBounds().width - x;

		if (column.getSort() == SWT.NONE) {
			width -= rightMargin;
		} else {
			width -= arrowMargin + arrowRenderer.getSize().x + arrowMargin;
		}

		gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.ColumnHeaders, ColourElements.Foreground));

		int y = bottomMargin;

		if (column.getHeaderControl() == null) {
			y = getBounds().y + getBounds().height - bottomMargin - gc.getFontMetrics().getHeight();
		} else {
			y = getBounds().y + getBounds().height - bottomMargin - gc.getFontMetrics().getHeight() - computeControlSize(column).y;
		}

		String text = column.getText();

		if (!isWordWrap()) {
			text = TextUtils.getShortString(gc, text, width);
			// y -= gc.getFontMetrics().getHeight();
		}

		if (column.getAlignment() == SWT.CENTER || forceCenter) {
			int len = gc.stringExtent(text).x;
			if (len < width) {
				x += (width - len) / 2;
			}
		} else if (column.getAlignment() == SWT.RIGHT) {
			int len = gc.stringExtent(text).x;
			if (len < width) {
				x += width - len;
			}
		}

		if (!isWordWrap()) {
			gc.drawString(text, getBounds().x + x + pushedDrawingOffset, y + pushedDrawingOffset, true);
		} else {
			getTextLayout(gc, column);
			textLayout.setWidth(width < 1 ? 1 : width);
			textLayout.setText(text);
			y -= textLayout.getBounds().height;

			// remove the first line shift
			y += gc.getFontMetrics().getHeight();

			if (column.getParent().isAutoHeight()) {
				column.getParent().recalculateHeader();
			}

			textLayout.draw(gc, getBounds().x + x + pushedDrawingOffset, y + pushedDrawingOffset);
		}

		if (column.getSort() != SWT.NONE) {
			if (column.getHeaderControl() == null) {
				y = getBounds().y + ((getBounds().height - arrowRenderer.getBounds().height) / 2) + 1;
			} else {
				y = getBounds().y + ((getBounds().height - computeControlSize(column).y - arrowRenderer.getBounds().height) / 2) + 1;
			}

			arrowRenderer.setSelected(column.getSort() == SWT.UP);
			if (drawSelected) {
				arrowRenderer.setLocation(getBounds().x + getBounds().width - arrowMargin - arrowRenderer.getBounds().width + 1, y);
			} else {
				if (column.getHeaderControl() == null) {
					y = getBounds().y + ((getBounds().height - arrowRenderer.getBounds().height) / 2);
				} else {
					y = getBounds().y + ((getBounds().height - computeControlSize(column).y - arrowRenderer.getBounds().height) / 2);
				}
				arrowRenderer.setLocation(getBounds().x + getBounds().width - arrowMargin - arrowRenderer.getBounds().width, y);
			}
			arrowRenderer.paint(gc, null);
		}

		if (!flat) {

			if (drawSelected) {
				gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Background));
			} else {
				gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Background));
			}

			gc.drawLine(getBounds().x, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y);
			gc.drawLine(getBounds().x, getBounds().y, getBounds().x, getBounds().y + getBounds().height - 1);

			if (!drawSelected) {
				gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Background));
				gc.drawLine(getBounds().x + 1, getBounds().y + 1, getBounds().x + getBounds().width - 2, getBounds().y + 1);
				gc.drawLine(getBounds().x + 1, getBounds().y + 1, getBounds().x + 1, getBounds().y + getBounds().height - 2);
			}

			if (drawSelected) {
				gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Background));
			} else {
				gc.setForeground(TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Background));
			}
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

	/**
	 * {@inheritDoc}
	 */
	public void setDisplay(Display display) {
		super.setDisplay(display);
		arrowRenderer.setDisplay(display);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean notify(int event, Point point, Object value) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Rectangle getTextBounds(Object value, boolean preferred) {
		GridColumn column = (GridColumn) value;

		int x = leftMargin;

		if (column.getImage() != null) {
			x += column.getImage().getBounds().width + imageSpacing;
		}

		GC gc = new GC(column.getParent());
		gc.setFont(column.getParent().getFont());
		int y = getBounds().height - bottomMargin - gc.getFontMetrics().getHeight();

		Rectangle bounds = new Rectangle(x, y, 0, 0);

		Point p = gc.stringExtent(column.getText());

		bounds.height = p.y;

		if (preferred) {
			bounds.width = p.x;
		} else {
			int width = getBounds().width - x;
			if (column.getSort() == SWT.NONE) {
				width -= rightMargin;
			} else {
				width -= arrowMargin + arrowRenderer.getSize().x + arrowMargin;
			}
			bounds.width = width;
		}

		gc.dispose();

		return bounds;
	}

	public void setCenter(boolean center) {
		// forceCenter = center;
	}

	/**
	 * @return the bounds reserved for the control
	 */
	protected Rectangle getControlBounds(Object value, boolean preferred) {
		Rectangle bounds = getBounds();
		GridColumn column = (GridColumn) value;
		Point controlSize = computeControlSize(column);

		int y = getBounds().y + getBounds().height - bottomMargin - controlSize.y;

		return new Rectangle(bounds.x + 3, y, bounds.width - 6, controlSize.y);
	}

	private Point computeControlSize(GridColumn column) {
		if (column.getHeaderControl() != null) {
			return column.getHeaderControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		}
		return new Point(0, 0);
	}

	private void getTextLayout(GC gc, GridColumn column) {
		if (textLayout == null) {
			textLayout = new TextLayout(gc.getDevice());
			textLayout.setFont(gc.getFont());
			column.getParent().addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					textLayout.dispose();
				}
			});
		}

		if (forceCenter) {
			textLayout.setAlignment(SWT.CENTER);
		} else {
			textLayout.setAlignment(column.getAlignment());
		}
	}
}
