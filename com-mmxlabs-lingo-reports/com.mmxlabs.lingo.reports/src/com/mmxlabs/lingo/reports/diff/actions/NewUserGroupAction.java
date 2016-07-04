/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.actions;

import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class NewUserGroupAction extends AbstractDiffAction {

	private Table table;

	public NewUserGroupAction() {
		super("New Group");
		setEnabled(false);
	}

	public void setTable(final Table table) {
		this.table = table;
		setEnabled(table != null);
	}

	@Override
	public void run() {

		if (table != null) {
			final UserGroup group = ScheduleReportFactory.eINSTANCE.createUserGroup();
			group.setComment("New Group");
			table.getUserGroups().add(group);
		}
	}
}
