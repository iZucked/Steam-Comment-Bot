package com.mmxlabs.lingo.reports.diff;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class MergeAction extends Action {

	private final IStructuredSelection selection;
	private final Table table;

	public MergeAction(final IStructuredSelection selection, final Table table) {
		super("Merge");
		this.selection = selection;
		this.table = table;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		final Set<UserGroup> userGroups = new HashSet<>();
		final Set<CycleGroup> cycleGroups = new HashSet<>();
		final Iterator<Object> itr = selection.iterator();
		UserGroup firstUserGroup = null;
		while (itr.hasNext()) {
			final Object o = itr.next();
			if (o instanceof UserGroup) {
				final UserGroup userGroup = (UserGroup) o;
				userGroups.add(userGroup);
				if (firstUserGroup == null) {
					firstUserGroup = userGroup;
				}
			} else if (o instanceof CycleGroup) {
				final CycleGroup cycleGroup = (CycleGroup) o;
				final UserGroup userGroup = cycleGroup.getUserGroup();
				if (userGroup != null) {
					userGroups.add(userGroup);
					if (firstUserGroup == null) {
						firstUserGroup = userGroup;
					}
				}
			} else if (o instanceof Row) {
				final Row row = (Row) o;
				final CycleGroup cycleGroup = row.getCycleGroup();
				if (cycleGroup != null) {
					cycleGroups.add(cycleGroup);
					final UserGroup userGroup = cycleGroup.getUserGroup();
					if (userGroup != null) {
						userGroups.add(userGroup);
						if (firstUserGroup == null) {
							firstUserGroup = userGroup;
						}
					}
				}
			}
		}

		// Get or create user group
		if (firstUserGroup == null) {
			firstUserGroup = ScheduleReportFactory.eINSTANCE.createUserGroup();
			firstUserGroup.setComment("New Group");
			table.getUserGroups().add(firstUserGroup);
		}

		// Move stuff around.
		for (final UserGroup userGroup : userGroups) {
			if (userGroup != firstUserGroup) {
				// Move cycle groups across
				for (final CycleGroup cycleGroup : new ArrayList<>(userGroup.getGroups())) {
					cycleGroup.setUserGroup(firstUserGroup);
					// Remove from set of groups not yet processed
					cycleGroups.remove(cycleGroup);
				}
				table.getUserGroups().remove(userGroup);
			}
		}
		for (final CycleGroup cycleGroup : cycleGroups) {
			if (cycleGroup.getUserGroup() == firstUserGroup) {
				continue;
			}
			cycleGroup.setUserGroup(firstUserGroup);
		}
	}
}
