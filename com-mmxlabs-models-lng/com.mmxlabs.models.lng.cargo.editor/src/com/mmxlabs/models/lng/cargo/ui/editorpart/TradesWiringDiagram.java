/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.GroupData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RootData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.WireData;

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
 *         FIXME: Fork of {@link WiringDiagram} as which this does work as is, partial re-paints do not work - bg fill is ok, but terminals and lines are only partially drawn if at all. Unknown why
 *         this is the case as it is the same listener code on a canvas, but the objects are separated. Disabling double buffer does not help. Only idea is that some methods are synchronized - it
 *         maybe by splitting the classes code which was previously synchronious can now be executed in parallel.
 * @since 3.0
 */
public abstract class TradesWiringDiagram implements PaintListener, MouseListener, MouseMoveListener {

	/**
	 * The actual wiring permutation; if the ith element is j, left hand terminal i is wired to right hand terminal j. If the ith element is -1, the ith element is not connected to anywhere.
	 */
	private RootData rootData = new RootData();

	private int terminalSize = 9;
	private int pathWidth = 2;
	private int borderWidth = 1;

	private boolean dragging = false;
	private int draggingFrom = -1;
	private boolean draggingFromLeft = false;

	private int dragX, dragY;
	private boolean locked;

	private Canvas canvas;
	private int[] sortedIndices;
	private int[] reverseSortedIndices;

	/**
	 * Create a new wiring diagram
	 * 
	 * @param parent
	 * @param style
	 */
	public TradesWiringDiagram(final Canvas canvas) {
		// public WiringDiagram(final Composite parent, int style) {
		// super(parent, style);
		this.canvas = canvas;
		canvas.addPaintListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMoveListener(this);
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
		final Rectangle ca = getCanvasClientArea();

		// Could be null is column is no longer visible.
		if (ca == null) {
			return;
		}

		// Get a list of terminal positions from subclass
		final List<Float> terminalPositions = getTerminalPositions(rootData);

		// Copy ref in case of concurrent change during paint
		RootData root = rootData;

		final GC graphics = e.gc;

		graphics.setAntialias(SWT.ON);

		// Clip to reported client area to avoid overdraw
		graphics.setClipping(ca);

		graphics.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		// Fill whole area - not for use in a table
		// graphics.fillRectangle(0, 0, ca.width, ca.height);

		graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		graphics.setLineWidth(2);

		// draw paths
		for (GroupData groupData : root.getGroups()) {
			for (WireData wire : groupData.getWires()) {
				int unsortedSource = root.getRows().indexOf(wire.loadRowData);
				int unsortedDestination = root.getRows().indexOf(wire.dischargeRowData);
				if (unsortedSource < 0 || unsortedDestination < 0) {
					// Error?
					continue;
				}
				// Map back between current row (sorted) and data (unsorted)
				int sortedDestination = sortedIndices == null ? unsortedDestination : sortedIndices[unsortedDestination];
				int sortedSource = sortedIndices == null ? unsortedSource : sortedIndices[unsortedSource];

				// Filtering can lead to missing terminals
				if (sortedDestination == -1 || sortedSource == -1) {
					continue;
				}

				// If this is the wire currently being dragged, skip and handle in next code block
				if (dragging) {
					if (draggingFromLeft && draggingFrom == sortedSource) {
						continue;
					} else if (!draggingFromLeft && sortedDestination == draggingFrom) {
						continue;
					}
				}

				final float startMid = terminalPositions.get(sortedSource);
				final float endMid = terminalPositions.get(sortedDestination);

				// Draw wire - offset by ca.x to as x pos is relative to left hand side
				final Path path = makeConnector(e.display, ca.x + 1.5f * terminalSize, startMid, ca.x + ca.width - 1.5f * terminalSize, endMid);

				graphics.setForeground(wire.colour);
				graphics.drawPath(path);
				path.dispose();

			}
		}

		// Clear clip rect for dragged wire
		graphics.setClipping((Rectangle) null);

		if (dragging) {
			// draw dragging path
			final float mid = terminalPositions.get(draggingFrom);

			final Path path;
			if (draggingFromLeft) {
				path = makeConnector(e.display, ca.x + 1.5f * terminalSize, mid, dragX, dragY);
			} else {
				// move to mouse, path to right point
				path = makeConnector(e.display, dragX, dragY, ca.x + ca.width - 1.5f * terminalSize, mid);
			}
			graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED));
			graphics.drawPath(path);
			path.dispose();
		}
		// Re-apply clip rect
		graphics.setClipping(ca);

		// draw terminal blobs
		graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		graphics.setLineWidth(borderWidth);
		int rawI = 0;
		for (final float midpoint : terminalPositions) {
			// Map back between current row (sorted) and data (unsorted)
			int i = (reverseSortedIndices == null || rawI < reverseSortedIndices.length) ? reverseSortedIndices[rawI] : rawI;
			// -1 indicates filtered row
			if (i == -1) {
				continue;
			}

			// Draw left hand terminal
			graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			RowData row = root.getRows().get(i);
			if (row.loadSlot != null) {
				graphics.setBackground(row.loadTerminalColour);
				graphics.fillOval(ca.x + terminalSize, (int) (midpoint - (terminalSize / 2)), terminalSize, terminalSize);
				graphics.drawOval(ca.x + terminalSize, (int) (midpoint - (terminalSize / 2)), terminalSize, terminalSize);
				if (row.loadSlot.isOptional()) {
					graphics.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					graphics.fillOval(ca.x + terminalSize + 3, (int) midpoint - (terminalSize / 2) + 3, 3, 3);
				}
			}

			// Draw right hand terminal
			graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			if (row.dischargeSlot != null) {
				graphics.setBackground(row.dischargeTerminalColour);
				graphics.fillOval(ca.x + ca.width - 2 * terminalSize, (int) (midpoint - terminalSize / 2), terminalSize, terminalSize);
				graphics.drawOval(ca.x + ca.width - 2 * terminalSize, (int) (midpoint - terminalSize / 2), terminalSize, terminalSize);
				if (row.dischargeSlot.isOptional()) {
					graphics.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					graphics.fillOval(ca.x + ca.width - 2 * terminalSize + 3, (int) (midpoint - terminalSize / 2) + 3, 4, 4);
				}
			}
			rawI++;
		}
	}

	public Rectangle getCanvasClientArea() {
		return canvas.getClientArea();
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

		// determine which row ( if any )
		int terminal = 0;
		final List<Float> positions = getTerminalPositions(rootData);
		for (final float y : positions) {
			if (e.y >= (y - terminalSize / 2) && e.y <= (y + terminalSize / 2)) {
				break;
			}
			terminal++;
		}

		if (terminal >= positions.size()) {
			return;
		}
		onMouseDown();

		// now find column
		if ((e.x >= ca.x + terminalSize && e.x <= ca.x + 2 * terminalSize)) {
			// left column
			dragging = true;
			draggingFrom = terminal;
			draggingFromLeft = true;
		} else if (e.x >= ca.x + ca.width - terminalSize * 2 && e.x <= ca.x + ca.width - terminalSize) {
			// right column
			dragging = true;
			draggingFromLeft = false;
			draggingFrom = terminal;
		}
		if (dragging) {
			dragX = e.x;
			dragY = e.y;
			canvas.redraw();
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
			canvas.redraw();
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

			final List<Float> positions = getTerminalPositions(rootData);

			int terminal = 0;

			for (final float y : positions) {
				if (e.y >= (y - terminalSize / 2) && e.y <= (y + terminalSize / 2)) {
					break;
				}
				terminal++;
			}

			// Map back from the sorted display order to the raw data order
			if (!(terminal >= positions.size()) && reverseSortedIndices != null) {
				terminal = reverseSortedIndices[terminal];
			}
			RowData toRowData = terminal >= rootData.getRows().size() ? null : rootData.getRows().get(terminal);
			final boolean draggedToNowhere = terminal >= positions.size() || !(draggingFromLeft ? toRowData.dischargeSlot != null : toRowData.loadSlot != null);

			final Rectangle ca = getCanvasClientArea();

			// final boolean control = (e.stateMask & SWT.CONTROL) != 0;

			// Map back from the sorted display order to the raw data order
			int sortedIndex = reverseSortedIndices == null ? draggingFrom : reverseSortedIndices[draggingFrom];

			// New Load -> Discharge pairing
			// May contain a null key!
			Map<RowData, RowData> newWiring = new HashMap<RowData, RowData>();

			RowData fromRowData = rootData.getRows().get(sortedIndex);

			// now find column
			if (!draggedToNowhere && !draggingFromLeft && (e.x >= ca.x + terminalSize && e.x <= ca.x + 2 * terminalSize)) {
				// arrived in left column from right
				newWiring.put(fromRowData, null);
				newWiring.put(toRowData, fromRowData);
				newWiring.put(null, toRowData);
			} else if (!draggedToNowhere && draggingFromLeft && (e.x >= ca.x + ca.width - terminalSize * 2 && e.x <= ca.x + ca.width - terminalSize)) {
				// arrived in right column
				newWiring.put(fromRowData, toRowData);
				newWiring.put(toRowData, null);
				newWiring.put(null, fromRowData);
			} else {
				// clear wire
				newWiring.put(fromRowData, null);
				newWiring.put(null, fromRowData);
			}
			wiringChanged(newWiring);
		}

		if (!canvas.isDisposed()) {
			canvas.redraw();
		}
	}

	/**
	 * subclassses should use this to provide the vertical positions of the terminals given the input {@link RootData} object.
	 * 
	 * @return
	 */
	protected abstract List<Float> getTerminalPositions(RootData rootData);

	/**
	 * called to tell subclasses wiring has changed. Probably should use listener pattern really. The format of the map is the new mapping between left and right side based on the {@link RowData}
	 * instances. For example, given two rows, A and B - rewiring Load A to discharge B will give a map with a <key,value> pair of <A,B> (the new link) and two additional <key,value> pairs of <null,
	 * A> (representing the disconnected discharge on A) and <B, null> (representing the disconnected load on B).
	 */
	protected abstract void wiringChanged(final Map<RowData, RowData> newWiring);

	public void setLocked(final boolean locked) {
		this.locked = locked;
	}

	public synchronized void redraw() {
		canvas.redraw();
	}

	public synchronized void setSortOrder(RootData rootData, int[] sortedIndices, int[] reverseSortedIndices) {
		this.rootData = rootData;
		this.sortedIndices = sortedIndices;
		this.reverseSortedIndices = reverseSortedIndices;
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
}
