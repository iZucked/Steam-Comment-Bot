/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.curveeditor;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * Plots and edits an interpolated poly-line type curve
 * 
 * @author Tom Hinton
 * 
 */
public class CurveCanvas extends Canvas implements PaintListener,
		MouseListener, MouseMoveListener {
	private static final float WIDTH = 4;
	private static final int TICK_VERTICAL_SPACE = 16;
	private static final int MIN_TICK_SPACE = 128;
	private TreeMap<Integer, Double> values = new TreeMap<Integer, Double>();

	private boolean dragging = false;

	private int lastViewHeight = 0;
	/**
	 * How many pixels correspond to an x unit in the values map
	 */
	private int zoom = 32;
	private int draggingX;

	public CurveCanvas(Composite parent, int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);
		addPaintListener(this);
		addMouseListener(this);
		addMouseMoveListener(this);
	}

	/**
	 * take a value from values and convert it to display space
	 * 
	 * @param value
	 * @return
	 */
	private int zoom(int value) {
		return value / zoom;
	}

	/**
	 * take a value from display space and convert it to values space
	 * 
	 * @param value
	 * @return
	 */
	private int unzoom(int value) {
		return value * zoom;
	}

	public void zoomOut() {
		zoom *= 2;
		redraw();
	}

	public void zoomIn() {
		if (zoom == 1)
			return;
		zoom /= 2;
		redraw();
	}

	@Override
	public void paintControl(PaintEvent e) {
		final GC graphics = e.gc;

		final Device d = graphics.getDevice();

		graphics.setBackground(d.getSystemColor(SWT.COLOR_WHITE));
		graphics.fillRectangle(getClientArea());
		graphics.setAntialias(SWT.ON);

		graphics.setBackground(d.getSystemColor(SWT.COLOR_DARK_GREEN));

		final Path p = new Path(d);
		boolean drawFirstEdge = true;
		final int height = getClientArea().height;
		final int maxWidth = getClientArea().width;

		final int voffset = height - graphics.textExtent("X").y - 4
				- TICK_VERTICAL_SPACE;
		lastViewHeight = voffset;
		float lastX = 0, lastY = 0;
		for (final Map.Entry<Integer, Double> entry : values.entrySet()) {
			final float x = zoom(entry.getKey());
			final float y = (float) (voffset - voffset * entry.getValue());

			if (drawFirstEdge) {
				p.moveTo(0, y);
				drawFirstEdge = false;
			}

			p.lineTo(x, y);
			p.moveTo(x, y);
			lastX = x;
			lastY = y;
		}

		if (lastX < maxWidth) {
			p.lineTo(maxWidth, lastY);
		}

		graphics.setLineWidth(2);
		graphics.drawPath(p);
		for (final Map.Entry<Integer, Double> entry : values.entrySet()) {
			final float x = zoom(entry.getKey());
			final float y = (float) (voffset - voffset * entry.getValue());
			graphics.fillOval((int) (x - WIDTH), (int) (y - WIDTH),
					(int) (2 * WIDTH), (int) (2 * WIDTH));
			graphics.drawOval((int) (x - WIDTH), (int) (y - WIDTH),
					(int) (2 * WIDTH), (int) (2 * WIDTH));
		}

		if (dragging) {
			final float x = zoom(draggingX) + WIDTH;
			final float y = (float) (voffset - voffset * values.get(draggingX)) - graphics.textExtent("X").y - 4;
			graphics.setBackground(d.getSystemColor(SWT.COLOR_WHITE));

			graphics.drawText(String.format("(%d, %d%%)", draggingX,
					(int) (100 * values.get(draggingX))), (int) x, (int) y);
		}

		drawTicks(graphics);
	}

	/**
	 * @param graphics
	 */
	private void drawTicks(final GC graphics) {
		graphics.setBackground(graphics.getDevice().getSystemColor(
				SWT.COLOR_WHITE));
		int weeksPerTick = 1;
		// while (tickSpacing < MIN_TICK_SPACE) {
		while (zoom(24 * 7 * weeksPerTick) < MIN_TICK_SPACE) {
			weeksPerTick++;
		}

		final int tickSpacing = zoom(24 * 7 * weeksPerTick);
		int maxWidth = getClientArea().width;
		int h = getClientArea().height;
		int tickNum = 1;
		final int voffset = h - graphics.textExtent("X").y - 4;
		graphics.drawLine(0, voffset - TICK_VERTICAL_SPACE / 2, maxWidth,
				voffset - TICK_VERTICAL_SPACE / 2);
		for (int x = tickSpacing; x < maxWidth; x += tickSpacing) {
			final String label = tickNum * weeksPerTick + " weeks";
			final Point textSize = graphics.textExtent(label);

			graphics.drawLine(x, voffset, x, voffset - TICK_VERTICAL_SPACE);

			graphics.drawText(label, x - textSize.x / 2, voffset);
			tickNum++;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt
	 * .events.MouseEvent)
	 */
	@Override
	public void mouseDoubleClick(MouseEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events
	 * .MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		float y = (float) ((lastViewHeight - e.y) / (double) lastViewHeight);
		if (y > 1)
			y = 1;
		final int x = unzoom(e.x);

		for (final Map.Entry<Integer, Double> entry : values.subMap(
				(int) (x - 2 * unzoom((int) WIDTH)),
				(int) (x + 2 * unzoom((int) WIDTH))).entrySet()) {

			// project entry into view space
			final int viewX = zoom(entry.getKey());
			final int viewY = (int) (lastViewHeight * (1 - entry.getValue()));

			if (Math.sqrt(Math.pow(viewX - e.x, 2) + Math.pow(viewY - e.y, 2)) < WIDTH) {
				// delete clicked point (improve this behaviour later, drag,
				// remove when in-line?)
				if ((e.stateMask & SWT.CONTROL) != 0) {
					values.remove(entry.getKey());
				} else {
					dragging = true;
					draggingX = entry.getKey();
				}
				redraw();
				return;
			}
		}
		if (y > 0) {
			values.put(x, (double) y);
			dragging = true;
			draggingX = x;
		}
		redraw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent e) {
		if (dragging) {
			dragging = false;
			redraw();
		}
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		final Point p = new Point(Math.max(wHint, values.isEmpty() ? 0
				: zoom(values.descendingKeySet().first())), hHint);
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events
	 * .MouseEvent)
	 */
	@Override
	public void mouseMove(final MouseEvent e) {
		if (dragging) {
			float y = (float) ((lastViewHeight - e.y) / (double) lastViewHeight);
			if (y > 1)
				y = 1;
			if (y < 0)
				y = 0;
			final int x = unzoom(e.x);

			values.remove(draggingX);
			values.put(x, (double) y);
			draggingX = x;

			redraw();
		}
	}

	/**
	 * @param points
	 */
	public void setPoints(Map<Integer, Double> points) {
		values.clear();
		values.putAll(points);
		redraw();
	}

	/**
	 * @return
	 */
	public Map<Integer,Double> getPoints() {
		return Collections.unmodifiableMap(values);
	}
}
