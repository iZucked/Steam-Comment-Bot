package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ReentrantSelectionManager;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventProvider;
import com.mmxlabs.widgets.schedulechart.viewer.ScheduleChartViewer;

public class NinetyDayScheduleReport extends ViewPart {

	private Action zoomInAction;
	private Action zoomOutAction;
	private Action packAction;
	
	private ScheduleChartViewer<ScheduleModel> viewer;
	private final IScheduleEventProvider<Integer> oldEventProvider = new IScheduleEventProvider<>() {

		@Override
		public List<ScheduleEvent> getEvents(Integer input) {
			List<ScheduleEvent> res = new ArrayList<>();
			LocalDateTime curr = LocalDateTime.now();
			for (int i = 0; i < 10; i++) {
				LocalDateTime next = curr.plusDays(ThreadLocalRandom.current().nextInt(1, 10));
				res.add(new ScheduleEvent(curr, next));
				curr = next;
			}
			return res;
		}

		@Override
		public String getKeyForEvent(ScheduleEvent event) {
			return event.toString();
		}
	};
	
	private final NinetyDayScheduleEventProvider eventProvider = new NinetyDayScheduleEventProvider();
	private final NinetyDayScheduleEventStylingProvider eventStylingProvider = new NinetyDayScheduleEventStylingProvider();
	
	private @Nullable ScenarioComparisonService scenarioComparisonService;
	private ReentrantSelectionManager selectionManager;
	protected final ISelectedScenariosServiceListener scenariosServiceListener = new ISelectedScenariosServiceListener() {
		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {
			ViewerHelper.runIfViewerValid(viewer, block, () -> {
				if (selectedDataProvider != null) {
					List<ScenarioResult> rs = selectedDataProvider.getAllScenarioResults();
					if (rs.isEmpty()) return;
					ScheduleModel sm = rs.get(0).getTypedResult(ScheduleModel.class);
					if (sm != null) {
						viewer.typedSetInput(sm);
					}
				}
			});
		}
	};

	@Override
	public void createPartControl(Composite parent) {
		
		viewer = new ScheduleChartViewer<>(parent, eventProvider, eventStylingProvider);
		this.scenarioComparisonService = getSite().getService(ScenarioComparisonService.class);
		selectionManager = new ReentrantSelectionManager(viewer, scenariosServiceListener, scenarioComparisonService);
		
		
		makeActions();
		contributeToActionBars();
		
		
		try {
			scenarioComparisonService.triggerListener(scenariosServiceListener, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(zoomInAction);
		manager.add(zoomOutAction);
		manager.add(packAction);
	}

	private void makeActions() {
		zoomInAction = new ZoomAction(true, viewer.getCanvas());
		zoomOutAction = new ZoomAction(false, viewer.getCanvas());
		packAction = new PackAction(viewer.getCanvas());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void dispose() {
		if (scenarioComparisonService != null) {
			scenarioComparisonService.removeListener(scenariosServiceListener);
		}
		super.dispose();
	}
	
	private static class ZoomAction extends Action {
		
		private final boolean zoomIn;
		private final ScheduleCanvas canvas;
		
		public ZoomAction(final boolean zoomIn, final ScheduleCanvas canvas) {
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
	
	private static class PackAction extends Action {
		private final ScheduleCanvas canvas;
		
		public PackAction(final ScheduleCanvas canvas) {
			super();
			this.canvas = canvas;

			setText("Fit");
			CommonImages.setImageDescriptors(this, IconPaths.Pack);
		}
		
		@Override
		public void run() {
			canvas.getTimeScale().pack();
			canvas.redraw();
		}
		
	}
	

}
