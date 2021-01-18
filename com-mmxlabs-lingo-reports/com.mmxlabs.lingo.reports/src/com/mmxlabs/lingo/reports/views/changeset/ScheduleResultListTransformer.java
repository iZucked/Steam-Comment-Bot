/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.NamedObject;
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
					ChangeSet changeSet = buildDiffToBaseChangeSet(stages.get(0), prev, current, null, null);
					root.getChangeSets().add(changeSet);
				}
				prev = current;
				monitor.worked(1);

			}
		} finally {
			monitor.done();
		}

		return root;

	}

	public ChangeSet buildSingleChangeChangeSet(final ScenarioResult base, final ScenarioResult current, ChangeDescription changeDescription, UserSettings userSettings) {
		return buildDiffToBaseChangeSet(null, base, current, changeDescription, userSettings, null);
	}

	public ChangeSet buildSingleChangeChangeSet(final ScenarioResult base, final ScenarioResult current, ChangeDescription changeDescription, UserSettings userSettings,
			@Nullable NamedObject targetToSortFirst) {
		return buildDiffToBaseChangeSet(null, base, current, changeDescription, userSettings, targetToSortFirst);
	}

	public ChangeSet buildDiffToBaseChangeSet(final ScenarioResult base, final ScenarioResult prev, final ScenarioResult current, ChangeDescription changeDescription, UserSettings userSettings) {
		return buildDiffToBaseChangeSet(base, prev, current, changeDescription, userSettings, null);
	}

	public ChangeSet buildDiffToBaseChangeSet(final ScenarioResult base, final ScenarioResult prev, final ScenarioResult current, ChangeDescription changeDescription, UserSettings userSettings,
			@Nullable NamedObject targetToSortFirst) {

		final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();
		changeSet.setChangeDescription(changeDescription);
		changeSet.setBaseScenario(prev);
		changeSet.setCurrentScenario(current);
		generateDifferences(prev, current, changeSet, false, targetToSortFirst);

		changeSet.setAltBaseScenario(base);
		changeSet.setAltCurrentScenario(current);
		generateDifferences(base, current, changeSet, true, targetToSortFirst);

		return changeSet;
	}

	public ChangeSet buildParallelDiffChangeSet(final ScenarioResult base, final ScenarioResult current, final ScenarioResult altBase, final ScenarioResult altCurrent,
			ChangeDescription changeDescription, UserSettings userSettings, @Nullable NamedObject targetToSortFirst) {

		final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();
		changeSet.setBaseScenario(base);
		changeSet.setCurrentScenario(current);
		changeSet.setChangeDescription(changeDescription);
		changeSet.setUserSettings(userSettings);

		generateDifferences(base, current, changeSet, false, targetToSortFirst);
		if (altBase != null && altCurrent != null) {
			changeSet.setAltCurrentScenario(altCurrent);
			changeSet.setAltBaseScenario(altBase);
			generateDifferences(altBase, altCurrent, changeSet, true, targetToSortFirst);
		}

		return changeSet;
	}

	private void generateDifferences(final ScenarioResult from, final ScenarioResult to, final ChangeSet changeSet, final boolean isAlternative, @Nullable NamedObject targetToSortFirst) {

		if (from == null || to == null) {
			return;
		}

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
		// Other PNL segment
		{
			ChangeSetRow row = ChangesetFactory.eINSTANCE.createChangeSetRow();
			ChangeSetRowDataGroup afterGroup = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
			ChangeSetRowDataGroup beforeGroup = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
			ChangeSetRowData beforeData = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			ChangeSetRowData afterData = ChangesetFactory.eINSTANCE.createChangeSetRowData();

			beforeData.setPrimaryRecord(true);
			afterData.setPrimaryRecord(true);

			beforeData.setLhsGroupProfitAndLoss(beforeSchedule.getOtherPNL());
			afterData.setLhsGroupProfitAndLoss(afterSchedule.getOtherPNL());
			
			beforeGroup.getMembers().add(beforeData);
			afterGroup.getMembers().add(afterData);
			
			row.setBeforeData(beforeGroup);
			row.setAfterData(afterGroup);
			
			beforeData.setLhsName("Knock-on P&L");
			afterData.setLhsName("Knock-on P&L");
			
			rows.add(row);
		}

		// //

		// Add to data model
		if (isAlternative) {
			changeSet.getChangeSetRowsToAlternativeBase().addAll(rows);
		} else {
			changeSet.getChangeSetRowsToDefaultBase().addAll(rows);
		}

		ChangeSetTransformerUtil.setRowFlags(rows);
		// // ChangeSetTransformerUtil.filterRows(rows);
		// // ChangeSetTransformerUtil.sortRows(rows);
		// Build metrics
		ChangeSetTransformerUtil.calculateMetrics(changeSet, beforeSchedule, afterSchedule, isAlternative);
	}

}