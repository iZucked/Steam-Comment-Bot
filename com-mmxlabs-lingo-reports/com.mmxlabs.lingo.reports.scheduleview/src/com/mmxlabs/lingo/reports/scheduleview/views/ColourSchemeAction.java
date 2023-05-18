/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.lingo.reports.scheduleview.views.positionssequences.ISchedulePositionsSequenceProvider;
import com.mmxlabs.lingo.reports.scheduleview.views.positionssequences.ISchedulePositionsSequenceProviderExtension;
import com.mmxlabs.lingo.reports.scheduleview.views.positionssequences.PositionsSequenceProviderException;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

class ColourSchemeAction extends SchedulerViewAction {
	
	public ColourSchemeAction(final SchedulerView schedulerView, final EMFScheduleLabelProvider lp, final GanttChartViewer viewer) {
		super("Label", IAction.AS_DROP_DOWN_MENU, schedulerView, viewer, lp);
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Label, IconMode.Enabled));
	}

	@Override
	public void run() {

		// cycle to next colour scheme
		final List<IScheduleViewColourScheme> colourSchemes = lp.getColourSchemes();
		final IScheduleViewColourScheme currentScheme = lp.getCurrentScheme();
		int nextIdx = -1;
		if (currentScheme != null) {
			nextIdx = colourSchemes.indexOf(currentScheme);
			nextIdx = (nextIdx + 1) % colourSchemes.size();
		}
		if (nextIdx != -1) {
			lp.setScheme(colourSchemes.get(nextIdx));
		}

		viewer.setInput(viewer.getInput());
		schedulerView.redraw();
	}

	@Override
	protected void createMenuItems(final Menu menu) {
		Action canalAction;
		Action destinationLabelsAction;
		{
			canalAction = new Action("Canals", IAction.AS_CHECK_BOX) {
				@Override
				public void run() {
					lp.toggleShowCanals();
					setChecked(lp.showCanals());
					viewer.setInput(viewer.getInput());
					schedulerView.redraw();
				}
			};
			canalAction.setChecked(lp.showCanals());
			final ActionContributionItem actionContributionItem = new ActionContributionItem(canalAction);
			actionContributionItem.fill(menu, -1);
		}
		{
			destinationLabelsAction = new Action("Cargoes", IAction.AS_CHECK_BOX) {

				@Override
				public void run() {
					lp.toggleShowDestinationLabels();
					setChecked(lp.showDestinationLabels());
					viewer.setInput(viewer.getInput());
					schedulerView.redraw();
				}
			};
			destinationLabelsAction.setChecked(lp.showDestinationLabels());

			final ActionContributionItem aci = new ActionContributionItem(destinationLabelsAction);
			aci.fill(menu, -1);
		}
		{
			final Action showDaysOnEventsAction = new Action("Days", IAction.AS_CHECK_BOX) {
				@Override
				public void run() {
					lp.toggleShowDays();
					setChecked(lp.showDays());
					viewer.setInput(viewer.getInput());
					schedulerView.redraw();
				}
			};
			showDaysOnEventsAction.setChecked(lp.showDays());
			final ActionContributionItem actionContributionItem = new ActionContributionItem(showDaysOnEventsAction);
			actionContributionItem.fill(menu, -1);
		}
		{
			final Separator separator = new Separator();
			separator.fill(menu, -1);
		}
		{
			final Action toggleShowNominalsByDefaultAction = new Action("Nominals", SWT.CHECK) {
				@Override
				public void run() {
					// TODO: Tidy all this state up.
					schedulerView.showNominalsByDefault = !schedulerView.showNominalsByDefault;
					schedulerView.contentProvider.showNominalsByDefault = schedulerView.showNominalsByDefault;
					setChecked(schedulerView.showNominalsByDefault);
					schedulerView.refresh();
				}
			};
			toggleShowNominalsByDefaultAction.setToolTipText("Toggles always show nominal cargoes or only when selected");
			toggleShowNominalsByDefaultAction.setChecked(schedulerView.showNominalsByDefault);
			final ActionContributionItem actionContributionItem = new ActionContributionItem(toggleShowNominalsByDefaultAction);
			actionContributionItem.fill(menu, -1);
		}
		
		Map<String, Optional<PositionsSequenceProviderException>> enabled = schedulerView.contentProvider.enabledPositionsSequenceProviders;

		for (ISchedulePositionsSequenceProviderExtension ext: schedulerView.positionsSequenceProviderExtensions) {
			if (ext.showMenuItem().equals("true")) {
				ISchedulePositionsSequenceProvider provider = ext.createInstance();
				final Action toggleShowPartition = new Action(ext.getName(), SWT.CHECK) {
					@Override
					public void run() {
						if (enabled.containsKey(provider.getId())) {
							var optError = enabled.get(provider.getId());
							if (optError.isPresent()) {
								PositionsSequenceProviderException e = optError.get();
								MessageDialog dialog = new MessageDialog(menu.getShell(), e.getTitle(), null, e.getDescription(), 0, 0, "OK");
								dialog.create();
								dialog.open();
								return;
							}
							enabled.remove(provider.getId());
						} else {
							enabled.put(provider.getId(), Optional.empty());
						}
						setChecked(schedulerView.contentProvider.isProviderEnabledWithNoError(provider.getId()));
						schedulerView.refresh();
					}
				};
				toggleShowPartition.setToolTipText("Partitions the unshipped cargoes based on a given split when selected");
				toggleShowPartition.setChecked(schedulerView.contentProvider.isProviderEnabledWithNoError(provider.getId()));
				final ActionContributionItem actionContributionItem = new ActionContributionItem(toggleShowPartition);
				actionContributionItem.fill(menu, -1);
			}
		}
	}

}