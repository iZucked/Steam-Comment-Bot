/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.breakeven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
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

import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

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
public class MainTableWiringDiagram implements PaintListener, MouseListener, MouseMoveListener, KeyListener {

	// TODO: Use Colour Palette API
	static final Color Green = new Color(Display.getCurrent(), new RGB(0, 180, 50));
	static final Color Light_Green = new Color(Display.getCurrent(), new RGB(100, 255, 100));
	static final Color Light_Blue = new Color(Display.getCurrent(), new RGB(100, 100, 255));
	static final Color White = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	static final Color Light_Grey = new Color(Display.getCurrent(), new RGB(240, 240, 240));
	static final Color Grey = new Color(Display.getCurrent(), new RGB(200, 200, 200));

	static final Color InvalidTerminalColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private final Color InvalidWireColour = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);

	static final Color ValidTerminalColour = Light_Green;
	static final Color MixedTerminalColour = Light_Green;
	private final Color RewirableColour = Grey;// Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private final Color FixedWireColour = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

	private int terminalSize = 11;
	private int pathWidth = 2;
	private final int borderWidth = 1;

	private boolean dragging = false;
	private int draggingFrom = -1;
	private boolean draggingFromLeft = false;
	private int draggingFromColumn = -1;

	private int dragFromX, dragFromY;

	private int dragX, dragY;
	private boolean locked;

	/**
	 * The Grid control we are rendering on to
	 */
	private final Grid grid;
	/**
	 * The {@link GridViewerColumn} column which is the "Wiring" column
	 */
	private final GridViewerColumn wiringColumn;
	/**
	 * The {@link OptionAnalysisModel} data structure containing all the required data
	 */
	private BreakEvenAnalysisModel table;

	/**
	 * Create a new wiring diagram
	 * 
	 * @param parent
	 * @param style
	 */
	public MainTableWiringDiagram(final Grid grid, final GridViewerColumn wiringColumn) {
		this.grid = grid;
		this.wiringColumn = wiringColumn;
		this.grid.addPaintListener(this);
		this.grid.addMouseListener(this);
		this.grid.addMouseMoveListener(this);
		this.grid.addKeyListener(this);
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
		final BreakEvenAnalysisModel root = table;
		// Get a list of terminal positions from subclass
		final Map<BreakEvenAnalysisRow, Integer> rowIndices = new HashMap<>();
		final Map<Integer, BreakEvenAnalysisRow> indexToRow = new HashMap<>();

		final List<Float> terminalPositions = getTerminalPositions(rowIndices, indexToRow);
		final GC graphics = e.gc;

		graphics.setAntialias(SWT.ON);

		final int linewidth = graphics.getLineWidth();
		graphics.setLineWidth(2);

		// Clip to reported client area to avoid overdraw
		// graphics.setClipping(ca);
		int rawI = 0;

		graphics.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		// Fill whole area - not for use in a table
		graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));

		// draw paths
		for (final BreakEvenAnalysisRow row : root.getRows()) {

			BreakEvenAnalysisRow otherRow = null;
			for (final BreakEvenAnalysisRow bcr : root.getRows()) {
				for (BreakEvenAnalysisResultSet resultSet : bcr.getRhsResults()) {
					for (BreakEvenAnalysisResult result : resultSet.getResults()) {
						if (row.getRhsBasedOn() == result) {
							otherRow = bcr;
						}
					}
				}
				for (BreakEvenAnalysisResultSet resultSet : bcr.getLhsResults()) {
					for (BreakEvenAnalysisResult result : resultSet.getResults()) {
						if (row.getLhsBasedOn() == result) {
							otherRow = bcr;
						}
					}
				}
			}
			int offset = 0;
			if (row.getRhsBasedOn() != null) {
				// Client area is full table, reduce the size to just the wiring column.
				final Rectangle area = grid.getClientArea();

				boolean foundColumn = false;
				int wiringColumnIndexTmp = -1;
				for (GridColumn c : grid.getColumns()) {
					++wiringColumnIndexTmp;
					if (c.getData() == row.getRhsBasedOn().getTarget()) {
						foundColumn = true;
						break;
					}
				}

				final int wiringColumnIndex = wiringColumnIndexTmp;

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
				if (foundColumn) {
					// This used to be based in column index, now for some reason it is px based. If the old behaviour re-appears, then this value is the second for loop initialiser value.
					if (grid.getHorizontalBar() != null) {
						offset -= grid.getHorizontalBar().getSelection();
					}
				}

				// PartialCaseRow otherRow = null;
				// if (other != null) {
				// for (final PartialCaseRow pcr : root.getPartialCase().getPartialCase()) {
				// if (pcr.getSellOptions().contains(other.getSellOption())) {
				// otherRow = pcr;
				// break;
				// }
				// }
				// }

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

				// If this is the wire currently being dragged, skip and handle in next code block
				if (dragging) {
					// check
					if (draggingFromLeft && draggingFrom == sortedSource) {
						continue;
					} else if (!draggingFromLeft && sortedDestination == draggingFrom) {
						continue;
					}
				}

				final float startMid = terminalPositions.get(sortedSource);
				final float endMid = terminalPositions.get(sortedDestination);

				// Draw wire - offset by ca.x to as x pos is relative to left hand side
				final Path path = makeConnector(e.display, ca.x + 1.5f * terminalSize, startMid, offset + 1.5f * terminalSize, endMid);

				graphics.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));

				// if (wire.dashed) {
				graphics.setLineDash(new int[] { 2, 3 });
				// } else {
				// graphics.setLineDash(null);
				// }

				graphics.drawPath(path);
				path.dispose();

				graphics.setLineDash(null);
			}
			if (row.getLhsBasedOn() != null) {
				// Client area is full table, reduce the size to just the wiring column.
				final Rectangle area = grid.getClientArea();

				boolean foundColumn = false;
				int wiringColumnIndexTmp = -1;
				for (GridColumn c : grid.getColumns()) {
					++wiringColumnIndexTmp;
					if (c.getData() == row.getLhsBasedOn().getTarget()) {
						foundColumn = true;
						break;
					}
				}

				final int wiringColumnIndex = wiringColumnIndexTmp;

				if (grid.isRowHeaderVisible()) {
					offset += grid.getRowHeaderWidth();
				}
				// TODO: Get col number
				foundColumn = false;
				final int[] columnOrder = grid.getColumnOrder();
				for (int ii = 0; ii < columnOrder.length; ++ii) {
					final int idx = columnOrder[ii];
					if (grid.getColumn(idx).isVisible()) {
						offset += grid.getColumn(idx).getWidth();
					}
					if (idx == wiringColumnIndex) {
						foundColumn = true;
						break;
					}
				}
				if (foundColumn) {
					// This used to be based in column index, now for some reason it is px based. If the old behaviour re-appears, then this value is the second for loop initialiser value.
					if (grid.getHorizontalBar() != null) {
						offset -= grid.getHorizontalBar().getSelection();
					}
				}

				// PartialCaseRow otherRow = null;
				// if (other != null) {
				// for (final PartialCaseRow pcr : root.getPartialCase().getPartialCase()) {
				// if (pcr.getSellOptions().contains(other.getSellOption())) {
				// otherRow = pcr;
				// break;
				// }
				// }
				// }

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

				// If this is the wire currently being dragged, skip and handle in next code block
				if (dragging) {
					// check
					if (draggingFromLeft && draggingFrom == sortedSource) {
						continue;
					} else if (!draggingFromLeft && sortedDestination == draggingFrom) {
						continue;
					}
				}

				final float startMid = terminalPositions.get(sortedSource);
				final float endMid = terminalPositions.get(sortedDestination);

				// Draw wire - offset by ca.x to as x pos is relative to left hand side
				final Path path = makeConnector(e.display, offset - 1.5f * terminalSize, endMid, ca.width + ca.x - 1.5f * terminalSize, startMid);

				graphics.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));

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

		if (dragging) {
			// check
			final Path path;
			if (draggingFromLeft) {
				path = makeConnector(e.display, dragFromX, dragFromY, dragX, dragY);
			} else {
				// move to mouse, path to right point
				path = makeConnector(e.display, dragX, dragY, dragFromX, dragFromY);
			}
			graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED));
			graphics.drawPath(path);
			path.dispose();
		}

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

			final BreakEvenAnalysisRow row = indexToRow.get(i);
			if (row == null) {
				continue;
			}
			// if (!row.isWiringChange()) {
			// continue;
			// }
			// Draw left hand terminal
			if (row.getBuyOption() != null) {

				final BuyOption option = row.getBuyOption();
				final boolean isSpot = option instanceof BuyMarket;

				final boolean isFOB = AnalyticsBuilder.isShipped(option);

				final Color terminalColour = ValidTerminalColour;// : InvalidTerminalColour;
				drawTerminal(true, !isFOB, terminalColour, false, isSpot, ca, graphics, midpoint);

			}

			graphics.setLineWidth(linewidth);
			// Draw right hand terminal
			if (row.getSellOption() != null) {

				final SellOption option = row.getSellOption();
				final boolean isSpot = option instanceof SellMarket;

				final boolean isDES = AnalyticsBuilder.isShipped(option);

				final Color terminalColour = ValidTerminalColour;// : InvalidTerminalColour;
				drawTerminal(false, isDES, terminalColour, false, isSpot, ca, graphics, midpoint);
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

	protected List<Float> getTerminalPositions(final Map<BreakEvenAnalysisRow, Integer> rTI, final Map<Integer, BreakEvenAnalysisRow> iTR) {

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
			for (final GridItem item : items) {
				if (item.isVisible()) {
					heights[idx] = 1 + heights[idx - 1] + item.getHeight();

					final Object data = item.getData();
					if (data instanceof BreakEvenAnalysisRow) {
						final BreakEvenAnalysisRow changeSetRow = (BreakEvenAnalysisRow) data;
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
		final ScrollBar verticalBar = grid.getVerticalBar();
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

	public void setRoot(final BreakEvenAnalysisModel root) {
		this.table = root;
	}

	/*
	 * Drag handling stuff to edit wiring
	 */

	@Override
	public void mouseDoubleClick(final MouseEvent e) {
	}

	@Override
	public void mouseDown(final MouseEvent e) {
		if (locked) {
			return;
		}

		// detect whether we have clicked on a terminal
		final Rectangle ca = getCanvasClientArea();
		if (ca == null) {
			return;
		}

		// determine which row ( if any )

		final Map<BreakEvenAnalysisRow, Integer> rowIndices = new HashMap<>();
		final Map<Integer, BreakEvenAnalysisRow> indexToRow = new HashMap<>();

		final List<Float> positions = getTerminalPositions(rowIndices, indexToRow);
		GridItem itm = null;
		int terminal = 0;
		{
			// Pass one, get heights
			final GridItem[] items = grid.getItems();

			// TODO: Header row need to be included here.
			// int idx = -1;
			{
				int height = grid.getHeaderHeight();
				if (e.y < height) {
					dragging = false;
					return;
				}
				for (final GridItem item : items) {
					if (item.isVisible()) {
						height += 1 + item.getHeight();
						if (e.y < height) {
							itm = item;
							break;
						}
						terminal++;

					}
				}
			}
		}

		// for (final float y : positions) {
		// if (e.y >= (y - terminalSize / 2) && e.y <= (y + terminalSize / 2)) {
		// break;
		// }
		// terminal++;
		// }

		if (terminal >= positions.size()) {
			return;
		}
		onMouseDown();

		SpotMarket targetMarket = null;
		{

			draggingFromColumn = -1;
			int offset = 0;
			if (grid.isRowHeaderVisible()) {
				offset += grid.getRowHeaderWidth();
			}
			// TODO: Get col number
			final int[] columnOrder = grid.getColumnOrder();
			for (int ii = 0; ii < columnOrder.length; ++ii) {
				final int idx = columnOrder[ii];
				if (e.x >= offset && e.x < offset + grid.getColumn(idx).getWidth()) {
					draggingFromColumn = idx;
					if (!(grid.getColumn(idx).getData() instanceof SpotMarket)) {
						dragging = false;
						return;
					}
					targetMarket = (SpotMarket) grid.getColumn(idx).getData();
				}
				if (grid.getColumn(idx).isVisible()) {
					offset += grid.getColumn(idx).getWidth();
				}
			}

		}

		// now find column
		if (draggingFromColumn != -1) {
			// left column
			dragging = true;
			draggingFrom = terminal;

			draggingFromLeft = true;
			Object data = itm.getData();
			if (data instanceof BreakEvenAnalysisRow) {
				BreakEvenAnalysisRow row = (BreakEvenAnalysisRow) data;
				if (row.getBuyOption() != null) {
					draggingFromLeft = false;

					if (!(targetMarket instanceof FOBSalesMarket || targetMarket instanceof DESSalesMarket)) {
						dragging = false;
						return;
					}

				} else if (row.getSellOption() != null) {
					if (!(targetMarket instanceof FOBPurchasesMarket || targetMarket instanceof DESPurchaseMarket)) {
						dragging = false;
						return;
					}
				} else {
					dragging = false;
					return;
				}

			}

		} else if (e.x >= ca.x + ca.width - terminalSize * 2 && e.x <= ca.x + ca.width - terminalSize) {
			// right column
			dragging = true;
			draggingFromLeft = false;
			draggingFrom = terminal;
		}
		if (dragging) {
			dragFromX = e.x;
			dragFromY = e.y;
			dragX = e.x;
			dragY = e.y;
			grid.redraw();
		}
	}

	@Override
	public void mouseMove(final MouseEvent e) {
		if (locked) {
			dragging = false;
			return;
		}

		// if we are dragging, move the wire terminus and refresh
		if (dragging) {

			// checkScroll(e);
			dragX = e.x;
			dragY = e.y;
			grid.redraw();
		}
	}

	@Override
	public void mouseUp(final MouseEvent e) {
		if (locked) {
			dragging = false;
			return;
		}
		onMouseup();

		// if we are dragging, awesome, we have finished.
		if (dragging) {
			dragging = false;

			boolean ctrlPressed = (e.stateMask & SWT.CTRL) != 0;

			final Map<BreakEvenAnalysisRow, Integer> rowIndices = new HashMap<>();
			final Map<Integer, BreakEvenAnalysisRow> indexToRow = new HashMap<>();

			final List<Float> positions = getTerminalPositions(rowIndices, indexToRow);

			int terminal = 0;
			{
				// Pass one, get heights
				final GridItem[] items = grid.getItems();

				// TODO: Header row need to be included here.
				// int idx = -1;
				{
					int height = grid.getHeaderHeight();
					if (e.y < height) {
						dragging = false;
						return;
					}
					for (final GridItem item : items) {
						if (item.isVisible()) {
							height += 1 + item.getHeight();
							if (e.y < height) {
								// itm = item;
								break;
							}
							terminal++;

						}
					}
				}
			}

			// // Map back from the sorted display order to the raw data order
			// if (!(terminal >= positions.size()) && reverseSortedIndices != null) {
			// terminal = reverseSortedIndices[terminal];
			// }
			final BreakEvenAnalysisRow toRowData = terminal >= table.getRows().size() ? null : table.getRows().get(terminal);
			final boolean draggedToNowhere = terminal >= positions.size() || !(draggingFromLeft ? toRowData.getSellOption() != null : toRowData.getBuyOption() != null);

			final Rectangle ca = getCanvasClientArea();

			// final boolean control = (e.stateMask & SWT.CONTROL) != 0;

			// Map back from the sorted display order to the raw data order
			final int sortedIndex = draggingFrom;
			if (terminal == sortedIndex) {
				return;
			}
			// New Load -> Discharge pairing
			// May contain a null key!
			final Map<BreakEvenAnalysisRow, BreakEvenAnalysisRow> newWiring = new HashMap<>();

			final BreakEvenAnalysisRow fromRowData = table.getRows().get(sortedIndex);
			Object data = grid.getColumn(draggingFromColumn).getData();
			BreakEvenAnalysisResult targetResult = null;
			if (draggingFromLeft) {
				for (BreakEvenAnalysisResultSet rs : fromRowData.getLhsResults()) {
					if (rs.getBasedOn() == null) {
						for (BreakEvenAnalysisResult r : rs.getResults()) {
							if (r.getTarget() == data) {
								targetResult = r;
							}
						}
					}
				}
			} else {
				for (BreakEvenAnalysisResultSet rs : fromRowData.getRhsResults()) {
					if (rs.getBasedOn() == null) {
						for (BreakEvenAnalysisResult r : rs.getResults()) {
							if (r.getTarget() == data) {
								targetResult = r;
							}
						}
					}
				}
			}

			{
				// now find column
				if (!draggedToNowhere && !draggingFromLeft && (e.x >= ca.x + terminalSize && e.x <= ca.x + 2 * terminalSize)) {
					// arrived in left column from right
					for (BreakEvenAnalysisRow r : table.getRows()) {
						r.setRhsBasedOn(null);
					}
					toRowData.setRhsBasedOn(targetResult);
				} else if (!draggedToNowhere && draggingFromLeft && (e.x >= ca.x + ca.width - terminalSize * 2 && e.x <= ca.x + ca.width - terminalSize)) {
					// arrived in right column
					newWiring.put(fromRowData, toRowData);
					for (BreakEvenAnalysisRow r : table.getRows()) {
						r.setLhsBasedOn(null);
					}

					toRowData.setLhsBasedOn(targetResult);
				} else {
					// clear wire
					// newWiring.put(fromRowData, null);
					// toRowData.setLhsBasedOn(null);
				}
				// wiringChanged(newWiring, ctrlPressed);
			}
		}

		if (!grid.isDisposed()) {
			grid.redraw();
		}
	}

	/**
	 * Overridable method callback invoked during a mouse down event when a valid terminal has been selected.
	 */
	public void onMouseDown() {

	}

	/**
	 * Overridable method callback notifying that a mouse up event has been received. Note, there may be more called to {@link #onMouseup()} than to {@link #onMouseDown()}
	 */
	public void onMouseup() {

	}

	/**
	 * Sent when a key is pressed on the system keyboard.
	 * 
	 * @param e
	 *            an event containing information about the key press
	 */
	public void keyPressed(final KeyEvent e) {

	}

	/**
	 * Sent when a key is released on the system keyboard.
	 * 
	 * @param e
	 *            an event containing information about the key release
	 */
	public void keyReleased(final KeyEvent e) {
		// Cancel any drag in effect
		if (e.character == SWT.ESC) {
			if (dragging) {
				dragging = false;
				grid.redraw();
			}
		}
	}
}
