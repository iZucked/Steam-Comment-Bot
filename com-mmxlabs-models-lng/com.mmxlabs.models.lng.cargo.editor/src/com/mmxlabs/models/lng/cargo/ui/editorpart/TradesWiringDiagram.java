/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Canvas;

import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RootData;
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

	private TradesWiringColourScheme colourScheme;


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
	public TradesWiringDiagram(final Canvas canvas, TradesWiringColourScheme colourScheme) {
		// public WiringDiagram(final Composite parent, int style) {
		// super(parent, style);
		this.canvas = canvas;
		canvas.addPaintListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMoveListener(this);
		canvas.addKeyListener(this);
		this.colourScheme = colourScheme;
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

		final int linewidth = graphics.getLineWidth();
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
			final TradesRow row = root.getRows().get(i);
			graphics.setBackground(colourScheme.getWhite());
			// Left
			if (row.getLoadSlot() != null) {
				final int extraRadius = 1;
				final int x = ca.x + terminalSize - extraRadius / 2;
				final int y = (int) (midpoint - (terminalSize) / 2 - 1 - extraRadius / 2);
				graphics.fillOval(x - 2, y - 2, terminalSize + extraRadius + 4, terminalSize + extraRadius + 4);
			}
			// Right
			if (row.getDischargeSlot() != null) {
				int extraRadius = 0;
				extraRadius = 1;
				final int x = ca.x + ca.width - 2 * terminalSize - extraRadius / 2;
				final int y = (int) (midpoint - (terminalSize) / 2 - 1 - extraRadius / 2);
				graphics.setBackground(colourScheme.getWhite());
				graphics.fillOval(x - 2, y - 2, terminalSize + extraRadius + 4, terminalSize + extraRadius + 4);
			}
			rawI++;
		}

		graphics.setBackground(colourScheme.getSystemWhite());
		// Fill whole area - not for use in a table
		// graphics.fillRectangle(0, 0, ca.width, ca.height);
		graphics.setForeground(colourScheme.getSystemBlack());
		// graphics.setLineWidth(2);

		// draw paths
		for (final TradesRowGroup groupData : root.getGroups()) {
			for (final WireData wire : groupData.getWires()) {
				final int unsortedSource;
				if (wire.isBracket) {
					unsortedSource = root.getRows().indexOf(wire.sourceDischargeTradesRow);
				} else {
					unsortedSource = root.getRows().indexOf(wire.loadTradesRow);
				}

				final int unsortedDestination = root.getRows().indexOf(wire.dischargeTradesRow);
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
				if (wire.isBracket) {
					// draw LDD bracket

					// graphics.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));

					int x1 = Math.round(ca.x + 1.5f * terminalSize);
					int x2 = Math.round(ca.x + ca.width - 2 * terminalSize);
					int x1a = x1 + ((x2 - x1) * 3 / 4);
					if (startMid != endMid) {
						graphics.drawLine(x1a, Math.round(startMid), x2, Math.round(startMid));
						graphics.drawLine(x1a, Math.round(startMid), x1a, Math.round(endMid));
					}
					graphics.drawLine(x1a, Math.round(endMid), x2, Math.round(endMid));
				} else {
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
		}
		// Draw lines for keep-open slots
		for (final TradesRow row : root.getRows()) {
			final int unsortedSource = root.getRows().indexOf(row);
			if (unsortedSource < 0) {
				// Error?
				continue;
			}
			final int sortedSource = sortedIndices == null ? unsortedSource : sortedIndices[unsortedSource];

			// Filtering can lead to missing terminals
			if (sortedSource == -1) {
				continue;
			}

			// If this is the wire currently being dragged, skip and handle in next code block
			if (dragging) {
				if (draggingFromLeft && draggingFrom == sortedSource) {
					continue;
				}
			}

			final float startMid = terminalPositions.get(sortedSource);
			graphics.setLineDash(null);
			graphics.setForeground(colourScheme.getBlack());
			if (row.getLoadSlot() != null && row.getLoadSlot().getCargo() == null && row.getLoadSlot().isLocked()) {// && row.loadSlot.isCancelled()) {
				// Draw wire - offset by ca.x to as x pos is relative to left hand side
				final Path path = makeConnector(e.display, ca.x + 1.5f * terminalSize, startMid, ca.x + ca.width / 2 - 4, startMid);
				graphics.drawPath(path);
				path.dispose();
				graphics.setLineDash(null);
			}
			if (row.getDischargeSlot() != null && row.getDischargeSlot().getCargo() == null && row.getDischargeSlot().isLocked()) {// && row.dischargeSlot.isCancelled()) {
				// Draw wire - offset by ca.x to as x pos is relative to left hand side
				final Path path = makeConnector(e.display, ca.x + ca.width / 2 + 4, startMid, ca.x + ca.width - 1.5f * terminalSize, startMid);
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
			graphics.setForeground(colourScheme.getSystemDarkRed());
			graphics.drawPath(path);
			path.dispose();
		}
		// Re-apply clip rect
		graphics.setClipping(ca);

		// draw terminal blobs
		graphics.setForeground(colourScheme.getSystemBlack());
		graphics.setLineWidth(borderWidth);
		rawI = 0;
		for (final float midpoint : terminalPositions) {
			// Map back between current row (sorted) and data (unsorted)
			final int i = (reverseSortedIndices != null && rawI < reverseSortedIndices.length) ? reverseSortedIndices[rawI] : rawI;
			// -1 indicates filtered row
			if (i == -1) {
				continue;
			}

			final TradesRow row = root.getRows().get(i);
			// Draw line for keep-open positions
			if (row.getLoadSlot() != null && row.getLoadSlot().getCargo() == null && row.getLoadSlot().isLocked()) {// && row.loadSlot.isCancelled()) {
				drawCross(true, colourScheme.getBlack(), ca, graphics, midpoint);
			}
			// Draw left hand terminal
			if (row.getLoadSlot() != null) {
				drawTerminal(true, row.getLoadSlot().isDESPurchase(), colourScheme.getLoadTerminalColour(row), row.getLoadSlot().isOptional(), row.getLoadSlot() instanceof SpotSlot, ca, graphics, midpoint);
			}
			// Draw line for cancelled positions
			if (row.getLoadSlot() != null && row.getLoadSlot().isCancelled()) {
				drawDiagonalCross(true, colourScheme.getBlack(), ca, graphics, midpoint);
			}

			graphics.setLineWidth(linewidth);
			// Draw right hand terminal
			if (row.getDischargeSlot() != null && row.getDischargeSlot().getCargo() == null && row.getDischargeSlot().isLocked()) {// && row.dischargeSlot.isCancelled()) {
				drawCross(false, colourScheme.getBlack(), ca, graphics, midpoint);
			}
			// Draw line for keep-open positions
			if (row.getDischargeSlot() != null) {
				drawTerminal(false, !row.getDischargeSlot().isFOBSale(), colourScheme.getDischargeTerminalColour(row), row.getDischargeSlot().isOptional(), row.getDischargeSlot() instanceof SpotSlot, ca, graphics, midpoint);
			}
			// Draw right hand terminal
			if (row.getDischargeSlot() != null && row.getDischargeSlot().isCancelled()) {
				drawDiagonalCross(false, colourScheme.getBlack(), ca, graphics, midpoint);
			}
			rawI++;
		}
	}

	private void drawTerminal(final boolean isLeft, final boolean hollow, Color terminalColour, final boolean isOptional, final boolean isSpot, final Rectangle ca, final GC graphics,
			final float midpoint) {
		Color outlineColour = colourScheme.getGreen();
		Color fillColour = terminalColour;

		// make non-shipped a bit bigger, but hollow
		final int extraRadius = 1;
		if (isSpot) {
			terminalColour = colourScheme.getGrey();
			outlineColour = colourScheme.getGrey();
			fillColour = colourScheme.getLightGrey();
		}
		if (hollow) {
			outlineColour = terminalColour;
			fillColour = colourScheme.getSystemWhite();
		}

		graphics.setLineWidth(2);
		int x = 0;
		if (isLeft) {
			x = ca.x + terminalSize - extraRadius / 2;
		} else {
			x = ca.x + ca.width - 2 * terminalSize - extraRadius / 2;
		}
		int y = (int) (midpoint - (terminalSize) / 2 - 1 - extraRadius / 2);
		graphics.setForeground(terminalColour == colourScheme.getInvalidTerminalColour() ? colourScheme.getInvalidTerminalColour() : outlineColour);
		graphics.setBackground(fillColour);
		graphics.fillOval(x, y, terminalSize + extraRadius, terminalSize + extraRadius);
		graphics.drawOval(x, y, terminalSize + extraRadius, terminalSize + extraRadius);
		// draw internal dot for optional slots
		if (isOptional && !isSpot) {
			graphics.setBackground(colourScheme.getGreen());
			y = (int) midpoint - (terminalSize / 2) + 3;
			if (isLeft) {
				graphics.fillOval(ca.x + terminalSize + 4, y, 4, 4);
			} else {
				graphics.fillOval(x + 4 + extraRadius / 2, y, 4, 4);
			}
		}
	}

	private void drawCross(final boolean isLeft, final Color terminalColour, final Rectangle ca, final GC graphics, final float midpoint) {

		graphics.setLineWidth(3);
		int x = 0;
		final int terminalSize2 = terminalSize - 2;
		if (isLeft) {
			x = ca.x + ca.width / 2 - 4;
		} else {
			x = ca.x + ca.width / 2 + 4;
		}
		final int y = (int) (midpoint - terminalSize2 / 2 - 1);

		graphics.setForeground(terminalColour);
		graphics.setBackground(terminalColour);
		graphics.drawLine(x, y, x, y + terminalSize2);
	}

	// X - cross
	private void drawDiagonalCross(final boolean isLeft, final Color terminalColour, final Rectangle ca, final GC graphics, final float midpoint) {

		graphics.setLineWidth(2);
		final int extraRadius = 1;
		final int terminalSize2 = terminalSize - 2;
		int x = 0;
		if (isLeft) {
			x = ca.x + terminalSize + extraRadius;
		} else {
			x = ca.x + ca.width - 2 * terminalSize + extraRadius;
		}
		final int y = (int) (midpoint - (terminalSize) / 2 - extraRadius / 2);

		graphics.setForeground(terminalColour);
		graphics.setBackground(terminalColour);
		graphics.drawLine(x, y, x + terminalSize2 + extraRadius, y + terminalSize2 + extraRadius);
		graphics.drawLine(x, y + terminalSize2 + extraRadius, x + terminalSize2 + extraRadius, y);
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
		if (e.button != 1) {
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

			final boolean ctrlPressed = (e.stateMask & SWT.CTRL) != 0;
			final boolean shiftPressed = (e.stateMask & SWT.SHIFT) != 0;
			final boolean altPressed = (e.stateMask & SWT.ALT) != 0;

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
			final TradesRow toTradesRow = terminal >= rootData.getRows().size() ? null : rootData.getRows().get(terminal);
			final boolean draggedToNowhere = terminal >= positions.size() || !(draggingFromLeft ? toTradesRow.getDischargeSlot() != null : toTradesRow.getLoadSlot() != null);

			final Rectangle ca = getCanvasClientArea();

			// final boolean control = (e.stateMask & SWT.CONTROL) != 0;

			// Map back from the sorted display order to the raw data order
			final int sortedIndex = reverseSortedIndices == null ? draggingFrom : reverseSortedIndices[draggingFrom];

			// New Load -> Discharge pairing
			// May contain a null key!
			final Map<TradesRow, TradesRow> newWiring = new HashMap<>();

			final TradesRow fromTradesRow = rootData.getRows().get(sortedIndex);

			// check if the user is trying to pair two slots which are a ship-to-ship transfer
			// (i.e. they are effectively the same slot!)
			final boolean shipToShipLink = toTradesRow != null && fromTradesRow != null
					&& ((toTradesRow.getLoadSlot() != null && fromTradesRow.getDischargeSlot() != null && toTradesRow.getLoadSlot().getTransferFrom() == fromTradesRow.getDischargeSlot())
							|| (toTradesRow.getDischargeSlot() != null && fromTradesRow.getLoadSlot() != null && toTradesRow.getDischargeSlot().getTransferTo() == fromTradesRow.getLoadSlot()));

			if (!shipToShipLink) {
				// now find column
				if (!draggedToNowhere && !draggingFromLeft && (e.x >= ca.x + terminalSize && e.x <= ca.x + 2 * terminalSize)) {
					// arrived in left column from right
					newWiring.put(toTradesRow, fromTradesRow);
				} else if (!draggedToNowhere && draggingFromLeft && (e.x >= ca.x + ca.width - terminalSize * 2 && e.x <= ca.x + ca.width - terminalSize)) {
					// arrived in right column
					newWiring.put(fromTradesRow, toTradesRow);
				} else {
					// clear wire
					newWiring.put(fromTradesRow, null);
				}

				var itr = newWiring.entrySet().iterator();
				while (itr.hasNext()) {
					var entry = itr.next();
					if (entry.getKey() != null && entry.getValue() != null) {
						if (entry.getKey() == entry.getValue() && entry.getKey().getCargo() != null) {
							if (entry.getKey().getCargo().getSlots().size() <= 2) {
								itr.remove();
							}
						}
					}

				}
				if (!newWiring.isEmpty()) {
					wiringChanged(newWiring, ctrlPressed, shiftPressed, altPressed);
				}
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
	protected abstract void wiringChanged(final Map<TradesRow, TradesRow> newWiring, boolean ctrlPressed, final boolean shiftPressed, final boolean altPressed);

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
		colourScheme = null;
	}
}
