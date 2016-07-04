/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.actions;

import java.util.Iterator;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class ExtractCycleGroupAction extends AbstractDiffAction {

	public ExtractCycleGroupAction() {
		super("Extract");
	}

	@Override
	public void run() {
		final Iterator<?> itr = selection.iterator();
		while (itr.hasNext()) {
			final Object target = itr.next();
			if (target instanceof CycleGroup) {
				final CycleGroup cycleGroup = (CycleGroup) target;
				final UserGroup userGroup = cycleGroup.getUserGroup();
				if (userGroup != null) {
					final Table table = (Table) userGroup.eContainer();
					if (table != null) {
						table.getCycleGroups().add(cycleGroup);
						if (userGroup.getGroups().isEmpty()) {
							table.getUserGroups().remove(userGroup);
						}
					}
				}
			}
		}
	}
}
