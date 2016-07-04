/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.actions;

import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class DeleteDiffAction extends AbstractDiffAction {

	public DeleteDiffAction() {
		super("Delete");
	}

	@Override
	public void run() {
		if (target instanceof UserGroup) {
			final UserGroup userGroup = (UserGroup) target;
			final Table table = (Table) userGroup.eContainer();
			if (table != null) {
				table.getCycleGroups().addAll(userGroup.getGroups());
				table.getUserGroups().remove(userGroup);
			}
//		} else if (target instanceof CycleGroup) {
//			final CycleGroup cycleGroup = (CycleGroup) target;
//			final Table table = (Table) cycleGroup.eContainer();
//			if (table != null) {
//				table.getCycleGroups().remove(cycleGroup);
//				cycleGroup.getRows().clear();
//				cycleGroup.setUserGroup(null);
//			}
//		} else if (target instanceof Row) {
//			final Row row = (Row) target;
//			final Table table = (Table) row.eContainer();
//			if (table != null) {
//				table.getRows().remove(row);
//				row.setCycleGroup(null);
//				row.setRowGroup(null);
//				row.setReferenceRow(null);
//				row.getReferringRows().clear();
//			}
		}

	}
}
