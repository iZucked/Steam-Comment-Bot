/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class ScheduleReportTreeStructureAdvisorImpl extends TreeStructureAdvisor {
	@Override
	public Object getParent(final Object element) {
		if (element instanceof UserGroup) {
			return ((UserGroup) element).eContainer();
		}
		if (element instanceof CycleGroup) {
			return ((CycleGroup) element).getUserGroup();
		}
		// cycle group -> usergroup
		if (element instanceof Row) {
			return ((Row) element).getCycleGroup();
		}
		return null;
	}

	@Override
	public Boolean hasChildren(final Object element) {
		if (element instanceof Table && (((Table) element).getUserGroups().size() > 0)) {
			return Boolean.TRUE;
		}
		if (element instanceof UserGroup && (((UserGroup) element).getGroups().size() > 0)) {
			return Boolean.TRUE;
		}
		if (element instanceof CycleGroup && (((CycleGroup) element).getRows().size() > 0)) {
			return Boolean.TRUE;
		}
		return super.hasChildren(element);

	}
}