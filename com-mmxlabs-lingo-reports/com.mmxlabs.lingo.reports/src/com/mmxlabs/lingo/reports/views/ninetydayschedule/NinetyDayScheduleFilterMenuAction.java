/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;

public class NinetyDayScheduleFilterMenuAction extends NinetyDayScheduleAction {
	
	private final ScheduleCanvas canvas;

	protected NinetyDayScheduleFilterMenuAction(NinetyDayScheduleReport parent, ScheduleCanvas canvas) {
		super("Filter", AS_DROP_DOWN_MENU, parent);
		this.canvas = canvas;
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Filter, IconMode.Enabled));
	}

	@Override
	protected void createMenuItems(Menu menu) {
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

}
