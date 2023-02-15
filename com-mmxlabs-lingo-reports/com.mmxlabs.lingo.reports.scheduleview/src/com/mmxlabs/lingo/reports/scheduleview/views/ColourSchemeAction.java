/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.nebula.widgets.ganttchart.plaque.DayAndHoursAsTextContentProvider;
import org.eclipse.nebula.widgets.ganttchart.plaque.DaysContentProvider;
import org.eclipse.nebula.widgets.ganttchart.plaque.IPlaqueContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

class ColourSchemeAction extends SchedulerViewAction {

	public enum ShowDaysOption {
		OFF, DECIMAL, HOURS_AND_DAYS
	}
	private ShowDaysOption selectedShowDaysOption = ShowDaysOption.OFF;

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
		{
			final Action canalAction = new Action("Show canals", IAction.AS_CHECK_BOX) {
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
			final Action showDaysOnEventsAction = new DefaultMenuCreatorAction("Show days") {
				
				@Override
				protected void populate(Menu menu) {
					final Action off = new Action("off") {
						@Override
						public void run() {
							final boolean b = viewer.getGanttChart().getGanttComposite().isShowingDaysOnEvents();
							if (b) {
								viewer.getGanttChart().getGanttComposite().setShowDaysOnEvents(!b);
								viewer.getGanttChart().getGanttComposite().redraw();
							}
							selectedShowDaysOption = ShowDaysOption.OFF;
						}
					};

					final Action oneDp = new Action("\"1.5\"") {
						@Override
						public void run() {
							final boolean b = viewer.getGanttChart().getGanttComposite().isShowingDaysOnEvents();
							if (!b) {
								final List<@NonNull IPlaqueContentProvider> newContentProviders = new ArrayList<>(2);
								newContentProviders.add(new DaysContentProvider(1));
								newContentProviders.add(new DaysContentProvider(0));
								viewer.getGanttChart().getGanttComposite().replacePlaqueContentProviders(newContentProviders);
								viewer.getGanttChart().getGanttComposite().setShowDaysOnEvents(true);
								viewer.getGanttChart().getGanttComposite().redraw();
							} else {
								final List<@NonNull IPlaqueContentProvider> newContentProviders = new ArrayList<>(2);
								newContentProviders.add(new DaysContentProvider(1));
								newContentProviders.add(new DaysContentProvider(0));
								viewer.getGanttChart().getGanttComposite().replacePlaqueContentProviders(newContentProviders);
								viewer.getGanttChart().getGanttComposite().redraw();
							}
							selectedShowDaysOption = ShowDaysOption.DECIMAL;
						}
					};
					final Action daysAndHoursText = new Action("\"1d 12h\"") {
						@Override
						public void run() {
							final boolean b = viewer.getGanttChart().getGanttComposite().isShowingDaysOnEvents();
							if (!b) {
								final List<@NonNull IPlaqueContentProvider> newContentProviders = new ArrayList<>(2);
								newContentProviders.add(new DayAndHoursAsTextContentProvider());
								newContentProviders.add(new DaysContentProvider(0));
								viewer.getGanttChart().getGanttComposite().replacePlaqueContentProviders(newContentProviders);
								viewer.getGanttChart().getGanttComposite().setShowDaysOnEvents(true);
								viewer.getGanttChart().getGanttComposite().redraw();
							} else {
								final List<@NonNull IPlaqueContentProvider> newContentProviders = new ArrayList<>(2);
								newContentProviders.add(new DayAndHoursAsTextContentProvider());
								newContentProviders.add(new DaysContentProvider(0));
								viewer.getGanttChart().getGanttComposite().replacePlaqueContentProviders(newContentProviders);
								viewer.getGanttChart().getGanttComposite().redraw();
							}
							selectedShowDaysOption = ShowDaysOption.HOURS_AND_DAYS;
						}
					};
					if (selectedShowDaysOption != null) {
						switch (selectedShowDaysOption) {
						case OFF:
							off.setChecked(true);
							break;
						case DECIMAL:
							oneDp.setChecked(true);
							break;
						case HOURS_AND_DAYS:
							daysAndHoursText.setChecked(true);
							break;
						default:
							break;
						}
					}
					addActionToMenu(off, menu);
					addActionToMenu(oneDp, menu);
					addActionToMenu(daysAndHoursText, menu);
				}

			};

			showDaysOnEventsAction.setChecked(selectedShowDaysOption != null && selectedShowDaysOption != ShowDaysOption.OFF);
			final ActionContributionItem actionContributionItem = new ActionContributionItem(showDaysOnEventsAction);
			actionContributionItem.fill(menu, -1);
		}
		{
			final Action toggleShowNominalsByDefaultAction = new Action("Show nominals", SWT.CHECK) {
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
	}
}