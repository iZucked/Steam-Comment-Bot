/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.mmxcore.NamedObject;

public class ChangeSetToTableTransformer {

	public ChangeSetTableRoot createViewDataModel(final ChangeSetRoot changeSetRoot, final boolean isDiffToBase, NamedObject targetToSortFirst) {

		final ChangeSetTableRoot changeSetTableRoot = ChangesetFactory.eINSTANCE.createChangeSetTableRoot();

		for (final ChangeSet changeSet : changeSetRoot.getChangeSets()) {

			List<ChangeSetRow> changeSetRows;
			if (isDiffToBase) {
				changeSetRows = changeSet.getChangeSetRowsToBase();
			} else {
				changeSetRows = changeSet.getChangeSetRowsToPrevious();
			}

			final List<ChangeSetTableRow> changeSetTableGroupRows = convertRows(changeSetRows);
			ChangeSetTransformerUtil.filterRows(changeSetTableGroupRows);

			if (!changeSetTableGroupRows.isEmpty()) {
				final ChangeSetTableGroup changeSetTableGroup = ChangesetFactory.eINSTANCE.createChangeSetTableGroup();
				changeSetTableGroup.setDescription(changeSet.getDescription());
				ChangeSetTransformerUtil.sortRows(changeSetTableGroupRows, targetToSortFirst);
				changeSetTableGroup.getRows().addAll(changeSetTableGroupRows);
				changeSetTableRoot.getGroups().add(changeSetTableGroup);
				changeSetTableGroup.setDeltaMetrics(EcoreUtil.copy(isDiffToBase ? changeSet.getMetricsToBase() : changeSet.getMetricsToPrevious()));
				changeSetTableGroup.setCurrentMetrics(EcoreUtil.copy(changeSet.getCurrentMetrics()));

				changeSetTableGroup.setChangeSet(changeSet);

			}

		}

		return changeSetTableRoot;
	}

	public List<ChangeSetTableRow> convertRows(final List<ChangeSetRow> changeSetRows) {
		final List<ChangeSetTableRow> changeSetTableGroupRows = new LinkedList<>();
		final List<Runnable> callbacks = new LinkedList<>();
		final Map<ChangeSetRowData, ChangeSetTableRow> mapping = new HashMap<>();

		for (final ChangeSetRow changeSetRow : changeSetRows) {
			// Max number of rows. Until LDD turned on expect 0 or 1.
			final int beforeSize = changeSetRow.getBeforeData() == null ? 0 : changeSetRow.getBeforeData().getMembers().size();
			final int afterSize = changeSetRow.getAfterData() == null ? 0 : changeSetRow.getAfterData().getMembers().size();

			// Here we want to filter open before discharges. Note the "open discharge" may not exist in the before hence we check the RHSName.
			// We filter out these rows as P&L will be included in the linked after row.
			if (beforeSize == 1 && afterSize == 0) {
				ChangeSetRowData data = changeSetRow.getBeforeData().getMembers().get(0);
				if (data.getRhsName() != null && !data.getRhsName().isEmpty()) {
					if (data.getLhsName() == null || data.getLhsName().isEmpty()) {
						continue;
					}
				}
			}

			final int size = Math.max(beforeSize, afterSize);
			boolean primaryIsWiring = false;
			for (int i = 0; i < size; ++i) {
				// TODO: Lots of null checks
				final ChangeSetRowData before = (i < beforeSize) ? changeSetRow.getBeforeData().getMembers().get(i) : null;
				final ChangeSetRowData after = (i < afterSize) ? changeSetRow.getAfterData().getMembers().get(i) : null;

				final ChangeSetTableRow changeSetTableRow = ChangesetFactory.eINSTANCE.createChangeSetTableRow();

				// We have this split to help with the diffing in the report - may need to play with it a bit. E.g. RHS after may need to be getLink().getRhsAfter()
				changeSetTableRow.setLhsAfter(after);
				changeSetTableRow.setLhsBefore(before);

				if (after != null) {
					changeSetTableRow.setRhsAfter(after);
					changeSetTableRow.setRhsBefore(after.getRhsLink());
				}
				// TODO: Some logic needed to handle before spot positions
				if (after != null && ChangeSetTransformerUtil.isSet(after.getLhsName())) {
					changeSetTableRow.setLhsName(after.getLhsName());
					if (after.getLoadSlot() != null) {
						changeSetTableRow.setLhsSlot(true);
						changeSetTableRow.setLhsOptional(after.getLoadSlot().isOptional());
						changeSetTableRow.setLhsSpot(after.getLoadSlot() instanceof SpotSlot);
						changeSetTableRow.setLhsNonShipped(after.getLoadSlot().isDESPurchase());

						changeSetTableRow.setLhsValid(after.getLoadAllocation() != null || after.getLoadSlot().isOptional());
					} else if (before != null && before.getLoadSlot() != null) {
						changeSetTableRow.setLhsSlot(true);
						changeSetTableRow.setLhsOptional(before.getLoadSlot().isOptional());
						changeSetTableRow.setLhsSpot(before.getLoadSlot() instanceof SpotSlot);
						changeSetTableRow.setLhsNonShipped(before.getLoadSlot().isDESPurchase());
					}
				} else if (after == null && before != null && ChangeSetTransformerUtil.isSet(before.getLhsName())) {
					changeSetTableRow.setLhsName(before.getLhsName());
					if (before.getLoadSlot() != null) {
						changeSetTableRow.setLhsSlot(true);
						changeSetTableRow.setLhsOptional(before.getLoadSlot().isOptional());
						changeSetTableRow.setLhsSpot(before.getLoadSlot() instanceof SpotSlot);
						changeSetTableRow.setLhsNonShipped(before.getLoadSlot().isDESPurchase());
					}
				}
				if (after != null && ChangeSetTransformerUtil.isSet(after.getRhsName())) {
					if (after.getRhsName().contains("FOB_SPL")) {
						int ii = 0;
					}
					changeSetTableRow.setRhsName(after.getRhsName());
					if (after.getDischargeSlot() != null) {
						changeSetTableRow.setRhsSlot(true);
						changeSetTableRow.setRhsOptional(after.getDischargeSlot().isOptional());
						changeSetTableRow.setRhsSpot(after.getDischargeSlot() instanceof SpotSlot);
						changeSetTableRow.setRhsNonShipped(after.getDischargeSlot().isFOBSale());
						changeSetTableRow.setRhsValid(after.getDischargeAllocation() != null || after.getDischargeSlot().isOptional());
					} else if (after.getRhsLink() != null && after.getRhsLink().getDischargeSlot() != null) {
						changeSetTableRow.setRhsSlot(true);
						changeSetTableRow.setRhsOptional(after.getRhsLink().getDischargeSlot().isOptional());
						changeSetTableRow.setRhsSpot(after.getRhsLink().getDischargeSlot() instanceof SpotSlot);
						changeSetTableRow.setRhsNonShipped(after.getRhsLink().getDischargeSlot().isFOBSale());
					}
				} else if (after == null && before != null && ChangeSetTransformerUtil.isSet(before.getRhsName())) {
					changeSetTableRow.setRhsName(before.getRhsName());
					if (before.getDischargeSlot() != null) {
						changeSetTableRow.setRhsSlot(true);
						changeSetTableRow.setRhsOptional(before.getDischargeSlot().isOptional());
						changeSetTableRow.setRhsSpot(before.getDischargeSlot() instanceof SpotSlot);
						changeSetTableRow.setRhsNonShipped(before.getDischargeSlot().isFOBSale());
					}
				}

				if (i == 0) {
					// Note complex cargo optimisation may invalidate this assumption
					assert before == null || before.isPrimaryRecord();
					assert after == null || after.isPrimaryRecord();

					changeSetTableRow.setVesselChange(changeSetRow.isVesselChange());
					changeSetTableRow.setWiringChange(changeSetRow.isWiringChange());
					primaryIsWiring = changeSetRow.isWiringChange();
					// Maybe useful filters?
					// changeSetTableRow.setPNLChange(changeSetRow.isPNLChange());
					// changeSetTableRow.setLatenessChange(changeSetRow.isLatenessChange());
					// changeSetTableRow.setCapacityChange(changeSetRow.isCapacityChange());

					if (after != null) {
						changeSetTableRow.setAfterVesselName(after.getVesselName());
						changeSetTableRow.setAfterVesselShortName(after.getVesselShortName());
					}
					if (before != null) {
						changeSetTableRow.setBeforeVesselName(before.getVesselName());
						changeSetTableRow.setBeforeVesselShortName(before.getVesselShortName());
					}
				} else {
					changeSetTableRow.setWiringChange(primaryIsWiring);
				}

				if (changeSetTableRow.isWiringChange() && before != null) {
					final ChangeSetRowData rhsLink = before.getRhsLink();
					if (before != null) {
						callbacks.add(() -> {
							final ChangeSetTableRow prev = mapping.get(before);
							if (prev != null) {
								changeSetTableRow.setPreviousRHS(prev);
							}
						});
					}
				}
				if (after != null && after.getRhsLink() != null) {
					mapping.put(after.getRhsLink(), changeSetTableRow);
				}

				changeSetTableGroupRows.add(changeSetTableRow);
			}
		}
		callbacks.forEach(Runnable::run);
		return changeSetTableGroupRows;
	}
}