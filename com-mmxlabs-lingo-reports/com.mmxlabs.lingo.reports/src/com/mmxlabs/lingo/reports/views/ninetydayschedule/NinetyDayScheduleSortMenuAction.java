/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Menu;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class NinetyDayScheduleSortMenuAction extends NinetyDayScheduleAction {

	private final NinetyDayScheduleChartSortingProvider comparator;
	
	protected NinetyDayScheduleSortMenuAction(final NinetyDayScheduleReport parent, final NinetyDayScheduleChartSortingProvider comparator) {
		super("Sort", AS_DROP_DOWN_MENU, parent);
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Sort, IconMode.Enabled));
		this.comparator = comparator;
	}

	@Override
	protected void createMenuItems(Menu menu) {
		for (final NinetyDayScheduleChartSortingProvider.Mode mode : NinetyDayScheduleChartSortingProvider.Mode.values()) {

			final Action a = new Action(mode.getDisplayName(), IAction.AS_RADIO_BUTTON) {
				@Override
				public void run() {
					comparator.setMode(mode);
					parent.redraw();
				}
			};
			a.setToolTipText(mode.getTooltip());

			final ActionContributionItem actionContributionItem = new ActionContributionItem(a);
			actionContributionItem.fill(menu, -1);

			// Set initially checked item.
			if(comparator.getMode() == mode) {
				a.setChecked(true);
			}
		}

		{
			final Separator actionContributionItem = new Separator();
			actionContributionItem.fill(menu, -1);
		}
		for (final NinetyDayScheduleChartSortingProvider.Category category : NinetyDayScheduleChartSortingProvider.Category.values()) {

			final Action a = new Action(category.getDisplayName(), IAction.AS_RADIO_BUTTON) {
				@Override
				public void run() {
					comparator.setCategory(category);
					parent.redraw();
				}
			};
			a.setToolTipText(category.getTooltip());

			final ActionContributionItem actionContributionItem = new ActionContributionItem(a);
			actionContributionItem.fill(menu, -1);

			// Set initially checked item.
			if(comparator.getCategory() == category) {
				a.setChecked(true);
			}
		}
		
	}

}
