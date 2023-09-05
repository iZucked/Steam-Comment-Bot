/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRowHeaders;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleTimeScale;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;
import com.mmxlabs.widgets.schedulechart.draw.GCBasedScheduleElementDrawer;
import com.mmxlabs.widgets.schedulechart.draw.ScheduleElementDrawer;
import com.mmxlabs.widgets.schedulechart.providers.IDrawableScheduleEventLabelProvider;
import com.mmxlabs.widgets.schedulechart.providers.IDrawableScheduleEventProvider;
import com.mmxlabs.widgets.schedulechart.providers.IDrawableScheduleEventTooltipProvider;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;
import com.mmxlabs.widgets.schedulechart.providers.ScheduleChartProviders;

public class ScheduleCanvas extends Canvas implements IScheduleChartEventEmitter {

	private final ScheduleCanvasState canvasState;

	private final ScheduleTimeScale timeScale;
	private final DrawableScheduleTimeScale<ScheduleTimeScale> drawableTimeScale;
	private final ScheduleFilterSupport filterSupport;

	private final HorizontalScrollbarHandler horizontalScrollbarHandler;
	private final DragSelectionZoomHandler dragSelectionZoomHandler;
	private final EventSelectionHandler eventSelectionHandler;
	private final EventHoverHandler eventHoverHandler;
	private final EventResizingHandler eventResizingHandler;
	private final RowHeaderMenuHandler rowHeaderMenuHandler;
	
	private final IScheduleChartSettings settings;
	private final IScheduleEventStylingProvider eventStylingProvider;
	private final IDrawableScheduleEventLabelProvider eventLabelProvider;
	private final IDrawableScheduleEventProvider drawableEventProvider;
	private final IDrawableScheduleEventTooltipProvider drawableTooltipProvider;
	
	private final List<IScheduleChartEventListener> listeners = new ArrayList<>();


	

	public ScheduleCanvas(Composite parent, ScheduleChartProviders providers) {
		this(parent, providers, new DefaultScheduleChartSettings());
	}

	public ScheduleCanvas(Composite parent, ScheduleChartProviders providers,  IScheduleChartSettings settings) {
		super(parent, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED | SWT.V_SCROLL | SWT.H_SCROLL);

		this.canvasState = new ScheduleCanvasState();

		this.settings = settings;
		this.drawableEventProvider = providers.drawableEventProvider();
		this.eventStylingProvider = null;
		this.drawableTooltipProvider = providers.drawableTooltipProvider();
		this.eventLabelProvider = providers.labelProvider();

		this.timeScale = new ScheduleTimeScale(canvasState, this, settings);
		this.drawableTimeScale = new DrawableScheduleTimeScale<>(timeScale, settings);
		this.filterSupport = new ScheduleFilterSupport(canvasState, settings);
		
		this.horizontalScrollbarHandler = new HorizontalScrollbarHandler(getHorizontalBar(), timeScale);
		this.eventSelectionHandler = new EventSelectionHandler(this);
		this.eventHoverHandler = new EventHoverHandler(this, canvasState);
		this.dragSelectionZoomHandler = new DragSelectionZoomHandler(this, settings, eventHoverHandler);
		this.eventResizingHandler = new EventResizingHandler(this, timeScale, settings, eventHoverHandler);
		this.rowHeaderMenuHandler = new RowHeaderMenuHandler(this, filterSupport);

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
		
		// to listen for zoom events and adjust the scrollbar
		addScheduleEventListener(horizontalScrollbarHandler.getEventListener());
		
		
	}

	private void repaint(GC gc) {
		GCBasedScheduleElementDrawer gcBasedDrawer = new GCBasedScheduleElementDrawer(gc);
		drawChartWithDrawer(gcBasedDrawer, gcBasedDrawer);
	}
	
	private void drawChartWithDrawer(ScheduleElementDrawer drawer, DrawerQueryResolver resolver) {
		// Always fill white to avoid occasional issue where we do not render over
		// the unused area correctly.
		drawer.drawOne(BasicDrawableElements.Rectangle.withBounds(getClientArea()).bgColour(Display.getDefault().getSystemColor(SWT.COLOR_WHITE)).create());

		Rectangle originalBounds = getClientArea();
		canvasState.setOriginalBounds(originalBounds);
		
		final int verticalScroll = getVerticalBar().isVisible() ? getVerticalBar().getSelection() : 0;
		Rectangle bounds = new Rectangle(originalBounds.x, originalBounds.y - verticalScroll, originalBounds.width, originalBounds.height + verticalScroll);
		
		final int rowHeaderWidth = calculateRowHeaderWidth(resolver);
		Rectangle mainBounds = new Rectangle(bounds.x + rowHeaderWidth, bounds.y, bounds.width - rowHeaderWidth, bounds.height);
		canvasState.setMainBounds(mainBounds);

		timeScale.setBounds(canvasState.getMainBounds());
		drawableTimeScale.setBounds(mainBounds);
		drawableTimeScale.setLockedHeaderY(originalBounds.y);

		// Draw grid
		drawer.drawOne(drawableTimeScale.getGrid(mainBounds.y + drawableTimeScale.getTotalHeaderHeight()), resolver);

		// Draw content
		canvasState.setLastDrawnContent(getDrawableRows(resolver));
		canvasState.getLastDrawnContent().forEach(r -> drawer.drawOne(r, resolver));
		
		// Draw labels
		getDrawableLabels(canvasState.getLastDrawnContent(), resolver).forEach(l -> drawer.drawOne(l, resolver));

		// Draw header
		drawer.drawOne(drawableTimeScale.getHeaders(), resolver);
		
		// Draw row headers
		var rowHeader = new DrawableScheduleChartRowHeaders(canvasState.getShownRows(), drawableTimeScale.getTotalHeaderHeight(), settings);
		rowHeader.setBounds(new Rectangle(originalBounds.x, mainBounds.y + drawableTimeScale.getTotalHeaderHeight(), rowHeaderWidth, mainBounds.height));
		rowHeader.setLockedHeaderY(originalBounds.y);
		drawer.drawOne(rowHeader, resolver);
		
		canvasState.getHoveredEvent().flatMap(de -> drawableTooltipProvider.getDrawableTooltip(de.getScheduleEvent())).ifPresent(dt -> {
			final Rectangle bs = canvasState.getHoveredEvent().get().getBounds();
			final Point anchor = new Point(bs.x, bs.y);
			dt.setBounds(mainBounds);
			dt.setAnchor(anchor, settings.getEventHeight() * 5 / 4);
			drawer.drawOne(dt, resolver);
		});
		
		drawer.drawOne(dragSelectionZoomHandler.getDrawableSelectionRectangle(), resolver);
	}
	
	private int calculateRowHeaderWidth(DrawerQueryResolver resolver) {
		final var systemFont = Display.getDefault().getSystemFont();
		final int maxTextWidth = canvasState.getRows().stream().map(r -> r.getName() == null ? 0 : resolver.findSizeOfText(r.getName(), systemFont).x).max(Integer::compare).orElse(0);
		final int maxTextHeaderWidth = maxTextWidth + settings.getRowHeaderLeftPadding() + settings.getRowHeaderRightPadding();
		return Math.max(settings.getMinimumRowHeaderWidth(), maxTextHeaderWidth);
	}

	private List<DrawableScheduleChartRow> getDrawableRows(DrawerQueryResolver r) {
		List<DrawableScheduleChartRow> res = new ArrayList<>();
		final Rectangle mainBounds = canvasState.getMainBounds();
		final int height = settings.getRowHeight();
		final int startY = mainBounds.y + drawableTimeScale.getTotalHeaderHeight();
		int i = 0, y = startY;
		for (final ScheduleChartRow scr : canvasState.getShownRows()) {
			DrawableScheduleChartRow drawableRow = new DrawableScheduleChartRow(scr, canvasState, i, timeScale, drawableEventProvider, eventStylingProvider, settings);
			drawableRow.setBounds(new Rectangle(mainBounds.x, y, mainBounds.width, height));
			res.add(drawableRow);
			i++;
			y += height;
		}
		return res;
	}
	
	private List<DrawableElement> getDrawableLabels(List<DrawableScheduleChartRow> drawnRows, DrawerQueryResolver r) {
		List<DrawableElement> labels = new ArrayList<>();
		for (final var row: drawnRows) {
			for (final var event: row.getLastDrawnEvents()) {
				labels.addAll(eventLabelProvider.getEventLabels(event, r));
			}
		}
		return labels;
	}

	@Override
	public boolean addScheduleEventListener(IScheduleChartEventListener l) {
		return listeners.add(l);
	}
	
	@Override
	public boolean removeScheduleEventListener(IScheduleChartEventListener l) {
		return listeners.remove(l);
	}
	
	@Override
	public void fireScheduleEvent(Consumer<IScheduleChartEventListener> f) {
		for (final IScheduleChartEventListener l: listeners) {
			f.accept(l);
		}
	}

	public final ScheduleTimeScale getTimeScale() {
		return timeScale;
	}

	public ScheduleFilterSupport getFilterSupport() {
		return filterSupport;
	}
	
	public void addRow(ScheduleChartRow r) {
		canvasState.getRows().add(r);
		recalculateContentBounds();
	}
	
	public void showHiddenRows() {
		canvasState.getHiddenRowKeys().clear();
		redraw();
	}
	
	public void addAll(List<ScheduleChartRow> rs) {
		canvasState.getRows().addAll(rs);
		recalculateContentBounds();
	}
	
	public void clear() {
		canvasState.getRows().clear();
		recalculateContentBounds();
	}

	private void recalculateContentBounds() {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		
		ScheduleEvent minEvent = null;
		ScheduleEvent maxEvent = null;

		for (final ScheduleChartRow row: canvasState.getShownRows()) {
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
		
		canvasState.setLeftMostEvent(minEvent);
		canvasState.setRightMostEvent(maxEvent);
	}

	// To be called only from the viewer
	public void setSelectedEvents(Collection<ScheduleEvent> newSelection) {
		// don't reassign so it keeps the references in the drawable rows

		for (final ScheduleEvent e: canvasState.getSelectedEvents()) {
			e.setSelectionState(ScheduleEventSelectionState.UNSELECTED);
		}

		canvasState.setSelectedEvents(newSelection);

		for (final ScheduleEvent e: newSelection) {
			e.setSelectionState(ScheduleEventSelectionState.SELECTED);
		}
		
		redraw();
	}

	public Optional<DrawableScheduleEvent> findEvent(int x, int y) {
		return findEvents(x, y, 0, 0).stream().findFirst();
	}
	
	public List<DrawableScheduleEvent> findEvents(int x, int y, int hRadius, int vRadius) {
		final List<DrawableScheduleChartRow> drawnRows = canvasState.getLastDrawnContent();
		List<DrawableScheduleEvent> found = new ArrayList<>();
		for (final DrawableScheduleChartRow row: drawnRows) {
			if (row.getBounds().contains(x, y)) {
				for (final DrawableScheduleEvent evt: row.getLastDrawnEvents()) {
					Rectangle b = evt.getBounds();
					Rectangle area = (hRadius == 0 && vRadius == 0) ? b : new Rectangle(b.x - hRadius, b.y - vRadius, b.width + 2 * hRadius, b.height + 2 * vRadius);
					if (area.contains(x, y) && evt.getScheduleEvent().isVisible()) {
						found.add(evt);
					}
				}
				
				if (vRadius == 0) {
					return found;
				}
			}
		}
		
		return found;
	}
	
	public Optional<DrawableScheduleChartRow> findRowHeader(int x, int y) {
		for (final DrawableScheduleChartRow row: canvasState.getLastDrawnContent()) {
			Rectangle b = row.getBounds();
			if (b.contains(new Point(b.x, y))) {
				if (x >= canvasState.getOriginalBounds().x && x <= canvasState.getMainBounds().x) {
					return Optional.of(row);
				}
			} else {
				Optional.empty();
			}
		}
		
		return Optional.empty();
	}

	public boolean clickedRowHeaderRegion(int x, int y) {
		return canvasState.getOriginalBounds().x <= x && x <= canvasState.getMainBounds().x;
	}

}
