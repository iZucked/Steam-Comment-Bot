/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class NinetyDayScheduleLabelMenuAction extends NinetyDayScheduleAction {
	
	private NinetyDayDrawableEventLabelProvider labelProvider;

	protected NinetyDayScheduleLabelMenuAction(NinetyDayScheduleReport parent, NinetyDayDrawableEventLabelProvider labelProvider) {
		super("Label", AS_DROP_DOWN_MENU, parent);
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Label, IconMode.Enabled));
		
		this.labelProvider = labelProvider;
	}

	@Override
	protected void createMenuItems(Menu menu) {
		{
			Action canalAction = new Action("Canals", IAction.AS_CHECK_BOX) {
				@Override
				public void run() {
					labelProvider.toggleShowCanals();
					setChecked(labelProvider.showCanals());
					parent.redraw();
				}
			};
			canalAction.setChecked(labelProvider.showCanals());
			final ActionContributionItem aci = new ActionContributionItem(canalAction);
			aci.fill(menu, -1);
		}
		{
			Action destinationLabelsAction = new Action("Cargoes", IAction.AS_CHECK_BOX) {

				@Override
				public void run() {
					labelProvider.toggleShowDestinationLabels();
					setChecked(labelProvider.showDestinationLabels());
					parent.redraw();
				}
			};
			destinationLabelsAction.setChecked(labelProvider.showDestinationLabels());
			final ActionContributionItem aci = new ActionContributionItem(destinationLabelsAction);
			aci.fill(menu, -1);
		}
		{
			final Action showDaysOnEventsAction = new Action("Days", IAction.AS_CHECK_BOX) {
				@Override
				public void run() {
					labelProvider.toggleShowDays();
					setChecked(labelProvider.showDays());
					parent.redraw();
				}
			};
			showDaysOnEventsAction.setChecked(labelProvider.showDays());
			final ActionContributionItem aci = new ActionContributionItem(showDaysOnEventsAction);
			aci.fill(menu, -1);
		}
		
	}

}
