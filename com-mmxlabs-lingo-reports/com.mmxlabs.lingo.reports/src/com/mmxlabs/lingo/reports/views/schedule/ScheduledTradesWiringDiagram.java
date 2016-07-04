/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

//import com.mmxlabs.lingo.reports.views.AbstractConfigurableGridReportView.SortData;
import com.mmxlabs.lingo.reports.views.schedule.model.ChangeType;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;

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
public class ScheduledTradesWiringDiagram implements PaintListener {

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
	 * The {@link Table} data structure containing all the required data
	 */
	private Table table;
//	/**
//	 * Class representing the current sort order of the table contents.
//	 */
//	private SortData sortData;

	/**
	 * Create a new wiring diagram
	 * 
	 * @param parent
	 * @param style
	 */
	public ScheduledTradesWiringDiagram(final Grid grid, final GridViewerColumn wiringColumn) {
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

		int[] sortedIndices = null;
		int[] reverseSortedIndices = null;
//		if (sortData != null) {
//			sortedIndices = sortData.sortedIndices;
//			reverseSortedIndices = sortData.reverseSortedIndices;
//		}

		// // Copy ref in case of concurrent change during paint
		final Table root = table;
		// Get a list of terminal positions from subclass
		final List<Float> terminalPositions = getTerminalPositions(root);

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

		// Collect cycle groups
		final List<CycleGroup> cycleGroups = new LinkedList<>();
		for (final UserGroup g1 : root.getUserGroups()) {
			cycleGroups.addAll(g1.getGroups());
		}
		cycleGroups.addAll(root.getCycleGroups());

		// draw paths
		for (final CycleGroup g : cycleGroups) {
			for (final Row row : g.getRows()) {
				if (row.getLoadAllocation() == null && row.getDischargeAllocation() == null) {
					continue;
				}

				Row a = null;
				Row b = null;
				if (g.getChangeType() == ChangeType.WIRING) {

					if (row.isReference()) {
						a = row;
						if (!row.getReferringRows().isEmpty()) {
//							b = row.getReferringRows().get(0);
							 Row z = row.getReferringRows().get(0);
							 for (Row x : g.getRows()) {
							 if (x != row && x.isReference() && x.getDischargeAllocation().getName() .equals( z.getDischargeAllocation().getName())) {
							
							 b = x;
							 break;
							 }
							 }
						}

					} else {
						if (row.getLoadAllocation() != null && row.getLoadAllocation().getSlot() instanceof SpotSlot) {
							a = row;
							b = row;
							// b = row.getReferenceRow();
						}
						// a = row;
						// b = row.getReferenceRow();
					}
				} else {
					a = row;
					b = row;
				}

				if (a == null || b == null) {
					continue;
				}

				final int unsortedSource = root.getRows().indexOf(a);
				final int unsortedDestination = root.getRows().indexOf(b);
				if (unsortedSource < 0 || unsortedDestination < 0) {
					// Error?
					continue;
				}
				// Map back between current row (sorted) and data (unsorted)
				final int sortedDestination = sortedIndices == null ? unsortedDestination : sortedIndices[unsortedDestination];
				final int sortedSource = sortedIndices == null ? unsortedSource : sortedIndices[unsortedSource];

				// Filtering can lead to missing terminals
				if (sortedDestination == -1 || sortedSource == -1) {
					continue;
				}

				final float startMid = terminalPositions.get(sortedSource);
				final float endMid = terminalPositions.get(sortedDestination);

				// Draw wire - offset by ca.x to as x pos is relative to left hand side
				final Path path = makeConnector(e.display, ca.x + 1.5f * terminalSize, startMid, ca.x + ca.width - 1.5f * terminalSize, endMid);

				graphics.setForeground(RewirableColour);

				// if (wire.dashed) {
				// graphics.setLineDash(new int[] { 2, 3 });
				// } else {
				graphics.setLineDash(null);
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
			final int i = (reverseSortedIndices != null && rawI < reverseSortedIndices.length) ? reverseSortedIndices[rawI] : rawI;
			// -1 indicates filtered row
			if (i == -1) {
				continue;
			}

			final Row row = root.getRows().get(i);
			final CycleGroup cycleGroup = row.getCycleGroup();
			final boolean drawLoad = cycleGroup == null ? true : ((cycleGroup.getChangeType() == ChangeType.WIRING) ? row.isReference() : true);
			final boolean drawDischarge = cycleGroup == null ? true : ((cycleGroup.getChangeType() == ChangeType.WIRING) ? row.isReference() : true);

			// Draw left hand terminal
			// if (drawLoad && row.getLoadAllocation() != null) {
			if (row.getLoadAllocation() != null && (drawLoad || row.getLoadAllocation().getSlot() instanceof SpotSlot)) {
				final LoadSlot loadSlot = (LoadSlot) row.getLoadAllocation().getSlot();
				final Color terminalColour = (loadSlot.getCargo() != null || loadSlot.isOptional()) ? ValidTerminalColour : InvalidTerminalColour;
				drawTerminal(true, loadSlot.isDESPurchase(), terminalColour, loadSlot.isOptional(), loadSlot instanceof SpotSlot, ca, graphics, midpoint);
			}

			graphics.setLineWidth(linewidth);
			// Draw right hand terminal
			if (row.getDischargeAllocation() != null && (drawDischarge || row.getDischargeAllocation().getSlot() instanceof SpotSlot)) {
				final DischargeSlot dischargeSlot = (DischargeSlot) row.getDischargeAllocation().getSlot();
				final Color terminalColour = (dischargeSlot.getCargo() != null || dischargeSlot.isOptional()) ? ValidTerminalColour : InvalidTerminalColour;
				drawTerminal(false, !dischargeSlot.isFOBSale(), terminalColour, dischargeSlot.isOptional(), dischargeSlot instanceof SpotSlot, ca, graphics, midpoint);
			}
			rawI++;
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

	protected List<Float> getTerminalPositions(final Table rootData) {

		// Determine the mid-point in each row and generate an ordered list of heights.

		// +1 to to make loop simpler
		final int[] heights = new int[rootData.getRows().size() + 1];
		heights[0] = grid.getHeaderHeight();

		// Pass one, get heights
		final GridItem[] items = grid.getItems();
		for (int idx = 1; idx < Math.min(heights.length, 1 + items.length); ++idx) {
			final GridItem item = items[idx - 1];
			heights[idx] = 1 + heights[idx - 1] + item.getHeight();
		}
		// Find the row at the top of the table and get it's "height" so we can adjust it later
		final int vPod = grid.getVerticalBar().getSelection();
		final int hOffset = (heights[vPod]) - grid.getHeaderHeight();
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
		offset += grid.getRowHeaderWidth();
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
		offset -= grid.getHorizontalBar().getSelection();
		// TODO: Take into account h scroll final int colWidth = grid.getColumn(wiringColumnIndex).getWidth();
		return new Rectangle(area.x + offset, area.y + grid.getHeaderHeight(), wiringColumn.getColumn().getWidth(), area.height);
	}

	public Table getTable() {
		return table;
	}

	public void setTable(final Table table) {
		this.table = table;
	}
//
//	public SortData getSortData() {
//		return sortData;
//	}
//
//	public void setSortData(final SortData sortData) {
//		this.sortData = sortData;
//	}

}
