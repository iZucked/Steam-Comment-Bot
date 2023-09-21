/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;
import com.mmxlabs.widgets.schedulechart.ScheduleChartMode;
import com.mmxlabs.widgets.schedulechart.ScheduleFilterSupport;

public class NinetyDayScheduleFilterMenuAction extends NinetyDayScheduleAction {
	
	private static final String NOMINALS_FILTER = "NOMINALS";
	
	private final ScheduleCanvas canvas;
	private final NinetyDayScheduleChartSettings settings;
	private ScheduleChartMode preToggleMode;


	protected NinetyDayScheduleFilterMenuAction(NinetyDayScheduleReport parent, ScheduleCanvas canvas, NinetyDayScheduleChartSettings settings) {
		super("Filter", AS_DROP_DOWN_MENU, parent);
		this.canvas = canvas;
		this.settings = settings;
		this.preToggleMode = canvas.getScheduleChartMode();
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Filter, IconMode.Enabled));
	}
	
	@Override
	public void run() {
		ScheduleChartMode temp = canvas.getScheduleChartMode();
		canvas.setScheduleChartMode(canvas.getScheduleChartMode() == ScheduleChartMode.FILTER ? preToggleMode : ScheduleChartMode.FILTER);
		if (temp != ScheduleChartMode.FILTER) {
			preToggleMode = temp;
			setChecked(true);
		} else {
			setChecked(false);
		}
		canvas.redraw();
	}

	@Override
	protected void createMenuItems(Menu menu) {
		Action showNominals = new Action("Show Nominals", AS_CHECK_BOX) {
			@Override
			public void run() {
				ScheduleFilterSupport fs = canvas.getFilterSupport();
				boolean isShowingNominals = !fs.isFilterActive(NOMINALS_FILTER);
				if (isShowingNominals) {
					fs.applyFilter(NOMINALS_FILTER, key -> key.getData() instanceof Sequence s && s.getSequenceType() == SequenceType.ROUND_TRIP);
				} else {
					fs.removeFilter(NOMINALS_FILTER);
				}
				setChecked(!fs.isFilterActive(NOMINALS_FILTER));
				canvas.redraw();
			}
		};
		showNominals.setChecked(!canvas.getFilterSupport().isFilterActive(NOMINALS_FILTER));
		ActionContributionItem showNominalsACI = new ActionContributionItem(showNominals);
		showNominalsACI.fill(menu, -1);

		final Separator separator = new Separator();
		separator.fill(menu, -1);

		Action showHiddenRows = new Action("Show Hidden Rows", AS_PUSH_BUTTON) {
			@Override
			public void run() {
				canvas.getFilterSupport().showHiddenRows();
				setEnabled(canvas.getFilterSupport().isFiltered());
				canvas.redraw();
			}
		};
		showHiddenRows.setEnabled(canvas.getFilterSupport().isFiltered());
		ActionContributionItem showHiddenRowsACI = new ActionContributionItem(showHiddenRows);
		showHiddenRowsACI.fill(menu, -1);

	}
	
	public void applyDefaultFilters() {
		if (!settings.showNominalsByDefault()) {
			canvas.getFilterSupport().addDefaultFilter(NOMINALS_FILTER, key -> key.getData() instanceof Sequence s && s.getSequenceType() == SequenceType.ROUND_TRIP);
		}
	}

}
