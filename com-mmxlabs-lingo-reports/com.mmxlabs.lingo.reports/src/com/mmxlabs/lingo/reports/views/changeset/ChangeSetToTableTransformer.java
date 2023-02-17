/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.ScenarioResult;

public class ChangeSetToTableTransformer {

	public enum SortMode {
		BY_GROUP, BY_PNL, BY_PNL_PER_CHANGE
	}

	public void bindModels(ChangeSetTableRoot base, ChangeSetTableRoot alt) {
		if (base == null || alt == null) {
			return;
		}

		int count = Math.min(base.getGroups().size(), alt.getGroups().size());
		for (int i = 0; i < count; ++i) {
			ChangeSetTableGroup a = base.getGroups().get(i);
			ChangeSetTableGroup b = alt.getGroups().get(i);
			a.setLinkedGroup(b);
			b.setLinkedGroup(a);
		}
	}

	public ChangeSetTableRoot createViewDataModel(final ChangeSetRoot changeSetRoot, final boolean isAlternative,SortMode sortMode) {

		final ChangeSetTableRoot changeSetTableRoot = ChangesetFactory.eINSTANCE.createChangeSetTableRoot();
		int groupIdx = 0;
		for (final ChangeSet changeSet : changeSetRoot.getChangeSets()) {

			List<ChangeSetTableRow> changeSetRows;
			ScenarioResult baseResult;
			ScenarioResult currentResult;
			if (isAlternative) {
				changeSetRows = changeSet.getChangeSetRowsToAlternativeBase();
				baseResult = changeSet.getAltBaseScenario();
				currentResult = changeSet.getAltCurrentScenario();
			} else {
				changeSetRows = changeSet.getChangeSetRowsToDefaultBase();
				baseResult = changeSet.getBaseScenario();
				currentResult = changeSet.getCurrentScenario();
			}

			if (baseResult == null && currentResult == null) {
				return null;
			}

			final List<ChangeSetTableRow> changeSetTableGroupRows = new LinkedList<>(changeSetRows);
			ChangeSetTransformerUtil.filterRows(changeSetTableGroupRows);

			
			
			// -------------
			if (!changeSetTableGroupRows.isEmpty()) {
				final ChangeSetTableGroup changeSetTableGroup = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
				++groupIdx;
				changeSetTableGroup.setDescription(changeSet.getDescription());
				changeSetTableGroup.getRows().addAll(changeSetTableGroupRows);
				changeSetTableRoot.getGroups().add(changeSetTableGroup);
				changeSetTableGroup.setDeltaMetrics(EMFCopier.copy(isAlternative ? changeSet.getMetricsToAlternativeBase() : changeSet.getMetricsToDefaultBase()));
				changeSetTableGroup.setCurrentMetrics(EMFCopier.copy(changeSet.getCurrentMetrics()));

				changeSetTableGroup.setChangeSet(changeSet);

				changeSetTableGroup.setBaseScenario(baseResult);
				changeSetTableGroup.setCurrentScenario(currentResult);
			// -------------	Below is an alternative version. When showing dual result mode, we need to keep dual data mode in sync. 		
//			final ChangeSetTableGroup changeSetTableGroup = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
//			// Note: We always add to the root otherwise when binding DUAL data models we can get out of sync if the
//			// changeSetTableGroupRows.isEmpty() is different. However this can lead to empty solutions being displayed.
//			// This was changed as part of the Sandbox V4 work ~ SG, June 2019
//			changeSetTableRoot.getGroups().add(changeSetTableGroup);
//			++groupIdx;
//			changeSetTableGroup.setChangeSet(changeSet);
//
//			changeSetTableGroup.setBaseScenario(baseResult);
//			changeSetTableGroup.setCurrentScenario(currentResult);
//			changeSetTableGroup.setDescription(changeSet.getDescription());
//			changeSetTableGroup.setDeltaMetrics(EcoreUtil.copy(isAlternative ? changeSet.getMetricsToAlternativeBase() : changeSet.getMetricsToDefaultBase()));
//			changeSetTableGroup.setCurrentMetrics(EcoreUtil.copy(changeSet.getCurrentMetrics()));
//			if (!changeSetTableGroupRows.isEmpty()) {
//				ChangeSetTransformerUtil.sortRows(changeSetTableGroupRows, targetToSortFirst);
//				changeSetTableGroup.getRows().addAll(changeSetTableGroupRows);
/// -------------
				int majorChanges = 0;
				for (final var row : changeSetTableGroupRows) {
					 if (row.isMajorChange()) {
						++majorChanges;
					}
				}
				changeSetTableGroup.setComplexity(majorChanges);
				// TODO: For B.E. grab the B/E. price
				if (sortMode == SortMode.BY_PNL) {
					// Lower is better for default sort
					changeSetTableGroup.setSortValue(-changeSetTableGroup.getDeltaMetrics().getPnlDelta());
				} else if (sortMode == SortMode.BY_PNL_PER_CHANGE) {
					changeSetTableGroup.setSortValue((double) -changeSetTableGroup.getDeltaMetrics().getPnlDelta() / (double) (majorChanges == 0 ? 1 : majorChanges));
				} else if (sortMode == SortMode.BY_GROUP) {
					changeSetTableGroup.setSortValue(groupIdx);
				} else {
					throw new IllegalArgumentException();
				}
			}
		}

		return changeSetTableRoot;
	}
}