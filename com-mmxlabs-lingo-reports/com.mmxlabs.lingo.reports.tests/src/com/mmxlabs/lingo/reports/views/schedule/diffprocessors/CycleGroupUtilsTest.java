/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;

public class CycleGroupUtilsTest {

	@Test
	public void createOrReturnTest_create() {
		final Table table = ScheduleReportFactory.eINSTANCE.createTable();
		final Row row = ScheduleReportFactory.eINSTANCE.createRow();

		table.getRows().add(row);

		final CycleGroup returnedCycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, row);

		Assert.assertNotNull(returnedCycleGroup);
		Assert.assertSame(returnedCycleGroup, row.getCycleGroup());
		Assert.assertTrue(table.getCycleGroups().contains(returnedCycleGroup));
	}

	@Test
	public void createOrReturnTest_return() {
		final Table table = ScheduleReportFactory.eINSTANCE.createTable();
		final Row row = ScheduleReportFactory.eINSTANCE.createRow();
		final CycleGroup cycleGroup = ScheduleReportFactory.eINSTANCE.createCycleGroup();

		table.getRows().add(row);
		table.getCycleGroups().add(cycleGroup);
		row.setCycleGroup(cycleGroup);

		final CycleGroup returnedCycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, row);

		Assert.assertNotNull(returnedCycleGroup);
		Assert.assertSame(cycleGroup, returnedCycleGroup);
		Assert.assertSame(returnedCycleGroup, row.getCycleGroup());
		Assert.assertTrue(table.getCycleGroups().contains(returnedCycleGroup));
	}

	@Test
	public void addToOrMergeCycleGroup_add() {
		final Table table = ScheduleReportFactory.eINSTANCE.createTable();
		final Row row1 = ScheduleReportFactory.eINSTANCE.createRow();
		final Row row2 = ScheduleReportFactory.eINSTANCE.createRow();
		final CycleGroup cycleGroup = ScheduleReportFactory.eINSTANCE.createCycleGroup();

		table.getRows().add(row1);
		table.getRows().add(row2);
		table.getCycleGroups().add(cycleGroup);

		cycleGroup.getRows().add(row1);

		CycleGroupUtils.addToOrMergeCycleGroup(table, row2, cycleGroup);

		Assert.assertSame(cycleGroup, row1.getCycleGroup());
		Assert.assertSame(cycleGroup, row2.getCycleGroup());
		Assert.assertTrue(cycleGroup.getRows().contains(row1));
		Assert.assertTrue(cycleGroup.getRows().contains(row2));
		Assert.assertTrue(table.getCycleGroups().contains(cycleGroup));
	}

	@Test
	public void addToOrMergeCycleGroup_merge() {
		final Table table = ScheduleReportFactory.eINSTANCE.createTable();
		final Row row1 = ScheduleReportFactory.eINSTANCE.createRow();
		final Row row2 = ScheduleReportFactory.eINSTANCE.createRow();
		final CycleGroup cycleGroup1 = ScheduleReportFactory.eINSTANCE.createCycleGroup();
		final CycleGroup cycleGroup2 = ScheduleReportFactory.eINSTANCE.createCycleGroup();

		table.getRows().add(row1);
		table.getRows().add(row2);
		table.getCycleGroups().add(cycleGroup1);
		table.getCycleGroups().add(cycleGroup2);

		cycleGroup1.getRows().add(row1);
		cycleGroup2.getRows().add(row2);

		CycleGroupUtils.addToOrMergeCycleGroup(table, row2, cycleGroup1);

		Assert.assertSame(cycleGroup1, row1.getCycleGroup());
		Assert.assertSame(cycleGroup1, row2.getCycleGroup());
		Assert.assertTrue(cycleGroup1.getRows().contains(row1));
		Assert.assertTrue(cycleGroup1.getRows().contains(row2));
		Assert.assertTrue(table.getCycleGroups().contains(cycleGroup1));
		Assert.assertFalse(table.getCycleGroups().contains(cycleGroup2));
	}

}
