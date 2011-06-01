/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.curveeditor;

import java.util.Map;
import java.util.TreeMap;

import javax.security.auth.Refreshable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * Plots and edits an interpolated poly-line type curve
 * 
 * @author Tom Hinton
 * 
 */
public class CurveCanvas extends Canvas implements PaintListener, MouseListener {
	private static final float WIDTH = 4;
	private TreeMap<Integer, Double> values = new TreeMap<Integer, Double>();

	public CurveCanvas(Composite parent, int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);
		addPaintListener(this);
		addMouseListener(this);
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
		for (final Map.Entry<Integer, Double> entry : values.entrySet()) {
			final float x = entry.getKey();
			final float y = (float) (height - height * entry.getValue());

			if (drawFirstEdge) {
				p.moveTo(0, y);
				drawFirstEdge = false;
			}

			p.lineTo(x, y);
			p.moveTo(x, y);
		}
		graphics.setLineWidth(2);
		graphics.drawPath(p);
		for (final Map.Entry<Integer, Double> entry : values.entrySet()) {
			final float x = entry.getKey();
			final float y = (float) (height - height * entry.getValue());
			graphics.fillOval((int) (x - WIDTH), (int) (y - WIDTH),
					(int) (2 * WIDTH), (int) (2 * WIDTH));
			graphics.drawOval((int) (x - WIDTH), (int) (y - WIDTH),
					(int) (2 * WIDTH), (int) (2 * WIDTH));
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
		// check for existing blob
		final int height = getClientArea().height;

		final float y = (float) ((height - e.y)/(double)height);
		
		for (final Map.Entry<Integer, Double> entry : values.subMap(
				(int) (e.x - 2*WIDTH), (int) (e.x + 2*WIDTH)).entrySet()) {
			if (Math.sqrt(
					Math.pow(entry.getKey() - e.x, 2) +
					Math.pow(entry.getValue() - y , 2))<WIDTH) {
				// have hit value; not really clear what to do about it though.
				values.remove(entry.getKey());
				redraw();
				return;
			}	
		}
		values.put(e.x, (height - e.y) / (double) height);
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

	}
}
