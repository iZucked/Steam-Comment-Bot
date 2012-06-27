/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.dialogs;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.Pair;

/**
 * A control for drawing a wiring diagram. This just displays an arbitrary
 * bipartite graph / matching.
 * 
 * The graph must have equal numbers of vertices on each side, which are
 * identified by the integers from 0 to n-1. The wiring itself is described in
 * the {@link #wiring} list. The ith element of the list contains the index of
 * the right-hand element to which left hand element i is connected, or -1 if
 * there is no connection.
 * 
 * Thus wiring.get(1) gives the right-hand node connected to left-hand node 1,
 * and wiring.indexOf(1) gives the left-hand node connected to right-hand node
 * 1.
 * 
 * TODO allow mismatched states, where there may be differing numbers of
 * left-hand and right-hand terminals.
 * 
 * @author Tom Hinton
 * 
 */
public abstract class WiringDiagram extends Canvas implements PaintListener,
		MouseListener, MouseMoveListener {
	private final List<Pair<Color, Color>> terminalColours = new ArrayList<Pair<Color, Color>>();
	private final List<Color> wireColours = new ArrayList<Color>();
	// Indices?
	private final List<Integer> wiring = new ArrayList<Integer>();

	private int terminalSize = 10;
	private int pathWidth = 2;

	private boolean dragging = false;
	private int draggingFrom = -1;
	private boolean draggingFromLeft = false;

	private int dragX, dragY;

	/**
	 * Create a new wiring diagram
	 * 
	 * @param parent
	 * @param style
	 */
	public WiringDiagram(final Composite parent, int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);
		addPaintListener(this);
		addMouseListener(this);
		addMouseMoveListener(this);
	}

	/**
	 * Set the colour of left hand terminal i to the given color. Doesn't
	 * refresh.
	 * 
	 * @param i
	 * @param color
	 */
	public void setLeftTerminalColor(final int index, final Color color) {
		terminalColours.get(index).setFirst(color);
	}

	/**
	 * Set the colour of right hand terminal i to the given color. Doesn't
	 * refresh
	 * 
	 * @param i
	 * @param color
	 */
	public void setRightTerminalColor(final int index, final Color color) {
		terminalColours.get(index).setSecond(color);
	}

	/**
	 * Set all the terminal colours at once; the ith element is a pair, whose
	 * first member is the left hand colour
	 * 
	 * @param colours
	 */
	public void setTerminalColors(final List<Pair<Color, Color>> colours) {
		terminalColours.clear();
		terminalColours.addAll(colours);
	}

	public void setWireColor(final int index, final Color color) {
		wireColours.set(index, color);
	}

	public void setWireColors(final List<Color> colours) {
		wireColours.clear();
		wireColours.addAll(colours);
	}

	public void setWiring(final List<Integer> wiring) {
		this.wiring.clear();
		this.wiring.addAll(wiring);
	}

	public List<Integer> getWiring() {
		return Collections.unmodifiableList(wiring);
	}

	public int getTerminalSize() {
		return terminalSize;
	}

	public void setTerminalSize(int terminalSize) {
		this.terminalSize = terminalSize;
	}

	public int getPathWidth() {
		return pathWidth;
	}

	public void setPathWidth(int pathWidth) {
		this.pathWidth = pathWidth;
	}

	private Path makeConnector(final Device d, final float x1, final float y1,
			final float x2, final float y2) {
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
	public void paintControl(final PaintEvent e) {
		final List<Float> terminalPositions = getTerminalPositions();
		final GC graphics = e.gc;
		graphics.setAntialias(SWT.ON);

		graphics.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));

		Rectangle ca = getClientArea();

		graphics.fillRectangle(0, 0, ca.width, ca.height);

		graphics.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_BLACK));
		graphics.setLineWidth(pathWidth);

		// draw paths
		for (int i = 0; i < wiring.size(); i++) {
			final int destination = wiring.get(i);

			if (destination < 0)
				continue; // no wire

			if (dragging) {
				if (draggingFromLeft && draggingFrom == i)
					continue;
				else if (!draggingFromLeft && destination == draggingFrom)
					continue;
			}

			final float startMid = terminalPositions.get(i);
			final float endMid = terminalPositions.get(destination);

			final float midMid = (startMid + endMid) / 2.0f;

			// final Path path = new Path(e.display);
			// path.moveTo((float) (1.5 * terminalSize), startMid);
			// path.quadTo(ca.width / 4, startMid, ca.width / 2, midMid);
			// path.quadTo(ca.width - ca.width / 4, endMid,
			// (float) (ca.width - 1.5 * terminalSize), endMid);

			final Path path = makeConnector(e.display, 1.5f * terminalSize,
					startMid, ca.width - 1.5f * terminalSize, endMid);

			graphics.setForeground(wireColours.get(i));
			graphics.drawPath(path);
		}

		if (dragging) {
			// draw dragging path
			final float mid = terminalPositions.get(draggingFrom);

			final Path path;
			if (draggingFromLeft) {
				path = makeConnector(e.display, 1.5f * terminalSize, mid,
						dragX, dragY);
			} else {
				// move to mouse, path to right point
				path = makeConnector(e.display, dragX, dragY, ca.width - 1.5f
						* terminalSize, mid);
			}
			graphics.setForeground(Display.getCurrent().getSystemColor(
					SWT.COLOR_DARK_RED));
			graphics.drawPath(path);
		}

		int i = 0;
		graphics.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_BLACK));
		
		for (final float midpoint : terminalPositions) {

			// draw terminal blobs
			// final int midpoint = vMidPoints.get(i);
			graphics.setBackground(terminalColours.get(i).getFirst());

			graphics.fillOval(terminalSize,
					(int) (midpoint - (terminalSize / 2)), terminalSize,
					terminalSize);
			graphics.drawOval(terminalSize,
					(int) (midpoint - (terminalSize / 2)), terminalSize,
					terminalSize);

			graphics.setBackground(terminalColours.get(i).getSecond());

			graphics.fillOval(ca.width - 2 * terminalSize,
					(int) (midpoint - terminalSize / 2), terminalSize,
					terminalSize);
			graphics.drawOval(ca.width - 2 * terminalSize,
					(int) (midpoint - terminalSize / 2), terminalSize,
					terminalSize);

			i++;
			
		}
	}

	/*
	 * Drag handling stuff to edit wiring
	 */

	@Override
	public void mouseDoubleClick(final MouseEvent e) {
	}

	@Override
	public void mouseDown(final MouseEvent e) {
		// detect whether we have clicked on a terminal
		final Rectangle ca = getClientArea();

		// determine which row ( if any )
		int terminal = 0;
		final List<Float> positions = getTerminalPositions();
		for (final float y : positions) {
			if (e.y >= (y - terminalSize / 2) && e.y <= (y + terminalSize / 2))
				break;
			terminal++;
		}

		if (terminal >= positions.size())
			return;

		// now find column
		if ((e.x >= terminalSize && e.x <= 2 * terminalSize)) {
			// left column
			dragging = true;
			draggingFrom = terminal;
			draggingFromLeft = true;
		} else if (e.x >= ca.width - terminalSize * 2
				&& e.x <= ca.width - terminalSize) {
			// right column
			dragging = true;
			draggingFromLeft = false;
			draggingFrom = terminal;
		}
		if (dragging) {
			dragX = e.x;
			dragY = e.y;
			redraw();
		}
	}

	@Override
	public void mouseMove(final MouseEvent e) {
		// if we are dragging, move the wire terminus and refresh
		if (dragging) {
			dragX = e.x;
			dragY = e.y;
			redraw();
		}
	}

	@Override
	public void mouseUp(final MouseEvent e) {
		// if we are dragging, awesome, we have finished.
		if (dragging) {
			dragging = false;

			final List<Float> positions = getTerminalPositions();
			
			int terminal = 0;

			for (final float y : positions) {
				if (e.y >= (y - terminalSize / 2)
						&& e.y <= (y + terminalSize / 2))
					break;
				terminal++;
			}

			boolean draggedToNowhere = terminal >= positions.size();
			
			
			
			final Rectangle ca = getClientArea();

			final boolean control = (e.stateMask & SWT.CONTROL) != 0;

			// now find column
			if (!draggedToNowhere && !draggingFromLeft
					&& (e.x >= terminalSize && e.x <= 2 * terminalSize)) {
				// arrived in left column from right
				final int ix = wiring.indexOf(draggingFrom);
				if (control) {
					if (ix >= 0)
						wiring.set(ix, wiring.get(terminal));
				} else {
					if (ix >= 0)
						wiring.set(ix, -1);
				}
				wiring.set(terminal, draggingFrom);
			} else if (!draggedToNowhere
					&& draggingFromLeft
					&& (e.x >= ca.width - terminalSize * 2 && e.x <= ca.width
							- terminalSize)) {
				// arrived in right column
				final int ix = wiring.indexOf(terminal);
				if (control) {
					if (ix >= 0)
						wiring.set(ix, wiring.get(draggingFrom));
				} else {
					if (ix >= 0)
						wiring.set(ix, -1);
				}
				wiring.set(draggingFrom, terminal);
			} else {
				// clear wire
				if (draggingFromLeft) {
					wiring.set(draggingFrom, -1);
				} else {
					final int ix = wiring.indexOf(draggingFrom);
					if (ix >= 0) {
						wiring.set(ix, -1);
					}
				}
			}
			wiringChanged(wiring);
		}

		if (!isDisposed()) redraw();
	}

	/**
	 * subclassses should use this to provide the heights of the terminals.
	 * 
	 * @return
	 */
	protected abstract List<Float> getTerminalPositions();

	/**
	 * called to tell subclasses wiring has changed. Probably should use
	 * listener pattern really. Called just before a redraw, so setting colours
	 * will take effect immediately;
	 */
	protected abstract void wiringChanged(final List<Integer> newWiring);
}
