package com.mmxlabs.widgets.schedulechart;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

public class DragSelectionZoomHandler implements MouseListener, MouseMoveListener {

	final ScheduleCanvas canvas;
	final IScheduleChartColourScheme colourScheme;
	
	boolean visible;

	int startX;
	int startY;
	int endX;
	int endY;

	public DragSelectionZoomHandler(final ScheduleCanvas canvas, IScheduleChartColourScheme colourScheme) {
		this.canvas = canvas;
		this.colourScheme = colourScheme;
		this.visible = false;
		
		canvas.addMouseListener(this);
		canvas.addMouseMoveListener(this);
	}

	@Override
	public void mouseMove(MouseEvent e) {
		if (visible) {
			endX = e.x;
			endY = e.y;
			canvas.redraw();
		}
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (e.button != 3) return;
		startX = endX = e.x;
		startY = endY = e.y;
		visible = true;
		canvas.redraw();
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if (e.button != 3) return;
		visible = false;
		if (Math.abs(endX - startX) > 10) {
			canvas.getTimeScale().fitSelection(startX, endX);
		}
		canvas.redraw();
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// Do nothing
	}

	public DrawableElement getDrawableSelectionRectangle() {
		var drawableElement = new DrawableElement() {

			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
				if (!visible) return List.of();
				var colour = colourScheme.getSelectionRectangleColour();
				return List.of(BasicDrawableElements.Rectangle.withBounds(Math.min(startX, endX), Math.min(startY, endY), Math.abs(endX - startX), Math.abs(endY - startY))
						.bgColour(colour).border(colour, 2).alpha(60).create());
			}
		};
		drawableElement.setBounds(canvas.getBounds());
		return drawableElement;
	}

}
