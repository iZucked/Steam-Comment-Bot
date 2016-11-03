/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;

import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;

/**
 * A control for drawing a wiring diagram. This just displays an arbitrary bipartite graph / matching.
 * 
 * The graph must have equal numbers of vertices on each side, which are identified by the integers from 0 to n-1. The wiring itself is described in the {@link #rootData} list. The ith element of the
 * list contains the index of the right-hand element to which left hand element i is connected, or -1 if there is no connection.
 * 
 * Thus wiring.get(1) gives the right-hand node connected to left-hand node 1, and wiring.indexOf(1) gives the left-hand node connected to right-hand node 1.
 * 
 * TODO allow mismatched states, where there may be differing numbers of left-hand and right-hand terminals.
 * 
 * @author Simon Goodall (original work by Tom Hinton)
 * 
 */
public class ResultsSetWiringDiagram implements PaintListener {

	// TODO: Use Colour Palette API
	static final Color Green = new Color(Display.getCurrent(), new RGB(0, 180, 50));
	static final Color Light_Green = new Color(Display.getCurrent(), new RGB(100, 255, 100));
	static final Color White = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	static final Color Light_Grey = new Color(Display.getCurrent(), new RGB(240, 240, 240));
	static final Color Grey = new Color(Display.getCurrent(), new RGB(200, 200, 200));

	static final Color InvalidTerminalColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private final Color InvalidWireColour = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);

	static final Color ValidTerminalColour = Light_Green;
	private final Color RewirableColour = Grey;// Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private final Color FixedWireColour = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

	private int terminalSize = 11;
	private int pathWidth = 2;
	private final int borderWidth = 1;

	/**
	 * The Grid control we are rendering on to
	 */
	private final Grid grid;
	/**
	 * The {@link GridViewerColumn} column which is the "Wiring" column
	 */
	private final GridViewerColumn wiringColumn;
	/**
	 * The {@link ChangeSetRoot} data structure containing all the required data
	 */
	private OptionAnalysisModel table;

	/**
	 * Create a new wiring diagram
	 * 
	 * @param parent
	 * @param style
	 */
	public ResultsSetWiringDiagram(final Grid grid, final GridViewerColumn wiringColumn) {
		this.grid = grid;
		this.wiringColumn = wiringColumn;
		this.grid.addPaintListener(this);
	}

	public int getTerminalSize() {
		return terminalSize;
	}

	public synchronized void setTerminalSize(final int terminalSize) {
		this.terminalSize = terminalSize;
	}

	public int getPathWidth() {
		return pathWidth;
	}

	public synchronized void setPathWidth(final int pathWidth) {
		this.pathWidth = pathWidth;
	}

	private synchronized Path makeConnector(final Device d, final float x1, final float y1, final float x2, final float y2) {
		final Path path = new Path(d);

		final float xMidpoint = (x1 + x2) / 2.0f;
		final float yMidpoint = (y1 + y2) / 2.0f;
		final float xoff = (Math.max(x1, x2) - xMidpoint) / 2;
		// move to x1,y1
		path.moveTo(x1, y1);
		path.quadTo(x1 + xoff, y1, xMidpoint, yMidpoint);
		path.quadTo(x2 - xoff, y2, x2, y2);

		return path;
	}

	@Override
	public synchronized void paintControl(final PaintEvent e) {

		if (!wiringColumn.getColumn().isVisible()) {
			return;
		}

		final Rectangle ca = getCanvasClientArea();

		// Could be null is column is no longer visible.
		if (ca == null) {
			return;
		}

		if (table == null) {
			return;
		}

		// Copy ref in case of concurrent change during paint
		final OptionAnalysisModel root = table;
		// Get a list of terminal positions from subclass
		Map<AnalysisResultRow, Integer> rowIndices = new HashMap<>();
		Map<Integer, AnalysisResultRow> indexToRow = new HashMap<>();

		final List<Float> terminalPositions = getTerminalPositions(rowIndices, indexToRow);
		final GC graphics = e.gc;

		graphics.setAntialias(SWT.ON);

		final int linewidth = graphics.getLineWidth();
		graphics.setLineWidth(2);

		// Clip to reported client area to avoid overdraw
		graphics.setClipping(ca);
		int rawI = 0;

		graphics.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		// Fill whole area - not for use in a table
		graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));

		// draw paths
		for (ResultSet resultSet : root.getResultSets()) {
			List<AnalysisResultRow> rows = resultSet.getRows();
			for (final AnalysisResultRow row : rows) {

				BaseCaseRow other = null;
				for (BaseCaseRow bcr : root.getBaseCase().getBaseCase()) {
					if (row.getBuyOption() == bcr.getBuyOption()) {
						other = bcr;
						break;
					}
				}
				AnalysisResultRow otherRow = null;
				if (other != null) {
					for (AnalysisResultRow arr : resultSet.getRows()) {
						if (arr.getSellOption() == other.getSellOption()) {
							otherRow = arr;
							break;
						}
					}
				}

				final Integer unsortedSource = rowIndices.get(row);
				final Integer unsortedDestination = rowIndices.get(otherRow);
				if (unsortedSource == null || unsortedDestination == null) {
					// Error?
					continue;
				}
				// Map back between current row (sorted) and data (unsorted)
				final int sortedDestination = unsortedDestination;
				final int sortedSource = unsortedSource;

				// Filtering can lead to missing terminals
				if (sortedDestination < 0 || sortedSource < 0) {
					continue;
				}

				final float startMid = terminalPositions.get(sortedSource);
				final float endMid = terminalPositions.get(sortedDestination);

				// Draw wire - offset by ca.x to as x pos is relative to left hand side
				final Path path = makeConnector(e.display, ca.x + 1.5f * terminalSize, startMid, ca.x + ca.width - 1.5f * terminalSize, endMid);

				graphics.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));

				// if (wire.dashed) {
				graphics.setLineDash(new int[] { 2, 3 });
				// } else {
				// graphics.setLineDash(null);
				// }

				graphics.drawPath(path);
				path.dispose();

				graphics.setLineDash(null);
			}
		}

		// Clear clip rect for dragged wire
		graphics.setClipping((Rectangle) null);

		// Re-apply clip rect
		graphics.setClipping(ca);

		// // draw terminal blobs
		graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		graphics.setLineWidth(borderWidth);
		rawI = 0;
		for (final float midpoint : terminalPositions) {

			// Map back between current row (sorted) and data (unsorted)
			final int i = rawI++;
			// -1 indicates filtered row
			if (i == -1) {
				continue;
			}

			final AnalysisResultRow row = indexToRow.get(i);
			if (row == null) {
				continue;
			}
			// Draw left hand terminal
			if (row.getBuyOption() != null) {
				final BuyOption buy = row.getBuyOption();
				boolean isDES = !AnalyticsBuilder.isShipped(buy);
				final Color terminalColour = ValidTerminalColour;// : InvalidTerminalColour;
				drawTerminal(true, isDES, terminalColour, false, buy instanceof BuyMarket, ca, graphics, midpoint);
			}

			graphics.setLineWidth(linewidth);
			// Draw right hand terminal
			if (row.getSellOption() != null) {
				final SellOption sell = row.getSellOption();
				boolean isFOB = !AnalyticsBuilder.isShipped(sell);
				final Color terminalColour = ValidTerminalColour;// : InvalidTerminalColour;
				drawTerminal(false, !isFOB, terminalColour, false, sell instanceof SellMarket, ca, graphics, midpoint);
			}
		}
	}

	private void drawTerminal(final boolean isLeft, final boolean hollow, Color terminalColour, final boolean isOptional, final boolean isSpot, final Rectangle ca, final GC graphics,
			final float midpoint) {
		Color outlineColour = Green;
		Color fillColour = terminalColour;
		// make non-shipped a bit bigger, but hollow
		final int extraRadius = 1;
		if (isSpot) {
			terminalColour = Grey;
			outlineColour = Grey;
			fillColour = Light_Grey;
		}
		if (hollow) {
			outlineColour = terminalColour;
			fillColour = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		}

		graphics.setLineWidth(2);
		int x = 0;
		if (isLeft) {
			x = ca.x + terminalSize - extraRadius / 2;
		} else {
			x = ca.x + ca.width - 2 * terminalSize - extraRadius / 2;
		}
		int y = (int) (midpoint - (terminalSize) / 2 - 1 - extraRadius / 2);
		graphics.setForeground(outlineColour);
		graphics.setBackground(fillColour);
		graphics.fillOval(x, y, terminalSize + extraRadius, terminalSize + extraRadius);
		graphics.drawOval(x, y, terminalSize + extraRadius, terminalSize + extraRadius);
		// draw internal dot for optional slots
		if (isOptional && !isSpot) {
			graphics.setBackground(Green);
			y = (int) midpoint - (terminalSize / 2) + 3;
			if (isLeft) {
				graphics.fillOval(ca.x + terminalSize + 4, y, 4, 4);
			} else {
				graphics.fillOval(x + 4 + extraRadius / 2, y, 4, 4);
			}
		}
	}

	public synchronized void redraw() {
		grid.redraw();
	}

	protected List<Float> getTerminalPositions(Map<AnalysisResultRow, Integer> rTI, Map<Integer, AnalysisResultRow> iTR) {

		// Determine the mid-point in each row and generate an ordered list of heights.

		// TODO: needs to handle tree state

		// +1 to to make loop simpler
		// final int[] heights = new int[numRows + 1];

		// Pass one, get heights
		final GridItem[] items = grid.getItems();

		final int[] heights = new int[items.length + 1];
		heights[0] = grid.getHeaderHeight();
		{
			int idx = 1;
			for (GridItem item : items) {
				if (item.isVisible()) {
					heights[idx] = 1 + heights[idx - 1] + item.getHeight();

					Object data = item.getData();
					if (data instanceof AnalysisResultRow) {
						AnalysisResultRow changeSetRow = (AnalysisResultRow) data;
						rTI.put(changeSetRow, idx - 1);
						iTR.put(idx - 1, changeSetRow);
					}
					idx++;
				}
			}
		}

		// for (int idx = 1; idx < Math.min(heights.length, 1 + items.length); ++idx) {
		// final GridItem item = items[idx - 1];
		// if (item.getParentItem() == null || item.getParentItem().isExpanded()) {
		// heights[idx] = 1 + heights[idx - 1] + item.getHeight();
		// }
		// Find the row at the top of the table and get it's "height" so we can adjust it later
		ScrollBar verticalBar = grid.getVerticalBar();
		final int vPod = verticalBar == null ? 0 : verticalBar.getSelection();
		final int hOffset = (verticalBar == null || vPod >= heights.length) ? 0 : (heights[vPod]) - grid.getHeaderHeight();
		// Pass 2 get mid-points
		for (int idx = 0; idx < Math.min(heights.length, items.length); ++idx) {
			final GridItem item = items[idx];
			heights[idx] += item.getHeight() / 2;
		}

		// Pass 3, offset to so top visible row in table is at height "0"
		for (int idx = 0; idx < Math.min(heights.length, items.length); ++idx) {
			heights[idx] -= hOffset;
		}

		// Convert to
		final List<Float> data = new ArrayList<Float>(heights.length);
		for (final int h : heights) {
			data.add((float) h);
		}
		// Take off extra row added earlier
		return data.subList(0, data.size() - 1);
	}

	public Rectangle getCanvasClientArea() {
		// Client area is full table, reduce the size to just the wiring column.
		final Rectangle area = grid.getClientArea();

		int wiringColumnIndexTmp = -1;
		boolean foundColumn = false;
		for (final GridColumn gc : grid.getColumns()) {
			++wiringColumnIndexTmp;
			if (gc == wiringColumn.getColumn()) {
				foundColumn = true;
				break;
			}
		}
		if (!foundColumn) {
			return null;
		}
		final int wiringColumnIndex = wiringColumnIndexTmp;

		int offset = 0;
		if (grid.isRowHeaderVisible()) {
			offset += grid.getRowHeaderWidth();
		}
		// TODO: Get col number
		foundColumn = false;
		final int[] columnOrder = grid.getColumnOrder();
		for (int ii = 0; ii < columnOrder.length; ++ii) {
			final int idx = columnOrder[ii];
			if (idx == wiringColumnIndex) {
				foundColumn = true;
				break;
			}
			if (grid.getColumn(idx).isVisible()) {
				offset += grid.getColumn(idx).getWidth();
			}
		}
		if (!foundColumn) {
			return null;
		}
		// This used to be based in column index, now for some reason it is px based. If the old behaviour re-appears, then this value is the second for loop initialiser value.
		if (grid.getHorizontalBar() != null) {
			offset -= grid.getHorizontalBar().getSelection();
		}
		// TODO: Take into account h scroll final int colWidth = grid.getColumn(wiringColumnIndex).getWidth();
		return new Rectangle(area.x + offset, area.y + grid.getHeaderHeight(), wiringColumn.getColumn().getWidth(), area.height);
	}

	public void setRoot(final OptionAnalysisModel root) {
		this.table = root;
	}

}
