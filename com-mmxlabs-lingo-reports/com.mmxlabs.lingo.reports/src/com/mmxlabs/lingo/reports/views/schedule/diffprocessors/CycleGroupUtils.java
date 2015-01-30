package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;

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
			row.setCycleGroup(cycleGroup);
		}
		return cycleGroup;
	}

	public static void addToOrMergeCycleGroup(final Table table, final Row row, final CycleGroup cycleGroup) {
		if (row == null) {
			return;
		}
		final CycleGroup rowCycleGroup = row.getCycleGroup();
		if (rowCycleGroup == null) {
			row.setCycleGroup(cycleGroup);
		} else if (rowCycleGroup != cycleGroup) {
			row.setCycleGroup(cycleGroup);
			cycleGroup.getRows().addAll(rowCycleGroup.getRows());
			table.getCycleGroups().remove(rowCycleGroup);
		}
	}

}
