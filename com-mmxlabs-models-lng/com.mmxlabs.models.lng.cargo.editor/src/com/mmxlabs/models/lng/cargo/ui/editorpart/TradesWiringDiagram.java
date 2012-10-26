/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.ui.dialogs.WiringDiagram;

/**
 * s A control for drawing a wiring diagram. This just displays an arbitrary bipartite graph / matching.
 * 
 * The graph must have equal numbers of vertices on each side, which are identified by the integers from 0 to n-1. The wiring itself is described in the {@link #wiring} list. The ith element of the
 * list contains the index of the right-hand element to which left hand element i is connected, or -1 if there is no connection.
 * 
 * Thus wiring.get(1) gives the right-hand node connected to left-hand node 1, and wiring.indexOf(1) gives the left-hand node connected to right-hand node 1.
 * 
 * TODO allow mismatched states, where there may be differing numbers of left-hand and right-hand terminals.
 * 
 * @author Tom Hinton
 * 
 *         FIXME: Fork of {@link WiringDiagram} as which this does work as is, partial re-paints do not work - bg fill is ok, but terminals and lines are only partially drawn if at all. Unknown why
 *         this is the case as it is the same listener code on a canvas, but the objects are separated. Disabling double buffer does not help. Only idea is that some methods are synchronized - it
 *         maybe by splitting the classes code which was previously synchronious can now be executed in parallel.
 */
public abstract class TradesWiringDiagram implements PaintListener, MouseListener, MouseMoveListener {
	/**
	 * Contains pairs whose first element is left terminal colour and second is right terminal colour, for each element in {@link #wiring}
	 */
	private final List<Pair<Color, Color>> terminalColours = new ArrayList<Pair<Color, Color>>();
	private final List<Pair<Boolean, Boolean>> terminalOptionals = new ArrayList<Pair<Boolean, Boolean>>();
	private final List<Boolean> leftTerminalValid = new ArrayList<Boolean>();
	private final List<Boolean> rightTerminalValid = new ArrayList<Boolean>();

	/**
	 * Contains the colour for each wire; the ith colour applies to the wire from the ith left hand terminal
	 */
	private final List<Color> wireColours = new ArrayList<Color>();
	/**
	 * The actual wiring permutation; if the ith element is j, left hand terminal i is wired to right hand terminal j. If the ith element is -1, the ith element is not connected to anywhere.
	 */
	private final List<Integer> wiring = new ArrayList<Integer>();

	private int terminalSize = 11;
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

	/**
	 * Set the colour of left hand terminal i to the given color. Doesn't refresh.
	 * 
	 * @param i
	 * @param color
	 */
	public synchronized void setLeftTerminalColor(final int index, final Color color) {
		terminalColours.get(index).setFirst(color);
	}

	/**
	 * Set the colour of right hand terminal i to the given color. Doesn't refresh
	 * 
	 * @param i
	 * @param color
	 */
	public synchronized void setRightTerminalColor(final int index, final Color color) {
		terminalColours.get(index).setSecond(color);
	}

	public void setLeftTerminalOptional(int i, boolean b) {

		terminalOptionals.get(i).setFirst(b);
	}

	public void setRightTerminalOptional(int i, boolean b) {

		terminalOptionals.get(i).setSecond(b);
	}

	/**
	 * Set all the terminal colours at once; the ith element is a pair, whose first member is the left hand colour.
	 * 
	 * The list of colours should be the same size as the wiring list last passed to {@link #setWiring(List)}
	 * 
	 * @param colours
	 */
	public synchronized void setTerminalColors(final List<Pair<Color, Color>> colours) {
		terminalColours.clear();
		terminalColours.addAll(colours);
	}

	public synchronized void setTerminalOptionals(final List<Pair<Boolean, Boolean>> vals) {
		terminalOptionals.clear();
		terminalOptionals.addAll(vals);
	}

	public synchronized void setWireColor(final int index, final Color color) {
		wireColours.set(index, color);
	}

	public synchronized void setWireColors(final List<Color> colours) {
		wireColours.clear();
		wireColours.addAll(colours);
	}

	public synchronized void setLeftTerminalValid(final int index, final boolean valid) {
		leftTerminalValid.set(index, valid);
	}

	public synchronized void setRightTerminalValid(final int index, final boolean valid) {
		rightTerminalValid.set(index, valid);
	}

	public synchronized void setTerminalsValid(final List<Boolean> leftTerminalsValid, final List<Boolean> rightTerminalsValid) {
		this.leftTerminalValid.clear();
		this.leftTerminalValid.addAll(leftTerminalsValid);
		this.rightTerminalValid.clear();
		this.rightTerminalValid.addAll(rightTerminalsValid);
	}

	public synchronized void setWiring(final List<Integer> wiring) {
		this.wiring.clear();
		this.wiring.addAll(wiring);
	}

	public List<Integer> getWiring() {
		return Collections.unmodifiableList(wiring);
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
		// Get a list of terminal positions from subclass
		final List<Float> terminalPositions = getTerminalPositions();

		// Copy arrays in case of concurrent change during paint
		final List<Integer> wiring2 = new ArrayList<Integer>(wiring);
		final List<Boolean> leftTerminalValid2 = new ArrayList<Boolean>(leftTerminalValid);
		final List<Pair<Color, Color>> terminalColours2 = new ArrayList<Pair<Color, Color>>(terminalColours);
		final List<Pair<Boolean, Boolean>> terminalOptionals2 = new ArrayList<Pair<Boolean, Boolean>>(terminalOptionals);
		final List<Boolean> rightTerminalValid2 = new ArrayList<Boolean>(rightTerminalValid);

		final GC graphics = e.gc;

		graphics.setAntialias(SWT.ON);

		final Rectangle ca = getCanvasClientArea();
		// Clip to reported client area to avoid overdraw
		graphics.setClipping(ca);

		graphics.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		// Fill whole area - not for use in a table
		// graphics.fillRectangle(0, 0, ca.width, ca.height);

		graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		graphics.setLineWidth(2);

		// draw paths
		for (int rawI = 0; rawI < wiring2.size(); rawI++) {

			// Map back between current row (sorted) and data (unsorted)
			int i = reverseSortedIndices == null ? rawI : reverseSortedIndices[rawI];

			// -1 indicates filtered row
			if (i == -1) {
				continue;
			}

			// See if there is a valid wire
			final Integer destinationInteger = wiring2.get(i);

			if (destinationInteger == null) {
				continue;
			}
			final int destination = destinationInteger.intValue();
			if (destination < 0) {
				continue; // no wire
			}
			// Map back between current row (sorted) and data (unsorted)
			int rawDestination = sortedIndices == null ? destination : sortedIndices[destination];

			// If this is the wire currently being dragged, skip and handle in next code block
			if (dragging) {
				if (draggingFromLeft && draggingFrom == rawI) {
					continue;
				} else if (!draggingFromLeft && rawDestination == draggingFrom) {
					continue;
				}
			}

			final float startMid = terminalPositions.get(rawI);
			final float endMid = terminalPositions.get(rawDestination);

			// Draw wire - offset by ca.x to as x pos is relative to left hand side
			final Path path = makeConnector(e.display, ca.x + 1.5f * terminalSize, startMid, ca.x + ca.width - 1.5f * terminalSize, endMid);

			graphics.setForeground(wireColours.get(i));
			graphics.drawPath(path);
			path.dispose();
		}

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

		// draw terminal blobs
		graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		graphics.setLineWidth(borderWidth);
		int rawI = 0;
		for (final float midpoint : terminalPositions) {
			// Map back between current row (sorted) and data (unsorted)
			int i = reverseSortedIndices == null ? rawI : reverseSortedIndices[rawI];
			// -1 indicates filtered row
			if (i == -1) {
				continue;
			}

			// Draw left hand terminal
			graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			if (leftTerminalValid2.get(i)) {
				graphics.setBackground(terminalColours2.get(i).getFirst());
				graphics.fillOval(ca.x + terminalSize, (int) (midpoint - (terminalSize / 2)), terminalSize, terminalSize);
				graphics.drawOval(ca.x + terminalSize, (int) (midpoint - (terminalSize / 2)), terminalSize, terminalSize);
				if (terminalOptionals2.get(i).getFirst()) {
					graphics.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					graphics.fillOval(ca.x + terminalSize + 4, (int) midpoint - (terminalSize / 2) + 4, 4, 4);
				}
			}

			// Draw right hand terminal
			graphics.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			if (rightTerminalValid2.get(i)) {
				graphics.setBackground(terminalColours2.get(i).getSecond());
				graphics.fillOval(ca.x + ca.width - 2 * terminalSize, (int) (midpoint - terminalSize / 2), terminalSize, terminalSize);
				graphics.drawOval(ca.x + ca.width - 2 * terminalSize, (int) (midpoint - terminalSize / 2), terminalSize, terminalSize);
				if (terminalOptionals2.get(i).getSecond()) {
					graphics.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					graphics.fillOval(ca.x + ca.width - 2 * terminalSize + 4, (int) (midpoint - terminalSize / 2) + 4, 4, 4);
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
		final List<Float> positions = getTerminalPositions();
		for (final float y : positions) {
			if (e.y >= (y - terminalSize / 2) && e.y <= (y + terminalSize / 2)) {
				break;
			}
			terminal++;
		}

		if (terminal >= positions.size()) {
			return;
		}

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

		if (e.button == 3) {
			final List<Float> positions = getTerminalPositions();

			int terminal = 0;

			for (final float y : positions) {
				if (e.y >= (y - terminalSize / 2) && e.y <= (y + terminalSize / 2)) {
					break;
				}
				terminal++;
			}

			final boolean draggedToNowhere = terminal >= positions.size() || !(draggingFromLeft ? rightTerminalValid.get(terminal) : leftTerminalValid.get(terminal));

			if (!draggedToNowhere) {
				openContextMenu(draggingFromLeft, terminal, e.x, e.y);
			}
			return;

		}

		// if we are dragging, awesome, we have finished.
		if (dragging) {
			dragging = false;

			final List<Float> positions = getTerminalPositions();

			int terminal = 0;

			for (final float y : positions) {
				if (e.y >= (y - terminalSize / 2) && e.y <= (y + terminalSize / 2)) {
					break;
				}
				terminal++;
			}

			if (!(terminal >= positions.size()) && reverseSortedIndices != null) {
				terminal = reverseSortedIndices[terminal];
			}
			final boolean draggedToNowhere = terminal >= positions.size() || !(draggingFromLeft ? rightTerminalValid.get(terminal) : leftTerminalValid.get(terminal));

			final Rectangle ca = getCanvasClientArea();

			final boolean control = (e.stateMask & SWT.CONTROL) != 0;

			int sortedIndex = reverseSortedIndices == null ? draggingFrom : reverseSortedIndices[draggingFrom];
			// now find column
			if (!draggedToNowhere && !draggingFromLeft && (e.x >= ca.x + terminalSize && e.x <= ca.x + 2 * terminalSize)) {

				// arrived in left column from right
				final int ix = wiring.indexOf(sortedIndex);
				if (control) {
					if (ix >= 0) {
						wiring.set(ix, wiring.get(terminal));
					}
				} else {
					if (ix >= 0) {
						wiring.set(ix, -1);
					}
				}
				wiring.set(terminal, sortedIndex);
			} else if (!draggedToNowhere && draggingFromLeft && (e.x >= ca.x + ca.width - terminalSize * 2 && e.x <= ca.x + ca.width - terminalSize)) {
				// arrived in right column
				final int ix = wiring.indexOf(terminal);
				if (control) {
					if (ix >= 0) {
						wiring.set(ix, wiring.get(sortedIndex));
					}
				} else {
					if (ix >= 0) {
						wiring.set(ix, -1);
					}
				}
				wiring.set(sortedIndex, terminal);
			} else {
				// clear wire
				if (draggingFromLeft) {
					wiring.set(sortedIndex, -1);
				} else {
					final int ix = wiring.indexOf(sortedIndex);
					if (ix >= 0) {
						wiring.set(ix, -1);
					}
				}
			}
			wiringChanged(wiring);
		}

		if (!canvas.isDisposed()) {
			canvas.redraw();
		}
	}

	/**
	 * subclassses should use this to provide the vertical positions of the terminals.
	 * 
	 * @return
	 */
	protected abstract List<Float> getTerminalPositions();

	/**
	 * called to tell subclasses wiring has changed. Probably should use listener pattern really. Called just before a redraw, so setting colours will take effect immediately;
	 */
	protected abstract void wiringChanged(final List<Integer> newWiring);

	protected void openContextMenu(final boolean leftSide, final int terminal, final int mouseX, final int mouseY) {
	}

	public void setLocked(final boolean locked) {
		this.locked = locked;

	}

	public void redraw() {
		canvas.redraw();
	}

	public void setSortOrder(int[] sortedIndices, int[] reverseSortedIndices) {
		this.sortedIndices = sortedIndices;
		this.reverseSortedIndices = reverseSortedIndices;
	}
}
