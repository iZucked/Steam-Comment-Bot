/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;


import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.lingo.reports.scheduleview.internal.Activator;

class ColourSchemeAction extends SchedulerViewAction {

	public ColourSchemeAction(SchedulerView schedulerView, final EMFScheduleLabelProvider lp, GanttChartViewer viewer) {
		super("Colour Scheme", IAction.AS_DROP_DOWN_MENU, schedulerView, viewer, lp);
		setImageDescriptor(Activator.getImageDescriptor("/icons/colour_scheme.gif"));
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
		
		final List<IScheduleViewColourScheme> colourSchemes = lp.getColourSchemes();
	
		for (final IScheduleViewColourScheme scheme : colourSchemes) {
	
			final Action a = new Action(scheme.getName(), IAction.AS_RADIO_BUTTON) {
	
	
				@Override
				public void run() {
					lp.setScheme(scheme);
					viewer.setInput(viewer.getInput());
					schedulerView.redraw();
				}
			};
	
			// a.setActionDefinitionId(mode.toString());
			final ActionContributionItem actionContributionItem = new ActionContributionItem(a);
			actionContributionItem.fill(menu, -1);
	
			// Set initially checked item.
			if (lp.getCurrentScheme() == scheme) {
				a.setChecked(true);
			}
		}
		
		final Action canalAction = new Action("Show Canals", IAction.AS_CHECK_BOX) {
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
}