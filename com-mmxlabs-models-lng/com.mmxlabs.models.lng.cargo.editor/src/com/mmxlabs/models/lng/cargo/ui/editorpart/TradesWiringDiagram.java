/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.cargo.SpotSlot;
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
 */
public abstract class TradesWiringDiagram implements PaintListener, MouseListener, MouseMoveListener, KeyListener {

	static final Color Green = new Color(Display.getCurrent(), new RGB(0, 180, 50));
	static final Color Light_Green = new Color(Display.getCurrent(), new RGB(100, 255, 100));
	static final Color White = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	static final Color Light_Grey = new Color(Display.getCurrent(), new RGB(240, 240, 240));
	static final Color Grey = new Color(Display.getCurrent(), new RGB(200, 200, 200));

	/**
	 * The actual wiring permutation; if the ith element is j, left hand terminal i is wired to right hand terminal j. If the ith element is -1, the ith element is not connected to anywhere.
	 */
	private RootData rootData = new RootData();

	private int terminalSize = 11;
	private int pathWidth = 2;
	private final int borderWidth = 1;

	private boolean dragging = false;
	private int draggingFrom = -1;
	private boolean draggingFromLeft = false;

	private int dragX, dragY;
	private boolean locked;

	private final Canvas canvas;
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
		canvas.addKeyListener(this);
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
		final RootData root = rootData;

		final GC graphics = e.gc;

		graphics.setAntialias(SWT.ON);

		int linewidth = graphics.getLineWidth();
		graphics.setLineWidth(2);

		// Clip to reported client area to avoid overdraw
		graphics.setClipping(ca);
		int rawI = 0;
		for (final float midpoint : terminalPositions) {
			// Map back between current row (sorted) and data (unsorted)
			final int i = (reverseSortedIndices != null && rawI < reverseSortedIndices.length) ? reverseSortedIndices[rawI] : rawI;
			// -1 indicates filtered row
			if (i == -1) {
				continue;
			}

			// Draw terminal background discs
			final RowData row = root.getRows().get(i);
			graphics.setBackground(White);
			// Left
			if (row.loadSlot != null) {
				int extraRadius = 1;
				int x = ca.x + terminalSize - extraRadius / 2;
				int y = (int) (midpoint - (terminalSize) / 2 - 1 - extraRadius / 2);
				graphics.fillOval(x - 2, y - 2, terminalSize + extraRadius + 4, terminalSize + extraRadius + 4);
			}
			// Right
			if (row.dischargeSlot != null) {
				int extraRadius = 0;
				extraRadius = 1;
				int x = ca.x + ca.width - 2 * terminalSize - extraRadius / 2;
				int y = (int) (midpoint - (terminalSize) / 2 - 1 - extraRadius / 2);
				graphics.setBackground(White);
				graphics.fillOval(x - 2, y - 2, terminalSize + extraRadius + 4, terminalSize + extraRadius + 4);
			}
			rawI++;
		}

		graphics.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		// Fill whole area - not for use in a table
		// graphics.fillRectangle(0, 0, ca.width, ca.height);
		graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		// graphics.setLineWidth(2);

		// draw paths
		for (final GroupData groupData : root.getGroups()) {
			for (final WireData wire : groupData.getWires()) {
				final int unsortedSource = root.getRows().indexOf(wire.loadRowData);
				final int unsortedDestination = root.getRows().indexOf(wire.dischargeRowData);
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

				if (wire.dashed) {
					graphics.setLineDash(new int[] { 2, 3 });
				} else {
					graphics.setLineDash(null);
				}

				graphics.drawPath(path);
				path.dispose();

				graphics.setLineDash(null);
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
		rawI = 0;
		for (final float midpoint : terminalPositions) {
			// Map back between current row (sorted) and data (unsorted)
			final int i = (reverseSortedIndices != null && rawI < reverseSortedIndices.length) ? reverseSortedIndices[rawI] : rawI;
			// -1 indicates filtered row
			if (i == -1) {
				continue;
			}

			final RowData row = root.getRows().get(i);
			// Draw left hand terminal
			if (row.loadSlot != null) {
				drawTerminal(true, row.loadSlot.isDESPurchase(), row.loadTerminalColour, row.loadSlot.isOptional(), row.loadSlot instanceof SpotSlot, ca, graphics, midpoint);
			}
			graphics.setLineWidth(linewidth);
			// Draw right hand terminal
			if (row.dischargeSlot != null) {
				drawTerminal(false, !row.dischargeSlot.isFOBSale(), row.dischargeTerminalColour, row.dischargeSlot.isOptional(), row.dischargeSlot instanceof SpotSlot, ca, graphics, midpoint);
			}
			rawI++;
		}
	}

	private void drawTerminal(boolean isLeft, boolean hollow, Color terminalColour, boolean isOptional, boolean isSpot, final Rectangle ca, final GC graphics, final float midpoint) {
		Color outlineColour = Green;
		Color fillColour = terminalColour;
		// make non-shipped a bit bigger, but hollow
		int extraRadius = 1;
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
		graphics.setForeground(terminalColour == CargoModelRowTransformer.InvalidTerminalColour ? CargoModelRowTransformer.InvalidTerminalColour : outlineColour);
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
		if (ca == null) {
			return;
		}

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

			boolean ctrlPressed = (e.stateMask & SWT.CTRL) != 0;

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
			final RowData toRowData = terminal >= rootData.getRows().size() ? null : rootData.getRows().get(terminal);
			final boolean draggedToNowhere = terminal >= positions.size() || !(draggingFromLeft ? toRowData.dischargeSlot != null : toRowData.loadSlot != null);

			final Rectangle ca = getCanvasClientArea();

			// final boolean control = (e.stateMask & SWT.CONTROL) != 0;

			// Map back from the sorted display order to the raw data order
			final int sortedIndex = reverseSortedIndices == null ? draggingFrom : reverseSortedIndices[draggingFrom];

			// New Load -> Discharge pairing
			// May contain a null key!
			final Map<RowData, RowData> newWiring = new HashMap<RowData, RowData>();

			final RowData fromRowData = rootData.getRows().get(sortedIndex);

			// check if the user is trying to pair two slots which are a ship-to-ship transfer
			// (i.e. they are effectively the same slot!)
			final boolean shipToShipLink = toRowData != null
					&& fromRowData != null
					&& ((toRowData.loadSlot != null && fromRowData.dischargeSlot != null && toRowData.loadSlot.getTransferFrom() == fromRowData.dischargeSlot) || (toRowData.dischargeSlot != null
							&& fromRowData.loadSlot != null && toRowData.dischargeSlot.getTransferTo() == fromRowData.loadSlot));

			if (!shipToShipLink) {
				// now find column
				if (!draggedToNowhere && !draggingFromLeft && (e.x >= ca.x + terminalSize && e.x <= ca.x + 2 * terminalSize)) {
					// arrived in left column from right
					newWiring.put(toRowData, fromRowData);
				} else if (!draggedToNowhere && draggingFromLeft && (e.x >= ca.x + ca.width - terminalSize * 2 && e.x <= ca.x + ca.width - terminalSize)) {
					// arrived in right column
					newWiring.put(fromRowData, toRowData);
				} else {
					// clear wire
					newWiring.put(fromRowData, null);
				}
				wiringChanged(newWiring, ctrlPressed);
			}
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
	 * 
	 * @param ctrlPressed
	 */
	protected abstract void wiringChanged(final Map<RowData, RowData> newWiring, boolean ctrlPressed);

	public void setLocked(final boolean locked) {
		this.locked = locked;
	}

	public synchronized void redraw() {
		canvas.redraw();
	}

	public synchronized void setSortOrder(final RootData rootData, final int[] sortedIndices, final int[] reverseSortedIndices) {
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
				canvas.redraw();
			}
		}
	}

	public void dispose() {
		rootData = null;
	}
}
