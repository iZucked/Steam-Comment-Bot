/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.views.schedule.model.ChangeType;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

/**
 * Utils class to help with creating and merging {@link CycleGroup} objects.
 * 
 * @author Simon Goodall
 * 
 */
public class CycleGroupUtils {

	public static CycleGroup createOrReturnCycleGroup(final Table table, final Row row) {
		CycleGroup cycleGroup = row.getCycleGroup();
		if (cycleGroup == null) {
			cycleGroup = ScheduleReportFactory.eINSTANCE.createCycleGroup();
			table.getCycleGroups().add(cycleGroup);
			// row.setCycleGroup(cycleGroup);
			setRowCycleGroup(row, cycleGroup);
		}
		return cycleGroup;
	}

	private static void setRowCycleGroup(final Row row, final CycleGroup cycleGroup) {
		row.setCycleGroup(cycleGroup);
		if (row.getReferenceRow() != null) {
			row.getReferenceRow().setCycleGroup(cycleGroup);
		} else {
			for (final Row r : row.getReferringRows()) {
				r.setCycleGroup(cycleGroup);
			}
		}
	}

	public static void addToOrMergeCycleGroup(final Table table, final Row row, final CycleGroup cycleGroup) {
		if (row == null) {
			return;
		}
		final CycleGroup rowCycleGroup = row.getCycleGroup();
		if (rowCycleGroup == null) {
			setRowCycleGroup(row, cycleGroup);

		} else if (rowCycleGroup != cycleGroup) {
			setChangeType(cycleGroup, rowCycleGroup.getChangeType());
			setRowCycleGroup(row, cycleGroup);

			// Copy all rows into new cycle group.
			cycleGroup.getRows().addAll(rowCycleGroup.getRows());

			final UserGroup rowUserGroup = rowCycleGroup.getUserGroup();
			if (cycleGroup.getUserGroup() != rowUserGroup) {
				if (rowUserGroup != null && cycleGroup.getUserGroup() == null) {
					cycleGroup.setUserGroup(rowUserGroup);
					rowCycleGroup.setUserGroup(null);
				} else if (rowUserGroup != null && cycleGroup.getUserGroup() != null) {
					cycleGroup.getUserGroup().getGroups().addAll(rowUserGroup.getGroups());
				}
			}

			if (rowUserGroup != null) {
				if (cycleGroup.getUserGroup() != null) {

				}

			} else {
				table.getCycleGroups().remove(rowCycleGroup);
			}

		}
	}

	public static UserGroup createOrReturnUserGroup(final Table table, final CycleGroup cycleGroup) {
		UserGroup userGroup = cycleGroup.getUserGroup();
		if (userGroup == null) {
			userGroup = ScheduleReportFactory.eINSTANCE.createUserGroup();
			userGroup.setComment("New Group");
			table.getUserGroups().add(userGroup);

			cycleGroup.setUserGroup(userGroup);
		}
		return userGroup;
	}

	public static UserGroup createOrReturnUserGroup(final Table table, @NonNull final Row row) {
		final CycleGroup cycleGroup = createOrReturnCycleGroup(table, row);

		UserGroup userGroup = cycleGroup.getUserGroup();
		if (userGroup == null) {
			userGroup = ScheduleReportFactory.eINSTANCE.createUserGroup();
			userGroup.setComment("New Group");
			table.getUserGroups().add(userGroup);

			cycleGroup.setUserGroup(userGroup);
		}
		return userGroup;
	}

	public static void addToOrMergeUserGroup(final Table table, final CycleGroup cycleGroup, final UserGroup userGroup) {
		if (cycleGroup == null || userGroup == null) {
			return;
		}
		if (cycleGroup.getUserGroup() != userGroup) {
			final UserGroup oldGroup = cycleGroup.getUserGroup();
			cycleGroup.setUserGroup(userGroup);
			if (oldGroup != null && oldGroup.getGroups().isEmpty()) {
				table.getUserGroups().remove(oldGroup);
			}
		}

	}

	public static void addToOrMergeUserGroup(final Table table, final Row row, final UserGroup userGroup) {
		final CycleGroup cycleGroup = createOrReturnCycleGroup(table, row);

		if (cycleGroup == null || userGroup == null) {
			return;
		}
		if (cycleGroup.getUserGroup() != userGroup) {
			final UserGroup oldGroup = cycleGroup.getUserGroup();
			cycleGroup.setUserGroup(userGroup);
			if (oldGroup != null) {
				userGroup.getGroups().addAll(oldGroup.getGroups());
				if (oldGroup.getGroups().isEmpty()) {
					table.getUserGroups().remove(oldGroup);
				}
			}
		}

	}

	public static void setChangeType(final CycleGroup cycleGroup, final ChangeType changeType) {
		if (cycleGroup != null && changeType != null) {
			if (changeType.ordinal() > cycleGroup.getChangeType().ordinal()) {
				cycleGroup.setChangeType(changeType);
			}
		}
	}

}
