/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Sets;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;

public class StructuralDifferencesProcessorTest {

	@Test
	public void testDifferences_same() {

		// Create source datamodel
		final EObject referenceElement = EcoreFactory.eINSTANCE.createEObject();
		final EObject equivalentElement1 = EcoreFactory.eINSTANCE.createEObject();
		final EObject equivalentElement2 = EcoreFactory.eINSTANCE.createEObject();

		// Set up equivalences
		final Map<EObject, Set<EObject>> equivalancesMap = new HashMap<EObject, Set<EObject>>();
		equivalancesMap.put(referenceElement, Sets.newHashSet(equivalentElement1, equivalentElement2));

		// Create Table Datamodel
		final Table table = ScheduleReportFactory.eINSTANCE.createTable();
		final Row referenceRow = ScheduleReportFactory.eINSTANCE.createRow();
		final Row equivalentRow1 = ScheduleReportFactory.eINSTANCE.createRow();
		final Row equivalentRow2 = ScheduleReportFactory.eINSTANCE.createRow();

		referenceRow.setVisible(false);
		equivalentRow1.setVisible(false);
		equivalentRow2.setVisible(false);

		final Map<EObject, Row> elementToRowMap = new HashMap<>();
		elementToRowMap.put(referenceElement, referenceRow);
		elementToRowMap.put(equivalentElement1, equivalentRow1);
		elementToRowMap.put(equivalentElement2, equivalentRow2);

		final ScheduleDiffUtils scheduleDiffUtils = Mockito.mock(ScheduleDiffUtils.class);
		Mockito.when(scheduleDiffUtils.isElementDifferent(referenceElement, equivalentElement1)).thenReturn(false);
		Mockito.when(scheduleDiffUtils.isElementDifferent(referenceElement, equivalentElement2)).thenReturn(false);

		final StructuralDifferencesProcessor processor = new StructuralDifferencesProcessor(scheduleDiffUtils);

		processor.runDiffProcess(table, Collections.singletonList(referenceElement), Collections.<EObject> emptyList(), equivalancesMap, elementToRowMap);

		Assert.assertFalse(referenceRow.isVisible());
		Assert.assertFalse(equivalentRow1.isVisible());
		Assert.assertFalse(equivalentRow2.isVisible());
	}

	@Test
	public void testDifferences_different() {

		// Create source datamodel
		final EObject referenceElement = EcoreFactory.eINSTANCE.createEObject();
		final EObject equivalentElement1 = EcoreFactory.eINSTANCE.createEObject();
		final EObject equivalentElement2 = EcoreFactory.eINSTANCE.createEObject();

		// Set up equivalences
		final Map<EObject, Set<EObject>> equivalancesMap = new HashMap<EObject, Set<EObject>>();
		equivalancesMap.put(referenceElement, Sets.newHashSet(equivalentElement1, equivalentElement2));

		// Create Table Datamodel
		final Table table = ScheduleReportFactory.eINSTANCE.createTable();
		final Row referenceRow = ScheduleReportFactory.eINSTANCE.createRow();
		final Row equivalentRow1 = ScheduleReportFactory.eINSTANCE.createRow();
		final Row equivalentRow2 = ScheduleReportFactory.eINSTANCE.createRow();

		referenceRow.setVisible(false);
		equivalentRow1.setVisible(false);
		equivalentRow2.setVisible(false);

		final Map<EObject, Row> elementToRowMap = new HashMap<>();
		elementToRowMap.put(referenceElement, referenceRow);
		elementToRowMap.put(equivalentElement1, equivalentRow1);
		elementToRowMap.put(equivalentElement2, equivalentRow2);

		final ScheduleDiffUtils scheduleDiffUtils = Mockito.mock(ScheduleDiffUtils.class);
		Mockito.when(scheduleDiffUtils.isElementDifferent(referenceElement, equivalentElement1)).thenReturn(false);
		Mockito.when(scheduleDiffUtils.isElementDifferent(referenceElement, equivalentElement2)).thenReturn(true);

		final StructuralDifferencesProcessor processor = new StructuralDifferencesProcessor(scheduleDiffUtils);

		processor.runDiffProcess(table, Collections.singletonList(referenceElement), Collections.<EObject> emptyList(), equivalancesMap, elementToRowMap);

		Assert.assertTrue(referenceRow.isVisible());
		Assert.assertTrue(equivalentRow1.isVisible());
		Assert.assertTrue(equivalentRow2.isVisible());
	}

}
