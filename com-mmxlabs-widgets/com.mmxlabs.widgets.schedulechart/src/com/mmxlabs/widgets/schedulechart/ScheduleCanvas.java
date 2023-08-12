package com.mmxlabs.widgets.schedulechart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

import com.mmxlabs.common.Pair;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DefaultScheduleChartColourScheme;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRowHeaders;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleTimeScale;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;
import com.mmxlabs.widgets.schedulechart.draw.GCBasedScheduleElementDrawer;
import com.mmxlabs.widgets.schedulechart.draw.ScheduleElementDrawer;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public class ScheduleCanvas extends Canvas implements IScheduleChartContentBoundsProvider, IScheduleChartEventEmitter {
	
	private final ScheduleTimeScale timeScale;
	private final DrawableScheduleTimeScale<ScheduleTimeScale> drawableTimeScale;

	private final HorizontalScrollbarHandler horizontalScrollbarHandler;
	private final DragSelectionZoomHandler dragSelectionZoomHandler;
	private final EventSelectionHandler eventSelectionHandler;
	
	private final IScheduleChartSettings settings;
	private final IScheduleChartColourScheme colourScheme;
	private final IScheduleEventStylingProvider eventStylingProvider;
	
	private final List<IScheduleChartEventListener> listeners = new ArrayList<>();

	private Rectangle mainBounds;
	private List<ScheduleChartRow> rows = new ArrayList<>();
	
	private ScheduleEvent leftmostEvent;
	private ScheduleEvent rightmostEvent;
	
	private Set<ScheduleEvent> selectedEvents = new HashSet<>();
	private List<DrawableScheduleChartRow> lastDrawnContent;

	public ScheduleCanvas(Composite parent, IScheduleEventStylingProvider eventStylingProvider) {
		this(parent, eventStylingProvider, new DefaultScheduleChartSettings(), new DefaultScheduleChartColourScheme());
	}

	public ScheduleCanvas(Composite parent, IScheduleEventStylingProvider eventStylingProvider, IScheduleChartSettings settings, IScheduleChartColourScheme colourScheme) {
		super(parent, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED | SWT.V_SCROLL | SWT.H_SCROLL);
		this.settings = settings;
		this.colourScheme = colourScheme;

		this.timeScale = new ScheduleTimeScale(this, this, settings);
		this.drawableTimeScale = new DrawableScheduleTimeScale<>(timeScale, colourScheme, settings);
		
		this.horizontalScrollbarHandler = new HorizontalScrollbarHandler(getHorizontalBar(), timeScale);
		this.dragSelectionZoomHandler = new DragSelectionZoomHandler(this, colourScheme);
		this.eventSelectionHandler = new EventSelectionHandler(this);
		
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
		
		final int verticalScroll = getVerticalBar().isVisible() ? getVerticalBar().getSelection() : 0;
		Rectangle bounds = new Rectangle(originalBounds.x, originalBounds.y - verticalScroll, originalBounds.width, originalBounds.height + verticalScroll);
		
		final int rowHeaderWidth = calculateRowHeaderWidth(resolver);
		mainBounds = new Rectangle(bounds.x + rowHeaderWidth, bounds.y, bounds.width - rowHeaderWidth, bounds.height);

		timeScale.setBounds(mainBounds);
		drawableTimeScale.setBounds(mainBounds);
		drawableTimeScale.setLockedHeaderY(originalBounds.y);

		drawer.drawOne(drawableTimeScale.getGrid(mainBounds.y + drawableTimeScale.getTotalHeaderHeight()), resolver);
		lastDrawnContent = getDrawableRows(resolver);
		lastDrawnContent.forEach(r -> drawer.drawOne(r, resolver));
		drawer.drawOne(drawableTimeScale.getHeaders(), resolver);
		
		var rowHeader = new DrawableScheduleChartRowHeaders(rows, drawableTimeScale.getTotalHeaderHeight(), colourScheme, settings);
		rowHeader.setBounds(new Rectangle(originalBounds.x, mainBounds.y + drawableTimeScale.getTotalHeaderHeight(), rowHeaderWidth, mainBounds.height));
		rowHeader.setLockedHeaderY(originalBounds.y);
		drawer.drawOne(rowHeader, resolver);
		
		drawer.drawOne(dragSelectionZoomHandler.getDrawableSelectionRectangle(), resolver);
	}
	
	private int calculateRowHeaderWidth(DrawerQueryResolver resolver) {
		final int maxTextWidth = rows.stream().map(r -> r.getName() == null ? 0 : resolver.findSizeOfText(r.getName()).x).max(Integer::compare).orElse(0) + settings.getRowHeaderRightPadding();
		return Math.max(settings.getMinimumRowHeaderWidth(), maxTextWidth);
	}

	private List<DrawableScheduleChartRow> getDrawableRows(DrawerQueryResolver resolver) {
		List<DrawableScheduleChartRow> res = new ArrayList<>();
		final int height = settings.getRowHeight();
		final int startY = mainBounds.y + drawableTimeScale.getTotalHeaderHeight();
		for (int i = 0, y = startY; i < rows.size(); i++, y += height  + 1) {
			DrawableScheduleChartRow drawableRow = new DrawableScheduleChartRow(rows.get(i), i, timeScale, colourScheme, eventStylingProvider, settings, selectedEvents);
			drawableRow.setBounds(new Rectangle(mainBounds.x, y, mainBounds.width, height));
			res.add(drawableRow);
		}
		return res;
	}

	public boolean addScheduleEventListener(IScheduleChartEventListener l) {
		return listeners.add(l);
	}
	
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

	public Collection<@NonNull ScheduleEvent> getSelectedEvents() {
		return selectedEvents;
	}

	// To be called only from the viewer
	public void setSelectedEvents(Collection<ScheduleEvent> newSelection) {
		// don't reassign so it keeps the references in the drawable rows

		for (final ScheduleEvent e: selectedEvents) {
			e.setSelected(false);
		}

		this.selectedEvents.clear();
		this.selectedEvents.addAll(newSelection);

		for (final ScheduleEvent e: newSelection) {
			e.setSelected(true);
		}
		
		redraw();
	}

	public List<DrawableScheduleChartRow> getLastDrawnContent() {
		return lastDrawnContent;
	}

}
