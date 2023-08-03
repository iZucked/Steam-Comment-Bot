package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventProvider;
import com.mmxlabs.widgets.schedulechart.viewer.ScheduleChartViewer;

public class NinetyDayScheduleReport extends ViewPart {

	private Action zoomInAction;
	private Action zoomOutAction;
	
	private ScheduleChartViewer<Integer> viewer;
	private final IScheduleEventProvider<Integer> eventProvider = new IScheduleEventProvider<>() {

		@Override
		public List<ScheduleEvent> getEvents(Integer input) {
			return List.of(new ScheduleEvent(LocalDateTime.now(), LocalDateTime.now().plusDays(3)));
		}

		@Override
		public String getKeyForEvent(ScheduleEvent event) {
			return "first event";
		}
	};
	
	@Override
	public void createPartControl(Composite parent) {
		
		viewer = new ScheduleChartViewer(parent, eventProvider);
		viewer.typedSetInput(0);
		
		
		makeActions();
		contributeToActionBars();
	}

	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(zoomInAction);
		manager.add(zoomOutAction);
	}

	private void makeActions() {
		zoomInAction = new ZoomAction(true, viewer.getCanvas());
		zoomOutAction = new ZoomAction(false, viewer.getCanvas());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private static class ZoomAction extends Action {
		
		private final boolean zoomIn;
		private final ScheduleCanvas canvas;
		
		public ZoomAction(boolean zoomIn, ScheduleCanvas canvas) {
			super();
			this.zoomIn = zoomIn;
			this.canvas = canvas;

			setText(zoomIn ? "Zoom In" : "Zoom Out");
			CommonImages.setImageDescriptors(this, zoomIn ? IconPaths.ZoomIn : IconPaths.ZoomOut);
		}
		
		@Override
		public void run() {
			if (zoomIn) {
				canvas.getTimeScale().zoomIn();
			} else {
				canvas.getTimeScale().zoomOut();
			}
			canvas.redraw();
		}
	}

}
