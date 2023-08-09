package com.mmxlabs.widgets.schedulechart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.Pair;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DefaultScheduleCanvasColourScheme;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRowHeaders;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleTimeScale;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;
import com.mmxlabs.widgets.schedulechart.draw.GCBasedScheduleElementDrawer;
import com.mmxlabs.widgets.schedulechart.draw.ScheduleElementDrawer;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public class ScheduleCanvas extends Canvas implements IScheduleChartContentBoundsProvider {
	
	private final ScheduleTimeScale timeScale;
	private final DrawableScheduleTimeScale<ScheduleTimeScale> drawableTimeScale;

	private final HorizontalScrollbarHandler horizontalScrollbarHandler;
	
	private final IScheduleChartSettings settings;
	private final IScheduleChartColourScheme colourScheme;
	private final IScheduleEventStylingProvider eventStylingProvider;

	private Rectangle mainBounds;
	private List<ScheduleChartRow> rows = new ArrayList<>();

	private ScheduleEvent leftmostEvent;
	private ScheduleEvent rightmostEvent;

	public ScheduleCanvas(Composite parent, IScheduleEventStylingProvider eventStylingProvider) {
		this(parent, eventStylingProvider, new DefaultScheduleChartSettings(), new DefaultScheduleCanvasColourScheme());
	}

	public ScheduleCanvas(Composite parent, IScheduleEventStylingProvider eventStylingProvider, IScheduleChartSettings settings, IScheduleChartColourScheme colourScheme) {
		super(parent, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED | SWT.V_SCROLL | SWT.H_SCROLL);
		this.settings = settings;
		this.colourScheme = colourScheme;

		this.timeScale = new ScheduleTimeScale(this, settings);
		this.drawableTimeScale = new DrawableScheduleTimeScale<>(timeScale, colourScheme, settings);
		
		this.horizontalScrollbarHandler = new HorizontalScrollbarHandler(getHorizontalBar(), timeScale);
		
		this.eventStylingProvider = eventStylingProvider;
		
		initListeners();
	}

	private void initListeners() {
		addPaintListener(e -> ScheduleCanvas.this.repaint(e.gc));
		
		addMouseWheelListener(e -> {
			// TODO: change MOD1 and MOD2 to be dependent on settings object
			if (e.stateMask == SWT.MOD1) {
				timeScale.zoomBy(new Point(e.x, e.y), e.count * 5, e.count > 0);
			} else if (e.stateMask == SWT.MOD2) {
				horizontalScrollbarHandler.handle(e);
			} else {
				// vertical scroll
			}
			redraw();
		});
		
		addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				// empty for now
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				horizontalScrollbarHandler.handle(e);
				redraw();
			}
		});
	}

	private void repaint(GC gc) {
		GCBasedScheduleElementDrawer gcBasedDrawer = new GCBasedScheduleElementDrawer(gc);
		drawChartWithDrawer(gcBasedDrawer, gcBasedDrawer);
	}
	
	private void drawChartWithDrawer(ScheduleElementDrawer drawer, DrawerQueryResolver resolver) {
  Rectangle originalBounds;
		// Always fill white to avoid occasional issue where we do not render over
		// the unused area correctly.
		drawer.drawOne(BasicDrawableElements.Rectangle.withBounds(getClientArea()).bgColour(Display.getDefault().getSystemColor(SWT.COLOR_WHITE)).create());

		originalBounds = getClientArea();
		
		final int verticalScroll = getVerticalBar().isVisible() ? getVerticalBar().getSelection() : 0;
		Rectangle bounds = new Rectangle(originalBounds.x, originalBounds.y - verticalScroll, originalBounds.width, originalBounds.height + verticalScroll);
		
		final int rowHeaderWidth = calculateRowHeaderWidth(resolver);
		mainBounds = new Rectangle(bounds.x + rowHeaderWidth, bounds.y, bounds.width - rowHeaderWidth, bounds.height);

		timeScale.setBounds(mainBounds);
		drawableTimeScale.setBounds(mainBounds);
		drawableTimeScale.setLockedHeaderY(originalBounds.y);

		drawer.drawOne(drawableTimeScale.getGrid(mainBounds.y + drawableTimeScale.getTotalHeaderHeight()), resolver);
		drawer.draw(getDrawableRows(resolver), resolver);
		drawer.drawOne(drawableTimeScale.getHeaders(), resolver);
		
		var rowHeader = new DrawableScheduleChartRowHeaders(rows, drawableTimeScale.getTotalHeaderHeight(), colourScheme, settings);
		rowHeader.setBounds(new Rectangle(originalBounds.x, mainBounds.y + drawableTimeScale.getTotalHeaderHeight(), rowHeaderWidth, mainBounds.height));
		rowHeader.setLockedHeaderY(originalBounds.y);
		drawer.drawOne(rowHeader, resolver);
	}
	
	private int calculateRowHeaderWidth(DrawerQueryResolver resolver) {
		final int maxTextWidth = rows.stream().map(r -> r.getName() == null ? 0 : resolver.findSizeOfText(r.getName()).x).max(Integer::compare).orElse(0) + settings.getRowHeaderRightPadding();
		return Math.max(settings.getMinimumRowHeaderWidth(), maxTextWidth);
	}

	private List<DrawableElement> getDrawableRows(DrawerQueryResolver resolver) {
		List<DrawableElement> res = new ArrayList<>();
		final int height = settings.getRowHeight();
		final int startY = mainBounds.y + drawableTimeScale.getTotalHeaderHeight();
		for (int i = 0, y = startY; i < rows.size(); i++, y += height  + 1) {
			DrawableScheduleChartRow drawableRow = new DrawableScheduleChartRow(rows.get(i), i, timeScale, colourScheme, eventStylingProvider, settings);
			drawableRow.setBounds(new Rectangle(mainBounds.x, y, mainBounds.width, height));
			res.add(drawableRow);
		}
		return res;
	}


	public final ScheduleTimeScale getTimeScale() {
		return timeScale;
	}
	
	public void addRow(ScheduleChartRow r) {
		rows.add(r);
		recalculateContentBounds();
	}
	
	public void addAll(List<ScheduleChartRow> rs) {
		rows.addAll(rs);
		recalculateContentBounds();
	}
	
	public void clear() {
		rows.clear();
		recalculateContentBounds();
	}

	@Override
	public Pair<Integer, Integer> getContentBounds() {
		int min = timeScale.getXBoundsFromEvent(leftmostEvent).getFirst();
		int max = timeScale.getXBoundsFromEvent(rightmostEvent).getSecond();
		return Pair.of(min, max);
	}

	@Override
	public ScheduleEvent getLeftmostEvent() {
		return leftmostEvent;
	}

	@Override
	public ScheduleEvent getRightmostEvent() {
		return rightmostEvent;
	}
	
	
	private void recalculateContentBounds() {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		
		ScheduleEvent minEvent = null;
		ScheduleEvent maxEvent = null;

		for (final ScheduleChartRow row: rows) {
			for (final ScheduleEvent event: row.getEvents()) {
				var bounds = timeScale.getXBoundsFromEvent(event);
				if (bounds.getFirst() < min) {
					min = bounds.getFirst();
					minEvent = event;
				}

				if (bounds.getSecond() > max) {
					max = bounds.getSecond();
					maxEvent = event;
				}
			}
		}
		
		leftmostEvent = minEvent;
		rightmostEvent = maxEvent;
	}

}
