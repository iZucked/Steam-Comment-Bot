/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetTransformerUtil.MappingModel;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ScheduleResultListTransformer {

	// TODO: Split into diff to base and diff to previous
	public ChangeSetRoot createDataModel(final List<ScenarioResult> stages, final IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		try {
			monitor.beginTask("Opening action sets", stages.size());
			ScenarioResult prev = null;
			for (final ScenarioResult current : stages) {
				if (prev != null) {
					root.getChangeSets().add(buildChangeSet(stages.get(0), prev, current));
				}
				prev = current;
				monitor.worked(1);

			}
		} finally {
			monitor.done();
		}

		return root;

	}

	public ChangeSet buildChangeSet(final ScenarioResult base, final ScenarioResult prev, final ScenarioResult current) {
		return buildChangeSet(base, prev, current, null);
	}

	public ChangeSet buildChangeSet(final ScenarioResult base, final ScenarioResult prev, final ScenarioResult current, @Nullable NamedObject targetToSortFirst) {
		final ModelReference baseReference = base.getScenarioInstance().getReference("ScheduleResultListTransformer:1");
		final ModelReference prevReference = prev.getScenarioInstance().getReference("ScheduleResultListTransformer:2");
		final ModelReference currentReference = current.getScenarioInstance().getReference("ScheduleResultListTransformer:3");

		// Pre-Load
		baseReference.getInstance();
		prevReference.getInstance();
		currentReference.getInstance();

		final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();
		changeSet.setBaseScenario(base);
		changeSet.setBaseScenarioRef(baseReference);
		changeSet.setPrevScenario(prev);
		changeSet.setPrevScenarioRef(prevReference);
		changeSet.setCurrentScenario(current);
		changeSet.setCurrentScenarioRef(currentReference);

		generateDifferences(base, current, changeSet, true, targetToSortFirst);
		generateDifferences(prev, current, changeSet, false, targetToSortFirst);

		return changeSet;
	}

	private void generateDifferences(final ScenarioResult from, final ScenarioResult to, final ChangeSet changeSet, final boolean isBase, @Nullable NamedObject targetToSortFirst) {

		final ScheduleModel beforeScheduleModel = from.getTypedResult(ScheduleModel.class);
		final ScheduleModel afterScheduleModel = to.getTypedResult(ScheduleModel.class);
		if (beforeScheduleModel == null || afterScheduleModel == null) {
			return;
		}

		final Schedule beforeSchedule = beforeScheduleModel.getSchedule();
		final Schedule afterSchedule = afterScheduleModel.getSchedule();
		if (beforeSchedule == null || afterSchedule == null) {
			return;
		}

		// Generate the row data
		List<EObject> beforeTargets = ChangeSetTransformerUtil.extractTargets(beforeSchedule);
		List<EObject> afterTargets = ChangeSetTransformerUtil.extractTargets(afterSchedule);
		final MappingModel beforeDifferences = ChangeSetTransformerUtil.generateMappingModel(beforeTargets);
		final MappingModel afterDifferences = ChangeSetTransformerUtil.generateMappingModel(afterTargets);

		final List<ChangeSetRow> rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeDifferences, afterDifferences);
		// //

		// Add to data model
		if (isBase) {
			changeSet.getChangeSetRowsToBase().addAll(rows);
		} else {
			changeSet.getChangeSetRowsToPrevious().addAll(rows);
		}

		ChangeSetTransformerUtil.setRowFlags(rows);
		// // ChangeSetTransformerUtil.filterRows(rows);
		// // ChangeSetTransformerUtil.sortRows(rows);
		// Build metrics
		ChangeSetTransformerUtil.calculateMetrics(changeSet, beforeSchedule, afterSchedule, isBase);
	}

}