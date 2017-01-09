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

class HighlightAction extends SchedulerViewAction {

	public HighlightAction(SchedulerView schedulerView, GanttChartViewer viewer, final EMFScheduleLabelProvider lp) {
		super("Highlight", IAction.AS_DROP_DOWN_MENU, schedulerView, viewer, lp);
		setImageDescriptor(Activator.getImageDescriptor("/icons/highlight.gif"));
	}
	
//	@Override
//	public void run() {
//	
//		final List<IScheduleViewColourScheme> colourSchemes = lp.getColourSchemes();
//		final IScheduleViewColourScheme currentScheme = lp.getCurrentScheme();
//		int nextIdx = -1;
//		if (currentScheme != null) {
//			nextIdx = colourSchemes.indexOf(currentScheme);
//			nextIdx = (nextIdx + 1) % colourSchemes.size();
//		}
//		if (nextIdx != -1) {
//			lp.setScheme(colourSchemes.get(nextIdx));
//		}
//	
//		viewer.setInput(viewer.getInput());
//		schedulerView.redraw();
//	}

	@Override
	protected void createMenuItems(final Menu menu) {
		
		final List<IScheduleViewColourScheme> highlighters = lp.getHighlighters();
	
		for (final IScheduleViewColourScheme highlighter : highlighters) {
	
			final Action a = new Action(highlighter.getName(), IAction.AS_CHECK_BOX) {	
	
				@Override
				public void run() {
					lp.toggleHighlighter(highlighter);
					setChecked(lp.isActive(highlighter));
					viewer.setInput(viewer.getInput());
					schedulerView.redraw();
				}
			};
	
			// a.setActionDefinitionId(mode.toString());
			final ActionContributionItem actionContributionItem = new ActionContributionItem(a);
			actionContributionItem.fill(menu, -1);
	
			// Set initially checked item.
			if (lp.isActive(highlighter)) {
				a.setChecked(true);
			}
		}
				
	}
}